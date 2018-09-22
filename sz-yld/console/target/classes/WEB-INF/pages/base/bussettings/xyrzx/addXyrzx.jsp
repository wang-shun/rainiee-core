<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S51.enums.T5123_F06"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.XyrzxList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.AddXyrzx" %>
<%@page import="com.dimeng.p2p.S51.enums.T5123_F03" %>
<%@page import="com.dimeng.p2p.S51.entities.T5123" %>
<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css"
      rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "XYRZX";

    T5123 entity = (T5123) request.getAttribute("entity");
    if (entity == null) {
        entity = new T5123();
        entity.F03 = EnumParser.parse(T5123_F03.class, "S");
        entity.F06 = EnumParser.parse(T5123_F06.class, "GR");
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>新增信用认证项
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, AddXyrzx.class)%>"
                                  method="post" class="form1">
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>类型名称：
											</span>
                                        <input name="F02" maxlength="20" type="text"
                                               class="text border w300 yw_w5 required"
                                               value="<%=StringHelper.isEmpty(entity.F02) ? "" : entity.F02 %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>必要认证：
											</span>
											<span class="tir required">
												<select name="F03"
                                                        class="yhgl_sel border">
                                                    <%
                                                        for (T5123_F03 t5123_F03 : T5123_F03.values()) {
                                                    %>
                                                    <option value="<%=t5123_F03.name()%>"
                                                            <%if (t5123_F03.name() == entity.F03.name()) {%>
                                                            selected="selected" <%}%>><%=t5123_F03.getChineseName()%>
                                                    </option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
												</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户类型：
											</span>
											<span class="tir required">
												<select name="F06"
                                                        class="yhgl_sel border">
                                                    <%
                                                        for (T5123_F06 t5123_F06 : T5123_F06.values()) {
                                                    %>
                                                    <option value="<%=t5123_F06.name()%>"
                                                            <%if (t5123_F06.name() == entity.F06.name()) {%>
                                                            selected="selected" <%}%>><%=t5123_F06.getChineseName()%>
                                                    </option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
												</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%-- <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>最高分数：
											</span>
                                        <input name="F05" type="text"
                                               class="text border w300 yw_w5 required isint max-size-100 max-length-3"
                                               value="<%=entity.F05==0 ? "" : entity.F05%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li> --%>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, XyrzxList.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
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
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
$("#info").html(showDialogInfo("<%=message%>", "wrong"));
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

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
$("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
$("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>