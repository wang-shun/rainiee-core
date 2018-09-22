<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.CheckPsd" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="com.dimeng.p2p.front.servlets.financing.gyb.GybXq" %>
<%@page import="java.util.Calendar" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.S61.entities.T6101" %>
<%@page import="java.sql.Timestamp" %>
<%@page import="com.dimeng.p2p.S62.entities.T6242" %>
<%@page import="com.dimeng.p2p.S62.entities.T6243" %>
<%@page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@page import="com.dimeng.p2p.repeater.donation.GyLoanManage" %>
<%@page import="com.dimeng.p2p.repeater.donation.entity.GyLoanStatis" %>
<%@page import="com.dimeng.p2p.repeater.donation.GyLoanProgresManage" %>
<%@page import="com.dimeng.p2p.repeater.donation.entity.BidProgres" %>
<%@page import="com.dimeng.p2p.repeater.donation.query.ProgresQuery" %>
<%@page import="com.dimeng.p2p.repeater.donation.entity.Donation" %>
<%@page import="com.dimeng.p2p.repeater.donation.query.DonationQuery" %>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@ page import="com.dimeng.util.parser.IntegerParser" %>
<%@ page import="java.util.Map" %>
<html>
<head>
	<title><%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
	boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));

    final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
    int currentPage = IntegerParser.parse(requestWrapper.getParameter("paging.current"));
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
    
    //是否进行认证
    boolean checkFlag=true;
    String checkMessage = "";
    String rzUrl = "";
    String failReason = "";
    if(dimengSession !=null && dimengSession.isAuthenticated()){
        com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
        Map<String, String> retMap = userCommManage.checkAccountInfo();
        checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
        checkMessage = retMap.get("checkMessage");
        rzUrl = retMap.get("rzUrl");
        failReason = retMap.get("failReason");
    }
    
    {

        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        final int id = IntegerParser.parse(request.getParameter("id"));
        //公益标详情（统计数据）
        BigDecimal zxMoney = new BigDecimal(0);
        GyLoanStatis gys = gyLoanManage.gyLoanStatistics(id);
        T6242 creditInfo = gyLoanManage.get(id);
        if (creditInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        boolean isTimeEnd = false;
        Calendar _calendar = Calendar.getInstance();
        if (creditInfo.F19 == null) {
            _calendar.setTime(creditInfo.F13);
            _calendar.add(Calendar.DAY_OF_MONTH, creditInfo.F08 - 1);
            if (Calendar.getInstance().getTime().after(_calendar.getTime())) {
                isTimeEnd = true;
            }
        } else {
            isTimeEnd = true;
        }

        //计算进度
        Double jd = (creditInfo.F05.doubleValue() - creditInfo.F07.doubleValue()) / creditInfo.F05.doubleValue();
        if(isTimeEnd)
        {
            jd = 1d;
        }
        T6243 cInfo = gyLoanManage.getT6243(id);
        if (cInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GyLoanProgresManage manage = serviceSession.getService(GyLoanProgresManage.class);
        PagingResult<BidProgres> result = manage.search4front(new ProgresQuery() {
            @Override
            public String getSysName() {
                return null;
            }

            @Override
            public T6242_F11 getStatus() {
                return null;
            }

            @Override
            public String getLoanTitle() {
                return null;
            }

            @Override
            public String getGyName() {
                return null;
            }

            @Override
            public Timestamp getCreateTimeStart() {
                return null;
            }

            @Override
            public Timestamp getCreateTimeEnd() {
                return null;
            }

            @Override
            public Timestamp getComTimeStart() {
                return null;
            }

            @Override
            public Timestamp getComTimeEnd() {
                return null;
            }

            @Override
            public String getBidNo() {
                return null;
            }

            @Override
            public int getBidId() {
                return id;
            }
        }, new Paging() {
            @Override
            public int getSize() {
                return 100;
            }

            @Override
            public int getCurrentPage() {
                return 0;
            }
        });
        //查询捐助记录
        PagingResult<Donation> tbList = gyLoanManage.searchTbjl(new DonationQuery() {
            @Override
            public int getUserId() {
                return 0;
            }

            @Override
            public Timestamp getCreateTimeStart() {
                return null;
            }

            @Override
            public Timestamp getCreateTimeEnd() {
                return null;
            }

            @Override
            public int getBidId() {
                return id;
            }

        }, new Paging() {
            @Override
            public int getSize() {
                return 10;
            }

            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(requestWrapper.getParameter("paging.current"));
            }
        });
%>
<!--B 主题内容-->
<div class="main_bg">
    <!--筛选投资项目-->
    <form method="post" class="form1"
          action="<%configureProvider.format(out,URLVariable.PAY_BID_URL_4_GYB);%>"
          onkeydown="if(event.keyCode==13){return false;}">
        <%=FormToken.hidden(serviceSession.getSession()) %>
        <input type="hidden" id="sbSucc" name="sbSucc"
               value="<%configureProvider.format(out, URLVariable.MYGYB);%>">
        <input type="hidden" name="loanId1" id="loanId1" value="<%=id%>">
        <input type="hidden" name="tranPwd" id="tranPwd"/>

        <div class="item_details_top w1002 clearfix">
            <!--左侧-->
            <div class="left">
                <div class="til">
                    <span class="item_icon charity_icon"></span><%=creditInfo.F03%>
                </div>
                <div class="clearfix mt30">
                    <%-- <div class="pic">
                        <img
                                src="<%=controller.getStaticPath(request)%>/images/item_details_pic.jpg">
                    </div> --%>
                    <div class="info" style="width: 100%;">
                    	<div class="border_d_b pb15">金额：<span class="gray3 f30"><%=isTimeEnd ? Formater.formatAmount(creditInfo.F05.subtract(creditInfo.F07)):Formater.formatAmount(creditInfo.F05)%></span>元</div>
                        <ul class="other_info clearfix mt20">
                            <li>筹款开始时间：<%=TimestampParser.format(creditInfo.F15, "yyyy-MM-dd")%>
                            </li>
                            <li>筹款截止时间：<%
                                Calendar calendar = Calendar.getInstance();
                                if (creditInfo.F19 == null) {
                                    calendar.setTime(creditInfo.F13);
                                    calendar.add(Calendar.DAY_OF_MONTH, creditInfo.F08 - 1);
                            %> <%=DateParser.format(calendar.getTime(), "yyyy-MM-dd") %> <%} else {%>
                                <%=TimestampParser.format(creditInfo.F19, "yyyy-MM-dd")%> <%} %>
                            </li>
                            <li>最低起捐：<%=Formater.formatAmount(creditInfo.F06)%>元
                            </li>
                            <li>可捐金额：<%=isTimeEnd ? "0.00" : Formater.formatAmount(creditInfo.F07)%>元
                            </li>
                            <li class="long">公益方：<%=creditInfo.F22%>
                            </li>
                            <li class="long"><div class="progress">进度<span class="progress_bg ml5"><span class="progress_bar" style="width:<%=(int)(jd*100)%>%;"></span></span> <span class="cent"><%=Formater.formatProgress(jd)%></span> </div></li>
                            <li class="long">简介：<%=creditInfo.F24%>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <!--左侧-->

            <!--右侧-->
            <div class="right">
                <%
                    if (dimengSession != null && dimengSession.isAuthenticated()) {
                        T6101 userInfo = userInfoManage.search();
                        String isYuqi = userInfoManage.isYuqi();
                        T6110 t6110_1 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
                %>
                <%if (!checkFlag && "thirdNull".equals(failReason)) {%>
                <div class="f18 tc mt100">
                    您需要在第三方托管平台上进行注册，才可申请捐助！ <a href="<%=rzUrl %>"
                                                class="orange">立即注册</a>
                </div>
                <%} else {%>
                <div class="f18 border_d_b tc pt10 pb20">可捐金额<br/><span class="f30"><%=isTimeEnd ? "0.00" : Formater.formatAmount(creditInfo.F07)%></span>元</div>
                <div class="mt20">
                    <a
                            href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>"
                            class="blue fr">充值</a>可用金额：<span class="orange"><em
                        class="bold"><%=Formater.formatAmount(userInfo.F06)%>
                </em>元</span>
                </div>

                <%if ((jd != null && jd.intValue() < 1 && !isTimeEnd && T6110_F07.HMD != t6110_1.F07)) {%>
                <div class="focus_input pr mt20">
                    <input id="amount1" name="amount1" maxlength="10" type="text"
                           class="focus_text"
                           onblur="isDouble()" onkeyup="setDMKeyup(this);"/>
                    <label for="amount1">起捐金额：￥<%=Formater.formatAmount(creditInfo.F06)%>元</label>
                    <p id="dxje">&nbsp;</p>
                </div>
                <input type="hidden" name="loanId1" id="loanId1" value="<%=creditInfo.F01%>">
                <input type="hidden" name="kyMoney" id="kyMoney" value="<%=userInfo.F06%>">
                <input type="hidden" name="syje" id="syje" value="<%=creditInfo.F07%>">
                <input type="hidden" name="minBid" id="minBid" value="<%=creditInfo.F06%>">
				<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd" value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
       			<input type="hidden" name="isTG" id="isTG" value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>

                <%
                    TermManage termManage = serviceSession.getService(TermManage.class);
                    T5017 term = termManage.get(TermType.GYJZXY);
                    if(term != null){
                %>
                <div class="mt20 ">
                    <input name="iAgree" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
                    <a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, TermType.GYJZXY.name())%>" class="highlight">《<%=term.F01.getName()%>》</a>
                    <a href="javascript:void(0);" id="" fromname="form1" style="margin-top: 5px;" class="btn01 btn_gray btn_disabled sub-btn">我要捐助</a>
                </div>
                <%}else{%>
                <div class="mt20 ">
                    <a href="javascript:void(0);" id="tbButton2" fromname="form1" class="btn01">我要捐助</a>
                </div>
                <%}}%>
                <%}%>
                <input type="hidden" id="isYuqi" name="isYuqi" value="<%=isYuqi%>">
                <%} else {%>
                <div class="f18 border_d_b tc pb5">可捐金额<br/><span class="f30"><%=isTimeEnd ? "0.00" : Formater.formatAmount(creditInfo.F07)%></span>元</div>
                <div class="f18 tc mt100">
                   	 请立即<a href="<%configureProvider.format(out,URLVariable.LOGIN);%>?_z=/financing/gyb/gybXq/<%=id %>.html"
                          class="orange">登录</a>，或<a
                        href="<%configureProvider.format(out,URLVariable.REGISTER);%>"
                        class="orange">注册</a>
                </div>
                <%}%>
            </div>
            <!--右侧-->
        </div>
    </form>

    <div class="item_details_con w1002">
        <%BidProgres[] rs = result.getItems();%>
        <!--标的详情-->
        <div class="main_tab">
            <ul class="clearfix">
                <li id="item1" onclick="setTab('item',1,4)" class="hover">倡议书</li>
                <%if (rs != null && rs.length >= 1) {%>
                <li id="item2" onclick="setTab('item',2,4)">最新进展</li>
                <%} else {%>
                <li id="item2" onclick="setTab('item',2,4)" style="display: none">最新进展</li>
                <%}%>
                <li id="item3" onclick="setTab('item',3,4)">捐助记录</li>
            </ul>
        </div>
        <div class="con" id="con_item_1">
            <!--捐助详情-->
            <%--  <div class="donor_xq f18 blue"><%=cInfo.F02%></div> --%>
            <%StringHelper.format(out, cInfo.F02, fileStore);%>
            <%--
      <div class="donor_xq02"><img src="<%=controller.getStaticPath(request)%>/images/gygg_tu2.png"/></div>
       --%>
        </div>
        <div class="con charity_details_news" id="con_item_2"
             style="display: none">
            <ul>
                <%
                    if (rs != null && rs.length >= 1) {
                        for (BidProgres cr : rs) {
                %>
                <li>
                    <div class="ico"></div>
                    <div class="time"><%=TimestampParser.format(cr.F08, "yyyy-MM-dd")%>
                    </div>
                    <p><span class="mr15">【<%=cr.F04%>】</span><span><%=cr.F06%></span>
                    </p> <%if (!StringHelper.isEmpty(cr.F09)) {%>
                    <p>
                        <a href="<%=cr.F09 %>" target="_blank" class="blue">查看更多</a>
                    </p> <%} %>
                </li>
                <%
                        }
                    }
                %>
            </ul>
        </div>

        <div class="con" id="con_item_3" style="display: none">
            <p class="f16">
                捐款加入人数<span class="orange"><%=gys.totalNum%>人</span>，捐助总额<span
                    class="orange"><%=Formater.formatAmount(gys.donationsAmount)%>元</span>
            </p>
            <%int index = 0; %>
            <table width="100%" border="0" cellspacing="0" cellpadding="0"
                   class="table mt10">
                <tr class="til">
                    <td align="center">序号</td>
                    <td align="center">捐款人</td>
                    <td align="center">捐款金额（元）</td>
                    <td align="center">捐款时间</td>
                </tr>
                <%--  <%out.print("----------输出捐助长度："+tbList.getItems().length); %> --%>
                <%
                    if (tbList != null && tbList.getItems() != null) {
                        for (Donation da : tbList.getItems()) {
                            index++;
                %>
                <tr>
                    <td align="center"><%=index%>
                    </td>
                    <td align="center"><%=da.userName.substring(0, 2) + "****" + da.userName.substring(da.userName.length()-2, da.userName.length())%>
                    </td>
                    <td align="center"><%=Formater.formatAmount(da.F04)%>
                    </td>
                    <td align="center"><%=TimestampParser.format(da.F06, "yyyy-MM-dd HH:mm:ss")%>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
            <%GybXq.rendPaging(out, tbList, controller.getPagingURI(request, GybXq.class), id);%>
        </div>
    </div>
</div>
<input type="hidden" name="isTG" id="isTG"
       value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd"
       value="<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))%>"/>
<!--E 主题内容-->
<%-- <%@include file="/WEB-INF/include/script.jsp" %> --%>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/jquery-ui.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog1.js"></script>
<%-- <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/zqzr.js"></script> --%>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/financing/sbtz.js"></script>
<script language="javascript"
        src="<%=controller.getStaticPath(request)%>/js/common.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/amountUtil.js"></script>
<input type="hidden" name="tranPwd" id="tranPwd"/>

<div class="popup_bg" style="display: none;"></div>
<div id="info1"></div>
<input type="hidden" name="isTG" id="isTG" value="<%=BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG))%>"/>
<input type="hidden" name="escrow_pre" id="escrow_pre" value="<%=BooleanParser.parse(configureProvider.format(SystemVariable.ESCROW_PREFIX))%>"/>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info1").html(showSuccInfo("<%=message%>", "successful", $("#sbSucc").val()));
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
    $("#info1").html(showDialogInfo("<%=errorMessage%>", "error"));
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
    $("#info1").html(showDialogInfo("<%=warnMessage%>", "doubt"));
    $("div.popup_bg").show();
</script>
<%
    }%>

<div class="dialog" style="display: none;">
    <div class="title"><a href="javascript:void(0);" id="cancel" class="out"></a>捐款确认</div>
    <div class="content">
        <div class="tip_information">
            <div class="successful"></div>
            <div class="tips">
                <span class="f20 gray3">您此次捐款金额为<span id="zxMoney"></span>元</span>
            </div>
            <%if (isOpenPwd) { %>
            <div class="mt20">
                <span class="red">*</span>交易密码：<input type="password" id="tran_pwd" class="text_style" autocomplete="off"/>

                <p class="red" style="display: none"></p>
            </div>
            <%} %>
        </div>
        <div class="tc mt10">
            <a href="javascript:void(0)" id="ok" class="btn01">确 定</a><a
                href="javascript:void(0)" id="cancel" class="btn01 btn_gray ml20">取 消</a>
        </div>
    </div>
</div>
<%}%>

</body>
<script type="text/javascript">
var havaRZTG = '<%=checkFlag%>';
var authText = "<%=checkMessage %>";
var _rzUrl = "<%=rzUrl%>";
    <%if(currentPage > 0){%>
    setTab('item', 3, 4);
    <%}%>
    var checkUrl = "<%=controller.getURI(request, CheckPsd.class)%>";
    var accountId = 0;
    <%if (dimengSession != null && dimengSession.isAuthenticated()) {%>
        accountId = <%=serviceSession.getSession().getAccountId()%>;
    <%}%>
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    
    function isDouble(){
    	$("#dxje").html("&nbsp;");
    	var amount = $("#amount1");
    	if(amount.val()==null||amount.val()==""||amount.val()=='undefined'){
    		return false;
    	}
    	var myreg = /^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/;
		if(!myreg.test(amount.val())){
			$("#info1").html(showDialogInfo("捐助金额输入必须为数字，且最多只能包含两位小数","doubt"));
			$("div.popup_bg").show();
			return false;
		}
    	var $minBid=$('input[name="minBid"]');
    	if(amount.val()<parseFloat($minBid.val())){
    		$("#info1").html(showDialogInfo("您的捐助金额小于起捐金额"+fmoney($minBid.val(),2)+"元。","doubt"));
    		$("div.popup_bg").show();
    		return false;
    	}
    	var kyMoney= $("#kyMoney").val();
    	if(parseFloat(kyMoney) < parseFloat(amount.val())){
    		$("#info1").html(showDialogInfo("可用金额不足。","doubt"));
    		$("div.popup_bg").show();
    		return false;
    	}
    	var syje= $("#syje").val();
    	if(parseFloat(syje) < parseFloat(amount.val())){
    		$("#info1").html(showDialogInfo("您的捐助金额大于标的剩余金额。","doubt"));
    		$("div.popup_bg").show();
    		return false;
    	}
    	if(parseFloat(syje) - parseFloat(amount.val()) <  parseFloat($minBid.val()) &&  parseFloat(syje) - parseFloat(amount.val()) >  0){
    		$("#info1").html(showDialogInfo("捐助后剩余可捐金额不能在0.00元到"+fmoney($minBid.val(),2)+"元之间。","doubt"));
    		$("div.popup_bg").show();
    		return;
    	}
		$("#dxje").html(chinaCost(amount.val()));
		return true;
    }
    
    $(function () {
    	$("#amount1").keydown(function (d) {
            var c = d.keyCode || d.charCode;
            if (onKeyResult(c)) {
            	
            } else {
                d.preventDefault();
            }
        });
    	
    	function onKeyResult(keyCode){
    	    var keyCodeArray = [8, 37, 39, 46, [47, 58], [95, 106], 110, 190];
    	    var flag = false;
    	    if(keyCode){
    	        flag = codeValidate(keyCode, keyCodeArray);
    	    }
    	    return flag;
    	}
    	
    	function codeValidate(keyCode, codeArray){
    	    var flag = false;
    	    for(var i = 0, j = codeArray.length; i < j ; i ++){
    	        if(codeArray[i].length == 2){
    	            if(keyCode > codeArray[i][0] &&  keyCode  < codeArray[i][1]){
    	                flag = true;
    	                break;
    	            }
    	        }else{
    	            if(keyCode == codeArray[i]){
    	                flag = true;
    	                break;
    	            }
    	        }
    	    }
    	    return flag;
    	}
    	
        //“我同意”按钮切回事件
        $("input:checkbox[name='iAgree']").attr("checked", false);
        $("input:checkbox[name='iAgree']").click(function() {
            var iAgree = $(this).attr("checked");
            var register = $(".sub-btn");
            if (iAgree) {
                register.removeClass("btn_gray btn_disabled");
                register.attr("id","tbButton2");
                //选中“我同意”，绑定事件
                $("#tbButton2").click(function(){
                    checkBid2();
                });
            } else {
                register.addClass("btn_gray btn_disabled");
                $("#tbButton2").unbind("click");
                register.attr("id","");
            }
        });
    });
    
    function setDMKeyup(obj){
    	$("#dxje").html(chinaCost($(obj).val()));
    }
</script>
</html>
