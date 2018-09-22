<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.LetterList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.ExportLetter" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.Letter" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/kindeditor.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    Letter letter = ObjectHelper.convert(
            request.getAttribute("letter"), Letter.class);
    if (letter == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "ZNXTG";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容 开始-->
                <div class="p20">
                    <!--切换栏目-->
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>站内信推广</div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>管理员：</span>

                                        <div class="pl200"><%StringHelper.filterHTML(out, letter.name);%></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>创建时间：</span>

                                        <div class="pl200"><%=DateTimeParser.format(letter.createTime)%>
                                        </div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>发送数量：</span>

                                        <div class="pl200"><%=letter.count%>
                                        </div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>用户名：</span>

                                        <div class="pl200 clearfix"><textarea name="userName" id="textarea" cols="45"
                                                                              rows="5" readonly="readonly"
                                                                              class="ww50 h120 border p5 fl"><%
                                            if (letter.userNames != null) {
                                                for (String s : letter.userNames) {
                                        %><%=s + "\n"%><%
                                                }
                                            }
                                        %></textarea>
                                            <a href="<%=controller.getURI(request, ExportLetter.class)%>?id=<%=letter.id%>"
                                               class="btn-blue2 btn white radius-6 pl20 pr20 ml20 fl">导出</a></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>站内信标题：</span>

                                        <div class="pl200"><%StringHelper.filterHTML(out, letter.title);%></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>站内信内容：</span>

                                        <div class="pl200"><textarea name="content" readonly="readonly" cols="80"
                                                                     rows="9"
                                                                     style="width: 80%; height: 200px; visibility: hidden;"><%StringHelper.filterHTML(out, letter.content);%></textarea>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200"><input type="button" value="返回"
                                                                  class="btn btn-blue2 radius-6 pl20 pr20"
                                                                  onclick="window.location.href='<%=controller.getURI(request, LetterList.class)%>'">
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<script type="text/javascript">
    KindEditor.ready(function (K) {
        var editor1 = K.create('textarea[name="content"]', {
            readonlyMode: true,
            allowPreviewEmoticons: false,
            allowImageUpload: false,
            items: [
                'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'link'],
        });
        prettyPrint();
    });
</script>
</body>
</html>