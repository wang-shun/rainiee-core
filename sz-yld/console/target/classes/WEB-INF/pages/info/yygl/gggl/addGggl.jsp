<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.SearchGggl"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.AddGggl" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord" %>
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
    CURRENT_SUB_CATEGORY = "GGGL";
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增广告</div>
                    </div>
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, AddGggl.class) %>" method="post"
                          enctype="multipart/form-data" onsubmit="return checkImage();" id="form1">
                          <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border mt20">
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
<%--                                             <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%> --%>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>广告图片标题：</span>
                                            <input type="text" maxlength="30" name="title"
                                                   class="text border w300 yw_w5 required"
                                                   value="<%StringHelper.filterHTML(out,request.getParameter("title"));%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>广告类型：</span>
                                            <select name="advType" id="advType" class="border mr20 h32 mw100">
                                                <%
                                                    if (T5016_F12.values() != null && T5016_F12.values().length > 0) {
                                                        for (T5016_F12 t5016_F12 : T5016_F12.values()) {
                                                %>
                                                <option value="<%=t5016_F12.name()%>"
                                                        <%if(t5016_F12.name().equals(T5016_F12.PC.name())) {%>selected="selected"<%} %>>
                                                    <%=t5016_F12.getChineseName() %>
                                                </option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>广告图片：</span>
                                            <input type="file" name="image" class="text border w300 yw_w5 required"
                                                   value=""/><span id="adviceSpan">（建议尺寸：宽1920像素，高720像素）</span>
                                            <span id="errorImage">&nbsp;</span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">链接：</span>
                                            <input type="text" maxlength="250" name="url" class="text border w300 yw_w5"
                                                   value="<%StringHelper.filterHTML(out,request.getParameter("url"));%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>排序值：</span>
                                            <input type="text" maxlength="10" name="sortIndex"
                                                   class="text border w300 yw_w5 required isint"
                                                   value="<%StringHelper.filterHTML(out,request.getParameter("sortIndex"));%>"/>
                                            <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上架时间：</span>
                                            <input type="text" id="datepicker1" readonly="readonly" name="showTime"
                                                   class="text border w300 yw_w5 date datetime required"
                                                   value="<%StringHelper.filterHTML(out,request.getParameter("showTime"));%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>下架时间：</span>
                                            <input type="text" id="datepicker2" readonly="readonly" name="unshowTime"
                                                   class="text border w300 yw_w5 date datetime required"
                                                   value="<%StringHelper.filterHTML(out,request.getParameter("unshowTime"));%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                                   fromname="form1" value="确认">
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchGggl.class) %>'">
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
            if (obj == '<%=T5016_F12.APP.name()%>' || obj == '<%=T5016_F12.APPGYJZ.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽640像素，高200像素）");
            } else if (obj == '<%=T5016_F12.PCLOGIN.name()%>' || obj == '<%=T5016_F12.PCREGISTER.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽1920像素，高720像素）");
            }else{
                $("#adviceSpan").html("（建议尺寸：宽1680像素，高300像素）");
            }
        });
    });

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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchGggl.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>