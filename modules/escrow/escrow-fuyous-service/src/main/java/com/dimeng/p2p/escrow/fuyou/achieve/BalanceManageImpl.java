package com.dimeng.p2p.escrow.fuyou.achieve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.cond.BalanceCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.BalanceEntity;
import com.dimeng.p2p.escrow.fuyou.service.BalanceManage;
import com.google.gson.Gson;

/**
 * 
 * 余额查询
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public class BalanceManageImpl extends AbstractEscrowService implements BalanceManage
{
    
    public BalanceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Map<String, String> createBalance(BalanceCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("mchnt_txn_dt", cond.mchntTxnDt());
        param.put("cust_no", cond.custNo());
        logger.info("余额拼接字符-余额查询：" + new Gson().toJson(param));
        String str = getSignature(param);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        return param;
    }
    
    @Override
    public ArrayList<BalanceEntity> balanceDecoder(Map<String, Object> xmlMap, String plain)
        throws Throwable
    {
        ArrayList<BalanceEntity> arrayList = null;
        boolean verifyResult = verifyByRSA(plain, (String)(xmlMap.get("signature")));
        logger.info("验签结果：" + verifyResult);
        if (verifyResult)
        {
            int isnull = xmlMap.toString().indexOf("user_id");
            if (isnull < 0)
            {
                logger.info("交易查询结果为NULL");
                return arrayList;
            }
            
            xmlMap.remove("signature");
            xmlMap.remove("mchnt_txn_ssn");
            
            // 单条记录情况
            if (xmlMap.toString().indexOf("[") < 0)
            {
                return balanceOne(xmlMap);
            }
            ArrayList<String> cu_balance = handleList(xmlMap, "cu_balance");
            ArrayList<String> ca_balance = handleList(xmlMap, "ca_balance");
            ArrayList<String> ct_balance = handleList(xmlMap, "ct_balance");
            ArrayList<String> cf_balance = handleList(xmlMap, "cf_balance");
            ArrayList<String> user_id = handleList(xmlMap, "user_id");
            int n = user_id.size();
            for (int i = 0; i < n; i++)
            {
                BalanceEntity balanceEntity = new BalanceEntity();
                if (arrayList == null)
                {
                    arrayList = new ArrayList<BalanceEntity>();
                }
                balanceEntity.thirdTag = user_id.get(i);
                balanceEntity.cu_balance = formatAmtRet(cu_balance.get(i));
                balanceEntity.ca_balance = formatAmtRet(ca_balance.get(i));
                balanceEntity.ct_balance = formatAmtRet(ct_balance.get(i));
                balanceEntity.cf_balance = formatAmtRet(cf_balance.get(i));
                arrayList.add(balanceEntity);
            }
        }
        return arrayList;
    }
    
    /**
     * 获取lsit
     * @param xmlMap
     * @param temp
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> handleList(Map<String, Object> xmlMap, String temp)
    {
        ArrayList<String> list = new ArrayList<String>();
         list = (ArrayList<String>)xmlMap.get(temp);
        return list;
    }
    
    /**
     * 查询一条
     * <功能详细描述>
     * @param xmlMap
     * @return
     * @throws Throwable
     */
    private ArrayList<BalanceEntity> balanceOne(Map<String, Object> xmlMap)
    {
        ArrayList<BalanceEntity> arrayList = new ArrayList<BalanceEntity>();
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.thirdTag = xmlMap.get("user_id").toString();
        balanceEntity.ct_balance = formatAmtRet(xmlMap.get("ct_balance").toString());
        balanceEntity.ca_balance = formatAmtRet(xmlMap.get("ca_balance").toString());
        balanceEntity.cf_balance = formatAmtRet(xmlMap.get("cf_balance").toString());
        balanceEntity.cu_balance = formatAmtRet(xmlMap.get("cu_balance").toString());
        arrayList.add(balanceEntity);
        return arrayList;
    }
}
