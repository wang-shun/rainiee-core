<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppStartFindSet"%>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.ViewStartFindSet"%>
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
AdvertisementRecord record = ObjectHelper.convert(request.getAttribute("entity"), AdvertisementRecord.class);
String advType = ObjectHelper.convert(request.getAttribute("advType"),String.class);
String title = null;
if(advType.equals("IOSPIC")){
    title = "IOS启动页图片";
}else if(advType.equals("ANDROIDPIC")){
    title = "Android启动页图片";
}else if(advType.equals("FINDPIC")){
    title = "发现页广告图片";
}
String flag = "true";
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改启动发现页图片</div>
                    </div>
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, ViewStartFindSet.class) %>" method="post"
                          enctype="multipart/form-data" onsubmit="return checkImage();" class="form1">
                        <input type="hidden" name="advType" value="<%=advType%>"/>

                        <div class="border mt20">
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">名称：</span>
                                            <input type="text" maxlength="30" name="title" readonly="readonly"
                                                   class="text border w300 yw_w5 required"
                                                   value="<%StringHelper.filterHTML(out,title);%>"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>图片上传：</span>
                                        	<%if(record == null){ flag="false";%>
                                        		<img src="" width="277" height="89"/>
                                        	<%}else{ %>
                                            <img src="<%=fileStore.getURL(record.imageCode) %>" width="277" height="89"/>
                                             <%} %>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200 ml5">
                                                <input type="file" name="image" class="text border w300 yw_w5 required" value=""/>
                                                <span id="adviceSpan">（建议尺寸：1080px*750px，支持jpg、jpeg、png格式）</span>
                                                <span id="errorImage">&nbsp;</span>
                                            </div>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">链接：</span>
                                            <input type="text" maxlength="250" name="url" class="text border w300 yw_w5"
                                                   <%if(record == null){ %> value="" <%}else{%>value="<%StringHelper.filterHTML(out,record.url);%>"<%}%>/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                            <input type="submit"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
                                                   fromname="form1" value="确认">
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, AppStartFindSet.class) %>'">
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

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript">
    function checkImage() {
        var suffix = $("input:file").val();
        var flag = '<%=flag%>';
        if(flag  == 'true'){return true;}
        if ('' == suffix || suffix == null || suffix.length == 0) {
            $("#errorImage").addClass("red");
            $("#errorImage").html("您还没有选择图片");
            return false;
        }
        var img = ['jpg', 'png', 'jpeg'];
        for (var i = 0; i < img.length; i++) {
            if (suffix.length > img[i].length && img[i] == suffix.substring(suffix.length - img[i].length)) {
                $("#errorImage").removeClass("red");
                $("#errorImage").html("&nbsp;");
                return true;
            }
        }
        $("#errorImage").addClass("red");
        $("#errorImage").html("您插入的图片格式不正确");
        return false;
    }
</script>
</body>
</html>