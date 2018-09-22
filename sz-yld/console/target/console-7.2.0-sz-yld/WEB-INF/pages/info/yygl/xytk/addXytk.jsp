<%--
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.console.servlets.info.xytk.AddXytk" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "XYTK";
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="wrap">
    <!--左边内容-->
    <div class="left-container">
        <%@include file="/WEB-INF/include/left.jsp" %>
    </div>
    <!--右边内容-->
    <div class="right-container">
        <a class="left-hide-arrow icon-i"></a>

        <div class="viewFramework-body">
            <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增协议条款
                        </div>
                    </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form name="example" method="post" action="<%=controller.getURI(request, AddXytk.class)%>"
                              class="form1" onsubmit="return onSubmit();">
                            <ul class="gray6">
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>

                                    <div class="pl200 ml5  orange">
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>协议类型：</span>
                                    <select name="type" class="border mr20 h32 mw100">
                                        <%String status = request.getParameter("type"); %>
                                        <option value="<%=TermType.BXBZ %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.BXBZ.name())){ %>selected="selected" <%} %>><%=TermType.BXBZ.getName() %>
                                        </option>
                                        <option value="<%=TermType.ZCXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.ZCXY.name())){ %>selected="selected" <%} %>><%=TermType.ZCXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.DBBXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.DBBXY.name())){ %>selected="selected" <%} %>><%=TermType.DBBXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.XYRZBXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.XYRZBXY.name())){ %>selected="selected" <%} %>><%=TermType.XYRZBXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.SDRZBXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.SDRZBXY.name())){ %>selected="selected" <%} %>><%=TermType.SDRZBXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.YXLCXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.YXLCXY.name())){ %>selected="selected" <%} %>><%=TermType.YXLCXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.ZQZRXY %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.ZQZRXY.name())){ %>selected="selected" <%} %>><%=TermType.ZQZRXY.getName() %>
                                        </option>
                                        <option value="<%=TermType.JKXYSB %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.JKXYSB.name())){ %>selected="selected" <%} %>><%=TermType.JKXYSB.getName() %>
                                        </option>
                                        <option value="<%=TermType.ZQZRSMS %>"
                                                <%if(!StringHelper.isEmpty(status) && status.equals(TermType.ZQZRSMS.name())){ %>selected="selected" <%} %>><%=TermType.ZQZRSMS.getName() %>
                                        </option>
                                    </select>

                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>文章内容：</span>

                                    <div class="pl200 ml5">
                                        <textarea name="content" cols="100" class="w400 h120 border p5 required"
                                                  rows="8"
                                                  style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, request.getParameter("content"), fileStore); %></textarea>
                                        <span id="errorContent">&nbsp;</span>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <div class="pl200 ml5"><input type="submit"
                                                                  class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                                  style="cursor: pointer;" fromname="form1"
                                                                  value="确认"/>
                                        <input type="button" value="返回" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                               onclick="window.location.href='<%=controller.getURI(request, SearchXytk.class) %>'"/>
                                    </div>
                                </li>
                            </ul>
                        </form>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/include/footer.jsp" %>
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
                    $("#errorContent").html("文章内容不能为空");
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
            $("#errorContent").html("文章内容不能为空");
            return false;
        } else if (editor1.count('text') > 60000) {
            $("#errorContent").addClass("red");
            $("#errorContent").html("文章内容长度不能超过60000");
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
</html>--%>
