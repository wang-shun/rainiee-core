function show(obj1) {
	var obj = document.getElementById(obj1);
	if (obj.style.display == "block") {
		obj.style.display = "none";
	} else{
		obj.style.display = "block";
	}
}

function openShutManager(oSourceObj, oTargetObj, shutAble, oOpenTip, oShutTip,phoneNumber) {
	$("p[errortip]").hide().removeClass("red").html("");
	$("p[tip]").show();
	var sourceObj = typeof oSourceObj == "string" ? document.getElementById(oSourceObj) : oSourceObj;
	var targetObj = typeof oTargetObj == "string" ? document.getElementById(oTargetObj) : oTargetObj;
	var openTip = oOpenTip || "";
	var shutTip = oShutTip || "";
	if (targetObj.style.display != "none") {
		if (shutAble){
			return;
		}
		targetObj.style.display = "none";
		if (openTip && shutTip) {
			sourceObj.innerHTML = shutTip;
		}
		if("box1"!= oTargetObj){
			$("div#"+oTargetObj +" .text").val("");
			$("div#"+oTargetObj +" .error_tip").html("");
		}
		//history.go(0);
		if("box3"== oTargetObj && oShutTip=="修改"){
			initBandEmail();
		}

		if( "box4" == oTargetObj && oShutTip=="修改"){
			initBandPhone();
		}
	} else {
		if(("box5" == oTargetObj || "box10" == oTargetObj) && phoneNumber == null){
			//targetObj.style.display = "block";
			//$(".tou_li").html("<li style='text-align: center;color: red;'>请先认证手机号码</li>");
			//sourceObj.innerHTML = openTip;
			//$("#box51").show();
			//artDialog.alert("请先认证手机号码",function(){});

			$("#info").html(showDialogInfo("请先认证手机号码!","doubt"));
			$("div.popup_bg").show();
			
			//$("#box51").find("input").bind("click",function(){
			//	$("#box51").hide();
			//});
			//$("#vp>li").html("请先认证手机号码");
		}else{
			if("box6" == oTargetObj){
				document.getElementById("box7").style.display = "none";
				document.getElementById("jymmzh").innerHTML = "找回";
			}else if("box7" == oTargetObj){
				document.getElementById("box6").style.display = "none";
				document.getElementById("jymmxg").innerHTML = "修改&nbsp;|&nbsp;";
			}else if("box8" == oTargetObj){
				$("select option[value='']").attr("selected", "selected"); 
				$(".issue_answer").show(); 
				$(".answerIn").hide(); 
			}else if("box9" == oTargetObj){
				document.getElementById("box10").style.display = "none";
				document.getElementById("mbwtzh").innerHTML = "找回";
				$("#mbwtSteps2").hide();
				$("#mbwtSteps3").hide();
				$("#mbwtSteps1").show();
				$("select option[value='']").attr("selected", "selected"); 
			}else if("box10" == oTargetObj){
				document.getElementById("box9").style.display = "none";
				document.getElementById("mbwtxg").innerHTML = "修改&nbsp;|&nbsp;";
				$("#securityStep2").hide();
				$("#securityStep3").hide();
				$("#securityStep1").show();
				$("select option[value='']").attr("selected", "selected"); 
			}
			
			targetObj.style.display = "block";
			if (openTip && shutTip) {
				sourceObj.innerHTML = openTip;
			}
		}
	}
}

function setTab(name, cursel, n) {
	for (var i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

function initBandEmail(){
	$("div#box3 #mt1").show();
	$("div#box3 #e1").hide();
	$("div#box3 #e2").hide();
	$("div#box3 #e3").hide();
	$("div#box3 #ep1").hide();
	$("div#box3 #ep2").hide();
	$("div#box3 #ep3").hide();
	$("div#box3 #notBindPhone_div").hide();
}

function initBandPhone(){
	$("div#box4 #p1").hide();
	$("div#box4 #p2").hide();
	$("div#box4 #p3").hide();
	$("div#box4 #ip1").hide();
	$("div#box4 #ip2").hide();
	$("div#box4 #ip3").hide();
	$("div#box4 #mt2").show();
}

