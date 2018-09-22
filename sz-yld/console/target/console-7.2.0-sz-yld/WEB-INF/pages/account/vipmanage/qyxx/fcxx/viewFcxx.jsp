<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    QyManage manage = serviceSession.getService(QyManage.class);
    T6112 info = ObjectHelper.convert(request.getAttribute("info"), T6112.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改企业信息
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">房产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>小区名称：</span>
                                        <%StringHelper.filterHTML(out, info.F03);%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>建筑面积：</span>
                                        <%=new BigDecimal(info.F04 + "") %>平方米
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>使用年限：</span>
                                        <%=info.F05%>年
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>购买价格：</span>
                                        <%=Formater.formatAmount(info.F06)%>万元
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>评估价格：</span>
                                        <%=Formater.formatAmount(info.F07)%>万元
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>区域：</span>
                                        <%StringHelper.filterHTML(out, manage.getRegion(info.F08));%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>地址：</span>
                                        <%StringHelper.filterHTML(out, info.F09);%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>房产证编号：</span>
                                        <%StringHelper.filterHTML(out, info.F10);%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>参考价格：</span>
                                        <%=info.F11%>万元
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>'"
                                                   value="返回"/></div>
                                    </li>
                                </ul>
                            </div>
                            <div class="mb15"></div>
                            <div class="box2 clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%
            String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
            if (!StringHelper.isEmpty(warringMessage)) {
        %>
        <div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
            <div class="info clearfix">
                <div class="clearfix">
                    <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
                </div>
                <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                               class="btn4 ml50"/></div>
            </div>
        </div>
        <%} %>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>