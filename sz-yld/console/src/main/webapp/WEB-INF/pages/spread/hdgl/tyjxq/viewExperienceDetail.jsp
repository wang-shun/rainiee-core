<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.ViewExperienceDetail" %>
<%@page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceProfit" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "TYJXQLB";
    PagingResult<ExperienceProfit> list = (PagingResult<ExperienceProfit>) request.getAttribute("list");
    ExperienceProfit[] eArray = (list == null ? null : list.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, ViewExperienceDetail.class)%>" method="post">
                    <input type="hidden" name="id" value="<%=request.getParameter("id")%>"/>
                    <input type="hidden" name="userId" value="<%=request.getParameter("userId")%>"/>

                    <div class="p20">

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>体验金收益列表
                            </div>
                        </div>
                        <div class="border mt20 ">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>标编号</th>
                                    <th>年化利率</th>
                                    <th>实际收益期</th>
                                    <th>还款日</th>
                                    <th>利息(元)</th>
                                    <th>状态</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (eArray != null) {
                                        int i = 1;
                                        for (ExperienceProfit experienceProfit : eArray) {
                                %>
                                <tr>
                                    <td class="tc"><%=i++%>
                                    </td>
                                    <td class="tc"><% StringHelper.filterHTML(out, experienceProfit.bidNo);%></td>
                                    <td class="tc"><%=Formater.formatRate(experienceProfit.rate)%>
                                    </td>
                                    <td class="tc">
                                        <%if (T6231_F21.F == experienceProfit.dayOrMonth) {
                                            if("false".equals(experienceProfit.incomeMethod)){%>
                                                <%=experienceProfit.num*30 > experienceProfit.totalNum ? experienceProfit.totalNum : experienceProfit.num*30%> 天
                                        <% }else{ %>
                                            <%=experienceProfit.num > experienceProfit.totalNum ? experienceProfit.totalNum : experienceProfit.num%> 个月
                                        <%}} else {
                                            if("false".equals(experienceProfit.incomeMethod)){
                                        %>
                                            <%=experienceProfit.borrowDays > experienceProfit.totalNum ? experienceProfit.totalNum : experienceProfit.borrowDays%>天
                                        <%}else{%>
                                            <%=experienceProfit.borrowDays > experienceProfit.totalNum * 30 ? experienceProfit.totalNum * 30 : experienceProfit.borrowDays%>天
                                        <%}
                                        }%>




                                    </td>

                                    <td class="tc"><%=DateParser.format(experienceProfit.time)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(experienceProfit.interest)%>
                                    </td>
                                    <td class="tc"><%=experienceProfit.status.getChineseName()%>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                                </tbody>
                            </table>

                            <!--分页-->
                            <%
                                AbstractConsoleServlet.rendPagingResult(out, list);
                            %>
                            <!--分页 end-->
                        </div>
                        <div class="tc w220 pt20">
                            <input type="button"
                                   onclick="history.back(-1);"
                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                        </div>
                    </div>

                </form>
            </div>

        </div>
    </div>

</body>
</html>