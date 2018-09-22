<%@page import="com.dimeng.p2p.console.servlets.info.xxpl.yybg.SearchYybg"%>
<%@page import="com.dimeng.p2p.console.servlets.info.xxpl.yybg.AddYybg"%>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
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
    CURRENT_SUB_CATEGORY = "YYBG";
    String publishTime = request.getParameter("publishTime");
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
    
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增<%=xcglmanage.getCategoryNameByCode("YYBG") %>
                        </div>
                    </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form name="example" method="post" action="<%=controller.getURI(request, AddYybg.class)%>"
                              enctype="multipart/form-data" class="form1" onsubmit="return onSubmit();">
                            <ul class="gray6 ">
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                    <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>标题：</span>
                                    <input type="text" class="text border w300 pl5  required max-length-30" name="title"
                                           value="<%StringHelper.filterHTML(out,request.getParameter("title"));%>"/>
                                    <span tip>最大30个字</span>
                                    <span errortip class="" style="display: none"></span>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>年份：</span>
                                    <input type="text" class="text border w300 pl5 required " mtest="/^\d{4}/" mtestmsg="必须为4位整数"  name="year" 
                                           value="<%StringHelper.filterHTML(out,request.getParameter("year"));%>"/>
                                    <span errortip class="" style="display: none"></span>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>显示时间：</span>
                                    <input type="text" readonly="readonly" id="datepicker1" name="publishTime"
                                           onblur="checkDate()" style="width:275px;" class="text border pl5 date required"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("publishTime"));%>"/>
                                    <span tip></span>
                                    <span errortip class="" style="display: none"></span>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>文件：</span>
                                    <input type="file" class="text border w300 pl5" name="file"/>
                                    <span tip id="tip">支持导入PDF文件格式</span>
                                    <span id="errorFile" errortip class=""></span>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">封面图片：</span>
                                    <input type="file" class="text border w300 pl5" name="image"/>
                                    <span tip id="tip">（建议尺寸：宽210像素，高150像素）</span>
                                    <span id="errorImage">&nbsp;</span>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red"></span>是否发布：</span>
                                    <select name="status" class="border mw100 pl5 h32">
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
                                    <div class="pl200 ml5">
                                        <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                               style="cursor: pointer;" fromname="form1" value="提交"/>
                                        <input type="button" value="取消"
                                               onclick="window.location.href='<%=controller.getURI(request, SearchYybg.class) %>'"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40"/>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                            </ul>
                        </form>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<div id="info"></div>
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
    
    function onSubmit() {
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
        checkImage();
        if (checkFile() && isImage) {
            return true;
        }
        return false;
    }
    
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
    
    var isImage = false;
    function checkImage() {
        var suffix = $("input[name='image']").val();
        if ('' != suffix && suffix != null && suffix.length > 0) {
        	var img = ['jpg', 'png', 'jpeg', 'gif', 'ico', 'JPG', 'PNG', 'JPEG', 'GIF', 'ICO'];
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
        }else{
        	isImage = true;
        }
    }
    
    function checkFile() {
    	var suffix = $("input[name='file']").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
        	$("#tip").hide();
            $("#errorFile").addClass("red");
            $("#errorFile").html("您还没有选择文件");
            return false;
        }
        var fileType = ['pdf'];
        for (var i = 0; i < fileType.length; i++) {
            if (suffix.length > fileType[i].length && fileType[i] == suffix.substring(suffix.length - fileType[i].length)) {
                $("#errorFile").removeClass("red");
                $("#errorFile").html("&nbsp;");
                return true;
            }
        }
        $("#tip").hide();
        $("#errorFile").addClass("red");
        $("#errorFile").html("您选择的文件格式不正确");
        return false;
    }
    
    
</script>
</body>
</html>