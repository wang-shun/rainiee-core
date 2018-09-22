package com.dimeng.p2p.escrow.fuyou.face;

import java.io.IOException;
import java.util.Map;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.cond.BankQueryCond;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;

/**
 * 
 * 银行卡变更查询
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月21日]
 */
public class BankQueryFace
{
    /**
     * 用户更换银行卡查询接口
     * @param mchnt_cd 商户代码
     * @param mchnt_txn_ssn 流水号
     * @param login_id  个人用户
     * @param txn_ssn   交易流水
     * @param actionUrl
     * @param serviceSession
     * @return
     * @throws Throwable
     */
    public Map<String, Object> executeBankQuery(final String mchnt_cd, final String mchnt_txn_ssn,
        final String login_id, final String txn_ssn, String actionUrl, ServiceSession serviceSession)
        throws Throwable
    {
        BankManage manage = serviceSession.getService(BankManage.class);
        Map<String, String> params = manage.bankQuery(new BankQueryCond()
        {
            
            @Override
            public String txnSsn()
            {
                return txn_ssn;
            }
            
            @Override
            public String mchntTxnSsn()
            {
                return mchnt_txn_ssn;
            }
            
            @Override
            public String mchntCd()
                throws IOException
            {
                return mchnt_cd;
            }
            
            @Override
            public String loginId()
            {
                return login_id;
            }
        });
        
        String xmlpost = HttpClientHandler.doPost(params, actionUrl);
        String plain = XmlHelper.getPlain(xmlpost);
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        if (manage.verifyByRSA(plain, xmlMap.get("signature").toString()))
        {
            return xmlMap;
        }
        return null;
    }
}
