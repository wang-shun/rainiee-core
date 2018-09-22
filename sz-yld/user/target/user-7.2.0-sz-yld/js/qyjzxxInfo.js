/**
 * Created by liuguangwen on 2015/5/7.
 */
var currentPage = 1;
var pageCount;
$(function(){
	if(!$("#szhyli").is(":hidden")){
		szhyCheck();
	}
    $("li.normalMenu").click(function(){
        setNormalMenu(this);
    });

});

function setNormalMenu(obj){
    /*if($(obj).hasClass("hover")){
        return;
    }*/
    var hoverLi = $("li.hover");
    hoverLi.attr("class","normalMenu");
    hoverLi.addClass("qyNormalMenu");
    hoverLi.removeClass("qyHover");
    $(obj).attr("class","hover");
    $(obj).addClass("qyHover");
    $(obj).removeClass("qyNormalMenu");
    $(hoverLi).unbind("click");
    $(hoverLi).click(function(){
        setNormalMenu(this);
    });
    $("#pageContent").html("");
    setCountInfo();

    setParamToAjax(1);
}

//设置标信息查询条件，并调用ajax查询结果
function setParamToAjax(currentPageP,obj){
    var pageId = $("li.hover").val();
    var ajaxUrl  = $("#basesUrl").val();;
    var data = {"qyFlag":pageId,"currentPage":currentPageP};
    postService(ajaxUrl,data,obj,"false");
};

//调用ajax查询标信息
function postService(url,data,obj,returnFlag){
	var returnObj = "";
    $.ajax({
        type:"post",
        url:url,
        data:data,
        async: false ,
        dataType:"json",
        success: function(returnData){
        	if(returnFlag == "true"){
        		returnObj = returnData;
        		return;
        	}
            if(1 == returnData.qyFlag)
            {
               if(!isEmpty(returnData.qyBases.F03) || !isEmpty(returnData.qyBases.F19)){	
            	   setBasesInfo(returnData.qyBases);
               }else{
            	   updateBasesInfo(returnData.qyBases);
               }
            }
            else if(2 == returnData.qyFlag)
            {
                setJszlInfo(returnData.qyJszl);
            }
            else if(4 == returnData.qyFlag)
            {
                setLxxxInfo(returnData.qyLxxx);
            }
            else if(5 == returnData.qyFlag)
            {
                setCcxxInfo(returnData.resultCcxx);
                if(null == returnData.resultCcxx || 0 == returnData.resultCcxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            else if(6 == returnData.qyFlag)
            {
                setFcxxInfo(returnData.resultFcxx);
                if(null == returnData.resultFcxx || 0 == returnData.resultFcxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            else if(3 == returnData.qyFlag)
            {
                setCwzlInfo(returnData.resultCwzl);
            }
            else if(7 == returnData.qyFlag && jgFlag == "F")
            {
                setRzxxInfo(returnData);
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
    
    return returnObj;
}

function pageSet(info)
{
    pageCount = info.pageCount;
    $("#pageContent").html(info.pageStr);
    $("a.page-link").click(function(){
        pageParam(this);
    });
}

function setCountInfo(){
    var pageId = $("li.hover").val();
    var addQyCcxxUrl = $("#addQyCcxxUrl").val();
    var addQyFcxxUrl = $("#addQyFcxxUrl").val();
    if(pageId == 1){
        $("#qyJcxx").show();
        $("#qyJxzl").hide();
        $("#qyCcxx").hide();
        $("#pageContent").hide();
        $("#ccxxTable").hide();
        $("#fcxxTable").hide();
        $("#cwzkTable").hide();
        $("#qyLxxx").hide();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
    }else if(pageId == 5)
    {
    	$("#qyBasesAdd").attr("href",addQyCcxxUrl).show();
        $("#qyJcxx").hide();
        $("#qyJxzl").hide();
        $("#qyCcxx").show();
        $("#pageContent").show();
        $("#ccxxTable").show();
        $("#fcxxTable").hide();
        $("#cwzkTable").hide();
        $("#qyLxxx").hide();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
        $("#cwzkBtn").hide();
    }
    else if(pageId ==6)
    {
    	$("#qyBasesAdd").attr("href",addQyFcxxUrl).show();
        $("#qyJcxx").hide();
        $("#qyJxzl").hide();
        $("#qyCcxx").show();
        $("#pageContent").show();
        $("#ccxxTable").hide();
        $("#fcxxTable").show();
        $("#cwzkTable").hide();
        $("#qyLxxx").hide();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
        $("#cwzkBtn").hide();
    }
    else if(pageId == 3)
    {
    	$("#qyBasesAdd").hide();
        $("#qyJcxx").hide();
        $("#qyJxzl").hide();
        $("#qyCcxx").show();
        $("#pageContent").hide();
        $("#ccxxTable").hide();
        $("#fcxxTable").hide();
        $("#cwzkTable").show();
        $("#qyLxxx").hide();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
        $("#cwzkBtn").show();
    }
    else if(pageId==4)
    {
        $("#qyJcxx").hide();
        $("#qyJxzl").hide();
        $("#qyCcxx").hide();
        $("#pageContent").hide();
        $("#ccxxTable").hide();
        $("#fcxxTable").hide();
        $("#cwzkTable").hide();
        $("#qyLxxx").show();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
    }
    else if(pageId ==7)
    {
        $("#qyJcxx").hide();
        $("#qyJxzl").hide();
        $("#qyCcxx").hide();
        $("#rz_div").show();
        $("#userRzxxTable").show();
        $("#pageContent").show();
        $("#ccxxTable").hide();
        $("#fcxxTable").hide();
        $("#cwzkTable").hide();
        $("#qyLxxx").hide();
    }
    else{
        $("#qyJcxx").hide();
        $("#qyJxzl").show();
        $("#qyCcxx").hide();
        $("#pageContent").hide();
        $("#ccxxTable").hide();
        $("#fcxxTable").hide();
        $("#cwzkTable").hide();
        $("#qyLxxx").hide();
        $("#rz_div").hide();
        $("#userRzxxTable").hide();
    }
}

function setCwzlInfo(info)
{
    var table = $("#cwzkTable");
    $("#cwzkTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td style='text-align:center;'></td>");
        td.html("暂无数据");
        td.attr("colspan",6);
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(info[i].F02+"年");
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F03));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F05));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F06));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F07));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(subStringLength(info[i].F08,18));
        td.attr("title",info[i].F08);
        td.appendTo(tr);

        tr.appendTo(table);
    }
    
    $("#cwzkBtn").html('<input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateCwzk();" style="display: inline;">');
}

function showUpdateCwzk(){
	var table = $("#cwzkTable");
    $("#cwzkTable tr:not(:first)").remove();
    var tip = "<p tip></p>";
	var errortip = '<p errortip class="" style="display: none"></p>';
	var ajaxUrl  = $("#basesUrl").val();;
    var data = {"qyFlag":3,"currentPage":1};
    var returnData = postService(ajaxUrl,data,"","true");
    var info = "";
    if(!isEmpty(returnData) && 3 == returnData.qyFlag){
    	info = returnData.resultCwzl;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html('<input type="hidden" value="'+info[i].F02+'" name="t6163s['+i+'].F02"/>'+info[i].F02+"年");
        td.appendTo(tr);

        var tdstr = '<input name="t6163s['+i+'].F03" type="text" maxlength="18" class="text w100 border" style="height:25px;"'+
        	' value="'+info[i].F03+'" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>';
        td = $("<td align='center'></td>");
        td.html(tdstr+tip+errortip);
        td.appendTo(tr);
        
        tdstr = '<input name="t6163s['+i+'].F05" type="text" maxlength="18" class="text w100 border" style="height:25px;"'+
        	' value="'+info[i].F05+'" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>';
        td = $("<td align='center'></td>");
        td.html(tdstr+tip+errortip);
        td.appendTo(tr);

        tdstr = '<input name="t6163s['+i+'].F06" type="text" maxlength="18" class="text w100 border" style="height:25px;"'+
	    	' value="'+info[i].F06+'" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>';
	    td = $("<td align='center'></td>");
	    td.html(tdstr+tip+errortip);
        td.appendTo(tr);

        tdstr = '<input name="t6163s['+i+'].F07" type="text" maxlength="18" class="text w100 border" style="height:25px;"'+
	    	' value="'+info[i].F07+'" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>';
	    td = $("<td align='center'></td>");
	    td.html(tdstr+tip+errortip);
        td.appendTo(tr);

        tdstr = '<input name="t6163s['+i+'].F08" type="text" maxlength="200" class="text w100 border" style="height:25px;" '+
        	'value="'+(isEmpty(info[i].F08) ? "" : info[i].F08)+'"/>';
        td = $("<td align='center'></td>");
        td.html(tdstr+tip+errortip);
        td.appendTo(tr);

        tr.appendTo(table);
    }
    var btn = '<input type="button" class="btn01 mr10 sumbitForme" fromname="form4" value="保存" onclick="saveCwzk();" style="display: inline;">'+
	'<input type="button" onclick="setNormalMenu($(\'#cwzkHover\'));" class="btn01" value="取消" style="display: inline;"/>';
    
    $("#cwzkBtn").html(btn);
    
    //初始化校验
    initVal();
}

function saveCwzk(){
	if(submitVal("form4")){
		var updateQyCwzkUrl = $("#updateQyCwzkUrl").val();
		$.ajax({
	        type:"post",
	        url:updateQyCwzkUrl,
	        data:$("#form4").serialize(),
	        async: false ,
	        dataType:"json",
	        success: function(returnData){
	        	if(returnData.status == "success"){
	        		setNormalMenu($("#cwzkHover"));
	        	}else{
	        		$(".popup_bg").show();
	        		$("#info").html(showDialogInfo(returnData.msg,"error"));
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
}


function setLxxxInfo(info)
{
	$("#qyLxxx span.red").hide();
    if(isEmpty(info.address))
    {
        $("#qyLxxx_address").html("");
    }
    else
    {
        $("#qyLxxx_address").html(info.address);
    }
    if(isEmpty(info.F03))
    {
        $("#qyLxxx_F03").html("");
    }
    else
    {
        $("#qyLxxx_F03").html(info.F03);
    }
    if(isEmpty(info.F07))
    {
        $("#qyLxxx_F07").html("");
    }
    else
    {
    	$("#qyLxxx_F07").html(info.F07);
    }
    if(isEmpty(info.F04))
    {
        $("#qyLxxx_F04").html("");
    }
    else
    {
    	$("#qyLxxx_F04").html(info.F04);
    }
    if(isEmpty(info.F05))
    {
        $("#qyLxxx_F05").html("");
    }
    else
    {
        $("#qyLxxx_F05").html(info.F05);
    }
    
    $("#lxxxBtn").html('<input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateLxxx();" style="display: inline;">');
   /* if(isEmpty(info.F06))
    {
        $("#qyLxxx_F06").text("");
    }
    else
    {
        $("#qyLxxx_F06").text(info.F06);
    }
    if(isEmpty(info.email)){
    	$("#qyLxxx_email").text("");
    }else{
    	$("#qyLxxx_email").text(info.email);
    }*/
}

//修改联系信息
function showUpdateLxxx(){
	$("#qyLxxx span.red").show();
	var tip = "<p tip></p>";
	var errortip = '<p errortip class="" style="display: none"></p>';
	var ajaxUrl  = $("#basesUrl").val();;
    var data = {"qyFlag":4,"currentPage":1};
    var returnData = postService(ajaxUrl,data,"","true");
    var info = "";
    var shengId = "";
    var shiId = "";
    var xianId = "";
    if(!isEmpty(returnData) && 4 == returnData.qyFlag){
    	info = returnData.qyLxxx;
    }
    if(!isEmpty(info.F02)){
    	var regionId = info.F02 + "";
    	shengId = regionId.substr(0, 2)+"0000";
    	shiId = regionId.substr(0, 4)+"00";
    	xianId = regionId;
    }
    
	var hiddenStr = '<input id="shengId" value="'+shengId+'" type="hidden"/><input id="shiId" value="'+shiId+'" type="hidden"/>'+
    	'<input id="xianId" value="'+xianId+'" type="hidden"/>';
	var addressStr = '<select name="sheng" id="sheng" class="select6 required"></select><select name="shi" id="shi" class="select6">'+
		'</select><select name="xian" id="xian" class="select6"></select>';
    $("#qyLxxx_address").html(hiddenStr+addressStr+tip+errortip);
    
    $("#qyLxxx_F03").html('<input name="F03" maxlength="50" type="text" class="text border w300 pl5 required" value="'+(isEmpty(info.F03) ? "" : info.F03)+'"/>'+tip+errortip);
    
    $("#qyLxxx_F07").html('<input name="F07" maxlength="20" type="text" class="text border w300 pl5" value="'+(isEmpty(info.F07) ? "" : info.F07)+'"/>');
    
    $("#qyLxxx_F04").html('<input name="F04" maxlength="11" type="text" class="text border w300 pl5 mobile" value="'+(isEmpty(info.F04) ? "" : info.F04)+'"/>');
    
    $("#qyLxxx_F05").html('<input name="F05" maxlength="40" type="text" class="text border w300 pl5" value="'+(isEmpty(info.F05) ? "" : info.F05)+'"/>');
    
    var btn = '<input type="button" class="btn01 mr10 sumbitForme" fromname="form3" value="保存" onclick="saveLxxx();" style="display: inline;">'+
	'<input type="button" onclick="setNormalMenu($(\'#lxxxHover\'));" class="btn01" value="取消" style="display: inline;"/>';
    
    $("#lxxxBtn").html(btn);
    
    //初始化省市县
    initRegion();
    
    //初始化校验
    initVal();
    
}

//保存联系信息数据
function saveLxxx(){
	if(submitVal("form3")){
		var updateQyLxxxUrl = $("#updateQyLxxxUrl").val();
		$.ajax({
	        type:"post",
	        url:updateQyLxxxUrl,
	        data:$("#form3").serialize(),
	        async: false ,
	        dataType:"json",
	        success: function(returnData){
	        	if(returnData.status == "success"){
	        		setNormalMenu($("#lxxxHover"));
	        	}else{
	        		$(".popup_bg").show();
	        		$("#info").html(showDialogInfo(returnData.msg,"error"));
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
}


function setFcxxInfo(info)
{
	var viewQyFcxxUrl = $("#viewQyFcxxUrl").val();
    var updateQyFcxxUrl = $("#updateQyFcxxUrl").val();
    var table = $("#fcxxTable");
    $("#fcxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td style='text-align:center;'></td>");
        td.html("暂无数据");
        td.attr("colspan",6);
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(info[i].F03);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F04));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F06));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F09);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F10);
        td.appendTo(tr);
        
        td = $("<td align='center'></td>");

        var a = $("<a></a>");
        a.attr("href",viewQyFcxxUrl+"?id="+info[i].F01);
        a.html("查看");
        a.attr("class","highlight");
        a.appendTo(td);

        a = $("<a></a>");
        a.attr("href",updateQyFcxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class","highlight ml10");
        a.appendTo(td);

        td.appendTo(tr);
        tr.appendTo(table);
    }
}

function setCcxxInfo(info)
{
    var updateQyCcxxUrl = $("#updateQyCcxxUrl").val();
    var table = $("#ccxxTable");
    $("#ccxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td style='text-align:center;'></td>");
        td.html("暂无数据");
        td.attr("colspan",6);
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(subStringLength(info[i].F03,18));
        td.attr("title",info[i].F03);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F04);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F05+"年");
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F06));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F07));
        td.appendTo(tr);
        
        td = $("<td align='center'></td>");
        a = $("<a></a>");
        a.attr("href", updateQyCcxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class", "highlight ml10");
        a.appendTo(td);
        td.appendTo(tr);
        tr.appendTo(table);
    }
}

function setJszlInfo(info)
{
	$("#qyJxzl span.red").hide();
    if(!isEmpty(info.F02))
    {
        $("#qyJxzl_F02").html(info.F02);
    }
    else
    {
        $("#qyJxzl_F02").html("");
    }
    if(!isEmpty(info.F03))
    {
        $("#qyJxzl_F03").html(info.F03);
    }
    else
    {
        $("#qyJxzl_F03").html("");
    }
    if(!isEmpty(info.F04))
    {
        $("#qyJxzl_F04").html(info.F04);
    }
    else
    {
        $("#qyJxzl_F04").html("");
    }
    if(!isEmpty(info.F05))
    {
        $("#qyJxzl_F05").html(info.F05);
    }
    else
    {
        $("#qyJxzl_F05").html("");
    }
    
    $("#btnDiv").html('<input type="button" class="btn01 mr10 " value="修改" onclick="showUpdateJszl();" style="display: inline;">');

}

//修改介绍资料
function showUpdateJszl(){
	$("#qyJxzl span.red").show();
	var errortip = '<p errortip class="" style="display: none"></p>';
	var ajaxUrl  = $("#basesUrl").val();;
    var data = {"qyFlag":2,"currentPage":1};
    var returnData = postService(ajaxUrl,data,"","true");
    var info = "";
    if(!isEmpty(returnData) && 2 == returnData.qyFlag){
    	info = returnData.qyJszl;
    }
    $("#qyJxzl_F02").html('<textarea name="F02" cols="" rows="" class="w400 h120 border p5 required max-length-1000">'+(!isEmpty(info.F02)?info.F02:"")+'</textarea><p tip>1000字以内</p>'+errortip);
    
    $("#qyJxzl_F03").html('<textarea name="F03" cols="" rows="" class="w400 h120 border p5 required max-length-500">'+(!isEmpty(info.F03)?info.F03:"")+'</textarea><p tip>500字以内</p>'+errortip);
   
    $("#qyJxzl_F04").html('<textarea name="F04" cols="" rows=""  class="w400 h120 border p5 required max-length-250">'+(!isEmpty(info.F04)?info.F04:"")+'</textarea><p tip>250字以内</p>'+errortip);
    
    $("#qyJxzl_F05").html('<textarea name="F05" cols="" rows="" class="w400 h120 border p5 required max-length-250">'+(!isEmpty(info.F05)?info.F05:"")+'</textarea><p tip>250字以内</p>'+errortip);
    
    var btn = '<input type="button" class="btn01 mr10 sumbitForme" fromname="form2" value="保存" onclick="saveJszl();" style="display: inline;">'+
    	'<input type="button" onclick="setNormalMenu($(\'#jxzlHover\'));" class="btn01" value="取消" style="display: inline;"/>';
    
    $("#btnDiv").html(btn);
    
    //初始化校验
    initVal();
}

//保存介绍资料数据
function saveJszl(){
	if(submitVal("form2")){
		var updateQyJszlUrl = $("#updateQyJszlUrl").val();
		$.ajax({
	        type:"post",
	        url:updateQyJszlUrl,
	        data:$("#form2").serialize(),
	        async: false ,
	        dataType:"json",
	        success: function(returnData){
	        	if(returnData.status == "success"){
	        		setNormalMenu($("#jxzlHover"));
	        	}else{
	        		$(".popup_bg").show();
	        		$("#info").html(showDialogInfo(returnData.msg,"error"));
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
}

function setBasesInfo(info)
{
	$("#qyJcxx span.red").hide();
	$("#szhyli").hide();
	$("#subli").hide();
	if(!isEmpty(info.F18)){
		$("#qyjc").show();
		$("#qyJcxx_F18").html(info.F18);
	}else{
		$("#qyjc").hide();
	}
	$("#qyJcxx_F04").html(info.F04);
	if(info.F20=="Y"){
		$(".nszhy").hide();
		$("#xydm").show();
		$("#qyJcxx_F19").html(info.F19);
	}else{
		$(".nszhy").show();
		$("#xydm").hide();
	    $("#qyJcxx_F03").html(info.F03);
	    $("#qyJcxx_F05").html(info.F05);
	    $("#qyJcxx_F06").html(info.F06);
	}
    
    if("0" == info.F07 || isEmpty(info.F07))
    {
        $("#qyJcxx_F07").html("");
    }
    else
    {
        $("#qyJcxx_F07").html(info.F07+"年");
    }
    if(!isEmpty(info.F08) && parseFloat(info.F08)>0)
    {
        $("#qyJcxx_F08").html(fmoney(info.F08)+"万");
    }
    else
    {
        $("#qyJcxx_F08").html("");
    }
    $("#qyJcxx_F09").html(info.F09);
    if(isEmpty(info.F10) || "0" == info.F10)
    {
        $("#qyJcxx_F10").html("");
    }
    else
    {
        $("#qyJcxx_F10").html(info.F10+"人");
    }
    /*$("#qyJcxx_F11").text(info.F11);
    $("#qyJcxx_F12").text(info.F12);*/
    if(!isEmpty(info.F14) && parseFloat(info.F14)>0)
    {
        $("#qyJcxx_F14").html(fmoney(info.F14)+"万");
    }
    else
    {
        $("#qyJcxx_F14").html("");
    }
    if(!isEmpty(info.F15) && parseFloat(info.F15)>0)
    {
        $("#qyJcxx_F15").html(fmoney(info.F15)+"万");
    }
    else
    {
        $("#qyJcxx_F15").html("");
    }
    //贷款卡证书编号
    if(!isEmpty(info.F16)){
    	$("#dkzsbh").show();
    	$("#qyJcxx_F16").html(info.F16);
    }else{
    	$("#dkzsbh").hide();
    }
    //企业信用证书编号
    if(!isEmpty(info.F17)){
    	$("#xyzsbh").show();
    	$("#qyJcxx_F17").html(info.F17);
    }else{
    	$("#xyzsbh").hide();
    }
    
    //机构担保描述
    if(!isEmpty(info.jgmx)){
    	$("#dbms").show();
    	$("#qyJcxx_jgmx").html(info.jgmx);
    }else{
    	$("#dbms").hide();
    }
    
    //担保情况描述
    if(!isEmpty(info.qkmx)){
    	$("#qkmx").show();
    	$("#qyJcxx_qkmx").html(info.qkmx);
    }else{
    	$("#qkmx").hide();
    }
    
    //开户银行许可证
    if(yeepayFlag == "true"){
    	$("#yhxkz").show();
    	$("#qyJcxx_F21").html(info.F21);
    }else{
    	$("#yhxkz").hide();
    }

}

//加载更新企业基础信息页面
function updateBasesInfo(info)
{
	var tip = "<p tip></p>";
	var errortip = '<p errortip class="" style="display: none"></p>';
	$("#qyJcxx span.red").show();
	$("#szhyli").show();
	$("#qyjc").show();
	//企业名称
	$("#qyJcxx_F04").html(info.F04);
	//$("#qyJcxx_F04").html('<input name="F04" maxlength="20" type="text" class="text border w300 pl5 required min-length-6" value=""/>'+tip+errortip);
	//企业简称
	$("#qyJcxx_F18").html('<input name="F18" maxlength="20" type="text" class="text border w300 pl5" value=""/>'+tip+errortip);
	
	//社会信用代码
	var xydm = "<input id=\"idF19\" name=\"F19\" maxlength=\"18\" type=\"text\" class=\"text border w300 pl5 required\" " +
	" onkeyup=\"value=value.replace(/[\\u4e00-\\u9fa5]/g,'');value=value.trim()\" mtest=\"/^([A-Za-z0-9]+){18}$/\" +" +
	"mtestmsg=\"长度为18位，由字母,数字组成\" value=\"\"/>";
	//营业执照登记注册号
	var yyzz = "<input name=\"F03\" maxlength=\"20\" type=\"text\" class=\"text border w300 pl5 required\" " +
			"onkeyup=\"value=value.replace(/[\\u4e00-\\u9fa5]/g,'');value=value.trim()\" value=\"\"/>";
	//企业纳税号
	var nsh = "<input name=\"F05\" maxlength=\"20\" type=\"text\" class=\"text border w300 pl5 required\" " +
			"onkeyup=\"value=value.replace(/[\\u4e00-\\u9fa5]/g,'');value=value.trim()\" value=\"\"/>";
	//组织机构代码
	var jgdm = "<input name=\"F06\" maxlength=\"20\" type=\"text\" class=\"text border w300 pl5 required max-length-20 min-length-9\" " +
			"onkeyup=\"value=value.replace(/[\\u4e00-\\u9fa5]/g,'');value=value.trim()\" value=\"\"/>";
	$(".nszhy").show();
	$("#xydm").hide();
	$("#qyJcxx_F19").html(xydm+tip+errortip);
	$("#qyJcxx_F03").html(yyzz+tip+errortip);
    $("#qyJcxx_F05").html(nsh+tip+errortip);
    $("#qyJcxx_F06").html(jgdm+tip+errortip);
	//注册年份
    var zcnf = "<input name=\"F07\" maxlength=\"4\" type=\"text\" class=\"text border w300 pl5 required isyear max-length-4 min-length-4\" value=\"\"/>年";
    $("#qyJcxx_F07").html(zcnf+tip+errortip);
    //注册资金
    var zczj = "<input name=\"F08\" maxlength=\"15\" mtest=\"/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/\" " +
    		"mtestmsg=\"必须为数字格式，且最多为两位小数\" type=\"text\" class=\"text border w300 pl5 required\" value=\"\"/>万元";
    $("#qyJcxx_F08").html(zczj+tip+errortip);
    
    //行业
    $("#qyJcxx_F09").html("<input name=\"F09\" maxlength=\"20\" type=\"text\" class=\"text border w300 pl5 required\" value=\"\"/>"+tip+errortip);
    //规模
    $("#qyJcxx_F10").html('<input name="F10" maxlength="9" minlength="1" type="text" class="text border w300 pl5 required isint" value=""/>人'+tip+errortip);
    
    //资产净值
    var zcjz = "<input name=\"F14\" maxlength=\"15\" mtest=\"/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/\"  " +
    		"mtestmsg=\"必须为数字格式，且最多为两位小数\" type=\"text\" class=\"text border w300 pl5 required\" value=\"\"/>万元";
    $("#qyJcxx_F14").html(zcjz+tip+errortip);
    
    //上年度经营现金流入
    var snjyxj = "<input name=\"F15\" maxlength=\"15\" mtest=\"/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\\.[0-9]{1,2}))$/\" " +
    		"mtestmsg=\"必须为数字格式，且最多为两位小数\" type=\"text\" class=\"text border w300 pl5 required\" value=\"\"/>万元";
    $("#qyJcxx_F15").html(snjyxj+tip+errortip);
    
    //贷款卡证书编号
    $("#qyJcxx_F16").html('<input name="F16" maxlength="50" type="text" class="text border w300 pl5" value=""/>');
    //企业信用证书编号
    $("#qyJcxx_F17").html('<input name="F17" maxlength="50" type="text" class="text border w300 pl5" value=""/>');
    
    //开户银行许可证
    if(yeepayFlag == "true"){
    	$("#yhxkz").show();
    	$("#qyJcxx_F21").html('<input name="F21"  maxlength="20" type="text" class="text border w300 pl5 yhgl_ser required" value=""/>'+tip+errortip);
    }else{
    	$("#yhxkz").hide();
    }
    $("#subli").show();
    //初始化校验
    initVal();

}

function szhyCheck() {
    if ($("input[name='szhy']:checked").val() == "Y") {
        $(".sszhy").show();
        $(".nszhy").hide();
    } else {
        $(".sszhy").hide();
        $(".nszhy").show();
    }
    $("#szhy").val($("input[name='szhy']:checked").val());
}

//保存企业基础信息
function saveBasesInfo(){
	if(submitVal("form1")){
		$(".popup_bg").show();
		$("#info").html(showForwardInfo("企业基础信息只可填写一次不可修改，是否确定保存？","question","javaScript:ajaxSubmit();"));
	}
}
//ajax提交数据
function ajaxSubmit(){
	var updateQyBasesUrl = $("#updateQyBasesUrl").val();
	$.ajax({
        type:"post",
        url:updateQyBasesUrl,
        data:$("#form1").serialize(),
        async: false ,
        dataType:"json",
        success: function(returnData){
        	if(returnData.status == "success"){
        		closeInfo();
        		setNormalMenu($("#qyHover"));
        	}else{
        		$(".popup_bg").show();
        		$("#info").html(showDialogInfo(returnData.msg,"error"));
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

//认证信息
var mustImg = [];
var kxrzImg = [];
function setRzxxInfo(data){
    var table = $("#userRzxxTable");
    var xyInfo = data.xyInfo;
    var mustRz = data.mustRz;
    var rzxx = data.rzxx;
    var xyjl = data.xyjl;
    var rzUrl = $("#rzUrl").val();

    var allowUploadFileType = $("#allowUploadFileType").val();
    
    $("#xyed").html("");
    $("#xyjl").html("");
    $("#userRzxxTable tr").remove();
    
    $("#xyed").html("<i class='lines_ico'></i>信用额度(元)："+fmoney(xyInfo.xyed,2) +"");
    
    var xy = "<div class='til'>借款记录：</div><div class='line'></div> <ul class='fl'><li>还清笔数(笔)<br />"+xyjl.hxbs+"</li>";
    xy += "<li>逾期次数(次)<br/>"+xyjl.yqcs+"</li>";
    xy += "<li>严重逾期笔数(笔)<br />"+xyjl.yzyqcs+"</li></ul>";
    $("#xyjl").html(xy);
    
    var tr_title = $("<tr class='til'></tr>");
    tr_title.html("<td width='20%' align='center'>&nbsp;</td><td width='40%' align='left'>认证项</td><td width='10%' align='center'>状态</td><td width='30%' align='center'>操作</td>");
    tr_title.appendTo(table);
    if(mustRz != null){
        for(var i = 0; i < mustRz.length; i++) {
            var tr = $("<tr></tr>");
            var td = $("<td></td>");
            if(i == 0){
            	td = $("<td rowspan='"+mustRz.length+"' align='center' class='border_r'></td>");
            	td.html("<span class='f16'>必要认证：</span>");
            	td.appendTo(tr);
            }

            td = $("<td align='left'></td>");
            td.html(mustRz[i].name);
            td.appendTo(tr);

            td = $("<td align='center'></td>");
            var img_span ="<span title='支持"+allowUploadFileType+"格式,图片不得超过4M' >";
            var type_span = "";
            if('WYZ' == mustRz[i].type){
                type_span ="未验证";
            }else if('TG' == mustRz[i].type){
                type_span ="通过";
            }else if('BTG' == mustRz[i].type){
                type_span ="不通过";
            }else if('DSH' == mustRz[i].type){
                type_span ="待审核";
            }

            td.html(img_span+type_span+"</span>");
            td.appendTo(tr);
            tr.appendTo(table);
            
            td = $("<td align='center'></td>");
            var viewStr = "";
            var imgStr = "";
            var imgDivStr = "<div class='highslide-gallery' style='display:none;'> </div>";
            var img_span ="<span title='支持"+allowUploadFileType+"格式,图片不得超过4M' >";
            var type_span = "";
            var fileCodes = mustRz[i].fileCodes;
            if('WYZ' == mustRz[i].type){
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?isMulti=yes&id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04\" >立即认证</a> </span>";
            }else if('TG' == mustRz[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'must',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?isMulti=yes&id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04 fl ml20\" >再次认证</a> </span>";
            }
            if('BTG' == mustRz[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'must',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?isMulti=yes&id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04 fl ml20\" >重新上传</a> </span>";
            }
            else if('DSH' == mustRz[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'must',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
                type_span ="<span><a href='javascript:void(0)' class=\"btn04 btn_gray fl ml20\" >立即认证</a> </span>";
            }
            mustImg[i] = imgStr;
            td.html(viewStr+imgDivStr+img_span+type_span+"</span>");
            td.appendTo(tr);
            tr.appendTo(table);
        }
    }
    if(rzxx != null){
        for(var i = 0; i < rzxx.length; i++) {
            var tr = $("<tr></tr>");
            var td = $("<td></td>");
            if(i == 0){
            	td = $("<td rowspan='"+rzxx.length+"' align='center' class='border_r'></td>");
            	td.html("<span class='f16'>可选认证：</span>");
            	td.appendTo(tr);
            }
            
            td = $("<td align='left'></td>");
            td.html(rzxx[i].name);
            td.appendTo(tr);

            td = $("<td align='center'></td>");
            var img_span ="<span title='支持"+allowUploadFileType+"格式,图片不得超过4M' >";
            var type_span = "";
            if('WYZ' == rzxx[i].type){
                type_span ="未验证";
            }else if('TG' == rzxx[i].type){
                type_span ="通过";
            }else if('BTG' == rzxx[i].type){
                type_span ="不通过";
            }else if('DSH' == rzxx[i].type){
                type_span ="待审核";
            }

            td.html(img_span+type_span+"</span>");
            td.appendTo(tr);
            tr.appendTo(table);
            
            td = $("<td align='center'></td>");
            var img_span ="<span title='支持"+allowUploadFileType+"格式,图片不得超过4M' >";
            var type_span = "";
            var viewStr = "";
            var imgStr = "";
            var imgDivStr = "<div class='highslide-gallery' style='display:none;'> </div>";
            var fileCodes = rzxx[i].fileCodes;
            if('WYZ' == rzxx[i].type){
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?isMulti=yes&id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04\" >立即认证</a> </span>";
            }else if('TG' == rzxx[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'kx',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	
            	type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?isMulti=yes&id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04 fl ml20\" >再次认证</a> </span>";
            }
            if('BTG' == rzxx[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'kx',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?isMulti=yes&id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04 fl ml20\" >重新上传</a> </span>";
            }
            else if('DSH' == rzxx[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'kx',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	type_span ="<span><a href='javascript:void(0)' class=\"btn04 btn_gray fl ml20\" >立即认证</a> </span>";
            }
            kxrzImg[i] = imgStr;
            td.html(viewStr+imgDivStr+img_span+type_span+"</span>");
            td.appendTo(tr);
            tr.appendTo(table);
        }
    }
    
}

function showRzImg(obj,type,i){
	$(".highslide-gallery").html("");
	var divObj = $(obj).next();
	if(type=="must"){
		divObj.html(mustImg[i]);
	}else{
		divObj.html(kxrzImg[i]);
	}
	var aObj = divObj.children().eq(0);
	aObj.click();
}

function tcc(evn, tid, url, titl) {
    global_art = art.dialog.open(url, {
        id : tid,
        title : titl,
        opacity : 0.1,
        width : 540,
        height : 385,
        lock : true,
        close : function() {
            //var iframe = this.iframe.contentWindow;
           // window.location.reload();
            setParamToAjax(1);
        }
    }, false);
}


function pageParam(obj){
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
    setParamToAjax(currentPage,obj);
}

function isEmpty(str){
    if(str == null || str == "" || str == undefined){
        return true;
    }else{
        return false;
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


function subStringLength(str,maxLength,replace){
    if(isEmpty(str)){
        return;
    }
    if(typeof(replace) == "undefined" || isEmpty(replace)){
        replace = "...";
    }
    var rtnStr = "";
    var index = 0;
    var end = Math.min(str.length,maxLength);
    for(; index < end; ++index){
        rtnStr = rtnStr + str.charAt(index);
    }
    if(str.length > maxLength){
        rtnStr = rtnStr + replace;
    }
    return rtnStr;
}

