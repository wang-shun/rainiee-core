<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.UpdateTask" %>
<%@page import="com.dimeng.p2p.S66.entities.T6601" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.TaskList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.AddTask" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DSRWGL";
    T6601 t6601 = ObjectHelper.convert(request.getAttribute("t6601"), T6601.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改定时任务
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, UpdateTask.class)%>" method="post"
                                  class="form1">
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <input type="hidden" name="F01" value="<%=t6601.F01%>"/>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>任务名：</span>
                                        <input name="F02" type="text"
                                               class="text border w300 yw_w5 required max-length-100"
                                               value="<%=t6601.F02%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>执行类：</span>
                                        <input name="F03" type="text"
                                               class="text border w300 yw_w5 required max-length-200"
                                               value="<%=t6601.F03%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>执行方法：</span>
                                        <input name="F04" type="text"
                                               class="text border w300 yw_w5 required max-length-50"
                                               value="<%=t6601.F04%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>执行时间：</span>
                                        <input name="F05" type="text"
                                               class="text border w300 yw_w5 required max-length-50"
                                               value="<%=t6601.F05%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">备注：</span>
                                        <textarea name="F11" cols="" rows=""
                                                  class="w400 h120 border p5 max-length-200"><%StringHelper.format(out, t6601.F11, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="til">&nbsp;</div>
                                        <div class="info">
	                      	<span errortip class="">
	                      		<%
                                    String msg = controller.getPrompt(request, response, PromptLevel.ERROR);
                                    if (msg != null) {
                                %>
	                      		<%StringHelper.filterHTML(out, msg);%>
	                      		<%} %>
	                      	</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   value="提交" fromname="form1"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, TaskList.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>

</html>