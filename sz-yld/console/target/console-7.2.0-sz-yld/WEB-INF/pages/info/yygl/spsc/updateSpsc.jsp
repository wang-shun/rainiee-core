<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.SearchSpsc"%>
<%@page import="com.dimeng.p2p.S50.enums.T5021_F07" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page
        import="com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.UpdateSpsc" %>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
    <link href="<%=controller.getStaticPath(request)%>/css/hhmmss.css"
          rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/jplayer.blue.monday.min.css"/>
</head>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "SPSC";
    AdvertSpscRecord record = ObjectHelper.convert(request.getAttribute("record"), AdvertSpscRecord.class);
    String url = fileStore.getURL(record.fileName);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post"
                          action="<%=controller.getURI(request, UpdateSpsc.class)%>"
                          enctype="multipart/form-data" onsubmit="return checkImage();"
                          class="form1">
                        <input type="hidden" name="id" value="<%=record.id %>">
                        <input type="hidden" name="url" value="<%=url %>">

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>更新视频
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>视频标题：</span>
                                        <input type="text" class="text border w300 required max-length-30" name="title"
                                               value="<%StringHelper.filterHTML(out,record.title);%>"/>

                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li><span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                    	<div id="jp_container_1" class="jp-video jp-video-360p" role="application" aria-label="media player" style="width: 245px;height: 183px;margin-left:210px;">
												<div class="jp-type-single">
													<div id="jquery_jplayer_1" class="jp-jplayer"></div>
													<div class="jp-gui">
														<div class="jp-video-play">
															<button class="jp-video-play-icon" role="button" tabindex="0">play</button>
														</div>
													</div>
													<div class="jp-no-solution">
														<span>Update Required</span>
														To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
													</div>
												</div>
											</div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>视频文件：</span>
                                        <input class="text border w300" type="file" name="file" value=""><font
                                                id="adviceSpan">（限mp4格式，不超过20M）</font><span
                                                id="errorImage">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否发布：</span>
                                        <select name="status" class="border mr20 h32 w100">
                                            <option value="<%=ArticlePublishStatus.WFB%>"
                                                    <%if (record.status == T5021_F07.WFB) {%>
                                                    selected <%} %>><%=ArticlePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=ArticlePublishStatus.YFB%>"
                                                    <%if (record.status == T5021_F07.YFB) {%>
                                                    selected <%} %>><%=ArticlePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否自动播放：</span>
                                        <select name="auto" class="border mr20 h32 w100">
                                            <option value="1" <%if (record.isAuto == 1) {%> selected <%} %>>否</option>
                                            <option value="2" <%if (record.isAuto == 2) {%> selected <%} %>>是</option>
                                        </select>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否置顶：</span>
                                        <select name="sortIndex" class="border mr20 h32 w100">
                                            <option value="0" <%if (record.sortIndex == 0) {%> selected <%} %>>否
                                            </option>
                                            <option value="1" <%if (record.sortIndex == 1) {%> selected <%} %>>是
                                            </option>
                                        </select>
                                    </li>

                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <%-- <a href="<%=controller.getURI(request, SearchSpsc.class) %>"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40">取消</a> --%>
                                            <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="javascript:window.location.href='<%=controller.getURI(request, SearchSpsc.class) %>';"/>
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
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
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
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script
        src="<%=controller.getStaticPath(request)%>/js/swfobject_modified.js"
        type="text/javascript"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.min.js"></script>
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
        $("#datepicker1").datetimepicker("setDate", new Date(<%=_showTime.getTime()%>));
        $("#datepicker2").datetimepicker("setDate", new Date(<%=_unshowTime.getTime()%>));

        $("#advType").change(function () {
            var obj = $(this).val();
            if (obj == '<%=T5016_F12.APP.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽640像素，高280像素）");
            } else if (obj == '<%=T5016_F12.PC.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽1680像素，高300像素）");
            }
        });
    });
    //swfobject.registerObject("FlashID");
    function checkImage() {
        var suffix = $("input:file").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
            return true;
        }
        var img = ['mp4'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length
                    && img[i] == suffix.substring(suffix.length
                    - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的文件格式不正确");
        return false;
    }
    
    $(document).ready(function(){
		$("#jquery_jplayer_1").jPlayer({
    		ready: function () {
    			$(this).jPlayer("setMedia", {
    				m4v: "<%=url%>"
    			}).jPlayer("play");
    		},
    		swfPath: "<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.swf",
    		supplied: "m4v",
    		size: {
    			width: "245px",
    			height: "183px",
    			cssClass: "jp-video-360p"
    		},
    		useStateClassSkin: true,
    		autoBlur: false,
    		smoothPlayBar: true,
    		keyEnabled: true,
    		remainingDuration: true,
    		toggleDuration: true
    	});			
    });

</script>
</body>
</html>