define(['text!indexTemplate/index/appDownload.html', 
		], function(appDownloadTemplate) {
	var appDownloadView = DMJS.DMJSView.extend({ //债权投资
		id: 'appDownload',
		name: 'appDownload',
		tagName: 'div',
		className: "",
		events: {
			'tap #download': 'download'
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
		},
		render: function() {
			var self = this;
            self.$el.html(_.template(appDownloadTemplate, self)); // 将tpl中的内容写入到 this.el 元素中  
			return this;
		},
		"download":function(){
			window.location.href="/app/downloadApp.htm";
		}
	});

	return appDownloadView;
});