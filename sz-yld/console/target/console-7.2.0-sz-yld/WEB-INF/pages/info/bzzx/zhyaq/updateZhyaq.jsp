<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.UpdateZhyaq" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
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
    CURRENT_SUB_CATEGORY = "ZHYAQ";
    String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改 账户与安全
                        </div>
                    </div>
                    <div class="con">
                        <div class="clear"></div>
                        <form action="<%=controller.getURI(request, UpdateZhyaq.class)%>" method="post" name="example"
                              onsubmit="return onSubmit();">
                            <ul class="cell xc_jcxx ">
                                <li>
                                    <div class="til">&nbsp;</div>
                                    <div class="info red">
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li>
                                    <div class="til"><span class="red">*</span>内容编辑：</div>
                                    <div class="info orange">
                                        <textarea name="content" cols="100" rows="8"
                                                  style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, content, fileStore);%></textarea>

                                        <p id="errorContent">&nbsp;</p>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                            </ul>
                            <div class="tc w220 pt20"><input type="submit" class="btn4 mr30" value="确认"/></div>
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
                    $("#errorContent").html("账户与安全内容不能为空");
                }
                else {
                    $("#errorContent").removeClass("red");
                    $("#errorContent").html("&nbsp;");
                }
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });

    function onSubmit() {
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("账户与安全内容不能为空");
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