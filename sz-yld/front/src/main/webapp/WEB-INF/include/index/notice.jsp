<%@page import="com.dimeng.p2p.S50.enums.T5015_F02"%>
<%@page import="com.dimeng.p2p.S50.entities.T5015"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.NoticeManage"%>
<%@page import="com.dimeng.p2p.front.servlets.AbstractFrontServlet"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Wzgg"%>
<div class="clear"></div>
<%{
	NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
	PagingResult<T5015> noticeResult = noticeManage.search(AbstractFrontServlet.INDEX_PAGING);
	T5015[] notices = noticeResult.getItems();
	
%>
<!--公告-->
<div class="notice">
    <div class="wrap clearfix">
          <div class="til fl">
            <i></i>
            <span class="n_til">最新公告</span>
        </div>
         <div class="bd">
              <ul>
              <%if (notices != null) {for (T5015 notice : notices) {%>
                  <li>	
                    <a href="<%=controller.getPagingItemURI(request, Wzgg.class,notice.F01)%>">
                        <span class="lbt">【<%=DateParser.format(notice.F09)%>】</span>
                        <span class="lbt_con">【<%=notice.F02.getChineseName() %>】<%StringHelper.filterHTML(out, notice.F05);%></span>
                    </a>
                  </li> 
              <%}}%>
              </ul>
          </div>  
    	<a href="<%configureProvider.format(out, URLVariable.ZXDT_WZGG);%>"  class="more fr">更多&gt;</a>
	</div>
</div>
<!--公告-->
<%}%>