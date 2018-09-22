/*
 * 文 件 名:  FundFreezeFace.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月3日
 */
package com.dimeng.p2p.escrow.fuyou.face;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.cond.FreezeCond;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FreezeRet;
import com.dimeng.p2p.escrow.fuyou.service.freeze.FreezeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;

/**
 * 
 * 资金冻结管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class FundFreezeFace
{
    private static Log logger = LogFactory.getLog(FundFreezeFace.class);
    
    /**
     * 资金冻结
     * @param mchnt_cd 商户代码
     * @param mchnt_txn_ssn 商户流水号
     * @param cust_no 冻结目标登录账户
     * @param amt 冻结金额
     * @param rem 备注
     * @param serviceSession
     * @return
     * @throws Throwable
     */
    public FreezeRet executeFundFreeze(final String mchntCd, final String mchntTxnSsn, final String custNo,
        final String amt, final String rem, final String actionUrl, ServiceSession serviceSession)
        throws Throwable
    {
        
        FreezeManage manage = serviceSession.getService(FreezeManage.class);
        // 冻结参数
        Map<String, String> paramRet = manage.createFreeze(new FreezeCond()
        {
            @Override
            public String rem()
            {
                return rem;
            }
            
            @Override
            public String mchntTxnSsn()
                throws Throwable
            {
                return mchntTxnSsn;
            }
            
            @Override
            public String mchntCd()
                throws Throwable
            {
                return mchntCd;
            }
            
            @Override
            public String custNo()
            {
                return custNo;
            }
            
            @Override
            public String amt()
            {
                return PayUtil.getAmt(new BigDecimal(amt));
            }
        });
        
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(paramRet, actionUrl);
        logger.info("资金冻结返回 - xmlpost ==" + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        HashMap<String, Object> xmlMap = (HashMap<String, Object>)XmlHelper.xmlToMap(xmlpost);
        
        if (manage.verifyByRSA(plain, xmlMap.get("signature").toString()))
        {
            FreezeRet freezeRet = manage.freezeReturnDecoder(xmlMap, plain);
            return freezeRet;
        }
        logger.info("资金冻结-验签失败 ==" + BackCodeInfo.info(xmlMap.get("resp_code").toString()));
        return null;
    }
}
