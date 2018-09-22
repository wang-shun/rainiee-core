/*
 * 文 件 名:  BusinessManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月3日
 */
package com.dimeng.p2p.app.service;

import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecordInfo;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;

/**
 * app业务管理
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月3日]
 */
public interface BusinessManage extends Service
{
    /**
     * 查看标的投资记录
     * 
     * @param id 标ID
     * @return 标的投资记录
     * @throws Throwable 异常信息
     */
    public BidRecordInfo[] getBidRecordList(int id)
        throws Throwable;
    
    /**
     * 根据标的type查询新手入门后台配置名称
     * <功能详细描述>
     * @param loanId
     * @return
     * @throws Throwable
     */
    public String getT5010Name(String type)
        throws Throwable;
    
    /**
     * 根据手机号码查询交易密码
     * @param phoneNumber
     * @throws Throwable
     */
    public abstract String getTxPwdByPhone(String phoneNumber) throws Throwable;
 
    /**
     * 根据ID获取地址详情
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6355 getAddress(int id)
        throws Throwable;
    
    /**
     * 没有默认地址时，获取最新的收货地址
     * <功能详细描述>
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract T6355[] queryHarvestAddress()
        throws Throwable;
    
    /**
     * 获取收获地址
     * <功能详细描述>
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6355> queryHarvestAddress(Paging page)
        throws Throwable;
    
    /**
     * 我的体验金统计信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyExperience> searMyExperienceById(Map<String, Object> params, Paging paging)
        throws Throwable;
    
    /**
     * 根据债权ID查询债权创建时间
     * @param phoneNumber
     * @throws Throwable
     */
    public abstract String getData(String creditorId) throws Throwable;
    
    /**
     * 查询是否有逾期的还款
     * 
     * @param F11 债权ID
     * @return 是否存在逾期
     * @throws Throwable 异常信息
     */
    public String isExpired(int creditorId) throws Throwable;
    
    /**
     * 已投新手标数量 
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    public int getXsbCount(int userId) throws SQLException;
    
    /**
     * 获取某用户的债权转让数量
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
     public int getZqzrCount(int userId) throws SQLException;
    
     /**
      * 根据债权ID查询债权下个还款日
      * @param phoneNumber
      * @throws Throwable
      */
     public abstract String getNextData(String creditorId) throws Throwable;
     
     /**
      * 查询交易记录
      * @param paging 分页信息
      * @return
      * @throws Throwable
      */
     public PagingResult<CapitalLs> mobileSearchLs(Paging paging)
         throws Throwable;
     
     /**
      * 更新认证时间
      * 
      * @param source 来源
      * @throws Throwable
      */
     public void updateT6198F06(String source) throws Throwable;
     
     /**
      * 获取某用户的转让中债权数量
      * 
      * @param userId
      * @return
      * @throws Throwable
      */
      public int getZrzZqCount() throws SQLException;
      
      /**
       * 获取某用户的已转出债权数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getYzcZqCount() throws SQLException;
      
      /**
       * 获取某用户的已转入债权数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getYzrZqCount() throws SQLException;
      
      /**
       * 获取某用户的回款中债权数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getHkzZqCount() throws SQLException;
      
      /**
       * 获取某用户的已结清债权数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getYjqZqCount() throws SQLException;
      
      /**
       * 获取某用户的投资中债权数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getTzzZqCount() throws SQLException;
      
      /**
       * 获取某用户的订单数量
       * 
       * @param userId
       * @return
       * @throws Throwable
       */
      public int getOrderCount() throws SQLException;
      
      /**
       * 查询所有散标
       * 
       * @param query
       * @param paging
       * @return
       * @throws Throwable
       */
      public abstract PagingResult<Bdlb> searchAll(QyBidQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
          throws Throwable;
      
      /**
       * 通过手机获取用户名
       * <功能详细描述>
       * @param phone
       * @return
       * @throws Throwable
       */
      public String getUserNameByPhone(String phone) throws Throwable;

    /**
     * 获取用户绑定的银行卡信息
     *
     * @param userId
     * @return
     * @throws Throwable
     */
    public T6114 getBankCard(int userId) throws SQLException;

    /**
     * 获取用户绑定银行卡的银行编码
     *
     * @param userId
     * @return
     * @throws Throwable
     */
    public String getBankCode(int bankId) throws SQLException;
}
