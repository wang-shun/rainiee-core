<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.wdhyzx.SearchWdhyzx"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.console.servlets.info.UpdateOrder"%>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.console.servlets.info.UpdateCategoryName"%>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.wdhyzx.AddWdhyzx" %>
<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.wdhyzx.UpdateWdhyzx" %>
<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.wdhyzx.DeleteWdhyzx" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord" %>
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
    CURRENT_SUB_CATEGORY = "WDHYZX";
    String lastUpdateTimeStart = request.getParameter("lastUpdateTimeStart");
    String lastUpdateTimeEnd = request.getParameter("lastUpdateTimeEnd");
    PagingResult<ArticleRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ArticleManage manage = serviceSession.getService(ArticleManage.class);
    T5010 category = manage.getArticleTypeByCode("WDHYZX");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchWdhyzx.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i><%StringHelper.filterHTML(out, category.F03);%>
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">标题：</span>
                                        <input type="text" name="title" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("title"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">发布者：</span>
                                        <input type="text" name="publisherName" id="textfield4"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">修改时间：</span>
                                        <input type="text" name="lastUpdateTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("lastUpdateTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="lastUpdateTimeEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("lastUpdateTimeEnd"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">发布状态：</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <option value="<%=ArticlePublishStatus.WFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("status")) && request.getParameter("status").equals(ArticlePublishStatus.WFB.name())){ %>selected="selected"<%} %>><%=ArticlePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=ArticlePublishStatus.YFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("status")) && request.getParameter("status").equals(ArticlePublishStatus.YFB.name())){ %>selected="selected"<%} %>><%=ArticlePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddWdhyzx.class)) {%>
                                        <a href="<%=controller.getURI(request, AddWdhyzx.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteWdhyzx.class)) {%>
                                        <a href="javascript:void(0)" id="delAll"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl10 pr10">批量删除</span>
                                        <%} %>
                                    </li>
                                    <li>
                                     <%if (dimengSession.isAccessableResource(SearchWdhyzx.class)) {%>
                                        <a href="javascript:void(0)" onclick="openUpdateTitle();"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">设置</a>
                                      <%} else { %>
                                       <span class="btn btn-gray radius-6 mr5 pl10 pr10">设置</span>
                                       <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(UpdateOrder.class)) {%>
                                       	<a href="javascript:void(0)" id="updateOrder" class="btn btn-blue2 radius-6 mr5 pl10 pr10">修改排序值</a>
                                        <%} else { %>
                                      		<span class="btn btn-gray radius-6 mr5 pl10 pr10">修改排序值</span>
                                      <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title" align="center">
                                    <th class="tc"><input type="checkbox" id="checkAll"/></th>
                                    <th class="tc">序号</th>
                                    <th>文章标题</th>
                                    <th>浏览次数</th>
                                    <th>来源</th>
                                    <th>发布者</th>
                                    <th>是否发布</th>
                                    <th>最后修改时间</th>
                                    <th>显示时间</th>
                                    <th>排序值</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% ArticleRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (ArticleRecord record : records) {
                                %>
                                <tr class="titile" align="center">
                                    <td class="tc"><input name="id" type="checkbox" value="<%=record.id %>"/></td>
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, record.title);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(record.title, 15)); %></td>
                                    <td><%=record.viewTimes %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, record.source); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(record.source, 15)); %></td>
                                    <td><%StringHelper.filterHTML(out, record.publisherName); %></td>
                                    <td><%StringHelper.filterHTML(out, record.publishStatus.getName()); %></td>
                                    <td><%=DateTimeParser.format(record.updateTime) %>
                                    </td>
                                    <td><%=DateParser.format(record.publishTime) %>
                                    </td>
                                    <td><%=record.sortIndex %>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateWdhyzx.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateWdhyzx.class)%>?id=<%=record.id %>"
                                           class="libk-deepblue mr20">修改</a>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteWdhyzx.class)) {%>
                                        <a href="javascript:void(0)" onclick="delThis('<%=record.id %>')"
                                           class="link-orangered">删除</a>
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
                        <% SearchWdhyzx.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
            <div class="popup_bg hide"></div>
        </div>
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
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
                </div>
            </div>
    </div>
</div>
</div>
<div id="info"></div>
<input id="orderTable" type="hidden" name="orderTable" value="T5011"/>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteWdhyzx.class) %>';
    var upateUrl = '<%=controller.getURI(request, UpdateCategoryName.class)%>';
    var reloadUrl = '<%=controller.getURI(request, SearchWdhyzx.class)%>';
    var upateOrderUrl = '<%=controller.getURI(request, UpdateOrder.class)%>';
</script>
<script type="text/javascript">
    function updateNameLol(){
        updateName('WDHYZX')
        //保存成功，修改左菜单标题
        parent.frames["leftFrame"].window.showZXDTMenu("WDHYZX");
    }
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
        <%if(!StringHelper.isEmpty(lastUpdateTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("lastUpdateTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(lastUpdateTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("lastUpdateTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>