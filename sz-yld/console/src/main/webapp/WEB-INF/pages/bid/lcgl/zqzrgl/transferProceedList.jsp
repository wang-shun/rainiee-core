<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferDshList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferProceedList" %>
<%@page import="com.dimeng.p2p.modules.financial.console.service.entity.TransferDshEntity" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.ZqzrxjManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferFinishList" %>
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
    TransferDshEntity transferZrzAmount = (TransferDshEntity)request.getAttribute("transferZrzAmount");
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, TransferProceedList.class)%>" method="post">
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
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ZqzrxjManage.class)) {%>
                                        <a href="javascript:batchPlxj();" class="btn btn-blue2 radius-6 mr5 pl10 pr10">批量下架</a>
                                        <%} else {%>
                                        <a href="javascript:void(0);"
                                           class="btn btn-gray radius-6 mr5 pl10 pr10">批量下架</a>
                                        <%} %>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, TransferDshList.class)%>"
                                           class="tab-btn ">待转让</a></li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">转让中<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, TransferFinishList.class)%>"
                                           class="tab-btn ">已转让</a></li>
                                    <li><a href="<%=controller.getURI(request, TransferFailList.class)%>"
                                           class="tab-btn ">转让失败</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th><input type="checkbox" id="checkAll" class="mr5"/></th>
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
                                        <th>操作</th>
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
                                    <tr class="tc">
                                        <td><input type="checkbox" name="zqId" value="<%=transferDsh.F01%>"/></td>
                                        <td><%=i++%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, transferDsh.F02); %></td>
                                        <td><%StringHelper.filterHTML(out, transferDsh.F14); %>
                                        </td>
                                        <td><%=transferDsh.F13%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, transferDsh.F03);%></td>
                                        <td><%=transferDsh.F05 %>/<%=transferDsh.F04 %>
                                        </td>
                                        <td><%=Formater.formatRate(transferDsh.F06)%>
                                        </td>
                                        <td><%=Formater.formatAmount(transferDsh.F07)%>
                                        </td>
                                        <td><%=Formater.formatAmount(transferDsh.F08)%>
                                        </td>
                                        <td><%=Formater.formatRate(transferDsh.F09)%>
                                        </td>
                                        <td><%=TimestampParser.format(transferDsh.F10) %>
                                        </td>
                                        <td>
                                            <%if (dimengSession.isAccessableResource(ZqzrxjManage.class)) {%>
                                            <a href="javascript:void(0)"
                                               onclick="bfbZqzr('<%=transferDsh.F01 %>','<%=controller.getURI(request, ZqzrxjManage.class) %>?ids=<%=transferDsh.F01%>')"
                                               class="link-blue">下架</a>
                                            <%}else{%>
                                            <a class="disabled mr20">下架</a>
                                            <%} %>
                                        </td>
                                    </tr>

                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="title tc">
                                        <td colspan="13">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">债权价值总金额：<em
                                    class="red"><%=Formater.formatAmount(transferZrzAmount.F07) %>
                            </em> 元</span>
                            <span class="mr30">转让价格总金额：<em
                                    class="red"><%=Formater.formatAmount(transferZrzAmount.F08) %>
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
        <div class="popup_bg hide"></div>
    </div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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

    $("#checkAll").click(function () {

        if ($(this).attr("checked")) {
            $("input:checkbox[name='zqId']").attr("checked", true);
        } else {
            $("input:checkbox[name='zqId']").attr("checked", false);
        }
    });

    $("input:checkbox[name='zqId']").click(function () {
        if (!$(this).attr("checked")) {
            $("#checkAll").attr("checked", false);
        } else {
            var c1 = $("input:checkbox[name='zqId']:checked").length;
            var c2 = $("input:checkbox[name='zqId']").length;
            if (c1 == c2) {
                $("#checkAll").attr("checked", true);
            }
        }
    });
    function toConfirm(msg, type) {
        $("#info").html("");
        location.href = _url;
    }
    var _url = "";
    function bfbZqzr(zqId, url) {
        _url = url;
        $(".popup_bg").show();
        $("#info").html(showConfirmDiv("确认下架该债权转让？", 0, "xj"));
    }


    function batchPlxj() {
        var ckeds = $("input:checkbox[name='zqId']:checked");
        if (ckeds == null || ckeds.length <= 0) {
            $("#info").html(showInfo("请选择要处理的记录!", "wrong"));
            return;
        }

        var ids = "";
        for (var i = 0; i < ckeds.length; i++) {
            var val = $(ckeds[i]).val();
            if (i == 0) {
                ids += val;
            } else {
                ids += "," + val;
            }
        }

        _url = "<%=controller.getURI(request,ZqzrxjManage.class)%>?ids=" + ids;
        $(".popup_bg").show();
        $("#info").html(showConfirmDiv("确认批量下架？", 0, "plxj"));
    }
</script>
</body>
</html>