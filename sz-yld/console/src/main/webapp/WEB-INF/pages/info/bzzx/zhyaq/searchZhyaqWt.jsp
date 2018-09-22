<%@page import="com.dimeng.p2p.console.servlets.info.gywm.gltd.SearchGltd"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.AddZhyaqWt" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWt" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionRecord" %>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.UpdateZhyaqWt" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWt" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.DeleteZhyaqWt" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.AddZhyaqWt" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage" %>
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
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    PagingResult<QuestionRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ArticleManage manage = serviceSession.getService(ArticleManage.class);
    QuestionTypeRecord[] questionTypes = manage.getQuestionTypes(T5011_F02.ZHYAQ);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchZhyaqWt.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>账户与安全问题
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">账户与安全问题：</span>
                                        <input type="text" name=question id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("question"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">发布者：</span>
                                        <input type="text" name="publisherName" id="textfield4"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">修改时间：</span>
                                        <input type="text" name=createTimeStart readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>"/>
                                        <span class="display-ib mr5">至</span>
                                        <input type="text" name="createTimeEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">发布状态：</span>
                                        <select name="publishStatus" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <option value="<%=NoticePublishStatus.WFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("publishStatus")) && request.getParameter("publishStatus").equals(NoticePublishStatus.WFB.name())){ %>selected="selected"<%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("publishStatus")) && request.getParameter("publishStatus").equals(NoticePublishStatus.YFB.name())){ %>selected="selected"<%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">问题类型：</span>
                                        <select name="questionTypeID" class="border mr20 h32 mw100">
                                            <option value="0">全部</option>
                                            <%
                                                if (questionTypes != null && questionTypes.length > 0) {
                                                    for (QuestionTypeRecord qtr : questionTypes) {
                                            %>
                                            <option value="<%=qtr.id%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("questionTypeID")) && Integer.parseInt(request.getParameter("questionTypeID")) == qtr.id){ %>selected="selected"<%} %>>
                                                <%StringHelper.filterHTML(out, qtr.questionType);%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddZhyaqWt.class)) {%>
                                        <a href="<%=controller.getURI(request, AddZhyaqWt.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteZhyaqWt.class)) {%>
                                        <a href="javascript:void(0)" id="delAll"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl10 pr10">批量删除</span>
                                        <%} %>
                                    </li>
                                    <li>
                                        <a href="<%=controller.getURI(request, SearchZhyaqWtlx.class)%>"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">返回</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc"><input type="checkbox" id="checkAll"/></th>
                                    <th class="tc">序号</th>
                                    <th>账户与安全问题</th>
                                    <th>问题类型</th>
                                    <th>发布者</th>
                                    <th>是否发布</th>
                                    <th>最后修改时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% QuestionRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (QuestionRecord record : records) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><input name="id" type="checkbox" value="<%=record.id %>"/></td>
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.question); %></td>
                                    <td><%StringHelper.filterHTML(out, record.questionType); %></td>
                                    <td><%StringHelper.filterHTML(out, record.publisherName); %></td>
                                    <td><%=record.publishStatus.getName()%>
                                    </td>
                                    <td><%=DateTimeParser.format(record.lastModifyTime) %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateZhyaqWt.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateZhyaqWt.class)%>?id=<%=record.id %>"
                                           class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteZhyaqWt.class)) {%>
                                        <a href="javascript:void(0)" onclick="delThis('<%=record.id %>')"
                                           class="link-orangered ">删除</a>
                                        <%} else { %>
                                        <span class="disabled">删除</span>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title">
                                    <td colspan="11" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <% SearchGltd.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
        </div>
    </div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteZhyaqWt.class) %>';
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>