<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddEnterpriseXm"%>
<%@page import="com.dimeng.p2p.common.HighPrecisionFormater" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddUserInfoXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDyw" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.entities.*" %>
<%@ page import="com.dimeng.p2p.S62.enums.*" %>
<%@ page import="com.dimeng.p2p.console.servlets.upload.Upload" %>
<%@ page import="com.dimeng.p2p.variables.FileType" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    T6230 info = ObjectHelper.convert(request.getAttribute("loan"), T6230.class);
    T6231 t6231 = ObjectHelper.convert(request.getAttribute("t6231"), T6231.class);
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6211[] t6211s = ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    T6238 t6238 = ObjectHelper.convert(request.getAttribute("t6238"), T6238.class);
    String userName = ObjectHelper.convert(request.getAttribute("userName"), String.class);
    if (info == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    int shengId = 0;
    int shiId = 0;
    int xianId = 0;
    final int regionId = t6231.F07;
    if (regionId % 10000 < 100) {
        shengId = regionId;
    } else if (regionId % 100 < 1) {
        shengId = regionId / 10000 * 10000;
        shiId = regionId;
    } else {
        shengId = regionId / 10000 * 10000;
        shiId = regionId / 100 * 100;
        xianId = regionId;
    }

    T6216 product = ObjectHelper.convert(request.getAttribute("product"), T6216.class);
    if (product == null) product = new T6216();
    String minSizeJKZQ = "min-size-" + product.F07;
    String maxSizeJKZQ = "max-size-" + product.F08;
    String minSizeJKZQDay = "min-size-" + product.F16;
    String maxSizeJKZQDay = "max-size-" + product.F17;
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理-修改借款信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="javascript:void(0)" class="tab-btn select-a">项目信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddUserInfoXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%} %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddEnterpriseXm.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">企业信息</a></li>
                                <%} %>

                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">

                            <form action="<%=controller.getURI(request, UpdateProject.class)%>" method="post" id="form1"
                                  enctype="multipart/form-data" class="form1">
                                <input id="shengId" value="<%=shengId%>" type="hidden"/> <input id="shiId"
                                                                                                value="<%=shiId%>"
                                                                                                type="hidden"/> <input
                                    id="xianId" value="<%=xianId%>" type="hidden"/>
                                <input type="hidden" name="flag" id="flag"/>
                                <input type="hidden" name="userId" value="<%=userId%>">
                                <input type="hidden" name="userType" value="<%=userType%>">
                                <input type="hidden" name="loanId" value="<%=loanId%>">
                                <input type="hidden" name="F25" value="<%StringHelper.filterHTML(out, info.F25);%>"/>
                                <input type="hidden" name="submitType" id="submitType" value="processPost"/>
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>产品名称</span>
                                        <select id="productId" class="border mr20 h32 mw100 required" name="productId">
                                            <%
                                                PagingResult<T6216> result = ObjectHelper.convert(request.getAttribute("productResult"), PagingResult.class);
                                                if (result != null && result.getItems() != null && product.F01 > 0) {
                                                    T6216[] products = result.getItems();
                                                    for (T6216 t6216 : products) {
                                                        if (product.F01 == t6216.F01) {
                                            %>
                                            <option value="<%=t6216.F01 %>" selected="selected"><%
                                                StringHelper.filterHTML(out, t6216.F02); %></option>
                                            <%} else {%>
                                            <option value="<%=t6216.F01 %>"><%
                                                StringHelper.filterHTML(out, t6216.F02); %></option>
                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
									<li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>标的类型</span>
                                        <select class="border mr20 h32 mw100" name="F04" id="bidType">
                                            <%
                                                int type = info.F04;
                                                String[] typeStrs = null;
                                                if (product != null && product.F03 != null) {
                                                    typeStrs = product.F03.split(",");
                                                }
                                                if (typeStrs == null) {
                                                    typeStrs = new String[0];
                                                }
                                            %>
                                            <%
                                                if (t6211s != null) {
                                                    for (int i = 0; i < t6211s.length; i++) {
                                                        for (int j = 0; j < typeStrs.length; j++) {
                                                            if (typeStrs[j].equals(String.valueOf(t6211s[i].F01))) {%>
                                            <option value="<%=t6211s[i].F01 %>"
                                                    <%if(type==t6211s[i].F01){ %>selected="selected"<%} %>><%
                                                StringHelper.filterHTML(out, t6211s[i].F02); %></option>


                                            <% }
                                            }
                                            }
                                            }
                                            %>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>借款账户</span>
                                        <%
                                            String name = request.getParameter("name");
                                        %>
                                        <%
                                            if (userId > 0) {
                                        %>
                                        <input type="text" id="userName" class="text border pl5 w300" name="name"
                                               disabled="disabled"
                                               value="<%StringHelper.filterHTML(out, userName); %>"/>
                                        <%} else { %>
                                        <input type="text" id="userName" onblur="checkUserInfo()"
                                               class="text border pl5 w300 required max-length-20"
                                               name="name" <%if (!StringHelper.isEmpty(name)) { %>
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>" <%} else { %>
                                               value="<%StringHelper.filterHTML(out, userName); %>" <%} %>/>
                                        <%} %>
                                        <span tip></span>
                                        <span errortip id="userMsg" class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>借款标题</span>
                                        <input type="text" maxlength="14"
                                               class="text border pl5 w300 required max-length-14" name="F03"
                                               value="<%StringHelper.filterHTML(out, info.F03);%>"/>
                                        <span tip></span>
                                        <span errortip id="userMsg" class="" style="display: none"></span>
                                    </li>
                                    
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>借款金额</span>
                                        <input type="text" onblur="changeJkje(this.value)"
                                               class="text border pl5 w300 required min-size-<%=HighPrecisionFormater.toPlainString(product.F05) %>  max-size-<%=HighPrecisionFormater.toPlainString(product.F06) %>"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               maxlength="15" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/"
                                               mtestmsg="必须为整数" name="F05"
                                               value="<%=HighPrecisionFormater.toPlainString(info.F05) %>"/>
                                        元 <font>借款金额范围 (<%=HighPrecisionFormater.toPlainString(product.F05) %>
                                            -<%=HighPrecisionFormater.toPlainString(product.F06) %>) 元</font>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>标的属性</span>
                                        <input id="ydb" type="checkbox" onclick="bdsxClick()" name="F11"
                                               value="S" <%if (info.F11 == T6230_F11.S) { %> checked="checked" <%} %>/>担保
                                        <input type="checkbox" onclick="bdsxClick()" name="F13"
                                               value="S" <%if (info.F13 == T6230_F13.S) { %> checked="checked" <%} %>/>抵（质）押
                                        <input type="checkbox" onclick="bdsxClick()" name="F14"
                                               value="S" <%if (info.F14 == T6230_F14.S) { %> checked="checked" <%} %>/>实地认证
                                        <input type="checkbox" onclick="bdsxClick()" name="F33"
                                               value="S" <%if (info.F33 == T6230_F33.S) { %> checked="checked" <%} %>/>信用
                                        <span id="errorBdsx">&nbsp;</span>
                                    </li>
                                    <li id="dbfa" class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>担保方案</span>
                                        <select class="border mr20 h32 mw100" name="F12">

                                            <%
                                                for (T6230_F12 t6230_F122 : T6230_F12.values()) {
                                            %>
                                            <option value="<%=t6230_F122.name() %>"
                                                    <%if(info.F12==t6230_F122){ %>selected="selected"<%} %>><%=t6230_F122.getChineseName() %>
                                            </option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>新增标识</span>
                                        <select class="border mr20 h32 mw100" name="bidFlag"
                                                onchange="changeJlb(this.value)">
                                            <option value=""
                                                    <%if("F".equals(info.xsb.name()) && "F".equals(t6231.F27.name())){ %>selected="selected"<%} %>>
                                                未标识
                                            </option>
                                            <option value="xsb"
                                                    <%if("S".equals(info.xsb.name())){ %>selected="selected"<%} %>>新手标
                                            </option>
                                            <option value="jlb"
                                                    <%if("S".equals(t6231.F27.name())){ %>selected="selected"<%} %>>奖励标
                                            </option>
                                        </select>
                                    </li>
                                    <li id="jlll" class="mb10"
                                        <%if("F".equals(t6231.F27.name())){%>style="display: none"<%} %>>
                                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>奖励利率</span>
                                        <input id="czjlll" type="text"
                                               class="text border pl5 w300 required minf-size-0.01 max-size-99.999"
                                               mtest="/^\d*(\d|(\.[0-9]{1}|\.[0-9]{2}))$/" mtestmsg="只能有两位小数"
                                               name="jlll" value="<%=Formater.formatRate(t6231.F28,false)%>"/> %&nbsp;
                                        <span tip>奖励利率精确到小数点后两位,大于0且小于100</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>还款方式</span>
                                        <select class="border mr20 h32 mw100" name="F10" id="hkfs">
                                            <%
                                                T6230_F10 hkfs = info.F10;
                                                String ways = product.F11;
                                            %>
                                            <%
                                                if (ways != null) {
                                                    for (T6216_F11 way : T6216_F11.values()) {
                                                        if (ways.indexOf(way.name()) > -1) {
                                            %>
                                            <option value="<%=way.name()%>"
                                                    <%if(hkfs != null && hkfs.name().equals(way.name()) ){ %>selected="selected"<%} %>><%=way.getChineseName()%>
                                            </option>
                                            <%}%>
                                            <%
                                                    }
                                                }
                                            %>

                                        </select>
						<span id="_accDay">
							<%String accDay = t6231.F21.name();%>
							<input id="accDay_S" type="radio" name="accDay" value="S" <%if ("S".equals(accDay)){ %> checked="checked"<%} %>/>按天
							<input id="accDay_F" type="radio" name="accDay" value="F" <%if ("F".equals(accDay) || StringHelper.isEmpty(accDay)) { %>checked="checked"<%} %>/>按月
						</span>
                                    </li>
                                    <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa"><em class="red pr5">*</em>借款期限</span>
                                        <%
                                            int dayOrMonth = 0;
                                            if (T6230_F10.YCFQ == info.F10 && T6231_F21.S == t6231.F21) {
                                                dayOrMonth = t6231.F22;
                                            } else {
                                                dayOrMonth = info.F09;
                                            }%>
                                        <div class="pl200 ml10"><input id="jkqx" type="text"
                                               class="text border pl5 w300 required min-size-<%="S".equals(accDay) ? product.F16: product.F07%> max-size-<%="S".equals(accDay) ? product.F17: product.F08%>"
                                               mtest="/^\d+$/" mtestmsg="必须为整数" name="F09" value="<%=dayOrMonth %>"/>
                                        月/天<font>借款期限范围 <span id="jkxqDayDe">(<%="S".equals(accDay) ? product.F16: product.F07%>-<%="S".equals(accDay) ? product.F17: product.F08%>) <%="S".equals(accDay) ? "天":"月" %></span></font>

                                        <span tip>除本息到期一次付清可选按天计算外，其他皆以月为单位</span>
                                        <span errortip class="" style="display: none" id="jkqxDiv"></span></div>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>付息方式</span>
                                        <input type="radio" name="F17" value="ZRY" <%if (info.F17 == T6230_F17.ZRY) { %>
                                               checked="checked" <%} %> id="zry"/>自然月
                                        <input type="radio" name="F17" value="GDR" <%if (info.F17 == T6230_F17.GDR) { %>
                                               checked="checked" <%} %> id="gdr"/>固定日
                                    </li>
                                    <li id="fxr" class="mb10" style="display: none;"><span class="display-ib w200 tr mr5"><em
                                            class="red pr5">*</em>付息日</span>
                                        <select class="border mr20 h32 mw100" name="F18">
                                            <%
                                                for (int i = 1; i <= 28; i++) {
                                            %>
                                            <option value="<%=i %>"
                                                    <%if(i==info.F18){ %>selected="selected"<%} %>><%=i %>号
                                            </option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>起息日</span>
                                        <select class="border mr20 h32 mw100" name="F19">
                                            <%
                                                int qxr = IntegerParser.parse(request.getParameter("F19"));
                                            %>
                                            <option value="0" <%if(info.F19==0){ %>selected="selected"<%} %>>T+0</option>
                                            <option value="1" <%if(info.F19==1){ %>selected="selected"<%} %>>T+1</option>
                                            <option value="2" <%if(info.F19==2){ %>selected="selected"<%} %>>T+2</option>
                                            <option value="3" <%if(info.F19==3){ %>selected="selected"<%} %>>T+3</option>
                                            <option value="4" <%if(info.F19==4){ %>selected="selected"<%} %>>T+4</option>
                                            <option value="5" <%if(info.F19==5){ %>selected="selected"<%} %>>T+5</option>
                                        </select>
                                        <span>T+0表示放款当天开始计算利息(当日计息)，T+1表示放款后第一天开始计算利息(次日计息)，以此类推</span>
                                    </li>
                                    <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa"><em class="red pr5">*</em>年化利率</span>
                                        <div class="pl200 ml10"><input type="text"
                                               class="text border w300 pl5 required minf-size-<%=HighPrecisionFormater.toPlainString(product.F09)%> maxf-size-<%=HighPrecisionFormater.toPlainString(product.F10)%>"
                                               mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="只能有两位小数" name="F06"
                                               value="<%=Formater.formatRate(info.F06,false)%>"/>
                                        % <font>年化利率范围 (<%=HighPrecisionFormater.toPlainString(product.F09) %>
                                            -<%=HighPrecisionFormater.toPlainString(product.F10) %>) %</font>
                                        <span errortip class="" style="display: none"></span>
                                        <span tip>借款最低利率由您的借款期限确定，一般来说借款利率越高，筹款速度越快。</span></div>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>成交服务费率</span>
                                        <input type="text"
                                               class="text border w300 pl5 max-nesize-1 minf-size-0 max-precision-9 required"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数" name="cjfwfl"
                                               value="<%=HighPrecisionFormater.toPlainString(StringHelper.isEmpty(request.getParameter("cjfwfl")) ? product.F12: new BigDecimal(request.getParameter("cjfwfl")))%>"/>
                                        精确到小数点后三位且小于1&nbsp;借款人给平台的管理费
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>投资管理费率</span>
                                        <input type="text"
                                               class="text border w300 pl5 max-nesize-1 minf-size-0 max-precision-9 required"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数" name="tzglfl"
                                               value="<%=HighPrecisionFormater.toPlainString(StringHelper.isEmpty(request.getParameter("tzglfl")) ? product.F13: new BigDecimal(request.getParameter("tzglfl"))) %>"/>
                                        精确到小数点后三位且小于1&nbsp;投资人给平台的投资管理费
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>逾期罚息利率</span>
                                        <input type="text"
                                               class="text border w300 pl5 max-nesize-1 minf-size-0 max-precision-9 required"
                                               mtest="/^\d*(\d|(\.[0-9]{1,3}))$/" mtestmsg="只能有三位小数" name="yqflfl"
                                               value="<%=HighPrecisionFormater.toPlainString(StringHelper.isEmpty(request.getParameter("yqflfl")) ? product.F14: new BigDecimal(request.getParameter("yqflfl"))) %>"/>
                                        精确到小数点后三位且小于1&nbsp;借款人逾期后给投资人的逾期罚息
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>筹款期限</span>
                                        <select class="border mr20 h32 mw100" name="F08">
                                            <%int cbqx = info.F08; %>
                                            <%
                                                for (int i = 1; i <= 31; i++) {
                                            %>
                                            <option value="<%=i %>" <%if(cbqx==i){ %>selected="selected"<%} %>><%=i %>
                                            </option>
                                            <%} %>
                                        </select>天&nbsp;借款项目的筹款期限
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>起投金额</span>
                                        <input type="text" class="text border w300 pl5 more-than-zero"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为整数" name="zxtze" value="<%=StringHelper.isEmpty(request.getParameter("zxtze"))? product.F15 : request.getParameter("zxtze")%>"/>元&nbsp;单笔投资最低可投金额
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                        <input type="hidden" name="minBidingAmount" value="<%=product.F15%>">
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>最大投资金额</span>
                                        <input type="text" id="zdtze" class="text border w300 pl5 more-than-zero"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               maxlength="15"
                                               mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)\.[0-9]{1,2}))$/"
                                               mtestmsg="必须为整数" name="zdtze" value="<%=HighPrecisionFormater.toPlainString(t6231.F26) %>"/>元&nbsp;累计投资最大可投金额
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                        <input type="hidden" name="minBidingAmount" value="<%=product.F15%>">
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>项目所在区域</span>
                                        <select name="sheng" class="border mr10 h32 mw100">
                                        </select> <select name="shi" class="border mr10 h32">
                                        </select> <select name="xian" class="border mr10 h32 required" id="xianSlt">
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                   <%-- <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>还款来源</span>
                                        <input type="text" class="text border w300 pl5 required max-length-50"
                                               name="hkly" value="<%StringHelper.filterHTML(out, t6231.F16);%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>借款用途</span>
                                        <input type="text" class="text border w300 pl5 required max-length-100"
                                               name="jkyt" value="<%=t6231.F08%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>--%>
                                    <%-- <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>活动使用方式</span>
                                        <%
                                            for(T6231_F36 t6231_f36 : T6231_F36.values()){
                                        %>
                                        <input type="radio" <%if("S".equals(info.xsb.name()) || "S".equals(t6231.F27.name())){%>disabled="disabled"<%}%> name="rewardUsedType" value="<%=t6231_f36.name()%>" <%if (t6231_f36 == t6231.F36) { %>
                                               checked="checked" <%} %> id="<%=t6231_f36.name()%>"/><span class="mr10"><%=t6231_f36.getChineseName()%></span>
                                        <%}%>

                                    </li> --%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>投资端</span>
                                        <input type="radio" name="terminal" value="PC_APP" <%if (t6231.F33 == T6231_F33.PC_APP) { %>
                                               checked="checked" <%} %> id="PC_APP"/>PC+APP
                                        <input type="radio" name="terminal" value="PC" <%if (t6231.F33 == T6231_F33.PC) { %>
                                               checked="checked" <%} %> id="PC"/>PC
                                        <input type="radio" name="terminal" value="APP" <%if (t6231.F33 == T6231_F33.APP) { %>
                                               checked="checked" <%} %> id="APP"/>APP
                                    </li>
                                    <%if (!StringHelper.isEmpty(info.F21)) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>

                                        <img src="<%=fileStore.getURL(info.F21)%>" width="200px" height="100px">


                                    </li>
                                    <%} %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">标的图标</span>
                                    	<%if(!StringHelper.isEmpty(info.F21)){ %>
                                        <input type="text" name="pcimage" class="text border pl5 w120 " value="<%StringHelper.filterHTML(out, info.F21);%>"/>
                                        <%} %>
                                        <input type="file" name="image" value="">
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em class="red pr5">*</em>借款描述</span>

                                        <div class="pl200 ml5">
                                            <textarea name="jkms" cols="100" rows="8"
                                                      style="width:700px;height:500px;visibility:hidden;"
                                                      class="required min-length-20 max-length-60000"><%StringHelper.format(out, t6231.F09, fileStore);%></textarea>
                                            <span id="jkmsError" tip>20-60000字</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5" id="saveBtn">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存" onclick="save()"/>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="下一步" onclick="saveCon()"/>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, LoanList.class) %>'"
                                                   value="取消">
                                        </div>
                                        <div class="tc w350" id="saveSpan" style="display:none;">
                                            <span style="color:red;">正在保存中...</span>
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
<div class="popup_bg hide"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/addProjectXqValidation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
    $("div.popup_bg").show();
</script>
<%} %>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=infoMessage%>', "yes"));
    $("div.popup_bg").show();
</script>
<%} %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<%@include file="/WEB-INF/include/kindeditor.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    //富文本框
    KindEditor.ready(function (K) {
        editor1 = K.create('textarea[name="jkms"]', {
            uploadJson: '<%=controller.getURI(request, Upload.class)%>?type=<%=FileType.BID_DETAILS_FILE.ordinal()%>',
            allowFileManager: false,
            formatUploadUrl: false,
            afterCreate:function(){
            	if(this.count()==0){
            		this.html('<div id="jkmsTip"><p><span style="font-size:14px;color:gray;">请填写借款详情描述内容，如：</span></p><p><span style="font-size:14px;color:gray;">还款来源：</span></p><p><br /></p><p><span style="font-size:14px;color:gray;"> 借款用途：</span></p></div>');
            		jkmsFlgs=true;
            	}
            },
            afterFocus:function(){
            	if(this.html().indexOf('<div id="jkmsTip">')>-1){
            		this.html("");
            	}
            },
            afterBlur: function () {
                this.sync();
                $error = $("#jkmsError");
                if (this.count() > 60000 || this.count() < 20) {
                    $error.addClass("error_tip");
                    $error.html("长度不对，20-60000字！");
                    jkmsFlgs = true;
                }
                else {
                    $error.removeClass("error_tip");
                    $error.html("20-60000字");
                    jkmsFlgs = false;
                }
                if(this.count()==0){
            		this.html('<div id="jkmsTip"><p><span style="font-size:14px;color:gray;">请填写借款详情描述内容，如：</span></p><p><span style="font-size:14px;color:gray;">还款来源：</span></p><p><br /></p><p><span style="font-size:14px;color:gray;"> 借款用途：</span></p></div>');
            		jkmsFlgs=true;
            	}
            },
            afterChange: function () {
                var maxNum = 60000, text = this.text();
                if (this.count() > maxNum) {
                    text = text.substring(0, maxNum);
                    this.text(text);
                }
            }
        });
        prettyPrint();
    });

    $("#zry").click(function () {
        $("#fxr").hide();
    });
    $("#gdr").click(function () {
        $("#fxr").show();
    });
    function save() {
        $("#flag").attr("value", "0");
    }
    function saveCon() {
        $("#flag").attr("value", "1");
    }
    $("#ydb").click(function () {
        var ydb = $("#ydb").attr("checked");
        if (ydb == 'checked') {
            $("#dbfa").show();
        } else {
            $("#dbfa").hide();
        }
    });
    $(function () {
    	var bdlx = $("#bidFlag").val();
        var hkfs = $("#hkfs").attr("value");
        var ydb = $("#ydb").attr("checked");
        
        var t6231_F27 = "<%=t6231.F27%>"; 
        if (bdlx == "jlb" || t6231_F27 == "S") 
        { 
        	$("#jlll").show(); 
        } else { 
        	$("#jlll").hide(); 
        }
        if (ydb == 'checked') {
            $("#dbfa").show();
        } else {
            $("#dbfa").hide();
        }
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#zry").attr("checked", true);
        }
        else {
            $("#gdr").attr("disabled", false);
        }
        var gdr = $("#gdr").attr("checked");
        if (gdr == 'checked') {
            $("#fxr").show();
        }
        loadPage();

        $("input[name=accDay]").change(function () {
            var value = $(this).val();
            changeDayShow(value);
        });

        $("#productId").change(function () {
            $("#submitType").val("processGet");
            $("input[name='cjfwfl']").val("");
            $("input[name='tzglfl']").val("");
            $("input[name='yqflfl']").val("");
            $("input[name='zxtze']").val("");
            $("#form1").submit();
            $("#submitType").val("processPost");
        });
    });

    function changeDayShow(obj) {
    	$("#jkqxDiv").hide();
        if (obj == "<%=T6231_F21.S%>") {
            $("#jkqx").removeClass("<%=minSizeJKZQ%>");
            $("#jkqx").removeClass("<%=maxSizeJKZQ%>");
            $("#jkqx").addClass("<%=maxSizeJKZQDay%>");
            $("#jkqx").addClass("<%=minSizeJKZQDay%>");
            var minStr = "";
            $("#jkxqDayDe").text(<%=product.F16%>+"-"+<%=product.F17%>);
        }
        else {
            $("#jkqx").removeClass("<%=maxSizeJKZQDay%>");
            $("#jkqx").removeClass("<%=minSizeJKZQDay%>");
            $("#jkqx").addClass("<%=minSizeJKZQ%>");
            $("#jkqx").addClass("<%=maxSizeJKZQ%>");
            $("#jkxqDayDe").text(<%=product.F07%>+"-"+<%=product.F08%>);

        }
    }

    $("#hkfs").change(function () {
        var hkfs = $("#hkfs").attr("value");
        if (hkfs == 'YCFQ' || hkfs == 'DEBX') {
            $("#gdr").attr("disabled", "disabled");
            $("#gdr").attr("checked", false);
            $("#zry").attr("checked", true);
            $("#fxr").hide();//隐藏付息日选项（只有等额本金、每月付息才显示）
        }
        else {
            $("#gdr").attr("disabled", false);
        }

        accDay(hkfs);
    });

    function accDay(hkv) {
        if ("YCFQ" == hkv) {
            $("#_accDay").show();
        } else {
            $("#_accDay").hide();
        }


        var fs = $("input[name='accDay']:checked").val();
        if(fs==null || fs=="" || fs=="F"  ||"YCFQ" != hkv ){
	        $("#jkqx").removeClass("<%=minSizeJKZQDay%>");
	        $("#jkqx").removeClass("<%=maxSizeJKZQDay%>");
            $("#jkqx").addClass("<%=minSizeJKZQ%>");
            $("#jkqx").addClass("<%=maxSizeJKZQ%>");
	        $("#jkxqDayDe").text("("+<%=product.F07%>+"-"+<%=product.F08%>+")月");
        }else{
	        $("#jkqx").removeClass("<%=minSizeJKZQ%>");
	        $("#jkqx").removeClass("<%=maxSizeJKZQ%>");
            $("#jkqx").addClass("<%=minSizeJKZQDay%>");
            $("#jkqx").addClass("<%=maxSizeJKZQDay%>");
	        $("#jkxqDayDe").text("("+<%=product.F16%>+"-"+<%=product.F17%>+")天");
        }
        $("#jkxqDayDe").show();
    }
    function loadPage() {
        var hkfs = $("#hkfs").attr("value");
        accDay(hkfs);
    }

    $("select[name='xian']").on("change", function () {
        if ($(this).val() != null && $(this).val() != "") {
            $(this).nextAll("p").empty();
        }
    });

    function bdsxClick() {
        $error = $("#errorBdsx");
        if ($("input[type='checkbox']").is(':checked')) {
            bdsx = false;
            $error.removeClass("error_tip");
            $error.html("");
        }
        else {
            bdsx = true;
            $error.addClass("error_tip");
            $error.html("请勾选标的属性!");
        }
    }
</script>
</body>
</html>