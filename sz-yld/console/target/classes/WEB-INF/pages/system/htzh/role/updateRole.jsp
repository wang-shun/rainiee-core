<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.RoleList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.UpdateRole" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.Role" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    Role roleBean = ObjectHelper.convert(request.getAttribute("role"), Role.class);
%>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "YHZGL";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改用户组</div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, UpdateRole.class)%>" method="post"
                                  class="form1">
                                <input name="roleId" type="hidden" id="roleId" value="<%=roleBean.roleId%>"/>
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户组名称：
											</span>
                                        <input name="name" type="text" class="text border w300 pl5 required"
                                               maxlength="15" value="<%=roleBean.roleName%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5 fl">
												用户组描述：
											</span>
                                        <textarea name="des" id="des" cols="45" rows="5"
                                                  class="w400 h120 border p5 max-length-200"
                                                  maxlength="200"><%StringHelper.filterHTML(out, roleBean.desc);%></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <%
                                                if (dimengSession.isAccessableResource(UpdateRole.class)) {
                                            %>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="确认"/>
                                            <%} else {%>
                                            <input type="button" class="disabled" value="确认"/>
                                            <%} %>
                                            <input type="button"
                                                   onclick="location.href='<%=controller.getURI(request, RoleList.class) %>'"
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
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>