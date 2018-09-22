<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.UpdateCcxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6113" %>
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
    T6113 entity = ObjectHelper.convert(request.getAttribute("info"), T6113.class);
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
                                <li><a href="javascript:void(0);" class="tab-btn select-a">车产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <form action="<%=controller.getURI(request, UpdateCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                      method="post" class="form1">
                                    <ul class="gray6">
                                        <li class="mb10">
                                            <span class="display-ib w200 tr mr5"><span class="red">*</span>汽车品牌：</span>
                                            <input name="F03" maxlength="45" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F03); %>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <span class="display-ib w200 tr mr5"><span class="red">*</span>车牌号码：</span>
                                            <input name="F04" maxlength="20" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F04); %>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <span class="display-ib w200 tr mr5"><span class="red">*</span>购车年份：</span>
                                            <input name="F05" maxlength="4" type="text"
                                                   class="text border w300 pl5 required isyear"
                                                   value="<%=entity.F05 %>"/>年
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <span class="display-ib w200 tr mr5"><span class="red">*</span>购买价格：</span>
                                            <input name="F06" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text"
                                                   class="text border w300 pl5 required" value="<%=entity.F06 %>"/>万元
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <span class="display-ib w200 tr mr5"><span class="red">*</span>评估价格：</span>
                                            <input name="F07" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text"
                                                   class="text border w300 pl5 required" value="<%=entity.F07 %>"/>万元
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <div class="info"><input type="hidden" name="F01" value="<%=entity.F01 %>"/>
                                            </div>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20  sumbitForme" value="确认"
                                                       fromname="form1"/>
                                                <input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request, ListCcxx.class)%>?id=<%=request.getParameter("id")%>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                            </div>
                                        </li>
                                    </ul>
                                </form>
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