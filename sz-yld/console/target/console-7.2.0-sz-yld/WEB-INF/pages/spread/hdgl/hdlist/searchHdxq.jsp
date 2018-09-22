<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.modules.activity.console.service.entity.ActivityLog" %>
<%@page import="com.dimeng.p2p.S63.entities.T6340" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S63.entities.T6344" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.dimeng.p2p.S63.enums.*" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "HDGL";
    T6340 result = ObjectHelper.convert(request.getAttribute("result"), T6340.class);
    ActivityLog[] logs = ObjectHelper.convertArray(request.getAttribute("logs"), ActivityLog.class);
    T6344[] t6344s =  ObjectHelper.convert(request.getAttribute("t6344s"), T6344[].class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <!--切换栏目-->
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>活动详情
                            <div class="fr" style="margin-top:5px;">
                                <input type="button" value="返回" class="btn btn-blue radius-6 pl20 pr20 ml40 mr10"
                                       onclick="history.back(-1);">
                            </div>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item border-b-s pb30">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动ID：</span>
                                        <%=result.F02 %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">奖励类型：</span>
                                        <%=result.F03.getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动类型：</span>
                                        <%=result.F04.getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动名称：</span>
                                        <%StringHelper.filterHTML(out, result.F05); %>
                                    </li>
                                    <%if (!(result.F04 == T6340_F04.foruser && !StringHelper.isEmpty(request.getAttribute("userNames").toString()))) {%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动开始时间：</span>
                                        <%=DateTimeParser.format(result.F06, "yyyy-MM-dd") %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动结束时间：</span>
                                        <%=DateTimeParser.format(result.F07, "yyyy-MM-dd") %>
                                    </li>
                                    <%   } %>
                                    <li class="mb10"><div class="pr pl200"><span class="display-ib pa top0 left0 w200 tr mr5 ">赠送规则：</span>
                                    <ul class="pl10">
                                    <%  String title = "";
                                        String jltitle = "";
                                        String tztitle = "";
                                        String qxtitle = "";
                                        String syqtitle="";
                                        for(T6344 t6344:t6344s){
                                            if (result.F04 == T6340_F04.register) {
                                                title = "注册，";
                                            }
                                            if (result.F04 == T6340_F04.recharge || result.F04 == T6340_F04.firstrecharge || result.F04 == T6340_F04.tjsccz) {
                                                title = "充值金额大于等于<span style=\"color:#FF9933;\">"+t6344.F06+"</span>元，";
                                            }
                                            if (result.F04 == T6340_F04.investlimit || result.F04 == T6340_F04.tjsctz) {
                                                title = "投资金额大于等于<span style=\"color:#FF9933;\">"+t6344.F06+"</span>元，";
                                            }
                                            if (result.F04 == T6340_F04.tjtzze) {
                                                title = "累计投资金额大于等于<span style=\"color:#FF9933;\">"+t6344.F06+"</span>元，";
                                            }
                                            if (result.F04 == T6340_F04.birthday) {
                                                if (result.F09 == T6340_F09.all) {
                                                    title = "生日当天";
                                                } else if (result.F09 == T6340_F09.login) {
                                                    title = "生日当天登录";
                                                } else if (result.F09 == T6340_F09.invest) {
                                                    title = "生日当天投资";
                                                }
                                            }

                                            if(result.F03 == T6340_F03.experience){
                                                jltitle = "奖励体验金额<span style=\"color:#FF9933;\">"+t6344.F05+"</span>元；";
                                            }
                                            if(result.F03 == T6340_F03.redpacket){
                                                jltitle = "奖励红包金额<span style=\"color:#FF9933;\">"+t6344.F05+"</span>元；";
                                            }
                                            if(result.F03 == T6340_F03.interest){
                                                jltitle = "奖励加息利率<span style=\"color:#FF9933;\">"+t6344.F05+"</span>%；";
                                            }

                                            if(t6344.F07 != null && t6344.F07.compareTo(BigDecimal.ZERO) > 0){
                                                tztitle = "投资满<span style=\"color:#FF9933;\">"+t6344.F07+"</span>元使用；";
                                            }else{
                                                tztitle = "无限制；";
                                            }

                                            if(t6344.F09 == T6344_F09.F){
                                                qxtitle = "使用有效期：<span style=\"color:#FF9933;\">"+t6344.F04+"</span>天；";
                                            }else{
                                                qxtitle = "使用有效期：<span style=\"color:#FF9933;\">"+t6344.F04+"</span>个月；";
                                            }

                                            if(result.F03 == T6340_F03.experience){
                                                if("true".equals(t6344.F11)){
                                                    syqtitle = "预计收益期：<span style=\"color:#FF9933;\">"+t6344.F10+"</span>个月；";
                                                }else{
                                                    syqtitle = "预计收益期：<span style=\"color:#FF9933;\">"+t6344.F10+"</span>天；";
                                                }
                                            }

                                            title = title + jltitle + tztitle + qxtitle + syqtitle;
                                    %>


                                    <li class="mb10">
                                        <%=title%>
                                    </li>
                                    <%   } %>
                                    </ul>
                                    </div>
                                    </li>


                                    <%if (result.F04 == T6340_F04.foruser && !StringHelper.isEmpty(request.getAttribute("userNames").toString())) {%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">指定的用户：</span>
                                        <%StringHelper.filterHTML(out, (String)request.getAttribute("userNames"));%>
                                    </li>
                                    <%} %>




                                    <li class="mb10"><div class="pl200 pr mh30"><span class="display-ib w200 tr mr5 pa top0 left0">备注：</span>
                                        <p class="pl10"><%StringHelper.filterHTML(out, result.F10); %></p></div>
                                    </li>


                                    <li class="mb10"><span class="display-ib w200 tr mr5">活动状态：</span>
                                        <%StringHelper.filterHTML(out, result.F08.getChineseName()); %>
                                    </li>
                                    <%if (T6340_F08.YXJ.name().equals(result.F08.name()) || T6340_F08.YZF.name().equals(result.F08.name())) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><%=T6340_F08.YXJ.name().equals(result.F08.name()) ? "下架" : "作废" %>时间：</span>
                                        <span class="mr10"><%=DateTimeParser.format(result.F12, "yyyy-MM-dd HH:mm:ss")%></span>（<%StringHelper.filterHTML(out, result.F13); %>）
                                    </li>
                                    <%} %>
                                </ul>
                            </div>
                            <div class="pat_title clearfix bold h30 lh30 pt20"> 活动统计：  </div>
                            <div class="table-container border">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                    	<%if(result.F03==T6340_F03.redpacket){ %>
                                        <th>序号</th> <th>奖励红包金额（元）</th>  <th>红包总数量（个）</th> <th>已领取数量（个）</th> <th>已领取总额（元）</th>
                                        <%}else if(result.F03==T6340_F03.interest){ %>
                                        <th>序号</th> <th>奖励加息利率（%）</th>  <th>加息券总数量（个）</th> <th>已领取数量（个）</th>
                                        <%}else if(result.F03==T6340_F03.experience){ %>
                                        <th>序号</th> <th>体验金金额（元）</th>  <th>体验金总数量（个）</th> <th>已领取数量（个）</th>
                                        <%}%>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (t6344s != null && t6344s.length > 0) {
                                            int i = 1;
                                            for (T6344 t6344obj : t6344s) {
                                                if (t6344obj == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%></td>
                                        <td><%=t6344obj.F05%></td>
                                        <td><%=t6344obj.F03%></td>
                                        <td><%=t6344obj.F08%></td>
                                        <%if(result.F03==T6340_F03.redpacket){ %>
                                        <td><%=t6344obj.F05.multiply(new BigDecimal(t6344obj.F08)) %>
                                        <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="5">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                            <div class="pb30 border-b-s">&nbsp;</div>
                            <div class="pat_title clearfix bold h30 lh30 pt20">  操作日志：  </div>
                            <div class="table-container border">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>操作人</th>
                                        <th>操作时间</th>
                                        <th>备注</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (logs != null && logs.length > 0) {
                                            int i = 1;
                                            for (ActivityLog log : logs) {
                                                if (log == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td><%=log.F06%>
                                        </td>
                                        <td><%=DateTimeParser.format(log.F04)%>
                                        </td>
                                        <td><%=log.F05 %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="4">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>