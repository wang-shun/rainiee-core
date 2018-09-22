<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.constant.ConstantList"%>
<%@page import="com.dimeng.framework.config.service.VariableManage.VariableQuery" %>
<%@page import="com.dimeng.util.filter.TextAreaFilter" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.constant.UpdateConstant" %>
<%@page import="com.dimeng.framework.config.entity.VariableBean" %>
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
    VariableBean variable = ObjectHelper.convert(request.getAttribute("variableBean"), VariableBean.class);
    if (variable == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    String cur = (String) request.getAttribute("cur");
    VariableQuery query = ObjectHelper.convert(request.getAttribute("variableQuery"), VariableQuery.class);

    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "PTCLSZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台常量设置
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <div class="clear"></div>
                            <form id="form1" action="<%=controller.getURI(request, UpdateConstant.class)%>"
                                  method="post" class="form1">
                                <input type="hidden" name="key" value="<%=variable.getKey()%>">
                                <input type="hidden" name="searchKey" value="<%=query.getKey() %>">
                                <input type="hidden" name="cur" value="<%=cur %>">
                                <input type="hidden" name="searchDes" value="<%=query.getDescription() %>">
                                <input type="hidden" name="searchType" value="<%=query.getType() %>">
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												 常量名称：
											</span>
                                        <span class="info orange"><%StringHelper.filterHTML(out, variable.getDescription());%></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												KEY值：
											</span>
                                        <span class="info orange"><%StringHelper.filterHTML(out, variable.getKey());%></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>  常量值：</span>

                                        <div class="pl200 ml5">
                                            <textarea id="regexTextId" rows="4" cols="60" name="value"
                                                      class="border required forbidden-s"><%new TextAreaFilter(out).append(variable.getValue());%></textarea>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交" onclick="submitCheck();"/>
                                            <input type="button"
                                                   onclick="location.href='<%=controller.getURI(request, ConstantList.class) %>?des=<%=query.getDescription() %>&key=<%=query.getKey() %>&type=<%=query.getType() %>&paging.current=<%=cur %>&decode=1'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                                <div class="clear"></div>
                            </form>
                        </div>
                    </div>
                    <div class="box2 clearfix"></div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=message%>', "wrong"));
</script>
<%} %>
<%
    String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(messageInfo)) {
        String forwordUrl = controller.getURI(request, ConstantList.class)+"?des="+query.getDescription()+"&key="+query.getKey()+"&type="+query.getType() +"&paging.current="+cur+"&decode=1";
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showSuccInfo('<%=messageInfo%>', "yes",'<%=forwordUrl%>'));
</script>
<%} %>
<script type="text/javascript">

    function submitCheck() {
        var keyValue = '<%StringHelper.filterHTML(out, variable.getKey());%>';
        var flag = false;
        if (keyValue == "SYSTEM.NEW_PASSWORD_REGEX" || keyValue == "SYSTEM.NEW_USERNAME_REGEX") {
            try {
                var regexText = $("#regexTextId").val();
                var len = regexText.length;
                if (len > 4 && regexText.indexOf('/') == 0 && regexText.indexOf('^') == 1 && regexText.lastIndexOf('$') == len - 2 && regexText.lastIndexOf('/') == len - 1) {
                    new RegExp(regexText);
                    flag = true;
                }
            } catch (e) {
                alert("无效正则,格式为以/^开头,以$/");
                return;
            }
        } else {
            flag = true;
        }
        if (flag) {
            $("#form1").submit();
        } else {
            alert("无效正则,格式为以/^开头,以$/");

        }
    }
</script>
</body>
</html>