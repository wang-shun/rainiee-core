<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.mallbuydzgl.MallBuyQuery"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.mallbuydzgl.MallBuydzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.console.TbdzEntity" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>

<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "SPGMDZ";
    PagingResult<TbdzEntity> result = (PagingResult<TbdzEntity>) request.getAttribute("result");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, MallBuydzList.class)%>" method="post" name="form1" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>商品购买对账
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">用户名：</span>
                                        <input type="text" name="userName" value="${userName}"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">流水号：</span>
                                        <input type="text" name="LoanNo" value="${LoanNo}"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">订单号：</span>
                                        <input type="text" name="OrderNo" value="${OrderNo}"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">状态：</span>
                                        <select name="state" class="border mr20 h32 mw100">
                                            <option value="" <% if ("".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>全部
                                            </option>
                                            <option value="DTJ" <%if ("DTJ".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>待提交
                                            </option>
                                            <option value="YTJ" <%if ("YTJ".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>已提交
                                            </option>
                                            <option value="DQR" <%if ("DQR".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>待确认
                                            </option>
                                            <option value="CG" <%if ("CG".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>成功
                                            </option>
                                            <option value="SB" <%if ("SB".equals(request.getAttribute("state"))) {%>
                                                    selected="selected"<%} %>>失败
                                            </option>
                                        </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">购买时间：</span>
                                        <input type="text" name="startExpireDatetime"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("startExpireDatetime")); %>"
                                               id="datepicker1" readonly="readonly" class="text border pl5 w120 mr20 date"/>至
                                        <input type="text" name="endExpireDatetime"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endExpireDatetime")); %>"
                                               id="datepicker2" readonly="readonly" class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                	<th class="tc">序号</th>
                                    <th class="tc">订单号</th>
                                    <th class="tc">用户名</th>
                                    <th class="tc">金额</th>
                                    <th class="tc">购买时间</th>
                                    <th class="tc">完成时间</th>
                                    <th class="tc">流水号</th>
                                    <th class="tc">状态</th>
                                    <th class="tc">操作</th>
                                    <th class="tc" width="3%">备注</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    TbdzEntity[] mallbuys = result.getItems();
                                    if (mallbuys.length > 0) {
                                        int i = 1;
                                        for (TbdzEntity mallbuy : mallbuys) {
                                            if (mallbuy == null) {
                                                continue;
                                            }
                                %>
                                <tr>
                                	<td class="tc"><%=i++%>
                                	</td>
                                    <td class="tc"><%=mallbuy.F01 %>
                                    </td>
                                    <td class="tc"><%=mallbuy.userName %>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(mallbuy.amount) %>
                                    </td>
                                    <td class="tc"><%=DateTimeParser.format(mallbuy.F04, "yyyy-MM-dd HH:mm")%>
                                    </td>
                                    <%if (mallbuy.F06 != null) { %>
                                    <td class="tc"><%=DateTimeParser.format(mallbuy.F06, "yyyy-MM-dd HH:mm")%>
                                    </td>
                                    <%} else { %>
                                    <td class="tc">&nbsp;</td>
                                    <%} %>
                                    <td class="tc"><%=mallbuy.F10%>
                                    </td>
                                    <td class="tc"><%=mallbuy.F03.getChineseName()%>
                                    </td>
                                    <td class="tc">
                                    	<% if ("S".equals(mallbuy.F11.name())) { %>
                                   			已对账
                                   		<%} else { if (dimengSession.isAccessableResource(MallBuyQuery.class)) { %>
											<a target="_blank" onclick="zdOrderNo(<%=mallbuy.F01%>)" class="blue ml5">去对账 </a> 
			                            <%}else{ %>
			                            	<span class="disabled">去对账</span>
			                            <%} } %> 
                                    </td>
                                    <td class="tc"  title="<%=mallbuy.F12%>"> 
                                    	<div style=" width:80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">
                                    		<%=mallbuy.F12%>
                                    	 </div>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="10" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                            <%
                                AbstractFinanceServlet.rendPagingResult(out, result);
                            %>
                        </div>
                        <div class="clear"></div>
                        <div class="box2 clearfix"></div>
                    </div>
                </form>
            </div>
        </div>
</div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<div class="popup_bg hide"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>

<script type="text/javascript">
    function zdOrderNo(orderNo){
		$.ajax({
			type:"post",
			url:"/console/finance/fuyou/dzgl/mallbuydzgl/mallBuyQuery.htm?orderNo=" + escape(orderNo),
			data:{"orderNo" : orderNo},
			dataType: 'text',
		    success: succFunction,
			error: erryFunction
		})
		function erryFunction() {  
		    $("div.popup_bg").show();
		    $("#info").html(showDialogInfoReload("系统异常，请稍后重试！", "error"));
	    }  
		function succFunction(msg) { 
			if (msg == "SUCCESS"){
			    $("div.popup_bg").show();
			    $("#info").html(showDialogInfoReload("对账成功！", "yes"));
			} else {
			    $("div.popup_bg").show();
			    $("#info").html(showDialogInfoReload(msg, "wrong"));
                //$('#form1').submit();
// 				$("#dialog").show();
			}
        }  
	}
</script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({inline: true});
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startExpireDatetime"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endExpireDatetime"));%>");
    });
</script>
</body>
</html>