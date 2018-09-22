<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.enums.T6230_F10" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%=configureProvider
            .getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    String daysOfYear =  configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR);
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
    <div class="w1002">
        <div class="main_tab">
            <ul class="clearfix">
                <li id="calcul1" onclick="location.href='<%configureProvider.format(out, URLVariable.LC_LCJSQ); %>'">
                    理财计算器
                </li>
                <li id="calcul2" class="hover">借款计算器</li>
            </ul>
        </div>
    </div>
    <div class="main_mod" id="con_calcul_2">
        <form action="" method="get" class="form1">
            <div class="main_form mt50 calculator_form">
                <ul>
                    <li class="item">
                        <div class="til">借款金额：</div>
                        <div class="con">
                            <input name="" type="text"
                                   class="text jejs required isint min-size-1"
                                   maxlength="11"/>元
                            <input id="min_amount" type="hidden"
                                   value="1"/>

                            <p tip class="gray9">输入金额为正整数</p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til">年化利率：</div>
                        <div class="con">
                            <input name="rating" type="text"
                                   class="text required minf-size-1 maxf-size-24 jejs isnumber"
                                   mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="只能有两位小数" value="1"/>%
                            <p tip class="gray9">利率精确到小数点后两位，范围1%-24%之间</p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item mb30">
                        <div class="til">还款方式：</div>
                        <div class="con">
                            <select name="select" id="select" class="select2 jejs">
                                <%for (T6230_F10 f10 : T6230_F10.values()) {
                                    if(f10 == T6230_F10.YCFQ){
                                        continue;
                                    }
                                    if (f10 == T6230_F10.DEBX) { %>
                                <option value="<%=f10 %>" selected="selected"><%=f10.getChineseName() %></option>
                                <%} else { %>
                                <option value="<%=f10 %>"><%=f10.getChineseName() %></option>
                                <%}} %>
                                <option value="YCFQ_M">本息到期一次付清（按月）</option>
                                <option value="YCFQ_D">本息到期一次付清（按天）</option>
                            </select>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til">借款期限：</div>
                        <div class="con">
                            <input name="" id="jkqx" type="text" class="text jejs required isint  min-size-1 max-size-36"  maxlength="11"/><span id="company">个月</span>
                            <p tip class="gray9">借款期限为1-36个月</p>
                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item mb50">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <a href="javascript:void(0);" class="btn06 jsqks sumbitForme" fromname="form1">开始计算</a>
                        </div>
                    </li>
                </ul>
            </div>
        </form>
        <div class="calculator_notes" style="display: none;" id="showjs">
            <div class="main_hd"><i class="icon"></i><span class="gray3 f18">借款描述</span></div>
            <div class="loanamount clearfix">
                <div class="til">
                    <p>借款金额：<span class="orange bold" id="mtoal">0.0</span>元</p>

                    <p>应还利息：<span class="orange bold" id="ghbx">0.0</span>元</p>
                </div>
                <div class="info"><i class="ico"></i>您将在<span class="f18 orange bold" id="mthns">5</span>后还清借款</div>
            </div>
            <div class="main_hd"><i class="icon"></i><span class="gray3 f18">本息偿还时间表</span></div>
            <div class="calculator_table">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jstr">

                </table>
            </div>
        </div>
        <div class="calculator_notes">
            <ul>
                <li>等额本息：等额本息还款法是在每月还款期内，每月偿还同等金额的借款（包括本金和利息）。借款人每月还款额中的本金比重逐月递增，利息比重逐月递减。</li>
                <li>每月付息、到期还本：每月付息、到期还本还款法是指每月偿还相同额度的利息，借款到期日一次性归还借款本金。</li>
                <li>本息到期一次付清：本息到期一次付清还款法是指在借款期内不是按月偿还本息，而是借款到期后一次性归还本金和利息。</li>
                <li>等额本金：等额本金还款法是指在还款期内把借款总额等分，每月偿还同等数额的本金和剩余借款在该月所产生的利息，每月的还款本金额固定，而利息越来越少。</li>
                <li>使用利息计算器，能帮您计算每月的本息情况；同时，一份完整的本息偿还时间表，让您能更直观地了解还款本息详情 。</li>
            </ul>
        </div>
    </div>

</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    //还款计算:年表示的天数
    var days_of_year = '<%=daysOfYear%>';
</script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/jkjsq.js"></script>
</body>
</html>
