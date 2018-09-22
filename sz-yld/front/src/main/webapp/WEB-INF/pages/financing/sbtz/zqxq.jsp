<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Annex"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.BdxqData"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.CheckPsd"%>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>线上债权转让详情-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/js/highslide.css"/>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
	<%@include file="/WEB-INF/include/sbtz/zqzrHeader.jsp" %>
<%
  	//是否进行认证
    boolean checkFlag=true;
    String checkMessage = "";
    String rzUrl = "";
    if(dimengSession !=null && dimengSession.isAuthenticated()){
        com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
        Map<String, String> retMap = userCommManage.checkAccountInfo();
        checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
        checkMessage = retMap.get("checkMessage");
        rzUrl = retMap.get("rzUrl");
    }
    %>
    <div class="item_details_con w1002">
        <div class="main_tab">
            <ul class="clearfix">
                <li id="bdxq" class="hover">标的详情</li>
                <%
                    if (creditInfo.F11 == T6230_F11.S) {
                %>
                <li id="fkxx">风控信息</li>
                <%
                    }
                %>
                <%
                    if (creditInfo.F13 == T6230_F13.S) {
                %>
                <li id="dywxx">抵押物信息</li>
                <%
                    }
                %>
                <%if (isXgwj) {%>
                <li id="xgwj">相关文件</li>
                <%} %>
                <li id="tzjl">投资记录</li>
                <%if (T6230_F20.HKZ == creditInfo.F20 || creditInfo.F20 == T6230_F20.YJQ || creditInfo.F20 == T6230_F20.YDF) { %>
                <li id="hkjh">还款计划</li>
                <%} %>
                <%if (T6230_F20.HKZ == creditInfo.F20 || creditInfo.F20 == T6230_F20.YJQ || creditInfo.F20 == T6230_F20.YDF) { %>
                	<li id="xmdt">项目动态</li>
                <% }%>
                
            </ul>
        </div>
        <div id="dataHtml" class="con details">

        </div>

    </div>

</div>
<input type="hidden" name="isTG" id="isTG"
       value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd"
       value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>

<div id="info"></div>
<script type="text/javascript">
    var _loginUrl = '<%configureProvider.format(out,URLVariable.LOGIN);%>';
	var _checkTxPwdUrl = '<%configureProvider.format(out,URLVariable.CHECK_TXPWD);%>';
    var checkUrl = "<%=controller.getURI(request, CheckPsd.class)%>";
    var accountId = '<%=dimengSession ==null || !dimengSession.isAuthenticated()?"":serviceSession.getSession().getAccountId()%>';
    var _dataUrl = "<%=controller.getURI(request, BdxqData.class)%>";
    var _bid = "<%=zqInfo.bidId%>";
    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 0;
    var attUrl = "<%=controller.getURI(request, Annex.class)%>";
    var isZrr = false;
    <%
    if(t6110.F06 == T6110_F06.FZRR){ %>
    isZrr = false;
    <%
    }else{
    %>
    isZrr = true;
    <%}%>

</script>

<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/jquery-ui.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzr.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/sbtz/bdxq.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "successful", $("#zqSucc").val()));
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highslide-with-gallery.js"></script>
<script type="text/javascript">
	<%-- var haveTXPW = '<%=haveTXPW%>';
	var havaSMRZ = '<%=havaSMRZ%>'; --%>
	var havaRZTG = '<%=checkFlag%>';
	var authText = "<%=checkMessage %>";
	var _rzUrl = "<%=rzUrl%>";
    hs.graphicsDir = '<%=controller.getStaticPath(request)%>/js/graphics/';
    hs.align = 'center';
    hs.transitions = ['expand', 'crossfade'];
    hs.wrapperClassName = 'dark borderless floating-caption';
    hs.fadeInOut = true;
    hs.dimmingOpacity = .75;

    // Add the controlbar
    if (hs.addSlideshow) hs.addSlideshow({
        //slideshowGroup: 'group1',
        interval: 5000,
        repeat: false,
        useControls: true,
        fixedControls: 'fit',
        overlayOptions: {
            opacity: .6,
            position: 'bottom center',
            hideOnMouseOut: true
        }
    });
</script>
</body>
</html>