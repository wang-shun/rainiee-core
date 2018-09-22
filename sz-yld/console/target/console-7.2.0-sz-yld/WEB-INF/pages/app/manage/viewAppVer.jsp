<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo" %>
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
    AppVersionInfo vinfo = (AppVersionInfo) request.getAttribute("result");

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form enctype="multipart/form-data" action="<%=controller.getURI(request, UpdateAppVer.class)%>"
                          method="post" name="example" class="form1" onsubmit="return onSubmit();">
                        <input type="hidden" name="F01" value="<%=vinfo.id %>">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改
                                app版本信息
                            </div>
                        <ul class="cell xc_jcxx p20">
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5 red">&nbsp;</span>
                                <span errortip
                                      class=""><%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR)); %></span>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客户端类型：</span>
                                <select name="F02" class="border mr20 h32 ">
                                    <option value="1" <%=vinfo.verType == 1 ? "selected='selected'" : "" %>>
                                        Android
                                    </option>
                                    <option value="2" <%=vinfo.verType == 2 ? "selected='selected'" : "" %>>IOS
                                    </option>
                                </select>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>版本号：</span>
                                <input type="text" id="verNo" class="text border w300 yw_w4" name="F03"
                                       value="<%=vinfo.verNO %>">
                                <span id="verNoError" class="red"></span>
                                <span style="padding-left:10px;">温馨提示：版本号格式为1.1.1</span>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5 fl">安装包：</span>

                                <div class="pl200 ml5">
                                    <%if (!StringHelper.isEmpty(vinfo.file)) { %> <a id="fileUrl"
                                                                                          href="<%=fileUploader.getURL(vinfo.file)%>"><%=vinfo.file %>
                                </a><br/><%}%>
                                    <input type="file" id="file" class="text border w300 yw_w4" name="file"/>
                                    <span id="fileError">&nbsp;</span>
                                </div>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5">网络地址：</span>
                                <input type="text" id="url" class="text border w300 yw_w4" name="F12"
                                       value="<%=StringHelper.isEmpty(vinfo.url)?"":vinfo.url %>">
                                <span id="urlError">&nbsp;</span>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否必须更新：</span>
                                <select name="F04" class="border mr20 h32 ">
                                    <option value="0" <%=vinfo.isMustUpdate == 0 ? "selected='selected'" : "" %>>
                                        否
                                    </option>
                                    <option value="1" <%=vinfo.isMustUpdate == 1 ? "selected='selected'" : "" %>>
                                        是
                                    </option>
                                </select>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>升级描述：</span>
                                <textarea name="F05" id="desc" cols="8" rows="4"
                                          style="width:400px;height:100px;"><%=vinfo.mark %></textarea>
                                <span id="descError">&nbsp;</span>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>使用状态：</span>
                                <select name="F07" class="border mr20 h32 ">
                                    <option value="0" <%=vinfo.status == 0 ? "selected='selected'" : "" %>>未使用
                                    </option>
                                    <option value="1" <%=vinfo.status == 1 ? "selected='selected'" : "" %>>使用中
                                    </option>
                                </select>
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5">发布时间：</span>
                                <input type="text" readonly="readonly"
                                       value="<%=TimestampParser.format(vinfo.publishTime) %>">
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5">发布人：</span>
                                <input type="text" readonly="readonly" value="<%=vinfo.publisher %>">
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5">最后更新时间：</span>
                                <input type="text" readonly="readonly"
                                       value="<%=TimestampParser.format(vinfo.updateTime) %>">
                            </li>
                            <li class="mb10">
                                <span class="display-ib w200 tr mr5">更新人：</span>
                                <input type="text" readonly="readonly" value="<%=vinfo.updater %>">
                            </li>
                            <li>
                                <div class="pl200 ml5">
                                    <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20" value="确认"/>
                                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="/console/app/appVerSearch.htm">返回</a></div> -->
                                    <input type="button" value="返回" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                           onclick="window.location.href='<%=controller.getURI(request, AppVerSearch.class) %>'">
                                </div>
                            </li>
                        </ul>
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
        var file = $("#fileUrl").html();
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
        if (!/^[0-9]+.[0-9]+.[0-9]+$/.test(verNo)) {
            $("#verNoError").addClass("red");
            $("#verNoError").html("版本号格式不对");
            return false;
        }
        var fileval = $("#file").val();
        if (file == null || file == "") {
            if ((fileval == null || fileval == "") && (url == null || url == "")) {
               /* $("#fileError").addClass("red");
                $("#fileError").html("安装包不能为空");*/
                $("#urlError").addClass("red");
                $("#urlError").html("安装包不能为空或者网络地址不能为空");
                return false;
            }
        }
        if (fileval != null && fileval != "") {
            var prefix = fileval.substring(fileval.lastIndexOf(".") + 1);
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