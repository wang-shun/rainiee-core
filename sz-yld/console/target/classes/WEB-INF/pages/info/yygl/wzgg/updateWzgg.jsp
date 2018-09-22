<%@page import="com.dimeng.p2p.console.servlets.info.yygl.wzgg.SearchWzgg"%>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.common.enums.NoticeType" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.wzgg.UpdateWzgg" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.NoticeRecord" %>
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
    CURRENT_SUB_CATEGORY = "WZGG";
    NoticeRecord record = ObjectHelper.convert(request.getAttribute("record"), NoticeRecord.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, UpdateWzgg.class)%>"
                          class="form1" onsubmit="return onSubmit();">
                        <input type="hidden" value="<%=record.id %>" name="id"/>

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改网站公告
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>文章标题：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name="title" value="<%StringHelper.filterHTML(out, record.title);%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>类型：</span>
                                        <select name="type" class="border mr20 h32">
                                            <option value="<%=NoticeType.XT %>"  <%if (record.type.equals(NoticeType.XT)) {%>
                                                    selected="selected" <%} %>><%=NoticeType.XT.getName() %>
                                            </option>
                                            <option value="<%=NoticeType.HD %>"  <%if (record.type.equals(NoticeType.HD)) {%>
                                                    selected="selected" <%} %>><%=NoticeType.HD.getName() %>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否发布：</span>
                                        <select name="publishStatus" class="yhgl_ser border">
                                            <option value="<%=NoticePublishStatus.WFB%>"  <%if (record.publishStatus.equals(NoticePublishStatus.WFB)) {%>
                                                    selected="selected" <%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"  <%if (record.publishStatus.equals(NoticePublishStatus.YFB)) {%>
                                                    selected="selected" <%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>公告内容：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, record.content, fileStore);%></textarea>
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
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
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
</body>
</html>