<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdgl"%>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.AddHdgl" %>
<%@page import="com.dimeng.p2p.variables.defines.SiteSwitchVariable" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03" %>
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
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, AddHdgl.class) %>" method="get" class="form1">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>活动管理-选择活动类型
                            </div>
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                    <%boolean isOpenHongJia = BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.REDPACKET_INTEREST_SWITCH)); %>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>奖励类型：</span>
                                            <select name="jlType" id="jlType" class="border mr20 h32 mw100 required">
                                                <option value="">请选择</option>
                                                <%for (T6340_F03 status : T6340_F03.values()) {
                                                	if(isOpenHongJia || status.name().equals(T6340_F03.experience.name())){                                                		
                                                %>                                               
                                                <option value="<%=status.name()%>"
                                                        <%if (status.name().equals(request.getParameter("jlType"))) {%>
                                                        selected="selected" <%}%>><%=status.getChineseName()%>
                                                </option>
                                                <%}	}%>
                                            </select>
                                            <span tip id="productTip" class="error_tip"></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                         
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动类型：</span>
                                            <select name="hdType" id="hdType" class="border mr20 h32 mw100 required">
                                                <option value="">请选择</option>
                                                <%for (T6340_F04 f04 : T6340_F04.values()) {
                                                if(!f04.name().equals(T6340_F04.integraldraw.name()) && !f04.name().equals(T6340_F04.exchange.name())){                                                                                                                                       
                                                %>
                                                <option value="<%=f04.name()%>"
                                                        <%if (f04.name().equals(request.getParameter("hdType"))) {%>selected="selected" <%}%>><%=f04.getChineseName()%>
                                                </option>
                                                <%}}%>
                                            </select>
                                            <span tip id="productTip" class="error_tip"></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <em class="red pr5">*</em>已有相同活动进行中，上架后以最新设置为准！
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                                   fromname="form1" value="下一步">
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchHdgl.class) %>'">
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
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
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
<%}%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>

<script type="text/javascript">
    function changeJlType(obj)
    {
        var value = $(obj).val();
        var hdType = $("select[name='hdType']");
        var optionHtml = "";
        hdType.html("");
        if("experience" == value)
        {
            optionHtml = '<option value="">请选择</option>';
            <%for (T6340_F04 status : T6340_F04.values()) {
                if("register".equals(status.name()) || "foruser".equals(status.name()))
                {%>
                    optionHtml = optionHtml + '<option value="<%=status.name()%>" selected="selected" ><%=status.getChineseName()%></option>';
                <%}
            }%>
        }
        else
        {
             optionHtml = '<option value="">请选择</option>';
            <%for (T6340_F04 status : T6340_F04.values()) {%>
                optionHtml = optionHtml + '<option value="<%=status.name()%>" selected="selected" ><%=status.getChineseName()%></option>';
            <%}%>
        }
        hdType.html(optionHtml);
        hdType.val("");
    }
</script>
</body>
</html>