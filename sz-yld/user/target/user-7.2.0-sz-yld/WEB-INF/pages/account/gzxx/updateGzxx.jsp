<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.Region" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.S61.enums.T6143_F03" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.UpdateGzxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6143" %>
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
    T6143 t = ObjectHelper.convert(request.getAttribute("t"), T6143.class);
    if (t == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    final int regionId = t.F07;
    if (regionId % 10000 < 100) {
        shengId = regionId;
    } else if (regionId % 100 < 1) {
        shengId = regionId / 10000 * 10000;
        shiId = regionId;
    } else {
        shengId = regionId / 10000 * 10000;
        shiId = regionId / 100 * 100;
        xianId = regionId;
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
                    <input id="shengId" value="<%=shengId%>" type="hidden"/>
                    <input id="shiId" value="<%=shiId%>" type="hidden"/>
                    <input id="xianId" value="<%=xianId%>" type="hidden"/>

                    <form action="<%=controller.getURI(request, UpdateGzxx.class) %>" class="form1" method="post">
                        <input type="hidden" name="F01" value="<%=t.F01%>"/>
                        <ul class="cell">
                            <li>
                                <div class="til">
                                    <span class="red">*</span>工作状态：
                                </div>
                                <div class="info">
                                    <select name="F03" id="F03" class="select6 required">
                                        <%for (T6143_F03 type : T6143_F03.values()) { %>
                                        <option value="<%=type.name()%>" <%if (type.name().equals(t.F03.name())){ %>
                                                selected="selected" <%} %>><%=type.getChineseName() %>
                                        </option>
                                        <%} %>
                                    </select>
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
                                           value="<%StringHelper.filterHTML(out, t.F04);%>"
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
                                           value="<%StringHelper.filterHTML(out, t.F05);%>"
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
                                           value="<%StringHelper.filterHTML(out, t.F06);%>"
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
                                    <select name="sheng" id="sheng" class="required select6"></select> <select
                                        name="shi" id="shi" class="select6"></select> <select name="xian" id="xian"
                                                                                          class="select6"></select>

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
                                           value="<%StringHelper.filterHTML(out, t.F08);%>"
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
                                           value="<%StringHelper.filterHTML(out, t.F09);%>"
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
                                           value="<%StringHelper.filterHTML(out, t.F10);%>"
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
                                           value="<%StringHelper.filterHTML(out, t.F11);%>"
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

<script type="text/javascript">

</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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