define(['text!indexTemplate/discover/discover.html'], function(discoverTemplate){
    var discoverView = DMJS.DMJSView.extend({
        id: 'discoverContent',
        name: 'discoverContent',
        tagName: 'div',
        className: "discoverContent", 
        events: {
            
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
            var self=this; 
            this.$el.html(_.template(discoverTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
            return this;
        },

    });

    return discoverView;
});



