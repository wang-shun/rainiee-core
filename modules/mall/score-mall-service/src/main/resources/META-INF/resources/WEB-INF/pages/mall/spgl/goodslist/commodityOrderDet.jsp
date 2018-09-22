<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.S63.enums.T6359_F08" %>
<%@page import="com.dimeng.p2p.S63.enums.T6352_F06" %>
<%@page import="com.dimeng.p2p.repeater.score.CommodityCategoryManage" %>
<%@page import="java.util.List" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.orderlist.ScoreOrderDetails" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityOrderDet" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityList" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "SPLB";
    CommodityCategoryManage ccmanage = serviceSession.getService(CommodityCategoryManage.class);
    PagingResult<ScoreOrderMainRecord> list = (PagingResult<ScoreOrderMainRecord>) request.getAttribute("mainRecords");
    ScoreOrderMainRecord[] mainRecords = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
    ScoreOrderStatistics sos = (ScoreOrderStatistics)request.getAttribute("ScoreOrderStatistics");
    ScoreOrderDetailRecord scoreOrderDetailRecord = (ScoreOrderDetailRecord)request.getAttribute("scoreOrderDetailRecord");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, CommodityOrderDet.class)%>" method="post">
      <input type="hidden" name="id" value="<%=request.getParameter("id")%>"/>
		<div class="p20">
			<div class="border">
			    <div class="title-container">
                  <i class="icon-i w30 h30 va-middle title-left-icon"></i>订单列表
                  <div class="fr"><input type="button" onclick="window.location.href='<%=controller.getURI(request, CommodityList.class)%>'" class="btn btn-blue radius-6 pl20 pr20 ml40  right0 mt5 mr10" value="返回"></div>
                </div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">商品名称：<%=scoreOrderDetailRecord.productName%></span>
			        </li>
			        <li><span class="display-ib ml150">商品类别：<%=scoreOrderDetailRecord.productTypeName%></span>
			        </li>
			      </ul>
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">订单号：</span>
			          <input type="text" name="orderNo" value="<%StringHelper.filterHTML(out, request.getParameter("orderNo"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">用户名：</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">手机号码：</span>
			          <input type="text" name="phoneNum" value="<%StringHelper.filterHTML(out, request.getParameter("phoneNum"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">成交时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li><span class="display-ib mr5">支付方式</span>
			          <select name="payType" class="border mr10 h32 mw100">
                                	<option value="">全部</option>
                                	<%for(T6352_F06 t6352_F06:T6352_F06.values()) {%>
                                		<option value="<%=t6352_F06.name() %>" <%if (t6352_F06.name().equals(request.getParameter("payType"))) {%> selected="selected" <%}%>><%=t6352_F06.getChineseName()%></option>
                                	<%} %>
                                </select>
			        </li>
			        <li><span class="display-ib mr5">状态</span>
			          <select name="status" class="border mr10 h32 mw100">
                                	<option value="">全部</option>
                                	<%for(T6359_F08 t6359_F08:T6359_F08.values()) {%>
                                		<option value="<%=t6359_F08.name() %>" <%if (t6359_F08.name().equals(request.getParameter("status"))) {%> selected="selected" <%}%>><%=t6359_F08.getChineseName()%></option>
                                	<%} %>
                                </select>
			        </li>
			        <li> <a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			      </ul>
			    </div>
			  </div>
			  <div class="border mt20 table-container">
			    <table class="table table-style gray6 tl">
			      <thead>
			        <tr class="title tc">
			          <th>序号</th>
			          <th>订单号</th>
			          <th>用户名</th>
			          <th>真实姓名</th>
			          <th>手机号码</th>
			          <th>价值</th>
			          <th>支付方式</th>
			          <th>数量</th>
			          <th>状态</th>
			          <th>成交时间</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			     	 <%
                       if (mainRecords != null){
                          int i = 1;
                          for(ScoreOrderMainRecord mainRecord :mainRecords){
                              if (mainRecord == null) {
                                  continue;
                              }
                     %>
                     <tr class="tc">
                          <td><%=i++%></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.orderNo+""); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.loginName); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.realName); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.phoneNum); %></td>
                          <% if(T6352_F06.score.name().equals(mainRecord.payType.name()))
                          {%>
                          	<td><%StringHelper.filterHTML(out, mainRecord.worth.toString().substring(0, mainRecord.worth.toString().length()-3)); %></td>
                          <% }
                          else
                          {%>
                          	<td><%StringHelper.filterHTML(out, mainRecord.worth.toString()); %></td>
                          <%} %>
                          <td><%StringHelper.filterHTML(out, mainRecord.payType.getChineseName()); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.quantity+""); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.status.getChineseName()); %></td>
                          <td><%=DateTimeParser.format(mainRecord.dealTime)%></td>
                     </tr>
                     <%} }else{%>
                      <tr class="dhsbg tc"><td colspan="10" class="tc">暂无数据</td></tr>
                     <%} %>
                    </tbody>
                    </table>
                   	</div>
                    <div class="clear"></div>
                    <div class="mb10">
                        <span class="mr30">兑换积分总计：<em
                                class="red"><%=sos.scoreAmount.setScale(0, BigDecimal.ROUND_HALF_UP)%>
                        </em> 分</span>
                        <span class="mr30">余额购买总计：<em
                                class="red"><%=Formater.formatAmount(sos.balanceAmount) %>
                        </em> 元</span>
                        <span class="mr30">商品数量总计：<em
                                class="red"><%=sos.count %></em></span>
                    </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, list);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">

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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });


	function showData(scoreOrderId)
	{
		$("#showDataId").show();

		var ajaxUrl  = "<%=controller.getURI(request, ScoreOrderDetails.class)%>";
		var data = {"scoreOrderId":scoreOrderId};
		postService(ajaxUrl,data);
	}
</script>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();

</script>
<%
    }
%>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>