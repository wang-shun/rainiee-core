<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.tzyhk.SearchTzyhkWt" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.common.enums.NoticePublishStatus" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionRecord" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.tzyhk.UpdateTzyhkWt" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
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
    CURRENT_SUB_CATEGORY = "TZYHK";
    QuestionRecord record = ObjectHelper.convert(request.getAttribute("record"), QuestionRecord.class);
    ArticleManage manage = serviceSession.getService(ArticleManage.class);
    QuestionTypeRecord[] questionTypes = manage.getQuestionTypes(T5011_F02.TZYHK);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改投资与回款问题
                        </div>
                        <div class="content-container pl40 pr40 pb20">
                            <form name="example" method="post"
                                  action="<%=controller.getURI(request, UpdateTzyhkWt.class)%>" class="form1" onsubmit="return onSubmit();">
                                <input type="hidden" value="<%=record.id %>" name="id"/>
                                <ul class="gray6">
                                    <li class="mb10 red">
                                        <span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>问题：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name=question
                                               value="<%StringHelper.filterHTML(out, record.question);%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>问题类型：</span>
                                        <select name="questionTypeID" class="border mr20 h32">
                                            <%
                                                if (questionTypes != null && questionTypes.length > 0) {
                                                    for (QuestionTypeRecord qtr : questionTypes) {
                                            %>
                                            <option value="<%=qtr.id%>"
                                                    <%if(record.questionTypeID == qtr.id){ %>selected="selected"<%} %>>
                                                <%StringHelper.filterHTML(out, qtr.questionType);%>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>是否发布：</span>
                                        <select name="publishStatus" class="border mr20 h32">
                                            <%String publishStatus = record.publishStatus.name(); %>
                                            <option value="<%=NoticePublishStatus.WFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.WFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=NoticePublishStatus.YFB%>"
                                                    <%if(!StringHelper.isEmpty(publishStatus) && publishStatus.equals(NoticePublishStatus.YFB.name())){ %>selected="selected" <%} %>><%=NoticePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>问题答案：</span>
                                        <textarea name="questionAnswer" class=""
                                                  cols="100" rows="8"
                                                  style="width:700px;height:400px;"><%StringHelper.format(out, record.questionAnswer, fileStore); %></textarea>
                                        <br/>
                                        <span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        <span tip>最大300个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="返回" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchTzyhkWt.class) %>'">
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="questionAnswer"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("textarea[name='questionAnswer']").nextAll("span[errortip]").addClass("red").html("问题答案不能为空！").show();
                    $("textarea[name='questionAnswer']").nextAll("span[tip]").hide();
                } else {
                    $("textarea[name='questionAnswer']").nextAll("span[errortip]").removeClass("red").html("&nbsp;").hide();
                    $("textarea[name='questionAnswer']").nextAll("span[tip]").show();
                }
            },
            afterChange: function () {
                var maxNum = 300, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });

    function onSubmit() {
        if (editor1.count('text') == '') {
            $("textarea[name='questionAnswer']").nextAll("span[errortip]").addClass("red").html("问题答案不能为空！").show();
            $("textarea[name='questionAnswer']").nextAll("span[tip]").hide();
            return false;
        }
        return true;
    }
</script>
</body>
</html>