<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AnnexWz" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S62.entities.T6212" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexWz" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    T6212[] t6212s = ObjectHelper.convertArray(request.getAttribute("annexTypes"), T6212.class);
    int typeId = IntegerParser.parse(request.getParameter("typeId"));
    String allowFileType = configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE);
%>
<body style="background: #fff !important;">
<%@include file="/WEB-INF/include/upload.jsp" %>
<div class="clear"></div>
<div class="popup_tips_jz">
    <div class="">
        <div class="con clearfix">
            <div class="mt10">
                <div>
                    <span style="margin-left: 230px;font-size: 15px;color: red;"><span class="red">*</span>&nbsp;&nbsp;请选择附件类型:</span>
                    <select name="typeId" id="type" onchange="changeType()" class="required" style="border: #000 1px solid">
                        <option value="">全部</option>
                        <%
                            if (t6212s != null) {
                                for (T6212 t6212 : t6212s) {
                                    if (t6212 == null) {
                                        continue;
                                    }
                        %>
                        <option value="<%=t6212.F01%>" <%if (typeId == t6212.F01) {%> selected="selected" <%} %>><%
                            StringHelper.filterHTML(out, t6212.F02); %></option>
                        <%
                                }
                            }
                        %>
                    </select>

                    <p id="errortip" style="margin-left: 101px"></p>
                </div>
                <br/>

                <form method="post" enctype="multipart/form-data">
                    <%if (!StringHelper.isEmpty(request.getParameter("typeId"))) { %>
                    <div id="uploader" style="width: 523px; height: 208px; margin-left: 101px; margin-bottom: 103px;">
                        <p>您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p>
                    </div>
                    <%} else { %>
                    <div style="width: 490px; height: 208px; margin-left: 260px; margin-bottom: 103px;">
                        <p>请先选择附件的类型！</p>
                    </div>
                    <%} %>
                </form>
                <div class="pt10 ml300">
                    <a id="closeFormBtn" href="javascript:void(-1);" onclick="closeForm()"
                       class="btn btn-blue2 radius-6 pl20 pr20 ml40">关闭</a>
                    <a id="submitFormBtn" href="javascript:void(-1);" onclick="submitForm()"
                       class="btn btn-blue2 radius-6 pl20 pr20 ml40">关闭</a>
                </div>
            </div>
        </div>
    </div>
    <input id="isupload" value="0" type="hidden"/>
</div>
<div id="info"></div>
<div class="hse_bk" style="display: none;"></div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        var plupload = function () {
            var msg = "";
            var num = 0;
            var type = 'yes';
            $("#uploader").plupload({
// 				autostart:true,
                runtimes: 'html5,flash,silverlight,html4',
                url: '<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&typeId=<%=typeId%>&userId=<%=userId%>',
                max_file_size: '4mb',
                chunk_size: '4mb',
                filters: [{
                    title: "Image files",
                    extensions: "<%=allowFileType%>",
                    prevent_duplicates: true //不允许选取重复文件
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
                        //$("#uploader_start").hide(); //“开始上传”按钮
                        //$("#uploader_browse").hide();
                        //$(".hse_bk").show();
                        var toremove;
                        for (var i in files) {
                            var fileName = files[i].name.substring(0, files[i].name.lastIndexOf("."));
                            var fileId = "#" + files[i].id;

                            if (fileName.length > 60) {
                                toremove = i;
                                $(fileId).remove();
                                up.removeFile(files[i].id);
                            }
                        }
                        /*setTimeout(function() {
                         up.start();
                         }, 2000);*/
                    },
                    FileFiltered: function (up, file) {
                        //选择文件后触发
                        var fileName = file.name.substring(0, file.name.lastIndexOf("."));
                        if (fileName.length > 60) {
                            msg = "附件的名字" + file.name + "太长，系统自动过滤。如要上传，请修改文件名！（附件名60个字节或30个汉字。）";
                            $("#info").html(showInfo(msg, "wrong"));
                            $("div.popup_bg").show();
                            msg="";
                        }
                    },
                    BeforeUpload: function (up, file) {
                        var count = $("#uploader_filelist").children().size();
                        var total = parseInt(count);
                        $("#isupload").val(1);
                    },
                    FileUploaded: function (up, file, result) {
                    	var data = eval('(' + result.response + ')');
                        if(data.num=='1'){
                        	type='wrong';
                        }
                        msg=data.msg;
                    },
                    UploadComplete: function (up, file) {
                        $("#info").html(showInfo(msg, type));
                        $("div.popup_bg").show();
                        //$("#uploader_browse").show();
                        $(".hse_bk").hide();
                        msg = "";
                    },
                    Error: function (up, err) {
                    }
                }
            });
        }
        if ($("#type").val() != null && $("#type").val() != '') {
            plupload();
            $("#closeFormBtn").hide();
            $("#submitFormBtn").show();
        }
        else {
            $("#submitFormBtn").hide();
        }
    });
    function closeForm() {
        var list = parent.art.dialog.list;
        for (var i in list)list[i].close();
    }
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
    function changeType() {
        var typeId = $("#type").val();
        location.href = "<%=controller.getURI(request, AnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&typeId=" + typeId;
    }

    //附件太长需要滚动条 luoyi 2015-04-25
    function showAttrDialogInfo(msg, type) {
        return '<div class="dialog w510 thickpos" style="margin:-80px 0 0 -255px;">' +
                '<div class="dialog_close fr" onclick="closeInfo()"><a></a></div>' +
                '<div class="con clearfix">' +
                '<div class="d_' + type + ' fl"></div>' +
                '<div class="info fr">' +
                '<p class="f20 gray33" style="height:120px;line-height:30px;margin:0;overflow-y:auto; overflow-x:hidden">' + msg + '</p>' +
                '</div>' +
                '<div class="clear"></div>' +
                '<div class="dialog_btn"><button type="button" class="btn3 mr10" onclick="closeInfo()"  class="btn btn01">确认</button></div>' +
                '</div>' +
                '</div>';
    }
</script>
</body>
</html>
