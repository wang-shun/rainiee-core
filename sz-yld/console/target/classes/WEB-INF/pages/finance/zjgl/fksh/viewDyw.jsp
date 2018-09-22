<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewHkRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewRecord" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewEnterprise" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewUserInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProject" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailDyw" %>
<%@page import="com.dimeng.p2p.S62.entities.T6235" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "FKGL";
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    //T6234[] t6234s = ObjectHelper.convertArray(request.getAttribute("t6234s"), T6234.class);
    T6235 t6235 = ObjectHelper.convert(request.getAttribute("t6235"), T6235.class);
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看借款信息
                        </div>
                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix pr pr80">
                                <%if (loanId > 0) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">项目信息</a></li>
                                <%}%>
                                <%if (userType == T6110_F06.ZRR) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewUserInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">个人认证信息</a></li>
                                <%}%>
                                <%if (userType == T6110_F06.FZRR) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewEnterprise.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">企业信息</a></li>
                                <%}%>
                                <li><a href="javascript:void(0);" class="tab-btn-click select-a">抵押物信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%if (T6230_F11.S == t6230.F11) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">担保信息</a></li>
                                <%}%>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">附件(完整版)</a></li>
                                <%if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">投资记录</a></li>
                                <%if(t6230.F20 != T6230_F20.YLB){%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">还款计划</a></li>

                                <li><a href="<%=controller.getURI(request, ViewProgresBidInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">项目动态</a></li>
                                <%}}%>
                                <li class="pa right0 top0 mt5"><input type="button" value="返回"
                                                                       class="btn btn-blue radius-6 pl20 pr20 ml40 mr10"
                                                                       onclick="window.location.href='<%=configureProvider.format(URLVariable.FKSHLIST_URL)%>'">
                                </li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20 ml100">
							<textarea name="dywxx" id="textarea_id" cols="100" rows="8" style="width: 700px; height: 500px;"><%if (t6235 != null) {StringHelper.format(out, t6235.F04, fileStore);}%></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">
KindEditor.ready(function(K) {
    window.editor = K.create('#textarea_id', {
    	readonlyMode : true
    });
});
</script>
</body>
</html>