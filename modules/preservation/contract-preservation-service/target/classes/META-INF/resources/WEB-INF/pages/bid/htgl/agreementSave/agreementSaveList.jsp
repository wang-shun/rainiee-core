<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.repeater.preservation.entity.AgreementSave"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreementSave.OneKeySaveOperation"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreementSave.AgreementSaveOperation"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreementSave.AgreementSaveList"%>
<%@page import="com.dimeng.p2p.S62.enums.T6272_F06"%>
<%@page import="com.dimeng.p2p.S61.entities.T6141" %>
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
    CURRENT_SUB_CATEGORY = "XYBQ";
    PagingResult<AgreementSave> result = (PagingResult<AgreementSave>) request.getAttribute("result");
    AgreementSave[] agreementSaves = result.getItems();
    String agreementTimeStart = request.getParameter("agreementTimeStart");
    String agreementTimeEnd = request.getParameter("agreementTimeEnd");
    String agreementState = request.getParameter("agreementState");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, AgreementSaveList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>协议保全列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">姓名/企业名称</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全ID </span>
                                        <input type="text" name="agreementId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("agreementId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全时间</span> <input
                                            type="text" name="agreementTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> 
                                            <input type="text" readonly="readonly" name="agreementTimeEnd" id="datepicker2" class="text border pl5 w120 date mr20"/>
                                    </li>
									<li><span class="display-ib mr5">协议编号</span>
                                        <input type="text" name="agreementNum"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("agreementNum"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">保全状态 </span>
                                        <select name="agreementState" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                if (T6272_F06.values() != null && T6272_F06.values().length > 0) {
                                                    for (T6272_F06 t6272_F06 : T6272_F06.values()) {
                                            %>
                                            <option value="<%=t6272_F06.name()%>"
                                                    <%if(t6272_F06.name().equals(agreementState)) {%>selected="selected"<%} %>><%=t6272_F06.getChineseName() %>
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
                                    	<%
                                           if (dimengSession.isAccessableResource(OneKeySaveOperation.class)) {
                                       	%>
                                        <a href="javascript:void(0);"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10 click-link" onclick="oneKeySave();">一键保全</a>
                                        <%
                                        } else {
                                        %>
                                        <a class="btn btn-gray radius-6 mr5 pl10 pr10 click-link" >一键保全</a>
                                        <%
                                            }
                                        %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>用户名</th>
                                        <th>姓名/企业名称</th>
                                        <th>协议编号</th>
                                        <th>保全ID</th>
                                        <th>保全时间</th>
                                        <th>保全状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (agreementSaves != null && agreementSaves.length > 0) {
                                            int i = 1;
                                            for (AgreementSave agreementSave : agreementSaves) {
                                                if (agreementSave == null) {
                                                    continue;
                                                }

                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, agreementSave.userName); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, agreementSave.name);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, agreementSave.F08);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, agreementSave.F04);%></td>
                                        <td align="center"><%=TimestampParser.format(agreementSave.F05) %>
                                        <td align="center"><%StringHelper.filterHTML(out,agreementSave.F06.getChineseName());%>
                                        </td>
                                        <td align="center">
                                        	<%if(T6272_F06.YBQ == agreementSave.F06){ %>
                                            <a target="_blank" href="<%configureProvider.format(out, URLVariable.CONSOLE_AGREEMENT_VIEW_URL); %>?id=<%=agreementSave.F01 %>" class="link-blue">查看</a>
                                            <%}else if(T6272_F06.WBQ == agreementSave.F06){ %>
                                            <%
                                            if (dimengSession.isAccessableResource(AgreementSaveOperation.class)) {
                                        	%>
                                            <a href="javascript:void(0);" class="link-blue" onclick="agreementSave(<%=agreementSave.F01%>)">保全</a>
                                            <%
	                                        } else {
	                                        %>
	                                        <a class="disabled">保全</a>
	                                        <%
	                                            }
	                                        %>
                                            <%} %>
                                            
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="8">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
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
<div class="popup_bg" style="display: none"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
        <%if(!StringHelper.isEmpty(agreementTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("agreementTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(agreementTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("agreementTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    
    var saveId;
    function agreementSave(id)
	{
    	saveId = id;
	    $(".popup_bg").show();
		$("#info").html(showConfirmDiv("协议将会重新上传到第三方做数据保全？",0,'xybq'));
	}
    
    function oneKeySave(){
    	$(".popup_bg").show();
    	$("#info").html(showConfirmDiv("您确定上传所有未成功保全的电子协议给第三方保全？",0,'yjbq'));
    }
    
    function toConfirm(param,type){
		$("#info").html("");
		if(type=='xybq'){
			location.href = "<%=controller.getURI(request, AgreementSaveOperation.class)%>"+"?saveId=" + saveId;
		}else if(type=='yjbq'){
			location.href = "<%=controller.getURI(request, OneKeySaveOperation.class)%>";
		}
    }

</script>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

</body>
</html>