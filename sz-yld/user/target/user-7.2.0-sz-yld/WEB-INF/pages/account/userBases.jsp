<%@page import="com.dimeng.p2p.user.servlets.account.Upload" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.ViewXlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.UpdateXlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.AddXlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.ViewGzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.UpdateGzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.AddGzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.ViewFcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.UpdateFcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.AddFcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.ViewCcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.UpdateCcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.AddCcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Rzsc" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/js/highslide.css"/>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "GRJCXX";
    String userBasesFlag = request.getParameter("userBasesFlag");
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="user_tab clearfix Menubox">
                    <ul>
                        <li id="grJzxxC" class="normalMenu" value="1">个人基本信息<i></i></li>
                        <li id="grRZxxC" class="normalMenu" value="6">认证信息<i></i></li>
                        <li id="grXlxxC" class="normalMenu" value="2">个人学历信息<i></i></li>
                        <li id="grGzxxC" class="normalMenu" value="3">个人工作信息<i></i></li>
                        <li id="grFcxxC" class="normalMenu" value="4">房产信息<i></i></li>
                        <li id="grCcxxC" class="normalMenu" value="5">车产信息<i></i></li>
                    </ul>
                </div>
                <div class="mt20" id="userBases" style="display: none">
                    <div class="user_mod_gray basic_info_top clearfix">
                        <div class="portrait" id="userBases_icon"></div>
                        <div class="upload">
                            <!--上传选择文件-->
                            <div onclick="addCard();" class="btn04">上传头像</div>
                            <p class="mt5">支持<%= configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE)%>
                               	 格式图片，上传图片最大4M，可上传头像尺寸为120*120</p>
                            <!--上传选择文件--END-->
                        </div>
                    </div>
                    <div class="basic_info">
                        <%@include file="/WEB-INF/pages/account/safetymsg.jsp" %>
                    </div>
                </div>
                <div id="userXlxx" style="display:none;">
                    <div class="user_table">
                        <div class="tr mb10"><a id="userBasesAdd" href="javascript:void(0);" class="btn04">新增</a></div>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="userXlxxTable">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">毕业院校</td>
                                <td align="center">入学年份</td>
                                <td align="center">毕业年份</td>
                                <td align="center">专业</td>
                                <td align="center">在校情况简介</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="userGzxxTable">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">工作状态</td>
                                <td align="center">单位名称</td>
                                <td align="center">职位</td>
                                <td align="center">工作地址</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="userFcxxTable">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">小区名称</td>
                                <td align="center">建筑面积(平方米)</td>
                                <td align="center">购买价格(万元)</td>
                                <td align="center">地址</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="userCcxxTable">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">汽车品牌</td>
                                <td align="center">车牌号码</td>
                                <td align="center">购车年份(年)</td>
                                <td align="center">购买价格(万元)</td>
                                <td align="center">评估价值(万元)</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <div id="pageContent"></div>
                        <div class="clear"></div>
                    </div>
                </div>
                <div id="rz_div" style="display:none;" class="mt20 rz_info">
                    <div id="xyed" class="lines pt5 f16">
                    </div>
                    <div id="xyjl" class="user_mod_gray record clearfix">
                    </div>
                    <div class="user_table mt20">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="userRzxxTable"
                               style="position: relative;">
                        </table>
                    </div>
                </div>
                <div class="clear"></div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>

<div id="dialogError" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDivError()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <span class="f20 gray3" id="rz_con_error"></span>
                </div>
            </div>
            <div class="tc mt20"><a class="btn01"  href="javascript:void(0);" onclick="closeDivError()">确定</a></div>
        </div>
    </div>
</div>

<input type="hidden" id="basesUrl" value="<%=controller.getURI(request,UserBases.class)%>"/>
<input type="hidden" id="loginUrlHD" value="<%=controller.getURI(request,Login.class)%>">
<input type="hidden" id="userViewXlxxUrl" value="<%=controller.getURI(request, ViewXlxx.class) %>"/>
<input type="hidden" id="userAddXlxxUrl" value="<%=controller.getURI(request, AddXlxx.class) %>"/>
<input type="hidden" id="userUpdateXlxxUrl" value="<%=controller.getURI(request, UpdateXlxx.class) %>"/>
<input type="hidden" id="userViewGzxxUrl" value="<%=controller.getURI(request, ViewGzxx.class) %>"/>
<input type="hidden" id="userAddGzxxUrl" value="<%=controller.getURI(request, AddGzxx.class) %>"/>
<input type="hidden" id="userUpdateGzxxUrl" value="<%=controller.getURI(request, UpdateGzxx.class) %>"/>
<input type="hidden" id="userViewFcxxUrl" value="<%=controller.getURI(request, ViewFcxx.class) %>"/>
<input type="hidden" id="userAddFcxxUrl" value="<%=controller.getURI(request, AddFcxx.class) %>"/>
<input type="hidden" id="userUpdateFcxxUrl" value="<%=controller.getURI(request, UpdateFcxx.class) %>"/>
<input type="hidden" id="userViewCcxxUrl" value="<%=controller.getURI(request, ViewCcxx.class) %>"/>
<input type="hidden" id="userAddCcxxUrl" value="<%=controller.getURI(request, AddCcxx.class) %>"/>
<input type="hidden" id="userUpdateCcxxUrl" value="<%=controller.getURI(request, UpdateCcxx.class) %>"/>
<input type="hidden" id="rzUrl" value="<%=controller.getURI(request, Rzsc.class) %>"/>
<input type="hidden" id="userBasesImg" value="<%=controller.getStaticPath(request)%>"/>
<input type="hidden" id="userBasesUserNciic" value="<%=configureProvider.format(URLVariable.USER_NCIIC)%>"/>
<input type="hidden" id="allowUploadFileType" value="<%= configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE)%>"/>
<input type="hidden" id="siteName" value="<%=configureProvider.getProperty(SystemVariable.SITE_NAME) %>"/>
<input type="hidden" id="txnanTb" value="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNANTB)) %>"/>
<input type="hidden" id="txnvTb" value="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNVTB)) %>"/>

<%@include file="/WEB-INF/include/footer.jsp" %>
<%if (DIALOG_NOT_INCLUDED) {%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/aui-artDialog/artDialog.js?skin=chrome"></script>
<script type="text/javascript"  src="<%=controller.getStaticPath(request)%>/js/aui-artDialog/plugins/iframeTools.js"></script>
<%
        DIALOG_NOT_INCLUDED = false;
    }
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/grjzxxInfo.js"></script>
<div id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highslide-with-gallery.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"successful"));
		</script>
<%
    }
%>
<script type="text/javascript">
    var userBasesFlagJs = <%=userBasesFlag%>;
    if (2 == userBasesFlagJs) {
        setNormalMenu($("#grXlxxC"));
    }
    else if (3 == userBasesFlagJs) {
        setNormalMenu($("#grGzxxC"));
    }
    else if (4 == userBasesFlagJs) {
        setNormalMenu($("#grFcxxC"));
    }
    else if (5 == userBasesFlagJs) {
        setNormalMenu($("#grCcxxC"));
    } else if (6 == userBasesFlagJs) {
        setNormalMenu($("#grRZxxC"));
    }
    else {
        setNormalMenu($("#grJzxxC"));
    }

    function addCard() {

        global_art = art.dialog.open("<%=controller.getViewURI(request, Upload.class)%>", {
            id: 'uploadImg',
            title: '上传图像',
            opacity: 0.1,
            width: 600,
            height: 365,
            lock: true,
            close: function () {
                //window.location.reload();
                window.location.href = "<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.account.UserBases.class)%>?userBasesFlag=1";
            }
        }, false);
    }
    
    function closeDivError() {
        window.location.href=$("#loginUrlHD").val();
    }
    
    function toAjaxPage(){
    	setParamToAjax(currentPage);
    }
    
    hs.graphicsDir = '<%=controller.getStaticPath(request)%>/js/graphics/';
    hs.align = 'center';
    hs.transitions = ['expand', 'crossfade'];
    hs.wrapperClassName = 'dark borderless floating-caption';
    hs.fadeInOut = true;
    hs.dimmingOpacity = .75;
    hs.showCredits = 0;
    hs.padToMinWidth = true;
    hs.numberPosition = 'caption';
    hs.lang.number = "%1/%2";


    // Add the controlbar
    if (hs.addSlideshow) hs.addSlideshow({
        slideshowGroup: 'gallery',
        interval: 5000,
        repeat: false,
        useControls: true,
        fixedControls: 'fit',
        overlayOptions: {
            opacity: .6,
            position: 'bottom center',
            hideOnMouseOut: true
        },
    	thumbstrip: {
    		position: 'bottom center',
    		mode: 'horizontal',
    		relativeTo: 'viewport'
    	}
    }); 
</script>
</body>
</html>