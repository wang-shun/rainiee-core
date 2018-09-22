<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XYCreditLevel"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.ExportXYgl" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XYCredit" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XYjlList" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.FundXYAccountType" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.FundsXYView" %>
<%@page import="com.dimeng.p2p.S61.enums.T6116_F05" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    PagingResult<FundsXYView> fundsXYViewList = (PagingResult<FundsXYView>) request.getAttribute("fundsXYViewList");
	BigDecimal searchAmount = (BigDecimal)request.getAttribute("searchAmount");
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "XYGL";
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="searchForm" name="form1" action="<%=controller.getURI(request, XyList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户信用管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名： </span>
                                        <input type="text" name="loginName" id="loginName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">用户类型： </span>
                                        <select name="fundAccountType" class="border mr20 h32 mw100">
                                            <option value="0">全部</option>
                                            <%
                                                for (FundXYAccountType fundAccountType : FundXYAccountType.values()) {
                                            %>
                                            <option value="<%=fundAccountType.name()%>" <%if (fundAccountType.name().equals(request.getParameter("fundAccountType"))) {%>
                                                    selected="selected" <%}%>><%=fundAccountType.getName()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li><a href="javascript:onSubmit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportXYgl.class)) {%>
                                        <a href="javascript:void(0);" onclick="showExport();"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%}%>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">用户名</th>
                                    <th class="tc">用户类型</th>
                                    <th class="tc">姓名或企业名称</th>
                                    <th class="tc">授信额度(元)</th>
                                    <th class="tc">信用等级</th>
                                    <th class="tc">最后更新时间</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    FundsXYView[] fundsViews = null;
                                    if (fundsXYViewList != null) {
                                        fundsViews = fundsXYViewList.getItems();
                                    }
                                    if (fundsViews != null) {
                                        for (int i = 0; i < fundsViews.length; i++) {
                                            FundsXYView record = fundsViews[i];

                                            if (record == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.F05);%></td>
                                    <td><%StringHelper.filterHTML(out, record.userType); %></td>
                                    <td><%StringHelper.filterHTML(out, record.userName); %></td>
                                    <td><%=Formater.formatAmount(record.F03)%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.F14);%></td>
                                    <td><%if (record.F04 != null) { %><%=Formater.formatDateTime(record.F04)%><%}%></td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(XYjlList.class)) {%>
                                        <a class="link-blue mr20"
                                           href="<%=controller.getURI(request, XYjlList.class)%>?id=<%=record.F01%>">信用记录</a>
                                        <%} else { %>
                                        <a class="disabled">信用记录</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(XYCredit.class)) { %>
                                        <a href="javascript:void(0)"
                                           onclick="showxycredit('<%=i+1 %>','<%=Formater.formatAmount(record.F03)%>')"
                                           class="mr10 link-blue popup-link" >修改信用额度</a>
                                        <%} else { %>
                                        <a class="disabled">修改信用额度</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(XYCreditLevel.class)) { %>
                                        <a href="javascript:void(0)"
                                           onclick="showxycreditLevel(<%=record.F01 %>,'<%=record.F14 %>')"
                                           class="mr10 link-blue popup-link" data-wh="400*200">修改信用等级</a>
                                        <%} else { %>
                                        <a class="disabled">修改信用等级</a>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="8" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">授信总额度：<em
                                    class="red"><%=Formater.formatAmount(searchAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, fundsXYViewList); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
        <div id="popup_bg" class="popup_bg hide"></div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>

<%
    if (fundsViews != null) {
        for (int i = 0; i < fundsViews.length; i++) {
            FundsXYView record = fundsViews[i];
            if (record == null) {
                continue;
            }
%>
<div class="popup-box hide" id="xycredit_<%=i+1%>" style="min-height: 220px;min-width: 500px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">修改信用额度</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);"
           onclick="javascript:document.getElementById('xycredit_<%=i+1%>').style.display='none';document.getElementById('popup_bg').style.display='none';return false;"></a>
    </div>
    <form action="<%=controller.getURI(request, XYCredit.class)%>" id="xycreditForm_<%=i+1%>" method="post"
          class="form2">
        <input type="hidden" name="userId" value="<%=record.F01%>"/>

        <div class="popup-content-container pt20 ob20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em>信用额度：</span>
                        <input class="text border w150 pl5 required max-precision-2 maxf-size-999999999999.99"
                               type="text" name="userCredit" id="xyedTxt_<%=i+1%>" mtest="/^[0-9.]*$/"
                               mtestmsg="请输入数字，最多保留小数点后两位!">
                        <span tip>精确到小数点后两位</span>
                        <span errortip class="" style="display: none"></span>
                    </li>
                </ul>
            </div>
            <div class="tc f16">
                <a href="javascript:void(0);" name="save" fromname="form2" onclick="xycredit(<%=i+1%>)"
                   class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                   onclick="javascript:document.getElementById('xycredit_<%=i+1%>').style.display='none';document.getElementById('popup_bg').style.display='none';return false;">取消</a>
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" style="min-height: 150px;" id="creditLevel">
    <div class="popup-title-container">
        <h3 class="pl20 f18">修改信用等级</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <form action="<%=controller.getURI(request, XYCreditLevel.class)%>" id= "xycreditFormLevel" method="post" >
        <input type="hidden" name="userIdLevel" value=""/>

        <div class="popup-content-container pt20 ob20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em>信用等级：</span>
                    	<select class="border w150 pl5" name="userCreditLevel">
                    	<%
                    		T6116_F05[] allLevel = T6116_F05.values();
                    		for(T6116_F05 level: allLevel)
                    		{
                    		    %>
                    		    	 <option value="<%=level.name() %>">
                    		    	 		<% StringHelper.filterHTML(out, level.getChineseName()); %>
                    		    	 </option>
                    		    <%
                    		}
                    	%>
                    	</select>
                    </li>
                </ul>
            </div>
            <div class="tc f16">
                <a href="javascript:void(0);" name="save" onclick="xycreditLevel()"
                   class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                   onclick="closeInfo()">取消</a>
            </div>
        </div>
    </form>
</div>
<%
        }
    }
%>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportXYgl.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, XyList.class)%>";
    }
    function showxycredit(i, xyed) {
        $(".popup_bg").show();
        $("#xycredit_" + i).show();
        $("#xyedTxt_" + i).val(xyed.replace(/,/g, ""));
        $("#xyedTxt_" + i).nextAll("span[errortip]").html("");
        $("#xyedTxt_" + i).nextAll("span[tip]").show();
        return;
    }
    
    function xycredit(i) {
        if (checkText($("#xyedTxt_" + i))) {
            $("#xycreditForm_" + i).submit();
        }
        return;
    }
    
    function showxycreditLevel(userIdLevel,xydj) {
    	$(".popup_bg").show();
    	$("#creditLevel").show();
    	$("input[name='userIdLevel']").val(userIdLevel);
    	$("select[name='userCreditLevel']").val(xydj);
    }
    
    function xycreditLevel() {
        $("#xycreditFormLevel").submit();
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#searchForm').submit();
    }
    
</script>
</body>
</html>	