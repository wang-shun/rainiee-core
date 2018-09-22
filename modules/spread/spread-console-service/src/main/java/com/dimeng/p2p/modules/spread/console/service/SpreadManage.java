package com.dimeng.p2p.modules.spread.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.entities.T6010;
import com.dimeng.p2p.S60.entities.T6011;
import com.dimeng.p2p.S60.entities.T6053;
import com.dimeng.p2p.S60.entities.T6054;
import com.dimeng.p2p.S60.entities.T6055;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusList;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SeriesDetailList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadDetailList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadDetailQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalInfo;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalQuery;

/**
 * 推广管理 
 *
 */
public abstract interface SpreadManage extends Service{
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询推广奖励概要列表
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
	 * <li>查询{@link T6053}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T6053.F01 = T6010.F01 }{@code T6053.F01 = T6011.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadTotalQuery#id()}大于0  则{@code T6053.F01 = }{@link SpreadTotalQuery#id()}</li>
	 * <li>如果{@link SpreadTotalQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadTotalQuery#userName()}</li>
	 * <li>如果{@link SpreadTotalQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadTotalQuery#name()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadTotalList#personID}对应{@code T6053.F01}</li>
	 * <li>{@link SpreadTotalList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadTotalList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadTotalList#customNum}对应{@code T6053.F02}</li>
	 * <li>{@link SpreadTotalList#spreadMoney}对应{@code T6053.F03}</li>
	 * <li>{@link SpreadTotalList#seriesRewarMoney}对应{@code T6053.F04}</li>
	 * <li>{@link SpreadTotalList#validRewardMoney}对应{@code T6053.F05}</li>
	 * <li>{@link SpreadTotalList#rewardTotalMoney}对应{@code T6053.F04} 加上 {@code T6053.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link SpreadTotalList}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public PagingResult<SpreadTotalList> searchTotalList(SpreadTotalQuery query,Paging paging) throws Throwable;
    
    /**
     * 查询字段列表:连续奖励总计,有效推广奖励总计,推广奖励总计
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public SpreadTotalList searchTotalListAmount(SpreadTotalQuery query)
        throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询推广奖励概要统计信息
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
	 * <li>查询{@link T6053}表  </li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadTotalInfo#personNum}对应{@code COUNT(T6053.F01)}</li>
	 * <li>{@link SpreadTotalInfo#spreadPersonNum}对应{@code IFNULL(SUM(T6053.F02),0)}</li>
	 * <li>{@link SpreadTotalInfo#totalMoney}对应{@code IFNULL(SUM(T6053.F03),0)}</li>
	 * <li>{@link SpreadTotalInfo#returnMoney}对应{@code IFNULL(SUM(T6053.F04),0)} 加上 {@code IFNULL(SUM(T6053.F05),0)}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return {@link SpreadTotalInfo} 查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public SpreadTotalInfo getSpreadTotal() throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询持续奖励列表
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
	 * <li>查询{@link T6055}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T6055.F03=T6010.F01 }{@code T6010.F01=T6011.F01 }{@code T6055.F02 = id }</li>
	 * <li>按照{@code T6055.F06}字段降序排序</li>
	 *
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SeriesDetailList#id}对应{@code T6055.F03}</li>
	 * <li>{@link SeriesDetailList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SeriesDetailList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SeriesDetailList#money}对应{@code T6055.F04}</li>
	 * <li>{@link SeriesDetailList#rewarMoney}对应{@code T6055.F05}</li>
	 * <li>{@link SeriesDetailList#investTime}对应{@code T6055.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 推广人ID
	 * @param paging 分页参数
	 * @return {@link SeriesDetailList} 查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public PagingResult<SeriesDetailList> searchSeriesList(int id,Paging paging) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询推广人的姓名
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
	 * <li>查询{@link T6011}表   查询条件：{@code T6011.F01 = id} </li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>返回结果{@link String}对应{@code T6011.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 推广人ID
	 * @return {@link String} 查询结果
	 * @throws Throwable
	 */
	public String getName(int id) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询推广持续奖励详情列表.
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
	 * <li>查询{@link T6055}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:     
	 * <ol>
	 * <li>{@code T6055.F02 = T6010.F01 }{@code T6055.F02 = T6011.F01 }
	 *   {@code T6055.F03 = T6010.F01 }{@code T6055.F03 = T6011.F01 }
	 * </li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadQuery#id()}大于0  则{@code T6055.F02= }{@link SpreadQuery#id()}</li>
	 * <li>如果{@link SpreadQuery#personID()}大于0  则{@code T6055.F03= }{@link SpreadQuery#personID()}</li>
	 * <li>如果{@link SpreadQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadQuery#userName()}</li>
	 * <li>如果{@link SpreadQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadQuery#name()}</li>
	 * <li>如果{@link SpreadQuery#investStartTime()}不为空  则{@code T6055.F06 >= }{@link SpreadQuery#investStartTime()}</li>
	 * <li>如果{@link SpreadQuery#investEndTime()}不为空  则{@code DATE(T6055.F06) <= }{@link SpreadQuery#investEndTime()}</li>
	 * <li>如果{@link SpreadQuery#personUserName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadQuery#personUserName()}</li>
	 * <li>如果{@link SpreadQuery#personName()}不为空  则{@code T6011.F06 LIKE }{@link SpreadQuery#personName()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6055.F06}字段降序排序</li>
	 * 
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadList#id}对应{@code T6055.F02}</li>
	 * <li>{@link SpreadList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadList#personID}对应{@code T6055.F03}</li>
	 * <li>{@link SpreadList#personUserName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadList#personName}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadList#spreadTotalMoney}对应{@code T6055.F04}</li>
	 * <li>{@link SpreadList#rewardMoney}对应{@code T6055.F05}</li>
	 * <li>{@link SpreadList#investTime}对应{@code T6055.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页查询参数
	 * @return {@link PagingResult}{@code <}{@link SpreadDetailList}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public PagingResult<SpreadList> searchSpreadList(SpreadQuery query,Paging paging) throws Throwable;
	    
    /**
     * 描述：分页查询推广持续奖励详情列表.推广投资总金额，持续奖励总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public SpreadList searchSpreadListAmount(SpreadQuery query)
        throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：分页查询分页查询推广详情列表
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
	 * <li>查询{@link T6054}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:     
	 * <ol>
	 * <li>{@code T6054.F02 = T6010.F01 }{@code T6054.F02 = T6011.F01 }
	 *   {@code T6054.F03 = T6010.F01 }{@code T6054.F03 = T6011.F01 }
	 * </li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadDetailQuery#id()}大于0  则{@code T6054.F02= }{@link SpreadDetailQuery#id()}</li>
	 * <li>如果{@link SpreadDetailQuery#personID()}大于0  则{@code T6054.F03= }{@link SpreadDetailQuery#personID()}</li>
	 * <li>如果{@link SpreadDetailQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadDetailQuery#userName()}</li>
	 * <li>如果{@link SpreadDetailQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadDetailQuery#name()}</li>
	 * <li>如果{@link SpreadDetailQuery#startTime()}不为空  则{@code DATE(T6054.F06) >= }{@link SpreadDetailQuery#startTime()}</li>
	 * <li>如果{@link SpreadDetailQuery#endTime()}不为空  则{@code DATE(T6054.F06) <= }{@link SpreadDetailQuery#endTime()}</li>
	 * <li>如果{@link SpreadDetailQuery#personUserName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadDetailQuery#personUserName()}</li>
	 * <li>如果{@link SpreadDetailQuery#personName()}不为空  则{@code T6011.F06 LIKE }{@link SpreadDetailQuery#personName()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6054.F06}字段降序排序</li>
	 * 
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadDetailList#id}对应{@code T6054.F02}</li>
	 * <li>{@link SpreadDetailList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadDetailList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadDetailList#personID}对应{@code T6054.F03}</li>
	 * <li>{@link SpreadDetailList#personUserName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadDetailList#personName}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadDetailList#firstMoney}对应{@code T6054.F04}</li>
	 * <li>{@link SpreadDetailList#firstTime}对应{@code T6054.F06}</li>
	 * <li>{@link SpreadDetailList#spreadRewardMoney}对应{@code T6054.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link SpreadDetailList}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public PagingResult<SpreadDetailList> searchSpreadDetailList(SpreadDetailQuery query,Paging paging) throws Throwable; 
    
    /**
     * 描述：分页查询分页查询推广详情列表有效推广总奖励
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public BigDecimal searchSpreadDetailListAmount(SpreadDetailQuery query)
        throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：导出推广持续奖励详情列表
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
	 * <li>查询{@link T6055}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:     
	 * <ol>
	 * <li>{@code T6055.F02 = T6010.F01 }{@code T6055.F02 = T6011.F01 }
	 *   {@code T6055.F03 = T6010.F01 }{@code T6055.F03 = T6011.F01 }
	 * </li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadQuery#id()}大于0  则{@code T6055.F02= }{@link SpreadQuery#id()}</li>
	 * <li>如果{@link SpreadQuery#personID()}大于0  则{@code T6055.F03= }{@link SpreadQuery#personID()}</li>
	 * <li>如果{@link SpreadQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadQuery#userName()}</li>
	 * <li>如果{@link SpreadQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadQuery#name()}</li>
	 * <li>如果{@link SpreadQuery#investStartTime()}不为空  则{@code T6055.F06 >= }{@link SpreadQuery#investStartTime()}</li>
	 * <li>如果{@link SpreadQuery#investEndTime()}不为空  则{@code DATE(T6055.F06) <= }{@link SpreadQuery#investEndTime()}</li>
	 * <li>如果{@link SpreadQuery#personUserName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadQuery#personUserName()}</li>
	 * <li>如果{@link SpreadQuery#personName()}不为空  则{@code T6011.F06 LIKE }{@link SpreadQuery#personName()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6055.F06}字段降序排序</li>
	 * 
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadList#id}对应{@code T6055.F02}</li>
	 * <li>{@link SpreadList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadList#personID}对应{@code T6055.F03}</li>
	 * <li>{@link SpreadList#personUserName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadList#personName}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadList#spreadTotalMoney}对应{@code T6055.F04}</li>
	 * <li>{@link SpreadList#rewardMoney}对应{@code T6055.F05}</li>
	 * <li>{@link SpreadList#investTime}对应{@code T6055.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @return {@link SpreadList}{@code []} 查询结果数组 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public SpreadList[] exportSpreadList(SpreadQuery query) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：导出推广奖励概要列表
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
	 * <li>查询{@link T6053}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T6053.F01 = T6010.F01 }{@code T6053.F01 = T6011.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadTotalQuery#id()}大于0  则{@code T6053.F01 = }{@link SpreadTotalQuery#id()}</li>
	 * <li>如果{@link SpreadTotalQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadTotalQuery#userName()}</li>
	 * <li>如果{@link SpreadTotalQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadTotalQuery#name()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadTotalList#personID}对应{@code T6053.F01}</li>
	 * <li>{@link SpreadTotalList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadTotalList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadTotalList#customNum}对应{@code T6053.F02}</li>
	 * <li>{@link SpreadTotalList#spreadMoney}对应{@code T6053.F03}</li>
	 * <li>{@link SpreadTotalList#seriesRewarMoney}对应{@code T6053.F04}</li>
	 * <li>{@link SpreadTotalList#validRewardMoney}对应{@code T6053.F05}</li>
	 * <li>{@link SpreadTotalList#rewardTotalMoney}对应{@code T6053.F04} 加上 {@code T6053.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param query 查询条件
	 * @return {@link SpreadTotalList}{@code []} 查询结果数组 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public SpreadTotalList[] exportSpreadTotalLists(SpreadTotalQuery query) throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：导出推广详情列表
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
	 * <li>查询{@link T6054}表{@link T6010}表{@link T6011}表</li>
	 * <li>查询条件:     
	 * <ol>
	 * <li>{@code T6054.F02 = T6010.F01 }{@code T6054.F02 = T6011.F01 }
	 *   {@code T6054.F03 = T6010.F01 }{@code T6054.F03 = T6011.F01 }
	 * </li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link SpreadDetailQuery#id()}大于0  则{@code T6054.F02= }{@link SpreadDetailQuery#id()}</li>
	 * <li>如果{@link SpreadDetailQuery#personID()}大于0  则{@code T6054.F03= }{@link SpreadDetailQuery#personID()}</li>
	 * <li>如果{@link SpreadDetailQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadDetailQuery#userName()}</li>
	 * <li>如果{@link SpreadDetailQuery#name()}不为空  则{@code T6011.F06 LIKE }{@link SpreadDetailQuery#name()}</li>
	 * <li>如果{@link SpreadDetailQuery#startTime()}不为空  则{@code DATE(T6054.F06) >= }{@link SpreadDetailQuery#startTime()}</li>
	 * <li>如果{@link SpreadDetailQuery#endTime()}不为空  则{@code DATE(T6054.F06) <= }{@link SpreadDetailQuery#endTime()}</li>
	 * <li>如果{@link SpreadDetailQuery#personUserName()}不为空  则{@code T6010.F02 LIKE }{@link SpreadDetailQuery#personUserName()}</li>
	 * <li>如果{@link SpreadDetailQuery#personName()}不为空  则{@code T6011.F06 LIKE }{@link SpreadDetailQuery#personName()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6054.F06}字段降序排序</li>
	 * 
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link SpreadDetailList#id}对应{@code T6054.F02}</li>
	 * <li>{@link SpreadDetailList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadDetailList#name}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadDetailList#personID}对应{@code T6054.F03}</li>
	 * <li>{@link SpreadDetailList#personUserName}对应{@code T6010.F02}</li>
	 * <li>{@link SpreadDetailList#personName}对应{@code T6011.F06}</li>
	 * <li>{@link SpreadDetailList#firstMoney}对应{@code T6054.F04}</li>
	 * <li>{@link SpreadDetailList#firstTime}对应{@code T6054.F06}</li>
	 * <li>{@link SpreadDetailList#spreadRewardMoney}对应{@code T6054.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @return {@link SpreadDetailList}{@code []} 查询结果数组  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public SpreadDetailList[] exportsSpreadDetailLists(SpreadDetailQuery query) throws Throwable;

	/**
	 * 奖励金详情
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public PagingResult<BonusList> getBonusList(BonusQuery query,Paging paging) throws Throwable;
    
    /**
     * 奖励金详情投资总金额，奖励总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public BonusList getBonusListAmount(BonusQuery query)
        throws Throwable;

	/**
	 *投资奖励金额总计
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getBonusSum() throws Throwable;

	/**
	 * 奖励金导出
	 *
	 * @param outputStream
	 * @throws Throwable
	 */
	public abstract void export(BonusList[] bonusLists,OutputStream outputStream) throws Throwable;
}
