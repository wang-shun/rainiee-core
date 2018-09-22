<%@page import="com.dimeng.p2p.console.servlets.info.gywm.zjgw.SearchZjgw"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.console.servlets.info.gywm.zjgw.AddZjgw" %>
<%@page import="com.dimeng.p2p.common.FormToken"%>
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
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增<%=xcglmanage.getCategoryNameByCode("ZJGW") %>
                        </div>

                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form name="example" method="post" action="<%=controller.getURI(request, AddZjgw.class)%>"
                                  class="form1" enctype="multipart/form-data" onsubmit="return checkImage();">
                                  <%=FormToken.hidden(serviceSession.getSession()) %>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>姓名：</span>
                                        <input type="text" class="text border w300 pl5 required max-length-15"
                                               name="title"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("title"));%>"/>
                                        <span tip>最大15个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>相片：</span>
                                        <input type="file" class="text border w300 pl5" name="image"/>
                                        <span id="errorImage">&nbsp;</span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>个人简介：</span>
                                        <textarea name="content" class="w400 h120 border p5 required max-length-140"
                                                  cols="100" rows="8"
                                                  style="width:700px;height:200px;"><%StringHelper.format(out, request.getParameter("content"), fileStore); %></textarea>
                                        <br/>
                                        <span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <span tip>最大140个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>排序值：</span>
                                        <input type="text" class="text border w300 pl5 required isint max-length-10"
                                               name="sortindex"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("sortindex"));%>"/>
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
    <div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    function checkImage() {
        var suffix = $("input:file").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
            $("#errorImage").addClass("red");
            $("#errorImage").html("您还没有选择图片");
            return false;
        }
        var img = ['jpg', 'png', 'jpeg', 'gif', 'ico'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的图片格式不正确");
        return false;
    }
</script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMessage%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchZjgw.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>