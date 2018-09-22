<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=<%=resourceProvider.getCharset() %>" />
    <meta name="keywords" content="快速借款,短期借款,在线贷款"/>
    <meta name="description" content="<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>为您提供最新的贷款产品,包括个人借款、公司借款、短期借款等贷款产品，马上在线快速申请,<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>免费为你审核评估,专业团队为您量身定制贷款方案,额度高，利息低，流程简单！"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Pragma" CONTENT="no-cache"/>
    <meta http-equiv="Cache-Control" CONTENT="no-cache"/>
    <meta http-equiv="Expires" CONTENT="0"/>
    <title>在线贷款_快速借款_短期借款首选<%=configureProvider.getProperty(SystemVariable.SITE_NAME) %></title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
    <div class="main_mod">
        <div class="main_hd"><i class="icon"></i><span
                class="gray3 f18"><%=configureProvider.getProperty(SystemVariable.SITE_NAME) %>借款 - 产品介绍</span></div>
        <div class="product_list">
            <ul>
                <li class="modular01">
                    <div class="top"></div>
                    <div class="content clearfix">
                        <div class="mo_left">
                            <div class="con">
                                <i class="ico ico01"></i>

                                <p class="text">信用贷</p>
                            </div>
                        </div>
                        <div class="mo_right">
                            <div class="info">
                            	<div class="til">适用工薪阶层</div>
    							<div class="label">申请条件</div>
                                <%configureProvider.format(out, HTMLVariable.LOAN_PRODUCTS_XJD);%>
                            </div>
                            <div class="immediate">
                                <div class="con">
                                    <%--<a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.xjd.Index.class)%>"--%>
                                       <%--class="viewdetails_ico"><i></i>查看详细信息</a>--%>
                                    <a target="_blank"
                                       href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.Jkjsq.class)%>"
                                       class="calculator_ico"><i></i>借款计算器</a>
                                    <%
                                        if (dimengSession == null || !dimengSession.isAuthenticated()) {
                                    %>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.xjd.Index.class)%>"
                                       class="btn06">立即申请</a>
                                    <%
                                    } else {
                                        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
                                        T6110 t6110_menu = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
                                        T6110_F06 t6110_f06 = t6110_menu.F06;
                                        if (t6110_f06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_menu.F07) {
                                    %>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.xjd.Index.class)%>"
                                       class="btn06">立即申请</a>
                                    <%
                                            }
                                        }
                                    %>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <% if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.GUARANTEE_DBD_SWITCH))){ %>
                <!--新增担保贷开始-->
                <li class="modular06">
                	<div class="top"></div>
                    <div class="content clearfix">
                    	<div class="mo_left">
                        	<div class="con">
                                <i class="ico ico06"></i>
                                <p class="text">担保贷</p>
                            </div>
                        </div>
                        <div class="mo_right">
                        	<div class="info">
                            	<div class="til">适用工薪阶层或者私营企业主</div>
                                <div class="label">申请条件</div>
                                <%configureProvider.format(out, HTMLVariable.LOAN_PRODUCTS_DBD);%>                              
                            </div>
                            <div class="immediate">
                            	<div class="con">
                                    <%--<a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.dbd.Index.class)%>" class="viewdetails_ico"><i></i>查看详细信息</a>--%>
                                    <a target="_blank" href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.Jkjsq.class)%>"
                                       class="calculator_ico"><i></i>借款计算器</a>
                                    <%
                                        if (dimengSession == null || !dimengSession.isAuthenticated()) {
                                    %>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.dbd.Index.class)%>"
                                       class="btn06">立即申请</a>
                                    <%
                                    } else {
                                        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
                                        T6110 t6110_menu = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
                                        T6110_F06 t6110_f06 = t6110_menu.F06;
                                        if (t6110_f06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_menu.F07) {
                                    %>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.credit.dbd.Index.class)%>"
                                       class="btn06">立即申请</a>
                                    <%
                                            }
                                        }
                                    %>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <!--新增担保贷结束-->
                <%} %>
                <li class="modular02">
                    <div class="top"></div>
                    <div class="content clearfix">
                        <div class="mo_left">
                            <div class="con">
                                <i class="ico ico02"></i>

                                <p class="text">个人借款意向</p>
                            </div>
                        </div>
                        <div class="mo_right">
                            <div class="info">
                                <%configureProvider.format(out, HTMLVariable.GRJKYXJS);%>
                            </div>
                            <div class="immediate">
                                <div class="con">
                                    <a href="<%configureProvider.format(out,URLVariable.CREDIT_DKYX);%>" class="btn06">立即申请</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="modular03">
                    <div class="top"></div>
                    <div class="content clearfix">
                        <div class="mo_left">
                            <div class="con">
                                <i class="ico ico03"></i>

                                <p class="text">企业借款意向</p>
                            </div>
                        </div>
                        <div class="mo_right">
                            <div class="info">
                                <%configureProvider.format(out, HTMLVariable.QYJKYXJS);%>
                            </div>
                            <div class="immediate">
                                <div class="con">
                                    <a href="<%configureProvider.format(out,URLVariable.CREDIT_QYDKYX);%>"
                                       class="btn06">立即申请</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>
