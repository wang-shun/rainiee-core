<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.framework.http.entity.RoleBean" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.AddSysUser" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.UpdateSysUser" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.UpdateSysUserPwd" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.SysUser" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.SysUserList" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.ResetConsoleLoginError" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "GLYGL";
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    PagingResult<SysUser> result = (PagingResult<SysUser>) request.getAttribute("result");
    RoleBean[] roleBeans = ObjectHelper.convertArray(request.getAttribute("roles"), RoleBean.class);
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form action="<%=controller.getURI(request, SysUserList.class)%>"
                      method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>管理员管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">用户名：</span> <input type="text" name="accountName"
                                                                                       value="<%StringHelper.filterQuoter(out,request.getParameter("accountName"));%>"
                                                                                       id="textfield"
                                                                                       class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">姓名：</span> <input type="text" name="name"
                                                                                       value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>"
                                                                                       id="textfield7"
                                                                                       class="text border pl5 mr20"/>
                                    </li>
                                    <%if(is_business){ %>
					                <li><span class="display-ib mr5">工号：</span>
					                  	<input type="text" name="employNum" value="<%StringHelper.filterHTML(out, request.getParameter("employNum"));%>" class="text border pl5 mr20" />
					                </li>
					                <%} %>
                                    <li>
                                        <span class="display-ib mr5">最后登录时间：</span> <input type="text"
                                                                                           name="createTimeStart"
                                                                                           id="datepicker1"
                                                                                           readonly="readonly"
                                                                                           class="text border pl5 w120 date"
                                                                                           value="<%StringHelper.filterHTML(out,request.getParameter("createTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="createTimeEnd" id="datepicker2"
                                               readonly="readonly" class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("createTimeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">用户组：</span> <select name="roleId"
                                                                                         class="border mr20 h32 mw100">
                                        <option value="0">--全部--</option>
                                        <%
                                            if (roleBeans != null) {
                                                for (RoleBean roleBean : roleBeans) {
                                                    if (roleBean == null) {
                                                        continue;
                                                    }
                                        %>
                                        <option value="<%=roleBean.getRoleId()%>"
                                                <%if (roleBean.getRoleId() == IntegerParser.parse(request.getParameter("roleId"))) {%>
                                                selected="selected" <%}%>>
                                            <%
                                                StringHelper.filterHTML(out, roleBean.getName());
                                            %>
                                        </option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">用户状态：</span> <select name="status"
                                                                                          class="border mr20 h32 mw100">
                                        <option value="">--全部--</option>
                                        <%
                                            for (SysAccountStatus status : SysAccountStatus.values()) {
                                        %>
                                        <option value="<%=status.name() %>"
                                                <%if (status.name().equals(request.getParameter("status"))) {%>
                                                selected="selected" <%}%>><%=status.getName() %>
                                        </option>
                                        <%} %>
                                    </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">所属部门：</span> <input type="text" name="dept"
                                                                                       value="<%StringHelper.filterQuoter(out, request.getParameter("dept"));%>"
                                                                                       id="textfield8"
                                                                                       class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                        <%
                                            if (dimengSession.isAccessableResource(AddSysUser.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, AddSysUser.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号</th>
                                    <th>用户名</th>
                                    <th>姓名</th>
                                    <th>用户组</th>
                                    <th>工号</th>
                                    <th>手机号码</th>
                                    <th>职称</th>
                                    <th>所属部门</th>
                                    <th>用户状态</th>
                                    <th>创建时间</th>
                                    <th>最后登录时间</th>
                                    <th>最后登录IP</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    SysUser[] sysUsers = result.getItems();
                                    if (sysUsers != null && sysUsers.length != 0) {
                                        int i = 1;
                                        for (SysUser sysUser : sysUsers) {
                                            if (sysUser == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td class="tc"><%=i++%>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, sysUser.accountName);
                                        %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, sysUser.name);
                                        %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, sysUser.roleName); %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, sysUser.employNum); %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, sysUser.phone); %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, sysUser.pos); %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, sysUser.dept); %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, sysUser.status.getName());
                                        %>
                                    </td>
                                    <td><%=DateTimeParser.format(sysUser.createTime)%>
                                    </td>
                                    <td><%=DateTimeParser.format(sysUser.lastTime)%>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, sysUser.lastIp);
                                        %>
                                    </td>
                                    <td>
                                    	<span> 
                                    	<% if (dimengSession.isAccessableResource(UpdateSysUser.class)) {%>
                                    		<a href="<%=controller.getURI(request,UpdateSysUser.class)%>?id=<%=sysUser.id%>"
                                            class="link-blue mr20">信息修改</a>
										<%} else { %> 
											<span class="disabled mr20">信息修改</span>
										<%} %>
										</span> 
										<span>
										<%if (dimengSession.isAccessableResource(UpdateSysUserPwd.class)) {%>
											<span class="blue">
												<a href="<%=controller.getURI(request,UpdateSysUserPwd.class)%>?id=<%=sysUser.id%>" class="link-blue mr20">密码修改</a>
											</span> 
										<%} else { %>
                                        	<span class="disabled mr20">密码修改</span> 
                                        <%} %>
										</span>
										<span>
										<%if (dimengSession.isAccessableResource(ResetConsoleLoginError.class)) {%>
											<span class="blue">
												<a href="javascript:void(0)" onclick="resetLoginError('<%=sysUser.accountName%>');" class="link-orangered ">登录错误数重置</a>
											</span> 
										<%} else { %>
                                        	<span class="disabled">登录错误数重置</span> 
                                        <%} %>
                                        </span>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title">
                                    <td colspan="13" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </div>
                </form>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function resetLoginError(userName) {
        $.ajax({
            type: "post",
            url: "<%=controller.getURI(request, ResetConsoleLoginError.class)%>",
            data: {"userName": userName},
            dataType: "text",
            success: function (returnData) {
                $("#info").html(showDialogInfo(returnData, "perfect"));
                $("div.popup_bg").show();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>