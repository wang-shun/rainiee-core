<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.account.user.service.SpreadManage" %>
<%-- <%@include file="/WEB-INF/include/authenticatedSession.jsp" %> --%>
<html>
<head>
    <%
        CURRENT_CATEGORY = "TGGL";
        CURRENT_SUB_CATEGORY = "WYTG";
    %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    <script language="javascript"
            src="<%=controller.getStaticPath(request)%>/js/ZeroClipboard/ZeroClipboard.js"></script>
    <script type="text/javascript">
        $(function () {
            function init() {
                var clip;
                clip = new ZeroClipboard.Client(); // 新建一个对象

                clip.setHandCursor(true);
                clip.setText($('#content').text().split("！")[1]); // 设置要复制的文本。
                clip.addEventListener("mouseUp", function (client) {
                    alert("复制推广链接成功！");
                });
                // 注册一个 button，参数为 id。点击这个 button 就会复制。
                //这个 button 不一定要求是一个 input 按钮，也可以是其他 DOM 元素。
                clip.glue("copyLink"); // 和上一句位置不可调换
            }

            init();
            // $("#bdshare").attr("data",{"url" : location.href});
        });

    </script>
</head>
<body>
<div class="container fr mb20">
    <div class="p15-30">
        <div style="width:380px; margin:auto; line-height:45px;" class='w200'>
            <div class="d_succeed fl"></div>
            <span class="f20 orange bold">成功邀请好友投资，可获现金奖励！</span></div>
    </div>
</div>
<div class="container fr">
    <div class="p15-30">
        <div class="mb50">
            <div class="fl w200">
                <div class="tc blue"><span class="f50">1</span><br><span class="f20 bold">推广码邀请</span></div>
            </div>
            <div class="fl mt22">
                <div>这是您的专属推广码，请您邀请的好友在注册时输入：<br><span class="orange f30 bold">
                <%
                    SpreadManage manage1 = serviceSession.getService(SpreadManage.class);
                    String tgm = manage1.getMyyqNo();
                %>
                <%StringHelper.filterHTML(out, tgm); %></span></div>
            </div>
            <div class="clear"></div>
        </div>
        <div class="mb20 bom pb20">
            <div class="fl w200 mt30">
                <div class="tc blue"><span class="f50">2</span><br><span class="f20 bold">链接邀请</span></div>
            </div>
            <div class="fl mt22">
                <div>
                    <p class="mb10">这是您的专用邀请链接，请通过 QQ或邮箱 发送给好友：</p>
                    <%
                        String msg = configureProvider.getProperty(SystemVariable.TGWB);
                        String wzmc = configureProvider.getProperty(SystemVariable.SITE_NAME);
                        String wzdz = configureProvider.getProperty(SystemVariable.SITE_DOMAIN);
                        msg = msg.replaceAll("SITE_NAME", wzmc);
                        msg = msg.replaceAll("SITE_DOMAIN", wzdz);
                        msg = msg.replaceAll("xxx", tgm);
                    %>
                    <p class="mb10"><textarea name="content" id="content" cols="" rows=""
                                              class="yhgl_ser wh450 "><%= msg %></textarea></p>

                    <p><input name="input" type="button" id="copyLink" style="cursor: pointer;" value="复制链接"
                              class="btn01 fl mb10"/></p>

                    <div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"
                         data="{'url':'<%= configureProvider.format(URLVariable.REGISTER)%>'}">
                        <a class="bds_qzone"></a>
                        <a class="bds_tsina"></a>
                        <a class="bds_tqq"></a>
                        <a class="bds_renren"></a>
                        <a class="bds_t163"></a>
                        <span class="bds_more">更多</span>
                    </div>
                    <script type="text/javascript" id="bdshare_js" data="type=tools&uid=6478904"></script>
                    <script type="text/javascript" id="bdshell_js"></script>
                    <script type="text/javascript">
                        $(function () {
                            document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);
                        })
                    </script>
                </div>
            </div>
            <div class="clear"></div>
        </div>
        <div class="ln30 pl20 mb40">
            <p class="blue f16">邀请好友活动规则：</p>
            1. 只有在注册时输入您的邀请码或通过您复制的上述链接完成注册，并且要完成首笔充值<br/>
            （充值下限为<%=configureProvider.getProperty(SystemVariable.TG_YXCZJS) %>元），才能被确认为成功邀请。<br/>
            2. 每成功邀请一位充值成功的客户，您就获得<%=configureProvider.getProperty(SystemVariable.TG_YXTGJL) %>
            元奖励，每人每月<%=configureProvider.getProperty(SystemVariable.TG_YXTGSX) %>人封顶。<br/>
            3. 您成功邀请的好友单笔投资金额每增加<%=configureProvider.getProperty(SystemVariable.TG_TZJS) %>
            元，您即可多获得<%=configureProvider.getProperty(SystemVariable.TG_TZJL) %>元的连续奖励。<br/>
            4. 若发现任何作弊或非法手段获得奖励的，将取消全部返利金额。<br/>
        </div>
    </div>
</div>
<div class="clear"></div>
</div>
</body>
</html>