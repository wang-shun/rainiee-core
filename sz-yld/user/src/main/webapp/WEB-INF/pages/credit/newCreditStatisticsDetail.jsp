<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.ExportNewCreditStatisticsDetail" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.NewCreditStatisticsDetail" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    PagingResult<NewCreditList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "JKTJ_NEW";
    String year = (String) request.getParameter("year");
    String month = (String) request.getParameter("month");
%>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="container fr">
            <div class="p15">
                <div class="user_lsjt mb20">借款统计详情</div>
                <form id="form_loan" action="<%=controller.getURI(request, NewCreditStatisticsDetail.class)%>"
                      method="post">
                    <div class="table clearfix">
                    <span>
							年：<input type="text" id="year" name="year" readonly="readonly"
                                     class="date w100 yhgl_ser" onClick="WdatePicker({dateFmt:'yyyy'})"
                                     value="<%= year%>"/>&nbsp;&nbsp;
							月：<input type="text" id="month" name="month" readonly="readonly"
                                     class="date w100 yhgl_ser"
                                     value="<%= month%>"/>
						</span>
                        <input name="input" type="submit" value="搜索" class="btn01 fl mb10 ml10"/>
                        <input name="input" type="button" value="导出" class="btn01 fl mb10 ml10" onclick="showExport()"/>
                    </div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
                        <tr class="user_lsbg">
                            <td width="4%">序号</td>
                            <td width="5%">年</td>
                            <td width="4%">月</td>
                            <td width="9%">借款金额</td>
                            <td width="8%">借款利息</td>
                            <td width="9%">借款<br>管理费</td>
                            <td width="9%">已还本金</td>
                            <td width="9%">已还利息</td>
                            <td width="9%">已还<br>逾期罚息</td>
                            <td width="9%">违约金</td>
                            <td width="9%">待还本金</td>
                            <td width="9%">待还利息</td>
                            <td width="9%">待还逾<br>期罚息</td>
                        </tr>
                        <%
                            if (result != null && result.getItemCount() > 0) {
                                int i = 0;
                                for (NewCreditList entity : result.getItems()) {
                                    if (entity == null) {
                                        continue;
                                    }
                                    i++;

                        %>
                        <tr>
                            <td><%=i%>
                            </td>
                            <td><%=entity.year%>
                            </td>
                            <td><%=entity.month%>
                            </td>
                            <td><%=entity.loanMoney%>
                            </td>
                            <td><%=entity.loanInterest%>
                            </td>
                            <td><%=entity.manageMoney%>
                            </td>

                            <td><%=entity.payMoney%>
                            </td>
                            <td><%=entity.payInterest%>
                            </td>
                            <td><%=entity.payDefaultIns%>
                            </td>
                            <td><%=entity.deditMoney%>
                            </td>
                            <td><%=entity.notPayMoney%>
                            </td>
                            <td><%=entity.notPayInterest%>
                            </td>
                            <td><%=entity.notPayDefaultIns%>
                            </td>
                        </tr>
                        <%
                                }
                            }%>
                    </table>
                    <div class="page">
                        <%NewCreditStatisticsDetail.rendPagingResult(out, result); %>
                    </div>
                </form>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="dialog w510 thickpos"
     style="margin: -100px 0 0 -255px; display: none;">
    <div class="dialog_close fr">
        <a href="#"></a>
    </div>
    <div class="con clearfix">
        <div class="d_perfect fl"></div>
        <div class=" info fr" id="infoDiv">
            <p class="f20 gray33">请选择年份，再选择月份!</p>
        </div>
        <div class="clear"></div>
        <div class="dialog_btn">
            <a href="javascript:void(0)" id="ok" class="btn btn001">确定</a> <!-- <a
                href="javascript:void(0)" id="cancel" class="btn btn001">取消</a> -->
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/zankai.js"></script>
<script type="text/javascript">
    $(function () {
        $("div.dialog_close").click(function () {
            $("div.dialog").hide();
        });
        $("#cancel").click(function () {
            $("div.dialog").hide();
        });
        $("#ok").click(function () {
            $("div.dialog").hide();
        });

        $("#year").click(function () {
            $("div [lang = 'zh-cn']").show();
            WdatePicker({
                dateFmt: 'yyyy',
                isShowToday: false,
                onclearing: function () {
                    $('#year').val('');
                    $('#month').val('');
                    return true;
                }
            });
        });

        $("#month").click(function () {
            var year = $("#year").val();
            if (year == null || year == "") {
                $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份！</p>");
                $("div [lang = 'zh-cn']").hide();
                $("div.dialog").show();
                $("div.popup_bg").show();
                return;
            } else {
                WdatePicker({dateFmt: 'MM', isShowToday: false});
            }
        });
    });

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportNewCreditStatisticsDetail.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, NewCreditStatisticsDetail.class)%>";
    }
</script>
</body>
</html>