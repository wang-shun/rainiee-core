package com.dimeng.p2p.modules.bid.front.service;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6299;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6299_F04;
import com.dimeng.p2p.S70.entities.T7050;
import com.dimeng.p2p.S70.entities.T7051;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecord;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecordInfo;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.FrontReleaseBid;
import com.dimeng.p2p.modules.bid.front.service.entity.FrontT6250;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.modules.bid.front.service.entity.InvestorTotal;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Sytjsj;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.front.service.query.BidAllQuery;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.BidQueryAccount;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery_Order;
import com.dimeng.p2p.modules.bid.front.service.query.BidTypeQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQueryAccount;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQueryExt;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidTypeQuery;

/**
 * 标管理
 * 
 */
public interface BidManage extends Service
{
    
    /**
     * <dt>
     * <dl>
     * 描述：根据标ID查询标主信息.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public Bdxq get(int id)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据ID查询标扩展信息.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public T6231 getExtra(int id)
        throws Throwable;
    
    /**
     * 描述：根据ID查询债权转让详情信息
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public Zqzqlb getZqzrXq(int id)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：标.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> search(BidQuery query, Paging paging)
        throws Throwable;
    
    public abstract PagingResult<Bdlb> search(BidQuery_Order query, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：标.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract Bdlb[] searchblx(QyBidQuery query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable;
    
    /**
     * 描述：标.
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchAccount(BidQueryAccount query, Paging paging)
        throws Throwable;
    
    /**
     * 分页查询所有标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchBids(BidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询个人标列表
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchQy(QyBidQuery query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    /**
     * 查询企业标列表 带标类型查询
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchQyblx(QyBidTypeQuery query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    /**
     * 查询个人标列表-贷款金额范围
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchQyAccount(QyBidQueryAccount query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    public abstract PagingResult<Bdlb> searchQyExt(QyBidQueryExt query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    /**
     * 查询所有散标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchAll(QyBidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询所有非推荐标的散标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchNotTJB(QyBidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 线下债权转让列表
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchXXZQ(QyBidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询线下债权转让详情
     * 
     * @param bidId
     * @return
     * @throws Throwable
     */
    public abstract T6240 getXXZQ(int bidId)
        throws Throwable;
    
    /**
     * 投资记录
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6250[] getRecord(int id)
        throws Throwable;
    
    public abstract PagingResult<T6250> getRecords(int id, Paging paging)
        throws Throwable;
    
    /**
     * 查询投资人数
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract int getTbCount(int id)
        throws Throwable;
    
    /**
     * 获取还款记录
     * 
     * @param 借款标id
     * @return
     */
    public abstract Hkjllb[] getHk(int id)
        throws Throwable;
    
    public abstract PagingResult<Hkjllb> getHks(int id, Paging paging)
        throws Throwable;
    
    public abstract PagingResult<Hkjllb> getMyHks(int id, Paging paging)
        throws Throwable;
    
    /**
     * 获取还款记录-不分组
     * 
     * @param 借款标id
     * @return
     */
    public abstract Hkjllb[] getHkNotGroup(int id)
        throws Throwable;
    
    /**
     * 查询风控信息
     * 
     * @param 借款标id
     * @return
     */
    public abstract T6237 getFk(int id)
        throws Throwable;
    
    /**
     * 标担保信息
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract Dbxx getDB(int id)
        throws Throwable;
    
    /**
     * 标抵押列表
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract Bdylx getDylb(int id)
        throws Throwable;
    
    /**
     * 根据抵押ID查询抵押属性
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract Bdysx[] getDysx(int id)
        throws Throwable;
    
    /**
     * 标附件列表(非公开)
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6233[] getFjfgk(int id)
        throws Throwable;
    
    /**
     * 标附件列表(公开)
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6232[] getFjgk(int id)
        throws Throwable;
    
    /**
     * 标债权信息
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6251[] getZqxx(int id)
        throws Throwable;
    
    /**
     * 线上债权转让信息
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract Zqzrxx[] getZqzrxx(int id)
        throws Throwable;
    
    /**
     * 获取企业及个人投资统计信息
     * @return
     * @throws Throwable
     */
    public abstract Tztjxx getStatistics()
        throws Throwable;
    
    /**
     * 获取企业投资统计信息.
     * 
     * @return {@link Tztjxx}
     * @throws Throwable
     */
    public abstract Tztjxx getStatisticsQy()
        throws Throwable;
    
    /**
     * 获取个人投资统计信息.
     * 
     * @return {@link Tztjxx}
     * @throws Throwable
     */
    public abstract Tztjxx getStatisticsGr()
        throws Throwable;
    
    /**
     * 获取线下债权投资统计信息.
     * 
     * @return {@link Tztjxx}
     * @throws Throwable
     */
    public abstract Tztjxx getStatisticsXxzq()
        throws Throwable;
    
    /**
     * 标的类型列表
     * 
     * @return {@link Tztjxx}
     * @throws Throwable
     */
    public abstract T6211[] getBidType()
        throws Throwable;
    
    /**
     * 根据条件查询标的类型
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6211[] getBidTypeByCondition(String ids)
        throws Throwable;
    
    /**
     * 信用等级列表
     * 
     * @return {@link Tztjxx}
     * @throws Throwable
     */
    public abstract T5124[] getLevel()
        throws Throwable;
    
    /**
     * 查询满标的待还金额
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public abstract Mbxx getMbxx(int loanId)
        throws Throwable;
    
    /**
     * 根据附件ID查询附件内容
     */
    public abstract Blob getAttachment(int id)
        throws Throwable;
    
    /**
     * 查询用户的借款列表
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchCredit(int userId, Paging paging)
        throws Throwable;
    
    /**
     * 查询预发布的标信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract Bdlb getNewBid()
        throws Throwable;
    
    /**
     * 首页统计数据
     * 
     * @return
     * @throws Throwable
     */
    public abstract Sytjsj getSytj()
        throws Throwable;
    
    /**
     * 查询首页统计
     */
    public abstract IndexStatic getIndexStatic()
        throws Throwable;
    
    /**
     * 获取线下债权转让
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public abstract T6240 getT6240(int loanId)
        throws Throwable;
    
    /**
     * 获取标附件类型列表
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public abstract T6212[] getT6212(int loanId, boolean b)
        throws Throwable;
    
    /**
     * 根据用户的ID得到用户的投资等级
     * 
     * @param userID
     * @return
     * @throws Throwable
     */
    public abstract T5127_F03 getTzdj(int userID)
        throws Throwable;
    
    /**
     * 查询标是否有相关文件
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public abstract boolean isXgwj(int loanId)
        throws Throwable;
    
    /**
     * 根据条件分页查询标列表
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchAllBid(BidAllQuery query, T6110_F06 f06, Paging paging)
        throws Throwable;
    
    /**
     * 根据条件分页查询线下债权转让列表 所有条件
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchXXZQ(BidAllQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 投资总人数
     * 
     * @return
     * @throws Throwable
     */
    public abstract int lcTotle()
        throws Throwable;
    
    /**
     * 用户累计赚取
     * 
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal yhzqTotle()
        throws Throwable;
    
    /**
     * 投资人信息统计
     * 
     * @return {@link InvestorTotal}
     * @throws Throwable
     */
    public abstract InvestorTotal getInvestorTotal()
        throws Throwable;
    
    /**
     * 获取相应数量的投资记录
     * 
     * @param count
     *            默认20
     * @return
     * @throws Throwable
     */
    public abstract BidRecord[] getBidRecords(int count)
        throws Throwable;
    
    /**
     * 获取用户周投资统计排行
     * 
     * @param count
     * @return
     * @throws Throwable
     */
    public abstract T7050[] getUserBidRankForWeek(int count)
        throws Throwable;
    
    /**
     * 获取用户月投资统计排行
     * 
     * @param count
     * @return
     * @throws Throwable
     */
    public abstract T7051[] getUserBidRankForMonth(int count)
        throws Throwable;
    
    /**
     * 查询用户总风险备用金
     */
    public abstract BigDecimal getTotalAmount()
        throws Throwable;
    
    /**
     * 查询用户已使用风险备用金
     */
    public abstract BigDecimal getUseAmount()
        throws Throwable;
    
    /**
     * 查询标的最高年化利率
     */
    public abstract BigDecimal getBidMaxYearRate()
        throws Throwable;
    
    /**
     * 根据参数的标的类型获取标的数据
     * @param bidType
     * @param paging
     * @return PagingResult<Bdlb>
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> getBidDbType(BidTypeQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06,
        Paging paging)
        throws Throwable;
    
    /**
     * 获取融资金额
     * @return 融资金额
     * @throws Throwable
     */
    public abstract BigDecimal getRzje()
        throws Throwable;
    
    /**
     * 已投资总金额
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal total()
        throws Throwable;
    
    /**
     * 已投资记录
     * @param limit
     * @return
     * @throws Throwable
     */
    public abstract FrontT6250[] getBids(int limit)
        throws Throwable;
    
    /**
     * 已发布借款记录
     * @param limit
     * @param status
     * @return
     * @throws Throwable
     */
    public abstract FrontReleaseBid[] getReleaseBids(int limit, String status)
        throws Throwable;
    
    public abstract BigDecimal yhzsy()
        throws Throwable;
    
    /**
     * 根据标ID查询逾期金额
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqje(int loanId)
        throws Throwable;
    
    public abstract BigDecimal LoanTotal()
        throws Throwable;
    
    public abstract BigDecimal backOnTime()
        throws Throwable;
    
    /**
     * 分页功能
     * 返回拼接好的分页字符串
     * @param paging 分页对象
     * @return 分页字符串
     * @throws Throwable
     */
    public String rendPaging(PagingResult<?> paging)
        throws Throwable;
    
    /**
     * 根据标的id,查询标的信息
     * @return 标的信息
     * @throws SQLException
     * @author luoyi
     * @date 2015-04-29
     */
    T6230 findT6230ByF01(int f01)
        throws SQLException;
    
    /**
     * 根据标的id,查询标的扩展信息
     * @return 标的信息
     * @throws SQLException
     * @author luoyi
     * @date 2015-04-29
     */
    T6231 findT6231ByF01(int f01)
        throws SQLException;
    
    /**
     * 返回数据库当前时间
     * @param
     * @return
     * @throws Throwable
     */
    Date getCurrentDate()
        throws Throwable;
    
    /**
     * 返回数据库当前时间
     * @param
     * @return
     * @throws Throwable
     */
    Timestamp getCurrentTimestamp()
        throws Throwable;
    
    /**
     * 根据标Id查询投资总额
     * @param bid
     * @return
     * @throws SQLException
     */
    public BigDecimal getTzMoney(int bid)
        throws SQLException;
    
    /**
     * 标附件列表(非公开)
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6233[] getFjfgks(int id)
        throws Throwable;
    
    /**
     * 标附件列表(公开)
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6232[] getFjgks(int id)
        throws Throwable;
    
    /**
     * 查询所有新手标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchXSB(QyBidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询所有推荐标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Bdlb> searchTJB(QyBidQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询所有推荐标
     * 
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract boolean haveTJB()
        throws Throwable;
    
    /**
     * 标的产品
     *
     */
    public abstract T6216[] getProducts()
        throws Throwable;
    
    /**
     * 根据产品ID查询产品实体
     *
     */
    public abstract T6216 getProductById(Integer id)
        throws Throwable;
    
    /**
     * 是否能投新手标
     * @param loanId
     * @return
     * @throws Throwable
     */
    boolean isCanTXSB(int loanId)
        throws Throwable;
    
    /**
     * 获取用户年投资统计排行
     * 
     * @param 
     * @return
     * @throws Throwable
     */
    public abstract T7051[] getUserBidRankForYear()
        throws Throwable;
    
    /**
     * 获取用户年投资统计排行
     * 
     * @param 
     * @return
     * @throws Throwable
     */
    public abstract T7051[] getUserBidRankForYear(int count)
        throws Throwable;
    
    /**
     * 投资记录
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract BidRecordInfo[] getBidRecordList(int id)
        throws Throwable;
    
    /** 获取第一条筛选条件
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299 getFirst(T6299_F04 type)
        throws Throwable;
    
    /** 获取最后一条筛选条件
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299 getLast(T6299_F04 type)
        throws Throwable;
    
    /**
     * 获取增加的筛选条件集合
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T6299[] getAddFilter(T6299_F04 type)
        throws Throwable;
    
    public BidRecordInfo[] getBidRecordList1(int id)
        throws Throwable;
    
    /**
     * 查看标动态管理列表
     * @param loandId
     * @return
     * @throws Throwable
     */
    PagingResult<T6248> viewBidProgresList(int loandId, Paging paging)
        throws Throwable;
    
    /**
     * 根据风险等级类型查询
     * @param F02
     * @return
     * @throws Throwable
     */
    public T6148 getT6148(String F02)
        throws Throwable;
    
    /**
     * 查询用户已投金额
     * @param bid
     * @param userId
     * @return
     * @throws Throwable
     */
    public BigDecimal selectUserMaxAmount(int bid, int userId)
        throws Throwable;
    
    /**
     * @param id
     * @return
     * @throws Throwable
     */
    public T6230 queryT6230(int id)
        throws Throwable;
    
    /**
     * 判断用户是否存在逾期借款
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract boolean isUserHasYQ(int userId)
        throws Throwable;
}
