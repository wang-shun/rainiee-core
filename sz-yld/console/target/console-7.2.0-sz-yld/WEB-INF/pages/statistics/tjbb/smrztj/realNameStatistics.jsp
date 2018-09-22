<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RealNameStatisticsEntity" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.smrztj.RealNameStatistics" %>
<%@page import="com.dimeng.p2p.S61.enums.T6198_F04" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.smrztj.ExportRealNameStatistics" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "SMRZTJB";
    PagingResult<RealNameStatisticsEntity> entityList = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    RealNameStatisticsEntity[] entityArray = (entityList == null ? null : entityList.getItems());
    String authPassTimeStart = request.getParameter("authPassTimeStart");
    String authPassTimeEnd = request.getParameter("authPassTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, RealNameStatistics.class)%>" method="post">
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>实名认证统计表</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">用户名：</span>
			          <input type="text" name="userName" value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名：</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li>
                       <span class="display-ib mr5">认证来源：</span> 
                       <select name="authSource" class="border mr20 h32 mw100">
                        <option value="">全部</option>
                        <%
                        for(T6198_F04 authSource : T6198_F04.values()){
                        %>
                        <option value="<%=authSource.name() %>"
                                <%if(authSource.name().equals(request.getParameter("authSource"))){ %>
                                selected="selected" <%}%>><%=authSource.getChineseName() %>
                        </option>
                        <%} %>
                    </select>
                   </li>
                   <li><span class="display-ib mr5">认证通过时间</span>
			          <input type="text" name="authPassTimeStart" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="authPassTimeEnd" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportRealNameStatistics.class)) {
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
			          <th>用户名</th>
			          <th>真实姓名</th>
			          <th>错误认证次数</th>
			          <th>认证来源</th>
			          <th>认证通过时间</th>
			          <th>累计登录次数</th>
			          <th>最后登录时间</th>
			          
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%      
			                int rztotal = 0;
			                int dltotal = 0;
                        	if (entityArray != null && entityArray.length>0) {
                        		int index = 1;
                        		for (RealNameStatisticsEntity entity : entityArray)
                        		{
                        		    rztotal += entity.F03;
                        		    dltotal += entity.F05;
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%StringHelper.filterHTML(out, entity.userName);%></td>
                          <td><%StringHelper.filterHTML(out, entity.realName);%></td>
                          <td><%=entity.F03%></td>
                          <td><%=entity.F04.getChineseName()%></td>
                          <td><%=TimestampParser.format(entity.F06)%></td>
                          <td><%=entity.F05%></td>
                          <td><%=TimestampParser.format(entity.F07)%></td>
                        </tr>
						<%}%>
						<tr class="tc">
                            <td>合计</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td><%=rztotal%></td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td><%=dltotal%></td>
                            <td>&nbsp;</td>
                        </tr>
						<%}else{%>
                        <tr class="dhsbg"><td colspan="8" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, entityList);
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
        <%if(!StringHelper.isEmpty(authPassTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, authPassTimeStart);%>");
        <%}%>
        <%if(!StringHelper.isEmpty(authPassTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, authPassTimeEnd);%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportRealNameStatistics.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, RealNameStatistics.class)%>";
    }
</script>
</body>
</html>