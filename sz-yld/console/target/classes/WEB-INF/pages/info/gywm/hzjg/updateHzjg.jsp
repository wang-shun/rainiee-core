<%@page import="com.dimeng.p2p.console.servlets.info.aqbz.hzjg.UpdateHzjg"%>
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
    CURRENT_SUB_CATEGORY = "HZJG";
    String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改 合作机构
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, UpdateHzjg.class)%>" method="post"
                                  name="example" onsubmit="return onSubmit();">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <div class="til"><span class="red">&nbsp;</span></div>
                                        <div class="info orange">
                                            <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
		                        <span class="display-ib w200 tr mr5">
									<span class="red">*</span>内容编辑：
								</span>

                                        <div class="pl200 ml5">
									<textarea name="content" cols="100" rows="8"
                                              style="width: 700px; height: 500px; visibility: hidden;">
											<%
                                                StringHelper.format(out, content, fileStore);
                                            %>
										</textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5"><input type="submit"
                                                                      class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                                      value="确认"/></div>
                                    </li>
                                </ul>
                            </form>
                            <div class="clear"></div>
                        </div>
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
                    $("#errorContent").html("合作机构内容不能为空");
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
            $("#errorContent").html("合作机构内容不能为空");
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