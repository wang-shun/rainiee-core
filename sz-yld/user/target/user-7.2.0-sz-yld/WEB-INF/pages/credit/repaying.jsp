<%@page import="com.dimeng.p2p.modules.bid.user.service.WdjkManage" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.HkEntity" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.*" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME);%>_借款管理_我的借款</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    WdjkManage manage = serviceSession.getService(WdjkManage.class);
	Boolean isBadClaimTransfer = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    HkEntity[] repayLoans = null;
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "WDJK";
%>
<div class="clear"></div>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="charity_total clearfix">
                <div class="total fl" id="repayCountMoneyId">
                    借款总金额(元)<br/> <span class="orange f22">0.00</span>
                </div>
                <div class="line"></div>
                <div class="list">
                    <ul id="repayUlId">
                        <li>逾期金额(元)<br/>0.00
                        </li>
                        <li>待还金额(元)<br/>0.00
                        </li>
                        <li>近30天应还金额(元)<br/>0.00
                        </li>
                    </ul>
                </div>
            </div>
            <div class="user_mod border_t15">
                <div id="divAppendId" class="user_tab clearfix">
                    <ul>
                        <li id="hkzdLiId" class="hover" onclick="repay();">还款中的借款<i></i></li>
                        <li id="yhqdjkLiId" onclick="yhqdjk();return false">已还清的借款<i></i></li>
                        <%if(isBadClaimTransfer){ %>
                        <li id="yzrdjkLiId" onclick="yzrdjk();return false">已转让的借款<i></i></li>
                        <%} %>
                    </ul>
                </div>
                <div id="yhqdjqDivId" class="user_table pt5" style="display: none;">
                    <table id="yhqdjqTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr class="til">
                        	<td align="center">序号</td>
                            <td align="center">借款标题</td>
                            <td align="center">借款金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">期限</td>
                            <td align="center">还款总额(元)</td>
                            <td align="center">还清日期</td>
                        </tr>
                        </thead>
                        <tr>
                            <td colspan="7" align="center">没有记录</td>
                        </tr>
                    </table>
                </div>
                <%if(isBadClaimTransfer){ %>
                <div id="yzrdjqDivId" class="user_table pt5" style="display: none;">
                    <table id="yzrdjqTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr class="til">
                        	<td align="center">序号</td>
                            <td align="center">借款标题</td>
                            <td align="center">借款金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">转让期限</td>
                            <td align="center">转让总额(元)</td>
                            <td align="center">转让日期</td>
                            <!-- <td align="center">操作</td> -->
                        </tr>
                        </thead>
                        <tr>
                            <td colspan="7" align="center">没有记录</td>
                        </tr>
                    </table>
                </div>
                <%} %>
                <div class="page" id="pageContent"></div>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="popup_bg" style="display:none;"></div>
<div id="hiddenDivListId"></div>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<div class="popup_bg"></div>
<div class="dialog " id="errorDiv">
    <div class="title"><a href="javascript:void(0);" class="out"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="error"></div>
            <div class="tips">
			<span class="f20 gray33">
				<%
                    StringHelper.filterHTML(out, errorMessage);
                %>
			</span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="javascript:void(0);" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%
    }
%>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<div class="popup_bg"></div>
<div class="dialog " id="errorDiv">
    <div class="title"><a href="javascript:void(0);" class="out"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="successful"></div>
            <div class="tips">
			<span class="f20 gray33">
				<%
                    StringHelper.filterHTML(out, infoMessage);
                %>
			</span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="<%=controller.getViewURI(request, Repaying.class)%>" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%
    }
%>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="popup_bg"></div>
<div class="dialog " id="warringDiv">
    <div class="title"><a href="javascript:void(0);" class="out"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
			<span class="f20 gray33"><%
                if ("余额不足".equals(warringMessage)) {
            %>您的账户不足以本次还款，请<a href='<%configureProvider.format(out,URLVariable.USER_CHARGE);%>' class="blue">充值</a>
				<%} else {%><%StringHelper.filterHTML(out, warringMessage);%><%} %></span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="javascript:void(-1);" href="#" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%} %>
<input type="hidden" id="ajaxUrl" value="<%=controller.getURI(request, Repaying.class)%>"/>
<input type="hidden" id="ajaxPayOffUrl" value="<%=controller.getURI(request, PayOff.class)%>"/>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    function showPayment(id) {
        $("#r" + id).show();
        $(".popup_bg").show();
    }
    function showForwardPayment(id) {
        $("#t" + id).show();
        $(".popup_bg").show();
    }
    function closeDiv(id) {
        $("#" + id).hide();
        $(".popup_bg").hide();
    }
    function confirmRepay(id) {
        $("#confirmBtn_" + id).hide();
        $("#confirmLoading_" + id).show();
    }

    function tq_confirmRepay(id) {
        $("#tq_confirmBtn_" + id).hide();
        $("#tq_confirmLoading_" + id).show();
    }
    $(".close").click(function () {
        $("#errorDiv").hide();
        $("#warringDiv").hide();
        $(".popup_bg").hide();
    });

</script>
<script type="text/javascript">
    var currentPage = 1;
    var pageSize = 5;
    var pageCount = 1;

    $(function () {
        repay();
    });

    /**
     *我的借款
     */
    function repay() {
        $("#yhqdjqDivId").hide();
        $("#yhqdjkLiId").removeClass();
        $("#yzrdjqDivId").hide();
        $("#yzrdjkLiId").removeClass();
        $("#hkzdLiId").attr("class", "hover");


        var ajaxUrl = $("#ajaxUrl").val();
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};
        //先清除原有的数据
        for (var i = 0; i < pageSize; i++) {
            var divId = "#divId" + i;
            $(divId).remove();
        }

        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            async: false,
            success: function (returnData) {
            	if(returnData.loginTimeOut){
            		window.location.href="<%=controller.getViewURI(request, Login.class)%>";
            		return;
            	}
                //借款总金额
                var txt = "借款总金额(元)<br /><span class='orange f22'>" + fmoney(returnData.loanCount.countMoney, 2) + "</span>";
                $("#repayCountMoneyId").html(txt);
                //ul添加li
                var ulLiTxt = "<li>逾期金额(元)<br/>" + fmoney(returnData.loanCount.overdueMoney, 2) + "</li>" +
                        "<li>待还金额(元)<br/>" + fmoney(returnData.loanCount.repayMoney, 2) + "</li>" +
                        "<li>近30天应还金额(元)<br/>" + fmoney(returnData.loanCount.newRepayMoney, 2) + "</li>";
                $("#repayUlId").html("");
                $("#repayUlId").html(ulLiTxt);
                if (null == returnData || null == returnData.wdjkList || returnData.wdjkList.length == 0) {
                    var noRecordUrl = "<div name='nodatadiv' id='noRecordDivId' class='temporarily-pic'></div>";
                    if ($("div[name='nodatadiv']").length == 0) {
                        $("#divAppendId").after(noRecordUrl);
                    }
                    $("#pageContent").html("");
                    return;
                }
                //分页信息
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 1);
                });
                
                //填充表格数据
                if (returnData.wdjkList.length > 0) {
                    var rAssests = returnData.wdjkList;
                    $(".borrowing_list").remove();
                    var jkdiv = "<div class='borrowing_list pt5'><ul>";
                    $.each(rAssests, function (n, value) {
                        var recordUrl = "";
                        var divId = "divId" + n;
                        recordUrl += "<li id='" + divId + "' class='item'>";

                        var zdhk = "";
                        <% 
		            	String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
		            	if(null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX)){%>
	                        if (null == value.ext) {
	                        	zdhk="<a class='btn04 mr10' href='/user/yeepay/openAutoRepaymentServlet.htm?loadId="+value.F01+"'>开启自动还款</a>";
	                        } else {
	                        	zdhk="<a class='btn04 mr10' href='/user/yeepay/colseAutoRepaymentServlet.htm?loadId="+value.F01+"'>关闭自动还款</a>";
	                        }
	                        if (value.F20 == "YDF") {
	                        	zdhk="<span class='btn04 mr10 btn_gray'>开启自动还款</span>";
	                        }
                        <%}%>
                        
                        
                        
                        var tqhkUrl = "<span class='btn04 btn_gray'>提前还款</span>";//提前还款
                        if (value.F28.F19 == "F" && value.F20 != "YDF") {//if(repayLoan.F28.F19.name() == T6231_F19.F.name() && T6230_F20.YDF.name() != repayLoan.F20.name())
                            tqhkUrl = "<a href='javascript:void(0)' onclick=\"showForwardPayment('" + value.F01 + "')\" class='btn04'>提前还款</a>";
                        }
                        //查看合同
                        var htUrl = "<%configureProvider.format(out, URLVariable.XY_PTDZXY); %>?id=" + value.F01;
                        var loanDetailUrl = "<%=controller.getViewURI(request, LoanDetail.class)%>?id=" + value.F01;
                        var dfhtUrl = "<%configureProvider.format(out, URLVariable.XY_PTDZXY); %>?df=df&id=" + value.F01;

                        recordUrl += "<div class='clearfix'>";
                        recordUrl += "<div class='fl'>";
                        recordUrl += "<a class='gray3 f18' title='" + value.F03 + "' href='" + "<%=configureProvider.format(URLVariable.FINANCING_SBTZ_XQ)%>" + value.F01 + "<%=rewriter.getViewSuffix() %>" + "'>" + value.F03 + "</a>";
                        recordUrl += "<a target='_blank' href='" + htUrl + "' class='highlight ml10'>借款合同</a>";
                        if(value.F20 == "YDF"){
                        	recordUrl += "<a target='_blank' href='" + dfhtUrl + "' class='highlight ml10'>垫付合同</a>";
                        }
                        recordUrl += "<a href='" + loanDetailUrl + "' class='highlight ml10'>还款详情</a>";
                        recordUrl += "</div>";
                        recordUrl += "<div class='fr'>";
                        recordUrl += zdhk;
                        recordUrl += tqhkUrl;
                        recordUrl += "<a href='javascript:void(0)' onclick=\"showPayment('" + value.F01 + "')\" class='ml10 btn04'>还款</a>";
                        recordUrl += "</div></div>";

                        recordUrl += "<ul class='info'>";
                        recordUrl += "<li>借款金额(元)：" + fmoney((value.F05 - value.F07), 2) + "</li>";
                        recordUrl += "<li>年化利率：" + fmoney(value.F06 * 100, 2) + "%</li>";
                        var startTime = 0;
                        var endTime = 0;
                        var xghkr = "";//下个还款日
                        if (null != returnData.t6231List && returnData.t6231List.length > 0) {
                            var timeResultList = returnData.t6231List;
                            $.each(timeResultList, function (m, value1) {
                            	if(value1 != null){
	                                if (value1.F01 == value.F01) {
	                                    startTime = value1.F03;
	                                    endTime = value1.F02;
	                                    xghkr = getTime(value1.F06);
	                                }
	                            }
                            });
                        }

                        recordUrl += "<li>剩余期数(期)：" + value.leftNum + "/" + value.totalNum + "期</li>";
                        recordUrl += "<li>下个还款日：" + xghkr + "</li>";
                        recordUrl += "<li>还款金额(元)：" + fmoney(value.dqyhje, 2) + "</li>";
                        recordUrl += "<li>状态：" + getChineseName(value.F20) + "</li>";
                        recordUrl += "</ul></li>";

                        jkdiv += recordUrl;
                        var repayLoan = value;
                        if (repayLoan != null) {
                            var repayInfo = repayLoan.repayInfo;
                            var forwardRepayInfo = repayLoan.forwardRepayInfo;
                            if (null != repayInfo) {
                                //还款div
                                var htmlUrl = "";
                                htmlUrl += "<div class='dialog refund_dialog' style='display: none' id='r" + repayInfo.loanID + "'>";
                                htmlUrl += "<div class='title'><a href='javascript:void(0);' class='out'></a>还款</div>";
                                htmlUrl += "<div class='content'><div class='border_b f16 pb15 mb15'>";
                                htmlUrl += "<span class='gray3'>当期还款总需(元)：</span><span class='red'>" + fmoney(repayInfo.loanTotalMoney, 2) + "</span>" +
                                        "<span class='gray3 ml20'>可用金额(元)：</span> <span class='red'>" + fmoney(repayInfo.accountAmount, 2) + "</span></div>";
                                htmlUrl += "<form action='<%=configureProvider.format(URLVariable.PAY_PAYMENT_URL)%>' method='post'>";
                                htmlUrl += "<input type='hidden' name='id' value='" + repayInfo.loanID + "' /><input type='hidden' name='number' value='" + repayInfo.number + "' />";
                                htmlUrl += "<div class='border_b pb15 mb15 lh24'><table width='100%' border='0' cellspacing='0' ><tr><td>当期应还本息(元)</td><td>逾期金额(元)</td></tr>";
                                htmlUrl += "<tr><td><i class='red'>" + fmoney(repayInfo.loanMustMoney, 2) + "</i></td><td><i class='red'>" + fmoney(repayInfo.loanArrMoney, 2) + "</i></td></tr></table>";
                                htmlUrl += "</div><div class='clear'></div>";
                                htmlUrl += "<div id='confirmBtn_" + repayInfo.loanID + "' class='tc mt20'>";
                                htmlUrl += "<input type='submit' class='btn01' value='确认' onclick='confirmRepay(" + repayInfo.loanID + ")'/>" +
                                        "<input type='button' onclick=\"closeDiv('r" + repayLoan.F01 + "')\" class='btn01 btn_gray ml20' value='取 消' />";
                                htmlUrl += "</div><div id='confirmLoading_" + repayInfo.loanID + "' style='color:red; display: none; text-align:center;'>正在处理，请稍候...</div>";
                                htmlUrl += "</form></div></div>";
                                $("#hiddenDivListId").after(htmlUrl);
                            }

                            //提前还款div
                            if (null != forwardRepayInfo) {
                                var htmlRepayInfoUrl = "";
                                htmlRepayInfoUrl += "<div class='dialog refund_dialog' style='display:none;' id='t" + repayInfo.loanID + "'>";
                                htmlRepayInfoUrl += "<div class='title'><a href='javascript:void(0);' class='out'></a>提前还款</div><div class='content'><div class='border_b f16 pb15 mb15'>";
                                htmlRepayInfoUrl += "<span class='gray3'>提前还款总需(元)：</span><span class='red'>" + fmoney(forwardRepayInfo.loanTotalMoney, 2) + "</span>";
                                htmlRepayInfoUrl += "<span class='gray3 ml20'>可用金额(元)：</span><span class='red'>" + fmoney(repayInfo.accountAmount, 2) + "</span></div>";
                                htmlRepayInfoUrl += "<form action='<%=configureProvider.format(URLVariable.PAY_PREPAYMENT_URL)%>' method='post'>";
                                htmlRepayInfoUrl += "<input type='hidden' name='id' value='" + forwardRepayInfo.loanID + "' /><input type='hidden' name='number' value='" + forwardRepayInfo.number + "' />";
                                htmlRepayInfoUrl += "<div class='border_b pb15 mb15 lh24'><table width='100%' border='0' cellspacing='0' >";
                                htmlRepayInfoUrl += "<tr><td>当期应还本息(元)</td><td>剩余本金(元)</td><td>违约金(元)</td><td>提前还款手续费(元)</td></tr>";
                                htmlRepayInfoUrl += "<tr><td>" + fmoney(forwardRepayInfo.loanMustMoney, 2) + "</td>";
                                htmlRepayInfoUrl += "<td>" + fmoney(forwardRepayInfo.sybj, 2) + "</td>";
                                htmlRepayInfoUrl += "<td>" + fmoney(forwardRepayInfo.loanPenalAmount, 2) + "</td>";
                                htmlRepayInfoUrl += "<td>" + fmoney(forwardRepayInfo.loanManageAmount, 2) + "</td></tr>";
                                htmlRepayInfoUrl += "</table></div><div class='clear'></div>";
                                $("#hiddenDivListId").after(htmlRepayInfoUrl);
                                if ((repayInfo.accountAmount - forwardRepayInfo.loanTotalMoney) < 0) {
                                    htmlRepayInfoUrl += "<div class='tc mt20'> <p class='f20'>您的账户的可用金额不足，请<a href='<%configureProvider.format(out,URLVariable.USER_CHARGE);%>' class='blue'>充值</a></p></div>";
                                } else {
                                    var forwardSum = forwardRepayInfo.loanPenalAmount + forwardRepayInfo.loanManageAmount;
                                    htmlRepayInfoUrl += "<div class='gray3 f16'>";
                                    htmlRepayInfoUrl += "提前还款可为您节约 <span class='red'>" + fmoney(forwardRepayInfo.sylx - forwardSum, 2) + "</span>&nbsp;元&nbsp;" +
                                            "<span class='hover_tips'> <div class='hover_tips_con'><div class='arrow'></div><div class='border'>节约金额=剩余利息-总手续费(违约金+提前还款手续费)</div> </div>";
                                    htmlRepayInfoUrl += "</span></div>";
                                    htmlRepayInfoUrl += "<div id='tq_confirmBtn_" + repayInfo.loanID + "' class='tc mt20'>"
                                    htmlRepayInfoUrl += "<input type='submit' class='btn01' value='确认' onclick='tq_confirmRepay(" + repayInfo.loanID + ")'/>" +
                                            "<input type='button' onclick=\"closeDiv('t" + repayLoan.F01 + "')\" class='btn01 btn_gray ml20' value='取 消' /></div>";
                                }
                                htmlRepayInfoUrl += "<div class='clear'></div><div id='tq_confirmLoading_" + repayInfo.loanID + "' style='color:red; display: none; text-align:center;'>正在处理，请稍候...</div></form></div></div>";
                                $("#hiddenDivListId").after(htmlRepayInfoUrl);
                            }
                            
                        }
                    });
                    setOutClick();
                    jkdiv += "</ul></div>";
                    $("#divAppendId").after(jkdiv);
                }
            },
            error: function (XMLHttpRequest, textStatus) {
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
            }
        });
    }

    /**
     *已还清的借款
     * @date 2015-05-13
     * @author luoyi
     */
    function yhqdjk() {
        $("#yhqdjqDivId").show();
        $("#noRecordDivId").remove(); 
        
        $("#yzrdjqDivId").hide();
        $("#yzrdjkLiId").removeClass();
        
        $("#hkzdLiId").removeClass();
        $("#yhqdjkLiId").attr("class", "hover");
        //先清除原有的数据
        for (var i = 0; i < pageSize; i++) {
            var divId = "#divId" + i;
            $(divId).remove();
        }
        $("#pageContent").html("");
        $("#yhqdjqTableId tbody tr").empty();

        var ajaxUrl = $("#ajaxPayOffUrl").val();
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};

        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
            	if(returnData.loginTimeOut){
            		window.location.href="<%=controller.getViewURI(request, Login.class)%>";
            		return;
            	}
            	
                if (null == returnData || null == returnData.yjqList || returnData.yjqList.length == 0) {
                    $("#yhqdjqTableId tbody").html("");
                    $("#yhqdjqTableId tbody").append("<tr class='nodatatr'><td colspan='7' align='center'>暂无数据</td></tr>");
                    $("#pageContent").html("");
                    return;
                }

                //借款总金额
                var txt = "借款总金额(元)<br /><span class='orange f22'>" + fmoney(returnData.loanCount.countMoney, 2) + "</span>";
                $("#repayCountMoneyId").html(txt);
                //ul添加li
                var ulLiTxt = "<li>逾期金额(元)<br/>" + fmoney(returnData.loanCount.overdueMoney, 2) + "</li>" +
                        "<li>待还金额(元)<br/>" + fmoney(returnData.loanCount.repayMoney, 2) + "</li>" +
                        "<li>近30天应还金额(元)<br/>" + fmoney(returnData.loanCount.newRepayMoney, 2) + "</li>";
                $("#repayUlId").html("");
                $("#repayUlId").html(ulLiTxt);
                //分页信息
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 2);
                });

                if (returnData.yjqList.length > 0) {
                    $("#yhqdjqTableId tbody").empty();
                    var rAssests = returnData.yjqList;
                    $.each(rAssests, function (n, value) {
                        var trHtmlStr = "";
                        var hrefUrl = "<%=configureProvider.format(URLVariable.FINANCING_SBTZ_XQ)%>" + value.F01 + "<%=rewriter.getViewSuffix() %>";
                        trHtmlStr += "<tr><td align='center'>" + (n+1) + "</td><td align='center'><a class='highlight' href='" + hrefUrl + "'  title='"+value.F03+"'>" + subStringLength(value.F03,10,"...") + "</a></td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.F26, 2) + "</td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.F06 * 100, 2) + "%</td>";
                        var timeStr = "";
                        var t6231_F13 = "";
                        $.each(returnData.t6231List, function (n, value1) {
                            if (value1.F01 == value.F01) {
                                t6231_F13 = getTime(value1.F13);
                            }
                        })
                        if (value.F28.F21 == "F") {//T6231_F21.F
                            timeStr = value.F09 + "个月";
                        } else if (t6231_F13.length > 0) {
                            timeStr = value.F28.F22 + "天";
                        }
                        trHtmlStr += "<td align='center'>" + timeStr + "</td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.hkTotle, 2) + "</td>";
                        trHtmlStr += "<td align='center'>" + t6231_F13 + "</td></tr>";
                        $("#yhqdjqTableId").append(trHtmlStr);
                    });
                }
            },
            error: function (XMLHttpRequest, textStatus) {
                //var trHTML = "<tr><td colspan='7' align='center'>数据错误</td></tr>";
                //$("#yhqdjqTableId tbody").append(trHTML);
                //alert(textStatus);
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
            }
        });
    }

    /**
     *已转让的借款
     * @date 2016-06-22
     * @author huqinfu
     */
    function yzrdjk() {
        $("#yzrdjqDivId").show();
        $("#noRecordDivId").remove(); 
        
        $("#yhqdjqDivId").hide();
        $("#yhqdjkLiId").removeClass();
        
        $("#hkzdLiId").removeClass();
        $("#yzrdjkLiId").attr("class", "hover");
        //先清除原有的数据
        for (var i = 0; i < pageSize; i++) {
            var divId = "#divId" + i;
            $(divId).remove();
        }
        $("#pageContent").html("");
        $("#yzrdjqTableId tbody tr").empty();

        var ajaxUrl = "/user/credit/alreadyTransferLoan.htm";
		/* var htUrl = "/user/financing/agreement/blzqzrAgreement.html"; */
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
            	if(returnData.loginTimeOut){
            		window.location.href="<%=controller.getViewURI(request, Login.class)%>";
            		return;
            	}
            	
                if (null == returnData || null == returnData.yzrList || returnData.yzrList.length == 0) {
                    $("#yzrdjqTableId tbody").html("");
                    $("#yzrdjqTableId tbody").append("<tr class='nodatatr'><td colspan='8' align='center'>暂无数据</td></tr>");
                    $("#pageContent").html("");
                    return;
                }

                //分页信息
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 3);
                });

                if (returnData.yzrList.length > 0) {
                    $("#yzrdjqTableId tbody").empty();
                    var rAssests = returnData.yzrList;
                    $.each(rAssests, function (n, value) {
                        var trHtmlStr = "";
                        var hrefUrl = "<%=configureProvider.format(URLVariable.FINANCING_SBTZ_XQ)%>" + value.F01 + "<%=rewriter.getViewSuffix() %>";
                        trHtmlStr += "<tr><td align='center'>" + (n+1) + "</td><td align='center'><a class='highlight' href='" + hrefUrl + "' title='"+value.F03+"'>" + subStringLength(value.F03,10,"...") + "</a></td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.F26, 2) + "</td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.F06 * 100, 2) + "%</td>";
                        /* var timeStr = "";
                        if (value.F28.F21 == "S") {//T6231_F21.F
                            timeStr = value.F28.F22 + "天";
                        } else {
                        	 timeStr = value.F09 + "个月";
                        } */
                        trHtmlStr += "<td align='center'>" + value.F28.F03 + "/" + value.F28.F02 + "</td>";
                        trHtmlStr += "<td align='center'>" + fmoney(value.zrTotle, 2) + "</td>";
                        trHtmlStr += "<td align='center'>" + getTime(value.zrTime) + "</td></tr>";
                        /* trHtmlStr += "<td align='center'><a class='blue' target='_blank' href='"+htUrl+"?id="+value.zrId+"'>合同</a></td></tr>"; */
                        $("#yzrdjqTableId").append(trHtmlStr);
                    });
                }
            },
            error: function (XMLHttpRequest, textStatus) {
                /* var trHTML = "<tr><td colspan='8' align='center'>数据错误</td></tr>";
                $("#yzrdjqTableId tbody").append(trHTML);
                alert(textStatus); */
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
            }
        });
    }
    
    $(function () {
        /*tips提示*/
        $(".hover_tips").mouseover(function () {
            $(this).children().show();
        });
        $(".hover_tips").mouseout(function () {
            $(this).children().hide();
        });
        setOutClick();
    });
    
    function setOutClick(){
    	$("a.out").click(function () {
            $("div.dialog").hide();
            $("div.popup_bg").hide();
        });
    }
    
    function toAjaxPage(){
    	if ($("#hkzdLiId").hasClass("hover")) {
    		//先清除原有的数据
    		for(var i=0;i<pageSize;i++){
    			var divId = "#divId"+i;
    			$(divId).remove();
    		}
    		repay();
    	}else if ($("#yhqdjkLiId").hasClass("hover")) {
    		//先清除原有的数据
    		for(var i=0;i<pageSize;i++){
    			var divId = "#divId"+i;
    			$(divId).remove();
    		}
    		yhqdjk();
    	}else if ($("#yzrdjkLiId").hasClass("hover")) {
    		//先清除原有的数据
    		for(var i=0;i<pageSize;i++){
    			var divId = "#divId"+i;
    			$(divId).remove();
    		}
    		yzrdjk();
    	}
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/repaying.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/zankai.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/ajaxDateUtil.js"></script>
</body>
</html>