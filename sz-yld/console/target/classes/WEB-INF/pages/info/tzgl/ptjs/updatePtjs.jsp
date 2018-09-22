<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.console.servlets.info.tzgl.ptjs.UpdatePtjs" %>
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
    CURRENT_SUB_CATEGORY = "PTJS";
    T5010 category = ObjectHelper.convert(request.getAttribute("t5010"), T5010.class);
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改 <%=xcglmanage.getCategoryNameByCode("PTJS") %>
                        </div>
                    </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form action="<%=controller.getURI(request, UpdatePtjs.class)%>" method="post" name="example"
                              onsubmit="return onSubmit();" class="form1">
                            <ul class="gray6 ">
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>

                                    <%-- <div class="pl200 ml5 orange" id="info_msg">
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.INFO));%>
                                    </div>
                                    <div class="clear"></div> --%>
                                </li>
                                <li class="mb10">
									<span class="display-ib w200 tr mr5 fl">
										<span class="red">*</span>标题：
									</span>
									<input type="text"
                                              class="text border w300 pl5 required max-length-8" maxlength="8" 
                                              name="title"
                                              value="<%StringHelper.filterHTML(out, category.F03);%>"/>
                                       <span tip>不超过8个字</span>
                                       <span errortip class="" style="display: none"></span>
                                   </li>
                                   <li class="mb10">
									<span class="display-ib w200 tr mr5 fl">
										<span class="red">*</span>状态：
									</span>

                                       <select name="status"  class="border mr20 h32 required mw100">
                                               <%
                                                   if (T5010_F04.values() != null && T5010_F04.values().length > 0) {
                                                       for (T5010_F04 t5010_F04 : T5010_F04.values()) {
                                               %>
                                               <option value="<%=t5010_F04.name()%>"
                                                       <%if(t5010_F04 ==category.F04) {%>selected="selected"<%} %>><%=t5010_F04.getChineseName() %>
                                               </option>
                                               <%
                                                       }
                                                   }
                                               %>
                                           </select>
                                   </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>内容编辑：</span>

                                    <div class="pl200 ml5">
                                        <textarea name="content" cols="100" class="w400 h120 border p5 required"
                                                  rows="8"
                                                  style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, ObjectHelper.convert(request.getAttribute("content"), String.class), fileStore);%></textarea>
                                        <span id="errorContent">&nbsp;</span>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <div class="pl200 ml5"><input type="submit"
                                                                  class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1" value="确认"/>
                                    </div>
                                </li>
                            </ul>
                        </form>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>    
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $(function(){
        //保存成功，修改左菜单标题
        parent.frames["leftFrame"].window.showTZGLMenu("PTJS");
    });
</script>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterFocus: function () {
                $("#info_msg").removeClass("orange");
                $("#info_msg").html("");
            },
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("平台介绍内容不能为空");
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
            $("#errorContent").html("平台介绍内容不能为空");
            return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            return true;
        }
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, UpdatePtjs.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>