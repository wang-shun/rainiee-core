<%@page import="com.dimeng.p2p.user.servlets.mall.GetAddress"%>
<%@page import="com.dimeng.p2p.user.servlets.mall.UpdateAddress"%>
<%@page import="com.dimeng.p2p.user.servlets.mall.DelAddress"%>
<%@page import="com.dimeng.p2p.user.servlets.mall.AddAddress"%>
<%@page import="com.dimeng.p2p.common.enums.YesOrNo"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.S63.entities.T6355"%>
<%@page import="com.dimeng.p2p.repeater.score.UserCenterScoreManage"%>
<%@page import="com.dimeng.p2p.user.servlets.AbstractUserServlet" %>
<%@page import="com.dimeng.p2p.user.servlets.mall.HarvestAddress" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>收货地址管理-<%
        configureProvider.format(out, SystemVariable.SITE_NAME);
    %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDSC";
    CURRENT_SUB_CATEGORY = "SHDZGL";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <!--右边内容-->
            <form id="form1" action="<%=controller.getURI(request, HarvestAddress.class)%>" method="post">
        <div class="r_main">
        	<div class="user_mod">
        		<%
        		PagingResult<T6355> result = (PagingResult<T6355>)request.getAttribute("result");
        		int countAddress = IntegerParser.parse(request.getAttribute("countAddress"));
        	    T6355[] t6355Array = (result == null ? null : result.getItems());
        		%>
            	<div class="user_til clearfix"><i class="icon"></i><span class="gray3 f18">收货地址管理</span></div>
                <div class="mt20 mb20"><a href="javascript:void(0);" id ="addAddress" class="btn01 mr20">新增收货地址</a>您已创建<%=countAddress %>个收货地址，最多可创建20个</div>
                <div class="user_table">
                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr class="til">
                      	<td align="center">序号</td>
                        <td align="center">收货人</td>
                        <td align="center">所在地区</td>
                        <td align="center">详细地址</td>
                        <td align="center">手机号码</td>
                        <td align="center">邮编</td>
                        <td align="center">操作</td>
                        <td align="center">&nbsp;</td>
                      </tr>
                      <%
                         	if(t6355Array != null && t6355Array.length>0){
                         	    int i = 1;
                         	    for(T6355 t6355 : t6355Array){
                      %>
                      <tr>
                      	<td align="center"><%=i %></td>
                        <td align="center" title="<%StringHelper.filterHTML(out, t6355.F03);%>"><%StringHelper.filterHTML(out, StringHelper.truncation(t6355.F03, 4));%></td>
                        <td align="center" title="<%StringHelper.filterHTML(out, t6355.szdq);%>"><%StringHelper.filterHTML(out, StringHelper.truncation(t6355.szdq, 10));%></td>
                        <td align="center" title="<%StringHelper.filterHTML(out, t6355.F05);%>"><%StringHelper.filterHTML(out, StringHelper.truncation(t6355.F05, 20));%></td>
                        <td align="center"><%StringHelper.filterHTML(out, t6355.F06);%></td>
                        <td align="center"><%StringHelper.filterHTML(out, t6355.F07);%></td>
                        <td align="center"><a href="javascript:void(0);" onclick="updateAddress(<%=t6355.F01 %>);" class="highlight">修改</a><a href="javascript:void(0);" onclick="deleteAddress(<%=t6355.F01 %>);" class="highlight">删除</a></td>
                        <td align="center"><%if(t6355.F08.equals(YesOrNo.yes)) {%>默认地址<%} %></td>
                      </tr>
                      <%i++;}} else {%>
                       <tr>
                           <td colspan="9" align="center">暂无数据</td>
                       </tr>
                       <%} %>
                    </table>
                    <!--分页-->
                        <%
                            AbstractUserServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->
                </div>
            </div>
        </div> 
        </form>       
        <!--右边内容-->
        </div>
    </div>
    <div class="popup_bg" style="display: none;"></div>
    <!-- 新增收货地址弹窗 -->
    <div class="dialog dialog_shipping_address" id="addAddress_dialog" style="display: none;">
        <div class="title">
            <a href="javascript:void(0);" class="out close" id="addAddress_close"></a>新增收货地址
        </div>
        <div class="content">
            <form action="<%=controller.getURI(request, AddAddress.class)%>"
                  class="form1" method="post" onsubmit="return onSubmit()">
                  <%-- <input id="shengId" value="<%=request.getParameter("sheng")%>" type="hidden"/>
                  <input id="shiId" value="<%=request.getParameter("shi")%>" type="hidden"/>
                  <input id="xianId" value="<%=request.getParameter("xian")%>" type="hidden"/> --%>
               <ul class="text_list">
               		<%
                        String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(ermsg)) { %>
                    <li style="color: red;border: 1px solid red;padding: 10px;text-align: center;"><%
                        StringHelper.filterHTML(out, ermsg);%></li>
                    <%} %>
		            <li>
		            	<div class="til"><span class="red">*</span> 收货人：</div>
		                <div class="con">
		                	<input name="F03" type="text" class="text_style required max-length-30" maxlength="30" value="<%StringHelper.filterHTML(out, request.getParameter("F03")); %>" mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法姓名"/>
		                    <p tip></p>
                        	<p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 所在地区：</div>
		                <div class="con" id="regionAdd">
		                	<select name="sheng" id="sheng" class="required select6">
		                	</select>
		                    <p tip></p>
                        	<p errortip class="" style="display: none"></p>
		              </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 详细地址：</div>
		                <div class="con">
		                	<input name="F05" type="text" class="text_style required max-length-100" maxlength="100" value="<%StringHelper.filterHTML(out, request.getParameter("F05")); %>" />
		                    <p tip></p>
							<p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 手机号码：</div>
		                <div class="con">
		                	<input name="F06" type="text" class="text_style required phonenumber max-length-11" maxlength="11" value="<%StringHelper.filterHTML(out, request.getParameter("F06")); %>"/>
		                    <p tip></p>
                            <p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til">邮编：</div>
		                <div class="con">
		                	<input name="F07" type="text" class="text_style postcode" value="<%StringHelper.filterHTML(out, request.getParameter("F07")); %>"/>
			            	<p tip></p>
	                        <p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til">&nbsp;</div>
		            	<%YesOrNo t6355_F08 = EnumParser.parse(YesOrNo.class, request.getParameter("F08"));%>
		                <div class="con"><input name="F08" type="checkbox" value="yes" <%if (t6355_F08 == YesOrNo.yes) { %> checked="checked" <%} %>/> 设为默认地址</div>
		            </li>
		        </ul>
                <div class="tc mt20">
                    <input type="submit" class="btn04 btn btn001 sumbitForme"
                           fromname="form1" style="cursor: pointer;" value="提交">
					<input type="button" class="btn04 btn btn001 close btn_gray"
						   style="cursor: pointer;" value="取消">
                </div>
            </form>
        </div>
    </div>
    <!-- 修改收货地址弹窗 -->
    <div class="dialog dialog_shipping_address" id="updateAddress_dialog" style="display: none;">
        <div class="title">
            <a href="javascript:void(0);" class="out close" id="addAddress_close"></a>修改收货地址
        </div>
        <div class="content">
            <form action="<%=controller.getURI(request, UpdateAddress.class)%>"
                  class="form2" method="post" onsubmit="return onSubmit()">
                  <input type="hidden" id="updateAddressId" name="updateAddress" value=""/>
               <ul class="text_list">
               		<%
                        String ermsgUpdate = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(ermsgUpdate)) { %>
	                    <li style="color: red;border: 1px solid red;padding: 10px;text-align: center;"><%
	                        StringHelper.filterHTML(out, ermsg);%></li>
	                    <%} %>
		            <li>
		            	<div class="til"><span class="red">*</span> 收货人：</div>
		                <div class="con">
		                	<input name="harvestName" type="text" class="text_style required max-length-30" maxlength="30" value="" mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法的姓名"/>
		                    <p tip></p>
                        	<p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 所在地区：</div>
		                <div class="con" id="regionUpdate">
		                	<select name="sheng" id="shengUpdate" class="select6 required">
		                	</select>
		                    <select name="shi" id="shiUpdate" class="select6">
		                	</select>
		                    <select name="xian" id="xianUpdate" class="select6">
		                	</select>
		                    <p tip></p>
                        	<p errortip class="" style="display: none"></p>
		              </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 详细地址：</div>
		                <div class="con">
		                	<input name="fullAddress" type="text" class="text_style required max-length-100"  maxlength="100" value="" />
		                    <p tip></p>
                        	<p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til"><span class="red">*</span> 手机号码：</div>
		                <div class="con">
		                	<input name="phone" type="text" class="text_style required phonenumber" maxlength="11" value=""/>
		                    <p tip></p>
                            <p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til">邮编：</div>
		                <div class="con">
		                	<input name="postcode" type="text" class="text_style postcode" value=""/>
		                	<p tip></p>
                            <p errortip class="" style="display: none"></p>
		                </div>
		            </li>
		            <li>
		            	<div class="til">&nbsp;</div>
		            	<%YesOrNo t6355_F08Update = EnumParser.parse(YesOrNo.class, request.getParameter("yesOrNo"));%>
		                <div class="con"><input name="yesOrNo" id="F08" type="checkbox" value="yes" <%if (t6355_F08Update == YesOrNo.yes) { %> checked="checked" <%} %>/> 设为默认地址</div>
		            </li>
		        </ul>
                <div class="tc mt20">
                    <input type="submit" class="btn04 btn btn001 sumbitForme"
                           fromname="form2" style="cursor: pointer;" value="提交">
					<input type="button" class="btn04 btn btn001 close btn_gray"
						    style="cursor: pointer;" value="取消">
                </div>
            </form>
        </div>
    </div>
    <!-- 删除收货地址弹窗 -->
    <div class="dialog" style="display: none;" id="delc">
    <div class="title"><a href="javascript:void(0);" class="out close"></a>提示</div>
	    <div class="content">
	        <div class="tip_information">
	            <div class="question"></div>
	            <div class="tips">
	                <input type="hidden" name="delId" id="delId">
	                <span class="f20 gray3">确认删除该收货地址吗?</span>
	            </div>
	        </div>
	        <div class="tc mt20"><a href="javascript:okDelete();" class="btn01">确定</a><a href="javascript:void(0);"
	                                                                                    class="btn01 btn_gray ml20 close">取消</a>
	        </div>
	    </div>
	</div>
    
    <div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript">
	$(function() {
		$("#addAddress").click(function() {
			var count = '<%=countAddress%>';
			$("div.popup_bg").show();
			if(count>=20){
				$("#info").html(showDialogInfo("您已达到新增地址规则数量最大值，<br/>请删除已有地址，再重新新增地址.","doubt"));
			}else{
				var regionAdd = "<select name=\"sheng\" id=\"sheng\" class=\"required select6\"></select>";
            	regionAdd += "<p tip></p><p errortip class='' style='display: none'></p>";
            	$("#regionUpdate").html("");
				$("#regionAdd").html(regionAdd);
				initRegion();
				$("#addAddress_dialog").show();
			}
		});
		
		$(".close").click(function () {
			clearData();
	        $("div.dialog").hide();
	        $("div.popup_bg").hide();
	    });
	});
	
	function clearData(){
		$("input[name='F03'],input[name='harvestName']").val('');
		$("input[name='F05'],input[name='fullAddress']").val('');
		$("input[name='F06'],input[name='phone']").val('');
		$("input[name='F07'],input[name='postcode']").val('');
		$("input[name='F08'],input[name='yesOrNo']").attr("checked",false);
		$("select[name='sheng']").html(getSelectHtml(region, $("#shengId").val()));
		$("select[name='shi'],select[name='xian']").empty();
		$error = $("p[errortip]");
		$tip = $("p[tip]");
		$error.removeClass("red");
		$error.hide();
		$tip.show();
	}
		
	function deleteAddress(id) {
        $("#delId").val(id);
        $("div.popup_bg").show();
        $("#delc").show();
    }
	
	function okDelete() {
        var id = $("#delId").val();
        var data = {"value": id};
        $.ajax({
            type: "post",
            dataType: "html",
            url: "<%=controller.getURI(request, DelAddress.class)%>",
            data: data,
            success: function (data) {
            	if (data == "failed") {
            		$("div.dialog").hide();
            		$("#info").html(showDialogInfo("删除收货地址失败！","error"));
                    return false;
                }else {
                	location.href = "<%=controller.getURI(request, HarvestAddress.class)%>";
                }
            }
        });
    }
	
	function updateAddress(id) {
		var data = {"value": id};
		$("#updateAddressId").val(id);
		
        $("div.popup_bg").show();
        $("#updateAddress_dialog").show();
        var regionUpdate = '<input id="shengId" value="" type="hidden"/>';
		regionUpdate +='<input id="shiId" value="" type="hidden"/>';
		regionUpdate +='<input id="xianId" value="" type="hidden"/>';
		regionUpdate += '<select name="sheng" id="sheng" class="select6 required"></select>';
		regionUpdate += '<select name="shi" id="shi" class="select6"></select>';
		regionUpdate += '<select name="xian" id="xian" class="select6"></select>';
		regionUpdate += '<p tip></p><p errortip class="" style="display: none"></p>';
		$("#regionAdd").html("");
		$("#regionUpdate").html(regionUpdate);
        $.ajax({
            type: "post",
            dataType: "html",
            url: "<%=controller.getURI(request, GetAddress.class)%>",
            data: data,
            success: function (returnData) {
            	returnData = eval("("+returnData+")");
                $("input[name='harvestName']").val(returnData.F03);
                var shengId = 0;
                var shiId = 0;
                var xianId = 0;
                var regionId = returnData.F04+"";
				$("#shengId").val(regionId.substr(0,2)+"0000");
				$("#shiId").val(regionId.substr(0,4)+"00");
				$("#xianId").val(regionId);
				initRegion();
               /*  if (regionId % 10000 < 100) {
                    $("#shengUpdate").html(getSelectHtml(region, regionId));
                } else if (regionId % 100 < 1) {
                	$("#shengUpdate").html(getSelectHtml(region, regionId / 10000 * 10000));
                	$("#shiUpdate").html(getSelectHtml(region, regionId));
                } else {
                	$("#shengUpdate").html(selectShengHtml(region,Math.floor(regionId / 10000) * 10000));
                	$("#shiUpdate").html(selectShiHtml(region,Math.floor(regionId / 10000) * 10000, Math.floor(regionId / 100) * 100));
                	$("#xianUpdate").html(selectXianHtml(region,Math.floor(regionId / 10000) * 10000, Math.floor(regionId / 100) * 100,regionId));
                } */
                $("input[name='fullAddress']").val(returnData.F05);
                $("input[name='phone']").val(returnData.F06);
                $("input[name='postcode']").val(returnData.F07);
                if(returnData.F08 == "yes"){
                	$("input[name='yesOrNo']").attr("checked","checked");
                }else{
                	$("input[name='yesOrNo']").removeAttr("checked");
                }
            }
        });
    }

	    
</script>
</body>
</html>