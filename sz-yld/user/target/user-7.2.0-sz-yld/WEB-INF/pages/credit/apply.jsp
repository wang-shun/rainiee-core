<%@page import="com.dimeng.p2p.user.servlets.credit.ApplyBid" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.BidZf" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.Apply" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME); %>_借款管理_借款申请查询</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "JKSQCX";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="user_til border_b pb10"><i class="icon"></i><span class="gray3 f18">借款申请查询</span></div>
                <div class="user_table">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                        	<td align="center">序号</td>
                            <td align="center">借款标题</td>
                            <td align="center">借款金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">借款期限</td>
                            <td align="center">状态</td>
                            <td align="center">操作</td>
                        </tr>
                        <tbody id="dataBody">

                        </tbody>

                    </table>
                    <div id="pageContent">

                    </div>
                </div>
            </div>

        </div>
        <div class="clear"></div>
    </div>
</div>
<div id="dialog" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:closeDiv('dialog');" class="out"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt" id="tip_div"></div>
                <div class="tips info">
                    <span class="f20 gray33" id="con_error">该标已流标</span>
                </div>
            </div>
            <div class="clear"></div>
            <div class="tc mt20">
                <input type="button" onclick="closeDiv('dialog')" class="btn01 close" value="确 认"/>
            </div>
        </div>
    </div>
</div>

<div id="confirm" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog ">
        <div class="title"><a href="javascript:closeDiv('confirm');" class="out"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="confirm_info tips">

                </div>
            </div>
            <div class="clear"></div>
            <div class="tc mt20">
                <input type="button" onclick="delBid()" class="btn01 close" value="确 认"/>
                <input type="button" onclick="closeDiv('confirm')" class="btn01 btn_gray ml20 close" value="取 消"/>
            </div>
        </div>
    </div>
</div>
<%
    String ermsg = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(ermsg)) {
%>
<div id="confirm2">
    <div class="popup_bg"></div>
    <div class="dialog ">
        <div class="title"><a href="javascript:void(-1);" class="out" onclick="closeDiv('confirm2');"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="successful"></div>
                <div class="tips">
                    <p class="f20 gray33">
                        <%
                            StringHelper.filterHTML(out, ermsg);
                        %>
                    </p>
                </div>
            </div>
            <div class="tc mt20">
                <a class="btn01"
                   href="javascript:void(0);" onclick="closeDiv('confirm2');">确定</a>
            </div>
        </div>
    </div>
</div>
<%
    }
%>
<a href="javascript:void(0);"  style="display: none;" target='_blank' id='openUrl'></a>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/credit/apply.js"></script>
<script type="text/javascript">
    var currentPage = 1;
    var pageSize = 10;
    var url = "<%=controller.getURI(request, Apply.class)%>";
    var pageCount = 0;
    var bdxqUrl = "<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ);%>";
    var viewSuffix = "<%=rewriter.getViewSuffix()%>";
    var bidzfUrl = "<%=controller.getURI(request, BidZf.class) %>";
    function closeDiv(id) {
        $("#" + id).hide();
        initData();
    }

    function view(id, url) {
        $.post("<%=controller.getURI(request, ApplyBid.class)%>", {id: id}, function (data) {
            if (data != null && data != "") {
                $("#dialog").show();
            } else {
            	$("#openUrl").attr("href",url);
				document.getElementById("openUrl").click();
            }
        });
    }
    
    function toAjaxPage(){
    	initData();
    }
</script>
</body>
</html>