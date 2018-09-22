<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S51.entities.T5124" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F14" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.common.enums.CreditTerm" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Bdxq" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdlb" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.query.BidQuery_Order" %>
<%@page import="com.dimeng.util.http.URLParameter" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>个人标-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%!
    static final String TYPE_KEY = "p";
    static final String RATE_KEY = "r";
    static final String TERM_KEY = "m";
    static final String LEVEL_KEY = "l";
    static final String MULTISELECT_KEY = "s";
    static final String ORDER_KEY = "o";
    static final String ETP_KEY = "e";
%>
<%
    BidManage bidManage = serviceSession.getService(BidManage.class);
    final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    Tztjxx total = bidManage.getStatisticsGr();
    T6211[] t6211s = bidManage.getBidType();
    T5124[] t5124s = bidManage.getLevel();
    T6216[] t6216s = bidManage.getProducts();
    boolean multiSelect = "1".equals(request.getParameter(MULTISELECT_KEY));
    URLParameter parameter = new URLParameter(request,
            controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Grlb.class),
            multiSelect,
            MULTISELECT_KEY,
            TYPE_KEY,
            RATE_KEY,
            TERM_KEY,
            LEVEL_KEY,
            ORDER_KEY);
    String[] values = requestWrapper.getParameterValues(TYPE_KEY);
    PagingResult<Bdlb> result = bidManage.search(
            new BidQuery_Order() {
                public T6211[] getType() {
                    String[] values = requestWrapper.getParameterValues(TYPE_KEY);
                    if (values == null || values.length == 0) {
                        return null;
                    }
                    T6211[] types = new T6211[values.length];
                    for (int i = 0; i < values.length; i++) {
                        types[i] = new T6211();
                        types[i].F01 = IntegerParser.parse(values[i]);
                    }
                    return types;
                }

                public int getRate() {
                    int rate = IntegerParser.parse(requestWrapper.getParameter(RATE_KEY));
                    return rate;
                }

                public CreditTerm[] getTerm() {
                    String[] values = requestWrapper.getParameterValues(TERM_KEY);
                    if (values == null || values.length == 0) {
                        return null;
                    }
                    CreditTerm[] terms = new CreditTerm[values.length];
                    for (int i = 0; i < values.length; i++) {
                        terms[i] = EnumParser.parse(CreditTerm.class, values[i]);
                    }
                    return terms;
                }

                public T6230_F20[] getStatus() {
                    String[] values = requestWrapper.getParameterValues(LEVEL_KEY);

                    if (values == null || values.length == 0) {
                        return null;
                    }
                    T6230_F20[] levels = new T6230_F20[values.length];
                    for (int i = 0; i < values.length; i++) {
                        levels[i] = EnumParser.parse(T6230_F20.class, values[i]);
                    }
                    return levels;
                }

                public int getOrder() {
                    return IntegerParser.parse(requestWrapper.getParameter(ORDER_KEY));
                }
                public  int getProductId(){
                    return 0;
                }
            }, new Paging() {
                public int getCurrentPage() {
                    return IntegerParser.parse(requestWrapper.getParameter("paging.current"));
                }

                public int getSize() {
                    return 10;
                }
            });

%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="w1002 clearfix">
        <div>
            <div class="item_list">
                <div class="plan_tab clearfix">
                    <ul>
                        <li class="normalMenu"><a
                                href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class)%>">企业标</a>
                        </li>
                        <li class="hover">个人标</li>
                        <%-- <li><a href="<%=controller.getViewURI(request, Xxzqlb.class)%>">线下债权转让</a></li> --%>
                        <li class="normalMenu"><a
                                href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Zqzrlb.class)%>">债权转让</a>
                        </li>
                        <li style="border-left:1px solid #ddd;padding:0;"></li>
                    </ul>
                </div>
                <div class="bd jtab-con">
                    <div class="screening">
                        <div class="til">筛选投资项目</div>
                        <div class="con">
                            <dl class="filtrate">
                                <dt>标的类型</dt>
                                <dd>
                                    <%
                                        if (parameter.contains(TYPE_KEY)) {
                                            parameter.remove(TYPE_KEY);
                                    %>
                                    <a href="<%=parameter.toString()%>">不限</a>
                                    <%
                                    } else {
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">不限</a>
                                    <%}%>
                                    <%
                                        if (t6211s != null && t6211s.length > 0) {
                                            for (T6211 type : t6211s) {
                                                if (parameter.contains(TYPE_KEY, Integer.toString(type.F01))) {
                                                    parameter.remove(TYPE_KEY, type.F01 + "");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>"><%=type.F02%>
                                    </a>
                                    <%
                                    } else {
                                        parameter.set(TYPE_KEY, Integer.toString(type.F01));
                                    %>
                                    <a href="<%=parameter.toString()%>"><%=type.F02%>
                                    </a>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </dd>
                            </dl>
                            <dl class="filtrate">
                                <dt>年化利率</dt>
                                <dd>
                                    <%
                                        if (parameter.contains(RATE_KEY)) {
                                            parameter.remove(RATE_KEY);
                                    %>
                                    <a href="<%=parameter.toString()%>">不限</a>
                                    <%
                                    } else {
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">不限</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(RATE_KEY, "1")) {
                                            parameter.remove(RATE_KEY, "1");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">10%以下</a>
                                    <%
                                    } else {
                                        parameter.set(RATE_KEY, "1");
                                    %>
                                    <a href="<%=parameter.toString()%>">10%以下</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(RATE_KEY, "2")) {
                                            parameter.remove(RATE_KEY, "2");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">10%-15%</a>
                                    <%
                                    } else {
                                        parameter.set(RATE_KEY, "2");
                                    %>
                                    <a href="<%=parameter.toString()%>">10%-15%</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(RATE_KEY, "3")) {
                                            parameter.remove(RATE_KEY, "3");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">15%-20%</a>
                                    <%
                                    } else {
                                        parameter.set(RATE_KEY, "3");
                                    %>
                                    <a href="<%=parameter.toString()%>">15%-20%</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(RATE_KEY, "4")) {
                                            parameter.remove(RATE_KEY, "4");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">20%以上</a>
                                    <%
                                    } else {
                                        parameter.set(RATE_KEY, "4");
                                    %>
                                    <a href="<%=parameter.toString()%>">20%以上</a>
                                    <%}%>
                                </dd>
                            </dl>
                            <dl class="filtrate">
                                <dt>借款期限</dt>
                                <dd>
                                    <%
                                        if (parameter.contains(TERM_KEY)) {
                                            parameter.remove(TERM_KEY);
                                    %>
                                    <a href="<%=parameter.toString()%>">不限</a>
                                    <%
                                    } else {
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">不限</a>
                                    <%}%>
                                    <%
                                        for (CreditTerm term : CreditTerm.values()) {
                                            if (parameter.contains(TERM_KEY, term.name())) {
                                                parameter.remove(TERM_KEY, term.name());
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>"><%=term.getName()%>
                                    </a>
                                    <%
                                    } else {
                                        parameter.set(TERM_KEY, term.name());
                                    %>
                                    <a href="<%=parameter.toString()%>"><%=term.getName()%>
                                    </a>
                                    <%
                                            }
                                        }
                                    %>
                                </dd>
                            </dl>

                            <dl class="filtrate">
                                <dt>项目状态</dt>
                                <dd>
                                    <%
                                        if (parameter.contains(LEVEL_KEY)) {
                                            parameter.remove(LEVEL_KEY);
                                    %>
                                    <a href="<%=parameter.toString()%>">不限</a>
                                    <%
                                    } else {
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">不限</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(LEVEL_KEY, "TBZ")) {
                                            parameter.remove(LEVEL_KEY, "TBZ");
                                    %>
                                    <a href="<%=parameter.toString()%>" class="cur">可投资</a>
                                    <%
                                    } else {
                                        parameter.set(LEVEL_KEY, "TBZ");
                                    %>
                                    <a href="<%=parameter.toString()%>">可投资</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(LEVEL_KEY, "HKZ")) {
                                            parameter.remove(LEVEL_KEY, "HKZ");
                                    %>
                                    <a href="<%=parameter.toString()%>" class="cur">还款中</a>
                                    <%
                                    } else {
                                        parameter.set(LEVEL_KEY, "HKZ");
                                    %>
                                    <a href="<%=parameter.toString()%>">还款中</a>
                                    <%}%>
                                    <%
                                        if (parameter.contains(LEVEL_KEY, "YJQ")) {
                                            parameter.remove(LEVEL_KEY, "YJQ");
                                    %>
                                    <a href="<%=parameter.toString()%>" class="cur">已还款</a>
                                    <%
                                    } else {
                                        parameter.set(LEVEL_KEY, "YJQ");
                                    %>
                                    <a href="<%=parameter.toString()%>">已还款</a>
                                    <%}%>
                                </dd>
                            </dl>
                            <dl class="filtrate">
                                <dt>产品名称</dt>
                                <dd>
                                    <%
                                        if (parameter.contains(ETP_KEY)) {
                                            parameter.remove(ETP_KEY);
                                    %>
                                    <a href="<%=parameter.toString()%>">不限</a>
                                    <%
                                    } else {
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>">不限</a>
                                    <%}%>
                                    <%
                                        if (t6216s != null && t6216s.length > 0) {
                                            for (T6216 type : t6216s) {
                                                if (parameter.contains(ETP_KEY, Integer.toString(type.F01))) {
                                                    parameter.remove(ETP_KEY, type.F01 + "");
                                    %>
                                    <a class="cur" href="<%=parameter.toString()%>"><%=type.F02%>
                                    </a>
                                    <%
                                    } else {
                                        parameter.set(ETP_KEY, Integer.toString(type.F01));
                                    %>
                                    <a href="<%=parameter.toString()%>"><%=type.F02%>
                                    </a>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    <div class=" w1002">
                        <div class="clearfix">
                            <div class="tzlc_bg">
                                <div class="tzlb_title fl">投资列表<a target="_blank"
                                                                  href="<%=controller.getURI(request, com.dimeng.p2p.front.servlets.credit.Lcjsq.class)%>"
                                                                  class="multi">理财计算器</a></div>
                                <ul class="total_money fr tc pb20">
                                    <li><span class="mt10">累计成交总金额<br/>
                  <%-- <%if(total.totleMoney.doubleValue()>=100000000){%>
		        	   <em class="f24"><%=Formater.formatAmount(total.totleMoney.doubleValue()/100000000)%></em>亿元
		        	<%}else if(total.totleMoney.doubleValue()>=10000 && total.totleMoney.doubleValue() <100000000){%>
		        		<em class="f24"><%=Formater.formatAmount(total.totleMoney.doubleValue()/10000)%></em>万元
		        	<%}else{%>
		        		<em class="f24"><%=Formater.formatAmount(total.totleMoney)%></em>元
		        	<%}%>--%>
					    <em class="f24" id="emObj"></em><em id="unit"></em>
                   </span><span class="line ml30"></span></li>
                                    <li><span class="mt10">累计成交总笔数<br/><em class="f24"><%=total.totleCount%>
                                    </em>笔</span><span class="line ml30"></span></li>
                                    <li><span class="mt10">为用户累计赚取<br/>
                <%--   <%
		           if(total.userEarnMoney.doubleValue()>=100000000){%>
		        	   <em class="f24"><%=Formater.formatAmount(total.userEarnMoney.doubleValue()/100000000)%></em>亿元
		        	<%}else if(total.userEarnMoney.doubleValue()>=10000 && total.userEarnMoney.doubleValue() <100000000){%>
		        		<em class="f24"><%=Formater.formatAmount(total.userEarnMoney.doubleValue()/10000)%></em>万元
		        	<%}else{%>
		        		<em class="f24"><%=Formater.formatAmount(total.userEarnMoney)%></em>元
		        	<%}%>--%>
					   <em class="f24" id="emObj2"></em><em id="unit2"></em>
                   </span></li>
                                </ul>
                                <div class="clear"></div>
                                <div class="invest_con">
                                    <div class="invest_con">
                                        <div class="info">
                                            <table width="100%" cellspacing="0">
                                                <tr class="big_tit">
                                                    <td width="212">借款标题</td>
                                                    <%
                                                        if (parameter.contains(ORDER_KEY, "42")) {
                                                            parameter.set(ORDER_KEY, "41");
                                                    %>
                                                    <td width="111"><a href="<%=parameter.toString()%>" class="arrow">贷款金额</a>
                                                    </td>
                                                    <%
                                                    } else {
                                                        parameter.set(ORDER_KEY, "42");
                                                    %>
                                                    <td width="111"><a href="<%=parameter.toString()%>"
                                                                       class="arrow up">贷款金额</a></td>
                                                    <%}%>

                                                    <%
                                                        if (parameter.contains(ORDER_KEY, "12")) {
                                                            parameter.set(ORDER_KEY, "11");
                                                    %>
                                                    <td width="79"><a href="<%=parameter.toString()%>"
                                                                      class="arrow">年化利率</a></td>
                                                    <%
                                                    } else {
                                                        parameter.set(ORDER_KEY, "12");
                                                    %>
                                                    <td width="79"><a href="<%=parameter.toString()%>" class="arrow up">年化利率</a>
                                                    </td>
                                                    <%}%>

                                                    <%
                                                        if (parameter.contains(ORDER_KEY, "52")) {
                                                            parameter.set(ORDER_KEY, "51");
                                                    %>
                                                    <td width="81"><a href="<%=parameter.toString()%>" class="arrow">贷款期限</a>
                                                    </td>
                                                    <%
                                                    } else {
                                                        parameter.set(ORDER_KEY, "52");
                                                    %>
                                                    <td width="81"><a href="<%=parameter.toString()%>" class="arrow up">贷款期限</a>
                                                    </td>
                                                    <%}%>

                                                    <%
                                                        if (parameter.contains(ORDER_KEY, "62")) {
                                                            parameter.set(ORDER_KEY, "61");
                                                    %>
                                                    <td width="89"><a href="<%=parameter.toString()%>" class="arrow">还需金额</a>
                                                    </td>
                                                    <%
                                                    } else {
                                                        parameter.set(ORDER_KEY, "62");
                                                    %>
                                                    <td width="89"><a href="<%=parameter.toString()%>" class="arrow up">还需金额</a>
                                                    </td>
                                                    <%}%>

                                                    <%
                                                        if (parameter.contains(ORDER_KEY, "72")) {
                                                            parameter.set(ORDER_KEY, "71");
                                                    %>
                                                    <td width="131" class="w108"><a href="<%=parameter.toString()%>"
                                                                                    class="arrow">进度</a></td>
                                                    <%
                                                    } else {
                                                        parameter.set(ORDER_KEY, "72");
                                                    %>
                                                    <td width="131" class="w108"><a href="<%=parameter.toString()%>"
                                                                                    class="arrow up">进度</a></td>
                                                    <%}%>
                                                    <td width="101" class="w150"></td>
                                                </tr>
                                                <%
                                                    Bdlb[] creditInfos = result.getItems();
                                                    if (creditInfos != null) {
                                                        for (Bdlb creditInfo : creditInfos) {
                                                            if (creditInfo == null) {
                                                                continue;
                                                            }
                                                %>
                                                <tr class="all_bj">
                                                    <td>
                                                        <div class="w250"><span class="xin ml30 mr10">
															<%
                                                                boolean falg = true;
                                                                if (creditInfo.F28.name().equals("S")) {
                                                                    falg = false;
                                                            %>新<%
                                                            }
                                                            if (creditInfo.F29.name().equals("S")) {
                                                                falg = false;
                                                        %>奖<%}%>
			<%
                if (falg) {
                    if (creditInfo.F16 == T6230_F11.S) {
            %>保
            <%
            } else if (creditInfo.F17 == T6230_F13.S) {
            %>抵
            <%
            } else if (creditInfo.F18 == T6230_F14.S) {
            %>实
            <%
            } else {
            %>信<%
                                                                }
                                                            }
                                                        %>
            </span><span class="w200"><a title="<%=creditInfo.F04%>"
                                         href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>">
                                                            <%
                                                                StringHelper.filterHTML(out,
                                                                        StringHelper.truncation(creditInfo.F04, 7));
                                                            %>
                                                        </a></span></div>
                                                    </td>
                                                    <td><%
                                                        if (creditInfo.F11 == T6230_F20.HKZ ||
                                                                creditInfo.F11 == T6230_F20.YJQ ||
                                                                creditInfo.F11 == T6230_F20.YDF) {
                                                            creditInfo.F06 = creditInfo.F06.subtract(creditInfo.F08);
                                                            creditInfo.proess = 1;
                                                            creditInfo.F08 = new BigDecimal(0);
                                                        }

                                                        if (creditInfo.F06.doubleValue() >= 10000) {%><span
                                                            class="f16"><%=creditInfo.F06.doubleValue() /
                                                            10000%></span>
                                                        <span class="f12">万元</span>
                                                        <%
                                                        } else {
                                                        %><span class="f16"><%=Formater.formatAmount(creditInfo.F06)%></span><span
                                                                class="f12">元</span><%}%>
                                                    </td>
                                                    <td><span class="f16"><%=Formater.formatRate(creditInfo.F07,
                                                            false)%></span><span class="f12">%</span></td>
                                                    <%-- <td><span class="f16"><%=creditInfo.F10%></span><span class="f12">个月</span> </td> --%>
                                                    <td><span class="f16"><%
                                                        if (T6231_F21.S == creditInfo.F19) {
                                                            out.print(creditInfo.F20 + "<span class=\"f12\">天<span>");
                                                        } else {
                                                            out.print(creditInfo.F10 + "<span class=\"f12\">个月</span>");
                                                        }
                                                    %></span></td>
                                                    <td><%
                                                        if (creditInfo.F08.doubleValue() >= 10000) {%><span
                                                            class="f16"><%=creditInfo.F08.doubleValue() /
                                                            10000%></span>
                                                        <span class="f12">万元</span>
                                                        <%
                                                        } else {
                                                        %><span class="f16"><%=Formater.formatAmount(creditInfo.F08)%></span><span
                                                                class="f12">元</span><%}%></td>
                                                    <td>
                                                        <%
                                                            if (creditInfo.F11 != T6230_F20.YFB) {
                                                        %>
                                                        <div class="pl30"><span class="ui-list-field w108">
	            <strong class="ui-progressbar-mid ui-progressbar-mid-<%=(int)(creditInfo.proess*100)%>"><em><%=Formater.formatProgress(
                        creditInfo.proess)%>
                </em></strong></span>
                                                        </div>
                                                        <%
                                                        } else {
                                                        %>
                                                        <span class="ln24"><%=TimestampParser.format(creditInfo.F13,
                                                                "yyyy-MM-dd HH:mm")%>即将开启</span>
                                                        <%} %>
                                                    </td>
                                                    <td><%
                                                        if (creditInfo.F11 == T6230_F20.TBZ) {
                                                    %>
                                                        <%
                                                            if (dimengSession != null &&
                                                                    dimengSession.isAuthenticated()) {
                                                                UserInfoManage uManage =
                                                                        serviceSession.getService(UserInfoManage.class);
                                                                T6110 t6110 =
                                                                        uManage.getUserInfo(serviceSession.getSession()
                                                                                .getAccountId());
                                                                if (T6110_F07.HMD != t6110.F07) {
                                                        %>
                                                        <span><a
                                                                href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>"
                                                                class="btn btn01 ml15">立即投资</a></span>
                                                        <%
                                                        } else {
                                                        %>
                                                        <span class="btn btn02 ml15">立即投资</span>
                                                        <%
                                                            }
                                                        } else {
                                                        %>
                                                        <span><a
                                                                href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>"
                                                                class="btn btn01 ml15">立即投资</a></span>
                                                        <%} %>

                                                        <%
                                                        } else {
                                                        %>
                                                        <%
                                                            if (creditInfo.F11 == T6230_F20.HKZ) {
                                                        %><span class="btn btn02 ml15">还款中</span><%
                                                        } else {
                                                        %><span class="btn btn02 ml15"><%=creditInfo.F11.getChineseName() %></span><%}%><%}%>
                                                    </td>
                                                </tr>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </table>
                                        </div>
                                        <%
                                            AbstractFinancingServlet.rendPaging(out,
                                                    result,
                                                    controller.getPagingURI(request,
                                                            com.dimeng.p2p.front.servlets.financing.sbtz.Grlb.class),
                                                    parameter.getQueryString());
                                        %>

                                    </div>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <!--投资项目列表-->
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    getUserEarnMoney();
    getTotleMoneyStr();
    function getUserEarnMoney() {
        var userEarnMoney = '<%=total.userEarnMoney.doubleValue()%>';
        var emObj = $("#emObj2");
        var unit = $("#unit2");
        formatCountMoney(userEarnMoney, emObj, unit);
    }

    function getTotleMoneyStr() {
        var amountobj = '<%=total.totleMoney.doubleValue()%>';
        var emObj = $("#emObj");
        var unit = $("#unit");
        formatCountMoney(amountobj, emObj, unit);
    }

    function formatCountMoney(amount, emObj, unitObj) {
        var s = "";
        var unit = "";
        if (amount == 0) {
            s = s + amount + ".00";
            unit = "元";
        } else {
            if (amount > 100000000) {
                amount = amount * 10000 / 1000000000000;
                if (amount.toString().indexOf(".") < 0) {
                    s = s + formatMoney(amount, 2) + "";
                    unit = "亿元";
                } else {
                    s = s + formatMoney(amount, 2) + "";
                    unit = "亿元";
                }
            } else if (amount >= 10000 && amount < 100000000) {
                amount = amount * 10000 / 100000000;
                if (amount.toString().indexOf(".") < 0) {
                    s = s + formatMoney(amount, 2) + "";
                    unit = "万元";
                } else {
                    s = s + formatMoney(amount, 2) + "";
                    unit = "万元";
                }
            } else {
                s = s + formatMoney(amount, 2) + "";
                unit = "元";
            }
        }
        emObj.html(s);
        unitObj.html(unit);
    }

    function formatMoney(s, flg) {
        if (flg == 1) {
            if (s > 10000) {
                s = s / 10000;
                if (s.toString().indexOf(".") < 0) {
                    return s.toString() + ".0";
                }
                return s;
            }
        }
        if (/[^0-9\.]/.test(s)) {
            return "0.00";
        }
        if (s == null || s == "") {
            return "0.00";
        }
        s = s.toString().replace(/^(\d*)$/, "$1.");
        s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
        s = s.replace(".", ",");
        var re = /(\d)(\d{3},)/;
        while (re.test(s)) {
            s = s.replace(re, "$1,$2");
        }
        s = s.replace(/,(\d\d)$/, ".$1");
        return s;
    }
</script>
</body>
</html>