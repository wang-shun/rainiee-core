<%@page import="com.dimeng.p2p.account.user.service.BankCardManage" %>
<%@page import="com.dimeng.p2p.account.user.service.TxManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.BankCard" %>
<%@page import="com.dimeng.p2p.common.enums.BankCardStatus" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Deletebankcard" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Bankcardlist" %>
<%@ page import="com.dimeng.p2p.user.servlets.fuyou.FyouChangeCard" %>
<%@ page import="com.dimeng.p2p.user.servlets.fuyou.FyouBankcard" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.SafetymsgFZRR" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<%@page import="com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable" %>
<%@page import="com.dimeng.p2p.escrow.fuyou.service.BankManage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
    BankCard[] card = bankCardManage.getBankCars(BankCardStatus.QY.name());

    UserManage usrManage = serviceSession.getService(UserManage.class);
    String usrCustId = usrManage.getUsrCustId();

	String url = configureProvider.format(FuyouVariable.FUYOU_BANKCARD);
    BankManage bankManage = serviceSession.getService(BankManage.class);
    boolean flag = bankManage.selectT6114Ext();
    
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "YHKXX";

    Map<String, String> bankMap = new HashMap<String, String>();
    bankMap.put("中国银行", "BOC");
    bankMap.put("中国工商银行", "ICBC");
    bankMap.put("中国农业银行", "ABC");
    bankMap.put("中国建设银行", "CCB");
    bankMap.put("招商银行", "CMB");
    bankMap.put("交通银行", "BOCOM");
    bankMap.put("中国邮政储蓄银行", "PSBC");
    bankMap.put("中国民生银行", "CMBC");
    bankMap.put("中国光大银行", "CEB");
    bankMap.put("平安银行股份有限公司", "PINGAN");
    bankMap.put("平安银行", "PINGAN");
    bankMap.put("上海浦东银行", "SPDB");
    bankMap.put("上海浦东发展银行", "SPDB");
    
    bankMap.put("国家邮政局邮政储汇局", "PSBC");//富有的邮政银行是这个
    bankMap.put("汇丰银行", "HSBC");
    bankMap.put("兴业银行", "CIB");
    bankMap.put("中信实业银行", "CITIC");
    bankMap.put("华夏银行", "HB");
    bankMap.put("广东发展银行", "GDB");
    bankMap.put("深圳发展银行", "SDB");
    bankMap.put("北京银行", "BOB");
    bankMap.put("北京商业银行", "BCCB");
    bankMap.put("微商银行", "WSBANK");
    bankMap.put("浙商银行", "ZSBANK");

    boolean checkFlag = true;
    String checkMessage = "";
    String rzUrl = "";
    String failReason = "";

    com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
    Map<String, String> retMap = userCommManage.checkAccountInfo();
    checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
    checkMessage = retMap.get("checkMessage");
    rzUrl = retMap.get("rzUrl");
    failReason = retMap.get("failReason");
%>

<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="user_til">
                    <i class="icon"></i><span class="gray3 f18">银行卡管理</span>
                </div>
                <%if (!checkFlag && "thirdNull".equals(failReason)) { %>
                <div class="f16 mt30 pt50 pb50 tc ml30 mr30" style="background: #f3f3f3;">
                	您需要在第三方托管平台上进行注册，才可管理银行卡！请
                	<a href="<%=rzUrl %>" class="red">立即注册</a>！
                </div>
                <%} else { %>
                <ul class="bank_manage_list clearfix">
                    <%for (BankCard b : card) {
                            if (bankMap.containsKey(b.Bankname)) {
                            	String bank = bankMap.get(b.Bankname); %>
                        <%if (T6110_F06.ZRR == t6110.F06 && !StringHelper.isEmpty(usrCustId)) { %>
	                    	<%if(flag) { %>
		                    	<li>
			                        <div class="delete clearfix" align="center" style="margin-top: 40px;">
				                    	<a href="javascript:void(-1);" onclick="queryBank();">更换银行卡审核中<p></p>点击查询进度</a>
			                        </div>
			                    </li>
	                    	<%}else{ %>
	                    		<li>
			                        <div class="pic">
			                            <img src="<%=controller.getStaticPath(request)%>/images/bank_logo_<%=bank %>.png" width="148" height="38">
			                        </div>
			                        <div class="number">
			                            <%StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
			                        </div>
			                        <div class="delete clearfix" align="center">
			                        	<a href="javascript:void(-1);" onclick="updateCard('<%=b.id%>');" >查看</a>
			                        	&nbsp;&nbsp;&nbsp;&nbsp;
				                    	<a href="javascript:void(-1);" onclick="updateCards();">更换银行卡</a>
			                        </div>
		                    	</li>
	                    	<%}%>
                    	<%}else{ %>
                    	   <li>
		                        <div class="pic">
		                            <img src="<%=controller.getStaticPath(request)%>/images/bank_logo_<%=bank %>.png" width="148" height="38">
		                        </div>
		                        <div class="number">
		                            <%StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
		                        </div>
		                        <div class="delete clearfix" align="center">
		                        	<a href="javascript:void(-1);" onclick="updateCard('<%=b.id%>');" >立即查看</a>
		                        </div>
		                    </li>
                    	<%}%>
                    <%} else { %>
                    
                     <%if (T6110_F06.ZRR == t6110.F06 && !StringHelper.isEmpty(usrCustId)) { %>
	                    	<%if(flag) { %>
		                    	<li>
			                        <div class="delete clearfix" align="center" style="margin-top: 40px;">
				                    	<a href="javascript:void(-1);" onclick="queryBank();">更换银行卡审核中<p></p>点击查询进度</a>
			                        </div>
			                    </li>
	                    	<%}else{ %>
	                    		<li>
			                        <div class="pic">
			                              <%StringHelper.truncation(out, b.Bankname, 10, "***");%>
			                        </div>
			                        <div class="number">
			                            <%StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
			                        </div>
			                        <div class="delete clearfix" align="center">
			                        	<a href="javascript:void(-1);" onclick="updateCard('<%=b.id%>');" >查看</a>
			                        	&nbsp;&nbsp;&nbsp;&nbsp;
				                    	<a href="javascript:void(-1);" onclick="updateCards();">更换银行卡</a>
			                        </div>
		                    	</li>
	                    	<%}%>
                    	<%}else{ %>
                    	   <li>
		                        <div class="pic">
		                            <%StringHelper.truncation(out, b.Bankname, 10, "***");%>
		                        </div>
		                        <div class="number">
		                            <%StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
		                        </div>
		                        <div class="delete clearfix" align="center">
		                        	<a href="javascript:void(-1);" onclick="updateCard('<%=b.id%>');" >立即查看</a>
		                        </div>
		                    </li>
                    	<%}%>
                  <%--   <li>
                        <div class="pic"
                             title="<%StringHelper.filterHTML(out, b.Bankname);%>">
                            <%StringHelper.truncation(out, b.Bankname, 6, "***");%>
                        </div>
                        <div class="number">
                            <%
                                StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
                        </div>
                        <div class="delete clearfix">
                        </div>
                    </li> --%>
                    <%
                            }
                        }
                    %>
                    <%if(card.length <= 0) { %>
	                    <%if (T6110_F06.ZRR == t6110.F06 && !StringHelper.isEmpty(usrCustId)) { %>
	                    	<%if(flag) { %>
	                    		<li>
			                        <div class="delete clearfix" align="center" style="margin-top: 40px;">
				                    	<a href="javascript:void(-1);" onclick="queryBank();">更换银行卡审核中<p></p>点击查询进度</a>
			                        </div>
			                    </li>
                   	<%} } }%>
                    <div class="clear"></div>
                </ul>
                <%} if (T6110_F06.ZRR == t6110.F06 && !StringHelper.isEmpty(usrCustId)) {%>
                <div class="prompt_mod mt20 mb302">
                <span class="highlight">温馨提示：</span><br>
                1.申请更换银行卡，将无法提现(原卡将停用)，第三方托管(富友)两个日内完成审核。<br>
                2.审核通过则启动新卡，审核未通过则依使用原卡，在此期间用户可查询换卡情况(无法提现)。<br>
                3.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
                4.如果您不确定开户行支行名称，可打电话到所在地银行的营业网点询问或上网查询。<br>
                5.不支持提现至信用卡账户。
            	</div>
            	<%} else if (!StringHelper.isEmpty(usrCustId)) {%>
            	    <div class="prompt_mod mt20 mb302">
                <span class="highlight">温馨提示：</span><br>
                1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
                2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网点询问或上网查询。<br>
                3.不支持提现至信用卡账户。
            	</div>
            	<%}%>
            </div>
            <!-- <div class="mt20 mb30" style="padding:15px 20px; background:#f8f8f8; line-height:24px;">
            <span class="highlight">温馨提示：</span><br>
            1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
            2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网点询问或上网查询。<br>
            3.不支持提现至信用卡账户。
            </div> -->

            <%
                if (!checkFlag) {
            %>

            <div id="smrz" style="display: none;">
                <div class="dialog ">
                    <div class="title"><a href="javascript:void(-1);" class="out" onclick="hidebg('smrz');"></a>认证提示
                    </div>
                    <div class="content">
                        <div class="tip_information">
                            <div class="doubt"></div>
                            <div class="tips">
                                <p class="f20 gray33"><%=checkMessage%></p>
                            </div>
                        </div>
                        <div class="tc mt20">
                            <a class="btn01"
                               href="<%=rzUrl%>"
                               target="_blank" onclick="hidebg('smrz')">去认证</a>
                        </div>
                    </div>
                </div>
            </div>
            <%} %>

        </div>
        <div class="clear"></div>

    </div>
</div>

<div class="dialog" style="display: none;" id="updateCard">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>申请更换银行卡</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
                <input type="hidden" name="delId" id="delId">
                <span class="f20 gray3">是否确认更换银行卡？</span>
            </div>
        </div>
        <div class="tc mt20"><a href="<%=url%>" class="btn01">是</a><a href="javascript:void(0);"
                                                              class="btn01 btn_gray ml20 close">否</a>
        </div>
    </div>
</div>
<!-- 提示-HSP -->
	<%
		String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
	%>
	<div id="dialogMessage" style="<%=StringHelper.isEmpty(infoMsg)?"display: none;":"" %>">
			<div class="dialog">
			<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>
		    <div class="content">
		    	<div class="tip_information">
		    		<div class="successful"></div>
		    		<div class="tips">
		    			<span class="f20 gray3"><%=StringHelper.isEmpty(infoMsg)?"":infoMsg %></span>
		    		</div>
		    	</div>
		    	<div class="tc mt20">
		    	<input type="button" onclick="closeDialogMessageDiv()" class="btn04" value="确认" />
		    	</div> 
		   </div>
		    </div>
	</div>
	
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
	function updateCards() {
	    $("#updateCard").show();
        $("div.popup_bg").show();
	}
    $(".close").click(function () {
        $("div#updateCard").hide();
        $("div.popup_bg").hide();
    });
    function closeDialogMessageDiv(){
        $("#dialogMessage").hide();
        $("div.popup_bg").hide();
        location.reload();
    }
    function addCard(id) {
        if ('false' == '<%=checkFlag%>') {
            $("#smrz").show();
            $("div.popup_bg").show();
            return false;
        }

        global_art = art.dialog.open("<%=controller.getViewURI(request, FyouChangeCard.class)%>", {
            id: 'addCard',
            title: '申请更换银行卡',
            opacity: 0.1,
            width: 700,
            height: 520,
            padding: 0,
            lock: true,
            close: function () {
                window.location.reload();
            }

        }, false);
    }

    function updateCard(id) {
        if ('false' == '<%=checkFlag%>') {
            $("#smrz").show();
            $("div.popup_bg").show();
            return false;
        }

        global_art = art.dialog.open("<%=controller.getViewURI(request, FyouBankcard.class)%>?id=" + id, {
            id: 'addCard',
            title: '银行卡信息',
            opacity: 0.1,
            width: 700,
            height: 520,
            lock: true,
            close: function () {
//                 window.location.reload();
            }

        }, false);
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
            $("div.popup_bg").show();
		    $("#info").html(showDialogInfoReload('error', "error"));
	    }  
		function succFunction(msg) { 
			if (msg == "OK"){
                $("div.popup_bg").show();
			    $("#info").html(showDialogInfoReload('银行卡更换成功！', "successful"));
			}else if(msg == "SHSB"){
                $("div.popup_bg").show();
			    $("#info").html(showDialogInfoReload('审核未通过,启动原银行卡！', "doubt"));
			} else {
                $("div.popup_bg").show();
			    $("#info").html(showDialogInfoReload(msg, "doubt"));
			}
	    }  
	}
	
    
    function hidebg(id) {
        $("#" + id).hide();
        $("div.popup_bg").hide();
    }

</script>
</body>
</html>
