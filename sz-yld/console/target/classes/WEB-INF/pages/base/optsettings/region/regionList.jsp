<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.RegionList"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.DisableAll" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.EnableAll" %>
<%@page import="com.dimeng.p2p.S50.enums.T5019_F11" %>
<%@page import="com.dimeng.p2p.S50.enums.T5019_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.Disable" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.Enable" %>
<%@page import="com.dimeng.p2p.S50.entities.T5019" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "QYGL";
    PagingResult<T5019> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T5019[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <form id="form1" action="<%=controller.getURI(request, RegionList.class)%>" method="post">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>区域管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">名称：</span><input type="text" name="name"
                                                                                      class="text border pl5 mr20"
                                                                                      value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">类型：</span>
                                        <select name="type" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5019_F11 type : T5019_F11.values()) {
                                            %>
                                            <option value="<%=type.name()%>" <%if (type.name().equals(request.getParameter("type"))) {%>
                                                    selected="selected" <%}%>><%=type.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">状态：</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5019_F13 status : T5019_F13.values()) {
                                            %>
                                            <option value="<%=status.name()%>" <%if (status.name().equals(request.getParameter("status"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle search-icon " name="input"></i>搜索</a>
                                        <%if (dimengSession.isAccessableResource(EnableAll.class)) {%>
                                        <a href="<%=controller.getURI(request, EnableAll.class)%>"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">全部启用</a>
                                        <%}else{ %>
                                        <span class="btn btn-gray radius-6 mr5 pl10 pr10">全部启用</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DisableAll.class)) {%>
                                        <a href="<%=controller.getURI(request, DisableAll.class)%>"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10 click-link">全部停用</a>
                                        <%}else{ %>
                                        <span class="btn btn-gray radius-6 mr5 pl10 pr10">全部停用</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th><input type="checkbox" id="checkAll" class="mr5"/></th>
                                    <th>序号</th>
                                    <th>名称</th>
                                    <th>省级名称</th>
                                    <th>市级名称</th>
                                    <th>县级名称</th>
                                    <th>电话区号</th>
                                    <th>级别</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (entityArray != null && entityArray.length != 0) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            T5019 t5019 = entityArray[i];
                                            if (t5019 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><input type="checkbox" name="zqId" value="<%=t5019.F01%>" onclick="cancelAllSel(this);"/></td>
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t5019.F05);%></td>
                                    <td><%StringHelper.filterHTML(out, t5019.F06);%></td>
                                    <td><%StringHelper.filterHTML(out, t5019.F07);%></td>
                                    <td><%StringHelper.filterHTML(out, t5019.F08);%></td>
                                    <td><%=t5019.F09%>
                                    </td>
                                    <td><%=t5019.F11.getChineseName() %>
                                    </td>
                                    <td><%=t5019.F13.getChineseName() %>
                                    </td>
                                    <td>
                                        <%if (t5019.F13 == T5019_F13.TY) { %>
                                        <%if (dimengSession.isAccessableResource(Enable.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,Enable.class)%>?id=<%=t5019.F01%>">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(Disable.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,Disable.class)%>?id=<%=t5019.F01%>">停用</a></span>
                                        <%} else { %>
                                        <span class="disabled">停用</span>
                                        <%} %>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="10">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                            <div class="mt10">
                            	<%if (dimengSession.isAccessableResource(Enable.class)) {%>
                                	<input type="button" style="cursor: pointer;" class="btn btn-blue2 radius-6 mr5 pl10 pr10" value="批量启用" 
                                		onclick="batchPlcz(true)"/>
								<%}else{ %>
									<span class="btn btn-gray radius-6 mr5 pl10 pr10">批量启用</span>
								<%} %>
								<%if (dimengSession.isAccessableResource(Disable.class)) {%>
                                	<input type="button" style="cursor: pointer;"
                                       class="btn btn-blue2 radius-6 mr5 pl10 pr10" value="批量停用"
                                       onclick="batchPlcz(false)"/>
	                            <%}else{ %>
	                            	<span class="btn btn-gray radius-6 mr5 pl10 pr10">批量停用</span>
	                            <%} %>
                            </div>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $("#checkAll").click(function () {
        if ($(this).attr("checked")) {
            $("input:checkbox[name='zqId']").attr("checked", true);
        } else {
            $("input:checkbox[name='zqId']").attr("checked", false);
        }
    });

    //		$("input:checkbox[name='zqId']").click(fucntion(){
    //			var[] zqIdCheckBox =
    //		});
    // 批量操作
    function batchPlcz(flag) {
        var ckeds = $("input:checkbox[name='zqId']:checked");
        if (ckeds == null || ckeds.length <= 0) {
        	$("#info").html(showDialogInfo("请选择要处理的记录!", "wrong"));
            $("div.popup_bg").show();
            return;
        }

        var ids = "";
        for (var i = 0; i < ckeds.length; i++) {
            var val = $(ckeds[i]).val();
            if (i == 0) {
                ids += val;
            } else {
                ids += "," + val;
            }
        }

        var message =  "确认批量" + (flag ? "启用" : "停用") + "？" ;
        var url = flag ? "<%=controller.getURI(request,Enable.class)%>?ids=" + ids : "<%=controller.getURI(request,Disable.class)%>?ids=" + ids;
        $("#info").html(showForwardInfo(message,"question",url));
        $("div.popup_bg").show();
        
        <%-- if (confirm("确认批量" + (flag ? "启用" : "停用") + "？")) {
            location.href = flag ? "<%=controller.getURI(request,Enable.class)%>?ids=" + ids
                    : "<%=controller.getURI(request,Disable.class)%>?ids=" + ids;
        } else {
            return;
        } --%>
    }

    function onSubmit() {
        $("#form1").submit();
    }
    
    function cancelAllSel(obj){
    	if(!$(obj).is(':checked')){
			$("#checkAll").attr("checked",false);
		}else{
			var c1 = $("input:checkbox[name='zqId']:checked").length;
			var c2 = $("input:checkbox[name='zqId']").length;
			if(c1==c2){
				$("#checkAll").attr("checked",true);
			}
		}
    }
</script>
</body>
</html>