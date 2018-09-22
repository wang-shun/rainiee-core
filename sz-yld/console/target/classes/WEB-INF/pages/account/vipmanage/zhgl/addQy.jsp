<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ZhList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.AddQy" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "ZHGL";
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>增加企业账号
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, AddQy.class)%>" method="post" class="form1">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业名称：</span>
                                        <input name="F04" maxlength="20" type="text"
                                               class="text border w300 pl5 required min-length-6"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F04")); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5"></em>企业简称：</span>
                                        <input name="F18" maxlength="20" type="text"
                                               class="text border w300 pl5"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F18")); %>"/>
                                    </li>
                                    <li id="szhyli" class="mb10"><span
                                            class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否三证合一：</span>
                                        <input id="szhy" type="hidden" name="F20"
                                               value="<%=request.getParameter("F20")%>"> <label
                                                id="szhy1" class="cursor-p display-ib mr50 pt5 pb5"
                                                for="ChoiceIs"> <input type="radio" name="szhy"
                                                                       class="mr10" value="Y"
                                                                       <%if("Y".equals(request.getParameter("F20"))){ %>checked="checked"<%} %>
                                                                       onclick="szhyCheck()"/>是
                                        </label> <label class="cursor-p display-ib mr50 pt5 pb5"
                                                        for="ChoiceNo"> <input id="szhy2" type="radio"
                                                                               name="szhy" class="mr10" value="N"
                                                                               <%if("N".equals(request.getParameter("F20")) || StringHelper.isEmpty(request.getParameter("F20"))){ %>checked="checked"<%} %>
                                                                               onclick="szhyCheck()"/>否
                                        </label>

                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span></li>
                                    <li class="mb10 sszhy" id="shxy">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>社会信用代码：</span>
                                        <input id="idF19" name="F19" maxlength="18" type="text"
                                        	   class="text border w300 pl5 required" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'');value=value.trim()"
                                        	   mtest="/^([A-Za-z0-9]+){18}$/"
                                               mtestmsg="长度为18位，由字母,数字组成"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F19")); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                        <span id="idShxy" style="display: none;color:red;"></span>
                                    </li>
                                    <li class="mb10 nszhy" id="yyzz">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>营业执照登记注册号：</span>
                                        <input name="F03" maxlength="20" type="text"
                                               class="text border w300 pl5 required" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'');value=value.trim()"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F03")); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10 nszhy" id="nsh">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业纳税号：</span>
                                        <input name="F05" maxlength="20" type="text"
                                               class="text border w300 pl5 required" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'');value=value.trim()"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F05"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10 nszhy" id="jgdm">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>组织机构代码：</span>
                                        <%if(!"huifu".equalsIgnoreCase(ESCROW_PREFIX)){ %>
                                        	<input name="F06" maxlength="20" type="text"
                                               class="text border w300 pl5 required max-length-20 min-length-9" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'');value=value.trim()"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F06"));%>"/>
                                         <%} else { %>
                                         	<input name="F06" maxlength="9" type="text"
                                               class="text border w300 pl5 required max-length-9 min-length-1" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'');value=value.trim()"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F06"));%>"/>
                                         <%} %>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册年份：</span>
                                        <input name="F07" maxlength="4" type="text"
                                               class="text border w300 pl5 required isyear max-length-4 min-length-4"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F07"));%>"/>年
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册资金：</span>
                                        <input name="F08" maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式，且最多为两位小数" type="text" class="text border w300 pl5 required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F08"));%>"/>万元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>行业：</span>
                                        <input name="F09" maxlength="20" type="text"
                                               class="text border w300 pl5 required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F09"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业规模：</span>
                                        <input name="F10" maxlength="9" minlength="1" type="text"
                                               class="text border w300 pl5 required isint"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F10"));%>"/>人
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人：</span>
                                        <input name="F11" maxlength="11" type="text"
                                               class="text border w300 pl5 required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F11"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人身份证号：</span>
                                        <input name="F12" maxlength="18" type="text"
                                               class="text border w300 pl5 required idcard"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F12"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>资产净值：</span>
                                        <input name="F14" maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式，且最多为两位小数" type="text" class="text border w300 pl5 required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F14"));%>"/>万元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上年度经营现金流入：</span>
                                        <input name="F15" maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式，且最多为两位小数" type="text" class="text border w300 pl5 required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F15"));%>"/>万元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5">贷款卡证书编号：</span>
                                        <input name="F16" maxlength="50" type="text" class="text border w300 pl5"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F16"));%>"/>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5">企业信用证书编号：</span>
                                        <input name="F17" maxlength="50" type="text" class="text border w300 pl5"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F17"));%>"/>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人手机号码：</span>
                                        <input name="mobile" maxlength="11" type="text"
                                               class="text border w300 pl5 required phonenumber"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("mobile"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人邮箱地址：</span>
                                        <input name="email" maxlength="50" type="text"
                                               class="text border w300 pl5 required email"
                                               mtest="/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("email"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5"></em>企业联系人：</span>
                                        <input name="realName" maxlength="20" type="text" class="text border w300 pl5"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>

                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><em class="red pr5"></em>联系人手机：</span>
                                        <input name="lxTel" maxlength="11" type="text"
                                               class="text border w300 pl5 mobile"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("lxTel"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <% 
                                    	if(null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX)){ 
                                    %>
									<li class="mb10"><span class="display-ib w200 tr mr5">
										<em class="red pr5">*</em>开户银行许可证：</span> 
										<input name="F21"  maxlength="20" type="text" class="text border w300 pl5 yhgl_ser required"
                                             value="<%StringHelper.filterHTML(out, request.getParameter("F21"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%} %>
                                    <%-- <li class="mb10"><span
                                            class="display-ib w200 tr mr5">是否允许投资：</span>
                                        <input id="isInvestorHidden" type="hidden" name="isInvestorHidden" value="<%=request.getParameter("isInvestor")%>"> 
                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="ChoiceIs"> 
                                        	<input type="radio" name="isInvestor" class="mr10" value="S" <%if("S".equals(request.getParameter("isInvestor"))){ %>checked="checked"<%} %>/>是
                                        </label>
                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="ChoiceNo"> 
                                        	<input type="radio" name="isInvestor" class="mr10" value="F" <%if("F".equals(request.getParameter("isInvestor")) || StringHelper.isEmpty(request.getParameter("isInvestor"))){ %>checked="checked"<%} %>/>否
                                        </label>

                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li> --%>
                                    
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="提交" fromname="form1"/> 
                                            <input type="button" onclick="window.location.href='<%=controller.getURI(request, ZhList.class)%>'"
                                                 class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(messageInfo)) {
        String forwordUrl = controller.getURI(request, ZhList.class);
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showSuccInfo('<%=messageInfo%>', "yes",'<%=forwordUrl%>',false));
</script>
<%} %>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>

<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
</script>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    $(function () {
        szhyCheck();
    });

    function szhyCheck() {
        if ($("input[name='szhy']:checked").val() == "Y") {
            $(".sszhy").show();
            $(".nszhy").hide();
        } else {
            $(".sszhy").hide();
            $(".nszhy").show();
        }
        $("#szhy").val($("input[name='szhy']:checked").val());
    }
</script>
</body>
</html>