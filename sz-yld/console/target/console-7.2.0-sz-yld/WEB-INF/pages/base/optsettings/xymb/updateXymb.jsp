<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.xymb.XymbList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.xymb.UpdateXymb" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.FileType" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.Xymb" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "XYMBSZ";
    Xymb xymb = ObjectHelper.convert(request.getAttribute("xymb"), Xymb.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改协议模板
                        </div>
                        <form action="<%=controller.getURI(request, UpdateXymb.class)%>" method="post" class="form1"
                              onsubmit="return onSubmit()">
                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="cell noborder yxjh ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>协议名称：</span>
                                        <%StringHelper.filterHTML(out, xymb.F03);%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>协议模板内容：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%=StringEscapeUtils.escapeHtml(xymb.text)%></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="hidden" name="id" value="<%=xymb.F01%>"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, XymbList.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: true,
            filterMode: false,
            formatUploadUrl: false,
            designMode: false,
            items: [
                'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'image', 'link'],
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("协议模板内容不能为空");
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
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("内容不能为空");
            return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            return true;
        }
    }
</script>
</body>
</html>