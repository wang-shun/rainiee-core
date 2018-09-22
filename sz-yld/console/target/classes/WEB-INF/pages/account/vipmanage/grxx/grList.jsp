<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ExportGr" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ShList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ShjlList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.UpdateGr" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.KhjlManage" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Grxx" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F08"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "GRXX";
    KhjlManage manage = serviceSession.getService(KhjlManage.class);
    PagingResult<Grxx> list = ObjectHelper.convert(request.getAttribute("list"), PagingResult.class);
    Grxx[] userArray = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, GrList.class)%>" method="post">
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>个人信息</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">用户名</span>
			          <input type="text" name="userName" value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">姓名</span>
			          <input type="text" name="name" value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">手机号码</span>
			          <input type="text" name="phone" value="<%StringHelper.filterHTML(out, request.getParameter("phone"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">邮箱</span>
			          <input type="text" name="email" value="<%StringHelper.filterHTML(out, request.getParameter("email"));%>" class="text border pl5 mr20" />
			          <input type="hidden"  name="dshFlg"  value="<%StringHelper.filterHTML(out, request.getParameter("dshFlg"));%>"/>
			        </li>
			        <%if(is_business){ %>
			        <li><span class="display-ib mr5">业务员工号</span>
			           <input type="text" name="employNum" value="<%StringHelper.filterHTML(out, request.getParameter("employNum"));%>" class="text border pl5 mr20" />
			        </li>
			        <%} %>
			        <li><span class="display-ib mr5">注册时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li><span class="display-ib mr5">状态</span>
	                  <select name="status" class="border mr10 h32 mw60">
	                                  	<option value="">全部</option>
	                                  	<%
	                                  		for (T6110_F07 t6110_F08 : T6110_F07.values()) {
	                                  	%>
										<option value="<%=t6110_F08.name()%>" <%if (t6110_F08.name().equals(request.getParameter("status"))) {%> selected="selected" <%}%>><%=t6110_F08.getChineseName()%></option>
										<%
											}
										%>                                
	                                  </select>
	                </li>
			        <li><span class="display-ib mr5">注册来源</span>
			          <select name="zcType" class="border mr10 h32 mw80">
						<option value="">全部</option>
                        <%
                        	for (T6110_F08 t6110_F08 : T6110_F08.values()) {
                        %>
						<option value="<%=t6110_F08.name()%>" <%if (t6110_F08.name().equals(request.getParameter("zcType"))) {%> selected="selected" <%}%>><%=t6110_F08.getChineseName()%></option>
						<%
							}
						%> 
                      </select>
			        </li>
			        <li> <a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportGr.class)) {
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
			          <th>姓名</th>
			          <th>手机号码</th>
			          <th>身份证</th>
			          <th>邮箱</th>
			          <th>状态</th>
			          <%if(is_business){ %>
			          <th>业务员工号</th>
			          <%} %>
			          <th>注册来源</th>
			          <th>注册时间</th>
			          <th class="w200" style="text-align: center;">操作</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (userArray != null && userArray.length>0) {
                        		int index = 1;
                        		for (Grxx user:userArray)
                        		{if (user == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%StringHelper.filterHTML(out, user.userName);%></td>
                          <td><%StringHelper.filterHTML(out, user.name);%></td>
                          <td><%StringHelper.filterHTML(out, user.phone); %></td>
                          <td><%StringHelper.filterHTML(out, StringHelper.decode(user.sfzh));%></td>
                          <td><%StringHelper.filterHTML(out, user.email);%></td>
                          <td><%StringHelper.filterHTML(out, user.status.getChineseName());%></td>
                          <%if(is_business){ %>
                          <td><%StringHelper.filterHTML(out, user.employNum);%></td>
                          <%} %>
                          <td><%StringHelper.filterHTML(out, user.F08.getChineseName());%></td>
                          <td><%=TimestampParser.format(user.startTime)%></td>
                         <%--  <td><%StringHelper.filterHTML(out, user.kfjl);%></td> --%>
                          <td>
                          	<%if (dimengSession.isAccessableResource(JbxxView.class)) {%>
                          	<a href="<%=controller.getURI(request,JbxxView.class)%>?userId=<%=user.id%>" class="link-blue mr20 click-link">查看</a>
                          	<%}else{ %>
                          	<span class="disabled mr20">查看</span>
                          	<%} %>
                          	<%if (dimengSession.isAccessableResource(UpdateGr.class)) {%>
                          	<a href="<%=controller.getURI(request,UpdateGr.class)%>?userId=<%=user.id%>" class="link-blue mr20">修改</a>
                          	<%}else{ %>
                          	<span class="disabled mr20">修改</span>
                          	<%} %>
                          	<%if (dimengSession.isAccessableResource(ShList.class)) {%>
                          		<%if("0".equals(user.dshFlg)){ %>
                          			<a href="<%=controller.getURI(request,ShList.class)%>?id=<%=user.id%>" class="libk-deepblue mr20">审核</a>
                          		<%}else{ %>
                          			<a href="<%=controller.getURI(request,ShList.class)%>?id=<%=user.id%>" class="libk-deepblue mr20">待审核</a>
                          		<%} %>
                          	<%}else{ %>
                          		<%if("0".equals(user.dshFlg)){ %>
                          			<span class="disabled mr20">审核</span>
                          		<%}else{ %>
                          			<span class="disabled mr20">待审核</span>
                          		<%} %>
                          	<%} %>
                          	<%if (dimengSession.isAccessableResource(ShjlList.class)) {%>
                          		<a href="<%=controller.getURI(request, ShjlList.class)%>?yhId=<%=user.id%>" class="link-orangered mr20">审核记录</a>
                        	<%}else{ %>
                        		<span class="disabled mr20">审核记录</span>
                        	<%} %>
                          	<%-- <%if (dimengSession.isAccessableResource(Szkhjl.class)) {%>
	                          	<%if(manage.isExist(user.id)){ %>
	                          	<span class="blue"><a href="<%=controller.getURI(request,Szkhjl.class)%>?userId=<%=user.id%>" class="mr10">修改客户经理</a></span>
	                          	<%}else{ %>
	                          	<span class="blue"><a href="<%=controller.getURI(request,Szkhjl.class)%>?userId=<%=user.id%>" class="mr10">设置客户经理</a></span>
	                          	<%} %>
                          	<%}else{ %>
                          	<span class="disabled">设置客户经理</span>
                          	<%} %> --%>
                          </td>
                        </tr>
						<%
                        	}}else{
                        %>
                        <tr class="dhsbg"><td colspan="11" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
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


<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
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
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportGr.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, GrList.class)%>";
    }
    /* function onSubmit(){
     $("#form1").submit();
     } */
</script>
</body>
</html>