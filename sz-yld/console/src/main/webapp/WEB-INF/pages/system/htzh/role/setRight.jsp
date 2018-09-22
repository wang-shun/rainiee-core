<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.RoleList"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean" %>
<%@page import="com.dimeng.framework.http.entity.RightBean" %>
<%@page import="com.dimeng.framework.config.entity.ModuleBean" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.SetRight" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    ModuleBean[] moduleBeans = ObjectHelper.convertArray(
            request.getAttribute("moduleBeans"), ModuleBean.class);
    String rights = ObjectHelper.convert(request.getAttribute("rights"), String.class);
    RoleBean roleBean = ObjectHelper.convert(request.getAttribute("roleBean"), RoleBean.class);
    int roleId = IntegerParser.parse(request.getAttribute("roleId"));
    if (roleBean == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
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
                        <div class="content-container pl40 pt30 pr40 mb30">
                            <form action="<%=controller.getURI(request, SetRight.class)%>" method="post" id="form1">
                                <input type="hidden" value="<%=roleId%>" name="roleId"/>
                                <ul class="gray6 border-b-s">
                                    <li class="mb10">
                                        <span class="display-ib w100 tr mr5"><em class="red pr5">*</em>用户组名称</span>
                                        <input name="name" type="text" class="text border w300 pl5"
                                               value="<%StringHelper.filterHTML(out, roleBean.getName()); %>"
                                               readonly="readonly"/>
                                    </li>
                                    <li class="mb10 pr">
                                        <span class="display-ib w100 tr mr5 pa left0 top0">用户组描述</span>

                                        <div class="pl100 ml5">
                                            <textarea name="des" id="textarea2" cols="45" rows="5"
                                                      class="w300 p5 h150 border"
                                                      readonly="readonly"><%StringHelper.filterHTML(out, roleBean.getDescription()); %></textarea>
                                        </div>
                                    </li>
                                    <%
                                        int moduleIdx = 0;
                                        int j = 0;
                                    %>
                                    <li class="mb10"><span class="display-ib w100 tr mr5">用户组权限</span>
                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                            <input type="radio" class="mr10" name="_radio<%=moduleIdx %>" id="_<%=j %>"
                                                   onclick="selectAll('_module<%=moduleIdx%>')"/>全选
                                        </label>
                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                            <input type="radio" class="mr10" name="_radio<%=moduleIdx %>" id="_<%=j %>"
                                                   onclick="unselectAll('_module<%=moduleIdx%>')"/>全不选
                                        </label>
                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                            <input type="radio" class="mr10" name="_radio<%=moduleIdx %>" id="_<%=j %>"
                                                   onclick="revertAll('_module<%=moduleIdx%>')"/>反选
                                        </label>
                                    </li>
                                </ul>
                                <div id="_module<%=moduleIdx++%>">
                                    <%
                                        if (moduleBeans != null) {
                                            for (ModuleBean moduleBean : moduleBeans) {
                                                if (moduleBean == null) {
                                                    continue;
                                                }
                                    %>
                                    <div class="item-box gray6 pt10 border-b-s">
                                        <div class="item-title-container border-b-d">
												<span class="display-ib w100 tr mr5 fb">
												<%=moduleIdx%>.<%StringHelper.filterHTML(out, moduleBean.getName());%>
												</span>

                                            <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                                <input type="radio" class="mr10" name="_radio<%=moduleIdx %>"
                                                       id="_<%=j %>" onclick="selectAll('_module<%=moduleIdx%>')"/>全选
                                            </label>
                                            <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                                <input type="radio" class="mr10" name="_radio<%=moduleIdx %>"
                                                       id="_<%=j %>" onclick="unselectAll('_module<%=moduleIdx%>')"/>全不选
                                            </label>
                                            <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                                <input type="radio" class="mr10" name="_radio<%=moduleIdx %>"
                                                       id="_<%=j %>" onclick="revertAll('_module<%=moduleIdx%>')"/>反选
                                            </label>
                                        </div>
                                        <div class="item-content-container pl100 ml5 mt10">
                                            <ul id="_module<%=moduleIdx++%>"
                                                class="gray6 input-list-container clearfix">
                                                <%
                                                    if (moduleBean.getRightBeans() != null) {
                                                        for (RightBean rightBean : moduleBean.getRightBeans()) {
                                                            if (rightBean == null || rightBean.isMenu()) {
                                                                continue;
                                                            }
                                                %>
                                                <li class="w250">
                                                    <label class="cursor-p display-ib mr50 pt5 pb5" for="_<%=(j++)+1 %>">
                                                        <input id="_<%=j %>" onclick="chooseSimilar('_<%=j %>')"
                                                               name="rightIds" type="checkbox"
                                                               value="<%=rightBean.getId()%>" <%if (!StringHelper.isEmpty(rights) && rights.contains(rightBean.getId())) { %>
                                                               checked="checked" <%} %> class="mr10 rightId"/>
                                                        <%StringHelper.filterHTML(out, rightBean.getName());%>
                                                    </label>
                                                </li>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </ul>
                                        </div>
                                    </div>
                                    <%
                                            }
                                        }
                                    %>
                                </div>
                                <div class="tc w220 pt20">
                                    <%
                                        if (dimengSession.isAccessableResource(SetRight.class)) {
                                    %>
                                    <input type="button" class="btn btn-blue2 radius-6 pl20 pr20" onclick="check()"
                                           value="确认"/>
                                    <%} else { %>
                                    <input type="button" class="disabled" value="确认"/>
                                    <%} %>
                                    <input type="button"
                                           onclick="location.href='<%=controller.getURI(request, RoleList.class) %>'"
                                           class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                </div>
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
<script type="text/javascript">
    function selectAll(id) {
        var inputs = document.getElementById(id).getElementsByTagName("input");
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.type != 'checkbox') {
                continue;
            }
            input.checked = true;
        }
    }
    function unselectAll(id) {
        var inputs = document.getElementById(id).getElementsByTagName("input");
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.type != 'checkbox') {
                continue;
            }
            input.checked = false;
        }
    }
    function chooseSimilar(id) {
        var _input = document.getElementById(id);
        var _this_val = _input.value;
        var inputs = document.getElementsByTagName("input");
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.type != 'checkbox') {
                continue;
            }
            if (_this_val == input.value) {
                input.checked = _input.checked;
            }
        }
    }
    function revertAll(id) {
        var inputs = document.getElementById(id).getElementsByTagName("input");
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.type != 'checkbox') {
                continue;
            }
            input.checked = !input.checked;
        }
    }
    function check() {
        var checked = false;
        var checkBoxs = $(".rightId");
        for (var i = 0; i <= checkBoxs.length - 1; i++) {
            if ($(checkBoxs[i]).attr("checked") == "checked") {
                checked = true;
                break;
            }
        }
        if (!checked) {
            alert("请选择需要设置的权限.");
            return;
        }
        $("#form1").submit();
    }
</script>
</body>
</html>