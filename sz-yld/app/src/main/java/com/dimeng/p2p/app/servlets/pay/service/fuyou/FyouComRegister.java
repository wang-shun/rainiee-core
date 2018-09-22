/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.ComManage;
import com.dimeng.p2p.escrow.fuyou.cond.ComRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.ComRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

*//**
 * 
 * 企业\机构账号注册Servlet
 * <富友托管账号注册>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouComRegister extends AbstractFuyouServlet
{
    
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 638092449190969361L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("COM-APP富友托管注册开始——IP:" + request.getRemoteAddr());
        ComManage comManage = serviceSession.getService(ComManage.class);
        final ComRegisterEntity comEntity = comManage.selectLegealInfo();
        // 创建注册第三方请求信息
        Map<String, String> regMap = comManage.createRegisterUri(new ComRegisterCond()
        {
            
            @Override
            public String userIdFrom()
            {
                return trimBlank(comEntity.getUserIdFrom());
            }
            
            @Override
            public String signature()
            {
                return null;
            }
            
            @Override
            public String parentBankId()
            {
                return trimBlank(comEntity.getParentBankId());
            }
            
            @Override
            public String pageNotifyUrl()
                throws Throwable
            {
                return getSiteDomain("/app/pay/service/fuyou/ret/comRegisterRet.htm");
            }
            
            @Override
            public String mobileNo()
            {
                return trimBlank(comEntity.getMobileNo());
            }
            
            @Override
            public String mchntTxnSsn()
            {
                return MchntTxnSsn.getMts(FuyouTypeEnum.QYZC.name());
            }
            
            @Override
            public String mchntCd()
                throws Throwable
            {
                return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID));
            }
            
            @Override
            public String email()
            {
                return trimBlank(comEntity.getEmail());
            }
            
            @Override
            public String custNm()
            {
                return trimBlank(comEntity.getCustNm());
            }
            
            @Override
            public String cityId()
            {
                return trimBlank(comEntity.getCityId());
            }
            
            @Override
            public String certifId()
            {
                return trimBlank(comEntity.getCertifId());
            }
            
            @Override
            public String capAcntNo()
            {
                return trimBlank(comEntity.getCapAcntNo());
            }
            
            @Override
            public String bankNm()
            {
                return trimBlank(comEntity.getBankNm());
            }
            
            @Override
            public String backNotifyUrl()
                throws Throwable
            {
                return "";
            }
            
            @Override
            public String artifNm()
            {
                return trimBlank(comEntity.getArtifNm());
            }
        });
        // 注册提交地址
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_COMREG_URL);
        // 向第三方发送请求
        sendHttp(regMap, formUrl, response, true);
        logger.info("APP富友托管注册成功发送数据...");
    }
    
}
*/