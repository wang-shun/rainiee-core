<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdylx"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdysx"%>
<%{
	com.dimeng.p2p.modules.bid.front.service.BidManage investManage3 = serviceSession.getService(com.dimeng.p2p.modules.bid.front.service.BidManage.class);
	int id3 = IntegerParser.parse(request.getParameter("id"));
	com.dimeng.p2p.modules.bid.front.service.entity.Bdxq creditInfo3 = investManage3.get(id3);
	Bdylx dyxxs = investManage3.getDylb(id3);
	%>
		<br/><br/><%=dyxxs.dyName %><br/><br/>
		<%Bdysx[] dysxs= investManage3.getDysx(dyxxs.F01);
			if(dysxs != null && dysxs.length >0){
				for(Bdysx bdysx:dysxs){
		%>
		<%=bdysx.dxsxName %>：<%=bdysx.F04 %><br/>
		<%} %>

	<%} }%>

