var flg = true;

$(function () {
    $submit = $(".sumbitForme");
    $intext = $('input[type="text"]');

    $submit.click(function () {
    	flg = true;
    	$intext = $('input[type="text"]');
        var cname = $(this).attr("fromname");
        $form_intext = $('.' + cname + ' input[type="text"]');

        $form_intext.each(function () {
            return checkText($(this));
        });
        
        if (!flg) {
            return false;
        }
    });
    
    $intext.focus(function () {
        $(this).blur(function () {
            return checkText($(this));
        });
    });

});

//校验输入文本框
function checkText($eve) {

    if ($eve.is(":hidden")) {
        return;
    }
    if ($eve == undefined) {
        return false;
    }
    if ($eve.attr("class") == undefined) {
        return false;
    }
    msg = $eve.attr("class").split(" ");
    var mtest = $eve.attr("mtest");
    value = $eve.val();
    $prompt = $eve.prev("span.prompt-container");

    for (var i = 0; i < msg.length; i++) {
        var temp = $.trim(msg[i]);
        if (temp.length > 0) {
            if (temp == "required") {
            	$prompt.remove();
                if ($.trim(value) == "") {
                	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>不能为空！</span></span>');
                	flg = false;
                    return false;
                }
            }

            if (temp == "isint") {
                var myreg = /^[0-9]([0-9])*$/;
                $prompt.remove();
                if ($.trim(value) == "") {
                    return false;
                }
                if (!myreg.test(value)) {
                	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>必须为整数！</span></span>');
                	flg = false;
                    return false;
                }
            }

            if (temp.indexOf('min-size') != -1) {
                tsizearry = temp.split("-");
                if (tsizearry.length >= 3) {
                    tsize = tsizearry[2];
                    $prompt.remove();
                    if (parseInt(value) < parseInt(tsize)) {
                    	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>小于最小范围值:'+tsize+'</span></span>');
                    	flg = false;
                        return false;
                    }
                }
            }
            if (temp.indexOf('max-size') != -1) {
                tsizearry = temp.split("-");
                if (tsizearry.length >= 3) {
                    tsize = tsizearry[2];
                    $prompt.remove();
                    if (parseInt(value) > parseInt(tsize)) {
                    	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>超出最大范围值:'+tsize+'</span></span>');
                    	flg = false;
                        return false;
                    }
                }
            }

            if (temp.indexOf('minf-size') != -1) {
                tsizearry = temp.split("-");
                if (tsizearry.length >= 3) {
                    tsize = tsizearry[2];
                    $prompt.remove();
                    if (parseFloat(value) < parseFloat(tsize)) {
                    	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>小于最小范围值</span></span>');
                    	flg = false;
                        return false;
                    }
                }
            }
            if (temp.indexOf('maxf-size') != -1) {
                tsizearry = temp.split("-");
                if (tsizearry.length >= 3) {
                    tsize = tsizearry[2];
                    $prompt.remove();
                    if (parseFloat(value) > parseFloat(tsize)) {
                    	$($eve).before('<span class="prompt-container"><i class="icon-i prompt-icon"></i><span>超出最大范围值:'+tsizearry[2]+'</span></span>');
                    	flg = false;
                        return false;
                    }
                }
            }
        }
    }
}
