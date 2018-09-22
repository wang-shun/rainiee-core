<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.czytx.SearchCzytxWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.czytx.AddCzytxWtlx" %>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.gywm.gltd.AddGltd" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.common.FormToken"%>
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
    CURRENT_SUB_CATEGORY = "CZYTX";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, AddCzytxWtlx.class)%>"
                          class="form1" enctype="multipart/form-data" onsubmit="return checkImage();">
                          <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增充值提现问题类型
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>设置问题类型</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name=questionType
                                               value="<%StringHelper.filterHTML(out, request.getParameter("questionType"));%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否发布</span>
                                        <select name="publishStatus" class="border mr20 h32 ">
                                            <%String publishStatus = request.getParameter("publishStatus"); %>
                                            <option value="<%=NoticePublishStatus.WFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.WFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.YFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em
                                            class="red pr5">*</em>相片</span>
                                        <input type="file" id="file" class="text border w300 yw_w4" onchange="checkImage();" name="image"/>
                                        <span id="errorImage">&nbsp;</span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchCzytxWtlx.class) %>'">
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    function checkImage() {
        var suffix = $("input:file").val();
        if ('' == suffix || suffix == null || suffix.length == 0) {
            $("#errorImage").addClass("red");
            $("#errorImage").html("您还没有选择图片");
            return false;
        }
        var file = document.getElementById("file").files;
        var maxSize = 1024*1024;
        if(file[0].size > maxSize){
        	$("#errorImage").addClass("red");
            $("#errorImage").html("上传的图片不能大于1M");
            return false;
        }
        var img = ['jpg', 'png', 'jpeg', 'gif', 'ico'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的图片格式不正确");
        return false;
    }
</script>
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchCzytxWtlx.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>