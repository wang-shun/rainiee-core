<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.UpdateXlxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6142" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "GRJCXX";
    T6142 t = ObjectHelper.convert(request.getAttribute("t"), T6142.class);
    if (t == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">

            <div class="user_mod">
                <div class="user_tab clearfix Menubox">
                    <ul>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=1'">
                            个人基本信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=6'">
                            认证信息<i></i></li>
                        <li class="hover">个人学历信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=3'">
                            个人工作信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=4'">
                            房产信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=5'">
                            车产信息<i></i></li>
                    </ul>
                </div>
            </div>

            <div class="p30">
                <div class="form_info">
                    <form action="<%=controller.getURI(request, UpdateXlxx.class) %>" class="form1" method="post" onsubmit="return onSubmit()">
                        <input type="hidden" name="F01" value="<%=t.F01%>"/>
                        <ul class="cell">
                            <li>
                                <div class="til">
                                    <span class="red">*</span>毕业院校：
                                </div>
                                <div class="info">
                                    <input type="text" name="F03" value="<%StringHelper.filterHTML(out, t.F03);%>"
                                           class="yhgl_ser text required max-length-30"/>
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>入学年份：
                                </div>
                                <div class="info">
                                    <input type="text" name="F04" value="<%=t.F04%>" id="enrolYear"
                                           class="yhgl_ser text required isyear max-length-4"/>年
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>毕业年份：
                                </div>
                                <div class="info">
                                    <input type="text" name="F07" value="<%=t.F07%>" id="graduateYear"
                                           class="yhgl_ser text required isyear max-length-4"/>年
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>专业：
                                </div>
                                <div class="info">
                                    <input type="text" name="F05" value="<%StringHelper.filterHTML(out, t.F05);%>"
                                           class="yhgl_ser text required max-length-20"/>
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>在校情况简介：
                                </div>
                                <div class="info">
                                    <input type="text" name="F06" value="<%StringHelper.filterHTML(out, t.F06);%>"
                                           class="yhgl_ser text required max-length-30"/>
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="tc" style="width:80%">
                                    <input type="submit" class="btn01 mr10 sumbitForme" fromname="form1" value="提交"
                                           style="display: inline;">
                                    <input type="button"
                                           onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=2'"
                                           class="btn01" value="取消" style="display: inline;"/>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                        </ul>
                    </form>
                </div>

            </div>

        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="clear"></div>
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<script type="text/javascript">

</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
function onSubmit() {
	//毕业年份必须大于入学年份
	var enrolYear = $("#enrolYear").val();
	var graduateYear = $("#graduateYear").val();
	if (parseInt(enrolYear)>parseInt(graduateYear)) {
		$(".popup_bg").show();
	    $("#info").html(showDialogInfo('入学年份不能大于毕业年份！', "wrong"));
		return false;
	}
}
</script>
</body>
</html>