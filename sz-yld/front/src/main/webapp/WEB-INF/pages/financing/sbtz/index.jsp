<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F17"%>
<%@page import="com.dimeng.p2p.S62.entities.T6299"%>
<%@page import="com.dimeng.p2p.S62.enums.T6299_F04"%>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F27" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F28" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F14" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.common.enums.CreditTerm" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.Lcjsq" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Bdxq" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Grlb" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Index" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Zqzrlb" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdlb" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx" %>
<%@page import="com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@page import="com.dimeng.p2p.repeater.policy.OperateDataManage"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Bdlx" %>
<%@page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@page import="com.dimeng.p2p.S61.entities.T6196"%>
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.S61.entities.T6147"%>
<%@page import="com.dimeng.p2p.S61.entities.T6148"%>
<%@page import="com.dimeng.p2p.S61.enums.T6147_F04"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=<%=resourceProvider.getCharset() %>" />
    <meta name="keywords" content="投资项目大全,个人投资项目,最新投资项目"/>
    <meta name="description" content="<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>为您提供最新的个人投资项目大全,在这里您将找到最适合您的投资项目,超高年化利率等你来抢！<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>-你的投资理财首选平台。"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Pragma" CONTENT="no-cache"/>
    <meta http-equiv="Cache-Control" CONTENT="no-cache"/>
    <meta http-equiv="Expires" CONTENT="0"/>
    <title>最新投资项目_投资项目大全_个人投资项目-<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    BidManage bidManage = serviceSession.getService(BidManage.class);
    final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    Tztjxx total = bidManage.getStatisticsQy();
    //T6211[] t6211s = bidManage.getBidType();
    T6216[] t6216s = bidManage.getProducts();    //产品信息
    T6299 NHSYFirst = bidManage.getFirst(T6299_F04.NHSY); //获取第一条年化利率筛选条件
    T6299 NHSYLast = bidManage.getLast(T6299_F04.NHSY); //获取最后一条年化利率筛选条件
    T6299[] NHSYFilters = bidManage.getAddFilter(T6299_F04.NHSY); //获取后台添加的年化利率筛选条件
    T6299 JKQXFirst = bidManage.getFirst(T6299_F04.JKQX); //获取第一条借款期限筛选条件
    T6299 JKQXLast = bidManage.getLast(T6299_F04.JKQX); //获取最后一条借款期限筛选条件
    T6299[] JKQXFilters = bidManage.getAddFilter(T6299_F04.JKQX); //获取后台添加的借款期限筛选条件
    T6299 RZJDFirst = bidManage.getFirst(T6299_F04.RZJD); //获取第一条融资进度筛选条件
    T6299 RZJDLast = bidManage.getLast(T6299_F04.RZJD); //获取最后一条融资进度筛选条件
    T6299[] RZJDFilters = bidManage.getAddFilter(T6299_F04.RZJD); //获取后台添加的融资进度筛选条件
    PagingResult<Bdlb> result = bidManage.searchQy(
            new QyBidQuery() {
                public int getRate() {
                    return 0;
                }

                public int getJd() {
                    return 0;
                }

                public T6230_F20 getStatus() {
                    return EnumParser.parse(T6230_F20.class, null);
                }

                public int getOrder() {
                    return 0;
                }

                public int getProductId() {
                    return 0;
                }

                public int bidType() {
                    return 0;
                }

            }, T6110_F06.FZRR, new Paging() {
                public int getCurrentPage() {
                    return 1;
                }

                public int getSize() {
                    return 10;
                }
            });
    boolean sessionFlg = false;
    boolean hmdFlg = false;
    boolean zrrFlg = true;
    boolean isInvest = true;//企业帐号是否允许投资
    String rzUrl="";
    boolean isInvestLimit=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
    boolean isOpenRiskAccess=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
    String userRiskLevel= "BSX";
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
        sessionFlg = true;
        if (T6110_F07.HMD == t6110.F07) {
            hmdFlg = true;
        }
        if (T6110_F06.FZRR == t6110.F06) {
            zrrFlg = false;
        }
        T6147 t6147=uManage.getT6147();
        if(t6147==null){
        	t6147=new T6147();
        	t6147.F04=T6147_F04.BSX;
        }
        userRiskLevel = t6147.F04.name();
        if(T6110_F06.FZRR == t6110.F06){
            rzUrl = "/user/account/safetymsgFZRR.htm";
        }else{
            rzUrl = "/user/account/userBases.html?userBasesFlag=1";
        }
        if(T6110_F06.FZRR == t6110.F06 && T6110_F17.F == t6110.F17){
		    isInvest = false;
		}
    }
    String pageStr = bidManage.rendPaging(result);
    int pageCount = result.getPageCount();
    OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
    T6196 t6196 = manage.getT6196();
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
    <div class="w1002">
        <div class="main_tab">
            <ul class="clearfix">
                <li class="hover" value="1" id="busi_li">投资列表</li>
                <li class="normalMenu" value="3" id="busi_li_zq"><a href="javascript:void(0);">债权转让</a></li>
            </ul>
        </div>
        <div class="bd jtab-con">
            <div class="item_filter">
                <div class="main_hd02 mb20">筛选投资项目</div>
                <dl id="productDl">
                    <dt>产品名称：</dt>
                    <dd>
                        <a id="product_1" href="javascript:void(0);" name="" class="cur">不限</a><%--</div>--%>
                        <%
                            if (t6216s != null && t6216s.length > 0) {
                                int index = 2;
                                for (T6216 product : t6216s) {
                        %>
                        <a id="product_<%=index %>" href="javascript:void(0);"
                           name="<%=product.F01%>"><%=product.F02%>
                        </a>
                        <%
                                index++;
                            }
                        %>
                        <%}%>
                    </dd>
                </dl>
                <dl id="yearRateDl">
                    <dt>年化利率：</dt>
                    <dd>
                        <a id="yearRate_1" href="javascript:void(0);" name="" class="cur">不限</a>
                        <a id="yearRate_2" href="javascript:void(0);" name="<%=Integer.toString(NHSYFirst.F01) %>"><%=NHSYFirst.F03 %>%以下</a>
                        <%
                        	int i = 3;
                        	if(NHSYFilters != null && NHSYFilters.length > 0){
                        	    for (T6299 t6299 : NHSYFilters){
                        %>	        
                        <a id="yearRate_<%=i %>" href="javascript:void(0);" name="<%=Integer.toString(t6299.F01) %>"><%=t6299.F02 %>%-<%=t6299.F03 %>%</a>
                        <%	   i++; }
                        	}
                        %>
                        <a id="yearRate_<%=i %>" href="javascript:void(0);" name="<%=Integer.toString(NHSYLast.F01) %>"><%=NHSYLast.F02 %>%以上</a>
                    </dd>
                </dl>
                <dl id="creditTermDl"  style="display:none;">
                    <dt>借款期限：</dt>
                    <dd>
                        <a id="creditTerm_1" href="javascript:void(0);" name="" class="cur">不限</a>
                        <a id="creditTerm_2" href="javascript:void(0);" name="<%=Integer.toString(JKQXFirst.F01) %>" class="cur"><%=JKQXFirst.F03 %>个月以下</a>
                        <% 
                        	int j = 3;
                        	if(JKQXFilters !=null && JKQXFilters.length > 0){
                            for (T6299 t6299 : JKQXFilters) {%>
                        <a id="creditTerm_<%=j%>" href="javascript:void(0);" name="<%=Integer.toString(t6299.F01) %>"><%=t6299.F02 %>-<%=t6299.F03 %>个月
                        </a>
                        <% j++;}
                        }%>
                        <a id="creditTerm_<%=j%>" href="javascript:void(0);" name="<%=Integer.toString(JKQXLast.F01) %>" class="cur"><%=JKQXLast.F02 %>个月以上</a>
                    </dd>
                </dl>
                <dl id="processDl">
                    <dt>融资进度：</dt>
                    <dd>
                        <a id="progress_1" href="javascript:void(0);" name="" class="cur">不限</a>
                        <a id="progress_2" href="javascript:void(0);" name="<%=Integer.toString(RZJDFirst.F01) %>"><%=RZJDFirst.F03 %>%以下</a>
                        <% 
                        	int k = 3;
                        	if(RZJDFilters !=null && RZJDFilters.length > 0){
                            for (T6299 t6299 : RZJDFilters) {%>
                        <a id="progress_<%=k%>" href="javascript:void(0);" name="<%=Integer.toString(t6299.F01) %>"><%=t6299.F02 %>%-<%=t6299.F03 %>%
                        </a>
                        <% k++;}
                        }%>
                        <a id="progress_<%=k%>" href="javascript:void(0);" name="<%=Integer.toString(RZJDLast.F01) %>"><%=RZJDLast.F02 %>%以上</a>
                    </dd>
                </dl>
                <dl id="bidStatusDl">
                    <dt>项目状态：</dt>
                    <dd>
                        <a id="status_1" href="javascript:void(0);" name="" class="cur">不限</a>
                        <a id="status_2" href="javascript:void(0);" name="TBZ">可投资</a>
                        <a id="status_3" href="javascript:void(0);" name="HKZ">还款中</a>
                        <a id="status_4" href="javascript:void(0);" name="YJQ">已还款</a>
                    </dd>
                </dl>


            </div>
            <div class=" item_list">
                <div class="main_hd">
                    <a target="_blank" href="<%=controller.getURI(request, Lcjsq.class)%>"
                       class="calculator_ico fr"><i></i>理财计算器</a>
                    <i class="icon"></i><span id="tzlb_title" class="gray3 f18">投资列表</span>
                </div>
                <div class="total">
                    <ul>
                        <li id="ljcjzeLi"><span id="transCountAmountSpan">累计成交总金额<br/>
		                 <%
                             if (total.totleMoney.add(t6196.F02).doubleValue() >= 100000000) {
                         %>
			        	   <span class="f36 gray3"><%=Formater.formatAmount(total.totleMoney.add(t6196.F02).doubleValue() / 100000000)%></span>亿元
			        	<%
                        } else if (total.totleMoney.add(t6196.F02).doubleValue() >= 10000 && total.totleMoney.add(t6196.F02).doubleValue() < 100000000) {
                        %>
			        		<span class="f36 gray3"><%=Formater.formatAmount(total.totleMoney.add(t6196.F02).doubleValue() / 10000)%></span>万元
			        	<%
                        } else {
                        %>
			        		<span class="f36 gray3"><%=Formater.formatAmount(total.totleMoney.add(t6196.F02))%></span>元
			        		<%}%>
			        		</span>
                        </li>
                        <li class="line"></li>
                        <li id="ljcjbsLi">
	                 	   <span>累计成交总笔数<br/>
	                 	   	   <span class="f36 gray3" id="transCountEm"><%=total.totleCount+t6196.F05%>
	                         </span>笔
                 	  		 </span>
                        </li>
                        <li class="line" id="line"></li>
                        <li id="yhzqLi"><span id="earnCountSpan">为用户累计赚取<br/>
		                <%
                            if (total.userEarnMoney.add(t6196.F04).doubleValue() >= 100000000) {
                        %>
				       	   <span class="f36 gray3"><%=Formater.formatAmount(total.userEarnMoney.add(t6196.F04).doubleValue() / 100000000)%></span>亿元
				       		<%
                            } else if (total.userEarnMoney.add(t6196.F04).doubleValue() >= 10000 &&
                                    total.userEarnMoney.add(t6196.F04).doubleValue() < 100000000) {
                            %>
			       		 <span class="f36 gray3"><%=Formater.formatAmount(total.userEarnMoney.add(t6196.F04).doubleValue() / 10000)%> </span>万元
			       		<%
                        } else {
                        %>
			       		 <span class="f36 gray3"><%=Formater.formatAmount(total.userEarnMoney.add(t6196.F04))%></span>元
			       		<%}%>
			                </span>
                        </li>
                        <li id="zqzrbsLi" style="display:none;">
	                 		<span>累计转让总笔数<br/>
		                 	   <span class="f36 gray3" id="zqzrbsEm"><%=total.totleCount%>
		                        </span>笔
	                 		</span>
                        </li>
                    </ul>
                </div>
                <div class="list" id="bidInfo">
                    <table width="100%" cellspacing="0" id="bidTable">
                        <tr class="til">
                            <td width="28%">项目名称</td>
                            <td width="12%"><a id="sort_1" href="javascript:void(0);"
                                               class="sorting" name="42">贷款金额</a></td>
                            <td width="9%"><a id="sort_2" href="javascript:void(0);"
                                               class="sorting sorting2" name="12">年化利率</a></td>
                            <td width="7%"><a id="sort_3" href="javascript:void(0);"
                                               class="sorting sorting3" name="52">期限</a></td>
                            <td width="12%"><a id="sort_4" href="javascript:void(0);"
                                               class="sorting" name="62">还需金额</a></td>
                            <td width="18%"><a id="sort_5" href="javascript:void(0);"
                                               class="sorting" name="72">进度</a>
                            </td>
                            <td width="14%">操作</td>
                            
                        </tr>
                        <%
                            Bdlb[] creditInfos = result.getItems();
                            if (creditInfos != null) {
                                for (Bdlb creditInfo : creditInfos) {
                                    if (creditInfo == null) {
                                        continue;
                                    }
                        %>
                        <tr>
                            <td>
                                <div class="title">
                                    <%if (creditInfo.F28 == T6230_F28.S) { %>
                                    <span class="item_icon novice_icon">新</span>
                                    <%} %>
                                    <%if (creditInfo.F29 == T6231_F27.S) { %>
                                    <span class="item_icon reward_icon">奖</span>
                                    <%} %>

                                    <%
                                        if (creditInfo.F16 == T6230_F11.S) {
                                    %><span class="item_icon dan_icon">保</span>
                                    <%
                                    } else if (creditInfo.F17 == T6230_F13.S) {
                                    %><span class="item_icon di_icon">抵</span>
                                    <%
                                    } else if (creditInfo.F18 == T6230_F14.S) {
                                    %><span class="item_icon shi_icon">实</span>
                                    <%
                                    } else {
                                    %> <span class="item_icon xin_icon">信</span><%}%>
                                    <a title="<%=creditInfo.F04%>"
                                       href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>">
                                        <%
                                            StringHelper.filterHTML(out,
                                                    StringHelper.truncation(creditInfo.F04, 12));
                                        %>
                                    </a>
                                </div>
                            </td>
                            <td>
                                <%
                                    if (creditInfo.F11 == T6230_F20.HKZ ||
                                            creditInfo.F11 == T6230_F20.YJQ ||
                                            creditInfo.F11 == T6230_F20.YDF) {
                                        creditInfo.F06 = creditInfo.F06.subtract(creditInfo.F08);
                                        creditInfo.proess = 1;
                                        creditInfo.F08 = new BigDecimal(0);
                                    }
                                    if (creditInfo.F06.doubleValue() >= 10000) {
                                %><span class="f24 gray3"><%=Formater.formatAmount(creditInfo.F06.doubleValue() / 10000)%></span>万元
                                <%
                                } else {
                                %><span class="f24 gray3"><%=Formater.formatAmount(creditInfo.F06)%></span>元<%}%>
                            </td>
                            <td><span class="f24 gray3"><%=Formater.formatRate(creditInfo.F07, false)%></span>%</td>
                            <%-- <td><span class="f16"><%=creditInfo.F10%></span><span class="f12">个月</span> </td> --%>
                            <td><span class="f24 gray3"><%
                                if (T6231_F21.S == creditInfo.F19) {
                                    out.print(creditInfo.F20 + "天");
                                } else {
                                    out.print(creditInfo.F10 + "个月");
                                }
                            %></span></td>
                            <td><%
                                if (creditInfo.F08.doubleValue() >= 10000) {%><span
                                    class="f24 gray3"><%=Formater.formatAmount(creditInfo.F08.doubleValue() /
                                    10000)%></span>万元
                                <%
                                } else {
                                %><span class="f24 gray3"><%=Formater.formatAmount(creditInfo.F08)%></span>元<%}%>
                            </td>
                            <td>

                                <% if (creditInfo.F11 == T6230_F20.TBZ) {
                                    if (sessionFlg) {
                                        if (hmdFlg) {
                                %>
                                <a href="javascript:void(0);" class="btn06 btn_gray btn_disabled">立即投资</a>
                                <%
                                } else {
                                %>
                                <a href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>"
                                   class="btn06">立即投资</a>
                                <%
                                    }
                                } else {
                                %>
                                <a href="<%=controller.getPagingItemURI(request, Bdxq.class,creditInfo.F02)%>"
                                   class="btn06">立即投资</a>
                                <%} %>
                                <%
                                } else {
                                %>
                                <a href="javascript:void(0);"
                                   class="btn06 btn_gray btn_disabled"><%=creditInfo.F11.getChineseName() %>
                                </a>
                                <%}%>
                                <%
                                    if (creditInfo.F11 != T6230_F20.YFB) {
                                %>
                                <div class="progress mt10"><span class="progress_bg">
						            <span class="progress_bar" style="width:<%=(int)(creditInfo.proess*100)%>%;"></span></span>
                                    <span class="cent"><%=Formater.formatProgress(creditInfo.proess)%></span>
                                </div>
                                <%
                                } else {
                                %>
                                <p class="orange mt10"><%=TimestampParser.format(creditInfo.F13,
                                        "yyyy-MM-dd HH:mm")%><br/>即将开始</p>
                                <%} %>
                            </td>

                        </tr>
                        <%
                                }
                            }
                        %>
                    </table>
                </div>
                <div id="pageContent">
                    <%=pageStr %>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<!--投资项目列表-->

<table width="100%" cellspacing="0" id="zqzrTable" style="display:none;">
    <tr class="til">
        <td width="28%">线上债权转让标题</td>
        <td width="10%"><a id="sort_6" href="javascript:void(0);" class="sorting sorting" name="42">年化利率</a></td>
        <td width="10%"><a id="sort_7" href="javascript:void(0);" class="sorting" name="12">剩余期数</a></td>
        <td width="12%"><a id="sort_8" href="javascript:void(0);" class="sorting sorting2" name="52">债权价值</a></td>
        <td width="12%"><a id="sort_9" href="javascript:void(0);" class="sorting sorting3" name="62">待收本息</a></td>
        <td width="12%"><a id="sort_10" href="javascript:void(0);" class="sorting" name="72">转让价格</a></td>
        <td width="16%">操作</td>
    </tr>
</table>
<table width="100%" cellspacing="0" id="bidTableHdn" style="display:none;">
    <tr class="til">
        <td width="28%">项目名称</td>
        <td width="12%"><a id="sort_1" href="javascript:void(0);" class="sorting" name="42">贷款金额</a></td>
        <td width="9%"><a id="sort_2" href="javascript:void(0);" class="sorting sorting2" name="12">年化利率</a></td>
        <td width="7%"><a id="sort_3" href="javascript:void(0);" class="sorting sorting3" name="52">期限</a></td>
        <td width="12%"><a id="sort_4" href="javascript:void(0);" class="sorting" name="62">还需金额</a></td>
        <td width="18%"><a id="sort_5" href="javascript:void(0);" class="sorting" name="72">进度</a></td>
        <td width="14%">操作</td>
        <td width="101" class="w150"></td>
    </tr>
</table>
<input type="hidden" id="enterpriseUrl" value="<%=controller.getURI(request,Index.class)%>"/>
<input type="hidden" id="bdxqUrl" value="/financing/sbtz/bdxq/"/>
<input type="hidden" id="zqxqUrl" value="/financing/sbtz/zqxq/"/>
<input type="hidden" id="personalUrl" value="<%=controller.getURI(request,Grlb.class)%>"/>
<input type="hidden" id="zqzrUrl" value="<%=controller.getURI(request,Zqzrlb.class)%>"/> 
<input type="hidden" id="loginUrl" value="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR_XQ);%>"/>

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
    if (tg && !"FUYOU".equals(escrow)) {
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
    <div class="popup_bg" style="display: none;"></div>
    <div class="dialog" style="display: none;">
        <%
            if ((tg) && StringHelper.isEmpty(usrCustId)) {
        %>
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
        <% } else {  %>
        <div id="idRisk" style="display:none"></div>
        <div id="id_content" style="display:none">
	        <div class="title"><a href="javascript:void(0);" class="out" onclick="resetForm();"></a>债权购买确认</div>
	        <div class="content">
	            <div class="tip_information">
	                <div class="question"></div>
	                <div class="tips">
		          <span class="f20 gray3">
	               	 您此次购买债权价值为<i class="red"><span id="zqjz"></span></i>元，<br/>需支付金额<i class="red"><span id="zrjg"></span></i>元,确认购买？
	                </span>
	                </div>
	                <%if (isOpenPwd) { %>
	                <div class="mt20">
	                    <span class="red">*</span>交易密码:&nbsp;&nbsp;<input type="password"
	                                                                      class="required text_style"
	                                                                      id="tran_pwd" autocomplete="off"/><br/>
	                    <span id="errorSpan" class="red" style="display: none"></span>
	                </div>
	                <%} %>
	            </div>
	            <%
	                TermManage termManage = serviceSession.getService(TermManage.class);
	                T5017 term = termManage.get(TermType.ZQZRXY);
	                if(term != null){
	            %>
	            <div class="tc mt10">
	                <input name="iAgree" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
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
<input type="hidden" name="isTG" id="isTG"
       value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd"
       value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
<input type="hidden" name="escrow_pre" id="escrow_pre" value="<%=BooleanParser.parse(configureProvider.format(SystemVariable.ESCROW_PREFIX))%>"/>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzr.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/bidInfo.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "successful", $("#zqSucc").val()));
    $("div.popup_bg").show();
</script>
<%}%>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "error"));
    $("div.popup_bg").show();
</script>
<%}%>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "doubt"));
    $("div.popup_bg").show();
</script>
<%}%>

<script type="text/javascript">
	var isOpenRiskAccess ='<%=isOpenRiskAccess%>';
	var isInvestLimit = '<%=isInvestLimit%>';
	var userRiskLevel= '<%=userRiskLevel%>';
    var currentPage = 1;
    var pageSize = 10;
    var pageCount = <%=pageCount%>;
    var sessionFlg = <%=sessionFlg%>;
    var hmdFlg = <%=hmdFlg%>;
    var zrrFlg = <%=zrrFlg%>;
    var preOrderItem = "";
    var orderItemId = "";
    var _rzUrl="<%=rzUrl%>";
    var _bdlxUrl="<%=controller.getURI(request, Bdlx.class)%>";
    var isInvest = <%=isInvest%>;
    
    $(function () {
        $(document).keypress(function (e) {
            if (e.keyCode == 13){
                $(".btn_ok").click();
            }
       });
        $("a.out").click(function () {
            $("div.popup_bg").hide();
            $("div.dialog").hide();
        });

        <%String btype = request.getParameter("btype");
            if(!StringHelper.isEmpty(btype) && "3".equals(btype))
            {%>
                var liobj = $("#busi_li_zq")[0];
                setNormalMenu(liobj);
            <%}
            else
            {%>
                var liobj = $("#busi_li")[0];
                setNormalMenu(liobj);
            <%}
    %>
    });
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    var _checkTxPwdUrl = '<%configureProvider.format(out,URLVariable.CHECK_TXPWD);%>';

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