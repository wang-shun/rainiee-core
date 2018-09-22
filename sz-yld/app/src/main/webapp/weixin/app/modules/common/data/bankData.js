
define([], function(){
	var bankList_val=[1,2,3,4,5,6,7,8,9,10];
	var bankList_text=['中国银行','招商银行','工商银行','农业银行','平安银行','光大银行','华夏银行','建设银行','交通银行','测试银行'];
	var CardList_text=['中国银行 尾号9084','招商银行 尾号0977','工商银行 尾行6666'];
	var data={
 			getBankList:function(){
 				return {
 					'optionText':bankList_text,
 					'optionVals':bankList_text
 				}
 			},
 			getCardList:function(){
 				return {
 					'optionText':CardList_text,
 					'optionVals':CardList_text
 				}
 			}
 	};
	return data;
});
