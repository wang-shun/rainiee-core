<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.UsedScoreExchangeExt"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.exchange.ScoreExchangeRecord"%>
<%@page import="com.dimeng.p2p.S61.entities.T6106"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ExportScoreStatisticsList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreCountExt"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreGetRecord"%>
<%@page import="com.dimeng.p2p.S61.entities.T6105"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ExportGr" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "JFTJLB";
    PagingResult<ScoreOrderInfoExt> scoreOrderInfoList = ObjectHelper.convert(request.getAttribute("scoreOrderInfoList"), PagingResult.class);
    ScoreOrderInfoExt[] scoreOrderInfoArray = (scoreOrderInfoList == null ? null : scoreOrderInfoList.getItems());
    UsedScoreExchangeExt usedScoreExchangeExt = ObjectHelper.convert(request.getAttribute("usedScoreExchangeExt"), UsedScoreExchangeExt.class);
    int userId = (int)request.getAttribute("userId");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, ScoreExchangeRecord.class)%>" method="post">
      	<input type="hidden" name="userId" value="<%=userId%>">
		<div class="p20">
			<div class="title-container">
                  <i class="icon-i w30 h30 va-middle title-left-icon"></i>积分统计列表
                  <div class="fr mt5">
                <input type="button" value="返回" class="btn btn-blue radius-6 pl20 pr20 ml40 right0 mr10" onclick="window.location.href='/console/mall/jfgl/statistics/scoreStatisticsList.htm'">
                </div>
              </div>
              <div class="tabnav-container mt20">
                   <ul class="clearfix pr">
                       <li><a href="<%=controller.getURI(request, ScoreGetRecord.class)%>?userId=<%=userId%>" class="tab-btn mr20">积分获取记录</a>
                       </li>
                       <li>
                           <a href="javascript:void(0);"
                              class="tab-btn mr20 select-a">积分兑换记录<i class="icon-i tab-arrowtop-icon"></i></a></li>
                       <li>
                   </ul>
               </div>
			  <div class="border mt10 table-container">
			    <table class="table table-style gray6 tl">
			      <thead>
			        <tr class="title tc">
			          <th>序号</th>
			          <th>商品名称</th>
			          <th>数量</th>
			          <th>所需积分</th>
			          <th>成交时间</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (scoreOrderInfoArray != null && scoreOrderInfoArray.length>0) {
                        		int index = 1;
                        		for (ScoreOrderInfoExt scoreOrderInfo:scoreOrderInfoArray)
                        		{if (scoreOrderInfo == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%StringHelper.filterHTML(out,scoreOrderInfo.comdName);%></td>
                          <td><%=scoreOrderInfo.comdNum%></td>
                          <td><%=scoreOrderInfo.comdScore%></td>
                          <td><%=TimestampParser.format(scoreOrderInfo.F04)%></td>
                        </tr>
						<%}%>
						<tr class="title tc">
							<td></td>
                            <td></td>
                            <td>商品数量总计：<span class="red"><%=usedScoreExchangeExt.SumCommodity%></span></td>
                            <td>积分兑换总计：<span class="red"><%=usedScoreExchangeExt.SumExchangeScore%></span></td>
                            <td></td>
                        </tr>
						<%}else{%>
                        <tr class="dhsbg"><td colspan="5" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, scoreOrderInfoList);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->


<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
</script>
</body>
</html>