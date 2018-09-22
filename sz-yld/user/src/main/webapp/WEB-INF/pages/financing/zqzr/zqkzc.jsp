<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.MaySettleFinacing" %>
<%@page import="com.dimeng.p2p.user.servlets.Index" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.zqzr.Transfer" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>债权转让-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "ZQZR";
    int zqId = 0;
    BigDecimal zqValue = new BigDecimal(0);
    int kzNum = 0;
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
    BigDecimal zrfee = BigDecimal.ZERO;
    BigDecimal zqzrsx = BigDecimal.ZERO;//债权转让上限
    BigDecimal zqzrxx = BigDecimal.ZERO;//债权转让下限
    String logicErrorMessage = "";
    boolean isException = false;
    try {
        zrfee = new BigDecimal(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE));
        String zqzrbl = configureProvider.getProperty(SystemVariable.ZQZRBL);
        String[] zqzrblTemp = zqzrbl.split("-");
        if(zqzrblTemp.length==2){
	        zqzrxx = new BigDecimal(zqzrblTemp[0]);
	        zqzrsx = new BigDecimal(zqzrblTemp[1]);
	        if(zqzrxx.compareTo(zqzrsx)>0){
	            throw new RuntimeException();
	        }
        }
    } catch (Exception e) {
        isException = true;
        logicErrorMessage = "债权转让业务暂停交易，如需转让，请联系客服"+configureProvider.getProperty(SystemVariable.COMPANY_CONTACT_TEL);
    }
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <!--左菜单-->
        <!--右边内容-->
        <div class="r_main">
            <%@include file="/WEB-INF/include/zqzr/header.jsp" %>
            <div class="user_mod border_t15">
                <div class="user_tab clearfix">
                    <ul>
                        <li id="liId1" value="1" onclick="zqzrz(1)">转让中的债权<i></i></li>
                        <li id="liId2" value="2" onclick="kzczq(1)" class="hover">可转出的债权<i></i></li>
                        <li id="liId3" value="3" onclick="yzczq(1)">已转出的债权<i></i></li>
                        <li id="liId4" value="4" onclick="yzrzq(1)">已转入的债权<i></i></li>
                    </ul>
                </div>
                <div class="user_table pt5">
                    <input type="hidden" name="zcbId" id="zcbId" value="">
                    <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td>债权ID</td>
                            <td>剩余期数</td>
                            <td>下一还款日</td>
                            <td>年化利率</td>
                            <td>待收本息</td>
                            <td>债权价值</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <div class="page" id="pageContent"></div>
                </div>
            </div>
        </div>
        <!--右边内容-->
    </div>
</div>
<!--主体内容-->

<div class="dialog" id="zqCancelId" style="top:200px; display: none;">
    <div class="title"><a href="javascript:void(0)" class="out" onclick="displayNoneDiv('zqCancelId')"></a>债权转让</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
                <span class="f20 gray3">是否执行"取消转让"债权操作？</span>
            </div>
        </div>
        <div class="tc mt20"><a href="javascript:void(0)" id="ok" class="btn01">是</a><a href="javascript:void(0)"
                                                                                        id="cancel"
                                                                                        class="btn01 btn_gray ml20">否</a>
        </div>
    </div>
</div>

<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>

<form action="<%=controller.getURI(request, Transfer.class)%>" id="transferForm" method="post" class="form1">
    <input type="hidden" name="zqId" id="zqId" value="">
    <input type="hidden" name="zqValue" id="zqValue" value="">
    <input type="hidden" name="tranPwd" id="tranPwd"/>

    <div class="dialog" id="transferDivId" style="top:200px; display: none;">
        <div class="title"><a href="javascript:void(0)" class="out" onclick="closeZqzrDiv('transferDivId')"></a>债权转让
        </div>
        <div class="content">
            <ul class="text_list">
                <li>
                    <div class="til">债权价值(元)：</div>
                    <div class="con"><span class="red" id="zqjz"></span></div>
                </li>
                <li>
                    <div class="til">转让费率(%)：</div>
                    <div class="con"><%=zrfee.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP)%>
                    </div>
                </li>
                <li>
                    <div class="til">转让价格(元)：</div>
                    <div class="con">
                        <input type="text" name="zrValue" id="zrValue" maxlength="18"
                               onKeyUp="value=((value=value.replace(/\D/g,'')));setDMKeyup(this);"/>

                        <p tip></p>

                        <p errortip class="" style="display: none"></p>
                    </div>
                </li>
                <%if (isOpenPwd) { %>
                <li>
                    <div class="til"><span class="red">交易密码</span>：</div>
                    <div class="con">
                        <input type="password" onblur="checkVal()"
                               class="required text_style" id="tran_pwd"  autocomplete="off"/>
                        <p id="errorSpan" class="error_tip red" style="display: none"></p>
                    </div>
                </li>
                <%} %>
            </ul>
            <div class="tc mt20">
                <!-- 		<a href="#" class="btn04">转让</a><a href="#" class="btn04 btn_gray ml20">取消</a> -->
                <input type="button" id="zrSubmit" class="btn01" fromname="form1" style="cursor: pointer;" value="转让">
                <input type="button" id="zrCancel" class="btn01 btn_gray ml20" style="cursor: pointer;" value="取消">
            </div>
        </div>
    </div>


    <%-- <div id="transferDivId2" class="dialog d_error w380 thickpos" style="margin:-150px 0 0 -190px;display: none;">
      <div class="dialog_close fr"><a href="#"></a></div>
      <div class="con clearfix">
        <div class="clearfix">
          <table width="100%" border="0" cellspacing="0" style="table-layout:fixed;">
            <tr>
              <td width="80">债权价值：</td>
              <td><span id="zqjz"></span>元</td>
            </tr>
            <tr>
              <td>转让费率：</td>
              <td><%=DoubleParser.parse(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE))*100 %>%</td>
            </tr>
            <tr>
              <td valign="top">转让价格：</td>
              <td style="word-wrap:break-word;">
                  <input type="text" name="zrValue" id="zrValue" maxlength="18" onKeyUp="value=((value=value.replace(/\D/g,'')))" />
                  <span class="ml5 required">元</span>
                  <p tip></p>
                <p errortip class="" style="display: none"></p>
              </td>
            </tr>
            <%if(!tg && isOpenPwd){ %>
            <tr style="height: 30px;">
              <td valign="top">交易密码：</td>
              <td style="word-wrap:break-word;">
                  <input type="password" onblur="checkVal()"
                            name="tran_pwd" class="required" id="tran_pwd"
                            style="border: 1px solid #ddd; " />
                  <span id="errorSpan2" class="error_tip" style="display: none"></span>
              </td>
            </tr>
            <%} %>
          </table>
        </div>
        <div class="clear"></div>
        <div class="dialog_btn">
            <input type="button" id="zrSubmit2" class="btn001 sumbitForme" fromname="form1" style="cursor: pointer;" value="转让">
            <input type="button" id="zrCancel2" class="btn05" style="cursor: pointer;" value="取消">
        </div>
      </div>
    </div> --%>
</form>


<input type="hidden" name="isTG" id="isTG"
       value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="zqzrsx" id="zqzrsx" value="<%=zqzrsx%>">
<input type="hidden" name="zqzrxx" id="zqzrxx" value="<%=zqzrxx%>">
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzrz.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/zqzr.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/ajaxDateUtil.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/amountUtil.js"></script>

<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=message%>", "succeed", $("#sbSucc").val()));
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
    var isException = <%=isException%>;
    var logicErrorMessage = "<%=logicErrorMessage%>";
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    var isTG = <%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>;
    var isOpenWithPsd = <%= BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD )) %>;
    var zqzrz_url = '<%=controller.getURI(request, Zqzrz.class)%>';
    var zqkzc_url = '<%=controller.getURI(request, Zqkzc.class)%>';
    var zqyzc_url = '<%=controller.getURI(request, Zqyzc.class)%>';
    var zqyzr_url = '<%=controller.getURI(request, Zqyzr.class)%>';
    var sbtz_xq = '<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ); %>';
    var rewriter = '<%=rewriter.getViewSuffix()%>';
    var htUrl = "<%configureProvider.format(out, URLVariable.USER_ZQZR); %>";
    $(function () {
        $("#zrSubmit").click(function () {
        	
        	if(checkAmountRange()){
        		return;
        	}
        	if(checkAmount()){
        		return;
        	}
            if (isOpenWithPsd) {
                keleyidialog();
            } else {
                //提交
                $("#transferForm").submit();
            }
        });
        kzczq();
    });
    
    function checkAmountRange(){
    	var value = $("#zrValue").val();
    	var zqjz = parseFloat($("#zqjz").text());//债权价值
		var zqzrsx = $("#zqzrsx").val();//转出价格上限
		var zqzrxx = $("#zqzrxx").val();//转出价格下限
		zqzrsx = Math.ceil(zqjz*zqzrsx);
		zqzrxx = Math.ceil(zqjz*zqzrxx);
    	if((parseInt(value)>parseInt(zqzrsx)) || (parseInt(value)<parseInt(zqzrxx))){
    		return true;
    	}
    	return false;
    }
    
    function checkAmount(){
    	var $eve = $("#zrValue");
    	var amount = $eve.val();
    	$error = $eve.nextAll("p[errortip]");
    	$tip = $eve.nextAll("p[tip]");
    	if(!amount){
    		$error.addClass("red");
			$error.html("不能为空！");
			$tip.hide();
			$error.show();
			return true;
    	}
    	return false;
    }

	$("#zrCancel").click(function () {
        $("#transferDivId").hide();
        $("#transferDivId").find("input[type='text']").val("");
        $("#transferDivId").find("input[type='password']").val("");
        $("#transferDivId").find("p").html("");
        $("div.popup_bg").hide();
    });

    function closeZqzrDiv(id){
		$("#"+id).css('display','none');
		$("#transferDivId").find("input[type='text']").val("");
        $("#transferDivId").find("input[type='password']").val("");
        $("#transferDivId").find("p").html("");
        $("div.popup_bg").hide();
	}
    function keleyidialog() {
        var tran_pwd = $("#tran_pwd").val();
        if (!tran_pwd) {

            $("#tran_pwd").next("p").html("交易密码不能为空").show();
            //return;
        } else {
            $("#tran_pwd").next("p").hide();
            $("div.popup_bg").hide();
            $("div.dialog").hide();
            var sPwd = RSAUtils.encryptedString(key, tran_pwd);
            $("#tranPwd").val(sPwd);
            $("#tran_pwd").val(sPwd);
            //提交
            var form = $(".form1").get(0);
            form.submit();
        }
    }

    function checkVal() {
        var tran_pwd = $("#tran_pwd").val();
        if (!tran_pwd) {
            $("#errorSpan").html("交易密码不能为空").show();
            //return;
        } else {
            $("#errorSpan").hide();
        }
    }
    
    function toAjaxPage(){
    	if($("#liId1").hasClass("hover")){
    		zqzrz();
    	}else if($("#liId2").hasClass("hover")){
    		kzczq();
    	}else if($("#liId3").hasClass("hover")){
    		yzczq();
    	}else if($("#liId4").hasClass("hover")){
    		yzrzq();
    	}
    }
</script>
<script type="text/javascript">
function showLogicExceptionInfo(){
    $("#info").html(showDialogInfo("<%=logicErrorMessage%>", "error"));
    $("div.popup_bg").show();
}
</script>
</body>
</html>