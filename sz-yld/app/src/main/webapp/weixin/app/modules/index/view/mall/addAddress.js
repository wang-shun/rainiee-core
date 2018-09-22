define(['text!indexTemplate/mall/addAddress.html',
		'commonTool/validator',
		'commonClass/scroll/PTRScroll',
		'commonTool/tool',
	],
	function(addAddress,Validator,PTRScroll,tool) {
		var addAddressView = DMJS.DMJSView.extend({ //购物车
			id: 'addAddress',
			name: 'addAddress',
			tagName: 'div',
			className: "addAddress",
			events: {
				'tap #yesOrNoDiv': 'chargeBtn',
				'tap #editorInfo': 'editorInfo',
			},
			data : {
				userid: 0,
				regionID: 0,
				address: "",
				id: 0,
				name: "",
				phone: "",
				vo: "",
				yesOrNo: "",
				sheng: "",
				shi: "",
				xian: "",
			},
			init: function(options) {
				var self = this;
				_.extend(this, options);
			},
			render: function() {
				var self = this;
				if(self.options.addressId && self.options.addressId != ""){
					self.controller.indexModel.getAddressById({
		     			'data': {
		     				'id':self.options.addressId
		     			},
						"success": function(_data) {
							if(_data.code == "000000"){
								self.data = _data.data;
								var add = self.data.region.split(",");
								self.data.sheng = add[0];
								self.data.shi = add[1];
								self.data.xian = add[2];
								if(!self.data.vo){
									self.data.vo="";
								}
								self.$el.html(_.template(addAddress, self.data)); // 将tpl中的内容写入到 this.el 元素中  
								$("#address").val(self.data.address);
								self.selectProvince('SHENG'); //得到所有的省
								self.selectCity(self.data.regionID.toString().substring(0,2)+'0000','SHI'); //得到所有的省
								self.selectCounty(self.data.regionID.toString().substring(0,4)+'00','XIAN'); //得到所有的省
								
							}else{
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
							}
						},
					});
				}else{
					self.$el.html(_.template(addAddress, self.data)); // 将tpl中的内容写入到 this.el 元素中  
					self.selectProvince('SHENG'); //得到所有的省
				}
				
			},
	        toModifyAddress:function(e){
	        	var $dom=$(e.target);
			
                if(!$dom.is("#addressList > div")){
                    $dom=$dom.parents("#addressList > div");
                }
                var addressId = $dom.attr("addressId");
                if(addressId){
                	DMJS.router.navigate("index/index/addAddress/"+addressId, true);
                }
	        },
	        'chargeBtn' : function(e){//切换积分和金额支付按钮
				var self = this;
				var $dom=$(e.target);
				if($dom.hasClass('b_image-dragN')){//使用积分的按钮样式是灰色
					$dom.removeClass('b_image-dragN');
					$dom.addClass('b_image-dragY');
				}else{
					$dom.removeClass('b_image-dragY');
					$dom.addClass('b_image-dragN');
				}
				
			},
			selectProvince:function(type){//得到省
	        	var self=this;
	               self.controller.commonModel.searchAddress({
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
	        	//如果是双乾 就注释下面
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
	               self.controller.commonModel.searchAddress({
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
	               self.controller.commonModel.searchAddress({
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
	                },
					'error':function(response){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
	            });
	
	        },
	        editorInfo:function(){
	        	var self =this;
	        	if(self.options.addressId && self.options.addressId != ""){
	        		self.updateAddress(self.options.addressId)
	        	}else{
	        		self.addAddress();
	        	}
	        },
	     	addAddress:function(e){//增加收货地址
	     		console.log("增加地址")
	     		var self = this;
	     		var parms=$("#"+self.id).getFormValue();
        		if(!Validator.check($("#"+self.id))){return false;}
	     		if(self.data.XIAN==undefined){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请选择完整的地址");return false;}
	     		if(parms.va){
	     			if(!/^[1-9]\d{5}(?!\d)$/.test(parms.va)){
	     				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "输入的邮政编码不符合规范");
	     				return false;
	     			}
	     		}
        		parms['xian']=self.data.XIAN;
        		parms['address']=$("#address").val();
        		if($("#yesOrNoDiv").hasClass("b_image-dragY")){
        			parms['yesOrNo'] = "yes";
        		}else{
        			parms['yesOrNo'] = "";
        		}
	     		self.controller.indexModel.addAddress({
	     			'data': parms,
					"success": function(_data) {
						if(_data.code == "000000"){
//							DMJS.router.navigate("index/index/addressManager", true);
							tool._Navi_default("index/index/addressManager");
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
						}
					},
				});
	     		
	     	},
	     	updateAddress:function(id,e){//修改收货地址
	     		console.log("修改地址");
	     		var self = this;
	     		var parms=$("#"+self.id).getFormValue();
	     		if(self.data.XIAN==undefined){
	     			parms['xian'] = self.data.regionID;
	     		}else{
	     			parms['xian']=self.data.XIAN;
	     		}
	     		if($("#yesOrNoDiv").hasClass("b_image-dragY")){
        			parms['yesOrNo'] = "yes";
        		}else{
        			parms['yesOrNo'] = "";
        		}
        		if(!Validator.check($("#"+self.id))){return false;}
        		parms['id'] = self.data.id;
	     		self.controller.indexModel.updateAddress({
	     			'data': parms,
					"success": function(_data) {
						if(_data.code == "000000"){
							tool._Navi_default("index/index/addressManager", true);
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
						}
					},
				});
	     	},
		});
		return addAddressView;
	});