<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.BackMoney" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.Hszdzq" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.Tbzdzq" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.Yjqdzq" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Transfer" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的债权-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    String isTbzzq = request.getParameter("isTbzzq");
    if (StringHelper.isEmpty(isTbzzq)) {
        isTbzzq = "0";
    }
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "WDZQ";
    Boolean isBadClaimTransfer = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <!--左菜单-->
        <!--右边内容-->
        <div class="r_main">
            <%@include file="/WEB-INF/include/wdzq/header.jsp" %>
            <div class="user_mod border_t15">
                <div class="user_tab clearfix">
                    <ul>
                        <li id="liId1" value="1" class="hover" onclick="initPage('hsz')">回款中<i></i></li>
                        <li id="liId2" value="2" onclick="initPage('yjq')">已结清<i></i></li>
                        <li id="liId3" value="3" onclick="initPage('tbz')">投资中<i></i></li>
                    </ul>
                </div>
                <div id="con_one_1" class="user_table mt5 pr">
                    <div style="position:absolute; top:-45px; right:0px;">
                    	<a id="hzcxId" href="<%=controller.getViewURI(request, BackMoney.class)%>" class="highlight">-回账查询-</a>
                    </div>
                    <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td align="center">债权ID</td>
                            <td align="center">原始投资金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">待收本息(元)</td>
                            <td align="center">期数</td>
                            <td align="center">下个还款日</td>
                            <td align="center">状态</td>
                        </tr>
                    </table>
                </div>
                <div class="page" id="pageContent"></div>
            </div>
        </div>
        <!--右边内容-->
    </div>
</div>
<!--主体内容-->

<form action="<%=controller.getURI(request, Transfer.class)%>" method="post" class="form1">
    <input type="hidden" name="jkbId" id="jkbId" value="">
    <input type="hidden" name="zqValue" id="zqValue" value="">
    <input type="hidden" name="zkNum" id="zkNum" value="">
    <input type="hidden" id="hszdzqUrl" value="<%=controller.getURI(request, Hszdzq.class)%>"/>
    <input type="hidden" id="yjqdzqUrl" value="<%=controller.getURI(request, Yjqdzq.class)%>"/>
    <input type="hidden" id="tbzdzqUrl" value="<%=controller.getURI(request, Tbzdzq.class)%>"/>
    <input type="hidden" id="financingUrl" value="<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ); %>"/>
    <input type="hidden" id="viesSuffixUrl" value="<%=rewriter.getViewSuffix()%>"/>
    <input type="hidden" id="htIndexUrl" value="<%configureProvider.format(out, URLVariable.XY_PTDZXY); %>"/>
    <input type="hidden" id="userBlzqzrUrl" value="<%configureProvider.format(out, URLVariable.USER_BLZQZR_URL); %>"/>
    <input type="hidden" id="userZqzrUrl" value="<%configureProvider.format(out, URLVariable.USER_ZQZR); %>"/>
    <input type="hidden" name="isTbzzq" id="isTbzzq" value="<%=isTbzzq%>">
    <input type="hidden" name="isBadClaimTransfer" id="isBadClaimTransfer" value="<%=isBadClaimTransfer%>">
	<div id="info"></div>
    <div class="popup_bg" style="display: none;"></div>
    <div class="dialog d_error w380 thickpos" style="margin:-150px 0 0 -190px;display: none;">
        <div class="dialog_close fr"><a href="#"></a></div>
        <div class="con clearfix">
            <div class="clearfix">
                <table width="100%" border="0" cellspacing="0">
                    <tr>
                        <td width="24%">债权ID:</td>
                        <td width="76%"><span id="zqId"></span></td>
                    </tr>
                    <tr>
                        <td>债权价值：</td>
                        <td><span id="zqjz"></span>元/份</td>
                    </tr>
                    <tr>
                        <td>转让费率：</td>
                        <td><%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE)) * 100 %>
                            %
                        </td>
                    </tr>
                    <tr>
                        <td>转让价格：</td>
                        <td><input type="text" name="zrValue" id="zrValue"/><span class="ml5">元</span>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="clear"></div>
            <div class="dialog_btn">
                <input type="submit" class="btn btn001 sumbitForme" fromname="form1" style="cursor: pointer;"
                       value="转让">
                <input type="button" id="cancel" class="btn btn05" style="cursor: pointer;" value="取消">
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqkzc.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/wdzq.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/ajaxDateUtil.js"></script>
<script type="text/javascript">
	var loginUrl="<%=controller.getURI(request,Login.class)%>";
    $(document).ready(function () {
        //启动时进行ajax查询
        var isTbzzq = $("#isTbzzq").val();
        if (isTbzzq == 1) {
            tbzdzq();
        } else {
            hszdzq();
        }
    });
    
    function toAjaxPage(){
    	if($("#liId1").hasClass("hover")){
    		hszdzq();
    	}else if($("#liId2").hasClass("hover")){
    		yjqdzq();
    	}else if($("#liId3").hasClass("hover")){
    		tbzdzq();
    	}
    }
</script>
</body>
</html>