<%@page import="com.dimeng.p2p.console.servlets.app.manage.ViewStartFindSet"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord"%>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppVerSearch" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AddAppVer" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.ViewAppVer" %>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.DeleteAppVer" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
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
    CURRENT_CATEGORY = "APPVER";
	CURRENT_SUB_CATEGORY = "APPQDFXY";
	AdvertisementRecord[] results = ObjectHelper.convertArray(request.getAttribute("result"), AdvertisementRecord.class);
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
          <div class="p20">
              <div class="border">
                  <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>启动发现页设置</div>
                  <div class="content-container p20">
                  		<div class="border table-container">
                             <table class="table table-style gray6 tl">
                                 <thead>
                                 <tr class="title tc">
                                     <th>序号</th>
                                     <th>名称</th>
                                     <th>图片</th>
                                     <th class="w200">操作</th>
                                 </tr>
                                 </thead>
                                 <tbody class="f12">
                                 <%
                                     int index = 1;
                                 %>
                                 <tr class="tc">
                                     <td><%=index++ %>
                                     </td>
                                     <td>IOS启动页图片</td>
                                     <td>
                                         <div class="p5" id="IosPic"></div>
                                     </td>
                                     <td>
                                         <%
                                             if (dimengSession.isAccessableResource(ViewStartFindSet.class)) {
                                         %>
	                                         <a class="link-blue click-link"
	                                            href="<%=controller.getURI(request,ViewStartFindSet.class)%>?advType=IOSPIC">修改</a>
                                         <%} else { %>
                                         	<span class="disabled">修改</span>	
                                         <%} %>
                                     </td>
                                 </tr>
                                 <tr class="tc">
                                     <td><%=index++ %>
                                     </td>
                                     <td>Android启动页图片</td>
                                     <td>
                                         <div class="p5" id="AndroidPic"></div>
                                     </td>
                                     <td>
                                         <%
                                             if (dimengSession.isAccessableResource(ViewStartFindSet.class)) {
                                         %>
	                                         <a class="link-blue click-link"
	                                            href="<%=controller.getURI(request,ViewStartFindSet.class)%>?advType=ANDROIDPIC">修改</a>
                                         <%} else { %>
                                         	<span class="disabled">修改</span>	
                                         <%} %>
                                     </td>
                                 </tr>
                                 <tr class="tc">
                                     <td><%=index++ %>
                                     </td>
                                     <td>发现页广告图片</td>
                                     <td>
                                         <div class="p5" id="FindPic"></div>
                                     </td>
                                     <td>
                                         <%
                                             if (dimengSession.isAccessableResource(ViewStartFindSet.class)) {
                                         %>
	                                         <a class="link-blue click-link"
	                                            href="<%=controller.getURI(request,ViewStartFindSet.class)%>?advType=FINDPIC">修改</a>
                                         <%} else { %>
                                         	<span class="disabled">修改</span>	
                                         <%} %>
                                     </td>
                                 </tr>
                            	</tbody>
                           </table>
                         </div>
                  </div>
              </div>
          </div>
      </div>
     </div>
</div>
<div id="info"></div>
<div class="popup_bg hide"></div>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
$(function () {
	var iosPic = $("#IosPic");
	var androidPic = $("#AndroidPic");
	var findPic = $("#FindPic");
	<%
	if(results.length == 0){
	%>
	iosPic.html("");
	androidPic.html("");
	findPic.html("");
	<%}else{
	for(AdvertisementRecord result : results){
	    if (result == null) {
            continue;
        }
	    if(result.advType.equals("IOSPIC")){%>
		iosPic.html("<img src=\"<%=fileStore.getURL(result.imageCode)%>\" width=\"158\" height=\"51\"/>");
		<%}if(result.advType.equals("ANDROIDPIC")){%>
		androidPic.html("<img src=\"<%=fileStore.getURL(result.imageCode)%>\" width=\"158\" height=\"51\"/>");
		<%}if(result.advType.equals("FINDPIC")){%>
		findPic.html("<img src=\"<%=fileStore.getURL(result.imageCode)%>\" width=\"158\" height=\"51\"/>");
	<%}}}%>
});
</script>
</body>
</html>