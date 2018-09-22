<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F29" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F35" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@ page import="com.dimeng.p2p.modules.bid.console.service.entity.Bid" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);%>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    PagingResult<Bid> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    PagingResult<T6216> resultProduct = ObjectHelper.convert(request.getAttribute("resultProduct"), PagingResult.class);
    Bid[] loans = result.getItems();
    Bid searchAmount = (Bid)request.getAttribute("searchAmount");
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
    <!--右边内容-->
    <div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, LoanList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">标的ID</span>
                                        <input type="text" name="bidNo" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bidNo"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">产品名称</span>

                                        <%
                                            int productId = IntegerParser.parse(request.getParameter("productId"));
                                        %>
                                        <select id="productId" class="border mr20 h32 mw100" name="productId">
                                            <option value="0">全部</option>
                                            <%
                                                if (resultProduct != null && resultProduct.getItems() != null) {
                                                    T6216[] products = resultProduct.getItems();
                                                    for (T6216 product : products) {
                                            %>
                                            <option value="<%=product.F01%>"
                                                    <%if(productId==product.F01){%>selected="selected"<%}%>><%
                                                StringHelper.filterHTML(out, product.F02);
                                            %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题</span>
                                        <input type="text" name="title" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("title"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>

                                    <li><span class="display-ib mr5">处理时间</span>
                                        <input type="text" readonly="readonly" name="createTimeStart" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="createTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的属性</span>
                                        <select name="bidAttr" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="dbb"
                                                    <%if("dbb".equals(request.getParameter("bidAttr"))){ %>selected="selected"<%} %>>
                                                担保标
                                            </option>
                                            <option value="dyb"
                                                    <%if("dyb".equals(request.getParameter("bidAttr"))){ %>selected="selected"<%} %>>
                                                抵押标
                                            </option>
                                            <option value="sdb"
                                                    <%if("sdb".equals(request.getParameter("bidAttr"))){ %>selected="selected"<%} %>>
                                                实地标
                                            </option>
                                            <option value="xyb"
                                                    <%if("xyb".equals(request.getParameter("bidAttr"))){ %>selected="selected"<%} %>>
                                                信用标
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">借款类型</span>
                                        <select id="select" class="border mr20 h32 mw100" name="type">
                                            <option value="0">全部</option>
                                            <%
                                                int type = IntegerParser.parse(request.getParameter("type"));
                                            %>
                                            <%
                                                if (t6211s != null) {
                                                    for (T6211 t6211 : t6211s) {
                                                        if (t6211 == null) {
                                                            continue;
                                                        }
                                            %>
                                            <option value="<%=t6211.F01%>"
                                                    <%if(type==t6211.F01){%>selected="selected"<%}%>><%
                                                StringHelper.filterHTML(out, t6211.F02);
                                            %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">账户类型</span>
                                        <select name="userType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR">个人</option>
                                            <option value="FZRR">企业</option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6230_F20 status : T6230_F20.values()) {
                                                    
                                                    // if((!isHasBadClaim && status == T6230_F20.YZR) || (!"huifu".equals(escrow) && (status == T6230_F20.SHZ || status == T6230_F20.DBL))){
                                                    if(!isHasBadClaim && status == T6230_F20.YZR){
                                                        continue;
                                                    }
                                                    if (status != T6230_F20.DFZ) {
                                            %>
                                            <option value="<%=status.name()%>" <%if (status.name().equals(request.getParameter("status"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">标识类型</span>
                                        <select name="bidFlag" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="wbz">未标识</option>
                                            <option value="xsb">新手标</option>
                                            <option value="jlb">奖励标</option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">来源</span>
                                        <select name="source" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6231_F35 source : T6231_F35.values()) {
                                            %>
                                            <option value="<%=source.name()%>" <%if (source.name().equals(request.getParameter("source"))) {%>
                                                    selected="selected" <%}%>><%=source.getChineseName()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li><a href="javascript:search();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>

                                        <%if (dimengSession.isAccessableResource(AddProjectXq.class)) {%>
                                        <a href="<%=controller.getURI(request, AddProjectXq.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>标的ID</th>
                                    <th>产品名称</th>
                                    <th>借款标题</th>
                                    <th>借款账户</th>
                                    <th>借款金额(元)</th>
                                    <th>投资金额(元)</th>
                                    <th>年化利率</th>
                                    <th>期限</th>
                                    <th>处理时间</th>
                                    <th>还款方式</th>
                                    <th>状态</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (loans != null && loans.length >0 ) {
                                        int i = 1;
                                        String hideLoanId;
                                        for (Bid loan : loans) {
                                            hideLoanId = "loan_" + loan.F01;
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=loan.F25%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, loan.productName);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.productName, 10));
                                    %></td>
                                    <td id="<%=hideLoanId%>" title="<%StringHelper.filterHTML(out, loan.F03);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.F03, 10));
                                    %></td>
                                    <td><%
                                        StringHelper.filterHTML(out, loan.F37);
                                    %></td>
                                    <td><%=Formater.formatAmount(loan.F05)%>
                                    </td>
                                    <td><%=Formater.formatAmount(loan.F05.subtract(loan.F07))%>
                                    </td>
                                    <td><%=Formater.formatRate(loan.F06)%>
                                    </td>
                                    <%-- <td><%=loan.F09 %>个月</td> --%>
                                    <td>
                                        <%
                                            if (T6231_F21.S == loan.F41) {
                                                out.print(loan.F42);
                                        %>天
                                        <%
                                        } else {
                                            out.print(loan.F09);
                                        %>个月<%
                                        }
                                    %>
                                    </td>
                                    <td><%=DateTimeParser.format(loan.F24)%>
                                    </td>
                                    <td><%
                                        StringHelper.filterHTML(out, loan.F10.getChineseName());
                                    %></td>
                                    <%-- <td><%StringHelper.filterHTML(out, loan.F30); %></td> --%>
                                    <td><%
                                        StringHelper.filterHTML(out, loan.F20.getChineseName());
                                    %></td>
                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewProject.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loan.F01%>&userId=<%=loan.F02%>&operationJK=CK"
                                           class="link-blue">查看</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">查看</a>
                                        <%
                                            }
                                        %>
                                        <%
                                            if (loan.F20 == T6230_F20.SQZ && dimengSession.isAccessableResource(UpdateProject.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loan.F01%>&userId=<%=loan.F02%>"
                                           class="link-blue">修改</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">修改</a>
                                        <%
                                            }
                                        %>
                                        
                               <%--          <% if (null != escrow && escrow.equals("huifu")) {
                                            if (loan.F20 == T6230_F20.DBL) {
                                        %>
                                        	<a href="<%=configureProvider.format(URLVariable.ESCROW_URL_BIDENTERAGAIN) %>?proId=<%=loan.F25%>" class="libk-deepblue">补录</a>
                                        <% 	}}%> --%>
                                        
                                        <%
                                            if (loan.F20 == T6230_F20.DSH && dimengSession.isAccessableResource(ViewProject.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loan.F01%>&userId=<%=loan.F02%>&operationJK=SH"
                                           class="libk-deepblue">审核</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">审核</a>
                                        <%
                                            }
                                        %>
                                        <%
                                            if (loan.F20 == T6230_F20.DFB && dimengSession.isAccessableResource(Release.class)) {
                                        %>
                                        <a href="javascript:void(0);"
                                           onclick="fbBid('<%=hideLoanId%>','<%=controller.getURI(request, Release.class)%>?loanId=<%=loan.F01%>')"
                                           class="link-orangered ">发布</a>
                                        <a href="javascript:void(0);" onclick="showYfbDiv('<%=loan.F01%>')"
                                           class="link-orangered popup-link" data-wh="450*100">预发布</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">发布</a>
                                        <a href="javascript:void(0)" class="disabled">预发布</a>
                                        <%
                                            }
                                        %>
                                        <%
                                            if (dimengSession.isAccessableResource(BidZf.class) && (loan.F20 == T6230_F20.SQZ || loan.F20 == T6230_F20.DFB || loan.F20 == T6230_F20.DSH)) {
                                        %>
                                        <a href="javascript:void(0);" onclick="showZf('<%=loan.F01%>');"
                                           class="libk-deepblue popup-link" data-wh="450*250">作废</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">作废</a>
                                        <%
                                            }
                                        %>

                                        <%if (loan.F43 == T6231_F29.S) { %>
                                        <%if (dimengSession.isAccessableResource(BidQXTJ.class)) { %>
                                        <a href="javascript:void(0);"
                                           onclick="showQXTJ('<%=loan.F01 %>','<%=loan.F25 %>');" class="libk-deepblue">取消推荐</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">取消推荐</a>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(BidTJ.class) && (loan.F20 == T6230_F20.YFB || loan.F20 == T6230_F20.TBZ)) { %>
                                        <a href="javascript:void(0);" onclick="showTJ('<%=loan.F01%>','<%=loan.F25%>');"
                                           class="link-blue">推荐</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">推荐</a>
                                        <%} %>
                                        <%} %>

                                        <%
                                            if (dimengSession.isAccessableResource(ViewBidProgres.class) && (loan.F20 == T6230_F20.HKZ || loan.F20 == T6230_F20.YJQ || loan.F20 == T6230_F20.YDF || loan.F20 == T6230_F20.DFZ)) {
                                        %>
                                            <a href="<%=controller.getURI(request, ViewBidProgres.class)%>?loanId=<%=loan.F01%>"
                                           class="libk-deepblue">动态管理</a>
                                        <%
                                        } else {
                                        %>
                                            <a href="javascript:void(0)" class="disabled">动态管理</a>
                                        <%
                                            }
                                        %>

                                        <%-- 									<%if(loan.F20==T6230_F20.HKZ || loan.F20==T6230_F20.YJQ|| loan.F20==T6230_F20.YDF){ %>
                                                                                    <span>合同</span>
                                                                                <%} %> --%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="13">暂无数据</td>
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
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
            <div class="popup_bg hide"></div>
        </div>
    </div>

<div class="popup-box hide" id="yfbDiv" style="min-height: 200px;">
    <form action="<%=controller.getURI(request, PreRelease.class) %>" method="post" class="form1">
        <input type="hidden" name="loanId" id="loanId"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">预发布</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix" style="max-height:50px;">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em
                            class="gray3">发布时间：</em></span>
                        <input type="text" class="text border pl5 date required" readonly="readonly" id="datepicker3"
                               name="releaseTime"/>
                        <span tip></span>
                        <span errortip class="red" style="display: none"></span></li>

                </ul>
            </div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" id="cancelDivId" style="min-height: 200px;">
    <form action="<%=controller.getURI(request, BidZf.class) %>" id="shForm" method="post" class="form1">
        <input type="hidden" name="loanId" id="cancelLoanId"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">作废</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix">
            <div>
                <ul class="gray6">
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em class="gray3">标的作废描述（50字以内）：</em></span>
                        <textarea name="reason" id="textarea2" rows="3"
                                  class="required ww100 border max-length-50"></textarea>
                        <span tip class="red"></span>
                        <span errortip class="red" style="display: none"></span></li>
                </ul>
            </div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" id="tjDivId">
    <form action="<%=controller.getURI(request, BidTJ.class) %>" id="shForm" method="post" class="form1">
        <input type="hidden" name="loanId" id="tjLoanId"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">是否将标的（<span id="tjShowId"></span>）设为推荐标，设置后原推荐标将不在推荐位置展示</span></div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" id="qxtjDivId">
    <form action="<%=controller.getURI(request, BidQXTJ.class) %>" id="shForm" method="post" class="form1">
        <input type="hidden" name="loanId" id="qxtjLoanId"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">是否取消推荐标的（<span id="qxtjShowId"></span>）</span></div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>
<%if (!StringHelper.isEmpty(errorMessage)) {%>
<div class="popup-box" id="errorInfo">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
    <div class="popup-content-container pb20 clearfix">
        <div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                class="f20 h30 va-middle ml10"><%=errorMessage %></span></div>
        <div class="tc f16">
            <a class="btn btn-blue2 radius-6 pl20 pr20" onclick="closeInfo()">确定</a>
        </div>
    </div>
</div>
<%} %>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
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
<%} %>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-yes-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, infoMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var errorMsg = <%controller.getPrompt(request,response,PromptLevel.ERROR);%>
            $(function () {
                if (errorMsg != "") {
                    showDialogInfo(errorMsg, "d_error")
                }
                $("[name='userType']").val('<%=request.getParameter("userType")%>');
                $("[name='bidFlag']").val('<%=request.getParameter("bidFlag")%>');
                $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
                $('#datepicker1').datepicker({
                    inline: true,
                    onSelect: function (selectedDate) {
                        $("#datepicker2").datepicker("option", "minDate", selectedDate);
                    }
                });
                $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
                $('#datepicker2').datepicker({inline: true});
                <%if(!StringHelper.isEmpty(createTimeStart)){%>
                $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
                <%}%>
                <%if(!StringHelper.isEmpty(createTimeEnd)){%>
                $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
                <%}%>
                $('#datepicker3').datetimepicker({
                    timeFormat: 'HH:mm:ss',
                    dateFormat: 'yy-mm-dd'
                });
                $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
                //$("#datepicker3").datepicker({inline: true});
                // $('#datepicker3').datepicker('option', {dateFormat:'yy-mm-dd'});
            });
    function showYfbDiv(loanId) {
    	$("#datepicker3").val("");
        $(".popup_bg").show();
        $("#loanId").attr("value", loanId);
        $("#yfbDiv").show();
    }
    var _url = "";
    function fbBid(hideLoanId, url) {
        _url = url;
        $(".popup_bg").show();
        $("#info").html(showConfirmDiv("您需要立即发布“" + $("#" + hideLoanId).attr("title") + "”标？", 0, "fb"));
    }

    function toConfirm(msg, i, type) {
        location.href = _url;
    }

    function showZf(loanId) {
        $(".popup_bg").show();
        $("#cancelLoanId").attr("value", loanId);
        $("#textarea2").val("");
        $("#cancelDivId").show();
    }

    function showTJ(loanId, bidBH) {
        $(".popup_bg").show();
        $("#tjLoanId").attr("value", loanId);
        $("#tjShowId").text(bidBH);
        $("#tjDivId").show();
    }

    function showQXTJ(loanId, bidBH) {
        $(".popup_bg").show();
        $("#qxtjLoanId").attr("value", loanId);
        $("#qxtjShowId").text(bidBH);
        $("#qxtjDivId").show();
    }

    function search() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }
</script>
</body>
</html>