<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.mtbd.SearchMtbd"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.common.enums.ArticlePublishStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.info.zxdt.mtbd.UpdateMtbd" %>
<%@page import="com.dimeng.p2p.variables.FileType" %>
<%@page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    CURRENT_SUB_CATEGORY = "MTBD";
    ArticleRecord record = ObjectHelper.convert(request.getAttribute("record"), ArticleRecord.class);
    String content = ObjectHelper.convert(request.getAttribute("content"), String.class);
    String nowDate = DateParser.format(record.publishTime);
    ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>修改<%=xcglmanage.getCategoryNameByCode("MTBD") %>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form name="example" method="post"
                                  action="<%=controller.getURI(request, UpdateMtbd.class)%>"
                                  enctype="multipart/form-data" class="form1">
                                <input type="hidden" value="<%=record.id %>" name="id"/>
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">&nbsp;</span></span>
                                        <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>文章名称：</span>
                                        <input type="text" class="text border w300 pl5 required max-length-30"
                                               name="title" value="<%StringHelper.filterHTML(out, record.title);%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5">来源：</span>
                                        <input type="text" class="text border w300 pl5 max-length-50" name="source"
                                               value="<%StringHelper.filterHTML(out, record.source);%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>封面图片：</span>
                                        <img src="<%=fileStore.getURL(record.imageCode) %>" width="277" height="89"/>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml10">
                                            <input type="file" class="text border w300 pl5" name="image" value=""/>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>排序值：</span>
                                        <input type="text" class="text border w300 pl5 isint max-length-10"
                                               name="sortIndex" value="<%=record.sortIndex%>"/>
                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0），如果不填写默认值是1.</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>是否发布：</span>
                                        <select name="status" class="border w300 pl5">
                                            <option value="<%=ArticlePublishStatus.WFB%>"
                                                    <%if(record.publishStatus.equals(ArticlePublishStatus.WFB)){ %>selected="selected"<%} %>><%=ArticlePublishStatus.WFB.getName()%>
                                            </option>
                                            <option value="<%=ArticlePublishStatus.YFB%>"
                                                    <%if(record.publishStatus.equals(ArticlePublishStatus.YFB)){ %>selected="selected"<%} %>><%=ArticlePublishStatus.YFB.getName()%>
                                            </option>
                                        </select>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>显示时间：</span>
                                        <input type="text" readonly="readonly" id="datepicker1" name="publishTime"
                                               onblur="checkDate()" class="text border w300 pl5 date required"
                                               value="<%=DateParser.format(record.publishTime)%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>文章摘要：</span>
                                        <input type="text" class="text border w300 pl5 max-length-110" name="summary"
                                               value="<%StringHelper.filterHTML(out, record.summary);%>"/>
                                        <span tip>最大110个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl"><span class="red">*</span>文章内容：</span>

                                        <div class="pl200 ml5">
                                            <textarea name="content" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"><%StringHelper.format(out, content, fileStore); %></textarea>
                                            <span id="errorContent">&nbsp;</span>
                                        </div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchMtbd.class) %>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml20" value="取消">
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
<script>
    var editor1;
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="content"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.ARTICLE_ATTACHMENT.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterBlur: function () {
                this.sync();
                if (this.count('text') == '') {
                    $("#errorContent").addClass("red");
                    $("#errorContent").html("文章内容不能为空");
                }
                else {
                    $("#errorContent").removeClass("red");
                    $("#errorContent").html("&nbsp;");
                }
            }
        });
        prettyPrint();

        function onSubmit() {
            if (editor1.count('text') == '') {
                $("#errorContent").addClass("red");
                $("#errorContent").html("文章内容不能为空");
                return false;
            }
            else {
                $("#errorContent").removeClass("red");
                $("#errorContent").html("&nbsp;");
                return true;
            }
        }
    });
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                var dates = $("#datepicker1")
                var d = dates.val();
                if (d != "") {
                    var $error = dates.nextAll("span[errortip]");
                    var $tip = dates.nextAll("span[tip]");
                    $error.removeClass("error_tip");
                    $error.hide();
                    $tip.show();
                }
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(nowDate)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, nowDate);%>");
        <%}%>
    });

    function checkDate() {
        window.setTimeout(function () {
            var dates = $("#datepicker1")
            var d = dates.val();
            if (d != "") {
                var $error = dates.nextAll("span[errortip]");
                var $tip = dates.nextAll("span[tip]");
                $error.removeClass("error_tip");
                $error.hide();
                $tip.show();
            }
        }, 100);
    }
</script>
</body>
</html>