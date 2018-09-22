<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddEnterpriseXm"%>
<%@page import="com.dimeng.p2p.S62.enums.T6233_F10" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.SetPic" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DelAnnexWz" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddUserInfoXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.SaveLine" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAnnexWz" %>
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
    WzAnnex[] t6233s = ObjectHelper.convertArray(request.getAttribute("t6233s"), WzAnnex.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改附件信息
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

                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%
                                    }
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <a href="javascript:void(0)"
                                   onclick="tcc(this,'FJ','<%=controller.getURI(request, AnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>','上传附件')"
                                   class="btn btn-blue radius-6 mr5 pl1 pr15 mb10"><i
                                        class="icon-i w30 h30 va-middle add-icon "></i>上传附件</a>
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>附件名称</th>
                                        <th>附件大小</th>
                                        <th>附件格式</th>
                                        <th>上传人</th>
                                        <th>附件类型</th>
                                        <th>上传时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (t6233s != null && t6233s.length > 0) {
                                            int i = 1;
                                            for (WzAnnex t6233 : t6233s) {
                                                if (t6233 == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td class="tc"><%=i++%>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6233.F04);
                                            %>
                                        </td>
                                        <td><%=t6233.F05 / 1000%>kb</td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6233.F07);
                                            %>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6233.name);
                                            %>
                                        </td>
                                        <td><%
                                            StringHelper.filterHTML(out, t6233.annexName);
                                        %></td>
                                        <td><%=DateTimeParser.format(t6233.F08)%>
                                        </td>
                                        <td class="tc">
                                            <%--<%if(t6233.F10 != T6233_F10.S){ %>
                                            <a href="<%=controller.getURI(request, SetPic.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&id=<%=t6233.F01%>">设为图标</a>
                                            <%} %>--%>
                                            <a href="<%=controller.getURI(request, ViewAnnexWz.class)%>?id=<%=t6233.F01%>"
                                               target="_blank" class="link-blue mr20">查看</a>&nbsp;<a
                                                href="javaScript:void(0);"
                                                onclick="return onDelete(<%=loanId%>,<%=userId%>,<%=t6233.F01%>)" class="link-blue">删除</a>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="8">暂无数据</td>
                                    </tr>
                                    <%} %>

                                    </tbody>
                                </table>
                            </div>
                            <div class="tc w220 pt20">
                            	<a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId %>"
                                   class="btn btn-blue2 radius-6 pl20 pr20 ml10">上一步</a>
                                <a href="<%=controller.getURI(request, SaveLine.class)%>?loanId=<%=loanId%>&userId=<%=userId %>"
                                   class="btn btn-blue2 radius-6 pl20 pr20 ml10">提交</a>
                                <a href="<%=controller.getURI(request, LoanList.class)%>"
                                   class="btn btn-blue2 radius-6 pl20 pr20 ml10">取消</a>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%} %>

<script type="text/javascript">
    function tcc(evn, tid, url, titl) {
        global_art = art.dialog.open(url, {
            id: tid,
            title: titl,
            opacity: 0.1,
            width: 783,
            height: 500,
            lock: true,
            close: function () {
                var iframe = this.iframe.contentWindow;
                window.location.reload();
            }
        }, false);
    }
    function onDelete(loanId,userId,id) {
    	var url="<%=controller.getURI(request, DelAnnexWz.class)%>?loanId="+loanId+"&userId="+userId+"&id="+id;
        $(".popup_bg").show();
        $("#info").html(showForwardInfo("你确认要删除该附件吗?","question",url));
    }
</script>
</body>
</html>