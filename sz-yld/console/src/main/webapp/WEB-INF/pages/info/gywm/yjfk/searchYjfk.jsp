<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6195_EXT" %>
<%@ page import="com.dimeng.p2p.console.servlets.info.gywm.yjfk.SearchYjfk" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6195_F08" %>
<%@ page import="com.dimeng.p2p.console.servlets.info.gywm.yjfk.UpdateYjfk" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6195_F06" %>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.console.servlets.info.UpdateCategoryName"%>
<%@page import="com.dimeng.p2p.console.servlets.info.gywm.yjfk.ExportYjfk"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "YJFK";
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    String publishTimeStart = request.getParameter("publishTimeStart");
    String publishTimeEnd = request.getParameter("publishTimeEnd");
    PagingResult<T6195_EXT> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ArticleManage manage = serviceSession.getService(ArticleManage.class);
    T5010 category = manage.getArticleTypeByCode("YJFK");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchYjfk.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i><%=category.F03 %>
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">回复状态：</span>
                                        <select name="replyStatus" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T6195_F06.yes%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("replyStatus")) && request.getParameter("replyStatus").equals(T6195_F06.yes.name())){ %>selected="selected"<%} %>><%=T6195_F06.yes.getChineseName()%>
                                            </option>
                                            <option value="<%=T6195_F06.no%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("replyStatus")) && request.getParameter("replyStatus").equals(T6195_F06.no.name())){ %>selected="selected"<%} %>><%=T6195_F06.no.getChineseName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">发布状态：</span>
                                        <select name="publishStatus" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T6195_F08.WFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("publishStatus")) && request.getParameter("publishStatus").equals(T6195_F08.WFB.name())){ %>selected="selected"<%} %>><%=T6195_F08.WFB.getChineseName()%>
                                            </option>
                                            <option value="<%=T6195_F08.YFB%>"
                                                    <%if(! StringHelper.isEmpty(request.getParameter("publishStatus")) && request.getParameter("publishStatus").equals(T6195_F08.YFB.name())){ %>selected="selected"<%} %>><%=T6195_F08.YFB.getChineseName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">反馈时间：</span>
                                        <input type="text" name="createTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="createTimeEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">发布时间：</span>
                                        <input type="text" name="publishTimeStart" readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publishTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="publishTimeEnd" readonly="readonly" id="datepicker4"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publishTimeEnd"));%>"/>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                     <%if (dimengSession.isAccessableResource(SearchYjfk.class)) {%>
                                        <a href="javascript:void(0)" onclick="openUpdateTitle();"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">设置</a>
                                      <%} else { %>
                                       <span class="btn btn-gray radius-6 mr5 pl10 pr10">设置</span>
                                       <%} %>
                                    </li>
                                    <li>
                	                <%if (dimengSession.isAccessableResource(ExportYjfk.class)) {%>
                     	              <a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                     	            <%}else{%>
                     	              <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                     	            <%}%>	
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号</th>
                                    <th>用户名</th>
                                    <th>反馈内容</th>
                                    <th>反馈时间</th>
                                    <th>回复状态</th>
                                    <th>回复人</th>
                                    <th>发布状态</th>
                                    <th>发布时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    T6195_EXT[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (T6195_EXT record : records) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><%=index %>  </td>
                                    <td><%=record.userName %>  </td>
                                    <td title="<%StringHelper.filterHTML(out, record.F03); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(record.F03, 30)); %></td>

                                    <td><%=DateTimeParser.format(record.F04) %>
                                    <td><%StringHelper.filterHTML(out, record.F06.getChineseName()); %></td>
                                    <td><%StringHelper.filterHTML(out, record.replyName); %></td>
                                    <td><%StringHelper.filterHTML(out, record.F08.getChineseName()); %></td>
                                    <td><%=DateTimeParser.format(record.F09) %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateYjfk.class)) {%>
                                        <%if(record.F08==T6195_F08.WFB ){%>
                                            <a href="javascript:void(0)" onclick="showReplyDiv('<%=index%>')" class="link-blue mr20">回复</a>
                                        <%}else{%>
                                        <a class="disabled mr20">回复</a>
                                        <%}%>
                                        <%if(record.F08==T6195_F08.WFB ){%>
                                        <%if(record.F06==T6195_F06.yes ){%>
                                            <a href="javascript:void(0)" onclick="showFb('<%=record.F01 %>')" class="link-blue  ">发布</a>
                                        <%}else{%>
                                            <a class="disabled">发布</a>
                                        <%}}else{%>
                                         <a href="javascript:void(0)" onclick="showXj('<%=record.F01 %>')" class="link-blue  ">下架</a>
                                        <%}%>
                                        <%} else { %>
                                            <a class="disabled">回复</a>
                                        <%if(record.F08==T6195_F08.WFB){%>
                                            <a class="disabled">发布</a>
                                        <%}else{%>
                                          <a class="disabled">下架</a>
                                        <%}%>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                        index++;}
                                } else {
                                %>
                                <tr class="title">
                                    <td colspan="9" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%SearchYjfk.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
    
<div id="szmc" class="popup-box hide" style="width:500px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">设置名称</h3>
        <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
    	<div class="popup-content-container-2" style="max-height:550px;">
            <div class="p30">
                <ul class="gray6">
					<li class="mb10">
					<span class="display-ib w100 tr mr5 fl">
						<span class="red">*</span>标题：
					</span>
					<input id="articleName" type="text" onblur="checkTitleName();"
                                  class="text border w150 pl5 required max-length-8 isInvalid" 
                                  name="articleName"
                                  value="<%StringHelper.filterHTML(out, category.F03);%>"/>
                                   <span id="tip" tip>不超过8个字</span>
                                   <span id="errortip" errortip class="" style="display: none"></span>
                          </li>
                          <li class="mb10">
								<span class="display-ib w100 tr mr5 fl">
									<span class="red">*</span>状态：
								</span>

                              <select id="articleStatus" name="articleStatus"  class="border mr20 h32 required w150">
                                      <%
                                    if (T5010_F04.values() != null && T5010_F04.values().length > 0) {
                                        for (T5010_F04 t5010_F04 : T5010_F04.values()) {
                                %>
                                <option value="<%=t5010_F04.name()%>"
                                        <%if(t5010_F04 ==category.F04) {%>selected="selected"<%} %>><%=t5010_F04.getChineseName() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                    </li>	
                    
                </ul>
				<input type="hidden" id="articleNameHidden" value="<%StringHelper.filterHTML(out, category.F03);%>" />
				<input type="hidden" id="articleStatusHidden" value="<%StringHelper.filterHTML(out, category.F04.name());%>" />
                <div class="tc f16 pt20">
                    <input type="button" onclick="updateNameLol();" value="确定" class="btn-blue2 btn white radius-6 pl20 pr20 "/>
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
                </div>
            </div>
    </div>
</div>


<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<%
    if (result != null) {
        for (int i = 0; i < result.getItems().length; i++) {
            T6195_EXT record = result.getItems()[i];
            if (record == null) {
                continue;
            }
%>
<div class="popup-box hide" id="reply_<%=i+1%>" style="min-height: 150px;width:570px;">
    <div class="popup-title-container">
        <h3 class="pl20 f18">回复</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeReplyDiv('<%=i+1%>');"></a>
    </div>
    <form action="<%=controller.getURI(request, UpdateYjfk.class)%>" id="replyForm_<%=i+1%>" method="post" class="form<%=i+1%>">
        <input type="hidden" name="F01" value="<%=record.F01%>"/>

        <div class="popup-content-container pt20 ob20 clearfix">
            <div class="gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5">反馈内容：</span>
                        <%StringHelper.filterHTML(out, record.F03); %>
                    </li>
                </ul>
            </div>
            <div class=" gray6">
                <ul>
                    <li class="mb10">
                        <textarea id="replyText_<%=i+1%>" name="F05" class="w500 h60 border p5 required min-length-2 max-length-100"><%StringHelper.filterHTML(out, record.F05); %></textarea>
                        <span tip></span>
                        <span errortip class="red" style="display: none"></span>
                    </li>
                </ul>
            </div>
            <div class="tc f16">
                <input type="submit" value="确定" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" fromname="form<%=i+1%>"/>
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeReplyDiv('<%=i+1%>')"/>
            </div>
        </div>
    </form>
</div>
<%
        }
    }
%>

<div class="popup-box hide" style="width:400px;min-height:200px;" id="fbDivId">
    <form action="<%=controller.getURI(request, UpdateYjfk.class) %>" id="fbForm" method="post" class="form1">
        <input type="hidden" name="F01" id="fb_id"/>
        <input type="hidden" name="F08" value="<%=T6195_F08.YFB.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定将该反馈信息发布到前台吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button3" href="javascript:$('#fbForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>
<div class="popup-box hide" style="width:400px;min-height:200px;" id="xjDivId">
    <form action="<%=controller.getURI(request, UpdateYjfk.class) %>" id="xjForm" method="post" class="form1">
        <input type="hidden" name="F01" id="xj_id"/>
        <input type="hidden" name="F08" value="<%=T6195_F08.WFB.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定下架该反馈信息吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button4" href="javascript:$('#xjForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(publishTimeStart)){%>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("publishTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(publishTimeEnd)){%>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("publishTimeEnd"));%>");
        <%}%>
        $("#datepicker5").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });
    function onSubmit() {
        $("#form1").submit();
    }
    //show回复DIV
    function showReplyDiv(index){
    	$("textarea[name='F05']").val("");
    	$("span[errortip]").hide();
    	
        $(".popup_bg").show();
        $("#reply_"+index).show();
    }
    //close回复DIV
    function closeReplyDiv(index){
        $(".popup_bg").hide();
        $("#reply_"+index).hide();
    }
    //发布操作
    function showFb(id) {
        $(".popup_bg").show();
        $("#fb_id").attr("value", id);
        $("#fbDivId").show();
    }

    //下架操作
    function showXj(id) {
        $(".popup_bg").show();
        $("#xj_id").attr("value", id);
        $("#xjDivId").show();
    }
    
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportYjfk.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, SearchYjfk.class)%>";
    }
    
    var upateUrl = '<%=controller.getURI(request, UpdateCategoryName.class)%>';
    var reloadUrl = '<%=controller.getURI(request, SearchYjfk.class)%>';
    function updateNameLol(){
        updateName('YJFK');
        //保存成功，修改左菜单标题
        parent.frames["leftFrame"].window.showGYWMMenu("YJFK");
    }
    
    function showSzmc(){
    	$("#szmc").show();
    	$(".popup_bg").show();
    }
</script>
</body>
</html>