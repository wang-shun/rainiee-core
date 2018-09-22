<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6352_F06" %>
<%@page import="com.dimeng.p2p.repeater.score.CommodityCategoryManage" %>
<%@page import="java.util.List" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.ScoreOrderReturn" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.YfhScoreOrderList" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.ScoreOrderModifyLogistics" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.ExportYfhDD" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.YfhOrderDetails" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
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
    CURRENT_SUB_CATEGORY = "YFHDD";
    CommodityCategoryManage ccmanage = serviceSession.getService(CommodityCategoryManage.class);
    List<T6350> t6350s = ccmanage.getT6350List("");
    PagingResult<ScoreOrderMainRecord> list = (PagingResult<ScoreOrderMainRecord>) request.getAttribute("mainRecords");
    ScoreOrderMainRecord[] mainRecords = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("fhStartTime");
    String createTimeEnd = request.getParameter("fhEndTime");
    ScoreOrderStatistics sos = (ScoreOrderStatistics)request.getAttribute("ScoreOrderStatistics");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, YfhScoreOrderList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>已发货
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">订单号</span>
                                        <input type="text" name="orderNo"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("orderNo"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="loginName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">真实姓名</span>
                                        <input type="text" name="realName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">手机号码</span>
                                        <input type="text" name="phoneNum"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("phoneNum"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">商品名称</span>
                                        <input type="text" name="productName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("productName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">支付方式</span>
                                        <select name="payType" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <%for (T6352_F06 t6352_F06 : T6352_F06.values()) {%>
                                            <option value="<%=t6352_F06.name() %>" <%if (t6352_F06.name().equals(request.getParameter("payType"))) {%>
                                                    selected="selected" <%}%>><%=t6352_F06.getChineseName()%>
                                            </option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">商品类别</span>
                                        <select name="productType" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                if (t6350s != null) {
                                                    for (T6350 t6350 : t6350s) {
                                            %>
                                            <option value="<%=t6350.F02%>" <%if (t6350.F02.equals(request.getParameter("productType"))) {%>
                                                    selected="selected" <%}%>><%=t6350.F03%>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">发货时间</span>
                                        <input type="text" name="fhStartTime" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="fhEndTime" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:onSubmit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportYfhDD.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
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
                                    <th>价值</th>
                                    <th>支付方式</th>
                                    <th>数量</th>
                                    <th>状态</th>
                                    <th>发货时间</th>
                                    <th class="w200" style="text-align: center;">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (mainRecords != null) {
                                        int i = 1;
                                        for (ScoreOrderMainRecord mainRecord : mainRecords) {
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, mainRecord.orderNo + ""); %></td>
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
                                    <td><%StringHelper.filterHTML(out, mainRecord.quantity + ""); %></td>
                                    <td><%StringHelper.filterHTML(out, mainRecord.status.getChineseName()); %></td>
                                    <td><%=DateTimeParser.format(mainRecord.fhTime)%>
                                    </td>
                                    <td>
                                    	<%if (dimengSession.isAccessableResource(YfhOrderDetails.class)) {%>
                                    	<a href="javascript:void(0)" onclick="showData(<%=mainRecord.id%>)"
                                           class="link-blue mr20 popup-link" data-wh="800*600">详情</a>
                                        <%}else{ %>
                                        <a class="disabled mr20">详情</a>
                                        <%} %>
                                        <% if(mainRecord.productType!=null && T6350_F07.kind.name().equals(mainRecord.productType.name())){
                                            if (dimengSession.isAccessableResource(ScoreOrderModifyLogistics.class)) {%>
	                                        <a href="javascript:void(0)" class="link-blue mr20 popup-link"
	                                           onclick="openLogistics(<%=mainRecord.id%>)" data-wh="600*350">修改</a>
	                                       <%} else { %>
	                                        <span class="disabled mr20">修改</span>
                                        <%}} %>
                                        <%if(T6352_F06.balance.name().equals(mainRecord.payType.name())&& mainRecord.productType!=null && T6350_F07.kind.name().equals(mainRecord.productType.name()))
                                        {%>
                                            <%if (dimengSession.isAccessableResource(ScoreOrderReturn.class)) {%>
                                            <a href="javascript:void(0)" class="link-blue mr20 popup-link"
                                               onclick="returned(<%=mainRecord.id%>)" data-wh="800*600">退货</a>
                                            <%} else { %>
                                            <span class="disabled mr20">退货</span>
                                            <%} %>
                                        <%}%>
                                    </td>

                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="dhsbg tc">
                                    <td colspan="13">暂无数据</td>
                                </tr>
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

            <form action="<%=configureProvider.format(URLVariable.SCOREORDER_RETURN_URL)%>" class="form2" method="post" onsubmit="return doRefundOrder();">
                <div class="pb10"  id="returnDiv" style="display: none;">
                    <input type="hidden" name="scoreOrderId" id="scoreOrderId"/>
                    <input type="hidden" name="zipCode"/>
                    <ul class="gray6">
                        <li class="mb15"><span class="display-ib tr mr5 fl"><em
                                class="red mr5">*</em>备注：</span>

                        <textarea id="refundRemarks" name="remarks" cols="45" rows="5"
                                  class="w400 h120 border p5 required min-length-2 max-length-100"></textarea>
                        <span id="refundErrorTips" tip>2-100字</span>
                        <span errortip class="" style="display: none"></span>

                        </li>
                    </ul>

                </div>

                <%--关闭--%>
                <div class="tc f16" id="close2" style="display: none">
                    <input type="submit" fromname="form2" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" value="退货" />
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);" onclick="closeShowData()">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeShowData();">
                </div>
            </form>

            <%--关闭--%>
            <div class="tc f16"  id="close1" style="display: none">
                <input type="submit" onclick="closeShowData()" class="btn-blue2 btn white radius-6 pl20 pr20" value="关闭" />
            </div>
        </div>
    </div>
</div>
    <div class="popup_bg"></div>
    </div>

<div id="modifyLogistics" style="display: none;">
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">订单修改</h3>
        <a class="icon-i popup-close2" onclick="closeLogistics()"></a>
    </div>
    <div class="popup-content-container-2" style="max-height:550px;">
        <form action="<%=configureProvider.format(URLVariable.SCOREORDER_MODIFY_LOGISTICS)%>" method="post" class="form2">
            <input type="hidden" name="orderModifyId" id="orderModifyId"/>
            <input type="hidden" name="zipCode"/>
            <div class="p30">
                <ul class="gray6">
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                            class="red mr5">*</em>物流方：</span>
                        <div class="pl120">
                            <input name="logistics" type="text"
                                   class="text border w300 pl5 required" maxlength="40"/>
                            <span tip></span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                            class="red mr5">*</em>快递单号：</span>
                        <div class="pl120">
                            <input name="oddNumbers" type="text"
                                   class="text border w300 pl5 required notChinese" maxlength="40"/>
                            <span tip></span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                            class="red mr5">*</em>备注：</span>
                        <div class="pl120">
                            <textarea name="remarks" id="remarks_id"
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
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeLogistics();">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeLogistics();">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="popup_bg"></div>
</div>

<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
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
<%}%>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/scoreOrder.js"></script>
<script type="text/javascript">
	
	var remark_tips = "请填写退货原因最大100个字符";
	
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("fhStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("fhEndTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        
        
        $("#refundRemarks").val(remark_tips).addClass("gray9");
        $("#refundRemarks").blur(function(){
       		if($(this).val() == ""){
       			$(this).val(remark_tips).addClass("gray9");
       		}
        }).focus(function(){
        	$("#refundErrorTips").removeClass("error_tip");
        	$("#refundErrorTips").html("2-100字");
       		if($(this).val() == remark_tips){
       			$(this).val("").removeClass("gray9");
       		}
        });
        
    });

    
    function doRefundOrder(){
    	if($("#refundRemarks").val() == remark_tips){
    		$("#refundErrorTips").addClass("error_tip");
    		$("#refundErrorTips").html(remark_tips);
    		return false;
    	}
    	$("#refundErrorTips").removeClass("error_tip");
    	$("#refundErrorTips").html("2-100字");
    	return true;
    }
    
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportYfhDD.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, YfhScoreOrderList.class)%>";
    }

    function showData(scoreOrderId)
    {
        $("#showDataId").show();
        $("#close1").show();

        var ajaxUrl  = "<%=controller.getURI(request, YfhOrderDetails.class)%>";
        var data = {"scoreOrderId":scoreOrderId};
        postService(ajaxUrl,data,"");
    }

    function closeShowData()
    {
        $("#showDataId").hide();
        $("#returnDiv").hide();
        $("#close1").hide();
        $("#close2").hide();
        $("#shxxDiv").addClass("border-b-s");

        $("span[tip]").show();
        $("span[errortip]").hide();
    }

    function returned(scoreOrderId)
    {
        $("#showDataId").show();
        $("#returnDiv").show();
        $("#close2").show();
        $("#shxxDiv").removeClass("border-b-s");

        var ajaxUrl  = "<%=controller.getURI(request, YfhOrderDetails.class)%>";
        var data = {"scoreOrderId":scoreOrderId};
        postService(ajaxUrl,data,"return");
        $("#scoreOrderId").val(scoreOrderId);
        $("#refundRemarks").val(remark_tips).addClass("gray9");
    }

    function closeLogistics()
    {
        $("#modifyLogistics").hide();

        $("span[tip]").show();
        $("span[errortip]").hide();
    }

    function openLogistics(scoreOrderId)
    {
        $("#modifyLogistics").show();
        var ajaxUrl  = "<%=controller.getURI(request, YfhOrderDetails.class)%>";
        var data = {"scoreOrderId":scoreOrderId};
        postService(ajaxUrl,data,"logistics");
        $("#orderModifyId").val(scoreOrderId);
        //$("#remarks_id").val("");
    }
</script>
</body>
</html>