<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.JjrList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.jjr.AddJjr" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DJSZ";
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="w_main">
    <div class="main clearfix">
        <div class="wrap">
            <div class="r_main">
                <div class="home_main">
                    <div class="box box1 mb15">
                        <div class="atil">
                            <h3>新增节假日</h3>
                        </div>
                        <div class="con">
                            <%
                                String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
                                if (!StringHelper.isEmpty(ermsg)) {
                            %>
                            <ul class="xx_li" id="kjd">
                                <li style="text-align: center;color: red;">
                                    <%StringHelper.filterHTML(out, ermsg);%>
                                </li>
                            </ul>
                            <%}%>
                            <div class="clear"></div>
                            <form action="<%=controller.getURI(request, AddJjr.class) %>" onsubmit="return check()"
                                  method="post" class="form1">
                                <ul class="cell yw_jcxx">
                                    <li>
                                        <div class="til"><span class="red">*</span>节假日名称：</div>
                                        <div class="info">
                                            <input type="text" name="F02"
                                                   class="text yhgl_ser required max-length-25"
                                                   value="<%=StringHelper.isEmpty(request.getParameter("F02")) ? "" : request.getParameter("F02")%>"/>

                                            <div class="clear"></div>
                                            <p tip>不能为空</p>

                                            <p errortip class="" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="til"><span class="red">*</span>节假日日期：</div>
                                        <div class="info">
                                            <input type="text" name="F03"
                                                   readonly="readonly" id="datepicker3"
                                                   class="yhgl_input mr5 date required"
                                                   value="<%=StringHelper.isEmpty(request.getParameter("F03")) ? "" : request.getParameter("F03")%>"/>

                                            <p id="endTimeError"></p>

                                            <div class="clear"></div>
                                            <p tip>不能为空</p>

                                            <p errortip class="" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="til"><span class="red">*</span>节假日天数：</div>
                                        <div class="info">
                                            <input name="F04" type="text" class="text yhgl_ser required" maxlength="11"
                                                   mtest="/^[0-9]*$/" mtestmsg="必须为数字格式"
                                                   value="<%=StringHelper.isEmpty(request.getParameter("F04")) ? "" : request.getParameter("F04")%>"/>

                                            <div class="clear"></div>
                                            <p tip>不能为空</p>

                                            <p errortip class="" style="display: none"></p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="clear"></div>
                                    </li>
                                </ul>
                                <div class="tc w220 pt20">
                                    <input type="submit" class="btn4 mr30 sumbitForme" style="cursor: pointer;"
                                           fromname="form1" value="确认"/>
                                    <a href="<%=controller.getURI(request, JjrList.class) %>" class="btn4">返回</a>
                                </div>
                                <div class="clear"></div>
                            </form>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <div class="box2 clearfix"></div>
                </div>
            </div>
        </div>
        <%
            String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
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
        <%@include file="/WEB-INF/include/left.jsp" %>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>

<script type="text/javascript">
    $(function () {
        $("#datepicker3").datepicker({inline: true});
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
    });
</script>

</body>
</html>