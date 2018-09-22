$("#checkAll").click(function() {
	var checked = $("#checkAll").attr("checked");
	if (checked == 'checked') {
		$(".checkId").attr("checked", true);
	} else {
		$(".checkId").attr("checked", false);
	}
});

$("#reverseall").click(function() {
	var checked = $(".checkId").attr("checked");
	checked=!checked;
});

$(".checkId").click(function() {
	var checkBoxs = $(".checkId");
	var isCheckedAll = true;
	for ( var i = 0; i <= checkBoxs.length - 1; i++) {
		if ($(checkBoxs[i]).attr("checked") != "checked") {
			isCheckedAll = false;
		}
	}
	if (isCheckedAll) {
		$("#checkAll").attr("checked", true);
	} else {
		$("#checkAll").attr("checked", false);
	}
});
var urlValue = "";

function load(url,title,userName,amount,trunTitle)
{	
	var arr = new Array();
	arr.push("<div class='popup-box' style='min-height:200px;'>"
			+"<div class='popup-title-container'>"
			+"<h3 class='pl20 f18'>放款</h3>"
			+"<a class='icon-i popup-close2' href='javascript:void(0);' onclick='closeShow();'></a>"
			+"</div>"
			+"<div class='popup-content-container pt20 ob20 clearfix'>"
			+"<div class='mb20 gray6 f18'>"
			+"<ul>"
			+"<li class='mb10'><span class='display-ib tr mr5'>借款标题：</span><span class='pl10' title='"+title+"'>"+ trunTitle + "</span></li>"
			+"<li class='mb10'><span class='display-ib tr mr5'>借款账户：</span><span class='pl10'>" + userName + "</span></li>"
			+"<li class='mb10'><span class='display-ib tr mr5'>放款金额(元)：</span><span class='pl10'>" + amount + "</span></li>"
			+"</ul>"
			+"</div>"
			+"<div class='tc f16'>"
			+"<a href='javascript:void(0);' onclick='realseSubmit();' class='btn-blue2 btn white radius-6 pl20 pr20' >确定</a>"
			+"<a class='btn btn-blue2 radius-6 pl20 pr20 ml40' href='javascript:void(0);' onclick='closeShow();'>取消</a>"
			+"</div>"
			+"</div>"
			+"</div>"
			+"<div class='popup_bg'></div>");
	
	$("#realse").html(arr.join(""));
	
	$("#realse").show();
	
	urlValue = url;
}

function realseSubmit(){
	var token = $("input[name=token]").val();
	location.href=urlValue+"&token="+token;
}

function closeShow (){
	$("#realse").hide();
}

function batchLoan(url) {
	var ids = "";
	var ckeds = $(".checkId:checked");
	if (ckeds == null || ckeds.length <= 0) {
		alert("请选择放款记录!");
		return;
	}
	if (!window.confirm("是否确认对您所选择的所有借款进行放款？")) {
		return;
	}
	for ( var i = 0; i < ckeds.length; i++) {
		var val = $(ckeds[i]).val();
		if (i == 0) {
			ids += val;
		} else {
			ids += "," + val;
		}
	}
	location.href = url + "?ids=" + ids;
}
function notLoad(id)
{
	$("#id").attr("value", id);
	$("#reasonDiv").show();
}
$(".close").click(function() {
	$("#reasonDiv").hide();
});
