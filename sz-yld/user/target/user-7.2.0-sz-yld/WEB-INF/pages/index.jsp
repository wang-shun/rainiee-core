<%@page import="com.dimeng.p2p.S61.entities.T6147"%>
<%@page import="com.dimeng.p2p.S61.enums.T6125_F05"%>
<%@page import="com.dimeng.p2p.S62.enums.*" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03" %>
<%@page import="com.dimeng.p2p.S63.enums.T6342_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6356_F04" %>
<%@page import="com.dimeng.p2p.account.user.service.FxbyjManage" %>
<%@page import="com.dimeng.p2p.account.user.service.IndexManage" %>
<%@page import="com.dimeng.p2p.account.user.service.MyRewardManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.*" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.BidManage" %>
<%@page import="com.dimeng.p2p.repeater.donation.GyLoanManage" %>
<%@page import="com.dimeng.p2p.repeater.donation.entity.GyLoanStatis" %>
<%@page import="com.dimeng.p2p.repeater.policy.RiskQuesManage" %>
<%@page import="com.dimeng.p2p.repeater.score.SetScoreManage" %>
<%@page import="com.dimeng.p2p.repeater.score.UserCenterScoreManage" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.SetScore" %>
<%@page import="com.dimeng.p2p.user.servlets.AbstractUserServlet" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.ByjCharge" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.ByjChargeOut" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.Repaying" %>
<%@page import="com.dimeng.p2p.user.servlets.fxbyj.Dbywmx" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.donation.DonationList" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.wdzq.Hszdzq" %>
<%@page import="com.dimeng.p2p.user.servlets.mall.MyScore" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<%configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<%
    CURRENT_CATEGORY = "WDWDW";
    CURRENT_SUB_CATEGORY = "WDWDW";
    IndexManage manage = serviceSession.getService(IndexManage.class);
    MyRewardManage myservice = serviceSession.getService(MyRewardManage.class);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("type", T6340_F03.interest);
    params.put("useStatus", T6342_F04.WSY);
    // 未使用加息券
    int unUserJxqCount = myservice.getJxqCount(params);

    Map<String, Object> paramses = new HashMap<String, Object>();
    paramses.put("type", T6340_F03.redpacket);
    paramses.put("useStatus", T6342_F04.WSY);
    // 未使用的红包金额
    BigDecimal hbJe = myservice.getHbAmount(paramses);
    //账户信息
    UserBaseInfo userBaseInfo = manage.getUserBaseInfo();
    String safeStr = "极低";
    if (userBaseInfo == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    if (userBaseInfo.safeLevel == 100) {
        safeStr = "极高";
    } else if (userBaseInfo.safeLevel == 75) {
        safeStr = "高";
    } else if (userBaseInfo.safeLevel == 50) {
        safeStr = "中等";
    } else if (userBaseInfo.safeLevel == 25) {
        safeStr = "低";
    }
    Notice notice = manage.getNotice();
    //借款负债
    BigDecimal loanAmount = manage.getLoanAmount();

    TenderAccount tenderAccount = manage.getTenderAccount();
    LoanAccount[] loanAccount = manage.getLoanAccount();
    FinancialPlan financialPlan = manage.getFinancialPlan();
    Bdlb[] bids = manage.getBids();
    UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
    T6110 user1 = uManage1.getUserInfo(serviceSession.getSession().getAccountId());

    GyLoanManage gymanage = serviceSession.getService(GyLoanManage.class);
    GyLoanStatis gyStatis = gymanage.gyLoanStatisticsByUid(serviceSession.getSession().getAccountId());
    String safetymesgView = configureProvider.format(URLVariable.USER_ZRR_NCIIC);
    if (user1.F06 == T6110_F06.FZRR) {
        // 安全信息
        safetymesgView = configureProvider.format(URLVariable.COM_FZRR);
    }

    // 是否托管项目
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    UserManage userManage = serviceSession.getService(UserManage.class);
    String usrCustId = userManage.getUsrCustId();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    UserLog userLog = userManage.getLastLoginTime(serviceSession.getSession().getAccountId());
    String lastTime = userLog == null ? "无" : sdf.format(userLog.F03);
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    String sayHi = "";
    switch (hour) {
        case 23:
        case 0:
        case 1:
        case 2:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_ZY);
            break;
        case 3:
        case 4:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_LC);
            break;
        case 5:
        case 6:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_ZS);
            break;
        case 7:
        case 8:
        case 9:
        case 10:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_SW);
            break;
        case 11:
        case 12:
        case 13:
        case 14:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_ZW);
            break;
        case 15:
        case 16:
        case 17:
        case 18:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_XW);
            break;
        case 19:
        case 20:
        case 21:
        case 22:
            sayHi = configureProvider.getProperty(SystemVariable.INDEX_GREETING_WS);
            break;
    }

    UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
    int usableScore = userCenterScore.getUsableScore();
    String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);

    //查询积分赠送规则
    SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
    SetScore setScore = setScoreManage.getSetScore();
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);

    Boolean isOpenRisk = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
    Boolean isAssessment = false;
    if(isOpenRisk) {
        RiskQuesManage riskQuesManage = serviceSession.getService(RiskQuesManage.class);
        T6147 t6147 = riskQuesManage.getMyRiskResult();
        if(null != t6147){
            isAssessment = true;
        }
    }
%>
<body>
<!--顶部、头部-->
<%@include file="/WEB-INF/include/header.jsp" %>
<!--顶部、头部-->

<script type="text/javascript">
    var safetymesgView = '<%=safetymesgView%>';
    function fmoney(s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        var f = s < 0 ? "-" : ""; //判断是否为负数
        s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
        var l = s.split(".")[0].split("").reverse(),
                r = s.split(".")[1];
        var t = "";
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return f + t.split("").reverse().join("") + "." + r.substring(0, 2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
    }

    function toSign(){
        $.ajax({
            type:"post",
            dataType:"json",
            url:"/user/sign.htm",
            success:function(data){
            	
            	if(data.msg != "undefined" && data.msg !=null && data.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(data.msg,"error",loginUrl));
            		return;
    			}
            	
                if (data.isSigned) {
                    $("#signSpan").html("<a href=\"javascript:void(0)\" class=\"btn03 btn03_gray mt10 fl\">已签到</a>");
                    if(parseInt(data.giveScore)!=0){
                        $("#info").html(showDialogInfo("您已获得"+data.giveScore+"积分", "successful"));
                        $("div.popup_bg").show();
                        $("#usableScore").text(data.usableScore);
                    }
                }
            }
        });
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/echarts/echarts.js"></script>
<script type="text/javascript">
    require.config({
        paths: {
            echarts: '<%=controller.getStaticPath(request)%>/js/echarts'
        }
    });
</script>

<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <div class="login_time">上次登录：<%=lastTime%>
        </div>
        <!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <!--左菜单-->
        <!--右边内容-->
        <div class="r_main">
            <div class="account_info">
           <%if(StringUtils.isBlank(usrCustId)){%>
            	<div class="border_b charitable_hosting clearfix">
           <%--  	 <%if(userBaseInfo.auth == false){ %>  --%>
                <div class="fl"><span class="va_m">您还未进行协议支付认证，账户认证成功后才能进行投资理财操作！</span></div>
              <%--   <a href="<%configureProvider.format(out,URLVariable.OPEN_ESCROW_GUIDE);%>" class="btn01 fr va_m">开通托管账户</a> --%>
                  <a href="<%configureProvider.format(out,URLVariable.OPEN_ESCROW_GUIDE);%>" class="btn01 fr va_m">协议支付签约</a>
             <%--       <%} %>  --%>
                </div>
            <%}else{%>
            	<div class="border_b charitable_hosting clearfix">
                <div class="fl"><span class="va_m">您已进行协议支付认证，可以进行投资理财操作！</span></div>
             	 <%--   <a href="<%configureProvider.format(out,URLVariable.OPEN_ESCROW_GUIDE);%>" class="btn01 fr va_m">开通托管账户</a> --%>
                  <a href="javascript:void(0)" onclick="showConfirmInfo('您确认进行协议支付解约操作？');" class="btn01 fr va_m">协议支付解约</a>
                </div>
            <%} %>
                <div class="top clearfix">
                    <div class="greetings" title="<%=sayHi%>"><%StringHelper.truncation(out, sayHi, 20);%></div>
                    <ul class="certification">
                        <li>
                            <i title="绑定手机，<%=userBaseInfo.phone?"已绑定":"未绑定"%>"
                               class="icon01 <%=userBaseInfo.phone?"cur ":""%> click_id">
                                <%if (!tg && !userBaseInfo.phone && T6110_F07.HMD != t6110.F07) {%>
                                <div class="hover_tips_con" style="display:block;">
                                    <div class="border">
                                        <div class="arrow"></div>
                                        <%-- <a href="javascript:void(0);" class="close"></a>--%>
                                        	为了您的账户更加的安全，请您绑定手机。<br/>
                                        <a href="<%=safetymesgView%>" class="highlight">立即认证</a>
                                    </div>
                                </div>
                                <%}%>
                            </i>
                        </li>
                        <li>
                            <i title="实名认证，<%=userBaseInfo.realName?"已认证":"未认证"%>"
                               class="icon02 <%=userBaseInfo.realName?"cur ":""%> click_id">
                                <%if (!tg && userBaseInfo.phone && !userBaseInfo.realName && T6110_F07.HMD != t6110.F07) {%>
                                <div class="hover_tips_con" style="display:block;">
                                    <div class="border">
                                        <div class="arrow"></div>
                                        <%--<a href="javascript:void(0);" class="close"></a>--%>
                                       	 进行实名认证后，您才可以进行投资借款。<br/>
                                        <a href="<%=safetymesgView%>" class="highlight">立即认证</a>
                                    </div>
                                </div>
                                <%}%>
                            </i>
                        </li>
                        <%
                            String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
                            if("true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
                        %>
                        <li>
                            <i title="交易密码，<%=userBaseInfo.withdrawPsw?"已设置":"未设置"%>"
                               class="icon03 <%=userBaseInfo.withdrawPsw?"cur ":""%> click_id">
                                <%if (!tg && userBaseInfo.phone && userBaseInfo.realName && !userBaseInfo.withdrawPsw && T6110_F07.HMD != t6110.F07) {%>
                                <div class="hover_tips_con" style="display:block;">
                                    <div class="border">
                                        <div class="arrow"></div>
                                        <%-- <a href="javascript:void(0);" class="close"></a>--%>
                                       	 为了您的资金更加安全，请您设置交易密码。<br/>
                                        <a href="<%=safetymesgView%>" class="highlight">立即认证</a>
                                    </div>
                                </div>
                                <%}%>
                            </i>
                        </li>
                        <%} %>
                        <li>
                            <i title="绑定邮箱，<%=userBaseInfo.email?"已绑定":"未绑定"%>"
                               class="icon04 <%=userBaseInfo.email?"cur ":""%> click_id">
                                <%
                                    boolean bl = false;
                                    if(!tg){
                                        bl = userBaseInfo.phone && userBaseInfo.realName && userBaseInfo.withdrawPsw && !userBaseInfo.email && T6110_F07.HMD != t6110.F07;
                                    }
                                    if (bl) {%>
                                <div class="hover_tips_con" style="display:block;">
                                    <div class="border">
                                        <div class="arrow"></div>
                                        <%--<a href="javascript:void(0);" class="close"></a>--%>
                                        	为了您的账户更加的安全，请您绑定邮箱。<br/>
                                        <a href="<%=safetymesgView%>" class="highlight">立即认证</a>
                                    </div>
                                </div>
                                <%}%>
                            </i>
                        </li>
                        <%if (tg) {%>
                        <li>
                            <i title="第三方注册，<%=StringHelper.isEmpty(usrCustId)?"未注册":"已注册"%>"
                               class="icon05 <%=StringHelper.isEmpty(usrCustId)?"":"cur "%> click_id">
                                <%--<%if (!tg && userBaseInfo.phone && (("yeepay".equals(escrow.toLowerCase()) && !StringHelper.isEmpty(userBaseInfo.idCard)) || userBaseInfo.realName) && userBaseInfo.email && T6110_F07.HMD != t6110.F07 && StringHelper.isEmpty(usrCustId)) {%>
                                <div class="hover_tips_con" style="display:block;">
                                    <div class="border">
                                        <div class="arrow"></div>
                                        &lt;%&ndash; <a href="javascript:void(0);" class="close"></a>&ndash;%&gt;
                                       	 为了您更方便投资，请您注册第三方账户。<br/>
                                        <a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>"
                                           target="_blank" class="highlight">立即注册</a>
                                    </div>
                                </div>
                                <%}%>--%>
                            </i>
                        </li>
                        <%}%>
                    </ul>
                    <div class="level fl ml10">安全等级<span title="<%=safeStr%>" class="progress_bg">
                    <span class="progress" style="width:<%=userBaseInfo.safeLevel%>%;"></span></span><%=safeStr%>
                    </div>
                </div>
                <!-- 用户中心 start -->
                <div class="clearfix">
                <%
                    if(isOpenRisk && !isAssessment && user1.F06 == T6110_F06.ZRR){
                %>
                <div class="pg-tip"><i class="pg-ico"></i>您还未进行过投资风险承受能力评估，请<a href="/user/policy/riskAssessment.htm" class="blue">立即评估</a></div>
                <%}%>
                    <% if (user1.F06 == T6110_F06.ZRR) {
                        BigDecimal tzzc = tenderAccount.yxzc.add(tenderAccount.sbzc);
                        BigDecimal zhkyye = userBaseInfo.balance.add(userBaseInfo.freezeFunds);
                        if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){
                            zhkyye = zhkyye.add(userBaseInfo.fxbzj);
                        }
                        BigDecimal zhjzc = zhkyye.add(tzzc).subtract(loanAmount);

                    %>
                    <div class="info_l">
                        <div class="chart" id="chart" style="width:520px;height:400px;"></div>
                        <script type="text/javascript">
                            require(
                                    [
                                        'echarts',
                                        'echarts/chart/pie'
                                    ],
                                    function (ec) {
                                        var myChart = ec.init(document.getElementById('chart'));
                                        var option = {
                                            tooltip: {
                                                trigger: 'item',
                                                textStyle: {
                                                    fontSize: '12'
                                                },
                                                formatter: function (a, b, c) {
                                                    return b.substring(0, b.length - 2) + "元" + "<br/>" + a.name + ": " + fmoney(a.value, 2) + "元";
                                                }
                                            },
                                            calculable: true,
                                            series: [
                                                {
                                                    name: '账户净资产:<%=Formater.formatAmount(zhjzc)%>',
                                                    type: 'pie',
                                                    radius: ['40%', '55%'],
                                                    itemStyle: {
                                                        normal: {
                                                            label: {
                                                                show: true,
                                                                formatter: function (a) {
                                                                    return a.name + "\n" + fmoney(a.value, 2) + "元";
                                                                },
                                                                textStyle: {
                                                                    fontSize: '15',
                                                                    fontWeight: 'bold'
                                                                }
                                                            },
                                                            labelLine: {
                                                                show: true,
                                                                length: 15
                                                            }
                                                        },
                                                        emphasis: {
                                                            label: {
                                                                show: true,
                                                                position: 'center',
                                                                textStyle: {
                                                                    fontSize: '20',
                                                                    fontWeight: 'bold'
                                                                }
                                                            }
                                                        }
                                                    },
                                                    data: [
                                                        {
                                                            value:<%=zhkyye%>,
                                                            name: '账户余额'
                                                        },
                                                        {value:<%=tzzc%>, name: '理财资产'},
                                                        {value:<%=loanAmount%>, name: '借款负债'}
                                                    ]
                                                }
                                            ]
                                        };
                                        myChart.setOption(option);
                                        var config = require('echarts/config');
                                        var _zr = myChart.getZrender();//获取zrender示例
                                        var TextShape = require('zrender/shape/Text');//调用zrender里面的方法来解决
                                        var textShape = new TextShape({
                                            style: {
                                                x: _zr.getWidth() / 2,
                                                y: _zr.getHeight() / 2,
                                                color: '#666',
                                                text: '账户净资产\n' + fmoney(<%=zhjzc%>, 2),
                                                textAlign: 'center',
                                                textFont: 'bold 15px verdana'
                                            }
                                        });
                                        _zr.addShape(textShape);//鼠标移到图标上时，将textshape的颜色设置的和背景一样
                                        myChart.on(config.EVENT.HOVER, function () {
                                            _zr.modShape(textShape.id, {style: {color: '#fff'}});
                                        });
                                        myChart.on(config.EVENT.MOUSEOUT, function () {
                                            _zr.modShape(textShape.id, {style: {color: '#666'}});
                                        });
                                        myChart.refresh();
                                    }
                            );
                        </script>
                        <div class="calculate">账户净资产 ＝ 账户余额
			              <span class="hover_tips">
			                <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">账户余额 = 可用金额 + 冻结金额<%if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){%> + 风险保证金<%}%></div>
                            </div>
			              </span>
                            	＋ 理财资产
			              <span class="hover_tips">
			                <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">理财资产 = 待收本金 + 待收利息 + 待收罚息</div>
                            </div>
			              </span>
                            	－ 借款负债
			              <span class="hover_tips">
			                <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">借款负债 = 待还本金 + 待还利息 + 逾期罚息</div>
                            </div>
			              </span>
                        </div>
                    </div>

                    <div class="info_r">
                        <ul class="money">
                            <li>可用余额(元)：<span class="orange"><%=Formater.formatAmount(userBaseInfo.balance)%></span>
                            </li>
                            <li>冻结金额(元)：<%=Formater.formatAmount(userBaseInfo.freezeFunds)%>
                            </li>
                            <%if(is_mall){%>
                            <li><a href="<%=controller.getViewURI(request, MyScore.class) %>">可用积分(分)：<span id="usableScore"><%=usableScore %></span></a></li>
                            <%} %>
                            <%
                                if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){
                            %>
                            <li>风险保证金(元)：<span id="dbfxbzj"><%=Formater.formatAmount(userBaseInfo.fxbzj) %></span></li>
                            <%}%>
                        </ul>
                        <ul class="vouchers">
                            <% if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.REDPACKET_INTEREST_SWITCH))){%>
                            <li><i class="icon01"></i><a
                                    href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.reward.Wdhb.class) %>">红包(元)：<%=Formater.formatAmount(hbJe)%>
                            </a></li>
                            <li><i class="icon02"></i><a
                                    href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.reward.Wdjxq.class) %>">加息券(张)：<%=unUserJxqCount%>
                            </a></li>
                            <%}%>
                            <li><i class="icon03"></i><a
                                    href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.spread.Wdtyj.class) %>">体验金(元)：<%=Formater.formatAmount(userBaseInfo.experienceAmount)%>
                            </a></li>
                        </ul>
                        <div class="tr mt20 clearfix mb20">
                            <a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>" class="btn01 fl">充值</a>
                            <%if (T6110_F07.HMD == t6110.F07) { %>
                            <a class="btn02 btn_gray ml10 fl">提现</a>
                            <%} else { %>
                            <a href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>" class="btn02 ml10 mr5 fl">提现</a>
                            <%}%>
                            <span id="signSpan">
                            <%if(is_mall && setScore.signCheckbox.equals(T6356_F04.on.name())){
                                boolean signed = (user1.F15 == null ? false : DateTimeParser.format(new Date(), "yyyyMMdd").equals(DateTimeParser.format(user1.F15, "yyyyMMdd")));
                                if(T6110_F07.HMD != t6110.F07){ %>
	                            <%if(!signed){%>
	                            <a href="javascript:void(0)" onclick="toSign()" class="btn03 mt10 fl">签到</a>
	                            <%} else{%>
	                            <a href="javascript:void(0)" class="btn03 btn03_gray mt10 fl">已签到</a>
	                            <%} %>
                            <%} else{%>
                            	<a href="javascript:void(0)" class="btn03 btn03_gray mt10 fl">签到</a>
                            <%} }%>
                            </span>
                            <a href="<%=controller.getViewURI(request, Repaying.class)%>" class="btn03 mt10 mr5 <%if(is_mall && setScore.signCheckbox.equals(T6356_F04.on.name())){%>ml10<%}%> fl">还款</a>
                            <%
                                if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){
                            %>
                            <a href="<%=controller.getViewURI(request, ByjCharge.class)%>" class="btn03 mt10 fl <%if(!is_mall || !setScore.signCheckbox.equals(T6356_F04.on.name())){%>ml5<%}%>" >转入保证金</a>
                            <a href="<%=controller.getViewURI(request, ByjChargeOut.class)%>" class="btn03 mt10 mr5 <%if(is_mall && setScore.signCheckbox.equals(T6356_F04.on.name())){%>ml5<%}%> fl">转出保证金</a>
                            <%}%>
                        </div>
                    </div>
                <%}%>
                <!-- 用户中心 end -->
                </div>
                <% if (user1.F06 == T6110_F06.FZRR) { %>
                <!-- 企业 机构  start-->
                <div class="user_mod">
                    <div class="chart tc" id="chart" style="height:400px;"></div>
                    <%
                        if (user1.F10 == T6110_F10.F) {
                            BigDecimal tzzc = tenderAccount.yxzc.add(tenderAccount.sbzc);
                            BigDecimal zhkyye = userBaseInfo.balance.add(userBaseInfo.freezeFunds);
                            if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){
                                zhkyye = zhkyye.add(userBaseInfo.fxbzj);
                            }
                            BigDecimal zhjzc = zhkyye.subtract(loanAmount);
                            if(user1.F18 == T6110_F18.S){
                                zhjzc = zhjzc.add(tzzc);
                            }

                    %>
                    <script type="text/javascript">
                        require(
                                [
                                    'echarts',
                                    'echarts/chart/pie'
                                ],
                                function (ec) {
                                    var myChart = ec.init(document.getElementById('chart'));
                                    var option = {
                                        tooltip: {
                                            trigger: 'item',
                                            textStyle: {
                                                fontSize: '12'
                                            },
                                            formatter: function (a, b, c) {
                                                return b.substring(0, b.length - 2) + "元" + "<br/>" + a.name + ": " + fmoney(a.value, 2) + "元";
                                            }
                                        },
                                        calculable: true,
                                        series: [
                                            {
                                                name: '账户净资产:<%=Formater.formatAmount(zhjzc)%>',
                                                type: 'pie',
                                                radius: ['40%', '55%'],
                                                itemStyle: {
                                                    normal: {
                                                        label: {
                                                            show: true,
                                                            formatter: function (a) {
                                                                return a.name + "\n" + fmoney(a.value, 2) + "元";
                                                            },
                                                            textStyle: {
                                                                fontSize: '15',
                                                                fontWeight: 'bold'
                                                            }
                                                        },
                                                        labelLine: {
                                                            show: true,
                                                            length: 15
                                                        }
                                                    },
                                                    emphasis: {
                                                        label: {
                                                            show: true,
                                                            position: 'center',
                                                            textStyle: {
                                                                fontSize: '20',
                                                                fontWeight: 'bold'
                                                            }
                                                        }
                                                    }
                                                },
                                                data: [
                                                    {
                                                        value:<%=zhkyye%>,
                                                        name: '账户余额'
                                                    },
                                                    <%if(user1.F18 == T6110_F18.S){ %>
                                                    {value:<%=tzzc%>, name: '理财资产'},
                                                    <%}%>
                                                    {value:<%=loanAmount%>, name: '借款负债'}
                                                ]
                                            }
                                        ]
                                    };
                                    myChart.setOption(option);
                                    var config = require('echarts/config');
                                    var _zr = myChart.getZrender();//获取zrender示例
                                    var TextShape = require('zrender/shape/Text');//调用zrender里面的方法来解决
                                    var textShape = new TextShape({
                                        style: {
                                            x: _zr.getWidth() / 2,
                                            y: _zr.getHeight() / 2,
                                            color: '#666',
                                            text: '账户净资产\n' + fmoney(<%=zhjzc%>, 2),
                                            textAlign: 'center',
                                            textFont: 'bold 15px verdana'
                                        }
                                    });
                                    _zr.addShape(textShape);//鼠标移到图标上时，将textshape的颜色设置的和背景一样
                                    myChart.on(config.EVENT.HOVER, function () {
                                        _zr.modShape(textShape.id, {style: {color: '#fff'}});
                                    });
                                    myChart.on(config.EVENT.MOUSEOUT, function () {
                                        _zr.modShape(textShape.id, {style: {color: '#666'}});
                                    });
                                    myChart.refresh();
                                }
                        );
                    </script>
                    <div class="calculate">账户净资产 ＝ 账户余额
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">账户余额 = 可用金额 + 冻结金额<%if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){%> + 风险保证金<%}%></div>
                            </div>
                        </span>
                        <%if(user1.F18 == T6110_F18.S){ %>
                        ＋ 理财资产
			              <span class="hover_tips">
			                <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">理财资产 = 待收本金 + 待收利息 + 待收罚息</div>
                            </div>
			              </span>
			              <%} %>
                        － 借款负债
                         <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">借款负债 = 待还本金 + 待还利息 + 逾期罚息</div>
                            </div>
                        </span>
                    </div>
                    <%} else if (user1.F10 == T6110_F10.S) {%>
                    <script type="text/javascript">
                        require(
                                [
                                    'echarts',
                                    'echarts/chart/pie'
                                ],
                                function (ec) {
                                    var myChart = ec.init(document.getElementById('chart'));
                                    var option = {
                                        calculable: true,
                                        series: [
                                            {
                                                type: 'pie',
                                                radius: ['40%', '55%'],
                                                itemStyle: {
                                                    normal: {
                                                        label: {
                                                            show: true,
                                                            formatter: function (a) {
                                                                return a.name + "\n" + fmoney(a.value, 2) + "元";
                                                            },
                                                            textStyle: {
                                                                fontSize: '15',
                                                                fontWeight: 'bold'
                                                            }
                                                        },
                                                        labelLine: {
                                                            show: true
                                                        }
                                                    },
                                                    emphasis: {
                                                        label: {
                                                            show: false
                                                        }
                                                    }
                                                },
                                                data: [
                                                    {value:<%=userBaseInfo.balance%>, name: '可用余额'},
                                                    {value:<%=userBaseInfo.freezeFunds%>, name: '冻结金额'},
                                                    {value:<%=userBaseInfo.fxbzj%>, name: '风险保证金'}
                                                ]
                                            }
                                        ]
                                    };
                                    myChart.setOption(option);
                                }
                        );
                    </script>
                    <%}%>

                    <div class="border_t pt30 mt10">
                        <span class="mr50 ml30">可用余额(元)：<span
                                class="orange"><%=Formater.formatAmount(userBaseInfo.balance)%></span></span>
                        <span class="mr50">冻结金额(元)：<%=Formater.formatAmount(userBaseInfo.freezeFunds)%></span>
                        <%if(user1.F10 == T6110_F10.S || (is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG))){%>
                        风险保证金(元)：<%=Formater.formatAmount(userBaseInfo.fxbzj)%>
                        <%}%>
                    </div>
                    <div class="mt20 ml30 mb10">
                        <a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>" class="btn01">充值</a>
                        <%if (T6110_F07.HMD == t6110.F07) { %>
                        <a class="btn_gray btn02 ml10">提现</a>
                        <%} else { %>
                        <a href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>" class="btn02 ml10">提现</a>
                        <%}%>
                        <%if (user1.F10 == T6110_F10.S) {%>
                        <a href="<%=controller.getViewURI(request, ByjCharge.class)%>" class="btn03 ml10">转入保证金</a>
                        <a href="<%=controller.getViewURI(request, ByjChargeOut.class)%>" class="btn03 ml10">转出保证金</a>
                        <%} else { %>
                        <a href="<%=controller.getViewURI(request, Repaying.class)%>" class="btn03 ml10 ">还款</a>
                        <%
                            if(is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG)){
                        %>
                        <a href="<%=controller.getViewURI(request, ByjCharge.class)%>" class="btn03 ml10">转入保证金</a>
                        <a href="<%=controller.getViewURI(request, ByjChargeOut.class)%>" class="btn03 ml10">转出保证金</a>
                        <%}%>
                        <%}%>
                    </div>
                </div>
                <!-- 企业 机构  end-->
                <%}%>
            </div>
            <%//开关判断，为false，则不显示公益标
                if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){%>
            <div class="border_t15 charitable_account clearfix">
                <div class="icon"></div>
                <div class="fl"><span class="f18 gray3">公益账户</span> <span
                        class="ml15 mr100">已捐助的总金额 ￥<%=Formater.formatAmount(gyStatis.donationsAmount) %></span>
                    捐助数 <%=gyStatis.donationsNum %> 笔
                </div>
                <a href="<%=controller.getViewURI(request, DonationList.class)%>" class="fr">更多&gt;</a>
            </div>
            <%}%>
            <div class="user_mod border_t15">
                <%--<% if (user1.F10 == T6110_F10.F) { %>--%>
                 <%if (user1.F06 == T6110_F06.ZRR || user1.F18 == T6110_F18.S) { %>
                <div class="user_til"><i class="icon va_m"></i><span
                        class="gray3 f18 mr20 va_m">理财账户</span><span class="va_m">已赚总金额￥<%=Formater.formatAmount(tenderAccount.yzzje)%></span>
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">已赚总金额=投资收益<%=user1.F06 == T6110_F06.ZRR ? "+体验金利息" : "" %>+债权转让盈亏</div>
                            </div>
                        </span>
                </div>
                <div class="user_table">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td>投资方式</td>
                            <td align="center">投资资产(元)</td>
                            <td align="center">已赚金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">持有数量</td>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <%
                        if(T6110_F06.ZRR == t6110.F06){
                        %>
                        <tr>
                            <td>体验金投资</td>
                            <td align="center"><%=Formater.formatAmount(tenderAccount.tyjze)%></td>
                            <td align="center"><%=Formater.formatAmount(tenderAccount.tyjyz)%></td>
                            <td align="center"><%=Formater.formatRate(tenderAccount.tyjsyl,false)%>
                            </td>
                            <td align="center"><%=tenderAccount.tyjcyl%>
                            </td>
                            <td align="center"><a
                                    href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.spread.Wdtyj.class) %>?status=CYZ"
                                    class="highlight">查看</a></td>
                        </tr>
                        <%}%>
                        <tr>
                            <td>散标投资</td>
                            <td align="center"><%=Formater.formatAmount(tenderAccount.sbzc)%></td>
                            <td align="center"><%=Formater.formatAmount(tenderAccount.sbyz)%></td>
                            <td align="center"><%=Formater.formatRate(tenderAccount.sbsyl,false)%>
                            </td>
                            <td align="center"><%=tenderAccount.sbcyl%>
                            </td>
                            <td align="center"><a href="<%=controller.getViewURI(request, Hszdzq.class)%>"
                                                  class="highlight">查看</a></td>
                        </tr>
                    </table>
                </div>
               <%}%>
                <%if (user1.F10 == T6110_F10.F) { %>
                <div class="user_til mt30"><i class="icon"></i><span
                        class="gray3 f18 mr20">借款账户</span>待还总金额￥<%=Formater.formatAmount(manage.getUnpayTotal())%>
                    <a href="<%=controller.getViewURI(request, Repaying.class)%>" class="fr">更多&gt;</a>
                </div>
                <div class="user_table">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td>借款标题</td>
                            <td align="center">待还本金(元)</td>
                            <td align="center">待还利息(元)</td>
                            <td align="center">逾期费用(元)</td>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <%
                            if (loanAccount != null && loanAccount.length > 0) {
                                int count = 0;
                                for (int i = 0; i < loanAccount.length; i++) {
                                    if (count == 3) {
                                        continue;
                                    }
                                    count++;
                                    LoanAccount account = loanAccount[i];
                        %>
                        <tr>
                            <td title="<%StringHelper.filterQuoter(out,account.title);%>">
                                
                                    <%
                                        boolean falg = true;
                                        if (account.F15.name().equals("S")) {
                                            falg = false;
                                    %><i class="item_icon novice_icon">新</i><%
                                    }
                                    if (account.F16.name().equals("S")) {
                                        falg = false;
                                %><i class="item_icon reward_icon">奖</i><%}%><%
                                    if (falg) {
                                        if (account.F11 == T6230_F11.S) {
                                %><i class="item_icon dan_icon">保</i>
                                    <%} else if (account.F13 == T6230_F13.S) { %><i class="item_icon di_icon">抵</i>
                                    <%} else if (account.F14 == T6230_F14.S) {%><i class="item_icon shi_icon">实</i>
                                    <%} else {%><i class="item_icon xin_icon">信 </i><%
                                        }
                                    }
                                %>
                               
                                <%StringHelper.truncation(out, account.title, 6, "...");%>
                            </td>
                            <td align="center"><%=Formater.formatAmount(account.dhbj)%></td>
                            <td align="center"><%=Formater.formatAmount(account.dhlx)%></td>
                            <td align="center"><%=Formater.formatAmount(account.yqfy)%></td>
                            <td align="center"><a href="<%=controller.getViewURI(request, Repaying.class)%>"
                                                  class="highlight">查看</a></td>
                        </tr>
                        <%
                                }
                            }else{
                        %>
                        <tr ><td align="center" colspan="5">暂无数据</td></tr>
                        <%
                                }
                        %>
                    </table>
                </div>
                <%--<%}%>--%>
                <%}
                    if(user1.F10 == T6110_F10.S || (is_has_guarant1 &&  t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG))){ %>

                <div class="user_til mt30"><i class="icon"></i><span class="gray3 f18 mr20">担保业务</span><a href="<%=controller.getViewURI(request, Dbywmx.class)%>" class="fr">更多&gt;</a></div>
                <div class="user_table">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td align="center">序号</td>
                            <td align="center">借款标题</td>
                            <td align="center">用户名</td>
                            <td align="center">借款金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">期限</td>
                            <td align="center">待还金额(元)</td>
                            <td align="center">状态</td>
                        </tr>
                        <%
                            FxbyjManage service = serviceSession.getService(FxbyjManage.class);
                            PagingResult<Dbxxmx> result = service.searchDb(AbstractUserServlet.INDEX_PAGING);
                            BidManage bidManage2 = serviceSession.getService(BidManage.class);
                            UserInfoManage userInfoManage2 = serviceSession.getService(UserInfoManage.class);
                            if (result != null && result.getItemCount() > 0) {
                                int i = 1;
                                for (Dbxxmx ibf : result.getItems()) {
                                    if(i>3)
                                    {
                                        break;
                                    }
                                    if (ibf == null) {
                                        continue;
                                    }
                        %>
                        <tr>
                            <td align="center"><%=i++ %>
                            </td>
                            <td align="center" title="<%=ibf.F02%>"><%
                                StringHelper.filterHTML(out, StringHelper.truncation(ibf.F02, 10));%></td>
                            <td align="center"><%=userInfoManage2.getUserName(ibf.F01)%>
                            </td>
                            <td align="center">
                                <%
                                    if (ibf.F06 == T6230_F20.YJQ || ibf.F06 == T6230_F20.HKZ || ibf.F06 == T6230_F20.YDF) {
                                        ibf.F03 = ibf.F03.subtract(ibf.F14);
                                    }
                                %>
                                <%=Formater.formatAmount(ibf.F03) %>
                            </td>
                            <td align="center"><%=Formater.formatRate(ibf.F04,false) %>
                            </td>
                            <td align="center">
                                <%if (ibf.F13.F21.equals(T6231_F21.F)) {%>
                                <%=ibf.F05%>个月
                                <%} else { %>
                                <%=ibf.F13.F22 %>天
                                <%} %>
                            </td>
                            <td align="center"><%=Formater.formatAmount(ibf.dhbj)%>
                            </td>
                            <td align="center"><%=ibf.F06.getChineseName()%>
                            </td>
                        </tr>
                        <%
                                }
                            }else{
                        %>
                        <tr ><td align="center" colspan="8">暂无数据</td></tr>
                        <%
                                }
                        %>
                    </table>
                </div>

                <%}%>
            </div>
            <%if (user1.F06 == T6110_F06.ZRR || user1.F18 == T6110_F18.S) { %>
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">理财推荐</span><a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>" class="fr">更多&gt;</a></div>
                <div class="user_table">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="til">
                            <td>借款标题</td>
                            <td align="center">金额(元)</td>
                            <td align="center">年化利率(%)</td>
                            <td align="center">借款期限</td>
                            <td align="center">进度</td>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <%
                            if (bids != null && bids.length > 0) {
                                for (Bdlb creditInfo : bids) {
                                    if (creditInfo == null) continue;
                        %>
                        <tr>
                            <td title="<%StringHelper.filterQuoter(out, creditInfo.F04);%>">
                                
                                    <% boolean lcfalg = true;
                                        if (creditInfo.F21.name().equals("S")) {
                                            lcfalg = false;
                                    %><i class="item_icon novice_icon">新</i><%
                                    }
                                    if (creditInfo.F22.name().equals("S")) {
                                        lcfalg = false;
                                %><i class="item_icon reward_icon">奖</i><%}%>
                                    <%
                                        if (lcfalg) {
                                            if (creditInfo.F16 == T6230_F11.S) {
                                    %><i class="item_icon dan_icon">保</i>
                                    <%} else if (creditInfo.F17 == T6230_F13.S) {%><i class="item_icon di_icon">抵</i>
                                    <%} else if (creditInfo.F18 == T6230_F14.S) {%><i class="item_icon shi_icon">实</i>
                                    <%} else {%><i class="item_icon xin_icon">信</i><%
                                        }
                                    }
                                %>
                                
                                <a href="<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ);%><%=creditInfo.F02%><%=rewriter.getViewSuffix()%>"
                                   class="til_120"><%StringHelper.truncation(out, creditInfo.F04, 8, "...");%></a>
                            </td>
                            <td align="center">
                                <%--<%if (creditInfo.F06.doubleValue() >= 10000) {%><%=Formater.formatAmount(creditInfo.F06.doubleValue() / 10000)%>
                                万元
                                <%} else {%>--%><%=Formater.formatAmount(creditInfo.F06)%><%--<%}%>--%>
                            </td>
                            <td align="center"><%=Formater.formatRate(creditInfo.F07, false)%></td>
                            <td align="center"><%
                                if (T6231_F21.S == creditInfo.F19) {
                                    out.print(creditInfo.F20 + "天");
                                } else {
                                    out.print(creditInfo.F10 + "个月");
                                }
                            %></td>
                            <td align="center">
                                <span class="progress_bg"><span class="progress"
                                                                style="width:<%=Formater.formatProgress(creditInfo.proess)%>;"></span></span><em
                                    class="progress_cent"><%=Formater.formatProgress(creditInfo.proess)%>
                            </em>
                            </td>
                            <td align="center"><a
                                    href="<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ);%><%=creditInfo.F02%><%=rewriter.getViewSuffix()%>"
                                    class="btn04">去投资</a></td>
                        </tr>
                        <%
                                }
                            }else{
                        %>
                        <tr ><td align="center" colspan="6">暂无数据</td></tr>
                        <%
                                }
                        %>
                    </table>
                </div>
            </div>
             <%}%>
        </div>
        <!--右边内容-->
    </div>
</div>
<!--主体内容-->
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<!--底部-->
<%@include file="/WEB-INF/include/footer.jsp" %>
<!--底部-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    <%
      if(!StringHelper.isEmpty(infoMsg)){%>
    $("#info").html(showDialogInfo("<%=infoMsg%>", "doubt"));
    $("div.popup_bg").show();
    <%}%>
</script>
<script type="text/javascript">
    <%
      if(!StringHelper.isEmpty(infoMsg)){%>
    $("#info").html(showDialogInfo("<%=infoMsg%>", "doubt"));
    $("div.popup_bg").show();
    <%}%>
    
  <%--   var _userIndex = "<%=controller.getViewURI(request, Index.class)%>"; --%>
    var _userIndex = "<%=configureProvider.format(URLVariable.USER_INDEX)%>";
	//解约确信
	function showConfirmInfo(msg,type,url){
		 $("#info").html('<div class="dialog"">'+
		'<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfo()"></a>提示</div>'+
		'<div class="content">'+
		'<div class="tip_information">'+
	    '<div class="doubt"></div>'+
	      '<div class="tips">' +
	      	'<span class="f20 gray3">'+msg+'</span>'+
	      	'<span class="f20 gray3">注意：解约后将不能充值、投资、提现等操作</span>'+
	      '</div>'+
	     '</div>'+
	     ' <div class="tc mt20"><a href="javascript:void(0);" onclick="allinpayTermination();" class="btn01">确 定</a><a onclick="closeInfo()" class="btn01 btn_gray ml20">取 消</a></div> '+
	   ' </div>'+
		'</div>');
		  $("div.popup_bg").show();
	}
	
    // 通联协议支付解约
    function allinpayTermination(){
    	
    	$.ajax({
			type:"post",
			url:"/user/capital/fuyouTermination.htm",
			data:{},
			dataType: 'text',
		    success: succFunction,
			error: erryFunction
		})
		function erryFunction() {  
    		 $("div.popup_bg").hide();
    		 $("#info").html("");
    		 
		    $("div.popup_bg").show();
		    $("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
	    }  
		function succFunction(msg) { 
			//去掉之 前的确认框
			 $("div.popup_bg").hide();
    		 $("#info").html("");
    		 
			if (msg == "SUCCESS"){
			    $("div.popup_bg").show();
			    $("#info").html(showSuccInfo("协议支付解约成功！", "yes",_userIndex));
				// window.location.reload();
			} else {
				 $("div.popup_bg").show();
				    $("#info").html(showDialogInfo(msg, "error"));
			}
       }  
    }
</script>
</body>
</html>