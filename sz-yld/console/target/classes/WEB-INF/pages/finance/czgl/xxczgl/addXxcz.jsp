<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.XxczglList"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.CheckNameExists" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "XXCZGL";
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>线下充值</div>
                    </div>
                    <%if(!tg || "baofu".equals(escrowFinance)|| "FUYOU".equals(escrowFinance) || "huifu".equals(escrowFinance) || "sina".equals(escrowFinance))
                    {%>
                        <form action="<%=configureProvider.format(URLVariable.ADDXXCZ_URL)%>" method="post" class="form1">
                    <%}
                    else
                    {%>
                        <form action="<%=configureProvider.format(URLVariable.ADDXXCZ_URL)%>" method="post" class="form1" target="_bank" onsubmit="return refreshCurrentPage()">
                    <%}%>
                    <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border mt20">
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <input type="hidden" name="token" value=""/>
                                    <ul class="gray6">
                                        <li class="mb10"><span class="pl200 ml5 red">
                                        	<%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
											</span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>用户账号：</span>
                                            <input name="accountName" type="text"
                                                   class="text border w300 yw_w5 "
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("accountName"));%>" onblur="checkAccountName(this.value);"/>
                                            <!-- <span tip></span> -->
                                            <span class="red accountNameErrorInfo"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>充值金额：</span>
                                            <%-- <input name="amount" type="text" class="yhgl_ser required minf-size-0" maxlength="18"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("amount"));%>" mtest="/^[-\+]?\d+(\.\d+)?$/" mtestmsg="金额格式不正确"/> --%>
                                            <input name="amount" type="text"
                                                   class="text border w300 yw_w5 required min-float-0.01" maxlength="10"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("amount"));%>"
                                                   mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>
                                            <span>元</span>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>备注：</span>
                                            <textarea name=remark class="w400 h120 border p5 required max-length-140"
                                                      cols="100" rows="8"
                                                      style="width: 700px; height: 200px;"><%StringHelper.format(out, request.getParameter("remark"), fileStore); %></textarea>
                                            <span tip>最大140个字</span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme" value="提交"
                                                   fromname="form1"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, XxczglList.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="popup_bg hide"></div>
<div id="info"></div>
    <div id="realse" style="display: none;"></div>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
    <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>  
    
</body>
<script type="text/javascript">
    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='token']").val(tokenValue);
        var msg = '<%=request.getAttribute("EMAIL_ERROR")==null ? "" : ((String)request.getAttribute("EMAIL_ERROR")).replace("class='red'","class=\"red\"")%>';
        if (msg) {
            //alert(msg);
        	$("#info").html(showDialogInfo(msg, "wrong"));
            $("div.popup_bg").show();
        }
    });
    
    var checkName = true;
    
  //提现审核时确认放款后提示信息，中间跳转操作
    function refreshCurrentPage()
    {
    	if(!checkName || !checkAccountName($("input[name='accountName']").val())){
    		return false;
    	}
    	$("#realse").html("");
    	var arr = new Array();
    	arr.push("<div class='popup-box' style='min-height:200px;width:400px'>"
    			+"<div class='popup-title-container'>"
    			+"<h3 class='pl20 f18'>提示</h3>"
    			+"<a class='icon-i popup-close2' href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();'></a>"
    			+"</div>"
    			+"<div class='popup-content-container pt20 ob20 clearfix'>"
    			+"<div class='mb20 gray6 f18' style='text-align: center;'>"
    			+"<ul>"
    			+"<li class='mb10'><span class='display-ib tr mr5'>请您在新打开面的页上完成操作</span></li>"
    			+"</ul>"
    			+"</div>"
    			+"<div class='tc f16'>"
    			+"<a href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();' class='btn-blue2 btn white radius-6 pl20 pr20' >已完成充值</a>"
    			+"<a class='btn btn-blue2 radius-6 pl20 pr20 ml40' href='<%=controller.getURI(request, XxczglList.class)%>' onclick='refreshPage();'>充值出现问题</a>"
    			+"</div>"
    			+"</div>"
    			+"</div>"
    			+"<div class='popup_bg'></div>");
    	$("#realse").html(arr.join(""));
    	$("#realse").show();
    	return true;
    }

    function refreshPage()
    {
    	$("#realse").hide();
    }
    
    function checkAccountName(value){
    	var isTrue = true;
    	$(".accountNameErrorInfo").html("");
    	if(!value){
    		$(".accountNameErrorInfo").html("不能为空！");
    		checkName = false;
    		return false;
    	}
    	$.ajax({
    		type: 'POST',
    		url: "<%=controller.getURI(request, CheckNameExists.class)%>",
    		async:false,
    		data: {accountName : value},
    		dataType: "html",
    		success:function(data){
    			if ($.trim(data) == 'false') {
    				$(".accountNameErrorInfo").html("用户名不存在！");
    				checkName = false;
    				isTrue = false;
    			}
    		},
    		error: function(XMLHttpRequest, textStatus, errorThrown){
    			checkName = false;
    			isTrue = false;
    		}
    	});
    	checkName = isTrue;
    	return isTrue;
    }
    
</script>
</html>