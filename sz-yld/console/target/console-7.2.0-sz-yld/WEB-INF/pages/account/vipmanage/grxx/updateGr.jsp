<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.SysUser"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Grxx"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.UpdateGr"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.UpdateAssessNum"%>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
	CURRENT_CATEGORY="YHGL";
	CURRENT_SUB_CATEGORY="GRXX";
	Grxx user = ObjectHelper.convert(request.getAttribute("user"), Grxx.class);
	SysUser[] users = ObjectHelper.convertArray(request.getAttribute("ywUsers"), SysUser.class);
	boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
	boolean is_open_risk_assess = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
    int assessNumInt = IntegerParser.parse( configureProvider.getProperty(RegulatoryPolicyVariavle.ONE_YEAR_RISK_ASSESS_NUM));
    String assessNumStr = "";
    assessNumStr = (assessNumInt-user.assessedNum)+"/"+assessNumInt;
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      	 <div class="p20">
      	 <form action="<%=controller.getURI(request, UpdateGr.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
      	 <input name="userId" type="hidden" value="<%=user.id%>"/>
          <div class="border">
            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改用户信息</div>
            <div class="content-container pl40 pt40 pr40 pb20">
              <ul class="gray6">
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>用户Id：</span>
                	<%=user.id %>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>用户名：</span>
                	<%StringHelper.filterHTML(out, user.userName);%>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>姓   名：</span>
                	<%StringHelper.filterHTML(out, user.name);%>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>身份证：</span>
                	<%StringHelper.filterHTML(out,StringHelper.decode(user.sfzh));%>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>手机号：</span>
                	<input name="msisdn" id="telphonId" maxlength="11" type="text" class="text border w300 pl5 required phonenumber" value="<%StringHelper.filterHTML(out, user.phone);%>" />
                        	<span tip></span>
							<span errortip class="" style="display: none"></span>
                </li>
                 <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>邮箱：</span>
                	<input name="mailbox" id="msisdnOldId" type="text" class="text border w300 pl5 required e-mail" maxlength="50" value="<%StringHelper.filterHTML(out, user.email);%>" />
                        	<span tip></span>
							<span errortip class="" style="display: none"></span>
                </li>
                <%if(is_business && StringHelper.isEmpty(user.code)){ %>
                <li class="mb10"><span class="display-ib w200 tr mr5">业务员工号：</span>
                	<select name="employNum" class="border mr20 h32 mw100"  id="staff" >
                		<%if(StringHelper.isEmpty(user.employNum)){ %>
						<option></option>
						<%} %>
						<%
						if(users!=null && users.length>0){
						for (SysUser sysUser : users) {%>
						<option value="<%=sysUser.employNum%>" <%if(sysUser.employNum.equals(user.employNum)){%>selected="selected"<%} %>><%=sysUser.employNum%>/<%=sysUser.name %></option>
						<%}}%>
					</select>
                </li>
                <%} %>
                <%if(is_open_risk_assess){ %>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5"></em>风险评估：</span>
                	<%=user.riskAssess==null?"未评估":user.riskAssess.getChineseName()%>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5"></em>评估剩余次数：</span>
                    <span class="assess_num_sp"><%=assessNumStr%></span>
                </li>
                <%} %>
                <li><div class="pl200 ml5">
               
                <input type="submit" value="提交 " class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1" />
                <%if(is_open_risk_assess){ %>
                <input type="button" value="增加评估次数 " name="addAssessNum" class="btn btn-blue2 radius-6 pl20 pr20 ml10"/>
                <%} %>
                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10" onclick="location.href='<%=controller.getURI(request, GrList.class) %>'" value="取消"></div></li>
              </ul>

                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
        T6110 t6110 = ObjectHelper.convert(request.getAttribute("T6110"), T6110.class);

    %>
    <div id="showDiv">
    <div class="popup_bg"></div>
	<div class="popup-box" style="width:445px;min-height: 250px;">
      <div class="popup-title-container">
	      <h3 class="pl20 f18">提示</h3>
	      <a class="icon-i popup-close2" href="javascript:void(0);" onclick="$('#showDiv').hide()"></a>
	    </div>
	    <div class="popup-content-container pt20 ob20 clearfix">
			<div class="tc mb30">
            <input type="hidden" id="msisdnId" value='<%=t6110.F04%>' />
            <input type="hidden" id="emailId" value="<%=t6110.F05%>"/>
            <input type="hidden" id="errorId" value="<%=warringMessage%>"/>
            <span class="icon-i w30 h30 va-middle radius-wrong-icon"></span>
            <span class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="tc f16 "><input type="button" name="button2" onclick="reValue();" value="确认"
                                    class="btn-blue2 btn white radius-6 pl20 pr20"/></div>
    </div>
</div>
</div>
<%} %>
<div id="info"></div>
<div class="popup_bg hide popup_bg_assess"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var assessNumInt = '<%=assessNumInt%>';
    var assessedNum = '<%=assessNumInt-user.assessedNum%>'; //用户剩余评估次数
    function reValue() {
        var telPhone = $('#msisdnId').val();
        var email = $('#emailId').val();
        var errorMsg = $('#errorId').val();
        //此处为了不改后台的代码,直接根据UpdateGr的返回异常进行决断
        if (errorMsg == "手机号码已经存在！") {
            $('#msisdnOldId').val(email);
        } else if (errorMsg == "邮箱已经存在！") {
            $('#telphonId').val(telPhone);
        }
        $('#showDiv').hide();
        //$("div.popup_bg").hide();
    }
    
    $(function () {
        $("input[name='addAssessNum']").click(function () {
        	$("#info").html(showQuestionInfo("确定要增加评估次数吗？", "question", "javascript:updateAssessNum()","closeQuestionInfo('questionInfo')"));
            $("div.popup_bg_assess").show();
        });
    });
    function updateAssessNum(){
    	var userId = $("input[name='userId']").val();
    	$.ajax({
    		type:"post",
    		dataType:"html",
    		url:"<%=controller.getURI(request, UpdateAssessNum.class)%>",
    		data:{userId: userId},
    		success:function(data){
    			try {
    				data = eval("(" + data + ")");
        			if(data.result == "001"){
        				var path = "<%=controller.getURI(request, UpdateGr.class)%>?userId="+userId;
        				$("#info").html(showSuccInfo("评估次数增加成功","yes","changeCount('"+data.message+"')"));
        			}else if(data.result == "000"){
        				$("#info").html(showSuccInfo(data.message,"wrong","closeQuestionInfo('succInfo')"));
        			}
        			$("div.popup_bg_assess").show();
				} catch (e) {
					$(parent.frames["topFrame"].document).find("#return_logout")[0].click();
				}
    		},
    		error: function(XMLHttpRequest, textStatus, errorThrown){
    			$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
    			$("div.popup_bg_assess").show();
    		}
    	});
    }
    
    function closeInfo() {
        window.location.href = "<%=controller.getURI(request, UpdateGr.class)%>?userId="+$("input[name='userId']").val();
    }

    function closeQuestionInfo(divId) {
        $("#"+divId).hide();
        $("div.popup_bg_assess").hide();
    }

    function showQuestionInfo(msg,type,url,method){
        return '<div class="popup-box" style="width:470px;min-height:200px;" id="questionInfo"><div class="popup-title-container">'+
                '<h3 class="pl20 f18">提示</h3>'+
                '<a class="icon-i popup-close2" onclick="'+method+';"></a></div>'+
                '<div class="popup-content-container pb20 clearfix">'+
                ' <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
                ' <div class="tc f16"><a href="'+url+'" class="btn-blue2 btn white radius-6 pl20 pr20">确 定</a><a href="javascript:'+method+';" class="btn btn-blue2 radius-6 pl20 pr20 ml40">取 消</a></div> '+
                ' </div>'+
                '</div>';
    }

    //增加评估次数成功后调用
    function showSuccInfo(msg,type,method){
        return '<div class="popup-box" style="width:470px;min-height:200px;" id="succInfo"><div class="popup-title-container">'+
                '<h3 class="pl20 f18">提示</h3>'+
                '<a class="icon-i popup-close2" onclick="'+method+';"></a></div>'+
                '<div class="popup-content-container pb20 clearfix">'+
                ' <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-'+type+'-icon"></span><span class="f20 h30 va-middle ml10">'+msg+'</span></div>'+
                ' <div class="tc f16"><a href="javascript:'+method+';" class="btn-blue2 btn white radius-6 pl20 pr20">确 定</a></div> '+
                ' </div>'+
                '</div>';
    }

    //更新评估次数
    function changeCount(num){
        var assessNumStr = (parseInt(assessNumInt)-parseInt(num))+"/"+assessNumInt;
        $(".assess_num_sp").html(assessNumStr);
        closeQuestionInfo('succInfo');
        assessedNum = parseInt(assessNumInt)-parseInt(num);
    }
</script>
</body>
</html>