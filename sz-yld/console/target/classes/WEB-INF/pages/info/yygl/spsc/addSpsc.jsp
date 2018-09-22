<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.SearchSpsc"%>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.AddSpsc" %>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
    <link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
</head>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "SPSC";
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, AddSpsc.class)%>"
                          enctype="multipart/form-data" onsubmit="return checkImage();" class="form1">
                          <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增视频
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
<%--                                         <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%> --%>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>视频标题：</span>
                                        <input type="text" class="text border w300 required max-length-30" name="title"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("title"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>视频文件：</span>
                                        <input class="text border w300" type="file" name="file" value=""><font
                                                id="adviceSpan">（限mp4格式，不超过20M）</font><span
                                                id="errorImage">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否发布：</span>
                                        <select name="status" class="border mr20 h32 w100">
                                            <option value="<%=ArticlePublishStatus.WFB%>"><%=ArticlePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=ArticlePublishStatus.YFB%>"><%=ArticlePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否自动播放：</span>
                                        <select name="auto" class="border mr20 h32 w100">
                                            <option value="1">否</option>
                                            <option value="2">是</option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否置顶：</span>
                                        <select name="sortIndex" class="border mr20 h32 w100">
                                            <option value="0">否</option>
                                            <option value="1">是</option>
                                        </select>
                                    </li>

                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <%-- <a href="<%=controller.getURI(request, SearchSpsc.class) %>"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40">取消</a> --%>
                                            <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="javascript:window.location.href='<%=controller.getURI(request, SearchSpsc.class) %>';">
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    Date _showTime = DateTimeParser.parse(request.getParameter("showTime"));
    Date _unshowTime = DateTimeParser.parse(request.getParameter("unshowTime"));
    Date _nowTime = new Date();
    if (_showTime == null) {
        _showTime = _nowTime;
    }
    if (_unshowTime == null) {
        _unshowTime = _nowTime;
    }
%>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datetimepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datetimepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datetimepicker('option', {dateFormat: 'yy-mm-dd', timeFormat: 'HH:ii'});
        $("#datepicker2").datetimepicker({inline: true});
        $('#datepicker2').datetimepicker('option', {
            dateFormat: 'yy-mm-dd',
            timeFormat: 'HH:ii',
            minDate: $("#datepicker1").datetimepicker().val()
        });
        $("#datepicker1").datetimepicker("setDate", new Date(<%=_showTime.getTime() %>));
        $("#datepicker2").datetimepicker("setDate", new Date(<%=_unshowTime.getTime() %>));

        $("#advType").change(function () {
            var obj = $(this).val();
            if (obj == '<%=T5016_F12.APP.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽640像素，高280像素）");
            } else if (obj == '<%=T5016_F12.PC.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽1680像素，高300像素）");
            }
        });
    });

    function checkImage() {
        var suffix = $("input:file").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
            $("#errorImage").addClass("red");
            $("#errorImage").html("您还没有选择文件");
            return false;
        }
        var img = ['mp4'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的文件格式不正确");
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchSpsc.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>