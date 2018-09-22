<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList"%>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.S62.entities.T6235" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.entities.T6234" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewUserInfo" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAuthentication" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewEnterprise" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewGuarantee" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexMsk" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexWz" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewRecord" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewHkRecord" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProgresBidInfo" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>

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
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    T6234[] t6234s = ObjectHelper.convertArray(request.getAttribute("t6234s"), T6234.class);
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    T6235 t6235 = ObjectHelper.convert(request.getAttribute("t6235"), T6235.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看抵押物信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl %>'" value="返回">
                            </div>
                        </div>

                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <%
                                    if (loanId > 0) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">项目信息</a></li>
                                <%
                                    }
                                %>
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
                                <li><a href="javascript:void(0);" class="tab-btn select-a">抵押物信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%
                                    }
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
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
                        <!--切换栏目-->
                        <div class="tab-content-container p20">


                            <ul class="gray6">
                                <li class="mb10">
                                    <div class="pl200 ml5">
													<textarea name="dywxx" cols="100" rows="8"
                                                              style="width:700px;height:500px;visibility:hidden;"
                                                              class="required min-length-20 max-length-60000">
													<%
                                                        if (t6235 != null) {
                                                            StringHelper.format(out, t6235.F04, fileStore);
                                                        }
                                                    %>
													</textarea>
                                        <span tip>20-60000字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </div>
                                    <%-- <table width="100%" border="0" cellspacing="0" cellpadding="3" class="yhgl_table tc">
                                        <tr class="hsbg">
                                            <td>序号</td>
                                            <td>抵押物类型ID</td>
                                            <td>抵押物名称</td>
                                            <td>操作</td>
                                        </tr>
                                        <%
                                            if(t6234s!=null)
                                            {int i=1;
                                            for(T6234 t6234:t6234s)
                                            {	if(t6234==null){continue;}
                                        %>
                                        <tr>
                                            <td><%=i++%></td>
                                            <td><%=t6234.F03%></td>
                                            <td>
                                                <%
                                                    StringHelper.filterHTML(out, t6234.F04);
                                                %>
                                            </td>
                                            <td><a href="<%=controller.getURI(request, DetailDyw.class)%>?loanId=<%=loanId %>&userId=<%=userId %>&id=<%=t6234.F01%>&type=1">查看</a>&nbsp;
                                            </td>
                                        </tr>
                                        <%}} %>
                                    </table> --%>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript">
    function save() {
        $("#flag").attr("value", "0");
    }
    function saveCon() {
        $("#flag").attr("value", "1");
    }

    //富文本框
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="dywxx"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.BID_DETAILS_FILE.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            items: ['fullscreen'],
            afterBlur: function () {
                this.sync();
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });
</script>
</body>
</html>