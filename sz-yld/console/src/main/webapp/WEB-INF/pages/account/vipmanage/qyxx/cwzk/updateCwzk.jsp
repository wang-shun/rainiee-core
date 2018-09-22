<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6163" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    T6163[] entitys = ObjectHelper.convertArray(request.getAttribute("info"), T6163.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改企业信息
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">财务状况<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <form id="form1" action="<%=controller.getURI(request, UpdateCwzk.class)%>" method="post"
                                      class="form1">
                                    <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                    <input type="hidden" id="entryType" name="entryType" value="">
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>年份</th>
                                            <th style="width: 19%;">主营收入（万元）</th>
                                            <th style="width: 19%;">净利润（万元）</th>
                                            <th style="width: 19%;">总资产（万元）</th>
                                            <th style="width: 19%;">净资产（万元）</th>
                                            <th>备注</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            int i = 0;
                                            for (T6163 entity : entitys) {
                                        %>
                                        <tr class="tc">
                                            <td>
                                                <input type="hidden" value="<%=entity.F01 %>"
                                                       name="t6163s[<%=i%>].F01"/>
                                                <input type="hidden" value="<%=entity.F02 %>"
                                                       name="t6163s[<%=i%>].F02"/>
                                                <%=entity.F02 %>年
                                            </td>
                                            <td><input name="t6163s[<%=i%>].F03" type="text" maxlength="18"
                                                       class="text yhgl_ser w100 border" value="<%=entity.F03 %>" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>

                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </td>
                                            <td><input name="t6163s[<%=i%>].F05" type="text" maxlength="18"
                                                       class="text yhgl_ser w100 border isnum" value="<%=entity.F05 %>" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>

                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </td>
                                            <td><input name="t6163s[<%=i%>].F06" type="text" maxlength="18"
                                                       class="text yhgl_ser w100 border" value="<%=entity.F06 %>" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>

                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </td>
                                            <td><input name="t6163s[<%=i%>].F07" type="text" maxlength="18"
                                                       class="text yhgl_ser w100 border" value="<%=entity.F07 %>" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/" mtestmsg="必须为数字格式(且是两位小数)"/>

                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </td>
                                            <td><input name="t6163s[<%=i%>].F08" type="text" maxlength="200"
                                                       class="text yhgl_ser w100 border"
                                                       value="<%StringHelper.filterHTML(out, entity.F08); %>"/>

                                                <p tip></p>

                                                <p errortip class="" style="display: none"></p>
                                            </td>
                                        </tr>
                                        <%
                                                i++;
                                            }
                                        %>

                                        </tbody>
                                    </table>
                                    <div class="tc  pt20">
                                    	<input type="button"
                                                       onclick="window.location.href='<%=controller.getURI(request,UpdateJscl.class)%>?id=<%=request.getParameter("id")%>'"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 " value="上一步"/>
                                        <input style="display:none;" type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1"/>
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20" value="下一步" onclick="nextButton();"/>
                                        <input type="button" onclick="tj();" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                        <input type="button"
                                               onclick="window.location.href='<%=controller.getURI(request, QyList.class)%>'"
                                               class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    function checkNum(obj){
        var myreg = /^\d*(\d|(\.[0-9]{1,3}))$/;
        if ($.trim($(obj).val()) == "") {
            $(obj).val(0.00);
            return false;
        }
        if (!myreg.test($(obj).val())) {
            $(obj).val(0.00);
            return false;
        }
    }
    
    function nextButton(){
    	$('#entryType').val('');
        $('.sumbitForme').click();
    }
    function tj() {
        $('#entryType').val('submit');
        $('.sumbitForme').click();
    }
</script>
</body>
</html>