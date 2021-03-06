<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.xydj.XydjList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.xydj.ViewXydj" %>
<%@page import="com.dimeng.p2p.S51.entities.T5124" %>
<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "XYDJGL";

    T5124 entity = (T5124) request.getAttribute("entity");
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="w_main">
    <div class="main clearfix">
        <div class="wrap">
            <div class="r_main">
                <div class="home_main">
                    <div class="box box1 mb15">
                        <div class="atil">
                            <h3>修改信用等级</h3>
                        </div>
                        <div class="con">
                            <form action="<%=controller.getURI(request, ViewXydj.class)%>" method="post" class="form1">
                                <ul class="cell noborder yxjh ">
                                    <li>
                                        <div class="til"><span class="red">*</span>等级名称：</div>
                                        <div class="info">
                                            <input name="F02" type="text" maxlength="20" class="text yhgl_ser required"
                                                   value="<%StringHelper.filterHTML(out, entity.F02);%>"/>

                                            <p tip></p>

                                            <p errortip class="fl" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="til"><span class="red">*</span>下限分数：</div>
                                        <div class="info">
                                            <input name="F03" type="text" id="XX_F03" maxlength="10"
                                                   class="text yhgl_ser required isint xxfs_ratio"
                                                   value="<%=entity.F03%>"/>

                                            <p tip></p>

                                            <p errortip class="fl" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="til"><span class="red">*</span>上限分数：</div>
                                        <div class="info">
                                            <input name="F04" type="text" id="SX_F04" maxlength="10"
                                                   class="text yhgl_ser required isint xxfs_ratio"
                                                   value="<%=entity.F04%>"/>

                                            <p tip></p>

                                            <p errortip class="fl" style="display: none"></p>

                                            <p id="investFloorError" class="fl"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="clear"></div>
                                    </li>
                                </ul>
                                <div class="tc w220 pt20">
                                    <input type="hidden" name="F01" value="<%=entity.F01%>"/>
                                    <input type="submit" class="btn4 mr30 sumbitForme" fromname="form1" value="保存"/>
                                    <input type="button"
                                           onclick="window.location.href='<%=controller.getURI(request, XydjList.class)%>'"
                                           class="btn4" value="返回"/>
                                </div>
                                <div class="clear"></div>
                            </form>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <div class="box2 clearfix"></div>
                </div>
            </div>
        </div>

        <%@include file="/WEB-INF/include/left.jsp" %>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    alert(message);
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    alert(errorMessage);
</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    alert(warnMessage);
</script>
<%
    }
%>
</body>
</html>