<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.entities.T6161"%>
<%@page import="com.dimeng.p2p.S62.entities.T6230"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<html>
<head>
	<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
	<%
		CURRENT_CATEGORY = "YWGL";
		CURRENT_SUB_CATEGORY = "BDGL";
		int loanId = IntegerParser.parse(request.getParameter("loanId"));
		int userId=IntegerParser.parse(request.getParameter("userId"));
		T6161 t6161=ObjectHelper.convert(request.getAttribute("t6161"), T6161.class);
		T6230 t6230=ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
		if(t6161==null){
			t6161=new T6161();
		}
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      	 <div class="p20">
  <div class="border">
    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增借款信息</div>
    
  </div>
  
  <!--切换栏目-->
  <div class="border mt20">
    <div class="tabnav-container">
      <ul class="clearfix">
		<%
			if(loanId>0){
		%>
		<li><a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="tab-btn ">项目信息</a></li>
		<%
			}
		%>			
		<li><a href="javascript:void(0);" class="tab-btn select-a">企业信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
		<%
			if(T6230_F13.S==t6230.F13){
		%>
		<li><a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="tab-btn ">抵押物信息</a></li>
		<%} %>
		<%
			if(T6230_F11.S==t6230.F11)
			{
		%>
		<li><a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="tab-btn ">担保信息</a></li>
		<%} %>
		<li><a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="tab-btn ">附件(马赛克)</a></li>
		<li><a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="tab-btn ">附件(完整版)</a></li>
	</ul>
    </div>
    <div class="tab-content-container p20"> 
      
     
      <div class="tab-item">
        <ul class="gray6">
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业名称：</span>
            <%StringHelper.filterHTML(out, t6161.F04); %>
            <span class="ml5"></span></li>
          <%if("N".equals(t6161.F20)){ %>
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>营业执照登记注册号：</span>
           <%StringHelper.filterHTML(out, t6161.F03); %>
          </li>
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业纳税号：</span>
            <%StringHelper.filterHTML(out, t6161.F05); %>
            <span class="ml5"></span> </li>
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>组织机构代码：</span>
            <%StringHelper.filterHTML(out, t6161.F06); %>
            <span class="ml5"></span></li>
          <%}else{ %>
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>社会信用代码：</span>
            <%StringHelper.filterHTML(out, t6161.F19); %>
            <span class="ml5"></span></li>
          <%} %>
          <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册年份：</span>
            <%=t6161.F07%>
            <span class="ml5">年</span></li>
            <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册资金：</span>
            <%=t6161.F08%>
            <span class="ml5">万元</span></li>
            <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>行业：</span>
            <%StringHelper.filterHTML(out,t6161.F09);%>
            <span class="ml5"></span></li>
            <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>资产净值：</span>
            <%=t6161.F14%>
            <span class="ml5">万元</span></li>
            <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上年度经营现金流入：</span>
            <%=t6161.F15%>
            <span class="ml5">万元</span></li>
            <li class="mb10">
				<div class="pl200 ml5">
					<input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=controller.getURI(request, UpdateProject.class) %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="上一步">
                        <%
                        String nextUrl = "";
                        if(T6230_F13.S==t6230.F13){
                            nextUrl = controller.getURI(request, AddDyw.class);
                        }else if(T6230_F11.S==t6230.F11){
                            nextUrl = controller.getURI(request, AddGuaranteeXq.class);
                        }else{
                            nextUrl = controller.getURI(request, AddAnnexMsk.class);
                        }
                        %>                           
                    <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                              onclick="window.location.href='<%=nextUrl %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                              value="下一步">
					<input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                               onclick="location.href='<%=controller.getURI(request, LoanList.class) %>'"
                                               value="取消"></div>
				</div>
			</li>
        </ul>
      </div>
      
    </div>
  </div>
</div>
      </div>
      </div>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
</body>
</html>