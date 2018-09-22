<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.info.UpdateOrder"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.AddGggl"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.DeleteGggl" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.SearchGggl" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.UpdateGggl" %>
<%@ page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord" %>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "GGGL";
    PagingResult<AdvertisementRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, SearchGggl.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>广告管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                       	<span class="display-ib mr5">广告类型</span>
                                       	<select name="advType" id="advType" class="border mr20 h32 mw120">
                                      		<option value="">全部</option>
                                              <%
                                                  if (T5016_F12.values() != null && T5016_F12.values().length > 0) {
                                                      for (T5016_F12 t5016_F12 : T5016_F12.values()) {
                                              %>
                                              <option value="<%=t5016_F12.name()%>" <%if (t5016_F12.name().equals(request.getParameter("advType"))) {%> selected="selected" <%}%>><%=t5016_F12.getChineseName() %></option>
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
                                        <%if (dimengSession.isAccessableResource(AddGggl.class)) {%>
                                        <a href="<%=controller.getURI(request, AddGggl.class) %>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
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
                                <tr class="title">
                                	<th class="tc"><input type="checkbox" id="checkAll"/></th>
                                    <th class="tc">序号</th>
                                    <th class="tc">广告图片标题</th>
                                    <th class="tc">广告类型</th>
                                    <th class="tc">广告图片</th>
                                    <th class="tc">图片链接</th>
                                    <th class="tc">状态</th>
                                    <th class="tc">发布者</th>
                                    <th class="tc">排序值</th>
                                    <th class="tc">上架时间/下架时间</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    AdvertisementRecord[] items = result.getItems();
                                    if (items != null && items.length > 0) {
                                        int index = 1;
                                        for (AdvertisementRecord item : items) {

                                %>
                                <tr class="tc">
                                	<td class="tc"><input name="id" type="checkbox" value="<%=item.id %>"/></td>
                                    <td><%=index++%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, item.title);%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(item.title, 15));%></td>
                                    <td><%=item.advType%>
                                    </td>
                                    <td><img src="<%=fileStore.getURL(item.imageCode)%>" width="158" height="51"/></td>
                                    <td><%if (!StringHelper.isEmpty(item.url)) {%><a href="<%=item.url %>"
                                                                                     target="_blank"><%
                                        StringHelper.filterHTML(out, item.url);%></a><%}%></td>
                                    <td><%=item.status.getName()%>
                                    </td>
                                    <td><%=item.publisherName%>
                                    </td>
                                    <td><%=item.sortIndex%>
                                    </td>
                                    <td><%=DateTimeParser.format(item.showTime) %>
                                        <br/><%=DateTimeParser.format(item.unshowTime) %>
                                    </td>
                                    <td>
					  <span>
					  <%if (dimengSession.isAccessableResource(UpdateGggl.class)) {%>
						<a href="<%=controller.getURI(request,UpdateGggl.class)%>?id=<%=item.id %>"
                           class="link-blue mr20">修改</a>
					  <%} else { %>
						<span class="disabled">修改</span>
					  <%} %>
					  <%if (dimengSession.isAccessableResource(DeleteGggl.class)) {%> 
						<a href="javascript:void(0)" onclick="delThis('<%=item.id %>')" class="link-orangered">删除</a>
					  <%} else { %>
						<span class="disabled">删除</span>
					  <%} %>
					  </span>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="11" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <!--分页-->
                        <%SearchGggl.rendPagingResult(out, result);%>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<div class="popup_bg hide"></div>
<div id="info"></div>
<input id="orderTable" type="hidden" name="orderTable" value="T5016"/>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteGggl.class) %>';
    var reloadUrl = '<%=controller.getURI(request, SearchGggl.class)%>';
    var upateOrderUrl = '<%=controller.getURI(request, UpdateOrder.class)%>';

    function onSubmit() {
        $("#form_loan").submit();
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
</body>
</html>