<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityList"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.CheckUserInfo" %>
<%@ page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityAdd" %>
<%@ page import="com.dimeng.p2p.S63.enums.T6351_F11" %>
<%@ page import="com.dimeng.p2p.common.enums.YesOrNo" %>
<%@ page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.SelectRules" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "SPLB";
    List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
    String allowFileType = configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE);
    String [] types = allowFileType.split(",");
    String acceptStr = "";
    for(String type:types){
        acceptStr += "image/"+type+",";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>商品管理-新增商品
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, CommodityAdd.class)%>" method="post" onsubmit="return onSubmit();" id="form1" enctype="multipart/form-data" class="form1">
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品编码：</span>
                                        <input type="text" maxlength="30" onKeyUp="value=value.replace(/[^\w\/]/ig,'')"
                                        	class="text border pl5 w300 required max-length-30 min-length-2" name="F02" value="<%StringHelper.filterHTML(out, request.getParameter("F02"));%>"/>
                                        <font>&emsp;长度2~30个字</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品名称：</span>
                                        <input type="text" maxlength="30" onKeyUp="value=value.replace(/\s/g,'')"  class="text border pl5 w300 required max-length-30 min-length-2" name="F03" value="<%StringHelper.filterHTML(out, request.getParameter("F03"));%>"/>
                                        <font>&emsp;长度2~30个字</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品类别：</span>
                                        <select id="catId" class="border mr20 h32 mw100 required" name="F04">
                                            <option value="">--请选择--</option>
                                            <%
                                                if (t6350List != null && t6350List.size() > 0) {
                                                    for (T6350 t6350 : t6350List) {
                                            %>
                                            <option code="<%=t6350.F02%>" <%if((t6350.F01+"").equals(request.getParameter("F04"))){ %>selected="selected"<%} %> value="<%=t6350.F01%>">
                                                <% StringHelper.filterHTML(out, t6350.F03); %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10 hide" id="ruleType"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em><span id="ruleName"></span>：</span>
                                        <select id="rule" class="border mr20 h32 mw100 required" name="F20">
                                            <option value="">--请选择--</option>

                                        </select>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>支付方式：</span>
                                        <input type="checkbox" id="score" class="mr10" name="F16" value="<%=YesOrNo.yes%>" <%if(YesOrNo.yes.name().equals(request.getParameter("F16"))){ %>checked="checked"<%} %>/><label for="score">支持积分购买</label>
                                        <%if(BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY))){%>
                                            &nbsp;&nbsp;&nbsp;
                                            <input type="checkbox" id="money" class="mr10" name="F17" value="<%=YesOrNo.yes%>" <%if(YesOrNo.yes.name().equals(request.getParameter("F17")) ){ %>checked="checked"<%} %>/><label for="money">支持余额购买</label>
                                        <%}%>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="score_li_id" style="display: none;"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品积分：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 required max-length-9 isint minf-size-1" name="F05" value="<%StringHelper.filterHTML(out, request.getParameter("F05"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="money_li_id" style="display: none;"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品价格：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 required max-length-9 minf-size-0.01"  mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F15" value="<%StringHelper.filterHTML(out, request.getParameter("F15"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="marketPrice"><span class="display-ib w200 tr mr5"><em class="red pr5"></em>市场参考价：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 max-length-9 minf-size-0.01"  mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F19" value="<%StringHelper.filterHTML(out, request.getParameter("F19"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品库存：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 required max-length-9 isint" name="F06" value="<%StringHelper.filterHTML(out, request.getParameter("F06"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">限购数量：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 max-length-9 isint minf-size-0" name="F18" value="<%StringHelper.filterHTML(out, request.getParameter("F18"));%>"/>
                                        <font>&emsp;允许每个用户购买的最大数量值（值为0，则不限制）</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5">PC商品LOGO：</span>
                                        <input type="file" name="F08" value="" accept="<%=acceptStr%>">
                                        <font>&emsp;图片仅支持<%=allowFileType%>格式，大小不超过1MB，建议尺寸800*800</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">APP商品LOGO：</span>
                                        <input type="file" name="F09" value="" accept="<%=acceptStr%>">
                                        <font>&emsp;图片仅支持<%=allowFileType%>格式，大小不超过1MB，建议尺寸280*280</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"> <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>商品详情：</span>
                                        <div class="pl200 ml5">
                                            <textarea name="F10" cols="100" rows="8" style="width: 700px; height: 500px; visibility: hidden;" class="required min-length-20 max-length-60000"><%StringHelper.format(out, request.getParameter("F10"), fileStore);%></textarea>
                                            <span id="F10Error" tip>20-60000字</span>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">上架：</span>
                                        <input type="radio" name="F11" value="<%=T6351_F11.sold.name()%>" <%if(StringHelper.isEmpty(request.getParameter("F11")) || T6351_F11.sold.name().equals(request.getParameter("F11"))){ %>checked="checked"<% }%> class="mr5 ml10"/>是
                                        <input type="radio" name="F11" value="<%=T6351_F11.forsold.name()%>" class="mr5 ml10" <%if(T6351_F11.forsold.name().equals(request.getParameter("F11"))){ %>checked="checked"<% }%>/>否
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5" id="saveBtn">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1" value="提交"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, CommodityList.class) %>'"
                                                   value="取消">
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
<div class="popup_bg hide"></div>
<div id="info"></div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">

    var _cURL = '<%=controller.getURI(request, CheckUserInfo.class)%>';
    var isCommit = true;
    //富文本框
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="F10"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.COMMODITY_DETAILS_FILE.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                $error = $("#F10Error");
                if (this.count() > 60000 || this.count() < 20) {
                    $error.addClass("error_tip");
                    $error.html("长度不对，20-60000字！");
                    jkmsFlgs = true;
                }
                else {
                    $error.removeClass("error_tip");
                    $error.html("20-60000字");
                    jkmsFlgs = false;
                }
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });
    
    function onSubmit() {
    	
    	if(!$("input[name='F16']").is(':checked') && !$("input[name='F17']").is(':checked')) {
    		$("#info").html(showDialogInfo("请选择支付方式", "wrong"));
    	    $("div.popup_bg").show();
    	    return false;
    	}
    	$error = $("#F10Error");
        if (editor1.count('text') == '') {
        	$error.addClass("error_tip");
        	$error.html("商品详情不能为空");
        	 jkmsFlgs = true;
            return false;
        }
        else {
        	$error.removeClass("error_tip");
        	$error.html("&nbsp;");
        	jkmsFlgs = false;
            return true;
        }
    }

    $(function(){
        <%if(YesOrNo.yes.name().equals(request.getParameter("F16"))){ %>
            $("#score_li_id").show();
        <%}if (YesOrNo.yes.name().equals(request.getParameter("F17"))){%>
            $("#money_li_id").show();
        <%}%>
        $("#score").bind("click",function(){
            if ($(this).attr("checked") == "checked") {
                $("#score_li_id").show();
            } else {
                $("#score_li_id").hide();
            }
        })

        $("#money").bind("click",function(){
            if($(this).attr("checked") == "checked"){
                $("#money_li_id").show();
            }else{
                $("#money_li_id").hide();
            }
        })

        $("#catId").on("change", function () {
            if ($(this).val() != null && $(this).val() != "") {
                $(this).nextAll("span").empty();
            }
            var code = $(this).find("option:selected").attr("code");
            if(code == "REDPACKET" || code == "INTEREST" || code == "EXPERIENCE")
            {
                var jlType;
                var hdType = "exchange";
                if(code == "REDPACKET")
                {
                    $("#ruleName").html("红包金额");
                    jlType = "redpacket";
                }
                else if(code == "INTEREST")
                {
                    $("#ruleName").html("加息利率");
                    jlType = "interest";
                }
                else if(code == "EXPERIENCE")
                {
                    $("#ruleName").html("体验金金额");
                    jlType = "experience";
                }
                $.ajax({
                    type:"post",
                    dataType:"json",
                    url:"<%=controller.getURI(request,SelectRules.class)%>",
                    data:{jlType:jlType,hdType:hdType},
                    async: false,
                    success:function(returnData){
                        if(returnData != null)
                        {
                            var optHTML = "";
                            $.each(returnData, function (n, value) {
                                optHTML+="<option value='"+value.ruleId+"'>"+value.rule+"</option>";
                                $("#rule").append(optHTML);
                                optHTML="";
                            });
                        }else{
                            $("#rule").html("<option value=''>--请选择--</option>");
                        }
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){
                        //alert(textStatus);
                    }
                });
                $("#ruleType").show();
                $("#marketPrice").hide();
                $("#score").attr("checked","checked").attr("disabled","disabled");
                $("#score_li_id").show();
                $("#money").hide();
                $("#money").next("label").hide();
            }
            else
            {
                $("#ruleType").hide();
                $("#score").attr("checked",false).attr("disabled",false);
                $("#rule").html("<option value=''>--请选择--</option>");
                $("#score_li_id").hide();
                $("#marketPrice").show();
                $("#money").show();
                $("#money").next("label").show();
            }
        });
    })

</script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMessage%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>