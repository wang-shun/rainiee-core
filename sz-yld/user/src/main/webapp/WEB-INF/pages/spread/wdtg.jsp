<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.user.servlets.spread.CheckYqm" %>
<%@page import="com.dimeng.p2p.S61.enums.T6111_F06" %>
<%@page import="com.dimeng.p2p.account.user.service.SpreadManage" %>
<%@page import="com.dimeng.p2p.user.servlets.spread.Wdtg" %>
<%-- <%@include file="/WEB-INF/include/authenticatedSession.jsp" %> --%>
<html>
<head>
    <title>我的推广-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
</head>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "WDTG";

    SpreadManage manage2 = serviceSession.getService(SpreadManage.class);
    String newYqm = manage2.getMyNewYqm();
%>
<body>
<div class="container fr mb20">
    <form action="<%=controller.getViewURI(request, Wdtg.class)%>" method="post">
            <%if(!StringHelper.isEmpty(newYqm)){ %>
        <div class="p15">
            <div class="ml15">
                <span class="f18 gray3">我的推荐人：</span>
	            <span>
	            	<%if (T6111_F06.S.getChineseName().equalsIgnoreCase(manage2.getT6111_F06().getChineseName())) {%>
	            		<%=newYqm.substring(0, 3) + "****" + newYqm.substring(7, 11)%>
	            	<%} else {%>
	            		<%=newYqm %>
	            	<%} %>
	            </span>
            </div>
        </div>
            <%}else{%>
        <div class="p15">
            <div class="ml15 clearfix">
                <span class="f18 gray3 fl">我的推荐人</span>
                <input type="text" value="邀请码/推广人手机号码" id="myCode" class="text gray9 pl10 pr10 yhgl_ser fl ml10">
                <input type="button" id="myCodeBtn" class="btn01 fl ml10" value="确定" style="cursor: pointer;">
            </div>
            <div class="red mt5" id="errMsg" style="margin-left:110px;"></div>
        </div>
            <%} %>
</div>
<div class="container fr">
    <div class="p15">
        <div class="user_lsjt mb20">邀请好友统计</div>
        <table id="dataTable1" width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc mb20">
            <tr class="user_lsbg">
                <td width="19%">推广客户数(个)</td>
                <td width="16%">持续奖励总计(元)</td>
                <td width="18%">有效推广奖励总计(元)</td>
                <td width="18%">推广奖励总计(元)</td>
            </tr>
        </table>
        <div class="user_lsjt mb20">邀请好友注册详情</div>
        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
            <tr class="user_lsbg">
                <td width="19%">您推荐好友的用户名</td>
                <td width="16%">注册时间</td>
            </tr>
        </table>
        <div class="page p15" id="pageContent"></div>
    </div>
</div>
<input type="hidden" id="wdtgUrl" value="<%=controller.getURI(request, Wdtg.class)%>"/>
</form>
<div class="clear"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/spread.js"></script>
<%@include file="/WEB-INF/include/script.jsp" %>
<%@include file="/WEB-INF/include/style.jsp" %>
<script type="text/javascript">
    $(function () {
        //invateFriendsRegCount();
        invateFriendsRegPaging();
    });


    var myCodeMsg = "邀请码/推广人手机号码";
    var errorMsg = "您输入的邀请码/手机号码不存在！";
    var errorMsg1 = "不能输入自己的邀请码！";
    $(function () {
        $("#myCode").focus(function () {
            var obj = $(this);
            var _val = obj.val();
            if (_val == myCodeMsg) {
                obj.val("");
            }
        }).blur(function () {
            var obj = $(this);
            var _val = obj.val();
            if (_val == "") {
                obj.val(myCodeMsg);
            }
        });

        $("#myCodeBtn").click(function () {
            ajaxGetCode();
        });
    });

    function ajaxGetCode() {
        var code = $("#myCode").val();
        if ($.trim(code) == "" || code.length == 0 || code == myCodeMsg) {
            return false;
        } else {
            $.ajaxSetup({async: false});
            var codeURL = '<%=controller.getURI(request, CheckYqm.class)%>';
            $.post(codeURL, {
                code: code
            }, function (data) {
                if ($.trim(data) == -1) {
                    $("#errMsg").html(errorMsg);
                    return false;
                } else if ($.trim(data) == -2) {
                    $("#errMsg").html(errorMsg1);
                    return false;
                } else {
                    window.location.reload(true);
                }
            });
        }
        return true;
    }
</script>
</body>
</html>