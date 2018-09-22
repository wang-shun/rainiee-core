<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppVerSearch" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AddAppVer" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.ViewAppVer" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.DeleteAppVer" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "APPVER";
	CURRENT_SUB_CATEGORY = "APPBBGL";
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <%
                	PagingResult<AppVersionInfo> list = ObjectHelper.convert(request.getAttribute("list"), PagingResult.class);
                	AppVersionInfo[] appVersionArray = (list == null ? null : list.getItems());
                %>
                <form id="form1" action="<%=controller.getURI(request, AppVerSearch.class)%>" method="post">
                    <div class="p20">
                        <div class="border">

                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>APP版本管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                <li><span class="display-ib mr5">客户端类型</span>
                                	<select name="verType" class="border mr20 h32 ">
                            			<option value="">全部</option>
                                        <option value="1" <%if(!StringHelper.isEmpty(request.getParameter("verType")) && request.getParameter("verType").equals("1")){ %>selected="selected"<%} %>>android</option>
                                        <option value="2" <%if(!StringHelper.isEmpty(request.getParameter("verType")) && request.getParameter("verType").equals("2")){ %>selected="selected"<%} %>>iOS</option>
                                     </select>
						        </li>
                                <li><span class="display-ib mr5">版本号</span>
						          <input type="text" name="verNO" value="<%StringHelper.filterHTML(out, request.getParameter("verNO"));%>" class="text border pl5 mr20" />
						        </li>
                                <li><span class="display-ib mr5">是否强制升级</span>
                                	<select name="isMustUpdate" class="border mr20 h32 ">
                            			<option value="">全部</option>
                                        <option value="0" <%if(!StringHelper.isEmpty(request.getParameter("isMustUpdate")) && request.getParameter("isMustUpdate").equals("0")){ %>selected="selected"<%} %>>否</option>
                                        <option value="1" <%if(!StringHelper.isEmpty(request.getParameter("isMustUpdate")) && request.getParameter("isMustUpdate").equals("1")){ %>selected="selected"<%} %>>是</option>
                                     </select>
						        </li>
                                <li><span class="display-ib mr5">是否正常使用</span>
                                	<select name="status" class="border mr20 h32 ">
                            			<option value="">全部</option>
                                        <option value="0" <%if(!StringHelper.isEmpty(request.getParameter("status")) && request.getParameter("status").equals("0")){ %>selected="selected"<%} %>>未使用</option>
                                        <option value="1" <%if(!StringHelper.isEmpty(request.getParameter("status")) && request.getParameter("status").equals("1")){ %>selected="selected"<%} %>>使用中</option>
                                     </select>
						        </li>
                                <li><span class="display-ib mr5">发布人</span>
						          <input type="text" name="publisher" value="<%StringHelper.filterHTML(out, request.getParameter("publisher"));%>" class="text border pl5 mr20" />
						        </li>
						        <li><span class="display-ib mr5">发布时间</span>
						          <input type="text" name="startPublishDate" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
						          <span class="pl5 pr5">至</span>
						          <input readonly="readonly" type="text" name="endPublishDate" id="datepicker2" class="text border pl5 w120 mr20 date" />
						        </li>
						        <li> <a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                 <li>
                                     <%if (dimengSession.isAccessableResource(AddAppVer.class)) {%>
                                     <a href="<%=controller.getURI(request, AddAppVer.class)%>"
                                        class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                             class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                     <%} else { %>
                                     <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                             class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                     <%} %>
                                 </li>
                                </ul>
                            </div>

                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>客户端类型</th>
                                    <th>版本号</th>
                                    <th>是否强制升级</th>
                                    <th>是否正常使用</th>
                                    <th>发布人</th>
                                    <th>发布时间</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
	                                if (appVersionArray != null && appVersionArray.length>0) {
	                        		int index = 1;
	                        		for (AppVersionInfo appVersion:appVersionArray)
	                        		{if (appVersion == null) {continue;}
                                %>
                                <tr class="title tc">
                                    <td><%=index++ %>
                                    </td>
                                    <td><%=appVersion.verType == 1 ? "android" : "iOS" %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, appVersion.verNO); %></td>
                                    <td><%=appVersion.isMustUpdate == 1 ? "是" : "否" %>
                                    </td>
                                    <td><%=appVersion.status == 1 ? "使用中" : "未使用" %>
                                    </td>
                                    <td><%=appVersion.publisher %>
                                    </td>
                                    <td><%=TimestampParser.format(appVersion.publishTime) %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(ViewAppVer.class)) {%>
                                        <a href="<%=controller.getURI(request, ViewAppVer.class)%>?id=<%=appVersion.id %>"
                                           class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteAppVer.class)) {%>
                                        <a href="javascript:void(0);"
                                           onclick="delAppVer('<%=appVersion.id %>','<%StringHelper.filterHTML(out, appVersion.verNO); %>')"
                                           class="link-blue mr20">删除</a>
                                        <%} else { %>
                                        <span class="disabled">删除</span>
                                        <%} %>
                                    </td>
                                </tr>
                                <% }
                                } else {%>
                                <tr class="title">
                                    <td colspan="8" class="tc">暂无数据</td>
                                </tr>
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
<div id="info"></div>
<div class="popup_bg hide"></div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    function delAppVer(id, name) 
    {
    	$(".popup_bg").show();
    	$("#info").html(showConfirmDiv("你确定删除" + name + "?",id,"del"));
    }
    
    function toConfirm(i,type)
    {
    	if(type=="del")
    	{
    		window.location.href = '<%=controller.getURI(request, DeleteAppVer.class) %>' + "?id=" + i;
    	}
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startPublishDate"));%>");
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endPublishDate"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>