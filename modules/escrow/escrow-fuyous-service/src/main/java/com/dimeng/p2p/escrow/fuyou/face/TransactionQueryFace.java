package com.dimeng.p2p.escrow.fuyou.face;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryRequestEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.service.TransactionQueryManage;
import com.dimeng.p2p.escrow.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.escrow.fuyou.util.XmlHelper;
import com.google.gson.Gson;

/**
 * 
 * 交易查询接口
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public class TransactionQueryFace
{
    private static Log logger = LogFactory.getLog(TransactionQueryFace.class);
    
    /**
     * 交易查询接口
     * 
     * @param mchnt_cd
     *            商户代码(必填)
     * @param mchnt_txn_ssn
     *            流水号(必填)
     * @param busi_tp
     *            交易类型(必填)
     * @param start_day
     *            起始时间(必填)
     * @param end_day
     *            截止时间(必填)
     * @param txn_ssn
     *            交易流水(可选-用于查询某笔交易)
     * @param cust_no
     *            交易用户(可选)
     * @param txn_st
     *            交易状态(可选)
     * @param remark
     *            交易备注(可选)
     * @param page_no
     *            页码(可选)
     * @param page_size
     *            每页条数 (可选)
     * @param transactionQueryURL
     *            请求地址
     * @param serviceSession
     * @return TransactionQueryResponseEntity 返回结果集实体类
     * @throws Throwable
     */
    public static TransactionQueryResponseEntity executeTransactionQuery(final String mchnt_cd,
        final String mchnt_txn_ssn, final String busi_tp, final String start_day, final String end_day,
        final String txn_ssn, final String cust_no, final String txn_st, final String remark, final String page_no,
        final String page_size, final String transactionQueryURL, ServiceSession serviceSession)
        throws Throwable
    {
        TransactionQueryManage manage = serviceSession.getService(TransactionQueryManage.class);
        // 数据签名后的请求参数据
        TransactionQueryRequestEntity requestEntity =
            manage.queryTransactionQuery(new TransactionQueryRequestEntity(mchnt_cd, mchnt_txn_ssn, busi_tp, start_day,
                end_day, txn_ssn, cust_no, txn_st, remark, page_no, page_size));
        
        // 请求富友地址，并获取返回参数
        Gson gs = new Gson();
        @SuppressWarnings("unchecked")
        String xmlpost =
            HttpClientHandler.doPost(gs.fromJson(gs.toJson(requestEntity), Map.class), transactionQueryURL);
        logger.info("xmlpost == " + xmlpost);
        // 获取plain值
        String plain = XmlHelper.getPlain(xmlpost);
        
        // 将返回的xml参数进行解析 String
        HashMap<String, Object> xmlMap = (HashMap<String, Object>)XmlHelper.xmlToMap(xmlpost);
        // 把解析的结果封装成一个对象
        TransactionQueryResponseEntity responseEntity = manage.queryDecoder(xmlMap, plain);
        logger.info("交易查询== " + gs.toJson(responseEntity));
        return responseEntity;
    }
}
