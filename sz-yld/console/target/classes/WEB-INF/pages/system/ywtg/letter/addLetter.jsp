<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.LetterList"%>
<%@page import="com.dimeng.p2p.common.enums.SendType" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.AddLetter" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.ImportLetter" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.SelectUser" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/kindeditor.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
	CURRENT_CATEGORY = "XTGL";
	CURRENT_SUB_CATEGORY = "ZNXTG";
    String[] userNames = ObjectHelper.convertArray(request.getAttribute("userNames"), String.class);
    String sendType = request.getParameter("sendType");
    String errorMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
    if (StringHelper.isEmpty(sendType)) {
        sendType = SendType.ZDR.name();
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容 开始-->
                <div class="p20">
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, AddLetter.class)%>" method="post" class="form1"
                          id="form1" onsubmit="return onSubmit();">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>站内信推广
                            </div>
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>发送对象：</span>
                                            <div class="pl200">
                                                <%for (SendType type : SendType.values()) {%>
                                                <input type="radio" value="<%=type.name()%>" name="sendType"
                                                       onclick="showLetter('<%=type%>')"
                                                        <%if (type.name().equals(sendType)) {%> checked="checked"
                                                        <%}%> /><%=type.getName()%><span class="ml50"></span><%}%>
                                            </div>
                                        </li>
                                        <li class="mb10" id="letterLi"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>用户名：</span>

                                            <div class="pl200 clearfix" id="userDIV"><textarea name="userName"
                                                                                               id="username"
                                                                                               placeholder="发送给多人请用空格隔开，如：user01 user02"
                                                                                               cols="45" rows="5"
                                                                                               class="ww50 h120 border p5 fl required"><%
                                                if (userNames != null) {
                                                    for (String s : userNames) {
                                            %><%=s + "\n"%><%
                                                }
                                            } else {
                                            %><%StringHelper.filterHTML(out, request.getParameter("userName"));%><%}%></textarea>
                                                <a href="javascript:void(0);" id="addUser"
                                                   class="btn-blue2 btn white radius-6 pl20 pr20 ml20 fl">新增</a>
                                                <a href="javascript:void(0);" id="importUser"
                                                   class="btn-blue2 btn white radius-6 pl20 pr20 ml20">导入用户名</a>
                                                <span tip></span>
                                                <span errortip class="" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb10" id="importinfo"><span
                                                class="display-ib w200 tr mr5 fl">&nbsp;</span>
											<%if(!StringHelper.isEmpty(errorMessage)){%>
							                <div class="pl200 clearfix red"><%=errorMessage %></div>
							                <%}else{ %>
                                            <div class="pl200 clearfix">支持导入csv、txt格式数据</div>
                                            <%} %>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>站内信标题：</span>

                                            <div class="pl200">
                                                <input class="text border w300 yw_w5 required required" type="text"
                                                       maxlength="30"
                                                       value="<%StringHelper.filterHTML(out, request.getParameter("title"));%>"
                                                       name="title">
                                                <span tip></span>
                                                <span errortip class="" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>站内信内容：</span>

                                            <div class="pl200">
                                                <textarea name="content" id="content" cols="80" rows="9"
                                                          style="width: 80%; height: 200px; visibility: hidden;"><%StringHelper.filterHTML(out, request.getParameter("content"));%></textarea>
                                                <span id="errorContent">&nbsp;</span>
                                            </div>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200">
                                                <input type="submit" value="确定"
                                                       class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                                                       fromname="form1"/>
                                                <input type="button" value="取消"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                       onclick="window.location.href='<%=controller.getURI(request, LetterList.class)%>'">
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<div id="import" style="display: none">
    <div class="popup-box">
        <div class="popup-title-container">
            <h3 class="pl20 f18">导入</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);"
               onclick="javascript:document.getElementById('import').style.display='none';return false;"></a>
        </div>
        <form action="<%=controller.getURI(request, ImportLetter.class)%>" id="import_form" method="post"
              enctype="multipart/form-data" class="form2">
            <div class="popup-content-container pt20 ob20 clearfix">
                <div class="mb40 gray6">
                    <ul>
                        <li class="mb10"><span class="display-ib tr mr5"><em class="f24 gray3">导入用户名：</em></span>
                            <input class="text border w150 pl5" type="file" name="file" value="导入用户名"/>
                        </li>
                    </ul>
                </div>
                <div class="tc f16">
                    <a href="javascript:$('#import_form').submit();"
                       class="btn-blue2 btn white radius-6 pl20 pr20">提交</a>
                    <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="javascript:document.getElementById('import').style.display='none';return false;">取消</a>
                </div>
            </div>
        </form>
    </div>
    <div class="popup_bg"></div>
</div>

<div id="addView" style="display: none">
    <div class="popup-box" style="width:650px;">
        <div class="popup-title-container">
            <h3 class="pl20 f18">新增</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);"
               onclick="javascript:document.getElementById('addView').style.display='none';javascript:document.getElementById('pageContent').style.display='none';return false;"></a>
        </div>
        <div class="popup-content-container pt20 ob20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="f24 gray3">用户名：</em></span>
                        <input class="text border w150 pl5" type="text" value="" id="selectUserName"/>
                        <a class="btn-blue2 btn white radius-6 pl20 pr20" id="selectUser" onclick="searchUser(1);"
                           href="javascript:void(0);">搜索</a>&nbsp;&nbsp;&nbsp;
                        <a class="btn btn-blue2 radius-6 pl20 pr20" href="javascript:void(0);"
                           onclick="javascript:document.getElementById('addView').style.display='none';javascript:document.getElementById('pageContent').style.display='none';return false;">取消</a>
                    </li>
                    <li class="mb10">
                        <table border="1" width="100%" height="100%" id="userList" style="margin-top: 10px;">
                            <tr align="center">
                                <td>序号</td>
                                <td>用户名</td>
                                <td>操作</td>
                            </tr>
                        </table>
                        <div class="page p15 paging" id="pageContent"></div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="popup_bg"></div>
</div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/pageUtil.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="popup-box" id="closeInfoShow">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfoShow()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfoShow();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
<script type="text/javascript">

    $("#importUser").click(function () {
        $("#import").show();
    });
    $("#close").click(function () {
        $("#import").hide();
    });
    $("#addUser").click(function () {
        $("#selectUserName").attr("value", "");
        $("table#userList tr").eq(0).nextAll().remove();
        $("#addView").show();
        searchUser(1);
    });
    var _selectUserURL = '<%=controller.getURI(request, SelectUser.class)%>';

    function searchUser(index) {
        var userName = $("#selectUserName").val();
        $.ajax({
            type: "post",
            dataType: "json",
            url: _selectUserURL,
            data: {"currentPage": index, "pageSize": pageSize, "userName": userName},
            async: false,
            success: function (data) {
                //展示数据前先清空数据
                $("#userList tr").empty();
                var trHTML = "<tr align='center'><td>序号</td><td>用户名</td><td>操作</td></tr>";
                $("#userList").append(trHTML);
                trHTML = "";
                //分页信息
                if (data.userList != null) {
                    $("#pageContent").html(data.pageStr);
                } else {
                    $("#pageContent").empty();
                }
                pageCount = data.pageCount;
                //分页点击事件
                $("a.number").click(function () {
                    pageParam(this, 1);
                });
                if (null == data.userList) {
                    $("#userList").append("<tr align='center'><td colspan='3'>暂无数据</td></tr>");
                } else if (data.userList.length > 0) {
                    var rAssests = data.userList;
                    $.each(rAssests, function (n, value) {

                        trHTML += "<tr align='center'><td>" + (n + 1) + "</td>" +
                                "<td>" + value.F02 + "</td>" +
                                "<td><a href=\"javascript:void(0);\" onclick=\"addUserName(\'" + value.F02 + "\',this);\" style=\"color: #2894d9;\" >新增</a>&nbsp;<span class=\"red\"></span></tr>";
                        $("#userList").append(trHTML);//在table最后面新增一行
                        trHTML = "";
                    });
                }
                $("#pageContent").show();
            }
        });
    }

    function toAjaxPage(){
    	searchUser(currentPage);
    }

    function addUserName(userName,obj) {
        var userTextarea = $("#username").val();
        if(userTextarea.indexOf(userName)==-1) {
            $("#username").val(userTextarea + userName + " ");
            $(obj).next().show().html("新增成功");
            $(obj).hide();
        }else{
            $(obj).hide();
            $(obj).next().show().html("不可新增重复的账号");
        }
    }

    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            allowPreviewEmoticons: false,
            allowImageUpload: false,
            items: [
                'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'link'],
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("站内信内容不能为空");
                }
                else if (this.count('text') > 250) {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("站内信内容不能超过250个字");
                }
                else {
                    $("#errorContent").removeClass("red");
                    $("#errorContent").html("&nbsp;");
                }
            }
        });
        prettyPrint();
    });
    function onSubmit() {
        if (editor1.count('text') == '') {
            $("#errorContent").addClass("red");
            $("#errorContent").html("站内信内容不能为空");
            return false;
        }
        else if (editor1.count('text') > 250) {
            $("#errorContent").addClass("red");
            $("#errorContent").html("站内信内容不能超过250个字");
            return false;
        }
        else {
            $("#errorContent").removeClass("red");
            $("#errorContent").html("&nbsp;");
            return true;
        }
    }
    function showLetter(type) {
        if (type == '<%=SendType.ZDR%>') {
            $("#letterLi").show();
            $("#importinfo").show();
            $("#username").attr("value", "");
            $("#zdrPrompt").show();
        } else if (type == '<%=SendType.SY%>') {
            $("#letterLi").hide();
            $("#importinfo").hide();
            $("#username").attr("value", "all");
            $("#zdrPrompt").hide();
        }
    }

    function closeInfoShow() {
        $("#closeInfoShow").hide();
        $(".popup_bg").hide();
    }
</script>
</body>
</html>