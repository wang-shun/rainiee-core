<%@page import="com.dimeng.p2p.account.user.service.entity.TenderAccount"%>
<%@page import="com.dimeng.p2p.account.user.service.MyExperienceManage"%>
<div class="user_mod">
    <div class="user_til"><i class="icon"></i><span class="gray3 f18">我的体验金统计</span></div>
    <div class="amount_list clearfix mt30 mb10">
     <%
        MyExperienceManage service=serviceSession.getService(MyExperienceManage.class);
        TenderAccount ai= service.censusAll();
        final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
    	Paging paging=new Paging(){
    		public int getCurrentPage(){
    			return currentPage;
    		}
    		
    		public int getSize(){
    			return 10;
    		}
    	};
     	if(ai != null){
     %>
        <ul>
            <li>体验金已赚金额
                <p class="f22 orange" ><%=Formater.formatAmount(ai.tyjyz) %>元</p>
            </li>
            <li>待赚金额
                <p class="f22 orange" ><%=Formater.formatAmount(ai.tyjdz) %>元</p>
            </li>
            <li>
                回收中的体验金数量
                <p class="f22 orange" ><%=ai.tyjcyl %>个</p>
            </li>

        </ul>
     <%
     	}else{
     %>	
        <ul>
            <li>体验金已赚金额
                <p class="f22 orange" >0.00元</p>
            </li>
            <li>待赚金额
                <p class="f22 orange" >0.00元</p>
            </li>
            <li>
                回收中的体验金理财数量
                <p class="f22 orange" >0个</p>
            </li>

        </ul>
     <%
     	}
     %>
    </div>
</div>