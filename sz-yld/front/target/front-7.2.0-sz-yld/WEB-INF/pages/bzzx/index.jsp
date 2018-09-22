<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5010" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5010_F04" %>
<%@ page import="com.dimeng.p2p.front.servlets.bzzx.Index" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>投资攻略</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background: url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <%	ArticleManage artManage = serviceSession.getService(ArticleManage.class);
        T5010[] tzgls = artManage.getArticleCategoryAll(4, T5010_F04.QY);
        String articleType = request.getParameter("type");
    %>
    <div class="about_tab">
        <div class="w1002 clearfix">
            <% if(tzgls != null && tzgls.length > 0){
                int i=1;
                boolean falg = false;
                for(T5010 t5010 : tzgls ){
                    if(t5010.F02.equals(articleType)){
                        falg = true;
                    }
            %>
            <a href="javascript:void(0);" class="<%=t5010.F02%>" onclick="setTab('tab_',<%=i%>,<%=tzgls.length%>);initTzglData('<%=t5010.F02%>');" id="tab_<%=i%>"><%=t5010.F03 %></a>
            <%
                   i++;
                }
                    if(!falg){
                        //如果要访问的资讯不存在或被停用了，则默认展示第一条
                        articleType = tzgls[0].F02;
                    }
                }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);return;}%>
        </div>
    </div>
    <div class="about_content clearfix" id="content_id">
    </div>
    <div id="pageContent" style="padding: 10px 0;width: 1002px;margin:0 auto;">
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    var _tzglUrl = '<%=controller.getURI(request, Index.class)%>';
    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 0;
    $(function () {
        initTzglData("<%=articleType%>");
    });
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/cxgl/gywm/index.js"></script>
</body>
</html>