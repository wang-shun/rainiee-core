<%@page import="com.dimeng.p2p.OrderType" %>
<%@page import="com.dimeng.p2p.S65.entities.T6501" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TradeTypeManage" %>
<%@page import="com.dimeng.p2p.S51.entities.T5132" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>订单查询-<% configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "ZJGL";
    CURRENT_SUB_CATEGORY = "DDCX";
    int orderType = IntegerParser.parse(request.getParameter("orderType"));
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form action="<%=controller.getURI(request, OrderServlet.class)%>"
      method="post">
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <div class="user_mod">
                    <div class="user_screening clearfix">
                        <div class="table top3">
                            <ul>
                                <li>类型：
                                    <select name="orderType" id="orderType" class="select6 mr10">
                                        <option value="">--全部--</option>
                                        <%
                                        TradeTypeManage tradeTypeManage = serviceSession.getService(TradeTypeManage.class);
                                        T5132[] t5132s = tradeTypeManage.searchT5132(userType, t6110.F10);
                                        if (t5132s != null && t5132s.length > 0) {
                                            for (T5132 type : t5132s) {
                                                if(!isHasBadClaim && type.F01 == OrderType.BUY_BAD_CLAIM.orderType()){
                                                    continue;
                                                }else if(type.F01 == OrderType.BUY_BAD_CLAIM.orderType() && t6110.F10 != T6110_F10.S){
                                                    continue;
                                                }
                                    %>
                                        <option value="<%=type.F01%>"
                                            <%if(type.F01 == orderType){ %>selected="selected"<%} %>><%=type.F02 %></option>
                                    <%
                                            }
                                        }
                                    %>
                                    </select>
                                </li>
                                <li>创建时间：
                                    <input type="text" id="startDate" name="startTime" readonly="readonly"
                                           class="text_style text_style_date" value=""/>
                                    &nbsp;至&nbsp;
                                    <input type="text" id="endDate" name="endTime" readonly="readonly"
                                           class="text_style text_style_date ml10" value=""/>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" class="btn04" onclick="orderQueryPaging(true)"> 查询</a>
                                </li>
                            </ul>
                            <input type="hidden" value="<%=controller.getURI(request, OrderServlet.class)%>"
                                   id="orderServletUrl"/>
                        </div>
                    </div>
                    <div class="user_table pt5">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">类型</td>
                                <td align="center">创建时间</td>
                                <%--<td align="center">提交时间</td>--%>
                                <td align="center">状态</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                        <div class="clear"></div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/capital/orderQuery.js"></script>
<script type="text/javascript">
    $(function () {
    	$('.select6').selectlist({
    		zIndex: 15,
    		width: 105,
    		optionHeight: 28,
    		height: 28
    	});
        $("#startDate").datepicker({
            inline: true, onSelect: function (selectedDate) {
                $("#endDate").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#endDate").datepicker({inline: true});
        $("#endDate").datepicker('option', 'minDate', $("#startDate").datepicker().val());
        //加载分页
        orderQueryPaging(true);
    });
    
    function toAjaxPage(){
    	orderQueryPaging(false);
    }
</script>
</body>
</html>