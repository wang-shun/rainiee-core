<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.entities.T6242" %>
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
    T6242 info = ObjectHelper.convert(request.getAttribute("loan"), T6242.class);

    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    //T6238 t6238=ObjectHelper.convert(request.getAttribute("t6238"), T6238.class);
    //String userName=ObjectHelper.convert(request.getAttribute("userName"), String.class);
    if (info == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    String enterType = request.getParameter("enterType");
    
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i><%="update".equals(enterType)?"修改":"新增"%>公益标信息</div>
                    </div>
                    <div class="border mt20">
                        <a href="<%=controller.getURI(request, GyLoanList.class)%>"
                           class="btn btn-blue2 radius-6 pl20 pr20 mr10" style="float: right;margin-top: 7px;">返回</a>

                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateGyLoan.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn select-a">项目信息<i class="icon-i tab-arrowtop-icon"></i></a></li>

                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanCys.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">倡议书</a></li>

                                <%--
                                <%
                                    if(T6230_F13.S==t6230.F13){
                                %>
                                <li><a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if(T6230_F11.S==t6230.F11)
                                    {
                                %>
                                <li><a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>">担保信息</a></li>
                                <%} %>
                                <li><a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>">附件(马赛克)</a></li>
                                <li><a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>">附件(完整版)</a></li>
                                --%>
                            </ul>
                        </div>

                        <form action="<%=controller.getURI(request, UpdateGyLoan.class)%>" method="post" class="form1"
                              id="form1">
                            <%=FormToken.hidden(serviceSession.getSession()) %>
                            <input type="hidden" name="flag" id="flag"/>
                            <input type="hidden" name="userId" value="<%=userId%>">
                            <input type="hidden" name="userType" value="<%=userType%>">
                            <input type="hidden" name="loanId" value="<%=loanId%>">
                            <input type="hidden" name="F21" value="<%StringHelper.filterHTML(out, info.F21);%>"/>

                            <div class="content-container pl40 pt40 pr40 pb20" id="con_one_1">
                                <ul class="gray6">
                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>公益标题：
												</span>
                                        <input type="text" maxlength="25"
                                               class="text border w300 pl5 required max-length-25" name="F03"
                                               value="<%StringHelper.filterHTML(out, info.F03);%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>公益金额： <br/>
													</span>
                                        <input type="text" class="text border w300 pl5 required min-size-100"
                                               maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F05" value="<%=info.F05%>"/>元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>公益机构：
													</span>
                                        <input type="text" class="text border w300 pl5 required max-length-20"
                                               name="F22" value="<%=info.F22%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>最低起捐： <br/>
													</span>
                                        <input type="text" class="text border w300 pl5 required min-size-0.01"
                                               maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F06" value="<%=info.F06%>"/>元
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>筹款期限：
													</span>
                                        <select id="selectF08" class="text border w300 pl5 required" name="F08">
                                            <%for (int i = 10; i <= 100; i = i + 10) {%>
                                            <option value="<%=i %>" <%if (i == info.F08) { %>
                                                    selected="selected"<%} %> ><%=i %>
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
                                                  class="w400 h120 border p5 required min-length-20 max-length-500"><%StringHelper.format(out, info.F24, fileStore); %></textarea>
                                        <span tip>20-500字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存" onclick="save()"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20  sumbitForme"
                                                   fromname="form1" value="保存并继续" onclick="saveCon()"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20"
                                                   onclick="window.location.href='<%=controller.getURI(request, GyLoanList.class) %>'"
                                                   value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<!--内容-->
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
</script>
<%} %>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=infoMessage%>', "yes"));
</script>
<%} %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>

<script type="text/javascript">
    $("#zry").click(function () {
        $("#fxr").hide();
    });
    $("#gdr").click(function () {
        $("#fxr").show();
    });
    function save() {
        $("#flag").attr("value", "0");
    }
    function saveCon() {
        $("#flag").attr("value", "1");
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

    $("select[name='xian']").on("change", function () {
        if ($(this).val() != null && $(this).val() != "") {
            $(this).nextAll("p").empty();
        }
    });
</script>
</body>
</html>