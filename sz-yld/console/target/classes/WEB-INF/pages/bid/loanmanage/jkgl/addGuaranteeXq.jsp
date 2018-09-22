<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Dbxx" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    int loanId = IntegerParser
            .parse(request.getParameter("loanId"));
    int userId = IntegerParser
            .parse(request.getParameter("userId"));
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    Dbxx[] t6236s = ObjectHelper.convertArray(request.getAttribute("t6236s"), Dbxx.class);
    if (t6236s == null) {
        t6236s = new Dbxx[0];
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改担保信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <%
                                    if (loanId > 0) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">项目信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%} %>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddUserInfoXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%} %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddEnterpriseXm.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">企业信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>

                                <li><a href="javascript:void(0)" class="tab-btn select-a">担保信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form action="<%=controller.getURI(request, AddGuaranteeXq.class)%>" method="post" id="form1"
                                  class="form1">
                                <input type="hidden" name="loanId" value="<%=loanId %>"/>
                                <input type="hidden" name="userId" value="<%=userId %>"/>

                                <div class="tab-item">
                                    <% if (t6236s.length == 0) { %>
                                    <a href="<%=controller.getURI(request, AddGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId %>"
                                       class="btn btn-blue radius-6 mr5 pl1 pr15 mb10"><i
                                            class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                    <%} %>
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>序号</th>
                                            <th>担保方</th>
                                            <!-- <th>是否主担保</th> -->
                                            <th>担保机构介绍</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            if (t6236s != null && t6236s.length > 0) {
                                                int i = 1;
                                                for (Dbxx t6236 : t6236s) {
                                                    if (t6236 == null) {
                                                        continue;
                                                    }
                                        %>
                                        <tr class="tc">
                                            <td><%=i++%>
                                            </td>
                                            <td><%StringHelper.filterHTML(out, t6236.F06);%></td>
                                            <%-- <td><%=t6236.F04.getChineseName()%>
                                            </td> --%>
                                            <td title="<%StringHelper.filterHTML(out, t6236.jgjs);%>"><%
                                                StringHelper.filterHTML(out, StringHelper.truncation(t6236.jgjs, 30));%></td>
                                            <td class="tc"><a
                                                    href="<%=controller.getURI(request, DetailGuarantee.class)%>?id=<%=t6236.F01%>&loanId=<%=loanId%>&userId=<%=userId%>&type=0"
                                                    class="link-blue">查看</a>&nbsp;
                                                <a href="<%=controller.getURI(request, UpdateGuarantee.class)%>?id=<%=t6236.F01%>&loanId=<%=loanId%>&userId=<%=userId%>"
                                                   class="link-blue ml20">修改</a>
                                                <a href="javascript:void(0);"
                                                   class="link-blue ml20" onclick="delDbxx('<%=controller.getURI(request, DelDbxx.class)%>?loanId=<%=loanId %>&userId=<%=userId %>&id=<%=t6236.F01%>')">删除</a>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr class="tc">
                                            <td colspan="4" class="tc">暂无数据</td>
                                        </tr>
                                        <%} %>

                                        </tbody>
                                    </table>
                                </div>
                                <div class="tc w220 pt20">
                                	<%
                                                   String prevUrl = "";
				                                	if (T6230_F13.S == t6230.F13){
				                                	    prevUrl = controller.getURI(request, AddDyw.class);
				                                	}else if (userType == T6110_F06.FZRR) {
                                                       prevUrl = controller.getURI(request, AddEnterpriseXm.class);
                                                   }else{
                                                       prevUrl = controller.getURI(request, ViewAuthentication.class);
                                                   }
                                                   
                                                   %>
                                      <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                 onclick="window.location.href='<%=prevUrl %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                 value="上一步">
                                	<input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1" value="下一步"/>
                                    <input type="button"
                                           onclick="window.location.href='<%=controller.getURI(request, LoanList.class)%>'"
                                           class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                </div>
                            </form>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();

</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">
function delDbxx(url){
	$(".popup_bg").show();
    $("#info").html(showForwardInfo("你确定要删除担保信息吗?","question",url));
}
</script>
</body>
</html>