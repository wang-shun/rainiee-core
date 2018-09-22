<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.SearchSpsc"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.IndexSpsc" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.DeleteSpsc" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.UpdateSpsc" %>
<%@page import="com.dimeng.p2p.S50.enums.T5021_F07" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.AddSpsc" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.*" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "SPSC";
    PagingResult<AdvertSpscRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchSpsc.class) %>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>视频管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddSpsc.class)) {%>
                                        <a href="<%=controller.getURI(request, AddSpsc.class) %>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title" align="center">
                                    <th class="tc">序号</th>
                                    <th>标题</th>
                                    <th>格式</th>
                                    <th>大小</th>
                                    <th>最后操作人</th>
                                    <th>状态</th>
                                    <th>是否自动播放</th>
                                    <th>上传时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    AdvertSpscRecord[] items = result.getItems();
                                    if (items != null) {
                                        int index = 1;
                                        for (AdvertSpscRecord item : items) {
                                            String url = fileStore.getURL(item.fileName);
                                %>
                                <tr class="title" align="center">
                                    <td class="tc"><%=index++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, StringHelper.truncation(item.title, 15));%></td>
                                    <td><%StringHelper.filterHTML(out, item.fileFormat);%></td>
                                    <td><%StringHelper.filterHTML(out, item.fileSize);%></td>
                                    <td><%StringHelper.filterHTML(out, item.publisherName);%></td>
                                    <td><%StringHelper.filterHTML(out, item.status.getChineseName());%></td>
                                    <td><%=item.isAuto == 2 ? "是" : "否"%>
                                    </td>
                                    <td><%=DateTimeParser.format(item.showTime) %>
                                    </td>
                                    <td>
										<span class="blue">
										<%if (dimengSession.isAccessableResource(IndexSpsc.class) && item.sortIndex != 1) {%>
											<a href="<%=controller.getURI(request,IndexSpsc.class)%>?id=<%=item.id %>"
                                               class="link-blue mr10">置顶</a>
										<%} else { %>
											<a class="disabled mr10">置顶</a>
										<%} %>
										<%if (dimengSession.isAccessableResource(UpdateSpsc.class)) {%>
											<a href="<%=controller.getURI(request,UpdateSpsc.class)%>?id=<%=item.id %>"
                                               class="link-blue mr10">修改</a>
										<%} else { %>
											<a class="disabled mr10">修改</a>
										<%} %>
										<%if (dimengSession.isAccessableResource(DeleteSpsc.class)) {%> 
											<a href="javascript:void(0)" onclick="delThis('<%=item.id %>&url=<%=url %>');" class="link-orangered ">删除</a>
										<%} else { %>
											<a class="disabled mr10">删除</a>
										<%} %>
										</span>
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
                        <%SearchSpsc.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div id="info"></div>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteSpsc.class) %>';
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
</body>
</html>