package com.dimeng.p2p.escrow.fuyou.achieve;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.service.ChargeWithdrawManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.parser.DateParser;
import com.google.gson.Gson;

/**
 * 
 * 充值提现查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
public class ChargeWithdrawImpl extends AbstractEscrowService implements ChargeWithdrawManage {
    
    public ChargeWithdrawImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public Map<String, Object> createChargeWithdraw(String mchnt_txn_ssn, String busi_tp, String txn_ssn, String cust_no) throws Throwable {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ver", configureProvider.format(FuyouVariable.FUYOU_VERSION));
        param.put("mchnt_cd", configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID));
        param.put("mchnt_txn_ssn", mchnt_txn_ssn);
        param.put("busi_tp", busi_tp);
        param.put("txn_ssn", txn_ssn);
        // 富友支持查询三十天内的订单对账
       Calendar cur = Calendar.getInstance();
       param.put("end_time", new SimpleDateFormat("yyyy-MM-dd").format(cur.getTime()).concat(" 23:59:59"));
       // 开始时间为当前日前10天
       cur = Calendar.getInstance();
       cur.set(Calendar.DAY_OF_MONTH, cur.get(Calendar.DAY_OF_MONTH) - 10);
       String startTime = DateParser.format(cur.getTime(),"yyyy-MM-dd");
       logger.info("富友充值对账查询开始时间为："+startTime);
        param.put("start_time", startTime.concat(" 00:00:00"));
        
        param.put("cust_no", cust_no);
        param.put("txn_st", "");
        param.put("page_no", "1");
        param.put("page_size", "100");
        logger.info("拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        logger.info("拼接=" + str);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        // 请求富友地址，并获取返回参数
        String xmlpost = HttpClientHandler.doPost(param, configureProvider.format(FuyouVariable.FUYOU_QUERYCZTX_URL));
        logger.info("充值提现查询：" + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        // 将返回的xml参数进行解析
        Map<String, Object> xmlMap = XmlHelper.xmlToMap(xmlpost);
        if (verifyByRSA(plain, xmlMap.get("signature").toString())) {
            return xmlMap;
        }
        logger.info("充值提现查询-验签失败 ==" + BackCodeInfo.info(xmlMap.get("resp_code").toString()));
        return null;
    }
    
}
