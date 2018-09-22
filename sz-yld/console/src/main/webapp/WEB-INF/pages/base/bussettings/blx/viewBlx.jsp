<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.BlxList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.ViewBlx" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "BLX";

    T6211 entity = (T6211) request.getAttribute("entity");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, ViewBlx.class) %>" method="post" name="form1"
                          class="form1">
                        <input type="hidden" name="F01" value="<%=entity.F01%>"/>

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改标类型
                            </div>
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>类型名称：</span>
                                            <input type="text" maxlength="20" name="F02"
                                                   class="text border w300 yw_w5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F02);%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>

                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                                   fromname="form1" value="提交">
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, BlxList.class)%>'">
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<div class="popup_bg hide"></div>
<div id="info"></div>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(function () {
        showInfo();
    });
    function showInfo() {
        $("#info").html(showDialogInfo("<%=message%>", "yes"));
        $("div.popup_bg").show();
    }
</script>
<%}%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $(function () {
        showError();
    });
    function showError() {
        $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
        $("div.popup_bg").show();
    }
</script>
<%}%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $(function () {
        showWarring();
    });
    function showWarring() {
        $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
        $("div.popup_bg").show();
    }
</script>
<%}%>

</body>
</html>