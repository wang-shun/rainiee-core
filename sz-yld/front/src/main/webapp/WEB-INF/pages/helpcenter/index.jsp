<%@ page import="com.dimeng.p2p.front.servlets.helpcenter.Index" %>
<%@ page import="com.dimeng.p2p.common.enums.ArticleType" %>
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
        String articleType = StringHelper.isEmpty(request.getParameter("type"))?"CZYTX":request.getParameter("type");
    %>
    <div class="about_tab">
        <div class="w1002 clearfix">
            <a href="javascript:void(0);" class="CZYTX" onclick="setTab('tab_',1,4);initHelpData('CZYTX');" id="tab_1"><%=ArticleType.CZYTX.getName() %></a>
            <a href="javascript:void(0);" class="TZYHK" onclick="setTab('tab_',2,4);initHelpData('TZYHK');" id="tab_2"><%=ArticleType.TZYHK.getName() %></a>
            <a href="javascript:void(0);" class="ZHYAQ" onclick="setTab('tab_',3,4);initHelpData('ZHYAQ');" id="tab_3"><%=ArticleType.ZHYAQ.getName() %></a>
            <a href="javascript:void(0);" class="ZCYDL" onclick="setTab('tab_',4,4);initHelpData('ZCYDL');" id="tab_4"><%=ArticleType.ZCYDL.getName() %></a>
        </div>
    </div>
   <div class="about_content clearfix" id="content_id">

   </div>
</div>

<script id="qtype_tmpl" type="text/x-jquery-tmpl">
     <div class="problem_type_tab">
        <ul class="clearfix">
            {{each(i,qtype) questionObj}}
                 <li id="type{{= i+1}}" class="qtype_li {{if i==0}} hover{{/if}}"  index="{{= i+1}}">
                    <img src="{{= qtype.imageCode}}"/>
                    <p title="{{= qtype.questionType}}"> {{= qtype.questionType}}</p>
                 </li>
            {{/each}}
        </ul>
     </div>

      {{each(i,qtype) questionObj}}
        <div class="problem_type_con" id="con_type_{{= i+1}}" {{if i != 0}} style="display: none" {{/if}}>
            <dl>
                {{each(i,qObj) qtype.questionRecord}}
                    <dt>
                        <i class="ico"></i> <i class="click"></i>
                        <a href="javascript:void(0);">{{= qObj.question}}</a>
                    </dt>
                    <dd>
                        <i class="ico"></i>
                        {{html qObj.questionAnswer}}
                    </dd>
                {{/each}}
            </dl>
        </div>
      {{/each}}
</script>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/cxgl/gywm/index.js"></script>
<script type="text/javascript">
    var _helpUrl = '<%=controller.getURI(request, Index.class)%>';
    $(function () {
        initHelpData("<%=articleType%>");
    });

</script>


<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>