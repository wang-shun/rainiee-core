<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.GrjkyxList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F17" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F16" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F15" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Grjkyx" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.ViewYclGrjkyx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.ViewWclGrjkyx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.GrjkyxCl" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.DcGrjkyx" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F11" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "GRJKYX";
    PagingResult<Grjkyx> list = (PagingResult<Grjkyx>) request.getAttribute("loanIntentions");
    Grjkyx[] grjkyxArray = list.getItems();
    BigDecimal searchPersonalAmount = (BigDecimal) request.getAttribute("searchPersonalAmount");
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, GrjkyxList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>个人借款意向管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">联系人</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                        <input type="hidden" id="shengId"
                                               value="<%String sheng=request.getParameter("sheng"); if(!StringHelper.isEmpty(sheng)){StringHelper.filterHTML(out, sheng.substring(0,2)+"0000");}%>">
                                        <input type="hidden" id="shiId"
                                               value="<%String shi=request.getParameter("shi"); if(!StringHelper.isEmpty(shi)){StringHelper.filterHTML(out, shi.substring(0,4)+"00");}%>">
                                        <input type="hidden" id="xianId"
                                               value="<%String xian=request.getParameter("xian"); if(!StringHelper.isEmpty(xian)){StringHelper.filterHTML(out, xian);}%>">
                                    </li>
                                    <li><span class="display-ib mr5">所在城市</span>
                                        <select name="sheng" class="border mr20 h32 mw100">
                                        </select>
                                        <select name="shi" class="border mr20 h32 mw100">
                                        </select>
                                        <select name="xian" class="border mr20 h32 mw100">
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">提交时间</span>
                                        <input type="text" readonly="readonly" name="createTimeStart" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="createTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>

                                        <select name="loanIntentionState" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6282_F11 t6282_F11 : T6282_F11.values()) {
                                            %>
                                            <option value="<%=t6282_F11.name()%>" <%if (t6282_F11.name().equals(request.getParameter("loanIntentionState"))) {%>
                                                    selected="selected" <%}%>><%=t6282_F11.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(DcGrjkyx.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i
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
                                    <th>联系人</th>
                                    <th>手机号码</th>
                                    <th>借款金额(元)</th>
                                    <th>借款期限</th>
                                    <th>借款类型</th>
                                    <th>所在城市</th>
                                    <th>筹款期限</th>
                                    <th>借款描述</th>
                                    <th>提交时间</th>
                                    <th>状态</th>
                                    <th class="w200 tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (grjkyxArray != null) {
                                        for (int i = 0; i < grjkyxArray.length; i++) {
                                            Grjkyx grjkyx = grjkyxArray[i];
                                            if (grjkyx == null) {
                                                continue;
                                            }
                                %>
                                <tr class="dhsbg tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, grjkyx.F03);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(grjkyx.F03, 4));
                                    %></td>
                                    <td><%StringHelper.filterHTML(out, grjkyx.F04);%></td>
                                    <td><%=Formater.formatAmount(grjkyx.F06)%>
                                    </td>
                                    <td><%=grjkyx.F19%>个月</td>
                                    <td>

                                        <% if (grjkyx.F15 == T6282_F15.F && grjkyx.F16 == T6282_F16.F && grjkyx.F17 == T6282_F17.F) { %>
                                        信用
                                        <% } else {%>
                                        <%if (grjkyx.F15 == T6282_F15.S) {%>抵押&nbsp; <%}%>
                                        <%if (grjkyx.F16 == T6282_F16.S) {%>实地认证&nbsp;  <%}%>
                                        <%if (grjkyx.F17 == T6282_F17.S) {%>担保 <%
                                            }
                                        }
                                    %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, grjkyx.szcs);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(grjkyx.szcs, 10));
                                    %></td>
                                    <td><%=StringHelper.isEmpty(grjkyx.F09)?"无":grjkyx.F09%></td>
                                    <td title="<%StringHelper.filterHTML(out, grjkyx.F10);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(grjkyx.F10, 13));%></td>
                                    <td><%=TimestampParser.format(grjkyx.F13)%>
                                    </td>
                                    <td><%=grjkyx.F11.getChineseName()%>
                                    </td>
                                    <td class="tc">
                                        <%
                                            if (T6282_F11.WCL.equals(grjkyx.F11)) {
                                                if (dimengSession.isAccessableResource(GrjkyxCl.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showCl('<%=grjkyx.F01%>')"
                                           class="link-blue mr20 click-link">处理</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">处理</a>
                                        <%
                                            }
                                            if (dimengSession.isAccessableResource(ViewWclGrjkyx.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewWclGrjkyx.class)%>?id=<%=grjkyx.F01%>"
                                           class="link-blue mr20 click-link">详情</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">详情</a>
                                        <%
                                            }
                                        } else {
                                            if (dimengSession.isAccessableResource(ViewYclGrjkyx.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewYclGrjkyx.class)%>?id=<%=grjkyx.F01%>"
                                           class="link-blue mr20 click-link">详情</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">详情</a>
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
                                <tr class="tc">
                                    <td colspan="12">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
						<div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchPersonalAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, list);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div class="popup-box hide" id="cl">
    <form action="<%=controller.getURI(request, GrjkyxCl.class)%>" method="post" class="form1">
        <input type="hidden" id="id" name="id"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">处理</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em class="gray3">处理结果描述
                        （50字以内）：</em></span>
                        <textarea name="disposeDesc" cols="40" rows="4"
                                  class="required w400 h120 border p5 max-length-50"></textarea>
                        <br/>
                        <span tip class=""></span>
                        <span errortip class="" style="display: none"></span>
                    </li>
                </ul>
            </div>
            <div class="tc f16">
                <input type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" id="button3"
                       fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcGrjkyx.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, GrjkyxList.class)%>";
    }

    $("#checkAll").click(function () {

        if ($(this).attr("checked")) {
            $("input:checkbox[name='clId']").attr("checked", true);
            var $tj = $("input[istrue]");
            if ($tj != undefined && $tj.attr("istrue") == "true") {
                $tj.attr("checked", false);
            }
        } else {
            $("input:checkbox[name='clId']").attr("checked", false);
        }
    });

    $("input:checkbox[name='clId']").click(function () {
        if (!$(this).attr("checked")) {
            $("#checkAll").attr("checked", false);
        } else {
            var c1 = $("input:checkbox[name='clId']:checked").length;
            var c2 = $("input:checkbox[name='clId']").length;
            if (c1 == c2) {
                $("#checkAll").attr("checked", true);
            }
        }
    });

    function batchCl() {
        var ckeds = $("input:checkbox[name='clId']:checked");
        if (ckeds == null || ckeds.length <= 0) {
            alert("请选择要处理的记录!");
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

        document.getElementById('id').value = ids;
        $("#cl").show();
    }

    function showCl(id) {
        $("#cl").show();
        $("textarea[name='disposeDesc']").val("");
        $("div.popup_bg").show();
        document.getElementById('id').value = id;
    }
</script>
</body>
</html>