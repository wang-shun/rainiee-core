define(['text!indexTemplate/loan/loanPerson.html',
		'commonTool/validator'], function(loanPersonTemplate,Validator){
    var loanPersonView = DMJS.DMJSView.extend({
        id: 'loanPersonContent',
        name: 'loanPersonContent',
        tagName: 'div',
        className: "loanPersonContent", 
        events: {
        },
        init: function(options){
           	_.extend(this, options); 
           	 this.noDestroy=false;
        },
        data:{repeatedlyClick:0},
        render: function(){
            var self=this;
            this.$el.html(_.template(loanPersonTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中 
            self.selectProvince('SHENG'); //得到所有的省
             DMJS.agreementEnable('GRXX',"agreementFlag",function(){
             	if($('#agreementFlag').hasClass('uhide')){
					$('#agreementFlag').remove();
				}
             });
            self.typeRL();
            self.loadScroller();
            
            return this;
        },
        submit:function(){
        	var self=this;
        	console.log($("input[type='checkBox']").length)
        	if(!Validator.check($("#"+self.id))) {return false}
        	if(self.data.XIAN==undefined){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请选择完整的地址");return false;}
        	if(self.repeatedlyClick!=0&&(new Date).getTime()-self.repeatedlyClick<500){
			   return false;
		    }
        	self.repeatedlyClick = (new Date).getTime();
        	var params = self.$el.getFormValue();
        //	params['repaySource']=$("#repaySource").val();
        	params['loanDescription']=$("#loanDescription").val();
        	params['xian']=self.data.XIAN;
        	var arr=[];
        	$(".Jtype").each(function(){
        		if(this.checked){
        			arr.push('S');
        		}else{
        			arr.push('F');
        		}
        	});
        	var str=arr.join(",");
        	params['loanType']=str;
        	self.controller.indexModel.grSub({
               	"data":params,
               	async: true,'cancelLightbox':true,
        	    "success":function(response){
        	    	self.data.clickTime=false;
        	    	DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description,function(){
        	    		DMJS.router.navigate('index/index/index', true);
        	    	});
                },
				'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
            });
        },
        typeRL:function(){
        	var self=this;
        	    	//构建数据 ids; selectIds;lists;
        	    	var ids='yqck',selectIds='yqckS',list=[{value:"",text:"请选择"},{value:"7天之内",text:"7天之内"},{value:"15天之内",text:"15天之内"},{value:"15-30天",text:"15-30天"},{value:"1-3个月",text:"1-3个月"},{value:"其他",text:"其他"}];
    	    		//数据绑定
    	    		self.selectTip(ids,selectIds,list);
        },
        selectTip:function(ids,selectIds,list){
		var self=this;
		var optionVals=[],optionText=[];
			for(var i=0;i<list.length;i++){
				optionVals.push(list[i].value);
				optionText.push(list[i].text);
			}
			DMJS.CommonTools.showList({
                 'inputID':ids,
                 'id':selectIds,
                 'optionVals':optionVals,
                 'optionText':optionText
             },function(){});
            // $("#yqck").val("");
		
	},
	selectProvince:function(type){//得到省
        	var self=this;
               self.controller.commonModel.searchLoanAddress({
               	"data":{"type":type},
               	async: true,'cancelLightbox':true,
        	    "success":function(response){
                   var list=response.data;
                   var optionVals=[];
                   var optionText=[];
                   for(var i=0;i<list.length;i++){
                       optionVals[i]=list[i].id;
                       optionText[i]=list[i].sheng;
                   }
                   //1.inputId  2.selectId 3.select-Vlaue数组  4.select-Text数组  5.type 6.回调函数 7.是否执行 8是否重写func方法
                 self.select('selectProvince','province',optionVals,optionText,'SHI',self.selectCity,true);
                },
				'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
            });
        
        },
        select:function(inputSelect,selectList,optionVals,optionText,type,next,execute,func){//构建可滑动选择的下拉框
        	var self=this;
        	var fun=func||function(e){
        	var option=e[0].options[e[0].selectedIndex];
        	$("#"+inputSelect).html(option.text);
        	self.data.XIAN=option.value;
        	if(execute)next.apply(self,[option.value,type]);
        	if(type=="SHI"){
        		$('#selectCity').html("请选择");
        		$('#selectCounty').html("请选择");
        		$('#city').html("");
        		$('#county').html("");
        		self.data.XIAN=undefined
        	}
        	if(type=="XIAN"){$('#selectCounty').html("请选择");$('#county').html("");self.data.XIAN=undefined}
        	 };
        	 DMJS.CommonTools.showList({
                 'inputID':inputSelect,
                 'id':selectList,
                 'optionVals':optionVals,
                 'optionText':optionText
             },fun);

        },
       selectCity:function(id,type){
        	   var self=this;
               self.controller.commonModel.searchLoanAddress({
               "data":{provinceId:id,"type":type},
               async: true,'cancelLightbox':true,
        	    "success":function(response){
                   var list=response.data;
                   var optionVals=[];
                   var optionText=[];
                   for(var i=0;i<list.length;i++){
                       optionVals[i]=list[i].id;
                       optionText[i]=list[i].shi;
                   }
                 self.select('selectCity','city',optionVals,optionText,'XIAN',self.selectCounty,true);
                },
				'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
            });
        
        
       },
       selectCounty:function(id,type){
         	   var self=this;
               self.controller.commonModel.searchLoanAddress({
               	"data":{cityId:id,"type":'XIAN'},
               	async: true,'cancelLightbox':true,
        	    "success":function(response){
                   var list=response.data;
                   var optionVals=[];
                   var optionText=[];
                   for(var i=0;i<list.length;i++){
                       optionVals[i]=list[i].id;
                       optionText[i]=list[i].xian;
                   }
                 self.select('selectCounty','county',optionVals,optionText);
                 $('#county').val("");
                },
				'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
            });
      },
     "turnRememberRidoType": function(e) {
			/*if ($("input[type='checkbox']").is(":checked")) {
				$("input[type='checkbox']").removeAttr("checked");
			} else {
				$("input[type='checkbox']").attr("checked", true);
			}*/
			var parent=e.parent();
			if (parent.find("input[type='checkbox']").is(":checked")) {
				parent.find("input[type='checkbox']").removeAttr("checked");
			} else {
				parent.find("input[type='checkbox']").attr("checked", true);
			}
		},
		//条款
		"turnRememberRido": function(e) {
			if ($("#autid").is(":checked")) {
				$("#autid").removeAttr("checked");
			} else {
				$("#autid").attr("checked", true);
			}
		},
        agreement: function(type) {
			this.noDestroy = true;
			DMJS.router.navigate("user/personal/agreement/"+type, true);
		},
    });

    return loanPersonView;
});
