<%@page import="com.dimeng.p2p.user.servlets.capital.Charge" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.TradingRecord" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.Unpay" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.UnpayXxcz" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<% configureProvider.format(out, SystemVariable.SITE_NAME); %>_未完成充值记录</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZJGL";
    CURRENT_SUB_CATEGORY = "CZ";
    String unpayType = request.getParameter("type") == null ? "unpay" : request.getParameter("type");
%>
<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <!--左菜单-->
        <!--右边内容-->
        <div class="r_main">
            <div class="user_mod">
                <div class="user_tab clearfix" id="infoHd">
                    <ul>
                        <li id="unpay" onclick="initInfoData('unpay')" class="hover">在线充值<i></i></li>
                        <li id="unpayXxcz" onclick="initInfoData('unpayXxcz')">线下充值<i></i></li>
                    </ul>
                    <div class="fr">
                        <a href="javascript:void(0);" class="btn04 ml10" onclick="retToBack()"> 返回</a>
                    </div>
                </div>
                <div id="unpayDiv" class="mt30">
                    <div class="user_table pt5">
                        <table id="unpayTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">订单号</td>
                                <td align="center">金额(元)</td>
                                <td align="center">时间</td>
                                <td align="center">状态</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent">
                        </div>
                    </div>
                </div>
                <div id="unpayXxczDiv" class="mt30" style="display:none;">
                    <div class="user_table pt5">
                        <table id="unpayXxczTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">订单号</td>
                                <td align="center">金额(元)</td>
                                <td align="center">时间</td>
                                <td align="center">状态</td>
                            </tr>
                        </table>
                        <form action="<%=controller.getViewURI(request, UnpayXxcz.class)%>" method="post" id="form2">
                            <div class="page" id="xxczPageContent">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--右边内容-->
    </div>
</div>


<div id="dialog" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <span class="f20 gray3" id="con_error"></span>
                </div>
            </div>
            <div class="tc mt20"><a href="javascript:void(0);" class="btn01" onclick="closeDiv()">确定</a></div>
        </div>
    </div>
</div>
<div id="problem" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog w510 thickpos" style="margin:-120px 0 0 -255px;">
        <div class="dialog_close fr"><a href="javascript:void(0)"></a></div>
        <div class="con clearfix">
            <div class="d_perfect fl"></div>
            <div class="info fr">
                <p class="f20 gray33">请您在新打开的页上完成付款。</p>
                <p>付款完成前请不要关闭此窗口。<br/>完成付款后请根据您的情况点击下面的按钮：</p>
                <p><a href="<%=controller.getViewURI(request, TradingRecord.class)%>" class="btn06 tc f14 fl mr10 mt5">已完成付款</a>
                    <a href="<%=controller.getViewURI(request, Charge.class)%>#001"
                       class="btn06 tc f14 fl mt5">付款遇到问题</a></p>
                <div class="clear"></div>
                <%--
                <p class="mt5"><a href="javascript:void(0);" id="otherPay" class="blue">返回选择其他支付方式</a></p>
                 --%>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<form action="<%=configureProvider.format(URLVariable.PAY_CHECK)%>" method="post" id="orderform" target="_blank">
    <input type="hidden" name="o" id="o" value=""/>
</form>
<!--主体内容-->

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    var unpayType = '<%=unpayType%>';
    $(function () {
        $("#problem div.dialog_close").click(function () {
            $("#problem").hide();
        });
        $("#problem a.btn06").click(function () {
            $("#problem").hide();
        });
        initInfoData(unpayType);
    });


    function toCharge() {
        $("#problem").show();
    }

    function dzOrder(id) {
        <%--
        $("#o").val(id);
        var form = document.getElementById("orderform");
        form.submit();
        --%>
        var url = "<%=configureProvider.format(URLVariable.PAY_CHECK)%>";
        var data = $("#o").val(id);
        $.ajax({
            type: "post",
            dataType: "html",
            url: url,
            data: data,
            success: function (returnData) {
                initInfoData('unpay');
                $("#con_error").html(returnData);
                $("#dialog").show();
                return;
            }
        });
    }

    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 1;
    var _tabI = 1;
    function initInfoData(infoType) {
        currentPage = 1;
        $("#infoHd li").removeClass("hover");
        $("#" + infoType).addClass("hover");
        $("#unpayXxczTable tr").empty();
        $("#unpayTable tr").empty();
        $("#unpayXxczDiv")[0].style.display = 'none';
        $("#unpayDiv")[0].style.display = 'none';
        if (infoType == 'unpayXxcz') {
            _tabI = 2;
            unpayXxczListPaging();
        } else {
            _tabI = 1;
            unpayListPaging();
        }
    }

    function unpayListPaging() {
        $("#xxczPageContent").html("");
        $("#unpayDiv")[0].style.display = 'block';
        var ajaxUrl = "<%=controller.getURI(request, Unpay.class)%>";
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
            	
            	if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
            		return;
    			}
            	
                $("#unpayTable tr").empty();
                var trHTML = "<tr class=\"til\"><td align=\"center\">序号</td><td align=\"center\">订单号</td><td align=\"center\">金额(元)</td><td align=\"center\">时间</td><td align=\"center\">状态</td><td align=\"center\">操作</td></tr>";
                $("#unpayTable").append(trHTML);
                trHTML = "";

                //分页信息
                if (returnData.orderList != null) {
                    $("#pageContent").html(returnData.pageStr);
                }
                pageCount = returnData.pageCount;

                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 1);
                });
                if (null == returnData.orderList) {
                    $("#unpayTable").append("<tr><td colspan='6' align=\"center\">暂无数据</td></tr>");
                } else if (returnData.orderList.length > 0) {
                    var rAssests = returnData.orderList;
                    var i = 1;
                    $.each(rAssests, function (n, value) {
                        trHTML += "<tr><td align=\"center\">" + i + "</td>" +
                                "<td align=\"center\">" + value.id + "</td>" +
                                "<td align=\"center\">" + value.amountStr + "</td>" +
                                "<td align=\"center\">" + value.orderTimeStr + "</td>" +
                                "<td align=\"center\">" + value.statusName + "</td>" +
                                "<td align=\"center\"><a href=\"javascript:void(0)\" class=\"blue ml5\" onclick=\"dzOrder('" + value.id + "');\">去对账</a></td></tr>";
                        $("#unpayTable").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                        i++;
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


    /**
     *未完成线下充值订单信息
     */
    function unpayXxczListPaging() {
        $("#unpayXxczDiv")[0].style.display = 'block';
        $("#pageContent").html("");
        var ajaxUrl = "<%=controller.getURI(request, UnpayXxcz.class)%>";
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
            	
            	if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
            		return;
    			}
            	
                $("#unpayXxczTable tr").empty();
                var trHTML = "<tr class=\"til\"><td align=\"center\">序号</td><td align=\"center\">订单号</td><td align=\"center\">金额(元)</td><td align=\"center\">时间</td><td align=\"center\">状态</td><td align=\"center\">备注</td></tr>";
                $("#unpayXxczTable").append(trHTML);
                trHTML = "";

                //分页信息
                if (returnData.orderList != null) {
                    $("#xxczPageContent").html(returnData.pageStr);
                }
                pageCount = returnData.pageCount;

                //分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 2);
                });
                if (null == returnData.orderList) {
                    $("#unpayXxczTable").append("<tr><td colspan='6' align=\"center\">暂无数据</td></tr>");
                } else if (returnData.orderList.length > 0) {
                    var rAssests = returnData.orderList;
                    var i = 1;
                    $.each(rAssests, function (n, value) {
                        trHTML += "<tr><td align=\"center\">" + i + "</td>" +
                                "<td align=\"center\">" + value.id + "</td>" +
                                "<td align=\"center\">" + value.amountStr + "</td>" +
                                "<td align=\"center\">" + value.orderTimeStr + "</td>" +
                                "<td align=\"center\">" + value.statusName + "</td>" +
                                "<td align=\"center\" title=\"" + value.remark + "\">" + subStringLength(value.remark, 8, '...') + "</td>";
                        $("#unpayXxczTable").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                        i++;
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

    /**
     *分页查询请求
     * @param obj
     * @param liId
     * @returns {boolean}
     */
    function pageParam(obj, liId) {
        if ($(obj).hasClass("on")) {
            return false;
        }
        $(obj).addClass("on");
        $(obj).siblings("a").removeClass("on");
        if ($(obj).hasClass("startPage")) {
            currentPage = 1;
        } else if ($(obj).hasClass("prev")) {
            currentPage = parseInt(currentPage) - 1;
        } else if ($(obj).hasClass("next")) {
            currentPage = parseInt(currentPage) + 1;
        } else if ($(obj).hasClass("endPage")) {
            currentPage = pageCount;
        } else {
            currentPage = parseInt($(obj).html());
        }

        if (liId == 1 || $("#unpay").hasClass("hover")) {
            unpayListPaging();
        } else if (liId == 2 || $("#unpayXxcz").hasClass("hover")) {
            unpayXxczListPaging();
        }


    }

    function closeDiv() {
        $("#dialog").hide();
    }

    function isEmpty(str) {
        if (str == null || str == "") {
            return true;
        } else {
            return false;
        }
    }
    function subStringLength(str, maxLength, replace) {
        if (isEmpty(str)) {
            return;
        }
        if (typeof(replace) == "undefined" || isEmpty(replace)) {
            replace = "...";
        }
        var rtnStr = "";
        var index = 0;
        var end = Math.min(str.length, maxLength);
        for (; index < end; ++index) {
            rtnStr = rtnStr + str.charAt(index);
        }
        if (str.length > maxLength) {
            rtnStr = rtnStr + replace;
        }
        return rtnStr;
    }

    function retToBack() {
        location.href = "<%configureProvider.format(out,URLVariable.USER_CHARGE);%>?i=" + _tabI;
    }
    
    function toAjaxPage(){
    	if($("#unpay").hasClass("hover")){
    		unpayListPaging();
    	}else if($("#unpayXxcz").hasClass("hover")){
    		unpayXxczListPaging();
    	}
    }
    
</script>
</body>
</html>