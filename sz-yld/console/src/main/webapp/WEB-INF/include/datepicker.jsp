<%@include file="/WEB-INF/include/jquery-ui.jsp"%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript">
	Date.prototype.Format = function(fmt)   
	{
	  var o = {   
	    "M+" : this.getMonth()+1,                 //月份   
	    "d+" : this.getDate(),                    //日   
	    "h+" : this.getHours(),                   //小时   
	    "m+" : this.getMinutes(),                 //分   
	    "s+" : this.getSeconds(),                 //秒   
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
	    "S"  : this.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(fmt))   
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(fmt))   
	  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return fmt;   
	}
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
		$.datepicker.setDefaults({
			dateFormat : 'yy-mm-dd',
			showButtonPanel : true,
			
			onSelect : function(input, inst) {
				this.value = input;
			}
		});
		
		//用来存放当前正在操作的日期文本框的引用。  
		var datepicker_CurrentInput = '';

		// 设置DatePicker 的默认设置  
		$.datepicker.setDefaults({
			showButtonPanel : true,
			closeText : '清空',
			beforeShow : function(input, inst) {
				datepicker_CurrentInput = input;
			}
		});

		// 绑定“Done”按钮的click事件，触发的时候，清空文本框的值  
		$(".ui-datepicker-close").live("click", function() {
			var index = $(".hasDatepicker").index(datepicker_CurrentInput);
			if(index == 0){
				var datepicker = $(".hasDatepicker").eq(1);
				if(datepicker != undefined){
		    		datepicker.datepicker("option", "minDate", null);
	    		}
			}
			datepicker_CurrentInput.value = "";
		});
	    
	    $(".ui-datepicker-current").live("click",function(){
	    	datepicker_CurrentInput.value = new Date().Format("yyyy-MM-dd");
	    	var index = $(".hasDatepicker").index(datepicker_CurrentInput);
	    	if(index == 0){
	    		var datepicker = $(".hasDatepicker").eq(1);
	    		if(datepicker != undefined){
		    		datepicker.datepicker("option", "minDate", new Date().Format("yyyy-MM-dd"));
		    		datepicker.focus().select();
	    		}else{
	    			$("body").focus().select();
	    		}
	    	}else{
	    		$("body").focus().select();
	    	}
	    });
	});
</script>