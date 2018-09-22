<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.ExportSms" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.SmsList" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.Sms" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    Sms sms = ObjectHelper.convert(request.getAttribute("sms"), Sms.class);
    if (sms == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "DXTG";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容 开始-->
                <div class="p20">
                    <!--切换栏目-->
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>短信推广</div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>管理员：</span>
                                        <div class="pl200"><%StringHelper.filterHTML(out, sms.name);%></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>创建时间：</span>
                                        <div class="pl200"><%=DateTimeParser.format(sms.createTime)%>
                                        </div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>发送数量：</span>
                                        <div class="pl200"><%=sms.count%>
                                        </div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>手机号码：</span>
                                        <div class="pl200 clearfix"><textarea name="mobile" id="textarea" cols="45"
                                                                              rows="5" readonly="readonly"
                                                                              class="ww50 h120 border p5 fl"><%
                                            if (sms.mobiles != null) {
                                                for (String s : sms.mobiles) {
                                        %><%=s + "\n"%><%
                                                }
                                            }
                                        %></textarea>
                                            <a href="<%=controller.getURI(request, ExportSms.class)%>?id=<%=sms.id%>"
                                               class="btn-blue2 btn white radius-6 pl20 pr20 ml20 fl">导出</a></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>短信内容：</span>

                                        <div class="pl200 clearfix"><textarea name="content" cols="45" rows="7"
                                                                              readonly="readonly"
                                                                              style="width: 80%; height: 200px;"
                                                                              class="border p5 fl"><%StringHelper.filterHTML(out, sms.content);%></textarea>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200"><input type="button" value="返回"
                                                                  class="btn btn-blue2 radius-6 pl20 pr20"
                                                                  onclick="window.location.href='<%=controller.getURI(request, SmsList.class)%>'">
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
</body>
</html>