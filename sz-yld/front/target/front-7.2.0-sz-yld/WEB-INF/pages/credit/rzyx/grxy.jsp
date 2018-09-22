<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.front.servlets.Region" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.rzyx.Grxy" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<form action="<%=controller.getURI(request, Grxy.class)%>" class="form1" method="post">
    <div class="contain clearfix">
        <div class="want_loan"><span>个人信用融资</span></div>
        <div class="contain_main want">
            <div class="tips">
                <%=configureProvider.format(HTMLVariable.GRXYRZSQTS)%>
            </div>
            <div class="pub_title mb30">借款意向填写</div>
            <div class="loan_con clearfix">
                <ul>
                    <%
                        String ermsg = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(ermsg)) {
                    %>
                    <li style="color: rgb(2, 196, 2);border: 1px solid rgb(3, 214, 3);padding: 10px;text-align: center;"><%
                        StringHelper.filterHTML(out, ermsg);%></li>
                    <%} %>
                    <li>
                        <div class="tit"><i class="red">*</i>联系人</div>
                        <div class="info">
                            <input name="F03" type="text" class="put01 required max-length-32"/>

                            <p tip>不超过32个字</p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <li>
                        <div class="tit"><i class="red">*</i>手机号码</div>
                        <div class="info">
                            <input name="F04" type="text" class="put01 required phonenumber"/>

                            <p tip></p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <li>
                        <div class="tit"><i class="red">*</i>意向融资金额</div>
                        <div class="info">
                            <input name="F06" type="text" class="put01 isint mulriple-100"/>元
                            <p tip></p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <li>
                        <div class="tit"><i class="red">*</i>所在城市</div>
                        <div class="info">
                            <select name="sheng" style="width:150px;" class="sel">
                            </select>
                            <select name="shi" style="width:200px;" class="sel">
                            </select>
                            <select name="xian" style="width:200px;" class="sel required">
                            </select>

                            <p tip></p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>
                    <li>
                        <div class="tit">预期筹款期限</div>
                        <div class="info"><select name="F09" class="sel">
                            <option value="">全部</option>
                            <option value="7天之内">7天之内</option>
                            <option value="15天之内">15天之内</option>
                            <option value="15-30天">15-30天</option>
                            <option value="1-3个月">1-3个月</option>
                            <option value="其他期限">其他期限</option>
                        </select></div>
                    </li>
                    <li>
                        <div class="tit"><i class="red">*</i>借款描述<br/>(20-500字)</div>
                        <div class="info"><textarea name="F10" cols="" rows=""
                                                    class="area required min-length-20 max-length-500"></textarea>

                            <p tip></p>

                            <p errortip class="" style="display: none"></p>
                        </div>
                    </li>

                    <li>
                        <div class="tit">&nbsp;</div>
                        <div class="info">
                            <input type="hidden" name="F15" value="F"/>
                            <input type="hidden" name="F16" value="F"/>
                            <input type="hidden" name="F17" value="F"/>
                            <input type="submit" class="btn btn01 sumbitForme" fromname="form1" value="提  交"/>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</form>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<div class="dialog w510" style="margin:-150px 0 0 -255px;" id="errorDiv">
    <div class="dialog_close fr"><a href="javascript:void(0)" onclick="$('#errorDiv').hide()"></a></div>
    <div class="con clearfix">
        <div class="d_perfect fl"></div>
        <div class="info fr">
            <%StringHelper.filterHTML(out, warnMessage); %>
        </div>
        <div class="clear"></div>
        <div class="dialog_btn"><a href="javascript:void(0)" class="btn btn01" onclick="$('#errorDiv').hide()">确认</a>
        </div>
    </div>
</div>
<%} %>
<%@include file="/WEB-INF/include/footer.jsp" %>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getURI(request, Region.class)%>"></script>
</body>
</html>
