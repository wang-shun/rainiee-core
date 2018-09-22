<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.mtbd.SearchMtbd"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.mtbd.AddMtbd" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
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
    CURRENT_SUB_CATEGORY = "MTBD";
    String publishTime = request.getParameter("publishTime");
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增<%=xcglmanage.getCategoryNameByCode("MTBD") %>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form name="example" method="post" action="<%=controller.getURI(request, AddMtbd.class)%>"
                                  class="form1" enctype="multipart/form-data" onsubmit="return onSubmit();">
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>文章名称：</span>
                                        <input type="text" class="text border w300 pl5 required max-length-30"
                                               name="title"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("title"));%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5">来源：</span>
                                        <input type="text" class="text border w300 pl5 max-length-50" name="source"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("source"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>封面图片：</span>
                                        <input type="file" class="text border w300 pl5" name="image"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("image"));%>"/><span
                                            id="errorImage">&nbsp;</span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>排序值：</span>
                                        <input type="text" class="text border w300 pl5 isint max-length-10"
                                               name="sortIndex"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("sortIndex"));%>"/>
                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0），如果不填写默认值是1.</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>是否发布：</span>
                                        <select name="status" class="border w300 pl5">
                                            <%String status = request.getParameter("status"); %>
                                            <option value="<%=ArticlePublishStatus.WFB%>"
                                                    <%if(!StringHelper.isEmpty(status) && status.equals(ArticlePublishStatus.WFB.name())){ %>selected="selected" <%} %>><%=ArticlePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=ArticlePublishStatus.YFB%>"
                                                    <%if(!StringHelper.isEmpty(status) && status.equals(ArticlePublishStatus.YFB.name())){ %>selected="selected" <%} %>><%=ArticlePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>显示时间：</span>
                                        <input type="text" readonly="readonly" id="datepicker1" name="publishTime"
                                               onblur="checkDate()" class="text border w300 pl5 date required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publishTime"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>文章摘要：</span>
                                        <input type="text" class="text border w300 pl5 max-length-110" name="summary"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("summary"));%>"/>
                                        <span tip>最大110个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>文章内容：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, request.getParameter("content"), fileStore); %></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchMtbd.class) %>'"
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
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("文章内容不能为空");
                }
                else {
                    $("#errorContent").removeClass("red");
                    $("#errorContent").html("&nbsp;");
                }
            }
        });
        prettyPrint();
    });
    function onSubmit() {
        checkImage();
        var dates = $("#datepicker1")
        var d = dates.val();
        var $error = dates.nextAll("span[errortip]");
        var $tip = dates.nextAll("span[tip]");
        if (d == "" || d == null) {
            $error.html("不能为空！");
            $error.addClass("error_tip");
            $error.show();
            $tip.hide();
            return false;
        } else {
            $error.removeClass("error_tip");
            $error.hide();
            $tip.show();
        }
        var isContent = false;
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("文章内容不能为空");
            isContent = false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            isContent = true;
        }
        if (isContent && isImage) {
            return true;
        }
        return false;
    }
    var isImage = false;
    function checkImage() {
        var suffix = $("input:file").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
            $("#errorImage").addClass("red");
            $("#errorImage").html("您还没有选择图片");
            isImage = false;
            return;
        }
        var img = ['jpg', 'png', 'jpeg', 'gif', 'ico'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                isImage = true;
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的图片格式不正确");
        isImage = false;
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                var dates = $("#datepicker1")
                var d = dates.val();
                if (d != "") {
                    var $error = dates.nextAll("span[errortip]");
                    var $tip = dates.nextAll("span[tip]");
                    $error.removeClass("error_tip");
                    $error.hide();
                    $tip.show();
                }
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <% if(!StringHelper.isEmpty(publishTime)){%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("publishTime"));%>");
        <%}%>
    });


    function checkDate() {
        window.setTimeout(function () {
            var dates = $("#datepicker1")
            var d = dates.val();
            if (d != "") {
                var $error = dates.nextAll("span[errortip]");
                var $tip = dates.nextAll("span[tip]");
                $error.removeClass("error_tip");
                $error.hide();
                $tip.show();
            }
        }, 100);
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchMtbd.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>