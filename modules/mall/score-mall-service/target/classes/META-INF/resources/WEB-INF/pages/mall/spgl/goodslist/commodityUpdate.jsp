<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityList"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.CheckUserInfo" %>
<%@ page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimeng.p2p.common.enums.YesOrNo" %>
<%@ page import="com.dimeng.p2p.repeater.score.entity.T6351Ext" %>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityUpdate" %>
<%@ page import="java.math.BigDecimal" %>
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
    T6351Ext t6351 = ObjectHelper.convert(request.getAttribute("t6351"), T6351Ext.class);
    List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
    boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>商品管理-修改商品
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, CommodityUpdate.class)%>" method="post" id="form1" enctype="multipart/form-data" class="form1" onsubmit="return onSubmit();">
                                <input type="hidden" value="<%=t6351.F01%>" name="F01"/>
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品编码：</span>
                                        <input type="text" maxlength="30" onKeyUp="value=value.replace(/[^\w\/]/ig,'')" class="text border pl5 w300 required max-length-30 min-length-2" name="F02" value="<%=t6351.F02%>"/>
                                        <font>&emsp;长度2~30个字</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品名称：</span>
                                        <input type="text" maxlength="30" onKeyUp="value=value.replace(/\s/g,'')" class="text border pl5 w300 required max-length-30 min-length-2" name="F03" value="<%=t6351.F03%>" />
                                        <font>&emsp;长度2~30个字</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品类别：</span>
                                        <select id="catId" class="border mr20 h32 mw100 required" name="F04" onchange="changeVirtual()">
                                            <option value="">--请选择--</option>
                                            <%
                                                if (t6350List != null && t6350List.size() > 0) {
                                                    for (T6350 t6350 : t6350List) {
                                            %>
                                            <option value="<%=t6350.F01%>" <%if(t6350.F01 == t6351.F04){%>selected="selected"<%}%> code="<%=t6350.F02%>">
                                                <% StringHelper.filterHTML(out, t6350.F03); %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10 hide" id="ruleType"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em><span id="ruleName"></span>：</span>
                                    <select id="rule" class="border mr20 h32 mw100 required" name="F20">
                                        <option value="">--请选择--</option>

                                    </select>
                                    <span errortip class="" style="display: none"></span>
                                </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>支付方式：</span>
                                        <input type="checkbox" id="score" class="mr10" name="F16" value="<%=YesOrNo.yes%>" <%if(YesOrNo.yes == t6351.F16){ %>checked="checked"<%} %>/><label for="score">支持积分购买</label>
                                        <%if(allowsBalance){%>
                                            &nbsp;&nbsp;&nbsp;
                                            <input type="checkbox" id="money" class="mr10" name="F17" value="<%=YesOrNo.yes%>" <%if(YesOrNo.yes == t6351.F17){ %>checked="checked"<%} %>/><label for="money">支持余额购买</label>
                                        <%}%>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="score_li_id" style="display: none;"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品积分：</span>
                                        <input type="text" maxlength="10" class="text border pl5 w300 required max-length-14 isint minf-size-1" name="F05" value="<%if(t6351.F05 > 0){%><%=t6351.F05%><%}%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="money_li_id" style="display: none;"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品价格：</span>
                                        <input type="text" maxlength="10" class="text border pl5 w300 required max-length-14 minf-size-0.01"  mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F15" value="<%if(t6351.F15.compareTo(new BigDecimal("0.00")) > 0){%><%=t6351.F15%><%}%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10" id="marketPrice"><span class="display-ib w200 tr mr5"><em class="red pr5"></em>市场参考价：</span>
                                        <input type="text" maxlength="9" class="text border pl5 w300 max-length-9 minf-size-0.01"  mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为数字格式(且是两位小数)" name="F19" value="<%=t6351.F19.compareTo(BigDecimal.ZERO) == 0 ?"":t6351.F19 %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>商品库存：</span>
                                        <input type="text" maxlength="10" class="text border pl5 w300 required max-length-14 isint" name="F06" value="<%=t6351.F06%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">限购数量：</span>
                                        <input type="text" maxlength="10" class="text border pl5 w300 max-length-14 isint minf-size-0" name="F18" value="<%=t6351.F18%>"/>
                                        <font>&emsp;允许每个用户购买的最大数量值（值为0，则不限制）</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5">PC商品LOGO：</span>
                                        <input type="text" name="pcimage" class="text border pl5 w120 " value="<%=t6351.F08%>"/>
                                        <input type="file" name="F08" value="" class="w200" accept="<%=acceptStr%>">
                                        <font>&emsp;图片仅支持<%=allowFileType%>格式，大小不超过1MB，建议尺寸800*800</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">APP商品LOGO：</span>
                                        <input type="text" name="appimage" class="text border pl5 w120 " value="<%=t6351.F09%>"/>
                                        <input type="file" name="F09" value="" class="w200" accept="<%=acceptStr%>">
                                        <font>&emsp;图片仅支持<%=allowFileType%>格式，大小不超过1MB，建议尺寸280*280</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"> <span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>商品详情：</span>
                                        <div class="pl200 ml5">
						                    <textarea name="F10" cols="100" rows="8" style="width: 700px; height: 500px; visibility: hidden;" class="required min-length-20 max-length-60000">
                                                <%StringHelper.format(out, t6351.F10, fileStore);%>
                                            </textarea>
                                            <span id="F10Error" tip>20-60000字</span>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                        <div class="clear"></div>
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

    $(function(){
        <%if(YesOrNo.yes == t6351.F16){ %>
            $("#score_li_id").show();
        <%}if (YesOrNo.yes == t6351.F17 && allowsBalance){%>
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

        changeVirtual();
    });

    function changeVirtual(){
        var code = $("#catId option:selected").attr("code");
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
            var ruleId = "<%=t6351.F20%>";
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
                            if(ruleId == value.ruleId) {
                                optHTML += "<option value='" + value.ruleId + "' selected='selected'>" + value.rule + "</option>";
                            }else{
                                optHTML += "<option value='" + value.ruleId + "'>" + value.rule + "</option>";
                            }
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
            $("#score").attr("checked","checked").attr("disabled","disabled");
            $("#marketPrice").hide();
            $("#score_li_id").show();
            $("#money").hide();
            $("#money").next("label").hide();
        }
        else
        {
            $("#ruleType").hide();
            $("#score").attr("checked",false).attr("disabled",false);
            $("#marketPrice").show();
            $("#rule").html("<option value=''>--请选择--</option>");
            $("#score_li_id").hide();
            $("#money").show();
            $("#money").next("label").show();
        }
    }

    function onSubmit() {
        if(!$("input[name='F16']").is(':checked') && !$("input[name='F17']").is(':checked')) {
            $("#info").html(showDialogInfo("请选择支付方式", "wrong"));
            $("div.popup_bg").show();
            return false;
        }
    }

    //保存成功，跳转到商品列表
    function returnList(){
        location.href='<%=controller.getURI(request, CommodityList.class)%>';
    }
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
    $("#info").html(showDialogInfoForMethod("<%=infoMessage%>", "yes","returnList()"));
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