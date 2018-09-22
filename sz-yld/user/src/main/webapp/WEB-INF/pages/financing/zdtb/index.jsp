<%@ page import="com.dimeng.p2p.user.servlets.financing.zdtb.UpdateRule" %>
<%@ page import="com.dimeng.p2p.user.servlets.financing.zdtb.DeleteRule" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dimeng.p2p.user.servlets.financing.zdtb.ExistRule" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6101" %>
<%@ page import="com.dimeng.p2p.common.enums.AutoSetStatus" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.CreditLevelManage" %>
<%@ page import="com.dimeng.p2p.modules.bid.user.service.ZdtbManage" %>
<%@ page import="com.dimeng.p2p.modules.bid.user.service.entity.AutoBidSet" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@ page import="com.dimeng.p2p.common.enums.TermType" %>
<%@ page import="com.dimeng.p2p.user.servlets.Term" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>自动投资工具-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "ZDTBGJ";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="user_til">
                    <i class="icon"></i><span class="gray3 f18">自动投资</span>
                </div>

                <%
                    ZdtbManage autoUtilFinacingManage = serviceSession.getService(ZdtbManage.class);
                    CreditLevelManage creditLevelManage = serviceSession.getService(CreditLevelManage.class);
                    int autoBidCountQY = autoUtilFinacingManage.autoBidCountQY(null);
                    List<AutoBidSet> autoBidSets = autoUtilFinacingManage.search();

                    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
                    T6101 userInfo = userInfoManage.search();
                    //只有自然人才能进入该页面
                    if (userInfo == null || T6110_F06.FZRR == t6110.F06) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
       	         		/* if(autoBidSet != null &&  autoBidSet.autoSetStatus == AutoSetStatus.QY){ */
                %>


                <!-- <div class="f24 pb20">自动投资状态：关闭状态</div>
         <div class=" mb20 pb30 tc mt40">
       <div style="width:360px; margin:auto">
           <a id="yjtb" href="javascript:void(0)" class="btn06 fl mr50" >开启一键式自动投资</a>
           <a id="zztb" href="javascript:void(0)"  class="btn07 fl">开启自助式自动投资</a>
       </div>
       </div> -->

                <div class="tr">
                	<%if("huifu".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX)) && autoBidSets!= null && autoBidSets.size() > 0){ %>
                	<input type="button" value="新增规则" class="btn04 reset" style="display:none"/>
                    <%}else{ %>
                    <input id="zztb" type="button" value="新增规则" class="btn04 reset"/>
                    <%} %>
                </div>
                <div class="user_table mt15">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td align="center">序号</td>
                            <td align="center">借款期限<br/>(月)</td>
                            <td align="center">年化利率范围<br/>(%)</td>
                            <td align="center">每次投资金额<br/>(元)</td>
                            <td align="center">账户保留余额<br/>(元)</td>
                            <td align="center">状态</td>
                            <td align="center">操作时间</td>
                            <td align="center">操作</td>
                        </tr>
                        <tbody id="dataBody">
                        <%
                            if (autoBidSets != null && autoBidSets.size() > 0) {
                                int a = 1;
                                for (AutoBidSet auto : autoBidSets) {
                        %>
                        <tr>
                            <td align="center"><%=a++ %>
                            </td>
                            <td align="center"><%=auto.jkqxStart %>~<%=auto.jkqxEnd %>
                            </td>
                            <td align="center"><%=Formater.formatAmount(auto.rateStart.doubleValue() * 100) %>
                                ~<%=Formater.formatAmount(auto.rateEnd.doubleValue() * 100) %>
                            </td>
                            <%if (auto.all == 0) {%>
                            <td align="center">全部</td>
                            <%} else {%>
                            <td align="center"><%=Formater.formatAmount(auto.timeMoney)%>
                            </td>
                            <%}%>
                            <td align="center"><%=Formater.formatAmount(auto.saveMoney)%>
                            </td>
                            <td align="center">
                                <%if (auto.autoSetStatus.name().equals(AutoSetStatus.QY.name())) { %>
                                开启 <%} else { %> 关闭 <%} %>
                            </td>
                            <td align="center"><%=DateTimeParser.format(auto.setTime, "yyyy-MM-dd HH:mm") %>
                            </td>
                            <td align="center">
                                <%if (auto.autoSetStatus.name().equals(AutoSetStatus.TY.name())) { %>
                                <a class="blue" href="javascript:void(0);"
                                   onclick="updateZdtb('QY',<%=auto.id%>)">开启</a> <%} else { %> <a
                                    class="blue" href="javascript:void(0);"
                                    onclick="updateZdtb('TY',<%=auto.id%>)">关闭</a> <%} %> <a
                                    class="blue" href="javascript:void(0);"
                                    onclick="deleteZdtb(<%=auto.id%>)">删除</a>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="8" align="center">暂无数据</td>
                        </tr>
                        <%} %>
                        </tbody>
                    </table>
                </div>
                <div class="mt30 lh30">
                    <span class="highlight">自动投资工具说明：</span><br/>
                    投资进度达到<%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_END_PROGRESS)) * 100 %>
                    %时停止自动投资。若投资后投资进度超过<%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_END_PROGRESS)) * 100 %>
                    %，则投达到<%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_END_PROGRESS)) * 100 %>
                    %的剩余金额。<br/>
                    单笔投资金额若超过该标借款总额的<%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_MAX_PERCENT)) * 100 %>
                    %，则按照该标借款总额的<%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_MAX_PERCENT)) * 100 %>
                    %金额投资。<br/>
                    <!-- <p>满足自动投资规则的金额小于设定的每次投资金额，也会进行自动投资。</p> -->
                    借款用户在获得借款时会自动关闭自动投资，以避免借款被用作自动投资资金。<br/> 投资排序规则如下：<br/>
                    a）每个用户可以开启多个不同借款期限范围的自动投资规则，最多可开启三个自动投资规则。<br/>
                    b）每个规则投资序列按照上一次自动投资时间先后进行排序。<br/> c）每个规则每个标仅自动投资一次，投资后，排到队尾。<br/>
                    d）如果余额不足，停止投资，余额充足后，继续投资。
                </div>

            </div>
        </div>
    </div>


    <div class="popup_bg" style="display: none;"></div>
    <div class="dialog" id="zztb_dialog" style="display: none;width: 520px;">
        <div class="title">
            <a href="javascript:void(0);" class="out" id="zztb_close"></a>新增规则
        </div>
        <div class="content">
            <form action="<%=configureProvider.format(URLVariable.AUTO_BID_AUTHORIZED) %>"
                  id="form1" class="form1" method="post" onsubmit="return onSubmit()">
                <%
                    boolean flag = userInfo.F06.compareTo(BigDecimalParser.parse(configureProvider.format(SystemVariable.AUTO_BIDING_MUST_AMOUNT))) >= 0 ? true : false;
                %>
                <ul class="text_list">
                    <li>
                        <div class="til">账户可用余额：</div>
                        <div><span class="red"><%=userInfo.F06 %></span>元<span class="f12">（大于等于<span
                                class="red"> <%=configureProvider.format(SystemVariable.AUTO_BIDING_MUST_AMOUNT) %></span>元才可开启自动投资工具）</span>
                        </div>
                    </li>
                    <li>
                        <div class="til">每次投资金额：</div>
                        <div class="con">
                            <input name="mctbje" type="radio" value="1" checked="checked" class="mr5 reset"/>指定金额
                            <input name="mctbje" type="radio" value="0" class="ml20 mr5 reset"/>全部

                        </div>
                    </li>
                    <li class="mctbje">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <input name="timeMoney" type="text"
                                   class="text_style yhgl_ser border required isint mulriple-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT) %> min-size-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MIN_AMOUNT) %> max-size-<%=userInfo.F06 %>"
                                   value=""/>元

                            <p class="f12" tip>
                                （该数值须不小于<span
                                    class="red"><%=configureProvider.format(SystemVariable.AUTO_BIDING_MIN_AMOUNT) %></span>元，
                                且为<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT) %>的倍数）
                            </p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <%-- <li class="mctbje">
                        <td rowspan="2" valign="top">&nbsp;</td>
                        <td><input name="timeMoney" type="text"
                            class="yhgl_ser border required isint mulriple-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT) %>
                    min-size-<%=configureProvider.format(SystemVariable.AUTO_BIDING_MIN_AMOUNT) %> max-size-<%=userInfo.F06 %>"
                            value="" />元
                            <p tip>
                                （该数值须不小于<span class="red"><%=configureProvider.format(SystemVariable.AUTO_BIDING_MIN_AMOUNT) %></span>元，
                                且为<%=configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT) %>的倍数）
                            </p>
                            <p errortip class="" style="display: none"></p></td>
                    </li> --%>
                    <li>
                        <div class="til">年化利率范围：</div>
                        <div class="con">
                            <input name="rateStart" type="text"
                                   class="text_style yhgl_ser w70 border required maxf-size-99.99 lxmin-size"
                                   style="width:85px;" 
                                   value=""/>
                            %- <input name="rateEnd" type="text"
                                      class="text_style yhgl_ser w70 border required maxf-size-99.99 minf-size-0.01 lxmax-size"
                                      mtest="/^\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="大于0且只能有两位小数" value=""
                                      style="width:85px;"/>
                            %
                            <p class="f12" tip>利率精确到小数点后两位且小于100</p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <li>
                        <div class="til">借款期限：</div>
                        <div class="con">
                            <select name="jkqxStart" id="jkqxStart" class="select7 required"
                                    onchange="vailJkqx()" style="width:97px;">
                                <%
                                    for (int i = 0; i <= 36; i++) {
                                %>
                                <option value="<%=i %>" <%if (i == 1) {%> selected="selected"
                                        <%} %>><%=i %>
                                </option>
                                <%} %>
                            </select> 月- <select name="jkqxEnd" id="jkqxEnd" class="select7 required"
                                                 onchange="vailJkqx()" style="width:97px;">
                            <%
                                for (int i = 1; i <= 36; i++) {
                            %>
                            <option value="<%=i %>" <%if (i == 36) {%> selected="selected"
                                    <%} %>><%=i %>
                            </option>
                            <%} %>
                        </select> 月
                            <p id="errorjk" class="" style="display: none"></p>
                        </div>
                    </li>
                    <li class="mctbje">
                        <div class="til">账户保留金额：</div>
                        <div class="con">
                            <input type="text" name="saveMoney"
                                   class="text_style yhgl_ser border required max-size-<%=userInfo.F06 %>"
                                   mtest="/^\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="大于0且只能有两位小数"
                                    />元
                            <p class="f12" tip>（您可填写一个金额，这部分钱不会加入自动投资）</p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                </ul>
                 <div class="mt20">
                    <% if (userInfo.F06.doubleValue() >= Double.valueOf(configureProvider.getProperty(SystemVariable.AUTO_BIDING_MUST_AMOUNT))) { %>
                    <%
                        TermManage termManage = serviceSession.getService(TermManage.class);
                        T5017 term = termManage.get(TermType.TFJKXY);
                        T5017 term2 = termManage.get(TermType.FFJKXY);
                    %>
                    <input name="iAgree" type="checkbox" id="iAgree" class="m_cb ml50"/>&nbsp;
                    <%
                        if(term != null || term2 != null){
                            T5017 fxtsh = termManage.get(TermType.FXTSH);
                    %>
                    <label for="iAgree">我已阅读</label>
                          <%if(fxtsh != null){%>
                              <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.FXTSH.name())%>" class="highlight">《<%=fxtsh.F01.getName()%>》</a>
                          <%}%>
                          <%if(term != null){%>
                              <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.TFJKXY.name())%>" class="highlight">《<%=term.F01.getName()%>》</a>
                          <%}%>
                          <%if(term2 != null){%>
                              <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.FFJKXY.name())%>" class="highlight">《<%=term2.F01.getName()%>》</a>
                          <%}%>
                          <br>
                          <label for="iAgree" style="margin-left:70px;">同意开启自动投资</label>
                    <%}else{%>
                    <label for="iAgree">同意开启自动投资</label>
                    <%}%>
                    
                    <div class="tc mt20">
                        <input type="submit" id="sub-btn" class="btn04 btn btn001 sumbitForme btn_gray btn_disabled" disabled="disabled"  fromname="form1" style="cursor: pointer;height:32px;" value="开启投资"/>
                    </div>
                    <% } else { %>
                    <div class="btn04 btn_gray" style="cursor: default;height:32px;line-height:32px;margin-left:190px;">开启投资</div>
                    <%}%>
                </div> 
            </form>

        </div>
    </div>
    <div id="info"></div>
    <%@include file="/WEB-INF/include/footer.jsp" %>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zdtb.js"></script>
    <%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "successful", $("#sbSucc").val()));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "error"));
    $("div.popup_bg").show();
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
    $("div.popup_bg").show();
</script>
<%
    }
%>
    <script type="text/javascript">
        var postUrl = '<%=controller.getURI(request, ExistRule.class)%>';
        var updateUrl = '<%=controller.getURI(request, UpdateRule.class)%>';
        var delUrl = '<%=controller.getURI(request, DeleteRule.class)%>';
        var count = '<%=autoBidCountQY%>';
        $(function () {
        	
        	$('.select7').selectlist({
        		zIndex: 20,
        		width: 95,
        		optionHeight: 28,
        		height: 28,
        		onChange: function(){
        			vailJkqx();
        		}
        	});
        	
            var flag = <%=flag%>;
            if(!flag){
                $("input[type='text'],input[type='radio'],select,input[type='submit'],input[type='checkbox']").attr("disabled","disabled");
            }
            //“我同意”按钮切回事件
            $("input:checkbox[name='iAgree']").attr("checked", false);
            $("input:checkbox[name='iAgree']").click(function () {
                var iAgree = $(this).attr("checked");
                var register = $("#sub-btn");
                if (iAgree) {
                    register.removeClass("btn_gray btn_disabled");
                    $("#sub-btn").attr('disabled',false);
                } else {
                    register.addClass("btn_gray btn_disabled");
                    $("#sub-btn").attr('disabled',true);
                }
            });
        });
    </script>
</body>
</html>