<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.tzjl.JgTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.S61.entities.T6164" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dfjl.ViewListDfjl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "JG";
    T6164 entity = ObjectHelper.convert(request.getAttribute("info"), T6164.class);
    String email = ObjectHelper.convert(request.getAttribute("email"), String.class);
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String ckUrl = controller.getURI(request, JgList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        ckUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>机构信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl %>'" value="返回">
                            </div>
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">机构信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">联系信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">房产信息</a></li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%
                                	boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
	                            	if (isHasBadClaim){
                                   %>
                                <li>
                                    <a class="tab-btn" href="/console/account/vipmanage/jgxx/blzqgmjl/blzqGmjlView.htm?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">不良债权购买记录</a>
                                </li>
                                <%} %>
                                <%if(!"huifu".equalsIgnoreCase(ESCROW_PREFIX)){ %>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, JgTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>所在区域：</span>
                                        <%StringHelper.filterHTML(out, String.valueOf(request.getAttribute("region")));%>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>联系地址：</span>
                                        <%StringHelper.filterHTML(out, entity.F03); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人手机号码：</span>
                                        <%StringHelper.filterHTML(out, entity.F06); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人邮箱地址：</span>
                                        <%StringHelper.filterHTML(out, email); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">企业联系人：</span>
                                        <%StringHelper.filterHTML(out, entity.F07); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">联系人电话：</span>
                                        <%StringHelper.filterHTML(out, entity.F04); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">网站地址：</span>
                                        <%StringHelper.filterHTML(out, entity.F05); %>
                                    </li>

                                </ul>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>