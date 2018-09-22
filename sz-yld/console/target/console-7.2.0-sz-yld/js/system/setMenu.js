$(function(){
    $(".zoom-in-btn").click(function() {
        var str = $(this).text();
        if (str == "-") {
            $(this).text("+");
            $(this).addClass("zoom-in-btn-show");
            $(this).parents("tr").find(".td-item-container").css({"height" : 30 + "px","overflow" : "hidden"});
        } else {
            $(this).text("-");
            $(this).removeClass("zoom-in-btn-show");
            $(this).parents("tr").find(".td-item-container").css({"height" : 100 + "%","overflow" : "inherit"});
        }
    });
    $("#all").click(function(){
        var allObj = $("#all");
        var checkObjs = $(":checkbox").not("#all");
        if(allObj.attr("checked")) {
            checkObjs.attr("checked", "checked");
        } else {
            checkObjs.removeAttr("checked");
        }
    });

    function ischeckAll(){
        var totalCount = $("input[name=oneBox]").not("#all").length;
        var checkCount = $("input[name=oneBox]:checked").not("#all").length;
        if(totalCount > checkCount){
            $("#all").removeAttr("checked");
        }else{
            $("#all").attr("checked","checked");
        }
    }

    $("[name=oneBox]").click(function(){
        var myCheckbox=$(this).closest("td").next().find(":checkbox");
        if($(this).attr("checked")){
            myCheckbox.attr("checked","checked");
        }else{
            myCheckbox.removeAttr("checked");
        }
        ischeckAll();
    });
    $("[name=twoBox]").click(function(){
        //第一级选中
        var myCheckbox3=$(this).closest("td").parent().find("[name=oneBox]:checkbox");
        //第三级，第四级选中
        var myCheckbox=$(this).closest("div").parent().find("div[module='"+$(this).val()+"']").find(":checkbox");
        var twoCount = $(this).closest("div").parent().find("input[name='twoBox']").length;
        var twoCheckCount = $(this).closest("div").parent().find("input[name='twoBox']:checked").length;
        if($(this).attr("checked")){
            myCheckbox.attr("checked","checked");
            if(twoCount == twoCheckCount){
                myCheckbox3.attr("checked","checked");
            }
        }else{
            myCheckbox.removeAttr("checked");//第三级，第四级取消
            if(twoCheckCount < twoCount){//2级菜单"取消选中"是最后一个,3,4,1都取消
                myCheckbox3.removeAttr("checked");
            }
        }
        ischeckAll();
    });
    $("[name=threeBox]").click(function(){
        //第四级选中
        var myCheckbox=$(this).parent().next().find(":checkbox");
        //第二级选中
        var module = $(this).closest("div").attr("module");
        var myCheckbox2=$(this).closest("div").parent().find("[name=twoBox][value='"+module+"']:checkbox");
        //第一级选中
        var myCheckbox3=$(this).closest("td").parent().find("[name=oneBox]:checkbox");
        var threeCheckCount = $(this).closest("div").parent().find("div[module='"+module+"']").find("[name=threeBox]:checked").size();
        var threeCount = $(this).closest("div").parent().find("div[module='"+module+"']").find("[name=threeBox]").size();

        if($(this).attr("checked")){
            myCheckbox.attr("checked","checked");
            if(threeCount == threeCheckCount){
                myCheckbox2.attr("checked","checked");
            }
            var twoCheckCount = $(this).closest("div").parent().find("input[name='twoBox']:checked").length;
            var twoCount = $(this).closest("div").parent().find("input[name='twoBox']").length;
            if(twoCheckCount == twoCount){
                myCheckbox3.attr("checked","checked");
            }
        }else{
            myCheckbox.removeAttr("checked");//第四级取消
            if(threeCount > threeCheckCount){
                myCheckbox2.removeAttr("checked");
            }
            var twoCheckCount = $(this).closest("div").parent().find("input[name='twoBox']:checked").length;
            var twoCount = $(this).closest("div").parent().find("input[name='twoBox']").length;
            if(twoCheckCount < twoCount){
                myCheckbox3.removeAttr("checked");
            }
        }
        ischeckAll();
    });
    $("[name=rightIds]").click(function(){
        //第三级选中
        var myCheckbox=$(this).closest("div").siblings().find("[name=threeBox]:checkbox");
        //第二级选中
        var myCheckbox2=$(this).closest("div.td-item-container").find("input[name=twoBox][value='"+$(this).closest("div.item-container").attr("module")+"']");
        //第一级选中
        var myCheckbox3=$(this).closest("td").parent().find("[name=oneBox]:checkbox");

        var rightCheckCount = $(this).closest("ul").find("input:checked[name='rightIds']").length;
        var rightCount =  $(this).closest("ul").find("input[name='rightIds']").length;

        if($(this).attr("checked")){
            if(rightCheckCount == rightCount) {
                myCheckbox.attr("checked", "checked");
            }
            var threeCheckCount = $(this).closest("div").parent().find("input:checked[name='threeBox']").length;
            var threeCount = $(this).closest("div").parent().find("input[name='threeBox']").length;
            if(threeCheckCount == threeCount) {
                myCheckbox2.attr("checked", "checked");
            }
            var twoBoxCheckCount = $(this).closest("td").find("input:checked[name=twoBox]").length;
            var twoBoxCount = $(this).closest("td").find("div.td-title-container").length;
            if(twoBoxCheckCount == twoBoxCount){
                myCheckbox3.attr("checked","checked");
            }
        }else{
            if(rightCheckCount < rightCount){
                myCheckbox.removeAttr("checked");
            }
            var threeCheckCount = $(this).closest("div").parent().find("input[name='threeBox']:checked").length;
            var threeCount = $(this).closest("div").parent().find("input[name='threeBox']").length;
            if(threeCheckCount < threeCount){//4级菜单"取消选中"是最后一个,1,2,3都取消
                myCheckbox2.removeAttr("checked");
            }
            var twoBoxCheckCount = $(this).closest("td").find("input[name=twoBox]:checked").length;
            var twoBoxCount = $(this).closest("td").find("div.td-title-container").length;
            if(twoBoxCheckCount < twoBoxCount){
                myCheckbox3.removeAttr("checked");
            }
        }
        ischeckAll();
    });
    //修改加载页面触发第四级checkBox
    $('input:checkbox[name="threeBox"]').each(function(i){
    	var rightsCount = $(this).parent().next().children("ul").children("li").length;
    	var rightCheckCount = $(this).parent().next().children("ul").find("input:checked[name='rightIds']").length;
    	//第三级选中
    	if(rightsCount==rightCheckCount){
    		$(this).attr("checked","checked");
    		//第二级选中
            var myCheckbox2=$(this).closest("div.td-item-container").find("input[name='twoBox'][value='"+$(this).closest("div.item-container").attr("module")+"']");
        	var threeCheckCount = $("div[module='"+myCheckbox2.val()+"']").find("input[name='threeBox']:checked").length;
            var threeCount = $("div[module='"+myCheckbox2.val()+"']").find("input[name='threeBox']").length;
            if(threeCheckCount == threeCount) {
            	myCheckbox2.attr("checked", "checked");
            	//第一级选中
                var myCheckbox3=$(this).closest("td").parent().find("[name='oneBox']:checkbox");
            	var twoBoxCheckCount = $(this).closest("td").find("input[name='twoBox']:checked").length;
                var twoBoxCount = $(this).closest("td").find("div.td-title-container").length;
                if(twoBoxCheckCount == twoBoxCount){
                    myCheckbox3.attr("checked","checked");
                    ischeckAll();
                }
            } 
    	}
    });
});
function check() {
    var checked = false;
    var checkBoxs = $(".rightId");
    for (var i = 0; i <= checkBoxs.length - 1; i++) {
        if ($(checkBoxs[i]).attr("checked") == "checked") {
            checked = true;
            break;
        }
    }
    if (!checked) {
        alert("请选择需要设置的菜单.");
        return;
    }
    $("#form1").submit();
}