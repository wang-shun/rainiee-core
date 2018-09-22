<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance.UserList"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.console.T6110_FY" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance.UserQuery" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>

<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "YHYECX";

    PagingResult<T6110_FY> pagingResult = (PagingResult<T6110_FY>) request.getAttribute("pagingResult");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, UserList.class)%>" method="post" name="form1" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>用户余额查询
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">用户名：</span>
                                        <input type="text" name="userName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName")); %>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">用户类型：</span>
                                        <select name="userTag" class="border mr20 h32 mw100">
                                            <option value="" <% if ("".equals(request.getAttribute("userTag"))) {%>
                                                    selected="selected"<%} %>>全部
                                            </option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getAttribute("userTag"))) {%>
                                                    selected="selected"<%} %>>个人
                                            </option>
                                            <option value="QY" <%if ("QY".equals(request.getAttribute("userTag"))) {%>
                                                    selected="selected"<%} %>>企业
                                            </option>
                                            <option value="JG" <%if ("JG".equals(request.getAttribute("userTag"))) {%>
                                                    selected="selected"<%} %>>机构
                                            </option>
                                        </select>
                                    </li>
									<li><a href="javascript:search();"
										class="btn btn-blue radius-6 mr5 pl1 pr15"><i
											class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
								</ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                	<th class="tc">序号</th>
                                    <th class="tc">用户ID</th>
                                    <th class="tc">用户名</th>
                                    <th class="tc">手机号码</th>
                                    <th class="tc">邮箱</th>
                                    <th class="tc">用户类型</th>
                                    <th class="tc">账号余额(元)</th>
                                    <th class="tc">可用余额(元)</th>
                                    <th class="tc">冻结余额(元)</th>
                                    <th class="tc">第三方余额(元)</th>
                                    <th class="tc">第三方可用金额(元)</th>
                                    <th class="tc">第三方冻结金额(元)</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    T6110_FY[] items = pagingResult.getItems();
                                    if (items != null && items.length > 0) {
                                        int i=1;
                                        for (T6110_FY item : items) {
                                            if (item == null) {
                                                continue;
                                            }
                                %>
                                <tr class="dhsbg">
                                	<td><%=i++%></td>
                                    <td class="tc"><%=item.F01 %>
                                    </td>
                                    <td class="tc"><%StringHelper.filterHTML(out, item.F02); %></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, item.F04); %></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, item.F05); %></td>
                                    <td class="tc">
                                        <%
                                            if (item.F06 == T6110_F06.ZRR) {
                                                out.print("个人");
                                            } else {
                                                if(T6110_F10.S == item.F10){
                                                	out.print("机构");
                                                }else{
                                                    out.print("企业");
                                                }
                                            }
                                        %>
                                    </td>
                                    <td class="tc">
                                        <%=item.pt_balance%>
                                    </td>
                                    <td class="tc">
                                        <%=item.pa_balance%>
                                    </td>
                                    <td class="tc">
                                        <%=item.pf_balance%>
                                    </td>
                                     <td class="tc">
                                        <% if (!item.pt_balance.equals(item.ct_balance)) {%>
											<span style="color: red">
											<%=item.ct_balance==null?"":item.ct_balance%></span>
                                        <%} else%>
                                        <%=item.ct_balance%>
                                    </td>
                                    <td class="tc">
                                        <% if (!item.pa_balance.equals(item.ca_balance)) {%>
											<span style="color: red">
											<%=item.ca_balance==null?"":item.ca_balance%></span>
                                        <%} else%>
                                        <%=item.ca_balance%>
                                    </td>
                                    <td class="tc">
                                        <% if (!item.pf_balance.equals(item.cf_balance)) {%>
											<span style="color: red">
											<%=item.cf_balance==null?"":item.cf_balance%></span>
                                        <%} else%>
                                        <%=item.cf_balance%>
                                    </td>
                                    <td class="tc">
	                                    <%if (dimengSession.isAccessableResource(UserQuery.class)) {
											if (!StringHelper.isEmpty(item.thirdTag)) { %>
	                                        	<a href="<%=controller.getURI(request, UserQuery.class)%>?id=<%=item.thirdTag %>&userType=<%=item.F06%>&userTag=<%=item.F10 %>"
												class="blue ml5" target="_self">第三方信息查询</a> 
										<%	
											} 
										}else{
										    if (!StringHelper.isEmpty(item.thirdTag)) {
										%>
						              		<span class="disabled">第三方信息查询</span>
						               	<%
						               		}
										} 
										%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="13" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                            <%
                                AbstractFinanceServlet.rendPagingResult(out, pagingResult);
                            %>
                        </div>
                        <div class="clear"></div>
                        <div class="box2 clearfix"></div>
                    </div>
                </form>
            </div>
        </div>
</div>
<script type="text/javascript">

function search()
{
	$("#form1").submit();
};

</script>

</body>
</html>