/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.achieve;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.cond.TransferCond;
import com.google.gson.Gson;

*//**
 * 
 * 转账/划拨
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 *//*
public class TransferManageImpl extends AbstractEscrowService implements TransferManage
{
    
    private static Log logger = LogFactory.getLog(TransferManageImpl.class);
    
    public TransferManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class TransferAccountsManageFactory implements ServiceFactory<TransferManage>
    {
        
        @Override
        public TransferManage newInstance(ServiceResource serviceResource)
        {
            return new TransferManageImpl(serviceResource);
        }
    }
    
    @Override
    public Map<String, String> createTransferMap(TransferCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("tran_name", cond.tranName());
        param.put("out_cust_no", cond.outCustNo());
        param.put("in_cust_no", cond.inCustNo());
        param.put("amt", cond.amt());
        param.put("mchnt_amt", cond.mchntAmt());
        param.put("contract_no", cond.contractNo());
        logger.info("投资拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        logger.info("拼接=" + str);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        return param;
    }
    
    @Override
    public boolean verifyByRSA(String plain, String chkValue)
        throws Exception
    {
        return super.verifyByRSA(plain, chkValue);
    }
}
*/