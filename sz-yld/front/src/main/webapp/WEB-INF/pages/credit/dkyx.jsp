<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.S62.entities.T6282" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F15" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F16" %>
<%@page import="com.dimeng.p2p.S62.enums.T6282_F17" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.front.servlets.Region_jkyx" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.Dkyx" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.PersonalLoanVerify" %>
<%@ page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.common.enums.TermType" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@ page import="com.dimeng.p2p.front.servlets.Term" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=<%=resourceProvider.getCharset() %>" />
    <meta name="keywords" content="个人无抵押小额贷款，个人短期借款，免担保个人贷款"/>
    <meta name="description" content="<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>为您提供最新的个人无抵押、免担保贷款产品,在线快速申请,<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>免费为你审核评估,专业团队为您量身定制贷款方案,额度高，利息低，流程简单！"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Pragma" CONTENT="no-cache"/>
    <meta http-equiv="Cache-Control" CONTENT="no-cache"/>
    <meta http-equiv="Expires" CONTENT="0"/>
    <title>个人无抵押小额贷款_免担保个人贷款_个人短期借款-<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></title>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form action="<%=controller.getURI(request, Dkyx.class)%>" class="form1" method="post">
    <%=FormToken.hidden(serviceSession.getSession()) %>
    <div class="main_bg">
        <div class="main_mod">
            <div class="main_hd"><i class="icon"></i><span class="gray3 f18">借款意向（个人）</span></div>
            <div class="ml30 lh28 pt10 mr30">
                <span class="highlight">提示</span> <br/>
                <%=configureProvider.format(HTMLVariable.GRDBRZSQTS)%>
            </div>
            <div class="main_hd02 mt30">借款意向填写</div>
            <div class="main_form mt50">
                <ul>
                    <%
                        T6282 t6282 = new T6282();
                        t6282.parse(request);
                        t6282.F08 = IntegerParser.parse(request.getParameter("xian"));
                        int sheng = IntegerParser.parse(request.getParameter("sheng"));
                        int shi = IntegerParser.parse(request.getParameter("shi"));
                        String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(ermsg) && !"无效的验证码或验证码已过期.".equals(ermsg)) {
                    %>
                    <li style="color: red;border: 1px solid red;padding: 10px;text-align: center;"><%
                        StringHelper.filterHTML(out, ermsg);
                    %></li>
                    <%}%>
                    <li class="item">
                        <div class="til"><span class="red">*</span>联系人：</div>
                        <div class="con">
                            <input name="F03" type="text" class="text required max-length-32"
                                   <%if(t6282.F03 != null) {%>value="<%=t6282.F03 %>"<%}%>/>

                            <p tip class="prompt"><span class="gray9">不超过32个字</span></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>手机号码：</div>
                        <div class="con">
                            <input name="F04" type="text" class="text required phonenumber" maxlength="11"
                                   <%if(t6282.F04 != null) {%>value="<%=t6282.F04 %>"<%}%>/>

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>意向借款金额：</div>
                        <div class="con">
                            <input name="F06" maxlength="15" type="text" class="text required isint mulriple-100 min-size-100"
                                   <%if(t6282.F06.intValue() !=0) {%>value="<%=t6282.F06 %>"<%}%>/>元
                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>借款期限：</div>
                        <div class="con">
                            <input type="text" class="text required min-size-1 max-size-24" mtest="/^\d+$/"
                                   mtestmsg="必须为整数" name="F19" <%if(t6282.F19!=0) {%>value="<%=t6282.F19 %>"<%}%>/>个月（1-24个月）
                            <p tip class="prompt"></p>
                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til">借款类型：</div>
                        <div class="con">
                            <input type="checkbox" name="F15" value="S" class="mr5"
                                   <%if(t6282.F15 == T6282_F15.S) {%>checked="checked" <%}%>/>抵押
                            <input type="checkbox" name="F16" value="S" class="ml20 mr5" 
                                   <%if(t6282.F16 == T6282_F16.S) {%>checked="checked" <%}%>/>实地认证
                            <input type="checkbox" name="F17" value="S" class="ml20 mr5" 
                                   <%if(t6282.F17 == T6282_F17.S) {%>checked="checked" <%}%>/>担保
                             <p class="prompt"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>所在城市：</div>
                        <div class="con" id="region">
                        	<input id="shengId" value="<%=sheng%>" type="hidden"/>
                    		<input id="shiId" value="<%=shi%>" type="hidden"/>
                    		<input id="xianId" value="<%=t6282.F08%>" type="hidden"/>
                            <select name="sheng" id="sheng"  class="select6 required">
                            </select>
                            

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til">预期筹款期限：</div>
                        <div class="con"><select name="F09" class="select2" id="F09">
                            <option value=" ">请选择</option>
                            <option value="7天之内">7天之内</option>
                            <option value="15天之内">15天之内</option>
                            <option value="15-30天">15-30天</option>
                            <option value="1-3个月">1-3个月</option>
                            <option value="其他期限">其他期限</option>
                        </select>

                            <p tip class="prompt"></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til lh24"><span class="red">*</span>借款描述：<br/><span
                                class="gray9 f12">(20-500字)</span></div>
                        <div class="con"><textarea name="F10" cols="65" rows="6" style="max-width: 710px;"
                                                   class="textarea required min-length-20 max-length-500"> <%if (t6282.F10 != null) {%><%=t6282.F10 %><%}%></textarea>

                            <p tip class="prompt"></p>

                            <p errortip class="prompt" style="display: none"></p>
                        </div>
                    </li>
                    <%
                        {
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                    %>
                    <li class="item">
                        <div class="til"><span class="red">*</span>验证码：</div>
                        <div class="con">
                            <input name="verifyCode" type="text" class="text required min-length-6 max-length-6"
                                   value="" maxlength="6" style="width:120px;"/>
                            <img src="<%=controller.getURI(request, PersonalLoanVerify.class)%>" width="80" height="31"
                                 onclick='this.src="<%=controller.getURI(request, PersonalLoanVerify.class)%>?"+Math.random()'
                                 style="cursor: pointer;" id="verify-img"/>
                            <a href="javascript:void(0)"
                               onclick="anotherImg('<%=controller.getURI(request, PersonalLoanVerify.class)%>?'+Math.random())"
                               class="highlight ml10 ht">看不清楚?换一张</a>
                            <p tip class="prompt"><span class="gray9 f12">请填写图片中的字符</span></p>
                            <p errortip class="prompt" style="display: none"></p>
                            <%if ("无效的验证码或验证码已过期.".equals(ermsg)) {%>
                             <p class="red">无效的验证码或验证码已过期.</p>
                            <%}%>
                        </div>
                    </li>
                    <%
                            }
                        }
                    %>
                    <%
                        TermManage termManage = serviceSession.getService(TermManage.class);
                        T5017 term = termManage.get(TermType.GRXXCQSQTK);
                        if(term != null){
                    %>
                    <li class="item">
                        <div class="til">&nbsp;</div>
                        <div class="con">
                            <input name="iAgree" onclick="checkoxBtn();" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
                            <a target="_blank"
                               href="<%=controller.getPagingItemURI(request, Term.class, TermType.GRXXCQSQTK.name())%>"
                               class="highlight">《<%=term.F01.getName()%>》</a>
                        </div>
                    <li/>
                    <li class="item mb50">
                        <div class="til">&nbsp;</div>
                        <div class="con"><input type="submit" id="sub-btn" class="btn06 sumbitForme btn_gray btn_disabled" disabled="disabled" fromname="form1" value="提  交"/>
                        </div>
                    </li>
                    <%}else{%>
                    <li class="item mb50">
                        <div class="til">&nbsp;</div>
                        <div class="con"><input type="submit" class="btn06 sumbitForme" fromname="form1" value="提  交"/>
                        </div>
                    </li>
                    <%}%>
                </ul>
            </div>
        </div>
    </div>
</form>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<% String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMsg)) {%>
<script type="text/javascript">
    $("#info").html(showSuccInfo("<%=infoMsg%>", "successful", "<%configureProvider.format(out, URLVariable.INDEX);%>"));
    $("div.popup_bg").show();
</script>
<%}%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getURI(request, Region_jkyx.class)%>"></script>
<script type="text/javascript">
    function anotherImg(contextPath) {
        $("#verify-img").attr("src", contextPath);
    }
    $(function () {
        <%-- $("#sheng").html(getSelectHtml2("sheng", "<%=sheng%>", "<%=shi%>", "<%=t6282.F08%>"));
        $("#shi").html(getSelectHtml2("shi", "<%=sheng%>", "<%=shi%>", "<%=t6282.F08%>"));
        $("#xian").html(getSelectHtml2("xian", "<%=sheng%>", "<%=shi%>", "<%=t6282.F08%>")); --%>
        
		<%if(sheng > 0  && shi > 0 && t6282.F08 > 0){%>
			var region = '<input id="shengId" value="<%=sheng%>" type="hidden"/>';
			region +='<input id="shiId" value="<%=shi%>" type="hidden"/>';
			region +='<input id="xianId" value="<%=t6282.F08%>" type="hidden"/>';
			region += '<select name="sheng" id="sheng" class="select6 required"></select>';
			region += '<select name="shi" id="shi" class="select6"></select>';
			region += '<select name="xian" id="xian" class="select6"></select>';
			region += '<p tip class="prompt"></p><p errortip class="prompt" style="display: none"></p>';
			$("#region").html(region);
			initRegion();
		
		<%}%>
        $("#F09 option").each(function () {
            if ($(this).val() == "<%=t6282.F09%>") {
                $(this).attr("selected", "selected");
            }
        });
        
        $('.select2').selectlist({
    		width: 372,
    		height: 36
    	});

        //“我同意”按钮切回事件
        $("input:checkbox[name='iAgree']").attr("checked", false);
        $("input:checkbox[name='iAgree']").click(function() {
            var iAgree = $(this).attr("checked");
            var register = $("#sub-btn");
            if (iAgree) {
                register.removeClass("btn_gray btn_disabled");
            } else {
                register.addClass("btn_gray btn_disabled");
            }
        });

    });

    function checkoxBtn()
    {
        var iAgree = $("input:checkbox[name='iAgree']").attr("checked");
        if (iAgree != "checked"){
            $("#sub-btn").attr('disabled',true);
        }else{
            $("#sub-btn").attr('disabled',false);
        }
    }
</script>
</body>
</html>
