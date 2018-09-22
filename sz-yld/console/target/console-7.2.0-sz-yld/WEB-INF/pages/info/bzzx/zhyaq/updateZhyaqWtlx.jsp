<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWtlx" %>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.UpdateZhyaqWtlx" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "ZHYAQ";
    QuestionTypeRecord record = ObjectHelper.convert(request.getAttribute("record"), QuestionTypeRecord.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, UpdateZhyaqWtlx.class)%>"
                          enctype="multipart/form-data" class="form1">
                        <input type="hidden" value="<%=record.id %>" name="id"/>

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改账户与安全问题类型
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>设置问题类型</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name=questionType
                                               value="<%StringHelper.filterHTML(out, record.questionType);%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>是否发布</span>
                                        <select name="publishStatus" class="border mr20 h32 ">
                                            <%String publishStatus = record.publishStatus.name(); %>
                                            <option value="<%=NoticePublishStatus.WFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.WFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.YFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>相片</span>
                                        <%if (record.imageCode != null) { %><span class="mb10"><img
                                                src="<%=fileStore.getURL(record.imageCode) %>" width="277" height="89"/></span><%} %>

                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input type="file" id="file" class="text border w300 yw_w4" name="image"/>
                                        <span id="errorImage">&nbsp;</span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <%-- <a href="<%=controller.getURI(request, SearchZhyaqWtlx.class) %>"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml20">取消</a> --%>
                                            <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="javascript:window.location.href='<%=controller.getURI(request, SearchZhyaqWtlx.class) %>';"/>
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
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
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>