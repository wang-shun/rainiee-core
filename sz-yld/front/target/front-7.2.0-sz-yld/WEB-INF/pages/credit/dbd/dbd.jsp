<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.service.SafetymsgViewManage"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.S51.enums.T5123_F03" %>
<%@page import="com.dimeng.p2p.S61.enums.T6120_F05" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F10" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F17" %>
<%@page import="com.dimeng.p2p.account.front.service.entity.UserInfo" %>
<%@page import="com.dimeng.p2p.common.enums.CreditType" %>
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.front.servlets.Region" %>
<%@page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.dbd.Dbd" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.dbd.Index" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.GrManage" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Rzxx" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidWillManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6119" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.front.servlets.credit.dbd.CheckGuaranteeCodeExists" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%
    boolean advice_complain_switch = BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.GUARANTEE_DBD_SWITCH));
    if (!advice_complain_switch) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    if (dimengSession == null || !dimengSession.isAuthenticated()) {
        String errorMessage = "您还未登录，请先登录系统，再进行担保贷申请！";
        String info = errorMessage +"<span class='display:none'><a id='to_validate' href=\""+"/user/login.html?_z=/credit/dbd/dbd.html"+"\" class=\"blue\"></a></span>";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response,controller.getViewURI(request, Index.class));
        return;
    }
    else
    {
        UserInfoManage userInfoManage=serviceSession.getService(UserInfoManage.class);
        T6110 t6110=userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
        if(t6110.F07==T6110_F07.SD)
        {
		    controller.redirectLogin(request, response, controller.getURI(request, Logout.class));
		    return;
        }
    }
%>


<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    int id = 0;
    UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
    UserManage manage = serviceSession.getService(UserManage.class);
    String usrCustId = manage.getUsrCustId();
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    if(tg && StringHelper.isEmpty(usrCustId)){//托管模式未第三方开户则统一跳转到开户引导页
        String openEscrowGuide = configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE);
        String errorMessage = "申请担保贷必须先在第三方注册托管账户，";
        //开户引导页
        String info = errorMessage +"<a id='to_validate' href=\""+openEscrowGuide+"\" class=\"blue\">立即注册</a>";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
    if (!mge.isSmrz() || !mge.getYhrzxx()) {
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        String errorMessage = "申请担保贷必须先进行真实身份认证、手机号码认证";
        if(isOpenWithPsd)
        {
    		errorMessage = "申请担保贷必须先进行真实身份认证、手机号码认证，交易密码设置";
        }
        //跳转到实名认证页面
        String info = errorMessage +"，请您到<a id='to_validate' href=\""+configureProvider.format(safeManage.getSafetymsgView())+"\" class=\"blue\">个人基础信息</a>设置。";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    boolean isMustEmail = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL));
    if(isMustEmail && !mge.isEmail())
    {
        //开启必须邮箱认证时，用户未验证的，提示其到个人基础信息认证
        String info = "申请担保贷必须认证邮箱，请您到<a id='to_validate' href=\""+configureProvider.format(safeManage.getSafetymsgView())+"\" class=\"blue\">个人基础信息</a>设置。";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    // 认证项是否通过
    GrManage personalManage = serviceSession.getService(GrManage.class);
    Rzxx[] t6120s = personalManage.getRzxx(serviceSession.getSession().getAccountId());
    String rzMsg = "";
    if (t6120s != null && t6120s.length > 0) {
        for (Rzxx rzxx : t6120s) {
    if (T5123_F03.S.name().equals(rzxx.mustRz.name()) && !T6120_F05.TG.equals(rzxx.status)) {
        rzMsg = rzMsg + rzxx.lxmc + ",";
    }
        }
    }

    //只有自然人需要认证项
    if (!StringHelper.isEmpty(rzMsg)) {
        // 跳转到认证信息页面
        String info = "认证信息中的必要认证没有通过，前往<br/><a id='to_validate' href=\""+configureProvider.format(URLVariable.USER_RZXX)+"\" class=\"blue\">认证信息</a>进行认证。";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response,  controller.getViewURI(request, Index.class));
        return;
    }

    if (mge.isYuqi().equals("Y")) {
        //当前用户存在逾期不能借款
        controller.prompt(request, response, PromptLevel.ERROR, "您存在逾期借款，请先进行<a href='/user/credit/repaying.html' class=\"blue\">还款</a>");
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    if (mge.isBid()) {
        //当前用户存在待审核的标不能借款
        controller.prompt(request, response, PromptLevel.ERROR, "您已申请过其他产品，不能再申请此产品");
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    int accountId = serviceSession.getSession().getAccountId();
    UserInfo userInfo = mge.search(accountId);
    int minAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.GUARANTEE_LOAN_AGE_MIN));
    int maxAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.GUARANTEE_LOAN_AGE_MAX));
    if (userInfo.age < minAge || userInfo.age > maxAge) {
        //当前用户年龄不符合规范
        controller.prompt(request, response, PromptLevel.ERROR, "您的年龄不在申请条件范围之内，不能申请。");
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }

    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);

    if((tg) && StringHelper.isEmpty(usrCustId)){
        //你还没在第三方注册账号
        String info = "您还没在第三方托管注册账号，请<a id='to_validate' href=\""+configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE)+"\" class=\"red\">立即注册</a>";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    if(tg && "shuangqian".equals(escrow))
    {
        T6119 t6119 = manage.getUsrAute();//获取用户授权情况
        if(t6119 == null || "0".equals(t6119.F04)){
    String info = "该用户尚未授权还款转账与二次分配转账，不能发标！<a id='to_validate' style=\"color:red\" href=\""+configureProvider.format(URLVariable.AUTHORIZE_URL)+"\">点击授权</a>";
    controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
    return;
        }
    }
    boolean isNetSigned = mge.isNetSigned();
    if (!isNetSigned) {
        controller.prompt(request, response, PromptLevel.ERROR, "申请担保贷必须先网签合同认证，<br/>请您到<a id='to_validate' href=\""+configureProvider.format(URLVariable.USER_NETSIGN_URL)+"\" class=\"blue\">网签合同</a>设置。");
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    T6211[] bidTypes = serviceSession.getService(BidWillManage.class).getBidTypeAll();
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form action="<%=controller.getURI(request, Dbd.class)%>" class="form1" method="post" onsubmit="return onSubmit()">
    <div class="main_bg">
        <input type="hidden" name="ltype"
               value="<%=CreditType.DBD.name()%>">
        <input id="shengId" value="<%=request.getParameter("sheng")%>" type="hidden"/>
        <input id="shiId" value="<%=request.getParameter("shi")%>" type="hidden"/>
        <input id="xianId" value="<%=request.getParameter("xian")%>" type="hidden"/>

        <div class="creditloan_bg">
            <div class="creditloan_lc"></div>
        </div>

        <div class="main_mod">
            <div class="main_hd"><i class="icon"></i><span class="gray3 f18">填写借款申请</span></div>
            <div class="main_form mt50 ">
                <ul>
                    <%-- <%
                        String ermsg = controller.getPrompt(request, response,PromptLevel.WARRING);if(!StringHelper.isEmpty(ermsg)){
                    %>
                     <li style="color: red;border: 1px solid red;padding: 10px;text-align: center;"><%
                         StringHelper.filterHTML(out, ermsg);
                     %></li>
                     <%
                         }
                     %> --%>
                    <li class="item">
                        <div class="til"><span class="red">*</span>担保人编号：</div>
                        <div class="con">
                            <input name="gCode" type="text" id="gCode"
                                   class="text"
                                   value="<%StringHelper.filterHTML(out,request.getParameter("gCode"));%>"/>

                            <p tip class="prompt"><span class="gray9">请填写担保人提供的担保人编号</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>

                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>借款标题：</div>
                        <div class="con">
                            <input name="F03" type="text"
                                   class="text required max-length-14"
                                   value="<%StringHelper.filterHTML(out,request.getParameter("F03"));%>"/>

                            <p tip class="prompt"><span class="gray9">不超过14个字</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>

                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>所在城市：</div>
                        <div class="con">
                            <select name="sheng" id="sheng"  class="select6 required">
                            </select>

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>借款金额：</div>
                        <div class="con">
                            <input name="F05" type="text"
                                   value="<%=StringHelper.isEmpty(request.getParameter("F05"))? "": request.getParameter("F05")%>"
                                   class="text required isint mulriple-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT)%> min-size-<%configureProvider.format(out,SystemVariable.GUARANTEE_LOAN_AMOUNT_MIN);%> max-size-<%=configureProvider.format(SystemVariable.GUARANTEE_LOAN_AMOUNT_MAX)%> jejs"
                                    />元
                            <p tip class="prompt"><span
                                    class="gray9">借款金额范围<%configureProvider.format(out,SystemVariable.GUARANTEE_LOAN_AMOUNT_MIN);%>-<%configureProvider.format(out,SystemVariable.GUARANTEE_LOAN_AMOUNT_MAX);%>，且为<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT)%>的倍数</span>
                            </p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>还款方式：</div>
                        <div class="con">
                            <select name="F10" class="select2" id="hkfs">
                                <%
                                    boolean isDEBX = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.GUARANTEE_IS_SHOW_DEBX));
                                    boolean isMYFX = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.GUARANTEE_IS_SHOW_MYFX));
                                    boolean isYCFQ = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.GUARANTEE_IS_SHOW_YCFQ));
                                    boolean isDEBJ = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.GUARANTEE_IS_SHOW_DEBJ));
                                    for (T6230_F10 t : T6230_F10.values()) { %>
                                <%
                                    if(!isDEBX && t == T6230_F10.DEBX) continue;
                                    if(!isMYFX && t == T6230_F10.MYFX) continue;
                                    if(!isYCFQ && t == T6230_F10.YCFQ) continue;
                                    if(!isDEBJ && t == T6230_F10.DEBJ) continue;
                                %>
                                <option value="<%StringHelper.filterHTML(out, t.name()); %>" <%if (t.name().equals(request.getParameter("F10"))) { %>
                                        selected="selected"<%} %>><%
                                    StringHelper.filterHTML(out, t.getChineseName()); %></option>
                                <%} %>
                            </select>
								<span id="_accDay" style="display:none;">
									<input id="accDay_S" type="radio" name="accDay" value="S"
                                           <%if("S".equals(request.getParameter("accDay"))){ %>checked="checked"<%} %> />按天
									<input id="accDay_F" type="radio" name="accDay" value="F"
                                           <%if("F".equals(request.getParameter("accDay")) || StringHelper.isEmpty(request.getParameter("accDay"))){ %>checked="checked"<%} %> />按月
								 </span>

                            <p tip class="prompt"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>借款期限：</div>
                        <div class="con">
                            <input id="jkqx" type="text"
                                   value="<%StringHelper.filterHTML(out, request.getParameter("F09")); %>"
                                   class="text   required min-size-<%configureProvider.format(out,SystemVariable.GUARANTEE_LOAN_QX_MIN);%> max-size-<%=configureProvider.format(SystemVariable.GUARANTEE_LOAN_QX_MAX)%>" mtest="/^\d+$/" mtestmsg="必须为整数" name="F09"/>
                            月/天
                            <p tip class="prompt"><span class="gray9">除本息到期一次付清可选按天计算外，其他皆以月为单位</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>筹款期限：</div>
                        <div class="con">
                            <select name="F08" class="select2 jejs" id="F08">
                                <%
                                    for (int i = 1; i <= 31; i++) {
                                %>
                                <option value="<%=i %>"
                                        <%if(!StringHelper.isEmpty(request.getParameter("F08"))){if(i==Integer.parseInt(request.getParameter("F08"))){ %>selected="selected"<%
                                        }
                                    }
                                %>><%=i %>天
                                </option>
                                <%} %>
                            </select>

                           <p tip class="prompt"></p>
                        </div>
                    </li>
                    <%
                        String rateMin = Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.GUARANTEE_LOAN_RATE_MIN)));
                        String rateMax = Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.GUARANTEE_LOAN_RATE_MAX)));
                    %>
                    <li class="item">
                        <div class="til"><span class="red">*</span>年化利率：</div>
                        <div class="con">
                            <input name="F06" type="text"
                                   class="text required minf-size-<%=rateMin%> maxf-size-<%=rateMax%> jejs"
                                   value="<%=StringHelper.isEmpty(request.getParameter("F06"))? "": request.getParameter("F06") %>"
                                   mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="两位小数以内"/>%
                            <p tip class="prompt"><span class="gray9">利率精确到小数点后两位，范围<%=rateMin%>-<%=rateMax%>之间<br/>借款最低利率由您的借款期限确定，一般来说借款利率越高，筹款速度越快。</span>
                            </p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til">
                            <span class="red">*</span>付息方式：
                        </div>
                        <div class="con">
                            <%
                                T6230_F17 fxfs = EnumParser.parse(T6230_F17.class,
                                        configureProvider.getProperty(SystemVariable.FXFS));
                            %>
                            <input type="radio" class="mr5" name="F17"
                                   value="ZRY" <%if (fxfs == T6230_F17.ZRY || "ZRY".equals(request.getParameter("F17"))) { %>
                                   checked="checked" <%} %> id="zry"/>自然月
                            <input type="radio" class="ml20 mr5" name="F17"
                                   value="GDR" <%if (fxfs == T6230_F17.GDR || "GDR".equals(request.getParameter("F17"))) { %>
                                   checked="checked" <%} %> id="gdr"/>固定日
                           <p tip class="prompt"></p>
                        </div>
                    </li>
                    <li id="fxr" class="item" style="display: none;">
                        <div class="til"><span class="red">*</span>付息日：</div>
                        <div class="con">
                            <select class="select2" name="F18" id="F18">
                                <%
                                    for (int i = 1; i <= 28; i++) {
                                %>
                                <option value="<%=i %>"
                                        <%if((i+"").equals(request.getParameter("F18"))){ %>selected="selected"<%} %>><%=i %>号
                                </option>
                                <%} %>
                            </select>
                            <p tip class="prompt"></p>
                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>起息日：</div>
                        <div class="con">
                            <select class="select2" name="F19" id="F19">
                                <%
                                    int qxr = IntegerParser.parse(request.getParameter("F19"));
                                %>
                                <option value="0" <%if(qxr==0){ %>selected="selected"<%} %>>T+0</option>
                               <option value="1" <%if(qxr==1){ %>selected="selected"<%} %>>T+1</option>
                               <option value="2" <%if(qxr==2){ %>selected="selected"<%} %>>T+2</option>
                               <option value="3" <%if(qxr==3){ %>selected="selected"<%} %>>T+3</option>
                               <option value="4" <%if(qxr==4){ %>selected="selected"<%} %>>T+4</option>
                               <option value="5" <%if(qxr==5){ %>selected="selected"<%} %>>T+5</option>
                            </select>
							 <p tip class="prompt"><span class="gray9">T+0表示放款当天开始计算利息(当日计息)，T+1表示放款后第一天开始计算利息(次日计息)，以此类推</span></p>
                            <p errortip class="prompt" style="display: none"></p>
                            
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>借款描述：</div>
                        <div class="con">
								<textarea name="t6231_f09" cols="" rows="" style="width:350px;height:100px;"
                                          class="textarea required min-length-20 max-length-500"><%StringHelper.filterHTML(out, request.getParameter("t6231_f09"));%></textarea>

                            <p tip class="prompt"><span class="gray9">输入20-500个字</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <%
                        TermManage termManage = serviceSession.getService(TermManage.class);
                        T5017 term = termManage.get(TermType.GRXXCQSQTK);
                        if(term != null){
                    %>
                    <li class="item mb10">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <input name="iAgree" onclick="checkoxBtn();" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
                            <a target="_blank"
                               href="<%=controller.getPagingItemURI(request, Term.class, TermType.GRXXCQSQTK.name())%>"
                               class="highlight">《<%=term.F01.getName()%>》</a>
                        </div>
                    </li>
                    <li class="item mb50">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <input type="submit" id="sub-btn" class="btn06 sumbitForme btn_gray btn_disabled" disabled="disabled" fromname="form1" value="提交"/>
                        </div>
                    </li>
                    <%}else{%>
                    <li class="item mb50">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <input type="submit" class="btn06 sumbitForme" fromname="form1" value="提交"/>
                        </div>
                    </li>
                    <%}%>
                </ul>
            </div>
        </div>
    </div>
</form>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "error"));
</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "doubt"));
</script>
<%
    }
%>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    $(".jejs").change(function () {
    	jejs();
    });

    function jejs(){
    	var $envs = $(".jejs");
        var toal = $envs.eq(0).val();
        var money = 100;
        var size = toal / money;
        //借款期限
        var month = $("input[name='F08']").val();
        //借款年化利率
        var year = $envs.eq(2).val();

        if (money == "" || month == "") {
            return false;
        }

        var $creaa = $("#creaa");
        var $crea = $("#crea");
        var $creb = $("#creb");
        var $crec = $("#crec");
        var $cred = $("#cred");
        var $cree = $("#cree");
        var $crehr = $("#crehr");
        $creaa.html((parseInt(money) * parseFloat($creaa.attr("fl")) * size).toFixed(2));
        $crea.html((parseInt(money) * parseFloat($crea.attr("fl")) * size).toFixed(2));
        $creb.html((parseInt(money) * parseFloat($creb.attr("fl")) * size).toFixed(2));
        $crec.html((parseInt(money) * parseFloat($crec.attr("fl")) * size).toFixed(2));
        $cred.html((parseInt(money) * parseFloat($cred.attr("fl")) * size).toFixed(2));
        $cree.html((parseInt(money) * parseFloat($cree.attr("fl")) * size).toFixed(2));
        $crehr.html((parseInt(money) * parseFloat($crehr.attr("fl")) * size).toFixed(2));

        if (money == "" || month == "" || year == "") {
            return false;
        }
        var mln = (parseFloat(year) / 12 / 100);
        //月还本息

        var s = ((parseFloat(parseInt(money) * mln * Math.pow((1 + mln), month)) / (Math.pow((1 + mln), month) - 1)));
        var j = ((money * parseFloat(<%=configureProvider.format(SystemVariable.LMONEY_SUCCESS_RATION)%>)));
        $("#ms").html(parseFloat(((s).toFixed(2)) * size).toFixed(2));
        $("#mj").html(parseFloat(((j).toFixed(2)) * size).toFixed(2));
    }
    
    $("#zry").click(function () {
        $("#fxr").hide();
    });
    $("#gdr").click(function () {
        $("#fxr").show();
    });

    $(function () {
        var hkfs = $("#hkfs").attr("value");
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#zry").attr("checked", true);
        }
        else {
            $("#gdr").attr("disabled", false);
        }
        var gdr = $("#gdr").attr("checked");
        if (gdr == 'checked') {
            $("#fxr").show();
        }
        loadPage();

        $("#F19").selectlist({
    		width: 372,
    		height: 36
    	});
        $("#F08").selectlist({
    		width: 372,
    		height: 36,
    		onChange: function(){
    			jejs();
    		}
    	});
        
        $("#F10").selectlist({
    		width: 372,
    		height: 36
    	});
        
        $("#hkfs").selectlist({
    		width: 372,
    		height: 36,
    		onChange: function(){
    			var hkfs = $("input[name='F10']").attr("value");
    	        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
    	            $("#gdr").attr("disabled", "disabled");
    	            $("#gdr").attr("checked", false);
    	            $("#fxr").hide();
    	            $("#zry").attr("checked", true);
    	        }
    	        else {
    	            $("#gdr").attr("disabled", false);
    	        }

    	        accDay(hkfs);
    		}
    	});
        
        //“我同意”按钮切回事件
        $("input:checkbox[name='iAgree']").attr("checked", false);
        $("input:checkbox[name='iAgree']").click(function() {
            var iAgree = $(this).attr("checked");
            var register = $("#sub-btn");
            if (iAgree) {
                register.removeClass("btn_gray btn_disabled");
            } else {
                register.addClass("btn_gray btn_disabled");
            }
        });
        
        /* $('#gCode').blur(function () {
        	guaranteeCheck();
        }); */
    });
    /* $("#hkfs").click(function () {
        var hkfs = $("#hkfs").attr("value");
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#fxr").hide();
            $("#zry").attr("checked", true);
        }
        else {
            $("#gdr").attr("disabled", false);
        }

        accDay(hkfs);
    }); */

    function loadPage() {
        var hkfs = $("#hkfs").attr("value");
        accDay(hkfs);
    }

    function accDay(hkv) {
        if ("YCFQ" == hkv) {
            $("#_accDay").show();
        } else {
            $("#_accDay").hide();
        }
    }

    function hidebg() {
        $("div.dialog").hide();
        $("div.popup_bg").hide();
    }

    function checkoxBtn()
    {
        var iAgree = $("input:checkbox[name='iAgree']").attr("checked");
        if (iAgree != "checked"){
            $("#sub-btn").attr('disabled',true);
        }else{
            $("#sub-btn").attr('disabled',false);
        }
    }
    
    /**
    * 根据担保号校验是否有符合要求的担保人存在
    * （1.启用中状态；2.没有逾期借款）
    */
    function guaranteeCheck() {
    	var gCodeObj = $("#gCode");
    	var gCode = gCodeObj.val();
    	var tip = gCodeObj.parent().find("p[tip]");
    	var correct = gCodeObj.parent().find("p[errortip]");
    	var isNull = /^[\s]{0,}$/;
    	correct.hide();
    	var validat = true;
    	if (isNull.test(gCode)) {
    		tip.hide();
    		correct.text("担保人编号不能为空");
    		correct.show();
    		validat = false;
    		return validat;
    	}
    	tip.show();
    	$.ajax({
    		type: 'POST',
    		url: '<%=controller.getURI(request, CheckGuaranteeCodeExists.class)%>',
    		data: {"gCode" : gCode},
    		dataType: "html",
    		async:false,
    		success:function(data){
    			if ($.trim(data) == 'false') {
    				tip.hide();
    				correct.text("您所输入的担保人编号错误或该担保人目前暂时不能提供担保业务！");
    				correct.show();
    				validat = false;
    			}else if(data.indexOf("script")>-1 && data.indexOf("alert")>-1){
    				tip.hide();
    				correct.text("担保人编号不能包含特殊字符");
    				correct.show();
    				validat = false;
    			}
    		},
    		error: function(XMLHttpRequest, textStatus, errorThrown){
           		$(".popup_bg").show();
           		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
    		}
    	});
    	return validat;
    }
    
    /**
    * 表单提交前自定义校验
    */
    function onSubmit() {
    	if(!guaranteeCheck())
    	{
    		var gCodeObj = $("#gCode");
        	var tip = gCodeObj.parent().find("p[tip]");
        	var correct = gCodeObj.parent().find("p[errortip]");
        	tip.hide();
			correct.text("您所输入的担保人编号错误或该担保人目前暂时不能提供担保业务！");
			correct.show();
    		return false;
    	}
    	return true;
    }

</script>
<script type="text/javascript"
        src="<%=controller.getURI(request, Region.class)%>"></script>
</body>
</html>