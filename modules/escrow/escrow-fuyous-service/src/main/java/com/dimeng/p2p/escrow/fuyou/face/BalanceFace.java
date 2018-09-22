package com.dimeng.p2p.escrow.fuyou.face;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.cond.BalanceCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.BalanceEntity;
import com.dimeng.p2p.escrow.fuyou.service.BalanceManage;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;

/**
 * 
 * 用户余额查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public class BalanceFace
{
    private static Log logger = LogFactory.getLog(BalanceFace.class);
    
    public ArrayList<BalanceEntity> executeBalance(final String mchnt_cd, final String mchnt_txn_ssn,
        final String mchnt_txn_dt, final String cust_no, String actionUrl, ServiceSession serviceSession)
        throws Throwable
    {
        BalanceManage manage = serviceSession.getService(BalanceManage.class);
        
        Map<String, String> params = manage.createBalance(new BalanceCond()
        {
            
            @Override
            public String mchntTxnSsn()
            {
                return mchnt_txn_ssn;
            }
            
            @Override
            public String mchntTxnDt()
            {
                return mchnt_txn_dt;
            }
            
            @Override
            public String mchntCd()
            {
                return mchnt_cd;
            }
            
            @Override
            public String custNo()
            {
                return cust_no;
            }
        });
        
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(params, actionUrl);
        logger.info("余额查询 - xmlpost == " + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        return manage.balanceDecoder(xmlMap, plain);
    }
}
