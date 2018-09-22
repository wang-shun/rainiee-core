<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.user.servlets.account.Rzsc" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    int rzId = IntegerParser.parse(request.getParameter("id"));
	String isMulti = request.getParameter("isMulti");
    String allowFileType = configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE);
%>
<body>
<%@include file="/WEB-INF/include/upload.jsp" %>
<div class="clear"></div>
<div class="dialog" style="top: 0px; border: 0px none; width: 580px; margin-left: -262px;">
    <form method="post" enctype="multipart/form-data" class="form1">
        <div class="popup_bg" style="display: none;"></div>
        <div>
            <p id="errortip" style="margin-left: 101px"></p>
        </div>
        <br/>

        <div id="uploader" style="width: 523px; height: 315px;">
            <p>您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p>
        </div>
        <!-- <div class="pt10" style="margin-left: 250px;">
            <a href="javascript:void(-1);" onclick="submitForm()" class="btn04 btn_gray">确定</a>
        </div> -->
    </form>
    <input id="isupload" value="0" type="hidden"/>
</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        $("#uploader").plupload({
            runtimes: 'html5,flash,silverlight,html4',
            url: '<%=controller.getURI(request, Rzsc.class) %>?isMulti=<%=isMulti%>&id=<%=rzId %> ',
            max_file_size: '4mb',
            chunk_size: '4mb',
            filters: [{
                title: "Image files",
                extensions: "<%=allowFileType%>"
            }],
            rename: false,
            sortable: true,
            dragdrop: true,
            multi_selection: <%=(!StringHelper.isEmpty(isMulti) && "yes".equalsIgnoreCase(isMulti)) ? true : false%>,
            views: {
                list: true,
                thumbs: true,
                active: 'thumbs'
            },
            flash_swf_url: '<%=controller.getStaticPath(request)%>/js/upload/js/Moxie.swf',
            silverlight_xap_url: '<%=controller.getStaticPath(request)%>/js/upload/js/Moxie.xap',
            init: {
                PostInit: function () {

                },
                FilesAdded: function (up, files) {
                	var liv=$("#uploader_filelist li").length;
                	var max = <%=(!StringHelper.isEmpty(isMulti) && "yes".equalsIgnoreCase(isMulti)) ? 6 : 1%>;
                    if (liv > max) {
                        alert("只允许上传一张图片！后面的图片覆盖前面的。");
                        while(liv > max){
                        	$("#uploader_filelist li:eq(0)").remove();
                        	liv=$("#uploader_filelist li").length;
                        	up.removeFile(up.files[0].id);
                        }
                    }
                },
                FileFiltered: function (up, file) {
                    //选择文件后触发
                },
                BeforeUpload: function (up, file) {
					
                },
                FileUploaded: function (up, file, result) {
                    var msg = "";
                    var flg = false;
                    var typeFlag="";
                    if (result.response == "1") {
                        msg = "附件已经上传成功！";
                        typeFlag="successful";
                        flg = false;
                    } else {
                        msg = result.response;
                        typeFlag="question";
                        flg = true;
                    }
                    $("#info").html(showInfo(msg, typeFlag));
                    $("div.popup_bg").show();
                    if (!flg) {
                        $("#uploader_buttons").hide();
                    }
                },
                Error: function (up, err) {
                }
            }
        });
    });
    function submitForm() {
        var typeId = $("#type").val();
        if (typeId == '') {
            $("#errortip").text("附件类型不能为空");
            $("#errortip").addClass("red");
            return;
        }
        var list = parent.art.dialog.list;
        for (var i in list)list[i].close();
    }

</script>

</body>
</html>
