<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.user.servlets.spread.CheckYqm" %>
<%@page import="com.dimeng.p2p.S61.enums.T6111_F06" %>
<%@page import="com.dimeng.p2p.user.servlets.spread.Wdtg" %>
<%@page import="com.dimeng.p2p.account.user.service.SpreadManage" %>
<%@page import="com.dimeng.p2p.user.servlets.spread.Qrcode" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<html>
<head>
    <title>我要推广-<% configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <style type="text/css">
        #codeico{
            position:absolute; top:50%; left:50%; margin-left:-35px;margin-top:-15px;
            z-index:5;
            width:30px; 
            height:30px;
            background:url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat;
        }
    </style>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    SpreadManage manage2 = serviceSession.getService(SpreadManage.class);
    String tgm = manage2.getMyyqNo();
    String newYqm = manage2.getMyNewYqm();
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "WYTG";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>

        <div class="r_main">
            <div class="user_mod">
                <div class="user_tab clearfix">
                    <ul>
                        <li id="liId1" value="1" class="hover">推荐好友</li>
                        <li id="liId2" value="2">我的推广</li>
                    </ul>
                </div>
                <div id="tjhy" class="bs_bg">
                    <div class="user_til clearfix pt15"><i class="icon"></i><span
                        class="gray3 f18">推广码邀请</span>
               		 </div>
                    <div class="user_mod_gray pt40 pb40 mt15">
                        <div class="tip_info">
                            <div class="successful"></div>
                            <div class="tips">
                                <%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG))){%><span class="f20 orange">成功邀请好友投资，可获现金奖励！ </span><br/><%}%>
                                	这是您的专属推广码，请您邀请的好友在注册时输入：<span
                                    class="orange f20"><%StringHelper.filterHTML(out, tgm);%></span>
                            </div>
                        </div>
                    </div>
                    <div class="user_til clearfix pt30"><i class="icon"></i><span
                        class="gray3 f18">链接邀请</span>
               		 </div>
               		 <!-- <div id="qrcode"></div> -->
                    <div class="user_mod_gray mt15 pt20 pb20 clearfix">
                        <div class="link_invite">
                            <%if("true".equals(configureProvider.getProperty(SystemVariable.IS_ALLOW_ACCESS_APP_ADDRESS))){%>
                        	<p class="mb15 highlight">方式1：推广链接</p>
                        	<%} %>
                            <p class="mb5">这是您的专用邀请链接，请通过QQ或邮箱分享给好友：</p>
                            <%
                                String msg = configureProvider.getProperty(SystemVariable.TGWB);
                                msg = msg.replaceAll("xxx", tgm);
                                String wzmc = configureProvider.getProperty(SystemVariable.SITE_NAME);
                                String wzdz = configureProvider.getProperty(SystemVariable.SITE_DOMAIN);
                                msg = msg.replaceAll("SITE_NAME", wzmc);
                                msg = msg.replaceAll("SITE_DOMAIN", wzdz);
                                String bdmsg = msg;
                                if (msg.indexOf("http://") > 0) {
                                    bdmsg = msg.substring(0, msg.indexOf("http://"));
                                } else if (msg.indexOf("https://") > 0) {
                                    bdmsg = msg.substring(0, msg.indexOf("https://"));
                                }
                            %>
                            <textarea name="content" id="content" cols="50" readonly rows="2"
                                      class="textarea_style"><%=msg%></textarea>

                            <div class="mt15 clearfix">
                                <input name="input" type="button" id="copyLink" style="cursor: pointer;" value="复制链接"
                                       class="btn01 mr20 fl"/>

                                <div class="bdsharebuttonbox">
									<a title="分享到新浪微博" href="#" class="bds_tsina" data-cmd="tsina"></a>
									<!-- <a title="分享到微信" href="#" class="bds_weixin" data-cmd="weixin"></a> -->
									<a title="分享到QQ好友" href="#" class="bds_sqq" data-cmd="sqq"></a>
									<a title="分享到QQ空间" href="#" class="bds_qzone" data-cmd="qzone"></a>
									<!-- <a title="分享到腾讯微博" href="#" class="bds_tqq" data-cmd="tqq"></a>
									<a title="分享到人人网" href="#" class="bds_renren" data-cmd="renren"></a> -->
									<a href="#" class="bds_more" data-cmd="more"></a>
								</div>
								<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdUrl":"<%=configureProvider.format(URLVariable.REGISTER)%>?code=<%=tgm %>","bdText":"<%=bdmsg%>","bdMini":"1","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"24"},"share":{},"image":{"viewList":["tsina","qzone","sqq","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"24"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["tsina","qzone","sqq","tqq","renren","weixin"]}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>
                            </div>
                        </div>
                        <%if("true".equals(configureProvider.getProperty(SystemVariable.IS_ALLOW_ACCESS_APP_ADDRESS))){%>
                        <div class="invite-code">
                        	<p class="highlight mb20">方式2：推荐好友扫一扫，快速推广</p>
                        	<%-- <img alt="扫一扫" src="<%=controller.getURI(request, Qrcode.class)%>"> --%>
	                        <div id="qrcode" class="pr ml40" >
	                        	<div id="codeico"></div>
	                        </div>
                        </div>
                        <%} %>
                    </div>
                    <%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG))){%>
                    <div class="lh24 mt30">
                        <span class="highlight">邀请好友活动规则：</span><br/>
                        1. 只有在注册时输入您的邀请码或通过您复制的上述链接完成注册，并且要完成首笔充值<br/>
                        	（充值下限为<%=configureProvider.getProperty(SystemVariable.TG_YXCZJS)%>元），才能被确认为成功邀请。<br/>
                        2. 每成功邀请一位充值成功的客户，您就获得<%=configureProvider.getProperty(SystemVariable.TG_YXTGJL)%>元奖励，每人每月<%=configureProvider.getProperty(SystemVariable.TG_YXTGSX)%>人封顶。<br/>
                        3. 您成功邀请的好友单笔投资金额每增加<%=configureProvider.getProperty(SystemVariable.TG_TZJS)%>元，您即可多获得<%=configureProvider.getProperty(SystemVariable.TG_TZJL)%>元的连续奖励。<br/>
                        4. 若发现任何作弊或非法手段获得奖励的，将取消全部返利金额。<br/>
                    </div>
                    <%} %>
                    
                </div>
                <div id="wdtg" class="bs_bg" style="display: none;">
                   <%-- <form action="<%=controller.getViewURI(request, Wdtg.class)%>" method="post">--%>
                        <%
                            if (!StringHelper.isEmpty(newYqm)) {
                        %>
                        <div class="user_mod_gray pt10 pb10 pl20 f16 mt30">
                            <span class="gray3">我的推荐人：</span>
                            <%=newYqm.substring(0, 2) + "****" + newYqm.substring(4, newYqm.length())%>
                        </div>
                        <%
                        } else {
                        %>
                        <div class="user_mod_gray pt15 pb10 pl20 f16 mt30">
                            <span class="gray3" style="line-height:32px;">我的推荐人：</span>
                            <input type="text" value="邀请码/推广人手机号码" id="myCode"
                                   class="text gray9 yhgl_ser ml10" style="border:1px solid #d3bb9f; padding:5px 10px; line-height:20px; height:20px;"/>
                            <input type="button" id="myCodeBtn" class="btn01 ml10" value="确定"
                                   style="cursor: pointer;"/>

                            <div class="red mt5" id="errMsg" style="margin-left: 110px;"></div>
                        </div>
                        <%
                            }
                        %>
                        <div class="user_til pt30"><i class="icon"></i><span class="gray3 f16 mr20">邀请好友统计</span></div>
                        <div class="user_table mt10">
                            <table id="dataTable1" width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr class="til">
                                    <td align='center'>推广客户数(位)</td>
                                    <td align='center'>持续奖励总计(元)</td>
                                    <td align='center'>有效推广奖励总计(元)</td>
                                    <td align='center'>推广奖励总计(元)</td>
                                </tr>
                            </table>
                        </div>
                         <div class="user_til pt30"><i class="icon"></i><span class="gray3 f16 mr20">邀请好友注册详情</span></div>
                        <div class="user_table mt10">
                            <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr class="til">
                                	<td align="center">序号</td>
                                    <td align="center">您推荐好友的用户名</td>
                                    <td align="center">注册时间</td>
                                </tr>
                            </table>
                            <div class="page p15" id="pageContent"></div>
                        </div>
                        <input type="hidden" id="wdtgUrl" value="<%=controller.getURI(request, Wdtg.class)%>"/>
                   <%-- </form>--%>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>

<div class="popup_bg" style="display: none;"></div>
 <div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/spread.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/ZeroClipboard/ZeroClipboard.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
</body>

<script type="text/javascript">
	$('#qrcode').qrcode({
	    render:"table",//渲染方式
	    height:140,
	    width:140,
	    typeNumber: -1,//计算模式，默认是-1
	    text:"<%=configureProvider.format(URLVariable.WX_REGISTER)%>?yqm=<%=tgm %>"
	});

    function init() {
        var clip;
        clip = new ZeroClipboard.Client(); // 新建一个对象

        clip.setHandCursor(true);
        clip.setText($('#content').text().split("！")[1]); // 设置要复制的文本。
        clip.addEventListener("mouseUp", function (client) {
            $("#info").html(showDialogInfo("复制推广链接成功！", "successful"));
            $("div.popup_bg").show();
        });
        // 注册一个 button，参数为 id。点击这个 button 就会复制。
        //这个 button 不一定要求是一个 input 按钮，也可以是其他 DOM 元素。
        clip.glue("copyLink"); // 和上一句位置不可调换
    }
    $(function () {

        init();
        $("li").click(function () {
            var that = this;
            var src = "";
            $("li").removeClass("hover");
            $(this).addClass("hover");
            switch (that.value) {
                case 1:
                    $("#tjhy").show();
                    $("#wdtg").hide();
                    $("#ZeroClipboardMovie_1").show();
                    break;
                case 2:
                    $("#tjhy").hide();
                    $("#wdtg").show();
                    $("#ZeroClipboardMovie_1").hide();
                    break;
            }
        });
        $("#myCode").focus(function () {
            var obj = $(this);
            var _val = obj.val();
            if (_val == myCodeMsg) {
                obj.val("");
            }
        }).blur(function () {
            var obj = $(this);
            var _val = obj.val();
            if (_val == "") {
                obj.val(myCodeMsg);
            }
        });

        $("#myCodeBtn").click(function () {
            ajaxGetCode();
        });
        invateFriendsRegPaging();
    });

    var myCodeMsg = "邀请码/推广人手机号码";
    var errorMsg = "您输入的邀请码/手机号码不存在，请重新输入！";
    var errorMsg1 = "不能输入自己的邀请码/手机号码，请重新输入！";
    var errorMsg2 = "邀请码不属于个人用户，请重新输入！";
    var errorMsg3 = "不能输入被推广人的邀请码/手机号码，请重新输入！";

    function ajaxGetCode() {
        var code = $("#myCode").val();
        if ($.trim(code) == "" || code.length == 0 || code == myCodeMsg) {
            $("#errMsg").html("不能为空！");
            return false;
        } else {
            $.ajaxSetup({
                async: false
            });
            var codeURL = '<%=controller.getURI(request, CheckYqm.class)%>';
            $.post(codeURL, {
                code: code
            }, function (data) {
            	
            	if(data.msg != "undefined" && data.msg !=null && data.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(data.msg,"error",loginUrl));
            		return;
    			}
            	
                if ($.trim(data) == -1) {
                    $("#errMsg").html(errorMsg);
                    return false;
                } else if ($.trim(data) == -2) {
                    $("#errMsg").html(errorMsg1);
                    return false;
                } else if ($.trim(data) == -3) {
                    $("#errMsg").html(errorMsg2);
                    return false;
                } else if ($.trim(data) == -4) {
                    $("#errMsg").html(errorMsg3);
                    return false;
                } else if ($.trim(data) == -5) {
                    $("#errMsg").html("不能邀请非自然人");
                    return false;
                } else {
                    window.location.reload(true);
                }
            });
        }
        return true;
    }
    function toAjaxPage(){
    	invateFriendsRegPaging();
    }
    
</script>
</html>