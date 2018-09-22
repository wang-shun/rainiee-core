<%@page import="com.dimeng.p2p.console.servlets.info.gywm.zjgw.SearchZjgw"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.console.servlets.info.gywm.zjgw.UpdateZjgw" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "ZJGW";
    ArticleRecord record = ObjectHelper.convert(request.getAttribute("record"), ArticleRecord.class);
    String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改<%=xcglmanage.getCategoryNameByCode("ZJGW") %>
                        </div>

                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form name="example" method="post"
                                  action="<%=controller.getURI(request, UpdateZjgw.class)%>" class="form1"
                                  enctype="multipart/form-data">
                                <input type="hidden" value="<%=record.id %>" name="id"/>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>姓名：</span>
                                        <input type="text" class="text border w300 pl5 required max-length-15"
                                               name="title" value="<%StringHelper.filterHTML(out, record.title);%>"/>
                                        <span tip>最大15个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>相片：</span>
                                        <img src="<%=fileStore.getURL(record.imageCode) %>" width="277" height="89"/>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="file" class="text border w300 pl5" name="image"/>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>个人简介：</span>
                                        <textarea name="content" class="w400 h120 border p5 required max-length-140"
                                                  cols="100" rows="8"
                                                  style="width:700px;height:200px;"><%StringHelper.format(out, content, fileStore); %></textarea>
                                        <br/>
                                        <span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <span tip>最大140个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>排序值：</span>
                                        <input type="text" class="text border w300 pl5 required isint max-length-10"
                                               name="sortindex" value="<%=record.sortIndex %>"/>

                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>

                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchZjgw.class) %>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml20" value="取消">
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>