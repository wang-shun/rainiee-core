<%@page import="com.dimeng.p2p.user.servlets.credit.ExportNewCreditStatisticsDetail" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME); %>_借款管理_借款统计</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "JKTJ_NEW";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div id="jktjDivId" class="r_main">
            <div class="user_mod">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">借款统计</span></div>
                <div class="amount_list clearfix mt30 mb10">
                    <ul id="ulCreditId" class="clearfix">
                        <li style="width:138px;padding-left:10px;">累计借款金额(元)
                        	<span class="hover_tips">
                        		<div class="hover_tips_con" style="margin-left: -85px;">
                                    <div class="arrow"></div>
                                    <div class="border" style="width:140px;">累计借款金额=∑借款金额</div>
                                </div>
                             </span>

                            <p class="mt10" id="ulCreditId1"></p>
                        </li>
                        <li style="width:138px;padding-left:10px;">累计借款利息(元)
                        	<span class="hover_tips">
                                <div class="hover_tips_con" style="margin-left: -85px;">
                                    <div class="arrow"></div>
                                    <div class="border" style="width:140px;">
                                        累计借款利息=∑借款利息
                                    </div>
                                </div>
                            </span>

                            <p class="mt10" id="ulCreditId2"></p>
                        </li>
                        <li style="width:138px;padding-left:10px;">累计管理费(元)
                        	<span class="hover_tips">
                                <div class="hover_tips_con" style="margin-left: -75px;">
                                    <div class="arrow"></div>
                                    <div class="border" style="width:120px;">
                                        累计管理费=∑管理费
                                    </div>
                                </div>
                            </span>

                            <p id="ulCreditId3" class="mt10"></p>
                        </li>
                        <li style="width:138px;padding-left:10px;">已还金额(元)
                        	<span class="hover_tips">
                                <div class="hover_tips_con">
                                    <div class="arrow"></div>
                                    <div class="border">
                                        已还金额=已还本金+已还利息+已还罚息+已还违约金+违约金手续费
                                    </div>
                                </div>
                            </span>

                            <p id="ulCreditId4" class="mt10"></p>
                        </li>
                        <li style="width:138px;padding-left:10px;">待还金额(元)
                        	<span class="hover_tips">
                                <div class="hover_tips_con">
                                    <div class="arrow"></div>
                                    <div class="border">
                                        待还金额=待还本金+待还利息+待还罚息
                                    </div>
                                </div>
                            </span>

                            <p id="ulCreditId5" class="mt10">0.00</p>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="user_mod border_t15">
                <div class="user_screening clearfix">
                    <ul>
                        <li>
                            年：<input type="text" id="year" name="year" readonly="readonly"
                                     class="text_style text_style_date"
                                     value=""/>&nbsp;&nbsp;
                            月：<input type="text" id="month" name="month" readonly="readonly"
                                     class="text_style text_style_date"
                                     value=""/>
                        </li>
                        <li>
                            <a href="javascript:void(0);" class="btn04 ml10" onclick="newCreditStatisticsQuery(1);"> 查询</a>
                        </li>
                    </ul>
                </div>
                <%--<a href="<%=controller.getURI(request, NewCreditStatisticsDetail.class)%>?year=<%=year%>&month=<%=month%>" class="blue fr" style="margin-top:6px;display:inline-block;">详情</a>--%>
                <div class="user_table">
                    <div class="tr mb10"><a id="creditDetailUrl" href="javascript:void(0);" class="highlight"
                                            onclick="creditDetailQuery(1);">详情</a></div>
                    <table id="jktjTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td align="center">序号</td>
                            <td align="center">年</td>
                            <td align="center">月</td>
                            <td align="center">借款金额(元)</td>
                            <td align="center">借款利息(元)</td>
                            <td align="center">成交服务费(元)</td>
                            <td align="center">已还金额(元)</td>
                            <td align="center">待还金额(元)</td>
                        </tr>

                    </table>
                    <div id="pageContent" class="page">

                    </div>
                </div>
                <div class="user_mod_gray lh24 p30 mt30 ">
                    <span class="highlight">借款统计说明:</span><br/>
                    	借款统计数据为每月初更新上一个月数据。
                </div>
            </div>
        </div>

        <!--借款详情 start-->
        <div id="jktjDetailDivId" class="r_main" style="display:none;">
            <div class="user_mod">
                <div class="user_til border_b pb10"><i class="icon"></i><span class="gray3 f18">借款统计详情</span></div>
                <form id="form_loan" action="<%=controller.getURI(request, ExportNewCreditStatisticsDetail.class)%>"
                      method="post">
                    <div class="user_screening clearfix">
                        <ul>
                            <li>
                                年：<input type="text" id="detailYear" name="year" readonly="readonly"
                                         class="text_style text_style_date"
                                         value=""/>&nbsp;&nbsp;
                                月：<input type="text" id="detailMonth" name="month" readonly="readonly"
                                         class="text_style text_style_date"
                                         value=""/>
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="btn04" onclick="creditDetailQuery(1);"> 查询</a>
                                <a href="javascript:void(0);" class="btn04 ml10" onclick="showExport()"> 导出</a>
                                <a href="javascript:void(0);" class="btn04 ml10" onclick="newCreditStatisticsQuery(1);"> 返回</a>
                            </li>
                        </ul>
                    </div>
                    <div class="user_table user_table_long">
                        <table id="jktjDetailTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til f12">
                                <td align="center">序号</td>
                                <td align="center">年</td>
                                <td align="center">月</td>
                                <td align="center">借款金额<br/>(元)</td>
                                <td align="center">借款利息<br/>(元)</td>
                                <td align="center">成交服务费<br/>(元)</td>
                                <td align="center">已还本金<br/>(元)</td>
                                <td align="center">已还利息<br/>(元)</td>
                                <td align="center">已还逾期罚息<br/>(元)</td>
                                <td align="center">违约金<br/>(元)</td>
                                <td align="center">待还本金<br/>(元)</td>
                                <td align="center">待还利息<br/>(元)</td>
                                <td align="center">待还逾期罚息<br/>(元)</td>
                            </tr>

                        </table>
                        <div class="page" id="detailPageContent">

                        </div>
                    </div>
                    <div class="user_mod_gray lh24 p30 mt30">
                        <span class="highlight">借款统计说明:</span><br/>
                        	借款统计数据为每月初更新上一个月数据。
                    </div>
                </form>
            </div>
        </div>

        <div class="clear"></div>
    </div>
</div>

	<div class="dialog dialog_year_month w510 thickpos" style="display: none;">
		<div class="popup_bg"></div>
		<div class="dialog" >
			<div class="title"><a class="out" id="cancel" href="javascript:void(0);"></a>提示</div>
			<div class="content">
				<div class="tip_information"> 
					<div class="doubt"></div>
					<div class="tips" id="infoDiv">
						<p class="f20 gray3">请选择年份，再选择月份!</p>
					</div>
				</div>
				<div class="tc mt20">
					<a href="javascript:void(0)" id="ok" class="btn01">确定</a> <!-- <a
						href="javascript:void(0)" id="cancel" class="btn01">取消</a> -->
				</div>
			</div>
		</div>
	</div>

<input type="hidden" id="ajaxUrl" value="<%=controller.getURI(request, NewCreditStatistics.class) %>"/>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/zankai.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/newCreditStatistics.js"></script>
</body>
</html>