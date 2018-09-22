package com.dimeng.p2p.escrow.fuyou.achieve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryRequestEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.service.TransactionQueryManage;

/**
 * 
 * 交易查询接口逻辑业务实现类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月27日]
 */
public class TransactionQueryManageImpl extends AbstractEscrowService implements TransactionQueryManage
{
    
    public TransactionQueryManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    private HashMap<String, Object> xmlMap = null;
    
    // 本次查询总条数，实际数量，非此值
    private int sum = 0;
    
    @Override
    public TransactionQueryRequestEntity queryTransactionQuery(TransactionQueryRequestEntity requestEntity)
        throws Throwable
    {
        List<String> params = new ArrayList<>();
        params.add(requestEntity.getBusi_tp());
        params.add(requestEntity.getCust_no());
        params.add(requestEntity.getEnd_day());
        params.add(requestEntity.getMchnt_cd());
        params.add(requestEntity.getMchnt_txn_ssn());
        params.add(requestEntity.getPage_no());
        params.add(requestEntity.getPage_size());
        params.add(requestEntity.getRemark());
        params.add(requestEntity.getStart_day());
        params.add(requestEntity.getTxn_ssn());
        params.add(requestEntity.getTxn_st());
        // 签名数据
        requestEntity.setSignature(chkValue(params));
        
        logger.info("交易查询数据~~~~~~~Start");
        logger.info("mchnt_cd == " + requestEntity.getMchnt_cd());
        logger.info("mchnt_txn_ssn == " + requestEntity.getMchnt_txn_ssn());
        logger.info("busi_tp == " + requestEntity.getBusi_tp());
        logger.info("start_day == " + requestEntity.getStart_day());
        logger.info("end_day == " + requestEntity.getEnd_day());
        logger.info("txn_ssn == " + requestEntity.getTxn_ssn());
        logger.info("cust_no == " + requestEntity.getCust_no());
        logger.info("remark == " + requestEntity.getRemark());
        logger.info("page_no == " + requestEntity.getPage_no());
        logger.info("page_size == " + requestEntity.getPage_size());
        logger.info("signature == " + requestEntity.getSignature());
        logger.info("交易查询数据~~~~~~End");
        
        return requestEntity;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public TransactionQueryResponseEntity queryDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable
    {
        this.xmlMap = xmlMap;
        TransactionQueryResponseEntity responseEntity = null;
        logger.info("返回参数值：plain == " + plain);
        logger.info("返回参数值：signature == " + xmlMap.get("signature"));
        
        boolean verifyResult = verifyByRSA(plain, (String)(xmlMap.get("signature")));
        logger.info("验签结果：" + verifyResult);
        if (verifyResult)
        {
            responseEntity = new TransactionQueryResponseEntity();
            //响应码
            responseEntity.respCode = xmlMap.get("resp_code").toString();
            // 商户代码
            responseEntity.mchntCd = xmlMap.get("mchnt_cd").toString();
            // 流水号  mchnt_txn_ssn
            responseEntity.mchntTxnSsn = xmlMap.get("mchnt_txn_ssn").toString();
            // 业务类型 busi_tp
            responseEntity.busiTp = xmlMap.get("busi_tp").toString();
            // 总记录数 total_number
            responseEntity.totalNumber = Integer.valueOf(xmlMap.get("total_number").toString());
            //  签名数据    signature
            responseEntity.signature = xmlMap.get("signature").toString();
            
            int isnull = xmlMap.toString().indexOf("mchnt_ssn");
            if (isnull < 0)
            {
                logger.info("交易查询结果为NULL");
                // 总记录数 
                responseEntity.totalNumber = sum;
                responseEntity.detailedEntity = null;
                return responseEntity;
            }
            
            // 单条记录情况
            if (xmlMap.toString().indexOf("[") < 0)
            {
                return queryOne(xmlMap);
            }
            
            responseEntity.detailedEntity = new ArrayList<TransactionQueryDetailedEntity>();
            List<String> mchnt_ssns = null;
            List<String> txn_rsp_cds = null;
            String temp = xmlMap.get("mchnt_ssn").toString().trim();
            String temps = xmlMap.get("txn_rsp_cd").toString().trim();
            if (!temp.equals("") && temp != null && !temps.equals("") && temps != null)
            {
                // 交易流水 mchnt_ssn
                mchnt_ssns = judgeList("mchnt_ssn");
                System.out.println(mchnt_ssns.size() + "==");
                txn_rsp_cds = judgeList("txn_rsp_cd");
            }
            else
            {
                responseEntity.detailedEntity.add(null);
                return responseEntity;
            }
            // 交易记录总条数据
            sum = ((mchnt_ssns.size() - txn_rsp_cds.size()) > 0 ? mchnt_ssns.size() : txn_rsp_cds.size());
            // 总记录数 
            responseEntity.totalNumber = sum;
            logger.info(sum + "====总条数");
            // 扩展类型
            List<String> ext_tp = judgeList("ext_tp");
            // 交易日期
            List<String> txn_date = judgeList("txn_date");
            //  交易时分
            List<String> txn_time = judgeList("txn_time");
            //  交易请求方式
            List<String> src_tp = judgeList("src_tp");
            //  交易流水
            List<String> mchnt_ssn = judgeList("mchnt_ssn");
            //  交易金额
            List<String> txn_amt = judgeList("txn_amt");
            // 成功交易金额
            List<String> txn_amt_suc = judgeList("txn_amt_suc");
            // 合同号
            List<String> contract_no = judgeList("contract_no");
            //  出账用户虚拟账户
            List<String> out_fuiou_acct_no = judgeList("out_fuiou_acct_no");
            // 出账用户名
            List<String> out_cust_no = judgeList("out_cust_no");
            //  出账用户名称
            List<String> out_artif_nm = judgeList("out_artif_nm");
            //  入账用户虚拟账户
            List<String> in_fuiou_acct_no = judgeList("in_fuiou_acct_no");
            //  入账用户名
            List<String> in_cust_no = judgeList("in_cust_no");
            //  入账用户名称
            List<String> in_artif_nm = judgeList("in_artif_nm");
            // 交易备注
            List<String> remark = judgeList("remark");
            //  交易返回码
            List<String> txn_rsp_cd = judgeList("txn_rsp_cd");
            //  交易返回码描述
            List<String> rsp_cd_desc = judgeList("rsp_cd_desc");
            
            //  签名数据    signature
            responseEntity.signature = xmlMap.get("signature").toString();
            TransactionQueryDetailedEntity detailedEntity = null;
            
            for (int i = 0; i < sum; i++)
            {
                detailedEntity =
                    new TransactionQueryDetailedEntity(ext_tp.get(i), txn_date.get(i), txn_time.get(i), src_tp.get(i),
                        mchnt_ssn.get(i), txn_amt.get(i), txn_amt_suc.get(i), contract_no.get(i),
                        out_fuiou_acct_no.get(i), out_cust_no.get(i), out_artif_nm.get(i), in_fuiou_acct_no.get(i),
                        in_cust_no.get(i), in_artif_nm.get(i), remark.get(i), txn_rsp_cd.get(i), rsp_cd_desc.get(i));
                responseEntity.detailedEntity.add(detailedEntity);
            }
            return responseEntity;
        }
        else
        {
            logger.info("验签失败");
            return null;
        }
    }
    
    /**
     * 返回一条
     * <功能详细描述>
     * @param xmlMap
     * @return
     * @throws Throwable
     */
    public TransactionQueryResponseEntity queryOne(HashMap<String, Object> xmlMap)
        throws Throwable
    {
        // 返回结果
        TransactionQueryResponseEntity responseEntity = new TransactionQueryResponseEntity();
        // 明细
        TransactionQueryDetailedEntity detailedEntity = new TransactionQueryDetailedEntity();
        responseEntity.detailedEntity = new ArrayList<TransactionQueryDetailedEntity>();
        
        // 流水号
        String temp = xmlMap.get("mchnt_ssn").toString().trim();
        // 状态码
        String temps = xmlMap.get("txn_rsp_cd").toString().trim();
        
        // 返回码
        responseEntity.respCode = xmlMap.get("resp_code").toString();
        responseEntity.mchntCd = xmlMap.get("mchnt_cd").toString();
        responseEntity.mchntTxnSsn = xmlMap.get("mchnt_txn_ssn").toString();
        // 业务类型
        responseEntity.busiTp = xmlMap.get("busi_tp").toString();
        
        if (!temp.equals("") && temp != null && !temps.equals("") && temps != null)
        {
            // 扩展类型
            detailedEntity.setExtTp(xmlMap.get("ext_tp").toString());
            // 交易日期
            detailedEntity.setTxnDate(xmlMap.get("txn_date").toString());
            //  交易时分
            detailedEntity.setTxnTime(xmlMap.get("txn_time").toString());
            //  交易请求方式
            detailedEntity.setSrcTp(xmlMap.get("src_tp").toString());
            //  交易流水
            detailedEntity.setMchntSsn(xmlMap.get("mchnt_ssn").toString());
            //  交易金额
            detailedEntity.setTxnAmt(xmlMap.get("txn_amt").toString());
            // 成功交易金额
            detailedEntity.setTxnAmtSuc(xmlMap.get("txn_amt_suc").toString());
            // 合同号
            detailedEntity.setContractNo(xmlMap.get("contract_no").toString());
            //  出账用户虚拟账户
            detailedEntity.setOutFuiouAcctNo(xmlMap.get("out_fuiou_acct_no").toString());
            // 出账用户名
            detailedEntity.setOutCustNo(xmlMap.get("out_cust_no").toString());
            //  出账用户名称
            detailedEntity.setOutartifNm(xmlMap.get("out_artif_nm").toString());
            //  入账用户虚拟账户
            detailedEntity.setInFuiouAcctNo(xmlMap.get("in_fuiou_acct_no").toString());
            //  入账用户名
            detailedEntity.setInCustNo(xmlMap.get("in_cust_no").toString());
            //  入账用户名称
            detailedEntity.setInArtifNm(xmlMap.get("in_artif_nm").toString());
            // 交易备注
            detailedEntity.setRemark(xmlMap.get("remark").toString());
            //  交易返回码
            detailedEntity.setTxnRspCd(xmlMap.get("txn_rsp_cd").toString());
            //  交易返回码描述
            detailedEntity.setRspCdDesc(xmlMap.get("rsp_cd_desc").toString());
            // 总条数
            responseEntity.totalNumber = Integer.valueOf(xmlMap.get("total_number").toString());
            // 明细
            responseEntity.detailedEntity.add(detailedEntity);
        }
        else
        {
            // 明细
            responseEntity.detailedEntity.add(null);
            // 总条数
            responseEntity.totalNumber = 0;
        }
        return responseEntity;
    }
    
    /**
     * 处理信息（判断返回字段是否有信息集，若无则用“”代替）
     * @param temp 字段
     * @return List<String>返回List集
     */
    @SuppressWarnings("unchecked")
    public List<String> judgeList(String temp)
    {
        List<String> list = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        String flag;
        flag = xmlMap.get(temp).toString().trim();
        if (!flag.equals("") && flag != null)
        {
            //            list = (List<String>)xmlMap.get(temp);
            list.add(temp);
        }
        int n = sum - list.size();
        if (n > 0)
        {
            for (int i = 0; i < n; i++)
            {
                list2.add("");
            }
            if (list.size() < sum && list2.size() < sum)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    list2.add(list.get(i));
                }
            }
        }
        return (list.size() > list2.size()) ? list : list2;
    }
}
