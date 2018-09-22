<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6130_F09" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.txsb.Txsb" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Bank" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    UserWithdrawals txglRecord = ObjectHelper.convert(request.getAttribute("txglRecord"), UserWithdrawals.class);
    Bank[] banks = ObjectHelper.convertArray(request.getAttribute("banks"), Bank.class);
    if (txglRecord == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "TXGL";
    
 	// 增加对托管的区分 hsp——20160323
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean escrowBoolean = true;
    if("FUYOU".equals(escrow)){
        escrowBoolean = false;
    }
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form>
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>审核原因
                            </div>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <ul class="gray6">
                                <li class="mb10">
										<span class="display-ib w200 tr mr5">
											<span class="red">&nbsp;</span>
										</span>

                                    <div class="pl200 ml5 orange"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">流水号：</span>
                                    <%=txglRecord.F01%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">用户名：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.userName);%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">开户名：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.realName);%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">提现银行：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.extractionBank);%>
                                </li>
                                <%if(!StringHelper.isEmpty(txglRecord.location)){ %>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">开户地：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.location);%>
                                </li>
                                <%} %>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">所在支行：</span>
                                    <%
                                        StringHelper.filterHTML(out, txglRecord.subbranch);
                                    %>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">银行卡账号：</span>
                                    <%
                                        StringHelper.filterHTML(out, StringHelper.decode(txglRecord.bankId));
                                    %>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">提现金额：</span>
                                    <%=Formater.formatAmount(txglRecord.F04)%>元
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">手续费：</span>
                                    <%=Formater.formatAmount(txglRecord.F07)%>元
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">申请时间：</span>
                                    <%=DateTimeParser.format(txglRecord.F08, "yyyy-MM-dd HH:mm")%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">审核时间：</span>
                                    <%=DateTimeParser.format(txglRecord.F11, "yyyy-MM-dd HH:mm")%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">审核人：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.shName);%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">审核意见：</span>
                                    <%
                                        StringHelper.filterHTML(out, txglRecord.F12);
                                    %>
                                </li>
                                <%if (!StringHelper.isEmpty(txglRecord.txName)) {%>
                                <li class="mb10">
                                    <%if (escrowBoolean) { %>
                                    <span class="display-ib w200 tr mr5">放款时间：</span>
                                    <%} else { %>
                                     <span class="display-ib w200 tr mr5">对账时间：</span>
                                    <%} %>
                                    <%=DateTimeParser.format(txglRecord.F14, "yyyy-MM-dd HH:mm")%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">放款人：</span>
                                    <%
                                        StringHelper.filterHTML(out, txglRecord.txName);
                                    %>
                                </li>
                                <li class="mb10">
                                    <%if (escrowBoolean) { %>
                                    <span class="display-ib w200 tr mr5">放款意见：</span>
                                    <%} else { %>
                                     <span class="display-ib w200 tr mr5">原因：</span>
                                    <%} %>
                                    <%
                                        StringHelper.filterHTML(out, txglRecord.F15);
                                    %>
                                </li>
                                <%}%>
                                <li class="mb10">
                                    <div class="pl200 ml5">
                                        <%if (txglRecord.F09 == T6130_F09.TXSB) { %>
                                        <a href="<%=controller.getURI(request, Txsb.class)%>"
                                           class="btn btn-blue2 radius-6 pl20 pr20 ml40">关闭</a>
                                        <%} %>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </form>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/txgl/withdrawals.js"></script>
</body>
</html>