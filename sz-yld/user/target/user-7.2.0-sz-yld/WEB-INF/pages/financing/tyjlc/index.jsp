<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>体验金理财-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "TYJLC";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <%@include file="/WEB-INF/include/tyjlc/header.jsp" %>
        <div class="w780 fr">
            <div>
                <div class="newsbox">
                    <div class="til clearfix Men_bt">
                        <%-- <a href="<%=controller.getURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.TyjBackAccount.class) %>" class="blue fr mt5">回账查询</a> --%>
                        <div class="Menubox">
                            <ul>
                                <li id="SQZ" class="hover" onclick="initData('SQZ')">申请中的体验金投资</li>
                                <li id="CYZ" onclick="initData('CYZ')">持有中的体验金投资</li>
                                <li id="YJZ" onclick="initData('YJZ')" style="border-right:1px #d7dfe3 solid;">
                                    已截止的体验金投资
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="bs_bg pt10">
                        <div class="no_table user_bolr bot">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
                                <thead id="dataHead">

                                </thead>
                                <tbody id="dataBody">

                                </tbody>

                            </table>
                        </div>
                        <div id="pageContent">

                        </div>

                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>


<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/tyjlc/tyjlc.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var url = "<%=controller.getURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.Index.class)%>";
</script>
</body>
</html>