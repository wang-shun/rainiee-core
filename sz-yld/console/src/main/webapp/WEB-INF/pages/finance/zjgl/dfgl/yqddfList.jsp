<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.S51.enums.T5131_F02" %>
<%@page import="com.dimeng.p2p.modules.finance.console.service.PtdfManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.DcYqddf" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F10" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.finance.console.service.entity.DfRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList" %>

<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "DFGL";
    PagingResult<DfRecord> dfRecord = (PagingResult<DfRecord>) request.getAttribute("dfRecord");
    DfRecord[] dfRecordArray = null;
    if(dfRecord != null){
        dfRecordArray = dfRecord.getItems();
    }
    DfRecord yqddfSearchAmount = (DfRecord)request.getAttribute("yqddfSearchAmount");
    //PtdfManage collectionManage = serviceSession.getService(PtdfManage.class);
    T5131_F02 dfType = (T5131_F02)request.getAttribute("dfType");
    /* if (dfType == null || T5131_F02.N.name().equals(dfType.name())) {
        dfRecord = null;
        dfRecordArray = null;
    } */
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" name="form1" action="<%=controller.getURI(request, YqddfList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>不良资产管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款编号： </span>
                                        <input type="text" name="bidNo"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bidNo"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题： </span>
                                        <input type="text" name="loanTitle"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanTitle"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款用户名： </span>
                                        <input type="text" name="loanName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">逾期天数： </span>
                                        <input type="text" name="yuqiFromDays" onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("yuqiFromDays"));%>"
                                               class="text border pl5 w120"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="yuqiEndDays" " onKeyUp="value=value.replace(/\D/g,'')"
                                        value="<%StringHelper.filterHTML(out, request.getParameter("yuqiEndDays"));%>"
                                        class="text border pl5 w120 mr20" />
                                    </li>
                                    <li><span class="display-ib mr5">还款方式： </span>
                                        <select name="hkfs" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T6230_F10.DEBX.name()%>"
                                                    <%if("DEBX".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.DEBX.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.MYFX.name()%>"
                                                    <%if("MYFX".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.MYFX.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.YCFQ.name()%>"
                                                    <%if("YCFQ".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.YCFQ.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.DEBJ.name()%>"
                                                    <%if("DEBJ".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.DEBJ.getChineseName() %>
                                            </option>
                                        </select>
                                    </li>
                                    <li><a href="javascript:onSubmit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DcYqddf.class)) {%>
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
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li id="one1"><a href="javascript:void(0);" class="tab-btn-click select-a">逾期待垫付<i class="icon-i tab-arrowtop-icon"></i></a>
                                    </li>
                                    <li id="one2"><a
                                            href="<%=controller.getStaticPath(request)%>/finance/zjgl/dfgl/ydfList.htm"
                                            class="tab-btn-click">垫付待还款</a></li>
                                    <li id="one3"><a
                                            href="<%=controller.getStaticPath(request)%>/finance/zjgl/dfgl/yjqList.htm"
                                            class="tab-btn-click">已结清</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title">
                                        <th class="tc">序号</th>
                                        <th class="tc">借款编号</th>
                                        <th class="tc">借款标题</th>
                                        <th class="tc">借款用户名</th>
                                        <th class="tc">借款金额(元)</th>
                                        <th class="tc">还款方式</th>
                                        <th class="tc">剩余期数</th>
                                        <th class="tc">应还本金(元)</th>
                                        <th class="tc">应还利息(元)</th>
                                        <th class="tc">逾期罚息(元)</th>
                                        <th class="tc">逾期时间</th>
                                        <th class="tc">逾期天数</th>
                                        <th class="tc">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (dfRecordArray != null && dfRecordArray.length > 0) {
                                            for (int i = 0; i < dfRecordArray.length; i++) {
                                                DfRecord yqddfRecord = dfRecordArray[i];
                                                if (yqddfRecord == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i + 1%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, yqddfRecord.bidNo);%></td>
                                        <td title="<%=yqddfRecord.loanRecordTitle%>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(yqddfRecord.loanRecordTitle, 10)); %></td>
                                        <td><%StringHelper.filterHTML(out, yqddfRecord.loanName); %></td>
                                        <td><%=Formater.formatAmount(yqddfRecord.loanAmount)%>
                                        </td>
                                        <td><%=yqddfRecord.hkfs.getChineseName()%>
                                        </td>
                                        <td><%=yqddfRecord.loandeadline%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.dhbj)%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.dhlx)%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.yuqiAmount)%>
                                        </td>
                                        <td>
                                            <%
                                                Date refunDay = yqddfRecord.refundDay;
                                                Calendar yqCal = Calendar.getInstance();
                                                if (refunDay != null) {
                                                    yqCal.setTime(refunDay);
                                                    yqCal.add(Calendar.DAY_OF_MONTH, 1);
                                                }
                                            %>
                                            <%=Formater.formatDate(refunDay == null ? null : yqCal.getTime())%>
                                        </td>
                                        <td><%=yqddfRecord.yuqi%>
                                        </td>
                                        <td>
                                            <a href="<%=controller.getURI(request, ViewProject.class) %>?loanId=<%=yqddfRecord.bidId %>&userId=<%=yqddfRecord.userID %>&operationJK=DFCK"
                                               class="link-blue">查看</a>
                                            <%if (dimengSession.isAccessableResource("P2P_C_FINANCE_SBDF_PTDF")) {%>
                                            <a href="javascript:void(0)" onclick="showDf('<%=i %>')" class="link-blue">垫付</a>
                                            <%} else { %>
                                            <span class="disabled">垫付</span>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="13" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(yqddfSearchAmount.loanAmount) %>
                            </em> 元</span>
                            <span class="mr30">应还本金总金额：<em
                                    class="red"><%=Formater.formatAmount(yqddfSearchAmount.dhbj) %>
                            </em> 元</span>
                            <span class="mr30">应还利息总额：<em
                                    class="red"><%=Formater.formatAmount(yqddfSearchAmount.dhlx) %>
                            </em> 元</span>
                            <span class="mr30">逾期罚息总额：<em
                                    class="red"><%=Formater.formatAmount(yqddfSearchAmount.yuqiAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, dfRecord); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<%
    if (dfRecordArray != null) {
        for (int i = 0; i < dfRecordArray.length; i++) {
            DfRecord yqddfRecord = dfRecordArray[i];
            if (yqddfRecord == null) {
                continue;
            }
%>
<div id="df_<%=i%>" style="display: none">
    <div class="popup-box">
        <div class="popup-title-container">
            <h3 class="pl20 f18">垫付信息确认</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);"
               onclick="javascript:document.getElementById('df_<%=i%>').style.display='none';return false;"></a>
        </div>
        <form action="" method="post" class="form<%=i%>">
            <input type="hidden" name="type" value="yqddf"/>
            <input type="hidden" name="bidId" value="<%=yqddfRecord.bidId %>"/>

            <div class="popup-content-container pt20 ob20 clearfix">
                <div class="mb40 gray6">
                    <ul>
                        <li class="mb10"><span
                                class="display-ib tr mr5">确定对标的“<%=yqddfRecord.loanRecordTitle%>”进行垫付吗？</span>
                        </li>
                        <li class="mb10"><span class="display-ib tr mr5">垫付本金：</span>
                            <span class="pl10"><%=Formater.formatAmount(yqddfRecord.dhbjTotal)%>元</span>
                            <i class="icon-i w30 h30 va-middle gantanhao-icon2 pr">
                              <span class="prompt-container prompt-container2" style="display:none;">
                                <i class="icon-i prompt-icon"></i>
                                <span class="display-b w200 lh20 tl">垫付本金=逾期本金+剩余本金</span>
                              </span>
                            </i>
                        </li>
                        <%if (dfType.name().equalsIgnoreCase(T5131_F02.BX.name())) { %>
                        <li class="mb10"><span class="display-ib tr mr5">垫付利息：</span>
                            <span class="pl10"><%=Formater.formatAmount(yqddfRecord.dhlxTotal.add(yqddfRecord.yuqiAmount))%>元</span>
                            <i class="icon-i w30 h30 va-middle gantanhao-icon2 pr">
                              <span class="prompt-container prompt-container2" style="display:none;">
                                <i class="icon-i prompt-icon"></i>
                                <span class="display-b w200 lh20 tl">垫付利息=逾期利息+剩余利息+逾期罚息</span>
                              </span>
                            </i>
                        </li>
                        <li class="mb10"><span class="display-ib tr mr5">本次垫付共计：</span>
                            <span class="pl10"><%=Formater.formatAmount(yqddfRecord.dinfuAmount)%>元</span>
                            <i class="icon-i w30 h30 va-middle gantanhao-icon2 pr">
                              <span class="prompt-container prompt-container2" style="display:none;">
                                <i class="icon-i prompt-icon"></i>
                                <span class="display-b w200 lh20 tl">本次垫付共计=垫付本金+垫付利息+垫付罚息</span>
                              </span>
                            </i>
                        </li>
                        <%} %>
                    </ul>
                </div>
                <div class="tc f16">
                    <a href="javascript:void(0)"
                       class="btn-blue2 btn white radius-6 pl20 pr20" onclick="resubmit('<%=configureProvider.format(URLVariable.PTADVANCE_URL) %>?loanId=<%=yqddfRecord.bidId %>&period=<%=(yqddfRecord.periods - yqddfRecord.period) +1%>','df_<%=i%>');">确定</a>
                    <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="javascript:document.getElementById('df_<%=i%>').style.display='none';return false;">取消</a>
                </div>
            </div>
        </form>
    </div>
    <div class="popup_bg"></div>
</div>
<%
        }
    }
%>

<div id="info"></div>
<script type="text/javascript"  src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(".c").show();
    $("#info").html(showDialogInfo('<%=message%>', "wrong"));
</script>
<%} %>
<%
    String messageError = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(messageError)) {
%>
<script type="text/javascript">
    $(".c").show();
    $("#info").html(showDialogInfo('<%=messageError%>', "wrong"));
</script>
<%} %>
<%
    String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(messageInfo)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=messageInfo%>', "yes"));
</script>
<%} %> 
<div id="realse" style="display: none;"></div>
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
        <%if (dfRecordArray != null) {for (int i =0;i<dfRecordArray.length;i++){DfRecord yqddfRecord=dfRecordArray[i];if (yqddfRecord == null) {continue;}%>
        $('#tempDatepicker<%=i%>').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                checkDate('<%=i%>');
            }
        });
        $('#tempDatepicker<%=i%>').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%}}%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcYqddf.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, YqddfList.class)%>";
    }

    function showDf(i) {
        $("#df_" + i).show();
    }

    function showDfConf(title, id) {
        if (confirm("您确定要垫付“" + title + "”标?")) {
            document.getElementById(id).style.display = 'none';
            return true;
        } else {
            return false;
        }
    }

    function showCk(i) {
        $("#ck_" + i).show();
    }

    function closeInfoShow() {
        $("#closeInfoShow").hide();
        $(".popup_bg").hide();
    }

    function checkDate(i) {
        //window.setTimeout(function(){
        var dates = $("#tempDatepicker" + i);
        var value = dates.val();
        var $error = dates.nextAll("p[errortip]");
        var $tip = dates.nextAll("p[tip]");
        if ($.trim(value) != "") {
            $error.removeClass("error_tip");
            $error.hide();
            $tip.show();
        }
        //},2000);
    }
    $(function () {
        $(".gantanhao-icon2").mouseover(function () {
            $(this).children(":first").show();
        }).mouseleave(function () {
            $(this).children(":first").hide();
        });
    });
    
    function resubmit(urlValue,id)
    {
    	$("#realse").hide();
    	if('<%=escrow%>' == 'shuangqian')
   		{
	    	window.open(urlValue);
	    	
	    	$("#"+id).hide();
	    	refreshCurrentPage();
   		
   		}
    	else
   		{
	    	location.href=urlValue;
   		}
    	
    	//var token = $("input[name=token]").val();
    	//window.open(urlValue+"&token="+token);
    }
    
  //提现审核时确认放款后提示信息，中间跳转操作
    function refreshCurrentPage()
    {
    	$("#realse").html("");
    	var arr = new Array();
    	arr.push("<div class='popup-box' style='min-height:200px;width:400px'>"
    			+"<div class='popup-title-container'>"
    			+"<h3 class='pl20 f18'>提示</h3>"
    			+"<a class='icon-i popup-close2' href='<%=controller.getURI(request, YqddfList.class) %>' onclick='refreshPage();'></a>"
    			+"</div>"
    			+"<div class='popup-content-container pt20 ob20 clearfix'>"
    			+"<div class='mb20 gray6 f18' style='text-align: center;'>"
    			+"<ul>"
    			+"<li class='mb10'><span class='display-ib tr mr5'>请您在新打开的页面上完成操作</span></li>"
    			+"</ul>"
    			+"</div>"
    			+"<div class='tc f16'>"
    			+"<a href='<%=configureProvider.format(URLVariable.INDEX).concat("/console/finance/zjgl/dfgl/ydfList.htm") %>' onclick='refreshPage();' class='btn-blue2 btn white radius-6 pl20 pr20' >垫付成功</a>"
    			+"<a class='btn btn-blue2 radius-6 pl20 pr20 ml40' href='<%=controller.getURI(request, YqddfList.class) %>' onclick='refreshPage();'>垫付失败</a>"
    			+"</div>"
    			+"</div>"
    			+"</div>"
    			+"<div class='popup_bg'></div>");
    	
    	$("#realse").html(arr.join(""));
    	
    	$("#realse").show();
    }

    function refreshPage()
    {
    	$("#realse").hide();
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#form1').submit();
    }
</script>
</body>
</html>