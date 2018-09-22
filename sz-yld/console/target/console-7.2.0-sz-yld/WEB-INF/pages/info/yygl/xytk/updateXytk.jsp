<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.SearchXytk"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.UpdateXytk" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.TermRecord" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    CURRENT_SUB_CATEGORY = "XYTK";
    TermRecord record = ObjectHelper.convert(request.getAttribute("record"), TermRecord.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改协议条款
                        </div>
                    </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form name="example" method="post" action="<%=controller.getURI(request, UpdateXytk.class)%>"
                              class="form1" onsubmit="return onSubmit();">
                            <ul class="gray6">
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>

                                    <div class="pl200 ml5 orange">
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5 fl">协议类型：</span>
                                    <div class="pl200 ml5">
                                        <%StringHelper.filterHTML(out, record.type.getName());%>
                                    </div>
                                    <input type="hidden" value="<%=record.type.name()%>" name="id"/>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>协议内容：</span>

                                    <div class="pl200 ml5">
                                        <textarea name="content" cols="100" rows="8"
                                                  class="w400 h120 border p5 required"
                                                  style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, record.content, fileStore); %></textarea>
                                        <span id="errorContent">&nbsp;</span>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <div class="pl200 ml5">
                                        <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                               style="cursor: pointer;" fromname="form1" value="确认"/>
                                        <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                               onclick="window.location.href='<%=controller.getURI(request, SearchXytk.class) %>'"/>
                                    </div>
                                </li>
                            </ul>
                            <div class="clear"></div>
                        </form>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("协议内容不能为空");
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
            $("#errorContent").html("协议内容不能为空");
            return false;
        } else if (editor1.count('text') > 60000) {
            $("#errorContent").addClass("red");
            $("#errorContent").html("协议内容长度不能超过60000");
            return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            return true;
        }
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>