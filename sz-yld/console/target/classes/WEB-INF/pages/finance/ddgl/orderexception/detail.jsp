<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S65.entities.T6550" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    T6550 t6550 = ObjectHelper.convert(request.getAttribute("t6550"), T6550.class);
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "DDYCRZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>订单异常日志
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <%StringHelper.filterHTML(out, t6550.F03); %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</body>
</html>