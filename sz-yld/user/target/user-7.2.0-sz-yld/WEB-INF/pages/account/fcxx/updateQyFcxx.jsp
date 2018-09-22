<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.UpdateQyFcxx"%>
<%@page import="com.dimeng.p2p.user.servlets.account.QyBases"%>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.Region" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.UpdateFcxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.text.DecimalFormat" %>
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
    CURRENT_SUB_CATEGORY = "QYJCXX";
    T6112 t = ObjectHelper.convert(request.getAttribute("t"), T6112.class);
    if (t == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    final int regionId = t.F08;
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
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=1'">
                            基础信息<i></i></li>
                      <%if(t6110.F10 == T6110_F10.F){ %>
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=7'">
                            认证信息<i></i></li>
                       <%} %>
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=2'">
                            介绍资料<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=3'">
                            财务状况<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=4'">
                            联系信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=5'">
                            车产信息<i></i></li>
                        <li class="hover">房产信息<i></i></li>
                    </ul>
                </div>
            </div>

            <div class="p30">
                <div class="form_info">
                    <input id="shengId" value="<%=shengId%>" type="hidden"/>
                    <input id="shiId" value="<%=shiId%>" type="hidden"/>
                    <input id="xianId" value="<%=xianId%>" type="hidden"/>

                    <form action="<%=controller.getURI(request, UpdateQyFcxx.class) %>" class="form1" method="post">
                        <input type="hidden" name="F01" value="<%=t.F01%>"/>
                        <ul class="cell">
                            <li>
                                <div class="til">
                                    <span class="red">*</span>小区名称：
                                </div>
                                <div class="info">
                                    <input type="text" name="F03" value="<%StringHelper.filterHTML(out,t.F03);%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>建筑面积：
                                </div>
                                <div class="info">
                                    <input type="text" name="F04" maxlength="9"
                                           mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                           mtestmsg="必须为数字格式(且是两位小数)"
                                           value="<%=new DecimalFormat("##0.00").format(t.F04)%>"
                                           class="yhgl_ser text"/>平方米

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>使用年限：
                                </div>
                                <div class="info">
                                    <input type="text" name="F05" value="<%=t.F05%>"
                                           class="yhgl_ser text required isint min-size-1" maxlength="4" onkeyup="value=value.replace(/\D/g,'')"/>年
                                    <p tip></p>
                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>购买价格：
                                </div>
                                <div class="info">
                                    <input type="text" name="F06" value="<%=t.F06%>" maxlength="15"
                                           mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                           mtestmsg="必须为数字格式(且是两位小数)" class="yhgl_ser text"/>万元

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>评估价格：
                                </div>
                                <div class="info">
                                    <input type="text" name="F07" value="<%=t.F07%>" maxlength="15"
                                           mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                           mtestmsg="必须为数字格式(且是两位小数)" class="yhgl_ser text required"/>万元

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>区域：
                                </div>
                                <div class="info">
                                    <select name="sheng" id="sheng" class="required select6"></select>
                                    <select name="shi"  id="shi" class="select6"></select>
                                    <select name="xian"  id="xian" class="select6"></select>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>地址：
                                </div>
                                <div class="info">
                                    <input type="text" name="F09" value="<%StringHelper.filterHTML(out,t.F09);%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>房产证编号：
                                </div>
                                <div class="info">
                                    <input type="text" name="F10" value="<%StringHelper.filterHTML(out,t.F10);%>"
                                           class="yhgl_ser text required max-length-30"/>

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            </li>
                            <li>
                                <div class="til">
                                    <span class="red">*</span>参考价格：
                                </div>
                                <div class="info">
                                    <input type="text" name="F11" value="<%=t.F11%>" maxlength="15"
                                           mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                           mtestmsg="必须为数字格式(且是两位小数)" class="yhgl_ser text"/>万元

                                    <p tip></p>

                                    <p errortip class="" style="display: none"></p>
                                </div>
                                <div class="tir"></div>
                                <div class="clear"></div>
                            <li>
                                <div class="tc" style="width:80%">
                                    <input type="submit" class="btn01 mr10 sumbitForme" fromname="form1" value="提交"
                                           style="display: inline;">
                                    <input type="button"
                                           onclick="location.href='<%=controller.getViewURI(request, QyBases.class) %>?qyBasesFlag=6'"
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
</body>
</html>