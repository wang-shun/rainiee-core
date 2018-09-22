<%@page import="com.dimeng.p2p.S50.entities.T5015" %>
<%@page import="com.dimeng.p2p.S50.enums.T5015_F02" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.NoticeManage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>网站公告 关于我们</title>

    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background:url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <div class="about_tab">
        <%
            CURRENT_CATEGORY = "ZXDT";
            CURRENT_SUB_CATEGORY = "WZGG";
        %>
        <%@include file="/WEB-INF/include/mtbd/menu.jsp" %>
    </div>
    <div class="about_content">
        <div class="news_list">
            <ul>
                <%
                    final int currentPage = IntegerParser.parse(request
                            .getParameter("paging.current"));
                    NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
                    PagingResult<T5015> results = noticeManage.search(
                            new Paging() {
                                public int getCurrentPage() {
                                    return currentPage;
                                }

                                public int getSize() {
                                    return 10;
                                }
                            });
                    T5015[] notices = results.getItems();
                    if (notices != null && notices.length > 0) {
                        int index = 1;
                        for (T5015 notice : notices) {
                %>
                <li>
                    <span class="fr gray9"><%=Formater.formatDate(notice.F09) %></span>
                    <a target="_blank"
                       href="<%=controller.getPagingItemURI(request, Wzgg.class,notice.F01)%>">
							<span title="<%StringHelper.filterHTML(out, notice.F05); %>"> 
							【<% StringHelper.filterHTML(out, notice.F02.getChineseName());%>】<%
                                StringHelper.filterHTML(out, StringHelper.truncation(notice.F05, 20));%>
							</span>
                    </a>
                </li>
                <%
                        }
                    }
                %>
            </ul>
            <%Wzgg.rendPaging(out, results, controller.getPagingURI(request, Wzgg.class)); %>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>