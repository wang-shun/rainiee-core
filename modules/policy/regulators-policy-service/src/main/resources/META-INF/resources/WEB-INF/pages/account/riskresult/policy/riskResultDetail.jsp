<%@ page import="com.dimeng.p2p.repeater.policy.query.RiskQueryResult" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dimeng.p2p.repeater.policy.query.AnswerQuery" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.riskresult.policy.RiskResultList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "FXPGJG";
    RiskQueryResult result = ObjectHelper.convert(request.getAttribute("result"),RiskQueryResult.class);
    String score = configureProvider.getProperty(RegulatoryPolicyVariavle.RISK_ASSESS_VALUE);
    String[] scores = score.split(",");
%>
<div class="right-container">
    <div class="viewFramework-body">
        <div class="viewFramework-content">

            <!--平台资金统计-->

            <div class="p20">
                <form>
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>风险评估详情-<%=result.userName%>
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr" onclick="location.href='<%=controller.getURI(request, RiskResultList.class) %>'" value="返回">
                            </div>
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <div class="ability_til tc mb20">风险承受能力测试</div>
                            <ul class="gray3 input-list-container clearfix f18">
                                <li><span class="display-ib mr5">用户名：</span><span
                                        class="display-ib mr40 gray3"><%StringHelper.filterHTML(out, result.userName);%></span>
                                </li>
                                <li><span class="display-ib mr5">题目数：</span><span
                                        class="display-ib mr40 gray3"><%=result.answerList.size()%></span>
                                </li>
                                <li><span class="display-ib mr5">总分：</span>
                                    <span class="display-ib mr40 gray3">
                                        <%=result.score%>
                                    </span>
                                </li>
                                <li><span class="display-ib mr5">风险等级：</span><span
                                        class="display-ib mr40 gray3"> <%=result.riskType%></span>
                                </li>
                                <li><span class="display-ib mr5">分值设置：</span><span
                                        class="display-ib mr40 gray3">
                                    A:<%=scores.length>0?scores[0]:0%>&nbsp;&nbsp;
                                    B:<%=scores.length>1?scores[1]:0%>&nbsp;&nbsp;
                                    C:<%=scores.length>2?scores[2]:0%>&nbsp;&nbsp;
                                    D:<%=scores.length>3?scores[3]:0%>&nbsp;&nbsp;
                                </span>
                                </li>
                                <li><span class="display-ib mr5">评估时间：</span><span
                                        class="display-ib mr40 gray3"> <%=DateTimeParser.format(result.time)%> </span>
                                </li>

                            </ul>
                        </div>
                        <div class="main_mod pl50 pr20 pb20">

                            <div class="ability_list pt20 clearfix">
                                <%
                                    List<AnswerQuery> list = result.answerList;
                                    if(list != null && list.size() > 0 ){
                                        for(int i = 0; i < list.size() ; i++){
                                            AnswerQuery answer = list.get(i);
                                %>
                                <dl>
                                    <dt><%=i+1%>.<%StringHelper.filterHTML(out,answer.question);%></dt>
                                    <dd>
                                        <p>
                                            <input name="" type="radio" value="" disabled = "disabled" <%if("A".equals(answer.answer)){%>checked = "checked"<%}%>>
                                            A.<%StringHelper.filterHTML(out,answer.optionA);%> </p>
                                        <p>
                                            <input name="" type="radio" disabled = "disabled" value="" <%if("B".equals(answer.answer)){%>checked = "checked"<%}%>>
                                            B.<%StringHelper.filterHTML(out,answer.optionB);%></p>
                                        <p>
                                            <input name="" type="radio" disabled = "disabled" value="" <%if("C".equals(answer.answer)){%>checked = "checked"<%}%>>
                                            C.<%StringHelper.filterHTML(out,answer.optionC);%></p>
                                        <p>
                                            <input name="" type="radio" disabled = "disabled" value="" <%if("D".equals(answer.answer)){%>checked = "checked"<%}%>>
                                            D.<%StringHelper.filterHTML(out,answer.optionD);%> </p>
                                    </dd>
                                </dl>
                                <%}}%>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>