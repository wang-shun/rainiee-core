<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewDyw" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.BidDywsx" %>
<%@page import="com.dimeng.p2p.S62.entities.T6234" %>
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
    T6234 t6234 = ObjectHelper.convert(request.getAttribute("t6234"), T6234.class);
    BidDywsx[] t6235s = ObjectHelper.convertArray(request.getAttribute("t6235s"), BidDywsx.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    int type = IntegerParser.parse(request.getParameter("type"));
    if (t6234 == null) {
        t6234 = new T6234();
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看抵押物</div>
                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>抵押物类型：</span>
                                        <%StringHelper.filterHTML(out, t6234.F04);%>
                                    </li>
                                    <%
                                        if (t6235s != null) {
                                            int i = 0;
                                            for (BidDywsx t6235 : t6235s) {
                                                if (t6235 == null) {
                                                    continue;
                                                }
                                    %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><%
                                        StringHelper.filterHTML(out, t6235.name);%>：</span>
                                        <%StringHelper.filterHTML(out, t6235.F04); %>
                                    </li>
                                    <%
                                                i++;
                                            }
                                        }
                                    %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <a href="<%=controller.getURI(request, ViewDyw.class) %>?loanId=<%=loanId %>&userId=<%=userId %>"
                                           class="btn-blue2 btn white radius-6 pl20 pr20">返回</a>
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