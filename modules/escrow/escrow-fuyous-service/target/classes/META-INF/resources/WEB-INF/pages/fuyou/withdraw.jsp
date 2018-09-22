<%@page import="com.dimeng.p2p.escrow.fuyou.service.BankManage"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.S61.entities.T6101"%>
<%@page import="com.dimeng.p2p.account.user.service.TxManage"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.BankCard"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page import="com.dimeng.p2p.user.servlets.account.Addbankcard"%>
<%@page import="com.dimeng.p2p.user.servlets.capital.Withdraw"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.bouncycastle.util.encoders.Hex"%>
<%@page import="com.dimeng.p2p.service.PtAccountManage"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey"%>
<%@ page import="com.dimeng.p2p.user.servlets.account.UserBases"%>
<%@ page import="com.dimeng.p2p.user.servlets.account.SafetymsgFZRR"%>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<%@page import="com.dimeng.p2p.S50.entities.T5023"%>
<%@page import="com.dimeng.p2p.S50.enums.T5023_F02"%>
<%@ page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的<%
    configureProvider.format(out, SystemVariable.SITE_NAME);
%>_提现
</title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<%
	    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
	    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
	    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
	    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	    TxManage manage = serviceSession.getService(TxManage.class);

	    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
	    T6101 t6101 = userInfoManage.search();
	    T6110 t6110_tmp = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());

	    BankCard[] cards = manage.bankCards();
	    int cardId = IntegerParser.parse(request.getAttribute("cardId"));
	    UserManage userManage = serviceSession.getService(UserManage.class);
	    String usrCustId = userManage.getUsrCustId();
	    boolean isOpenPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
		String escrow_prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	    boolean flag = false;
	    boolean bankNo = false;
		if (cards == null && !StringHelper.isEmpty(usrCustId)){
		    flag = true;
		    if (t6110_tmp.F06 == T6110_F06.ZRR) {
		        BankManage bankManage = serviceSession.getService(BankManage.class);
			    bankNo = bankManage.selectT6114Ext();
		    }
		}

	    CURRENT_CATEGORY = "ZJGL";
	    CURRENT_SUB_CATEGORY = "TXGL";

	    Map bankMap = new HashMap();
	    bankMap.put("中国银行", "BOC");
	    bankMap.put("中国工商银行", "ICBC");
	    bankMap.put("中国农业银行", "ABC");
	    bankMap.put("中国建设银行", "CCB");
	    bankMap.put("招商银行", "CMB");

	    bankMap.put("交通银行", "BOCOM");
	    /* bankMap.put("中国邮政储蓄银行", "PSBC"); */
	    //富友的邮政银行起名 国家邮政局邮政储汇局
	    bankMap.put("国家邮政局邮政储汇局", "PSBC");
	    bankMap.put("中国民生银行", "CMBC");
	    bankMap.put("中国光大银行", "CEB");
	    bankMap.put("平安银行", "PINGAN");
	    bankMap.put("上海浦东银行", "SPDB");

		boolean checkFlag=true;
		String rzUrl = "";
		String failReason = "";

		com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
		Map<String, String> retMap = userCommManage.checkAccountInfo();
		checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
		rzUrl = retMap.get("rzUrl");
		failReason = retMap.get("failReason");
		T5023 t5023 = manage.getT5023(T5023_F02.WITHDRAW);
	%>
	<div class="main_bg clearfix">
		<div class="user_wrap w1002 clearfix">
			<%@include file="/WEB-INF/include/menu.jsp"%>
			<form
				action="<%=configureProvider.format(URLVariable.ESCROW_URL_WITHDRAW)%>"
				autocomplete="off" method="post" onsubmit="return onSubmit();">
				<%=FormToken.hidden(serviceSession.getSession())%>
				<%
				    if (cardId > 0) {
				%>
				<input type="hidden" name="cardId" value="<%=cardId%>" />
				<%
				    } else {
				%>
				<input type="hidden" name="cardId"
					value="<%=(cards!=null&&cards.length>0)?cards[0].id:""%>" />
				<%
				    }
				%>
				<div class="r_main">
					<div class="user_mod">
						<%
						    if (!checkFlag && "thirdNull".equals(failReason)) {
						%>
						<div class="f16 mt30 pt50 pb50 tc ml30 mr30" style="background: #f3f3f3;">
							您需要在第三方托管平台上进行注册，才可申请充值提现！请
							<a href="<%=rzUrl%>" class="red">立即注册</a>！
						</div>
						<%
						    } else {
						%>
						<div class="lh30 clearfix">
							<h2 class="fl gray3 f16">提现银行卡</h2>
							<a href="<%=configureProvider.format(URLVariable.CARD_MANAGE)%>"
								class="fr ml20 highlight"><i class="gl_icon"></i><span
								class="va_m">管理银行卡</span></a>
						</div>
						<ul class="bank_manage_list clearfix">
							<%
							    if (cards != null && cards.length > 0) {
							                        int i = 0;
							                        for (BankCard card : cards) {
							                            if (card == null) {
							                                continue;
							                            }
							                            if (bankMap.containsKey(card.Bankname)) {
							%>
							<li><a href="javascript:void(0);"
								onclick="checkCard(this, <%=card.id%>)">
									<div class="pic">
										<img
											src="<%=controller.getStaticPath(request)%>/images/bank_logo_<%=bankMap.get(card.Bankname)%>.png"
											width="148" height="38">
									</div>
									<div class="number">
										<%
										    if (!StringHelper.isEmpty(card.BankNumber) && card.BankNumber.length() > 4) {
										                                StringHelper.filterHTML(out, card.BankNumber);
										                            }
										%>
									</div>
									<div class="delete clearfix">
										<%
										    if (cardId > 0) {
										                                if (cardId == card.id) {
										%>
										<i class="ico"></i>
										<%
										    }
										                        } else {
										                            if (i == 0) {
										%>
										<i class="ico"></i>
										<%
										    }
										                            }
										%>
									</div>
							</a></li>
							<%
							    } else {
							%>
							<li><a href="javascript:void(0);"
								onclick="checkCard(this, <%=card.id%>)">
									<div class="pic f16" style="width: 148px; height: 38px;">
										<%
										    StringHelper.filterQuoter(out, card.Bankname);
										%>
									</div>
									<div class="number">
										<%
										    if (!StringHelper.isEmpty(card.BankNumber) && card.BankNumber.length() > 4) {
										                                StringHelper.filterHTML(out, card.BankNumber);
										                            }
										%>
									</div>
									<div class="delete clearfix">
										<%
										    if (cardId > 0) {
										                                if (cardId == card.id) {
										%>
										<i class="ico"></i>
										<%
										    }
										                        } else {
										                            if (i == 0) {
										%>
										<i class="ico"></i>
										<%
										    }
										                            }
										%>
									</div>
							</a></li>
							<%
							    }
							                        i++;
							                    }
							                } else {
							%>
							<li class="add">
								<%
								    if (flag){ if(bankNo) {
								%> <a href="javascript:queryBank();">
									<i class="add_ico"></i>
									<p>更换银行卡审核中</p>
							</a> 
							<% } else { %> <a href="javascript:updateBank(<%=usrCustId%>);">
																	<i class="add_ico"></i>
																<p>刷新银行卡</p>
															</a> 
							<% }} else {%> 
								<a title="富友托管注册" href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE)%>">
									<i class="add_ico"></i>
									<p>立即绑卡</p>
							</a> <%
								     }
								 %>
							</li>
							<%
							    }
							%>
							<div class="clear"></div>
						</ul>
						<div class="gray3 f16 mt30">填写提现金额</div>
						<div class="user_mod_gray mt10 pt30 pb30">
							<div class="form_info topup_form">
								<ul class="cell">
									<li>
										<div class="til">可用金额(元)：</div>
										<div class="info">
											<span class="gray3 f18" id="aFunds"><%=Formater.formatAmount(t6101.F06)%></span>
										</div>
										<div class="clear"></div>
									</li>
									<li>
										<div class="til">
											<span class="red">*</span> 提现金额(元)：
										</div>
										<div class="info">
											<input type="text" name="hdnAmount" style="display: none;" />
											<input type="text" name="amount" autocomplete="off"
												onkeyup="return onlyNumber(this);" class="text yhgl_ser"
												value="<%=StringHelper.isEmpty(request.getParameter("amount"))?"":request.getParameter("amount")%>" />
											<span class="hover_tips">
												<div class="hover_tips_con" style="margin-left: -87px;">
													<div class="arrow"></div>
													<div class="border" style="width: 150px;">提现金额最多保留两位小数</div>
												</div>
											</span> <p tip></p>

											<p errortip class="red"></p>
										</div>
									</li>
									<li>
										<div class="til">提现手续费(元)：</div>
										<div class="info">
											<span class="gray3 f18 va_m" id="poundage">0.00</span> <span
												class="hover_tips">
												<div class="hover_tips_con tx_tips">
													<div class="arrow"></div>
													<div class="border">
														<p>第三方收取提现手续费</p>
														<%
														    String way = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY);
														                                                    String proportion = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION);
														                                                    if ("BL".equals(way)) {
														                                                        if (!StringHelper.isEmpty(proportion)) {
														                                                            float _proportion = Float.parseFloat(proportion);
														%>
														<table width="100%" border="0" cellspacing="0"
															class="mt5 mb5">
															<tr>
																<td>按提现金额的<%=_proportion * 100%>%收取
																</td>
															</tr>
														</table>
														<%
														    }
														                                                } else {
														%>
														<table width="100%" border="0" cellspacing="0"
															class="mt5 mb5">
															<tr>
																<td>5万以下</td>
																<td>5万（含）~20万以内</td>
															</tr>
															<tr>
																<td><%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5)%>
																	元/笔</td>
																<td><%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20)%>
																	元/笔</td>
															</tr>
														</table>
														<%
														    }
														%>
														<p>
															<span class="highlight">温馨提示：</span>单日提现金额超过500万元请提前三个工作日通知<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
															，以便我们尽快处理您的提现
														</p>
													</div>
												</div>
											</span> <span class="f12 gray9"> 提现费用将从您的<%
     if ("true".equals(configureProvider.getProperty(SystemVariable.TXSXF_KCFS))) {
 %>提现金额<%
     } else {
 %> <%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>账户余额<%
     }
 %>中扣除
											</span>
										</div>
									</li>
									<li>
										<div class="til">实际支付金额：</div>
										<div class="info">
											<span class="red f18" id="paySum">0.00</span>元
										</div>
									</li>
									<%-- <li>
                                    <!-- <div class="til">预计到账时间：</div>
                                    <div class="info">
                                        <span class="fl">
                                            <%=DateParser.format(new Date(System.currentTimeMillis()+2*24*60*60*1000)) %>
                                        </span>
                                        <a class="ml20 tx_an1" href="javascript:void(0)"></a>
                                    </div> -->
                                    <div class="pop-con"
                                        style="margin-left: 365px; display: none;">
                                        <div class="fl pop-pic"></div>
                                        <div class="pop-info">1-2个工作日之内到账（遇双休日和法定节假日往后顺延！）</div>
                                    </div>
                                    <div class="clear"></div>
                                </li> --%>
									<%-- <%
									    if (isOpenPsd && !tg) {
									%>
									<li>
										<div class="til">
											<span class="red">*</span> 交易密码：
										</div>
										<div class="info">
											<input id="hdnPsdId" type="password" class="text"
												name="hdnPsd" style="display: none;" autocomplete="off" /> <input
												id="passwordId" type="password" class="text"
												name="withdrawPsd" class="highlight"
												style="-moz-user-select: none;"
												onselectstart="return false;" ondragenter="return false;"
												onpaste="return false;" value="" autocomplete="off" maxlength="30"/> <a
												href="<%=safetymesgView%>" class="blue blue_line">找回密码？</a>

											<p errortip class="red"></p>
										</div>
									</li>
									<%
									    }
									%> --%>
								</ul>
								<div class="tc">
									<input type="submit" style="cursor: pointer;" value="提现"
										class="btn01" />
								</div>
							</div>
						</div>
						<%
						    }
						%>
						<%if (t5023 != null && !StringHelper.isEmpty(t5023.F03)) {%>
						<div class="mt20 lh24 htsc_pic">
							<span class="highlight">温馨提示：</span><br />
							<%StringHelper.format(out, t5023.F03, fileStore);%>
						</div>
						<%}%>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%
	    String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
	%>
	<div id="dialog"
		style="<%=StringHelper.isEmpty(infoMsg)?"display: none;":""%>">
		<div class="popup_bg"></div>
		<div class="dialog">
			<div class="title">
				<a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示
			</div>
			<div class="content">
				<div class="tip_information">
					<div class="doubt"></div>
					<div class="tips">
						<p class="f20 gray33" id="con_error"><%=StringHelper.isEmpty(infoMsg) ? "" : infoMsg%>
						</p>
					</div>
				</div>
				<div class="tc mt20">
					<a class="btn01" href="javascript:void(0);" onclick="closeDiv()">确定</a>
				</div>
			</div>
		</div>
	</div>
	<%
	    String infoMsg1 = controller.getPrompt(request, response, PromptLevel.ERROR);
	%>
	<div id="dialog1"
		style="<%=StringHelper.isEmpty(infoMsg1)?"display: none;":""%>">
		<div class="popup_bg"></div>
		<div class="dialog">
			<div class="title">
				<a href="javascript:void(0);" class="out" onclick="closeDiv1()"></a>提示
			</div>
			<div class="content">
				<div class="tip_information">
					<div class="doubt"></div>
					<div class="tips">
						<p class="f20 gray33" id="con_error"><%=StringHelper.isEmpty(infoMsg1) ? "" : infoMsg1%>
						</p>
					</div>
				</div>
				<div class="tc mt20">
					<a class="btn01" href="javascript:void(0);" onclick="closeDiv1()">确定</a>
				</div>
			</div>
		</div>
	</div>
	<div id="dialogRz" style="display: none;">
		<div class="popup_bg"></div>
		<div class="dialog">
			<div class="title">
				<a href="javascript:void(0);" class="out" onclick="closeDivRz()"></a>提示
			</div>
			<div class="content">
				<div class="tip_information">
					<div class="doubt"></div>
					<div class="tips">
						<span class="f20 gray3" id="rz_con_error"></span>
					</div>
				</div>
				<div class="tc mt20">
					<a href="<%=rzUrl%>" target="_blank" class="btn01"
						onclick="closeDivRz()">去认证</a>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<div class="popup_bg1 hide"></div>
	<div id="info"></div>
	<script type="text/javascript">
    var p1 = <%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5)%>;
    var p2 = <%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20)%>;
    var min = <%=configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS)%>;
    var max = <%=configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS)%>;
    var punWay = '<%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY)%>';
    var punProportion = <%=configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION)%>;
    var txsxfkcfs = '<%=configureProvider.getProperty(SystemVariable.TXSXF_KCFS)%>';
    var ye = '<%=t6101.F06%>';
    var mYxrz = '<%=configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL)%>';
    var mPhone = '<%=configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE)%>';
    var mRealName = '<%=configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC)%>';
    var mWithPsd = '<%=configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD)%>';
</script>
	<%@include file="/WEB-INF/include/footer.jsp"%>
	<%@include file="/WEB-INF/include/dialog.jsp"%>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/withdraw.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/amountUtil.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
	<script type="text/javascript">
	function updateBank(usrCustId){
		$.ajax({
			type:"post",
			url:"/user/fuyou/fyouBank.htm?usrCustId=" + escape(usrCustId),
			data:{"usrCustId" : usrCustId},
			dataType: 'text',
		    success: succFunction,
			error: erryFunction
		})
		function erryFunction() {  
		    $("div.popup_bg1").show();
		    $("#info").html(showDialogInfoReload('error', "error"));
	    }  
		function succFunction(msg) { 
			if (msg == "OK"){
				window.location.reload();
			} else {
			    $("div.popup_bg1").show();
			    $("#info").html(showDialogInfoReload(msg, "doubt"));
			}
	    }  
	}
	
	function queryBank(){
		$.ajax({
			type:"post",
			url:"/user/fuyou/fyouQueryBank.htm",
			dataType: 'text',
		    success: succFunction,
			error: erryFunction
		})
		function erryFunction() {  
		    $("div.popup_bg1").show();
		    $("#info").html(showDialogInfoReload('error', "error"));
	    }  
		function succFunction(msg) { 
			if (msg == "OK"){
			    $("div.popup_bg1").show();
			    $("#info").html(showDialogInfoReload('银行卡更换成功！', "successful"));
			}else if(msg == "SHSB"){
			    $("div.popup_bg1").show();
			    $("#info").html(showDialogInfoReload('审核未通过,启动原银行卡！', "doubt"));
			} else {
			    $("div.popup_bg1").show();
			    $("#info").html(showDialogInfoReload(msg, "doubt"));
			}
	    }  
	}
	
    var len = <%=cards==null?0:cards.length%>;

    function closeDiv() {
        $("#dialog").hide();
    }
    function closeDiv1() {
        $("#dialog1").hide();
    }
    function closeDivRz() {
        $("#dialogRz").hide();
    }
    var _isOpenPsd = "<%=isOpenPsd%>";
	var escrow_prefix = "<%=escrow_prefix%>";
    function onSubmit() {
    	var result = true;
		var _isZRR = "<%=t6110.F06 %>";
		var _finalZRR = "<%=T6110_F06.ZRR%>";
		
		//个人用户校验是否有更换卡申请记录
		if(_finalZRR == _isZRR) {
			$.ajax({
				type:"post",
				url:"/user/fuyou/fyouCheckBankT6114Ext.htm",
				dataType: 'text',
				async:false,
				success:function(data){
					if ($.trim(data) == 'true') {
					    $("div.popup_bg1").show();
					    $("#info").html(showDialogInfo('请先查询银行卡更换进度', "doubt"));
						result = false;
					}
				}
			});
		}
		if(result) {
	        /*if ((_tg == 'false' || "FUYOU" == escrow_prefix) && _isOpenPsd == 'true') {
	            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	            var key = RSAUtils.getKeyPair(exponent, '', modulus);
	            $("#hdnPsdId").val(RSAUtils.encryptedString(key, $("#passwordId").val()));
	            $("#passwordId").val(RSAUtils.encryptedString(key, $("#passwordId").val()));
	        }
	        富友提现不需要交易密码
	        */
	        return onSubmit1();
		}
		return false;
    }

    //限制只能输入数字
    function onlyNumber(obj) {
        var isNotNumber = isNaN($(obj).val());
        if (isNotNumber) {//如果不是数字
            $(obj).val("");
        }
        $(obj).nextAll("p[errortip]").hide();
        $(obj).nextAll("p[tip]").html(chinaCost($(obj).val())).show();
    }

</script>
</body>
</html>