<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety"%>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.Bank"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.BankCard"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.service.BankCardManage"%>
<%@page import="com.dimeng.p2p.common.enums.AttestationState"%>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
</title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<%
    BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
    int id = IntegerParser.parse(request.getParameter("id"));
    BankCard bcd = bankCardManage.getBankCar(id);
    SafetyManage userManage = serviceSession.getService(SafetyManage.class);
    Safety data = userManage.get();
    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
    T6110 t6110 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
%>

<body>
	<div class="clear"></div>
	<div class="dialog bank_dialog"
		style="top: 0px; width: 700px; height: 520px; margin-left: -351px">
		<div class="title">银行卡信息</div>
		<div class="content">
			<ul class="text_list">
				<li>
					<div class="til">
						<span class="red">*</span>开户名：
					</div>
					<div class="con">
						<span class="red"></span> <input type="hidden" id="userName"
							name="userName" value="<%=data.name%>" />
						<%
						    if (T6110_F06.ZRR == t6110.F06)
						        {
						%>
						<input type="hidden" name="type" value="1" />
						<%=data.name%>
						<%
						    }
						        else
						        {
						%>
						<select name="type" id="type" class="select_style jy_w1"
							disabled="disabled">
							<option value="1" <%if (bcd.type == 1)
                    {%>
								selected="selected" <%}%>><%=data.name%>
							</option>
							<option value="2" <%if (bcd.type == 2)
                    {%>
								selected="selected" <%}%>><%=data.qyName%>
							</option>
						</select>
						<%
						    }
						%>
					</div>
				</li>
				<li>
					<div class="til">
						<span class="red">*</span>银行卡号：
					</div>
					<div class="con">
						<span class="red"></span>
						<%
						    StringHelper.filterHTML(out,
						            bcd.bankNumber.substring(0, 4) + " *** *** "
						                + bcd.bankNumber.substring(bcd.bankNumber.length() - 4, bcd.bankNumber.length()));
						%>
					</div>

				</li>
				<li>
					<div class="til">
						<span class="red">*</span>开户银行：
					</div>
					<div class="con">
						<select name="bankname" class="select_style jy_w1"
							disabled="disabled">
							<option value="1"><%=bcd.bankname%></option>
						</select>
					</div>
				</li>
				<li>
					<div class="til">
						<span class="red">*</span>开户行所在地：
					</div>
					<div class="con">
						<select name="shengX" class="select_style jy_w1"
							style="width: 120px;" disabled="disabled">
							<option selected><%=bcd.province%></option>
						</select> <select name="shengX" class="select_style jy_w1" disabled="disabled"
							style="width: 120px;">
							<option selected><%=bcd.county%></option>
						</select>
					</div>
				</li>
				<li>
					<div class="til">
						<span class="red">*</span>开户行：
					</div>
					<div class="con">
						<input type="hidden" value="<%=id%>" name="id" /> <input
							type="text" name="subbranch" value="<%=bcd.bankKhhName%>" disabled="disabled"
							id="textfield" class="text_style border  required max-length-60" />
					</div>
				</li>
			</ul>
			<div class="tc mt20">
				<input type="submit" class="btn01 ml20 "
					onclick="var list=parent.art.dialog.list;for(var i in list)list[i].close();"
					value="返回" />
			</div>
			<div class="prompt_mod mt20">
				<span class="highlight">温馨提示：</span><br>
				1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
				2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网店询问或上网查询。<br> 3.不支持提现至信用卡账户
				
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/include/script.jsp"%>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
</body>
</html>
