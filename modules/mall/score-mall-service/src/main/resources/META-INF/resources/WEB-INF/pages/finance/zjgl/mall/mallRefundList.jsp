<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S63.enums.T6350_F07"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.mall.MallRefundList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@page import="com.dimeng.p2p.S63.enums.T6359_F08" %>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.MallRefund" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.mall.ExportMallRefund" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yth.YthOrderDetails" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.mall.CheckMallRefund" %>
<%@ page import="com.dimeng.p2p.common.FormToken" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "SCTKGL";
    PagingResult<MallRefund> result = ObjectHelper.convert(request.getAttribute("mainRecords"), PagingResult.class);
    List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, MallRefundList.class)%>" method="post">
          <%=FormToken.hidden(serviceSession.getSession()) %>
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>商城退款管理</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">订单号：</span>
			          <input type="text" name="orderNo" value="<%StringHelper.filterHTML(out, request.getParameter("orderNo"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">用户名：</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名：</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">商品名称：</span>
			          <input type="text" name="productName" value="<%StringHelper.filterHTML(out, request.getParameter("productName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">商品类别</span>
			          <select name="productType" class="border mr10 h32 mw100">
                            <option value="">全部</option>
                            <%if(t6350List!=null){for (T6350 t6350 : t6350List) {
                            	if(t6350.F07 == T6350_F07.virtual){
                            	    continue;
                            	}
                            %>
							<option value="<%=t6350.F02%>" <%if (t6350.F02.equals(request.getParameter("productType"))) {%> selected="selected" <%}%>><%=t6350.F03%></option>
							<%}}%>  
                      </select>
			        </li>
			        <li><span class="display-ib mr5">状态</span>
			          <select name="status" class="border mr10 h32 mw100">
                          <option value="">全部</option>
                          <%for(T6359_F08 t6359_F08:T6359_F08.values()) {%>
                          <%if("refunding".equals(t6359_F08.name()) || "norefund".equals(t6359_F08.name()) || "refund".equals(t6359_F08.name()))
                          {%>
                          	<option value="<%=t6359_F08.name() %>" <%if (t6359_F08.name().equals(request.getParameter("status"))) {%> selected="selected" <%}%>><%=t6359_F08.getChineseName()%></option>
                          <%}} %>
                      </select>
			        </li>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportMallRefund.class)) {
                     	%>
                     	<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                     	<%
                     		}else{
                     	%>
                     	<span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                     	<%
                     		}
                     	%>	
                </li>
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
			          <th>商品名称</th>
			          <th>商品类别</th>
			          <th>价值(元)</th>
			          <th>数量</th>
			          <th>退款金额(元)</th>
			          <th>状态</th>
			          <th class="w200" style="text-align: center;">操作</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			     	 <%
			     	       MallRefund[] lists = result.getItems();
                           if (lists != null && lists.length>0) {
                               
                               int index = 1;
                               for (MallRefund mallRefund : lists) {
                     %>
                     <tr class="tc">
                          <td><%=index++%></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.orderNo); %></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.loginName); %></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.realName); %></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.phoneNum); %></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.productName); %></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.productTypeName); %></td>
                          <td><%=Formater.formatAmount(mallRefund.worth)%></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.quantity+""); %></td>
                          <td><%=Formater.formatAmount(mallRefund.refundAmount)%></td>
                          <td><%StringHelper.filterHTML(out, mallRefund.status.getChineseName()); %></td>
                          <td>
                            <a href="javascript:void(0)" onclick="showData(<%=mallRefund.id%>)" class="link-blue mr10 popup-link" data-wh="800*600">详情</a>
                            <% if(T6359_F08.refunding.name().equals(mallRefund.status.name()) && dimengSession.isAccessableResource(CheckMallRefund.class)){%>
                            <a href="javascript:void(0)" onclick="onConfirmTg('<%=configureProvider.format(URLVariable.CHECKMALLREFUND_URL)%>?id=<%=mallRefund.id%>&status=<%=T6359_F08.refund.name()%>');" class="link-blue popup-link mr10" >退款</a>
                            <a href="javascript:void(0)" onclick="onConfirmBtg('<%=configureProvider.format(URLVariable.CHECKMALLREFUND_URL)%>','<%=mallRefund.id%>','<%=T6359_F08.norefund.name()%>');" class="link-blue popup-link" >不退款</a>
                            <%} else{%>
                            <span class="disabled mr10">退款</span>
                            <span class="disabled">不退款</span>
                            <%} %>
                            
                          </td>
                     </tr>
                     <% }} else {%>
                      <tr class="dhsbg"><td colspan="12" class="tc">暂无数据</td></tr>
                     <%} %>
                    </tbody>
                    </table>
                   	</div>
                    <div class="mb10 mt5">
                        <span class="mr30">购买金额总计：<em
                                class="red"><%=(lists!=null?Formater.formatAmount(lists[0].buyAllAmount):0)%>
                        </em> 元</span>
                        <span class="mr30">退款金额总计：<em
                                class="red"><%=(lists!=null?Formater.formatAmount(lists[0].refundAllAmount):0)%>
                        </em> 元</span>
                    </div>

                    <!--分页-->
                    <%
                        AbstractConsoleServlet.rendPagingResult(out, result);
                    %>
                    <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<div id="refundView">
</div>

<div id="showDataId" style="display: none;">
    <div class="popup-box" style="min-height: 270px; width: 800px; max-height: 600px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">订单详情</h3>
        <a class="icon-i popup-close2" onclick="closeShowData()"></a></div>
    <div class="popup-content-container-2" style="max-height:550px;">
        <div class="p30">
            <p class="blue f16 fb pb10">订单信息</p>
            <ul class="gray6">
                <li class="mb15"><span class="display-ib tr">订单号：</span>
                    <span class="" id="orderNo"></span>
                </li>
                <li class="mb15"><span class="display-ib tr">用户名：</span>
                    <span class="pr40" id="loginName"></span>
                    <span class="display-ib tr">真实姓名：</span>
                    <span class="pr40" id="realName"></span>
                    <span class="display-ib tr">手机号码：</span>
                    <span class="pr40" id="phoneNum"></span>
                </li>
            </ul>
            <div class="border table-container">
                <table class="table table-style gray6 tl">
                    <thead class="fb">
                    <tr class="title">
                        <th class="tc">商品编码</th>
                        <th class="tc">商品名称</th>
                        <th class="tc">价值</th>
                        <th class="tc">支付方式</th>
                        <th class="tc">数量</th>
                    </tr>
                    </thead>
                    <tbody class="f12">
                    <tr>
                        <td class="tc" id="productNum"></td>
                        <td class="tc" id="productName"></td>
                        <td class="tc" id="worth"></td>
                        <td class="tc" id="payType"></td>
                        <td class="tc" id="quantity"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="pt20 pb10 border-b-s" id="shxxDiv" style="display: none;">
                <p class="blue f16 fb pb10">收货信息</p>
                <ul class="gray6">
                    <li class="mb15"><span class="display-ib tr">收货人：</span>
                        <span class="" id="receiver"></span>
                    </li>
                    <li class="mb15"><span class="display-ib tr">所在地区：</span>
                        <span class="" id="addressMain"></span>
                    </li>
                    <li class="mb15"><span class="display-ib tr">详细地址：</span>
                        <span class="" id="addressDetail"></span>
                    </li>
                    <li class="mb15"><span class="display-ib tr">联系电话：</span>
                        <span class="" id="receiverPhoneNum"></span>
                    </li>
                    <li class="mb15"><span class="display-ib tr">邮编：</span>
                        <span class="" id="zipCode"></span>
                    </li>
                </ul>
            </div>
            <div class="pt20 pb10 border-b-s"  id="wlxxDiv" style="display: none;">
                <p class="blue f16 fb pb10">物流信息</p>
                <ul class="gray6">
                    <li class="mb15"><span class="display-ib tr">物流方：</span>
                        <span class="" id="express"></span>
                    </li>
                    <li class="mb15"><span class="display-ib tr">快递单号：</span>
                        <span class="" id="expressNum"></span>
                    </li>
                </ul>
            </div>
            <div class="pt20 pb10"  id="czxxDiv" style="display: none;">
                <p class="blue f16 fb pb10">操作信息</p>

                <div class="border table-container">
                    <table class="table table-style gray6 tl">
                        <thead class="fb">
                        <tr class="title">
                            <th class="tc">序号</th>
                            <th class="tc">操作人</th>
                            <th class="tc">操作类型</th>
                            <th class="tc">备注</th>
                            <th class="tc w120">操作时间</th>
                        </tr>
                        </thead>
                        <tbody class="f12" id="logList">
                        <tr>
                            <td class="tc">1</td>
                            <td>K003</td>
                            <td>审核</td>
                            <td>审核通过</td>
                            <td>2015-05-05 10:00</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <%--关闭--%>
            <div class="tc f16">
                <input type="submit" onclick="closeShowData()" class="btn-blue2 btn white radius-6 pl20 pr20" value="关闭" />
            </div>
        </div>
    </div>
</div>
</div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/scoreOrder.js"></script>
<script type="text/javascript">

    function onSubmit(){
	    $("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
	    $('#form1').submit();
    }
   
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportMallRefund.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, MallRefundList.class)%>";
    }
    
    function closeRefund(){
    	$("#refundView").html('');
    }
    

    function showData(scoreOrderId)
    {
        $("#showDataId").show();
        $(".popup_bg").show();
        var ajaxUrl  = "<%=controller.getURI(request, YthOrderDetails.class)%>";
        var data = {"scoreOrderId":scoreOrderId};
        postService(ajaxUrl,data);
    }

    function closeShowData()
    {
        $("#showDataId").hide();
        $(".popup_bg").hide();
    }
    
    function onConfirmTg(url){
        var token_id = '<%=FormToken.parameterName()%>';
    	var token_val = $("input[name="+token_id+"]").val();
    	$("#info").html(showForwardInfo("确定退款？","question",url+"&"+token_id+"="+token_val));
    	$("div.popup_bg").show();
    }
    
    function onConfirmBtg(url,id,status){
    	var token_id = '<%=FormToken.parameterName()%>';
     	var token_val = $("input[name="+token_id+"]").val();
    	var html = "";
    	html += "<form id='submitRefuse' action='"+url+"' method='post'>";
    	html += "<input type='hidden' name='id' value='"+id+"' />";
    	html += "<input type='hidden' name='status' value='"+status+"' />";
    	html += "<input type='hidden' name='"+token_id+"' value='"+token_val+"' />";
    	html += "<div class='popup-box' style='width:550px;'>";
    	html += "<div class='popup-title-container'>";
    	html += "<h3 class='pl20 f18'>不退款</h3>";
    	html += "<a class='icon-i popup-close2' href='javascript:void(0);' onclick='closeRefund();'></a>";
    	html += "</div>";
    	html += "<div class='popup-content-container pt20 ob20 clearfix'>";
    	html += "<div class='mb40 gray6'>";
    	html += "<ul>";
    	html += "<li class='mb10 pr'><span class='pa display-ib tr mr5'><em class='red'>*</em>备注：</span>";
    	html += "<div class='pl60'><textarea name='reason' cols='40' rows='4' autofocus placeholder='请填写拒绝原因最大100个字符' class='w400 h120 border p5 required max-length-100'></textarea>";
    	html += "<span class='ml5' id='errorInfo'>最大100个字</span></div></li>";
    	html += "<li class='mb10 pt10'>";
    	html += "<a class='btn-blue2 btn white radius-6 pl20 pr20 mr30 ml150' onclick='submitRefuse();' href='javascript:void(0);'>确定</a>";
    	html += "<a class='btn-blue2 btn white radius-6 pl20 pr20' onclick='closeRefund();' href='javascript:void(0);'>取消</a>";
    	html += "</li>";
    	html += "</ul>";
    	html += "</div>";
    	html += "</div>";
    	html += "</div>";
    	html += "<div class='popup_bg'></div>";
    	html += "</form>";
    	$("#refundView").html(html);
    }
      
    //兼容IE8
    String.prototype.trim=function(){
  　　    return this.replace(/(^\s*)|(\s*$)/g,"");
  　　    }
    
    function submitRefuse(){
    	var reason = $("textarea[name='reason']").val();
    	if(isEmpty(reason.trim())){
    		$("#errorInfo").html('不能为空').addClass('red');
    		return false;
    	}
    	if(reason.trim().length>100){
    		$("#errorInfo").html('超过最大100个字').addClass('red');
    		return false;
    	}
    	$("#submitRefuse").submit();
    }
    
    //关闭退款窗口
    function closeInfo(){
    	$(".popup_bg").hide();
    	$("#info").html('');
    }
    
</script>
<%	
		String message = controller.getPrompt(request, response, PromptLevel.WARRING); 
		if(!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"wrong"));
</script>
<%} %>
</body>
</html>