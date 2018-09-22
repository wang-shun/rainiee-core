

define([], function(){
    var otherModel = DMJS.DMJSModel.extend({
        defaults: {
        },
        'commonData':{},
      //站内信
        getLetter:function(param){
        	var urlKey="other.letterList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //读取站内信
        readLetter:function(param){
        	var urlKey="other.readLetter";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        }
        
        
        
    });
    return otherModel;
});
