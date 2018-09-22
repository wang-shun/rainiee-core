<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.XxczglList" %>
<%@page import="com.dimeng.p2p.console.servlets.common.Update" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.shtg.Shtg" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.txcg.Txcg" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.IndexCount" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.ToDoThingsEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptzxyhtj.UserOnline" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.grjkyx.GrjkyxList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.qyjkyx.QyjkyxList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.wsh.TxglList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl.TransferDshList" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ZhList" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <style type="text/css">
        .load-animate-container{ position:fixed; top:0px; left:0px; right:0px; bottom:0px; background:#000;  z-index:99; opacity:0.3;}
        .load-animate-container img{ position:absolute; left:50%; margin-left:-64px; top:50%; margin-top:-64px;}

    </style>
</head>
<%
    IndexCount indexCount = ObjectHelper.convert(request.getAttribute("indexCount"), IndexCount.class);
    boolean isOneLogin = ObjectHelper.convert(request.getAttribute("isOneLogin"), Boolean.class);
    ToDoThingsEntity todoEntity = ObjectHelper.convert(request.getAttribute("todoEntity"), ToDoThingsEntity.class);
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	// 增加对托管的区分 hsp——20160323
    boolean escrowBoolean = true;
    if("FUYOU".equals(escrowFinance)){
        escrowBoolean = false;
    }
%>
<body>
<%
    CURRENT_CATEGORY = "SY";
%>

<!--头部 结束-->

<div class="right-container">
  <div class="viewFramework-body">
    <div class="viewFramework-content"> 
                <!--首页内容-->
                <div class="p20">
                    <div class="index-top-container">
                        <ul class="clearfix">
                            <li class="ww25">
                                <div class="item-container mh120 tc pt50 item-container-by1">
                                    <div class="icon-i h60 w60 p3 index-pic1-icon va-middle"></div>
                                    <div class="display-ib white va-middle">
                                        <h2 class="f36 tc">
                                            <%
                                                if (dimengSession.isAccessableResource(ZhList.class)) {
                                                    String zhListurl = controller.getURI(request, ZhList.class) + "?startTime=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "&endTime=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "&zcType=ZC";
                                            %>
                                            <a class="white link_url" href="<%=zhListurl%>" showObj="ZHGL" data-title="user"><%=indexCount.todayRegisterUser%>
                                            </a>
                                            <%} else {%>
                                            <span><%=indexCount.todayRegisterUser%></span>
                                            <%}%>
                                        </h2>

                                        <p>今日新增用户数(人)</p>
                                    </div>
                                </div>
                            </li>
                            <li class="ww25">
                                <div class="item-container mh120 tc pt50 item-container-by2">
                                    <div class="icon-i h60 w60 p3 index-pic2-icon va-middle"></div>
                                    <div class="display-ib white va-middle">
                                        <h2 class="f36 tc">
                                            <span><%=indexCount.todayLoginUser%></span>
                                        </h2>

                                        <p>今日登录用户数(人)</p>
                                    </div>
                                </div>
                            </li>
                            <li class="ww25">
                                <div class="item-container mh120 tc pt50 item-container-by3">
                                    <div class="icon-i h60 w60 p3 index-pic3-icon va-middle"></div>
                                    <div class="display-ib white va-middle">
                                        <h2 class="f36 tc">
                                            <%if (dimengSession.isAccessableResource(ZhList.class)) {%>
                                            <a class="white link_url"
                                               href="<%=controller.getURI(request, ZhList.class)%>" showObj="ZHGL" data-title="user"><%=indexCount.historyRegisterUser%>
                                            </a>
                                            <%} else {%>
                                            <span><%=indexCount.historyRegisterUser%></span>
                                            <%}%>
                                        </h2>

                                        <p>历史总用户数(人)</p>
                                    </div>
                                </div>
                            </li>
                            <li class="ww25">
                                <div class="item-container mh120 tc pt50 item-container-by4">
                                    <div class="icon-i h60 w60 p3 index-pic4-icon va-middle"></div>
                                    <div class="display-ib white va-middle">
                                        <h2 class="f36 tc">
                                            <%if (dimengSession.isAccessableResource(UserOnline.class)) { %>
                                            <a class="white link_url"
                                               href="<%=controller.getURI(request, UserOnline.class)%>" showObj="PTZXYHTJ" data-title="statistics"><%=indexCount.onlineUser%>
                                            </a>
                                            <%} else { %>
                                            <span><%=indexCount.onlineUser%></span>
                                            <%}%>
                                        </h2>

                                        <p>当前在线用户数(人)</p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>

                <!--代办事项-->
                <div class=" ml40 mr40 main-bg-white">
                    <div class="pr agency-container h400 overflow-h">
                        <div class="agency-left-title tc"><span class="display-b h60 lh60 mt50">待</span><span
                                class="display-b h60 lh60">办</span><span class="display-b h60 lh60">事</span><span
                                class="display-b h60 lh60">项</span><i
                                class="icon-i pa bottom0 left15 w60 h60 agency-icon"></i><i
                                class="icon-i agency-arrow-icon"></i></div>
                        <ul class="clearfix agency-ul-box pt40 ml80">
                            <li>
                                <div class="agency-li-box">
                                    <%
                                        if (dimengSession.isAccessableResource(LoanList.class)) {
                                    %>
                                    <h3 class="gray3 f36"><a
                                            href="<%=controller.getURI(request, LoanList.class)%>?status=DSH" class="link_url" showObj="BDGL" data-title="business">${todoEntity.dshProCount }</a>
                                    </h3>
                                    <%} else { %>
                                    <h3 class="gray3 f36">${todoEntity.dshProCount }</h3>
                                    <%} %>
                                    <p class="gray6 f16">待审核借款项目（个）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(GrjkyxList.class)) { %>
                                        <a href="<%=controller.getURI(request, GrjkyxList.class)%>?loanIntentionState=WCL" class="link_url" showObj="GRJKYX" data-title="business">${todoEntity.dshOwnLoanCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.dshOwnLoanCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">待处理的个人借款意向（个）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(LoanList.class)) { %>
                                        <a href="<%=controller.getURI(request, LoanList.class)%>?status=DFB" class="link_url" showObj="BDGL" data-title="business">${todoEntity.dfbProCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.dfbProCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">待发布的借款项目（个）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(QyjkyxList.class)) { %>
                                        <a href="<%=controller.getURI(request, QyjkyxList.class)%>?loanIntentionState=WCL" class="link_url" showObj="QYJKYX" data-title="business">${todoEntity.dshEnterpriseLoanCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.dshEnterpriseLoanCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">待处理的企业借款意向（个）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(FkshList.class)) { %>
                                        <a href="<%=controller.getURI(request, FkshList.class)%>?fundStatus=1" class="link_url" showObj="FKGL" data-title="finance">${todoEntity.dfkProCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.dfkProCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">待放款的借款项目（个）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(GrList.class)) { %>
                                        <a href="<%=controller.getURI(request, GrList.class)%>?dshFlg=DSH" class="link_url" showObj="GRXX" data-title="user">${todoEntity.dshAuthCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.dshAuthCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">待审核的认证信息（个）</p>
                                </div>
                            </li>
                            <% if(!tg || !escrowFinance.equals("yeepay"))
                            		{ if (escrowBoolean){%>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(TxglList.class)) { %>
                                        <a href="<%=controller.getURI(request, TxglList.class)%>" class="link_url" showObj="TXGL" data-title="finance">${todoEntity.txTrialCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.txTrialCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">提现初审（笔）</p>
                                </div>
                            </li>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(Shtg.class)) { %>
                                        <a href="<%=controller.getURI(request, Shtg.class)%>" class="link_url"  showObj="TXGL" data-title="finance">${todoEntity.txReviewCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.txReviewCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">提现复审（笔）</p>
                                </div>
                            </li>
                            <%} %>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(XxczglList.class)) { %>
                                        <a href="<%=controller.getURI(request, XxczglList.class)%>?status=DSH" class="link_url" showObj="XXCZGL" data-title="finance">${todoEntity.underLineChargingCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.underLineChargingCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">线下充值审核（笔）</p>
                                </div>
                            </li>
                            <%} %>
                            <li>
                                <div class="agency-li-box">
                                    <h3 class="gray3 f36">
                                        <%if (dimengSession.isAccessableResource(TransferDshList.class)) { %>
                                        <a href="<%=controller.getURI(request, TransferDshList.class)%>" class="link_url" showObj="ZQZRGL" data-title="business">${todoEntity.assignmentCount }</a>
                                        <%} else { %>
                                        <span>${todoEntity.assignmentCount }</span>
                                        <%}%>
                                    </h3>

                                    <p class="gray6 f16">债权转让审核（笔）</p>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>

                <!--首页内容 结束-->
            </div>
        </div>
       
    </div>
    <!--右边内容 结束-->

<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (isOneLogin || !StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(parent.frames["topFrame"].document).find("div.updatepwd").show();
</script>
<div class="popup_bg"></div>
<div class="popup-box" id="updatePass" style="margin: -120px 0 0 -220px;">
    <div class="popup-title-container">
        <span class="fl pl15">修改密码</span>
    </div>
    <input id="newPasswordRegexId" type="hidden"
           value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
    <input id="passwordRegexContentId" type="hidden"
           value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>

    <form action="<%=controller.getURI(request, Update.class)%>?isLoad=true" method="post" class="form1"
          onsubmit="return onSubmit();">
        <div class="border">
            <div class="content-container pt40 pr40 pb20">
                <ul class="gray6">
                    <li class="mb10">
                        <div class="tc f16">
                            为保障账户安全，首次登录请修改您的密码！
                        </div>
                    </li>
                    <li class="mb10">
                        <span class="display-ib w200 tr mr5"><span class="red">*</span>原密码：</span>
                        <input id="oldPassword" type="password" class="text border required" name="oldPassWord"/>
                        <span class="display-b pl200 ml10" tip></span>
                        <span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
                    </li>
                    <li class="mb10">
                        <span class="display-ib w200 tr mr5"><span class="red">*</span>新密码：</span>
                        <input id="passwordOne" type="password"
                               class="text border required cpassword-a min-length-6 max-length-20" name="newPassWord1"/>
                        <span class="display-b pl200 ml10" tip></span>
                        <span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
                    </li>
                    <li class="mb10">
                        <span class="display-ib w200 tr mr5"><span class="red">*</span>重复新密码：</span>
                        <input id="passwordTwo" type="password" class="text border required cpassword-b" name="newPassWord2"/>
                        <span class="display-b pl200 ml10" tip></span>
                        <span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
                    </li>
                    <li class="mb10" id="msginfo">
                        <div class="display-b pl200 ml10" style="color: red">
                            <%
                                StringHelper.filterHTML(out, message);
                            %>
                        </div>
                    </li>
                    <li class="mb10">
                        <div class="tc f16">
                            <input type="submit" name="button2" id="button2" value="确认" fromname="form1"
                                   class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"/>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<%
    }
%>
<%
Boolean isLoad = BooleanParser.parse(request.getParameter("isLoad"));
    if(!isLoad && !isOneLogin){
%>
<div class="load-animate-container tc">
    <img src="<%=controller.getStaticPath(request)%>/images/loadanimate2.gif"/>
</div>
<%}%>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
</body>
<script type="text/javascript">
    function noRea() {
//alert('权限不足');
        $(this).attr('style', 'color:gray!important;white-space:nowrap; ');
    }
    $(function () {
    	parent.frames["ContentFrame1"].cols = "0,*";
        //给待办事项判断，如果为0，则置灰
        $.each($(".dbsx > a"), function (node, index) {
            var count = $(this).text();
            if (count == 0) {
                $(this).attr('style', 'color:gray!important;white-space:nowrap; ');
            }
        });
        var index = $(parent.frames["topFrame"].document).find("a[data-title='index']");
        if(!index.hasClass("select-a")){
        	index.addClass("select-a");
        }
        $("input[type='password']").focus(function(){
            $("#msginfo").hide();
        });
    });

    $(function(){
		$("#passwordOne").keydown(function(d){
			var c = d.keyCode || d.charCode;
            if (c==32) {
                d.preventDefault();
            }
		});
		$("#passwordTwo").keydown(function(d){
			var c = d.keyCode || d.charCode;
            if (c==32) {
                d.preventDefault();
            }
		});
	});
    
    function onSubmit() {
        var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
        var key = RSAUtils.getKeyPair(exponent, '', modulus);
        $("#oldPassword").val(RSAUtils.encryptedString(key, $("#oldPassword").val()));
        $("#passwordOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
        $("#passwordTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
        return true;
    }

    window.onload = function(){
        var flag = <%=isOneLogin%>;
        if(!flag)
        {
            $(parent.frames["topFrame"].document).find("div.updatepwd").hide();
        }
    }
</script>
</html>