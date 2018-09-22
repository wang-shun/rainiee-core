<%@page import="com.dimeng.p2p.console.servlets.info.gywm.gltd.SearchGltd"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.UpdateZhyaqWtlx" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWtlx" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWt" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.DeleteZhyaqWtlx" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.AddZhyaqWtlx" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.info.UpdateCategoryName" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5010" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5010_F04" %>
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
    PagingResult<QuestionTypeRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ArticleManage manage = serviceSession.getService(ArticleManage.class);
    T5010 category = manage.getArticleTypeByCode("ZHYAQ");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchZhyaqWtlx.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i><%StringHelper.filterHTML(out, category.F03);%>问题类型
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">问题类型：</span>
                                        <input type="text" name=questionType id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("questionType"));%>"/></span>
                                    </li>
                                    <li><span class="display-ib mr5">发布者：</span>
                                        <input type="text" name="publisherName" id="textfield4"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/></span>
                                    </li>
                                    <li><span class="display-ib mr5">修改时间：</span>
                                        <input type="text" name=createTimeStart readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>"/></span>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="createTimeEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>"/></span>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddZhyaqWtlx.class)) {%>
                                        <a href="<%=controller.getURI(request, AddZhyaqWtlx.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增问题类型</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增问题类型</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(SearchZhyaqWt.class)) {%>
                                        <a href="<%=controller.getURI(request, SearchZhyaqWt.class)%>"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">问题列表管理</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10 tc">问题列表管理</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteZhyaqWtlx.class)) {%>
                                        <a href="javascript:void(0)" id="delAll"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10" class="link-blue mr10">批量删除</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)"
                                           class="btn btn-gray radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(SearchZhyaqWtlx.class)) {%>
                                        <a href="javascript:void(0)" onclick="openUpdateTitle();"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">设置</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl10 pr10">设置</span>
                                        <%} %>
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
                                    <th>问题类型</th>
                                    <th>发布者</th>
                                    <th>是否发布</th>
                                    <th>最后修改时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% QuestionTypeRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (QuestionTypeRecord record : records) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><input name="id" type="checkbox" value="<%=record.id %>"/></td>
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.questionType); %></td>
                                    <td><%StringHelper.filterHTML(out, record.publisherName); %></td>
                                    <td><%=record.publishStatus.getName()%>
                                    </td>
                                    <td><%=DateTimeParser.format(record.createtime) %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateZhyaqWtlx.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateZhyaqWtlx.class)%>?id=<%=record.id %>"
                                           class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <a class="disabled">修改</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteZhyaqWtlx.class)) {%>
                                        <a href="javascript:void(0)" onclick="delThis('<%=record.id %>')"
                                           class="link-orangered ">删除</a>
                                        <%} else { %>
                                        <a class="disabled">删除</a>
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
        <div class="popup_bg hide"></div>
    </div>
<div id="szmc" style="width:500px;" class="hide">
    <div class="popup_bg sz"></div>
    <div class="popup-box sz">
        <div class="popup-title-container">
            <h3 class="pl20 f18">设置名称</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container-2" style="max-height:550px;">
            <div class="p30">
                <ul class="gray6">
                    <li class="mb10">
					<span class="display-ib w100 tr mr5 fl">
						<span class="red">*</span>标题：
					</span>
                        <input id="articleName" type="text" onblur="checkTitleName();"
                               class="text border w150 pl5 required max-length-8"
                               name="articleName"
                               value="<%StringHelper.filterHTML(out, category.F03);%>"/>
                        <span id="tip" tip>不超过8个字</span>
                        <span id="errortip" errortip class="" style="display: none"></span>
                    </li>
                    <li class="mb10">
								<span class="display-ib w100 tr mr5 fl">
									<span class="red">*</span>状态：
								</span>

                        <select id="articleStatus" name="articleStatus"  class="border mr20 h32 required w150">
                            <%
                                if (T5010_F04.values() != null && T5010_F04.values().length > 0) {
                                    for (T5010_F04 t5010_F04 : T5010_F04.values()) {
                            %>
                            <option value="<%=t5010_F04.name()%>"
                                    <%if(t5010_F04 ==category.F04) {%>selected="selected"<%} %>><%=t5010_F04.getChineseName() %>
                            </option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </li>

                </ul>
                <input type="hidden" id="articleNameHidden" value="<%StringHelper.filterHTML(out, category.F03);%>" />
                <input type="hidden" id="articleStatusHidden" value="<%StringHelper.filterHTML(out, category.F04.name());%>" />
                <div class="tc f16 pt20">
                    <input type="button" onclick="updateNameLol();" value="确定" class="btn-blue2 btn white radius-6 pl20 pr20 "/>
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
                </div>
            </div>
        </div>
    </div>
</div>
<div id="info"></div>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteZhyaqWtlx.class) %>';
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    var upateUrl = '<%=controller.getURI(request, UpdateCategoryName.class)%>';
    var reloadUrl = '<%=controller.getURI(request, SearchZhyaqWtlx.class)%>';
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

    function updateNameLol(){
        updateName('ZHYAQ')
        //保存成功，修改左菜单标题
        parent.frames["leftFrame"].window.showBZZXMenu("ZHYAQ");
    }
</script>
</body>
</html>