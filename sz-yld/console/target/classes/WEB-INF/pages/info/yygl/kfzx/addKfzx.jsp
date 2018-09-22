<%@page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.SearchKfzx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.AddKfzx" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5012_F03" %>
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
    CURRENT_SUB_CATEGORY = "KFZX";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form action="<%=controller.getURI(request, AddKfzx.class) %>" method="post"
                          enctype="multipart/form-data" onsubmit="return checkImage();" class="form1">
                          <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增客服中心
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服名称：</span>
                                        <input type="text" class="text border w300  yw_w4 required max-length-30"
                                               name="name"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("name"));%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服类型：</span>
                                        <select name="type" id="type" class="border mr20 h32 ">
                                            <%String type = request.getParameter("type"); %>
                                            <option value="<%=T5012_F03.QQ.name() %>" <%if(!StringHelper.isEmpty(type) && type.equals(T5012_F03.QQ.name())){ %>selected="selected" <%} %>><%=T5012_F03.QQ.getChineseName() %></option>
                                            <option value="<%=T5012_F03.QQ_QY.name() %>" <%if(!StringHelper.isEmpty(type) && type.equals(T5012_F03.QQ_QY.name())){ %>selected="selected" <%} %>><%=T5012_F03.QQ_QY.getChineseName() %></option>
                                        </select>
                                    </li>
                                    <li class="mb10" id="kfhm"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服号码：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name="number"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("number"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" style="display: none;" id="zxdm"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>在线代码：</span>
                                        <textarea name="zxdm" cols="" rows="" class="w400 h70 border p5 required max-length-1000"><%StringHelper.filterHTML(out,request.getParameter("number"));%></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>排序值：</span>
                                        <input type="text" class="text border w300 yw_w4 required isint max-length-10"
                                               name="sortIndex"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("sortIndex"));%>"/>
                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服图片：</span>
                                        <input type="file" class="text border w300 yw_w4" name="image"/><span
                                                id="errorImage">&nbsp;</span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchKfzx.class) %>'">
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
       /* var img = ['jpg', 'png', 'jpeg', 'gif', 'ico'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的图片格式不正确");*/
        return true;
    }

    $("#type").change(function(){
        var type_val =  $("#type").val();
        if(type_val == "QQ"){
            $("#kfhm").show();
            $("#zxdm").hide();
        }else{

            $("#kfhm").hide();
            $("#zxdm").show();
        }
    });
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
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>