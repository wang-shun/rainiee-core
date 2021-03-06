<%@page import="com.dimeng.p2p.front.servlets.ClearAssociatedFlag"%>
<%@page import="com.dimeng.p2p.front.servlets.Logout"%>
<%@page import="com.dimeng.p2p.account.front.service.UserManage"%>
<%@page import="com.dimeng.p2p.front.servlets.VerifyCommon"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.dimeng.p2p.front.servlets.Login"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12"%>
<%@page import="com.dimeng.p2p.S50.entities.T5016"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>

<%
	PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
	DimengRSAPulicKey publicKey = (DimengRSAPulicKey)ptAccountManage.getPublicKey();
	String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
	String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
	T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.PC.name());
    int loginFailCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.LOGIN_FAIL_COUNT));
    boolean isShowVcode = IntegerParser.parse(request.getSession().getAttribute("exceptionCount")) >= loginFailCount;
	if (advertisements != null && advertisements.length > 0) {
%>
<!--banner-->
<div class="banner clearfix">
    <ul class="rslides">
    <%for(T5016 advertisement : advertisements){if (advertisement == null) {continue;}%>
        <li style="background:url(<%=fileStore.getURL(advertisement.F05)%>) center 0 no-repeat;">
        <%if(!StringHelper.isEmpty(advertisement.F04)){%>
        <a target="_blank" href="http://<%StringHelper.filterHTML(out, advertisement.F04.replaceAll("http://", "").replaceAll("https://", ""));%>"></a>
		<%}%>
        </li>
     <%}%>
    </ul> 
    <form name="homeLoginForm" action="<%=controller.getURI(request, Login.class)%>" method="post" onsubmit="return onHomeSubmit();">
     <div class="home_login">
    	<div class="top">
        	<div class="til">最高年化利率 <span class="f22 bold"><%configureProvider.format(out, SystemVariable.INDEX_SHOW_POUNDAGE);%></span></div>
        </div>
        <%if(dimengSession ==null || !dimengSession.isAuthenticated()){ 
            String _accountName = null;
			Cookie[] _cookies = request.getCookies();
			if (_cookies != null) {
				for (Cookie cookie : _cookies) {
					if (cookie == null) {continue;}
					if ("ACCOUNT_NAME".equals(cookie.getName())) {_accountName = URLDecoder.decode(cookie.getValue(),resourceProvider.getCharset());
					}
				}
			}
        %>
        <div class="home_con">

            <div class="form">
                <div class="input_focus clearfix">
                    <div class="icon_input name_input">
                    	<span class="icon"></span>
                    	<input type="text" id="txtUserId" maxlength="30" value="<%StringHelper.filterHTML(out, _accountName);%>" autofocus placeholder="用户名/邮箱/手机号"/>
                    </div>
                    <input id="userId" name="accountName" type="hidden" />
                </div>
                <div class="tips" id="userNameError"></div>
                <div class="input_focus clearfix">
                    <div class="icon_input password_input">
                    	<span class="icon"></span>
                    	<input name="" type="password" maxlength="20"  id="txtpasswordId" placeholder="请输入密码" autofocus autocomplete="off"/>
                    </div>
					<input id="passwordId" name="password" type="hidden" />
                </div>
                <div class="tips" id="passWordError"></div>
                <%{
					if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        if(isShowVcode){
				%>
                <div class="input_focus clearfix">
                    <div class="icon_input code_input">
                    	<input name="verifyCode" type="text" class="input code" id="code" style="width:130px;" placeholder="验证码" maxlength="<%=systemDefine.getVerifyCodeLength() %>" autofocus/>
                    </div>
                    <img alt="验证码" id="_verifyImg" src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=login" class="yzm" title="点击刷新" width="95" height="42" onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=login'" style="cursor: pointer;" />
                </div>
	            <%}}%>
				<div class="tips" id="verifyCodeError">
                <%{String errorMessage = controller.getPrompt(request,response,PromptLevel.ERROR);if (!StringHelper.isEmpty(errorMessage)) {%>
	             	<%=errorMessage %>
	            <%}}%>
                </div>
				<%}%>
                <div class="keep"><input name="remember" type="checkbox" value="1" <%=StringHelper.isEmpty(_accountName) ? "": "checked=\"checked\""%>/> 记住用户名</div>
            </div>
            <div class="clearfix mt5">
            	<div class="fl mt5"><input name="" type="submit" class="btn" value="立即登录" /></div>
            	<div class="fr tr">
                	<p><a href="<%configureProvider.format(out, URLVariable.GET_PASSWORD);%>" class="white">忘记密码？</a><a href="<%configureProvider.format(out, URLVariable.REGISTER);%>" class="highlight2">免费注册</a></p>
                    <p class="other_login mt5" >
                    	<span class="white" style="font-size:11px;">其他方式 </span>
                    	<a id="qqLogin" href="https://graph.qq.com/oauth2.0/authorize?client_id=<%configureProvider.format(out,SystemVariable.INDEX_QQ_KEY);%>&response_type=token&scope=all&redirect_uri=<%configureProvider.format(out,URLVariable.INDEX_QQ_LOGIN);%>" class="qq"></a>
                    	<a href="javascript:void(0)" class="wb" onclick="javascript:document.getElementById('tpa_login_sina').click();"></a>
                    	<a href="javascript:void(0);" class="wx wx_pic" id="wx_login_btn"></a>
                    	<div class="fl" style="margin-left: 5px;display:none;">
                    		<wb:login-button type="3,2" id="tpa_login_sina" onlogin="login" onlogout="logout"></wb:login-button>
                    	</div>
                    </p>
                </div>
            </div>

            <!-- <div class="weChat_port" style="display:none;">
	           	<div class="tr"><a href="javascript:void(0);" class="other_port">其他登录方式</a></div>
	           	<div class="clearfix home_code" id="wx_login_container"></div>
	               <p class="f16 white tc pt20">请使用微信扫描二维码<br>登录您的账户</p>
	    	</div> -->
        </div>
        <%}else{%>
        <div class="home_con tc">
            <div class="f18 mt30"><%configureProvider.format(out, SystemVariable.SITE_NAME);%>欢迎您！</div>
            <div class="f16 mt5">您当前账户：<span class="name highlight2"><%=serviceSession.getService(UserManage.class).getAccountName() %></span></div>
            <div class="mt30 mb30"><a href="<%configureProvider.format(out,URLVariable.USER_INDEX);%>" class="btn btn2 mr5">管理我的账户</a> <a href="<%=controller.getURI(request, Logout.class)%>" onclick="DMQQLogout()" class="highlight2">退出登录</a></div>
             <p class="other_login mt5" id="qqLoginBtn" style="display: none;"><a id="qqLogin" href="https://graph.qq.com/oauth2.0/authorize?client_id=<%configureProvider.format(out,SystemVariable.INDEX_QQ_KEY);%>&response_type=token&scope=all&redirect_uri=<%configureProvider.format(out,URLVariable.INDEX_QQ_LOGIN);%>" class="qq"></a>
            </p> 
            <div class="fl" style="margin-left: 5px;display:none;"><wb:login-button type="3,2" id="tpa_login_sina" onlogin="login" onlogout="logout"></wb:login-button></div>
        </div>
        <%} %>
    </div>
    </form>
</div>
<!--banner-->

<script type="text/javascript">
jQuery.fn.placeholder = function(){
	var i = document.createElement('input'),
		placeholdersupport = 'placeholder' in i;
	if(!placeholdersupport){
		var inputs = jQuery(this);
		inputs.each(function(){
			var input = jQuery(this),
				text = input.attr('placeholder'),
				pdl = 0,
				height = input.outerHeight(),
				width = input.outerWidth(),
				placeholder = jQuery('<span class="phTips">'+text+'</span>');
			try{
				pdl = input.css('padding-left').match(/\d*/i)[0] * 1;
			}catch(e){
				pdl = 5;
			}
			placeholder.css({'margin-left': -(width-pdl),'height':height,'line-height':height+"px",'position':'absolute', 'color': "#cecfc9", 'font-size' : "14px"});
			placeholder.click(function(){
				input.focus();
			});
			if(input.val() != ""){
				placeholder.css({display:'none'});
			}else{
				placeholder.css({display:'inline'});
			}
			placeholder.insertAfter(input);
			input.keyup(function(e){
				if(jQuery(this).val() != ""){
					placeholder.css({display:'none'});
				}else{
					placeholder.css({display:'inline'});
				}
			});
		});
	}
	return this;
};
$(function(){
	jQuery('input[placeholder]').placeholder();
	$("input").keypress(function(event){
		event = event || window.event;
		if(event.keyCode==13) {
			$("form[name='homeLoginForm']").submit();
		}
	  });
	$("#qqLogin").click(function(){
		$.post("<%=controller.getURI(request, ClearAssociatedFlag.class)%>",{},function(returnData){});
	});
});
var isNull = /^[\s]{0,}$/;
var verify = /^\d{<%=systemDefine.getVerifyCodeLength() %>}$/;
var loginName = /^[a-z]([\w]*)$/i;
var testChinese = /.*[\u4e00-\u9fa5]+.*$/;

var appid= "<%configureProvider.format(out,SystemVariable.WX_APPID);%>";
var redirectUri = "<%configureProvider.format(out,URLVariable.USER_INDEX);%>wechat/wxCallback.htm";
var sessionId = "<%=serviceSession.getSession().getToken().replaceAll("-", "")%>";

$("#wx_login_btn").click(function(){
	window.location.href="https://open.weixin.qq.com/connect/qrconnect?appid="+appid+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_login&state="+sessionId+"#wechat_redirect";
});

function accountCheck() {
	var val = $("#txtUserId").val();
	var p = $("#userNameError");
	if (isNull.test($.trim(val))) {
		p.html("用户名不能为空");
		p.addClass("tips");
		return false;
	}else if(testChinese.test(val)){
        p.html("用户名不能为中文");
		p.addClass("tips");
        return false;
    }
	/* else if(!loginName.test(val)){
		 p.html("用户名必须为字母、数字、下划线，且以字母开头");
		 p.addClass("red");
		 return false; 
	 } */
	return true;
}
function passwordCheck() {
	var val = $("#txtpasswordId").val();
	var p = $("#passWordError");
	if (isNull.test(val)) {
		p.html("密码不能为空");
		p.addClass("tips");
		return false;
	}
	return true;
}
function verifyCheck() {
	//是否需要验证码.
	var flag = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
    var isShowVcode = '<%=isShowVcode%>';
	if((flag != "undefined" && flag == false) || isShowVcode=='false'){
		return true;
	}
	var val = $("input[name='verifyCode']").val();
	var p = $("#verifyCodeError");
	if (isNull.test(val)) {
		p.html("验证码不能为空");
		p.addClass("tips");
		return false;
	} else if (!verify.test(val)) {
		p.html("您输入的验证码有误");
		p.addClass("tips");
		return false;
	}
	return true;
}
function onHomeSubmit() {
	if(accountCheck() && passwordCheck() && verifyCheck()){
		var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
		var key = RSAUtils.getKeyPair(exponent, '', modulus);  
		$("#userId").val(RSAUtils.encryptedString(key,$.trim($("#txtUserId").val())));
		$("#passwordId").val(RSAUtils.encryptedString(key,$("#txtpasswordId").val()));
		return true;
	}else{
		return false;
	}
}
</script>
<script type="text/javascript">
            var DMLogon = <%=dimengSession != null && dimengSession.isAuthenticated()%>;
            var FromLogout = '<%=dimengSession.getAttribute("fromLogout")%>';
            var FromRegister = '<%=dimengSession.getAttribute("fromRegister")%>';
            if(FromLogout=="true"){
              //从退出跳转过来的时候退出QQ,完成整个流程
              QC.Login.signOut();
            }
            
            if(DMLogon){
            	$("#qqLogin").removeClass("fast_qq");
            	$("#qqLogin").css("display", "none");
            	$("#tpa_login_sina").css("display", "none");
            }
            
            function loginSubmit(){
                /* QC.Login.signOut(); */
                QC.Login.getMe(function (openId, accessToken) {
                  $("#openId").val(openId);
                  $("#accessToken").val(accessToken);
                  loginForm.submit();
                  if(opener) {
                    opener.location.reload();
                    self.focus();
                    self.close();
                  }
                });

            }

            function showQQInfo(data){
              //根据返回数据，更换按钮显示状态方法
              $("#qqLogo").css("display", "none");
              var dom = document.getElementById('qqLoginBtn'),
                  _logoutTemplate = [
                    //头像
                    '<span><img src="{figureurl}" class="qc_item figure"/></span>',
                    //昵称
                    '<span>{nickname}&nbsp&nbsp</span>',
                    //退出
                    '<span><a href="javascript:void(0);" style="background:0;width:51px;" onclick="QQlogout()">退出</a></span>'
                  ].join("");

              dom && (dom.innerHTML = QC.String.format(_logoutTemplate, {
                nickname: QC.String.escHTML(data.nickname),
                figureurl: QC.String.escHTML(data.figureurl)
              }));
              
              $("#qqLoginBtn").hide();
            }
            
            function QQlogout(){
            	QC.Login.signOut();
            	$("#qqLoginBtn").hide();
            }

            function DMQQShowInfo(){
              if (QC.Login.check()) {//如果已登录

                  var paras = {};

                  QC.api("get_user_info", paras)
                      .success(function(s){//成功回调
                        showQQInfo(s.data);
                      })
                      .error(function(f){//失败回调
                        alert("QQ登录获取用户信息失败！");
                      })
                      .complete(function(c){//完成请求回调
                      });

                }
            }

        function DMQQLogin(){
        	
            if (QC.Login.check()) {//如果已登录
              //系统已登录
              if(!DMLogon && FromRegister!='true') {
                loginSubmit();
              }else{

              var paras = {};

              QC.api("get_user_info", paras)
                  .success(function(s){//成功回调
                	  if(DMLogon){
	                	 $("#qqLogin").hide();
	                     showQQInfo(s.data);
                	  }
                  })
                  .error(function(f){//失败回调
                    alert("QQ登录获取用户信息失败！");
                  })
                  .complete(function(c){//完成请求回调
              });
              }
            } else {
            	$("#qqLogin").show();
            }
        }

          DMQQLogin();
          

          WB2.anyWhere(function (W) {
            W.widget.connectButton({
              id: "tpa_login_sina",
              type: "3,2",
              callback: {
                login: function (o) { //登录后的回调函数
                  var status = WB2.checkLogin();
                  if (status) {
                	QC.Login.signOut();  
                    WB2.logout();
                    $("#openId").val(o.id);
                    clearTime = setInterval(function () {
                       var status = WB2.checkLogin();
                      if (status) {
                        WB2.logout();
                      } else { 
                        clearInterval(clearTime);
                        loginForm.submit();
                      }
                    }, 1000);
                  }
                }
              }
            });
          });
           
</script>
<!-- 第三方登陆结束 -->

<%}%>