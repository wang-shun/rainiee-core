<%@page import="com.dimeng.p2p.S62.enums.T6285_F09" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.HzEntity" %>
<%@page import="com.dimeng.p2p.common.enums.QueryType" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.tyjlc.TyjBackAccount" %>
<%@page import="java.util.Map" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>体验金理财-<% configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "TYJLC";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="container fr mb15">
            <div class="user_bgls fl f20">
                <p class="mt40 ml40">
                    全部待收利息<br/>
                    <%
                        Map<String, Object> retMap = (Map<String, Object>) request.getAttribute("retMap");
                        BigDecimal interestTot = (BigDecimal) retMap.get("amount");
                        int count = (int) retMap.get("count");
                    %>
						<span class="red">
						<%=Formater.formatAmount(interestTot) %>
					           元
						</span>
                </p>
            </div>
            <div class="hzcx_st fl"></div>
            <div class="fl">
                <ul class="hzcx_li pl40">
                    <li>待收笔数<br> <%=count %> 笔
                    </li>
                </ul>
            </div>
        </div>
        <form action="<%=controller.getURI(request, TyjBackAccount.class)%>" method="post">
            <div class="container fr">
                <div class="p15">
                    <div class="bom mb20">
                        <%
                            PagingResult<HzEntity> boList = (PagingResult<HzEntity>) request.getAttribute("boList");
                            QueryType queryType1 = EnumParser.parse(QueryType.class, request.getParameter("queryType"));
                            if (queryType1 == null) {
                                queryType1 = QueryType.DS;
                            }
                        %>
                        <div class="table top3">
                            <span>查询类型：</span><span><select name="queryType" id="queryType" class="yhgl_ser"
                                                            style="width: 80px;">
                            <%
                                for (QueryType queryType : QueryType.values()) {
                            %>
                            <option value="<%=queryType.name()%>"
                                    <%if (queryType == queryType1) {%>
                                    selected="selected" <%}%>><%=queryType.getName()%>
                            </option>
                            <%
                                }
                            %>
                        </select></span>
                            <span>查询时间：</span>
									<span>
										<input type="text" class="date w100 yhgl_ser w88" id="datepicker1"
                                               name="timeStart"/>
									</span>
                            <span>到</span>
									<span>
										<input type="text" class="date w100 yhgl_ser w88 ml5" id="datepicker2"
                                               name="timeEnd"/>
									</span>
                            <span></span>
                            <input name="input" type="submit" style="cursor: pointer;" value="查询"
                                   class="btn01 fl mb10"/>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div class="mb10">
                        <p>
                            <%-- 查询总额：<span class="red f18"><%=Formater.formatAmount(bMoney)%>元</span> --%>
                        </p>
                    </div>
                    <div class="mb20">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0"
                               class="user_table">
                            <tr class="user_lsbg">
                                <td width="10%" align="center">序号</td>
                                <td width="30%" align="center">借款标题</td>
                                <td width="10%" align="center">类型明细</td>
                                <td width="15%" align="center">金额</td>
                                <td width="17%" align="center">收款日期</td>
                                <td width="10%" align="center">回款状态</td>
                            </tr>
                            <%
                                if (boList != null && boList.getItemCount() > 0) {
                                    int index = 0;
                                    for (HzEntity backOff : boList.getItems()) {
                                        if (backOff == null) {
                                            continue;
                                        }
                                        index++;
                            %>
                            <tr>
                                <td align="center">
                                    <%=    index%>
                                </td>
                                <td align="center">
                                    <%
                                        StringHelper.filterHTML(out, backOff.title);
                                    %>
                                </td>
                                <td align="center">
                                    <%
                                        StringHelper.filterHTML(out, backOff.orderType);
                                    %>
                                </td>
                                <td align="center"><%=Formater.formatAmount(backOff.amount)%>元</td>
                                <td align="center"><%=TimestampParser.format(backOff.skDate, "yyyy-MM-dd")%>
                                </td>
                                <td align="center">
                                    <%
                                        if (backOff.status != null && backOff.status == T6285_F09.YFH) {
                                    %> 已收<%
                                } else if (backOff.status != null && (backOff.status == T6285_F09.WFH || backOff.status == T6285_F09.FHZ)) {
                                %> 待收 <%
                                    }
                                %>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </table>
                        <div class="page">
                            <%
                                AbstractFinancingServlet.rendPagingResult(out, boList);
                            %>
                        </div>
                    </div>
                </div>
            </div>
            <div class="clear"></div>
        </form>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>

<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("timeStart"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("timeEnd"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>