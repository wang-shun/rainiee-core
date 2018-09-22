<%@page import="com.dimeng.p2p.FeeCode"%>
<%@page import="com.dimeng.p2p.S51.entities.T5122" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.TradingRecordExport" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.TradingRecord" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TradeTypeManage" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<% configureProvider.format(out, SystemVariable.SITE_NAME);%>_交易记录</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/jquery.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZJGL";
    CURRENT_SUB_CATEGORY = "JYJL";
    int tradingType = IntegerParser.parse(request.getParameter("tradingType"));
    String accountType = StringHelper.isEmpty(request.getParameter("accountType")) ? "WLZH" : request.getParameter("accountType");
    T6101_F03 f03 = null;
    if (!StringHelper.isEmpty(accountType)) {
        f03 = T6101_F03.valueOf(accountType);
    } else {
        f03 = T6101_F03.WLZH;
    }

    boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    ApplyGuarantorManage applyGuarantorManage = serviceSession.getService(ApplyGuarantorManage.class);
    T6125 t6125 = applyGuarantorManage.getGuanterInfo();
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
%>
<div class="main_bg clearfix">
    <form id="form1" method="post">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <%@include file="/WEB-INF/include/tradingRecord/header.jsp" %>
                <div class="user_mod border_t15">
                    <div class="user_screening clearfix">
                        <ul>
                            <li>
                                	交易类型：
                                <select name="tradingType" id="tradingType" class="select6">
                                    <option value="">--全部--</option>
                                    <%
                                        TradeTypeManage tradeTypeManage = serviceSession.getService(TradeTypeManage.class);
                                        T5122[] t5122s = tradeTypeManage.search(userType, t6110.F10);
                                        if (t5122s != null && t5122s.length > 0) {
                                            for (T5122 type : t5122s) {
                                                if(!isHasBadClaim && type.F01 == FeeCode.BLZQ_GM){
                                                    continue;
                                                }else if(type.F01 == FeeCode.BLZQ_GM && t6110.F10 != T6110_F10.S){
                                                    continue;
                                                }
                                    %>
                                    <option value="<%=type.F01%>"
                                            <%if(type.F01 == tradingType){ %>selected="selected"<%} %>><%=type.F02 %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </li>
                            <%if (t6110.F10 == T6110_F10.S || (isHasGuarant && t6125 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG))) { %>
                            <li>
                                账户类型：
                                <select name="accountType" id="accountType" class="select6">
                                    <%
                                        T6101_F03[] types = T6101_F03.values();
                                        for (T6101_F03 type : types) {
                                    %>
                                    <option value="<%=type.toString()%>"
                                            <%if(type.toString().equals(accountType)){ %>selected="selected"<%} %>><%=type.getChineseName() %>
                                    </option>
                                    <%
                                        }
                                    %>
                                </select>
                            </li>
                            <%} %>
                            <%--<input type="hidden"  name="zhlx"  value="<%=T6101_F03.WLZH%>"/>--%>
                            <li>时间：
                                <input type="text" id="startDate" name="startTime" readonly="readonly"
                                       class="text_style text_style_date"/>
                               	 到
                                <input type="text" id="endDate" name="endTime" readonly="readonly"
                                       class="text_style text_style_date ml10"/>
                            </li>
                            <li>
                                <div>
                                    <a href="javascript:void(0);" class="btn04 mb10 mr10 fl" onclick="tradingRecordPaging(true)"> 查询</a>
                                    <a href="javascript:void(0);" class="btn04 mb10 fl" onclick="tradingRecordExport('<%=controller.getURI(request, TradingRecordExport.class) %>')"> 导出</a>
                                </div>

                            </li>
                        </ul>
                    </div>
                    <div class="user_table pt5">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                            	<td align="center">序号</td>
                                <td align="center">时间</td>
                                <td align="center">类型明细</td>
                                <td align="center">收入(元)</td>
                                <td align="center">支出(元)</td>
                                <td align="center">结余(元)</td>
                                <td align="center">备注</td>
                            </tr>
                        </table>
                        <div class="page p15" id="pageContent"></div>

                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
        <input type="hidden" id="tradingRecordUrl" value="<%=controller.getURI(request, TradingRecord.class)%>"/>
    </form>
</div>
	<!-- 提示-HSP -->
	<%
		String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
	%>
	<div id="dialogMessage" style="<%=StringHelper.isEmpty(infoMsg)?"display: none;":"" %>">
		 <div class="popup_bg"></div>
			<div class="dialog">
			<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>
		    <div class="content">
		    	<div class="tip_information">
		    		<div class="successful"></div>
		    		<div class="tips">
		    			<span class="f20 gray3"><%=StringHelper.isEmpty(infoMsg)?"":infoMsg %></span>
		    		</div>
		    	</div>
		    	<div class="tc mt20">
		    	<a href="javascript:void(0);" onclick="closeDialogMessageDiv()" class="btn01"  />确认</a>
		    	</div> 
		   </div>
		    </div>
	</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/capital/tradingRecord.js"></script>
<script type="text/javascript">
	function closeInfo(){
		$("#dialogMessage").hide();
	}
	function closeDialogMessageDiv(){
		$("#dialogMessage").hide();
        location.reload();
	}
    $(function () {
    	$('.select6').selectlist({
    		zIndex: 15,
    		width: 105,
    		optionHeight: 28,
    		height: 28
    	});
        $("#startDate").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#endDate").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#endDate").datepicker({
            inline: true
        });
        $("#endDate").datepicker('option', 'minDate', $("#startDate").datepicker().val());
        //交易记录分页
        tradingRecordPaging(true);
    });
    
    function toAjaxPage(){
        tradingRecordPaging(false);
    }
</script>
</body>
</html>