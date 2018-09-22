<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.Open"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ByrzView"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.KxrzView"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Attestation"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.tzjl.QyTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jkxx.ViewJkxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dfjl.ViewListDfjl" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
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
    XyrzTotal xyrzTotal = ObjectHelper.convert(request.getAttribute("xyrzTotal"), XyrzTotal.class);
    Attestation[] notNeedAttestations = ObjectHelper.convertArray(request.getAttribute("notNeedAttestation"), Attestation.class);
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String returnUrl = controller.getURI(request, QyList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        returnUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">房产信息</a></li>
                                <%if(isHasGuarantor){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJkxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">借款记录</a></li>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, QyTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=xyrzTotal.byrztg %>/<%=xyrzTotal.needAttestation %>）</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">可选认证 （<%=xyrzTotal.kxrztg %>/<%=xyrzTotal.notNeedAttestation %>）
                                	<i class="icon-i tab-arrowtop-icon"></i></a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, KxrzView.class)%>" method="post">
                                <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                <input type="hidden" id="operationJK" name="operationJK" value="<%=operationJK%>">

                            <div class="tab-item">

                               <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title tc">
                                                <th>项目</th>
                                                <th>状态</th>
                                                <th>认证时间</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                if (notNeedAttestations != null) {
                                                    int i = 1;
                                                    for (Attestation need : notNeedAttestations) {
                                                        if (need == null) {
                                                            continue;
                                                        }
                                            %>

                                            <tr class="tc">
                                                <td><%StringHelper.filterHTML(out, need.attestationName);%></td>
                                                <td><%=need.attestationState.getChineseName()%>
                                                </td>
                                                <td><%=TimestampParser.format(need.attestationTime)%>
                                                </td>
                                                <% String[] fileUrls = need.fileUrls;
                                                    if (fileUrls != null) {
                                                %>
                                                <td align="center" class="blue">
                                                	<a href="javascript:void(0);" onclick="showImg(this,<%=need.id %>);"
						                                       class="link-blue mr20 ">查看</a>
					                                 <div class='highslide-gallery gallery-examples' style='display:none;'>
					                                 
					                                 </div>
                                                    <%-- <%
                                                        for (int a = 0; a < fileUrls.length; a++) {
                                                            String fileUrl = fileUrls[a];
                                                            if (!StringHelper.isEmpty(fileUrl)) {
                                                    %>
                                                    <a href="<%=fileUrl%>"
                                                       target="_blank" onclick="return hs.expand(this)"
                                                       class="link-blue mr20 highslide">查看证件&nbsp;<%=a+1 %>&nbsp;</a>
                                                    <%
                                                            }
                                                        }
                                                    %> --%>
                                                </td>
                                                <%} else { %>
                                                <td align="center" class="gray6">查看证件</td>
                                                <%} %>
                                            </tr>
                                            <%
                                                    }
                                                }
                                                    else {%>
                                            <tr>
                                                <td colspan="4" class="tc">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>
                                </div>
                            </div>
                            </form>
                        </div>

                    </div>
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