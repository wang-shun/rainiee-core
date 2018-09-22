<%@page import="com.dimeng.p2p.modules.bid.user.service.ZqzrManage"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.SellFinacingInfo"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Zqkzc"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Zqyzc"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Zqyzr"%>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Zqzrz"%>
<%
	final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
  	ZqzrManage service=serviceSession.getService(ZqzrManage.class);
	Paging paging=new Paging(){
		public int getCurrentPage(){
			return currentPage;
		}
		
		public int getSize(){
			return 10;
		}
	};
%>

<%
  	SellFinacingInfo bfInfo=service.getSellFinacingInfo();
   	if(bfInfo != null){
%>
<div class="debt_total clearfix">
	<div class="total fl mt50 pt10">
        债权转让盈亏(元)
        <span class="hover_tips">
            <div class="hover_tips_con">
                <div class="arrow"></div>
                <div class="border">
                    债权转让盈亏=债权折溢价交易盈亏-债权交易费用
                </div>
            </div>
        </span>
        <br />
        <span class="orange f22"><%=bfInfo.money %></span>
    </div>
    <div class="line"></div>
    <div class="list">
    	<ul>
            <li>成功转入金额(元)<br /><%=bfInfo.inMoney %></li>
            <li>债权转入盈亏(元)<br /><%=bfInfo.inAssetsMoney %></li>
            <li>已转入债权笔数(笔)<br /><%=bfInfo.inNum %></li>
            <li>成功转出金额(元)<br /><%=bfInfo.outMoney %></li>
            <li>债权转出盈亏(元)<br /><%=bfInfo.outAssetsMoney %></li>
            <li>已转出债权笔数(笔)<br /><%=bfInfo.outNum %></li>
        </ul>
    </div>
</div>
<%}else{%>
<div class="debt_total clearfix">
	<div class="total fl mt50 pt10">
        债权转让盈亏(元)
        <span class="hover_tips">
            <div class="hover_tips_con">
                <div class="arrow"></div>
                <div class="border">
                    债权转让盈亏=债权折溢价交易盈亏-债权交易费用
                </div>
            </div>
        </span>
        <br />
        <span class="orange f22">0.00</span>
    </div>
    <div class="line"></div>
    <div class="list">
    	<ul>
            <li>成功转入金额(元)<br />0.00</li>
            <li>债权转入盈亏(元)<br />0.00</li>
            <li>已转入债权笔数(笔)<br />0</li>
            <li>成功转出金额(元)<br />0.00</li>
            <li>债权转出盈亏(元)<br />0.00</li>
            <li>已转出债权笔数(笔)<br />0</li>
        </ul>
    </div>
</div>
<%}%>
