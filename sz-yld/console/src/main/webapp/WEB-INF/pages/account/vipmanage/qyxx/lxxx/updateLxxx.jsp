<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.Region" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6164" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.Check" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage" %>

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
    T6164 entity = ObjectHelper.convert(
            request.getAttribute("info"), T6164.class);
    String email = ObjectHelper.convert(
            request.getAttribute("email"), String.class);
    if (entity == null) {
        QyManage manage = serviceSession.getService(QyManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        entity = manage.getLxxx(id);
        email = manage.getEmail(id);
    }
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    final int regionId = entity.F02;
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
                                       class="tab-btn ">企业信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">联系信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <input id="shengId" value="<%=shengId%>" type="hidden"/>
                                <input id="shiId" value="<%=shiId%>" type="hidden"/>
                                <input id="xianId" value="<%=xianId%>" type="hidden"/>

                                <form id="form1" action="<%=controller.getURI(request, UpdateLxxx.class)%>" method="post"
                                      class="form1">
                                    <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                    <input type="hidden" name="F01" value="<%=entity.F01%>"/>
                                    <input type="hidden" id="entryType" name="entryType" value="">
                                    <ul class="gray6">

                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>所在区域：</span>
                                            <select name="sheng" class="border mr20 h32 "></select> <select
                                                    name="shi" class="border mr20 h32 "></select> <select name="xian"
                                                                                                          class="border mr20 h32 required"
                                                                                                          id="xianSlt"></select>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>联系地址：</span>
                                            <input name="F03" maxlength="50" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F03); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人手机号码：</span>
                                            <input name="F06" maxlength="20" type="text" id="phoneTxtId"
                                                   class="text border w300 pl5 required phonenumber"
                                                   value="<%StringHelper.filterHTML(out, entity.F06); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人邮箱地址：</span>
                                            <input name="email" maxlength="50" type="text"
                                                   class="text border w300 pl5 required email"
                                                   value="<%StringHelper.filterHTML(out, email); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">企业联系人：</span>
                                            <input name="F07" maxlength="20" type="text"
                                                   class="text border w300 pl5"
                                                   value="<%StringHelper.filterHTML(out, entity.F07); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">联系人手机：</span>
                                            <input name="F04" maxlength="11" type="text"
                                                   class="text border w300 pl5 mobile"
                                                   value="<%StringHelper.filterHTML(out, entity.F04); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                            <span id="errortip" class="error_tip"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">网站地址：</span>
                                            <input name="F05" maxlength="40" type="text"
                                                   class="text border w300 pl5"
                                                   value="<%StringHelper.filterHTML(out, entity.F05); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                            	<input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request,UpdateCwzk.class)%>?id=<%=request.getParameter("id")%>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 " value="上一步"/>
                                                <input style="display:none;" type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1"/>
                                                <input type="button"
                                                       class="btn btn-blue2 radius-6 pl20 pr20"
                                                       value="下一步" onclick="nextButton();"/>
                                                <input type="button" onclick="tj();" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                                <input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request, QyList.class)%>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                            </div>
                                        </li>
                                    </ul>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript"
        src="<%=controller.getURI(request, Region.class)%>"></script>
<script type="text/javascript">
    $("#xianSlt").on("change", function () {
        if ($(this).val() != null && $(this).val() != "") {
            $(this).nextAll("span").empty();
        }
    });
    function nextButton(){
    	$('#entryType').val('');
        $('.sumbitForme').click();
    }
    function tj() {
        $('#entryType').val('submit');
        $('.sumbitForme').click();
    }
</script>
</body>
</html>