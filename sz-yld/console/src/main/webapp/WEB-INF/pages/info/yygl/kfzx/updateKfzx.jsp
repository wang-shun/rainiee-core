<%@page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.SearchKfzx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.UpdateKfzx" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.CustomerServiceRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5012_F03" %>
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
    CURRENT_SUB_CATEGORY = "KFZX";
    CustomerServiceRecord record = ObjectHelper.convert(request.getAttribute("record"), CustomerServiceRecord.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form action="<%=controller.getURI(request, UpdateKfzx.class) %>" method="post"
                          enctype="multipart/form-data" class="form1">
                        <input type="hidden" value="<%=record.id %>" name="id"/>

                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改客服中心
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服名称：</span>
                                        <input type="text" class="text border w300  yw_w4 required max-length-30"
                                               name="name" value="<%StringHelper.filterHTML(out,record.name);%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服类型：</span>
                                        <select name="type" id="type" class="border mr20 h32">
                                            <option value="<%=T5012_F03.QQ.name() %>" <%if(record.type.name().equals(T5012_F03.QQ.name())){ %>selected="selected" <%} %>><%=T5012_F03.QQ.getChineseName() %></option>
                                            <option value="<%=T5012_F03.QQ_QY.name() %>" <%if(record.type.name().equals(T5012_F03.QQ_QY.name())){ %>selected="selected" <%} %>><%=T5012_F03.QQ_QY.getChineseName() %></option>
                                        </select>
                                    </li>

                                    <li class="mb10" id="kfhm" <%if(T5012_F03.QQ_QY==record.type){%>style="display: none;" <%}%>><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服号码：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name="number" value='<%=T5012_F03.QQ==record.type?record.number:"" %>'/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10" id="zxdm" <%if(T5012_F03.QQ==record.type){%>style="display: none;" <%}%>><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>在线代码：</span>
                                        <textarea id="Qy" name="Qy" cols="" rows="" class="w400 h70 border p5 required max-length-1000"><%if(T5012_F03.QQ_QY==record.type){StringHelper.filterHTML(out, record.number);} %></textarea>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>排序值：</span>
                                        <input type="text" class="text border w300 yw_w4 required isint max-length-10"
                                               name="sortIndex" value="<%=record.sortIndex%>"/>
                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>客服图片</span>
                                        <%if (record.imageCode != null) { %><span class="mb10"><img
                                                src="<%=fileStore.getURL(record.imageCode) %>" width="277" height="89"/></span><%} %>

                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input type="file" class="text border w300 yw_w4" name="image"/><span
                                                id="errorImage">&nbsp;</span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml20"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchKfzx.class) %>'">
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div> 
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $("#type").change(function(){
        var type_val =  $("#type").val();
        if(type_val == "QQ"){
            $("#kfhm").show();
            $("#zxdm").hide();
        }else{
            $("#kfhm").hide();
            $("#zxdm").show();
        }
    });
    
</script>
</body>
</html>