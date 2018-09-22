<%@page import="com.dimeng.p2p.S50.enums.T5021_F07"%>
<%@page import="com.dimeng.p2p.S61.entities.T6196"%>
<%@page import="com.dimeng.p2p.front.servlets.yysj.OperationData"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic"%>
<%@page import="com.dimeng.p2p.repeater.policy.OperateDataManage"%>
<%@page import="com.dimeng.p2p.S50.entities.T5015"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.NoticeManage"%>
<%@page import="com.dimeng.p2p.front.servlets.AbstractFrontServlet"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Wzgg"%>
<%{
	NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
	PagingResult<T5015> noticeResult = noticeManage.search(AbstractFrontServlet.INDEX_PAGING);
	T5015[] notices = noticeResult.getItems();
    if(notices != null && notices.length > 0)
    {
%>
<div class="notice">
    <div class="wrap clearfix">
          <div class="til fl">
            <i></i>
            <span class="n_til">最新公告</span>
        </div>
         <div class="bd">
              <ul>
              <%if (notices != null) {for (T5015 notice : notices) {%>
                  <li>	
                    <a href="<%=controller.getPagingItemURI(request, Wzgg.class,notice.F01)%>">
                        <span class="lbt">【<%=DateParser.format(notice.F09)%>】</span>
                        <span class="lbt_con"><%StringHelper.filterHTML(out, notice.F05);%></span>
                    </a>
                  </li> 
              <%}}%>
              </ul>
          </div>  
    	<a href="<%configureProvider.format(out, URLVariable.ZXDT_WZGG);%>"  class="more fr">更多&gt;</a>
	</div>
</div>

<%}}%>

<!--平台优势-->
<div class="advantage">
    <div class="wrap clearfix">
        <div class="adv">
            <div class="adv_wrap">
                <ul class="about_ul clearfix">
                    <li class="about_item ab_last01">
                        <a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>" class="abouts_con icon_1">
                            <span class="about_img"></span>
                            <p class="about_title">低门槛</p>
                            <p class="about_more">100元起投<br/>随时赎回期限灵活</p>
                        </a>
                        <i class="about_light"></i>
                    </li>
                    <li class="about_item ab_last02">
                        <a href="<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>" class="abouts_con icon_2">
                            <span class="about_img"></span>
                            <p class="about_title">高收益</p>
                            <p class="about_more">30倍银行存款利息<br />最高16%年化利率</p>
                        </a>
                        <i class="about_light"></i>
                    </li>
                    <li class="about_item ab_last03">
                        <a href="<%configureProvider.format(out,URLVariable.AQBZ_BXDB);%>" class="abouts_con icon_3">
                            <span class="about_img"></span>
                            <p class="about_title">安全保障</p>
                            <p class="about_more">第三方资金托管<br />权威机构先行赔付</p>
                        </a>
                        <i class="about_light"></i>
                    </li>
                    <li class="about_item ab_last  fr">
                        <div class="abouts_con icon_4">
                            <span class="about_img"></span>
                            <span class="about_title">快捷借款</span>
                            <br>
                            <span class="about_more">成为借款人<br> 快速获取所需资金 </span>
                        </div>
                        <i class="about_light"></i>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
<!--平台优势-->

<%
    UserManage manage1 = serviceSession.getService(UserManage.class);
    if (dimengSession != null && dimengSession.isAuthenticated() && manage1.isFirstLogin()) {
        String error = controller.getPrompt(request, response, PromptLevel.ERROR);
%>
<form action="<%=controller.getURI(request, UpdatePassword.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
    <input id="newPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
    <input id="passwordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><!-- <a href="javascript:void(0)" class="out"></a> -->修改密码</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <span class="f18 gray3">为保障账户安全，首次登录请修改您的密码！</span>
                </div>
            </div>
            <ul class="text_list">
                <%-- <li>
                    <div align="center" class="red">
                        <%
                            StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));
                        %>
                    </div>
                </li> --%>
                <li>
                    <div class="til"><span class="red">*</span> 用户名：</div>
                    <div class="con">
                        <p><%StringHelper.filterHTML(out, manage1.getAccountName());%></p>
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span> 密码：</div>
                    <div class="con">
                        <input id="passwordOne" name="passwordOne" type="password" maxlength="20" mntest="/^<%=manage1.getAccountName() %>$/" mntestmsg="密码不能与用户名一致"
                               class="text_style password-a" autocomplete="off" onkeyup="value=value.replace(/\s/g,'')"/>

                        <p tip class="f12 gray9"><%
                            configureProvider.format(out, SystemVariable.PASSWORD_REGEX_CONTENT);%></p>
                        <p errortip class="red" style="display: none"></p>
                    </div>
                </li>
                <li>
                    <div class="til"><span class="red">*</span> 确认密码：</div>
                    <div class="con">
                        <input id="passwordTwo" name="passwordTwo" type="password" class="text_style password-b"
                               maxlength="20" autocomplete="off" onkeyup="value=value.replace(/\s/g,'')"/>
                        <p tip class="f12 gray9" <%=StringHelper.isEmpty(error) ? "" : "style='display: none'" %>>请再次输入密码</p>
                        <p errortip class="red" <%=StringHelper.isEmpty(error) ? "style='display: none'" : "" %>><%StringHelper.filterHTML(out,error);%></p>
                    </div>
                </li>
            </ul>
            <div class="tc mt10"><input type="submit" class="btn01 sumbitForme" fromname="form1" value="确定"></div>
        </div>
    </div>
</form>
<%}%>

<div class="video_jplayer" style="display:none;">
    <span class="video_jplayer_close" id="vid_colse">×</span>
    <div class="video_show">
        <%
            AdvertisementManage advertisementManage1 = serviceSession.getService(AdvertisementManage.class);
            AdvertSpscRecord record = advertisementManage1.searchqtSpsc();
        %>
        <%
            if (record != null) {
                if (record.status == T5021_F07.YFB && record.isAuto == 1) {
        %>
        <div id="jp_container_1" class="jp-video jp-video-360p" role="application" aria-label="media player">
            <div class="jp-type-single">
                <div id="jquery_jplayer_1" class="jp-jplayer"></div>
                <div class="jp-gui">
                    <div class="jp-video-play">
                        <button class="jp-video-play-icon" role="button" tabindex="0">play</button>
                    </div>
                    <div class="jp-interface">
                        <div class="jp-progress">
                            <div class="jp-seek-bar">
                                <div class="jp-play-bar"></div>
                            </div>
                        </div>
                        <div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
                        <div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
                        <div class="jp-controls-holder">
                            <div class="jp-controls">
                                <button class="jp-play" role="button" tabindex="0">play</button>
                                <button class="jp-stop" role="button" tabindex="0">stop</button>
                            </div>
                            <div class="jp-volume-controls">
                                <button class="jp-mute" role="button" tabindex="0">mute</button>
                                <button class="jp-volume-max" role="button" tabindex="0">max volume</button>
                                <div class="jp-volume-bar">
                                    <div class="jp-volume-bar-value"></div>
                                </div>
                            </div>
                            <div class="jp-toggles">
                                <button class="jp-repeat" role="button" tabindex="0">repeat</button>
                                <button class="jp-full-screen" role="button" tabindex="0">full screen</button>
                            </div>
                        </div>
                        <div class="jp-details">
                            <div class="jp-title" aria-label="title">&nbsp;</div>
                        </div>
                    </div>
                </div>
                <div class="jp-no-solution">
                    <span>Update Required</span>
                    To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.min.js"></script>
<script type="text/javascript">

    $("#index_video").click(function () {
        $(".video_jplayer").show();
        $("#jquery_jplayer_1").jPlayer("play");

    });
    $("#vid_colse").click(function () {
        $("#jquery_jplayer_1").jPlayer("pause");
        $(".video_jplayer").hide();
    });

    function gd(i) {
        if (i == 1) {
            document.getElementById('gd').href = '<%=controller.getViewURI(request, Wdhyzx.class)%>';
        } else {
            document.getElementById('gd').href = '<%=controller.getViewURI(request, Hlwjryj.class)%>';
        }
    }
    function onSubmit() {
        var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
        var key = RSAUtils.getKeyPair(exponent, '', modulus);
        $("#passwordOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
        $("#passwordTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
        return true;
    }

    $(document).ready(function(){
        <%if(record != null && record.status == T5021_F07.YFB && record.isAuto == 1)
        {%>
        $("#jquery_jplayer_1").jPlayer({
            ready: function () {
                $(this).jPlayer("setMedia", {
                    m4v: "<%=fileStore.getURL(record.fileName)%>"
                }).jPlayer("play");
            },
            swfPath: "<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.swf",
            supplied: "m4v",
            size: {
                width: "800px",
                height: "450px",
                cssClass: "jp-video-360p"
            },
            useStateClassSkin: true,
            autoBlur: false,
            smoothPlayBar: true,
            keyEnabled: true,
            remainingDuration: true,
            toggleDuration: true
        });
        <%}
        else if(record != null && record.status == T5021_F07.YFB && record.isAuto != 1)
        {%>
        $("#jp_container_1").click(function () {
            $(".video_start").hide();
            $("#jquery_jplayer_1").jPlayer("play");
        });

        $("#jquery_jplayer_1").jPlayer({
            ready: function () {
                $(this).jPlayer("setMedia", {
                    m4v: "<%=fileStore.getURL(record.fileName)%>"
                }).jPlayer("play");
            },
            swfPath: "<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.swf",
            supplied: "m4v",
            size: {
                width: "318px",
                height: "240px",
                cssClass: "jp-video-360p"
            },
            ended: function() {
                $(".video_start").show();
            },
            useStateClassSkin: true,
            autoBlur: false,
            smoothPlayBar: true,
            keyEnabled: true,
            remainingDuration: true,
            toggleDuration: true
        });
        <%}%>
    });

    $(".video_start").click(function () {
        $(".video_start").hide();
        $("#jquery_jplayer_1").jPlayer("play");
    });
</script>
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
        if(flag != "undefined" && flag == false){
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
<%}%>
<!-- 第三方登陆结束 -->