<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl.XxczglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl.ExportXxczgl" %>
<%@page import="com.dimeng.p2p.S71.enums.T7150_F05" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl.Xxczqx" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl.Xxczshtg" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl.AddXxcz" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "XXCZGL";
    PagingResult<OfflineChargeRecord> result = (PagingResult<OfflineChargeRecord>) request.getAttribute("result");
    BigDecimal amountCount = (BigDecimal) request.getAttribute("amountCount");
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    String createTimeStart = request.getParameter("auditorStartTime");
    String createTimeEnd = request.getParameter("auditorEndTime");

    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, XxczglList.class)%>" method="post" name="form1" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>线下充值管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">用户名：</span>
                                        <input type="text" name="account"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("account"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">姓名：</span>
                                        <input type="text" name="name"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">手机号：</span>
                                        <input type="text" name="telPhone"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("telPhone"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">建立操作人：</span>
                                        <input type="text" name="createAccount"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createAccount"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">审核人：</span>
                                        <input type="text" name="auditor"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("auditor"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">充值状态：</span>
                                        <select name="status" class="border mr20 h32 mw100" id="cccc">
                                            <option>全部</option>
                                            <%for (T7150_F05 status : T7150_F05.values()) {%>
                                            <option value="<%=status.name()%>" <%if (status.name().equals(request.getParameter("status"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">充值时间：</span>
                                        <input type="text" name="startTime" id="datepicker1" readonly="readonly"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="endTime" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 date mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">审核时间：</span>
                                        <input type="text" name="auditorStartTime" id="datepicker3" readonly="readonly"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="auditorEndTime" id="datepicker4" readonly="readonly"
                                               class="text border mr20 w120 pl5 date"/>
                                    </li>
                                    <li><a href="javascript:onSubmit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(AddXxcz.class)) {
                                        %>
                                        <a href="javascript:void(0)" name="input"
                                           onclick="window.location.href='<%=controller.getURI(request, AddXxcz.class)%>'"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%
                                            }
                                        %>
                                    </li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportXxczgl.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>用户名</th>
                                    <th>姓名或企业名称</th>
                                    <th>手机号</th>
                                    <th>用户类型</th>
                                    <th>充值时间</th>
                                    <th>建立操作人</th>
                                    <th>状态</th>
                                    <th>金额(元)</th>
                                    <th>审核人</th>
                                    <th>审核时间</th>
                                    <th>备注</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    OfflineChargeRecord[] items = result.getItems();
                                    if (items != null && items.length > 0 ) {
                                        int i = 1;
                                        for (OfflineChargeRecord item : items) {
                                            if (item == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, item.F12);%></td>
                                    <td><%StringHelper.filterHTML(out, item.chargeUserName);%></td>
                                    <td><%StringHelper.filterHTML(out, item.telPhone);%></td>
                                    <td><%StringHelper.filterHTML(out, item.chargeUserType);%></td>
                                    <td><%=Formater.formatDateTime(item.F07)%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, item.createUserName);%></td>
                                    <td>
                                        <%if (item.F05 != null) {%><%
                                        StringHelper.filterHTML(out, item.F05.getChineseName());%><%}%>
                                    </td>
                                    <td><%=Formater.formatAmount(item.F04)%>
                                    </td>
                                    <td><%=StringHelper.isEmpty(item.auditorUserName) ? "" : item.auditorUserName%>
                                    </td>
                                    <td><%=Formater.formatDateTime(item.auditorTime)%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out,item.F08);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(item.F08, 15));%></td>
                                    <td>
                                        <%if (item.F05 == T7150_F05.DSH) { %>
                                        <%if (dimengSession.isAccessableResource(Xxczshtg.class)) {%>
                                        <span class="blue">
                                          <a href="javascript:void(0);" onclick="onConfirmTg('<%=configureProvider.format(URLVariable.CHECKYESXXCZ_URL)%>?id=<%=item.F01%>&action=pass');" class="link-blue mr20">审核通过</a>
                                        </span>
                                        <%}%>
                                        <%if (dimengSession.isAccessableResource(Xxczqx.class)) {%>
                                        <span class="blue">
                                          <a href="javascript:void(0);" onclick="onConfirmBtg('<%=configureProvider.format(URLVariable.CHECKNOXXCZ_URL)%>?id=<%=item.F01%>');" class="link-blue mr20">审核不通过</a>
                                        </span>
                                        <%}%>
                                        <%}%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="13" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="mb10">金额：<em class="red"><%=Formater.formatAmount(amountCount) %>
                        </em>元
                        </div>
                        <%
                            AbstractFinanceServlet.rendPagingResult(out, result);
                        %>
                        <div class="box2 clearfix"></div>
                    </div>
                </form>
            </div>

        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<div class="popup_bg hide"></div>
<div id="info"></div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
</script>
<%} %>

<div id="realse" style="display: none;"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>

<script type="text/javascript">
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportXxczgl.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, XxczglList.class)%>";
    }
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("auditorStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("auditorEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });
    var _url = "";
    function onConfirmTg(url) {
        _url = url;
    	$(".popup_bg").show();
        $("#info").html(showConfirmDiv("确定要审核通过吗?", 0, "fb"));
    }
    function onConfirmBtg(url) {
        _url = url;
    	$(".popup_bg").show();
        $("#info").html(showConfirmDiv("确定要审核不通过吗?", 0, "fb"));
    }
    function toConfirm(msg, i, type) {
        <%if(!tg || "baofu".equals(escrowFinance) || "FUYOU".equals(escrowFinance) || "huifu".equals(escrowFinance))
        {%>
            location.href = _url;
        <%}
        else
        {%>
            $(".popup_bg").html("");
            $(".popup_bg").hide();
            $(".popup-box").hide();
            refreshCurrentPage();
            window.open(_url);
        <%}%>
    }
    
  //提现审核时确认放款后提示信息，中间跳转操作
    function refreshCurrentPage()
    {
    	$("#realse").html("");
    	var arr = new Array();
    	arr.push("<div class='popup-box' style='min-height:200px;width:400px'>"
    			+"<div class='popup-title-container'>"
    			+"<h3 class='pl20 f18'>提示</h3>"
    			+"<a class='icon-i popup-close2' href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();'></a>"
    			+"</div>"
    			+"<div class='popup-content-container pt20 ob20 clearfix'>"
    			+"<div class='mb20 gray6 f18' style='text-align: center;'>"
    			+"<ul>"
    			+"<li class='mb10'><span class='display-ib tr mr5'>请您在新打开的页面上完成操作</span></li>"
    			+"</ul>"
    			+"</div>"
    			+"<div class='tc f16'>"
    			+"<a href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();' class='btn-blue2 btn white radius-6 pl20 pr20' >充值成功</a>"
    			+"<a class='btn btn-blue2 radius-6 pl20 pr20 ml40' href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();'>充值失败</a>"
    			+"</div>"
    			+"</div>"
    			+"</div>"
    			+"<div class='popup_bg'></div>");
    	
    	$("#realse").html(arr.join(""));
    	
    	$("#realse").show();
    }

    function refreshPage()
    {
    	$("#realse").hide();
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#form1').submit();
    }
</script>
</body>
</html>
