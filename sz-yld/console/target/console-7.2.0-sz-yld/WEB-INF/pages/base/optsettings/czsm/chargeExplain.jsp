<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.czsm.ChargeExplain" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.FunctionExplainManage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "CZSMGL";
    FunctionExplainManage manage = serviceSession.getService(FunctionExplainManage.class);
    boolean isXxCharge = manage.isXxCharge();
    String xsContent = ObjectHelper.convert(request.getAttribute("xsContent"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改
                            充值说明管理
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <div class="clear"></div>
                            <form action="<%=controller.getURI(request, ChargeExplain.class)%>" method="post"
                                  name="example" onsubmit="return onSubmit();">
                                <ul class="cell xc_jcxx ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <div class="clear"></div> 
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>在线充值温馨提示编辑：</span>
                                        <div class="pl200 ml5">
                                            <textarea name="xsContent" cols="100" rows="8"
                                                      style="width:700px;<%=isXxCharge?"height:250px;":"height:500px;"%>visibility:hidden;"><%StringHelper.format(out, xsContent, fileStore);%></textarea>
                                            <p id="errorContent">&nbsp;</p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <%if(!isXxCharge){ %>
                                    <li class="mb10">
                                        <div class="pl200 ml5"><input type="submit"
                                                                      class="btn btn-blue2 radius-6 pl20 pr20"
                                                                      value="提交"/></div>
                                    </li>
                                    <%} %>
                                </ul>
                                <%if(isXxCharge){ 
                                    String xxContent = ObjectHelper.convert(request.getAttribute("xxContent"), String.class);
                                %>
                                <ul class="cell xc_jcxx ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <div class="clear"></div> 
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>线下充值温馨提示编辑：</span>
                                        <div class="pl200 ml5">
                                            <textarea name="xxContent" cols="100" rows="8"
                                                      style="width:700px;height:250px;visibility:hidden;"><%StringHelper.format(out, xxContent, fileStore);%></textarea>
                                            <p id="errorContentLine">&nbsp;</p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5"><input type="submit"
                                                                      class="btn btn-blue2 radius-6 pl20 pr20"
                                                                      value="提交"/></div>
                                    </li>
                                </ul>
                                <%} %>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>   
<script>
    var isXxCharge = <%=isXxCharge%>;
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="xsContent"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterFocus: function () {
            },
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("在线充值温馨提示内容不能为空");
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
    
    var editor2 = null;
    if(isXxCharge){
    	KindEditor.ready(function (K) {
    	    editor2 = K.create('textarea[name="xxContent"]', {
    	        uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
    	        allowFileManager: false,
    	        formatUploadUrl: false,
    	        afterFocus: function () {
    	        },
    	        afterBlur: function () {
    	            this.sync();
    	            if (this.count('text') == '') {
    	                $("#errorContentLine").addClass("red");
    	                $("#errorContentLine").html("线下充值温馨提示内容不能为空");
    	            }
    	            else {
    	                $("#errorContentLine").removeClass("red");
    	                $("#errorContentLine").html("&nbsp;");
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

    }
    
    function onSubmit() {
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("在线充值温馨提示内容不能为空");
            return false;
        }else if(editor2 != null && editor2.count('text') == ''){
        	 $("#errorContentLine").addClass("red");
             $("#errorContentLine").html("线下充值温馨提示内容不能为空");
             return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            if(isXxCharge){
            	$("#errorContentLine").removeClass("red");
                $("#errorContentLine").html("&nbsp;");
            }
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, ChargeExplain.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>