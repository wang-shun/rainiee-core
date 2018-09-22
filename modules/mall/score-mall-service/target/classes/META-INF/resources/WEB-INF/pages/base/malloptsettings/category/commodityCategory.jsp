<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.CommodityCategory" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreComdCategoryExt" %>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F04" %>
<%@page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.AddCommodityCategory" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.UpdateCommodityCategory" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.UpdateCommodityCategoryStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.category.CheckCategoryExists" %>
<%@ page import="com.dimeng.p2p.S63.enums.T6350_F07" %>


<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
CURRENT_SUB_CATEGORY = "SPLBSZ";
PagingResult<ScoreComdCategoryExt> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                    <div class="p20">
                      <form id="form1" action="<%=controller.getURI(request, CommodityCategory.class)%>" method="post">
                        <div class="border">
                        	<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>商品类别设置</div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">商品类别</span>
                                        <select name="F02" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                if(null != t6350List){
                                                                                for (T6350 t6350 : t6350List) {
                                            %>
                                            <option value="<%=t6350.F02%>" <%if (t6350.F02.equals(request.getParameter("F02"))) {%>
                                                    selected="selected" <%}%>><%=t6350.F03%>
                                            </option>
                                            <%
                                                }}
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">属性</span>
                                        <select name="F07" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6350_F07 prop : T6350_F07.values()) {
                                            %>
                                            <option value="<%=prop.name()%>" <%if (prop.name().equals(request.getParameter("F07"))) {%>
                                                    selected="selected" <%}%>><%=prop.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                   <li><span class="display-ib mr5">状态</span>
                                        <select name="F04" class="border mr10 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6350_F04 state : T6350_F04.values()) {
                                            %>
                                            <option value="<%=state.name()%>" <%if (state.name().equals(request.getParameter("F04"))) {%>
                                                    selected="selected" <%}%>><%=state.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li>
                                      <a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15">
                                        <i class="icon-i w30 h30 va-middle search-icon "></i>搜索
                                      </a>
                                    </li>
                                    <li>
                                    	<%if(dimengSession.isAccessableResource(AddCommodityCategory.class)){ %>
	                                      	<a href="javascript:void(0);" onclick="add();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle add-icon "></i>新增类别</a>
                                        <%}else{ %>
                                        	<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle add-icon "></i>新增类别</span>
                                        <%} %>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>类别编码</th>
                                    <th>商品类别</th>
                                    <th>状态</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                              <%
                                  if (result != null && result.getItemCount() > 0) {
                                                              ScoreComdCategoryExt[] lists = result.getItems();
                                                              if (lists != null) {
                                                                  int index = 1;
                                                                  for (ScoreComdCategoryExt scoreComdCategoryExt : lists) {
                              %>
                                <tr class="tc">
                                    <td><%=index++%></td>
                                    <td><%
                                        StringHelper.filterHTML(out, scoreComdCategoryExt.F02);
                                    %></td>
                                    <td><%
                                        StringHelper.filterHTML(out, scoreComdCategoryExt.F03);
                                    %></td>
                                    <td><%
                                        StringHelper.filterHTML(out, scoreComdCategoryExt.F04.getChineseName());
                                    %></td>
                                    <td><%
                                        StringHelper.filterHTML(out, scoreComdCategoryExt.createName);
                                    %></td>
                                    <td><%=DateTimeParser.format(scoreComdCategoryExt.F06)%></td>
                                    <td>
                                    <span>
							             	<%
                                                if(T6350_F07.kind == scoreComdCategoryExt.F07)
                                                {
							             	    if (dimengSession.isAccessableResource(UpdateCommodityCategory.class)) {
							             	%>
							              	<a href="javascript:void(0);"
                                               class="link-blue mr20" onclick="updateCommodityCategory('<%=controller.getURI(request,UpdateCommodityCategory.class)%>',<%=scoreComdCategoryExt.F01%>,'<%=scoreComdCategoryExt.F02%>','<%=scoreComdCategoryExt.F03%>','<%=scoreComdCategoryExt.F07.name()%>');">修改</a>
							              	<%
							              	    } else {
							              	%>
						                   	<span class="disabled">修改</span>
						                   	<%
						                   	    }
                                                }
						                   	%>
					                   	</span>
                                        <%
                                            if (scoreComdCategoryExt.F04 == T6350_F04.off) {
                                        %>
                                        <%
                                            if (dimengSession.isAccessableResource(UpdateCommodityCategoryStatus.class)) {
                                        %>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,UpdateCommodityCategoryStatus.class)%>?F01=<%=scoreComdCategoryExt.F01%>&F04=on">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(UpdateCommodityCategoryStatus.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,UpdateCommodityCategoryStatus.class)%>?F01=<%=scoreComdCategoryExt.F01%>&F04=off">停用</a></span>
                                        <%} else { %>
                                        <span class="disabled">停用</span>
                                        <%} %>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="7">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->
                      </form>
                    </div>
            </div>
        </div>
    </div>
<div id="addCategory" style="display: none;">
    <div id="addCategory1" class="popup-box" style="min-height: 270px;min-width:630px;">
        <form action="<%=controller.getURI(request, AddCommodityCategory.class)%>" onsubmit="return addCategorySubmit();" method="post">
            <div class="popup-title-container">
                <h3 class="pl20 f18">新增类别</h3>
                <a class="icon-i popup-close2" href="javascript:void(0);"
                   onclick="javascript:document.getElementById('addCategory').style.display='none';document.getElementById('addCategory_popup').style.display='none';return false;"></a>
            </div>
            <div class="popup-content-container pt20 ob20 clearfix" >
               <%--<div class="mb20 gray6">
                    <span class="display-ib tr mr5">&nbsp;</span>
                    <input class="ml80" type="radio" value="kind" name="F07" checked="checked"/>实物
                    <input class="ml50" type="radio" value="fee" name="F07"/>话费
                </div>--%>
                <div class="mb20 gray6">
                    <span class="display-ib tr mr5">
					  <span class="red">*</span>类别编码：
					</span>
                    <input id="add_f02" name="F02" onblur="checkF02(this,0);" onfocus="clearErroInfo(this);" maxlength="10" type="text" class="text border w250 yw_w5"/>
                    <span tip>请输入2~10位字母+数字的组合</span>
                    <span errortip class="" style="display: none"></span>
                </div>
                <div class="mb20 gray6">
                    <span class="display-ib tr mr5">
					  <span class="red">*</span>商品类别：
					</span>
                    <input id="add_f03" name="F03" maxlength="30" onblur="checkF03(this,0);" onfocus="clearErroInfo(this);" type="text" class="text border w250 yw_w5 required required min-length-2"/>
                    <span tip>请输入长度2~30位字符</span>
                    <span errortip class="" style="display: none"></span>
                </div>
                <div class="tc f16">
                    <input type="submit" class="btn-blue2 btn white radius-6 pl20 pr20" value="提交" />
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="javascript:document.getElementById('addCategory').style.display='none';document.getElementById('addCategory_popup').style.display='none';return false;">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="javascript:document.getElementById('addCategory').style.display='none';document.getElementById('addCategory_popup').style.display='none';return false;">
                </div>
            </div>
        </form>
    </div>
</div>
<div id="updateCategory"></div>
<div id="info"></div>
<div id="addCategory_popup" class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING); 
	if(!StringHelper.isEmpty(message)) {
%>
		<script type="text/javascript">
		$("div.popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"wrong"));
		</script>
<%} %>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%}%>
<script type="text/javascript">
var _nURL = '<%=controller.getURI(request, CheckCategoryExists.class)%>';
function add(){
	$(".popup_bg").show();
	$("#addCategory1").show();
	$("#addCategory").show();
}
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/commodityCategory.js"></script>
</body>
</html>