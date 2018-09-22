<%@page import="com.dimeng.p2p.S50.entities.T5013"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.PartnerManage"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%{
    T5013[] partners=serviceSession.getService(PartnerManage.class).getAll();if(partners!=null&&partners.length>0){
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<!--合作伙伴-->
<div class="w1002 cooperation_mod">
	<div class="main_hd clearfix mt20"><h2><%=articleManage.getCategoryNameByCode("HZHB")%></h2></div>
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
</div>
<!--合作伙伴-->
<!-- </div> -->
<%}}%>
