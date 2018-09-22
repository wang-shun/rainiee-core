package com.dimeng.p2p.user.service;


import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;
import com.dimeng.p2p.user.entity.BindQueryResult;
import com.dimeng.p2p.user.entity.CheckResult;
import com.dimeng.p2p.user.entity.NewProtocolBindXmlBeanReq;
import com.dimeng.p2p.user.entity.NewProtocolOrderXmlBeanReq;
import com.dimeng.p2p.user.entity.NewProtocolQueryXmlBeanReq;
import com.dimeng.p2p.user.entity.PayResult;
import com.dimeng.p2p.user.entity.SignMsgResult;
import com.dimeng.p2p.user.entity.SignResult;
import com.dimeng.p2p.user.entity.TerminationResult;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述> 协议支付接口
 * 
 * @author 
 * @version  [版本号, 2018年6月28日]
 */
public abstract interface FuYouPayManageService extends Service
{
  
    /**
     * <一句话功能简述>
     * <功能详细描述> 协议支付签约短信触发
     * @param accountId
     * @return
     * @throws Throwable
     */
    public abstract SignMsgResult signMsg(NewProtocolBindXmlBeanReq req,int id)
        throws Throwable;
     
    /**
     * <一句话功能简述>
     * <功能详细描述> 协议支付签约
     * @param accountId
     * @param msg 短信
     * @return
     * @throws Throwable
     */
    public abstract SignResult sign(NewProtocolBindXmlBeanReq req,int id)
        throws Throwable;
    
    /**
     * <一句话功能简述> 
     * <功能详细描述>协议支付解约
     * 
     * @param accountId
     * @return
     * @throws Throwable
     */
   public abstract TerminationResult termination(NewProtocolBindXmlBeanReq req,int id )
        throws Throwable;
   
   /**
    * <一句话功能简述> 
    * <功能详细描述>协议卡查询
    * 
    */
   public abstract BindQueryResult bindQuery(NewProtocolBindXmlBeanReq req)
	        throws Throwable;
  
   
   /**
    * <一句话功能简述> 
    * <功能详细描述> 协议支付
    * 
    * @param accountId
    * @param amount
    * @return
    * @throws Throwable
    */
    public abstract PayResult signPay(NewProtocolOrderXmlBeanReq req,int id)
       throws Throwable;
   
   
    
    /**
     * <一句话功能简述>
     * <功能详细描述> 查询交易结果
     * @param orderId
     * @param querySn
     * url: http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkResult.pay
     * @throws Throwable
     */
    public abstract CheckResult chargeQuery(NewProtocolQueryXmlBeanReq req)
        throws Throwable;
    
    /**
     * 添加充值订单
     * 
     * @param amount
     *            充值金额
     * @param payCompanyCode
     *            支付公司代号
     * @return
     * 
     * @throws Throwable
     */
    public abstract ChargeOrder addOrder(BigDecimal amount, int payCompanyCode)
        throws Throwable;
    
    
    /**
     * <一句话功能简述>
     * <功能详细描述> 更新订单表状态
     * @param orderId
     * @param querySn
     * @throws Throwable
     */
    public abstract void updateT6501(int orderId, String querySn)
        throws Throwable;

	public abstract void updateT6502(int orderId,String F01,String F02) throws Throwable;;
    
}
