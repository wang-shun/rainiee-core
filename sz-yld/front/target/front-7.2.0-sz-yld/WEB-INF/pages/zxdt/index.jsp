<%@ page import="com.dimeng.p2p.front.servlets.zxdt.Index" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5010" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5010_F04" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>帮助中心</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/cxgl/gywm/jquery.tmpl.min.js"></script>
<div class="about_white_bg">
    <div class="front_banner"
         style="background: url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <%
        String articleType = request.getParameter("type");
        ArticleManage artManage = serviceSession.getService(ArticleManage.class);
        T5010[] zxdts = artManage.getArticleCategoryAll(3, T5010_F04.QY);
    %>
    <div class="about_tab">
        <div class="w1002 clearfix">
            <a href="javascript:void(0);" class="WZGG" onclick="setTab('tab_',1,<%=zxdts.length+1%>);initZxdtData('WZGG');" id="tab_1"><i></i>网站公告</a>
            <% if(zxdts != null && zxdts.length > 0){
                int i=2;
                boolean falg = false;
                for(T5010 t5010 : zxdts ){
                    if(t5010.F02.equals(articleType) || "WZGG".equals(articleType)){
                        falg = true;
                    }
            %>
            <a href="javascript:void(0);" class="<%=t5010.F02%>" onclick="setTab('tab_',<%=i%>,<%=zxdts.length+1%>);initZxdtData('<%=t5010.F02%>');" id="tab_<%=i%>"><%=t5010.F03 %></a>
            <%
                        i++;
                    }
                    if(!falg){
                        //如果要访问的资讯不存在或被停用了，则默认展示第一条
                        articleType = "WZGG";
                    }
                }
            %>
        </div>
    </div>
    <div class="about_content clearfix" id="content_id">

    </div>
    <div id="pageContent" style="padding: 10px 0;width: 1002px;margin:0 auto;">
    </div>
</div>

<script id="tmpl_one" type="text/x-jquery-tmpl">
     <div class="safety_list">
            <ul>
            {{each(i,article) artics}}
                <li>
                    <div class="pic">
                        {{if article.F09 != null}}<img src="{{= article.F09}}"/>{{/if}}
                    </div>
                    <div class="name">
                        <a target="_blank" href="/zxdt/mtbd/{{= article.F01}}.html?type={{= type}}">{{= article.F06}}</a>
                    </div>
                    <div class="label">{{= article.F07}}<span
                            class="date">{{= article.lastUPdateTime}}</span></div>
                    <div class="introduce">
                       {{= article.F08}}
                    </div>
                    <div class="details">
                        <a target="_blank"  href="/zxdt/mtbd/{{= article.F01}}.html?type={{= type}}">查看详情</a>
                    </div>
                </li>
            {{/each}}
        </ul>
     </div>
</script>

<script id="tmpl_two" type="text/x-jquery-tmpl">
      <div class="news_list">
            <ul>
            {{each(i,article) artics}}
                <li>
                    <span class="fr gray9">{{= article.lastUPdateTime}}</span>
                    <a target="_blank" href="/zxdt/wzgg/{{= article.F01}}.html">
                        <span title="{{= article.F05}}">
                            【{{= article.t5015_F02}}】{{= article.F05}}
                        </span>
                    </a>
                </li>
            {{/each}}
        </ul>
     </div>
</script>

<script id="tmpl_thr" type="text/x-jquery-tmpl">
      <div class="news_list">
            <ul>
            {{each(i,article) artics}}
                <li>
                    <span class="fr gray9">{{= article.lastUPdateTime}}</span>
                    <a target="_blank" href="/zxdt/mtbd/{{= article.F01}}.html?type={{= type}}">
                        <span title="{{= article.F06}}">
                            {{= article.F06}}
                        </span>
                    </a>
                </li>
            {{/each}}
        </ul>
     </div>
</script>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/cxgl/gywm/index.js"></script>
<script type="text/javascript">
    var _zxdtUrl = '<%=controller.getURI(request, Index.class)%>';
    var currentPage = 1;
    var pageSize = 5;
    var pageCount = 0;
    $(function () {
        initZxdtData("<%=articleType%>");
    });

</script>


<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>