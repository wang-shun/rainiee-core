<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList"%>
<%@page import="com.dimeng.p2p.S62.enums.T6233_F10" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.BidManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Dbxx" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<>
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
    WzAnnex[] t6233s = ObjectHelper.convertArray(request.getAttribute("t6233s"), WzAnnex.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    BidManage bidManage = serviceSession.getService(BidManage.class);
    Dbxx[] dbxx = bidManage.searchBidDb(loanId);
    BigDecimal creditAmount = ObjectHelper.convert(request.getAttribute("creditAmount"), BigDecimal.class);
    BigDecimal DbCreditAmount = ObjectHelper.convert(request.getAttribute("DbCreditAmount"), BigDecimal.class);
    boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container pr"><i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理-附件(完整版)
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
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">附件(完整版)<i class="icon-i tab-arrowtop-icon"></i></a></li>
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
                                        if (t6233s != null && t6233s.length > 0) {
                                            int i = 1;
                                            for (WzAnnex t6233 : t6233s) {
                                                if (t6233 == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
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
                                               target="_blank" class="link-blue">查看</a>
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
                                <%
                                    if (t6230.F20 == T6230_F20.DSH && "SH".equalsIgnoreCase(operationJK)) {
                                %>
                                <%if (creditAmount.compareTo(t6230.F05) >= 0 && !(DbCreditAmount.compareTo(t6230.F05) < 0 && t6230.F11 == T6230_F11.S)) {%>
                                <%if(dimengSession.isAccessableResource(Check.class)){%>
                                <a href="<%=configureProvider.format(URLVariable.BID_CHECK_URL) %>?loanId=<%=loanId %>&userId=<%=userId %>"
                                   class="btn btn-blue2 radius-6 pl20 pr20">审核通过</a>&nbsp;&nbsp;
                                <%}else{%>
                                <a href="javascript:void(0)"
                                   class="btn btn-gray radius-6 pl20 pr20">审核通过</a>&nbsp;&nbsp;
                                <%}%>
                                <%if(dimengSession.isAccessableResource(NotThrough.class)){%>
                                <a href="javascript:void(0)" onclick="showSh()" class="btn btn-blue2 radius-6 pl20 pr20">审核不通过</a>
                                <%}else{%>
                                <a href="javascript:void(0)"  class="btn btn-gray radius-6 pl20 pr20">审核不通过</a>
                                <%}%>
                                <%} else { %>
                                <a href="javascript:void(0)"  class="btn btn-gray radius-6 pl20 pr20">审核通过</a>
                                <%if(dimengSession.isAccessableResource(NotThrough.class)){%>
                                <a href="javascript:void(0)" onclick="showSh()" class="btn btn-blue2 radius-6 pl20 pr20">审核不通过</a>
                                <%}else{%>
                                <a href="javascript:void(0)"  class="btn btn-gray radius-6 pl20 pr20">审核不通过</a>
                                <%}%>
                                <%} %>
                                <%if(dimengSession.isAccessableResource(AdjustCredit.class)){%>
                                <%if (creditAmount.compareTo(t6230.F05) < 0) {%>
                                <span style="color: red">用户信用额度小于借款金额,<a href="javascript:showXyUserPage(1)"
                                                                         class="link-blue ml10">调整</a></span>
                                <%} %>
                                <%if (DbCreditAmount.compareTo(t6230.F05) < 0 && t6230.F11 == T6230_F11.S) {%>
                                <span style="color: red">担保额度小于借款金额,<a href="javascript:showXyUserPage(2)"
                                                                         class="link-blue ml10">调整</a></span>
                                <%} %>
                                <%}else{%>
                                <%if (creditAmount.compareTo(t6230.F05) < 0) {%>
                                <span style="color: red">用户信用额度小于借款金额,<a href="javascript:void(0)"
                                                                         class=" ml10 disabled">调整</a></span>
                                <%} %>
                                <%if (DbCreditAmount.compareTo(t6230.F05) < 0 && t6230.F11 == T6230_F11.S) {%>
                                <span style="color: red">担保额度小于借款金额,<a href="javascript:void(0)"
                                                                         class=" ml10 disabled">调整</a></span>
                                <%} %>
                                <%}%>
                                <%} %>

                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>

<div class="popup-box hide" id="sh" style="margin:-100px 0 0 -220px;display: none;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">审核不通过</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);"
           onclick="javascript:document.getElementById('sh').style.display='none';return false;"></a>
    </div>
    <form action="<%=controller.getURI(request, NotThrough.class) %>" id="shForm" method="post" class="form1">
        <input type="hidden" name="loanId" value="<%=loanId %>"/>
        <input type="hidden" name="useId" value="<%=userId %>"/>

        <div class="popup-content-container pt20 ob20 clearfix">
            <div id="u303 mb40 gray6">
                <span><i class="red">*</i>审核处理结果描述（50字以内）</span>
            </div>
            <div class="pt20">
                <textarea name="des" id="textarea2" cols="45" rows="5" class="area required max-length-50"
                          style="width:100%"></textarea>
                <span tip></span>
                <span errortip class="" style="display: none"></span>
            </div>
            <div class="tc f16 pt20">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <input type="button" id="button2" value="取消"
                       onclick="javascript:document.getElementById('sh').style.display='none';return false;"
                       class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme" fromname="form1"/>
            </div>
        </div>
    </form>
</div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>

<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
</script>
<%} %>
<form id="showXyUserPageForm" action="<%= controller.getURI(request, XyList.class)%>" target="_blank">
    <%
        String userName = "";
        if (creditAmount.compareTo(t6230.F05) < 0) {
            userName = serviceSession.getService(UserManage.class).getUserNameById(userId);
        }
    %>
    <input style="display:none;" name="loginName" value="<%=userName%>" id="loginName" class="yhgl_input"/>
</form>
<form id="showXyJgPageForm" action="<%= controller.getURI(request, XyList.class)%>" target="_blank">
    <%
        String jgName = "";
        if (dbxx != null && dbxx.length > 0) {
            jgName = serviceSession.getService(UserManage.class).getUserNameById(dbxx[0].F03);
        }
    %>
    <input type="hidden" name="loginName" value="<%=jgName%>" id="loginName" class="yhgl_input"/>
    <%
        if(isHasGuarant){
    %>
    <input type="hidden" name="userName" value="<%=jgName%>" id="userName" class="yhgl_input"/>
    <%}%>
</form>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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
                if (isupload == null || "" == isupload
                        || "undefind" == isupload) {

                }
                if (isupload.value == "1") {

                    $(evn).parent().parent().before(
                            '<dt class="ico14"></dt>');
                }
            }
        }, false);
    }
    function showSh() {
        $("#sh").show();
    }
    function showXyUserPage(type) {
        var is_has_guarant = <%=isHasGuarant%>;
        if (type == 1) {
            $("#showXyUserPageForm").submit();
        } else {
            if(is_has_guarant){
                $("#showXyJgPageForm").attr("action","/console/finance/zjgl/yhdbgl/dbList.htm");
            }
            $("#showXyJgPageForm").submit();
        }
    }
</script>
</body>
</html>