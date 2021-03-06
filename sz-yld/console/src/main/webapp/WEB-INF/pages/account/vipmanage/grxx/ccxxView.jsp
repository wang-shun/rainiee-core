<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.TbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JkjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.KxrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ByrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrxlxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrgzxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.FcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.CcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.TenderRecord" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BasicInfo" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.S61.entities.T6113" %>
<%@page import="org.codehaus.jackson.map.ObjectMapper" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "GRXX";
    BasicInfo basicInfo = ObjectHelper.convert(request.getAttribute("basicInfo"), BasicInfo.class);
    PagingResult<T6113> t6113s = ObjectHelper.convert(request.getAttribute("resultCcxx"), PagingResult.class);
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String returnUrl = controller.getURI(request, GrList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        returnUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户详细信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <ul class="gray9 input-list-container clearfix">
                                <li><span class="display-ib mr5">用户名：</span><span
                                        class="display-ib mr40 gray3"><%StringHelper.filterHTML(out, basicInfo.userName);%></span>
                                </li>
                                <li><span class="display-ib mr5">账户余额：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.accountBalance)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">必要认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int byrztg = basicInfo.byrztg;
                    if (byrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=byrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">借款负债：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.borrowingLiability)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">注册时间：</span><span
                                        class="display-ib mr40 gray3"><%=TimestampParser.format(basicInfo.registrationTime)%></span>
                                </li>
                                <li><span class="display-ib mr5">净资产：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.netAssets)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">理财资产：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.lczc)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">可选认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int kxrztg = basicInfo.kxrztg;
                    if (kxrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=kxrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.overdueCount%></span></li>
                                <li><span class="display-ib mr5">严重逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.seriousOverdue%></span></li>
                                <%if(is_business){ %>
                                <li><span class="display-ib mr5">业务员工号：</span><span class="display-ib mr40 gray3"><%=StringHelper.isEmpty(basicInfo.employNum)?"无":basicInfo.employNum %></span></li>
                                <%} %>
                            </ul>
                        </div>
                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix border-b-s">
                                <li>
                                    <a href="<%=controller.getURI(request, JbxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">基本信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrxlxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人学历信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrgzxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人工作信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, FcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">房产信息</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">车产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=byrztg%>/<%=basicInfo.needAttestation%>）</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, KxrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">可选认证（<%=kxrztg%>/<%=basicInfo.notNeedAttestation%>）</a></li>
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, JkjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">借款记录</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, TbjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">投资记录</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">

                            <!--个人学历信息-->
                           <form action="<%=controller.getURI(request, CcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                 method="post">
                            <div class="tab-item">
                                <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title tc">
                                                <th>序号</th>
                                                <th>汽车品牌</th>
                                                <th>车牌号码</th>
                                                <th>购车年份</th>
                                                <th>购买价格(万元)</th>
                                                <th>评估价值(万元)</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                T6113[] t6113Array = t6113s.getItems();
                                                if (t6113Array != null) {
                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                    Map<String, Object> jsonMap = new HashMap<String, Object>();
                                                    String result = null;
                                                    int i = 0;
                                                    for (T6113 t6113 : t6113Array) {
                                                        i++;
                                                        jsonMap.put("t6113", t6113);
                                                        result = objectMapper.writeValueAsString(jsonMap);
                                            %>

                                            <tr class="tc">
                                                <td><%=i%>
                                                </td>
                                                <td><%=t6113.F03%>
                                                </td>
                                                <td><%=t6113.F04%>
                                                </td>
                                                <td><%=t6113.F05%>年</td>
                                                <td><%=Formater.formatAmount(t6113.F06)%>
                                                </td>
                                                <td><%=Formater.formatAmount(t6113.F07)%>
                                                </td>
                                                <td><a href='javascript:void(0);' onclick='showDetail(<%=result%>);'
                                                       class="link-blue mr20">详情</a></td>
                                            </tr>
                                            <% }
                                            } else {%>
                                            <tr>
                                                <td colspan="7" class="tc">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>
                                </div>
                            </div>
                            <%
                                AbstractConsoleServlet.rendPagingResult(out, t6113s);
                            %>
                            </form>
                            <div class="border p30 Men_bt" id="t6113_detail" style="display:none;">
                                <ul>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>  汽车品牌：</span>
                                        <span class="info" id="qcpp"></span>

                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span> 车牌号码：</span>
                                        <span class="info" id="cphm"></span>

                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span> 购车年份：</span>
                                        <span class="info" id="gmnf"></span>

                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span> 购买价格(万元)：</span>
                                        <span class="info" id="gmjg"></span>

                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span> 评估价值(万元)：</span>
                                        <span class="info" id="pgjz"></span>

                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5"><input name="returnBtn" type="button"
                                                                      class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                                      value="返回" onclick="hideDetail();"/></div>
                                        <div class="clear"></div>
                                    </li>
                                </ul>
                                <div class="clear"></div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    function showDetail(obj) {
        $("#t6113_list").hide();
        $("#t6113_detail").show();
        $("#qcpp").html(obj.t6113.F03);
        $("#cphm").html(obj.t6113.F04);
        $("#gmnf").html(obj.t6113.F05 + "年");
        $("#gmjg").html(fmoney(obj.t6113.F06, 2));
        $("#pgjz").html(fmoney(obj.t6113.F07, 2));
    }
    function hideDetail() {
        $("#t6113_list").show();
        $("#t6113_detail").hide();
    }
    function fmoney(s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        var f = s < 0 ? "-" : ""; //判断是否为负数
        s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
        var l = s.split(".")[0].split("").reverse(),
                r = s.split(".")[1];
        var t = "";
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return f + t.split("").reverse().join("") + "." + r.substring(0, 2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
    }
</script>
</body>
</html>