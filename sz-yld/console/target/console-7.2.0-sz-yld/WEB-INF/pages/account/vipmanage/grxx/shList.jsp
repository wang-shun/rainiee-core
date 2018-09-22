<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.Rzbtg" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.Rztg" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.Open" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList" %>
<%@page import="com.dimeng.p2p.S61.enums.T6120_F05" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Rzxx" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    <%@include file="/WEB-INF/include/highslide.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "GRXX";
    Rzxx[] infos = ObjectHelper.convertArray(request.getAttribute("info"), Rzxx.class);
    int yhId = IntegerParser.parse(request.getAttribute("yhId"));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>审核列表</div>

                    </div>
                    <div class="border mt20 table-container">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title tc">
                                <th>序号</th>
                                <th>类型名称</th>
                                <th>必要认证</th>
                                <%--<th>分数</th>--%>
                                <th>认证次数</th>
                                <th>认证状态</th>
                                <th>认证时间</th>
                                <th class="w200" style="text-align: center;">操作</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <%
                                if (infos != null && infos.length > 0) {
                                    int index = 1;
                                    for (Rzxx info : infos) {
                            %>
                            <tr class="tc">
                                <td><%=index++ %>
                                </td>
                                <td><%StringHelper.filterHTML(out, info.lxmc); %></td>
                                <td><%=info.mustRz.getChineseName() %>
                                </td>
                                <%--<td><%=info.ds %></td>--%>
                                <td><%=info.rzcs %>
                                </td>
                                <td><%=info.status.getChineseName() %>
                                </td>
                                <td><%=DateTimeParser.format(info.time) %>
                                </td>
                                <td>
                                    <%if (info.status == T6120_F05.DSH) { %>
                                    <a href="javascript:void(0)" onclick="shtg(<%=info.yxjlID%>)"
                                       class="link-blue mr20">审核通过</a>
                                    <a href="javascript:void(0)" onclick="shbtg(<%=info.yxjlID%>)"
                                       class="link-blue mr20">审核不通过</a>
                                      <a href="javascript:void(0);" onclick="showImg(this,<%=info.yxjlID %>);"
                                       class="link-blue mr20 ">查看</a>
                                       <div class='highslide-gallery gallery-examples' style='display:none;'>
                                       
                                       </div>
                                    <%-- <a href="<%=controller.getURI(request, Open.class)%>?id=<%=info.yxjlID%>"
                                       target="_blank" class="link-blue mr20">查看</a> --%>
                                    <%}%>
                                    <%-- <span class="blue mr10"><a href="<%=controller.getURI(request, ShjlList.class)%>?id=<%=info.rzID%>&yhId=<%=yhId%>">审核记录</a></span> --%>
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
                           onclick="window.location.href='<%=controller.getURI(request, GrList.class)%>';"
                           class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回"/>
                </div>
                <div class="popup_bg hide"></div>
            </div>
        </div>
    </div>

<!--弹出框-->
<div class="popup-box hide" id="shtg">
    <form action="<%=controller.getURI(request, Rztg.class)%>" method="post" class="form2" id="form2">
        <input type="hidden" name="id" value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"/>
        <input type="hidden" id="tgyx" name="yxid"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">审核通过</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em
                            class="gray3">描述：</em></span>
                        <textarea name="desc" cols="40" rows="4"
                                  class="w400 h120 border p5 max-length-30"></textarea>
                        <br/>
                        <span class="display-ib tr mr5">&nbsp</span>
                        <span tip></span>
                        <span errortip class="red" style="display: none"></span></li>

                </ul>
            </div>
            <div class="tc f16">
                <input type="submit" value="提交" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       fromname="form2"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
            </div>
        </div>
    </form>
</div>

<!--弹出框-->
<div class="popup-box hide" id="shbtg">
    <form action="<%=controller.getURI(request, Rzbtg.class)%>" method="post" class="form1" id="form1">
        <input type="hidden" name="id" value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"/>
        <input type="hidden" id="btgyx" name="yxid"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">审核不通过</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix">
            <div class="mb40 gray6">
                <ul>
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em
                            class="gray3">描述：</em></span>
                        <textarea name="desc" cols="40" rows="4"
                                  class="required w400 h120 border p5 max-length-30"></textarea>
                        <br/>
                        <span class="display-ib tr mr5">&nbsp;</span>
                        <span tip class="red"></span>
                        <span errortip class="red" style="display: none"></span></li>

                </ul>
            </div>
            <div class="tc f16">
                <input type="submit" value="提交" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       fromname="form1"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a></div> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
        </div>
    </form>
</div>


<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $(function () {
        $("#backBtn").click(function () {
            window.location = "<%=controller.getURI(request, GrList.class)%>";
        });
    });
    function shtg(yxjlID) {
        $("#tgyx").attr("value", yxjlID);
        $(".popup_bg").show();
        $("#shtg").show();
    }

    function shbtg(yxjlID) {
        $("#btgyx").attr("value", yxjlID);
        $(".popup_bg").show();
        $("#shbtg").show();

    }
    
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
            			a += "<a onclick='return hs.expand(this)' href='"+fileCode+"' class='btn04 highslide fl' ><img src='"+fileCode+"'  alt='Highslide JS' /></a>";
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