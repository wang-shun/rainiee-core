<%@page import="com.dimeng.p2p.S50.enums.T5012_F03" %>
<%@page import="com.dimeng.p2p.S50.entities.T5012" %>
<%@page
        import="com.dimeng.p2p.modules.base.front.service.CustomerServiceManage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>在线客服</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background:url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <div class="about_tab">
        <%
            CURRENT_SUB_CATEGORY = "ZXKF";
        %>
        <%@include file="/WEB-INF/include/bzzx/menu.jsp" %>
    </div>
    <div class="about_content">
        <div class="service_content">
            <ul class="clearfix">
                <%
                    CustomerServiceManage customerServiceManage = serviceSession.getService(CustomerServiceManage.class);
                    T5012[] customerServices = customerServiceManage.getAll(T5012_F03.QQ);
                    if (customerServices != null && customerServices.length > 0) {
                        for (T5012 customerService : customerServices) {
                            String image = fileStore.getURL(customerService.F07);
                %>
                <li>
               		 <a href="http://wpa.qq.com/msgrd?v=3&uin=<%StringHelper.filterHTML(out, customerService.F06);%>&site=qq&menu=yes"
                   		target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                		<img src="<%if (StringHelper.isEmpty(image)){%>
                			<%=controller.getStaticPath(request)%>/images/lead_pic17.jpg
                			<%}else{%><%=image%><%}%>" width="115" height="110"/>
					</a>
                    <p>
                        <a
                                href="http://wpa.qq.com/msgrd?v=3&uin=<%StringHelper.filterHTML(out, customerService.F06);%>&site=qq&menu=yes"
                                target="_blank"
                                title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                            <%
                                StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 3));
                            %>
                        </a>
                    </p></li>
                <%
                        }
                    }
                %>
            </ul>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>