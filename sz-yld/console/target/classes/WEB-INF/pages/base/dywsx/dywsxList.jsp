<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S62.enums.T6214_F04" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywsx.TyDywsx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywsx.QyDywsx" %>
<%@page import="com.dimeng.p2p.S62.entities.T6213" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywsx.ViewDywsx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywsx.AddDywsx" %>
<%@page import="com.dimeng.p2p.S62.entities.T6214" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DYWSX";
    PagingResult<T6214> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T6214[] entityArray = (result == null ? null : result.getItems());
    T6213[] dywTypeArr = (T6213[]) request.getAttribute("dywType");
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<div class="w_main">
    <div class="main clearfix">
        <div class="wrap">
            <div class="r_main">
                <div class="home_main">
                    <div class="box box1 mb15">
                        <div class="atil">
                            <h3>抵押物属性管理</h3>
                        </div>
                    </div>
                    <form id="form1" action="<%=controller.getURI(request, DywsxList.class)%>" method="post">
                        <div class="box box1 mb15">
                            <div class="con">
                                <div class="admin_input admin_pb">
                                    <p>属性名称：<input type="text" name="f03" class="yhgl_input"
                                                   value="<%StringHelper.filterHTML(out, request.getParameter("f03"));%>"/>
                                    </p>

                                    <p>抵押物类型：
                                        <select name="f02" class="yhgl_sel">
                                            <option value="">全部</option>
                                            <%
                                                if (dywTypeArr != null) {
                                                    for (T6213 dywType : dywTypeArr) {
                                                        if (dywType == null) {
                                                            continue;
                                                        }
                                                        String f02str = dywType.F01 + "";
                                            %>
                                            <option value="<%=dywType.F01%>" <%if (f02str.equals(request.getParameter("f02"))) {%>
                                                    selected="selected" <%}%>><%
                                                StringHelper.filterHTML(out, dywType.F02);
                                            %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </p>
                                    <p><input name="input" type="submit" class="btn2" value="搜索"/></p>

                                    <p>
                                        <%
                                            if (dimengSession.isAccessableResource(AddDywsx.class)) {
                                        %>
                                        <%
                                            if (dywTypeArr == null || dywTypeArr.length == 0) {
                                        %>
                                        <a href="javascript:void(0)" name="input"
                                           onclick="$('#dialog_span').css('display', 'block')" class="btn3 "><span
                                                class="ico1"></span>新增</a>
                                        <%
                                        } else {
                                        %>
                                        <a href="javascript:void(0)" name="input"
                                           onclick="window.location.href='<%=controller.getURI(request, AddDywsx.class)%>'"
                                           class="btn3 "><span class="ico1"></span>新增</a>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <span class="btn3 mr10 btn5"><span class="ico1"></span>新增</span>
                                        <%
                                            }
                                        %>
                                    </p>
                                    <span id="dialog_span" style="display:none"><font color=red>缺少抵押物类型!</font></span>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>

                        <div class="newsbox">
                            <table width="100%" border="0" cellspacing="0" cellpadding="3" class="yhgl_table tc">
                                <tr class="hsbg">
                                    <td>序号</td>
                                    <td>属性名称</td>
                                    <td>抵押物类型</td>
                                    <td>状态</td>
                                    <td>操作</td>
                                </tr>
                                <%
                                    if (entityArray != null) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            T6214 t6214 = entityArray[i];
                                            if (t6214 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="dhsbg">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%
                                        StringHelper.filterHTML(out, t6214.F03);
                                    %></td>
                                    <td>
                                        <%
                                            if (dywTypeArr != null) {
                                                for (T6213 dywType : dywTypeArr) {
                                                    if (dywType == null) {
                                                        continue;
                                                    }
                                                    if (t6214.F02 == dywType.F01) {
                                        %>
                                        <%
                                            StringHelper.filterHTML(out, dywType.F02);
                                        %>
                                        <%
                                                    }
                                                }
                                            }
                                        %>
                                    </td>
                                    <td><%=t6214.F04.getChineseName() %>
                                    </td>
                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewDywsx.class)) {
                                        %>
                                        <span class="blue"><a
                                                href="<%=controller.getURI(request,ViewDywsx.class)%>?id=<%=t6214.F01%>">修改</a></span>
                                        <%
                                        } else {
                                        %>
                                        <span class="disabled">修改</span>
                                        <%
                                            }
                                        %>

                                        <% if (t6214.F04 == T6214_F04.TY) {
                                            if (dimengSession.isAccessableResource(QyDywsx.class)) {
                                        %>
                                        <span class="blue"><a
                                                href="<%=controller.getURI(request,QyDywsx.class)%>?id=<%=t6214.F01%>">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <%
                                            if (dimengSession.isAccessableResource(TyDywsx.class)) {
                                        %>
                                        <span class="blue"><a
                                                href="<%=controller.getURI(request,TyDywsx.class)%>?id=<%=t6214.F01%>">停用</a></span>
                                        <%} else { %>
                                        <span class="disabled">停用</span>
                                        <%
                                                }
                                            }
                                        %>
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