<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看公益标信息</div>
                    </div>
                    <div class="border mt20">
                        <a href="<%="jzgl".equals(enterType)?controller.getURI(request, GyLoanProgresList.class) : controller.getURI(request, GyLoanList.class)%>"
                           class="btn btn-blue2 radius-6 pl20 pr20 mr10" style="float: right;margin-top:5px;">返回</a>

                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request,ViewGyLoan.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">项目信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGyLoanCys.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn select-a">倡议书<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%if (null != info && (info.t6242.F11 == T6242_F11.DFB || info.t6242.F11 == T6242_F11.JKZ || info.t6242.F11 == T6242_F11.YJZ)) { %>

                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanProgres.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">最新进展</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanJzjl.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">捐助记录</a></li>
                                <%} %>

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
                                            <textarea name="content" readonly="readonly" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;display: none;"><%StringHelper.format(out, content, fileStore);%></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <% if (info.t6242.F11 == T6242_F11.DSH && "gyywsh".equals(enterType)) {%>
                                            <a href="<%=controller.getURI(request, CheckGyBid.class) %>?loanId=<%=loanId %>&userId=<%=userId %>"
                                               class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme">审核通过</a>
                                            <a href="javascript:void(0)" onclick="showSh()"
                                               class="btn btn-blue2 radius-6 pl20 pr20">审核不通过</a>
                                            <!-- <input type="submit" class="btn4 mr30" value="确认" />

                             <input type="submit" class="btn4 mr30 sumbitForme" fromname="form1" value="保存" onclick="save()" />
                             <input type="submit" class="btn4 mr30 sumbitForme" fromname="form1" value="提交" onclick="saveCon()"/>
                             -->

                                            <% }%>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="popup-box hide" id="sh" style="min-height: 200px;display: none;">
            <form action="<%=controller.getURI(request, NotThrough.class) %>" id="shForm" method="post" class="form1">
                <input type="hidden" name="loanId" value="<%=loanId %>"/>
                <input type="hidden" name="useId" value="<%=userId %>"/>

                <div class="popup-title-container">
                    <h3 class="pl20 f18">审核不通过</h3>
                    <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
                </div>
                <div class="popup-content-container pt20 pb20 clearfix">
                    <div>
                        <ul class="gray6">
                            <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em
                                    class="gray3">审核处理结果描述（50字以内）</em></span>
                                <textarea name="des" id="textarea2" rows="3"
                                          class="required ww100 border max-length-50"></textarea>
                                <span tip class="red"></span>
                                <span errortip class="red" style="display: none"></span></li>
                        </ul>
                    </div>
                    <div class="tc f16">
                        <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                               id="button3" fromname="form1" value="确认"/>
                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20" onclick="closeInfo()" value="取消"/>
                    </div>
                </div>
            </form>
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
        <div class="popup_bg hide"></div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            readonlyMode: true,
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
    function showSh() {
        $(".popup_bg").show();
        $("#sh").show();
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