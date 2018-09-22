<%@page import="com.dimeng.p2p.console.servlets.info.tzgl.xszy.SearchXszy"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.console.servlets.info.tzgl.xszy.UpdateXszy" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord" %>
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
    CURRENT_SUB_CATEGORY = "XSZY";
    ArticleRecord record = ObjectHelper.convert(request.getAttribute("record"), ArticleRecord.class);
    String connent = ObjectHelper.convert(request.getAttribute("connent"), String.class);
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改<%=xcglmanage.getCategoryNameByCode("XSZY") %>
                        </div>
                    </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form name="example" method="post" action="<%=controller.getURI(request, UpdateXszy.class)%>"
                              class="form1" onsubmit="return onSubmit();">
                            <input type="hidden" name="id" value="<%=record.id%>"/>
                            <ul class="gray6 ">
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                    <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>栏目名称：</span>
                                    <input type="text" class="text border w300 pl5 required max-length-10" name="title"
                                           value="<%StringHelper.filterHTML(out, record.title);%>"/>
                                    <span tip>最大10个字</span>
                                    <span errortip class="" style="display: none"></span>

                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">*</span>排序值：</span>
                                    <input type="text" name="sortIndex"
                                           class="text border w300 pl5  required isint max-length-10"
                                           value="<%=record.sortIndex %>"/>
                                    <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                    <span errortip class="" style="display: none"></span>

                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>文章内容：</span>

                                    <div class="pl200 ml5">
                                        <textarea name="content" class="w400 h120 border p5 required" cols="100"
                                                  rows="8"
                                                  style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, connent, fileStore);%></textarea>
                                        <span id="errorContent">&nbsp;</span>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li>
                                    <div class="pl200 ml5">
                                        <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                               style="cursor: pointer;" fromname="form1" value="确认"/>
                                        <input type="button" value="取消"
                                               onclick="window.location.href='<%=controller.getURI(request, SearchXszy.class) %>'"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml20"/>
                                    </div>
                                </li>
                            </ul>
                        </form>
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