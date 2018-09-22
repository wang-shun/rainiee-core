<%@page import="com.dimeng.p2p.S50.entities.T5016"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>

<%

	AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
	T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.PC.name());
	if (advertisements != null && advertisements.length > 0) {
%>
<!--banner-->
<div class="banner clearfix">  
        <ul class="rslides">
            <%for(T5016 advertisement : advertisements){if (advertisement == null) {continue;}%>
            <li style="background:url(<%=fileStore.getURL(advertisement.F05)%>) center 0 no-repeat;">
                <%if(!StringHelper.isEmpty(advertisement.F04)){%>
                <a target="_blank" href="http://<%StringHelper.filterHTML(out, advertisement.F04.replaceAll("http://", "").replaceAll("https://", ""));%>"></a>
                <%}%>
            </li>
            <%}%>
        </ul>
</div>
<!--banner end-->
<%}%>