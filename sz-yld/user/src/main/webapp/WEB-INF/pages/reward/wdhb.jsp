<%@page import="com.dimeng.p2p.S63.enums.T6342_F04" %>
<%@page import="com.dimeng.p2p.user.servlets.reward.Wdhb" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的红包-<%configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDJL";
    CURRENT_SUB_CATEGORY = "WDHB";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form id="wdtyjFormId" action="<%=controller.getURI(request, Wdhb.class)%>" method="post">
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <div class="user_mod">
                    <div class="user_til"><i class="icon"></i><span class="gray3 f18">我的红包统计</span></div>
                    <div class="amount_list clearfix mt30 mb10">
                        <ul>
                            <li>
                                	未使用红包(元)
                                <p id="unUserHbAmount" class="f22 orange"></p>
                            </li>
                            <li>
                                	已使用红包(元)
                                <p id="useredHbAmount" class="f22 orange"></p>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="user_mod border_t15">
                    <div class="user_til clearfix">
                        <div class="fr"> 查询合计&nbsp;
                            <span class="highlight f18" id="hbTotalAmonut"></span>元
                        </div>
                        <i class="icon"></i><span class="gray3 f18">我的红包详情</span>
                    </div>
                    <div class="user_screening clearfix">
                        <ul>
                            <li style="padding-left: 20px;">状态：
                                <select name="status" class="select6" id="statusSltId">
                                    <option value="">--全部--</option>
                                    <option value="WSY" <%if ("WSY".equals(request.getParameter("status"))) { %>
                                            selected="selected"<% }%> >未使用
                                    </option>
                                    <option value="YSY" <%if ("YSY".equals(request.getParameter("status"))) { %>
                                            selected="selected"<% }%> >已使用
                                    </option>
                                    <option value="YGQ" <%if ("YGQ".equals(request.getParameter("status"))) { %>
                                            selected="selected"<% }%> >已过期
                                    </option>
                                </select>
                            </li>
                        </ul>
                    </div>
                    <div class="user_table">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">红包金额(元)</td>
                                <td align="center">赠送时间</td>
                                <td align="center">过期时间</td>
                                <td align="center">红包状态</td>
                                <td align="center">来源</td>
                                <td align="center">使用规则</td>
                                <td align="center">使用时间</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                    </div>
                    <div class="user_mod_gray lh24 p30 mt30">
                        <span class="highlight">红包说明:</span><br/>
                        1、红包可以通过平台不定期举办的各种活动来获取，获取的红包可用于投资项目。<br/>
                        2、每次投资只能使用一个红包，红包不可叠加、不可拆分使用。<br/>
                        3、红包不可单独提现，不可转让，不可购买债权，不可单独使用。<br/>
                        4、在投资项目时，红包可跟随投资项目追加使用，投资项目结束后，可随本金一起提现。<br/>
                        5、红包不可以投资新手标、奖励标。<br/>
                        6、红包投资有使用规则限制，必须满足使用规则，才能投资成功。<br/>
                        7、红包具有使用有效期，必须在有效期内使用，过期失效。<br/>
                        8、红包最终解释权归平台所有。<br/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/reward/reward.js"></script>
<script type="text/javascript">

    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 1;
    var ajaxUrl = '<%=controller.getURI(request, com.dimeng.p2p.user.servlets.reward.Wdhb.class) %>';
    function ajaxSubmit(status) {
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize, "status": status};
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            async: false,
            success: function (returnData) {

            	//判断用户是否已经
    			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
            		return;
    			}
            	
                $("#unUserHbAmount").html(fmoney(returnData.unUserHbAmount, 2));
                $("#useredHbAmount").html(fmoney(returnData.useredHbAmount, 2));

                //移除table中的tr
                $("#dataTable tr").empty();
                //填充数据,li样式需要改变
                var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>红包金额(元)</td>" +
                        "<td align='center'>赠送时间</td><td align='center'>过期时间</td>" +
                        "<td align='center'>红包状态</td><td align='center'>来源</td>" +
                        "<td align='center'>使用规则</td><td align='center'>使用时间</td></tr>";
                $("#dataTable").append(trHTML);//在table最后面添加一行
                trHTML = "";
                //分页信息
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, status);
                });
                var amount = 0;
                if (null == returnData || null == returnData.retList || returnData.retList.length == 0) {
                    $("#pageContent").html("");
                    $("#dataTable").append("<tr><td colspan='8' align='center'>暂无数据</td></tr>");
                }
                if (returnData.retList != null && returnData.retList.length > 0) {
                    var rAssests = returnData.retList;

                    $.each(rAssests, function (n, value) {
                    	var sourceDesc= getSourceDesc(value.type, value.quota);
                    	if(sourceDesc.length>5){
                    		sourceDesc=sourceDesc.substring(0,5)+"...";
                    	}
                        trHTML += "<tr>" +
                                "<td align='center'>" + (n + 1) + "</td>" +
                                "<td align='center'>" + fmoney(value.value, 2) + "</td>" +
                                "<td align='center'>" + timeStamp2String(value.F07) + "</td>" +
                                "<td align='center'>" + timeStamp2String(value.F08) + "</td>" +
                                "<td align='center'>" + getStatusDesc(value.F04) + "</td>" +
                                "<td align='center' title='"+getSourceDesc(value.type, value.quota)+"'>" + sourceDesc + "</td>" +
                                "<td align='center'>" + getUseRuleDesc(value.useRule, value.investUseRule) + "</td>" +
                                "<td align='center'>" + timeStamp2String(value.F05) + "</td>" +
                                "</tr>";
                        $("#dataTable").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                        amount += parseFloat(value.value);
                    });
                }
                $("#hbTotalAmonut").html(fmoney(returnData.totalAmount, 2));
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
            }
        });
    }
    
    function toAjaxPage(){
    	ajaxSubmit($("#statusSltId").val());
    }
</script>
</body>
</html>