<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.CsList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.JsstList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S71.entities.T7152" %>
<%@page import="com.dimeng.p2p.common.enums.IsHovestatus" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.Cscl" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.Xscs" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.YzyqList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.DcYq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.dhklb.YqList" %>
<%@page import="com.dimeng.util.Formater" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.StayRefundInfo" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.CollectionManage" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Less30" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.StayRefundGather" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "DHKLB";
    StayRefundGather stayRefundGather = (StayRefundGather) request.getAttribute("stayRefundGather");
    PagingResult<Less30> less30s = (PagingResult<Less30>) request.getAttribute("less30s");
    Less30[] less30Array = less30s.getItems();
    Less30 less30SearchAmount = (Less30)request.getAttribute("less30SearchAmount");
    UserManage userManage = serviceSession.getService(UserManage.class);
    CollectionManage collectionManage = serviceSession.getService(CollectionManage.class);
    String[] emails = ObjectHelper.convertArray(request.getAttribute("emails"), String.class);
    String[] mobiles = ObjectHelper.convertArray(request.getAttribute("mobiles"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">

                <div class="p20">
                    <form id="form1" action="<%=controller.getURI(request, YqList.class)%>" method="post">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>待还款列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">待还本金总额</span>
                                        <span class="link-blue"><%=Formater.formatAmount(stayRefundGather.dhAmount) %></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">逾期待还总额</span>
                                        <span class="link-blue"><%=Formater.formatAmount(stayRefundGather.yqdhAmount) %></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">严重逾期待还总额</span>
                                        <span class="link-blue"><%=Formater.formatAmount(stayRefundGather.yzyqdhAmount) %></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题</span>
                                        <input type="text" name="loanRecordTitle" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanRecordTitle"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">催收记录</span>
                                        <select name="state" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="Y" <%if (IsHovestatus.Y.name().equals(request.getParameter("state"))) {%>
                                                    selected="selected" <%}%>>有
                                            </option>
                                            <option value="W" <%if (IsHovestatus.W.name().equals(request.getParameter("state"))) {%>
                                                    selected="selected" <%}%>>无
                                            </option>
                                        </select>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(DcYq.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%
                                            }
                                        %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(Xscs.class)) {%>
                                        <a href="javascript:xianshangcuishou();"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10 click-link popup-link"
                                           data-wh="800*600">线上催收</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)"
                                           class="btn btn-gray radius-6 mr5 pl10 pr10 click-link popup-link"
                                           data-wh="800*600">线上催收</a>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, JsstList.class)%>" class="tab-btn ">近30天待还款</a>
                                    </li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">逾期待还款<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, YzyqList.class)%>" class="tab-btn ">严重逾期待还款</a>
                                    </li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th><input type="checkbox" id="checkAll" onclick="selectAll()"/></th>
                                        <th>序号</th>
                                        <th>借款标题</th>
                                        <th>用户名</th>
                                        <th>借款金额(元)</th>
                                        <th>期数</th>
                                        <th>本期应还金额(元)</th>
                                        <th>剩余应还总额(元)</th>
                                        <th>逾期费用(元)</th>
                                        <th>逾期天数</th>
                                        <th>最近催收时间</th>
                                        <th class="w200">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (less30Array != null) {
                                            for (int i = 0; i < less30Array.length; i++) {
                                                Less30 less30 = less30Array[i];
                                                if (less30 == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td align="center"><input type="checkbox" name="checkbox" id="checkbox"
                                                                  value="<%=less30.userId %>,<%=less30.loanRecordId %>,<%=less30.collectionId %>,<%=less30.mobile %>,<%=less30.email %>"
                                                                  onclick="selectThis()"/></td>
                                        <td class="tc"><%=i + 1%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, less30.loanRecordTitle);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, less30.userName);%></td>
                                        <td align="center"><%=Formater.formatAmount(less30.loanAmount) %>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, less30.loandeadline); %></td>
                                        <td align="center"><%=Formater.formatAmount(less30.principalAmount) %>
                                        </td>
                                        <td align="center"><%=Formater.formatAmount(less30.residueAmount) %>
                                        </td>
                                        <td align="center"><%=Formater.formatAmount(less30.collectionAmount) %>
                                        </td>
                                        <td align="center"><%=less30.collectionNumber %>天</td>
                                        <td align="center"><%=Formater.formatDate(less30.refundDay)%>
                                        </td>
                                        <td align="center">
                                            <%if (T6110_F06.ZRR == less30.userType) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)%>?userId=<%=less30.userId%>&status=1"
                                               class="link-blue mr10 link_url" showObj="GRXX" data-title="user">个人资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">个人资料</a>
                                            <%} %>
                                            <%} else if (T6110_F06.FZRR == less30.userType && T6110_F10.F == less30.dbf) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx.class)%>?id=<%=less30.userId%>&status=1"
                                               class="link-blue mr10 link_url" showObj="QY" data-title="user">企业资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">企业资料</a>
                                            <%} %>
                                            <%} else if (T6110_F10.S == less30.dbf) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx.class)%>?id=<%=less30.userId%>&status=1"
                                               class="link-blue mr10 link_url" showObj="JG" data-title="user">机构资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">机构资料</a>
                                            <%} %>
                                            <%} %>
                                            <%if (dimengSession.isAccessableResource(Cscl.class)) {%>
                                            <a href="javascript:void(0)" onclick="showCs('<%=i %>')"
                                               class="link-blue popup-link" data-wh="800*600">线下催收</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">线下催收</a>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="12">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(less30SearchAmount.loanAmount) %>
                            </em> 元</span>
                            <span class="mr30">本期应还总金额：<em
                                    class="red"><%=Formater.formatAmount(less30SearchAmount.principalAmount) %>
                            </em> 元</span>
                            <span class="mr30">剩余应还总额：<em
                                    class="red"><%=Formater.formatAmount(less30SearchAmount.residueAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, less30s);
                        %>
                        <!--分页 end-->
                    </form>
                    <%
                        if (less30Array != null) {
                            for (int i = 0; i < less30Array.length; i++) {
                                Less30 less30 = less30Array[i];
                                if (less30 == null) {
                                    continue;
                                }
                                StayRefundInfo stayRefundInfo = collectionManage.findStayRefund(less30.collectionId);
                                T7152[] collectionRecords = userManage.csjlSearch(stayRefundInfo.userId);
                    %>
                    <div id="cs_<%=i%>" class="popup-box hide">
                        <div class="popup-title-container">
                            <h3 class="pl20 f18">线下催收</h3>
                            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
                        <div class="popup-content-container-2" style="max-height:550px;">
                            <form action="<%=controller.getURI(request, Cscl.class)%>" method="post" class="form<%=i%>">
                                <input type="hidden" name="type" value="near"/>
                                <input type="hidden" name="collectionId" value="<%=less30.collectionId %>"/>
                                <input type="hidden" name="loanRecordId" value="<%=stayRefundInfo.loanRecordId %>"/>
                                <input type="hidden" name="userId" value="<%=stayRefundInfo.userId%>"/>

                                <div class="p30">
                                    <ul class="gray6">
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">用户名：</span>

                                            <div class="pl120"><%
                                                StringHelper.filterHTML(out, stayRefundInfo.userName); %></div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">姓名：</span>

                                            <div class="pl120"><%
                                                StringHelper.filterHTML(out, stayRefundInfo.realName); %></div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">借款ID：</span>

                                            <div class="pl120"><%=stayRefundInfo.bidNum %>
                                            </div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">催收方式：</span>

                                            <div class="pl120">
                                                <label for="Xianxia-tel">
                                                    <input name="collectionType" type="radio" value="DH"
                                                           checked="checked" class="mr5"/>
                                                    电话</label>
                                                <label for="Xianxia-tel">
                                                    <input name="collectionType" type="radio" value="XC" class="mr5"/>
                                                    现场</label>
                                                <label for="Xianxia-tel">
                                                    <input name="collectionType" type="radio" value="FL" class="mr5"/>
                                                    法律</label>
                                            </div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                                                class="red mr5">*</em>催收人：</span>

                                            <div class="pl120">
                                                <input name="collectionPerson" type="text"
                                                       class="text border w200 pl5 required max-length-20"/>
                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                                                class="red mr5">*</em>催收时间：</span>

                                            <div class="pl120">
                                                <input readonly="readonly" name="collectionTime"
                                                       id="tempDatepicker<%=i %>" type="text"
                                                       class="text border w200 pl5 date required"/>
                                                <span tip></span>
                                                <span errortip class="red" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em
                                                class="red mr5">*</em>结果概要</span>

                                            <div class="pl120">
                                                <textarea name="resultDesc" id="keyWord"
                                                          class="ww60 border required max-length-150"></textarea>
                                                <span tip>150字以内</span>
                                                <span errortip class="red" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">备注：</span>

                                            <div class="pl120">
                                                <textarea name="comment" id=comment
                                                          class="ww60 border max-length-150"></textarea>
                                                <span tip>150字以内</span>
                                                <span errortip class="red" style="display: none"></span>
                                            </div>
                                        </li>
                                    </ul>

                                    <div class="tc f16">
                                        <input type="submit" value="确定"
                                               class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                                               fromname="form<%=i%>"/>
                                        <input type="button" value="取消"
                                               class="btn-blue2 btn white radius-6 pl20 pr20 ml40"
                                               onclick="closeInfo();"/>
                                    </div>
                                    <div class="mt20 h30 lh30 gray6">最近催收记录(所有借款)：</div>
                                    <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead class="fb">
                                            <tr class="title tc">
                                                <th>序号</th>
                                                <th>催收时间</th>
                                                <th>催收方式</th>
                                                <th>催收人</th>
                                                <th>结果概要</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                if (collectionRecords != null) {
                                                    for (int n = 0, length = Math.min(collectionRecords.length, 3); n < length; n++) {
                                                        T7152 t7152 = collectionRecords[n];
                                                        if (t7152 == null) {
                                                            continue;
                                                        }
                                            %>
                                            <tr class="tc">
                                                <td><%=n + 1%>
                                                </td>
                                                <td><%=Formater.formatDate(t7152.F08) %>
                                                </td>
                                                <td><%=t7152.F04.getChineseName() %>
                                                </td>
                                                <td><%StringHelper.filterHTML(out, t7152.F05); %></td>
                                                <td title="<%StringHelper.filterHTML(out, t7152.F06);%>"><%
                                                    StringHelper.filterHTML(out, StringHelper.truncation(t7152.F06, 15));%></td>
                                            </tr>
                                            <%
                                                }
                                            } else {
                                            %>
                                            <tr class="tc">
                                                <td colspan="5">暂无数据</td>
                                            </tr>
                                            <%} %>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="paging clearfix pt20">
                                        <div class="page-size fr gray6 mr20 lh30"><span
                                                data-bind="text: formatedItemCount">总共<span
                                                class="main-color pl5 pr5"><%=collectionRecords == null ? 0 : collectionRecords.length %></span>条记录</span>
                                            |
                                            <a href="<%=controller.getURI(request, CsList.class)%>?userName=<%=stayRefundInfo.userName%>"
                                               class="link-blue link_url" showObj="CSJL" data-title="business">查看全部&gt;&gt;</a>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
                <div class="popup_bg hide"></div>
            </div>
        </div>
    </div>
<%-- 线上催收 --%>
<div id="xscs" class="popup-box hide">
    <div class="popup-title-container">
        <h3 class="pl20 f18">线上催收</h3>
        <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
    <div class="popup-content-container-2" style="max-height:550px;">
        <form action="<%=controller.getURI(request, Xscs.class)%>" method="post" class="form">
            <input type="hidden" name="type" value="near"/>
            <input type="hidden" name="collectionId" value=""/>
            <input type="hidden" name="loanRecordId" value=""/>
            <input type="hidden" name="userId" value=""/>
            <input type="hidden" name="userNames" value=""/>
            <div class="p30">
                <ul class="gray6">
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl">催收方式：</span>
                        <div class="pl120">
                            <label for="Xianxia-tel">
                                <input name="collectionType" type="radio" value="ZNX" onclick="show(this);"
                                       checked="checked" class="mr5"/>站内信</label>
                            <label for="Xianxia-tel">
                                <input name="collectionType" type="radio" value="DX" onclick="show(this);" class="mr5"/>短信</label>
                            <label for="Xianxia-tel">
                                <input name="collectionType" type="radio" value="YJ" onclick="show(this);" class="mr5"/>邮件</label>
                        </div>
                    </li>
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>发送用户：</span>
                        <div class="pl120">
                            <div class="list02" id="users"></div>
                        </div>
                    </li>
                    
                    <li class="mb15"  id="mail" style="display: none"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>邮箱：</span>
                        <div class="pl120">
                            <textarea name="email" id="email" cols="45" rows="5" class="ww60 border required"><%
                                if (emails != null) {
                                    for (String s : emails) {
                            %><%=s + "\n"%><%
                                    }
                                }
                            %></textarea>
                            <p>如有多个邮箱，请使用“,”[半角逗号]隔开</p>
                            <span tip></span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                    
                    <li class="mb15" id="title"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>标题：</span>
                        <div class="pl120">
                            <input name="resultDesc" type="text" class="text border w200 pl5 required max-length-20"/>
                            <span tip></span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                    
                    <li class="mb15" id="number" style="display: none"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>电话号码</span>
                        <div class="pl120">
                            <textarea name="mobile" id="sms" cols="45" rows="5" class="ww60 border required"><%
                                if (mobiles != null) {
                                    for (String s : mobiles) {
                            %><%=s + "\n"%><%
                                    }
                                }
                            %></textarea>
                            <p>如有多个手机号码，请使用“,”[半角逗号]隔开</p>
                            <span tip></span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>内容：</span>
                        <div class="pl120">
                            <textarea name="comment" id="keyWord" class="ww60 border required max-length-150" style="width: 446px;height: 180px;"></textarea>
                            <span tip>150字以内</span>
                            <span errortip class="red" style="display: none"></span>
                        </div>
                    </li>
                </ul>

                <div class="tc f16">
                    <input type="submit" value="确定" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" fromname="form"/>
                    <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">取消</a> -->
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();"/>
                </div>
            </div>
        </form>
    </div>
</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

    var boxArrays = [];

    $(function () {
        <%if (less30Array != null) {for (int i =0;i<less30Array.length;i++){Less30 less30=less30Array[i];if (less30 == null) {continue;}%>
        $('#tempDatepicker<%=i%>').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                checkDate('<%=i%>');
            }
        });
        $('#tempDatepicker<%=i%>').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%}}%>
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcYq.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, YqList.class)%>";
    }

    function showCs(i) {
        $(".popup_bg").show();
        $("#cs_" + i).show();
    }

    function checkDate(i) {
        //window.setTimeout(function(){
        var dates = $("#tempDatepicker" + i);
        var value = dates.val();
        var $error = dates.nextAll("span[errortip]");
        var $tip = dates.nextAll("span[tip]");
        if ($.trim(value) != "") {
            $error.removeClass("error_tip");
            $error.hide();
            $tip.show();
        }
        //},2000);
    }

    function selectAll() {
        var ischecked = $("#checkAll").attr("checked");
        if (ischecked) {
            $(":checkbox").attr("checked", true);
        } else {
            $(":checkbox").attr("checked", false);
        }
    }

    function selectThis() {
        if (!$("#checkbox").checked) {
            $("#checkAll").attr("checked", false);
        }
        // 获取subcheck的个数
        var chsub = $("input[type='checkbox'][id='checkbox']").length;
        // 获取选中的
        var checkedsub = $("input[type='checkbox'][id='checkbox']:checked").length;
        if (checkedsub == chsub) {
            $("#SelectAll").attr("checked", true);
        }

        if ($("#checkbox").attr("checked")) {
            $("#checkbox").attr("checked", true);
        } else {
            $("#checkbox").attr("checked", false);
        }
    }

    // 线上催收
    function xianshangcuishou() {
    	
    	//手机号码正则表达式
    	var mobileRex=/^(13|14|15|17|18)[0-9]{9}$/;
    	//邮箱地址验证正则表达式
    	var emailRex=/^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$/;
    	
        boxArrays.length = 0;
        $('input[name="checkbox"]:checked').each(function () {
            var userId = $(this).val();
            var userName = $(this).parent().next().next().next().text();
            boxArrays.push({userId: userId, userName: userName});
        });

        if (boxArrays.length == 0) {
            $("#info").html(showInfo("请先选择催收人！", "wrong"));
            return;
        }
        $(".popup_bg").show();
        $("#xscs").show();

        var userIds = "";
        var userNames = "";
        loanRecordIds = "";
        collectionIds = "";
        emails="";
        mobiles="";
        var userInfo = [];

        for (var i = 0; i < boxArrays.length; i++) {
            userNames += boxArrays[i].userName + (i != (boxArrays.length - 1) ? "," : "");
            userInfo = boxArrays[i].userId.split(',');
            if(i<boxArrays.length-1){
            	userIds += userInfo[0] + ",";
                loanRecordIds += userInfo[1] + ",";
                collectionIds += userInfo[2] + ",";
                if(mobileRex.test(userInfo[3])){
                	mobiles	+= userInfo[3]+",";
                }
            	if(emailRex.test(userInfo[4])){
            		emails	+= userInfo[4]+",";
            	}
            }else{
            	userIds += userInfo[0];
                loanRecordIds += userInfo[1];
                collectionIds += userInfo[2];
                if(mobileRex.test(userInfo[3])){
            		mobiles+=userInfo[3];
                }else{
                	//去掉最后一个逗号
                	mobiles=mobiles.substring(0,mobiles.length-1);
                }
                //判断邮箱地址格式是否正确
                if(emailRex.test(userInfo[4])){
                	emails+=userInfo[4];
                }else{
                	//去掉最后一个逗号
                	emails=emails.substring(0, emails.length-1);
                }
            }
        }
        $("#users").text(userNames);
        $('input[name="userId"]').val(userIds);
        $('input[name="userNames"]').val(userNames);
        $('input[name="loanRecordId"]').val(loanRecordIds);
        $('input[name="collectionId"]').val(collectionIds);
        $('textarea[name="mobile"]').val(mobiles);
        $('textarea[name="email"]').val(emails);
    }


    function show(obj) {
        var radioValue = obj.value;
        if (radioValue == "DX")        // 短信
        {
            $("#number").show();
            $("#title").hide();
            $("#mail").hide();
        }
        else if (radioValue == "YJ")  // 邮箱
        {
            $("#mail").show();
            $("#title").show();
            $("#number").hide();
        }
        else                        // 站内信
        {
            $("#title").show();
            $("#number").hide();
            $("#mail").hide();
        }
    }
</script>
</body>
</html>