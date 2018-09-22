<%@page import="com.dimeng.p2p.S63.enums.T6342_F04" %>
<%@page import="com.dimeng.p2p.user.servlets.reward.Wdjxq" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的加息券-<%
        configureProvider.format(out, SystemVariable.SITE_NAME);
    %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDJL";
    CURRENT_SUB_CATEGORY = "WDJXQ";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form id="wdtyjFormId" action="<%=controller.getURI(request, Wdjxq.class)%>" method="post">
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <div class="user_mod">
                    <div class="user_til"><i class="icon"></i><span class="gray3 f18">我的加息券</span></div>
                    <div class="amount_list clearfix mt30 mb10">
                        <ul>
                            <li>待收加息奖励(元)
                                <p class="f22 orange" id="dsJxqAmount">0.00</p>
                            </li>
                            <li>已收加息奖励(元)
                                <p class="f22 orange" id="ysJxqAmount">0.00</p>
                            </li>
                            <li>
                               	 未使用加息券(张)
                                <p class="f22 orange" id="unUserJxqCount">0</p>
                            </li>
                            <li>
                               	 已使用加息券(张)
                                <p class="f22 orange" id="useredJxqCount">0</p>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="user_mod border_t15">
                    <div class="user_til"><i class="icon"></i><span class="gray3 f18">我的加息券统计详情</span></div>
                    <div class="user_screening clearfix">
                        <ul>
                            <li>状态：
                                <select name="status" class="select6" id="statusSltId">
                                    <option value="">--全部--</option>
                                    <%--<%for(T6342_F04 t6342_F04:T6342_F04.values()){ %>
                                      <option value="<%=t6342_F04.name()%>"  <%if(t6342_F04.name().equals(request.getParameter("status"))){ %>  selected="selected"<% }%>><%=t6342_F04.getChineseName() %></option>
                                    <%} %>--%>
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
                                <td align="center">加息利率(%)</td>
                                <td align="center">赠送时间</td>
                                <td align="center">过期时间</td>
                                <td align="center">状态</td>
                                <td align="center">来源</td>
                                <td align="center">使用规则</td>
                                <td align="center">使用时间</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                    </div>
                    <div class="user_mod_gray lh24 p30 mt30">
                        <span class="highlight">加息券说明:</span><br/>
                        1、加息券可以通过平台不定期举办的各种活动来获取，获取的加息券可用于投资项目。<br/>
                        2、每次投资只能使用一张加息券，加息券不可叠加、不可拆分使用。<br/>
                        3、加息券不可转让，不可购买债权，不可单独使用，只能跟随投资使用。<br/>
                        4、加息奖励可提现，加息奖励由平台支付，支付时间与跟随投资项目的正常还款时间一致。<br/>
                        5、当债权发生变更时（如项目提前还款、逾期垫付、债权转让），即债权不再属于投资者时，投资者不再获得加息奖励。<br/>
                        6、加息券不可以投资新手标、奖励标。<br/>
                        7、加息券投资有使用规则限制，必须满足使用规则，才能投资成功。<br/>
                        8、加息券具有使用有效期，必须在有效期内使用，过期失效。<br/>
                        9、加息券最终解释权归平台所有。<br/>
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
    var ajaxUrl = '<%=controller.getURI(request, com.dimeng.p2p.user.servlets.reward.Wdjxq.class) %>';
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
    			}
            	
                $("#dsJxqAmount").html(fmoney(returnData.jxqAmount.dsJxjlAmount));
                $("#ysJxqAmount").html(fmoney(returnData.jxqAmount.ysJxjlAmount));
                $("#unUserJxqCount").html(returnData.unUserJxqCount);
                $("#useredJxqCount").html(returnData.useredJxqCount);

                //移除table中的tr
                $("#dataTable tr").empty();
                //填充数据,li样式需要改变
                var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>加息利率(%)</td>" +
                        "<td align='center'>赠送时间</td><td align='center'>过期时间</td><td align='center'>状态</td>" +
                        "<td align='center'>来源</td><td align='center'>使用规则</td><td align='center'>使用时间</td></tr>";
                $("#dataTable").append(trHTML);//在table最后面添加一行
                trHTML = "";
                //分页信息
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, status);
                });
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
                                "<td align='center' title='"+getSourceDesc(value.type, value.quota)+"'>" +sourceDesc+ "</td>" +
                                "<td align='center'>" + getUseRuleDesc(value.useRule, value.investUseRule) + "</td>" +
                                "<td align='center'>" + timeStamp2String(value.F05) + "</td>" +
                                "</tr>";
                        $("#dataTable").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                    });
                }
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
    
    document.onkeydown = function () {   	
    	if (window.event && window.event.keyCode == 13) {
    	var _obj=$("input.btn_ok.page-link.cur").selector;
    	pageSubmit(_obj);
    	}   
    };
    	
    function onlyNum() 
    { 
    	if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39)) 
    	if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105))) 
    	event.returnValue=false; 
    }
</script>
</body>
</html>