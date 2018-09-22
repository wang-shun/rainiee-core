<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.entities.T6149" %>
<%@page import="com.dimeng.p2p.S61.enums.T6149_F07" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.RiskQuesList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.UpdateRiskQues" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "FXPGWTSZ";
    T6149 t6149 = ObjectHelper.convert(request.getAttribute("t6149"), T6149.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改风险评估问题
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, UpdateRiskQues.class)%>" method="post"
                                  class="form1">
                                <%=FormToken.hidden(serviceSession.getSession()) %>
                                <input type="hidden" name="F01" value="<%=t6149.F01%>"/>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>问题描述：</span>
                                        <textarea name="F02" cols="" rows=""
                                                  class="w400 h120 border required p5  max-length-150"><%StringHelper.format(out, t6149.F02, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>选项A：</span>
                                        <textarea name="F03" cols="" rows=""
                                                  class="w400 h120 border required p5  max-length-150"><%StringHelper.format(out, t6149.F03, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>选项B：</span>
                                        <textarea name="F04" cols="" rows=""
                                                  class="w400 h120 border required p5  max-length-150"><%StringHelper.format(out, t6149.F04, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>选项C：</span>
                                        <textarea name="F05" cols="" rows=""
                                                  class="w400 h120 border required p5  max-length-150"><%StringHelper.format(out, t6149.F05, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>选项D：</span>
                                        <textarea name="F06" cols="" rows=""
                                                  class="w400 h120 border required p5  max-length-150"><%StringHelper.format(out, t6149.F06, fileStore); %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>状态：</span>
                                        <select name="F07" class="border mr20 h32 mw100">
                                            <%
                                                for (T6149_F07 status : T6149_F07.values()) {
                                            %>
                                            <option value="<%=status.name()%>"
                                                    <%if (status.name().equals(t6149.F07.name())) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>排序值：</span>
                                        <input name="F08" maxlength="5" type="text"
                                               class="text border w300 yw_w5 required isint"
                                               value="<%=t6149.F08%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="til">&nbsp;</div>
                                        <div class="info">
	                      	<span errortip class=""></span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   value="提交" fromname="form1"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, RiskQuesList.class)%>'"
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
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String msg = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (msg != null) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=msg%>", "wrong"));
    $("div.popup_bg").show();

</script>
<%
    }
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>

</html>