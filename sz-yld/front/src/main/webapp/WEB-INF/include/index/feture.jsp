<%@page import="com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.S50.enums.T5021_F07"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.repeater.policy.OperateDataManage"%>
<%@page import="com.dimeng.p2p.S70.entities.T7051"%>
<%@page import="com.dimeng.p2p.S70.entities.T7050"%>
<%@page import="com.dimeng.p2p.S61.entities.T6196"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.BidRecord"%>
<%@page import="com.dimeng.p2p.front.servlets.yysj.OperationData"%>
<%
	{
BidManage bidManage = serviceSession.getService(BidManage.class);
IndexStatic indexStatic = bidManage.getIndexStatic();
OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
T6196 t6196 = manage.getT6196();
%>
<div class="w1002 clearfix pb20">
    <!--介绍-->
    <div class="intro">
        <ul class="clearfix">
            <li class="mod01">
                <a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">
                <div class="icon">
                    <div class="high"><span class="high0"></span><span class="high1"></span></div>
                </div>
                </a>
                <div class="til" onclick="location.href='<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>'" style="cursor: pointer;">投资理财</div>
                <div class="text"><%
                	configureProvider.format(out, SystemVariable.FETURE_TZLC);
                %></div>
            </li>
            <li class="mod02">
                <a href="<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>"><div class="icon"><span class="rotate"></span></div></a>
                <div class="til" onclick="location.href='<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>'" style="cursor: pointer;">快捷借款</div>
                <div class="text"><%
                	configureProvider.format(out, SystemVariable.FETURE_KJDK);
                %></div>
            </li>
            <li class="mod03" style="margin-right:0px;">
                <a href="<%configureProvider.format(out, URLVariable.AQBZ_BXDB);%>"><div class="icon"><span class="scale"></span></div></a>
                <div class="til" onclick="location.href='<%configureProvider.format(out,URLVariable.AQBZ_BXDB);%>'" style="cursor: pointer;">本息保障</div>
                <div class="text"><%
                	configureProvider.format(out, SystemVariable.FETURE_BZBZ);
                %></div>
            </li>
        </ul>
    </div>
    <!--介绍-->
</div>
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

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jplayer/jquery.jplayer.min.js"></script>
<script type="text/javascript">
function onSubmit() {
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    $("#passwordOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
    $("#passwordTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
    return true;
}
$("#index_video").click(function () {
    $(".video_jplayer").show();
    $("#jquery_jplayer_1").jPlayer("play");

});
$("#vid_colse").click(function () {
    $("#jquery_jplayer_1").jPlayer("pause");
    $(".video_jplayer").hide();
});
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
        	width: "240px",
			height: "180px",
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