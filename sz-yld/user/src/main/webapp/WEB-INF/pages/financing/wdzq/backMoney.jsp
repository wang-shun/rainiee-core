<%@page import="com.dimeng.p2p.common.enums.QueryType" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.WdzqManage" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.BackOff" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.BackOffList" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.BackMoney" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的债权-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "WDZQ";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <!--左菜单-->
        <!--右边内容-->
        <div class="r_main">
            <div class="hzcx_total clearfix">
                <div class="total fl">
                    全部待收本息(元)
                    <br/>
                    <%
                        WdzqManage backOffManage = serviceSession.getService(WdzqManage.class);
                        BackOff totle = backOffManage.searchTotle();
                    %>
                    <span class="orange f22"><%if (totle == null) { %>0<%} else {%><%=Formater.formatAmount(totle.dsbx) %><%}%></span>
                </div>
                <div class="line"></div>
                <div class="list">
                    <ul>
                        <li>未来一个月(元)<br/><%if (totle == null) { %>
                            0<%} else {%><%=Formater.formatAmount(totle.wlygy) %><%}%></li>
                        <li>未来三个月(元)<br/><%if (totle == null) { %>
                            0<%} else {%><%=Formater.formatAmount(totle.wlsgy) %><%}%></li>
                        <li>未来一年(元)<br/><%if (totle == null) { %>
                            0<%} else {%><%=Formater.formatAmount(totle.wlyn) %><%}%></li>
                    </ul>
                </div>
            </div>
            <div class="user_mod border_t15">
                <div class="user_screening clearfix">
                    <%
                        BigDecimal bMoney = new BigDecimal(0);
                        PagingResult<BackOffList> boList = (PagingResult<BackOffList>) request.getAttribute("boList");
                        QueryType queryType1 = EnumParser.parse(QueryType.class, request.getParameter("queryType"));
                        if (queryType1 == null) {
                            queryType1 = QueryType.DS;
                        }
       	     	
       	     	/* if(queryType1 == QueryType.DS){
       	     		bMoney = totle.dsbx;
       	     	}else{
       	     		bMoney = backOffManage.searchYsTotle();
       	     	} */

                        //bMoney = backOffManage.searchList(request.getParameter("queryType"),request.getParameter("timeStart"),request.getParameter("timeEnd"));
                        bMoney = backOffManage.searchList(request.getParameter("queryType"), request.getParameter("timeStart"), request.getParameter("timeEnd"));
                    %>
                    <ul>
                        <li>
                            回款状态：
                            <select name="queryType" id="queryType" class="select7">
                                <%
                                    for (QueryType queryType : QueryType.values()) {
                                %>
                                <option value="<%=queryType.name()%>"
                                        <%if (queryType == queryType1) {%>selected="selected"
                                        <%}%>><%=queryType.getName()%>
                                </option>
                                <%}%>
                            </select>
                        </li>
                        <li>收款日期：<input type="text" class="text_style text_style_date" id="datepicker1"
                                        name="timeStart"/>至<input type="text" class="text_style text_style_date ml10"
                                                                  id="datepicker2" name="timeEnd"/></li>
                        <li><a href="javascript:void(0);" class="btn04" onclick="hzQuery();">查询</a></li>
                        <li>
                        	<a href="javascript:history.go(-1);" class="btn04 ml10" >返回</a>
                        </li>
                    </ul>
                </div>
                <div class="user_table">
                    <div class="tr mb5">查询总额
                        <%
                            for (QueryType queryType : QueryType.values()) {
                                if (queryType.name().equals(request.getParameter("queryType"))) {%>
                        <%=queryType.getName()%>
                        <%
                                }
                            }
                        %>
                        <span class="f18 highlight" id="money"><%=Formater.formatAmount(bMoney) %></span> 元
                    </div>
                    <table id="backMoneyTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                        </tbody>
                    </table>
                    <!--分页-->
                    <div class="page" id="pageContent">
                        <!--分页  --END-->
                    </div>
                </div>
            </div>
            <!--右边内容-->
        </div>
    </div>
    <!--主体内容-->
    <%@include file="/WEB-INF/include/footer.jsp" %>
    <%@include file="/WEB-INF/include/datepicker.jsp" %>
    <script language="javascript" src="<%=controller.getStaticPath(request)%>/js/ajaxDateUtil.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#datepicker1").datepicker({
                inline: true,
                onSelect: function (selectedDate) {
                    $("#datepicker2").datepicker("option", "minDate", selectedDate);
                }
            });
            $("#datepicker2").datepicker({inline: true});
            $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
            $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
            $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("timeStart"));%>");
            $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("timeEnd"));%>");
            $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
            
            $('.select7').selectlist({
        		zIndex: 20,
        		width: 95,
        		optionHeight: 28,
        		height: 28
        	});
            hzQuery();
        });

        //执行ajax查询数据,填充数据
        var currentPage = 1;
        var pageSize = 10;
        var pageCount = 1;
        //1.回账查询
        function hzQuery() {
            //发送请求,生成数据,再将数据填充
//			var ajaxUrl = $("#yjqdzqUrl").val();
            var ajaxUrl = "<%=controller.getURI(request, BackMoney.class) %>";
            //查询参数
            var queryType = $("input[name='queryType']").val();
            var timeStart = $("#datepicker1").val();
            var timeEnd = $("#datepicker2").val();

            var dataParam = {
                "currentPage": currentPage,
                "pageSize": pageSize,
                "timeStart": timeStart,
                "timeEnd": timeEnd,
                "queryType": queryType
            };
            $.ajax({
                type: "post",
                dataType: "json",
                url: ajaxUrl,
                data: dataParam,
                success: function (returnData) {
                    $("#pageContent").html(returnData.pageStr);
                    $("#money").html(fmoney(returnData.totalMoney));
                    pageCount = returnData.pageCount;
                    //绑定分页点击事件
                    $("a.page-link").click(function () {
                        pageParam(this);
                    });
                    //移除table中的tr
                    $("#backMoneyTable tr").empty();
                    //填充数据
                    var trHTML = " <tr class='til'><td align='center'>序号</td><td align='left'>债权ID</td><td align='center'>借款人</td>" +
                            "<td align='center'>类型明细</td><td align='center'>金额(元)</td>" +
                            "<td align='center'>收款日期</td><td align='center'>回款状态</td></tr>";
                    $("#backMoneyTable tbody").append(trHTML);//在table最后面添加一行标题
                    trHTML = "";

                    if (null == returnData.hzcxList) {
                        trHTML += "<tr><td colspan='7' align='center'>暂无数据</td>"
                        "</tr>";
                        $("#backMoneyTable").append(trHTML);//在table最后面添加一行
                        return;
                    } else if (returnData.hzcxList.length > 0) {
                        var resultList = returnData.hzcxList;
                        var i = 0;
                        $.each(resultList, function (n, value) {
                            var idTypeHtml = "<i class='item_icon xin_icon'>信</i>";
                            if (value.F11 == "S") {//T6230_F11.S
                                idTypeHtml = "<i class='item_icon dan_icon'>保</i>"
                            } else if (value.F13 == "S") {//T6230_F13.S
                                idTypeHtml = "<i class='item_icon di_icon'>抵</i>"
                            } else if (value.F14 == "S") {//T6230_F14.S
                                idTypeHtml = "<i class='item_icon shi_icon'>实</i>"
                            }
                            var status = value.dsStatus;
                            if (null != status && status == 'YH') {//T6252_F09.YH
                                status = "已收";
                            } else if (null != status && status == 'WH') {//T6252_F09.WH
                                status = "待收";
                            }
							i++;
                            trHTML += "<tr><td align='center'>" + i + "</td><td align='left'>" + idTypeHtml + value.assestsId + "</td>" +
                                    "<td align='center'>" + value.creditor + "</td>" +
                                    "<td align='center'>" + value.backType + "</td>" +
                                    "<td align='center'>" + fmoney(value.money, 2) + "</td>" +
                                    "<td align='center'>" + getTime(value.receiveDate) + "</td>" +
                                    "<td align='center'>" + status + "</td>" +
                                    "</tr>";
                            $("#backMoneyTable").append(trHTML);//在table最后面添加一行
                            trHTML = "";
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    //console.log(textStatus);
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
        function pageParam(obj) {
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
            //查询
            hzQuery();
        }
         
         function toAjaxPage(){
        	 hzQuery();
         }

    </script>
</body>
</html>