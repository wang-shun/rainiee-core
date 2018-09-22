<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.SearchGggl"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.UpdateGggl" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
    <link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
</head>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "GGGL";
    AdvertisementRecord record = ObjectHelper.convert(request.getAttribute("record"), AdvertisementRecord.class);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改广告</div>
                    </div>
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, UpdateGggl.class) %>" method="post"
                          enctype="multipart/form-data" class="form1">
                        <input type="hidden" name="id" value="<%=record.id%>"/>

                        <div class="border mt20">
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>广告图片标题：</span>
                                            <input type="text" maxlength="30" name="title"
                                                   class="text border w300 yw_w5 required"
                                                   value="<%StringHelper.filterHTML(out,record.title);%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>广告类型：</span>
                                            <select name="advType" id="advType" class="border mr20 h32 mw100">
                                                <%
                                                    if (T5016_F12.values() != null && T5016_F12.values().length > 0) {
                                                        for (T5016_F12 t5016_F12 : T5016_F12.values()) {
                                                %>
                                                <option value="<%=t5016_F12.name()%>"
                                                        <%if(t5016_F12.name().equals(record.advType)) {%>selected="selected"<%} %>><%=t5016_F12.getChineseName() %>
                                                </option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">广告图片：</span>
                                            <img src="<%=fileStore.getURL(record.imageCode) %>" width="277"
                                                 height="89"/>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input type="file" name="image" class="text border w300 yw_w5"
                                                       value=""/><span
                                                    id="adviceSpan"><%if ("APP".equals(record.advType)||"APPGYJZ".equals(record.advType)) { %>（建议尺寸：宽640像素，高200像素）<%} else if("PCLOGIN".equals(record.advType)||"PCREGISTER".equals(record.advType)){ %>（建议尺寸：宽1920像素，高720像素）<%} else { %>（建议尺寸：宽1680像素，高300像素）<%} %></span>
                                            </div>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">链接：</span>
                                            <input type="text" maxlength="250" name="url" class="text border w300 yw_w5"
                                                   value="<%StringHelper.filterHTML(out,record.url);%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>排序值：</span>
                                            <input type="text" maxlength="10" name="sortIndex"
                                                   class="text border w300 yw_w5 required isint"
                                                   value="<%=record.sortIndex%>"/>
                                            <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">上架时间：</span>
                                            <input type="text" id="datepicker1" readonly="readonly" name="showTime"
                                                   class="text border w300 yw_w5 date datetime"
                                                   value="<%=DateTimeParser.format(record.showTime)%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>下架时间：</span>
                                            <input type="text" id="datepicker2" readonly="readonly" name="unshowTime"
                                                   class="text border w300 yw_w5 date datetime"
                                                   value="<%=DateTimeParser.format(record.unshowTime)%>"/>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                                   fromname="form1" value="确认">
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchGggl.class) %>'">
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    Date _showTime = record.showTime;
    Date _unshowTime = record.unshowTime;
    Date _nowTime = new Date();
    if (_showTime == null) {
        _showTime = _nowTime;
    }
    if (_unshowTime == null) {
        _unshowTime = _nowTime;
    }
%>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datetimepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datetimepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datetimepicker('option', {dateFormat: 'yy-mm-dd', timeFormat: 'HH:ii'});
        $("#datepicker2").datetimepicker({inline: true});
        $('#datepicker2').datetimepicker('option', {
            dateFormat: 'yy-mm-dd',
            timeFormat: 'HH:ii',
            minDate: $("#datepicker1").datetimepicker().val()
        });
        $("#datepicker1").datetimepicker("setDate", new Date(<%=_showTime.getTime() %>));
        $("#datepicker2").datetimepicker("setDate", new Date(<%=_unshowTime.getTime() %>));


        $("#advType").change(function () {
            var obj = $(this).val();
            if (obj == '<%=T5016_F12.APP.name()%>' || obj == '<%=T5016_F12.APPGYJZ.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽640像素，高200像素）");
            } else if (obj == '<%=T5016_F12.PCLOGIN.name()%>' || obj == '<%=T5016_F12.PCREGISTER.name()%>') {
                $("#adviceSpan").html("（建议尺寸：宽1920像素，高720像素）");
            }else{
                $("#adviceSpan").html("（建议尺寸：宽1680像素，高300像素）");
            }
        });
    });
</script>
</body>
</html>