<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.DshScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@page import="com.dimeng.p2p.S63.enums.T6359_F08" %>
<%@page import="com.dimeng.p2p.S63.enums.T6352_F06" %>
<%@page import="com.dimeng.p2p.repeater.score.CommodityCategoryManage" %>
<%@page import="java.util.List" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.DshScoreOrderDetails" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.ExportDSHDD" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.UpdateOrder" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.ApproveOrder" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics" %>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F07" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "DSHDD";
    CommodityCategoryManage ccmanage = serviceSession.getService(CommodityCategoryManage.class);
    List<T6350> t6350s = ccmanage.getT6350List("");
    PagingResult<ScoreOrderMainRecord> list = (PagingResult<ScoreOrderMainRecord>) request.getAttribute("mainRecords");
    ScoreOrderMainRecord[] mainRecords = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
    ScoreOrderStatistics sos = (ScoreOrderStatistics)request.getAttribute("ScoreOrderStatistics");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, DshScoreOrderList.class)%>" method="post">
        <input type="hidden" name="status" value="<%=T6359_F08.pendding.name() %>" >
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>待审核</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">订单号</span>
			          <input type="text" name="orderNo" value="<%StringHelper.filterHTML(out, request.getParameter("orderNo"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">用户名</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">手机号码</span>
			          <input type="text" name="phoneNum" value="<%StringHelper.filterHTML(out, request.getParameter("phoneNum"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">成交时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li><span class="display-ib mr5">商品名称</span>
			          <input type="text" name="productName" value="<%StringHelper.filterHTML(out, request.getParameter("productName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">支付方式</span>
			          <select name="payType" class="border mr10 h32 mw100">
                                	<option value="">全部</option>
                                	<%for(T6352_F06 t6352_F06:T6352_F06.values()) {%>
                                		<option value="<%=t6352_F06.name() %>" <%if (t6352_F06.name().equals(request.getParameter("payType"))) {%> selected="selected" <%}%>><%=t6352_F06.getChineseName()%></option>
                                	<%} %>
                                </select>
			        </li>
			        <li><span class="display-ib mr5">商品类别</span>
			          <select name="productType" class="border mr10 h32 mw100">
                                	<option value="">全部</option>
                                	<%if(t6350s!=null){for (T6350 t6350 : t6350s) {%>
									<option value="<%=t6350.F02%>" <%if (t6350.F02.equals(request.getParameter("productType"))) {%> selected="selected" <%}%>><%=t6350.F03%></option>
									<%}}%>  
                                </select>
			        </li>
			        <%-- <li><span class="display-ib mr5">状态</span>
			          <select name="status" class="border mr10 h32 mw100">
                                	<option value="">全部</option>
                                	<%for(T6359_F08 t6359_F08:T6359_F08.values()) {%>
                                		<option value="<%=t6359_F08.name() %>" <%if (t6359_F08.name().equals(request.getParameter("status"))) {%> selected="selected" <%}%>><%=t6359_F08.getChineseName()%></option>
                                	<%} %>
                                </select>
			        </li> --%>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
											<%
	                        if (dimengSession.isAccessableResource(ApproveOrder.class)) {
	                    %> <a href="javascript:batchPlsh();"
											class="btn btn-blue2 radius-6 mr5 pl10 pr10 popup-link"
											data-wh="1000*600">批量审核</a> <%
	                        } else {
	                    %> <a href="javascript:void(0);"
											class="btn btn-gray radius-6 mr5 pl10 pr10">批量审核</a> <%
	                        }
	                    %>
										</li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportDSHDD.class)) {
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
			          <th>成交时间</th>
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
                          <td><input type="checkbox" name="orderId" value="<%=mainRecord.id%>" /></td>
                          <td><%=i++%></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.orderNo+""); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.loginName); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.realName); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.phoneNum); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.productName); %></td>
                          <td><%StringHelper.filterHTML(out, mainRecord.productTypeName); %></td>
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
                          <td>
                          	<%if (dimengSession.isAccessableResource(ApproveOrder.class)) { %>
                          		<a href="javascript:void(0)" onclick="showData(<%=mainRecord.id%>)" data-wh="800*600" class="link-blue mr20 popup-link">审核</a>
                          	<%}else{ %>
                          		<a href="javascript:void(0)" class="disabled mr20 popup-link">审核</a>
                          	<%} %>
                          	<%if (dimengSession.isAccessableResource(UpdateOrder.class)) { %>
                          		<a href="javascript:void(0)" onclick="showUpdateDataDiv(<%=mainRecord.id%>)" <%if(mainRecord.productType!=null && mainRecord.productType.name().equals(T6350_F07.kind.name())){%>data-wh="600*500"<%}else{%>data-wh="600*320"<%}%>
                                   class="link-blue mr20 popup-link">修改</a>
                          	<%}else{ %>
                          		<a href="javascript:void(0)" class="disabled mr20 popup-link">修改</a>
                          	<%} %>
                          </td>
                     </tr>
                     <%} }else{%>
                      <tr class="dhsbg"><td colspan="14" class="tc">暂无数据</td></tr>
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

<div id="showDataId" style="display: none;">
    <div class="popup-box" style="min-height: 270px; width: 800px; max-height: 600px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">订单详情</h3>
        <a class="icon-i popup-close2" onclick="closeShowData()"></a></div>
    <div class="popup-content-container-2" style="max-height:550px;">
        <div class="p30">
            <p class="blue f16 fb pb10">订单信息</p>
            <ul class="gray6">
                <li class="mb15">
                	<span class="display-ib tr">订单号：</span>
                    <span class="pr40" id="orderNo"></span>
                    <span class="display-ib tr">充值手机号：</span>
                    <span class="pr40" id="chargePhoneNum"></span>
                </li>
                <li class="mb15">
                	<span class="display-ib tr">用户名：</span>
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

            <div class="pt20" id="shxxDiv" style="display: none;">
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

            <form id="form3" action="<%=controller.getURI(request, ApproveOrder.class)%>" method="post" class="form3" onsubmit="return doApproveOrder();">
                <div class="pt20">
                    <input type="hidden" name="scoreOrderId" id="scoreOrderId"/>
                    <input type="hidden" name="zipCode" value=""/>
                    <ul class="gray6">
                        <li class="mb15"><span class="display-ib tr mr5 fl"><em
                                class="red mr5">*</em>审核：</span>
                                <input name="approve" onclick="checkedRadio(this)" type="radio" class="pr5" value="pass" checked="checked">通过审核
                                <input name="approve" onclick="checkedRadio(this)" type="radio" class="pr5" value="nopass">未通过审核
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 fl"><em class="mr5" id="checkedRadioId"></em>备注：</span>
                            <textarea id="reason" name="reason" cols="45" rows="5"
                                      class="w400 h120 border p5 min-length-2 max-length-100"></textarea>
                            <span tip id="nopassErrorTips">2-100字</span>
                            <span errortip class="" style="display: none"></span>
                        </li>
                    </ul>
                </div>
                <%--关闭--%>
                <div class="tc f16">
                    <input type="submit" fromname="form3" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" value="确定" />
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);" onclick="closeShowData()">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeShowData();">
                </div>
            </form>
        </div>
    </div>
</div>
    </div>

<div id="updateDataId" style="display: none;">
    <div class="popup-box">
        <div class="popup-title-container">
            <h3 class="pl20 f18">订单修改</h3>
            <a class="icon-i popup-close2" onclick="closeUpdateData()"></a>
        </div>
        <div class="popup-content-container-2" style="max-height:550px;">
            <form action="<%=controller.getURI(request, UpdateOrder.class)%>" method="post" class="form2">
                <input type="hidden" name="updateScoreOrderId" id="updateScoreOrderId"/>
                <input type="hidden" name="localUrl" value="<%=controller.getURI(request, DshScoreOrderList.class) %>"/>
                <input id="shengId" value="" type="hidden"/>
                <input id="shiId" value="" type="hidden"/>
                <input id="xianId" value="" type="hidden"/>
                <div class="p30">
                    <ul class="gray6">
                        <li class="mb15 kind"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>收货人：</span>
                            <div class="pl120">
                                <input id="updateReceiver" name="updateReceiver" type="text" maxlength="30"
                                       class="text border w300 pl5 required max-length-30" mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法姓名"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 kind"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>所在地区：</span>
                            <div class="pl120">
                                <input type="hidden" class="text border pl5 mr20" id="updateAddressMain" name="updateAddressMain"></input>
                                <select name="sheng" class="border mr10 h32 mw100 required"></select>
                                <select name="shi" class="border mr10 h32 required"></select>
                                <select name="xian" class="border mr10 h32 required" id="xianSlt" name="xianSlt"></select>
                                <span tip></span>
                                <span errortip class="" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 kind"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>详细地址：</span>
                            <div class="pl120">
                                <input id="updateAddressDetail" name="updateAddressDetail" type="text"
                                       class="text border w300 pl5 required max-length-100" maxlength="100"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 kind"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>联系电话：</span>
                            <div class="pl120">
                                <input id="updateReceiverPhoneNum" name="updateReceiverPhoneNum" type="text"
                                       class="text border w300 pl5 required max-length-11 phonenumber"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 kind"><span class="display-ib tr mr5 w120 fl">邮编：</span>
                            <div class="pl120">
                                <input id="updateZipCode" name="updateZipCode" type="text"
                                       class="text border w300 pl5 max-length-6 min-length-6" maxlength="6" onkeyup="value=value.replace(/\D/g,'')"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 fee"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>充值手机号码：</span>
                            <div class="pl120">
                                <input id="updateChargePhoneNum" maxlength="11" name="updateChargePhoneNum" type="text"
                                       class="text border w300 pl5 required mobile"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                                class="red mr5">*</em>备注：</span>
                            <div class="pl120">
                            <textarea id="updateReason" name="updateReason"
                                      class="w300 h60 border p5 required min-length-2 max-length-100"></textarea>
                                <span tip>2-100字</span>
                                <span errortip class="" style="display: none"></span>
                            </div>
                        </li>
                    </ul>

                    <div class="tc f16">
                        <input type="submit" value="确定"
                               class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                               fromname="form2"/>
                        <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeUpdateData();">取消</a> -->
                        <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeUpdateData();">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="popup_bg div_popup_bg" style="display: none;"></div>
	<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/scoreOrder.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">

	var nopass_tips = "请填写未审核通过的原因 最大100个字符";
	
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        
        $("#reason").blur(function(){
        	var val = $("input[name='approve']:checked").val();
        	if(val == "nopass"){
        		if($(this).val() == ""){
        			$(this).val(nopass_tips).addClass("gray9");
        		}
        	}
        }).focus(function(){
        	$("#nopassErrorTips").removeClass("error_tip");
        	$("#nopassErrorTips").html("2-100字");
        	var val = $("input[name='approve']:checked").val();
        	if(val == "nopass"){
        		if($(this).val() == nopass_tips){
        			$(this).val("").removeClass("gray9");
        		}
        	}
        });
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportDSHDD.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, DshScoreOrderList.class)%>";
    }
    
    function showData(scoreOrderId)
	{
		$("#showDataId").show();
		$("div.popup_bg").show();
		
		//默认第一个选中
		$("input[name='approve']:eq(0)").attr("checked","checked");
		checkedRadio($("input[name='approve']"));
		
		var ajaxUrl  = "<%=controller.getURI(request, DshScoreOrderDetails.class)%>";
        $("#scoreOrderId").val(scoreOrderId);
		var data = {"scoreOrderId":scoreOrderId};
		postService(ajaxUrl,data,"examine");
	}
    function showUpdateDataDiv(scoreOrderId)
	{

		var ajaxUrl  = "<%=controller.getURI(request, DshScoreOrderDetails.class)%>";
		var data = {"scoreOrderId":scoreOrderId};
        $(".form2")[0].reset();
		postServiceForUpdate(ajaxUrl,data);
		$("#updateDataId").show();
		$("div.popup_bg").show();
		$("#updateScoreOrderId").val(scoreOrderId);
	}
    
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
    
    function batchPlsh() {
        var ckeds = $("input:checkbox[name='orderId']:checked");
        if (ckeds == null || ckeds.length <= 0) {
            $(".popup_bg").show();
            $("#info").html(showPLSHInfo("请选择审核订单!", "wrong"));
            return;
        }else{
        	$(".popup_bg").show();
			$("#info").html(showConfirmPLSHDiv("是否对选中"+ckeds.length+"笔订单进行批量审核 ？","",'form3'));
        }
    }
    
    function showPLSHInfo(msg,type){
		return '<div class="plsh-box popup-box" style="width:420px;min-height:200px;"> <div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
	    '<a class="icon-i popup-close2" onclick="closePLSHDiv()"></a></div>'+
	     ' <div class="popup-content-container pb20 clearfix">'+
	      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="closePLSHDiv()" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a></div> '+
	'</div></div>';
	}
    
    function showConfirmPLSHDiv(msg,param,type){
		return '<div class="plsh-box popup-box" style="width:420px;min-height:200px;"> <div class="popup-title-container">'+
		'<h3 class="pl20 f18">提示</h3>'+
	    '<a class="icon-i popup-close2" onclick="closePLSHDiv()"></a></div>'+
	     ' <div class="popup-content-container pb20 clearfix">'+
	      '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
	     ' <div class="tc f16"> <a href="javascript:void(0);" onclick="toConfirm(\''+param+'\',\''+type+'\');" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a><a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closePLSHDiv()">取消</a> </div> '+
	'</div></div>';
	}
    
  //窗口关闭
    function closePLSHDiv(){
    	$(".plsh-box").hide();
    	$(".popup_bg").hide();
    }
    
    function toConfirm(param,type){
		$("#info").html("");
		var scoreOrderIds = "";
		var ckeds = $("input:checkbox[name='orderId']:checked");
        for (var i = 0; i < ckeds.length; i++) {
            var val = $(ckeds[i]).val();
            if (i == 0) {
            	scoreOrderIds += val;
            } else {
            	scoreOrderIds += "," + val;
            }
        }
        if(!isEmpty(scoreOrderIds)){
        	var form = $("#form3");
    		var hidden = $("<input type='hidden' name='scoreOrderIds' id='scoreOrderIds'/ value='"+scoreOrderIds+"'>");
    		hidden.appendTo(form);
        }
		$("#"+type).submit();
	}
    
    function checkedRadio(obj)
    {
        var value = $(obj).val();
        $("span[tip]").show();
        $("span[errortip]").hide();

        if(value == "nopass")
        {
        	$("#reason").parent().show();
            $("#reason").addClass("required");
            $("#checkedRadioId").text("*");
            $("#checkedRadioId").addClass("red");
            if($("#reason").val() == ""){
            	$("#reason").val(nopass_tips).addClass("gray9");
            }
        }
        else
        {
        	$("#reason").parent().hide();
            $("#reason").removeClass("required");
            $("#checkedRadioId").removeClass("red");
            $("#checkedRadioId").text("");
            $("#reason").val("").removeClass("gray9");
        }
    }
    
    function doApproveOrder(){
    	$("input[name='zipCode']").val($("#zipCode").text());
    	var val = $("input[name='approve']:checked").val();
    	if(val == "nopass" && $("#reason").val() == nopass_tips){
    		$("#nopassErrorTips").addClass("error_tip");
    		$("#nopassErrorTips").html("请填写未审核通过的原因");
    		return false;
    	}
    	$("#nopassErrorTips").removeClass("error_tip");
    	$("#nopassErrorTips").html("2-100字");
    	return true;
    }
</script>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showPLSHInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>