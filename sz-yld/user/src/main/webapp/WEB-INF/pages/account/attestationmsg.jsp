<%@page import="com.dimeng.p2p.account.user.service.entity.Xyjl" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Rzsc" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.XyInfo" %>
<%@page import="com.dimeng.p2p.S61.enums.T6120_F05" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.RzxxInfo" %>
<%@page import="com.dimeng.p2p.account.user.service.RzxxManage" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "RZXX";

    RzxxManage manage = serviceSession.getService(RzxxManage.class);
    RzxxInfo[] rzxx = manage.getGRInfo();
    RzxxInfo[] mustRz = manage.getGRMustInfo();
    XyInfo xyInfo = manage.getXyInfo();
    Xyjl xyjl = manage.getXyjl();
    String score = "";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
        	<div class="user_mod">
		        <div id="con_one_2" class="mt20 rz_info" style="display:block;">
		                	<div class="lines pt5 f16"><i class="lines_ico"></i>信用额度：<%=xyInfo.xyed %></div>
		                    <div class="user_mod_gray record clearfix">
		                    	<div class="til">迪蒙网贷记录</div>
		                        <div class="line"></div>
		                        <ul class="fl">
		                        	<%
				                        /*score = +(xyjl.hxbs * IntegerParser.parse(configureProvider.getProperty(SystemVariable.YZYQJFCF))) + "";*/
				                    %>
		                        	<li>还清笔数(笔)<br /><%=xyjl.hxbs%></li>
		                        	<%
			                            if (xyjl.yqcs > 0) {
			                                /*score = "-" + (xyjl.yqcs * IntegerParser.parse(configureProvider.getProperty(SystemVariable.YZYQJFCF)));*/
			                            } else {
			                                score = "0";
			                            }
			                        %>
		                            <li>预期次数(次)<br /><%=xyjl.yqcs%></li>
		                            <%
			                            if (xyjl.yzyqcs > 0) {
			                                /*score = "-" + (xyjl.yzyqcs * IntegerParser.parse(configureProvider.getProperty(SystemVariable.YZYQJFCF)));*/
			                            } else {
			                                score = "0";
			                            }
			                        %>
		                            <li>严重预期笔数(笔)<br /><%=xyjl.yzyqcs%></li>
		                        </ul>
		                    </div>
		                    <div class="user_table mt20">
		                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                        <tr class="til">
		                            <td width="20%" align="center">&nbsp;</td>
		                            <td width="51%" align="left">项目</td>
		                            <td width="29%" align="left">状态</td>
		                        </tr>
		                        <%
			                        if (mustRz != null && mustRz.length > 0) {
			                            for (int i=0; i<mustRz.length; i++) {
			                                RzxxInfo info = mustRz[i];
			                     %>
		                          <tr>
		                          	<%if(i==0){ %>
		                            <td rowspan="<%=mustRz.length %>" align="center" class="border_r"><span class="f16">必要信用认证</span></td>
		                            <%} %>
		                            <td align="left"><%StringHelper.filterHTML(out, info.name); %></td>
		                            <td align="left">
		                            	<%
		                                    if (info.type == T6120_F05.WYZ) {
		                                %>
											<a href="javascript:void(0)"
		                                       onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')"
		                                       class="btn04 fl mr10">立即认证</a>
										<%} else if (info.type == T6120_F05.TG) { %>
											<a href="javascript:void(0)" class="btn04 fl mr10">通过</a>
											<a href='javascript:void(0)' onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')" class="btn04 fl mr10" >重新上传</a>
										<%} else if (info.type == T6120_F05.BTG) { %>
											<a href="javascript:void(0)"
		                                       onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')"
		                                       title="支持<%= configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE)%>格式,图片不得超过4M"
		                                       class="btn04 fl mr10">不通过</a>
										<%} else if (info.type == T6120_F05.DSH) { %>
											<a href="javascript:void(0)" class="btn04 fl mr10">待审核</a>
										<%} %>
		                            </td>
		                          </tr>
		                          <%
			                            }
			                        }
			                    %>
		                        <%
			                        if (rzxx != null && rzxx.length > 0) {
			                            for (int i=0; i<rzxx.length; i++) {
			                                RzxxInfo info = rzxx[i];
			                     %>
		                          <tr>
		                          	<%if(i==0){ %>
		                            <td rowspan="<%=rzxx.length %>" align="center" class="border_r"><span class="f16">可选信用认证</span></td>
		                            <%} %>
		                            <td align="left"><%StringHelper.filterHTML(out, info.name); %></td>
		                            <td align="left">
		                            	<%
		                                    if (info.type == T6120_F05.WYZ) {
		                                %>
											<a href="javascript:void(0)"
		                                       onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')"
		                                       class="btn04 fl mr10">立即认证</a>
										<%} else if (info.type == T6120_F05.TG) { %>
											<a href="javascript:void(0)" class="btn04 fl mr10">通过</a>
											<a href='javascript:void(0)' onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')" class="btn04 fl mr10" >重新上传</a>
										<%} else if (info.type == T6120_F05.BTG) { %>
											<a href="javascript:void(0)"
		                                       onclick="tcc(this,'<%=info.id %>','<%=controller.getURI(request, Rzsc.class) %>?id=<%=info.id %>','<%StringHelper.filterHTML(out,info.name);%>')"
		                                       title="支持<%= configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE)%>格式,图片不得超过4M"
		                                       class="btn04 fl mr10">不通过</a>
										<%} else if (info.type == T6120_F05.DSH) { %>
											<a href="javascript:void(0)" class="btn04 fl mr10">待审核</a>
										<%} %>
		                            </td>
		                          </tr>
		                          <%
			                            }
			                        }
			                    %>
		                      </table>
		                  </div>
		                </div>
                  </div>
                </div>
        <div class="clear"></div>
    </div>
</div>
<script type="text/javascript">

</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript">
    function tcc(evn, tid, url, titl) {
        global_art = art.dialog.open(url, {
            id: tid,
            title: titl,
            opacity: 0.1,
            width: 580,
            height: 400,
            lock: true,
            close: function () {
                var iframe = this.iframe.contentWindow;
                window.location.reload();
            }
        }, false);
    }
</script>
</body>
</html>