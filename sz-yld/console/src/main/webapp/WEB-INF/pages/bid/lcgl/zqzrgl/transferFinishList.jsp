<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferProceedList" %>
<%@page import="com.dimeng.p2p.modules.financial.console.service.entity.TransferDshEntity" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferFinishList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferDshList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferFailList" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "ZQZRGL";
    PagingResult<TransferDshEntity> transferDshs = (PagingResult<TransferDshEntity>) request.getAttribute("transferDshs");
    TransferDshEntity[] TransferDshArray = transferDshs.getItems();
    TransferDshEntity transferYjsAmount = (TransferDshEntity)request.getAttribute("transferYjsAmount");
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, TransferFinishList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>线上债权转让管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">债权ID</span>
                                        <input type="text" name="creditorId" class="text border pl5 mr20"
                                               value="${creditorId }"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的ID</span>
                                        <input type="text" name="loanId" class="text border pl5 mr20"
                                               value="${loanId }"/>
                                    </li>

                                    <li><span class="display-ib mr5">借款标题</span>
                                        <input type="text" name="loanTitle" class="text border pl5 mr20"
                                               value="${loanTitle }"/>
                                    </li>
                                    <li><span class="display-ib mr5">债权转让者</span>
                                        <input type="text" name="creditorOwner" class="text border pl5 mr20"
                                               value="${creditorOwner}"/>
                                    </li>
                                    <li><span class="display-ib mr5">申请时间</span> <input
                                            type="text" name="createTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="createTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 date"/></li>

                                    <li><a href="javascript:$('#form2').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15 ml10"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, TransferDshList.class)%>"
                                           class="tab-btn ">待转让</a></li>
                                    <li><a href="<%=controller.getURI(request, TransferProceedList.class)%>"
                                           class="tab-btn ">转让中</a></li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">已转让<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, TransferFailList.class)%>"
                                           class="tab-btn ">转让失败</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>债权ID</th>
                                        <th>标的ID</th>
                                        <th>借款标题</th>
                                        <th>债权转让者</th>
                                        <th>剩余期数</th>
                                        <th>年化利率</th>
                                        <th>债权价值(元)</th>
                                        <th>转让价格(元)</th>
                                        <th>转让费率</th>
                                        <th>申请时间</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (TransferDshArray != null && TransferDshArray.length > 0) {
                                            int i = 1;
                                            for (TransferDshEntity transferDsh : TransferDshArray) {
                                                if (transferDsh == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="title tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, transferDsh.F02); %>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, transferDsh.F14); %>
                                        </td>
                                        <td align="center"><%=transferDsh.F13%>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, transferDsh.F03);%>
                                        </td>
                                        <td align="center"><%=transferDsh.F05 %>/<%=transferDsh.F04 %>
                                        </td>
                                        <td align="center"><%=Formater.formatRate(transferDsh.F06)%>
                                        </td>
                                        <td align="center"><%=Formater.formatAmount(transferDsh.F11)%>
                                        </td>
                                        <td align="center"><%=Formater.formatAmount(transferDsh.F08)%>
                                        </td>
                                        <td align="center"><%=Formater.formatRate(transferDsh.F09)%>
                                        </td>
                                        <td align="center"><%=TimestampParser.format(transferDsh.F10) %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="title tc">
                                        <td colspan="11">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">债权价值总金额：<em
                                    class="red"><%=Formater.formatAmount(transferYjsAmount.F07) %>
                            </em> 元</span>
                            <span class="mr30">转让价格总金额：<em
                                    class="red"><%=Formater.formatAmount(transferYjsAmount.F08) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, transferDshs);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
        </div>
    </div>
<div id="info"></div>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({
            inline: true

        });
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

</script>


</body>
</html>