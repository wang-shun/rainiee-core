<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.S62.enums.T6217_F09" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.DxzInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.DxzList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.AddDxz" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.UpdateDxz" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.DxzStatusUpdate" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DXZ";
    PagingResult<DxzInfo> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    DxzInfo[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <form id="form1"
                          action="<%=controller.getURI(request, DxzList.class)%>"
                          method="post">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>定向组管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">定向组ID：</span><input
                                            type="text" name="dxzId" class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("dxzId"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">定向组名称：</span><input
                                            type="text" name="dxzTitle" class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("dxzTitle"));%>"/>
                                    </li>
                                    <li><a href="javascript:void(0)" onclick="onSubmit()" class="btn btn-blue radius-6 mr5 pl1 pr15" name="input">
                                        <i class="icon-i w30 h30 va-middle search-icon "></i>搜索
                                    </a>
                                        <%--回车提交表单--%>
                                        <input type="submit" style="display:block;overflow:hidden;width:0px;height:0px; position:absolute"/>
                                    </li>
                                     <%
                                            if (dimengSession.isAccessableResource(AddDxz.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, AddDxz.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增定向组</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增定向组</span>
                                        <%} %>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>定向组ID</th>
                                    <th>定向组名称</th>
                                    <th>人数</th>
                                    <th>创建人</th>
                                    <th>修改人</th>
                                    <th>创建时间</th>
                                    <th>最后修改时间</th>
                                    <th>备注</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (entityArray != null && entityArray.length != 0) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            DxzInfo dxzInfo = entityArray[i];
                                            if (dxzInfo == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, dxzInfo.F02);%>
                                    </td>
                                    <td>
                                    	<%StringHelper.filterHTML(out, dxzInfo.F03);%>
                                    </td>
                                    <td>
                                    	<%=dxzInfo.peopleCount %>
                                    </td>
                                    <td>
                                   		 <%StringHelper.filterHTML(out, dxzInfo.founderName);%>
                                    </td>
                                    <td>
                                   		 <%StringHelper.filterHTML(out, dxzInfo.updateUserName);%>
                                    </td>
                                    <td>
                                    	 <%=DateTimeParser.format(dxzInfo.F06)%>
                                    </td>
                                    <td>
                                    	<%=DateTimeParser.format(dxzInfo.F07)%>
                                    </td>
                                    <td>
                                    	<%StringHelper.filterHTML(out, dxzInfo.F08);%>
                                    </td>
                                    <td>
                                    	<%=dxzInfo.F09.getChineseName() %>
                                    </td>
                                    <td>
                                    	 <a href="javascript:void(0)" class="link-blue">管理用户</a>
                                    	 
                                    	 <%if (dimengSession.isAccessableResource(UpdateDxz.class)) {%> 
	                                        	<span class="blue">
	                                            	<a class="link-blue" href="<%=controller.getURI(request,UpdateDxz.class)%>?id=<%=dxzInfo.F01%>">修改</a>
	                                           	</span>
		                                        <%} else { %> 
		                                        	<span class="disabled mr20">修改</span> 
		                                 <%} %> 
                                    	 
                                    	 <%
                                    	 	if(T6217_F09.TY == dxzInfo.F09)
                                    	 	{%>
                                    	 	    <%if (dimengSession.isAccessableResource(DxzStatusUpdate.class)) {%> 
	                                        	<span class="blue">
	                                            	<a class="link-blue" href="javascript:void(0)" onclick="updateT6217Status(<%=dxzInfo.F01 %>,'QY')">启用</a>
	                                           	</span>
		                                        <%} else { %> 
		                                        	<span class="disabled mr20">启用</span> 
		                                        <%} %> 
                                    	 	<%}
                                    	 	else
                                    	 	{%>
                                    	 	    <%if (dimengSession.isAccessableResource(DxzStatusUpdate.class)) {%> 
	                                        	<span class="blue">
	                                            	<a class="link-blue" href="javascript:void(0)" onclick="updateT6217Status(<%=dxzInfo.F01 %>,'TY')">停用</a>
	                                           	</span>
		                                        <%} else { %> 
		                                        	<span class="disabled mr20">停用</span> 
		                                        <%} %>
                                    	 	<%}
                                    	 
                                    	 %>
                                    	 
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="11">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    function onSubmit() {
        $("#form1").submit();
    }
    
    // 修改定向组状态 
    function updateT6217Status(dxzId,status) {
        $.ajax({
            type: "post",
            url: "<%=controller.getURI(request, DxzStatusUpdate.class)%>",
            data: {"dxzId": dxzId,"status":status},
            dataType: "text",
            success: function (returnData) {
                $("#info").html(showSuccInfo(returnData, "yes","<%=controller.getURI(request, DxzList.class)%>"));
                $("div.popup_bg").show();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>
</body>
</html>