<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.total.ExportFunds"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.IndexCount"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.MoneyStatisticalEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
    <%
        IndexCount indexCount = ObjectHelper.convert(request.getAttribute("indexCount"), IndexCount.class);
        MoneyStatisticalEntity moneyStatistical = ObjectHelper.convert(request.getAttribute("moneyStatistical"), MoneyStatisticalEntity.class);
    %>

</head>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/echarts/echarts.js"></script>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "ZJTJZL";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20"> 
  <!--资金统计总览-->
  <div class="border">
    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>资金统计总览
	    <div class="fr">
	    <span><%if (dimengSession.isAccessableResource(ExportFunds.class)) {%>
	                        <a href="javascript:void(0);" onclick="showExport();"
	                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
	                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
	                   <%} else {%>
	                     	<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
	                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
	                   <%}%>
	                 </span>
	                            <form id="form_loan" action="<%=controller.getURI(request, ExportFunds.class)%>"
	                                  method="post">
	                                <input type="hidden" name="platformTotalIncome"
	                                       value="<%=Formater.formatAmount(indexCount.platformTotalIncome)%>">
	                            </form>
		</div>	                            
    </div>
    <div class="content-container pl40 pt10 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">用户账户资金统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30">
        <ul class="flat-line-ul clearfix lh30">
          <li class="ww25 fl">
            <div class="item-container border-r-s pt15 pb15 tc">
              <p class="gray6">账户余额总额(元)
                <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                  <span class="prompt-container prompt-container2" style="display:none;">
                    <i class="icon-i prompt-icon"></i>
                    <span class="display-b w350 lh20 tl">统计所有账户余额总额，包含个人、企业、机构；账户余额=可用金额+冻结金额+风险保证金</span>
                  </span>
                </i>
              </p>
              <h2 class="f24 gray3"><%=Formater.formatAmount(moneyStatistical.getAccountBalance())%></h2>
            </div>
          </li>
          <li class="ww25 fl">
            <div class="item-container border-r-s pt15 pb15 tc">
              <p class="gray6">可用余额总额(元)
                <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                  <span class="prompt-container prompt-container2" style="display:none;">
                    <i class="icon-i prompt-icon"></i>
                    <span class="display-b w200 lh20 tl">统计所有可用余额总额，包含个人、企业、机构</span>
                  </span>
                </i>
              </p>
              <h2 class="f24 gray3"><%=Formater.formatAmount(moneyStatistical.getUsableBalance())%></h2>
            </div>
          </li>
          <li class="ww25 fl">
            <div class="item-container border-r-s pt15 pb15 tc">
              <%
              boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                if(!isHasGuarant){
              %>
              <p class="gray6">机构风险保证金(元)
                <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                  <span class="prompt-container prompt-container2" style="display:none;">
                    <i class="icon-i prompt-icon"></i>
                    <span class="display-b w200 lh20 tl">统计所有机构账户风险保证金总额</span>
                  </span>
                </i>
              </p>
              <%}else{%>
              <p class="gray6">风险保证金(元)
                <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                  <span class="prompt-container prompt-container2" style="display:none;">
                    <i class="icon-i prompt-icon"></i>
                    <span class="display-b w200 lh20 tl">统计担保方风险保证金账户总额</span>
                  </span>
                </i>
              </p>
              <%}%>
              <h2 class="f24 gray3"><%=Formater.formatAmount(moneyStatistical.getMargin())%></h2>
            </div>
          </li>
          <li class="ww25 fl">
            <div class="item-container border-r-s pt15 pb15 tc">
              <p class="gray6">冻结金额总额(元)
                <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                  <span class="prompt-container3" style="display:none;">
                    <i class="icon-i prompt-icon"></i>
                    <span class="display-b w200 lh20 tl">统计所有冻结金额总额，包含个人、企业、机构</span>
                  </span>
                </i>
              </p>
              <h2 class="f24 gray3"><%=Formater.formatAmount(moneyStatistical.getAmountFrozen())%></h2>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
  <!--充值/提现资金统计-->
  <div class="border mt15">
    <div class="content-container pt20 pl40 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">充值/提现资金统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30 pr mh250">
        <div class="pl500  va-middle">
        <div class="w500 pa left0 top30 bottom0">
        <ul class="gray6 show-list-container clearfix lh24 pt20">
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#5ab530;"></span>
                <p class="h30">用户充值手续费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">统计非平台账户通过第三方支付接口充值成功时收取的手续费总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getCzsxf())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#ed767d;"></span>
                <p class="h30">用户充值总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">用户充值总额=线上充值总额+线下充值总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getOnlinePay().add(moneyStatistical.getOfflinePay()))%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#40bfeb;"></span>
                <p class="h30">线上充值总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计非平台账户通过第三支付接口充值成功的总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getOnlinePay())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#b7d445;"></span>
                <p class="h30">线下充值总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计通过平台账户成功转账到非平台账户的总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getOfflinePay())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f0851a;"></span>
                <p class="h30">今日用户总充值(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计今日线上总充值</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getTodayCharge())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#2096bc;"></span>
                <p class="h30">今日用户总提现(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计非平台账户今日提现成功的总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getTodayWithdraw())%></p>
                <%-- <p>
                <%
                                                        if (dimengSession.isAccessableResource(Txcg.class)) {
                                                            String url = controller.getURI(request, Txcg.class)
                                                                    + "?startExtractonTime=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                                                                    + "&endExtractionTime=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
               %>
                                                    <a href="<%=url%>"><%=Formater.formatAmount(moneyStatistical.getTodayWithdraw())%>
                                                    </a> <%
                                                } else {%>
                                                    <a href="javascript:void(0)"
                                                       class="disabled"><%=Formater.formatAmount(moneyStatistical.getTodayWithdraw())%>
                                                    </a>
               <%}%>
               </p> --%>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#68c2ac;"></span>
                <p class="h30">用户提现总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计非平台账户提现成功的总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getYhtxze())%></p>
                <%-- <p>
                <%if (dimengSession.isAccessableResource(Txcg.class)) {%>
                                                    <a href="<%=controller.getURI(request, Txcg.class)%>"><%=Formater.formatAmount(moneyStatistical.getYhtxze())%>
                                                    </a>
                                                    <%} else { %>
                                                    <a href="javascript:void(0)"
                                                       class="disabled"><%=Formater.formatAmount(moneyStatistical.getYhtxze())%>
                                                    </a>
                 <%}%>
                 </p> --%>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#217d93;"></span>
                <p class="h30">用户提现手续费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计非平台账户提现成功时收取的提现手续费总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getTxsxf())%></p>
              </div>
            </li>
          </ul>
        </div>
          <div class="ww100 h300" id="charge_withdraw_statistics"></div>
        </div>
      </div>
    </div>
  </div>
  
  <!--平台账户资金统计-->
  <div class="border mt15">
    <div class="content-container pt20 pl40 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">平台账户资金统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30 pr mh250">
        <div class="pl500 va-middle">
        <div class="w500 pa left0 top30 bottom0">
        <ul class="gray6 show-list-container clearfix lh24 pt20">
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f0851a;"></span>
                <p class="h30">平台总收益(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">平台总收益=平台总收入-平台总支出</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(indexCount.platformTotalIncome)%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#217d93;"></span>
                <p class="h30">理财管理费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有理财管理费总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getLcglf())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#ed767d;"></span>
                <p class="h30">违约金手续费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有违约金手续费在总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getWyjsxf())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#40bfeb;"></span>
                <p class="h30">成交服务费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有成交服务费金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getCjfwf())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#b7d445;"></span>
                <p class="h30">债权转让手续费总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有债权转让手续费总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getZqzrsxf())%></p>
              </div>
            </li>
          </ul>
        </div>
          <div class="ww100 h300" id="platform_account_statistics"></div>
        </div>
      </div>
    </div>
  </div>
  
  <!--投资/借款资金统计-->
  <div class="border mt15">
    <div class="content-container pt20 pl40 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">投资/借款资金统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30 pr mh250">
        <div class="pl500  va-middle">
        <div class="w500 pa left0 top30 bottom0">
        <ul class="gray6 show-list-container clearfix lh24 pt20">
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f0851a;"></span>
                <p class="h30">累计投资总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">累计投资总额 =个人账户+企业+机构账户投资总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getLjtzje())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#5ac3df;"></span>
                <p class="h30">累计投资总收益(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">累计投资总收益=个人账户+企业账户+机构账号投资总收益</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getLjtzzsy())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#ed767d;"></span>
                <p class="h30">散标投资总收益(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">散标投资总收益=个人账户+企业+机构账户散标投资总收益</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getSbtzzsy())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f3dd23;"></span>
                <p class="h30">债权转让盈亏总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">债权转让盈亏总额 = 个人账户+企业+机构账户债权转让盈亏总额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getZqzrykze())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#94d2d7;"></span>
                <p class="h30">借款已还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有借款已还总额，包括正常还款、提前还款、逾期还款、垫付后再还款的总金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getJkyhzje())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#35a5c1;"></span>
                <p class="h30">借款正常还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有正常还款的金额，即按时还款的金额，包含本金、利息等金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getJkzchkz())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f7bc67;"></span>
                <p class="h30">借款未还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有借款未还款的金额，包括正常未还、逾期未还、垫付后未还的金额</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getJkwhk())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#5ab530;"></span>
                <p class="h30">借款逾期未还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w200 lh20 tl">统计所有借款逾期未还的金额，即统计逾期当期的未还的本金、利息、罚息</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getJkyqwh())%></p>
              </div>
            </li>
          </ul>
        </div>
          <div class="ww100 h300" id="invest_loan_statistics"></div>
        </div>
      </div>
    </div>
  </div>
  
  <!--机构垫付资金统计-->
  <div class="border mt15">
    <div class="content-container pt20 pl40 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">垫付资金统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30 pr mh250">
        <div class="pl500  va-middle">
        <div class="w500 pa left0 top30 bottom0">
        <ul class="gray6 show-list-container clearfix lh24 pt20">
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#f0851a;"></span>
                <p class="h30">逾期垫付总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">统计逾期后垫付的总金额，包含本金、利息、罚息</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getYqjgdf())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#35a5c1;"></span>
                <p class="h30">逾期垫付未还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">统计逾期垫付后，借款者未还给垫付方的总金额，包含本金、利息、罚息</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getYqjgdfwh())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#88c248;"></span>
                <p class="h30">逾期垫付已还款总额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">统计逾期垫付后，借款者已还给垫付方的总金额，包含本金、利息、罚息</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getYqjgdfyh())%></p>
              </div>
            </li>
          </ul>
        </div>
          <div class="ww100 h300" id="organ_statistics"></div>
        </div>
      </div>
    </div>
  </div>
  
  <!--不良资产处理统计-->
  <div class="border mt15">
    <div class="content-container pt20 pl40 pr40">
      <div class="two-title-container h30 lh30">
        <h3 class="f16">不良资产处理统计</h3>
      </div>
      <div class="flat-line-container pt30 pb30 pr mh250">
        <div class="pl500  va-middle">
        <div class="w500 pa left0 top30 bottom0">
        <ul class="gray6 show-list-container clearfix lh24 pt20">
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#5ac3df;"></span>
                <p class="h30">待转让债权价值总金额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">不良资产待转让债权价值总金额（债权价值= 借款标本金 + 利息 + 罚息）</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getDzrzqze())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#5ab530;"></span>
                <p class="h30">转让中债权价值总金额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">不良资产转让中债权价值总金额（债权价值= 借款标本金 + 利息 + 罚息）</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getZrzzqze())%></p>
              </div>
            </li>
            <li>
              <div class="pl20 pr w230"><span class="by-cor pa w10 left0 top0 bottom0" style=" background:#ed767d;"></span>
                <p class="h30">已转让债权价值总金额(元)
                  <i class="icon-i w30 h30 va-middle gantanhao-icon pr">
                    <span class="prompt-container prompt-container2" style="display:none;">
                      <i class="icon-i prompt-icon"></i>
                      <span class="display-b w300 lh20 tl">不良资产已转让债权价值总金额（债权价值= 借款标本金 + 利息 + 罚息）</span>
                    </span>
                  </i>
                </p>
                <p><%=Formater.formatAmount(moneyStatistical.getYzrzqze())%></p>
              </div>
            </li>
          </ul>
        </div>
          <div class="ww100 h300" id="platform_statistics"></div>
        </div>
      </div>
    </div>
  </div>
  
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</div>
<script type="text/javascript">
    
    require.config({
        paths: {
            echarts: '<%=controller.getStaticPath(request)%>/js/echarts'
        }
    });
    
    require(
            [
                'echarts',
                'echarts/chart/bar'
            ],
            function ctChart(ec) {//充值/提现资金统计
                //图表渲染的容器对象
                var chargeWithdrawStatistics = document.getElementById("charge_withdraw_statistics");
                //加载图表
                var chargeWithdrawStatisticsChart = ec.init(chargeWithdrawStatistics);
                chargeWithdrawStatisticsChart.setOption(ctOption);
            }
    );
    
    //平台账户资金统计
    require(
            [
                'echarts',
                'echarts/chart/pie'
            ],
            function ptChart(ec) {
                //图表渲染的容器对象
                var platformAccountStatistics = document.getElementById("platform_account_statistics");
                //加载图表
                var platformAccountStatisticsChart = ec.init(platformAccountStatistics);
                platformAccountStatisticsChart.setOption(ptOption);
            }
    );
    
    //投资/借款资金统计
    require(
            [
                'echarts',
                'echarts/chart/pie'
            ],
            function ptChart(ec) {
                //图表渲染的容器对象
                var investLoanStatistics = document.getElementById("invest_loan_statistics");
                //加载图表
                var investLoanStatisticsChart = ec.init(investLoanStatistics);
                investLoanStatisticsChart.setOption(tzOption);
            }
    );

    //机构垫付资金统计
    require(
            [
                'echarts',
                'echarts/chart/pie'
            ],
            function ptChart(ec) {
                //图表渲染的容器对象
                var organPlatformStatistics = document.getElementById("organ_statistics");
                //加载图表
                var organPlatformStatisticsChart = ec.init(organPlatformStatistics);
                organPlatformStatisticsChart.setOption(jpsOption);
            }
    );
    
    //不良资产资金统计
    require(
            [
                'echarts',
                'echarts/chart/pie'
            ],
            function ptChart(ec) {
                //图表渲染的容器对象
                var platformStatistics = document.getElementById("platform_statistics");
                //加载图表
                var platformStatisticsChart = ec.init(platformStatistics);
                platformStatisticsChart.setOption(blzcOption);
            }
    );
    
    //充值/提现资金统计数据
    var ctOption = {
        calculable: true,
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: '12'
            },
            formatter: function (a, b, c) {
                return a[0].name + ": " + fmoney(a[0].value, 2) + "元";
            }
        },
        xAxis: [{
            type:'category',
            axisLabel :{
            	formatter:'{value}',
            	interval:0,
            	rotate:25
            },
        	data:['用户充值手续费总额','用户充值总额','线上充值总额','线下充值总额','今日用户总充值','今日用户总提现','用户提现总额','用户提现手续费总额']
        }],
        grid: { // 控制图的大小，调整下面这些值就可以，
            x: 100,
            x2: 70,
            y2: 70// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: [{
        	name:'',
        	type:'value',
        	splitArea:{
        		show:true
            },
            axisLabel :{
            	formatter: function(value) {
                	return value/10000 +'万';
                }
            }
        }],
        series:[{
        	type:'bar',
        	data:[
        	    <%=moneyStatistical.getCzsxf()%>,
        	    <%=moneyStatistical.getOnlinePay().add(moneyStatistical.getOfflinePay())%>,
        	    <%=moneyStatistical.getOnlinePay()%>,
        	    <%=moneyStatistical.getOfflinePay()%>,
        	    <%=moneyStatistical.getTodayCharge()%>,
        	    <%=moneyStatistical.getTodayWithdraw()%>,
        	    <%=moneyStatistical.getYhtxze()%>,
        	    <%=moneyStatistical.getTxsxf()%>
        	],
        	 itemStyle: {
                 normal: {
                     color: function(params) {
                         var colorList = ['#5ab530', '#ed767d', '#40bfeb', '#b7d445', '#f0851a', '#2096bc', '#68c2ac', '#217d93'];
                         return colorList[params.dataIndex];
                     }
                 }
             }
        }] 
    };
    
    //平台账户资金统计
    var ptOption = {
        calculable: true,
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: '12'
            },
            formatter: function (a, b, c) {
                return a[0].name + ": " + fmoney(a[0].value, 2) + "元";
            }
        },
        xAxis: [{
            type:'category',
            axisLabel :{
            	formatter:'{value}',
            	interval:0,
            	rotate:25
            },
        	data:['平台总收益','理财管理费总额','违约金手续费总额','成交服务费总额','债权转让手续费总额']
        }],
        grid: { // 控制图的大小，调整下面这些值就可以，
            x: 100,
            x2: 70,
            y2: 70// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: [{
        	name:'',
        	type:'value',
        	splitArea:{
        		show:true
            },
            axisLabel :{
            	formatter: function(value) {
                	return value/10000 +'万';
                }
            }
        }],
        series:[{
        	type:'bar',
        	data:[
        	    <%=indexCount.platformTotalIncome%>,
        	    <%=moneyStatistical.getLcglf()%>,
        	    <%=moneyStatistical.getWyjsxf()%>,
        	    <%=moneyStatistical.getCjfwf()%>,
        	    <%=moneyStatistical.getZqzrsxf()%>
        	],
        	 itemStyle: {
                 normal: {
                     color: function(params) {
                         var colorList = ['#f0851a', '#217d93', '#ed767d', '#40bfeb', '#b7d445'];
                         return colorList[params.dataIndex];
                     }
                 }
             }
        }] 
    };
    
    //投资/借款资金统计
    var tzOption = {
        calculable: true,
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: '12'
            },
            formatter: function (a, b, c) {
                return a[0].name + ": " + fmoney(a[0].value, 2) + "元";
            }
        },
        xAxis: [{
            type:'category',
            axisLabel :{
            	formatter:'{value}',
            	interval:0,
            	rotate:25
            },
        	data:['累计投资总额','累计投资总收益','散标投资总收益','债权转让盈亏总额','借款已还款总额','借款正常还款总额','借款未还款总额','借款逾期未还款总额']
        }],
        grid: { // 控制图的大小，调整下面这些值就可以，
            x: 100,
            x2: 70,
            y2: 70// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: [{
        	name:'',
        	type:'value',
        	splitArea:{
        		show:true
            },
            axisLabel :{
            	formatter: function(value) {
                	return value/10000 +'万';
                }
            }
        }],
        series:[{
        	type:'bar',
        	data:[
        	    <%=moneyStatistical.getLjtzje()%>,
        	    <%=moneyStatistical.getLjtzzsy()%>,
        	    <%=moneyStatistical.getSbtzzsy()%>,
        	    <%=moneyStatistical.getZqzrykze()%>,
        	    <%=moneyStatistical.getJkyhzje()%>,
        	    <%=moneyStatistical.getJkzchkz()%>,
        	    <%=moneyStatistical.getJkwhk()%>,
        	    <%=moneyStatistical.getJkyqwh()%>
        	],
        	 itemStyle: {
                 normal: {
                     color: function(params) {
                         var colorList = ['#f0851a', '#5ac3df', '#ed767d', '#f3dd23', '#94d2d7', '#35a5c1', '#f7bc67', '#5ab530'];
                         return colorList[params.dataIndex];
                     }
                 }
             }
        }] 
    };
    
    //机构垫付资金统计
    var jpsOption = {
        calculable: true,
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: '12'
            },
            formatter: function (a, b, c) {
                return a[0].name + ": " + fmoney(a[0].value, 2) + "元";
            }
        },
        xAxis: [{
            type:'category',
            axisLabel :{
            	formatter:'{value}',
            	interval:0,
            	rotate:25
            },
        	data:['逾期垫付总额','逾期垫付未还款总额','逾期垫付已还款总额']
        }],
        grid: { // 控制图的大小，调整下面这些值就可以，
            x: 100,
            x2: 70,
            y2: 80// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: [{
        	name:'',
        	type:'value',
        	splitArea:{
        		show:true
            },
            axisLabel :{
            	formatter: function(value) {
                	return value/10000 +'万';
                }
            }
        }],
        series:[{
        	type:'bar',
        	data:[
        	    <%=moneyStatistical.getYqjgdf()%>,
        	    <%=moneyStatistical.getYqjgdf().subtract(moneyStatistical.getYqjgdfyh())%>,
        	    <%=moneyStatistical.getYqjgdfyh()%>
        	],
        	 itemStyle: {
                 normal: {
                     color: function(params) {
                         var colorList = ['#f0851a', '#35a5c1', '#88c248'];
                         return colorList[params.dataIndex];
                     }
                 }
             }
        }] 
    };
    
    //不良资产处理资金统计
    var blzcOption = {
        calculable: true,
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: '12'
            },
            formatter: function (a, b, c) {
                return a[0].name + ": " + fmoney(a[0].value, 2) + "元";
            }
        },
        xAxis: [{
            type:'category',
            axisLabel :{
            	formatter:'{value}',
            	interval:0,
            	rotate:25
            },
        	data:['待转让债权价值总金额','转让中债权价值总金额','已转让债权价值总金额']
        }],
        grid: { // 控制图的大小，调整下面这些值就可以，
            x: 100,
            x2: 70,
            y2: 80// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: [{
        	name:'',
        	type:'value',
        	splitArea:{
        		show:true
            },
            axisLabel :{
            	formatter: function(value) {
                	return value/10000 +'万';
                }
            }
        }],
        series:[{
        	type:'bar',
        	data:[
        	    <%=moneyStatistical.getDzrzqze()%>,
        	    <%=moneyStatistical.getZrzzqze()%>,
        	    <%=moneyStatistical.getYzrzqze()%>
        	],
        	 itemStyle: {
                 normal: {
                     color: function(params) {
                         var colorList = ['#5ac3df', '#5ab530', '#ed767d'];
                         return colorList[params.dataIndex];
                     }
                 }
             }
        }] 
    };
  
    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportFunds.class)%>";
        $("#form_loan").submit();
    }

    function fmoney(s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        var f = s < 0 ? "-" : ""; //判断是否为负数
        s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
        var l = s.split(".")[0].split("").reverse(),
                r = s.split(".")[1];
        var t = "";
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return f + t.split("").reverse().join("") + "." + r.substring(0, 2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
    }
    
    $(function () {
       $(".gantanhao-icon").mouseover(function () {
    	   $(this).children(":first").show();
        }).mouseleave(function () {
        	$(this).children(":first").hide();
        });
    }); 
</script>
</body>
</html>