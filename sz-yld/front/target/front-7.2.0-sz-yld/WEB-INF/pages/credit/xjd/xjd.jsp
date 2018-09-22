<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.DecimalFormat"%>
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
<%@page import="com.dimeng.p2p.front.servlets.credit.xjd.Xjd" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.xjd.Index" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.GrManage" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Rzxx" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidWillManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6119" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%
    if (dimengSession == null || !dimengSession.isAuthenticated()) {
        String errorMessage = "您还未登录，请先登录系统，再进行信用贷申请！";
        String info = errorMessage +"<span class='display:none'><a id='to_validate' href=\""+"/user/login.html?_z=/credit/xjd/xjd.html"+"\" class=\"blue\"></a></span>";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response,controller.getViewURI(request, Index.class));
        return;
    }else
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
        String errorMessage = "申请信用贷必须先在第三方注册托管账户，";
        //开户引导页
        String info = errorMessage +"<a id='to_validate' href=\""+openEscrowGuide+"\" class=\"blue\">立即注册</a>";
        controller.prompt(request, response, PromptLevel.ERROR, info);
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }
    SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
    if (!mge.isSmrz() || !mge.getYhrzxx()) {
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        String errorMessage = "申请信用贷必须先进行真实身份认证、手机号码认证";
        if(isOpenWithPsd){
            errorMessage = "申请信用贷必须先进行真实身份认证、手机号码认证，交易密码设置";
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
        String info = "申请信用贷必须认证邮箱，请您到<a id='to_validate' href=\""+configureProvider.format(safeManage.getSafetymsgView())+"\" class=\"blue\">个人基础信息</a>设置。";
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
    int minAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MIN));
    int maxAge = Integer.parseInt(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AGE_MAX));
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
        controller.prompt(request, response, PromptLevel.ERROR, "申请信用贷必须先网签合同认证，<br/>请您到<a id='to_validate' href=\""+configureProvider.format(URLVariable.USER_NETSIGN_URL)+"\" class=\"blue\">网签合同</a>设置。");
        controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
        return;
    }

    String errmes = controller.getPrompt(request, response, PromptLevel.ERROR);
    T6211[] bidTypes = serviceSession.getService(BidWillManage.class).getBidTypeAll();
	String xyAmount = new DecimalFormat("#0").format(userInfo.xyAmont);
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form action="<%=controller.getURI(request, Xjd.class)%>" class="form1" method="post">
    <div class="main_bg">
        <input type="hidden" name="ltype"
               value="<%=CreditType.XJD.name()%>">
        <input id="shengId" value="<%=request.getParameter("sheng")%>" type="hidden"/>
        <input id="shiId" value="<%=request.getParameter("shi")%>" type="hidden"/>
        <input id="xianId" value="<%=request.getParameter("xian")%>" type="hidden"/>

        <div class="creditloan_bg">
            <div class="creditloan_lc"></div>
        </div>

        <div class="main_mod">
            <div class="main_hd"><i class="icon"></i><span class="gray3 f18">填写借款申请</span></div>
            <span style="color: red;"><%StringHelper.filterHTML(out, errmes);%></span>

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
                        <div class="til"><span class="red">*</span>借款标题：</div>
                        <div class="con">
                            <input name="F03" type="text"
                                   class="text required max-length-14"
                                   value="<%StringHelper.filterHTML(out,request.getParameter("F03"));%>"/>

                            <p tip class="prompt"><span class="gray9">不超过14个字</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>

                    </li>
                   <%-- <li class="item">
                        <div class="til"><span class="red">*</span>借款用途：</div>
                        <div class="con">
                            <select name="t6231_f08" class="select">
                                <option value="短期周转"
                                        <%if("短期周转".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    短期周转
                                </option>
                                <option value="购房借款"
                                        <%if("购房借款".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    购房借款
                                </option>
                                <option value="购车借款"
                                        <%if("购车借款".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    购车借款
                                </option>
                                <option value="装修借款"
                                        <%if("装修借款".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    装修借款
                                </option>
                                <option value="婚礼筹备"
                                        <%if("婚礼筹备".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    婚礼筹备
                                </option>
                                <option value="教育培训"
                                        <%if("教育培训".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    教育培训
                                </option>
                                <option value="投资创业"
                                        <%if("投资创业".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    投资创业
                                </option>
                                <option value="医疗支出"
                                        <%if("医疗支出".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    医疗支出
                                </option>
                                <option value="其他借款"
                                        <%if("其他借款".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    其他借款
                                </option>
                                <option value="个人消费"
                                        <%if("个人消费".equals(request.getParameter("t6231_f08"))){ %>selected="selected"<%} %>>
                                    个人消费
                                </option>
                            </select>

                            <p class="prompt"></p>
                        </div>
                    </li>--%>
                    <li class="item">
                        <div class="til"><span class="red">*</span>所在城市：</div>
                        <div class="con">
                            <select name="sheng" id="sheng"  class="select6 required">
                            </select>

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <%-- <li class="item">
                        <div class="til"><span class="red">*</span>借款标类型：</div>
                        <div class="con">
                            <select name="F04" class="select required">
                                <%
                                    if (bidTypes != null) {
                                        for (T6211 type : bidTypes) {
                                %>
                                <option value="<%=type.F01 %>"
                                        <%if(!StringHelper.isEmpty(request.getParameter("F04"))){if(type.F01 ==Integer.parseInt(request.getParameter("F04"))){  %>selected="selected"<%
                                        }
                                    }
                                %>><%StringHelper.filterHTML(out, type.F02); %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li> --%>


                    <li class="item">
                        <div class="til"><span class="red">*</span>借款金额：</div>
                        <div class="con">
                            <input name="F05" type="text"
                                   value="<%=StringHelper.isEmpty(request.getParameter("F05"))? "": request.getParameter("F05")%>" ftest="checkAmuont"
                                   class="text required isint mulriple-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT)%> min-size-<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_AMOUNT_MIN);%> max-size-<%=configureProvider.format(SystemVariable.CREDIT_LOAN_AMOUNT_MAX)%> jejs"
                                    />元
                            <p tip class="prompt"><span
                                    class="gray9">您当前的信用额度为<%=xyAmount %>,借款金额范围<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_AMOUNT_MIN);%>-<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_AMOUNT_MAX);%>，且为<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT)%>的倍数</span>
                            </p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>还款方式：</div>
                        <div class="con">
                            <select name="F10" class="select2" id="hkfs">
                                <%
                                    boolean isDEBX = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_DEBX));
                                    boolean isMYFX = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_MYFX));
                                    boolean isYCFQ = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_YCFQ));
                                    boolean isDEBJ = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_DEBJ));
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
                                   value="<%StringHelper.filterHTML(out, request.getParameter("jkqx")); %>"
                                   class="text   required min-size-<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_QX_MIN);%> max-size-<%=configureProvider.format(SystemVariable.CREDIT_LOAN_QX_MAX)%>" mtest="/^\d+$/" mtestmsg="必须为整数" name="F09"/>
                            月/天
                            <p tip class="prompt"><span class="gray9">除本息到期一次付清可选按天计算外，其他皆以月为单位</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>筹款期限：</div>
                        <div class="con">
                            <select name="F08" id="F08" class="select2 jejs">
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
                    <%-- <li>
                        <div class="tit">&nbsp;</div>
                        <div class="info">
                            <%
                                T6230_F11 t6230_F11=EnumParser.parse(T6230_F11.class, request.getParameter("F11"));
                                T6230_F13 t6230_F13=EnumParser.parse(T6230_F13.class, request.getParameter("F13"));
                                T6230_F14 t6230_F14=EnumParser.parse(T6230_F14.class, request.getParameter("F14"));
                                T6230_F15 t6230_F15=EnumParser.parse(T6230_F15.class, request.getParameter("F15"));
                                T6230_F16 t6230_F16=EnumParser.parse(T6230_F16.class, request.getParameter("F16"));
                                T6230_F15 sfzdfk=EnumParser.parse(T6230_F15.class, configureProvider.getProperty(SystemVariable.SFZDFK));
                                T6230_F16 sfyxlb=EnumParser.parse(T6230_F16.class, configureProvider.getProperty(SystemVariable.SFYXLB));
                            %>
                            <input type="checkbox" name="F11" value="S" <%if(t6230_F11==T6230_F11.S){ %> checked="checked" <%} %>/>有担保
                            <input type="checkbox" name="F13" value="S" <%if(t6230_F13==T6230_F13.S){ %> checked="checked" <%} %>/>有抵押
                            <input type="checkbox" name="F14" value="S" <%if(t6230_F14==T6230_F14.S){ %> checked="checked" <%} %>/>有实地认证
                            <input type="checkbox" name="F15" value="S" <%if(t6230_F15==T6230_F15.S||sfzdfk==T6230_F15.S){ %> checked="checked" <%} %>/>是否自动放款
                            <input type="checkbox" name="F16" value="S" <%if(t6230_F16==T6230_F16.S||sfyxlb==T6230_F16.S){ %> checked="checked" <%} %>/>是否允许流标
                        </div>
                    </li>
                     <li>
                        <div class="tit">担保方案：</div>
                        <div class="info">
                            <select class="sel jejs" name="F12">
                            <%
                            T6230_F12 dbfa=EnumParser.parse(T6230_F12.class,configureProvider.getProperty(SystemVariable.DBFA));
                                for(T6230_F12 t6230_F122:T6230_F12.values())
                                    {
                            %>
                            <option value="<%=t6230_F122.name() %>" <%if(dbfa==t6230_F122){ %>selected="selected"<%} %>><%=t6230_F122.getChineseName() %></option>
                            <%} %>
                            </select>
                        </div>
                    </li>
                     --%>
                    <%
                        String rateMin = Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MIN)));
                        String rateMax = Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MAX)));
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
                            <select class="select2" id="F18" name="F18">
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
                            <select class="select2" id="F19" name="F19">
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
                   <%-- <li class="item">
                        <div class="til"><span class="red">*</span>还款来源：</div>
                        <div class="con">
                            <input name="t6231_f16" type="text"
                                   class="text required max-length-30"
                                   value="<%StringHelper.filterHTML(out, request.getParameter("t6231_f16"));%>"/>

                            <p tip class="prompt"><span class="gray9">不超过30个字</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>

                    </li>--%>
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
<%--
<div id="smrz" style="display: none;">
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('smrz');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      	<div class="info">请完成<span class="red">实名认证，交易密码设置</span>，前往<a href="<%=configureProvider.format(URLVariable.USER_NCIIC)%>" class="blue">安全信息</a>进行认证。</div>
					<div class="clear"></div>
					<div class="dialog_btn">
						<a href="javascript:hidebg('smrz');" class="btn btn05">确认</a>
					</div>
			    </div>
			</div>
		</div>

		<div id="yqts" style="display: none;">
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('yqts');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      <div class="info">您存在借款逾期，请先进行还款！</div>
					<div class="clear"></div>
					<div class="dialog_btn">
						<a href="javascript:hidebg('yqts');" class="btn btn05">确认</a>
				  	</div>
			    </div>
			</div>
		</div>
		<div id="yycp" style="display: none;">
			<div class="popup_bg"></div>
			<div class="dialog d_error w510" style="margin: -150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('yycp');">
					<a href="javascript:hidebg('yycp');"></a>
				</div>
				<div class="con clearfix">
					<div class="borrowing clearfix"></div>
					<div class="borrowing clearfix">
						<p>您已申请过其他产品，不能再申请此产品！</p>
					</div>
					<div class="clear"></div>
					<div class="dialog_btn">
						<a href="javascript:hidebg('yycp');" class="btn btn05">确认</a>
					</div>
				</div>
			</div>
		</div>

		<div id="rzxx" style="display: none;">
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('rzxx');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      	<div class="info"><span class="red">认证信息中的必要认证没有通过</span>，前往<a href="<%=configureProvider.format(URLVariable.USER_RZXX)%>" class="blue">认证信息</a>进行认证。</div>
					<div class="clear"></div>
					<div class="dialog_btn">
						<a href="javascript:hidebg('rzxx');" class="btn btn05">确认</a>
					</div>
			    </div>
			</div>
		</div>

		<div id="ageErr" style="display: none;">
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('ageErr');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      	<div class="info">您的年龄不在申请条件范围之内，不能申请。</div>
					<div class="clear"></div>
					<div class="dialog_btn">
						<a href="javascript:hidebg('ageErr');" class="btn btn05">确认</a>
					</div>
			    </div>
			</div>
		</div>
	 <div id="wtgzc" <%if(StringHelper.isEmpty(erd) || !"6".equals(erd)){%> style="display: none;" <%}%>>
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;top:250px;">
				<div class="dialog_close fr" onclick="hidebg('wtgzc');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      	<div class="info">您还没在第三方托管注册账号，请<a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>" class="red">立即注册</a>！</div>
			    </div>
			</div>
		</div>

	<div id="ercifenpei" <%if(StringHelper.isEmpty(erd) || !"7".equals(erd)){%> style="display: none;" <%}%>>
			<div class="popup_bg"></div>
			<div class="dialog w510" style="margin:-150px 0 0 -255px;">
				<div class="dialog_close fr" onclick="hidebg('ercifenpei');"><a href="javascript:void(-1);"></a></div>
			    <div class="con clearfix">
			      <div class="d_error fl"></div>
			      	<div class="info">该用户尚未授权还款转账与二次分配转账，不能发标！<a style="color:red" href="<%configureProvider.format(out,URLVariable.AUTHORIZE_URL);%>">点击授权</a></div>
			    </div>
			</div>
	</div> --%>
<%
    String ermsg = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(ermsg)) {
%>
<div class="popup_bg"></div>
<div class="dialog ">
    <div class="title"><a href="javascript:void(-1);" class="out" onclick="hidebg();"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
                <p class="f20 gray33">
                    <%
                        StringHelper.filterHTML(out, ermsg);
                    %>
                </p>
            </div>
        </div>
        <div class="tc mt20">
            <a class="btn01"
               href="javascript:void(0);" onclick="hidebg()">确定</a>
        </div>
    </div>
</div>
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

    //校验信用额度
    function checkAmuont(){
    	var value = $(this).val();
    	var amount = <%=xyAmount %>;
   	 	var $error = $(this).nextAll("p[errortip]");
   		var $tip = $(this).nextAll("p[tip]");
   		if(parseInt(value) > parseInt(amount)){
	   	 	$error.addClass("error_tip");
			$error.html("大于信用额度值："+amount);
			$tip.hide();
			$error.show();
	    	return false;
   		}
   		
   		return true;
    } 
    
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
        $("#F18").selectlist({
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

</script>
<script type="text/javascript"
        src="<%=controller.getURI(request, Region.class)%>"></script>
</body>
</html>