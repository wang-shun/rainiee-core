<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.*" %>
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增/修改附件信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <%
                                    if (loanId > 0) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">项目信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%} %>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddUserInfoXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%} %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddEnterpriseXm.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">企业信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>

                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%
                                    }
                                %>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">附件(马赛克)<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form action="<%=controller.getURI(request, AddGuarantee.class)%>" method="post" id="form1"
                                  class="form1">
                                <input type="hidden" name="loanId" value="<%=loanId %>"/>
                                <input type="hidden" name="userId" value="<%=userId %>"/>

                                <div class="tab-item">
                                    <a href="javascript:void(0)"
                                       onclick="tcc(this,'FJ','<%=controller.getURI(request, AnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>','上传附件')"
                                       class="btn btn-blue radius-6 mr5 pl1 pr15 mb10"><i
                                            class="icon-i w30 h30 va-middle add-icon "></i>上传附件</a>
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
                                            <td class="tc"><%=i++%>
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
                                                    target="_blank" class="link-blue mr20">查看</a>&nbsp;<a
                                                    href="javaScript:void(0);"
                                                    onclick="onDelete(<%=loanId%>,<%=userId%>,<%=t6232.F01%>);" class="link-blue">删除</a></td>
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
                                                   String prevUrl = "";
					                                if (T6230_F11.S == t6230.F11) {
					                                    prevUrl = controller.getURI(request, AddGuaranteeXq.class);
					                                }else if (T6230_F13.S == t6230.F13){
				                                	    prevUrl = controller.getURI(request, AddDyw.class);
				                                	}else if (userType == T6110_F06.FZRR) {
                                                       prevUrl = controller.getURI(request, AddEnterpriseXm.class);
                                                   }else{
                                                       prevUrl = controller.getURI(request, ViewAuthentication.class);
                                                   }
                                                   
                                                   %>
                                	<a href="<%=prevUrl%>?loanId=<%=loanId%>&userId=<%=userId%>" class="btn btn-blue2 radius-6 pl20 pr20 ml10">上一步</a>
                                   	<a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme tc ml10">下一步</a>
                                     <a href="<%=controller.getURI(request, LoanList.class)%>" class="btn btn-blue2 radius-6 pl20 pr20 ml10">取消</a>
                                </div>
                            </form>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
                window.location.reload();
            }
        }, false);
    }
    function onDelete(loanId,userId,id) {
    	var url="<%=controller.getURI(request, DelAnnexMsk.class)%>?loanId="+loanId+"&userId="+userId+"&id="+id;
        $(".popup_bg").show();
        $("#info").html(showForwardInfo("你确认要删除该附件吗?","question",url));
    }
</script>
</body>
</html>