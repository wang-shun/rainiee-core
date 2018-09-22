<%@page import="com.dimeng.p2p.user.servlets.agreementSign.AgreementSignSave"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.service.SafetymsgViewManage"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.Wqxy"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.IOException"%>
<%@page import="freemarker.template.TemplateException"%>
<%@page import="freemarker.template.Template"%>
<%@page import="freemarker.template.Configuration"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.Dzxy"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.AgreementSignManage"%>
<%@page import="com.dimeng.p2p.user.servlets.credit.ExportNewCreditStatisticsDetail" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta content="email=no"  name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME); %>_借款管理_网签协议</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "WQXY";
    AgreementSignManage manage = serviceSession.getService(AgreementSignManage.class);
    Dzxy dzxy = manage.getSignContent();
    if (dzxy == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    Map<String, Object> valueMap = manage.getValueMap();
    
    UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
    UserManage usermanage = serviceSession.getService(UserManage.class);
    String usrCustId = usermanage.getUsrCustId();
    if(StringHelper.isEmpty(usrCustId)){
        usrCustId = "";
    }
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean isSmrz = mge.isSmrz();
    boolean yhrzxx = mge.getYhrzxx();
    
    String openEscrowGuide = configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE);
  	//跳转到实名认证页面
    SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
    String safetyAddr = configureProvider.format(safeManage.getSafetymsgView());
    
    boolean isXybq = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_LOAN_CONTRACT));
    
    String viewURL = configureProvider.format(URLVariable.USER_AGREEMENT_VIEW_URL);
    
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
		<!--右边内容-->
        <div class="r_main">
        	<div class="user_mod">
            	<div class="user_til"><i class="icon"></i><span class="gray3 f18">网签协议</span><span id = "lookAgreement"><a href="javascript:void(0);" class="highlight fr">查看&gt;</a></span></div>
                <div class="user_table">
                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr class="til">
                        <td align="center">协议版本</td>
                        <td align="center">协议更新时间</td>
                        <td align="center">协议签署时间</td>
                      </tr>
                      <tbody id="dataBody">

                        </tbody>
                    </table>
                </div>
            </div>
        	<div class="user_mod border_t15">
                <div class="user_table">
                	<div class="guarantee_down">
                		<% 
                		Configuration cfg = new Configuration();
					    Template template = new Template(dzxy.xymc, dzxy.content, cfg);
					    try {
					
					        template.process(valueMap, out);
					    } catch (TemplateException e) {
					        throw new IOException(e);
					    }
					    %>
                	</div>
                </div>
                <div class="clearfix mt30 tc" id = "saveButton"><a href="javascript:void(0);" class="btn06">确定</a></div>
            </div>
        </div>        
        <!--右边内容-->
    </div>
    <div id="error_div" style="display: none;">
        <div class="dialog">
            <div class="title"><a href="javascript:void(-1);" onclick="hidebg('error_div');" class="out"></a>提示</div>
            <div class="content">
                <div class="tip_information">
                    <div class="doubt"></div>
                    <div class="tips">
                        <span class="f20 gray3" id = "errorMess"></span>
                    </div>
                </div>
                <div class="tc mt20">
                    <a href="javascript:toValidate('error_div');" class="btn01">确认</a>
                </div>
            </div>
        </div>
    </div>
    <div id="affirm" style="display: none;">
        <div class="dialog">
            <div class="title"><a href="javascript:void(-1);" onclick="hidebg('affirm');" class="out"></a>提示</div>
            <div class="content">
                <div class="tip_information">
                    <div class="doubt"></div>
                    <div class="tips">
                        <span class="f20 gray3">我已认真阅读并同意《网签协议》中的内容</span>
                    </div>
                </div>
                <div class="tc mt20">
                    <a href="javascript:void(0);" onclick="netSign()" class="btn01" id="netSigon">确认</a>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script language="javascript" src="<%=controller.getStaticPath(request)%>/js/agreementSign/agreementSignDetail.js"></script>
<script type="text/javascript">
	var url = "<%=controller.getURI(request, AgreementSignDetail.class)%>";
	var netSignUrl = "<%=controller.getURI(request, AgreementSignSave.class)%>";
	var isXybq = <%=isXybq%>;
	
	var tg = <%=tg%>;
	var usrCustId = "<%=usrCustId%>";
	var escrow = "<%=escrow%>";
	var isSmrz = <%=isSmrz%>;
	var yhrzxx = <%=yhrzxx%>;
	
	var openEscrowGuide = "<%=openEscrowGuide%>";
	var safetyAddr = "<%=safetyAddr%>";
	
	var isOpenWithPsd = <%=isOpenWithPsd%>;
	var versionNum = <%=dzxy.versionNum%>;
	
	var viewURL = "<%=viewURL%>";
</script>

<script type="text/javascript">
    function hidebg(id) {
        $("#" + id).hide();
        $(".popup_bg").hide();
    }
    function toValidate(id) {
    	var $obj = $("#to_validate");
    	if($obj.length > 0 && $obj.attr("href")){
    		window.location.href = $obj.attr("href");
    	}else{
    		hidebg(id);
    	}
        
    }
</script>

</body>
</html>