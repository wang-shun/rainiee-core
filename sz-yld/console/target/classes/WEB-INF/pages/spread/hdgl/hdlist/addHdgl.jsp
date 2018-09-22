<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdgl"%>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F09" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.ImportUser" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.AddHdgl" %>
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
    String[] users = ObjectHelper.convertArray(  request.getAttribute("users"), String.class);
    String hdType = request.getParameter("hdType"); //活动类型
    String jlType = request.getParameter("jlType"); //奖励类型
    String jl = request.getParameter("jl"); //金额
%>
<div class="right-container">
    <div class="viewFramework-body">
        <div class="viewFramework-content">
            <div class="p20">
                <!--切换栏目-->
                <form action="<%=controller.getURI(request, AddHdgl.class) %>" method="post" name="form1" class="form1" onsubmit="return check_zdczed();">
                    <%=FormToken.hidden(serviceSession.getSession()) %>
                    <input type="hidden" name="jlType" value="<%=jlType%>"/>
                    <input type="hidden" name="hdType" value="<%=hdType%>"/>
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增活动
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6" id="ul_id">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>奖励类型：</span>
                                        <%=T6340_F03.parse(jlType).getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动类型：</span>
                                        <%=T6340_F04.parse(hdType).getChineseName() %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动名称：</span>
                                        <input type="text" name="title"
                                               class="text border w300 yw_w5 required min-length-2 max-length-20"
                                               value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(request.getParameter("title"))?request.getParameter("oldDataTitle"):request.getParameter("title"));%>"
                                               maxlength="20">
                                        <!-- <span></span> -->
                                        <span tip>(2-20个字)</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%if (T6340_F04.foruser.name().equals(hdType)) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                            class="red pr5">*</em>指定用户：</span>
                                            <textarea name="userId" id="userId" cols="45" rows="5" placeholder="发送给多人请用空格隔开，如：user01 user02"
                                                      class="w400 h120 border p5 required"><%=StringHelper.isEmpty(request.getParameter("oldData"))?"":request.getParameter("oldData")+"\n"%><%
                                                if (users != null) {
                                                    for (String s : users) {
                                            %><%=s + "\n"%><%
                                                    }
                                                }
                                            %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                        <a href="javascript:showImportUser();" id="importUser"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15 popup-link"
                                           data-wh="500*200"><i class="icon-i w30 h30 va-middle add-icon "></i>导入用户</a>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>支持导入csv、txt格式数据
                                    </li>
                                    <%} %>
                                    <%if (!T6340_F04.foruser.name().equals(hdType) && !T6340_F04.exchange.name().equals(hdType) && !T6340_F04.integraldraw.name().equals(hdType)) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动开始时间：</span>
                                        <input type="text" name="startTime" readonly="readonly" id="datepicker1"
                                               class="text border w300 yw_w5 date required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>">
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动结束时间：</span>
                                        <input type="text" name="endTime" readonly="readonly" id="datepicker2"
                                               class="text border w300 yw_w5 date required"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>">
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%} %>
                                </ul>

                                <%  String operationPrompt = "";
                                 if (T6340_F04.recharge.name().equals(hdType) || T6340_F04.firstrecharge.name().equals(hdType)  ||
                                            T6340_F04.tjsccz.name().equals(hdType)) {operationPrompt = "充值金额大于等于"; } %>
                                <%if (T6340_F04.tjsctz.name().equals(hdType) || T6340_F04.investlimit.name().equals(hdType)) { operationPrompt = "投资金额大于等于"; }  %>
                                <%if (T6340_F04.tjtzze.name().equals(hdType)) { operationPrompt = "累计投资金额大于等于"; }  %>
                                <div class="pl200 pr">
                                    <ul id="div_li_id" class="gray6">
                                        <span class="display-ib pa left0 top0 w200 tr mr5"><em class="red pr5">*</em>赠送规则：</span>
                                        <li class="mb10 role_li">
                                            <ul class="input-list-container clearfix ml10 p10 border" <%--<%if(T6340_F03.experience.name().equals(jlType)){%> style="width:60%;"<%}%>--%>>
                                            <%if(operationPrompt != ""){  %>
                                                <li class="h60">
                                                    <span class="display-ib tr mr5 fl"><em class="red pr5">*</em><%=operationPrompt%>：</span>
                                                    <div class="fl">
                                                        <input type="text" autocomplete="off"
                                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                                               maxlength="10" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                                               mtestmsg="必须为整数" name="zdczed"
                                                               value="<%StringHelper.filterHTML(out, request.getParameter("zdczed"));%>"
                                                               class="text border w80 yw_w5 required mulriple-100 min-size-100"
                                                                />元
                                                        <br>
                                                        <span tip>100的整数倍</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                            <%}%>
                                            <% if (T6340_F03.redpacket.name().equals(jlType) ) {  %>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>红包金额：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="9" name="jl"
                                                               class="text border w80 yw_w5 required isint min-size-1 max-size-9999"
                                                               value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(jl)?request.getParameter("oldDataJl"):jl);%>"
                                                                />元
                                                        <br>
                                                        <span tip>范围1-9999</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                            <% }else if (T6340_F03.experience.name().equals(jlType)) {  %>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>体验金额：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="9" name="jl"
                                                               class="text border w80 yw_w5 required isint min-size-1 max-size-9999"
                                                               value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(jl)?request.getParameter("oldDataJl"):jl);%>"
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
                                                               class="text border w80 yw_w5 required"
                                                               value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(jl)?request.getParameter("oldDataJl"):jl);%>"
                                                                />%
                                                        <br>
                                                        <span tip>精确到小数点后两位</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                            <%}%>
                                                <li class="h60 mw300">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>有效期：</span>
                                                    <div class="fl">
                                                        <input type="text" maxlength="4" name="syqx"
                                                               class="text border w80 required isint min-size-1 max-size-9999"
                                                               value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(request.getParameter("syqx"))?request.getParameter("oldDataSyqx"):request.getParameter("syqx"));%>"/>
                                                        <select name="syqxType" class="border mr20 h32 mw60 required">
                                                            <% String syqxType = StringHelper.isEmpty(request.getParameter("syqxType"))?request.getParameter("oldDataSyqxType"):request.getParameter("syqxType"); %>
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
                                                <%if(T6340_F03.experience.name().equals(jlType)) {%>
                                                    <li class="h60">
                                                        <span class="display-ib w100 tr mr5 fl"><em class="red pr5">*</em>体验金数量：</span>
                                                        <div class="fl">
                                                            <input type="text" maxlength="9" name="num"
                                                                   class="text border w80 yw_w5 required isint min-size-1 max-size-999999"
                                                                   value="<%StringHelper.filterHTML(out, request.getParameter("num"));%>"
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
                                                               value="<%StringHelper.filterHTML(out, request.getParameter("num"));%>"
                                                                />个
                                                        <br>
                                                        <span tip>范围1-999999</span>
                                                        <span errortip class="" style="display: none"></span>
                                                    </div>
                                                </li>
                                                <%} } %>
                                            <%if(!T6340_F03.experience.name().equals(jlType))  {%>
                                                <li class="h60">
                                                    <span class="display-ib w100 tr mr5 fl"><em class="red pr5"></em>投资满：</span>
                                                    <div class="fl">
                                                        <input type="text" name="leastInvest"
                                                        onkeyup="value=value.replace(/\D/g,'')"
                                                        value="<%StringHelper.filterHTML(out, StringHelper.isEmpty(request.getParameter("leastInvest"))? request.getParameter("oldDataLeastInvest"):request.getParameter("leastInvest"));%>"
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
                                                               value="<%StringHelper.filterHTML(out, request.getParameter("validDate"));%>"/>
                                                        <select name="validMethod" class="border mr20 h32 mw60 required">
                                                            <% String validMethod = request.getParameter("validMethod"); %>
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


                                            <%//if(!T6340_F03.experience.name().equals(jlType)){
                                                if (!T6340_F04.foruser.name().equals(hdType) &&
                                                        !T6340_F04.register.name().equals(hdType)  &&
                                                        !T6340_F04.birthday.name().equals(hdType)) {
                                            %>
                                                <a id="addAmountRange" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon"></a>
                                            <%
                                            }
                                            //}
                                            %>
                                        </li>
                                    </ul>
                                </div>

                                <ul class="gray6">
                                    <%if (T6340_F04.birthday.name().equals(hdType)) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>领取条件：</span>
                                        <%
                                            T6340_F09 lqtj = null;
                                            if (!StringHelper.isEmpty(request.getParameter("lqtj"))) {
                                                lqtj = EnumParser.parse(T6340_F09.class, request.getParameter("lqtj"));
                                            }
                                        %>
                                        <input type="radio" name="lqtj" value="login" checked="checked"/>生日当天登录
                                        <input type="radio" name="lqtj"
                                               value="invest" <%if (lqtj == T6340_F09.invest) { %>
                                               checked="checked" <%} %>/>生日当天投资
                                        <input type="radio" name="lqtj"
                                               value="all" <%if (lqtj == T6340_F09.all) { %>
                                               checked="checked" <%} %>/>不限
                                    </li>
                                    <%}%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl">备注：</span>
                                            <textarea name="remark" cols="45" rows="5"
                                                      class="w400 h120 border p5 max-length-100"><%StringHelper.filterHTML(out, StringHelper.isEmpty(request.getParameter("remark"))? request.getParameter("oldDataRemark"):request.getParameter("remark"));%></textarea>
                                        <span tip>最多输入100个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input type="submit"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                               fromname="form1" value="保存">
                                        <input type="button" value="返回" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
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
    <div class="popup_bg hide"></div>
</div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=warringMessage%>', 'wrong'));
</script>
<%}%>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=errorMessage%>', 'wrong'));
</script>
<%}%>
<div id="import" style="display: none">
    <div class="popup-box import-box" style="min-height: 150px;">
        <div class="popup-title-container">
            <h3 class="pl20 f18">导入用户</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="$('#import').hide();$('div.popup_bg').hide();"></a>
        </div>
        <form id="import_form" action="<%=controller.getURI(request, ImportUser.class)%>" name="form2" class="form2"
              method="post" enctype="multipart/form-data">
            <input type="hidden" name="jlType" value="<%=jlType%>"/>
            <input type="hidden" name="hdType" value="<%=hdType%>"/>
            <input type="hidden" name="oldData" value=""/>
            
            <input type="hidden" name="oldDataTitle" value=""/>
            <input type="hidden" name="oldDataJl" value=""/>
            <input type="hidden" name="oldDataSyqx" value=""/>
            <input type="hidden" name="oldDataSyqxType" value=""/>
            <input type="hidden" name="oldDataLeastInvest" value=""/>
            <input type="hidden" name="oldDataRemark" value=""/>
            <div class="popup-content-container pt20 ob20 clearfix">
                <div class="gray6">
                    <ul>
                        <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em>导入用户：</span>
                            <input class="text border required w150 pl5" type="file" name="file" value="导入用户">
                            <span errortip class="red"></span>
                        </li>
                        <li class="mb10"><span class="display-ib tr mr5" style="width:85px;">&nbsp;</span>支持导入csv、txt格式数据
                                    </li>
                    </ul>
                </div>
                <div class="tc f16">
                    <a href="javascript:void(0);" name="save" formname="form2" onclick="onSubmit();"
                       class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                    <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="$('#import').hide();$('div.popup_bg').hide();">取消</a>
                </div>
            </div>
        </form>
    </div>
</div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $(function () {
        //页面初始化的时候，生成一个用来克隆：id="clone_li_id"的li元素
        initCloneLi();

        $("#datepicker1").datepicker({
            inline: true,
            minDate: new Date(),
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
                var dates = $("#datepicker1");
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
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({
            inline: true,
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
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        if ($("input[name='sygz']:checked").val() == 0) {
            $(".rule").hide();
        } else {
            $(".rule").show();
        }
        $("input[name='sygz']").bind("change", function () {
            changeRule();
        });

        $("#close").click(function () {
            $("#import").hide();
            $("div.popup_bg").hide();
        });

        //增加规则事件
        $("#addAmountRange").click(function(){
            if($(".role_li").length>10){
                $("#info").html(showDialogInfo("最多10个规则","wrong"));
                return;
            }
            var clone_li = $('#clone_li_id').clone(true);
            clone_li.attr("id","clone_li_id_temp"); //改变ID
            clone_li.show();
            $("#div_li_id").append(clone_li);
        });
    });

    function showImportUser() {
        $("#import").show();
        $("div.import-box").show();
        $("div.popup_bg").show();
    }

    function changeRule() {
        if ($("input[name='sygz']:checked").val() == 0) {
            $(".rule").hide();
        } else {
            $(".rule").show();
        }
    }

    function onSubmit() {
        if ($("input[name='file']").val() == "") {
            $("input[name='file']").nextAll("span[errortip]").html("请选择导入的文件");
            return false;
        }
        //return true;
        $("input[name='oldData']").val($.trim($("textarea[name='userId']").val()));
        
        $("input[name='oldDataTitle']").val($.trim($("input[name='title']").val()));
        $("input[name='oldDataJl']").val($.trim($("input[name='jl']").val()));
        $("input[name='oldDataSyqx']").val($.trim($("input[name='syqx']").val()));
        $("input[name='oldDataSyqxType']").val($.trim($("select[name='syqxType']").val()));
        $("input[name='oldDataLeastInvest']").val($.trim($("input[name='leastInvest']").val()));
        $("input[name='oldDataRemark']").val($.trim($("textarea[name='remark']").val()));
        
        $("#import_form").submit();
    }

    //页面初始化的时候，生成一个用来克隆：id="clone_li_id"的li元素
    function initCloneLi(){
        var clone_li = $('.role_li').clone(true);
        clone_li.hide();
        clone_li.attr("id","clone_li_id");
        $(".popup_bg").append(clone_li);
        $("#clone_li_id #addAmountRange").removeClass("add-radiusbtn-icon")
        $("#clone_li_id #addAmountRange").addClass("remove-radiusbtn-icon");
        //绑定删除事件
        $("#clone_li_id #addAmountRange").bind("click",function(){
            $(this).parent().parent().remove();
        });
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