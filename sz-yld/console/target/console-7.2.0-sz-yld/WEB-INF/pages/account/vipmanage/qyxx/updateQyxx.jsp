<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F17" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    T6161 entity = ObjectHelper.convert(request.getAttribute("t6161"), T6161.class);
    T6110_F17 t6110_f17 = request.getAttribute("t6110_f17")==null?T6110_F17.F: (T6110_F17)request.getAttribute("t6110_f17");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改企业信息
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="javascript:void(0);" class="tab-btn select-a">企业信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <form id="form1" action="<%=controller.getURI(request, UpdateQyxx.class)%>" method="post"
                                      class="form1">
                                    <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                    <input type="hidden" id="entryType" name="entryType" value="">
                                    <ul class="gray6">
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业名称：</span>
                                            <input name="F04" maxlength="20" disabled="disabled" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F04); %>"/>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">企业简称：</span>
                                            <input name="F18" maxlength="20" type="text" class="text border w300 pl5"
                                                   value="<%StringHelper.filterHTML(out, entity.F18); %>"/>
                                        </li>
                                        <li id="szhyli" class="mb10"><span
                                                class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否三证合一：</span>
                                            <input id="szhy" type="hidden" name="F20" value="<%=entity.F20%>"> <label
                                                    id="szhy1" class="cursor-p display-ib mr50 pt5 pb5"
                                                    for="ChoiceIs"> <input type="radio" name="szhy"
                                                                           class="mr10" value="Y"
                                                                           <%if("Y".equals(entity.F20)){ %>checked="checked"<%} %>
                                                                           onclick="szhyCheck()"/>是
                                            </label> <label class="cursor-p display-ib mr50 pt5 pb5"
                                                            for="ChoiceNo"> <input id="szhy2" type="radio"
                                                                                   name="szhy" class="mr10" value="N"
                                                                                   <%if("N".equals(entity.F20)){ %>checked="checked"<%} %>
                                                                                   onclick="szhyCheck()"/>否
                                            </label>

                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span></li>
                                        <li class="mb10 sszhy">
                                            <span class="display-ib w200 tr mr5"><em
                                                    class="red pr5">*</em>社会信用代码：</span>
                                            <input id="idF19" name="F19" maxlength="18" type="text"
                                                   class="text border w300 pl5 required min-length-18 max-length-18" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'')"
                                                   value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(entity.F19) || "null".equalsIgnoreCase(entity.F19)?"":entity.F19); %>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                            <span id="idShxy" style="display: none;color:red;"></span>
                                        </li>

                                        <li class="mb10 nszhy"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>营业执照登记注册号：</span>
                                            <input name="F03" maxlength="20" type="text"
                                                   class="text border w300 pl5 required" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'')"
                                                   value="<%StringHelper.filterHTML(out, entity.F03); %>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10 nszhy"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业纳税号：</span>
                                            <input name="F05" maxlength="20" type="text"
                                                   class="text border w300 pl5 required" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'')"
                                                   value="<%StringHelper.filterHTML(out,  entity.F05);%>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10 nszhy"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>组织机构代码：</span>
                                            <input name="F06" maxlength="20" type="text"
                                                   class="text border w300 pl5 required max-length-20 min-length-9" onkeyup="value=value.replace(/[\u4e00-\u9fa5]/g,'')"
                                                   value="<%StringHelper.filterHTML(out, entity.F06);%>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册年份：</span>
                                            <input name="F07" maxlength="4" type="text"
                                                   class="text border w300 pl5 required isyear max-length-4 min-length-4"
                                                   value="<%=entity.F07%>"/>年
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册资金：</span>
                                            <input name="F08" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text" class="text border w300 pl5"
                                                   value="<%=entity.F08%>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>行业：</span>
                                            <input name="F09" maxlength="20" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out,  entity.F09);%>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业规模：</span>
                                            <input name="F10" maxlength="9" minlength="1" type="text"
                                                   class="text border w300 pl5 required isint" value="<%=entity.F10%>"/>人
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人：</span>
                                            <input name="F11" maxlength="11" type="text"
                                                   class="text border w300 pl5 required"
                                                   value="<%StringHelper.filterHTML(out, entity.F11);%>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人身份证号：</span>
                                            <input name="F12" maxlength="18" type="text"
                                                   class="text border w300 pl5 required idcard"
                                                   value="<%StringHelper.filterHTML(out,StringHelper.decode(entity.F13));%>"/>
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>资产净值：</span>
                                            <input name="F14" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text" class="text border w300 pl5"
                                                   value="<%=entity.F14%>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>

                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上年度经营现金流入：</span>
                                            <input name="F15" maxlength="15"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                                   mtestmsg="必须为数字格式(且是两位小数)" type="text" class="text border w300 pl5"
                                                   value="<%=entity.F15%>"/>万元
                                            <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">贷款卡证书编号：</span>
                                            <input name="F16" maxlength="50" type="text" class="text border w300 pl5"
                                                   value="<%StringHelper.filterHTML(out,  entity.F16);%>"/>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">企业信用证书编号：</span>
                                            <input name="F17" maxlength="50" type="text" class="text border w300 pl5"
                                                   value="<%StringHelper.filterHTML(out,  entity.F17);%>"/>
                                        </li>
                                        <% 
                                    	String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                                    	if(null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX)){ 
	                                    %>
										<li class="mb10"><span class="display-ib w200 tr mr5">
											<em class="red pr5">*</em>开户银行许可证：</span> 
											<input name="F21" maxlength="20" type="text" class="text border w300 pl5 yhgl_ser required"
	                                            value="<%StringHelper.filterHTML(out,  entity.F21);%>"/>
	                                        <span tip></span>
                                            <span errortip class="red" style="display: none"></span>
	                                    </li>
	                                    <%} %>
	                                   <%--  <li class="mb10"><span
	                                            class="display-ib w200 tr mr5">是否允许投资：</span>
	                                        <input id="isInvestorHidden" type="hidden" name="isInvestorHidden" value="<%=request.getParameter("isInvestor")%>"> 
	                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="ChoiceIs"> 
	                                        	<input type="radio" name="isInvestor" class="mr10" value="S" <%if(T6110_F17.S.equals(t6110_f17)){ %>checked="checked"<%} %>/>是
	                                        </label>
	                                        <label class="cursor-p display-ib mr50 pt5 pb5" for="ChoiceNo"> 
	                                        	<input type="radio" name="isInvestor" class="mr10" value="F" <%if(T6110_F17.F.equals(t6110_f17)){ %>checked="checked"<%} %>/>否
	                                        </label>
	
	                                        <span tip></span>
	                                        <span errortip class="" style="display: none"></span>
	                                    </li> --%>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input style="display:none;" type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1"/>
                                                <input type="button"
                                                       class="btn btn-blue2 radius-6 pl20 pr20"
                                                       value="下一步" onclick="nextButton();"/>
                                                <input type="button" onclick="tj();" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                                <input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request, QyList.class)%>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                            </div>
                                        </li>
                                    </ul>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    $(function () {
        szhyCheck();
    });


    function szhyCheck() {
        if ($("input[name='szhy']:checked").val() == "Y") {
            $(".sszhy").show();
            $(".nszhy").hide();
        } else {
            $(".sszhy").hide();
            $(".nszhy").show();
        }
        $("#szhy").val($("input[name='szhy']:checked").val());
    }
    
    function nextButton(){
    	$('#entryType').val('');
        $('.sumbitForme').click();
    }
    function tj() {
        $('#entryType').val('submit');
        $('.sumbitForme').click();
    }
    
</script>
</body>
</html>