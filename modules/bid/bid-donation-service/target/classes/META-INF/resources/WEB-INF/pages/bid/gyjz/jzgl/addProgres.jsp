<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.AddProgres" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.ViewProgres" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6245_F05" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "GYBD_PROGRES_GL";

    int userId = IntegerParser.parse(request.getParameter("userId"));
    int loanId = IntegerParser.parse(request.getAttribute("loanId") == null ? request.getParameter("loanId") : request.getAttribute("loanId"));
    T6245_F05[] allStatus = T6245_F05.values();

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>进展管理-新增公益标进展
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">

                            <form action="<%=controller.getURI(request, AddProgres.class)%>" method="post" id="form1"
                                  class="form1">
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <input type="hidden" name="flag" id="flag"/>
                                <input type="hidden" name="userId" id="userId" value="<%=userId %>"/>
                                <input type="hidden" name="loanId" id="loanId" value="<%=loanId %>"/>

                                <div class="content-container pl40 pt40 pr40 pb20" id="con_one_1">
                                    <ul class="gray6">
                                        <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>标题时间：
													</span>
                                            <input type="text" readonly="readonly" name="F08" id="datepicker1"
                                                   class="text border w300 pl5 date required" onblur="checkDate()"/>
                                            <%--
                                            <input type="text" maxlength="14" class="text yhgl_ser required max-length-14" name="F08" value="<%StringHelper.filterHTML(out, request.getParameter("F08"));%>"/>
                                            --%>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>主题标题：
													</span>
                                            <input type="text" maxlength="40"
                                                   class="text border w300 pl5 required min-length-4 max-length-40"
                                                   name="F04"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F04"));%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red">*</span>是否发布：
													</span>
                                            <select id="select" class="text border mr20 h32 required" name="F05">
                                                <% String isFb = request.getParameter("F05"); %>
                                                <%
                                                    if (allStatus != null) {
                                                        for (T6245_F05 fb : allStatus) {
                                                            if (fb == null) {
                                                                continue;
                                                            }
                                                %>
                                                <option value="<%=fb.name() %>"
                                                        <%if(isFb==fb.name()){ %>selected="selected"<%} %>><%
                                                    StringHelper.filterHTML(out, fb.getChineseName()); %></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>

                                        <li class="mb10">
													<span class="display-ib w200 tr mr5 fl">
														<span class="red">*</span>简要介绍：
													</span>
                                            <textarea name="F06" cols="45" rows="5"
                                                      class="w400 h120 border p5 required min-length-20 max-length-500"><%StringHelper.format(out, request.getParameter("F06"), fileStore); %></textarea>
                                            <span tip>20-500字</span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
													<span class="display-ib w200 tr mr5">
														<span class="red"></span>查看更多：
													</span>
                                            <input type="text" class="text border w300 pl5  max-length-100 isurl"
                                                   name="F09"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("F09"));%>"/>（复制外链地址，如：http://*********）
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5" id="saveBtn">
                                                <input type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                       fromname="form1" value="保存"/>
                                                <input type="button"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                       value="取消"
                                                       onclick="window.location.href='<%=controller.getURI(request, ViewProgres.class)%>?loanId=<%=loanId %>&userId=<%=userId %>'"/>
                                            </div>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5" id="saveSpan" style="display:none;">
                                                <span style="color:red;">正在保存中...</span>
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
    </div>
    <!--内容-->
    <div class="popup_bg" style="display: none;"></div>
    <div id="info"></div>
    <%@include file="/WEB-INF/include/datepicker.jsp" %>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/addProjectXqValidation.js"></script>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
    <script type="text/javascript">

        function save() {
            $("#flag").val("0");
        }
        function saveCon() {
            $("#flag").val("1");
        }

        $(function () {

            $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
            $('#datepicker1').datepicker({inline: true});
        });

        function checkDate() {
            window.setTimeout(function () {
                var dates = $("#datepicker1");
                var d = dates.val();
                //alert(d);
                if (d != "") {
                    var $error = dates.nextAll("span[errortip]");
                    var $tip = dates.nextAll("span[tip]");
                    $error.removeClass("error_tip");
                    $error.hide();
                    $tip.show();
                }
            }, 150);
        }

    </script>
        <%
		String warringMessage=controller.getPrompt(request, response, PromptLevel.WARRING);
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
		String errorMessage=controller.getPrompt(request, response, PromptLevel.ERROR);
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