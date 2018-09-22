<%@ page import="com.dimeng.p2p.modules.account.console.service.BankCardManage" %>
<%@ page import="com.dimeng.p2p.modules.account.console.service.entity.Bank" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.AddBankCard" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<%
    BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
    Bank[] bank = bankCardManage.getBank();
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
<%String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);%>
<form action="<%=controller.getURI(request, AddBankCard.class)%>" class="form1" method="post" autocomplete="off">
    <div class="clear"></div>
    <div>
        <div class="p30">
            <ul class="gray6">
                <li class="mb15"><span class="display-ib w120 tr">开户名：</span>
                    <span class="">
                        <input type="text" id="userName" name="userName" class="text border required"/>
                        </span><span>
                        <select name="type" id="type" class="text border">
                            <option value="1">个人
                            </option>
                            <option value="2">公司
                            </option>
                        </select></span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>选择银行：</span>
                    <span class="con">
                        <select name="bankname" class="text border">
                            <%if (bank != null && bank.length > 0) for (Bank b : bank) { %>
                            <option value="<%=b.id%>" >
                                <%StringHelper.filterHTML(out, b.name);%>
                            </option>
                            <%}%>
                        </select>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>开户行所在地：</span>
                    <span>
                        <select name="sheng" class="text border mr5" style="width:120px;"></select><span></span>
                        <select name="shi" class="text border mr5" style="width:120px;"></select><span></span>
                        <select name="xian" class="text border mr5 required" style="width:120px;"></select>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>开户行：</span>
                    <span class="con"><input type="hidden"  name="id"/>
                        <input type="text" name="subbranch" id="textfield" class="text border  required"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </span>
                </li>
                <li class="mb15">
                    <span class="display-ib w120 tr"><span class="red">*</span>银行卡号：</span>
                    <span class="con">
                        <input type="text" name="banknumber" id="textfield2" class="text border required max-length-30"
                               onkeyup="this.value=this.value.replace(/\D/g,'').replace(/....(?!$)/g,'$& ')"/>
                        <p tip></p>
                        <p errortip class="red" style=""><%StringHelper.filterHTML(out, ermsg); %></p>
                    </span>
                </li>
                <li>
                    <span class="display-ib w120 tr"><span class="red">*</span>确认卡号：</span>
                    <span class="con">
                        <input type="text" name="cbanknumber" onpaste="return false" id="textfield4" class="text border required "
                               onkeyup="this.value=this.value.replace(/\D/g,'').replace(/....(?!$)/g,'$& ')"/>
                        <p tip></p>
                        <p errortip class="red" style="display: none"><%StringHelper.filterHTML(out, ermsg); %></p>
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
    function fncKeyStop(evt) {
        if (!window.event) {
            var keycode = evt.keyCode;
            var key = String.fromCharCode(keycode).toLowerCase();
            if (evt.ctrlKey && key == "v") {
                evt.preventDefault();
                evt.stopPropagation();
            }
        }
    }

    $(function () {
        $("#type").bind("change", function () {

        });
    });
</script>
</body>
</html>
