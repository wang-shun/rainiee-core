<%@page import="com.dimeng.p2p.common.enums.PaymentStatus" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.WdjkManage" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.RepayInfo" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.RepayLoanDetail" %>
<%@page import="com.dimeng.p2p.user.servlets.AbstractUserServlet" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.Charge" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.LoanDetail" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.PaymentOne" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.Repaying" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%
        configureProvider.format(out, SystemVariable.SITE_NAME);
    %>_借款管理_我的借款_还款详情
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    WdjkManage creditManage = serviceSession.getService(WdjkManage.class);
    int id = IntegerParser.parse(request.getParameter("id"));
    final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
    Paging paging = new Paging() {
        public int getCurrentPage() {
            return currentPage;
        }

        public int getSize() {
            return 10;
        }
    };
    PagingResult<RepayLoanDetail> result = creditManage.getRepayLoanDetail(id, paging);
    RepayLoanDetail[] details = result.getItems();
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "WDJK";
%>
<div class="clear"></div>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <form action="<%=controller.getViewURI(request, LoanDetail.class)%>?id=<%=id%>" method="post">
            <div class="r_main">
                <div class="user_mod">
                    <div class="user_til border_b pb10 clearfix"><a
                            href="<%=controller.getViewURI(request, Repaying.class)%>" class="fr highlight">返回列表</a><i
                            class="icon"></i><span class="gray3 f18">还款详情</span></div>
                    <div class="user_table">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">期数</td>
                                <td align="center">金额(元)</td>
                                <td align="center">还款日期</td>
                                <td align="center">状态</td>
                                <td align="center">&nbsp;</td>
                            </tr>
                            <%
                                if (details != null && details.length > 0) {
                                    int i = 1;
                                    for (RepayLoanDetail detail : details) {
                                        if (detail == null) {
                                            continue;
                                        }
                            %>
                            <tr>
                                <td align="center">第<%=detail.F06%>期</td>
                                <td align="center"><i><%=Formater.formatAmount(detail.F07)%>
                                </i></td>
                                <td align="center"><%=DateParser.format(detail.F08)%>
                                </td>
                                <td align="center"><%=detail.paymentStatus == null ? "" : detail.paymentStatus.getName()%>
                                </td>
                                <td align="center"><%
                                    if (detail.paymentStatus == PaymentStatus.WH || PaymentStatus.YQ == detail.paymentStatus || PaymentStatus.YZYQ == detail.paymentStatus) {
                                %>
                                    <a href="javascript:void(0)" class="highlight"
                                       onclick="showPayment('<%=detail.F06%>')">还款</a> <%
                                        }
                                    %></td>
                            </tr>
                            <%
                                    i++;
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="8" align="center">没有记录</td>
                            </tr>
                            <%
                                }
                            %>
                        </table>
                        <%
                            AbstractUserServlet.rendPagingResult(out, result);
                        %>
                    </div>
                </div>

            </div>
        </form>
        <div class="clear"></div>
    </div>
</div>
<%
    if (details != null && details.length > 0) {
        for (RepayLoanDetail detail : details) {
            if (detail == null) {
                continue;
            }
            RepayInfo repayInfo = detail.repayInfo;
            if (repayInfo != null) {
%>
<div style="display: none" id="r<%=detail.F06%>">
    <div class="popup_bg"></div>
<div class="dialog refund_dialog" >
    <div class="title"><a href="javascript:void(0);" onclick="closeDiv(<%=detail.F06%>)" class="out"></a>还款</div>
    <div class="content">
        <div class="border_b f16 pb15 mb15">
            <span class="gray3">当期还款总需(元):</span>
            <span class="red"><%=Formater.formatAmount(repayInfo.loanTotalMoney)%></span>
            <span class="gray3 ml20">可用金额(元)：</span>
            <span class="red"><%=Formater.formatAmount(repayInfo.accountAmount)%></span>
        </div>
        <form action="<%=configureProvider.format(URLVariable.PAY_PAYMENTONE_URL)%>" method="post">
            <input type="hidden" name="id" value="<%=detail.F02%>"/>
            <input type="hidden" name="number" value="<%=detail.F06%>"/>

            <div class="border_b pb15 mb15 lh24">
                <table width="100%" border="0" cellspacing="0">
                    <tr>
                        <td>当期应还本息(元)</td>
                        <td>逾期金额(元)</td>
                    </tr>
                    <tr>
                        <td><i class="red"><%=Formater.formatAmount(repayInfo.loanMustMoney)%>
                        </i></td>
                        <td><i class="red"><%=Formater.formatAmount(repayInfo.loanArrMoney)%>
                        </i></td>
                    </tr>
                </table>
            </div>
            <div class="clear"></div>
            <div id="confirmBtn_<%=repayInfo.loanID %>" class="tc mt20">
                <input type="submit" class="btn01" value="确认扣费" onclick="confirmRepay(<%=repayInfo.loanID %>)"/>
                <input type="button" onclick="closeDiv(<%=detail.F06%>)" class="btn01 btn_gray ml20" value="取 消"/>
            </div>
            <div id="confirmLoading_<%=repayInfo.loanID %>" style="color:red; display: none; text-align:center;">
                正在处理，请稍候...
            </div>
        </form>
    </div>
</div>
</div>
<%
            }
        }
    }
%>
<%
    String errorMessage = controller.getPrompt(request, response,
            PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<div class="popup_bg divtip"></div>
<div class="dialog" id="warringDiv">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="error"></div>
            <div class="tips">
                <span class="f20 gray33"><%StringHelper.filterHTML(out, errorMessage);%></span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="javascript:void(-1);" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%}%>
<%
    String warringMessage = controller.getPrompt(request, response,
            PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="popup_bg divtip"></div>
<div class="dialog" id="warringDiv">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
			<span class="f20 gray33">
			<%if ("余额不足".equals(warringMessage)) {%>您的账户不足以本次还款，请<a
                    href='<%=configureProvider.format(URLVariable.USER_CHARGE)%>'>充值</a>
			<%} else {%><%StringHelper.filterHTML(out, warringMessage);%><%} %>
			</span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="javascript:void(-1);" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%}%>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<div class="popup_bg divtip"></div>
<div class="dialog" id="infoDiv">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="successful"></div>
            <div class="tips">
			<span class="f20 gray33">
			<%StringHelper.filterHTML(out, infoMessage);%>
			</span>
            </div>
        </div>
        <div class="clear"></div>
        <div class="tc mt20">
            <a href="javascript:void(-1);" onclick="closeDialogMessageDiv()" class="btn01 close">确认</a>
        </div>
    </div>
</div>
<%
    }
%>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/repaying.js"></script>
<script type="text/javascript">
	function closeDialogMessageDiv(){
	    location.reload();
	}
    function showPayment(id) {
        $("#r" + id).show();
    }
    function closeDiv(id) {
        $("#r" + id).hide();
    }
    function confirmRepay(id) {
        $("#confirmBtn_" + id).hide();
        $("#confirmLoading_" + id).show();
    }
    $(".close").click(function () {
        $("#paymentDiv").hide();
        $("#errorDiv").hide();
        $("#warringDiv").hide();
        $("#infoDiv").hide();
        $(".divtip").hide();
    });
</script>
</body>
</html>