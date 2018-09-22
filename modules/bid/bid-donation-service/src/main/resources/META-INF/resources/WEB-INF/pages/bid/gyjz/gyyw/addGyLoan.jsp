<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.GyLoanList"%>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.AddGyLoan" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "GYBDGL";
    //T6110_F06 userType=EnumParser.parse(T6110_F06.class, request.getParameter("userType"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    int loanId = IntegerParser.parse(request.getAttribute("loanId") == null ? request.getParameter("loanId") : request.getAttribute("loanId"));
    //String f25 = String.valueOf(request.getAttribute("F25")==null?"":request.getAttribute("F25"));
    //String userName=ObjectHelper.convert(request.getAttribute("userName"), String.class);
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    //String minBidAmount = configureProvider.getProperty(SystemVariable.MIN_BIDING_AMOUNT);

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>公益业务-新增公益标
                        </div>
                    <div class="content-container pl40 pt40 pr40 pb20">
                        <form action="<%=controller.getURI(request, AddGyLoan.class)%>" method="post" id="form1"
                              class="form1">
                            <%=FormToken.hidden(serviceSession.getSession()) %>
                            <input type="hidden" name="flag" id="flag"/>

                            <div class="content-container pl40 pt40 pr40 pb20" id="con_one_1">
                                <ul class="gray6">
                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>公益标题：
												</span>
                                        <input type="text" class="text border w300 pl5 required max-length-25"
                                               name="F03"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F03"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" style="display:none;">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>标的类型：
												</span>
                                        <select id="select" class="text border w300 pl5 required" name="F04">
                                            <%int type = IntegerParser.parse(request.getParameter("F04")); %>
                                            <%
                                                if (t6211s != null) {
                                                    for (T6211 t6211 : t6211s) {
                                                        if (t6211 == null) {
                                                            continue;
                                                        }
                                            %>
                                            <option value="<%=t6211.F01 %>"
                                                    <%if(type==t6211.F01){ %>selected="selected"<%} %>><%
                                                StringHelper.filterHTML(out, t6211.F02); %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>项目金额： <br/>
												</span>
                                        <input type="text"
                                               class="text border w300 pl5 required min-size-100 mulriple-100"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               maxlength="15" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                               mtestmsg="必须为整数" name="F05"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F05"));%>"/>元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>公益机构：
												</span>
                                        <input type="text" class="text border w300 pl5 required max-length-20"
                                               name="F22"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("F22"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">
											<span class="red">*</span>最低起捐： <br/>
										</span>
										<input type="text"
                                               class="text border w300 yw_w5 required more-than-zero" 
                                               maxlength="11" 
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为金额格式(且是两位有效数字)" name="F06" value="5.00"/>元
                                        <span tip>(精确到小数点后两位)</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>筹款期限：
												</span>
                                        <select id="selectF08" class="text border mr20 h32 required" name="F08">
                                            <%for (int i = 10; i <= 100; i = i + 10) { %>
                                            <option value="<%=i %>"><%=i %>
                                            </option>
                                            <%}%>
                                        </select>天
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
												<span class="display-ib w200 tr mr5 fl">
													<span class="red">*</span>项目简介：
												</span>
                                        <textarea name="F24" cols="45" rows="5"
                                                  class="w400 h120 border p5 required min-length-20 max-length-500"><%StringHelper.format(out, request.getParameter("F24"), fileStore); %></textarea>
                                        <span tip>20-500字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5" id="saveBtn">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存" onclick="save()"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存并继续" onclick="saveCon()"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20"
                                                   onclick="window.location.href='<%=controller.getURI(request, GyLoanList.class) %>'"
                                                   value="取消">
                                        </div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5" id="saveSpan" style="display:none;">
                                            <span style="color:red;">正在保存中...</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/addProjectXqValidation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $("#zry").click(function () {
        $("#fxr").hide();
    });
    $("#gdr").click(function () {
        $("#fxr").show();
    });
    function save() {
        $("#flag").val("0");
    }
    function saveCon() {
        $("#flag").val("1");
    }
    function checkSelectValue() {
        if ($("#xianSlt").val() == null || $("#xianSlt").val() == "") {
            return false;
        }
        return true;
    }
    $("#ydb").click(function () {
        var ydb = $("#ydb").attr("checked");
        if (ydb == 'checked') {
            $("#dbfa").show();
        } else {
            $("#dbfa").hide();
        }
    });
    $(function () {
        var hkfs = $("#hkfs").attr("value");
        var ydb = $("#ydb").attr("checked");
        if (ydb == 'checked') {
            $("#dbfa").show();
        } else {
            $("#dbfa").hide();
        }
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#zry").attr("checked", true);
        }
        else {
            $("#gdr").attr("disabled", false);
        }
        var gdr = $("#gdr").attr("checked");
        if (gdr == 'checked') {
            $("#fxr").show();
        }
        loadPage();
    });

    $("#hkfs").click(function () {
        var hkfs = $("#hkfs").attr("value");
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#zry").attr("checked", true);
        }
        else {
            $("#gdr").attr("disabled", false);
        }

        accDay(hkfs);
    });

    function accDay(hkv) {
        if ("YCFQ" == hkv) {
            $("#_accDay").show();
        } else {
            $("#_accDay").hide();
        }
    }
    function loadPage() {
        var hkfs = $("#hkfs").attr("value");
        accDay(hkfs);
    }

    $("#xianSlt").on("change", function () {
        if ($(this).val() != null && $(this).val() != "") {
            $(this).nextAll("p").empty();
        }
    });
</script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "perfect"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "perfect"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>