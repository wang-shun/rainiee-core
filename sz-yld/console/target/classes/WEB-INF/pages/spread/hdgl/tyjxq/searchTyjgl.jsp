<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.SearchTyjgl"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6103_F06" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.ExportTyjgl" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@ page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalList" %>
<%@ page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalInfo" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.ViewExperienceDetail" %>
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
    PagingResult<ExperienceTotalList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ExperienceTotalInfo totalInfo = ObjectHelper.convert(request.getAttribute("totalInfo"), ExperienceTotalInfo.class);
    ExperienceTotalList searchTotalAmount = ObjectHelper.convert(request.getAttribute("searchTotalAmount"), ExperienceTotalList.class);
    String invalidStartTime = request.getParameter("invalidStartTime");
    String invalidEndTime = request.getParameter("invalidEndTime");
    String lixiStartTime = request.getParameter("lixiStartTime");
    String lixiEndTime = request.getParameter("lixiEndTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchTyjgl.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>体验金详情列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">已使用体验金总计</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalInfo.totalUsedMoney)%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">体验金投资利息总计</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalInfo.returnMoney)%></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">标编号</span>
                                        <input type="text" name="bid" id="bid" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bid"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <%String status = request.getParameter("status"); %>
                                            <option>全部</option>
                                            <%
                                                for (T6103_F06 s : T6103_F06.values()) {
                                                    if (s == T6103_F06.YTZ || s == T6103_F06.YJQ) {
                                            %>
                                            <option value="<%=s.name() %>"
                                                    <%if(!StringHelper.isEmpty(status) && status.equals(s.name())){ %>selected="selected" <%} %>>
                                                <%=s.getChineseName() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">投资时间</span>
                                        <input type="text" name="invalidStartTime" readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("invalidStartTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="invalidEndTime" readonly="readonly" id="datepicker4"
                                               class="text border pl5 w120 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("invalidEndTime"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">起息日</span>
                                        <input type="text" name="lixiStartTime" readonly="readonly" id="datepicker5"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("lixiStartTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="lixiEndTime" readonly="readonly" id="datepicker6"
                                               class="text border pl5 w120 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("lixiEndTime"));%>"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportTyjgl.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="exportList()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%
                                            }
                                        %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>用户名</th>
                                    <th>姓名</th>
                                    <th>标编号</th>
                                    <th>投资金额</th>
                                    <th>体验金(元)</th>
                                    <th>年化利率</th>
                                    <th>项目期限</th>
                                    <th>状态</th>
                                    <th>投资时间</th>
                                    <!-- <th>实际收益期</th> -->
                                    <th>收益(元)</th>
                                    <th>起息日</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (result != null && result.getItemCount() > 0) {
                                        ExperienceTotalList[] lists = result.getItems();
                                        if (lists != null) {
                                            int index = 1;
                                            for (ExperienceTotalList list : lists) {
                                %>
                                <tr class="tc">
                                    <td><%=index++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.userName); %></td>
                                    <td><%StringHelper.filterHTML(out, list.userRealName); %></td>
                                    <td><%StringHelper.filterHTML(out, list.bid); %></td>
                                    <td><%=Formater.formatAmount(list.investmoney)%>
                                    </td>
                                    <td>
                                        <%=Formater.formatAmount(list.experienceMoney)%>
                                    </td>
                                    <td><%=Formater.formatRate(list.jkNlv)%>
                                    </td>
                                    <%
                                        if (list.borMethod == T6231_F21.F) {
                                    %>
                                    <td><%=list.jkTime%>个月</td>
                                    <%
                                    } else {
                                    %>
                                    <td><%=list.jkDay%> 天</td>
                                    <%
                                        }
                                    %>
                                    <td><%=list.status.getChineseName()%>
                                    </td>
                                    <td><%=TimestampParser.format(list.tbTime)%>
                                    </td>
                                    <%-- <td><%=list.expectedTerm%> --%>
                                    <td><%=list.experienceInterest%>
                                    </td>
                                    <td><%=DateTimeParser.format(list.interestTime) %>
                                    </td>
                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewExperienceDetail.class)) {
                                        %>
                                        <%
                                            if (list.status.name().equals("YTZ") || list.status.name().equals("YJQ")) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewExperienceDetail.class)%>?id=<%=list.id %>&userId=<%=list.userId %>"
                                           style="cursor:pointer" class="link-blue">查看</a>
                                        <%
                                            }
                                        %>
                                        <%}else{%>
                                        <a href="javascript:void(0)" class="disabled">查看</a>
                                        <%}%>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="13">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                    
                    <p class="mt5">
                        <span class="mr30">投资总金额：<em class="red"><%=Formater.formatAmount(searchTotalAmount.investmoney)%>
                        </em> 元</span>
                        <span class="mr30">体验金总金额：<em class="red"><%=Formater.formatAmount(searchTotalAmount.experienceMoney)%>
                        </em> 元</span>
                        <span class="mr30">总收益：<em class="red"><%=Formater.formatAmount(searchTotalAmount.experienceInterest)%>
                        </em> 元</span>
                    </p>
                    <!--分页-->
                    <%
                        AbstractConsoleServlet.rendPagingResult(out, result);
                    %>
                    <!--分页 end-->
					</div>
                </form>
            </div>
        </div>
    </div>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(invalidStartTime)){%>
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("invalidStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(invalidEndTime)){%>
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("invalidEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());

        $("#datepicker5").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker6").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker5').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker6").datepicker({inline: true});
        $('#datepicker6').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(lixiStartTime)){%>
        $("#datepicker5").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("lixiStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(lixiEndTime)){%>
        $("#datepicker6").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("lixiEndTime"));%>");
        <%}%>
        $("#datepicker6").datepicker('option', 'minDate', $("#datepicker5").datepicker().val());
    });
    function exportList() {
        var del_url = '<%=controller.getURI(request, ExportTyjgl.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchTyjgl.class)%>';
    }

    function openModelessDialog(id, userId) {
        //打开对话框并返回选中的值
        window.open("<%=controller.getURI(request, ViewExperienceDetail.class)%>?id=" + id + "&userId=" + userId, "", "height=570,width=800,Left=300px,Top=20px,menubar=no,titlebar=no,scrollbar=no,toolbar=no,status=no,location=no");
    }
</script>
</body>
</html>