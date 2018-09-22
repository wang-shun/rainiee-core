<%@ page import="com.dimeng.p2p.user.servlets.financing.donation.DonationList" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <title>我的公益标-<%configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "GYGL";
    CURRENT_SUB_CATEGORY = "DONATION";
%>
<body>
<!--顶部条-->
<%@include file="/WEB-INF/include/header.jsp" %>
<!--头部-->
<form action="<%=controller.getURI(request, DonationList.class)%>" method="post">
    <!--B 主题内容-->
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <!--左菜单-->
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <!--左菜单-END-->
            <div class="r_main">
                <div class="charity_total clearfix">
                    <div class="total fl">
                        <p class="total fl">公益捐赠总额(元)<br/>
                            <span class="orange f22" id="donationsAmount"></span>
                        </p>
                    </div>
                    <div class="line"></div>
                    <div class="list">
                        <ul>
                            <li>捐赠的公益标<br><span id="donationsNum"></span></li>
                        </ul>
                    </div>
                </div>
                <input type="hidden" value="<%=controller.getURI(request, DonationList.class)%>" id="donationUrl"/>
                <input type="hidden" value="<%configureProvider.format(out,URLVariable.GYB_BDXQ);%>" id="grbBdxqUrl"/>
                <input type="hidden"
                       value="<%=controller.getURI(request, com.dimeng.p2p.user.servlets.financing.agreement.Index.class) %>"
                       id="jzxyUrl"/>

                <div class="user_mod border_t15">
                    <div class="user_table">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">公益标题</td>
                                <td align="center">捐赠金额(元)</td>
                                <td align="center">捐赠时间</td>
                                <td align="center">操作</td>
                                <td align="center"></td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/donation/donation.js"></script>
<script type="text/JavaScript">
    $(function () {
        donationPaging();
    });
    
    function toAjaxPage(){
    	donationPaging();
    }
</script>
</body>
</html>
