<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
final ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
String url = configureProvider.format(SystemVariable.SITE_DOMAIN);

if (url.indexOf(protocolType) == -1)
{
   url = protocolType.concat(url);
}

final String userAgent = request.getHeader("user-agent").toLowerCase(Locale.ENGLISH);
   
boolean isAndroid = false;
boolean isIos = false;
//IOS
if (userAgent.indexOf("iphone") > -1)
{
   isIos = true;
} 
// android
else 
{
   isAndroid = true;
}

%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="email=no" name="format-detection" />
<meta content="telephone=no" name="format-detection" />
<title></title>
<link type="text/css" rel="stylesheet" href="css/main.css" />
</head>
<body>
	<ul class="list-type1" id='Janswer'>
	</ul>

	<div class="ub ub-f1 tab45" action='action:submit'
		style="width: 90%; margin: 1rem auto 1rem; border-radius: 5rem;">
		<div class="pos_re ub-f1 c-red sc-wid1">
			<div class="btn">立即评估</div>
		</div>
	</div>
	<div id="layer"
		style="width: 100%; position: fixed; top: 0; display: none;">
		<div class="ub ub-ac ub-pc" style="height: 100%;">
			<input type="hidden" name="resultCode" id="resultCode">
			<div
				style="width: 18rem; margin-left; background-color: white; border-radius: 6px; -webkit-border-radius: 6px;">
				<div class="ub ub-ac ub-pc"
					style="padding: 2rem 1rem; font-size: 1rem" id="content"></div>
				<div id="btn1" class="ub ub-ac ub-pc"
					style="padding: 1rem 1rem; border-top: 1px solid #EEEEEE; font-size: 1.3rem">确认</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="js/zepto.min.js"></script>
<script type="text/javascript">
if (! ('remove' in Element.prototype)) {
    Element.prototype.remove = function() {
        this.parentNode.removeChild(this);
    };
}

var layer = document.getElementById('layer');
layer.style.height = document.documentElement.clientHeight + "px";
layer.style.background = "rgba(32, 13, 13, 0.51)";

var content = document.getElementById('content');
var btn1 = document.getElementById('btn1');

var resultCode = document.getElementById('resultCode');

var isIos = <%=isIos %>;
var isAndroid = <%=isAndroid %>;

btn1.onclick = function() {
    layer.style.display = "none";

    if ( !! resultCode.value) {
        if (isIos) {
            finish();
        } else if (isAndroid) {
            window.jsInterface.finish();
        }
    }
}

var count = 0;
$('#Janswer').click(function(e) {
    var $dom = $(e.target);
    if ($dom.parents('div.Joption').length > 0) {
        $dom = $dom.parents('div.Joption');
    }
    if ( !! $dom.is('.Joption')) {
        $dom = $dom;
    } else {
        return false;
    }
    $dom.siblings().removeClass('lfx-gou');
    $dom.addClass('lfx-gou');
    var answerId = $dom.siblings('.JanswerId').attr('questionId');
    var option = $dom.attr('Joption');
    var answerOptioin = answerId + '_' + option;
    $dom.siblings('.JoptionAnswer').val(answerOptioin);
});

$(".btn").click(function() {

    var data = $('.JoptionAnswer'),
    params = [];
    for (var i = 0; i < data.length; i++) {
        if (!data[i].value) {
            layer.style.display = "block";
            content.innerHTML = "第" + (i + 1) + "题未做答, 请选择";
            return;
        }
        params.push(data[i].value);
    }

    params = params.join(',');
    jsonp.connect('<%=url%>' + '/app/user/riskAssessment.htm', new Date().getTime() + "&answer=" + params,
    function(response) {
        if (response && response.code == "000000" && response.data) {
            resultCode.value = response.code;
            layer.style.display = "block";
            content.innerHTML = "评估完成, 您的得分为" + response.data.score + "分，您的风险承受能力为:" + response.data.riskType;
        } else {
            layer.style.display = "block";
            content.innerHTML = response.description;
            resultCode.value = response.code;
        }
    },
    function(description) {
        layer.style.display = "block";
        content.innerHTML = description;
    });
});

var jsonp = {
    el: "",
    func: {},
    connect: function(url, parms, succ, erro) {
        try {
            var sc = document.createElement('script');
            sc.src = url + '?callback=getData&' + parms;
            document.body.appendChild(sc);
            el = sc;
            succ ? this.func['succ'] = succ: null;
            erro ? this.func['err'] = erro: null;
        } catch(e) {
            this.error();
        }
    },
    getData: function(reponse) {
        try {
            if ("" != el) {
                el.remove();
            }

            this.success(reponse);
        } catch(e) {
            this.error();
        }

    },
    success: function(reponse) {
        this.func['succ'] && this.func['succ'](reponse);
        this.func['succ'] = undefined;
    },
    error: function(reponse) {
        this.func['err'] && this.func['err'](reponse);
        this.func['err'] = undefined;
    }
}
function getData(reponse) {
    jsonp.getData(reponse);
}

(function() {
    jsonp.connect('<%=url%>' + '/app/user/riskQuestions.htm', new Date().getTime(),
    function(response) {
        if (response && response.code == "000000" && response.data) {
            var str = "";
            count = response.data.length;
            for (var i = 0; i < response.data.length; i++) {
                var data = response.data[i];
                str += '<li>' + '<input type="hidden" class="uhide JoptionAnswer" title="选项" validator="notEmpty;">' + '<dl class="mc  ub JanswerId" questionId="' + data.questionId + '">' + '<dt>' + (i + 1) + '.</dt>' + '<dd class="ub-f1">' + data.question + '</dd>' + '</dl>' + '<div class="mt ub Joption " Joption="A">' + '<p>A: </p>' + '<p class="ub-f1 lmg-r-20">' + data.answerA + '</p>' + '</div>' + '<div class="mt ub Joption " Joption="B">' + '<p>B: </p>' + '<p class="ub-f1 lmg-r-20">' + data.answerB + '</p>' + '</div>' + '<div class="mt ub Joption " Joption="C">' + '<p>C: </p>' + '<p class="ub-f1 lmg-r-20">' + data.answerC + '</p>' + '</div>' + '<div class="mt ub Joption " Joption="D">' + '<p>D: </p>' + '<p class="ub-f1 lmg-r-20">' + data.answerD + '</p>' + '</div>' + '</li>';
            }
            $('#Janswer').html(str);

        } else {
            jsonp.error();
        }
    },
    null);
})();
         
</script>
</html>
