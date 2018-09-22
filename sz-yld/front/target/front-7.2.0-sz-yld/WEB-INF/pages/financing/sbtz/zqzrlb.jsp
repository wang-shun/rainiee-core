<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F17"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Zqxq"%>
<%@page import="com.dimeng.p2p.common.RiskLevelCompareUtil"%>
<%@page import="com.dimeng.p2p.S62.entities.T6299"%>
<%@page import="com.dimeng.p2p.S62.enums.T6299_F04"%>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6260_F07" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.query.TransferQuery_Order" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S61.entities.T6110" %>
<%@page import="com.dimeng.p2p.account.front.service.UserInfoManage" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F14" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.common.enums.CreditTerm" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb" %>
<%@page import="com.dimeng.util.http.URLParameter" %>
<%@page import="com.dimeng.p2p.S51.entities.T5124" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Zqzrtj" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.TransferManage" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.common.enums.TermType" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@ page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.S61.entities.T6147"%>
<%@page import="com.dimeng.p2p.S61.entities.T6148"%>
<%@page import="com.dimeng.p2p.S61.enums.T6147_F04"%>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>理财投资列表-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
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
    static final String PRODUCT_KEY = "t";
%>
<%
    TransferManage service = serviceSession.getService(TransferManage.class);
    final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    Zqzrtj total = service.getStatistics();
    boolean multiSelect = "1".equals(request.getParameter(MULTISELECT_KEY));
    BidManage bidManage = serviceSession.getService(BidManage.class);
    T6216[] t6216s = bidManage.getProducts();
    T6216 t6216=null;
    for(int i=0;i<t6216s.length;i++){
    	if(IntegerParser.parse(requestWrapper.getParameter(PRODUCT_KEY))==t6216s[i].F01){
    		t6216=t6216s[i];
    		break;
    	}
    }
    T6211[] t6211s=null;
    if(t6216!=null){
    	t6211s = bidManage.getBidTypeByCondition(t6216.F03);
    }
    T5124[] t5124s = bidManage.getLevel();
    T6299 NHSYFirst = bidManage.getFirst(T6299_F04.NHSY); //获取第一条年化利率筛选条件
    T6299 NHSYLast = bidManage.getLast(T6299_F04.NHSY); //获取最后一条年化利率筛选条件
    T6299[] NHSYFilters = bidManage.getAddFilter(T6299_F04.NHSY); //获取后台添加的年化利率筛选条件
    T6299 JKQXFirst = bidManage.getFirst(T6299_F04.JKQX); //获取第一条借款期限筛选条件
    T6299 JKQXLast = bidManage.getLast(T6299_F04.JKQX); //获取最后一条借款期限筛选条件
    T6299[] JKQXFilters = bidManage.getAddFilter(T6299_F04.JKQX); //获取后台添加的借款期限筛选条件
    URLParameter parameter = new URLParameter(request, controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Zqzrlb.class), multiSelect, MULTISELECT_KEY, TYPE_KEY, RATE_KEY, TERM_KEY, LEVEL_KEY, ORDER_KEY, PRODUCT_KEY);
    PagingResult<Zqzqlb> results = service.search(
            new TransferQuery_Order() {

                public int getProductId() {
                    return IntegerParser.parse(requestWrapper.getParameter(PRODUCT_KEY));
                }
                
                public T6211[] getType() {
                	if(IntegerParser.parse(requestWrapper.getParameter(PRODUCT_KEY))>0){
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
                	}else{
                		return null;
                	}
                }

                public int getRate() {
                    int rate = IntegerParser.parse(requestWrapper.getParameter(RATE_KEY));
                    return rate;
                }

                public int getTerm(){
                    int term = IntegerParser.parse(requestWrapper.getParameter(TERM_KEY));
                    return term;
                }
                /* public CreditTerm[] getTerm() {
                    String[] values = requestWrapper.getParameterValues(TERM_KEY);
                    if (values == null || values.length == 0) {
                        return null;
                    }
                    CreditTerm[] terms = new CreditTerm[values.length];
                    for (int i = 0; i < values.length; i++) {
                        terms[i] = EnumParser.parse(CreditTerm.class, values[i]);
                    }
                    return terms;
                } */

                public T5124[] getCreditLevel() {
                    String[] values = requestWrapper.getParameterValues(LEVEL_KEY);

                    if (values == null || values.length == 0) {
                        return null;
                    }
                    T5124[] levels = new T5124[values.length];
                    for (int i = 0; i < values.length; i++) {
                        levels[i] = new T5124();
                        levels[i].F01 = IntegerParser.parse(values[i]);
                    }
                    return levels;
                }

                public int getOrder() {
                    return IntegerParser.parse(requestWrapper.getParameter(ORDER_KEY));
                }

            }, new Paging() {
                public int getCurrentPage() {
                    return IntegerParser.parse(requestWrapper.getParameter("paging.current"));
                }

                public int getSize() {
                    return 10;
                }
            });

    BigDecimal zqMoney = new BigDecimal(0);
    BigDecimal zfMoney = new BigDecimal(0);
    boolean isInvestLimit=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
    boolean isOpenRiskAccess=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
    String _rzUrl="";
    boolean isRiskMatch = false;
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
    <div class="w1002">
        <div class="main_tab">
            <ul class="clearfix">
                <li class="normalMenu"><a
                        href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class)%>">投资列表</a>
                </li>
                <%--<li class="normalMenu"><a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Grlb.class)%>">个人标</a></li>--%>
                <%-- <li><a href="<%=controller.getViewURI(request, Xxzqlb.class)%>">线下债权转让</a></li> --%>
                <li class="hover">债权转让</li>
            </ul>
        </div>
        <div>
            <div class="item_filter">
                <div class="main_hd02 mb20">筛选投资项目</div>
                <dl>
                    <dt>产品名称：</dt>
                    <dd>
                        <%
                            if (parameter.contains(PRODUCT_KEY)) {
                                parameter.remove(PRODUCT_KEY);
                                parameter.remove(TYPE_KEY);
                        %>
                        <a href="<%=parameter.toString()%>">不限</a>
                        <%} else {%>
                        <a class="cur" href="<%=parameter.toString()%>">不限</a>
                        <%}%>
                        <%
                         if (t6216s != null && t6216s.length > 0) {
                             for (T6216 product : t6216s) {
                                 if(parameter.contains(PRODUCT_KEY, Integer.toString(product.F01))) {
                                     parameter.remove(PRODUCT_KEY, product.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=product.F02%></a>
                        <%} else {
                            parameter.set(PRODUCT_KEY, Integer.toString(product.F01));
                            parameter.remove(TYPE_KEY);
                        %>
                        <a href="<%=parameter.toString()%>"><%=product.F02%></a>
                        <%
                        		}
                             }
                          }
                        %>
                    </dd>
                </dl>
                <%if(t6211s!=null&&t6211s.length>0){ %>
                <dl>
                    <dt>标的类型：</dt>
                    <dd>
                        <%
                            if (parameter.contains(TYPE_KEY)) {
                                parameter.remove(TYPE_KEY);
                        %>
                        <a href="<%=parameter.toString()%>">不限</a>
                        <%} else {%>
                        <a class="cur" href="<%=parameter.toString()%>">不限</a>
                        <%}%>
                        <%
                        for (T6211 type : t6211s) {
                            if (parameter.contains(TYPE_KEY, Integer.toString(type.F01))) {
                                parameter.remove(TYPE_KEY, Integer.toString(type.F01));
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=type.F02%></a>
                        <%
                        	} else {
                            	parameter.set(TYPE_KEY, Integer.toString(type.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=type.F02%></a>
                        <%}}%>
                    </dd>
                </dl>
                <%}%>
                <dl>
                    <dt>年化利率：</dt>
                    <dd>
                        <%
                            if (parameter.contains(RATE_KEY)) {
                                parameter.remove(RATE_KEY);
                        %>
                        <a href="<%=parameter.toString()%>">不限</a>
                        <%} else {%>
                        <a class="cur" href="<%=parameter.toString()%>">不限</a>
                        <%}%>
                        <%
                        if(JKQXFirst!=null){
                            if (parameter.contains(RATE_KEY, Integer.toString(NHSYFirst.F01))) {
                                parameter.remove(RATE_KEY, JKQXFirst.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=NHSYFirst.F03 %>%以下</a>
                        <%
                        } else {
                            parameter.set(RATE_KEY, Integer.toString(NHSYFirst.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=NHSYFirst.F03 %>%以下</a>
                        <%}}%>
                        
                        <%
                        if(NHSYFilters!=null && NHSYFilters.length>0){
                            for (T6299 t6299 : NHSYFilters) {
                                if (parameter.contains(RATE_KEY, Integer.toString(t6299.F01))) {
                                    parameter.remove(RATE_KEY, t6299.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=t6299.F02 %>%-<%=t6299.F03 %>%
                        </a>
                        <%
                        } else {
                            parameter.set(RATE_KEY, Integer.toString(t6299.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=t6299.F02 %>%-<%=t6299.F03 %>%
                        </a>
                        <%
                           }}}
                        %>
                        
                        <%
                            if (parameter.contains(RATE_KEY, Integer.toString(NHSYLast.F01))) {
                                parameter.remove(RATE_KEY, JKQXLast.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=NHSYLast.F02 %>%以上</a>
                        <%
                        } else {
                            parameter.set(RATE_KEY, Integer.toString(NHSYLast.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=NHSYLast.F02 %>%以上</a>
                        <%}%>
                    </dd>
                </dl>
                <dl>
                    <dt>借款期限：</dt>
                    <dd>
                        <%
                            if (parameter.contains(TERM_KEY)) {
                                parameter.remove(TERM_KEY);
                        %>
                        <a href="<%=parameter.toString()%>">不限</a>
                        <%} else {%>
                        <a class="cur" href="<%=parameter.toString()%>">不限</a>
                        <%}%>
                        <%
                            if (parameter.contains(TERM_KEY, Integer.toString(JKQXFirst.F01))) {
                                parameter.remove(TERM_KEY, JKQXFirst.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=JKQXFirst.F03 %>个月以下</a>
                        <%
                        } else {
                            parameter.set(TERM_KEY, Integer.toString(JKQXFirst.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=JKQXFirst.F03 %>个月以下</a>
                        <%}%>
                        
                        <%
                        if(JKQXFilters!=null && JKQXFilters.length>0){
                            for (T6299 t6299 : JKQXFilters) {
                                if (parameter.contains(TERM_KEY, Integer.toString(t6299.F01))) {
                                    parameter.remove(TERM_KEY, t6299.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=t6299.F02 %>-<%=t6299.F03 %>个月
                        </a>
                        <%
                        } else {
                            parameter.set(TERM_KEY, Integer.toString(t6299.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=t6299.F02 %>-<%=t6299.F03 %>个月
                        </a>
                        <%
                           }}}
                        %>
                        
                        <%
                            if (parameter.contains(TERM_KEY, Integer.toString(JKQXLast.F01))) {
                                parameter.remove(TERM_KEY, JKQXLast.F01+"");
                        %>
                        <a class="cur" href="<%=parameter.toString()%>"><%=JKQXLast.F02 %>个月以上</a>
                        <%
                        } else {
                            parameter.set(TERM_KEY, Integer.toString(JKQXLast.F01));
                        %>
                        <a href="<%=parameter.toString()%>"><%=JKQXLast.F02 %>个月以上</a>
                        <%}%>
                    </dd>
                </dl>

            </div>
            <div class=" item_list">
                <div class="main_hd"><a target="_blank"
                                        href="<%=controller.getURI(request, com.dimeng.p2p.front.servlets.credit.Lcjsq.class)%>"
                                        class="calculator_ico fr"><i></i>理财计算器</a><i class="icon"></i><span
                        class="gray3 f18">债权转让</span></div>
                <div class="total">
                    <ul>
                        <li>累计成交总金额<br/><span class="f36 gray3">
			        	   <em id="emObj"></em></span><em id="unit"></em>
                        </li>
                        <li class="line"></li>
                        <li>累计转让总笔数<br/><span class="f36 gray3">
		           	<%=total.totleCount%></span>笔
                        </li>
                    </ul>
                </div>
                <div class="list debt_list">
                    <table width="100%" cellspacing="0">
                        <tr class="til">
                            <td width="28%">线上债权转让标题</td>
                            <%
                                if (parameter.contains(ORDER_KEY, "42")) {
                                    parameter.set(ORDER_KEY, "41");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting sorting2">年化利率</a></td>
                            <%
                            } else if (parameter.contains(ORDER_KEY, "41")) {
                                parameter.set(ORDER_KEY, "42");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting sorting3">年化利率</a></td>
                            <%
                            } else {
                                parameter.set(ORDER_KEY, "42");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting">年化利率</a></td>
                            <%}%>
                            <%
                                if (parameter.contains(ORDER_KEY, "12")) {
                                    parameter.set(ORDER_KEY, "11");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting sorting2">剩余期数 </a></td>
                            <%
                            } else if (parameter.contains(ORDER_KEY, "11")) {
                                parameter.set(ORDER_KEY, "12");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting sorting3">剩余期数 </a></td>
                            <%
                            } else {
                                parameter.set(ORDER_KEY, "12");
                            %>
                            <td width="10%"><a href="<%=parameter.toString()%>" class="sorting">剩余期数 </a></td>
                            <%}%>
                            <%
                                if (parameter.contains(ORDER_KEY, "52")) {
                                    parameter.set(ORDER_KEY, "51");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting2">债权价值</a></td>
                            <%
                            } else if (parameter.contains(ORDER_KEY, "51")) {
                                parameter.set(ORDER_KEY, "52");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting3">债权价值</a></td>
                            <%
                            } else {
                                parameter.set(ORDER_KEY, "52");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting">债权价值</a></td>
                            <%} %>
                            <%
                                if (parameter.contains(ORDER_KEY, "62")) {
                                    parameter.set(ORDER_KEY, "61");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting2">待收本息</a></td>
                            <%
                            } else if (parameter.contains(ORDER_KEY, "61")) {
                                parameter.set(ORDER_KEY, "62");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting3">待收本息</a></td>
                            <%
                            } else {
                                parameter.set(ORDER_KEY, "62");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting">待收本息</a></td>
                            <%} %>
                            <%
                                if (parameter.contains(ORDER_KEY, "72")) {
                                    parameter.set(ORDER_KEY, "71");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting2">转让价格</a></td>
                            <%
                            } else if (parameter.contains(ORDER_KEY, "71")) {
                                parameter.set(ORDER_KEY, "72");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting sorting3">转让价格</a></td>
                            <%
                            } else {
                                parameter.set(ORDER_KEY, "72");
                            %>
                            <td width="12%"><a href="<%=parameter.toString()%>" class="sorting ">转让价格</a></td>
                            <%} %>
                            <td width="16%">操作</td>
                        </tr>
                        <%
                            Zqzqlb[] creditAssignments = results.getItems();
                            if (creditAssignments != null) {
                                for (Zqzqlb creditAssignment : creditAssignments) {
                                    if (creditAssignment == null) {
                                        continue;
                                    }
                        %>
                        <tr>
                            <td>
                                <div class="title">
                                    <%if (creditAssignment.F19 == T6230_F11.S) {%><span
                                        class="item_icon dan_icon">保</span>
                                    <%} else if (creditAssignment.F20 == T6230_F13.S) { %><span
                                        class="item_icon di_icon">抵</span>
                                    <%} else if (creditAssignment.F21 == T6230_F14.S) {%><span
                                        class="item_icon shi_icon">实</span>
                                    <%} else {%> <span class="item_icon xin_icon">信</span><%}%>
                                    <a title="<%=creditAssignment.F12 %>" href="<%=controller.getPagingItemURI(request,Zqxq.class,creditAssignment.F01)%>">
                                        <%
                                            StringHelper.filterHTML(out, StringHelper.truncation(creditAssignment.F12, 7));%>
                                    </a>
                                </div>
                            </td>
                            <td>
                                <span class="f18 gray3"><%=Formater.formatRate(creditAssignment.F14, false)%></span>%
                            </td>
                            <td><span class="f18 gray3"><%=creditAssignment.F23%></span>/<%=creditAssignment.F22%>
                            </td>
                            <td><span class="f18 gray3"><%=Formater.formatAmount(creditAssignment.F03)%></span>元</td>
                            <td><span class="f18 gray3"><%=Formater.formatAmount(creditAssignment.dsbx)%></span>元</td>
                            <td><span class="f18 gray3"><%=Formater.formatAmount(creditAssignment.F02)%></span>元</td>
                            <td>
                                <%if (T6260_F07.ZRZ == creditAssignment.F06) { %>
                                <%if (!(dimengSession != null && dimengSession.isAuthenticated())) {%>
                                <a href="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR_XQ+"?"+creditAssignment.F25);%>" style="cursor: pointer;"><span
                                        class="btn06">购买</span></a>
                                <%} else { %>
                                <%
                                    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
                                    T6110 t6110 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
                                    T6147 t6147=userInfoManage.getT6147();
                                    /* if(t6147==null){
                                    	t6147=new T6147();
                                    	t6147.F04=T6147_F04.BSX;
                                    } */
                                    if(RiskLevelCompareUtil.compareRiskLevel((t6147==null?null:t6147.F04), creditAssignment.F30.name())){
                                    	isRiskMatch=true;
                                    }
                                    T6110 t6110RZ = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
                                    if(T6110_F06.FZRR == t6110RZ.F06){
                                        _rzUrl = "/user/account/safetymsgFZRR.htm";
                                    }else{
                                        _rzUrl = "/user/account/userBases.html?userBasesFlag=1";
                                    }
                                    boolean isInvest = true;
                    				if(T6110_F06.FZRR == t6110.F06 && T6110_F17.F == t6110.F17){
                    				    isInvest = false;
                    				}
                                %>
                                <%-- <%if (T6110_F07.HMD == t6110.F07 || T6110_F06.FZRR == t6110.F06) { %> --%>
                                <%if (T6110_F07.HMD == t6110.F07 || !isInvest) { %>
                                <span class="btn06 btn_gray btn_disabled">购买</span>
                                <%} else { %>
                                <%-- <a onclick="buy(<%=creditAssignment.F02%>,<%=creditAssignment.F03%>,<%=creditAssignment.F25%>,'<%=isInvestLimit %>','<%=isRiskMatch %>','<%=isOpenRiskAccess%>>')"
                                   style="cursor: pointer;"><span class="btn06">购买</span></a> --%>
                                <a href="<%=controller.getPagingItemURI(request,Zqxq.class,creditAssignment.F01)%>"
                                   style="cursor: pointer;"><span class="btn06">购买</span></a>
                                <%} %>

                                <%
                                    }
                                } else if (T6260_F07.YJS == creditAssignment.F06) {
                                %>
                                <span class="btn06 btn_gray btn_disabled">已转让</span>
                                <%} %>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </table>
                </div>
                <%AbstractFinancingServlet.rendPaging(out, results, controller.getPagingURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Zqzrlb.class), parameter.getQueryString());%>

            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

<div id="info"></div>
<%
    UserManage userManage = serviceSession.getService(UserManage.class);
    String usrCustId = null;
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
    String action = "";
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        usrCustId = userManage.getUsrCustId();
    }
    if (tg) {
        action = configureProvider.format(URLVariable.ESCROW_URL_EXCHANGE);
    } else {
        action = configureProvider.format(URLVariable.TB_ZQZR);
    }
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
%>
<form method="post" class="form1" action="<%=action %>">
    <%=FormToken.hidden(serviceSession.getSession()) %>
    <input type="hidden" id="zqSucc" name="zqSucc" value="<%configureProvider.format(out, URLVariable.USER_ZQYZR);%>">
    <input type="hidden" name="zqzrId" id="zqzrId">

    <div class="popup_bg" style="display: none;"></div>
    <div class="dialog" style="display: none;">
        <%if ((tg) && StringHelper.isEmpty(usrCustId)) { %>
        <div class="title"><a href="javascript:void(0);" class="out"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
		          <span class="f20 gray3">
            	您需要在第三方托管平台上进行注册，才可购买债权！请<a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>"
                                            class="red">立即注册</a>！</span>
                </div>
            </div>
        </div>
        <%} else { %>
        <div id="idRisk" style="display:none"></div>
        <div id="id_content" style="display:none">
	        <div class="title"><a href="javascript:void(0);" class="out" onclick="resetForm();"></a>债权购买确认</div>
	        <div class="content">
	            <div class="tip_information">
	                <div class="question"></div>
	                <div class="tips">
		          <span class="f20 gray3">
	          	您此次购买债权价值为<i class="red"><span id="zqjz"><%=zqMoney%></span></i>元，<br/>需支付金额<i class="red"><span
	                      id="zrjg"><%=zfMoney%></span></i>元,确认购买？
	          	</span>
	                </div>
	                <%if (isOpenPwd) { %>
	                <div class="mt20">
	                    <span class="red">*</span>交易密码:&nbsp;&nbsp;
	                    	<input type="password" class="required text_style" id="tran_pwd" autocomplete="off"/>
	                    <br/>
	                    <span id="errorSpan" class="red" style="display: none"></span>
	                </div>
	                <%} %>
	            </div>
	            <%
	                TermManage termManage = serviceSession.getService(TermManage.class);
	                T5017 term = termManage.get(TermType.ZQZRXY);
	                if(term != null){
	                    T5017 fxtsh = termManage.get(TermType.FXTSH);
	            %>
	            <div class="tc mt10">
	                <input name="iAgree" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
	                <%if(fxtsh!=null){ %>
				    <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.FXTSH.name())%>"
	                   class="highlight">《<%=fxtsh.F01.getName()%>》</a>
				    <%} %>
	                <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.ZQZRXY.name())%>"
	                   class="highlight">《<%=term.F01.getName()%>》</a>
	            </div>
	            <div class="tc mt10">
	                <a href="javascript:void(0)" id="" class="btn01 btn_gray btn_disabled sub-btn">确 定</a>
	                <a href="javascript:void(0)" id="cancel" onclick="resetForm();" class="btn01 btn_gray ml20">取 消</a>
	            </div>
	            <%}else{ %>
	            <div class="tc mt10">
	                <a href="javascript:void(0)" id="ok" class="btn01">确 定</a>
	                <a href="javascript:void(0)" id="cancel" class="btn01 btn_gray ml20">取 消</a>
	            </div>
	            <%} %>
	        </div>
        </div>
        <%} %>
    </div>
    <input type="hidden" name="tranPwd" id="tranPwd"/>
</form>
<input type="hidden" name="isTG" id="isTG" value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd" value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzr.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
</script>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "successful", $("#zqSucc").val()));
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
    $("#info").html(showDialogInfo("<%=warnMessage%>", "doubt"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">

    $(function () {
        $("a.out").click(function () {
            $("div.popup_bg").hide();
            $("div.dialog").hide();
        });
    })

    getTotleMoneyStr();
    function getTotleMoneyStr() {
        var amount = '<%=total.totleMoney.doubleValue()%>';
        var s = "";
        var unit = "";
        if (amount == 0) {
            s = "0.00";
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
        $("#emObj").html(s);
        $("#unit").html(unit);
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
    var _checkTxPwdUrl = '<%configureProvider.format(out,URLVariable.CHECK_TXPWD);%>';
</script>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highslide-with-gallery.js"></script>
<script type="text/javascript">
	var _rzUrl='<%=_rzUrl%>';
    hs.graphicsDir = '<%=controller.getStaticPath(request)%>/js/graphics/';
    hs.align = 'center';
    hs.transitions = ['expand', 'crossfade'];
    hs.wrapperClassName = 'dark borderless floating-caption';
    hs.fadeInOut = true;
    hs.dimmingOpacity = .75;

    // Add the controlbar
    if (hs.addSlideshow) hs.addSlideshow({
        //slideshowGroup: 'group1',
        interval: 5000,
        repeat: false,
        useControls: true,
        fixedControls: 'fit',
        overlayOptions: {
            opacity: .6,
            position: 'bottom center',
            hideOnMouseOut: true
        }
    });

    $(function () {
        //“我同意”按钮切回事件
        $("input:checkbox[name='iAgree']").attr("checked", false);
        $("input:checkbox[name='iAgree']").click(function() {
            var iAgree = $(this).attr("checked");
            var register = $(".sub-btn");
            if (iAgree) {
                register.removeClass("btn_gray btn_disabled");
                register.attr("id","ok");
                //选中“我同意”，绑定事件
                $("#ok").click(function(){
                    ok_click();
                });
            } else {
                register.addClass("btn_gray btn_disabled");
                $("#ok").unbind("click");
                register.attr("id","");
            }
        });
    });

    //重置我同意按钮
    function resetForm(){
    	$("#tran_pwd").val("");
        var register = $(".sub-btn");
        $("input:checkbox[name='iAgree']").attr("checked", false);
        register.addClass("btn_gray btn_disabled");
        $("#ok").unbind("click");
        register.attr("id","");
    }
</script>
</body>
</html>