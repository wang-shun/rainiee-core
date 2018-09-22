<%@page import="com.dimeng.p2p.S63.enums.T6340_F09"%>
<%@page import="com.dimeng.p2p.S63.entities.T6340" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S63.entities.T6344" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdgl" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.UpdateHdgl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "HDGL";
    T6340 activity = ObjectHelper.convert(request.getAttribute("activity"), T6340.class);
    T6344[] t6344s = ObjectHelper.convert(request.getAttribute("t6344s"), T6344[].class);
%>
<div class="right-container">
    <div class="viewFramework-body">
        <div class="viewFramework-content">
            <div class="p20">
                <!--切换栏目-->
                <form action="<%=controller.getURI(request, UpdateHdgl.class) %>" method="post" name="form1"
                      class="form1" onsubmit="return check_zdczed();">
                    <input type="hidden" name="jlType" value="<%=activity.F03.name()%>"/>
                    <input type="hidden" name="hdType" value="<%=activity.F04.name()%>"/>
                    <input type="hidden" name="id" value="<%=activity.F01%>"/>

                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改活动
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <span class="red"><%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>奖励类型：</span>
                                        <%=activity.F03.getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动类型：</span>
                                        <%=activity.F04.getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动名称：</span>
                                        <input type="text" name="title"
                                               class="text border w300 yw_w5 required min-length-2 max-length-20"
                                               value="<%=activity.F05 %>" maxlength="20">
                                        <span tip>(2-20个字)</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%
                                        String hdType = activity.F04.name();
                                        String jlType = activity.F03.name();
                                    if(!T6340_F04.exchange.name().equals(hdType) && !T6340_F04.integraldraw.name().equals(hdType))
                                    {
                                    %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动开始时间：</span>
                                        <input type="text" name="startTime" readonly="readonly" id="datepicker1"
                                               class="text border w300 yw_w5 date required"
                                               value="<%=DateTimeParser.format(activity.F06,"yyyy-MM-dd") %>">
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动结束时间：</span>
                                        <input type="text" name="endTime" readonly="readonly" id="datepicker2"
                                               class="text border w300 yw_w5 date required"
                                               value="<%=DateTimeParser.format(activity.F07,"yyyy-MM-dd") %>">
                                        <span id="endTimeError"></span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%}%>
                                </ul>

                                <%  String operationPrompt = "";
                                    //String f_width = "w100";

                                    if (T6340_F04.recharge.name().equals(hdType) || T6340_F04.firstrecharge.name().equals(hdType)  ||
                                            T6340_F04.tjsccz.name().equals(hdType)) {operationPrompt = "充值金额大于等于"; } %>
                                <%if (T6340_F04.tjsctz.name().equals(hdType) || T6340_F04.investlimit.name().equals(hdType)) { operationPrompt = "投资金额大于等于"; }  %>
                                <%if (T6340_F04.tjtzze.name().equals(hdType)) { operationPrompt = "累计投资金额大于等于"; }  %>

                                <div class="pl200 pr">
                                    <ul id="div_li_id" class="gray6">
                                        <span class="display-ib pa left0 top0 w200 tr mr5"><em class="red pr5">*</em>赠送规则：</span>
                                        <%for (int i = 0;i<t6344s.length;i++){%>
                                        <li class="mb10 <%if(i == 0){%>role_li_first<%}else{%>role_li_other<%}%> role_li_length">
                                            <ul class="input-list-container clearfix ml10 p10 border" <%--<%if(T6340_F03.experience.name().equals(jlType)){%> style="width:60%;"<%}%>--%>>
                                                <%if(operationPrompt != ""){
                                                %>
                                                <li class="h60">
                                                    <span class="display-ib  tr mr5 fl"><em class="red pr5">*</em><%=operationPrompt%>：</span>
                                                    <div class="fl">
                                                        <input type="text"
                                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                                               maxlength="10" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                                               mtestmsg="必须为整数" name="zdczed" value="<%=t6344s[i].F06%>"
                                                               class="text border w80 yw_w5 required mulriple-100 min-size-100"
                                                               />元
                                                        <br>
                                                        <span tip>100的整数倍</span>
                                                        <span errortip class="" style="display: none"></span>
                                                     </div>
                                                </li>
                                                <%}%>

                                                <% if (T6340_F03.redpacket.name().equals(jlType)) {  %>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>红包金额：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="9" name="jl"
                                                               class="text border w80 yw_w5 required isint min-size-1 max-size-9999"
                                                               value="<%=t6344s[i].F05.intValue()%>"
                                                               />元
                                                        <br>
                                                        <span tip>范围1-9999</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>

                                                <%}else if (T6340_F03.experience.name().equals(jlType)) {  %>
                                                <li class="h60">
                                                        <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>体验金额：</span>
                                                        <div class="fl">
                                                            <input type="text" maxlength="9" name="jl"
                                                                   class="text border w80 yw_w5 required isint min-size-1 max-size-9999"
                                                                   value="<%=t6344s[i].F05.intValue()%>"
                                                                   />元
                                                            <br>
                                                            <span tip>范围1-9999</span>
                                                            <span errortip class="" style="display: none"></span>
                                                        </div>
                                                </li>
                                                <%} else { %>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>加息利率：</span>
                                                    <div class="fl">
                                                    <input type="text" name="jl" mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/"
                                                           mtestmsg="只能有两位小数" maxlength="5"
                                                           class="text border w80 yw_w5 required" value="<%=t6344s[i].F05%>"
                                                           />%
                                                        <br>
                                                        <span tip>精确到小数点后两位</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                                <% }  %>
                                                <li class="h60 mw300">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>有效期：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="4" name="syqx"
                                                               class="text border w80 required isint min-size-1 max-size-9999"
                                                               value="<%=t6344s[i].F04%>"/>

                                                        <select name="syqxType" class="border mr20 h32 mw60 required">
                                                            <% String syqxType = t6344s[i].F09.name(); %>
                                                            <option value="S" <%if ("S".equals(syqxType)) { %> selected="selected" <%} %>>个月
                                                            </option>
                                                            <option value="F" <%if ("F".equals(syqxType)) { %> selected="selected" <%} %>>天
                                                            </option>
                                                        </select>
                                                        <br>
                                                        <span tip></span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>

                                                </li>
                                                <%if (!T6340_F04.foruser.name().equals(hdType) && !T6340_F04.exchange.name().equals(hdType) && !T6340_F04.integraldraw.name().equals(hdType)) { %>
                                                <%if(T6340_F03.experience.name().equals(jlType))
                                                {%>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>体验金数量：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="9" name="num"
                                                               class="text border w80 yw_w5 required isint min-size-1 max-size-999999"
                                                               value="<%=t6344s[i].F03%>"
                                                               />个
                                                        <br>
                                                        <span tip>范围1-999999</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                                <%} else {%>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>
                                                    <%if (T6340_F03.redpacket.name().equals(jlType)) {%>红包数量<%} else { %>加息券数量<%} %>：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="9" name="num"
                                                               class="text border w80 yw_w5 required isint min-size-1 max-size-999999"
                                                               value="<%=t6344s[i].F03%>"
                                                               />个
                                                        <br>
                                                        <span tip>范围1-999999</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                                <%} }%>
                                                <%if(!T6340_F03.experience.name().equals(jlType)) {%>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5"></em>投资满：</span>
                                                    <div class="fl">
                                                        <input type="text" name="leastInvest" 
                                                        onkeyup="value=value.replace(/\D/g,'')" value="<%=t6344s[i].F07.compareTo(BigDecimal.ZERO)==0?"":t6344s[i].F07 %>"
                                                               maxlength="10"
                                                               class="text border w80 yw_w5"/>元使用
                                                         <br>
                                                        <span tip></span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                                <%}else{%>
                                                    <li class="h60">
                                                        <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>预计收益期：</span>
                                                        <div class="fl">
                                                            <input type="text" maxlength="4" name="validDate"
                                                                   class="text border w80 required isint min-size-1 max-size-9999"
                                                                   value="<%=t6344s[i].F10%>"/>
                                                            <select name="validMethod" class="border mr20 h32 mw60 required">
                                                                <% String validMethod = t6344s[i].F11; %>
                                                                <option value="true" <%if ("true".equals(validMethod)) { %> selected="selected" <%} %>>个月
                                                                </option>
                                                                <option value="false" <%if ("false".equals(validMethod)) { %> selected="selected" <%} %>>天
                                                                </option>
                                                            </select>
                                                            <br>
                                                            <span tip></span>
                                                            <span errortip class="" style="display: none"></span>
                                                        </div>
                                                    </li>
                                                <%}%>
                                                <%
                                                    //if(!T6340_F03.experience.name().equals(jlType)){
                                                    if (!T6340_F04.foruser.name().equals(hdType) &&
                                                            !T6340_F04.register.name().equals(hdType)  &&
                                                            !T6340_F04.birthday.name().equals(hdType)) {
                                                %>
                                                <%if(i==0){%>
                                                <li class="h60"> <a id="addAmountRange" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon" onclick="addLi();"></a></li>
                                                <%}else{%>
                                                <li class="h60"><a id="addAmountRange" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon" onclick="delLi(this);"></a></li>
                                                <%}%>
                                                <%}
                                                //}
                                                %>
                                            </ul>

                                        </li>
                                        <%}%>
                                    </ul>
                                </div>

                                <ul class="gray6">
                                    <%if (T6340_F04.birthday.name().equals(activity.F04.name())) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>领取条件：</span>
                                        <input type="radio" name="lqtj"
                                               value="login" <%if (T6340_F09.login.name().equals(activity.F09.name())) { %>
                                               checked="checked"<%} %>/>生日当天登录
                                        <input type="radio" name="lqtj"
                                               value="invest" <%if (T6340_F09.invest.name().equals(activity.F09.name())) { %>
                                               checked="checked"<%} %>/>生日当天投资
                                        <input type="radio" name="lqtj"
                                               value="all" <%if (T6340_F09.all.name().equals(activity.F09.name())) { %>
                                               checked="checked"<%} %>/>不限
                                    </li>
                                    <%}%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl">备注：</span>
                                            <textarea name="remark" cols="45" rows="5"
                                                      class="w400 h120 border p5 max-length-100"><%StringHelper.format(out, activity.F10, fileStore); %></textarea>
                                        <span tip>最多输入100个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input type="submit"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                               fromname="form1" value="保存">
                                        <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                               onclick="window.location.href='<%=controller.getURI(request, SearchHdgl.class)%>'">
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
<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        //页面初始化的时候，生成一个用来克隆：id="clone_li_id"的li元素
        initCloneLi();

        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker1").datepicker({
            minDate: new Date(),
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
                var dates = $("#datepicker1")
                var d = dates.val();
                if (d != "") {
                    var $error = dates.nextAll("span[errortip]");
                    var $tip = dates.nextAll("span[tip]");
                    $error.removeClass("error_tip");
                    $error.hide();
                    $tip.show();
                }
            }
        });
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        $("#datepicker2").datepicker({
            inline: true,
            minDate: $("#datepicker1").datepicker().val(),
            onSelect: function (selectedDate) {
                var dates = $("#datepicker2");
                var d = dates.val();
                if (d != "") {
                    var $error = dates.nextAll("span[errortip]");
                    var $tip = dates.nextAll("span[tip]");
                    $error.removeClass("error_tip");
                    $error.hide();
                    $tip.show();
                }
            }
        });

        $("input[name='sygz']").bind("change", function () {
            changeRule();
        });
    });
    function changeRule() {
        if ($("input[name='sygz']:checked").val() == 0) {
            $(".rule").hide();
        } else {
            $(".rule").show();
        }
    }

    //删除规则事件
    function delLi(obj){
        $(obj).parent().parent().parent().remove();
    }

    //添加规则事件
    function addLi(obj){
        if($(".role_li_length").length>10){
            $("#info").html(showDialogInfo("最多能增加10个规则","wrong"));
            return;
        }
        var clone_li = $('#clone_li_id').clone(true);
        clone_li.attr("id","clone_li_id_temp"); //改变ID
        clone_li.show();
        $("#div_li_id").append(clone_li);
    }

    //页面初始化的时候，生成一个用来克隆：id="clone_li_id"的li元素
    function initCloneLi(){
        var clone_li = $('.role_li_first').clone(true);
        clone_li.hide();
        clone_li.attr("id","clone_li_id");
        $(".popup_bg").append(clone_li);
        //将克隆好的输入框全部置为空
        $("#clone_li_id :text").each(function(){
            $(this).val("");
        });
        $("#clone_li_id #addAmountRange").removeClass("add-radiusbtn-icon")
        $("#clone_li_id #addAmountRange").addClass("remove-radiusbtn-icon");
        $("#clone_li_id #addAmountRange").attr("onclick","delLi(this);"); //替换单击事件
    }

    //检查投资、充值金额有没有重复的
    function check_zdczed(){
        var if_check = '<%=operationPrompt%>';
        if(if_check !=''){
            var y = new Array();
            var s = $("input[name='zdczed']");
            for(var i = 0; i < s.length; i++) {
                if($(s[i]).val() == ''){
                    continue;
                }
                if(typeof y[$(s[i]).val()] != "undefined") {
                    $("#info").html(showDialogInfo("【"+if_check+"】字段存在重复金额，请重新输入","wrong"));
                    return false;
                }else{
                    y[$(s[i]).val()] = true;
                }
            }
        }
        return true;
    }
</script>
</body>
</html>