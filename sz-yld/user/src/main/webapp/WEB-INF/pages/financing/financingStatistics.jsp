<%@page import="com.dimeng.p2p.user.servlets.financing.FinancingStatistics" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.FinancingStatisticsDetail" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.FinancingStatisticsSingleDetail" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.ExportFinancingStatisticsDetail" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>
        <%configureProvider.format(out, SystemVariable.SITE_NAME);%>_理财管理_理财统计
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%
        CURRENT_CATEGORY = "LCGL";
        CURRENT_SUB_CATEGORY = "LCTJ";
    %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<input type="hidden" name="judgePage" value=""/>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div id="lctjDivId" class="r_main">
            <div class="user_mod">
                <div class="user_til">
                    <i class="icon"></i><span class="gray3 f18">理财统计</span>
                </div>
                <div class="amount_list clearfix mt30 mb10">
                    <ul>
                        <li>累计收益(元)<span class="hover_tips"><div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">累计收益=已收利息+已收违约金+债权转让盈亏+已收罚息-理财管理费</div>
                        </div></span>

                            <p class="mt10 highlight" style="cursor: pointer;"
                               onclick="financingSingleDetailQuery('ljsy',1);"
                               id="ulFinancingId1"></p>
                        </li>
                        <li>累计投资金额(元)<span class="hover_tips"><div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">累计投资金额=投资金额（放款后的金额）+买入债权金额</div>
                        </div></span>

                            <p class="mt10 highlight" style="cursor: pointer;"
                               onclick="financingSingleDetailQuery('ljtzje',1);"
                               id="ulFinancingId2"></p>
                        </li>
                        <li>已回收金额(元)<span class="hover_tips"><div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">已收回金额=已收本金+已收利息+已收罚息+已收违约金</div>
                        </div></span>

                            <p class="mt10 highlight" style="cursor: pointer;"
                               onclick="financingSingleDetailQuery('yhsje',1);"
                               id="ulFinancingId3"></p>
                        </li>
                        <li>待回收金额(元)<span class="hover_tips"><div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">待收回金额=待收本金+待收利息+待收罚息</div>
                        </div></span>

                            <p class="mt10 highlight" style="cursor: pointer;"
                               onclick="financingSingleDetailQuery('dhsje',1);"
                               id="ulFinancingId4"></p>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="user_mod border_t15">
                <div class="user_screening clearfix">
                    <ul>
                        <li>年：<input type="text" id="sYear" name="sYear"
                                     readonly="readonly" class="text_style text_style_date" value=""
                                     onchange="timeChange(null,'year',this,'s')"/>
                            	月：<input type="text" id="sMonth" name="sMonth" readonly="readonly"
                                     class="text_style text_style_date ml10" value=""
                                     onchange="timeChange(null,'month',this,'s')"/>
                            <span class="ml5">至</span>
                            	年：<input type="text" id="eYear" name="eYear" readonly="readonly"
                                     class="text_style text_style_date ml10" value=""
                                     onchange="timeChange(null,'year',this,'e')"/>
                           	 	月：<input type="text" id="eMonth" name="eMonth" readonly="readonly"
                                     class="text_style text_style_date" value=""
                                     onchange="timeChange(null,'month',this,'e')"/>
                        </li>
                        <li>
                            <a href="javascript:void(0);" class="btn04 ml10" onclick="financingStatisticQuery(1)"> 查询</a>
                        </li>
                    </ul>
                </div>
                <div class="user_table">
                    <div class="tr mb10">
                        <a id="detailUrl" href="javascript:void(0);" class="highlight"
                           onclick="financingDetailQuery(1);">-详情-</a>
                    </div>
                    <table id="lctjTableId" width="100%" border="0" cellspacing="0"
                           cellpadding="0">
                        <tr class="til">
                            <td align="center">序号</td>
                            <td align="center">年</td>
                            <td align="center">月</td>
                            <td align="center">已赚金额(元)</td>
                            <td align="center">投资金额(元)</td>
                            <td align="center">已回收金额(元)</td>
                            <td align="center">待回收金额(元)</td>
                        </tr>
                    </table>
                    <div class="page" id="pageContent"></div>
                </div>
                <div class="user_mod_gray lh24 p30 mt30">
                    <span class="highlight">理财统计说明：</span><br/>
                    	理财统计数据为每月初更新上一个月数据。
                </div>
            </div>
        </div>
        <!--理财详情 start-->
        <div id="lctjDetailDivId" class="r_main" style="display: none;">
            <div class="user_mod">
                <div class="user_til border_b pb10"><i class="icon"></i><span class="gray3 f18">理财统计详情</span></div>
                <form id="form_loan"
                      action="<%=controller.getURI(request, FinancingStatisticsDetail.class)%>"
                      method="post">
                    <input type="hidden" name="searchType" value="0"/>

                    <div class="user_screening clearfix">
                        <ul>
                            <li> 年：<input type="text" id="sDetailYear" name="sYear"
                                          readonly="readonly" class="text_style text_style_date" value=""
                                          onchange="timeChange('detail','year',this,'s')"/>
                                	月： <input type="text" id="sDetailMonth" name="sMonth" readonly="readonly"
                                          class="text_style text_style_date ml10" value=""
                                          onchange="timeChange('detail','month',this,'s')"/>
                                <span class="ml5">至</span>
                               	 年：<input type="text" id="eDetailYear" name="eYear"
                                         readonly="readonly" class="text_style text_style_date ml10" value=""
                                         onchange="timeChange('detail','year',this,'e')"/>
                              	  月：<input type="text" id="eDetailMonth" name="eMonth" readonly="readonly"
                                         class="text_style text_style_date" value=""
                                         onchange="timeChange('detail','month',this,'e')"/>
                            <li>
                                <a href="javascript:void(0);" class="btn04" onclick="financingDetailQuery(2);"> 查询</a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="btn04 ml10" onclick="showExport()"> 导出</a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="btn04 ml10" onclick="backToLxtj()"> 返回</a>
                            </li>
                            <!--  <input name="input" type="button" value="返回" class="btn01 fl mb10 ml10" onclick="backToLxtj()"/> -->
                        </ul>
                    </div>
                    <table id="lctjDetailTableId" width="100%" border="0"
                           cellspacing="0" cellpadding="0" class="user_table tc">
                        <tr class="user_lsbg">
                            <td align="center">序号</td>
                            <td align="center" style="width:30px;">年</td>
                            <td align="center">月</td>
                            <td align="center">投资金额(元)</td>
                            <td align="center">债权<br>转让盈亏(元)</td>
                            <td align="center">已收本金(元)</td>
                            <td align="center">已收利息(元)</td>
                            <td align="center">已收罚息(元)</td>
                            <td align="center">已收违约金(元)</td>
                            <td align="center">待收本金(元)</td>
                            <td align="center">待收利息(元)</td>
                            <td align="center">待收罚息(元)</td>
                        </tr>
                    </table>
                    <div class="page" id="detailPageContent"></div>
                    <div class="user_mod_gray lh24 p30 mt30">
                        <span class="highlight">理财统计说明：</span><br/> 
                        	理财统计数据为每月初更新上一个月数据。
                    </div>
                </form>
            </div>
        </div>
        <!--理财详情 end-->
        <div class="clear"></div>
    </div>
</div>

<div class="dialog dialog_year_month w510 thickpos" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
       <div class="title"><a class="out" id="cancel" href="javascript:void(0);"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips" id="infoDiv">
                    <p class="f20 gray3">请选择年份，再选择月份!</p>
                </div>
            </div>
            <div class="tc mt20">
                <a href="javascript:void(0)" id="ok" class="btn01">确定</a><!--  <a
                    href="javascript:void(0)" id="cancel" class="btn01">关闭</a> -->
            </div>
        </div>
    </div>
</div>

<!--投资金额统计-->
<div id="lctjSingleDetailDivId" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title" id="lctjSingleDetailTitleId"></div>
        <div class="content">
            <div class="table">
                <table id="lctjSingleDetailTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                </table>
            </div>
            <div class="page" id="lctjSingleDetailPageContent"></div>
            <div class="tc mt20"><a href="javascript:void(0)" id="lctjOk" class="btn01">确定</a></div>
        </div>
    </div>
</div>
<!--投资金额统计--END-->
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript"
        src="<%=controller.getStaticPath(request)%>/js/zankai.js"></script>
<script language="javascript"
        src="<%=controller.getStaticPath(request)%>/js/ajaxDateUtil.js"></script>
<script type="text/javascript">

    $(function () {
    	setJudgePage('');
        $("div.dialog_close_year_month").click(function () {
            $("div.dialog_year_month").hide();
        });
        $("#cancel").click(function () {
            $("div.dialog_year_month").hide();
        });
        $("#ok").click(function () {
            $("div.dialog_year_month").hide();
        });
        $("#lctjOk").click(function () {
        	setJudgePage("");
            $("#lctjSingleDetailDivId").hide();
        });
        $("div.dialog_close_tj").click(function () {
            $("div.dialog_tj").hide();
        });
        $("#sYear").click(function () {
            $("div.dialog_year_month").hide();
            $("div.dialog_tj").hide();
            $("div [lang = 'zh-cn']").show();
            WdatePicker({
                dateFmt: 'yyyy',
                isShowToday: false,
                onclearing: function () {
                    $('#sYear').val('');
                    $('#sMonth').val('');
                    return true;
                }
            });

        });
        $("#sMonth").click(function () {
            $("div.dialog_tj").hide();
            var year = $("#sYear").val();
            if (year == null || year == "") {
                $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份!</p>");
                $('#sMonth').val('');
                $("div [lang = 'zh-cn']").hide();
                $("div.dialog_year_month").show();
                $("div.dialog").show();
                $("div.popup_bg").show();
                return;
            } else {
                WdatePicker({dateFmt: 'MM', isShowToday: false});
            }

        });

        $("#eYear").click(function () {
            $("div.dialog_year_month").hide();
            $("div.dialog_tj").hide();
            $("div [lang = 'zh-cn']").show();
            WdatePicker({
                dateFmt: 'yyyy',
                isShowToday: false,
                onclearing: function () {
                    $('#eYear').val('');
                    $('#eMonth').val('');
                    return true;
                }
            });

        });
        $("#eMonth").click(function () {
            $("div.dialog_tj").hide();
            var year = $("#eYear").val();
            if (year == null || year == "") {
                $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份!</p>");
                $('#eMonth').val('');
                $("div [lang = 'zh-cn']").hide();
                $("div.dialog_year_month").show();
                $("div.dialog").show();
                $("div.popup_bg").show();
                return;
            } else {
                WdatePicker({dateFmt: 'MM', isShowToday: false});
            }

        });

        $("#sDetailYear").click(function () {
            $("div.dialog_year_month").hide();
            $("div [lang = 'zh-cn']").show();
            WdatePicker({
                dateFmt: 'yyyy',
                isShowToday: false,
                onclearing: function () {
                    $('#sDetailYear').val('');
                    $('#sDetailMonth').val('');
                    return true;
                }
            });


        });
        $("#sDetailMonth").click(function () {
            var year = $("#sDetailYear").val();
            if (year == null || year == "") {
                $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份!</p>");
                $("div [lang = 'zh-cn']").hide();
                $("div.dialog_year_month").show();
                $("div.dialog").show();
                $("div.popup_bg").show();
                $('#sDetailMonth').val('');
                return;
            } else {
                WdatePicker({dateFmt: 'MM', isShowToday: false});
            }

        });
        $("#eDetailYear").click(function () {
            $("div.dialog_year_month").hide();
            $("div [lang = 'zh-cn']").show();
            WdatePicker({
                dateFmt: 'yyyy',
                isShowToday: false,
                onclearing: function () {
                    $('#eDetailYear').val('');
                    $('#eDetailMonth').val('');
                    return true;
                }
            });


        });
        $("#eDetailMonth").click(function () {
            var year = $("#eDetailYear").val();
            if (year == null || year == "") {
                $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份!</p>");
                $("div [lang = 'zh-cn']").hide();
                $("div.dialog_year_month").show();
                $("div.dialog").show();
                $("div.popup_bg").show();
                $('#eDetailMonth').val('');
                return;
            } else {
                WdatePicker({dateFmt: 'MM', isShowToday: false});
            }


        });

        //启动时进行ajax查询理财统计
        financingStatisticQuery(0);
    });

    function timeChange(type, timeType, obj, indexType) {

        if (type == "detail") {
            var eDetailYear = $("#eDetailYear").val();
            var sDetailYear = $("#sDetailYear").val();
            var sDetailMonth = $("#sDetailMonth").val();
            var eDetailMonth = $("#eDetailMonth").val();
            if (timeType == "year") {
                var msg = "开始年份不能大于结束年份!";
                if (indexType == "e") {
                    msg = "结束年份不能小于开始年份!";
                }
                if (eDetailYear != "" && eDetailYear != null) {
                    if (sDetailYear > eDetailYear) {
                        $("#infoDiv").html("<p class='f20 gray33'>" + msg + "</p>");
                        $(obj).val('');
                        $("div [lang = 'zh-cn']").hide();
                        $("div.dialog_year_month").show();
                        $("div.dialog").show();
                        $("div.popup_bg").show();
                        return;
                    }
                }
            }

            if (timeType == "month") {
                var msg = "开始月份不能大于结束月份!";
                if (indexType == "e") {
                    msg = "结束月份不能小于开始月份!";
                }
                if (eDetailYear == sDetailYear && sDetailMonth != "" && eDetailMonth != "") {

                    if (sDetailMonth > eDetailMonth) {
                        $("#infoDiv").html("<p class='f20 gray33'>" + msg + "</p>");
                        $('#eDetailMonth').val('');
                        $("div [lang = 'zh-cn']").hide();
                        $("div.dialog_year_month").show();
                        $("div.dialog").show();
                        $("div.popup_bg").show();
                        return;
                    }
                }
            }
        } else {
            var eYear = $("#eYear").val();
            var sYear = $("#sYear").val();
            var sMonth = $("#sMonth").val();
            var eMonth = $("#eMonth").val();
            if (timeType == "year") {
                var msg = "开始年份不能大于结束年份!";
                if (indexType == "e") {
                    msg = "结束年份不能小于开始年份!";
                }
                if (eYear != "" && eYear != null) {
                    if (sYear > eYear) {
                        $("#infoDiv").html("<p class='f20 gray33'>" + msg + "</p>");
                        $(obj).val('');
                        $("div [lang = 'zh-cn']").hide();
                        $("div.dialog_year_month").show();
                        $("div.dialog").show();
                        $("div.popup_bg").show();
                        return;
                    }
                }
            }
            if (timeType == "month") {
                var msg = "开始月份不能大于结束月份!";
                if (indexType == "e") {
                    msg = "结束月份不能小于开始月份!";
                }
                if (eYear == sYear && sMonth != "" && eMonth != "") {
                    if (sMonth > eMonth) {
                        $("#infoDiv").html("<p class='f20 gray33'>" + msg + "</p>");
                        $(obj).val('');
                        $("div [lang = 'zh-cn']").hide();
                        $("div.dialog_year_month").show();
                        $("div.dialog").show();
                        $("div.popup_bg").show();
                        return;
                    }
                }
            }
        }
    }

    var currentPage = 1;
    var pageSize = 10;
    var pageCount = 1;

    /**
     *查询理财统计信息
     */
    function financingStatisticQuery(searchType) {
        $("#lctjSingleDetailDivId").hide();
        $("#sDetailYear").val("");
        $("#sDetailMonth").val("");
        $("#eDetailYear").val("");
        $("#eDetailMonth").val("");
        //发送请求,生成数据,再将数据填充
        var ajaxUrl = "<%=controller.getURI(request, FinancingStatistics.class) %>";
        //查询参数
        var sYear = $("#sYear").val();
        var sMonth = $("#sMonth").val();
        var eYear = $("#eYear").val();
        var eMonth = $("#eMonth").val();
        var dataParam = {
            "paging.current": currentPage,
            "pageSize": pageSize,
            "sYear": sYear,
            "sMonth": sMonth,
            "eYear": eYear,
            "eMonth": eMonth,
            "searchType": searchType
        };
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
            	
                //移除table中的tr
                $("#lctjTableId tr").empty();
                //填充数据
                var trHTML = " <tr class='til'> " +
                        "<td align='center'>序号</td> " +
                        "<td align='center'>年</td> " +
                        "<td align='center'>月</td> " +
                        "<td align='center'>已赚金额(元)</td> " +
                        "<td align='center'>投资金额(元)</td>" +
                        "<td align='center'>已回收金额(元)</td>" +
                        "<td align='center'>待回收金额(元)</td>" +
                        "</tr>";
                $("#lctjTableId tbody").append(trHTML);//在table最后面添加一行标题

                //给合计设置值
                if (null != returnData.earnFinancingTotal) {
                    var result = returnData.earnFinancingTotal;
                    $("#ulFinancingId1").text(fmoney(result.ljsy, 2));
                    $("#ulFinancingId2").text(fmoney(result.ljtzje, 2));
                    $("#ulFinancingId3").text(fmoney(result.yhsje, 2));
                    $("#ulFinancingId4").text(fmoney(result.dhsje, 2));
                }

                //查询条件设置值
                var sYear = "";
                var sMonth = "";
                var eYear = "";
                var eMonth = "";
                if (null != returnData.sYear) {
                    sYear = returnData.sYear;
                    $("#sYear").val(sYear);
                }

                if (null != returnData.sMonth) {
                    sMonth = returnData.sMonth;
                    $("#sMonth").val(sMonth);
                }
                if (null != returnData.eYear) {
                    eYear = returnData.eYear;
                    $("#eYear").val(eYear);
                }

                if (null != returnData.eMonth) {
                    eMonth = returnData.eMonth;
                    $("#eMonth").val(eMonth);
                }
                if (null == returnData || null == returnData.lctjList || returnData.lctjList.length == 0) {
                    $("#pageContent").html("");
                    $("#lctjTableId tbody").append("<tr><td colspan='7' align='center'>暂无数据</td></tr>");//在table最后面添加一行标题
                    return;
                }
                $("#pageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //绑定分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 1);
                });

                trHTML = "";

                if (returnData.lctjList.length > 0) {
                    var resultList = returnData.lctjList;
                    $.each(resultList, function (n, value) {
                        trHTML += "<tr>" +
                                "<td align='center'>" + (n + 1) + "</td>" +
                                "<td align='center'>" + value.year + "</td>" +
                                "<td align='center'>" + value.month + "</td>" +
                                "<td align='center'>" + fmoney(value.ljsy, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.ljtzje, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.yhsje, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.dhsje, 2) + "</td>" +
                                "</tr>";
                        $("#lctjTableId").append(trHTML);//在table最后面添加一行
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

    /**
     *理财详情
     * luoyi
     * 2015-05-09
     */
    function financingDetailQuery(type) {
        $("#lctjSingleDetailDivId").hide();
        if (type == 1) {//1表示从"详情"跳转.需要将当前分页信息设置为1
            $("div.dialog_year_month").hide();
            currentPage = 1;
        }
        $("#lctjDivId").hide();
        $("#lctjDetailDivId").show();
        //发送请求,生成数据,再将数据填充
        var ajaxUrl = "<%=controller.getURI(request, FinancingStatisticsDetail.class) %>";
        //查询参数
        var sDetailYear = $("#sDetailYear").val();
        var sDetailMonth = $("#sDetailMonth").val();
        var eDetailYear = $("#eDetailYear").val();
        var eDetailMonth = $("#eDetailMonth").val();
        if (type == 1) {
            if (sDetailYear == null || sDetailYear == "") {
                sDetailYear = $("#sYear").val();
            }
            if (sDetailMonth == null || sDetailMonth == "") {
                sDetailMonth = $("#sMonth").val();
            }
            if (eDetailYear == null || eDetailYear == "") {
                eDetailYear = $("#eYear").val();
            }
            if (eDetailMonth == null || eDetailMonth == "") {
                eDetailMonth = $("#eMonth").val();
            }
        }
        var dataParam = {
            "paging.current": currentPage,
            "pageSize": pageSize,
            "sYear": sDetailYear,
            "sMonth": sDetailMonth,
            "eYear": eDetailYear,
            "eMonth": eDetailMonth
        };
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
                //移除table中的tr
                $("#lctjDetailTableId tr").empty();
                //填充数据
                var trHTML = "<tr class='til'>" +
                        "<td align='center'>序号</td>" +
                        "<td align='center'>年</td>" +
                        "<td align='center'>月</td>" +
                        "<td align='center'>投资金额(元)</td>" +
                        "<td align='center'>债权<br>转让盈亏(元)</td>" +
                        "<td align='center'>已收本金(元)</td>" +
                        "<td align='center'>已收利息(元)</td>" +
                        "<td align='center'>已收罚息(元)</td>" +
                        "<td align='center'>已收违约金(元)</td>" +
                        "<td align='center'>理财管理费(元)</td>" +
                        "<td align='center'>待收本金(元)</td>" +
                        "<td align='center'>待收利息(元)</td>" +
                        "<td align='center'>待收罚息(元)</td>" +
                        "</tr>";
                $("#lctjDetailTableId tbody").append(trHTML);//在table最后面添加一行标题

                //查询条件设置值
                var sYear = "";
                var sMonth = "";
                var eYear = "";
                var eMonth = "";
                if (null != returnData.sYear) {
                    sYear = returnData.sYear;
                    $("#sDetailYear").val(sYear);
                }

                if (null != returnData.sMonth) {
                    sMonth = returnData.sMonth;
                    $("#sDetailMonth").val(sMonth);
                }
                if (null != returnData.eYear) {
                    eYear = returnData.eYear;
                    $("#eDetailYear").val(eYear);
                }
                if (null != returnData.eMonth) {
                    eMonth = returnData.eMonth;
                    $("#eDetailMonth").val(eMonth);
                }

                if (null == returnData || null == returnData.lctjDetailList || returnData.lctjDetailList.length == 0) {
                    $("#detailPageContent").html("");
                    $("#lctjDetailTableId tbody").append("<tr><td colspan='13' align='center'>暂无数据</td></tr>");//在table最后面添加一行标题
                    return;
                }
                $("#detailPageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //绑定分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 2);
                });

                trHTML = "";

                if (returnData.lctjDetailList.length > 0) {
                    var resultList = returnData.lctjDetailList;
                    $.each(resultList, function (n, value) {
                        trHTML += "<tr>" +
                                "<td align='center'>" + (n + 1) + "</td>" +
                                "<td align='center'>" + value.F01 + "</td>" +
                                "<td align='center'>" + value.F02 + "</td>" +
                                "<td align='center'>" + fmoney(value.F03, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F04, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F05, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F06, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F07, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F08, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F13, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F09, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F10, 2) + "</td>" +
                                "<td align='center'>" + fmoney(value.F11, 2) + "</td>" +
                                "</tr>";
                        $("#lctjDetailTableId").append(trHTML);//在table最后面添加一行
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
    //返回到理财统计页面
    function backToLxtj() {
        $("div.dialog_year_month").hide();
        $("#detailYear").val("");
        $("#detailMonth").val("");
        $("#lctjDivId").show();
        $("#lctjDetailDivId").hide();
        currentPage = 1;
        financingStatisticQuery(0);
    }
    /**
     * 导出理财统计
     */
    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportFinancingStatisticsDetail.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, FinancingStatisticsDetail.class)%>";
    }

    /**
     *分页查询请求
     * @param obj
     * @param liId
     * @returns {boolean}
     */
    function pageParam(obj, divId, type) {
        if ($(obj).hasClass("cur")) {
            return false;
        }
        $(obj).addClass("cur");
        $(obj).siblings("a").removeClass("cur");
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
        //查询理财统计
        if (divId == 1) {
            financingStatisticQuery();
        } else if (divId == 2) {//查询理财详情
            financingDetailQuery(2);
        } else if (divId == 3) {
            financingSingleDetailQuery(type, divId);
        }
    }

    function financingSingleDetailQuery(type, index) {
        $("div.dialog_year_month").hide();
        var ajaxUrl = "<%=controller.getURI(request, FinancingStatisticsSingleDetail.class) %>";
        if (index == 1) {//1表示从"理财统计"项跳转.需要将当前分页信息设置为1
            currentPage = 1;
        }
        //查询参数
        var sYear = $("#sYear").val();
        var sMonth = $("#sMonth").val();
        var eYear = $("#eYear").val();
        var eMonth = $("#eMonth").val();
        var dataParam = {
            "paging.current": currentPage,
            "pageSize": pageSize,
            "sYear": sYear,
            "sMonth": sMonth,
            "eYear": eYear,
            "eMonth": eMonth,
            "type": type
        };
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            success: function (returnData) {
                //移除table中的tr
                $("#lctjSingleDetailTableId").empty();
                var arrayStr = getContentBytype(type);
                $("#lctjSingleDetailTableId").append(arrayStr[1]);//在table最后面添加一行标题
                $("#lctjSingleDetailDivId").show();
                if (null == returnData || null == returnData.lctjSingleDetailList || returnData.lctjSingleDetailList.length == 0) {
                    $("#lctjSingleDetailPageContent").html("");
                    $("#lctjSingleDetailTableId").append(arrayStr[0]);//在table最后面添加一行标题
                    return;
                }
                setJudgePage(type);
                $("#lctjSingleDetailPageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //绑定分页点击事件
                $("a.page-link").click(function () {
                    pageParam(this, 3, type);
                });
                trHTML = "";
                if (returnData.lctjSingleDetailList.length > 0) {
                    var resultList = returnData.lctjSingleDetailList;
                    $.each(resultList, function (n, value) {
                        trHTML = getTableContent(type, n, value);
                        $("#lctjSingleDetailTableId").append(trHTML);//在table最后面添加一行
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
    
    /**
     * 获取无数据提示,填充表格头部提示和关闭按钮,获取表格title内容
     * @param type
     * @returns Array
     */
     function getContentBytype(type){
    	var arrayStr = new Array();
    	var obj = $("#lctjSingleDetailTitleId");
    	var notDataTr = "<tr>";
    	var tableHeadTr = "<tr class='til'>";
        switch (type) {
            case 'ljsy':
            	obj.html("<a href='javascript:void(0)' class='out' id='out'></a>收益统计");
            	notDataTr += "<td colspan='7' align='center'>";
            	tableHeadTr += "<td align='center'>序号</td>";
            	tableHeadTr += "<td align='center'>年</td>";
            	tableHeadTr += "<td align='center'>月</td>";
            	tableHeadTr += "<td align='center'>已收利息(元)</td>";
            	tableHeadTr += "<td align='center'>已收罚息(元)</td>";
            	tableHeadTr += "<td align='center'>已收违约金(元)</td>";
            	tableHeadTr += "<td align='center'>债权转让盈亏(元)</td>";
                break;
            case 'ljtzje':
            	obj.html("<a href='javascript:void(0)' class='out' id='out'></a>投资金额统计");
            	notDataTr += "<td colspan='5' align='center'>";
            	tableHeadTr += "<td align='center'>序号</td>";
            	tableHeadTr += "<td align='center'>年</td>";
            	tableHeadTr += "<td align='center'>月</td>";
            	tableHeadTr += "<td align='center'>投资金额(元)</td>";
            	tableHeadTr += "<td align='center'>买入债权金额(元)</td>";
                break;
            case 'yhsje':
            	obj.html("<a href='javascript:void(0)' class='out' id='out'></a>已回收金额统计");
            	notDataTr += "<td colspan='7' align='center'>";
            	tableHeadTr += "<td align='center'>序号</td>";
            	tableHeadTr += "<td align='center'>年</td>";
            	tableHeadTr += "<td align='center'>月</td>";
            	tableHeadTr += "<td align='center'>已收本金(元)</td>";
            	tableHeadTr += "<td align='center'>已收利息(元)</td>";
            	tableHeadTr += "<td align='center'>已收罚息(元)</td>";
            	tableHeadTr += "<td align='center'>已收违约金(元)</td>";
                break;
            case 'dhsje':
            	obj.html("<a href='javascript:void(0)' class='out' id='out'></a>待回收金额统计");
            	notDataTr += "<td colspan='6' align='center'>";
            	tableHeadTr += "<td align='center'>序号</td>";
            	tableHeadTr += "<td align='center'>年</td>";
            	tableHeadTr += "<td align='center'>月</td>";
            	tableHeadTr += "<td align='center'>待收本金(元)</td>";
            	tableHeadTr += "<td align='center'>待收利息(元)</td>";
            	tableHeadTr += "<td align='center'>待收罚息(元)</td>";
                break;
        }
        notDataTr += "暂无数据</td></tr>";
        tableHeadTr += "</tr>";
        arrayStr.push(notDataTr);
        arrayStr.push(tableHeadTr);
        $("#out").click(function () {
        	setJudgePage("");
            $("#lctjSingleDetailDivId").hide();
        });
        return arrayStr;
    }
    
    /**
     * 获取表格内容
     * @param type
     * @returns String
     */
    function getTableContent(type, n, value) {
        var tr = "<tr>";
        switch (type) {
            case 'ljsy':
                tr += "<td align='center'>" + (n + 1) + "</td>";
                tr += "<td align='center'>" + value.F01 + "</td>";
                tr += "<td align='center'>" + value.F02 + "</td>";
                tr += "<td align='center'>" + fmoney(value.F06, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F07, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F08, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F04, 2) + "</td>";
                break;
            case 'ljtzje':
                tr += "<td align='center'>" + (n + 1) + "</td>";
                tr += "<td align='center'>" + value.year + "</td>";
                tr += "<td align='center'>" + value.month + "</td>";
                tr += "<td align='center'>" + fmoney(value.investMoney, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.buyCreditMoney, 2) + "</td>";
                break;
            case 'yhsje':
                tr += "<td align='center'>" + (n + 1) + "</td>";
                tr += "<td align='center'>" + value.F01 + "</td>";
                tr += "<td align='center'>" + value.F02 + "</td>";
                tr += "<td align='center'>" + fmoney(value.F05, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F06, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F07, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F08, 2) + "</td>";
                break;
            case 'dhsje':
                tr += "<td align='center'>" + (n + 1) + "</td>";
                tr += "<td align='center'>" + value.F01 + "</td>";
                tr += "<td align='center'>" + value.F02 + "</td>";
                tr += "<td align='center'>" + fmoney(value.F09, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F10, 2) + "</td>";
                tr += "<td align='center'>" + fmoney(value.F11, 2) + "</td>";
                break;
        }
        tr += "</tr>";
        return tr;
    }

    function toAjaxPage(){
    	var judgePage = $("input[name='judgePage']").val();
    	if(judgePage){
    		financingSingleDetailQuery(judgePage,3);
    	}else{
    		if($("#lctjDetailDivId").is(":hidden")){
        		financingStatisticQuery(0);
        	}else{
        		financingDetailQuery(2);
        	}
    	}
    }
    
    function setJudgePage(type){
    	$("input[name='judgePage']").val(type);
    }
</script>
</body>
</html>