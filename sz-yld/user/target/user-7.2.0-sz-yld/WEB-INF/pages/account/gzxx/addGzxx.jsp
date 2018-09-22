<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.S61.enums.T6143_F03" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.AddGzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.Region" %>
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
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=2'">
                            个人学历信息<i></i></li>
                        <li class="hover">个人工作信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=4'">
                            房产信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=5'">
                            车产信息<i></i></li>
                    </ul>
                </div>
            </div>

            <div class="p30">
                <div class="form_info">
                    <form action="<%=controller.getURI(request, AddGzxx.class)%>" class="form1"
                          method="post">
                        <%=FormToken.hidden(serviceSession.getSession()) %>
                        <ul class="cell">
                            <li>
                                <div class="til">
                                    <span class="red">*</span>工作状态：
                                </div>
                                <div class="info">
                                    <select name="F03" id="F03" class="select6 required">
                                        <%for (T6143_F03 t : T6143_F03.values()) { %>
                                        <option value="<%=t.name()%>" <%if (t.name().equals(request.getParameter("F03"))) %>
                                                selected="selected" <%; %>><%=t.getChineseName() %>
                                        </option>
                                        <%} %>
                                    </select>

                                    <p style="display: none" id="infoError"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>单位名称：
                                </div>
                                <div class="info">
                                    <input type="text" name="F04"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F04"));%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>职位：
                                </div>
                                <div class="info">
                                    <input type="text" name="F05"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F05"));%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>工作邮箱：
                                </div>
                                <div class="info">
                                    <input type="text" name="F06"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F06"));%>"
                                           class="yhgl_ser text required e-mail max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>工作城市：
                                </div>
                                <div class="info">
                                    <select name="sheng" id="sheng" class="required select6"></select> 

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>工作地址：
                                </div>
                                <div class="info">
                                    <input type="text" name="F08"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F08"));%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>公司类别：
                                </div>
                                <div class="info">
                                    <input type="text" name="F09"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F09"));%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>公司行业：
                                </div>
                                <div class="info">
                                    <input type="text" name="F10"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F10"));%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>公司规模：
                                </div>
                                <div class="info">
                                    <input type="text" name="F11"
                                           value="<%StringHelper.filterHTML(out, request.getParameter("F11"));%>"
                                           class="yhgl_ser text required isint max-length-10"/>人
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
                                           onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=3'"
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

<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#infoError").addClass("error_tip");
    $("#infoError").html("<%=message%>");
    $("#infoError").show();
</script>
<%
    }
%>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getURI(request, Region.class)%>"></script>
<script type="text/javascript">
$(function(){
	$('#F03').selectlist({
		zIndex: 15,
		width: 105,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			checkHidden($("input[name='F03']"));
		}
	});
});
</script>
</body>
</html>