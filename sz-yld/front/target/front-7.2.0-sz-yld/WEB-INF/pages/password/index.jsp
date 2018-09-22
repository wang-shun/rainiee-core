<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.front.servlets.password.IndexVerify" %>
<%@page import="com.dimeng.p2p.front.servlets.password.Index" %>
<%@ page import="com.dimeng.p2p.common.FormToken" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%configureProvider.format(out, SystemVariable.SITE_TITLE);%>-找回密码</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    String type = ObjectHelper.convert(request.getAttribute("type"), String.class);
    String emailError = ObjectHelper.convert(request.getAttribute("EMAIL_ERROR"), String.class);
    String evCodeError = ObjectHelper.convert(request.getAttribute("EMAIL_VERIFYCODE_ERROR"), String.class);
    String phoneError = ObjectHelper.convert(request.getAttribute("PHONE_ERROR"), String.class);
    String pvCodeError = ObjectHelper.convert(request.getAttribute("PHONE_VERIFYCODE_ERROR"), String.class);
    String accountNameError = ObjectHelper.convert(request.getAttribute("ACCOUNTNAME_ERROR"), String.class);
    String avCodeError = ObjectHelper.convert(request.getAttribute("ACCOUNTNAME_VERIFYCODE_ERROR"), String.class);
%>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">找回密码</span></div>
        <%=FormToken.hidden(serviceSession.getSession()) %>
        <div class="back_way clearfix mt50">
            <ul>
                <li class="phone" id="back_way1" onclick="setTab('back_way',1,3)">使用绑定手机号找回密码</li>
                <li class="email" id="back_way2" onclick="setTab('back_way',2,3)">使用绑定邮箱找回密码</li>
                <li class="problem" id="back_way3" onclick="setTab('back_way',3,3)">使用密保问题找回密码</li>
            </ul>
        </div>
        <form action="<%=controller.getURI(request, Index.class)%>" method="post" class="form1"
              onsubmit="return onSubmitEmail();" name="form1">
            <input type="hidden" name="type" value="email"/>
            <input type="hidden" name="tokenEmail" value="">

            <div class="login_form" id="con_back_way_2" <%="email".equals(type) ? "" : "style=\"display: none;\"" %>>
                <div class="radio_tab pl100 ml100 mb20">
                    <label>
                        <input type="radio" name="radio" id="person1" <%if("ZRR".equals(request.getParameter("radio"))){ %>checked="checked"<%} %> class="ml40 mr5" value="ZRR"/>个人账号找回密码
                    </label>
                    <label>
                        <input type="radio" name="radio" id="business1" <%if("FZRR".equals(request.getParameter("radio"))){ %>checked="checked"<%} %> class="ml30 mr5" value="FZRR"/>企业账号找回密码
                    </label>
                </div>
                <div class="radio_tab_con">
                    <ul>
                        <li class="item">
                            <div class="til"><span class="red">*</span>邮箱：</div>
                            <div class="con">
                                <div class="input">
                                    <i class="email_ico"></i>
                                    <input name="email" type="text" class="text" onblur="emailCheck()"
                                           value="<%StringHelper.filterHTML(out,request.getParameter("email"));%>"/>
                                </div>
                                <p class="gray9" tip><%=StringHelper.isEmpty(emailError) ? "请输入邮箱地址" : "" %>
                                </p>

                                <p errortip
                                   class="error_tip red"><%=StringHelper.isEmpty(emailError) ? "" : emailError %>
                                </p>
                            </div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li class="item">
                            <div class="til"><span class="red">*</span>验证码：</div>
                            <div class="con">
                                <div class="clearfix">
                                    <div class="input verify_input fl">
                                        <i class="verify_ico"></i>
                                        <input name="emailCode" type="text" class="text" onblur="verifyCodeCheck(this)"
                                               maxlength="6"/>
                                    </div>
                                    <div class="fl mr5">
                                        <img id="_emailVerifyImg"
                                             src="<%=controller.getURI(request, IndexVerify.class)%>?t=e"
                                             class="border" width="78" style="cursor:pointer " title="点击换一张"
                                             onclick='this.src="<%=controller.getURI(request, IndexVerify.class)%>?t=e&"+Math.random()'
                                             height="34"/>
                                    </div>
                                    <div class="fl">看不清楚？<a href="javascript:void(0)"
                                                            onclick="$('#_emailVerifyImg').attr('src','<%=controller.getURI(request, IndexVerify.class)%>?t=e&'+Math.random())"
                                                            class="highlight">换一张</a></div>
                                </div>
                                <p class="gray9" tip><%=StringHelper.isEmpty(evCodeError) ? "请输入6位验证码" : "" %>
                                </p>

                                <p errortip
                                   class="error_tip red"><%=StringHelper.isEmpty(evCodeError) ? "" : evCodeError %>
                                </p>
                            </div>
                        </li>
                        <%}%>
                    </ul>
                    <div class="tc mt40">
                        <input class="btn06 tokenReset" fromname="form1" type="submit" value="发送验证邮件"/>
                    </div>
                </div>
            </div>
        </form>
        <form action="<%=controller.getURI(request, Index.class)%>" method="post" class="form2"
              onsubmit="return onSubmitPhone()" name="form2">
            <input type="hidden" name="type" value="phone"/>
            <input type="hidden" name="tokenPhone" value="">

            <div class="login_form" id="con_back_way_1"
                    <%="phone".equals(type) ? "" : "style=\"display: none;\""%>>
                <div class="radio_tab pl100 ml100 mb20">
                    <label>
                        <input type="radio" name="radio" id="person2" <%if("ZRR".equals(request.getParameter("radio"))){ %>checked="checked"<%} %> class="ml40 mr5" value="ZRR"/>个人账号找回密码
                    </label>
                    <label>
                        <input type="radio" name="radio" id="business2" <%if("FZRR".equals(request.getParameter("radio"))){ %>checked="checked"<%} %> class="ml30 mr5" value="FZRR"/>企业账号找回密码
                    </label>
                </div>
                <div class="radio_tab_con">
                    <ul>
                        <li class="item">
                            <div class="til"><span class="red">*</span>手机号：</div>
                            <div class="con">
                                <div class="input">
                                    <i class="phone_ico"></i><input id="phoneId" name="phone" maxlength="11" type="text"
                                                                    class="text" onblur="phoneCheck()"
                                                                    value="<%StringHelper.filterHTML(out,request.getParameter("phone"));%>"/>
                                </div>
                                <p tip class="gray9"><%=StringHelper.isEmpty(phoneError) ? "请输入11位手机号" : "" %>
                                </p>

                                <p errortip
                                   class="error_tip red"><%=StringHelper.isEmpty(phoneError) ? "" : phoneError %>
                                </p>
                            </div>
                        </li>
                        <%
                            {
                                if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        %>
                        <li class="item">
                            <div class="til"><span class="red">*</span>验证码：</div>
                            <div class="con">
                                <div class="clearfix">
                                    <div class="input verify_input fl">
                                        <i class="verify_ico"></i><input name="phoneCode" type="text"
                                                                         class="text required"
                                                                         onblur="verifyCodeCheck(this)" maxlength="6"/>
                                    </div>
                                    <div class="fl mr5">
                                        <img id="_phoneVerifyImg" style="cursor:pointer " title="点击换一张"
                                             src="<%=controller.getURI(request, IndexVerify.class)%>?t=p"
                                             onclick='this.src="<%=controller.getURI(request, IndexVerify.class)%>?t=p&"+Math.random()'
                                             width="78" height="34"/>
                                    </div>
                                    <div class="fl">看不清楚？<a href="javascript:void(0)"
                                                            onclick="$('#_phoneVerifyImg').attr('src','<%=controller.getURI(request, IndexVerify.class)%>?t=p&'+Math.random())"
                                                            class="highlight">换一张</a></div>
                                </div>
                                <p class="gray9" tip><%=StringHelper.isEmpty(pvCodeError) ? "请输入6位验证码" : "" %>
                                </p>

                                <p errortip
                                   class="error_tip red"><%=StringHelper.isEmpty(pvCodeError) ? "" : pvCodeError %>
                                </p>
                            </div>
                        </li>
                        <%
                                }
                            }
                        %>
                    </ul>
                    <div class="tc mt40">
                        <input class="btn06" fromname="form2" type="submit" value="提 交"/>
                    </div>
                </div>
            </div>
        </form>
        <form action="<%=controller.getURI(request, Index.class)%>" method="post" class="form3"
              onsubmit="return onSubmitAccount();" name="form3">
            <input type="hidden" name="type" value="accountName"/>
            <input type="hidden" name="tokenAccountName" value="">

            <div class="login_form" id="con_back_way_3"
                    <%="accountName".equals(type) ? "" : "style=\"display: none;\"" %>>
                <ul>
                    <li class="item">
                        <div class="til"><span class="red">*</span>用户名：</div>
                        <div class="con">
                            <div class="input">
                                <i class="name_ico"></i><input name="accountName" type="text" class="text"
                                                               maxlength="30" onblur="nameCheck()"
                                                               value="<%StringHelper.filterHTML(out,request.getParameter("accountName"));%>"/>
                            </div>
                            <p class="gray9" tip><%=StringHelper.isEmpty(accountNameError) ? "请输入正确的用户名" : "" %>
                            </p>

                            <p errortip
                               class="error_tip red"><%=StringHelper.isEmpty(accountNameError) ? "" : accountNameError %>
                            </p>
                        </div>
                    </li>
                    <%
                        {
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                    %>
                    <li class="item">
                        <div class="til"><span class="red">*</span>验证码：</div>
                        <div class="con">
                            <div class="clearfix">
                                <div class="input verify_input fl">
                                    <i class="verify_ico"></i><input name="accountCode" type="text" class="text"
                                                                     onblur="verifyCodeCheck(this)" maxlength="6"/>
                                </div>
                                <div class="fl mr5">
                                    <img id="_nameVerifyImg" style="cursor:pointer " title="点击换一张"
                                         src="<%=controller.getURI(request, IndexVerify.class)%>?t=a"
                                         onclick='this.src="<%=controller.getURI(request, IndexVerify.class)%>?t=a&"+Math.random()'
                                         width="78" height="34"/>
                                </div>
                                <div class="fl">看不清楚？<a href="javascript:void(0)"
                                                        onclick="$('#_nameVerifyImg').attr('src','<%=controller.getURI(request, IndexVerify.class)%>?t=a&'+Math.random())"
                                                        class="highlight">换一张</a></div>
                            </div>
                            <p class="gray9" tip><%=StringHelper.isEmpty(avCodeError) ? "请输入6位验证码" : "" %>
                            </p>

                            <p errortip class="error_tip red"><%=StringHelper.isEmpty(avCodeError) ? "" : avCodeError %>
                            </p>
                        </div>
                    </li>
                    <%
                            }
                        }
                    %>
                </ul>
                <div class="tc mt40">
                    <input class="btn06" fromname="form3" type="submit" value="提 交"/>
                </div>
            </div>
        </form>
        <div class="border_t_d pt15 mt50 tc">
            若您操作过程中遇到任何问题，请联系客服<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL) %>
        </div>
    </div>
</div>

<div class="popup_bg" style="display: none;"></div>
<div class="dialog" style="display: none;">
    <div class="title"><a href="javascript:void(0)" class="out" onclick="closeInfo()"></a>提交成功</div>
    <div class="content">
        <div class="tip_information">
            <div class="successful"></div>
            <div class="tips">
               	 系统已经向您的邮箱(<span class="red"><%=request.getAttribute("email") %></span>)发送了一封验证邮件，请进入邮箱点击链接以找回密码。
            </div>
        </div>
        <div class="tc mt20"><a href="javascript:void(0)" id="ok" class="btn01">再发一封</a><a href="javascript:void(0)"
                                                                                           id="cancel"
                                                                                           class="btn01 btn_gray ml20">返回首页</a>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    var sfxyyzm =<%=configureProvider.getProperty(SystemVariable.SFXYYZM)%>;

    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='tokenEmail']").val(tokenValue);
        $("input[name='tokenPhone']").val(tokenValue);
        $("input[name='tokenAccountName']").val(tokenValue);

        var showMsg = '<%= request.getAttribute("showMsg")==null ? "" : request.getAttribute("showMsg")%>';
        var key = '<%= StringHelper.isEmpty(request.getParameter("key"))? false :true %>';
        if (key == 'true') {
            setTab('back_way', 2, 3);
        }
        if (showMsg) {
            $("div.popup_bg").show();
            $("div.dialog").show();
        }

        $("#ok").click(function () {
            $("div.popup_bg").hide();
            $("div.dialog").hide();
        })

        $("#cancel").click(function () {
            location.href = "<%configureProvider.format(out,URLVariable.INDEX);%>";
        })
    });

    function closeInfo() {
        $("div.popup_bg").hide();
        $("div.dialog").hide();
    }

    function onSubmitPhone() {
        //判断是否需要验证码
        if (!(sfxyyzm != "undifined" && sfxyyzm == false)) {
            if (phoneCheck() && verifyCodeCheck($("input[name='phoneCode']"))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (phoneCheck()) {
                return true;
            } else {
                return false;
            }
        }
    }

    function onSubmitEmail() {
        if (!(sfxyyzm != "undifined" && sfxyyzm == false)) {
            if (emailCheck() && verifyCodeCheck($("input[name='emailCode']"))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (emailCheck()) {
                return true;
            } else {
                return false;
            }
        }
    }

    function onSubmitAccount() {
        if (!(sfxyyzm != "undifined" && sfxyyzm == false)) {
            if (nameCheck() && verifyCodeCheck($("input[name='accountCode']"))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (nameCheck()) {
                return true;
            } else {
                return false;
            }
        }
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/password.js"></script>

<script type="text/javascript">
$(function () {
	if($('input[type="radio"]:checked').val()==null){
		$("#person1").attr("checked","checked");
		$("#person2").attr("checked","checked");
	}
	
});
</script>
</body>
</html>