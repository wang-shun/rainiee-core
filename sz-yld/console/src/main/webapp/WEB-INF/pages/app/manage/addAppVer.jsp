<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.app.manage.UpdateAppVer" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppVerSearch" %>
<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "APPVER";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form enctype="multipart/form-data" action="<%=controller.getURI(request, UpdateAppVer.class)%>"
                          method="post" name="example" onsubmit="return onSubmit();">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增
                                app版本信息
                            </div>
                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客户端类型</span>
                                        <select name="F02" class="border mr20 h32 ">
                                            <option value="1">Android</option>
                                            <option value="2">IOS</option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>版本号</span>
                                        <input type="text" class="text border w300 yw_w4" id="verNo" name="F03" />
                                        <span id="verNoError" class="red"></span>
                                        <span style="padding-left:10px;">温馨提示：版本号格式为1.1.1</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">安装包</span>
                                        <input type="file" id="file" class="text border w300 yw_w4" name="file"/>
                                        <span id="fileError">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">网络地址</span>
                                        <input type="text" id="url" class="text border w300 yw_w4" name="F12">
                                        <span id="urlError">&nbsp;</span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否强制更新</span>
                                        <select name="F04" class="border mr20 h32 ">
                                            <option value="0">否</option>
                                            <option value="1">是</option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>升级描述</span>
                                        <textarea name="F05" cols="8" rows="4" style="width:400px;height:100px;" id="desc" class="w400 h120 border p5"></textarea>
                                        <span id="descError">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>使用状态</span>
                                        <select name="F07" class="border mr20 h32 ">
                                            <option value="0">未使用</option>
                                            <option value="1">使用中</option>
                                        </select>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20" value="确认"/>
                                            <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="/console/app/appVerSearch.htm">返回</a></div> -->
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, AppVerSearch.class) %>'">
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript">
	$(function(){
		$("#verNo").on("blur",function(){
			if(this.value==""||this.value==null){
				$("#verNoError").addClass("red");
	            $("#verNoError").html("版本号不能为空");
			}else if(!(/^[0-9]+\.[0-9]+\.[0-9]+$/.test(this.value))) {
	            $("#verNoError").addClass("red");
	            $("#verNoError").html("版本号格式不对");
	        }else{
				$("#verNoError").removeClass("red");
				$("#verNoError").html("");
			}
		});
		
		$("#desc").on("blur",function(){
			if(this.value==""||this.value==null){
				$("#descError").addClass("red");
	            $("#descError").html("升级描述不能为空");
			} else if (this.value.length > 500) {
	            $("#descError").addClass("red");
	            $("#descError").html("升级描述不能超过500个字");
	            return false;
	        }else{
				$("#descError").removeClass("red");
				$("#descError").html("");
			}
		});
	});
    function onSubmit() {
        var verNo = $("#verNo").val();
        var desc = $("#desc").val();
        var file = $("#file").val();
        var url = $("#url").val();
        $("#verNoError").html("");
        $("#fileError").html("");
        $("#urlError").html("");
        $("#descError").html("");
        if (verNo == null || verNo == "") {
            $("#verNoError").addClass("red");
            $("#verNoError").html("版本号不能为空");
            return false;
        }
        if (!/^[0-9]+\.[0-9]+\.[0-9]+$/.test(verNo)) {
            $("#verNoError").addClass("red");
            $("#verNoError").html("版本号格式不对");
            return false;
        }
        if ((file == null || file == "") && (url == null || url == "")) {
            /*$("#fileError").addClass("red");
            $("#fileError").html("安装包不能为空");*/
            $("#urlError").addClass("red");
            $("#urlError").html("安装包不能为空或者网络地址不能为空");
            return false;
        }
        if (file != null && file != "") {
            var prefix = file.substring(file.lastIndexOf(".") + 1);
            prefix = prefix.toLowerCase();
            if ("apk" != prefix && "ipa" != prefix) {
                $("#fileError").addClass("red");
                $("#fileError").html("安装包类型必须为apk或ipa");
                return false;
            }
        }
        if (desc == null || desc == "") {
            $("#descError").addClass("red");
            $("#descError").html("升级描述不能为空");
            return false;
        } else if (desc.length > 500) {
            $("#descError").addClass("red");
            $("#descError").html("升级描述不能超过500个字");
            return false;
        }

    }
</script>
</body>
</html>