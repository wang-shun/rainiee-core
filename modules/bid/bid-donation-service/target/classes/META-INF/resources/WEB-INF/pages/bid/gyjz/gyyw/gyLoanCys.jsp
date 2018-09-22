<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.GyLoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.UpdateGyLoan" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.GyLoanCys" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.GyLoanList" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    GyLoan info = ObjectHelper.convert(request.getAttribute("loan"), GyLoan.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
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
                                       class="tab-btn ">项目信息</a></li>

                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanCys.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn select-a">倡议书<i class="icon-i tab-arrowtop-icon"></i></a></li>

                            </ul>
                        </div>

                        <%
                            String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
                        %>
                        <form action="<%=controller.getURI(request, GyLoanCys.class)%>" method="post" name="example"
                              onsubmit="return onSubmit();">
                            <%=FormToken.hidden(serviceSession.getSession()) %>
                            <input type="hidden" name="flag" id="flag"/>
                            <input type="hidden" name="userId" value="<%=userId%>">
                            <input type="hidden" name="loanId" value="<%=loanId%>">
                            <input type="hidden" name="F21" value="<%StringHelper.filterHTML(out, info.t6242.F21);%>"/>

                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>内容编辑：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, content, fileStore);%></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <!-- <input type="submit" class="btn4 mr30" value="确认" /> -->

                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存" onclick="save()"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交" onclick="saveCon()"/>
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
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.GYLOAN_CYS_FILE.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("倡议书内容不能为空");
                }
                else {
                    $("#errorContent").removeClass("red");
                    $("#errorContent").html("&nbsp;");
                }
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });


    function onSubmit() {
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("倡议书内容不能为空");
            return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            return true;
        }
    }

    function save() {
        $("#flag").attr("value", "0");
    }
    function saveCon() {
        $("#flag").attr("value", "1");
    }
</script>
</body>
</html>