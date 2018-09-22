<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbzz.UpdateDbzz" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.UpdateJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.AddFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.Region" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "JG";
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    final int regionId = IntegerParser.parse(request.getParameter("xian"));
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改机构信息
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">机构信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateDbzz.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">担保情况</a></li>
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
                                <input id="shengId" value="<%=shengId%>" type="hidden"/>
                                <input id="shiId" value="<%=shiId%>" type="hidden"/>
                                <input id="xianId" value="<%=xianId%>" type="hidden"/>

                                <form action="<%=controller.getURI(request, AddFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                      method="post" class="form1">
                                    <input type="hidden" name="F02" value="<%=request.getParameter("id") %>"/>
                                    <ul class="gray6">

                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>小区名称：</span>
                                            <input name="F03" maxlength="20" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F03")); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>建筑面积：</span>
                                            <input name="F04" maxlength="20" type="text"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" class="text border w300 pl5 required max-float-9999999"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F04")); %>"/>平方米
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>使用年限：</span>
                                            <input name="F05" maxlength="4" type="text"
                                                   class="text border w300 pl5 required isint"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F05")); %>"/>年
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>购买价格：</span>
                                            <input name="F06" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F06")); %>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>评估价格：</span>
                                            <input name="F07" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F07")); %>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                            <span id="errortip" class="error_tip"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>区域：</span>
                                            <select name="sheng" class="border mr10 h32 "></select> 
                                            <select name="shi" class="border mr10 h32 "></select> 
                                            <select name="xian" class="border mr10 h32 required" id="xianSlt"></select>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>地址：</span>
                                            <input name="F09" maxlength="40" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F09")); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                            <span id="errortip" class="error_tip"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>房产证编号：</span>
                                            <input name="F10" maxlength="20" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F10")); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                            <span id="errortip" class="error_tip"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>参考价格：</span>
                                            <input name="F11" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F11")); %>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                            <span id="errortip" class="error_tip"></span>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="确认"
                                                       fromname="form1"/>
                                                <input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request, ListFcxx.class)%>?id=<%=request.getParameter("id")%>'"
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
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-yes-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getURI(request, Region.class)%>"></script>
<script type="text/javascript">
$("#xianSlt").on("change", function () {
    if ($(this).val() != null && $(this).val() != "") {
        $(this).nextAll("span").empty();
    }
});
</script>
</body>
</html>