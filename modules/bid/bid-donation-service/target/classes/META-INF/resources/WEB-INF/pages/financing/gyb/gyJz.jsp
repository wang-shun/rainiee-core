<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet" %>
<%@page import="java.sql.Timestamp" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<%@page import="com.dimeng.p2p.S50.entities.T5016" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage" %>
<%@ page import="com.dimeng.p2p.repeater.donation.GyLoanManage" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoanStatis" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<%@ page import="com.dimeng.p2p.repeater.donation.query.GyLoanQuery" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>公益捐赠-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
    T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.GYJZ.name());
    GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
    //公益标列表（统计数据）
    GyLoanStatis gys = gyLoanManage.gyLoanStatistics(0);
    PagingResult<GyLoan> gyList = gyLoanManage.search4front(new GyLoanQuery() {
        @Override
        public T6242_F11 getStatus() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getLoanTitle() {
            return null;
        }

        @Override
        public String getGyName() {
            return null;
        }

        @Override
        public Timestamp getCreateTimeStart() {
            return null;
        }

        @Override
        public Timestamp getCreateTimeEnd() {
            return null;
        }

        @Override
        public String getBidNo() {
            return null;
        }
    }, new Paging() {
        public int getCurrentPage() {
            return IntegerParser.parse(requestWrapper.getParameter("paging.current"));
        }

        public int getSize() {
            return 10;
        }

    });
    //查询公益标列表
    GyLoan[] loanList = gyList.getItems();
    boolean hmdFlg = false;
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
        if (T6110_F07.HMD == t6110.F07) {
            hmdFlg = true;
        }
    }
%>

<!--B 主题内容-->
<div class="main_bg" <%if (advertisements != null){%>style="padding-top:0px;"<%}%>>
    <%if (advertisements != null) {%>
    <div class="charity_banner">
        <ul class="rslides">
            <%
                for (T5016 advertisement : advertisements) {
                    if (advertisement == null) {
                        continue;
                    }
            %>
            <li style="background:url(<%=fileStore.getURL(advertisement.F05)%>) center 0 no-repeat;">
                <%if (!StringHelper.isEmpty(advertisement.F04)) {%>
                <a target="_blank"
                   href="http://<%StringHelper.filterHTML(out, advertisement.F04.replaceAll("http://", ""));%>"
                   title="<%=StringHelper.isEmpty(advertisement.F03)?"":advertisement.F03%>"></a>
                <%}%>
            </li>
            <%}%>
        </ul>
    </div>
    <%} %>

    <!--投资列表-->
    <div class="item_list charity_list w1002">
        <div class="main_hd"><i class="icon"></i><span class="gray3 f18">公益捐赠列表</span></div>
        <div class="total">
            <ul>
                <li>累计捐赠总金额<br/>
                    <%if (gys.donationsAmount.doubleValue() >= 10000) { %>
                    <span class="f36 gray3"><%=Formater.formatAmount(gys.donationsAmount.doubleValue() / 10000)%></span>万元
                    <%} else if (gys.donationsAmount.doubleValue() >= 100000000) { %>
                    <span class="f36 gray3"><%=Formater.formatAmount(gys.donationsAmount.doubleValue() / 100000000)%></span>亿元
                    <%} else { %>
                    <span class="f36 gray3"><%=gys.donationsAmount%></span>元
                    <%} %>
                </li>
                <li class="line"></li>
                <li>累计捐助总笔数<br/><span class="f36 gray3"><%=gys.donationsNum%></span>笔</li>
            </ul>
        </div>
        <!--投资项目列表-->
        <div class="list">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="til">
                    <td width="26%">公益项目</td>
                    <td width="20%">公益机构</td>
                    <td width="20%">金额</td>
                    <td width="20%">进度</td>
                    <td width="14%">操作</td>
                </tr>
                <%
                    if (loanList != null) {
                        for (GyLoan lo : loanList) {

                %>
                <tr class="all_bj all_bjline" style="border:0;">
                    <td>
                        <div class="title">
                            <span class="item_icon charity_icon"></span>
                            <a title="<%StringHelper.filterHTML(out, lo.t6242.F03);%>"
                               href="<%configureProvider.format(out,URLVariable.GYB_BDXQ);%><%=lo.t6242.F01%>.html"><%
                                StringHelper.filterHTML(out, StringHelper.truncation(lo.t6242.F03, 10));%></a>
                        </div>
                    </td>
                    <td>
                        <div class="institution">
                            <%StringHelper.filterHTML(out, StringHelper.truncation(lo.t6242.F22, 14));%>
                        </div>
                    </td>
                    <td>
                        <%if (lo.t6242.F05.doubleValue() >= 10000) { %>
                        <span class="f18 gray3"><%=Formater.formatAmount(lo.t6242.F05.doubleValue() / 10000) %></span>万元
                        <%} else if (lo.t6242.F05.doubleValue() >= 100000000) { %>
                        <span class="f18 gray3"><%=Formater.formatAmount(lo.t6242.F05.doubleValue() / 10000) %></span>亿元
                        <%} else { %>
                        <span class="f18 gray3"><%=Formater.formatAmount(lo.t6242.F05) %></span>元
                        <%} %>
                    </td>
                    <td>
                        <div class="progress"><span class="progress_bg"><span class="progress_bar"
                                                                              style="width:<%=lo.isTimeEnd ? 100 : (int)(lo.perCent*100)%>%;"></span></span>
                            <span class="cent"><%=lo.isTimeEnd ? "100%" : lo.perCentFormat%></span>
                        </div>
                    </td>
                    <%
                        if (T6242_F11.JKZ.name().equalsIgnoreCase(lo.t6242.F11.name()) && !lo.isTimeEnd) {
                            if (hmdFlg) {
                    %>
                    <td><span class="btn06 btn_gray btn_disabled">我要捐款</span></td>
                    <%} else {%>
                    <td><a class="btn06"
                           href="<%configureProvider.format(out,URLVariable.GYB_BDXQ);%><%=lo.t6242.F01%>.html">我要捐款</a>
                    </td>
                    <%}%>
                    <%} else if (T6242_F11.YJZ.name().equalsIgnoreCase(lo.t6242.F11.name()) || lo.isTimeEnd) {%>
                    <td><span class="btn06 btn_gray btn_disabled">捐助完成</span></td>
                    <%} else {%>
                    <td><span class="btn06 btn_gray btn_disabled"><%=lo.t6242.F11.getChineseName()%></span></td>
                    <%}%>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>
        <!--投资项目列表-->

        <!--分页操作 -->
        <%AbstractFinancingServlet.rendPaging(out, gyList, controller.getPagingURI(request, com.dimeng.p2p.front.servlets.financing.gyb.GyJz.class), null);%>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<!--E 主题内容-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery.SuperSlide.2.1.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/common.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/front.js"></script>
</body>
</html>
