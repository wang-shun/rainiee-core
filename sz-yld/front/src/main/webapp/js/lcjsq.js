Number.prototype.toFixed = function(d)
	  {
	      var s=this+"";if(!d)d=0;
	      if(s.indexOf(".")==-1)s+=".";s+=new Array(d+1).join("0");
	      if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+ (d+1) +"})?)\\d*$").test(s))
	      {
	          var s="0"+ RegExp.$2, pm=RegExp.$1, a=RegExp.$3.length, b=true;
	          if (a==d+2){a=s.match(/\d/g); if (parseInt(a[a.length-1])>4)
	          {
	              for(var i=a.length-2; i>=0; i--) {a[i] = parseInt(a[i])+1;
	              if(a[i]==10){a[i]=0; b=i!=1;} else break;}
	          }
	          s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");
	      }if(b)s=s.substr(1);return (pm+s).replace(/\.$/, "");} return this+"";
	};
	function accDiv(arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length;}catch(e){}
		try{t2=arg2.toString().split(".")[1].length;}catch(e){}
		with(Math){
		r1=Number(arg1.toString().replace(".",""));
		r2=Number(arg2.toString().replace(".",""));
		return (r1/r2)*pow(10,t2-t1);
		}
	}
	function accMul(arg1,arg2)
	{
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length;}catch(e){}
		try{m+=s2.split(".")[1].length;}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}
	Number.prototype.mul = function (arg){
		return accMul(arg, this);
	};
	
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}
		try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
		m=Math.pow(10,Math.max(r1,r2));
		return (arg1*m+arg2*m)/m;
	}
	
	Number.prototype.add = function (arg){
		return accAdd(arg,this);
	};
	
	
	Number.prototype.div = function (arg){
		return accDiv(this, arg);
	};
	
	function Subtr(arg1,arg2){
		var r1,r2,m,n;
		try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}
		try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
		m=Math.pow(10,Math.max(r1,r2));
	     //last modify by deeka
		 //动态控制精度长度
		n=(r1>=r2)?r1:r2;
		return ((arg1*m-arg2*m)/m).toFixed(n);
	}
	Number.prototype.substr = function (arg){
		return Subtr(this, arg);
	};
	
		$(".jsqks").click( function() {
				var $envs = $(".jejs");
				//借款期限
				var month = $envs.eq(3).val();
			    if($(".prompt:visible").length > 0){
					return;
				}

				//借款金额
				var toal = $envs.eq(0).val();

			  	//借款年化利率
			  	var year = $envs.eq(1).val();
			  	//还款方式
			  	var type = $("input[name='select']").val();
			  	//月利率
			  	var yln = parseFloat(year)/12;
			  	//最小借款金额
			  	var min_amount = $("#min_amount").val();
			  	
				if($.trim(toal) == "" || $.trim(month) == "" || $.trim(year) == "0"){
			  		return;
			  	}
				
				/*if(toal < 100 || toal % 100 != 0) {
			  		return;
			  	}*/
				
				if(year < 1 || year > 24) {
					return;
				}
			  	
				var myreg = /^[0-9]([0-9])*$/;
				var yeareg=/^\d+(|\d|(\.[0-9]{1,2}))$/;
				if(!myreg.test(toal)){
					return;
				}
				if(!yeareg.test(year)){
					return;
				}
				$("#showjs").show();
				if(type == "DEBX"){
					yln = yln/100;
					//月还本息
					//power（1+x,n）*x*y/ (power（1+x,n）-1)
					var yhbx = ((Math.pow(1+yln, month)*yln*parseInt(toal))/(Math.pow(1+yln, month)-1)).toFixed(2);
					$("#mthns").html(month+"个月");
					$("#mtoal").html(toal);
						   
					//应收利息
					var yslxh = 0;
					$(".jstr").html("");
					var content='<tr>';
						content+='<td align="center">期次</td>';
						content+='<td align="center">应收本息(元)</td>';
						content+='<td align="center">应收利息(元)</td>';
						content+='<td align="center">应收本金(元)</td>';
						content+='<td align="center">剩余本金(元)</td>';
						content+='</tr>';
							 
					//已还本金之和
					var totalYhbj = 0;
					var dsbj = 0;
					var yhbj = 0;
					var  interest_cur = 0;
					var zhdhbj = toal;
					for(var i=1;i<=month;i++){
						//应还利息
						interest_cur = parseFloat(Subtr(toal,totalYhbj)*yln).toFixed(2);
						//应还本金
						yhbj = parseFloat(Subtr(yhbx, interest_cur)).toFixed(2);

						totalYhbj =accAdd(totalYhbj, yhbj);
						//待收本金
						dsbj  = parseFloat(Subtr(toal, totalYhbj)).toFixed(2);

						if(i == (month-1)){
							zhdhbj =  dsbj;
						}
						if(i == month){
							yhbj = zhdhbj;
							 //应还利息
							interest_cur = parseFloat(Subtr(yhbx, yhbj)).toFixed(2);
							if(interest_cur < 0){
								interest_cur = parseFloat(0).toFixed(2);
							}
							totalYhbj =accAdd(totalYhbj, yhbj);
							dsbj  = parseFloat(0).toFixed(2);
						}

						content+='<tr>';
						content+='<td align="center">'+i+'</td>';
						content+='<td align="center" id="ysbx'+i+'">'+yhbx+'</td>';
						content+='<td align="center" id="yslx'+i+'">'+interest_cur+'</td>';
						content+='<td align="center" id="ysbj'+i+'">'+yhbj+'</td>';
						content+='<td align="center" id="dsbj'+i+'">'+dsbj+'</td>';
						content+='</tr>';
						yslxh=accAdd(yslxh, interest_cur);
					}

					$("#ghbx").html(yslxh.toFixed(2));
					$(".jstr").append(content);
			 
					}else if(type == "DEBJ"){
						//月利息
					    $("#mthns").html(month+"个月");
					    $("#mtoal").html(toal);


						$(".jstr").html("");
						var content='<tr>';
						    content+='<td align="center">期次</td>';
							content+='<td align="center">应收本息(元)</td>';
							content+='<td align="center">应收利息(元)</td>';
							content+='<td align="center">应收本金(元)</td>';
							content+='<td align="center">剩余本金(元)</td>';
							content+='</tr>';

						var new_ysbx = 0;
						var new_yhbj = 0;
						var new_yslx = 0;
						var new_sybj = 0;
						var yslxTotle = 0;
						var ysbjTotle = 0;
						new_ysbj = parseInt(toal).div(month);
						for(var i=1;i<=month;i++){
							new_yslx = (toal - ysbjTotle)*(yln/100);
							ysbjTotle = ysbjTotle + new_ysbj;
							new_ysbx = new_yslx + new_ysbj;
							new_sybj = toal - new_ysbj*i;

							if( i == month - 1)
							{
								new_sybj = toal - new_yhbj - new_ysbj.toFixed(2);
							}
							if(i == month){
								new_ysbj = toal - new_yhbj;
								new_yslx = new_ysbj*(yln/100);
								new_ysbx = new_yslx + new_ysbj;
							}

							content+='<tr>';
							content+='<td align="center">'+i+'</td>';
							content+='<td align="center" id="ysbx'+i+'">'+new_ysbx.toFixed(2)+'</td>';
							content+='<td align="center" id="yslx'+i+'">'+new_yslx.toFixed(2)+'</td>';
							content+='<td align="center" id="ysbj'+i+'">'+new_ysbj.toFixed(2)+'</td>';
							content+='<td align="center" id="dsbj'+i+'">'+new_sybj.toFixed(2)+'</td>';
							content+='</tr>';

							yslxTotle = accAdd(yslxTotle,new_yslx);
							new_yhbj = accAdd(new_yhbj,new_ysbj.toFixed(2));
						}

						$("#ghbx").html(yslxTotle.toFixed(2));
						$(".jstr").append(content);
					}else if(type == "YCFQ_M" || type == "YCFQ_D"){
					    //还利息
					    var yslx = 0;
						if(type == "YCFQ_M"){
							$("#mthns").html(month+"个月");
							yslx = (yln * parseInt(toal) * month)/100;
						}else{
							$("#mthns").html(month+"天");
							yslx = (year * parseInt(toal) * month)/100/days_of_year;
						}
					   $("#mtoal").html(toal);
					   $("#ghbx").html(yslx.toFixed(2));

					 $(".jstr").html("");
					 var content='<tr>';
						 content+='<td align="center">期次</td>';
						 content+='<td align="center">应收本息(元)</td>';
						 content+='<td align="center">应收利息(元)</td>';
						 content+='<td align="center">应收本金(元)</td>';
						 content+='<td align="center">剩余本金(元)</td>';
						 content+='</tr>';

					/* for(var i=1;i<=month;i++){
						 var new_ysbx = 0;
						 var new_ysbj = 0;
						 var new_yslx = 0;
						 var new_sybj = parseInt(toal);*/
						/* if(i == month){*/
							 new_yslx = yslx;
							 new_ysbj = parseInt(toal);
							 new_ysbx = new_yslx + new_ysbj;
							 new_sybj = 0;
						 /*}*/

						content+='<tr>';
						content+='<td align="center">'+1+'</td>';
						content+='<td align="center" id="ysbx'+month+'">'+new_ysbx.toFixed(2)+'</td>';
						content+='<td align="center" id="yslx'+month+'">'+new_yslx.toFixed(2)+'</td>';
						content+='<td align="center" id="ysbj'+month+'">'+new_ysbj.toFixed(2)+'</td>';
						content+='<td align="center" id="dsbj'+month+'">'+new_sybj.toFixed(2)+'</td>';
						content+='</tr>';
					 /*}*/
					 $(".jstr").append(content);
					}else if(type == "MYFX"){
					   //月还本息
					   var yslx = (yln * parseInt(toal))/100;
					   //总利息
					   var total_lx = parseInt(toal) * parseFloat(yln) * parseInt(month) /100;
					   $("#mthns").html(month+"个月");
					   $("#mtoal").html(toal);
					   var yslxTotle = 0;
					   var totalExlastLx =total_lx;

					 $(".jstr").html("");
					 var content='<tr>';
						 content+='<td align="center">期次</td>';
						 content+='<td align="center">应收本息(元)</td>';
						 content+='<td align="center">应收利息(元)</td>';
						 content+='<td align="center">应收本金(元)</td>';
						 content+='<td align="center">剩余本金(元)</td>';
						 content+='</tr>';

					 for(var i=1;i<=month;i++){
						 var new_ysbx = 0;
						 var new_ysbj = 0;
						 var new_yslx = 0;
						 var new_sybj = parseInt(toal);
						 new_yslx = yslx;
						 new_ysbx = new_yslx + new_ysbj;
						 if(i == month){
							 new_ysbj = parseInt(toal);
							 new_sybj = 0;
							 new_ysbx = totalExlastLx + new_ysbj;
						 }
						 if(i < month){
							 totalExlastLx = totalExlastLx - new_yslx.division(2);
						 }

						content+='<tr>';
						content+='<td align="center">'+i+'</td>';
						 if(i == month) {
							 content+='<td align="center" id="ysbx'+i+'">'+new_ysbx.division(2)+'</td>';
							 content += '<td align="center" id="yslx' + i + '">' + totalExlastLx.toFixed(2) + '</td>';
						 }else{
							 content+='<td align="center" id="ysbx'+i+'">'+new_ysbx.division(2)+'</td>';
							 content += '<td align="center" id="yslx' + i + '">' + new_yslx.division(2) + '</td>';
						 }
						content+='<td align="center" id="ysbj'+i+'">'+new_ysbj.toFixed(2)+'</td>';
						content+='<td align="center" id="dsbj'+i+'">'+new_sybj.toFixed(2)+'</td>';
						content+='</tr>';

						yslxTotle  = total_lx;//accAdd(yslxTotle,new_yslx.toFixed(2));

					 }

					 $("#ghbx").html(yslxTotle.toFixed(2));
					 $(".jstr").append(content);

					}
		});

	//分割字符串，精确到指定位数
	Number.prototype.division = function(d)
	{
		var s=this+"";
		if(!d)d=0;
		var index = s.indexOf(".");
		if(index ==-1){
			s+=".";
			s+=new Array(d+1).join("0");
			return s;
		}
		if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+ (d+1) +"})?)\\d*$").test(s))
		{
			s = s.substr(0, index+3);
		}
		return s;
	};

	//还款方式改变事件
	/*$("#select").change(function(){
		var jkqx_tip = $("#jkqx").nextAll("p[tip]");
		var jkqx_error = $("#jkqx").nextAll("p[errortip]");
		if($("input[name='select']").val()=='YCFQ_D'){
			jkqx_tip.html("借款期限为1-"+days_of_year+"天");
			$("#company").html("天")
			$("#jkqx").removeClass("max-size-36").addClass("max-size-"+days_of_year);
		}else{
			jkqx_tip.html("借款期限为1-36个月");
			$("#jkqx").removeClass("max-size-"+days_of_year).addClass("max-size-36");
			$("#company").html("个月")
		}
		jkqx_tip.show();
		jkqx_error.hide().val("");
	});*/
$(function(){
	$('#select').selectlist({
		width: 372,
		height: 36,
		onChange: function(){
			var jkqx_tip = $("#jkqx").nextAll("p[tip]");
			var jkqx_error = $("#jkqx").nextAll("p[errortip]");
			if($("input[name='select']").val()=='YCFQ_D'){
				jkqx_tip.html("借款期限为1-"+days_of_year+"天");
				$("#company").html("天")
				$("#jkqx").removeClass("max-size-36").addClass("max-size-"+days_of_year);
			}else{
				jkqx_tip.html("借款期限为1-36个月");
				$("#jkqx").removeClass("max-size-"+days_of_year).addClass("max-size-36");
				$("#company").html("个月")
			}
			jkqx_tip.show();
			jkqx_error.hide().val("");
		}
	});
});	
	