<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.DfhScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.BatchSipping"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.BatchSippingList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.ConfirmOrder"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.UpdateDfhOrder"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.SippingOrder"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.S63.entities.T6350"%>
<%@page import="com.dimeng.p2p.S63.enums.T6352_F06"%>
<%@page import="com.dimeng.p2p.repeater.score.CommodityCategoryManage"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.ExportDfhDD"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.DfhScoreOrderDetails"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics"%>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F07"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "DFHDD";
    CommodityCategoryManage ccmanage = serviceSession.getService(CommodityCategoryManage.class);
    List<T6350> t6350s = ccmanage.getT6350List("");
    String status=(String)request.getAttribute("status");
    PagingResult<ScoreOrderMainRecord> list = (PagingResult<ScoreOrderMainRecord>) request.getAttribute("mainRecords");
    ScoreOrderMainRecord[] mainRecords = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("shStartTime");
    String createTimeEnd = request.getParameter("shEndTime");
    ScoreOrderStatistics sos = (ScoreOrderStatistics)request.getAttribute("ScoreOrderStatistics");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<form id="form1"
						action="<%=controller.getURI(request, DfhScoreOrderList.class)%>"
						method="post">
						 <input type="hidden" name="status" value="<%=status %>" >
						<div class="p20">
							<div class="border">
								<div class="title-container">
									<i class="icon-i w30 h30 va-middle title-left-icon"></i>待发货
								</div>
								<div class="content-container pl40 pt30 pr40">
									<ul class="gray6 input-list-container clearfix">
										<li><span class="display-ib mr5">订单号</span> <input
											type="text" name="orderNo"
											value="<%StringHelper.filterHTML(out, request.getParameter("orderNo"));%>"
											class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">用户名</span> <input
											type="text" name="loginName"
											value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>"
											class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">真实姓名</span> <input
											type="text" name="realName"
											value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"
											class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">手机号码</span> <input
											type="text" name="phoneNum"
											value="<%StringHelper.filterHTML(out, request.getParameter("phoneNum"));%>"
											class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">商品名称</span> <input
											type="text" name="productName"
											value="<%StringHelper.filterHTML(out, request.getParameter("productName"));%>"
											class="text border pl5 mr20" /></li>
										<li>
											<span class="display-ib mr5">支付方式</span> 
											<select name="payType" class="border mr10 h32 mw100">
												<option value="">全部</option>
												<%
			                                	    for(T6352_F06 t6352_F06:T6352_F06.values()) {
			                                	%>
													<option value="<%=t6352_F06.name()%>"
														<%if (t6352_F06.name().equals(request.getParameter("payType"))) {%>
														selected="selected" <%}%>><%=t6352_F06.getChineseName()%></option>
												<%
			                                	    }
			                                	%>
											</select>
										</li>
										<li>
											<span class="display-ib mr5">商品类别</span> 
											<select name="productType" class="border mr10 h32 mw100">
												<option value="">全部</option>
												<% if(t6350s!=null){for (T6350 t6350 : t6350s) {%>
												<option value="<%=t6350.F02%>"
													<%if (t6350.F02.equals(request.getParameter("productType"))) {%>
													selected="selected" <%}%>><%=t6350.F03%></option>
												<%}}%>
											</select>
										</li>
										<li><span class="display-ib mr5">审核时间</span> <input
											type="text" name="shStartTime" readonly="readonly"
											id="datepicker1" class="text border pl5 w120 date" /> <span
											class="pl5 pr5">至</span> <input readonly="readonly"
											type="text" name="shEndTime" id="datepicker2"
											class="text border pl5 w120 mr20 date" /></li>
										<li><a href="javascript:onSubmit();"
											class="btn btn-blue radius-6 mr5 pl1 pr15"><i
												class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
										<li>
											<%
						                        if (dimengSession.isAccessableResource(BatchSippingList.class)) {
						                    %> 
						                    <a href="javascript:batchPlfh();" class="btn btn-blue2 radius-6 mr5 pl10 pr10" data-wh="1000*600">批量发货</a> 
											<%
						                        } else {
						                    %> 
						                    <span class="btn btn-gray radius-6 mr5 pl10 pr10">批量发货</span> 
						                    <%
						                        }
						                    %>
										</li>
										<li>
											<%
						                	    if (dimengSession.isAccessableResource(ExportDfhDD.class)) {
						                	%> 
						                		<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15">
						                		<i class="icon-i w30 h30 va-middle export-icon "></i>导出</a> 
					                		<%
					                     	    }else{
					                     	%> 
					                     		<span class="btn btn-gray radius-6 mr5  pl1 pr15">
					                     		<i class="icon-i w30 h30 va-middle export-icon "></i>导出</span> 
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
											<th><input type="checkbox" id="checkAll" class="mr5" /></th>
											<th>序号</th>
											<th>订单号</th>
											<th>用户名</th>
											<th>真实姓名</th>
											<th>手机号码</th>
											<th>商品名称</th>
											<th>商品类别</th>
											<th>价值</th>
											<th>支付方式</th>
											<th>数量</th>
											<th>状态</th>
											<th>审核时间</th>
											<th class="w200" style="text-align: center;">操作</th>
										</tr>
									</thead>
									<tbody class="f12">
										<%
			     	     				if (mainRecords != null){
			     	                   		int i = 1;
			     	                   		for(ScoreOrderMainRecord mainRecord :mainRecords){
			     	 					%>
										<tr class="tc">
											<td><input type="checkbox" name="orderId"
												value="<%=mainRecord.id%>" /></td>
											<td><%=i++%></td>
											<td>
											<% StringHelper.filterHTML(out, mainRecord.orderNo+""); %>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.loginName);%>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.realName); %>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.phoneNum);%>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.productName);%>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.productTypeName);%>
											</td>
											<% if(T6352_F06.score.name().equals(mainRecord.payType.name()))
											{%>
											<td><%StringHelper.filterHTML(out, mainRecord.worth.toString().substring(0, mainRecord.worth.toString().length()-3)); %></td>
											<% }
											else
											{%>
												<td><%StringHelper.filterHTML(out, mainRecord.worth.toString()); %></td>
											<%} %>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.payType.getChineseName());%>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.quantity+"");%>
											</td>
											<td>
											<%StringHelper.filterHTML(out, mainRecord.status.getChineseName());%>
											</td>
											<td><%=DateTimeParser.format(mainRecord.shTime)%></td>
											<td>
											<%if (dimengSession.isAccessableResource(ConfirmOrder.class)) {%> 
											<a href="javascript:void(0)"
												onclick="showData(<%=mainRecord.id%>)" data-wh="800*600"
												class="link-blue mr20 popup-link">发货</a> 
											<%}else{%> 
											<a href="javascript:void(0)" class="disabled mr20">发货</a> 
											<%}%> 
											<% if (dimengSession.isAccessableResource(UpdateDfhOrder.class)) {%> 
											<a href="javascript:void(0)"
												onclick="showUpdateDataDiv(<%=mainRecord.id%>)"
												<%if(mainRecord.productType!=null && mainRecord.productType.name().equals(T6350_F07.kind.name())){%>
												data-wh="600*500" <%}else{%> data-wh="600*320" <%}%>
												class="link-blue mr20 popup-link">修改</a> 
											<%
				                          	    }else{
				                          	%> 
				                          	<a href="javascript:void(0)" class="disabled mr20">修改</a> 
				                          	<%
				                          	    }
				                          	%>
											</td>
										</tr>
										<%
					                         } }else{
					                     %>
										<tr class="dhsbg">
											<td colspan="14" class="tc">暂无数据</td>
										</tr>
										<%
					                         }
					                     %>
									</tbody>
								</table>
							</div>
							<div class="clear"></div>
							<div class="mb10 pt10">
								<span class="mr30">兑换积分总计：<em class="red"><%=sos.scoreAmount.setScale(0, BigDecimal.ROUND_HALF_UP)%>
								</em> 分
								</span> <span class="mr30">余额购买总计：<em class="red"><%=Formater.formatAmount(sos.balanceAmount)%>
								</em> 元
								</span> <span class="mr30">商品数量总计：<em class="red"><%=sos.count%></em></span>
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

	<div id="showDataId" style="display: none;">
		<div class="popup-box div_popup_box"
			style="min-height: 270px; width: 800px; max-height: 600px;">
			<div class="popup-title-container">
				<h3 class="pl20 f18">订单发货</h3>
				<a class="icon-i popup-close2" onclick="closeShowData()"></a>
			</div>
			<form action="<%=controller.getURI(request, ConfirmOrder.class)%>"
				method="post" id="form1" class="form1" onsubmit="return checkData();">
				<input type="hidden" name="scoreOrderId" id="scoreOrderId" />
				<input type="hidden" name="zipCode" value="" /> <input
					type="hidden" name="localUrl"
					value="<%=controller.getURI(request, DfhScoreOrderList.class)%>" />
				<div class="popup-content-container-2" style="max-height: 550px;">
					<div class="p30">
						<p class="blue f16 fb pb10">订单信息</p>
						<ul class="gray6">
							<li class="mb15">
								<span class="display-ib tr">订单号：</span>
                                <span class="pr40" id="orderNo"></span>
                                <span class="display-ib tr">充值手机号：</span>
                                <span class="pr40" id="chargePhoneNum"></span>
							</li>
							<li class="mb15"><span class="display-ib tr">用户名：</span> <span
								class="pr40" id="loginName"></span> <span class="display-ib tr">真实姓名：</span>
								<span class="pr40" id="realName"></span> <span
								class="display-ib tr">手机号码：</span> <span class="pr40"
								id="phoneNum"></span></li>
						</ul>


						<div class="border table-container">
							<table class="table table-style gray6 tl">
								<thead class="fb">
									<tr class="title tc">
										<th>商品编码</th>
										<th>商品名称</th>
										<th class="">价值</th>
										<th class="">支付方式</th>
										<th class="">数量</th>
									</tr>
								</thead>
								<tbody class="f12">
									<tr class="tc">
										<td id="productNum"></td>
										<td id="productName"></td>
										<td id="worth"></td>
										<td id="payType"></td>
										<td id="quantity"></td>
									</tr>
								</tbody>
							</table>
						</div>

						<div class="pt20 pb10 border-b-s" id="shxxDiv"
							style="display: none;">
							<p class="blue f16 fb pb10">收货信息</p>
							<ul class="gray6">
								<li class="mb15"><span class="display-ib tr">收货人：</span> <span
									class="" id="receiver"></span></li>
								<li class="mb15"><span class="display-ib tr">所在地区：</span> <span
									class="" id="addressMain"></span></li>
								<li class="mb15"><span class="display-ib tr">详细地址：</span> <span
									class="" id="addressDetail"></span></li>
								<li class="mb15"><span class="display-ib tr">联系电话：</span> <span
									class="" id="receiverPhoneNum"></span></li>
								<li class="mb15"><span class="display-ib tr">邮编：</span> <span
									class="" id="zipCode"></span></li>
							</ul>
						</div>

						<div class="pt20 pb10" id="czxxDiv" style="display: none;">
							<p class="blue f16 fb pb10">操作信息</p>

							<div class="border table-container">
								<table class="table table-style gray6 tl">
									<thead class="fb">
										<tr class="title tc">
											<th>序号</th>
											<th>操作人</th>
											<th class="">操作类型</th>
											<th class="">备注</th>
											<th class="w120">操作时间</th>
										</tr>
									</thead>
									<tbody class="f12" id="logList">
										<tr class="tc">
											<td>1</td>
											<td>K003</td>
											<td>审核</td>
											<td>审核通过</td>
											<td>2015-05-05 10:00</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>

						<div class="pt20 pb10" id="wlxxDiv" style="display: none;">
							<p class="blue f16 fb pb10">物流信息</p>
							<ul class="gray6">
								<li class="mb15"><span class="display-ib tr mr15">物流方：</span> <input
									id="express" name="express" maxlength="100" style=""
									value="<%StringHelper.filterHTML(out, request.getParameter("express"));%>"
									class="text border pl5 mr20" /></li>
								<li class="mb15"><span class="display-ib tr">快递单号：</span> <input
									id="expressNum" name="expressNum" maxlength="100" style=""
									value="<%StringHelper.filterHTML(out, request.getParameter("expressNum"));%>"
									class="text border pl5 mr5" /><span class="red errorInfo"></span>
								</li>
							</ul>
						</div>

						<%--关闭--%>
						<div class="tc f16">
							<input type="submit"
								class="btn-blue2 btn white radius-6 pl20 pr20" value="发货" /> 
							<!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
								href="javascript:void(0);" onclick="closeShowData()">取消</a> -->
							<input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeShowData();">
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="popup_bg"></div>
	</div>

	<div id="updateDataId" style="display: none;">
		<div class="popup-box div_popup_box">
			<div class="popup-title-container">
				<h3 class="pl20 f18">订单修改</h3>
				<a class="icon-i popup-close2" onclick="closeUpdateData()"></a>
			</div>
			<div class="popup-content-container-2" style="max-height: 550px;">
				<form action="<%=controller.getURI(request, UpdateDfhOrder.class)%>"
					method="post" class="form2">
					<input type="hidden" name="updateScoreOrderId"
						id="updateScoreOrderId" /> <input type="hidden" name="localUrl"
						value="<%=controller.getURI(request, DfhScoreOrderList.class)%>" />
					<input id="shengId" value="" type="hidden" /> <input id="shiId"
						value="" type="hidden" /> <input id="xianId" value=""
						type="hidden" />
					<div class="p30">
						<ul class="gray6">
							<li class="mb15 kind"><span
								class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>收货人：</span>
								<div class="pl120">
									<input id="updateReceiver" name="updateReceiver" type="text"
										class="text border w300 pl5 required max-length-30" maxlength="30" /> <span
										tip></span> <span errortip class="red" style="display: none"></span>
								</div></li>
							<li class="mb15 kind"><span
								class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>所在地区：</span>
								<div class="pl120">
									<input type="hidden" class="text border pl5 mr20"
										id="updateAddressMain" name="updateAddressMain"></input> <select
										name="sheng" class="border mr10 h32 mw100 required"></select>
									<select name="shi" class="border mr10 h32 required"></select> <select
										name="xian" class="border mr10 h32 required" id="xianSlt"
										name="xianSlt"></select> <span tip></span> <span errortip
										class="" style="display: none"></span>
								</div></li>
							<li class="mb15 kind"><span
								class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>详细地址：</span>
								<div class="pl120">
									<input id="updateAddressDetail" name="updateAddressDetail"
										type="text"
										class="text border w300 pl5 required max-length-100" maxlength="100" /> <span
										tip></span> <span errortip class="red" style="display: none"></span>
								</div></li>
							<li class="mb15 kind"><span
								class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>联系电话：</span>
								<div class="pl120">
									<input id="updateReceiverPhoneNum"
										name="updateReceiverPhoneNum" type="text"
										class="text border w300 pl5 required max-length-11 phonenumber" /> <span
										tip></span> <span errortip class="red" style="display: none"></span>
								</div></li>
							<li class="mb15 kind"><span
								class="display-ib tr mr5 w120 fl">邮编：</span>
								<div class="pl120">
									<input id="updateZipCode" name="updateZipCode" type="text"
										class="text border w300 pl5 max-length-6 min-length-6" maxlength="6" onkeyup="value=value.replace(/\D/g,'')"/> <span tip></span>
									<span errortip class="red" style="display: none"></span>
								</div></li>
							<li class="mb15 fee"><span class="display-ib tr mr5 w120 fl"><em
									class="red mr5">*</em>充值手机号码：</span>
								<div class="pl120">
									<input id="updateChargePhoneNum" name="updateChargePhoneNum"
										type="text"
										class="text border w300 pl5 required max-length-16 mobile" />
									<span tip></span> <span errortip class="red"
										style="display: none"></span>
								</div></li>
							<li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
									class="red mr5">*</em>备注：</span>
								<div class="pl120">
									<textarea id="updateReason" name="updateReason"
										class="w300 h60 border p5 required min-length-2 max-length-100"></textarea>
									<span tip>2-100字</span> <span errortip class=""
										style="display: none"></span>
								</div></li>
						</ul>

						<div class="tc f16">
							<input type="submit" value="确定"
								class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
								fromname="form2" /> 
						<!-- 	<a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
								href="javascript:closeUpdateData();">取消</a> -->
								<input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeUpdateData();">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!--  批量发货弹窗 -->
	<form action="<%=controller.getURI(request, BatchSipping.class)%>"
		method="post" class="form3">
		<div id="batchSippingView" style="display: none;">
			<div class="popup-box batchSippingView "style="min-height: 270px; width: 800px; margin-left:-400px; margin-top:-200px; max-height: 600px;">
				<div class="popup-title-container">
					<h3 class="pl20 f18">批量发货</h3>
					<a class="icon-i popup-close2" onclick="closeBatchSippingView()"></a>
				</div>
				<div class="popup-content-container-2" style="max-height: 450px;">
					<div class="p20" style="min-height: 450px;">
						<div class="pr">
							<a class="btn btn-blue2 radius-6 mr5 pl10 pr10"
								onClick="piliangshuruShow();">批量输入</a>
							<div class="piliangshuru-pa hide">
								<a class="close-btn"><i
									class="icon-i display-ib h30 w30 radius-wrong-icon pa"></i></a>
								<div class="p20 clearfix">
									<div class="border-b-s h30 lh30 f16">提示</div>
									<div class="mb40 gray6 pt20">
										<ul>
											<li class="mb10"><span class="display-ib tr mr5 w90"><em
													class="red pr5">*</em><em class="gray3">物流方：</em></span> <input
												class="text border w200 pl5" maxlength="100" name="publicExpress"
												id="publicExpress" type="text">
												<span tip></span> <span errortip class="red"
										style="display: none"></span>
											</li>
											<li class="mb10">
												<span class="display-ib tr mr5 w90">
													<em class="red pr5">*</em>
													<em class="gray3">快递单号：</em>
												</span>
												<input class="text border w200 pl5" maxlength="100" name="publicExpressNum"
												id="publicExpressNum" type="text"
												mtest="/^\w+$/" mtestmsg="只能输入数字、字母、下划线">
												<br>
												<span class="display-ib tr mr5 w90"></span>
												<span tip></span>
												<span errortip class="red" style="display: none"></span>
											</li>
										</ul>
									</div>
									<div class="tc f16">
										<a href="javascript:void(0);" onclick="update()"
											class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
									</div>
								</div>
							</div>
						</div>
						<span id="errorContent">&nbsp;</span>
						<div class="border mt5 table-container">

							<table class="table table-style gray6 tl" id="batchSippingList">
							</table>

						</div>
						<%--关闭--%>
						<div class="tc pt20">
							<input type="submit"
								class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" value="发货"
								fromname="form3" /> 
								<!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
								href="javascript:closeBatchSippingView();">取消</a> -->
								<input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeBatchSippingView();">
						</div>
					</div>
				</div>
			</div>
			<div class="popup_bg"></div>
		</div>
	</form>

	<div class="popup_bg div_popup_bg" style="display: none;"></div>
	<div id="info"></div>
	<%@include file="/WEB-INF/include/datepicker.jsp"%>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/scoreOrder.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<script type="text/javascript">
	
	/*var testChinese = /.*[\u4e00-\u9fa5]+.*$/;
	function checkData(){
		var expressNum = $.trim($("#expressNum").val());
		if(expressNum && testChinese.test(expressNum)){
			$(".errorInfo").html("不能为中文");
            return false;
        }
		return true;
	}*/

	var testChinese = /^\w+$/;
	function checkData(){
		var expressNum = $.trim($("#expressNum").val());
		if(expressNum && !testChinese.test(expressNum)){
			$(".errorInfo").html("只能输入数字、字母、下划线");
			return false;
		}
		return true;
	}
	
	function onSubmit(){
		$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
		$('#form1').submit();
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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("shStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("shEndTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    
    $("#checkAll").click(function () {

        if ($(this).attr("checked")) {
            $("input:checkbox[name='orderId']").attr("checked", true);
        } else {
            $("input:checkbox[name='orderId']").attr("checked", false);
        }
    });
    
    $("input:checkbox[name='orderId']").click(function(){
		if(!$(this).attr("checked")){
			$("#checkAll").attr("checked",false);
		}else{
			var c1 = $("input:checkbox[name='orderId']:checked").length;
			var c2 = $("input:checkbox[name='orderId']").length;
			if(c1==c2){
				$("#checkAll").attr("checked",true);
			}
		}
	});
    
    function batchPlfh() {
        var ckeds = $("input:checkbox[name='orderId']:checked");
        if (ckeds == null || ckeds.length <= 0) {
            //alert("请选择要处理的记录!");
            $(".popup_bg").show();
            $("#info").html(showInfo("请选择发货订单!", "wrong"));
            return;
        }
        
        $("table#batchSippingList tr").eq(0).nextAll().remove();
        $("#batchSippingView").show();
        $(".batchSippingView").show();
        
        var ids = "";
        for (var i = 0; i < ckeds.length; i++) {
            var val = $(ckeds[i]).val();
            if (i == 0) {
                ids += val;
            } else {
                ids += "," + val;
            }
        }
        var batchSippingUrl = "<%=controller.getURI(request,BatchSippingList.class)%>";
        var batchSippingData = {"ids":ids};
        $.ajax({
    		type:"post",
    		url:batchSippingUrl,
    		data:batchSippingData,
    		async: false ,
    		dataType:"json",
    		success: function(returnData){
    			//展示数据前先清空数据
                $("#batchSippingList tr").empty();
                var trHTML = "<thead class='fb'><tr align='title tl'><th><input type='checkbox' onClick='checkBatchSippingAll(this)' class='mr5'/>全选</th>"
                +"<th>物流方</th><th>快递单号</th><th>订单号</th><th>用户名</th><th>真实姓名</th><th>手机号</th><th>商品编码</th><th>商品名称</th><th>数量</th></tr></thead>";
                $("#batchSippingList").append(trHTML);
                trHTML = "";
    			if (null == returnData.scoreOrderMainRecord) {
                    $("#batchSippingList").append("<tr align='center'><td colspan='10'>暂无数据</td></tr>");
                } else if (returnData.scoreOrderMainRecord.length > 0) {
                    var rAssests = returnData.scoreOrderMainRecord;
                    $.each(rAssests, function (n, value) {
                        trHTML += "<tbody class='f12'><tr class='tl'>"+
                        		"<input type='hidden' name='orderSipping' value='"+value.id+"'/>"+
                        		"<td><input type='checkbox' name='orderSippingId' value='"+value.id+"'/></td>" +
                                "<td><input type='text' id='batchSippingExpress' "+((value.productType=='virtual')?"readonly='readonly'":"")+" maxlength='100' name='batchSippingExpress' style='text' value='' class='text border pl5 mr20' /></td>" +
                                "<td><input type='text' id='batchSippingExpressNum' "+((value.productType=='virtual')?"readonly='readonly'":"")+" maxlength='100' name='batchSippingExpressNum' style='text' value='' class='text border pl5 ' mtest='/^\\w+$/' mtestmsg='只能输入数字、字母、下划线'/><span tip class='mr20'></span><span errortip class='mr20' style='display: none'></span></td>" +
                                "<td>" + value.orderNo + "</td>" +
                                "<td>" + value.loginName + "</td>" +
                                "<td>" + value.realName + "</td>" +
                                "<td>" + value.phoneNum + "</td>" +
                                "<td>" + value.comEncoding + "</td>" +
                                "<td>" + value.productName + "</td>" +
                                "<td>" + value.quantity + "</td></tr></tbody>";
                        $("#batchSippingList").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                    });
                }
    		}
    		});

		$intext = $('#batchSippingView input[type="text"]');
		$intext.focus(function () {
			$(this).blur(function () {
				return checkText($(this));
			});
		});
        
    }

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportDfhDD.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, DfhScoreOrderList.class)%>";
    }
    
    function showData(scoreOrderId)
	{
    	$("#wlxxDiv").find("input").val("");
		$("#showDataId").show();
		$(".div_popup_box").show();
		$(".div_popup_bg").show();
		var ajaxUrl  = "<%=controller.getURI(request, DfhScoreOrderDetails.class)%>";
		var data = {"scoreOrderId":scoreOrderId};
		postService(ajaxUrl,data,"send");
		$("#scoreOrderId").val(scoreOrderId);
	}
    function showUpdateDataDiv(scoreOrderId)
	{
		var ajaxUrl  = "<%=controller.getURI(request, DfhScoreOrderDetails.class)%>";
		var data = {"scoreOrderId":scoreOrderId};
		$(".form2")[0].reset();
		postServiceForUpdate(ajaxUrl,data);
		$("#updateDataId").show();
		$(".div_popup_box").show();
		$(".div_popup_bg").show();
		$("#updateScoreOrderId").val(scoreOrderId);

	}
    
    function piliangshuruShow(){
    	var ckeds = $("input:checkbox[name='orderSippingId']:checked");
    	if (ckeds == null || ckeds.length <= 0) {
    		$("#errorContent").addClass("red");
            $("#errorContent").html("请选择批量输入物流订单");
            return;
        }
    	$("span[errortip]").hide();
    	$("input[name='publicExpress']").val("");
    	$("input[name='publicExpressNum']").val("");
    	$("#errorContent").removeClass("red");
        $("#errorContent").html("");
    	$(".piliangshuru-pa").show();	
    }

    $(".piliangshuru-pa .close-btn").click(function(){
    	$(".piliangshuru-pa").hide();	
    	});
    
    function update(){
    	var publicExpress = $("input[name='publicExpress']").val();
    	var $publicExpress = $("input[name='publicExpress']").nextAll("span[errortip]");
    	var publicExpressNum = $("input[name='publicExpressNum']").val();
    	var $publicExpressNum = $("input[name='publicExpressNum']").nextAll("span[errortip]");
    	
    	if(publicExpress == "" || undefined || null )
    	{
    		$publicExpress.addClass("error_tip");
            $publicExpress.html("不能为空！");
            $publicExpress.show();
            return;
    	}
    	if(publicExpress.length > 100)
    	{
    		$publicExpress.addClass("error_tip");
            $publicExpress.html("超过输入限制" + 100 + "，当前长度为" + publicExpress.length);
            $publicExpress.show();
            return;	
    	}
    	
    	if(publicExpressNum == "" || undefined || null )
    	{
    		$publicExpressNum.addClass("error_tip");
            $publicExpressNum.html("不能为空！");
            $publicExpressNum.show();
            return;		   		
    	}
    	
    	if(publicExpressNum.length > 32)
    	{
    		$publicExpressNum.addClass("error_tip");
            $publicExpressNum.html("超过输入限制" + 32 + "，当前长度为" + publicExpressNum.length);
            $publicExpressNum.show();
            return;		
    	}
    	
    	var ckeds = $("input:checkbox[name='orderSippingId']:checked");
    	if(ckeds != null){
    		for (var i = 0; i < ckeds.length; i++) {
    			$(ckeds[i]).parent().next().find("input[name='batchSippingExpress']").attr("value",publicExpress);
    			$(ckeds[i]).parent().next().next().find("input[name='batchSippingExpressNum']").attr("value",publicExpressNum);
            }
    	}
    	$(".piliangshuru-pa").hide();
    }
    
    function isNull(data)
    {
    	return (data == "" || undefined || null )?"false":"true";
    }
    
    function checkBatchSippingAll(object){
    	if ($(object).attr("checked")) {
            $("input:checkbox[name='orderSippingId']").attr("checked", true);
        } else {
            $("input:checkbox[name='orderSippingId']").attr("checked", false);
        }
    }
</script>
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