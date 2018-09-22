<%@page import="com.dimeng.p2p.S50.enums.T5021_F07"%>
<%@page import="com.dimeng.p2p.S61.entities.T6196"%>
<%@page import="com.dimeng.p2p.front.servlets.yysj.OperationData"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic"%>
<%@page import="com.dimeng.p2p.repeater.policy.OperateDataManage"%>
<%
	{
        PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
        DimengRSAPulicKey publicKey = (DimengRSAPulicKey)ptAccountManage.getPublicKey();
        String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
        String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
BidManage bidManage = serviceSession.getService(BidManage.class);
IndexStatic indexStatic = bidManage.getIndexStatic();
OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
T6196 t6196 = manage.getT6196();
%>
<!--investment-->
<div class="stat_data">
	 <div class="wrap clearfix">
        <div class="statistical">
            <div class="stat_wrap">
            	        <ul class="clearfix">
                    <li class="fi">
                        <span class="s_til">累计投资总金额</span>
                        <span class="num" data-from="0" data-to="<%=Formater.formatAmount(indexStatic.rzzje.add(t6196.F02).doubleValue()/10000).replace(",", "") %>"><%=Formater.formatAmount(indexStatic.rzzje.add(t6196.F02).doubleValue()/10000) %></span>
                        <span class="unit">万元</span><br />
                    </li>
                    <li class="se">
                        <span class="s_til">累计投资总收益</span>
                        <span class="num" data-from="0" data-to="<%=Formater.formatAmount(indexStatic.yhzsy.add(t6196.F04).doubleValue()/10000).replace(",", "")%>"><%=Formater.formatAmount(indexStatic.yhzsy.add(t6196.F04).doubleValue()/10000) %></span>
                        <span class="unit">万元</span><br />
                    </li>

                    <li class="last">
                        <span class="s_til">累计投资人数</span>
                        <span class="num" data-from="0" data-to="<%=indexStatic.yhjys.add(new BigDecimal(t6196.F06+""))%>"><%=indexStatic.yhjys.add(new BigDecimal(t6196.F06+""))%></span>
                        <span class="unit">人</span><br />
                    </li>
                </ul>
                <a href="<%configureProvider.format(out, URLVariable.XXPL_BAXX_YYSJ);%>" class="stat_more more">更多&gt;</a> 
              </div>
         </div>
    </div>
</div>




















