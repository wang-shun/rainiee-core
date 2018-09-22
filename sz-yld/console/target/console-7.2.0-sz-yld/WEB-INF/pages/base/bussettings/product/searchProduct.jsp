<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.SearchBank"%>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6216_F04" %>
<%@page import="com.dimeng.p2p.S62.enums.T6216_F11" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.product.AddProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.CheckProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.DisableProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.EnableProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.SearchProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.UpdateProduct" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "CPSZ";
    String allowCount = configureProvider.getProperty(SystemVariable.ALLOW_PRODUCT_COUNT);
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    boolean is_invest_limit = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
    PagingResult<T6216> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form action="<%=controller.getURI(request, SearchProduct.class)%>"
                      method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>标产品管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">

                                    <li><span class="display-ib mr5">产品名称：</span><input
                                            type="text" name="name" id="textfield4"
                                            class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>

                                    <li><span class="display-ib mr5">标的类型：</span> <select
                                            id="select" class="border mr20 h32 mw100" name="bidType">
                                        <option value="">全部</option>
                                        <%int type = IntegerParser.parse(request.getParameter("bidType")); %>
                                        <%
                                            if (t6211s != null) {
                                                for (T6211 t6211 : t6211s) {
                                                    if (t6211 == null) {
                                                        continue;
                                                    }
                                        %>
                                        <option value="<%=t6211.F01 %>" <%if (type == t6211.F01) { %>
                                                selected="selected" <%} %>>
                                            <%StringHelper.filterHTML(out, t6211.F02); %>
                                        </option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select></li>

                                    <li><span class="display-ib mr5">还款方式：</span> <select
                                            id="select" class="border mr20 h32 mw100" name="repaymentWay">
                                        <option value="">全部</option>
                                        <%String repaymentWay = request.getParameter("repaymentWay"); %>
                                        <%
                                            for (T6216_F11 way : T6216_F11.values()) {
                                        %>
                                        <option value="<%=way.name()%>"
                                                <%if (way.name().equals(request.getParameter("repaymentWay"))) {%>
                                                selected="selected" <%}%>><%=way.getChineseName()%>
                                        </option>
                                        <%}%>
                                    </select></li>
                                    <li><span class="display-ib mr5">状态：</span> <select
                                            name="status" class="border mr20 h32 mw100">
                                        <option value="">全部</option>
                                        <%
                                            for (T6216_F04 status : T6216_F04.values()) {
                                        %>
                                        <option value="<%=status.name()%>"
                                                <%if (status.name().equals(request.getParameter("status"))) {%>
                                                selected="selected" <%}%>><%=status.getChineseName()%>
                                        </option>
                                        <%
                                            }
                                        %>
                                    </select></li>
                                    <li><a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                        <i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                        <%if (dimengSession.isAccessableResource(AddProduct.class)) {%>
                                        <a href="javascript:void(0);"
                                           onclick="checkProductCount('<%=controller.getURI(request, AddProduct.class)%>','标产品已达<%=allowCount%>个,不能新增');"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %> <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span> <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>产品名称</th>
                                    <th>年化利率</th>
                                    <th>借款期限</th>
                                    <th>还款方式</th>
                                    <%if(is_invest_limit){ %>
                                    <th>投资限制</th>
                                    <%}%>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    T6216[] banks = (result == null ? null : result.getItems());
                                    if (banks != null && banks.length != 0) {
                                        int index = 1;
                                        for (T6216 model : banks) {
                                %>
                                <tr class="title tc">
                                    <td><%=index++%></td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, model.F02);
                                        %>
                                    </td>
                                    <td><%=model.F09 %> - <%=model.F10 %> %</td>
                                    <td><%=model.F07 %> - <%=model.F08 %> 月</td>
                                    <td>
                                        <%
                                            String way = model.F11 == null ? "" : model.F11;
                                            String wayStr = "";
                                            for (T6216_F11 ways : T6216_F11.values()) {

                                                if (way.indexOf(ways.name()) > -1) {
                                                    wayStr += "," + ways.getChineseName();
                                                }
                                            }
                                            if (!wayStr.equals("")) {
                                                wayStr = wayStr.substring(1);
                                            }
                                        %> <%=wayStr %>
                                    </td>
                                    <%if(is_invest_limit){ %>
                                    <td><%=model.F18.getChineseName() %></td>
                                    <%}%>
                                    <td><%=model.F04.getChineseName() %></td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateProduct.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateProduct.class)%>?id=<%=model.F01%>"
                                                class="link-blue">修改</a> 
                                        <%} else { %> 
                                        <span class="disabled">修改</span> 
                                        <%} %> <%if (model.F04 == T6216_F04.TY) { %>
                                        <%if (dimengSession.isAccessableResource(EnableProduct.class)) {%>
												<span> <a class="link-blue" href="javascript:void(0);"
                                                          onclick="eabledProduct('<%=controller.getURI(request,EnableProduct.class)%>?id=<%=model.F01%>');">启用</a>
											</span> <%} else { %> <span
                                            class="disabled">启用</span> <%} %> <%} else { %> <%
                                        if (dimengSession.isAccessableResource(DisableProduct.class)) {
                                    %> <span><a class="link-blue"
                                                href="javascript:void(0);"
                                                onclick="disabledProduct('<%=controller.getURI(request,DisableProduct.class)%>?id=<%=model.F01%>')">停用</a></span>
                                        <%} else { %> <span class="disabled">停用</span> <%} %> <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="8">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%SearchBank.rendPagingResult(out, result);%>
                    </div>
                </form>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var addUrl = "<%=controller.getURI(request, AddProduct.class)%>";
    var searchUrl = "<%=controller.getURI(request, SearchProduct.class)%>";
    var checkUrl = "<%=controller.getURI(request, CheckProduct.class)%>";
    function checkProductCount(url, message) {
        var bl = true;

        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            url: checkUrl,
            success: function (data) {
                if (data.success) {
                    location.href = url;
                }
                else {
                    $("#info").html(showDialogInfo(message, "wrong"));
                    $("div.popup_bg").show();
                }
            }
        });
    }

    function eabledProduct(url) {
        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            url: checkUrl,
            success: function (data) {
                if (data.success) {
                	$("#info").html(showConfirmDiv("确定启用此活动吗?",url,"question"));
                    $("div.popup_bg").show();
                }
                else {
                    $("#info").html(showDialogInfo("启用的标产品已达最大限制数","wrong"));
                    $("div.popup_bg").show();
                }
            }
        });
        $("#info").html(showConfirmDiv("确定启用此产品吗?",url,"question"));
        $("div.popup_bg").show();
    }

    function disabledProduct(url) {
        $("#info").html(showConfirmDiv("确定停用此产品吗?",url,"question"));
        $("div.popup_bg").show();
    }
    
    function toConfirm(param,type){
    	document.location.href = param;
    }

    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>