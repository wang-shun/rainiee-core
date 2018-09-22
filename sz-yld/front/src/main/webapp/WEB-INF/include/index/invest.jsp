<%@page import="com.dimeng.p2p.S61.enums.T6110_F17"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="org.bouncycastle.util.encoders.Hex"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey"%>
<%@page import="com.dimeng.p2p.service.PtAccountManage"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.front.servlets.Invest"%>
<%@page import="com.dimeng.p2p.account.front.service.UserManage"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.TransferManage"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.front.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.common.enums.TermType" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@ page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.S61.entities.T6147"%>
<%{
	BidManage investManage = serviceSession.getService(BidManage.class);
	TransferManage service = serviceSession.getService(TransferManage.class);
	String tjbId = (String)dimengSession.getAttribute("tjbId");//推荐标ID
	boolean isInvestLimit=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
	boolean isOpenRiskAccess=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
	String _rzUrl = "";
	String userRiskLevel = "BSX";
	Timestamp timemp = investManage.getCurrentTimestamp();//取数据库的当前时间
%>
<div class="hd clearfix" id="investTitle">
     <ul class="fl">
         <li id="sbtz" onclick="initInvestData('sbtz')" class="hover">投资项目</li>
         <li>|</li>
         <li id="zqzr" onclick="initInvestData('zqzr')">债权转让</li>
         <li>|</li>
		 <%//开关判断，为false，则不显示公益标
			 if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){%>
         <li id="gyjz" onclick="initInvestData('gyjz')">公益捐赠</li>
		 <%}%>
     </ul>
</div>
<div id="info"></div>
<%
BigDecimal zqMoney = new BigDecimal(0);
BigDecimal zfMoney = new BigDecimal(0);
UserManage userManage = serviceSession.getService(UserManage.class);
String usrCustId = null;
boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
String action = "";
if(dimengSession!=null && dimengSession.isAuthenticated()){
	usrCustId = userManage.getUsrCustId();
}
if(tg && !"FUYOU".equals(escrow)){
	action = configureProvider.format(URLVariable.ESCROW_URL_EXCHANGE);
}else{
	action = configureProvider.format(URLVariable.TB_ZQZR);
}
%>
<form method="post" class="form1" name="zqzrForm" id="zqzrForm" action="<%=action %>" >
<%=FormToken.hidden(serviceSession.getSession()) %>
<input type="hidden" id="zqSucc" name="zqSucc" value="<%configureProvider.format(out, URLVariable.USER_ZQYZR);%>">
<input type="hidden" name="zqzrId" id="zqzrId">
<div class="popup_bg"  style="display: none;"></div>
<div class="dialog d_error w510" style="margin:-150px 0 0 -255px; display: none;" >
<%if((tg) && StringHelper.isEmpty(usrCustId)){ %>
   	<div class="popup_bg"></div>
	<div class="dialog">
	    <div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>
	    <div class="content">
	    	<div class="tip_information"> 
	    	  <div class="doubt"></div>
	    	  <div class="tips">
	    	    <span class="f20 gray3">您需要在第三方托管平台上进行注册，才可购买债权！请<a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>" class="red">立即注册</a>！</span>
	    	  </div>
	        </div>
	    </div>
	</div>
<%}else{ %>
	<div id="idRisk" style="display:none"></div>
	<div id="id_content" style="display:none">
		<div class="popup_bg"></div>
		<div class="dialog">
		    <div class="title"><a href="javascript:closeInfo();" class="out" onclick="resetForm();"></a>债权购买确认</div>
		    <div class="content">
		    	<div class="tip_information"> 
		          <div class="question"></div>
		          <div class="tips">
		              <span class="f20 gray3">您此次购买债权价值为<span id="zqjz"><%=zqMoney%></span>元，<br />需支付金额<span id="zrjg"><%=zfMoney%></span>,确认购买？</span>
		          </div>
		          <%if(isOpenPwd){ %>
		          <div class="mt20">
			          <span class="red">*</span>
			          	交易密码：<input type="password" onblur="checkVal()" class="required text_style" id="tran_pwd" autocomplete="off"/>
		          </div>
		          <div class="mt20">
			          <span id="errorSpan"  class="red" style="display: none"></span>
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
					<a href="javascript:void(0);" id="cancel" onclick="resetForm();" class="btn01 btn_gray ml20">取消</a>
				</div>
				<%}else{ %>
				<div class="tc mt10">
					<a href="javascript:void(0);" class="btn01" id="okInvest">确定</a>
					<a href="javascript:void(0);" id="cancel" class="btn01 btn_gray ml20">取消</a>
				</div>
				<%} %>
		    </div>
		</div>
	</div>
   <%} %>
</div>
<input type="hidden" name="tranPwd" id="tranPwd" />
</form>
<input type="hidden" name="isTG" id="isTG" value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd" value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzr.js"></script>
<script type="text/javascript">
var _investUrl = "<%=controller.getURI(request, Invest.class)%>";
var _sbtzMoreUrl = "<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>";
var _zqzrMoreUrl = "<%configureProvider.format(out,URLVariable.FINANCING_ZQZR);%>";
var _sbtzXqUrl = "/financing/sbtz/index/";
var _zqxqUrl = "/financing/sbtz/zqxq/";
var _loginUrl = "<%configureProvider.format(out,URLVariable.LOGIN);%>";
var _gyjzXqUrl = "/financing/gyb/gybXq/";
var _gyjzListUrl = "<%configureProvider.format(out,URLVariable.FINANCING_GYJZ);%>";
var _checkTxPwdUrl = '<%configureProvider.format(out,URLVariable.CHECK_TXPWD);%>';
var tjbId = <%=tjbId%>;
var isInvestLimit='<%=isInvestLimit%>';
var isOpenRiskAccess ='<%=isOpenRiskAccess%>';
$(function(){
	$("#okInvest").click(function(){
		okInvest_click();
	});

	//“我同意”按钮切回事件
	$("input:checkbox[name='iAgree']").attr("checked", false);
	$("input:checkbox[name='iAgree']").click(function() {
		var iAgree = $(this).attr("checked");
		var register = $(".sub-btn");
		if (iAgree) {
			register.removeClass("btn_gray btn_disabled");
			register.attr("id","okInvest");
			//选中“我同意”，绑定事件
			$("#okInvest").click(function(){
				okInvest_click();
			});
		} else {
			register.addClass("btn_gray btn_disabled");
			$("#okInvest").unbind("click");
			register.attr("id","");
		}
	});
});

//重置我同意按钮
function resetForm(){
	var register = $(".sub-btn");
	$("input:checkbox[name='iAgree']").attr("checked", false);
	register.addClass("btn_gray btn_disabled");
	$("#okInvest").unbind("click");
	register.attr("id","");
}

function okInvest_click(){
	var isTG= $("#isTG").val();
	var isOpenWithPds =  $("#isOpenWithPsd").val();
	if("true" == isOpenWithPds)
	{
		keleyidialog();
	}else{
		$("#zqzrForm").get(0).submit();
	}

}
function keleyidialog() {
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	$("#errorSpan").html("交易密码不能为空").css("fontSize","14px").show();
    	//return;
    }else{
    	$("#errorSpan").hide();
    	var sPwd= RSAUtils.encryptedString(key,tran_pwd);
	    $("#tranPwd").val(sPwd);
    	var param = {'tranPwd':sPwd};
    	$.ajax({
			type:"post",
			dataType:"html",
			url: _checkTxPwdUrl,
			data: param,
			success:function(data){
				data = eval("("+data+")");
				if(data.code=="0001"){
					$("#tran_pwd").next("span").hide();
			    	$("div.dialog").hide();
					$("div.popup_bg").hide();
				    //提交
					$("#zqzrForm").get(0).submit();
				}else{
					$("#errorSpan").html(data.msg).css("fontSize","14px").show();
				}
			}
			,error : function(){
			 	alert("系统繁忙，请稍后重试！");
			}
	    });
    	
    }
}

function checkVal(){
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	$("#errorSpan").html("交易密码不能为空").css("fontSize","14px").show();
    	//return;
    }else{
    	$("#errorSpan").hide();
    }
}

var isLogin = false;
var isHmd = false;
var isZrr = false;
var isInvest = true;
<%
if(dimengSession!=null && dimengSession.isAuthenticated()){
	%>
	isLogin = true;
	<%
	UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
	T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
	T6147 t6147 = uManage.getT6147();
	userRiskLevel = t6147==null ? "BSX" : t6147.F04.name() ;
    if(T6110_F06.FZRR == t6110.F06){
        _rzUrl = "/user/account/safetymsgFZRR.htm";
    }else{
        _rzUrl = "/user/account/userBases.html?userBasesFlag=1";
    }
	if(T6110_F07.HMD == t6110.F07){
	%>
	var isHmd = true;
	<%
	}
	if(T6110_F06.ZRR == t6110.F06){
		%>
	isZrr = true;
		<%}
	if(T6110_F06.FZRR == t6110.F06 && T6110_F17.F == t6110.F17){%>
	    isInvest = false;
	<%}
}
{
PtAccountManage ptAccountManage1 = serviceSession.getService(PtAccountManage.class);
DimengRSAPulicKey publicKey1 = ptAccountManage1.getPublicKey();
String modulus1 = new String(Hex.encode(publicKey1.getModulus().toByteArray()));
String exponent1 = new String(Hex.encode(publicKey1.getPublicExponent().toByteArray()));
%>
var userRiskLevel = '<%=userRiskLevel%>';
var _rzUrl = '<%=_rzUrl%>';
var modulus = "<%=modulus1%>", exponent = "<%=exponent1%>";
var key = RSAUtils.getKeyPair(exponent, '', modulus);
<%}%>



function sbtzCountdown(F13Time,id) {
	//去掉此倒计时操作，只在详情页倒计时
	/*var date = new Date();
	var timeamp = '<%=timemp.getTime()%>';
	date.setTime(F13Time);
	var sbtzEndTime = date.getTime()-timeamp;
	setInterval(function() {
		sbtzEndTime = sbtzEndTime - 1000;
		var time = sbtzEndTime;
		//sbtzTime(time,id);
		if(time <= 0){
			clearInterval(this);
			location.reload();
		}
	}, 1000);*/
}

function sbtzTime(sbtzEndTime,id) {
	var leftsecond = parseInt(sbtzEndTime / 1000);
	var day = Math.floor(leftsecond / (60 * 60 * 24)) < 0 ? 0 : Math
			.floor(leftsecond / (60 * 60 * 24));
	var hour = Math.floor((leftsecond - day * 24 * 60 * 60) / 3600) < 0 ? 0
			: Math.floor((leftsecond - day * 24 * 60 * 60) / 3600);
	var minute = Math
			.floor((leftsecond - day * 24 * 60 * 60 - hour * 3600) / 60) < 0 ? 0
			: Math
					.floor((leftsecond - day * 24 * 60 * 60 - hour * 3600) / 60);
	var second = Math.floor(leftsecond - day * 24 * 60 * 60 - hour * 3600
			- minute * 60) < 0 ? 0 : Math.floor(leftsecond - day * 24 * 60
			* 60 - hour * 3600 - minute * 60);
	if (hour < 10) {
		hour = "0" + hour;
	}
	if (minute < 10) {
		minute = "0" + minute;
	}
	if (second < 10) {
		second = "0" + second;
	}
	//$("#sbtzTime"+id).html(hour+"时"+minute+"分"+second+"秒 开始发售");
}
</script>
<%}%>