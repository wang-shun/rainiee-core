<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "XZCP";
    PagingResult<T6216> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="w_main">
    <div class="main clearfix">
        <div class="wrap">
            <div class="r_main">
                <div class="home_main">
                    <div class="box box1 mb15">
                        <div class="atil">
                            <h3>借款管理-选择标的产品</h3>
                        </div>
                        <div class="con">
                            <div class="clear"></div>
                            <form action="<%=controller.getURI(request, AddProjectXq.class) %>" method="post"
                                  class="form1" onsubmit="return onSubmit();">

                                <input type="hidden" name="userId" value="<%=request.getAttribute("userId")%>">
                                <input type="hidden" name="type" value="chooseProduct">
                                <ul class="cell yw_jcxx">
                                    <li>
                                        <div class="til"><span class="red">*</span>产品名称：</div>
                                        <div class="info">
                                            <select id="productId" class="text yhgl_ser required" name="productId">
                                                <option value="">--请选择--</option>
                                                <%
                                                    if (result != null && result.getItems() != null) {
                                                        T6216[] products = result.getItems();
                                                        for (T6216 product : products) {%>
                                                <option value="<%=product.F01 %>"><%
                                                    StringHelper.filterHTML(out, product.F02); %></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>

                                            <p tip id="productTip" class="error_tip"></p>

                                            <p errortip class="" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>

                                </ul>
                                <div class="tc w220 pt20"><input type="submit" class="btn4 mr30 sumbitForme"
                                                                 style="cursor: pointer;" fromname="form1" value="下一步"/>
                                    <a href="<%=controller.getURI(request, LoanList.class) %>" class="btn4">返回</a>
                                </div>
                                <div class="clear"></div>
                            </form>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <div class="box2 clearfix"></div>
                </div>
            </div>
        </div>
        <%
            String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
            if (!StringHelper.isEmpty(warringMessage)) {
        %>
        <div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
            <div class="info clearfix">
                <div class="clearfix">
                    <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
                </div>
                <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                               class="btn4 ml50"/></div>
            </div>
        </div>
        <%} %>
        <%@include file="/WEB-INF/include/left.jsp" %>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    function onSubmit() {
        var bl = true;
        $("#productTip").html("");

        var productId = $("#productId").val();

        //alert(minAmount>maxAmount);
        if (productId == "") {
            $("#productTip").html("请选择标的产品");
            //$("#amountTip").addClass("error_tip");

            bl = false;

        }


        return bl;

    }
</script>
</body>
</html>