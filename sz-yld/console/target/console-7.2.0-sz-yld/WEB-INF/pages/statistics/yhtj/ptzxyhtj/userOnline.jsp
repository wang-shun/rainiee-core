<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptzxyhtj.UserOnline"%>
<%@page import="com.dimeng.p2p.variables.P2PConst" %>
<%@page import="com.dimeng.framework.http.service.UserStatisticsManage" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    java.sql.Date date = SQLDateParser.parse(request.getParameter("date"));
    if (date == null) {
        date = new java.sql.Date(System.currentTimeMillis());
    }
    String nowDate = DateParser.format(date);
    UserStatisticsManage manage = serviceSession.getService(UserStatisticsManage.class);
    int[] onlineDatas = manage.getOnlineHistory(date, P2PConst.DB_USER_SESSION);

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "PTZXYHTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台在线用户统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, UserOnline.class) %>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li><span class="display-ib mr5">时间： </span>
                                            <input type="text" name="date" readonly="readonly" id="datepicker"
                                                   class="text border pl5 w120 date mr20"/>
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
                                <h3 class="f16 fb"><%=nowDate %>平台在线用户统计曲线图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<script type="text/javascript">var data = [<%boolean notFirst=false;for(int onlineData : onlineDatas){if(notFirst){out.print(',');}else{notFirst=true;}out.print(Integer.toString(onlineData));}%>]</script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/userOnline.js"></script>
<script type="text/javascript">
    $("#datepicker").datepicker({inline: true});
    $('#datepicker').datepicker('option', {dateFormat: 'yy-mm-dd'});
    <%if(!StringHelper.isEmpty(nowDate)){%>
    $("#datepicker").val("<%StringHelper.filterHTML(out, nowDate);%>");
    <%}%>
</script>
</body>
</html>