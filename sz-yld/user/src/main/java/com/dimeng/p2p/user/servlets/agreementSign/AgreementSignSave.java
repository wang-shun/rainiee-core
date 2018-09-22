package com.dimeng.p2p.user.servlets.agreementSign;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6272;
import com.dimeng.p2p.S62.enums.T6272_F06;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.modules.bid.user.service.AgreementSignManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.service.SafetymsgViewManage;
import com.dimeng.p2p.user.servlets.AbstractUserServlet;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

public class AgreementSignSave extends AbstractUserServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
        UserManage usermanage = serviceSession.getService(UserManage.class);
        String usrCustId = usermanage.getUsrCustId();
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (tg && StringHelper.isEmpty(usrCustId))
        {
            //你还没在第三方注册账号
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':01,'msg':'");
            sb.append("网签合同必须先注册第三方托管账户，<a href=\"" + configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE)
                + "\" class=\"red\">立即注册</a>" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (!mge.isSmrz() || !mge.getYhrzxx())
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            boolean isOpenWithPsd =
                BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
            String errorMessage = "网签合同必须先绑定手机号码，实名认证。";
            if (isOpenWithPsd)
            {
                errorMessage = "网签合同必须先绑定手机号码，实名认证。交易密码设置";
            }
            //跳转到实名认证页面
            SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
            String info =
                errorMessage + "，请您到<a href=\"" + configureProvider.format(safeManage.getSafetymsgView())
                    + "\" class=\"blue\">个人基础信息</a>设置。" + "'}]";
            sb.append(info);
            out.write(sb.toString());
            return;
        }
        
        AgreementSignManage manage = serviceSession.getService(AgreementSignManage.class);
        boolean xybq = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_LOAN_CONTRACT));
        //判断是否网签
        boolean isNetSign = manage.isNetSign();
        //判断是否保全
        boolean isSaveAgreement = manage.isSaveAgreement();
        if (!xybq && !isNetSign)
        {
            int signAgreementId = manage.insertSignAgreement();
            if (signAgreementId > 0)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':00,'msg':'");
                sb.append("协议网签成功" + "'}]");
                out.write(sb.toString());
                return;
            }
        }
        if (xybq && !isSaveAgreement)
        {
            ResourceProvider resourceProvider = getResourceProvider();
            Dzxy dzxy = manage.getSignContent();
            if (dzxy == null)
            {
                logger.error("网签协议内容为空");
                return;
            }
            Map<String, Object> valueMap = manage.getValueMap();
            String xyNo = (String)valueMap.get("xy_no");//协议编号
            String charset = resourceProvider.getCharset();
            PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
            StringBuffer sb = new StringBuffer();
            sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
            sb.append(dzxy.content);
            sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
            String path = cpfe.createHTML(valueMap, "agreement", dzxy.xymc, sb.toString(), charset, xyNo);
            String pdfPath = "";
            if (!StringHelper.isEmpty(path))
            {
                StringBuffer sbs = new StringBuffer();
                sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                    .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                    .append("/console/");
                pdfPath = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                logger.info("生成pdf合同文档成功！");
            }
            
            T6272 t6272 = new T6272();
            t6272.F03 = dzxy.versionNum;
            t6272.F06 = T6272_F06.WBQ;
            t6272.F07 = pdfPath;
            t6272.F08 = xyNo;
            int signAgreementId = manage.insertAgreementContent(t6272);
            if (signAgreementId > 0)
            {
                PreservationExecutor executor = getResourceProvider().getResource(PreservationExecutor.class);
                executor.agreementPreservation(signAgreementId);
                StringBuilder strb = new StringBuilder();
                strb.append("[{'num':00,'msg':'");
                strb.append("网签协议成功" + "'}]");//保全不一定成功
                out.write(strb.toString());
                return;
            }
        }
    }
}
