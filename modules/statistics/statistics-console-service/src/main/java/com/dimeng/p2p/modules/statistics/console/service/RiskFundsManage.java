package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S70.entities.T7029;
import com.dimeng.p2p.S70.entities.T7039;
import com.dimeng.p2p.S70.entities.T7040;
import com.dimeng.p2p.common.enums.OrganizationStatus;
import com.dimeng.p2p.modules.statistics.console.service.entity.Organization;
import com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds;
import com.dimeng.p2p.modules.statistics.console.service.entity.Sponsors;
import com.dimeng.p2p.modules.statistics.console.service.entity.YearRiskFunds;
import com.dimeng.util.io.CVSWriter;

public abstract interface RiskFundsManage extends Service
{
    
    /**
     * <dt>
     * <dl>
     * 描述：获取季度风险保证金统计
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0} 则直接退出</li>
     * <li>如果{@code orgId < 0}并且{@code orgId != -1} 则直接返回{@code null}</li>
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
     * <li>如果{@code orgId == -1}则 查询{@link T7039}表   查询条件：{@code T7039.F01 = year GROUP BY T7039.F02}</li>
     * <li>按照{@code T7039.F02}字段升序排序</li>
     * <li>查询字段列表:</li>
     * <ol>
     * <li>{@code QuarterFunds[T7039.F02 -1 ]}得到{@code QuarterFunds}</li>
     * <li>{@link QuarterFunds#quarter}对应{@code T7039.F02}</li>
     * <li>{@link QuarterFunds#amountIn}对应{@code IFNULL(SUM(T7039.F03),0)}</li>
     * <li>{@link QuarterFunds#amountOut}对应{@code IFNULL(SUM(T7039.F04),0)}</li>
     * <li>{@link QuarterFunds#sum}对应{@code IFNULL(SUM(T7039.F05),0)}</li>
     * </ol>
     * <li>如果{@code orgId >= 0}则 查询{@link T7039}表   查询条件：{@code T7039.F01 = year AND T7039.F07 = orgId}</li>
     * <li>按照{@code T7039.F02}字段升序排序</li>
     * <li>查询字段列表:</li>
     * <ol>
     * <li>{@code QuarterFunds[T7039.F02 -1 ]}得到{@code QuarterFunds}</li>
     * <li>{@link QuarterFunds#quarter}对应{@code T7039.F02}</li>
     * <li>{@link QuarterFunds#amountIn}对应{@code T7039.F03}</li>
     * <li>{@link QuarterFunds#amountOut}对应{@code T7039.F04}</li>
     * <li>{@link QuarterFunds#sum}对应{@code T7039.F05}</li>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param orgId -1:全部（平台和所有机构），0：平台，>0,机构id
     * 			机构id
     * @param year 年份
     * @return {@link QuarterFunds}{@code []} 查询结果
     * @throws Throwable
     */
    public abstract QuarterFunds[] getQuarterFunds(int orgId, int year)
        throws Throwable;
    
    /**
     * 查询担保机构列表
     * @return
     * @throws Throwable
     */
    public abstract T6161[] selectT6161()
        throws Throwable;
    
    /**
     * 查询个人、企业担保方列表
     * @return
     * @throws Throwable
     */
    public abstract Sponsors[] selectSponsors()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：获取年度风险保证金统计
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0} 则直接返回{@code null}</li>
     * <li>如果{@code orgId < 0}并且{@code orgId != -1} 则直接返回{@code null}</li>
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
     * <li>如果{@code orgId == -1}则 查询{@link T7040}表   查询条件：{@code T7040.F01 = year }</li>
     * <li>查询字段列表:</li>
     * <ol>
     * <li>{@link YearRiskFunds#df}对应{@code IFNULL(SUM(T7040.F03),0)}</li>
     * <li>{@link YearRiskFunds#dffh}对应{@code IFNULL(SUM(T7040.F04),0)}</li>
     * <li>{@link YearRiskFunds#jkcjfwf}对应{@code IFNULL(SUM(T7040.F05),0)}</li>
     * <li>{@link YearRiskFunds#sdzjbzj}对应{@code IFNULL(SUM(T7040.F06),0)}</li>
     * <li>{@link YearRiskFunds#sdkcbzj}对应{@code IFNULL(SUM(T7040.F07),0)}</li>
     * </ol>
     * <li>如果{@code orgId >= 0}则 查询{@link T7040}表   查询条件：{@code T7040.F01 = year AND T7040.F08 = orgId}</li>
     * <li>查询字段列表:</li>
     * <ol>
     * <li>{@link YearRiskFunds#df}对应{@code T7040.F03}</li>
     * <li>{@link YearRiskFunds#dffh}对应{@code T7040.F04}</li>
     * <li>{@link YearRiskFunds#jkcjfwf}对应{@code T7040.F05}</li>
     * <li>{@link YearRiskFunds#sdzjbzj}对应{@code T7040.F06}</li>
     * <li>{@link YearRiskFunds#sdkcbzj}对应{@code T7040.F07}</li>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param year
     * @return {@link YearRiskFunds} 查询结果
     * @param orgId  -1:全部（平台和所有机构），0：平台，>0,机构id
     * 			机构id
     * @throws Throwable
     */
    public abstract YearRiskFunds getYearRiskFunds(int orgId, int year)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述： 获取已经统计的年份
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
     * <li>查询{@link T7039}表<li>
     * 查询字段列表:
     * <ol>
     * <li>{@code int}对应{@code DISTINCT(T7039.F01)}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return {@code int[]} 查询结果
     * @throws Throwable
     */
    public abstract int[] getStatisticedYear()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：获取机构列表
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
     * <li>查询{@link T7029}表  查询条件{@code T7029.F05 = }{@link OrganizationStatus#YX}<li>
     * 查询字段列表:
     * <ol>
     * <li>{@link Organization#id}对应{@code T7029.F01}</li>
     * <li>{@link Organization#name}对应{@code T7029.F02}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return {@link Organization}{@code []}查询结果
     * @throws Throwable
     */
    public abstract Organization[] orgList()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出风险保证金统计csv文件
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code outputStream == null}则直接返回</li>
     * <li>如果{@code quarterFunds == null}或则{@code quarterFunds}长度小于等于0   则直接返回</li>
     * <li>如果{@code charset}为空 则赋值GBK</li>
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
     * <li>调用{@link CVSWriter}封装类，导出文件<li>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param quarterFunds 平台资金统计
     * @param outputStream 输出流
     * @param charset 字符编码
     * @throws Throwable
     */
    public abstract void export(QuarterFunds[] quarterFunds, OutputStream outputStream, String charset)
        throws Throwable;
    
}
