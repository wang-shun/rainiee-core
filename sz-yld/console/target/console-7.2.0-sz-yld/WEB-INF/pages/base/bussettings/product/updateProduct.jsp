<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.S62.enums.T6216_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6216_F18" %>
<%@page import="com.dimeng.p2p.common.HighPrecisionFormater" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.SearchProduct" %>
<%@page
        import="com.dimeng.p2p.console.servlets.base.bussettings.product.UpdateProduct" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.dimeng.util.DateHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "CPSZ";
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    T6216 model = ObjectHelper.convert(request.getAttribute("model"), T6216.class);
    boolean is_invest_limit = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改标产品名称
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <div class="clear"></div>
                            <form
                                    action="<%=controller.getURI(request, UpdateProduct.class) %>"
                                    method="post" class="form1" onsubmit="return onSubmit();">
                                <input type="hidden" name="F01" value="<%=model.F01%>">
                                <ul class="cell yw_jcxx">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>产品名称：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser border pl5 required max-length-50" name="F02"
                                               value="<%StringHelper.filterHTML(out,model.F02); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <div class="pr pl200">
											<span class="display-ib pa left0 w200 tr mr5">
												<em class="red pr5">*</em>标的类型：
											</span>
                                            <div class="pl10">
                                        <%String[] types = model.F03.split(","); %>
                                        <%
                                            if (t6211s != null) {
                                                int index = 0;
                                                for (T6211 t6211 : t6211s) {
                                                    if (t6211 == null) {
                                                        continue;
                                                    }
                                                    if (index == 7) {

                                        %><input type="checkbox" class="mr5" name="F03" value="<%=t6211.F01%>"
                                        <%for(int j=0;j<types.length;j++){
									 if(types[j].equals(String.valueOf(t6211.F01))){ %>
                                                 checked="checked" <%}} %>><%StringHelper.filterHTML(out, t6211.F02); %>
                                        &nbsp;&nbsp;&nbsp;&nbsp;<br/>
                                        <%} else { %>
                                        <input type="checkbox" class="mr5" name="F03" value="<%=t6211.F01%>"
                                            <%for(int j=0;j<types.length;j++){
									 if(types[j].equals(String.valueOf(t6211.F01))){ %>
                                               checked="checked" <%}} %>><%StringHelper.filterHTML(out, t6211.F02); %>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <%
                                                    }
                                                    index++;
                                                }
                                            }else{
                                        %>
                                        &nbsp;
                                        <%}%>
                                        <span tip id="bidTypeTip" class="red"></span>
                                        <span errortip class="" style="display: none"></span>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>借款金额：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser2 border required max-length-20 min-size-1" name="F05"
                                               id="minAmount"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F05)%>"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                               mtestmsg="必须为整数"/> -&nbsp; <input type="text"
                                                                                 class="text yhgl_ser2 border required max-length-20 min-size-1"
                                                                                 name="F06"
                                                                                 id="maxAmount"
                                                                                 value="<%=HighPrecisionFormater.toPlainString(model.F06)%>"
                                                                                 onKeyUp="value=value.replace(/\D/g,'')"
                                                                                 mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                                                                 mtestmsg="必须为整数"/> 元
                                        <span tip ></span>
                                        <span errortip  id="amountTip" class="red" style="display: none"></span>
                                    </li>
                                    
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>还款方式：
											</span>


                                        <%
                                            for (T6216_F11 way : T6216_F11.values()) {
                                        %>

                                        <input type="checkbox" class="mr5" <%if(way == T6216_F11.YCFQ){ %>onclick="changeQx(this)" <%} %> name="F11" value="<%=way.name()%>"
                                                <%if (model.F11 != null && model.F11.indexOf(way.name()) > -1) {%>
                                               checked="checked" <%}%> /><%
                                        StringHelper.filterHTML(out, way.getChineseName());%>&nbsp;&nbsp;&nbsp;&nbsp;
                                        <%
                                            }
                                        %>

                                        <span tip id="wayTip" class="red"></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>借款期限：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser2 border required max-length-20 min-size-1"
                                               mtest="/^\d+$/" mtestmsg="必须为整数" name="F07" id="minQx"
                                               value="<%=model.F07%>"/> -&nbsp; <input type="text"
                                                                                       class="text yhgl_ser2 border required max-length-20 min-size-1 max-size-36"
                                                                                       mtest="/^\d+$/" mtestmsg="必须为整数"
                                                                                       name="F08" id="maxQx"
                                                                                       value="<%=model.F08%>"/> 月
                                        <span tip></span>
                                        <span errortip id="qxTip" class="red" style="display: none"></span>
                                    </li>
                                    <%Calendar cal = Calendar.getInstance();
                                        cal.setTime(new Date());
                                        cal.set(Calendar.HOUR_OF_DAY, 0);
                                        cal.set(Calendar.MINUTE, 0);
                                        cal.set(Calendar.SECOND, 0);
                                        long curDateInMills = cal.getTimeInMillis();
                                        cal.add(Calendar.MONTH, 36);
                                        long upDateInMills = cal.getTimeInMillis();
                                        int upDays = (int)((upDateInMills - curDateInMills) / DateHelper.DAY_IN_MILLISECONDS);%>
                                    <li class="mb10" id="xqDay" <%if(model.F11.indexOf(T6216_F11.YCFQ.name()) == -1) {%>style="display: none;"<%} %>>
											<span class="display-ib w200 tr mr5">
												&nbsp;
											</span>
                                        <input type="text"
                                               class="text yhgl_ser2 border required max-length-20 min-size-1"
                                               mtest="/^\d+$/" mtestmsg="必须为整数" name="F16" id="minQxDay"
                                               value="<%=model.F16%>"/>
                                        -&nbsp; <input type="text"
                                                       class="text yhgl_ser2 border required max-length-20 min-size-1 max-size-<%=upDays%>"
                                                       mtest="/^\d+$/" mtestmsg="必须为整数" name="F17" id="maxQxDay"
                                                       value="<%=model.F17%>"/>
                                        		天	
                                        <span tip >(针对还款方式为本息到期一次付清按天还款)</span>
                                        <span errortip id="qxDayTip" class="red" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>年化利率：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser2 border required max-length-20" name="F09"
                                               id="minNhl"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F09)%>"
                                               mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="只能有两位小数"/>
                                        -&nbsp; <input type="text"
                                                       class="text yhgl_ser2 border required max-length-20" name="F10"
                                                       id="maxNhl"
                                                       value="<%=HighPrecisionFormater.toPlainString(model.F10)%>"
                                                       mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="只能有两位小数"/> %
                                        <span tip ></span>
                                        <span errortip id="nhlTip" class="red" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>成交服务费率：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser border required max-nesize-1 minf-size-0 max-precision-9"
                                               name="F12"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F12)%>"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数"/>
                                        借款人给平台的管理费
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>投资管理费率：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser border required max-nesize-1 minf-size-0 max-precision-9"
                                               name="F13"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F13)%>"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数"/>
                                        投资人给平台的投资管理费
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>逾期罚息利率：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser border required max-nesize-1 minf-size-0 max-precision-9 required"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数"
                                               name="F14"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F14)%>"/>
                                        罚息=逾期本息*逾期罚息利率*天数
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<em class="red pr5">*</em>起投金额：
											</span>
                                        <input type="text"
                                               class="text yhgl_ser border required max-length-20 min-size-1"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                               mtestmsg="必须为整数" name="F15"
                                               value="<%=HighPrecisionFormater.toPlainString(model.F15)%>"/>元
                                        <span tip id="qtTip" class="red"></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%if(is_invest_limit){ %>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">
											<em class="red pr5">*</em>投资限制：
										</span>
										<select class="border mr20 h32 mw160" name="F18">
										    <%for (T6216_F18 investLimit : T6216_F18.values()) {%>
                                            <option value="<%=investLimit.name()%>" <%if(investLimit==model.F18){ %>selected="selected"<%} %>><%=investLimit.getChineseName()%></option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <%}%>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="提交"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchProduct.class) %>'"
                                                   value="取消">
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<div class="popup_bg hide"></div>
<div id="info"></div>
<!--内容-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
     $(".popup_bg").show();
     $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
</script>
<%} %>
<script type="text/javascript">
    function onSubmit() {
     
        $("#bidTypeTip").html("");
        $("#amountTip").html("");
        $("#qxTip").html("");
        $("#nhlTip").html("");
        $("#wayTip").html("");
        
        var bidTypeTip = $("input[name=F03]:checked").length;
        if (bidTypeTip == 0) {
            $("#bidTypeTip").html("请至少选择一项标的类型");
            return false;
        }

        var minAmount = $("#minAmount").val() * 1;
        var maxAmount = $("#maxAmount").val() * 1;
        if (minAmount > maxAmount) {
            $("#amountTip").html("最小借款金额不能大于最大借款金额").show();
            return false;

        }

        var minQx = $("#minQx").val() * 1;
        var maxQx = $("#maxQx").val() * 1;
        if (minQx > maxQx) {
            $("#qxTip").html("最小借款期限不能大于最大借款期限").show();
            return false;
        }
        var minQxDay = $("#minQxDay").val() * 1;
        var maxQxDay = $("#maxQxDay").val() * 1;
        var waychecked = $("input[name=F11]:checked").length;
        if (waychecked == 0) {
            $("#wayTip").html("请至少选择一项还款方式");
            return false;
        }

        if ($("#xqDay").css("display")!="none" && minQxDay > maxQxDay) {
            $("#qxDayTip").html("最小借款期限不能大于最大借款期限").show();
            bl = false;
        }else{
        	$("#qxDayTip").hide();
        }
        
        var minNhl = $("#minNhl").val() * 1;
        var maxNhl = $("#maxNhl").val() * 1;
        if (minNhl > maxNhl) {
            $("#nhlTip").html("最小年化利率不能大于最大年化利率").show();
            return false;
        }
        
        var qtAmount = $("input[name='F15']").val() * 1;
        if (qtAmount > minAmount) {
            $("#qtTip").html("起投金额不能大于最小借款金额");
            return false;
        }
       
        return true;

    }
    
    function changeQx(obj){
    	if($(obj).attr("checked")){
    		$("#xqDay").show();
    	}else{
    		$("#xqDay").hide();
    	}
    }
</script>
</body>
</html>