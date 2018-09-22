/**
 * Created by liuguangwen on 2015/5/7.
 */
var userAddCcxxUrl = $("#userAddCcxxUrl").val();
var userAddFcxxUrl = $("#userAddFcxxUrl").val();
var userAddGzxxUrl = $("#userAddGzxxUrl").val();
var userAddXlxxUrl = $("#userAddXlxxUrl").val();
var userBasesImg = $("#userBasesImg").val();
var txnanTbUrl = $("#txnanTb").val();
var txnvTbUrl = $("#txnvTb").val();
var userBasesUserNciic = $("#userBasesUserNciic").val();
var currentPage = 1;
var pageCount;
$(function(){

    $("li.normalMenu").click(function(){
        setNormalMenu(this);
    });

});


function setNormalMenu(obj){
    if($(obj).hasClass("hover")){
        return;
    }
    var hoverLi = $("li.hover");
    hoverLi.attr("class","normalMenu");
    $(obj).attr("class","hover");
    $(hoverLi).unbind("click");
    $(hoverLi).click(function(){
        setNormalMenu(this);
    });
    $("#pageContent").html("");
    setCountInfo();

    setParamToAjax(1,this);
}

//设置标信息查询条件，并调用ajax查询结果
function setParamToAjax(currentPageP,obj){
    var pageId = $("li.hover").val();
    var ajaxUrl  = $("#basesUrl").val();
    var data = {"qyFlag":pageId,"currentPage":currentPageP};
    postService(ajaxUrl,data,obj);
};

//调用ajax查询标信息
function postService(url,data,obj){
    $.ajax({
        type:"post",
        url:url,
        data:data,
        async: false ,
        dataType:"json",
        success: function(returnData){
            if(1 == returnData.qyFlag)
            {
                setUserBasesInfo(returnData.userbaseData);
            }
            if(2 == returnData.qyFlag)
            {
                setXlxxInfo(returnData.resultXlxx);
                if(null == returnData.resultXlxx || 0 == returnData.resultXlxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            if(3 == returnData.qyFlag)
            {
                setGzxxInfo(returnData.resultGzxx);
                if(null == returnData.resultGzxx || 0 == returnData.resultGzxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            if(4 == returnData.qyFlag)
            {
                setFcxxInfo(returnData.resultFcxx);
                if(null == returnData.resultFcxx || 0 == returnData.resultFcxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            if(5 == returnData.qyFlag)
            {
                setCcxxInfo(returnData.resultCcxx);
                if(null == returnData.resultCcxx || 0 == returnData.resultCcxx.length)
                {
                    return;
                }
                pageSet(returnData);
            }
            if(6 == returnData.qyFlag)
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
}

function pageSet(info)
{
    pageCount = info.pageCount;
    $("#pageContent").html(info.pageStr);
    $("a.page-link").click(function(){
        pageParam(this);
    });
}

function setCcxxInfo(info)
{
    var userViewCcxxUrl = $("#userViewCcxxUrl").val();
    var userUpdateCcxxUrl = $("#userUpdateCcxxUrl").val();
    var table = $("#userCcxxTable");
    $("#userCcxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html("暂无数据");
        td.attr("colspan",7);
        td.attr("align",'center');
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++) {
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(i + 1);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F03);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F04);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F05);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F06));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F07));
        td.appendTo(tr);

        td = $("<td align='center'></td>");

        var a = $("<a></a>");
        a.attr("href", userViewCcxxUrl+"?id="+info[i].F01);
        a.html("查看");
        a.attr("class", "highlight");
        a.appendTo(td);

        a = $("<a></a>");
        a.attr("href", userUpdateCcxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class", "highlight ml10");
        a.appendTo(td);

        td.appendTo(tr);

        tr.appendTo(table);
    }
}

function setFcxxInfo(info)
{
    var userViewFcxxUrl = $("#userViewFcxxUrl").val();
    var userUpdateFcxxUrl = $("#userUpdateFcxxUrl").val();
    var table = $("#userFcxxTable");
    $("#userFcxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html("暂无数据");
        td.attr("colspan",6);
        td.attr("align",'center');
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(i+1);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(subStringLength(info[i].F03,10));
        td.attr("title",info[i].F03);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F04));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(fmoney(info[i].F06));
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(subStringLength(info[i].F09,15));
        td.attr("title",info[i].F09);
        td.appendTo(tr);

        td = $("<td align='center'></td>");

        var a = $("<a></a>");
        a.attr("href",userViewFcxxUrl+"?id="+info[i].F01);
        a.html("查看");
        a.attr("class","highlight");
        a.appendTo(td);

        a = $("<a></a>");
        a.attr("href",userUpdateFcxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class","highlight ml10");
        a.appendTo(td);

        td.appendTo(tr);

        tr.appendTo(table);
    }
}

function setXlxxInfo(info)
{
    var userViewXlxxUrl = $("#userViewXlxxUrl").val();
    var userUpdateXlxxUrl = $("#userUpdateXlxxUrl").val();
    var table = $("#userXlxxTable");
    $("#userXlxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html("暂无数据");
        td.attr("colspan",7);
        td.attr("align",'center');
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(i+1);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F03);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F04+"年");
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F07+"年");
        td.appendTo(tr);
        
        td = $("<td align='center'></td>");
        td.html(info[i].F05);
        td.appendTo(tr);

        td = $("<td align='center' title='"+info[i].F06+"'></td>");
        td.html(subStringLength(info[i].F06,10));
        td.appendTo(tr);

        td = $("<td align='center'></td>");

        var a = $("<a></a>");
        a.attr("href",userViewXlxxUrl+"?id="+info[i].F01);
        a.html("查看");
        a.attr("class","highlight");
        a.appendTo(td);

        a = $("<a></a>");
        a.attr("href",userUpdateXlxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class","highlight ml10");
        a.appendTo(td);

        td.appendTo(tr);

        tr.appendTo(table);
    }
}

function setGzxxInfo(info)
{
    var userViewGzxxUrl = $("#userViewGzxxUrl").val();
    var userUpdateGzxxUrl = $("#userUpdateGzxxUrl").val();
    var table = $("#userGzxxTable");
    $("#userGzxxTable tr:not(:first)").remove();
    if(null == info || 0 == info.length )
    {
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html("暂无数据");
        td.attr("colspan",6);
        td.attr("align",'center');
        td.appendTo(tr);
        tr.appendTo(table);
        return;
    }
    for(var i = 0; i < info.length; i++){
        var tr = $("<tr></tr>");
        var td = $("<td align='center'></td>");
        td.html(i+1);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        if("LZ" == info[i].F03)
        {
            td.html("离职");
        }
        else
        {
            td.html("在职");
        }
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F04);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F05);
        td.appendTo(tr);

        td = $("<td align='center'></td>");
        td.html(info[i].F08);
        td.appendTo(tr);

        td = $("<td align='center'></td>");

        var a = $("<a></a>");
        a.attr("href",userViewGzxxUrl+"?id="+info[i].F01);
        a.html("查看");
        a.attr("class","highlight");
        a.appendTo(td);

        a = $("<a></a>");
        a.attr("href",userUpdateGzxxUrl+"?id="+info[i].F01);
        a.html("修改");
        a.attr("class","highlight ml10");
        a.appendTo(td);

        td.appendTo(tr);

        tr.appendTo(table);
    }
}

function setUserBasesInfo(info)
{
    if(isEmpty(info.icon))
    {
    	var txImagUrl = "";
    	if(!isEmpty(info.sex) && info.sex=="女")
        {
    		txImagUrl = txnvTbUrl?txnvTbUrl:userBasesImg+'/images/woman_portrait.jpg';
        }else{
        	txImagUrl = txnanTbUrl?txnanTbUrl:userBasesImg+'/images/portrait.jpg';
        }
    	$("#userBases_icon").html('<img src="'+txImagUrl+'" />');
    }
    else
    {
        $("#userBases_icon").html('<img src='+info.userImg+'  />');
    }
    $("#userBases_userName").text(info.userName);


    if(isEmpty(info.name))
    {
        $("#userBases_nameRz").text("");
        $("#userBases_nameRz").html('<a href='+userBasesUserNciic+'>去绑定</a>');
        $("#userBases_nameRz").removeClass("jc_ico1");
        $("#userBases_nameRz").addClass("jc_ico2");
    }
    else
    {
        $("#userBases_name").text(info.name.substring(0,1)+"**");
        $("#userBases_nameRz").text("已认证");
        $("#userBases_nameRz").removeClass("jc_ico2");
        $("#userBases_nameRz").addClass("jc_ico1");

    }
    if(isEmpty(info.idCard))
    {
        $("#userBases_idCard").text("");
        $("#userBases_idCardRz").html('<a href='+userBasesUserNciic+'>去绑定</a>');
        $("#userBases_idCardRz").removeClass("jc_ico1");
        $("#userBases_idCardRz").addClass("jc_ico2");
    }
    else
    {
        $("#userBases_idCard").text(info.idCard);
        $("#userBases_idCardRz").text("已认证");
        $("#userBases_idCardRz").removeClass("jc_ico2");
        $("#userBases_idCardRz").addClass("jc_ico1");
    }
    if(!isEmpty(info.phoneNumber) && info.phoneNumber.length == 11)
    {
        $("#userBases_phoneNumber").text(info.phoneNumber.substring(0,3)+"****"+info.phoneNumber.substring(8,11));
        $("#userBases_phoneNumberRz").text("已认证");
        $("#userBases_phoneNumberRz").removeClass("jc_ico2");
        $("#userBases_phoneNumberRz").addClass("jc_ico1");
    }
    else
    {
        $("#userBases_phoneNumber").text("");
        $("#userBases_phoneNumberRz").html('<a href='+userBasesUserNciic+'>去绑定</a>');
        $("#userBases_phoneNumberRz").removeClass("jc_ico1");
        $("#userBases_phoneNumberRz").addClass("jc_ico2");
    }
    if(!isEmpty(info.emil))
    {
        $("#userBases_emil").text(info.emil.substring(0,1)+"*****"+info.emil.split("@")[1]);
        $("#userBases_emilRz").text("已认证");
        $("#userBases_emilRz").removeClass("jc_ico2");
        $("#userBases_emilRz").addClass("jc_ico1");
    }
    else
    {
        $("#userBases_emil").text("");
        $("#userBases_emilRz").html('<a href='+userBasesUserNciic+'>去绑定</a>');
        $("#userBases_emilRz").removeClass("jc_ico1");
        $("#userBases_emilRz").addClass("jc_ico2");
    }
    if(!isEmpty(info.sex))
    {
        $("#userBases_sfzh").html(info.sex);
    }
    else
    {
        $("#userBases_sfzh").html("");
    }
    if(!isEmpty(info.birthday))
    {
        $("#userBases_sfzh1").html(info.birthday);
    }
    else
    {
        $("#userBases_sfzh1").html("");
    }

}

function setCountInfo(){
    var pageId = $("li.hover").val();
    if(pageId == 2)
    {
        $("#userBasesAdd").attr("href",userAddXlxxUrl).show();
        $("#userXlxx").show();
        $("#userXlxxTable").show();
        $("#userGzxxTable").hide();
        $("#userFcxxTable").hide();
        $("#userCcxxTable").hide();
        $("#userBases").hide();
        $("#rz_div").hide();
    }
    else if(pageId == 3)
    {
        $("#userBasesAdd").attr("href",userAddGzxxUrl).show();
        $("#userXlxx").show();
        $("#userXlxxTable").hide();
        $("#userGzxxTable").show();
        $("#userFcxxTable").hide();
        $("#userCcxxTable").hide();
        $("#userBases").hide();
        $("#rz_div").hide();
    }
    else if(pageId == 4)
    {
        $("#userBasesAdd").attr("href",userAddFcxxUrl).show();
        $("#userXlxx").show();
        $("#userXlxxTable").hide();
        $("#userGzxxTable").hide();
        $("#userFcxxTable").show();
        $("#userCcxxTable").hide();
        $("#userBases").hide();
        $("#rz_div").hide();
    }
    else if(pageId == 5)
    {
        $("#userBasesAdd").attr("href",userAddCcxxUrl).show();
        $("#userXlxx").show();
        $("#userXlxxTable").hide();
        $("#userGzxxTable").hide();
        $("#userFcxxTable").hide();
        $("#userCcxxTable").show();
        $("#userBases").hide();
        $("#rz_div").hide();
    }
    else if(pageId == 6)
    {
        $("#userBasesAdd").hide();
        $("#userXlxx").hide();
        $("#rz_div").show();
        $("#userXlxxTable").hide();
        $("#userGzxxTable").hide();
        $("#userFcxxTable").hide();
        $("#userCcxxTable").hide();
        $("#userBases").hide();
    }
    else
    {
        $("#userBases").show();
        $("#userXlxx").hide();
        $("#rz_div").hide();
    }
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
    if(str == null || str == ""){
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

//认证信息
var mustImg = [];
var kxrzImg = [];
function setRzxxInfo(data){
    //var userViewCcxxUrl = $("#userViewCcxxUrl").val();
    //var userUpdateCcxxUrl = $("#userUpdateCcxxUrl").val();
    var table = $("#userRzxxTable");
    var xyInfo = data.xyInfo;
    var mustRz = data.mustRz;
    var rzxx = data.rzxx;
    var xyjl = data.xyjl;
    var rzUrl = $("#rzUrl").val();

    var allowUploadFileType = $("#allowUploadFileType").val();
    //var siteName = $("#siteName").val();
    
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
            var img_span ="<span title='支持"+allowUploadFileType+"格式,图片不得超过4M' >";
            var type_span = "";
            var viewStr = "";
            var imgStr = "";
            var imgDivStr = "<div class='highslide-gallery' style='display:none;'> </div>";
            var fileCodes = mustRz[i].fileCodes;
            if('WYZ' == mustRz[i].type){
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04\" >立即认证</a> </span>";
            }else if('TG' == mustRz[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'must',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04 fl ml20\" >再次认证</a> </span>";
            }
            if('BTG' == mustRz[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'must',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+mustRz[i].id+"','"+rzUrl+"?id="+mustRz[i].id+"','"+mustRz[i].name+"')\" class=\"btn04 fl ml20\" >重新上传</a> </span>";
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
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04\" >立即认证</a> </span>";
            }else if('TG' == rzxx[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'kx',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
                type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04 fl ml20\" >再次认证</a> </span>";
            }
            if('BTG' == rzxx[i].type){
            	viewStr += "<a onclick=\"showRzImg(this,'kx',"+i+")\" href='javascript:void(0);' class=\"btn04 fl\" >查看</a>";
            	for(var j=0;j<fileCodes.length;j++){
            		imgStr += "<a onclick=\"return hs.expand(this)\" href='"+fileCodes[j]+"' class=\"btn04 highslide fl\" ><img src='"+fileCodes[j]+"'  alt=\"Highslide JS\" /></a>";
            		
            	}
            	type_span ="<span><a href='javascript:void(0)' onclick=\"tcc(this,'"+rzxx[i].id+"','"+rzUrl+"?id="+rzxx[i].id+"','"+rzxx[i].name+"')\" class=\"btn04 fl ml20\" >重新上传</a> </span>";
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

