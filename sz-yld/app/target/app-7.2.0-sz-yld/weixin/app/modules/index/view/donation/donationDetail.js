define(['text!indexTemplate/donation/donationDetail.html'], function(donationDetailTemplate){
    var donationDetailView = DMJS.DMJSView.extend({//项目投资
        id: 'donationDetailViewContent',
        name: 'donationDetailViewContent',
        tagName: 'div',
        className: "donationDetailViewContent", 
        events: {
            
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
            var self=this; 
           // console.log(this.data.donationId);
           self.donationDetail()
            return this;
        },
        donationDetail:function(){
        	var self=this;
				self.controller.indexModel.gyLoanItem({
					"data" : {bidId:self.data.donationId},'cancelLightbox':true,
					"success" : function(response) {
						if(!response.data.bidProgres){
							response.data.bidProgres="";
						}
						var progress = response.data.progress*100;
						if(progress<=1){
							response.data.progress = 1;
						}else if(progress<100&&progress>=90){
							response.data.progress = 99;
						}else{
							response.data.progress = progress.toFixed(2);
						}
					_.extend(self.data,response.data);
						  self.$el.html(_.template(donationDetailTemplate, response.data)); // 将tpl中的内容写入到 this.el 元素中  
						  DMJS.CommonTools.imgDown.apply(self,[$('img')]);
					},
					"error" : function(response) {
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					}
				});
        }

    });

    return donationDetailView;
});



