<%@page import="com.dimeng.p2p.user.servlets.account.GetBankForAjax"%>
<%@page import="com.dimeng.p2p.user.servlets.account.BankcardById"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety"%>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.Bank"%>
<%@page import="com.dimeng.p2p.account.user.service.BankCardManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.BankCard" %>
<%@page import="com.dimeng.p2p.common.enums.BankCardStatus" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Deletebankcard" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Bankcardlist" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Addbankcard" %>
<%@ page import="com.dimeng.p2p.user.servlets.account.Editbankcard" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
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
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));

    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "YHKXX";

    Map<String, String> bankMap = new HashMap<String, String>();
    bankMap.put("中国银行", "BOC");
    bankMap.put("工商银行", "ICBC");
    bankMap.put("农业银行", "ABC");
    bankMap.put("建设银行", "CCB");
    bankMap.put("招商银行", "CMB");

    bankMap.put("交通银行", "BOCOM");
    bankMap.put("中国邮政储蓄银行", "PSBC");
    bankMap.put("中国民生银行", "CMBC");
    bankMap.put("中国光大银行", "CEB");
    bankMap.put("平安银行", "PINGAN");
    bankMap.put("上海浦东银行", "SPDB");
 	// 增加对托管的区分
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);

    boolean checkFlag=true;
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
                <div class="f16 mt30 pt50 pb50 tc ml30 mr30"
                     style="background: #f3f3f3;">
                    您需要在第三方托管平台上进行注册，才可管理银行卡！请<a href="<%=rzUrl %>"
                                                  class="red">立即注册</a>！
                </div>
                <%} else { %>
                <ul class="bank_manage_list clearfix">
                    <%
                        for (BankCard b : card) {
                            if (bankMap.containsKey(b.Bankname)) {
                    %>
                    <li>
                        <div class="pic">
                            <img
                                    src="<%=controller.getStaticPath(request)%>/images/bank_logo_<%=bankMap.get(b.Bankname) %>.png"
                                    width="148" height="38">
                        </div>
                        <div class="number">
                            <%StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
                        </div>
                        <div class="delete clearfix">
                        <%if(!"yeepay".equalsIgnoreCase(escrow)){%>
                            <a href="javascript:void(-1);"
                               onclick="updateCard('<%=b.id%>');" class="fl">修改</a>
                         <%} %>      
                            <%-- <%if (tg) { %>
                            <a
                                    href="<%=configureProvider.format(URLVariable.ESCROW_URL_UNBINDCARD) %>?id=<%=b.id %>"
                                    class="fr">删除</a>
                            <%} else { %> --%>
                            <% if (b.jbRequestNo == null || b.jbRequestNo.isEmpty()) { %>
                            <a href="javascript:void(-1);" onclick="deletecard(<%=b.id %>);"
                               class="fr">删除</a>
                               <%} else { %>
                            <span class="fc red">解绑中</span>
                            <%} %>
                            <%-- <%} %> --%>
                        </div>
                    </li>
                    <%} else {%>
                    <li>
                        <div class="pic"
                             title="<%StringHelper.filterHTML(out, b.Bankname);%>">
                            <%StringHelper.truncation(out, b.Bankname, 6, "***");%>
                        </div>
                        <div class="number">
                            <%
                                StringHelper.filterHTML(out, b.BankNumber.substring(0, 4) + " *** *** " + b.BankNumber.substring(b.BankNumber.length() - 4, b.BankNumber.length()));%>
                        </div>
                        <div class="delete clearfix">
                        <%if(!"yeepay".equalsIgnoreCase(escrow)){%>
                            <a href="javascript:void(-1);"
                               onclick="updateCard('<%=b.id%>');" class="fl">修改</a>
                        <%}%>
                            <%-- <%if (tg) { %>
                            <a
                                    href="<%=configureProvider.format(URLVariable.ESCROW_URL_UNBINDCARD) %>?id=<%=b.id %>"
                                    class="fr">删除</a>
                            <%} else { %> --%>
                            <% if (b.jbRequestNo == null || b.jbRequestNo.isEmpty()) { %>
                            <a href="javascript:void(-1);" onclick="deletecard(<%=b.id %>);"
                               class="fr">删除</a>
                               <%} else { %>
                            <span class="fc red">解绑中</span>
                            <%} %>
                            <%-- <%} %> --%>
                        </div>
                    </li>
                    <%
                            }
                        }
                    %>
                    <%if (card.length < IntegerParser.parse(configureProvider.getProperty(SystemVariable.MAX_BANKCARD_COUNT))) {%>
                    <%if (tg) { %>
                    <li class="add"><a target="_blank"
                                       href="<%=configureProvider.format(URLVariable.ESCROW_URL_BINDCARD) %>">
                        <i class="add_ico"></i>

                        <p>添加银行卡</p>
                    </a></li>
                    <%} else { %>
                    <li class="add"><a href="javascript:void(-1);"
                                       onclick="addCard(0);"> <i class="add_ico"></i>

                        <p>添加银行卡</p>
                    </a></li>
                    <%} %>
                    <%}%>
                    <div class="clear"></div>
                </ul>
                <%} %>
            </div>
            <div class="mt20 mb30" style="padding:15px 20px; background:#f8f8f8; line-height:24px;">
            <span class="highlight">温馨提示：</span><br>
            1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
            2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网点询问或上网查询。<br>
            3.不支持提现至信用卡账户。
            </div>
            <%
                if (!checkFlag) {
            %>

            <div id="smrz" style="display: none;">
                <div class="popup_bg"></div>
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
<div class="dialog" style="display: none;" id="delc">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>提示</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
                <input type="hidden" name="delId" id="delId">
                <span class="f20 gray3">是否确认删除银行卡?</span>
            </div>
        </div>
        <div class="tc mt20">
        	<%if (tg && escrow.equals("yeepay")) { %>
				<%for (BankCard b : card) {%>
	        	<a href="<%=configureProvider.format(URLVariable.ESCROW_URL_UNBINDCARD) %>?id=<%=b.id %>" class="btn01">是</a>
	        	<%} %>
        	<%} else { %>
        		<a href="javascript:okDelete();" class="btn01">是</a>
        	<%} %>
        	<a href="javascript:void(0);" class="btn01 btn_gray ml20 close">否</a>
        </div>
    </div>
</div>

<div id="bankDialog" class="dialog bank_dialog" style="display: none;">
</div>

<%
	Bank[] bank = bankCardManage.getBank();
	SafetyManage userManage = serviceSession.getService(SafetyManage.class);
	Safety data = userManage.get();
%>
<div id="editBank" class="dialog bank_dialog" style="display: none;">
<form id="form2" action="" class="form2" method="post">
        <div class="title"><a  class="out close" href="javascript:void(0);"></a>修改银行卡</div>
        <div class="content">
            <ul class="text_list">
                <li>
                    <div class="til"><span class="red">*</span>开户名：</div>
                    <div class="con"><span class="red"></span>
                        <input type="hidden" name="userName" value="<%=data.name %>"/>
                        <%if (T6110_F06.ZRR == t6110.F06) { %>
                        <input type="hidden" name="type" value="1"/>
                        <%=data.name%>
                        <%} else { %>
                        <select name="type" id="updateType" class="select8">
                            <option value="1" selected="selected"><%=data.name %>
                            </option>
                            <option value="2" ><%=data.qyName %>
                            </option>
                        </select>
                        <%} %>
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>银行卡号：</div>
                    <div class="con" id="bankNum"></div>

                </li>
                <li>
                    <div class="til"><span class="red">*</span>选择银行：</div>
                    <div class="con">
                        <select name="bankname" id="bankname" class="select8">
                            <%if (bank != null && bank.length > 0) for (Bank b : bank) { %>
                            <option value="<%=b.id%>" ><%
                                StringHelper.filterHTML(out, b.name);%></option>
                            <%}%>
                        </select></div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>开户行所在地：</div>
                    <div class="con" id="region">
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>开户行：</div>
                    <div class="con"><input type="hidden" value="" name="id"/><input type="text" name="subbranch"
                                                                                            value=""
                                                                                            class="text_style border  required max-length-60"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                </li>
            </ul>
            <div class="tc mt20">
                <input type="button" fromname="form2" class="btn01 sumbitForme" onclick="updateCardConfirm();" value="提交"/>
                <input type="button" class="btn01 btn_gray ml20 close"  value="取消"/>
            </div>
            <div class="prompt_mod mt20">
                <span class="highlight">温馨提示：</span><br>
                1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
                2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网店询问或上网查询。<br>
                3.不支持提现至信用卡账户
            </div>
        </div>
</form>
</div>

<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=message%>', "error"));
</script>
<%} %>
<%
    String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(messageInfo)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=messageInfo%>', "successful"));
</script>
<%} %> 

<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<input type="hidden" id="bankUrl" value="<%=controller.getURI(request, GetBankForAjax.class)%>"/>
<input type="hidden" id="addBankCardUrl" value="<%=controller.getURI(request, Addbankcard.class)%>"/>
<input type="hidden" id="reloadUrl" value="<%=controller.getURI(request, Bankcardlist.class)%>"/>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/account/addBankCard.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/region.js"></script>        
<script type="text/javascript">
    function deletecard(id) {
        $(".popup_bg").show();
        $("#delId").val(id);
        $("#delc").show();
        <%-- artDialog.confirm("是否确认删除银行卡?",function(){
            var data={"value":id};
            $.ajax({
                type:"post",
                dataType:"html",
                url:"<%=controller.getURI(request, Deletebankcard.class)%>",
                data:data,
                success:function(data){
                    location.href="<%=controller.getViewURI(request, Bankcardlist.class)%>";
                }
            });
        },function(){}); --%>
    }

    function okDelete() {
        var id = $("#delId").val();
        var data = {"value": id};
        $.ajax({
            type: "post",
            dataType: "html",
            url: "<%=controller.getURI(request, Deletebankcard.class)%>",
            data: data,
            success: function (data) {
                location.href = "<%=controller.getViewURI(request, Bankcardlist.class)%>";
            }
        });
    }

    $(".close").click(function () {
        $("div.dialog").hide();
        $("div.popup_bg").hide();
    });

    var banknameSelect;
    var updateTypeSelect;
    $(function () {
    	banknameSelect = $('#bankname').selectlist({
    		width: 232,
    		optionHeight: 28,
    		height: 28
    	});
    	updateTypeSelect = $('#updateType').selectlist({
    		width: 232,
    		optionHeight: 28,
    		height: 28,
    		onChange:function(){
    			if ($("#editBank input[name='type']").val() == 2) {
                    $("#editBank input[name='userName']").val("<%=data.qyName%>");
                } else {
                    $("#editBank input[name='userName']").val("<%=data.name%>");
                }
    		}
    	});
        
        
    });
    

    /**
     * 打开添加银行卡弹框
     */
    function addCard(id) {
        if ('false' == '<%=checkFlag%>') {
            $("#smrz").show();
            return false;
        }
        $("#region").html("");
        showBankCard();
        
       <%--  global_art = art.dialog.open("<%=controller.getViewURI(request, Addbankcard.class)%>", {
            id: 'addCard',
            title: '添加银行卡',
            opacity: 0.1,
            width: 700,
            height: 520,
            padding: 0,
            lock: true,
            close: function () {
                window.location.reload();
            }

        }, false); --%>
    }
    

    /**
     * 打开修改银卡卡弹框
     */
    function updateCard(id) {
        if ('false' == '<%=checkFlag%>') {
            $("#smrz").show();
            return false;
        }
        $("#bankDialog").html("");
        var region = "<input type=\"hidden\" id=\"shengId\" value=\"\">";
        region += "<input type=\"hidden\" id=\"shiId\" value=\"\">";
        region += "<input type=\"hidden\" id=\"xianId\" value=\"\">";
        region += "<select name=\"sheng\" id=\"sheng\" class=\"required select6\" ></select>";
        region += "<select name=\"shi\" id=\"shi\" class=\" select6\" ></select>";
        region += "<select name=\"xian\" id=\"xian\" class=\"required select6\"></select>";
        region += "<p tip></p>";
        region += "<p errortip class=\"\" style=\"display: none\"></p>";
        $("#region").html(region);
        $.ajax({
            type: "post",
            dataType: "json",
            url: "<%=controller.getURI(request, BankcardById.class)%>",
            data: {"id":id},
            success: function (data) {
            	if(data != null){
            		if(data.num == '0000'){
	            		var bankCard = data.bankCard;
	            		var bankNum = bankCard.BankNumber.substring(0, 4) + " *** *** " + bankCard.BankNumber.substring(bankCard.BankNumber.length - 4, bankCard.BankNumber.length);
	            		//开户名
	            		if($("#updateType").length > 0){
	            			
	            			updateTypeSelect[0].setSelectValue(bankCard.type);
		            		
		            		if ($("#editBank input[name='type']").val() == 2) {
		                        $("#editBank input[name='userName']").val("<%=data.qyName%>");
		                    } else {
		                        $("#editBank input[name='userName']").val("<%=data.name%>");
		                    }
	            		}
	            		
	            		$("#bankNum").html("<span class='red'></span>"+bankNum);
	            		banknameSelect[0].setSelectValue(bankCard.BankID);
	            		
	            		if(bankCard.City != null && bankCard.City != ""){
	            			$("#shengId").val(bankCard.City.substring(0,2)+"0000");
	            			$("#shiId").val(bankCard.City.substring(0,4)+"00");
	            			$("#xianId").val(bankCard.City);
	            			initRegion();
	            		}
	            		$("#editBank input[name='id']").val(id);
	            		$("#editBank input[name='subbranch']").val(bankCard.BankKhhName);
	            		
	            		$("#editBank").show();
	                    $(".popup_bg").show();
	                    $("p[errortip]").hide();
	                    $("p[tip]").show();
	                    //初始化表单校验
	                    initVal();
            		}else if(data.num == '0002') {
                		$('.popup_bg').show();
                		$('#info').html(showSuccInfo(data.msg,'error',loginUrl));
                	}else{
            			$('#info').html(showDialogInfo(data.msg, 'error'));
            			$('div.popup_bg').show();
            		}
            	}else{
            		$("#info").html(showDialogInfo("银行卡信息不存在", "error"));
        			$("div.popup_bg").show();
            	}
            },
    		error: function(XMLHttpRequest, textStatus, errorThrown){
    			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
    		}
        });

        <%-- global_art = art.dialog.open("<%=controller.getViewURI(request, Editbankcard.class)%>?id=" + id, {
            id: 'addCard',
            title: '修改银行卡',
            opacity: 0.1,
            width: 700,
            height: 520,
            lock: true,
            close: function () {
                window.location.reload();
            }

        }, false); --%>
    }

    /**
     * 提交修改银行卡信息
     */
    function updateCardConfirm(){
    	if(submitVal("form2")){
	    	$.ajax({
	            type: "post",
	            dataType: "json",
	            url: "<%=controller.getURI(request, Editbankcard.class)%>",
	            data: $("#form2").serialize(),
	            success: function (data) {
	            	if(data != null){
	            		$("#editBank").hide();
	            		if(data.num == "0000"){
	            			$("#info").html(showSuccInfo(data.msg, "successful","<%=controller.getViewURI(request, Bankcardlist.class)%>"));
	            		    $("div.popup_bg").show();
	            		}else if(data.num == '0002') {
	                		$('div.popup_bg').show();
	                		$('#info').html(showSuccInfo(data.msg,'error',loginUrl));
	                	}else{
	            			$("#info").html(showDialogInfo(data.msg, "error"));
	            			$("div.popup_bg").show();
	            		}
	            	}
	            },
	    		error: function(XMLHttpRequest, textStatus, errorThrown){
	    			$("#editBank").hide();
	    			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
	            		$(".popup_bg").show();
	            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
	            	}else if(XMLHttpRequest.responseText.indexOf("非法字符")>-1){
	        			$("#info").html(showDialogInfo("当前请求中存在非法字符，请重新输入！", "error"));
	        			$("div.popup_bg").show();
	        		}else if(XMLHttpRequest.responseText != "") {
	            		$(".popup_bg").show();
	            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
	            	}else{
	            		$(".popup_bg").show();
	            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
	            	}
	    		}
	        });
    	}
    }
    
    

    function hidebg(id) {
        $("#" + id).hide();
    }

</script>
</body>
</html>
