<%@page import="com.dimeng.p2p.console.servlets.info.yygl.wzgg.SearchWzgg"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.common.enums.NoticeType" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.wzgg.AddWzgg" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
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
    CURRENT_SUB_CATEGORY = "WZGG";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, AddWzgg.class)%>"
                          class="form1" onsubmit="return onSubmit();">
                        <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增网站公告
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>文章标题：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name="title"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("title"));%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">类型：</span>
                                        <select name="type" class="border mr20 h32">
                                            <%String status = request.getParameter("type"); %>
                                            <option value="<%=NoticeType.XT %>"
                                                    <%if(!StringHelper.isEmpty(status) && status.equals(NoticeType.XT.name())){ %>selected="selected" <%} %>><%=NoticeType.XT.getName() %>
                                            </option>
                                            <option value="<%=NoticeType.HD %>"
                                                    <%if(!StringHelper.isEmpty(status) && status.equals(NoticeType.HD.name())){ %>selected="selected" <%} %>><%=NoticeType.HD.getName() %>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">是否发布：</span>
                                        <select name="publishStatus" class="border mr20 h32">
                                            <%String publishStatus = request.getParameter("publishStatus"); %>
                                            <option value="<%=NoticePublishStatus.WFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.WFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.YFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>公告内容：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, request.getParameter("content"), fileStore); %></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchWzgg.class) %>'">
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
                    $("#errorContent").html("公告内容不能为空");
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
            $("#errorContent").html("公告内容不能为空");
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
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMessage%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchWzgg.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>