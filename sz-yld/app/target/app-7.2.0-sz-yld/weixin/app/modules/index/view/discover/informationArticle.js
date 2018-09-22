define(['text!indexTemplate/discover/informationArticle.html'], function(informationArticleTemplate){
    var informationArticleView = DMJS.DMJSView.extend({//项目投资
        id: 'informationArticleContent',
        name: 'informationArticleContent',
        tagName: 'div',
        className: "informationArticleContent", 
        events: {
            
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
            var self=this; 
           	self.article();
           	return this;
        },
        article:function(){
        	var self=this;
        	self.controller.indexModel.articleItem({
        		data:{id:self.data.articleId,type:self.data.type},
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                	self.data.tabType=self.data.type;
                    _.extend(self.data, response.data);
                    if(!response.data.from&&response.data.from!="") self.data.from="";
                    ///response.data
                    self.data.releaseTime=self.data.dateS;
                    self.data.content= self.data.content.replace(/\<a\s/g,'<a style="color:blue;" ');
                    self.$el.html(_.template(informationArticleTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
                    /*self.$el.find(img).each(function(){
                    	this.src=this.src
                    });*/
                    DMJS.CommonTools.imgDown.apply(self,[$('img')]);
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        },

    });

    return informationArticleView;
});



