<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.UpdateJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbzz.UpdateDbzz" %>
<%@page import="com.dimeng.p2p.S61.entities.T6180" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "JG";
    //T6180 t6180 = ObjectHelper.convert(request.getAttribute("info"), T6180.class );
    String content = (String) request.getAttribute("info");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改机构信息
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">机构信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn  select-a">担保情况<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <form id="form1" action="<%=controller.getURI(request, UpdateDbzz.class)%>" method="post"
                                      class="form1">
                                    <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                    <input type="hidden" id="entryType" name="entryType" value="">
                                    <ul class="gray6">
                                        <li class="mb10">

                                            <span class="display-ib w200 tr mr5 fl"><span
                                                    class="red"></span>担保情况描述：</span>

                                            <div class="pl200 ml5">
                                                <textarea name="dbzz" cols="100" rows=""
                                                          class="area required max-length-255"><%StringHelper.filterHTML(out, content); %></textarea>
                                                <span tip></span>
                                                <span errortip class="" style="display: none"></span>
                                            </div>
                                            <div class="clear"></div>
                                        </li>


                                        <%--
                                        cols="100" rows="8" style="width:700px;height:500px;visibility:hidden;" class="required min-length-20 max-length-500">
                                        <%StringHelper.format(out, t6180.F02, fileStore); %>

                                       <li>
                                            <div class="til"><span class="red">*</span>经营情况：</div>
                                            <div class="info">
                                                 <textarea name="F03" cols="" rows="" class="area required max-length-500"><%StringHelper.format(out,entity.F03, fileStore); %></textarea>
                                                <p tip></p>
                                                <p errortip class="" style="display: none"></p>
                                            </div>
                                            <div class="clear"></div>
                                        </li>
                                         <li>
                                            <div class="til"><span class="red">*</span>涉诉情况：</div>
                                            <div class="info">
                                                 <textarea name="F04" cols="" rows="" class="area required max-length-255"><%StringHelper.format(out,entity.F04, fileStore); %></textarea>
                                                <p tip></p>
                                                <p errortip class="" style="display: none"></p>
                                            </div>
                                            <div class="clear"></div>
                                        </li>
                                         <li>
                                            <div class="til"><span class="red">*</span>征信情况：</div>
                                            <div class="info">
                                                 <textarea name="F05" cols="" rows="" class="area required max-length-255"><%StringHelper.format(out, entity.F05, fileStore); %></textarea>
                                                <p tip></p>
                                                <p errortip class="" style="display: none"></p>
                                            </div>
                                            <div class="clear"></div>
                                        </li>
                                        <li>
                                          <div class="info"><input type="hidden" name="F01" value="<%=entity.F01 %>"/></div>
                                        </li>
                                        <li>
                                          <div class="clear"></div>
                                        </li>
                                         --%>

                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                            	<input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request,UpdateJgxx.class)%>?id=<%=request.getParameter("id")%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 " value="上一步"/>
                                                <input style="display:none;" type="submit"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1"/>
                                                <input type="button"
                                                       class="btn btn-blue2 radius-6 pl20 pr20"
                                                       value="下一步" onclick="nextButton();"/>
                                                <input type="button" onclick="tj();"  class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                       onclick="window.location.href='<%=controller.getURI(request, JgList.class)%>'" value="取消"/>
                                                       
                                             </div>
                                        </li>


                                    </ul>
                                </form>
                            </div>
                            <div class="mb15"></div>
                            <div class="box2 clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%
            String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
            if (!StringHelper.isEmpty(warringMessage)) {
        %>
        <div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
            <div class="info clearfix">
                <div class="clearfix">
                    <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
                </div>
                <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                               class="btn4 ml50"/></div>
            </div>
        </div>
        <%} %>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>

<script type="text/javascript">
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="dbzz"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
    });
    
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