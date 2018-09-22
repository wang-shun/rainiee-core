<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.qyjkyx.QyjkyxList"%>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Qyjkyx" %>
<%@page import="com.dimeng.util.Formater" %>
<%@page import="com.dimeng.p2p.S62.enums.T6281_F18" %>
<%@page import="com.dimeng.p2p.S62.enums.T6281_F19" %>
<%@page import="com.dimeng.p2p.S62.enums.T6281_F20" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "QYJKYX";
    Qyjkyx qyjkyx = (Qyjkyx) request.getAttribute("qyjkyx");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业借款意向详情
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <ul class="gray6">
                                <%if (qyjkyx.F02 > 0) {%>
                                <li class="mb10"><span class="display-ib w200 tr mr5">会员账号：</span><%
                                    StringHelper.filterHTML(out, qyjkyx.hyzh);%></li>
                                <%}%>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">企业名称：</span><%StringHelper.filterHTML(out, qyjkyx.F03);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">注册号：</span><%StringHelper.filterHTML(out, qyjkyx.F04);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">公司地址：</span><%StringHelper.filterHTML(out, qyjkyx.F08);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">联系人：</span><%StringHelper.filterHTML(out, qyjkyx.F05);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">手机号码：</span><%StringHelper.filterHTML(out, qyjkyx.F06);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">借款金额：</span><%=Formater.formatAmount(qyjkyx.F09)%>
                                   	 元
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">借款期限：</span><%=qyjkyx.F22%>个月</li>
                                <li class="mb10"><span class="display-ib w200 tr mr5">借款类型：</span>
	                                <% if (qyjkyx.F18 == T6281_F18.F && qyjkyx.F19 == T6281_F19.F && qyjkyx.F20 == T6281_F20.F) { %>
	                                <input type="checkbox" checked="checked" disabled/>信用
	                                <input type="checkbox" disabled/>抵押
	                                <input type="checkbox" disabled/>实地认证
	                                <input type="checkbox" disabled/>担保
	                                <% } else {%>
	                                 <input type="checkbox" disabled/>信用
	                                 <input type="checkbox" disabled <%if (qyjkyx.F18 == T6281_F18.S) { %> checked="checked"<%}%>/>抵押
	                                 <input type="checkbox" disabled <%if (qyjkyx.F19 == T6281_F19.S) { %> checked="checked"<%}%>/>实地认证
	                                 <input type="checkbox" disabled <%if (qyjkyx.F20 == T6281_F20.S) { %> checked="checked"<%}%>/>担保
	                                 <%}%>
                                 </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">所在城市：</span><%StringHelper.filterHTML(out, qyjkyx.szcs);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">筹款期限：</span><%StringHelper.filterHTML(out, qyjkyx.F12);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">借款描述：</span><%StringHelper.filterHTML(out, qyjkyx.F13);%>
                                </li>
                                <li class="mb10"><span
                                        class="display-ib w200 tr mr5">提交时间：</span><%=TimestampParser.format(qyjkyx.F16)%>
                                </li>
                                <li class="mb10">
                                    <div class="pl200 ml5">
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                               onclick="location.href='<%=controller.getURI(request, QyjkyxList.class) %>'"
                                               value="返回"></div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>