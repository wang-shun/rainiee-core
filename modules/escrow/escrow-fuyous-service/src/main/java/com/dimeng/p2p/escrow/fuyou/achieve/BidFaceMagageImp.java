package com.dimeng.p2p.escrow.fuyou.achieve;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.service.BidFaceManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.google.gson.Gson;

/**
 * 
 * 预授权申请实现类\预授权撤销
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月22日]
 */
public class BidFaceMagageImp extends AbstractEscrowService implements BidFaceManage {
    
    public BidFaceMagageImp(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public Map<String, Object> createBidFace(String mchntTxnSsn, String outCustNo, String inCustNo, String amt) throws Throwable  {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ver", configureProvider.format(FuyouVariable.FUYOU_VERSION));
        param.put("mchnt_cd", configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID));
        param.put("mchnt_txn_ssn", mchntTxnSsn);
        param.put("out_cust_no", outCustNo);
        param.put("in_cust_no", inCustNo);
        param.put("amt", amt);
        param.put("rem", "投资");
        logger.info("投资拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(param, configureProvider.format(FuyouVariable.FUYOU_PREAUTH_URL));
        logger.info("投标 - xmlpost == " + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        
        if(verifyByRSA(plain, xmlMap.get("signature").toString())){
            return xmlMap;
        }
        logger.info("投标-验签失败 ==" + BackCodeInfo.info(xmlMap.get("resp_code").toString()));
        return null;        
    }
    
    @Override
    public Map<String, Object> createBidCancel(String mchnt_txn_ssn, String out_cust_no, String in_cust_no, String contract_no) throws Throwable {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ver", configureProvider.format(FuyouVariable.FUYOU_VERSION));
        param.put("mchnt_cd", configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID));
        param.put("mchnt_txn_ssn", mchnt_txn_ssn);
        param.put("out_cust_no", out_cust_no);
        param.put("in_cust_no", in_cust_no);
        param.put("contract_no", contract_no);
        param.put("rem", "投资失败-流标");
        logger.info("流标拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(param, configureProvider.format(FuyouVariable.FUYOU_PREAUTHCANCEL_URL));
        logger.info("流标 - xmlpost == " + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        
        if(verifyByRSA(plain, xmlMap.get("signature").toString())){
            return xmlMap;
        }
        logger.info("流标-验签失败 ==" + BackCodeInfo.info(xmlMap.get("resp_code").toString()));
        return null;
    }  
    
}
