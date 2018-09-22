<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6125_F05"%>
<%@page import="com.dimeng.p2p.common.enums.FundAccountType"%>
<%@ page import="com.dimeng.p2p.repeater.guarantor.entity.GuarantorEntity" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhdbgl.*" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.AbstractGuarantorServlet" %>
<%@ page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    PagingResult<GuarantorEntity> result = (PagingResult<GuarantorEntity>) request.getAttribute("result");
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "DBGL";
    
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="searchForm" name="form1" action="<%=controller.getURI(request, DbList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户担保管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名： </span>
                                        <input type="text" name="userName" id="userName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">姓名/企业名： </span>
                                        <input type="text" name="realName" id="realName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">担保人编号： </span>
                                        <input type="text" name="code" id="code"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("code"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <%if(!"huifu".equals(ESCROW_PREFIX)){ %>
                                    <li><span class="display-ib mr5">用户类型： </span>
                                        <select name="userType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (FundAccountType fundAccountType : FundAccountType.values()) {
                                                    if(fundAccountType == FundAccountType.PT)
                                                    {
                                                        continue;
                                                    }
                                            %>
                                            <option value="<%=fundAccountType.name()%>" <%if (fundAccountType.name().equals(request.getParameter("userType"))) {%>
                                                    selected="selected" <%}%>><%= fundAccountType == FundAccountType.JG ? "机构" : fundAccountType.getName()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <%} %>
                                    <li><a href="javascript:$('#searchForm').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportDbList.class)) {%>
                                        <a href="javascript:void(0);" onclick="showExport();"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%}%>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">用户名</th>
                                    <th class="tc">用户类型</th>
                                    <th class="tc">姓名/企业名</th>
                                    <th class="tc">担保人编号</th>
                                    <th class="tc">担保额度(元)</th>
                                    <th class="tc">状态</th>
                                    <th class="tc">最后更新时间</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    GuarantorEntity[] guarantors = null;
                                    if (result != null) {
                                        guarantors = result.getItems();
                                    }
                                    if (guarantors != null) {
                                        int i = 0;
                                        for (GuarantorEntity guarantor : guarantors) {
                                %>
                                <tr class="tc">
                                    <td><%=++i%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, guarantor.userName); %></td>
                                    <td><%StringHelper.filterHTML(out, guarantor.userType);%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, guarantor.realName);%></td>
                                    <td><%StringHelper.filterHTML(out, guarantor.code);%></td>
                                    <td><%=Formater.formatAmount(guarantor.guarantAmount) %></td>
                                    <td><%=guarantor.status.getChineseName()%></td>
                                    <td><%=DateTimeParser.format(guarantor.updateTime)%></td>
                                    <td class="blue">
                                        <%
                                        if(guarantor.status == T6125_F05.SQDCL || guarantor.status == T6125_F05.QXDCL){
                                        %>
                                            <%
                                                if(dimengSession.isAccessableResource(Dbsh.class)){
                                            %>
                                            <a class="link-blue mr20"
                                               href="javascript:void(0)" onclick="dbsh(<%=guarantor.id%>)">审核</a>
                                            <%}else{%>
                                            <a class="disabled">审核</a>
                                            <%}%>
                                        <%}else{%>
                                        	<%if(!"机构".equals(guarantor.userType)){ %>
		                                        <a class="link-blue mr20"
		                                           href="javascript:void(0)" onclick="viewDetail(<%=guarantor.id%>)">详情</a>
                                           <%} %>
                                            <%if(guarantor.status == T6125_F05.SQCG){%>
                                            <%
                                                if(dimengSession.isAccessableResource(UpdateGuarantAmount.class)){
                                            %>
                                            <a class="link-blue mr20"
                                               href="javascript:void(0)" onclick="updateDb(<%=guarantor.id%>,'<%=guarantor.guarantAmount%>')">修改额度</a>
                                            <%}else{%>
                                            <a class="disabled mr20">修改额度</a>
                                            <%}%>
                                            <%
                                                if(dimengSession.isAccessableResource(DbRecordList.class)){
                                            %>
                                            <a class="link-blue mr20"
                                               href="<%=controller.getURI(request,DbRecordList.class)%>?id=<%=guarantor.userId%>">额度明细</a>
                                            <%}else{%>
                                            <a class="disabled mr20">额度明细</a>
                                            <%}%>
                                            <%
                                                if(!guarantor.userType.equals("机构")){
                                                if(dimengSession.isAccessableResource(CancelGuarantor.class)){
                                            %>
                                            <a class="link-blue mr20"
                                               href="javascript:void(0)" onclick="cancelGuarant(<%=guarantor.id%>,'<%=guarantor.realName%>')">取消担保</a>
                                            <%}else{%>
                                            <a class="disabled mr20">取消担保</a>
                                            <%}}%>
                                        <%}else if(guarantor.status == T6125_F05.QXCG){
                                            if(dimengSession.isAccessableResource(DbRecordList.class)){
                                        %>
                                            <a class="link-blue mr20"
                                               href="<%=controller.getURI(request,DbRecordList.class)%>?id=<%=guarantor.userId%>">额度明细</a>
                                       <%}else{%>
                                            <a class="disabled mr20">额度明细</a>
                                        <%}}}%>

                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="9" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>

                        <!--分页-->
                        <%AbstractGuarantorServlet.rendPagingResult(out, result); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
        <div id="popup_bg" class="popup_bg hide"></div>
    </div>
    <!--右边内容 结束-->

<%
    if (guarantors != null) {
        for (int i = 0; i < guarantors.length; i++) {
            GuarantorEntity record = guarantors[i];
            if (record == null) {
                continue;
            }
%>
<div id="dbAmount_<%=record.id%>" style="display: none;" class="dbAmount">
<div class="popup-box" style="min-height: 100px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">修改担保额度</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);"
           onclick="javascript:document.getElementById('dbAmount_<%=record.id%>').style.display='none';closeInfo();;return false;"></a>
    </div>
    <form action="<%=controller.getURI(request, UpdateGuarantAmount.class)%>" id="dbAmountForm_<%=record.id%>" method="post"
          class="form2">
        <input type="hidden" name="id" value="<%=record.id%>"/>
        <div class="popup-content-container pt20 ob20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em>担保额度：</span>
                        <input class="text border w150 pl5 required max-precision-2 maxf-size-999999999999.99"
                               type="text" name="guarantAmount" id="guarantAmount_<%=record.id%>" mtest="/^[0-9.]*$/" value="<%=record.guarantAmount%>"
                               mtestmsg="输入存在非法字符!">
                        <span tip>精确到小数点后两位</span>
                        <span errortip class="" style="display: none"></span>
                    </li>
                </ul>
            </div>
            <div class="tc f16">
                <a href="javascript:void(0);" name="save" fromname="form2" onclick="subUpdateDb(<%=record.id%>)"
                   class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                   onclick="javascript:document.getElementById('dbAmount_<%=record.id%>').style.display='none';closeInfo();return false;">取消</a>
            </div>
        </div>
    </form>
</div>
    <div class="popup_bg"></div>
</div>
<div id="showDataId_<%=record.id%>" style="display: none;" class="xqdb">
    <div class="popup-box" style="min-height: 270px; margin-top:-200px;">
        <div class="popup-title-container">
            <h3 class="pl20 f18">申请详情</h3>
            <a class="icon-i popup-close2" onclick="javascript:document.getElementById('showDataId_<%=record.id%>').style.display='none';closeInfo();return false;"></a></div>
        <div class="popup-content-container-2" style="max-height:400px;">
            <div class="p30">

                <div class=" pb10" style="min-height: 280px;">
                    <ul class="gray6">
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">用户名：</span>
                            <div class="pl120"><%StringHelper.filterHTML(out, record.userName);%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">用户类型：</span>
                            <div class="pl120"><%=record.userType%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">姓名/企业名：</span>
                            <div class="pl120"><%=record.realName%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">担保人编号：</span>
                            <div class="pl120"><%=record.code%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">状态：</span>
                            <div class="pl120"><%=record.status.getChineseName()%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">操作人：</span>
                            <div class="pl120"><%=record.auditor%></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">操作时间：</span>
                            <div class="pl120"><%=DateTimeParser.format(record.auditTime)%></div>
                        </li>
                        <li class="mb15"><div class="display-ib tr mr5 w120 fl">审核意见：</div>
                            <div class="pl120"><%StringHelper.filterHTML(out, record.auditDesc);%></div>
                        </li>
                    </ul>
                </div>
                <div class="tc f16"  id="close1">
                    <input type="submit" onclick="javascript:document.getElementById('showDataId_<%=record.id%>').style.display='none';closeInfo();return false;" class="btn-blue2 btn white radius-6 pl20 pr20" value="关闭" />
                </div>
            </div>
        </div>
    </div>
    <div class="popup_bg"></div>
</div>
<%
        }
    }
%>

<div id="shDiv" style="display: none;">
    <div class="popup-box" style="min-height: 270px;">
        <form action="<%=controller.getURI(request, Dbsh.class)%>" id="sh_form" method="post">
            <input type="hidden" name="id" id="id"/>
            <input type="hidden" name="status" id="status"/>
            <div class="popup-title-container">
                <h3 class="pl20 f18">申请担保</h3>
                <a class="icon-i popup-close2" href="javascript:void(0);"
                   onclick="javascript:document.getElementById('shDiv').style.display='none';closeInfo();return false;"></a>
            </div>
            <div class="popup-content-container pt20 ob20 clearfix" >
                <div class="mb20 gray6">
                    <p class="h30 lh30"><span class="red">*</span>审核意见：</p>

                    <div>
                        <textarea class="w400 h120 border p5 required" rows="4" cols="40" name="desc" id="desc"></textarea>
                        <span id="errortip" class="error_tip pl20"></span>
                    </div>
                </div>
                <div class="tc f16">
                    <input type="submit" onclick="return checkStatus('SQCG');" class="btn-blue2 btn white radius-6 pl20 pr20" value="同意" />
                    <input type="submit" onclick="return checkStatus('SQSB');" value="拒绝" class="btn-blue2 btn white radius-6 pl20 pr20 ml40"/>
                </div>
            </div>
        </form>
    </div>
    <div class="popup_bg"></div>
</div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportDbList.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, DbList.class)%>";
    }

    function dbsh(id)
    {
        $("#desc").val("");
        $("#id").val(id);
        $("#shDiv").show();
        $("#errortip").html("");
        $("#shDiv .popup-box").show();
        $(".popup_bg").show();
    }

    function checkStatus(param)
    {
        var area = $.trim($("#desc").val());
        $("#status").val(param);
        var max = 100; //获取maxlength的值
        if(area.length == 0){
            $("#errortip").html("不能为空！");
            return false;
        }
        if(area.length > max){//textarea的文本长度大于maxlength
            $("#errortip").html("超过输入限制" + max + ",当前长度为" + area.length);
            return false;
        }
        return true;
    }

    function viewDetail(id){
        $("#showDataId_"+id).show();
        $(".xqdb .popup-box").show();
        $(".popup_bg").show();
    }

    function updateDb(id,amount){
        $("#guarantAmount_"+id).val(amount);
        $("#dbAmount_"+id).show();
        $(".dbAmount .popup-box").show();
        $(".popup_bg").show();
        $("#guarantAmount_" + id).nextAll("span[errortip]").html("");
        $("#guarantAmount_" + id).nextAll("span[tip]").show();
    }

    function subUpdateDb(id)
    {
        if (checkText($("#guarantAmount_" + id))) {
            $("#dbAmountForm_" + id).submit();
        }
        return;
    }

    function cancelGuarant(id,realName)
    {
        $(".popup_bg").show();
        $("#info").html(showConfirmDiv("是否取消“" +realName + "”的担保权限？", id, null));
    }


    function toConfirm(id, type) {
        location.href = "<%=controller.getURI(request,CancelGuarantor.class)%>?id="+id;
    }
</script>
</body>
</html>	