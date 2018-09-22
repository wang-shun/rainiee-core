<%@page import="com.dimeng.p2p.S50.entities.T5013"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.PartnerManage"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%{
    T5013[] partners=serviceSession.getService(PartnerManage.class).getAll();if(partners!=null&&partners.length>0){
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<!--合作伙伴-->
<div class="wrap cooperation">
	<div class="hd clearfix">
	        <span class="f22"><%=articleManage.getCategoryNameByCode("HZHB")%></span>
	</div>
	<div class="cooperation_bd clearfix">
        <div class="prev"><a class="">上一个</a></div>
        <div class="bd">
        <ul class="picList clearfix">
           <%for(T5013 parter:partners){if(parter==null){continue;}%>
           
              <li><a href="http://<%StringHelper.filterHTML(out, parter.F05.replaceAll("http://", "").replaceAll("https://",""));%>" target="_blank"><img src="<%=fileStore.getURL(parter.F06)%>" /><span><%StringHelper.filterHTML(out, parter.F04);%></span></a></li>   
                <%}%>
        </ul>
    </div>
        <div class="next"><a class="">下一个</a></div>
    </div>
</div>     
<%--     <div class="hd clearfix"><%=articleManage.getCategoryNameByCode("HZHB")%></div>
    <div class="cooperation_bd clearfix">
        <div class="prev"><a class="">上一个</a></div>
        <div class="bd">
            <ul class="picList clearfix">
                <%for(T5013 parter:partners){if(parter==null){continue;}%>
                <li><a href="http://<%StringHelper.filterHTML(out, parter.F05.replaceAll("http://", "").replaceAll("https://",""));%>" target="_blank" title="<%StringHelper.filterHTML(out, parter.F04);%>"><img src="<%=fileStore.getURL(parter.F06)%>" alt="<%StringHelper.filterHTML(out, parter.F04);%>"/></a></li>
                <%}%>
            </ul>
        </div>
        <div class="next"><a class="">下一个</a></div>
    </div>
</div> --%>
<!--合作伙伴-->
<%}}%>
