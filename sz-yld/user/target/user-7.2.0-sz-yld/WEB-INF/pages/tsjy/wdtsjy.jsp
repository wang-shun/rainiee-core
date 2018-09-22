<%@ page import="com.dimeng.p2p.user.servlets.tsjy.Wdtsjy" %>
<%@ page import="com.dimeng.p2p.user.servlets.tsjy.SaveTsjy" %>
<%@ page import="com.dimeng.p2p.user.servlets.VerifyCommon" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<%configureProvider.format(out, SystemVariable.SITE_NAME);%>-我要吐槽
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "TSJY";
    CURRENT_SUB_CATEGORY = "WDTSJY";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <div class="user_mod">
                    <div class="user_til"><i class="icon"></i><span class="gray3 f18">我要吐槽</span></div>
                    <textarea name="content" id="content" cols="" rows="5" style="width:98%;" class="textarea_style"></textarea>
                    <p errortip class="red" style="display: none"></p>
                    <%
                        String verifyFlag = configureProvider.getProperty(SystemVariable.SFXYYZM);
                        if("true".equals(verifyFlag)){
                    %>
                    <div class="mt20">验证码：
                        <input name="verifyVal" id="verifyVal" type="text" class="text_style" maxlength="<%=systemDefine.getVerifyCodeLength() %>"/>
                        <img src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=tsjy" width="90" height="30" alt="验证码" title="点击刷新" class="border" height="34"
                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=tsjy'"
                             style="cursor: pointer;" id="verify_img">
                        <a href="javascript:void (0);" class="btn01 ml10" onclick="submit_adv()" id="submit_adv_id">提交</a>
                        <p errortip class="red" style="display: none"></p>
                    </div>
                    <%}else{%>
                    <div class="mt20">
                        <a href="javascript:void (0);" class="btn01 ml10" onclick="submit_adv()" id="submit_adv_id">提交</a>
                        <p errortip class="red" style="display: none"></p>
                    </div>
                    <%}%>
                </div>
                <div class="user_mod border_t15">
                    <div class="user_til"><i class="icon"></i><span class="gray3 f18 mr20">我的吐槽</span></div>
                    <div class="feedback_content">
                        <ul id="data_ul">  </ul>
                    </div>
                    <!--分页-->
                    <div id="pageContent">   </div>
                    <!--分页  --END-->
                </div>
            </div>
        <div class="clear"></div>
    </div>
</div>
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<script type="text/javascript">
    var currentPage = 1;
    var pageSize = 5;
    var pageCount = 0;
    var siteName = "<%=configureProvider.format(SystemVariable.SITE_NAME)%>";
    var url_wdtsjy = "<%=controller.getURI(request, Wdtsjy.class)%>";
    var url_saveTsjy = "<%=controller.getURI(request, SaveTsjy.class)%>";
    $(function(){
        initData();
        $("#content").bind("focus",function(){
            $("#content").nextAll("p[errortip]").hide().html("")
        });
        $("#content").bind("blur",function(){
            checkContent();
        });
        $("#verifyVal").bind("focus",function(){
            $("#submit_adv_id").nextAll("p[errortip]").hide().html("")
        });
    });

    function toAjaxPage(){
        initData();
    }
    //初始化
    function initData(){
        $.post(url_wdtsjy,{pageSize:pageSize,currentPage:currentPage},function(returnData){
            returnData = eval("("+returnData+")");
            pageCount = returnData.pageCount;
            var t6195_EXTs = eval(returnData.t6195_EXTs);
            if(t6195_EXTs != null && t6195_EXTs.length > 0){
                $("#pageContent").html(returnData.pageStr);
                var listr ="";
                $("#data_ul .item").remove();
                for(var i = 0; i < t6195_EXTs.length; i++){
                    var item = t6195_EXTs[i];
                    listr += "<li class=\"item data_li\">";
                    listr += "<div class=\"hd f16 gray3\">";
                    listr += "我："+item.F03+"</div>";
                    if(item.F06 == 'yes'){
                        listr += "<div class=\"bd\"><i class=\"arrow\"></i>"+siteName;
                        listr += "："+item.F05+"</div>";
                    }
                    listr += "</li>";
                }
                $(".data_li").remove();
                $(listr).appendTo("#data_ul");
            }else{
                var listr = "<li class=\"item\"><div style=\"text-align:center;\">暂无数据</div></li>";
                $(".data_li").remove();
                $("#pageContent").html("");
                $(listr).appendTo("#data_ul");
            }
            $("a.page-link").click(function(){
                pageParam(this);
            });
        });
    }

    function pageParam(obj){
        if($(obj).hasClass("cur")){
            return false;
        }
        currentPage = parseInt(currentPage);
        $(obj).addClass("cur");
        $(obj).siblings("a").removeClass("cur");
        if($(obj).hasClass("startPage")){
            currentPage = 1;
        }else if($(obj).hasClass("prev")){
            currentPage = parseInt(currentPage) - 1;
        }else if($(obj).hasClass("next")){
            currentPage = parseInt(currentPage) + 1;
        }else if($(obj).hasClass("endPage")){
            currentPage = pageCount;
        }else{
            currentPage = parseInt($(obj).html());
        }
        initData();
    }

    function checkContent(){
        var content =$.trim($("#content").val());
        var errortip =  $("#content").nextAll("p[errortip]");
        var pattern = new RegExp("[<>&]");
        if(pattern.test(content)){
            errortip.html("当前请求中存在非法字符，请重新输入").show();
            return false;
        }
        if(content == "") {
            errortip.html("反馈内容不能为空").show();
            return false;
        }
        if(content.length >150) {
            errortip.html("反馈内容超过输入限制150，当前长度为"+content.length).show();
            return false;
        }
        return true;
    }

    //提交投诉建议
    function submit_adv(){
    	var content =$.trim($("#content").val());
        if(!verifyCheck() || !checkContent()){return;}
        var verifyVal = $("#verifyVal").val();
        $.post(url_saveTsjy,{"content":content,"verifyVal":verifyVal},function(returnData){
        	try {
        		returnData = eval("("+returnData+")");
        		
        		//判断用户是否已经
    			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
            		return;
    			}
        		
                var erroMsg = returnData.erroMsg;
                var erroMsgDialog = returnData.erroMsgDialog;
                var saveMsg = returnData.saveMsg;
                if(erroMsg){
                    $("#submit_adv_id").nextAll("p[errortip]").show().html(erroMsg);
                    dealCode();
                }else if(erroMsgDialog){
                    $("#info").html(showDialogInfo(erroMsgDialog,"doubt"));
                    $("div.popup_bg").show();
                    dealCode();
                }else if(saveMsg){
                    $("#info").html(showDialogInfo(saveMsg,"successful"));
                    $("div.popup_bg").show();
                    $("#content").val("");
                    dealCode();
                    initData();
                }
			} catch (e) {
                $("#info").html(showDialogInfo("当前请求中存在非法字符，请重新输入","error"));
                $("div.popup_bg").show();
                dealCode();
			}
            
        });
    }

    function dealCode(){
    	$("#verify_img").trigger("click");
        $("#verifyVal").val("");
    }
    
    function verifyCheck() {
        //是否需要验证码.
        var flag = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
        if (flag != "undefined" && flag == false) {
            return true;
        }
        var isNull = /^[\s]{0,}$/;
        var verify = /^\d{<%=systemDefine.getVerifyCodeLength() %>}$/;
        var val = $("input[name='verifyVal']").val();
        var errortip = $("#submit_adv_id").nextAll("p[errortip]");;
        if (isNull.test(val)) {
            errortip.html("验证码不能为空").show();
            return false;
        } else if (!verify.test(val)) {
            errortip.html("您输入的验证码有误").show();
            return false;
        }
        return true;
    }
</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
</body>
</html>