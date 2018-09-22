<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    
</head>
<body>
<%
	CURRENT_CATEGORY = "CWGL";
	boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->

                <!--用户详细信息-->
                <div class="p20">
                    <div class="user-container border p20 pl30 pr30 gray6">
                        <ul class="lh24">
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">资金管理</h3>
                                <p class="">资金管理主要是对充值、提现、项目放款、不良资产处理、商城退款、平台调账、用户信用、用户担保额度进行管理、统计。</p>
                            </li>
                            <% if(tg){ %>
                                <% if(escrowFinance.equals("yeepay")){ %>
	                              <li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">托管账户处理</h3>
	                                <p class="">可通过开户信息同步及银行卡信息同步对用户进行第三方相关信息的同步操作，对发生异常的转账请求（投标、债权转让、还款）做确认或是取消转账操作，对已注册易宝托管的用户进行资金冻结解冻操作。</p>
	                              </li>
                            	  <li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">对账管理</h3>
	                                <p class="">可对易宝处理成功，但是平台处理不成功的充值、提现、投标、债权转让、公益投标、商品订单等请求做查询及对账操作，通过账户信息查询查询用户第三方资金账户信息，也通过自助转账进行平台-用户进行直接的资金流转。</p>
	                              </li>
	                            <%} else if("shuangqian".equals(escrowFinance)){%>
	                              <li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">对账管理</h3>
	                                <p class="">可对双乾处理成功，但是平台处理不成功的充值、提现、投资、还款、垫付、债权转让、公益标投资、商城订单、不良债权购买等请求做对账操作，也可查询用户余额、商户信息。</p>
	                              </li>
	                            <%}else if("baofu".equals(escrowFinance)){%>
                                    <li class="pb10 pt20 border-b-s">
                                        <h3 class="f18">对账管理</h3>
                                        <p class="">可对宝付处理成功，但是平台处理不成功的充值、提现等请求做对账操作，也可查询用户余额、平台信息。</p>
                                    </li>
                                <%}else if("huifu".equals(escrowFinance)){ %>
                                	<li class="pb10 pt20 border-b-s">
                                        <h3 class="f18">对账管理</h3>
                                        <p class="">可对汇付处理成功，但是平台处理不成功的充值、提现、投标、债权转让等请求做对账操作，也可查询用户余额、平台信息。</p>
                                    </li>
                                <%} %>
                            <%} %>
                            <% if(!tg){ %>
                             <li class="pb10 pt20 border-b-s">
                               <h3 class="f18">资金明细</h3>
                               <p class="">资金明细主要是对个人、企业、机构、平台资金明细进行管理、统计，可查看所有的交易记录。</p>
                             </li>
							<% } %>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">订单管理</h3>
                                <p class="">订单管理主要是对生成的订单进行管理、统计，可查看所有的订单记录以及异常订单信息。</p>
                            </li>
                            <%if("FUYOU".equals(escrowFinance)){ %>
	                            <li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">托管账户管理</h3>
	                                <p class="">托管账户管理主要是对个人、企业、机构的资金账户进行管理，可查看所有的冻结及解冻订单信息。</p>
	                            </li>
                            <%} else if("baofu".equals(escrowFinance)){ %>
	                            <li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">托管账户管理</h3>
	                                <p class="">托管账户管理主要是对银行卡信息进行同步，可查看所有的银行卡状态。</p>
	                            </li>
                            <%} else if("huifu".equals(escrowFinance)){ %>
                            	<li class="pb10 pt20 border-b-s">
	                                <h3 class="f18">托管账户管理</h3>
	                                <p class="">托管账户管理主要是对开户、银行卡信息进行同步，以及商户账户信息的查询。</p>
	                            </li>
                            <%} %>
                        </ul>
                    </div>
                </div>

                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<!--内容-->
</body>
</html>