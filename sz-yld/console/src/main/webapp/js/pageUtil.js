var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

function pageSubmit(_obj){
	if(setCurrentPage(_obj)){
		toAjaxPage();
	}
}

//输入分页，点确定查询时，设置当前页
function setCurrentPage(_obj){
	var currentPage1 = $(_obj).prev().val();
	var re =  /^[1-9]+[0-9]*]*$/;
	if(!re.test(currentPage1)){
		return false;
	}
	currentPage = currentPage1;
	if(currentPage>$(_obj).prev().attr("maxSize")*1){
		currentPage = $(_obj).prev().attr("maxSize")*1;
	}
	currentPage = parseInt(currentPage);
	return true;
}

/**
 *分页查询请求
 * @param obj
 * @param liId
 * @returns {boolean}
 */
function pageParam(obj, liId) {
    if ($(obj).hasClass("selected")) {
        return false;
    }
    $(obj).addClass("selected");
    $(obj).siblings("a").removeClass("selected");
    if ($(obj).hasClass("startPage")) {
        currentPage = 1;
    } else if ($(obj).hasClass("previous")) {
        currentPage = parseInt(currentPage) - 1;
    } else if ($(obj).hasClass("next")) {
        currentPage = parseInt(currentPage) + 1;
    } else if ($(obj).hasClass("endPage")) {
        currentPage = parseInt(pageCount);
    } else {
        currentPage = parseInt($(obj).html());
    }
    toAjaxPage();
}