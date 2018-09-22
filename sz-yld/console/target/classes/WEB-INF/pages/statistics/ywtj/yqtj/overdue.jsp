<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S70.entities.T7053" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.ywtj.yqtj.Overdue" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "YQSJTJ";
    T7053[] t7053s = ObjectHelper.convertArray(request.getAttribute("t7053s"),T7053.class);
    int[] options = ObjectHelper.convertIntArray(request.getAttribute("options"));
    int year = IntegerParser.parse(request.getAttribute("year"));
    
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>逾期数据统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, Overdue.class) %>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>年份：
                                            <select name="year" class="border mr20 h32 mw100">
                                                <%
                                                    if (options != null && options.length > 0) {
                                                        for (int option : options) {
                                                %>
                                                <option value="<%=option %>" <%=year == option ? "selected=\"selected\"" : "" %>><%=option %> 年</option>
                                                <%
                                                    }
                                                }else{
                                                %>
                                                <option value="<%=year%>"><%=year%> 年</option>
                                                <%}%>
                                            </select>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    </ul>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                               <%-- <h3 class="f16 fb">逾期人数及金额统计</h3>--%>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="column1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                    
                    
                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<script type="text/javascript">
var y = <%=year %>;    
var yqrsData = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
var yqjeData = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
<%for(T7053 t7053 : t7053s){%>
yqrsData[<%=t7053.F02-1%>] = <%=t7053.F03%>;
yqjeData[<%=t7053.F02-1%>] = <%=t7053.F04%>;
<%}%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/tjgl/overdue.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</body>
</html>