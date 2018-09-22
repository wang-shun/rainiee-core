<%@page import="com.dimeng.p2p.S61.entities.T6125" %>
<%@page import="com.dimeng.p2p.S61.entities.T6147" %>
<%@page import="com.dimeng.p2p.S61.enums.*" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.account.user.service.UserManage" %>
<%@page import="com.dimeng.p2p.account.user.service.achieve.PwdSafeCacheManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.repeater.policy.RiskQuesManage" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.user.servlets.Login" %>
<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon" %>
<%@page import="com.dimeng.p2p.user.servlets.account.*" %>
<%@page import="com.dimeng.p2p.variables.defines.EmailVariavle" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@ page import="org.bouncycastle.util.encoders.Hex" %>
<%@ page import="java.util.List" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></title>
</head>
<body>--%>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "AQXX";
    SafetyManage userManage = serviceSession.getService(SafetyManage.class);
    Safety data = userManage.get();
    if(T6110_F06.FZRR.name().equals(data.userType))
    {
        controller.sendRedirect(request, response, controller.getViewURI(request, QyBases.class));
		return;
    }
    String a = data.phoneNumber;
    String phonenumber = StringHelper.isEmpty(a) || a.length() != 11 ? "" : a.substring(0, 3) + "*****" + a.substring(7, a.length());
    String phoneFlg = null;
    String emilFlg = null;
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    PwdSafeCacheManage pwdSafeService = serviceSession.getService(PwdSafeCacheManage.class);
    List<PwdSafetyQuestion> questionList = pwdSafeService.getQuestionList();
    UserManage manage = serviceSession.getService(UserManage.class);
    String usrCustId = manage.getUsrCustId();
    PwdSafeCacheManage pwdSafeCacheManage = serviceSession.getService(PwdSafeCacheManage.class);
    List<PwdSafetyQuestion> questions = pwdSafeCacheManage.getPasswordQuestionByUser(serviceSession.getSession().getAccountId());
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean CHARGE_MUST_WITHDRAWPSD = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
%>
<input type="hidden" name="phoneFlg" id="phoneFlg" value="1"/>
<input type="hidden" name="emilFlg" id="emilFlg" value="1"/>
<input id="newPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
<input id="passwordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>
<input id="newTxPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_TXPASSWORD_REGEX);%>"/>
<input id="txPasswordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%>"/>

<div id="token">
    <%=FormToken.hidden(serviceSession.getSession()) %>
</div>

<%
    String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(ermsg)) {
%>
<ul class="xx_li" id="kjd">
    <li style="text-align: center;color: red;">
        <%-- <%StringHelper.filterHTML(out, ermsg);%> --%>
    </li>
</ul>
<%}%>
<ul>
    <li class="item">
        <div class="con">
            <div class="mod01">用户名</div>
            <div class="mod02"><%StringHelper.filterHTML(out, data.username);%></div>
        </div>
    </li>
    <li class="item">
        <%if(!StringHelper.isEmpty(data.isIdCard) && data.isIdCard.equals(T6141_F04.TG.name())) { %>
        <div class="con">
            <div class="mod01">实名认证</div>
            <div class="mod02"><%StringHelper.filterHTML(out, data.idCard); %></div>
            <div class="operation"><%=data.name.substring(0, 1) + "**" %>
            </div>
        </div>
        <%} else {%>
        <!-- <form action="" class="form1" method="post">  -->
        <div class="con">
            <div class="mod01">实名认证</div>
            <%if(!tg){ %>
	            <div class="mod02 red"><%=StringHelper.isEmpty(data.sfzh)?"未设置":"已提交" %></div>
	            <div class="operation">
	                <a href="javascript:void(0);"
	                   onclick="openShutManager(this,'box1',false,'取消设置','设置')"
	                   class="highlight">设置</a>
	            </div>
            <%}else{ %>
            	<div class="mod02 red"><%=StringHelper.isEmpty(data.sfzh)?"未认证":"已提交" %></div>
            <%} %>
        </div>
        <div id="box1" style="display: none" class="form_info amend">
            <ul class="cell">
                <li class="tc ln30 orange f16" style="display:none;" id="sbNameLi">恭喜您，实名认证成功！3秒后将刷新此页面。。。<br>
                    <p></p></li>
                <li>
                    <div class="til">
                        <span class="red">*</span>真实姓名：
                    </div>
                    <div class="info">
                        <input type="text" value="<%StringHelper.filterQuoter(out, data.name); %>" name="name" class="text yhgl_ser fl mr10 w200 required max-length-10"
                               mtest="/^[\u4e00-\u9fa5]{2,}$/"  mtestmsg="请输入合法的姓名" style="width:173px"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <div class="til">
                        <span class="red">*</span>身份证号：
                    </div>
                    <div class="info">
                        <input type="text" value="<%StringHelper.filterQuoter(out, StringHelper.decode(data.sfzh)); %>" name="idCard" class="text yhgl_ser w200 required idcard" style="width:173px"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
            </ul>
            <div class="tc">
                <input name="input" type="button" value="提 交" class="btn01" onclick="updateName(this)"/>
            </div>
        </div>
        <!-- </form> -->
        <%}%>
        <div class="clear"></div>
    </li>
    <li class="item">
        <div class="con">
            <div class="mod01">登录密码</div>
            <% String type = "设置";
               if (!StringHelper.isEmpty(data.password)) {
                    type = "修改";%>
            <div class="mod02">已设置</div>
            <%} else {
                type = "设置";
            %>
            <div class="mod02 red">未设置</div>
            <%} %>
            <div class="operation">
                <a href="javascript:void(0);" onclick="openShutManager(this,'box2',false,'取消<%=type%>','<%=type%>')"
                   class="highlight"><%=type%>
                </a>
            </div>
        </div>
        <div class="clear"></div>
        <%-- <form action="<%=controller.getURI(request, UpdatePassword.class)%>" class="form2" method="post"> --%>
        <div id="box2" style="display: none" class="form_info amend">
            <ul class="cell">
                <li class="tc ln30 orange f16" style="display:none;" id="sbPswLi">恭喜您，登录密码修改成功！3秒后将刷新此页面。。。<br>
                    <p></p></li>
                <li>
                    <div class="til">
                        <span class="red">*</span> 原密码：
                    </div>
                    <div class="info">
                        <input id="oldPassword" type="password" name="old" maxlength="20" onselectstart="return false;"
                               ondragenter="return false;" onpaste="return false;" style="width:173px"
                               class="text yhgl_ser w200 fl mr10 required min-length-6"/>
                        <br/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <div class="til">
                        <span class="red">*</span>新密码：
                    </div>
                    <div class="info">
                        <input id="passwordOne" type="password" name="new" fromname="form2" maxlength="20"
                               onselectstart="return false;" ondragenter="return false;" onpaste="return false;" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致！"
                               class="text yhgl_ser w200 fl mr10 required password-a min-length-6 max-length-20"
                               style="float: left;width:173px"/>
                               <br/>
                        <p class="gray9 f12" tip><%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
                <li>
                    <div class="til">
                        <span class="red">*</span>确认新密码：
                    </div>
                    <div class="info">
                        <input id="passwordTwo" type="password" name="news" maxlength="20" onselectstart="return false;"
                               ondragenter="return false;" onpaste="return false;"
                               class="text yhgl_ser w200 fl mr10 required password-b min-length-6 max-length-20"
                               style="float: left;width:173px"/>
                        <br/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
            </ul>
            <div class="tc">
                <input name="submit" type="button" onclick="updatePassword('<%=data.username %>')" value="提 交" class="btn01"/>
            </div>
        </div>
        <!-- </form> -->
    </li>
    <li class="item">
        <div class="con">
            <div class="mod01">绑定邮箱</div>
            <%
                String emil = "";
                String realEmail = "";
                if (!StringHelper.isEmpty(data.isEmil) && data.isEmil.equals(T6118_F04.TG.name())) {
                    type = "修改";
                    emil = data.emil.substring(0, 1) + "*****" + data.emil.split("@")[1];
                    realEmail = data.emil;
            %>
            <div class="mod02"><%=emil%>
            </div>
            <%
            } else {
                type = "设置";
            %>
            <div class="mod02 red">未设置</div>
            <%} %>
            <div class="operation">
                <a href="javascript:void(0);"
                   onclick="openShutManager(this,'box3',false,'取消<%=type%>','<%=type%>');clearTimes(2);"
                   class="highlight"><%=type%>
                </a>
            </div>
        </div>
        <div class="clear"></div>
        <div id="box3" style="display: none" class="amend">
            <div class="binding" id="mt1" <%if(type.equals("设置")){ %>style="display: none"<%} %>>
                <p class="tc">您正在修改的绑定邮箱是<span class="orange"><%=emil %></span></p>
                <ul class="clearfix">
                    <li class="w111">
                        <a href="javascript:void(0);" class="icon icon01"></a>
                        <p><a href="javascript:void(0);">通过原邮箱修改</a></p>
                    </li>
                    <li class="w111">
                        <a href="javascript:void(0);" class="icon icon02"></a>
                        <p><a href="javascript:void(0);">通过手机号码修改</a></p>
                    </li>
                </ul>
            </div>
            <!-- 通过验证原邮箱名称修改绑定邮件==开始 -->
            <div class="steps" id="e1" style="display: none">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证当前邮箱</span>
                    <span class="ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span>3.成功</span>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>
                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">
                                <span class="red">*</span>原邮箱地址：
                            </div>
                            <div class="info">
                                <input id="oldEmailCheck" type="text" class="text code p_text required e-mail" style="width:173px;"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="eUpdateVerify" type="text"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_1"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=updateEmail"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updateEmail'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_1').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updateEmail')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%}%>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="oldone" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <!-- 通过验证原邮箱名称修改绑定邮件==结束 -->

            <!-- 通过验证原邮箱名称修改绑定邮件 -->
            <div class="steps" id="e2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证当前邮箱</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span>3.成功</span>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span>验证新邮箱：</div>
                            <div class="info">
                                <input type="text" class="text sendemil required e-mail" style="width:173px;" maxlength="26"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="eNewVerify" type="text" maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                       class="text code required verify-code" style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_2"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=newEmail"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newEmail'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_2').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newEmail')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%}%>
                    </ul>

                    <div class="tc">
                        <input name="input" type="button" value="发送验证邮件" class="btn01 emilsend_email coden"/>
                    </div>
                    <div class="clear"></div>
                </div>

                <div id="emailSendSuccess3" style="display: none">
                    <p class="tc mb20">激活邮件已发送至您的邮箱：<span id="emailSendSuccess3Email" class="red"></span>
				                        请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>小时内<br/>
				                        登录邮箱并点击邮件中的链接，完成验证。<br/>
				                        没收到？<a onclick="showEmailSend('3')" class="red pl5" style="cursor: pointer;">再次发送</a>
                    </p>
                </div>
            </div>

            <div class="steps" id="e3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证当前邮箱</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span class="highlight">3.成功</span>
                </div>
                <p class="tc mb20 orange">恭喜您，已经成功绑定您的新邮箱！3秒后将刷新此页面...</p>

                <p class="tc mb20"></p>
            </div>

            <div class="steps" id="ep1" style="display:none;">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span>3.成功</span>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">原手机号码：</div>
                            <div class="info"><%=phonenumber%>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="epUpdateEmailVerify" type="text"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" maxlength="6"
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_3"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=epUpdateEmail"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=epUpdateEmail'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_3').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=epUpdateEmail')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%}%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>手机验证码：
                            </div>
                            <div class="info clearfix">
                                <input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                <input name="input" type="button" value="获取手机验证码" onclick="epupdate(this);" class="btn05" id="yzmbtn2"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="epoldone" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>

            <div class="steps" id="ep2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span>3.成功</span>

                    <div class="clear"></div>
                </div>
                <p class="tc mb20">原手机号码验证已通过，请填写您的新邮箱。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span>验证新邮箱：</div>
                            <div class="info">
                                <input type="text" class="text sendemil required e-mail" maxlength="26" style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="epNewEmailVerify" type="text" maxlength="6"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_4"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=newEmail"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newEmail'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_4').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newEmail')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>

                                <p tip></p>

                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%}%>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="发送验证邮件" class="btn01 emilsend_phone coden"/>
                    </div>
                    <div class="clear"></div>
                </div>

                <div id="emailSendSuccess2" style="display: none">
                    <p class="tc mb20">激活邮件已发送至您的邮箱：<span id="emailSendSuccess2Email" class="red"></span>
				                        请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>小时内<br/>
				                        登录邮箱并点击邮件中的链接，完成验证。<br/></p>
                    <p class="tc mb20">没收到？<a href="javascript:void(0)" onclick="showEmailSend('2')" class="red pl5" style="cursor: pointer;">再次发送</a></p>
                </div>
            </div>

            <div class="steps" id="ep3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新邮箱</span>
                    <span class="highlight">3.成功</span>
                    <div class="clear"></div>
                    <p class="tc mb20 orange">恭喜您，已经成功绑定您的新邮箱！3秒后将刷新此页面...</p>
                    <p class="tc mb20"></p>
                </div>
            </div>
            <div id="emailSend1" class="form_info" <%if(type.equals("修改")){ %>style="display: none"<%} %>>
                <ul class="cell">
                    <li class="tc mb20 orange" style="display:none;" id="succLi">恭喜您，已经成功绑定您的邮箱！3秒后将刷新此页面。。。<br/>

                        <p></p></li>
                    <li>
                        <div class="til"><span class="red">*</span>邮箱：</div>
                        <div class="info">
                            <input name="bemil" type="text" class="text  required e-mail" maxlength="26" style="width:173px"/>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </div>
                        <div class="clear"></div>
                    </li>
                    <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                    <li>
                        <div class="til">
                            <span class="red">*</span>验证码：
                        </div>
                        <div class="info">
                            <input name="bindEmailVerify" type="text" maxlength="6"
                                   maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                   style="width:173px;"/>
								<span>
									<img alt="验证码" id="_verifyImg_6"
                                         src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=bindEmail"
                                         alt="验证码" title="点击刷新" width="80" height="31"
                                         onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindEmail'"
                                         style="cursor: pointer;"/>
									<a href="javascript:void(0)"
                                       onclick="$('#_verifyImg_6').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindEmail')"
                                       class="blue ml10 blue_line">换一张</a>
								</span>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </div>
                        <div class="clear"></div>
                    </li>
                    <%} else {%>
                    <li style="display: none">
                        <div class="clear"></div>
                    </li>
                    <%}%>
                </ul>
                <div class="tc">
                    <input name="input" type="button" onclick="submitBandEmil()" value="发送验证邮件" class="btn01 sumbitForme"/>
                </div>
                <div class="clear"></div>
            </div>

            <div id="emailSendSuccess1" style="display: none">
                <p class="tc mb20">激活邮件已发送至您的邮箱：<span id="emailSendSuccess1Email" class="red"></span>
			                    请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>小时内<br/>
			                    登录邮箱并点击邮件中的链接，完成验证。<br/></p>
                <p class="tc mb20">没收到？<a onclick="showEmailSend('1')" class="red pl5" style="cursor: pointer;">再次发送</a>
                </p>
            </div>
        </div>
    </li>

    <li class="item">
        <div class="con">
            <div class="mod01">绑定手机</div>
            <%
                String phone = "";
                if (!StringHelper.isEmpty(data.phoneNumber)) {
                    type = "修改";
                    phone = phonenumber;
            %>
            <div class="mod02"><%=phone%></div>
            <%  } else {
                type = "设置";
            %>
            <div class="mod02 red">未设置</div>
            <%} %>
            <div class="operation">
                <% if(!StringHelper.isEmpty(usrCustId) && (escrow.equals("yeepay") || escrow.equals("FUYOU"))){ %>
                <a href="<%=configureProvider.format(URLVariable.ESCROW_URL_RESETMOBILE) %>" class="highlight">修改</a>
                <%}else{ %>
                <a href="javascript:void(0);" onclick="openShutManager(this,'box4',false,'取消<%=type%>','<%=type%>');clearTimes(7);" class="highlight"><%=type%></a>
                <%} %>
            </div>
            <div class="clear"></div>
        </div>
        <div id="box4" style="display: none" class="amend">
            <div class="binding" id="mt2" <%if(type.equals("设置")){ %>style="display: none"<%} %>>
                <p class="tc">您正在修改的绑定手机号是<span class="orange"><%=phone%></span></p>
                <ul class="clearfix">
                    <li class="w111">
                        <a href="javascript:void(0)" class="icon icon03"></a>
                        <p><a href="javascript:void(0)">通过原手机短信修改</a></p>
                    </li>
                    <li class="w111">
                        <a href="javascript:void(0)" class="icon icon04"></a>
                        <p><a href="javascript:void(0)">通过身份证修改</a></p>
                    </li>
                </ul>
            </div>
            <div class="steps" id="p1" style="display:none;">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span>3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">原手机号码：</div>
                            <div class="info"><%=phone%>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="phoneUpdateVerify" type="text" 
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_9"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=updatePhone"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updatePhone'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_9').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updatePhone')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%}%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>手机验证码：
                            </div>
                            <div class="info clearfix">
                                <input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                <input name="input" type="button" value="获取手机验证码" onclick="pupdate(this);" class="btn05" id="yzmbtn7"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="poldone" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>

            <div class="steps" id="p2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span>3.成功</span>

                    <div class="clear"></div>
                </div>
                <p class="tc mb20">原手机号码验证已通过，请填写您的新手机号码。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">
                                <span class="red">*</span>新手机号码 ：
                            </div>
                            <div class="info">
                                <input type="text" maxlength="11" class="text sendphone" style="width:173px;"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="phoneNewVerify" type="text"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" 
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_7"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=newPhone"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newPhone'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_7').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newPhone')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%
                            }
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>手机验证码：
                            </div>
                            <div class="info clearfix">
                                <input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                <input name="input" type="button" value="获取手机验证码" class="btn05 phonesend_phone clearTimedes"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="pnewtwo" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="steps" id="p3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证原手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span class="highlight">3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20 orange">恭喜您，已经成功绑定您的新手机号码！3秒后将刷新此页面。。。</p>
                <p class="tc mb20"></p>
            </div>

            <div class="steps" id="ip1" style="display:none;">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">
                    <span class="highlight">1.填写身份证号码</span>
                    <span class="ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span>3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>
                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">用户名：</div>
                            <div class="info"><%=data.username%>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>身份证号码：
                            </div>
                            <div class="info">
                                <input type="text" class="text" maxlength="18"  style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <% if(CHARGE_MUST_WITHDRAWPSD){%>
                        <li>
                            <div class="til">
                                <span class="red">*</span>交易密码：
                            </div>
                            <div class="info">
                                <input type="password" class="text" onselectstart="return false;" style="width:173px"
                                       ondragenter="return false;" onpaste="return false;"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%}%>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="ipoldone" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="steps" id="ip2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">1.填写身份证号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span>3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20">身份证号码验证已通过，请填写您的新手机号码。</p>
                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">
                                <span class="red">*</span>新手机号码 ：
                            </div>
                            <div class="info">
                                <input type="text" maxlength="11" class="text sendphone" style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="cphoneNewVerify" type="text"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" 
                                       style="width:173px;"/>
									<span>
										<img alt="验证码" id="_verifyImg_8"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=newPhone"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newPhone'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_8').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=newPhone')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%
                            }
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>手机验证码：
                            </div>
                            <div class="info clearfix">
                                <input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                <input name="input" type="button" value="获取手机验证码" class="btn05 phonesend_card clearTimedes"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="ipnewtwo" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="steps" id="ip3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                    <span class="highlight">1.填写身份证号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.验证新手机号码</span>
                    <span class="highlight">3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20 orange">恭喜您，已经成功绑定您的新手机号码！3秒后将刷新此页面。。。</p>
                <p class="tc mb20"></p>
            </div>
            <div class="form_info" <%if(type.equals("修改")){ %>style="display: none"<%} %>>
                <ul class="cell">
                    <li class="tc orange" style="display:none;" id="sbPhoneLi">恭喜您，已经成功绑定您的手机号码！3秒后将刷新此页面。。。<br>
                        <p></p></li>
                    <li>
                        <div class="til"><span class="red">*</span>手机号：</div>
                        <div class="info">
                            <input name="binphpne" type="text" maxlength="11" class="text required phonenumber " style="width:173px"/>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </div>
                        <div class="clear"></div>
                    </li>
                    <%
                        if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                    %>
                    <li>
                        <div class="til">
                            <span class="red">*</span>验证码：
                        </div>
                        <div class="info">
                            <input name="bindPhoneVerify" type="text"
                                   maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                   style="width:173px;" />
								<span>
									<img alt="验证码" id="_verifyImg_5"
                                         src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=bindPhone"
                                         alt="验证码" title="点击刷新" width="80" height="31"
                                         onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone'"
                                         style="cursor: pointer;"/>
									<a href="javascript:void(0)"
                                       onclick="$('#_verifyImg_5').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone')"
                                       class="blue ml10 blue_line">换一张</a>
								</span>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </div>
                        <div class="clear"></div>
                    </li>
                    <%} else {%>
                    <li style="display: none">
                        <div class="clear"></div>
                    </li>
                    <%
                        }
                    %>
                    <li>
                        <div class="til"><span class="red">*</span>手机验证码：</div>
                        <div class="info clearfix">
                            <input name="bphoneCode" type="text" class="text required p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                            <input name="input" type="button" onclick="bandPhone(this)" value="获取手机验证码" class="btn05 "/>
                            <p tip></p>
                            <p errortip class="" style="display: none"></p>
                        </div>
                        <div class="clear"></div>
                    </li>
                </ul>
                <div class="tc"><input name="input" type="button" onclick="submitBandPhone(this)" value="提交"
                                       class="btn01 sumbitForme"/></div>
                <div class="clear"></div>
            </div>
        </div>
    </li>

    <%
        if(CHARGE_MUST_WITHDRAWPSD){
    %>
    <li class="item">
        <div class="con">
            <div class="mod01">交易密码</div>
            <%if (!StringHelper.isEmpty(data.txpassword)) {%>
            <div class="mod02">已设置</div>
            <div class="operation">
                <a id="jymmxg" href="javascript:void(0);"
                   onclick="openShutManager(this,'box6',false,'取消修改&nbsp;|&nbsp;','修改&nbsp;|&nbsp;');clearTimes(5);"
                   class="highlight">修改 &nbsp;|&nbsp;</a>
                <a id="jymmzh" href="javascript:void(0);" onclick="openShutManager(this,'box7',false,'取消找回','找回');clearTimes(5);"
                   class="highlight">找回</a>
            </div>
            <%} else {%>
            <div class="mod02 red">未设置</div>
            <div class="operation">
                <a href="javascript:void(0);"
                   onclick="openShutManager(this,'box5',false,'取消设置','设置',<%=data.phoneNumber %>);"
                   class="highlight">设置</a>
            </div>
            <%}%>
            <div class="clear"></div>
        </div>
        <div id="box5" style="display: none" class="amend">
            <%--设定密码--%>
            <%-- <form action="<%=controller.getURI(request, SettxPassword.class)%>" class="form4" method="post"> --%>
            <div class="">
                <p class="tc mb20">为了您的账户安全，请定期更换交易密码，并确保交易密码设置与登录密码不同</p>

                <div class="form_info">
                    <ul class="cell">
                        <li style="margin-bottom: 0"></li>
                        <li class="tc mb20 orange" style="display:none;" id="sbSetPswLi">
                            	恭喜您，已经成功设置您的交易密码！3秒后将刷新此页面。。。<br>
                            <p></p></li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>交易密码：<br/>
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" name="new" onchange="setSubmitFlg();" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致"
                                       onselectstart="return false;" ondragenter="return false;" onpaste="return false;"
                                       class="text required cpassword-f" style="width:173px"/>
                                <p class="gray9 f12" tip><%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%></p>
                                <p errortip class="" style="display: none"></p>
                                <div class="clear"></div>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>确认交易密码：
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" name="cnew" onchange="setSubmitFlg();"
                                       onselectstart="return false;" ondragenter="return false;" onpaste="return false;"
                                       class="text required password-bf" style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="提 交" onclick="settxPassword()" class="btn01"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>

        <div id="box6" style="display: none" class="amend">
            <div class="">
                <p class="tc mb20">为了您的账户安全，请定期更换交易密码，并确保交易密码设置与登录密码不同</p>
                <div class="form_info">
                    <ul class="cell">
                        <li style="margin-bottom: 0"></li>
                        <li class="tc ln30 orange f16" style="display:none;" id="sbUpdPswLi">
                           	 恭喜您，已经成功修改您的交易密码！3秒后将刷新此页面。。。<br>
                            <p></p></li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>原交易密码：<br/>
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" name="old" onchange="setSubmitFlg();"
                                       onselectstart="return false;" ondragenter="return false;" onpaste="return false;"
                                       class="text required " style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>新交易密码：
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" name="new" onchange="setSubmitFlg();" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致！"
                                       onselectstart="return false;" ondragenter="return false;" onpaste="return false;"
                                       class="text required cpassword-f" style="width:173px"/>
                                <p class="gray9 f12" tip><%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>确认交易密码：
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" name="cnew" onchange="setSubmitFlg();"
                                       onselectstart="return false;" ondragenter="return false;" onpaste="return false;"
                                       class="text required password-bf" style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input2" type="button" onclick="updatetxPassword()" value="提 交"
                               class="btn01 sumbitForme"/>
                    </div>
                    <div class="user_mod_gray p10 mt20">
				                        如果您在操作过程中出现问题，请点击页面顶侧在线客服，或拨打<%=configureProvider.getProperty(SystemVariable.SITE_NAME) %>
				                        客服电话：<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL)  %>
                    </div>
                </div>
            </div>
        </div>

        <div id="box7" style="display: none" class="amend">
            <%--找回密码--%>
            <div class="steps" id="ps1">
                <%if (!StringHelper.isEmpty(phonenumber)) { %>
                <div class="steps_pic step_01"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证手机号码</span>
                    <span class="ml30 pl5 mr30 pr5">2.重设交易密码</span>
                    <span>3.成功</span>

                    <div class="clear"></div>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>

                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">原手机号码 ：</div>
                            <div class="info"><%=phonenumber%>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>验证码：
                            </div>
                            <div class="info">
                                <input name="updatePasVerify" type="text"
                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code"
                                       style="width:173px;" />
									<span>
										<img alt="验证码" id="_verifyImg_0"
                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=updatePasw"
                                             alt="验证码" title="点击刷新" width="80" height="31"
                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updatePasw'"
                                             style="cursor: pointer;"/>
										<a href="javascript:void(0)"
                                           onclick="$('#_verifyImg_0').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updatePasw')"
                                           class="blue ml10 blue_line">换一张</a>
									</span>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <%} else {%>
                        <li style="display: none">
                            <div class="clear"></div>
                        </li>
                        <%
                            }
                        %>
                        <li>
                            <div class="til">
                                <span class="red">*</span>手机验证码：
                            </div>
                            <div class="info clearfix">
                                <input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                <input name="input" type="button" value="获取手机验证码" onclick="getoldcode(this);"
                                       class="btn05" id="yzmbtn5"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="pswone" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
                <%} else { %>
                <p style="margin-left: 200px;color: red;">请先绑定手机，才能找回交易密码!</p>
                <%} %>
            </div>
            <div class="steps" id="ps2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.重设交易密码</span>
                    <span>3.成功</span>

                    <div class="clear"></div>
                </div>
                <p class="tc mb20">为保障您的账户信息安全，在变更重要信息时需要进行验证。</p>
                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">
                                <span class="red">*</span>输入新交易密码：
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" onselectstart="return false;"
                                       ondragenter="return false;" onpaste="return false;" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致！"
                                       class="text required cpassword-f" style="width:173px"/>
                                <p class="gray9 f12" tip><%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>再次输入新交易密码：
                            </div>
                            <div class="info">
                                <input type="password" maxlength="20" onselectstart="return false;"
                                       ondragenter="return false;" onpaste="return false;"
                                       class="text  required password-bf" style="width:173px"/>
                                <p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                    <div class="tc">
                        <input name="input" type="button" value="下一步" checktype="pswtwo" class="btn01 coden"/>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>

            <div class="steps" id="ps3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                    <span class="highlight">1.验证手机号码</span>
                    <span class="highlight ml30 pl5 mr30 pr5">2.重设交易密码</span>
                    <span class="highlight">3.成功</span>
                    <div class="clear"></div>
                </div>
                <p class="tc mb20 orange">恭喜您，已经成功修改您的交易密码！3秒后将刷新此页面。。。</p>
            </div>
        </div>
    </li>
    <%} %>
    <%if(tg && !StringHelper.isEmpty(usrCustId) && escrow.equals("yeepay")){ %>
    <li class="item">
        <div class="con">
            <div class="mod01">交易密码</div>
            <div class="mod02">已设置</div>
            <div class="operation">
                <a href="<%=configureProvider.format(URLVariable.ESCROW_URL_RESETPASSWORD) %>" class="highlight">修改</a>
            </div>
        </div>
    </li>
    <%} %>
    <li class="item">
        <div class="con">
            <div class="mod01">密保问题</div>
            <%if (!StringHelper.isEmpty(data.isPwdSafety) && data.isPwdSafety.equals(T6118_F10.TG.name())) {%>
            <div class="mod02">已设置</div>
            <div class="operation">
                <a href="javascript:void(0);"
                   onclick="openShutManager(this,'box9',false,'取消修改&nbsp;|&nbsp;','修改&nbsp;|&nbsp;');clearInput();"
                   class="highlight" id="mbwtxg">修改 &nbsp;|&nbsp;
                </a>
                <a href="javascript:void(0);"
                   onclick="openShutManager(this,'box10',false,'取消找回','找回',<%=a %>);clearInput();"
                   class="highlight" id="mbwtzh">找回
                </a>
            </div>
            <% } else { %>
            <div class="mod02 red">未设置</div>
            <div class="operation">
                <a href="javascript:void(0);"
                   onclick="openShutManager(this,'box8',false,'取消设置','设置');clearInput();"
                   class="highlight">设置
                </a>
            </div>
            <% } %>
            <div class="clear"></div>
        </div>
        <%if (StringHelper.isEmpty(data.isPwdSafety) || data.isPwdSafety.equals(T6118_F10.BTG.name())) {%>
        <div id="box8" style="display: none" class="amend">
            <div class="problem">
                <ul>
                    <li style="margin-bottom: 0"></li>
                    <li class="tc ln30 orange" style="display:none;" id="mbLi">恭喜您，已经成功设置您的密保问题！3秒后将刷新此页面。。。<br>
                        <p></p>
                    </li>
                    <%
                        for (int index = 0; index < 3; index++) {
                            //Object qType = objs[index];
                    %>
                    <li>
                        <span class="red"></span>问题<%= index + 1 %>：
                        <select style="width: 165px" name="question<%= index + 1 %>" id="question<%= index + 1 %>"
                                class="select_style question">
                            <option value="">请选择密保问题</option>
                            <% //List<PwdSafetyQuestion> safeQuestions = pwdSafeQuestions.get(qType);
                                for (PwdSafetyQuestion ques : questionList) {
                            %>
                            <option value="<%=ques.id %>"><%=ques.descr %></option>
                            <%
                                }
                            %>
                        </select>
                        <input type="text" maxlength="20" class="text_style text_w190 issue_answer" data="show"
                               id="answerText<%=index+1 %>" value="请输入问题答案" name="answerText<%= index + 1 %>"/>
                        <input type="text" maxlength="20" data="hide" class="text_style text_w190 required answerIn"
                               id="answer<%=index+1 %>" name="answer<%= index + 1 %>" style="display:none;"/>
                        <p id="answer<%= index + 1 %>Msg" class="prompt"></p>
                        <!-- <p tip style="left:450px;"></p> -->
                        <p errortip class="" style="display: none;left:450px;"></p>
                        <div class="clear"></div>
                    </li>
                    <% } %>
                </ul>
                <div class="tc mt20">
                    <input id="safeSubmit" type="button" value="提 交" class="btn01"/>
                </div>
            </div>
        </div>
        <%}else{ %>
        <div id="box9" style="display: none" class="amend">
        	<div class="steps" id="mbwtSteps1">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">                                      
                    <span class="highlight">填写原问题答案</span>
                    <span class="ml30 pl5 mr30 pr5">设置新问题及答案</span>
                    <span>完成</span>
                </div>
                <div class="form_info clearfix">
                    <ul class="cell clearfix"> 
                    	<%for(int i = 0 ; i<questions.size() ; i++){
                    	    PwdSafetyQuestion ques = (PwdSafetyQuestion) questions.get(i);
                    	%>
                        <li>
                        	<div class="til pb5">问题<%=i+1 %>：</div>
                            <div class="info pb5"><%=ques.descr %></div>
                            <div class="til"><span class="red">*</span>答案：</div>
                            <div class="info">
                            	<input type="text" name="answer_<%=i+1 %>" qid="<%=ques.id %>" maxlength="20" class="text_style text_w190 required answerIn"/>
                            	<p tip class="gray">请输入密保问题答案</p>
                    			<p errortip class="prompt" style="display:none"></p>
                            </div>
                        </li>
                        <%} %>
                    </ul>
                    <div class="tc mt20">
						<input name="input" type="button" value="下一步" checktype="securityPwd" class="btn01 coden"/>
					</div>
                </div>
            </div>
            <div class="steps" id="mbwtSteps2" style="display:none;">
                <div class="steps_pic step_02"></div>
                <div class="steps_text">
                    <span class="highlight">填写原问题答案</span>
                    <span class="highlight ml30 pl5 mr30 pr5">设置新问题及答案</span>
                    <span>完成</span>
                </div>
                <div class="form_info clearfix">
                    <ul class="cell clearfix"> 
                    	<%for(int x= 1 ; x <=3 ;x++  ){ %>
	                        <li>
	                        	<div class="til pb10">问题<%=x %>：</div>
	                            <div class="info pb10">
	                            	<select name="question<%=x+3%>" id="question<%=x+3%>" class="select_style question" style="width: 212px">
	                            		<option value="">请选择密保问题</option>
	                            		<%for(PwdSafetyQuestion ques : questionList){ %>
	                            		<option value="<%=ques.id %>"><%=ques.descr %></option>
	                            		<%} %>
	                            	</select>
	                            </div>
	                            <div class="til">
	                            	<span class="red">*</span>答案：
	                            </div>
	                            <div class="info">
	                            	<input type="text" maxlength="20" value="请输入问题答案" name="" class="text_style issue_answer fl" data="show" style="width: 200px"/>
                        			<input type="text" maxlength="20" data="hide" class="text_style required answerIn fl" style="display:none; width: 200px"/>
	                            	<p tip></p>
	                    			<p errortip class="prompt" style="display:none"></p>
	                            </div>
	                        </li>
                        <%} %>
                    </ul>
                    <div class="tc mt20">
                    	<input name="input" type="button" value="下一步" id="safeUpdateSubmit" class="btn01"/>
                    </div>
                </div>
            </div>
            <div class="steps" id="mbwtSteps3" style="display:none;">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                   <span class="highlight">填写原问题答案</span>
                    <span class="highlight ml30 pl5 mr30 pr5">设置新问题及答案</span>
                    <span class="highlight">完成</span>
                </div>
                <p class="tc complete_ico mt20"></p>
                <p class="tc mb20 highlight mt30">密保问题设置成功，该页面将于<span>3</span>秒后刷新</p>                      
            </div>
        </div>
        <div id="box10" style="display: none" class="amend">
            <div class="steps" id="securityStep1">
                <div class="steps_pic step_01"></div>
                <div class="steps_text">                                         
                    <span class="highlight">安全认证</span>
                    <span class="ml30 pl5 mr30 pr5">重设密保问题及答案</span>
                    <span>完成</span>
                </div>
                <div class="form_info clearfix">
                    <ul class="cell clearfix"> 
                        <li>
                            <div class="til">手机号码：</div>
                            <div class="info"><%=phonenumber %></div>
                            <div class="clear"></div>
                        </li>
                        <%
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                        %>
                        <li>
                            <div class="til"><span class="red">*</span>验证码：</div>
                            <div class="info">
                            	<input type="text" name="updateSecurityVerify" maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" style="width:173px;"  />
                            	<span>
									<img alt="验证码" id="_verifyImg_10"
                                        src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=updateSecurity"
                                        alt="验证码" title="点击刷新" width="80" height="31"
                                        onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updateSecurity'"
                                        style="cursor: pointer;"/>
									<a href="javascript:void(0)"
                                        onclick="$('#_verifyImg_10').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=updateSecurity')"
                                        class="blue ml10 blue_line">换一张</a>
								</span>
								<p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                        </li>
                        <%} else {%>
	                    <li style="display: none">
	                        <div class="clear"></div>
	                    </li>
	                    <%}%>
                        <li>
                            <div class="til"><span class="red">*</span>手机验证码：</div>
                            <div class="info">
                            	<input type="text" class="text code p_text verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                            	<input name="input" type="button" value="获取手机验证码" onclick="getSecurityCode(this);" class="btn05"/>
                            	<p tip></p>
                                <p errortip class="" style="display: none"></p>
                            </div>
                        </li>
                    </ul>
                    <div class="tc mt20">
                    	<input name="input" type="button" value="下一步" checktype="securityPhone" class="btn01 coden"/>
                    </div>
                </div>
            </div>
            <div class="steps" style="display:none;" id="securityStep2">
	            <div class="steps_pic step_02"></div>
	            <div class="steps_text">
	                <span class="highlight">安全认证</span>
	                <span class="highlight ml30 pl5 mr30 pr5">重设密保问题及答案</span>
	                <span>完成</span>
	            </div>
				<div class="form_info clearfix">
               		<ul class="cell clearfix"> 
	                   <%for(int x= 1 ; x <=3 ;x++  ){ %>
	                        <li>
	                        	<div class="til pb10">问题<%=x %>：</div>
	                            <div class="info pb10">
	                            	<select name="question<%=x+6 %>" id="question<%=x+6 %>" class="select_style question" style="width: 212px">
	                            		<option value="">请选择密保问题</option>
	                            		<%for(PwdSafetyQuestion ques : questionList){ %>
	                            		<option value="<%=ques.id %>"><%=ques.descr %></option>
	                            		<%} %>
	                            	</select>
	                            </div>
	                            <div class="til">
	                            	<span class="red">*</span>答案：
	                            </div>
	                            <div class="info">
	                            	<input type="text" maxlength="20" value="请输入问题答案" name="" class="text_style issue_answer fl" data="show" style="width: 200px"/>
                        			<input type="text" maxlength="20" data="hide" name="" class="text_style required answerIn fl" style="display:none; width: 200px"/>
	                            	<p tip></p>
	                    			<p errortip class="prompt" style="display:none"></p>
	                            </div>
	                        </li>
                        <%} %>
	               	</ul>
                   	<div class="tc mt20">
                   		<input name="input" type="button" value="下一步" id="safeFindSubmit" class="btn01"/>
               		</div>
               </div>                            
           	</div>
            <div class="steps"  style="display:none;" id="securityStep3">
                <div class="steps_pic step_03"></div>
                <div class="steps_text">
                	<span class="highlight">安全认证</span>
                    <span class="highlight ml30 pl5 mr30 pr5">重设密保问题及答案</span>
                    <span class="highlight">完成</span>
                </div>
                <p class="tc complete_ico mt20"></p>
                <p class="tc mb20 highlight mt30">密保问题设置成功，该页面将于<span>3</span>秒后刷新</p>                               
            </div>
        </div>
        <%} %>
    </li>
    <%
        Boolean isOpenRisk = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        Boolean isAssessment = false;
        T6147 t6147 = null;
        if(isOpenRisk) {
            RiskQuesManage riskQuesManage = serviceSession.getService(RiskQuesManage.class);
            t6147 = riskQuesManage.getMyRiskResult();
            if(null != t6147){
                isAssessment = true;
            }
            int count = riskQuesManage.leftRiskCount();
    %>
    <li class="item">
        <div class="con">
            <div class="mod01">风险评估</div>
            <div class="mod02"><%=isAssessment ? t6147.F04.getChineseName() : "未评估" %></div>
            <div class="operation">
                <%if(count>0){%><span class="f12 mr5 red">(今年还可再评估<%=count%>次)</span><%}%>
                <%if(t6147==null || count>0){%>
                <a href="/user/policy/riskAssessment.htm" class="highlight" target="_blank">评估</a>
                <%}%>

            </div>
        </div>
    </li>
    <%}%>

   <%--  <%
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        if(isHasGuarantor){
            T6125 t6125 = applyGuarantorManage1.getGuanterInfo();
    %>
    <li class="item">
        <div class="con">
            <div class="mod01">担保申请</div>
            <div class="mod02" style="border: white solid 1px;">
                <%if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>我已经是担保方（担保码：<%=t6125.F03%>）
                <span class="hover_tips ml10">
                    <div style="display: none;" class="hover_tips_con">
                        <div class="arrow"></div>
                        <div class="border">
                            <p>1.您当前已成为担保人，可以为其他个人或企业进行担保。</p>
                            <p>2.担保方不能担保自己的借款项目。</p>
                            <p>3.担保方不能投标自己担保的项目。</p>
                            <p>4.若您想取消担保权限，请联系客服人员。</p>
                        </div>
                    </div>
                 </span>
                <%}%>
            </div>

            <div class="operation">
              <%
                    if(t6125.F05 == T6125_F05.QXDCL){
                %>
                <a href="javascript:void(0)" class="btn01 mt10 disabled">取消担保审核中</a>
                <%}else{%>
                <a href="javascript:void(0)" onclick="cancelGuarantor()" class="btn01 mt10">取消担保</a>
                <%}%>

            </div>
            <%if(t6125 == null || t6125.F05 != T6125_F05.SQCG){%>
            <div class="operation">
                <%if(t6125 != null && t6125.F05 == T6125_F05.SQDCL){%>
                <a href="javascript:void(0)" class="btn01 mt10 btn_gray" style="cursor: default;">担保审核中</a>
                <%}else{
                    if((tg) && StringHelper.isEmpty(usrCustId)){
                %>
                <a href="javascript:void(0)" class="btn01 mt10 btn_gray">我要担保</a>
                <%}else{
                %>
                <a href="javascript:void(0)" onclick="applyGuarantor()" class="btn01 mt10">我要担保</a>
                <%}}%>
                <span class="hover_tips ml10">
                    <div style="display: none;" class="hover_tips_con">
                        <div class="arrow"></div>
                        <div class="border">
                            <%
                                int index = 0;
                                if(tg){
                            %>
                            <p>1.您必须在第三方注册托管账户成功后，才能够申请担保。</p>
                            <%index++;}%>
                            <p><%=++index%>.如果有逾期的借款项目不能申请担保。</p>
                            <p><%=++index%>.申请担保后，可以为其他个人或企业进行担保。</p>
                            <p><%=++index%>.担保方不能担保自己的借款项目。</p>
                            <p><%=++index%>.担保方不能投标自己担保的项目。</p>
                        </div>
                    </div>
                 </span>
            </div>
            <%}else if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>
            <div class="operation">
                <a href="javascript:void(0);" onclick="openShutManager(this,'box11',false,'取消修改','修改')" class="highlight">修改</a>
            </div>
            <%}%>
        </div>
        <%if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>
          <div id="box11" style="display: none" class="form_info amend">
            <ul class="cell">
                <li class="tc ln30 orange f16" style="display:none;" id="sbDbmLi">恭喜您，修改成功！3秒后将刷新此页面。。。<br>
                    <p></p></li>
                <li>
                    <div class="til">
                        <span class="red">*</span>担保码：
                    </div>
                    <div class="info">
                        <input type="text" value="" name="dbm" maxlength="7" class="text yhgl_ser fl mr10 w200 required max-length-7"
                               mtest="/^[a-zA-Z]([A-Za-z0-9]{6})$/"  mtestmsg="请输入合法的担保码！" style="width:173px"/>
                        <p tip></p>
                        <p errortip class="" style="display: none"></p>
                    </div>
                    <div class="clear"></div>
                </li>
            </ul>
            <div class="tc">
                <input name="input" type="button" value="提 交" class="btn01" onclick="updateDbm(this)"/>
            </div>
        </div>
        <%}%>
    </li>
    <%}%> --%>
</ul>

<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/zhankai.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
	var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	<%--用户手机号码--%>
	var phoneNumber = "<%=a%>";
	<%--是否需要图片验证码--%>
	var flagSfxyyzm = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
	<%--是否托管--%>
	var isTg = "<%=tg%>";
	<%--用户登录名--%>
	var userName= "<%=data.username%>";
	<%--密码匹配规则--%>
	var myregPsw = <%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>;
	<%--密码匹配规则提示语--%>
    var myregPswMsg = "<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>";
    <%--身份证号码是否为空--%>
    var isIdCardEmpty=<%=StringHelper.isEmpty(data.idCard)%>;
    <%--身份证号码是否认证通过--%>
    var isIdCard = <%=T6141_F04.TG.name().equals(data.isIdCard)%>;
    <%--手机号码认证是否通过返回[TG,BTG]--%>
    var isPhone = "<%=data.isPhone%>";
    <%--充值是否必须提现[交易]密码--%>
    var chargeMustWithdrawPwd = <%=CHARGE_MUST_WITHDRAWPSD%>;
    var escrow = "<%=escrow%>";
    <%--交易密码是否为空--%>
    var isTxPasswordEmpty=<%=StringHelper.isEmpty(data.txpassword)%>;
    <%--邮箱是否必要认证--%>
    var isMustMail =<%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL))%>;
    <%--邮箱是否通过认证--%>
    var isMail = <%=T6141_F04.TG.name().equals(data.isEmil)%>;
    <%--身份证号码--%>
    var dataSfzh = "<%=StringHelper.decode(data.sfzh)%>";
    <%--登录地址--%>
	var _loginUrl="<%=controller.getViewURI(request, Login.class)%>" ;
	<%--获取短信/邮件验证码地址--%>
	var _sendUrl="<%=controller.getURI(request, Send.class)%>";
	<%--token key--%>
	var _tokenName= "<%=FormToken.parameterName()%>";
	<%--绑定手机号码地址--%>
	var _bindPhoneUrl="<%=controller.getURI(request, BindPhone.class)%>";
	<%--修改交易密码地址--%>
	var _updateTxPassword="<%=controller.getURI(request, UpdatetxPassword.class)%>";
	<%--设置交易密码地址--%>
	var _setTxPassword="<%=controller.getURI(request, SettxPassword.class)%>";
	<%--实名认证请求URL--%>
	var _updateName="<%=controller.getURI(request, UpdateName.class)%>";
	<%--密码修改请求URL--%>
    var _updatePassword = "<%=controller.getURI(request, UpdatePassword.class)%>";
    var _checkEmailUrl="<%=controller.getURI(request, CheckEmail.class)%>";
    var _checkCodeUrl="<%=controller.getURI(request, CheckCode.class)%>";
    var _checkNewCodeUrl = "<%=controller.getURI(request, CheckNewCode.class)%>";
    var _checkPasswordUrl = "<%=controller.getURI(request, Checkpassword.class)%>";
    var _pdatetxPasswordajxUrl = "<%=controller.getURI(request, PdatetxPasswordAjax.class)%>";
    var _verifyCommonUrl = "<%=controller.getURI(request, VerifyCommon.class)%>";
    var _pwdSafeServletUrl = "<%=controller.getURI(request, PwdSafeServlet.class)%>";
    var _queryQuestionUrl="<%=controller.getURI(request, QueryQuestion.class)%>";
    var _checkMbwtUrl = "<%=controller.getURI(request, CheckMbwt.class)%>";
    var _applyQuarantorUrl = "<%=controller.getURI(request, ApplyGuarantor.class)%>";
    var _isNetSign = <%=applyGuarantorManage1.isNetSign()%>;
    var _netSignUrl = "<%=configureProvider.format(URLVariable.USER_NETSIGN_URL)%>";
    
    <%--修改担保码请求URL--%>
	var _updateDbm="<%=controller.getURI(request, UpdateDbm.class)%>";
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/account/safetymsg.js"></script>
<%--
</body>
</html>--%>
