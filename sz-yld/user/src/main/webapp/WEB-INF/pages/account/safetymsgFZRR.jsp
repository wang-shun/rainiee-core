<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.user.servlets.account.*" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.S61.enums.*" %>
<%@page import="com.dimeng.p2p.account.user.service.achieve.PwdSafeCacheManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion" %>
<%@page import="com.dimeng.p2p.account.user.service.UserManage" %>
<%@page import="java.util.*" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@ page import="com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6125" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/dialog.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "AQXX";
    SafetyManage userManage = serviceSession.getService(SafetyManage.class);
    Safety data = userManage.get();
    if(T6110_F06.ZRR.name().equals(data.userType))
    {
        controller.sendRedirect(request, response, controller.getViewURI(request, UserBases.class)+"?userBasesFlag=1");
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
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    UserManage manage = serviceSession.getService(UserManage.class);
    String usrCustId = manage.getUsrCustId();
    PwdSafeCacheManage pwdSafeCacheManage = serviceSession.getService(PwdSafeCacheManage.class);
    List<PwdSafetyQuestion> questions = pwdSafeCacheManage.getPasswordQuestionByUser(serviceSession.getSession().getAccountId());
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);   
    boolean CHARGE_MUST_WITHDRAWPSD = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
%>
<div class="main_bg clearfix">
    <input type="hidden" name="phoneFlg" id="phoneFlg" value="1"/>
    <input type="hidden" name="emilFlg" id="emilFlg" value="1"/>
    <input id="newPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
    <input id="passwordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>
    <input id="newTxPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_TXPASSWORD_REGEX);%>"/>
    <input id="txPasswordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%>"/>
    <div id="token">
        <%=FormToken.hidden(serviceSession.getSession()) %>
    </div>
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div class="basic_info">
                    <div class="user_til "><i class="icon"></i><span class="gray3 f18">安全信息</span></div>
                    <%
                        String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(ermsg)) {
                    %>
                    <ul class="xx_li" id="kjd">
                        <li style="text-align: center;color: red;">
                            <%StringHelper.filterHTML(out, ermsg);%>
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
                            <%if (!StringHelper.isEmpty(data.isIdCard) && data.isIdCard.equals(T6141_F04.TG.name())) { %>
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
                                <div class="mod02 red">未设置</div>
                                <div class="operation">
                                    <a href="javascript:void(0);"
                                       onclick="openShutManager(this,'box1',false,'取消设置','设置')"
                                       class="highlight">设置</a>
                                </div>
                            </div>
                            <div class="clear"></div>
                            <div id="box1" style="display: none" class="form_info amend">
                                <ul class="cell">
                                    <li class="tc ln30 orange" style="display:none;" id="sbNameLi">
                                       		 恭喜您，实名认证成功！3秒后将刷新此页面。。。<br>
                                        <p></p></li>
                                    <li>
                                        <div class="til">
                                            <span class="red">*</span>真实姓名：
                                        </div>
                                        <div class="info">
                                            <input type="text" name="name" class="text yhgl_ser fl mr10 w200 required"
                                                   mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法的姓名"/>

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
                                            <input type="text" name="idCard"
                                                   class="text yhgl_ser w200 required idcard"/>

                                            <p tip></p>

                                            <p errortip class="" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                </ul>
                                <div class="tc">
                                    <input name="input" type="button" value="提 交" class="btn01"
                                           onclick="updateName(this)"/>
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
                                <%
                                } else {
                                    type = "设置";
                                %>
                                <div class="mod02 red">未设置</div>
                                <%} %>
                                <div class="operation">
                                    <a href="javascript:void(0);"
                                       onclick="openShutManager(this,'box2',false,'取消<%=type%>','<%=type%>')"
                                       class="highlight"><%=type%>
                                    </a>
                                </div>
                            </div>
                            <%-- <form action="<%=controller.getURI(request, UpdatePassword.class)%>" class="form2" method="post"> --%>
                            <div id="box2" style="display: none" class="form_info amend">
                                <ul class="cell">
                                    <li class="tc ln30 orange" style="display:none;" id="sbPswLi">
                                        	恭喜您，登录密码修改成功！3秒后将刷新此页面。。。<br>
                                        <p></p></li>
                                    <li>
                                        <div class="til">
                                            <span class="red">*</span> 原密码：
                                        </div>
                                        <div class="info">
                                            <input id="oldPassword" type="password" name="old" maxlength="20"
                                                   onselectstart="return false;" ondragenter="return false;"
                                                   onpaste="return false;"
                                                   class="text yhgl_ser w200 fl mr10 required min-length-6" autocomplete="off"/>
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
                                            <input id="passwordOne" type="password" name="new" fromname="form2"
                                                   maxlength="20" onselectstart="return false;"
                                                   ondragenter="return false;" onpaste="return false;" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致"
                                                   class="text yhgl_ser w200 fl mr10 required password-a min-length-6 max-length-20"
                                                   style="float: left;" autocomplete="off"/>
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
                                            <input id="passwordTwo" type="password" name="news" maxlength="20"
                                                   onselectstart="return false;" ondragenter="return false;"
                                                   onpaste="return false;"
                                                   class="text yhgl_ser w200 fl mr10 required password-b min-length-6 max-length-20"
                                                   style="float: left;" autocomplete="off"/>
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
                                <div class="clear"></div>
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
                                       onclick="openShutManager(this,'box3',false,'取消<%=type%>','<%=type%>');clearTimes(2)"
                                       class="highlight"><%=type%>
                                    </a>
                                </div>
                            </div>
                            <div class="clear"></div>
                            <div id="box3" style="display: none" class="amend">
                                <!-- <div class="xx_txmm" id="notBindPhone_div" style="display: none">
									<p class="mb20">
										<span class="xx_ts"></span><span class="f20">请先绑定手机。</span>
									</p>
									<div class="clear"></div>
									<input name="input" type="button" value="返 回" onclick="localch('notBindPhone_div', 'mt1');" class="btn01"
										style="margin: auto" />
								 </div> -->
                                <div class="binding" id="mt1" <%if(type.equals("设置")){ %>style="display: none"<%} %>>
                                    <p class="tc">您正在修改的绑定邮箱是<span class="orange"><%=emil %></span></p>
                                    <ul class="clearfix">
                                        <li class="w111">
                                            <a href="javascript:void(0);" class="icon icon01"></a>
                                            <p>
                                                <a href="javascript:void(0);">通过原邮箱修改</a>
                                            </p>
                                        </li>
                                        <li class="w111">
                                            <a href="javascript:void(0);" class="icon icon02"></a>
                                            <p>
                                                <a href="javascript:void(0);">通过手机号码修改</a>
                                            </p>
                                        </li>
                                    </ul>
                                </div>
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
                                                <div class="til"><span class="red">*</span>验证邮箱：</div>
                                                <div class="info">
                                                    <input id="oldEmailCheck" type="text" class="text yhgl_ser required e-mail"/>
                                                    <p tip></p>
                                                    <p errortip class="error_tip" style="display: none"></p>
                                                </div>
                                            </li>
                                            <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>验证码：
                                                </div>
                                                <div class="info">
                                                    <input name="eUpdateVerify" type="text"
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                            <input name="input" type="button" value="下一步" checktype="oldone"
                                                   class="btn01 coden"/>
                                        </div>
                                    </div>
                                </div>

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
                                                <div class="til"><span class="red">*</span>验证邮箱：</div>
                                                <div class="info">
                                                    <input type="text" class="text sendemil required e-mail"
                                                           maxlength="26"/>

                                                    <p tip></p>

                                                    <p errortip class="error_tip" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                            <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>验证码：
                                                </div>
                                                <div class="info">
                                                    <input name="eNewVerify" type="text"
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                            <input name="input" type="button" value="发送验证邮件"
                                                   class="btn01 emilsend_email coden"/>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                    <div id="emailSendSuccess3" style="display: none">
                                        <p class="tc mb20">激活邮件已发送至您的邮箱：
                                        	<span id="emailSendSuccess3Email" class="red"></span>
								                                            请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>
								                                            小时内<br/>
								                                            登录邮箱并点击邮件中的链接，完成验证。<br/></p>
                                        <p class="tc mb20">没收到？<a onclick="showEmailSend('3')" class="red pl5"
                                                                  style="cursor: pointer;">再次发送</a></p>
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
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                    <p errortip class="error_tip" style="display: none"></p>
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
                                                <div class="info">
                                                    <input type="text" class="text yhgl_ser code verify-code" maxlength="<%=systemDefine.getVerifyCodeLength()%>" style="width:173px;"/>
                                                    <input name="input" type="button" value="获取手机验证码"
                                                           onclick="epupdate(this);" class="btn05" id="yzmbtn2"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="epoldone"
                                                   class="btn01 coden"/>
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
                                                <div class="til">验证新邮箱：</div>
                                                <div class="info">
                                                    <input type="text" class="text sendemil required e-mail"
                                                           maxlength="26"/>

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
                                                    <input name="epNewEmailVerify" type="text"
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                    <p errortip class="error_tip" style="display: none"></p>
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
                                            <input name="input" type="button" value="发送验证邮件"
                                                   class="btn01 emilsend_phone coden"/>
                                        </div>
                                    </div>

                                    <div id="emailSendSuccess2" style="display: none">
                                        <p class="tc mb20">激活邮件已发送至您的邮箱：
                                        	<span id="emailSendSuccess2Email" class="red"></span>
								                                            请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>
								                                            小时内<br/>
								                                            登录邮箱并点击邮件中的链接，完成验证。<br/></p>
                                        <p class="tc mb20">没收到？
                                        	<a onclick="showEmailSend('2')" class="red pl5" style="cursor: pointer;">再次发送</a>
                                        </p>
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
                                <div id="emailSend1" class="form_info"
                                     <%if(type.equals("修改")){ %>style="display: none"<%} %>>
                                    <ul class="cell">
                                        <li class="tc mb20 orange" style="display:none;" id="succLi">
                                           	 恭喜您，已经成功绑定您的邮箱！3秒后将刷新此页面。。。<br>
                                            <p></p>
                                        </li>
                                        <li>
                                            <div class="til"><span class="red">*</span>邮箱：</div>
                                            <div class="info">
                                                <input name="bemil" type="text" class="text  required e-mail" maxlength="26"/>
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
                                                <input name="bindEmailVerify" type="text"
                                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                       class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                    <div class="tc"><input name="input" type="button" onclick="submitBandEmil()"
                                                           value="发送验证邮件" class="btn01 sumbitForme"/></div>
                                    <div class="clear"></div>
                                </div>
                                <div id="emailSendSuccess1" style="display: none">
                                    <p class="tc mb20">激活邮件已发送至您的邮箱：
                                    	<span id="emailSendSuccess1Email" class="red"></span>
								                                        请在<%=configureProvider.getProperty(EmailVariavle.EMAIL_LINK_VALID_PERIOD)%>
								                                        小时内<br/>
								                                        登录邮箱并点击邮件中的链接，完成验证。<br/></p>
                                    <p class="tc mb20">没收到？
                                    	<a onclick="showEmailSend('1')" class="red pl5" style="cursor: pointer;">再次发送</a>
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
                                <%
                                } else {
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
                            </div>
                            <div class="clear"></div>
                            <div id="box4" style="display: none" class="amend">
                                <!-- <div class="xx_txmm" id="pr1" style="display: none">
									<p class="mb20">
										<span class="xx_ts"></span><span class="f20">请先设置交易密码。</span>
									</p>
									<div class="clear"></div>
									<input name="input" type="button" onclick="localch('pr1', 'ip1');" value="返 回" class="btn01"
										style="margin: auto" />
								</div>
								<div class="xx_txmm" id="pr2" style="display: none">
									<p class="mb20">
										<span class="xx_ts"></span><span class="f20">请先设置实名认证。</span>
									</p>
									<div class="clear"></div>
									<input name="input" type="button" value="返 回" onclick="localch('pr2', 'ip1');" class="btn01"
										style="margin: auto" />
								</div> -->
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
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                <div class="info">
                                                    <input type="text" class="text yhgl_ser code verify-code"
                                                           style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                                    <input name="input" type="button" value="获取手机验证码"
                                                           onclick="pupdate(this);" class="btn05" id="yzmbtn5"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="poldone"
                                                   class="btn01 coden"/>
                                        </div>
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
                                                    <input type="text" class="text sendphone" style="width:173px;"/>
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
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                <div class="info">
                                                    <input type="text" class="text yhgl_ser code verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                                    <input name="input" type="button" value="获取手机验证码"
                                                           class="btn05 phonesend_phone clearTimedes"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="pnewtwo"
                                                   class="btn01 coden"/>
                                        </div>
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
                                                    <input type="text" class="text yhgl_ser "/>

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
                                                    <input type="password" class="text yhgl_ser "
                                                           onselectstart="return false;" ondragenter="return false;"
                                                           onpaste="return false;" autocomplete="off"/>

                                                    <p tip></p>

                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                            <%}%>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="ipoldone"
                                                   class="btn01 coden"/>
                                        </div>
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
                                                    <input type="text" class="text sendphone"/>

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
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                <div class="info">
                                                    <input type="text" class="text yhgl_ser code verify-code" style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                                    <input name="input" type="button" value="获取手机验证码"
                                                           class="btn05 phonesend_card clearTimedes"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="ipnewtwo"
                                                   class="btn01 coden"/>
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

                                <%-- <form action="<%=controller.getURI(request, BindPhone.class)%>" method="post" class="form12"> --%>
                                <div class="form_info" <%if(type.equals("修改")){ %>style="display: none"<%} %>>
                                    <ul class="cell">
                                        <li class="tc orange" style="display:none;" id="sbPhoneLi">
                                           	 恭喜您，已经成功绑定您的手机号码！3秒后将刷新此页面。。。<br>
                                            <p></p></li>
                                        <li>
                                            <div class="til"><span class="red">*</span>手机号：</div>
                                            <div class="info">
                                                <input name="binphpne" type="text" class="text required phonenumber "/>
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
                                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                       class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                            <div class="info">
                                                <input name="bphoneCode" type="text" class="text yhgl_ser required verify-code"
                                                       style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                                <input name="input" type="button" onclick="bandPhone(this)"
                                                       value="获取手机验证码" class="btn05"/>
                                                <p tip></p>
                                                <p errortip class="" style="display: none"></p>
                                            </div>
                                            <div class="clear"></div>
                                        </li>
                                    </ul>
                                    <div class="tc">
                                    	<input name="input" type="button" onclick="submitBandPhone(this)" value="提交" class="btn01 sumbitForme"/>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </li>
                        <% if(CHARGE_MUST_WITHDRAWPSD){%>
                        <li class="item">
                            <div class="con">
                                <div class="mod01">交易密码</div>
                                <%if (!StringHelper.isEmpty(data.txpassword)) {%>
                                <div class="mod02">已设置</div>
                                <div class="operation">
                                    <a id="jymmxg" href="javascript:void(0);"
                                       onclick="openShutManager(this,'box6',false,'取消修改&nbsp;|&nbsp;','修改&nbsp;|&nbsp;');clearTimes(5);"
                                       class="highlight">修改 &nbsp;|&nbsp;</a>
                                    <a id="jymmzh" href="javascript:void(0);"
                                       onclick="openShutManager(this,'box7',false,'取消找回','找回');clearTimes(5);"
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
                            </div>
                            <div class="clear"></div>
                            <div id="box5" style="display: none" class="amend">
                                <div class="">
                                    <p class="tc mb20">为了您的账户安全，请定期更换交易密码，并确保交易密码设置与登录密码不同</p>

                                    <div class="form_info">
                                        <ul class="cell">
                                            <li style="margin-bottom: 0"></li>
                                            <li class="tc mb20 orange" style="display:none;" id="sbSetPswLi">
                                               	 恭喜您，已经成功设置您的交易密码！3秒后将刷新此页面。。。<br>
                                                <p></p>
                                            </li>
                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>交易密码：<br/>
                                                </div>
                                                <div class="info">
                                                    <input type="password" maxlength="20" name="new"
                                                           onchange="setSubmitFlg();" onselectstart="return false;"
                                                           ondragenter="return false;" onpaste="return false;" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致"
                                                           class="text yhgl_ser required cpassword-f" autocomplete="off"/>
                                                    <p tip></p>
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
                                                    <input type="password" maxlength="20" name="cnew"
                                                           onchange="setSubmitFlg();" onselectstart="return false;"
                                                           ondragenter="return false;" onpaste="return false;"
                                                           class="text yhgl_ser  required password-bf" autocomplete="off"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="提 交" onclick="settxPassword()" class="btn01"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="box6" style="display: none" class="amend">
                                <div class="">
                                    <p class="tc mb20">为了您的账户安全，请定期更换交易密码，并确保交易密码设置与登录密码不同</p>
                                    <div class="form_info">
                                        <ul class="cell">
                                            <li style="margin-bottom: 0"></li>
                                            <li class="tc ln30 orange " style="display:none;" id="sbUpdPswLi">
                                                	恭喜您，已经成功修改您的交易密码！3秒后将刷新此页面。。。<br>
                                                <p></p>
                                            </li>
                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>原交易密码：<br/>
                                                </div>
                                                <div class="info">
                                                    <input type="password" maxlength="20" name="old"
                                                           onchange="setSubmitFlg();" onselectstart="return false;"
                                                           ondragenter="return false;" onpaste="return false;"
                                                           class="text yhgl_ser required " autocomplete="off"/>
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
                                                    <input type="password" maxlength="20" name="new"
                                                           onchange="setSubmitFlg();" onselectstart="return false;"
                                                           ondragenter="return false;" onpaste="return false;"
                                                           class="text yhgl_ser required cpassword-f" autocomplete="off"/>
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
                                                    <input type="password" maxlength="20" name="cnew"
                                                           onchange="setSubmitFlg();" onselectstart="return false;"
                                                           ondragenter="return false;" onpaste="return false;"
                                                           class="text yhgl_ser  required password-bf" autocomplete="off"/>

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
                                                           maxlength="<%=systemDefine.getVerifyCodeLength()%>"
                                                           class="text yhgl_ser code verify-code" style="width:173px;"/>
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
                                                <div class="info">
                                                    <input type="text" class="text yhgl_ser code verify-code"
                                                           style="width:173px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                                                    <input name="input" type="button" value="获取手机验证码"
                                                           onclick="getoldcode(this);" class="btn05" id="yzmbtn5"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="pswone"
                                                   class="btn01 coden"/>
                                        </div>
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
                                                           ondragenter="return false;" onpaste="return false;" mntest="/^<%=data.username %>$/" mntestmsg="密码不能与用户名一致"
                                                           class="text yhgl_ser  required cpassword-f" autocomplete="off"/>

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
                                                           class="text yhgl_ser  required password-bf" autocomplete="off"/>
                                                    <p tip></p>
                                                    <p errortip class="" style="display: none"></p>
                                                </div>
                                                <div class="clear"></div>
                                            </li>
                                        </ul>
                                        <div class="tc">
                                            <input name="input" type="button" value="下一步" checktype="pswtwo" class="btn01 coden"/>
                                        </div>
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
                                <%if (!StringHelper.isEmpty(data.isPwdSafety) && T6118_F10.TG.name().equals(data.isPwdSafety)) {%>
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
                            </div>
                            <div class="clear"></div>
                            <% if (StringHelper.isEmpty(data.isPwdSafety) || T6118_F10.BTG.name().equals(data.isPwdSafety)) { %>
                            <div id="box8" style="display: none" class="amend">
                                <div class="problem">
                                    <ul>
                                        <li style="margin-bottom: 0"></li>
                                        <li class="tc ln30 orange" style="display:none;" id="mbLi">
                                          	  恭喜您，已经成功设置您的密保问题！3秒后将刷新此页面。。。<br>
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
				                            <option value="<%=ques.id %>"><%=ques.descr %>
				                            </option>
				                            <%
				                                }
				                            %>
				                        </select>
				                        <input type="text" maxlength="20" class="text_style text_w190 issue_answer" data="show"
				                               id="answerText<%=index+1 %>" value="请输入问题答案" name="answerText<%= index + 1 %>"/>
				                        <input type="text" maxlength="20" data="hide" class="text_style text_w190 required answerIn"
				                               id="answer<%=index+1 %>" name="answer<%= index + 1 %>" style="display:none;"/>
				                        <p id="answer<%= index + 1 %>Msg" class="prompt"></p>
				                        <p tip style="left:450px;"></p>
				                        <p errortip class="prompt" style="display: none;left:450px;"></p>
				                        <div class="clear"></div>
				                    </li>
				                    <% } %>
                                    </ul>
                                    <div class="tc">
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
                        <%-- <%
                          boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                          T6125 t6125 = applyGuarantorManage1.getGuanterInfo();
                          if(escrow.equals("huifu") && !isHasGuarantor && T6110_F10.S == t6110.F10){
                        %>
                        <li class="item">
                            <div class="con">
                                <div class="mod01">担保申请</div>
                                <div class="mod02">我已经是担保方（担保码：<%=t6125.F03%>）
                                    <span class="hover_tips ml10">
                                        <div style="display: none;" class="hover_tips_con">
                                            <div class="arrow"></div>
                                            <div class="border">
                                              <p>1.您当前已成为担保人，可以为其他个人或企业进行担保。</p>
	                                          <p>2.担保方不能投标自己担保的项目。</p>
                                            </div>
                                        </div>
                                     </span>
                                </div>
                            </div>
                        </li>
                        <%} %>
                        
                        <%if(isHasGuarantor){%>
                        <li class="item">
                            <div class="con">
                                <div class="mod01">担保申请</div>
                                <div class="mod02">
                                    <%if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>我已经是担保方（担保码：<%=t6125.F03%>）
                                    <span class="hover_tips ml10">
                                        <div style="display: none;" class="hover_tips_con">
                                            <div class="arrow"></div>
                                            <div class="border">
                                            <%if(T6110_F10.S == t6110.F10)
                                            { %>
	                                            <p>1.您当前已成为担保人，可以为其他个人或企业进行担保。</p>
	                                            <p>2.担保方不能投标自己担保的项目。</p>
                                            <%}else{ %>
                                                <p>1.您当前已成为担保人，可以为其他个人或企业进行担保。</p>
                                                <p>2.担保方不能担保自己的借款项目。</p>
                                                <p>3.担保方不能投标自己担保的项目。</p>
                                                <p>4.若您想取消担保权限，请联系客服人员。</p>
                                             <%} %>
                                            </div>
                                        </div>
                                     </span>
                                    <%}%>
                                </div>

                                <%
                                    if(T6110_F10.F == t6110.F10)
                                    {
                                    if(t6125 == null || t6125.F05 != T6125_F05.SQCG){%>
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
                                <%}} if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>
                                <div class="operation">
                                <a href="javascript:void(0);" onclick="openShutManager(this,'box11',false,'取消修改','修改')" class="highlight">修改</a>
                                </div>
                                <%}%>
                            </div> 
                            <%if(t6125 != null && t6125.F05 == T6125_F05.SQCG){%>
								<div id="box11" style="display: none" class="form_info amend">
									<ul class="cell">
										<li class="tc ln30 orange f16" style="display: none;"
											id="sbDbmLi">恭喜您，修改成功！3秒后将刷新此页面。。。<br>
												<p></p></li>
										<li>
											<div class="til">
												<span class="red">*</span>担保码：
											</div>
											<div class="info">
												<input type="text" value="" name="dbm" maxlength="7"
													class="text yhgl_ser fl mr10 w200 required max-length-7"
													mtest="/^[a-zA-Z]([A-Za-z0-9]{6})$/" mtestmsg="请输入合法的担保码！"
													style="width: 173px" />
												<p tip></p>
												<p errortip class="" style="display: none"></p>
											</div>
											<div class="clear"></div>
										</li>
									</ul>
									<div class="tc">
										<input name="input" type="button" value="提 交" class="btn01"
											onclick="updateDbm(this)" />
									</div>
								</div> 
								<%}%>
							</li>
                        <%}%> --%>
                        <li></li>
                    </ul>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<div id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/zhankai.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"yes"));
		</script>
<%
    }
%>
<script type="text/javascript">
	$(function(){
	    $("input[name='bphoneCode']").next().removeAttr("disabled");
	});

	<%--绑定邮箱时--%>
	var wait = 60;
	<%--新邮箱时--%>
	var wait1 = 60;
	<%--手机修改邮箱时--%>
	var wait2 = 60;
	<%--绑定手机时--%>
	var wait3 = 60;
	<%--新手机时--%>
	var wait4 = 60;
	<%--找回交易密码时--%>
	var wait5 = 60;
	<%--修改邮箱时--%>
	var wait6 = 60;
	<%--修改手机时--%>
	var wait7 = 60;
	<%--修改密保时--%>
	var wait8 = 60;
	
	var modulus = '<%=modulus%>';
	var exponent = '<%=exponent%>';
	var flagSfxyyzm = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
	var key = RSAUtils.getKeyPair(exponent, '', modulus);
	var isTg = "<%=tg%>";
	var tokenName = "<%=FormToken.parameterName()%>";
	var username= "<%=data.username%>";
	<%--密码正则表达式--%>
    var myregPsw = <%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>;
    <%--密码匹配正则表达式提示语--%>
    var myregPswMsg = "<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>";
    var email = "<%StringHelper.filterHTML(out, data.emil);%>";
    var phoneNumber = "<%StringHelper.filterHTML(out, data.phoneNumber);%>";
    var isPhone = "<%=data.isPhone%>";
    var dataSfzh = "<%=StringHelper.decode(data.sfzh)%>";
    var isIdCardEmpty = <%=StringHelper.isEmpty(data.isIdCard)%>;
    var isIdCardCheck = <%=T6141_F04.TG.name().equals(data.isIdCard)%>;
    var isTxPasswordEmpty = <%=StringHelper.isEmpty(data.txpassword)%> ;
    <%--邮箱是否必要认证--%>
    var isMustMail = <%=BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL))%>;
    <%--邮箱是否通过认证--%>
    var isMail = <%=T6141_F04.TG.name().equals(data.isEmil)%>;
    var escrow = "<%=escrow%>";
    var chargeMustWithdrawPsd = <%=CHARGE_MUST_WITHDRAWPSD%>;
	var _sendUrl = "<%=controller.getURI(request, Send.class)%>";
	var _bindPhoneUrl ="<%=controller.getURI(request, BindPhone.class)%>";
	var _updateTxPasswordUrl ="<%=controller.getURI(request, UpdatetxPassword.class)%>";
	var _settxPasswordUrl = "<%=controller.getURI(request, SettxPassword.class)%>";
	var _updateNameUrl = "<%=controller.getURI(request, UpdateName.class)%>";
	var _updatePasswordUrl = "<%=controller.getURI(request, UpdatePassword.class)%>";
	var _loginUrl = '<%=controller.getViewURI(request, Login.class)%>';
	var _checkEmailUrl = "<%=controller.getURI(request, CheckEmail.class)%>";
	var _checkCodeUrl = "<%=controller.getURI(request, CheckCode.class)%>";
	var _checkNewCodeUrl = "<%=controller.getURI(request, CheckNewCode.class)%>";
	var _checkPasswordUrl = "<%=controller.getURI(request, Checkpassword.class)%>";
	var _pdatetxPasswordAjaxUrl = "<%=controller.getURI(request, PdatetxPasswordAjax.class)%>";
	var _verifyCommonUrl = "<%=controller.getURI(request, VerifyCommon.class)%>";
	var _pwdSafeServletUrl = "<%=controller.getURI(request, PwdSafeServlet.class)%>";
	var _queryQuestionUrl = "<%=controller.getURI(request, QueryQuestion.class)%>";
	var _checkMbwtUrl = "<%=controller.getURI(request, CheckMbwt.class)%>";
    var _applyQuarantorUrl = "<%=controller.getURI(request, ApplyGuarantor.class)%>";
    var _isNetSign = <%=applyGuarantorManage1.isNetSign()%>;
    var _netSignUrl = "<%=configureProvider.format(URLVariable.USER_NETSIGN_URL)%>";
    
    <%--修改担保码请求URL--%>
	var _updateDbm="<%=controller.getURI(request, UpdateDbm.class)%>";
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/account/safetymsgFZRR.js"></script>
</body>
</html>