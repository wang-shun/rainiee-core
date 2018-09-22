<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Bank" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.BankCard" %>
<%@page import="com.dimeng.p2p.account.user.service.BankCardManage" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Editbankcard" %>
<%@page import="com.dimeng.p2p.common.enums.AttestationState" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
    Bank[] bank = bankCardManage.getBank();
    int id = IntegerParser.parse(request.getParameter("id"));
    BankCard bcd = bankCardManage.getBankCar(id);
    SafetyManage userManage = serviceSession.getService(SafetyManage.class);
    Safety data = userManage.get();
    UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
    T6110 t6110 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
    if (data.isIdCard.equals(AttestationState.WYZ.name()) || StringHelper.isEmpty(data.txpassword) || request.getAttribute("close") != null) {
%>
<script type="text/javascript">
    var list = parent.art.dialog.list;
    for (var i in list) {
        list[i].close();
    }
</script>
<%
        return;
    }

    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "YHKXX";
%>
<body>
<form action="<%=controller.getURI(request, Editbankcard.class)%>" class="form1" method="post">
    <div class="clear"></div>
    <div class="dialog bank_dialog" style="top:0px;width:700px;height: 520px;margin-left:-351px">
        <div class="title">修改银行卡</div>
        <div class="content">
         <%
        if(null != bcd){
        %>
            <ul class="text_list">
                <li>
                    <div class="til"><span class="red">*</span>开户名：</div>
                    <div class="con"><span class="red"></span>
                        <input type="hidden" id="userName" name="userName" value="<%=data.name %>"/>
                        <%if (T6110_F06.ZRR == t6110.F06) { %>
                        <input type="hidden" name="type" value="1"/>
                        <%=data.name%>
                        <%} else { %>
                        <select name="type" id="type" class="select_style jy_w1">
                            <option value="1" <%if(bcd.type == 1){ %>selected="selected"<%} %>><%=data.name %>
                            </option>
                            <option value="2" <%if(bcd.type == 2){ %>selected="selected"<%} %>><%=data.qyName %>
                            </option>
                        </select>
                        <%} %>
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>银行卡号：</div>
                    <div class="con"><span
                            class="red"></span><%StringHelper.filterHTML(out, bcd.BankNumber.substring(0, 4) + " *** *** " + bcd.BankNumber.substring(bcd.BankNumber.length() - 4, bcd.BankNumber.length()));%>
                    </div>

                </li>
                <li>
                    <div class="til"><span class="red">*</span>选择银行：</div>
                    <div class="con">
                        <select name="bankname" class="select_style jy_w1">
                            <%if (bank != null && bank.length > 0) for (Bank b : bank) { %>
                            <option value="<%=b.id%>" <%if (bcd.BankID == b.id) {%> selected="selected" <%}%> ><%
                                StringHelper.filterHTML(out, b.name);%></option>
                            <%}%>
                        </select></div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>开户行所在地：</div>
                    <div class="con">
                        <input type="hidden" id="shengId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City.substring(0,2)+"0000");}%>">
                        <input type="hidden" id="shiId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City.substring(0,4)+"00");}%>">
                        <input type="hidden" id="xianId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City);}%>">
                        <select name="sheng" class="select_style jy_w1" style="width:120px;">
                        </select>
                        <select name="shi" class="select_style jy_w1" style="width:120px;">
                        </select>
                        <select name="xian" class="select_style jy_w1 required" style="width:120px;">
                        </select>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span>开户行：</div>
                    <div class="con"><input type="hidden" value="<%=id%>" name="id"/><input type="text" name="subbranch"
                                                                                            value="<%=bcd.BankKhhName%>"
                                                                                            id="textfield"
                                                                                            class="text_style border  required max-length-60"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                </li>
            </ul>
            <div class="tc mt20">
                <input type="submit" fromname="form1" class="btn01 sumbitForme" value="提交"/>
                <input type="button" class="btn01 btn_gray ml20 "
                       onclick="var list=parent.art.dialog.list;for(var i in list)list[i].close();" value="取消"/>
            </div>
            <div class="prompt_mod mt20">
                <span class="highlight">温馨提示：</span><br>
                1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
                2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网店询问或上网查询。<br>
                3.不支持提现至信用卡账户
            </div>
            <%
            }else{
            %>
            <div class="tc mt20">银行卡信息不存在</div>
            <%}%>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript">
    $(function () {
        if ($("#type").val() == 2) {
            $("#userName").val("<%=data.qyName%>");
        } else {
            $("#userName").val("<%=data.name%>");
        }
        $("#type").bind("change", function () {
            if ($(this).val() == 2) {
                $("#userName").val("<%=data.qyName%>");
            } else {
                $("#userName").val("<%=data.name%>");
            }
        });
    });
</script>
</body>
</html>
