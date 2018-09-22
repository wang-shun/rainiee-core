<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.Open"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6121" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    <%@include file="/WEB-INF/include/highslide.jsp" %>
    <script type="text/javascript">
    var galleryOptions = {
    		slideshowGroup: 'gallery',
    		wrapperClassName: 'dark',
    		//outlineType: 'glossy-dark',
    		dimmingOpacity: 0.75,
    		align: 'center',
    		transitions: ['expand', 'crossfade'],
    		fadeInOut: true,
    		wrapperClassName: 'borderless floating-caption'
    	};
    </script>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    T6121[] info = ObjectHelper.convertArray(request.getAttribute("info"), T6121.class);
    UserManage manage = serviceSession.getService(UserManage.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>审核记录列表
                        </div>

                    </div>
                    <div class="border mt20 table-container">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title tc">
                                <th class="tc">序号</th>
                                <th class="tc">类型名称</th>
                                <th class="tc">审核描述内容</th>
                                <th class="tc">认证结果</th>
                                <th class="tc">认证人</th>
                                <th class="tc">认证时间</th>
                                <th class="w200" class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <%
                                if (info != null) {
                                    int index = 1;
                                    for (T6121 entity : info) {
                            %>
                            <tr class="tc">
                                <td class="tc"><%=index++ %></td>
                                <td class="tc"><%StringHelper.filterHTML(out, entity.F08); %></td>
                                <td class="tc"><%StringHelper.filterHTML(out, entity.F04); %></td>
                                <td class="tc"><%=entity.F05.getChineseName() %>
                                </td>
                                <td class="tc"><%StringHelper.filterHTML(out, manage.getAccountName(entity.F06)); %></td>
                                <td class="tc"><%=DateTimeParser.format(entity.F07) %>
                                </td>
                                <td class="tc">
	                                <a href="javascript:void(0);" onclick="showImg(this,<%=entity.F01 %>);"
		                                       class="link-blue mr20 ">查看</a>
	                                 <div class='highslide-gallery gallery-examples' style='display:none;'>
	                                 
	                                 </div>     
                                </td>
                            </tr>
                            <%
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

                </div>
                <div class="tc">
                    <input type="button"
                           onclick="history.back(-1);"
                           class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    function showImg(obj,id){
    	$(".highslide-gallery").html("");
    	var divObj = $(obj).next();
    	$.ajax({
            type:"post",
            url:"<%=controller.getURI(request, Open.class)%>",
            data:{"id":id},
            async: false ,
            dataType:"json",
            success: function(returnData){
            	if(returnData.fileCodes != null){
            		var a = "";
            		for(var i=0;i<returnData.fileCodes.length;i++){
            			var fileCode = returnData.fileCodes[i];
            			a += "<a onclick='return hs.expand(this,galleryOptions)' href='"+fileCode+"' class='btn04 highslide fl' ><img src='"+fileCode+"'  alt='Highslide JS' /></a>";
            		}
            		divObj.html(a);
                	var aObj = divObj.children().eq(0);
                	aObj.click();
            	}
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	alert(textStatus);
            }
        });
    	
    }
    </script>
</body>
</html>