<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=<%=resourceProvider.getCharset() %>" />
    <meta name="keywords" content="p2p投资理财,投资理财产品,投资理财项目"/>
    <meta name="description" content="<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>最新理财产品介绍：p2p投资理财,投资理财产品,投资理财项目,<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>-你的投资理财得力助手,助你投资理财发大财。"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Pragma" CONTENT="no-cache"/>
    <meta http-equiv="Cache-Control" CONTENT="no-cache"/>
    <meta http-equiv="Expires" CONTENT="0"/>
    <title>p2p投资理财_投资理财产品_投资理财项目-<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>理财产品介绍
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>

<div class="main_bg">
    <div class="main_mod">
        <div class="main_hd"><i class="icon"></i><span
                class="gray3 f18"><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>理财-产品介绍</span></div>
        <div class="product_list">
            <ul>
                <li class="modular01">
                    <div class="top"></div>
                    <div class="content clearfix">
                        <div class="mo_left">
                            <div class="con">
                                <i class="ico ico04"></i>

                                <p class="text">散标投资</p>
                            </div>
                        </div>
                        <div class="mo_right">
                            <div class="info">

                                <%configureProvider.format(out, HTMLVariable.FINANCIAL_PRODUCTS_SBTZ);%>
                            </div>
                            <div class="immediate">
                                <div class="con">
                                    <a href="<%configureProvider.format(out,URLVariable.LC_LCJSQ);%>"
                                       class="calculator_ico" target="_blank"><i></i>理财计算器</a>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class)%>"
                                       class="btn06">去投资</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="modular02">
                    <div class="top"></div>
                    <div class="content clearfix">
                        <div class="mo_left">
                            <div class="con">
                                <i class="ico ico05"></i>

                                <p class="text">债权转让</p>
                            </div>
                        </div>
                        <div class="mo_right">
                            <%configureProvider.format(out, HTMLVariable.FINANCIAL_PRODUCTS_ZQZR);%>
                            <div class="immediate">
                                <div class="con">
                                    <a href="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR);%>"
                                       class="btn06">去购买</a>
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