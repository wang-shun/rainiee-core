<%@page import="com.dimeng.p2p.user.servlets.account.Upload" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    String allowFileType = configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE);
%>
<body>
<%@include file="/WEB-INF/include/upload.jsp" %>
<!-- <div class="dialog_pak_dialog_close fr" onclick="var list=parent.art.dialog.list;for(var i in list)list[i].close();">
  <a href="javascript:void(-1)"></a></div> -->
<div class="clear"></div>
<div class="dialog " style="width:580px;top:0;margin-left: -291px;border:0;">
    <form action="<%=controller.getURI(request, Upload.class)%>" method="post" enctype="multipart/form-data">
    	<div class="popup_bg" style="display: none;"></div>
        <div>
            <p id="errortip" style="margin-left: 101px"></p>
        </div>
        <br/>
    	
        <div id="uploader" style="width:582px;height:315px;">
            <p>您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p>
        </div>
    </form>
    <!-- <div class="tc mb20">
        <a href="javascript:void(-1);" onclick="var list=parent.art.dialog.list;for(var i in list)list[i].close();" class="btn04 btn_gray">确定</a>
    </div> -->
</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        $("#uploader").plupload({
            runtimes: 'html5,flash,silverlight,html4',
            url: '<%=controller.getURI(request, Upload.class)%>',
            max_file_size: '4mb',
            chunk_size: '4mb',
            filters: [{
                title: "Image files",
                extensions: "<%=allowFileType%>"
            }],
            rename: false,
            sortable: true,
            dragdrop: true,
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
                    if (liv > 1) {
                        alert("只允许上传一张图片！后面的图片覆盖前面的。");
                      	//$("#uploader_filelist li:eq(0)").remove();
                        $("#uploader_filelist li").each(function(_i,_o){
                        	if(_i<liv-1){
                        		$("#uploader_filelist li:eq(0)").remove();
                        		up.removeFile(up.files[0].id);
                        	}
                        });
                    }
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

</script>
</body>
</html>
