<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Tbjl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>散标详情-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <%@include file="/WEB-INF/include/sbtz/header.jsp" %>

    <div class="plan_tab clearfix">
        <ul>
            <li>
                <a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Bdxq.class, id)%>">标的详情</a>
            </li>
            <%if (creditInfo.F11 == T6230_F11.S) { %>
            <li>
                <a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Dbxx.class, id)%>">风控信息</a>
            </li>
            <%} %>
            <%
                if (creditInfo.F13 == T6230_F13.S) {
            %>
            <li>
                <a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Dyxx.class, id)%>">抵押物信息</a>
            </li>
            <%
                }
            %>
            <%if (isXgwj) {%>
            <li>
                <a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Xgwj.class, id)%>">相关文件</a>
            </li>
            <%} %>
            <li>
                <a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Hkjl.class, id)%>">还款计划</a>
            </li>
            <li class="hover">投资记录</li>
            <li style="border-left: 1px solid #d1dfea; padding: 0;"></li>
        </ul>
    </div>
    <%
        {
            final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
            int currentPage = IntegerParser.parse(requestWrapper.getParameter("paging.current"));
            T6250[] tenderRecords = null;
            PagingResult<T6250> result = null;
            final int pageSize = 15;
            if (currentPage <= 0) {
                tenderRecords = investManage.getRecord(id);
            } else {
                result = investManage.getRecords(id, new Paging() {
                    public int getCurrentPage() {
                        return IntegerParser.parse(requestWrapper.getParameter("paging.current"));
                    }

                    public int getSize() {
                        return pageSize;
                    }
                });
                tenderRecords = result.getItems();
            }
            T6250[] tenderRecordss = investManage.getRecord(id);

    %>
    <div class="contain_main clearfix">
        <div class="plan_tab_con01 clearfix">
            <div class="fr">
                <%
                    BigDecimal totleMoney = new BigDecimal(0);
                    if (tenderRecordss != null && tenderRecordss.length > 0) {
                        for (T6250 tenderRecord : tenderRecordss) {
                            if (tenderRecord == null) {
                                continue;
                            }
                            ;
                            totleMoney = totleMoney.add(tenderRecord.F04);
                        }
                    }
                %>
					<span class="mr10">加入人次<em
                            class="f18 orange"><%=tenderRecordss == null ? 0 : tenderRecordss.length %>
                    </em>人
					</span> <span class="mr10">投资总额<em class="f18 orange"><%=Formater.formatAmount(totleMoney) %>
            </em>元
					</span>
            </div>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tc">
                <tr class="leve_titbj">
                    <td width="330">序号</td>
                    <td width="156">投资人</td>
                    <td width="362">投资金额</td>
                    <td width="362">投资时间</td>
                </tr>
                <%
                    int i = 1;
                    if (tenderRecords != null) {
                        for (T6250 tenderRecord : tenderRecords) {
                            if (tenderRecord == null) {
                                continue;
                            }
                            String userTbr = userInfoManage.getUserName(tenderRecord.F03);
                %>
                <tr>
                    <td width="330"><%=i++ %>
                    </td>
                    <td width="156">
                        <%
                            StringHelper.filterHTML(out, userTbr.substring(0, 2) + "******" + userTbr.substring(userTbr.length() - 2, userTbr.length()));%>
                    </td>
                    <td width="362"><%=tenderRecord.F04 %>元</td>
                    <td width="362"><%=DateTimeParser.format(tenderRecord.F06, "yyyy-MM-dd HH:mm") %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4"></td>
                </tr>
                <%} %>
            </table>
            <div>
                <%
                    if (currentPage <= 0) {
                        if (i > pageSize) {
                %>
                <div class='page'><a
                        href='<%=controller.getPagingURI(request, Tbjl.class) + id + ".html?paging.current=1" %>'
                        class='page-link rightBtn'>返回分页</a></div>
                <%
                    }
                } else {
                    if (result.getPageCount() > 1) {
                %>
                <%Tbjl.rendPaging(out, result, controller.getPagingURI(request, Tbjl.class), id);%>
                <%
                        }
                    }
                %>
            </div>
        </div>
    </div>
    <%}%>

</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/sbtz.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "succeed", $("#sbSucc").val()));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "error"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "perfect"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>