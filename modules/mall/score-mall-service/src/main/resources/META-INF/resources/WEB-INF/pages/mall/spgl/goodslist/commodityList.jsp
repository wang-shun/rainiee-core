<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S63.enums.T6351_F11" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityAdd" %>
<%@ page import="com.dimeng.p2p.repeater.score.entity.T6351Ext" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityUpdate" %>
<%@ page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@ page import="com.dimeng.p2p.common.enums.YesOrNo" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityExport" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityOrderDet" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
%>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "SPLB";
    PagingResult<T6351Ext> t6351List = ObjectHelper.convert(request.getAttribute("t6351List"), PagingResult.class);
    List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
    Map<String, Object> t6351Sum = ObjectHelper.convert(request.getAttribute("t6351Sum"), Map.class);

    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, CommodityList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>商品列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">商品名称</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>

                                    <li><span class="display-ib mr5">商品类别</span>
                                        <%
                                            int catId = IntegerParser.parse(request.getParameter("catId"));
                                        %>
                                        <select id="catId" class="border mr20 h32 mw100" name="catId">
                                            <option value="0">全部</option>
                                            <%
                                                if (t6350List != null && t6350List.size() > 0) {
                                                    for (T6350 t6350 : t6350List) {
                                            %>
                                            <option value="<%=t6350.F01%>"
                                                    <%if(catId==t6350.F01){%>selected="selected"<%}%>><%
                                                StringHelper.filterHTML(out, t6350.F03);
                                            %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>
                                        <%
                                            String status = request.getParameter("status");
                                        %>
                                        <select id="" class="border mr20 h32 mw100" name="status">
                                            <option value="">全部</option>
                                            <%  for(T6351_F11 c : T6351_F11.values()) {  %>
                                            <option value="<%=c.name()%>" <%if(c.name().equals(status)){%>selected="selected"<%}%>>
                                                <%StringHelper.filterHTML(out, c.getChineseName());%>
                                            </option>
                                                <%}%>
                                        </select>
                                    </li>

                                    <li><span class="display-ib mr5">创建时间</span>
                                        <input type="text" readonly="readonly" name="startTime" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="endTime" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>

                                    <li><span class="display-ib mr5">库存</span>
                                        <input type="text" name="stockMin" id="stockMin" onblur="checkStock();" class="text border pl5 w60" value="<%StringHelper.filterHTML(out, request.getParameter("stockMin"));%>" onkeyup="value=value.replace(/\D/g,'')"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="stockMax" id="stockMax" onblur="checkStock();" class="text border pl5 w60 mr20" value="<%StringHelper.filterHTML(out, request.getParameter("stockMax"));%>" onkeyup="value=value.replace(/\D/g,'')"/>
                                    </li>


                                    <li><a href="javascript:search();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>

                                        <%if (dimengSession.isAccessableResource(CommodityAdd.class)) {%>
                                        <a href="<%=controller.getURI(request, CommodityAdd.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增商品</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增商品</span>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(CommodityExport.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        }else{
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
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
                                    <th>商品编码</th>
                                    <th>商品名称</th>
                                    <th>商品积分</th>
                                    <th>商品价格(元)</th>
                                    <th>成交数量</th>
                                    <th>成交笔数</th>
                                    <th>库存</th>
                                    <th>状态</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
                                    <th>操作时间</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (t6351List != null && t6351List.getItems().length>0) {
                                        int i = 1;
                                        for (T6351Ext t6351 : t6351List.getItems()) {
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t6351.F02);%></td>
                                    <td title="<%StringHelper.filterHTML(out, t6351.F03);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(t6351.F03, 10));
                                    %></td>
                                    <td><%=(YesOrNo.yes.name().equals(t6351.F16.name())?t6351.F05:0) %></td>
                                    <td><%=(YesOrNo.yes.name().equals(t6351.F17.name())?Formater.formatAmount(t6351.F15):"0.00")%>
                                    </td>
                                    <td><%=t6351.recordNum %></td>
                                    <td><%=t6351.F07 %></td>
                                    <td><%=t6351.F06 %></td>
                                    <td><%StringHelper.filterHTML(out, t6351.F11.getChineseName()); %></td>
                                    <td><%StringHelper.filterHTML(out, t6351.createName); %></td>

                                    <td><%=DateTimeParser.format(t6351.F13)%></td>
                                    <td><%=DateTimeParser.format(t6351.F14)%></td>

                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(CommodityUpdate.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, CommodityUpdate.class)%>?F01=<%=t6351.F01%>"
                                           class="link-blue">修改</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">修改</a>
                                        <%
                                            }
                                        %>

                                        <%
                                            if (dimengSession.isAccessableResource(CommodityUpdate.class)) {
                                        %>

                                            <%if(T6351_F11.sold.name().equals(t6351.F11.name())){%>
                                                <a href="javascript:void(0)" class="link-blue" id="xj_id" onclick="showXj(<%=t6351.F01%>)"> 下架 </a>
                                            <%}else{%>
                                                <a href="javascript:void(0)" class="link-blue" id="sj_id" onclick="showSj(<%=t6351.F01%>)"> 上架 </a>
                                            <%}%>

                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" class="disabled">
                                            <%if(T6351_F11.sold.name().equals(t6351.F11.name())){%>
                                            下架
                                            <%}else{%>
                                            上架
                                            <%}%>
                                        </a>
                                        <%
                                            }
                                        %>
                                        
                                         <%
                                            if (dimengSession.isAccessableResource(CommodityOrderDet.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, CommodityOrderDet.class)%>?id=<%=t6351.F01%>" class="link-blue">查看订单 </a>
                                        <%} else {%>
                                        <a href="javascript:void(0)" class="disabled">查看订单</a>
                                        <%}%>

                                    </td>
                                </tr>
                                <%
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
                        <div class="mb10">
                            <span class="mr20">成交数量总计：<em class="red"><%=t6351Sum.get("sumF07") %>
                            </em> 件</span>
                            <span class="mr20">库存总计：<em class="red"><%=t6351Sum.get("sumF06") %>
                            </em> 件</span>

                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, t6351List);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
            <div class="popup_bg hide"></div>
        </div>
    </div>





<div class="popup-box hide" style="width:400px;min-height:200px;" id="sjDivId">
    <form action="<%=controller.getURI(request, CommodityUpdate.class) %>" id="sjForm" method="post" class="form1">
        <input type="hidden" name="F01" id="sj_cid"/>
        <input type="hidden" name="F11" value="<%=T6351_F11.sold.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要上架此商品吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button3" href="javascript:$('#sjForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" style="width:400px;min-height:200px;" id="xjDivId">
    <form action="<%=controller.getURI(request, CommodityUpdate.class) %>" id="xjForm" method="post" class="form1">
        <input type="hidden" name="F01" id="xj_cid"/>
        <input type="hidden" name="F11" value="<%=T6351_F11.unsold.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要下架此商品吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button3" href="javascript:$('#xjForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var errorMsg = '<%=errorMessage%>'+'<%=warringMessage%>';
    var infoMsg = '<%=infoMessage%>';
    $(function () {
        if (errorMsg != "" && errorMsg != "nullnull" ) {
            $("#info").html(showDialogInfo(errorMsg, "wrong"));
            $("div.popup_bg").show();
        }
        if (infoMsg != "" && infoMsg != "null" ) {
            $("#info").html(showDialogInfo(infoMsg, "yes"));
            $("div.popup_bg").show();
        }
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker1').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker2').datepicker({inline: true});
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    //上架操作
    function showSj(cid) {
        $(".popup_bg").show();
        $("#sj_cid").attr("value", cid);
        $("#sjDivId").show();
    }

    //下架操作
    function showXj(cid) {
        $(".popup_bg").show();
        $("#xj_cid").attr("value", cid);
        $("#xjDivId").show();
    }

    //查询
    function search() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    //校验库存查询条件
    function checkStock(){
        var stockMin = parseInt($("#stockMin").val());
        var stockMax = parseInt($("#stockMax").val());
        //最小值不能大于最大值
        if(stockMin > 0 && stockMin > stockMax){
            $("#stockMax").val(stockMin);
        }
    }

    //导出
    function showExport() {
        $("#form_loan").attr("action","<%=controller.getURI(request, CommodityExport.class)%>");
        $("#form_loan").submit();
        $("#form_loan").attr("action","<%=controller.getURI(request, CommodityList.class)%>");
    }
</script>
</body>
</html>