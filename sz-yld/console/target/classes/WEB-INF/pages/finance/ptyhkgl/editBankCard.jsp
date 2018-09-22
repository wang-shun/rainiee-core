<%@ page import="com.dimeng.p2p.modules.account.console.service.BankCardManage" %>
<%@ page import="com.dimeng.p2p.modules.account.console.service.entity.Bank" %>
<%@ page import="com.dimeng.p2p.modules.account.console.service.entity.BankCard" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.EditBankCard" %>
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
if (request.getAttribute("close") != null) {
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
<form action="<%=controller.getURI(request, EditBankCard.class)%>" class="form1" method="post">
    <div class="clear"></div>
    <div>
        <div class="p30">
            <ul class="gray6">
                <li class="mb15"><span class="display-ib w120 tr">开户名：</span>
                    <span class="">
                        <input type="text" id="userName" name="userName" class="text border" value="<%=bcd.userName %>"/>
                        </span><span>
                        <select name="type" id="type" class="text border required">
                            <option value="1" <%if(bcd.type == 1){ %>selected="selected"<%} %>>个人
                            </option>
                            <option value="2" <%if(bcd.type == 2){ %>selected="selected"<%} %>>公司
                            </option>
                        </select></span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>银行卡号：</span>
                    <span class="con"><span
                            class="red"></span><%StringHelper.filterHTML(out, bcd.BankNumber.substring(0, 4) + " *** *** " + bcd.BankNumber.substring(bcd.BankNumber.length() - 4, bcd.BankNumber.length()));%>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>选择银行：</span>
                    <span class="con">
                        <select name="bankname" class="text border">
                            <%if (bank != null && bank.length > 0) for (Bank b : bank) { %>
                            <option value="<%=b.id%>" <%if (bcd.BankID == b.id) {%> selected="selected" <%}%> ><%
                                StringHelper.filterHTML(out, b.name);%>
                            </option>
                            <%}%>
                        </select>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>开户行所在地：</span>
                    <span>
                        <input type="hidden" id="shengId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City.substring(0,2)+"0000");}%>">
                        <input type="hidden" id="shiId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City.substring(0,4)+"00");}%>">
                        <input type="hidden" id="xianId"
                               value="<%if(!StringHelper.isEmpty(bcd.City)){StringHelper.filterHTML(out, bcd.City);}%>">
                        <select name="sheng" class="text border mr5" style="width:120px;"></select><span></span>
                        <select name="shi" class="text border mr5" style="width:120px;"></select><span></span>
                        <select name="xian" class="text border mr5 required" style="width:120px;"></select>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>开户行：</span>
                    <span class="con"><input type="hidden" value="<%=id%>" name="id"/>
                        <input type="text" name="subbranch" value="<%=bcd.BankKhhName%>" id="textfield" class="text border  required"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </span>
                </li>
            </ul>
            <div class="tc mt20">
                <input type="submit" fromname="form1" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="提交"/>
                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                       onclick="var list=parent.art.dialog.list;for(var i in list)list[i].close();" value="取消"/>
            </div>
            <div class="prompt_mod mt20 lh26 gray6">
                <p class="highlight">温馨提示：</p>
                1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>
                2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网店询问或上网查询。<br>
                3.不支持提现至信用卡账户
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript">
    $(function () {

    });
</script>
</body>
</html>