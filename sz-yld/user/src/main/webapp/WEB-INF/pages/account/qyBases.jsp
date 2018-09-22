<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.UpdateQyFcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.AddQyFcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.ViewQyFcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.UpdateQyCcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.AddQyCcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.UpdateQyCwzk"%>
<%@page import="com.dimeng.p2p.user.servlets.account.UpdateQyLxxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.UpdateQyJszl"%>
<%@page import="com.dimeng.p2p.user.servlets.account.UpdateQyBases"%>
<%@page import="com.dimeng.p2p.user.servlets.account.Rzsc"%>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases"%>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.account.user.service.QyBaseManage" %>
<%@page import="com.dimeng.p2p.user.servlets.account.QyBases" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
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
    QyBaseManage manage = serviceSession.getService(QyBaseManage.class);
    T6161 qy = manage.getQyjbxx();
    if (qy == null) {
        qy = new T6161();
    }
    SafetyManage userManage = serviceSession.getService(SafetyManage.class);
    Safety data = userManage.get();
    if(T6110_F06.ZRR.name().equals(data.userType))
    {
        controller.sendRedirect(request, response, controller.getViewURI(request, UserBases.class)+"?userBasesFlag=1");
		return;
    }
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean yeepayFlag = false;
    if(null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX)){ 
        yeepayFlag = true;
    }
    String qyBasesFlag = request.getParameter("qyBasesFlag");
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "QYJCXX";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="user_tab clearfix Menubox">
                    <ul>
                        <li id="qyHover" class="hover qyHover" value="1">基础信息<i></i></li>
                        <%if(t6110.F10 == T6110_F10.F){ %>
                        <li id="rzxxHover" class="normalMenu qyNormalMenu" value="7">认证信息<i></i></li>
                        <%} %>
                        <li id="jxzlHover" class="normalMenu qyNormalMenu" value="2">介绍资料<i></i></li>
                        <li id="cwzkHover" class="normalMenu qyNormalMenu" value="3">财务状况<i></i></li>
                        <li id="lxxxHover" class="normalMenu qyNormalMenu" value="4">联系信息<i></i></li>
                        <li id="ccxxHover" class="normalMenu qyNormalMenu" value="5">车产信息<i></i></li>
                        <li id="fcxxHover" class="normalMenu qyNormalMenu" value="6">房产信息<i></i></li>
                    </ul>
                </div>
                <div class="pt20 form_info " id="qyJcxx">
                	<form id="form1" action="" method="post" class="form1">
                    <ul class="cell" style="width:750px;padding-left:50px; ">
                        <li class="item">
                            <div class="til"><span class="red">*</span>企业名称：</div>
                            <div class="info" id="qyJcxx_F04"><%StringHelper.filterHTML(out, qy.F04); %>&nbsp;</div>
                            <div class="clear"></div>
                        </li>
                        <li class="item" id="qyjc">
                            <div class="til"><span class="red"></span>企业简称：</div>
                            <div class="info" id="qyJcxx_F18"><%StringHelper.filterHTML(out, qy.F18); %>&nbsp;</div>
                            <div class="clear"></div>
                        </li>
                        <li id="szhyli" class="mb10" style="display:none;"><div class="til"><span class="red">*</span>是否三证合一：</div>
                                            <input id="szhy" type="hidden" name="F20" value=""> <label
                                                    id="szhy1" class="cursor-p display-ib mr50 pt5 pb5"
                                                    for="ChoiceIs"> <input type="radio" name="szhy"
                                                                           class="mr10" value="Y"
                                                                           onclick="szhyCheck()"/>是
                                            </label> <label class="cursor-p display-ib mr50 pt5 pb5"
                                                            for="ChoiceNo"> <input id="szhy2" type="radio"
                                                                                   name="szhy" class="mr10" value="N" checked="checked"
                                                                                   onclick="szhyCheck()"/>否
                                            </label>

                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                        </li>
                        <li id="yyzz" class="nszhy">
                            <div class="til"><span class="red">*</span>营业执照登记注册号：</div>
                            <div class="info" id="qyJcxx_F03"><%StringHelper.filterHTML(out, qy.F03); %>&nbsp;<br/>
                            </div>
                            <div class="clear"></div>
                        </li>

                        <li id="nsh" class="nszhy">
                            <div class="til"><span class="red">*</span>企业纳税号：</div>
                            <div class="info" id="qyJcxx_F05"><%StringHelper.filterHTML(out, qy.F05); %>&nbsp;</div>
                            <div class="clear"></div>
                        </li>
                        <li id="zzjg" class="nszhy">
                            <div class="til"><span class="red">*</span>组织机构代码：</div>
                            <div class="info" id="qyJcxx_F06"><%StringHelper.filterHTML(out, qy.F06); %>&nbsp;</div>
                            <div class="clear"></div>
                        </li>
                        <li id="xydm" style="display:none;" class="sszhy">
                            <div class="til"><span class="red">*</span>社会信用代码：</div>
                            <div class="info" id="qyJcxx_F19"><%StringHelper.filterHTML(out, qy.F19); %>&nbsp;<br/>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>注册年份：</div>
                            <div class="info" id="qyJcxx_F07">
                                <%if (qy.F07 != 0) { %>
                                <%=qy.F07 %>年
                                <%} else { %>
                                &nbsp;
                                <%} %>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>注册资金：</div>
                            <div class="info" id="qyJcxx_F08">
                                <%if (BigDecimal.ZERO.compareTo(qy.F08) < 0) { %>
                                <%=qy.F08 %>万元
                                <%} else { %>
                                &nbsp;
                                <%} %>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>行业：</div>
                            <div class="info" id="qyJcxx_F09"><%StringHelper.filterHTML(out, qy.F09); %></div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>企业规模：</div>
                            <div class="info" id="qyJcxx_F10"><%=qy.F10 == 0 ? "" : qy.F10 %>人</div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                       <%--  <li>
                            <div class="til"><span class="red"></span>法人：</div>
                            <div class="info" id="qyJcxx_F11"><%StringHelper.filterHTML(out, qy.F11); %></div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red"></span>法人身份证号：</div>
                            <div class="info" id="qyJcxx_F12"><%StringHelper.filterHTML(out, qy.F12); %></div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li> --%>
                        <li>
                            <div class="til"><span class="red">*</span>资产净值：</div>
                            <div class="info" id="qyJcxx_F14">
                                <%if (BigDecimal.ZERO.compareTo(qy.F14) < 0) { %>
                                <%=qy.F14 %>万元
                                <%} else { %>
                                &nbsp;
                                <%} %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>上年度经营现金流入：</div>
                            <div class="info" id="qyJcxx_F15">
                                <%if (BigDecimal.ZERO.compareTo(qy.F15) < 0) { %>
                                <%=qy.F15 %>万元
                                <%} else { %>
                                &nbsp;
                                <%} %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li id="dkzsbh" style="display:none;">
                            <div class="til"><span class="red"></span>贷款卡证书编号：</div>
                            <div class="info" id="qyJcxx_F16"><%StringHelper.filterHTML(out, qy.F16); %>&nbsp;<br/>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li id="xyzsbh" style="display:none;">
                            <div class="til"><span class="red"></span>企业信用证书编号：</div>
                            <div class="info" id="qyJcxx_F17"><%StringHelper.filterHTML(out, qy.F17); %>&nbsp;<br/>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li id="dbms" style="display:none;">
                            <div class="til"><span class="red"></span>担保机构描述：</div>
                            <div class="info" id="qyJcxx_jgmx" style="width:530px;overflow-x: auto;">
                            <%-- <textarea cols="40" rows="5" readonly="readonly" class="border p5"><%StringHelper.filterHTML(out, qy.jgmx); %></textarea>&nbsp;<br/> --%>
                            <%--<textarea cols="40" rows="5" readonly="readonly" class="border p5">--%><%StringHelper.filterHTML(out, qy.jgmx); %><%--</textarea>--%>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li id="qkmx" style="display:none;">
                            <div class="til"><span class="red"></span>担保情况描述：</div>
                            <div class="info" id="qyJcxx_qkmx" style="width:530px;overflow-x: auto;"><%StringHelper.format(out, qy.qkmx, fileStore);%><br/>
                            </div>
                            <div class="clear"></div>
                        </li>
						<li id="yhxkz" style="display:none;">
                            <div class="til"><span class="red"></span>开户银行许可证：</div>
                            <div class="info" id="qyJcxx_F21"><%StringHelper.filterHTML(out,qy.F21);%><br/>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li id="subli" style="display:none;">
                            <div class="tc" style="width:80%">
                                <input type="button" class="btn01 mr10 sumbitForme" fromname="form1" value="保存" onclick="saveBasesInfo();"
                                       style="display: inline;">
                                <!-- <input type="button"
                                       onclick=""
                                       class="btn01" value="取消" style="display: inline;"/> -->
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    </form>
                </div>
                <div class="form_info  pt20" id="qyJxzl" style="display: none">
                	<form id="form2" action="" method="post" class="form2">
                    <ul class="cell" style="width:400px; padding-left:200px; ">
                        <li>
                            <div class="til"><span class="red">*</span>企业简介：</div>
                            <div class="info" id="qyJxzl_F02"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>经营情况：</div>
                            <div class="info" id="qyJxzl_F03"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>涉诉情况：</div>
                            <div class="info" id="qyJxzl_F04"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>征信情况：</div>
                            <div class="info" id="qyJxzl_F05"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="tc" id="btnDiv" >
                                <input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateJszl();"
                                       style="display: inline;">
                            </div>
                        </li>
                    </ul>
                    </form>
                </div>

                <div class="form_info pt20" id="qyLxxx" style="display: none">
                	<form id="form3" action="" method="post" class="form3">
                    <ul class="cell" style="width:550px;padding-left: 170px;">
                        <li>
                            <div class="til"><span class="red">*</span>所在区域：</div>
                            <div class="info" id="qyLxxx_address"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span>联系地址：</div>
                            <div class="info" id="qyLxxx_F03"></div>
                            <div class="clear"></div>
                        </li>
                        <!-- <li>
                            <div class="til"><span class="red"></span>法人手机号码：</div>
                            <div class="info" id="qyLxxx_F06"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red"></span>法人邮箱地址：</div>
                            <div class="info" id="qyLxxx_email"></div>
                            <div class="clear"></div>
                        </li> -->
                        <li>
                            <div class="til"><span class="red"></span>企业联系人：</div>
                            <div class="info" id="qyLxxx_F07"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red"></span>联系人手机号：</div>
                            <div class="info" id="qyLxxx_F04"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til"><span class="red"></span>网站地址：</div>
                            <div class="info" id="qyLxxx_F05"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="tc" id="lxxxBtn" >
                                <input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateLxxx();"
                                       style="display: inline;">
                            </div>
                        </li>
                        
                    </ul>
                    </form>
                </div>

                <div class="pt10" id="qyCcxx" style="display: none">
                	<div class="tr mb10"><a id="qyBasesAdd" href="javascript:void(0);" class="btn04">新增</a></div>
                	<form id="form4" action="" method="post" class="form4">
                    <div class="user_table" style="display: block;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tbody id="ccxxTable">
                            <tr class="til">
                                <td align="center">汽车品牌</td>
                                <td align="center">车牌号码</td>
                                <td align="center">购车年份(年)</td>
                                <td align="center">购买价格(万元)</td>
                                <td align="center">评估价格(万元)</td>
                                <td align="center">操作</td>
                            </tr>

                            </tbody>

                            <tbody id="fcxxTable">
                            <tr class="til">
                                <td align="center">小区名称</td>
                                <td align="center">建筑面积(平方米)</td>
                                <td align="center">购买价格(万元)</td>
                                <td align="center">地址</td>
                                <td align="center">房产证编号</td>
                                <td align="center">操作</td>
                            </tr>
                            </tbody>

                            <tbody id="cwzkTable">
                            <tr class="til">
                                <td align="center">年份</td>
                                <td align="center">主营收入(万元)</td>
                                <td align="center">净利润(万元)</td>
                                <td align="center">总资产(万元)</td>
                                <td align="center">净资产(万元)</td>
                                <td align="center">备注</td>
                            </tr>
                            </tbody>

                        </table>
                        <div class="clear"></div>
                        <div id="pageContent">

                        </div>
                    </div>
                    <div class="tc pt20" id="cwzkBtn" style="display:none;" >
                        <input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateCwzk();"
                               style="display: inline;">
                    </div>
                    </form>
                </div>
                <%if(t6110.F10 == T6110_F10.F){ %>
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
                <%} %>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
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
<div id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<input type="hidden" id="updateQyBasesUrl" value="<%=controller.getURI(request, UpdateQyBases.class)%>" />
<input type="hidden" id="updateQyJszlUrl" value="<%=controller.getURI(request, UpdateQyJszl.class)%>" />
<input type="hidden" id="updateQyLxxxUrl" value="<%=controller.getURI(request, UpdateQyLxxx.class)%>" />
<input type="hidden" id="updateQyCwzkUrl" value="<%=controller.getURI(request, UpdateQyCwzk.class)%>" />
<input type="hidden" id="addQyCcxxUrl" value="<%=controller.getURI(request, AddQyCcxx.class) %>"/>
<input type="hidden" id="updateQyCcxxUrl" value="<%=controller.getURI(request, UpdateQyCcxx.class) %>"/>
<input type="hidden" id="viewQyFcxxUrl" value="<%=controller.getURI(request, ViewQyFcxx.class) %>"/>
<input type="hidden" id="addQyFcxxUrl" value="<%=controller.getURI(request, AddQyFcxx.class) %>"/>
<input type="hidden" id="updateQyFcxxUrl" value="<%=controller.getURI(request, UpdateQyFcxx.class) %>"/>
<input type="hidden" id="basesUrl" value="<%=controller.getURI(request,QyBases.class)%>"/>
<input type="hidden" id="rzUrl" value="<%=controller.getURI(request, Rzsc.class) %>"/>
<input type="hidden" id="allowUploadFileType" value="<%= configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE)%>"/>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highslide-with-gallery.js"></script>
<script type="text/javascript">
var jgFlag = "<%=t6110.F10.name() %>";
var yeepayFlag = "<%=yeepayFlag%>";
$(function(){
	var userBasesFlagJs = <%=qyBasesFlag%>;
	if (2 == userBasesFlagJs) {
		setNormalMenu($("#jxzlHover"));
	}
	else if (3 == userBasesFlagJs) {
	    setNormalMenu($("#cwzkHover"));
	}
	else if (4 == userBasesFlagJs) {
	    setNormalMenu($("#lxxxHover"));
	}
	else if (5 == userBasesFlagJs) {
	    setNormalMenu($("#ccxxHover"));
	} else if (6 == userBasesFlagJs) {
	    setNormalMenu($("#fcxxHover"));
	}else if (7 == userBasesFlagJs) {
		<%if(t6110.F10 == T6110_F10.F){ %>
	    setNormalMenu($("#rzxxHover"));
	    <%}else{%>
	    setNormalMenu($("#qyHover"));
	    <%}%>
	}else {
	    setNormalMenu($("#qyHover"));
	}
});
function toAjaxPage(){
	setParamToAjax(currentPage);
}  

hs.graphicsDir = '<%=controller.getStaticPath(request)%>/js/graphics/';
hs.align = 'center';
hs.slideshowGroup='gallery';
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
<%@include file="/WEB-INF/include/footer.jsp" %>
<%if (DIALOG_NOT_INCLUDED) {%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/aui-artDialog/artDialog.js?skin=chrome"></script>
<script type="text/javascript"  src="<%=controller.getStaticPath(request)%>/js/aui-artDialog/plugins/iframeTools.js"></script>
<%
        DIALOG_NOT_INCLUDED = false;
    }
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/qyjzxxInfo.js"></script>
</body>
</html>