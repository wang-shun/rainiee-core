<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.contract.PreserveAll"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.contract.Preserve"%>
<%@page import="com.dimeng.p2p.repeater.preservation.entity.ContractEntity"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.contract.ContractPreservationList"%>
<%@page import="com.dimeng.p2p.S62.enums.T6271_F07"%>
<%@page import="com.dimeng.p2p.S62.enums.T6271_F08"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.GrManage" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "HTBQLB";
    PagingResult<ContractEntity> result = (PagingResult<ContractEntity>) request.getAttribute("result");
    ContractEntity[] contractEntitys=  (result == null ? null : result.getItems());
    String preservationStatus = request.getParameter("preservationStatus");
    String contractType = request.getParameter("contractType");
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, ContractPreservationList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>合同保全列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">姓名/企业名称</span>
                                        <input type="text" name="realName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全ID</span>
                                        <input type="text" name="preservationId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("preservationId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全时间</span> <input
                                            type="text" name="startTime" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> 
                                            <input type="text" readonly="readonly" name="endTime" id="datepicker2" class="text border pl5 w120 date mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的编号</span>
                                        <input type="text" name="bidId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bidId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题</span>
                                        <input type="text" name="loanTitle"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanTitle"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
									<li><span class="display-ib mr5">合同编号</span>
                                        <input type="text" name="contractNum"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("contractNum"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全状态 </span>
                                        <select name="preservationStatus" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                if (T6271_F07.values() != null && T6271_F07.values().length > 0) {
                                                    for (T6271_F07 t6271_F07 : T6271_F07.values()) {
                                            %>
                                            <option value="<%=t6271_F07.name()%>"
                                                    <%if(t6271_F07.name().equals(preservationStatus)) {%>selected="selected"<%} %>><%=t6271_F07.getChineseName() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">合同类型： </span>
                                        <select name="contractType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                if (T6271_F08.values() != null && T6271_F08.values().length > 0) {
                                                    for (T6271_F08 t6271_F08 : T6271_F08.values()) {
                                            %>
                                            <option value="<%=t6271_F08.name()%>"
                                                    <%if(t6271_F08.name().equals(contractType)) {%>selected="selected"<%} %>><%=t6271_F08.getChineseName() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><a href="javascript:$('#form2').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                    	<%if(dimengSession.isAccessableResource(PreserveAll.class)){ %>
                                        <a href="javascript:void(0)" onclick="preserveAll()"  class="btn btn-blue2 radius-6 mr5 pl10 pr10 click-link popup-link" id="preserveAll">一键保全</a>
                                        <%}else{ %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10 click-link">一键保全</a>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                            <div class=" table-container mt20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>用户名</th>
                                        <th>姓名/企业名称</th>
                                        <th>标的编号</th>
                                        <th>借款标题</th>
                                        <th>合同编号</th>
                                        <th>合同类型</th>
                                        <th>保全ID</th>
                                        <th>保全时间</th>
                                        <th>保全状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (contractEntitys != null && contractEntitys.length > 0) {
                                            int i = 1;
                                            for (ContractEntity contractEntity : contractEntitys) {
                                                if (contractEntity == null) {
                                                    continue;
                                                }

                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.userName); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.realName);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.bidNum);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.loanTitle);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.F04);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.F08.getChineseName());%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, contractEntity.F05);%></td>
                                        <td align="center"><%=TimestampParser.format(contractEntity.F06) %>
                                        <td align="center"><%StringHelper.filterHTML(out,contractEntity.F07.getChineseName());%>
                                        </td>
                                        <td align="center">
                                        	<%if(T6271_F07.YBQ == contractEntity.F07){ %>
                                            <a target="_blank" href="<%configureProvider.format(out, URLVariable.CONSOLE_CONTRACT_VIEW_URL); %>?id=<%=contractEntity.F01 %>" class="link-blue">查看</a>
                                            <%}else if(T6271_F07.WBQ == contractEntity.F07){ %>
	                                            <%if(dimengSession.isAccessableResource(Preserve.class)){ %>
	                                            	<a href="javascript:void(0)" onclick="preserve(<%=contractEntity.F01 %>,this)" name="preserveMenu" class="link-blue">保全</a>
	                                            <%}else{ %>
	                                            	<a href="javascript:void(0);" class="disabled mr20">保全</a>
	                                            <%} %>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="11">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<div></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
	String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
	if (!StringHelper.isEmpty(infoMessage)) {
%>
	<script type="text/javascript">
		$("div.popup_bg").show();
		$("#info").html(showInfo("<%=infoMessage%>","wrong"));
	</script>
<%} %>
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
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    
    var obb = null;
    function showConfirmDiv(msg,obj,url){
    	obb = obj;
    	return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
    	'<h3 class="pl20 f18">提示</h3>'+
        '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
        ' <div class="popup-content-container pb20 clearfix">'+
        '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
        ' <div class="tc f16"> <a href="javascript:void(0);" onclick="toConfirm(\''+url+'\');" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a><a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> </div> '+
    	'</div></div>';
    }
    
    function showConfirmDiv2(msg,url){
    	return '<div class="popup-box" style="width:470px;min-height:200px;"> <div class="popup-title-container">'+
    	'<h3 class="pl20 f18">提示</h3>'+
        '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
        ' <div class="popup-content-container pb20 clearfix">'+
        '<div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
        ' <div class="tc f16"> <a href="javascript:void(0);" onclick="toConfirm2(\''+url+'\');" class="btn-blue2 btn white radius-6 pl20 pr20" >确定</a><a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> </div> '+
    	'</div></div>';
    }
    
    function preserveAll()
    {
    	var url = "<%=controller.getURI(request, PreserveAll.class)%>";
    	$("div.popup_bg").show();
    	$("#info").html(showConfirmDiv2("您确定上传所有未成功保全的电子合同给第三方保全？",url));
    }
    
    //保全按钮
    function preserve(id,obj)
    {
    	var url = "<%=controller.getURI(request, Preserve.class)%>?id="+id;
    	$("div.popup_bg").show();
    	$("#info").html(showConfirmDiv("合同将会重新上传到第三方做数据保全？",obj,url));
    }
    
    function toConfirm2(url)
    {
    	$("#info").html("");
    	$("div.popup_bg").hide();
    	$("a[name='preserveMenu']").each(function(i,val){
    		$(val).html("正在保全中...");
        	$(val).removeAttr("onclick");
    	});
    	$("#preserveAll").removeClass("btn-blue2");
    	$("#preserveAll").addClass("btn-gray");
    	$("#preserveAll").removeAttr("onclick");
    	window.location.href = url;
    }
    
    function toConfirm(url)
    {
    	$("#info").html("");
    	$("div.popup_bg").hide();
    	$(obb).html("正在保全中...");
    	$(obb).removeAttr("onclick");
    	obb = null;
    	window.location.href = url;
    }
</script>
</body>
</html>