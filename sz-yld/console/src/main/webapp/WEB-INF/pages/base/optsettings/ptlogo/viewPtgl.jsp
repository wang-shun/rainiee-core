<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.ptlogo.PtglList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.ptlogo.ViewPtgl" %>
<%@page import="com.dimeng.p2p.S71.entities.T7101" %>
<%@page import="java.util.*" %>

<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "PT";

    T7101 entity = (T7101) request.getAttribute("entity");
    int index = (int) request.getAttribute("index");
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <form id="ptform" action="<%=controller.getURI(request, ViewPtgl.class)%>" method="post"
                              class="form1" enctype="multipart/form-data">
                            <input type="hidden" name="index" value="<%=index%>"/>

                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改平台图标
                            </div>
                            <div class="content-container p20 gray6">
                                <ul>
                                    <%
                                        List<String> nameList = new ArrayList<String>();
                                        List<String> imgList = new ArrayList<String>();
                                        List<String> proList = new ArrayList<String>();
                                        nameList.add("前台页头LOGO文件编码");
                                        nameList.add("前台页尾LOGO文件编码");
                                        nameList.add("后台登录LOGO文件编码");
                                        nameList.add("后台首页LOGO文件编码");
                                        nameList.add("微信二维码");
                                        nameList.add("微博二维码");
                                        nameList.add("手机客户端");
                                        nameList.add("标的默认图标");
                                        nameList.add("平台水印图标");
                                        nameList.add("用户中心头像图标（男）");
                                        nameList.add("用户中心头像图标（女）");
                                        imgList.add(configureProvider.getProperty(SystemVariable.QTLOGO));
                                        imgList.add(configureProvider.getProperty(SystemVariable.QTYWLOGO));
                                        imgList.add(configureProvider.getProperty(SystemVariable.HTDLLOGO));
                                        imgList.add(configureProvider.getProperty(SystemVariable.HTSYLOGO));
                                        imgList.add(configureProvider.getProperty(SystemVariable.QTWXEWM));
                                        imgList.add(configureProvider.getProperty(SystemVariable.QTWBEWM));
                                        imgList.add(configureProvider.getProperty(SystemVariable.QTSJKHD));
                                        imgList.add(configureProvider.getProperty(SystemVariable.BDMRTB));
                                        imgList.add(configureProvider.getProperty(SystemVariable.WATERIMAGE));
                                        imgList.add(configureProvider.getProperty(SystemVariable.TXNANTB));
                                        imgList.add(configureProvider.getProperty(SystemVariable.TXNVTB));
                                        proList.add("qtlg");
                                        proList.add("qtywlg");
                                        proList.add("htdl");
                                        proList.add("htsy");
                                        proList.add("qtwx");
                                        proList.add("qtwb");
                                        proList.add("qtapp");
                                        proList.add("qtbd");
                                        proList.add("sytb");
                                        proList.add("txnantb");
                                        proList.add("txnvtb");
                                    %>
                                    <li class="border-b-d pt15 pb15"><span class="display-ib w200 tr mr5">名称：</span>

                                        <div class="display-ib"><%= nameList.get(index - 2)%>
                                        </div>
                                    </li>
                                    <li class="border-b-d pt15 pb15"><span class="display-ib w200 tr mr5">图片上传：</span>

                                        <div class="display-ib"><img src="<%=fileStore.getURL(imgList.get(index-2))%>" style="width: 200px; height: 150px;">
                                        </div>
                                    </li>
                                    <li class="border-b-d pt15 pb15">
                                        <div class="pl200 ml5">
                                            <input type="file" name="<%= proList.get(index-2)%>"/>
                                        </div>
                                    </li>
                                    <li class="border-b-d pt15 pb15">
                                        <div class="pl200 ml5">
                                            <input type="hidden" name="F01" value="<%=entity.F01%>"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交"/>

                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, PtglList.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>

                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<div id="info"></div>
 <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%	
		String message = controller.getPrompt(request, response, PromptLevel.WARRING); 
		if(!StringHelper.isEmpty(message)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"wrong"));
		</script>
	<%} %>
<%	
		String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO); 
		if(!StringHelper.isEmpty(messageInfo)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=messageInfo%>',"yes"));
		</script>
	<%} %>
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