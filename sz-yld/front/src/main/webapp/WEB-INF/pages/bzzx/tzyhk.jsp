<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page
        import="com.dimeng.p2p.modules.base.front.service.entity.QuestionRecord" %>
<%@page
        import="com.dimeng.p2p.modules.base.front.service.entity.QuestionTypeRecord" %>
<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>投资与回款 帮助中心</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background: url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <div class="about_tab">
        <%
            CURRENT_CATEGORY = "BZZX";
            CURRENT_SUB_CATEGORY = "TZYHK";
            ArticleManage manage = serviceSession.getService(ArticleManage.class);
            QuestionTypeRecord[] qtrs = manage.getQuestionTypes(T5011_F02.TZYHK);
        %>
        <%@include file="/WEB-INF/include/bzzx/menu.jsp" %>
    </div>
    <div class="about_content">
    	<%if (qtrs != null && qtrs.length > 0) { %>
        <div class="problem_type_tab">
            <ul class="clearfix">
                <%
                        int index = 0;
                        for (QuestionTypeRecord qtr : qtrs) {
                            index++;
                %>
                <li id="type<%=index%>" <%if (index == 1) {%> class="hover" <%} %>
                    onclick="setTab('type',<%=index%>,<%=qtrs.length%>)"><img
                        src="<%=fileStore.getURL(qtr.imageCode)%>"/>

                    <p title="<%=qtr.questionType%>">
                        <%StringHelper.filterHTML(out, qtr.questionType);%>
                    </p></li>
                <%
                        }
                %>
            </ul>
        </div>
        <%} %>
        <%
            if (qtrs != null && qtrs.length > 0) {
                int index = 0;
                for (QuestionTypeRecord qtr : qtrs) {
                    index++;
                    QuestionRecord qrs[] = manage.getQuestions(T5011_F02.TZYHK, qtr.id);
        %>
        <div class="problem_type_con" id="con_type_<%=index%>"
                <%if (index != 1) { %> style="display: none" <%} %>>
            <dl>
                <%
                    if (qrs != null && qrs.length > 0) {
                        for (QuestionRecord qr : qrs) {
                %>
                <dt>
                    <i class="ico"></i> <i class="click"></i>
                    <%StringHelper.filterHTML(out, qr.question);%>
                </dt>
                <dd>
                    <i class="ico"></i>
                    <%StringHelper.filterHTML(out, qr.questionAnswer);%>
                </dd>
                <%
                        }
                    }
                %>
            </dl>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/front.js"></script>
</body>
</html>