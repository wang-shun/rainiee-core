$(function (){
    $("div.dialog_close").click(function(){
        $("div.dialog").hide();
    });
    $("#cancel").click(function(){
        $("div.dialog").hide();
    });
    $("#ok").click(function(){
        $("div.dialog").hide();
    });

    $("#year").click(function (){
        $("div [lang = 'zh-cn']").show();
        WdatePicker({dateFmt:'yyyy',
            isShowToday:false,
            onclearing:function(){
                $('#year').val('');
                $('#month').val('');
                return true;
            }
        });
    });

    $("#month").focus(function (){
        var year = $("#year").val();
        if(year == null || year == ""){
            $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份！</p>");
            $("div [lang = 'zh-cn']").hide();
            $("div.dialog").show();
            $("div.popup_bg").show();
            return;
        }else{
            WdatePicker({dateFmt:'MM',isShowToday:false});
        }
    });

    $("#detailYear").click(function (){
        $("div [lang = 'zh-cn']").show();
        WdatePicker({dateFmt:'yyyy',
            isShowToday:false,
            onclearing:function(){
                $('#detailYear').val('');
                $('#detailMonth').val('');
                return true;
            }
        });
    });
    $("#detailMonth").click(function (){
        var year = $("#detailYear").val();
        if(year == null || year == ""){
            $("#infoDiv").html("<p class='f20 gray33'>请选择年份，再选择月份!</p>");
            $("div [lang = 'zh-cn']").hide();
            $("div.dialog").show();
            $('#detailMonth').val('');
            $("div.popup_bg").show();
            return;
        }else{
            WdatePicker({dateFmt:'MM',isShowToday:false});
        }
    });
    //隐藏
    //$("#dpTodayInput").hide();
    //查询借款统计信息
    newCreditStatisticsQuery(1);
});


var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

/**
 *查询借款统计信息
 */
function newCreditStatisticsQuery(type){
    if(type==1){//1表示从"详情"跳转.需要将当前分页信息设置为1
        currentPage = 1 ;
    }
    //发送请求,生成数据,再将数据填充
    var ajaxUrl = $("#ajaxUrl").val();

    $("#jktjDivId").show();
    $("#jktjDetailDivId").hide();

    $("#detailYear").val("");
    $("#detailMonth").val("");
    //查询参数
    var timeStart = $("#year").val();
    var timeEnd = $("#month").val();
    var dataParam = {
        "paging.current": currentPage,
        "pageSize": pageSize,
        "year": timeStart,
        "month": timeEnd
    };
    $.ajax({
        type:"post",
        dataType:"json",
        url:ajaxUrl,
        data:dataParam,
        success:function(returnData){
            //移除table中的tr
            $("#jktjTableId tr").empty();
            //填充数据
            var trHTML = '<tr class="til">'+
                '<td align="center">序号</td>'+
                '<td align="center">年</td>'+
                '<td align="center">月</td>'+
                '<td align="center">借款金额(元)</td>'+
                '<td align="center">借款利息(元)</td>'+
                '<td align="center">借款管理费(元)</td>'+
                '<td align="center">已还金额(元)</td>'+
                '<td align="center">待还金额(元)</td>'+
                '</tr>';
            $("#jktjTableId tbody").append(trHTML);//在table最后面添加一行标题
            trHTML = "";

            //给合计设置值
            if (null != returnData.jktjTotal) {
                var resultTotal = returnData.jktjTotal;
                $("#ulCreditId1").text(fmoney(resultTotal.loanTotal));
                $("#ulCreditId2").text(fmoney(resultTotal.interestTotal));
                $("#ulCreditId3").text(fmoney(resultTotal.manageTotal));
                $("#ulCreditId4").text(fmoney(resultTotal.payTotal));
                $("#ulCreditId5").text(fmoney(resultTotal.notPayTotal));
            }

            //查询条件设置值
            var year = "";
            var month = "";
            if (null != returnData.year && "0" != returnData.year) {
                year = returnData.year;
                $("#year").val(year);
            }

            if (null != returnData.month && "0" != returnData.month) {
                month = returnData.month;
                $("#month").val(month);
            }

            if (null == returnData || null == returnData.jktjList || returnData.jktjList.length == 0) {
                $("#pageContent").html("")
                $("#jktjTableId tbody").append("<tr><td colspan='8' align='center'>暂无数据</td></tr>");//在table最后面添加一行标题
                return;
            }

            $("#pageContent").html(returnData.pageStr);
            pageCount = returnData.pageCount;
            //绑定分页点击事件
            $("a.page-link").click(function(){
                pageParam(this,1);
            });

            if(null == returnData.jktjList ){
                return;
            }else if(returnData.jktjList.length > 0){
                var resultList = returnData.jktjList;
                $.each(resultList, function (n, value) {
                    trHTML += "<tr>" +
                        "<td align='center'>"+ (n+1) + "</td>" +
                        "<td align='center'>"+ value.year +"</td>" +
                        "<td align='center'>"+ value.month +"</td>" +
                        "<td align='center'>"+ fmoney(value.loanMoney) +"</td>" +
                        "<td align='center'>" + fmoney(value.loanInterest)+"</td>" +
                        "<td align='center'>" + fmoney(value.manageMoney) +"</td>" +
                        "<td align='center'>" +fmoney(value.payMoneyTotal) +"</td>" +
                        "<td align='center'>" +fmoney(value.notPayMoneyTotal) +"</td>" +
                        "</tr>";
                    $("#jktjTableId").append(trHTML);//在table最后面添加一行
                    trHTML = "";
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
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
 *借款详情
 * 2015-05-09
 */
function creditDetailQuery(type){
	var timeStart = $("#detailYear").val();
    var timeEnd = $("#detailMonth").val();
    if(type==1){//1表示从"详情"跳转.需要将当前分页信息设置为1
        currentPage = 1 ;
        if(!timeStart){
        	timeStart = $("#year").val();
        }
        if(!timeEnd){
        	timeEnd = $("#month").val();
        }
    }
    $("#jktjDivId").hide();
    $("#jktjDetailDivId").show();
    //发送请求,生成数据,再将数据填充
    var ajaxUrl = $("#ajaxUrl").val();
    //查询参数
    var dataParam = {
        "paging.current": currentPage,
        "pageSize": pageSize,
        "year": timeStart,
        "month": timeEnd
    };
    $.ajax({
        type:"post",
        dataType:"json",
        url:ajaxUrl,
        data:dataParam,
        success:function(returnData){
            //移除table中的tr
            $("#jktjDetailTableId tr").empty();
            //填充数据
            var trHTML = '<tr class="til f12">'+
                '<td align="center">序号</td>'+
                '<td align="center">年</td>'+
                '<td align="center">月</td>'+
                '<td align="center">借款金额<br />(元)</td>'+
                '<td align="center">借款利息<br />(元)</td>'+
                '<td align="center">借款管理费<br />(元)</td>'+
                '<td align="center">已还本金<br />(元)</td>'+
                '<td align="center">已还利息<br />(元)</td>'+
                '<td align="center">已还逾期罚息<br />(元)</td>'+
                '<td align="center">违约金<br />(元)</td>'+
                '<td align="center">待还本金<br />(元)</td>'+
                '<td align="center">待还利息<br />(元)</td>'+
                '<td align="center">待还逾期罚息<br />(元)</td>'+
                '</tr>';
            $("#jktjDetailTableId tbody").append(trHTML);//在table最后面添加一行标题
            trHTML = "";

            //查询条件设置值
            var year = "";
            var month = "";
            if (null != returnData.year && "0" != returnData.year) {
                year = returnData.year;
                $("#detailYear").val(year);
            }
            if (null != returnData.month && "0" != returnData.month) {
                month = returnData.month;
                $("#detailMonth").val(month);
            }

            if (null == returnData || null == returnData.jktjList || returnData.jktjList.length == 0) {
                $("#detailPageContent").html("")
                $("#jktjDetailTableId tbody").append("<tr><td colspan='13' align='center'>暂无数据</td></tr>");//在table最后面添加一行标题
                return;
            }

            $("#detailPageContent").html(returnData.pageStr);
            pageCount = returnData.pageCount;
            //绑定分页点击事件
            $("a.page-link").click(function(){
                pageParam(this,2);
            });

            if(null == returnData.jktjList ){
                return;
            }else if(returnData.jktjList.length > 0){
                var resultList = returnData.jktjList;
                $.each(resultList, function (n, value) {
                    trHTML += "<tr>" +
                        "<td align='center'>"+ (n+1) + "</td>" +
                        "<td align='center'>"+ value.year +"</td>" +
                        "<td align='center'>"+ value.month +"</td>" +
                        "<td align='center'>"+ fmoney(value.loanMoney) +"</td>" +
                        "<td align='center'>"+ fmoney(value.loanInterest) +"</td>" +
                        "<td align='center'>"+ fmoney(value.manageMoney) +"</td>" +
                        "<td align='center'>" +fmoney(value.payMoney) +"</td>" +
                        "<td align='center'>" +fmoney(value.payInterest) +"</td>" +
                        "<td align='center'>" +fmoney(value.payDefaultIns) +"</td>" +
                        "<td align='center'>" +fmoney(value.deditMoney) +"</td>" +
                        "<td align='center'>" +fmoney(value.notPayMoney) +"</td>" +
                        "<td align='center'>" +fmoney(value.notPayInterest) +"</td>" +
                        "<td align='center'>" +fmoney(value.notPayDefaultIns) +"</td>" +
                        "</tr>";
                    $("#jktjDetailTableId").append(trHTML);//在table最后面添加一行
                    trHTML = "";
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
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

function showExport()
{
    $("#form_loan").submit();
}

/**
 *分页查询请求
 * @param obj
 * @param liId
 * @returns {boolean}
 */
function pageParam(obj,divId){
    if($(obj).hasClass("cur")){
        return false;
    }
    $(obj).addClass("cur");
    $(obj).siblings("a").removeClass("cur");
    if($(obj).hasClass("startPage")){
        currentPage = 1;
    }else if($(obj).hasClass("prev")){
        currentPage = parseInt(currentPage) - 1;
    }else if($(obj).hasClass("next")){
        currentPage = parseInt(currentPage) + 1;
    }else if($(obj).hasClass("endPage")){
        currentPage = pageCount;
    }else{
    	currentPage = parseInt($(obj).html());
    }

    if(divId == 1){
        newCreditStatisticsQuery(2);
    }else if(divId == 2){//查询理财详情
        creditDetailQuery(2);
    }
}

function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    var f = s < 0 ? "-" : ""; //判断是否为负数
    s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    var t = "";
    for(var i = 0; i < l.length; i++ ) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return f + t.split("").reverse().join("") + "." + r.substring(0,2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
}

function toAjaxPage(){
	if($("#jktjDetailDivId").is(":hidden")){
		newCreditStatisticsQuery(2);
	}else{
		creditDetailQuery(2);
	}
}