<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewUserInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewEnterprise" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewHkRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAnnexMsk" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.MskAnnex" %>
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
    String ckUrl = controller.getURI(request, LoanList.class);
    String operationJK = request.getParameter("operationJK");
    if (StringHelper.isEmpty(operationJK)) {
        operationJK = "CK";
    }else{
        CURRENT_CATEGORY = "CWGL";
        CURRENT_SUB_CATEGORY = "BLZQZRGLLIST";
        if("BLZQDZR".equals(operationJK))
        {
            ckUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
        }else if("BLZQDSH".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
        }else if("BLZQZRZ".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
        }else if("BLZQYZR".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
        }else if("BLZQZRSB".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
        }
    }
    MskAnnex[] t6232s = ObjectHelper.convertArray(request.getAttribute("t6232s"), MskAnnex.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container pr"><i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理-附件(马赛克)
                            <div class="pa right0 top5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl %>'" value="返回">
                            </div>
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">项目信息</a></li>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewUserInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewEnterprise.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">企业信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%} %>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">附件(马赛克)<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                                <%
                                    if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB && t6230.F20 != T6230_F20.YZF) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">投资记录</a></li>
                                <%if(t6230.F20 != T6230_F20.YLB){%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">还款计划</a></li>
                                <li><a href="<%=controller.getURI(request, ViewProgresBidInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">项目动态</a></li>
                                <%}} %>
                                
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
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
                                        if (t6232s != null && t6232s.length > 0) {
                                            int i = 1;
                                            for (MskAnnex t6232 : t6232s) {
                                                if (t6232 == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6232.F05);
                                            %>
                                        </td>
                                        <td><%=t6232.F06 / 1000%>kb</td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6232.F07);
                                            %>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, t6232.name);
                                            %>
                                        </td>
                                        <td><%
                                            StringHelper.filterHTML(out, t6232.annexName);
                                        %></td>
                                        <td><%=DateTimeParser.format(t6232.F08)%>
                                        </td>
                                        <td class="tc"><a
                                                href="<%=controller.getURI(request, ViewAnnexMsk.class)%>?id=<%=t6232.F01%>"
                                                target="_blank" class="link-blue">查看</a></td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="8" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>

<%@include file="/WEB-INF/include/dialog.jsp" %>
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
                var isupload = iframe.document.getElementById('isupload');
                if (isupload == null || "" == isupload || "undefind" == isupload) {

                }
                if (isupload.value == "1") {

                    $(evn).parent().parent().before('<dt class="ico14"></dt>');
                }
            }
        }, false);
    }
</script>
</body>
</html>