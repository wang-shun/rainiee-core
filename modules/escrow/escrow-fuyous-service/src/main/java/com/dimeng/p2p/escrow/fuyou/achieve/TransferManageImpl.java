package com.dimeng.p2p.escrow.fuyou.achieve;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.google.gson.Gson;

/**
 * 
 * 转账/划拨
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 */
public class TransferManageImpl extends AbstractEscrowService implements TransferManage {
    
    private static Log logger = LogFactory.getLog(TransferManageImpl.class);
    
    public TransferManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    public static class TransferAccountsManageFactory implements ServiceFactory<TransferManage> {
        
        @Override
        public TransferManage newInstance(ServiceResource serviceResource) {
            return new TransferManageImpl(serviceResource);
        }
    }
    
    @Override
    public Map<String, Object> createTransferMap(String mchnt_txn_ssn, String out_cust_no, String in_cust_no, String amt, String contract_no) throws Throwable {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ver", configureProvider.format(FuyouVariable.FUYOU_VERSION));
        param.put("mchnt_cd", configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID));
        param.put("mchnt_txn_ssn", mchnt_txn_ssn);
        param.put("out_cust_no", out_cust_no);
        param.put("in_cust_no", in_cust_no);
        param.put("amt", amt);
        param.put("rem", "");
        
        String actionUrl = configureProvider.format(FuyouVariable.FUYOU_TRANSFERBU_URL);
        if ("transferBmu".equals(contract_no)) {
        	actionUrl = configureProvider.format(FuyouVariable.FUYOU_TRANSFERBMU_URL);
        	param.put("contract_no", "");
        } else {
        	param.put("contract_no", contract_no);
        }
        
        logger.info("拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        logger.info("拼接=" + str);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(param, actionUrl);
        logger.info("转划/划拨 - xmlpost ==" + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        
        if (verifyByRSA(plain, xmlMap.get("signature").toString())) {
            return xmlMap;
        }
        logger.info("转划/划拨-验签失败 ==" + BackCodeInfo.info(xmlMap.get("resp_code").toString()));
        return null;
    }
        
}
