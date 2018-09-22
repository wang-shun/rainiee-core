<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.exchange.ScoreExchangeRecord"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreStatisticsList"%>
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
    PagingResult<T6106> t6106List = ObjectHelper.convert(request.getAttribute("t6106List"), PagingResult.class);
    T6106[] t6106Array = (t6106List == null ? null : t6106List.getItems());
    int ISumScore = (int)request.getAttribute("ISumScore");
    int userId = (int)request.getAttribute("userId");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, ScoreGetRecord.class)%>" method="post">
      	<input type="hidden" name="userId" value="<%=userId%>">
		<div class="p20">
			<div class="title-container">
                  <i class="icon-i w30 h30 va-middle title-left-icon"></i>积分统计列表
                  <div class="fr mt5">
                  <input type="button" value="返回" class="btn btn-blue radius-6 pl20 pr20 ml40 right0 mr10" onclick="window.location.href='<%=controller.getURI(request, ScoreStatisticsList.class)%>'">
                  </div>
              </div>
              <div class="tabnav-container mt20">
                   <ul class="clearfix pr">
                       <li>
                       		<a href="javascript:void(0);" class="tab-btn mr20 select-a">积分获取记录<i class="icon-i tab-arrowtop-icon"></i></a>
                       </li>
                       <li>
                           <a href="<%=controller.getURI(request, ScoreExchangeRecord.class)%>?userId=<%=userId%>" class="tab-btn mr20">积分兑换记录</a>
                       </li>
                   </ul>
               </div>
			  <div class="border mt10 table-container">
			    <table class="table table-style gray6 tl">
			      <thead>
			        <tr class="title tc">
			          <th>序号</th>
			          <th>获取时间</th>
			          <th>获取积分</th>
			          <th>类型</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (t6106Array != null && t6106Array.length>0) {
                        		int index = 1;
                        		for (T6106 t6106:t6106Array)
                        		{if (t6106 == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%=TimestampParser.format(t6106.F04)%></td>
                          <td><%=t6106.F03%></td>
                          <td><%StringHelper.filterHTML(out, t6106.F05.getChineseName());%></td>
                        </tr>
						<%}%>
						<tr class="tc">
                            <td>获取积分总计：</td>
                            <td></td>
                            <td class="red"><%=ISumScore%></td>
                            <td></td>
                        </tr>
						<%}else{%>
                        <tr class="dhsbg"><td colspan="4" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, t6106List);
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