<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.gain.ChargeScore"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.gain.ScoreGetList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.gain.ExportScoreGetList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6106_F05"%>
<%@page import="com.dimeng.p2p.S61.entities.T6106"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ExportScoreStatisticsList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreCountExt"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreGetRecord"%>
<%@page import="com.dimeng.p2p.S61.entities.T6105"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ExportGr" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.gain.CheckUserNameExists" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "JFHQJL";
    PagingResult<T6106> t6106List = ObjectHelper.convert(request.getAttribute("t6106List"), PagingResult.class);
    T6106[] t6106Array = (t6106List == null ? null : t6106List.getItems());
    int scoreSum = (int)request.getAttribute("scoreSum");
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, ScoreGetList.class)%>" method="post">
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>积分获取记录</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">用户名</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">获取时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li>
                       <span class="display-ib mr5">类型</span> 
                       <select name="scoreType" class="border mr20 h32 mw100">
                        <option value="">全部</option>
                        <%
                        for(T6106_F05 scoreType : T6106_F05.values()){
                        %>
                        <option value="<%=scoreType.name() %>"
                                <%if(scoreType.name().equals(request.getParameter("scoreType"))){ %>
                                selected="selected" <%}%>><%=scoreType.getChineseName() %>
                        </option>
                        <%} %>
                    </select>
                   </li>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                		<%
                     		if (dimengSession.isAccessableResource(ChargeScore.class)) {
                     	%>
                     	<a href="javascript:void(0)" onclick="showChargeScore()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle"></i>积分充值</a>
                     	<%
                     		}else{
                     	%>
                     	<span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle"></i>积分充值</span>
                     	<%
                     		}
                     	%>	
                	</li>
			        <li>
                		<%
                     		if (dimengSession.isAccessableResource(ExportScoreGetList.class)) {
                     	%>
                     	<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                     	<%
                     		}else{
                     	%>
                     	<span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                     	<%
                     		}
                     	%>	
                	</li>
			      </ul>
			    </div>
			  </div>
			  <div class="border mt20 table-container">
			    <table class="table table-style gray6 tl">
			      <thead>
			        <tr class="title tc">
			          <th>序号</th>
			          <th>用户名</th>
			          <th>真实姓名</th>
			          <th>获取时间</th>
			          <th>获取积分</th>
			          <th>类型</th>
			          <th>操作人</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (t6106Array != null && t6106Array.length>0) {
                        		int index = 1;
                        		for (T6106 t6106:t6106Array)
                        		{if (t6106 == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%StringHelper.filterHTML(out, t6106.F10);%></td>
                          <td><%StringHelper.filterHTML(out, t6106.F11);%></td>
                          <td><%=TimestampParser.format(t6106.F04)%></td>
                          <td><%=t6106.F03%></td>
                          <td><%StringHelper.filterHTML(out, t6106.F05.getChineseName());%></td>
                          <%if(T6106_F05.chargeScore.equals(t6106.F05)) {%>
                          <td><%StringHelper.filterHTML(out, t6106.F13);%></td>
                          <%}else{ %>
                          <td>当前用户</td>
                          <%} %>
                        </tr>
						<%}%>
						<tr class="tc">
                            <td>获取积分总计：</td>
                            <td colspan="3"></td>
                            <td class="red"><%=scoreSum%></td>
                            <td colspan="2"></td>
                        </tr>
						<%}else{%>
                        <tr class="dhsbg"><td colspan="7" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, t6106List);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<div id="chargeScoreDivId" style="display: none;">
    <div class="popup-box">
        <div class="popup-title-container">
            <h3 class="pl20 f18">订单修改</h3>
            <a class="icon-i popup-close2" onclick="closeChargeDiv()"></a>
        </div>
        <div class="popup-content-container-2" style="max-height:550px;">
            <form action="<%=controller.getURI(request, ChargeScore.class)%>" method="post" class="form2">
                <div class="p30">
                    <ul class="gray6">
                        <li class="mb15 kind">
                        	<span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>用户名：</span>
                            <div class="pl120">
                                <input id="chargeUserName" name="chargeUserName" type="text" ftest="isExists"
                                       class="text border w300 pl5 required max-length-20"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                        <li class="mb15 kind">
                        	<span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>充值积分：</span>
                            <div class="pl120">
                                <input id="chargeAmount" name="chargeAmount" type="text"
                                       class="text border w300 pl5 required wholeNumber" maxlength="6"/>
                                <span tip></span>
                                <span errortip class="red" style="display: none"></span>
                            </div>
                        </li>
                    </ul>
                    <div class="tc f16">
                        <input type="submit" value="确定"
                               class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                               fromname="form2"/>
                        <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeUpdateData();">取消</a> -->
                        <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeChargeDiv();">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="popup_bg div_popup_bg" style="display: none;"></div>
	<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
	function onSubmit(){
		$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
		$('#form1').submit();
	}
	
	/**
    * 校验用户名是否存在
    */
    function isExists() {
    	var chargeUserNameObj = $("#chargeUserName");
    	var chargeUserName = chargeUserNameObj.val();
    	var tip = chargeUserNameObj.parent().find("span[tip]");
    	var correct = chargeUserNameObj.parent().find("span[errortip]");
    	var isNull = /^[\s]{0,}$/;
    	correct.hide();
    	var validat = true;
    	if (isNull.test(chargeUserName)) {
    		tip.hide();
    		correct.text("用户名不能为空");
    		correct.show();
    		validat = false;
    		return validat;
    	}
    	tip.show();
    	$.ajax({
    		type: 'POST',
    		url: '<%=controller.getURI(request, CheckUserNameExists.class)%>',
    		data: {"chargeUserName" : chargeUserName},
    		dataType: "html",
    		async:false,
    		success:function(data){
    			if ($.trim(data) == 'false') {
    				tip.hide();
    				correct.text("用户名不存在！");
    				correct.show();
    				validat = false;
    			}else if(data.indexOf("script")>-1 && data.indexOf("alert")>-1){
    				tip.hide();
    				correct.text("用户名不能包含特殊字符");
    				correct.show();
    				validat = false;
    			}
    		},
    		error: function(XMLHttpRequest, textStatus, errorThrown){
           		$(".popup_bg").show();
           		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
    		}
    	});
    	return validat;
    }

    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportScoreGetList.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, ScoreGetList.class)%>";
    }
    
    function showChargeScore(){
    	$("#chargeScoreDivId").show();
    }
    
    function closeChargeDiv()
    {
    	$("#chargeScoreDivId").hide();
    	$(".div_popup_bg").hide();
    	$("span[tip]").show();
    	$("span[errortip]").hide();
    }
    /* function onSubmit(){
     $("#form1").submit();
     } */
</script>
</body>
</html>