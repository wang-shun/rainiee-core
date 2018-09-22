package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.PhoneManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 手机号码修改
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月5日]
 */
public class PhoneManageImpl extends AbstractEscrowService implements PhoneManage {
    
    public PhoneManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public Map<String, String> createPhoneUri() throws Throwable {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID));
        params.put("mchnt_txn_ssn", MchntTxnSsn.getMts(FuyouTypeEnum.SJHX.name()));
        params.put("login_id", getAccountId(serviceResource.getSession().getAccountId()));
        params.put("page_notify_url", configureProvider.format(FuyouVariable.FUYOU_PHONERET));
        String str = getSignature(params);
        String signature = encryptByRSA(str);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public void updatePhone(String phoneNumber)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            try
            {
                if (StringHelper.isEmpty(phoneNumber) || acount <= 0)
                {
                    throw new ParameterException("参数错误!");
                }
                if (isPhone(phoneNumber))
                {
                    throw new ParameterException("手机号码存在!");
                }
                execute(connection, "UPDATE  S61.T6110 SET F04=? WHERE F01 = ?", phoneNumber, acount);
                execute(connection,
                    "UPDATE S61.T6118 SET F03=?,F06=? WHERE F01 = ? ",
                    T6118_F03.TG,
                    phoneNumber,
                    acount);
                
                execute(connection, "UPDATE S61.T6164 SET F04 = ? WHERE F01 = ?", phoneNumber, acount);
                
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    public boolean isPhone(String idCard)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            }, "SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ?", idCard);
        }
    }
    
    @Override
    public boolean phoneRetDecode(Map<String, String> params)
        throws Throwable
    {
        Map<String, String> paramMap = null;
        
        if (params != null)
        {
            paramMap = new HashMap<String, String>();
            //个人用户
            paramMap.put("login_id", params.get("login_id"));
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //新手机号
            paramMap.put("new_mobile", params.get("new_mobile"));
            //响应码
            paramMap.put("resp_code", params.get("resp_code"));
            //响应消息
            paramMap.put("resp_desc", params.get("resp_desc"));
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        String str = getSignature(paramMap);
        if (!verifyByRSA(str, params.get("signature")))
        {
            logger.info("手机号更新-返信息==" + BackCodeInfo.info(params.get("resp_code")));
            return false;
        }
        return true;
    }
}
