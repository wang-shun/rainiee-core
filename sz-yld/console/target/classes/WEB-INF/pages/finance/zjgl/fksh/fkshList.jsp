<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProject" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Fksh" %>
<%@page import="com.dimeng.util.Formater" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fkcjjl.CjRecordList" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.NotLoan" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.Loan" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ExportFkshInfo" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<%
    PagingResult<Fksh> result = (PagingResult<Fksh>) request.getAttribute("result");
	Fksh searchAmount = (Fksh)request.getAttribute("searchAmount");
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "FKGL";
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    //托管前缀
    String escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                

                <form id="searchForm" name="form1" action="<%=controller.getURI(request, FkshList.class)%>"
                      method="post">
                      <%=FormToken.hidden(serviceSession.getSession()) %>
                <input type="hidden" name="token" value=""/>
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>放款管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款ID： </span>
                                        <input type="text" name="zqId" id="zqId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("zqId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户： </span>
                                        <input type="text" name="account" id="account"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("account"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态： </span>
                                        <select name="fundStatus" class="border mr20 h32 mw100">
                                            <option value="0">全部</option>
                                            <option value="1" <%if ("1".equals(request.getParameter("fundStatus"))) {%>
                                                    selected="selected" <%}%> >待放款
                                            </option>
                                            <option value="2" <%if ("2".equals(request.getParameter("fundStatus"))) {%>
                                                    selected="selected" <%}%> >已流标
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">满标时间： </span>
                                        <input type="text" name="startExpireDatetime"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("startExpireDatetime")); %>"
                                               readonly="readonly" id="datepicker1" class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endExpireDatetime")); %>"
                                               name="endExpireDatetime" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:void(0);" onclick="$('#searchForm').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportFkshInfo.class)) {%>
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
                                    <th class="tc">借款ID</th>
                                    <th class="tc">借款标题</th>
                                    <th class="tc">借款账户</th>
                                    <th class="tc">借款金额(元)</th>
                                    <th class="tc">投资金额(元)</th>
                                    <th class="tc">红包金额(元)</th>
                                    <th class="tc">年化利率</th>
                                    <th class="tc">期限</th>
                                    <th class="tc">满标时间</th>
                                    <th class="tc">体验金金额(元)</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    Fksh[] fkshs = result.getItems();
                                    if (fkshs != null && fkshs.length > 0) {
                                        int i = 1;
                                        for (Fksh fksh : fkshs) {
                                            if (fksh == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=fksh.F25%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, fksh.F03);%>">
                                        <%StringHelper.filterHTML(out, StringHelper.truncation(fksh.F03, 10));%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, fksh.userName);%></td>
                                    <td><%=Formater.formatAmount(fksh.F05)%>
                                    </td>
                                    <td><%=Formater.formatAmount(fksh.F05.subtract(fksh.F07))%>
                                    </td>
                                    <td><%=Formater.formatAmount(fksh.hbAmount)%>
                                    </td>
                                    <td><%=Formater.formatRate(fksh.F06)%>
                                    </td>
                                    <td>
                                        <%
                                            if (T6231_F21.S == fksh.t6231_F21) {
                                                out.print(fksh.limitDays);
                                        %>天
                                        <%
                                        } else {
                                            out.print(fksh.days);
                                        %>个月<%} %>
                                    </td>
                                    <td><%=DateTimeParser.format(fksh.mbTime, "yyyy-MM-dd HH:mm")%>
                                    </td>
                                    <td><%=Formater.formatAmount(fksh.experAmount)%>
                                    </td>
                                    <td>
                                        <%if (T6230_F20.YLB != fksh.F20) { %>
                                        <%if (dimengSession.isAccessableResource(ViewProject.class)) {%>
                                        <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=fksh.F01 %>&userId=<%=fksh.F02 %>"
                                           class="link-blue">查看</a>
                                        <%} else { %>
                                        <span class="disabled">查看</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(Loan.class)) {%>
                                        <a href="javascript:void(0)" class="link-blue popup-link"
                                           onclick="load('<%=configureProvider.format(URLVariable.LOAN_URL)%>?id=<%=fksh.F01%>','<%=fksh.F03%>','<%=fksh.userName%>','<%=Formater.formatAmount(fksh.F05.subtract(fksh.F07))%>','<%StringHelper.filterHTML(out, StringHelper.truncation(fksh.F03, 10));%>')" data-wh="400*250">放款</a>
                                        <%} else { %>
                                        <span class="disabled">放款</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(NotLoan.class)) {%>
                                        <a href="javascript:void(0)" class="link-blue popup-link"
                                           onclick="notLoad('<%=fksh.F01%>')" data-wh="500*320">不放款</a>
                                        <%} else { %>
                                        <span class=disabled>不放款</span>
                                        <%} %>
                                        <%} else { %>
                                        <span class=disabled>已流标</span>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="12" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.F05) %>
                            </em> 元</span>
                            <span class="mr30">投资总金额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.F05.subtract(searchAmount.F07)) %>
                            </em> 元</span>
                            <span class="mr30">红包总额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.hbAmount) %>
                            </em> 元</span>
                            <span class="mr30">体验金总额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.experAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, result); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</div>


<div id="reasonDiv" style="display: none;">
    <div class="popup-box" style="min-height: 270px;">
        <form action="<%=configureProvider.format(URLVariable.NOTLOAN_URL)%>" id="notLoan_form" method="post">
            <input type="hidden" name="id" id="id"/>

            <div class="popup-title-container">
                <h3 class="pl20 f18">不放款</h3>
                <a class="icon-i popup-close2" href="javascript:void(0);"
                   onclick="javascript:document.getElementById('reasonDiv').style.display='none';return false;"></a>
            </div>
            <div class="popup-content-container pt20 ob20 clearfix" >
                <div class="mb20 gray6">
                    <p class="h30 lh30"><em class="red pr5">*</em>流标说明：</p>

                    <div>
                        <textarea class="w400 h120 border p5" rows="4" cols="40" name="des" id="lbDes"></textarea>
                        <span id="errortip" class="error_tip pl20"></span>
                    </div>
                </div>
                <div class="tc f16">
                    <input type="submit" onclick="return check();" class="btn-blue2 btn white radius-6 pl20 pr20" value="确定" />
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="javascript:document.getElementById('reasonDiv').style.display='none';return false;">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="javascript:document.getElementById('reasonDiv').style.display='none';return false;"/>
                </div>
            </div>
        </form>
    </div>
    <div class="popup_bg"></div>
</div>

<div id="realse" style="display: none;"></div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/txgl/fksh.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        var msg = '<%=request.getAttribute("EMAIL_ERROR")==null ? "" : request.getAttribute("EMAIL_ERROR")%>';
        if (msg) {
            alert(msg);
        }
    });
    function check() {
        var area = $.trim($("#lbDes").val());
        var max = 0; //获取maxlength的值 
        <%if("huifu".equals(escrowPrefix)){ %>
        	max = 15;
	    <%} else { %>
	    	max = 100;
	    <%}%>
        if(area.length == 0){
        	$("#errortip").html("不能为空！");
        	return false;
        }
        if(area.length > max){//textarea的文本长度大于maxlength
        	$("#errortip").html("超过输入限制" + max + ",当前长度为" + area.length);
            return false;
        }
        return true;
    }
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportFkshInfo.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, FkshList.class)%>";
    }
</script>

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
</body>
<script type="text/javascript">
    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='token']").val(tokenValue);
    });

</script>
</body>
</html>