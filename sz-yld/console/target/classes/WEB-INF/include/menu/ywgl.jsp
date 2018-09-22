<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.contract.ContractPreservationList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreementSave.AgreementSaveList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.CsList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.JsstList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.hmd.HmdList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.QyjkDzxy"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.TbzList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferDshList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.GrjkyxList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.qyjkyx.QyjkyxList"%>

<div class="item-subnav-box" data-title="business">
	<dl>
		<dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-ywgl-icon2"></i>业务管理</dt>
		<dd><a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">理财管理</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
			<%
				if (dimengSession.isAccessableResource(TbzList.class)) {
			%>
			<li><a class="click-link <%if("ZQGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, TbzList.class)%>" target="mainFrame">债权管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">债权管理</a></li>
			<%
				}
			%>
			
			<%
				if (dimengSession.isAccessableResource(TransferDshList.class)) {
			%>
			<li><a class="click-link <%if("ZQZRGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="ZQZRGL" href="<%=controller.getURI(request, TransferDshList.class)%>" target="mainFrame">线上债权转让管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">线上债权转让管理</a></li>
			<%
				}
			%>
        </ul>
      </dd>
     <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">借款管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        	<%
				if (dimengSession.isAccessableResource(LoanList.class)) {
			%>
			<li><a class="click-link <%if("BDGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="BDGL" href="<%=controller.getURI(request, LoanList.class)%>" target="mainFrame">借款管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">借款管理</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource(GrjkyxList.class)) {
			%>
			<li><a class="click-link <%if("GRJKYX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="GRJKYX" href="<%=controller.getURI(request, GrjkyxList.class)%>" target="mainFrame">个人借款意向管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">个人借款意向管理</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource(QyjkyxList.class)) {
			%>
			<li><a class="click-link <%if("QYJKYX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="QYJKYX" href="<%=controller.getURI(request, QyjkyxList.class)%>" target="mainFrame">企业借款意向管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">企业借款意向管理</a></li>
			<%
				}
			%>
        </ul>
      </dd>
		<%//开关判断，为false，则不显示公益标
		if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){%>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">公益捐赠</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        	<%
				if (dimengSession.isAccessableResource("P2P_C_BUSI_GYLOANLIST")) {
			%>
			<li><a class="click-link <%if("GYBDGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/bid/gyjz/gyyw/gyLoanList.htm" target="mainFrame">公益项目</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">公益项目</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource("P2P_C_BUSI_GYLOANPROGRESLIST")) {
			%>
			<li><a class="click-link <%if("GYBD_PROGRES_GL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/bid/gyjz/jzgl/gyLoanProgresList.htm" target="mainFrame">进展管理</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">进展管理</a></li>
			<%
				}
			%>
        </ul>
      </dd>
		<%}%>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">催收管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        	<%
				if (dimengSession.isAccessableResource(JsstList.class)) {
			%>
			<li><a class="click-link <%if("DHKLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, JsstList.class)%>" target="mainFrame">待还款列表</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">待还款列表</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource(CsList.class)) {
			%>
			<li><a class="click-link <%if("CSJL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, CsList.class)%>" id="CSJL" target="mainFrame">催收记录</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">催收记录</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource(HmdList.class)) {
			%>
			<li><a class="click-link <%if("HMD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, HmdList.class)%>" target="mainFrame">黑名单</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">黑名单</a></li>
			<%
				}
			%>
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">合同管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
          
          	<%
				if (dimengSession.isAccessableResource(QyjkDzxy.class)) {
			%>
			<li><a class="click-link <%if("DZXY".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, QyjkDzxy.class)%>" target="mainFrame">平台电子协议</a></li>
			<%
				} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">平台电子协议</a></li>
			<%
				}
			%>
			
			
			<%
				boolean isSaveBidContract = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT));
				boolean isAllowBadClaimTransfer = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_ALLOW_BADCLAIM_TRANSFER));
				boolean isSaveAdvanceContract =  BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT));
				boolean isSaveTransferContract = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_TRANSFER_CONTRACT));
				if(isSaveBidContract||isAllowBadClaimTransfer||isSaveAdvanceContract||isSaveTransferContract){
				if (dimengSession.isAccessableResource("P2P_C_BID_HTGL_HTBQ_LIST")) {
			%>
			<li><a class="click-link <%if("HTBQLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/bid/htgl/contract/contractPreservationList.htm" target="mainFrame">合同保全列表</a></li>
			<%
			} else {
			%> 
			<li><a href="javascript:void(0)" class="disabled">合同保全列表</a></li>
			<%
				}}
			%>
			
			<%
				if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_LOAN_CONTRACT))){
				if (dimengSession.isAccessableResource("P2P_C_BID_HTGL_XYBQ_LIST")) {
			%>
			<li><a class="click-link <%if("XYBQ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/bid/htgl/agreementSave/agreementSaveList.htm" target="mainFrame">协议保全列表</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">协议保全列表</a></li>
			<%
				}
				}
			%>
        </ul>
      </dd>
    </dl>
  </div>