<%@page import="com.dimeng.p2p.S61.enums.T6110_F19"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page import="com.dimeng.p2p.variables.defines.BadClaimVariavle"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.user.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.framework.http.servlet.Controller"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.agreement.BlzqzrAgreement"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page import="com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaimTotal"%>
<%@page import="com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage"%>
<%@page import="com.dimeng.p2p.user.servlets.fxbyj.PayBadClaim"%>
<%@page import="org.bouncycastle.util.encoders.Hex"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey"%>
<%@page import="com.dimeng.p2p.service.PtAccountManage"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.common.enums.TermType"%>
<%@page import="com.dimeng.p2p.user.servlets.Term"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@page import="com.dimeng.p2p.user.servlets.fxbyj.CheckUser"%>
<%@page import="com.dimeng.p2p.S61.entities.T6101"%>
<%@page import="com.dimeng.p2p.user.servlets.fxbyj.Blzqzr"%>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@ page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>不良债权转让-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "JGDB";
    CURRENT_SUB_CATEGORY = "BLZQZR";
    UserInfoManage uifManage = serviceSession.getService(UserInfoManage.class);
	T6110 userInfo =uifManage.getUserInfo(serviceSession.getSession().getAccountId());
	Boolean is_badclaim = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
	SubscribeBadClaimManage sbcService = serviceSession.getService(SubscribeBadClaimManage.class);
	boolean isBuyBadClaim1 = sbcService.checkIsBuyBadClaim(serviceSession.getSession().getAccountId());
	if(!isBuyBadClaim1 && (!is_badclaim || userInfo.F10 != T6110_F10.S || userInfo.F19 != T6110_F19.S)){
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    SubscribeBadClaimTotal sbcTotal = sbcService.getSubscribeBadClaimTotal();
    String type = request.getParameter("type");
%>
<div class="clear"></div>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="charity_total clearfix">
                <div class="total fl" id="businessTotalMoneyId">
                    累计成交总价值（元） <span class="hover_tips">
                        <div class="hover_tips_con" style="margin-left:-85px;">
                            <div class="arrow"></div>
                            <div class="border" style="width:145px;">累计购买的不良债权原价值</div>
                        </div>
                    </span>
                    <br/> <span class="orange f22"><%=sbcTotal.totalCreditPrice %></span>
                </div>
                <div class="line"></div>
                <div class="total fl" id="buyTotalMoneyId">
                    累计认购价格（元）<br/> <span class="orange f22"><%=sbcTotal.totalSubscribePrice %></span>
                </div>
                <div class="line"></div>
                <div class="total fl" id="successBuyCountId">
                    成功认购笔数（笔）<br/> <span class="orange f22"><%=sbcTotal.subscribeCount %></span>
                </div>
            </div>
            <div class="user_mod border_t15">
                <div id="divAppendId" class="user_tab clearfix">
                    <ul>
                        <li id="krgzqLiId" class="hover" onclick="canBuyBadClaimPaging();">可认购的债权<i></i></li>
                        <li id="yrgzqLiId" onclick="alreadyBuyBadClaim();return false">已认购的债权<i></i></li>
                    </ul>
                </div>
                <div  class="user_table  pt5 " >
                
                    <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr class="til f12">
                        	<td align="center">序号</td>
                            <td align="center">借款标题</td>
                            <td align="center">借款金额（元）</td>
                            <td align="center">剩余期数</td>
                            <td align="center">逾期天数（天）</td>
                            <td align="center">债权价值（元）</td>
                            <td align="center">认购价格（元）</td>
                            <td align="center">操作</td>
                        </tr>
                        </thead>
                        <tr>
                            <td colspan="11" align="center">没有记录</td>
                        </tr>
                    </table>
                </div>
                <div class="page" id="pageContent"></div>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="popup_bg" style="display:none;"></div>
<div id="hiddenDivListId"></div>
<div id="info"></div>
<div id="dialogHmd" style="display: none;">
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDivHmd()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <span class="f20 gray33">您的账号已被拉入黑名单，不能进行安全认证。如有疑问，请联系客服！</span>
                </div>
            </div>
            <div class="tc mt20"><a href="javascript:void(0);" class="btn01" onclick="closeDivHmd()">确定</a></div>
        </div>
    </div>
</div>

<div id="dialogPay" style="display: none;">
	<form method="post" class="form1" id="form1" action="<%=configureProvider.format(URLVariable.PAYBADCLAIM_URL)%>" onkeydown="if(event.keyCode==13){return false;}">
    <div id="tokenStr"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDivPay()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <span class="f20 gray33" id="content"></span>
                    <%if (isOpenPwd) { %>
	                <div class="mt20">
	                    <span class="red">*</span>交易密码:&nbsp;&nbsp;
	                    	<input type="password" class="required text_style" id="tran_pwd" autocomplete="off"/>
	                    	<input type="hidden" name="tranPwd" id="tranPwd" />
	                    <div style="margin-left:77px">
	                    <span id="errorSpan" class="red" style="display: none"></span>
	                    </div>
	                </div>
	                <%} %>
                    <%
                        String isCheckAgree="";
                        TermManage termManage = serviceSession.getService(TermManage.class);
                        T5017 term = termManage.get(TermType.BLZQZRXY);
                        if(term != null){
                            isCheckAgree= "true";
                    %>
	                <div class="tc mt10">
	                    <input name="iAgree" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
	                    <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.BLZQZRXY.name())%>" class="highlight">《<%=TermType.BLZQZRXY.getName() %>》</a>
               		</div>
                    <%}%>
                </div>
            </div>
            <div class="tc mt10">
                <%if("true".equals(isCheckAgree)){%>
                <a href="javascript:void(0);" class="btn01 btn_gray btn_disabled sub-btn" >确定</a>
                <%}else{%>
                <a href="javascript:void(0);" id="ok" class="btn01 sub-btn" >确定</a>
                <%}%>
                <a href="javascript:closeDivPay();"  class="cancel btn01 btn_gray ml20">取消</a></div>
            <input type="hidden" id="blzqId" name="blzqId" value=""/>
        </div>
    </div>
    </form>
</div>
<input type="hidden" id="ajaxUrl" value="<%=controller.getURI(request, Blzqzr.class)%>"/>
<input type="hidden" id="htUrl" value="<%configureProvider.format(out, URLVariable.USER_BLZQZR_URL); %>"/>
<input type="hidden" id="bdxqUrl" value="/financing/sbtz/bdxq/"/>
<input type="hidden" id="checkUrl" value="<%=controller.getURI(request, CheckUser.class)%>"/>
<input type="hidden" name="isTG" id="isTG" value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd" value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/fxbyj/blzqzr.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/zankai.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
	var _checkTxPwdUrl = '<%configureProvider.format(out,URLVariable.CHECK_TXPWD);%>';
	var isCheckAgree = "<%=isCheckAgree%>";
	var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	var key = RSAUtils.getKeyPair(exponent, '', modulus);
	$(function(){
		<%if("2".equals(type)){%>
			alreadyBuyBadClaim();
		<%}else{%>
			canBuyBadClaimPaging();
		<%}%>
	})
</script>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "successful"));
    $(".popup_bg").show();
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
    $(".popup_bg").show();
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
    $(".popup_bg").show();
</script>
<%
    }
%>
</body>
</html>