<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.constant.ConstantLogList"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/kindeditor.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    com.dimeng.p2p.modules.systematic.console.service.entity.Constant constant = ObjectHelper.convert(
            request.getAttribute("result"), com.dimeng.p2p.modules.systematic.console.service.entity.Constant.class);
    if (constant == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "PTCLRZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台常量日志
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <ul class="gray6">
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											常量名：
										</span>
                                    <%
                                        StringHelper.filterHTML(out, constant.desc);
                                    %>
                                </li>
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											KEY值：
										</span>
                                    <%
                                        StringHelper.filterHTML(out, constant.key);
                                    %>
                                </li>
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											修改前的值：
										</span>
                                    <%
                                        StringHelper.filterHTML(out, constant.value1);
                                    %>
                                </li>
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											修改后的值：
										</span>
                                    <%
                                        StringHelper.filterHTML(out, constant.value2);
                                    %>
                                </li>
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											修改人：
										</span>
                                    <label>
                                        <%
                                            StringHelper.filterHTML(out, constant.name);
                                        %>
                                    </label>
                                </li>
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											修改时间：
										</span>
                                    <%=DateTimeParser.format(constant.updateTime)%>
                                </li>
                                <li>
                                    <div class="pl200 ml5">
                                        <input type="button"
                                               onclick="location.href='<%=controller.getURI(request, ConstantLogList.class)%>'"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
</body>
</html>