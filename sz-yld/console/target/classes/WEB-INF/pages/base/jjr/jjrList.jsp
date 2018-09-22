<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S51.enums.T5128_F05" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.UpdateJjr" %>
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.AddJjr" %>
<%@page import="com.dimeng.p2p.S51.entities.T5128" %>
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.JjrList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.UpdateJjrStatus" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "JJRSZ";
    PagingResult<T5128> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T5128[] entityArray = (result == null ? null : result.getItems());
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="w_main">
    <div class="main clearfix">
        <div class="wrap">
            <div class="r_main">
                <div class="home_main">
                    <div class="box box1 mb15">
                        <div class="atil">
                            <h3>节假日管理</h3>
                        </div>
                        <div class="con">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td height="40">
                                        <%if (dimengSession.isAccessableResource(AddJjr.class)) {%>
                                        <a href="<%=controller.getURI(request, AddJjr.class)%>" class="btn3 mr10"><span
                                                class="ico3"></span>新增</a>
                                        <%} else { %>
                                        <span class="btn3 mr10 btn5"><span class="ico1"></span>新增</span>
                                        <%} %>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="clear"></div>
                    </div>

                    <form id="form1" action="<%=controller.getURI(request, JjrList.class)%>" method="post">
                        <div class="newsbox">
                            <table width="100%" border="0" cellspacing="0" cellpadding="3" class="yhgl_table tc">
                                <tr class="hsbg">
                                    <td>序号</td>
                                    <td>节假日名称</td>
                                    <td>节假日日期</td>
                                    <td>节假日天数</td>
                                    <td>状态</td>
                                    <td>操作</td>
                                </tr>
                                <%
                                    if (entityArray != null) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            T5128 t5128 = entityArray[i];
                                            if (t5128 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="dhsbg">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%=t5128.F02%>
                                    </td>
                                    <td><%=DateParser.format(t5128.F03)%>
                                    </td>
                                    <td><%=t5128.F04%>
                                    </td>
                                    <td><%=t5128.F05.getChineseName()%>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateJjr.class)) {%>
                                        <span class="blue"><a
                                                href="<%=controller.getURI(request,UpdateJjr.class)%>?id=<%=t5128.F01%>">修改</a></span>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(UpdateJjrStatus.class)) {%>
                                        <%if (T5128_F05.QY == t5128.F05) {%>
		              	<span class="blue">
		              		<a href="<%=controller.getURI(request,UpdateJjrStatus.class)%>?F01=<%=t5128.F01%>&F05=<%=T5128_F05.TY%>">停用</a>
		              	</span>
                                        <%} else { %>
                                        <span class="blue"><a
                                                href="<%=controller.getURI(request,UpdateJjrStatus.class)%>?F01=<%=t5128.F01%>&F05=<%=T5128_F05.QY%>">启用</a></span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (T5128_F05.QY == t5128.F05) {%>
                                        <span class="disabled">停用</span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </table>
                            <%
                                AbstractConsoleServlet.rendPagingResult(out, result);
                            %>
                            <div class="clear"></div>
                        </div>
                    </form>
                    <div class="box2 clearfix"></div>
                </div>
            </div>
        </div>

        <%@include file="/WEB-INF/include/left.jsp" %>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>