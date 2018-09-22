package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S70.entities.T7037;
import com.dimeng.p2p.S70.entities.T7038;
import com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds;
import com.dimeng.p2p.modules.statistics.console.service.entity.YearFunds;
import com.dimeng.util.io.CVSWriter;

public abstract interface FundsManage extends Service
{
    
    /**
     * <dt>
     * <dl>
     * 描述：根据年份获取季度资金统计
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0} 则直接退出</li>
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
     * <li>查询{@link T7037}表   查询条件：{@code T7037.F01 = year}<li>
     * <li>按照{@code T7037.F02}字段降序排序</li>
     * 查询字段列表:
     * <ol>
     * <li>{@code QuarterFunds[T7037.F02 -1 ]}得到{@code QuarterFunds}</li>
     * <li>{@link QuarterFunds#quarter}对应{@code T7037.F02}</li>
     * <li>{@link QuarterFunds#amountIn}对应{@code T7037.F03}</li>
     * <li>{@link QuarterFunds#amountOut}对应{@code T7037.F04}</li>
     * <li>{@link QuarterFunds#sum}对应{@code T7037.F05}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param year 年份
     * @return {@link QuarterFunds}{@code []}查询结果数组
     * @throws Throwable
     */
    public abstract QuarterFunds[] getQuarterFunds(int year)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：根据年份获取年份资金统计
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0} 则直接退出</li>
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
     * <li>查询{@link T7038}表   查询条件：{@code T7038.F01 = year}<li>
     * 查询字段列表:
     * <ol>
     * <li>{@link YearFunds#czsxf}对应{@code T7038.F03}</li>
     * <li>{@link YearFunds#txsxf}对应{@code T7038.F04}</li>
     * <li>{@link YearFunds#czcb}对应{@code T7038.F05}</li>
     * <li>{@link YearFunds#txcb}对应{@code T7038.F06}</li>
     * <li>{@link YearFunds#sfyzsxf}对应{@code T7038.F07}</li>
     * <li>{@link YearFunds#jkglf}对应{@code T7038.F08}</li>
     * <li>{@link YearFunds#yqglf}对应{@code T7038.F09}</li>
     * <li>{@link YearFunds#zqzrf}对应{@code T7038.F10}</li>
     * <li>{@link YearFunds#hdfy}对应{@code T7038.F11}</li>
     * 
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param year
     *            年份
     * @return {@link YearFunds}查询结果   没有则返回{@code null}
     * @throws Throwable
     */
    public abstract YearFunds getYearFunds(int year)
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
     * <li>查询{@link T7037}表<li>
     * 查询字段列表:
     * <ol>
     * <li>{@code int}对应{@code DISTINCT(T7037.F01)}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return {@code int[]}查询结果
     * @throws Throwable
     */
    public abstract int[] getStatisticedYear()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出平台资金统计csv文件
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
     * 
     * 
     * @param quarterFunds  平台资金统计
     * @param outputStream 输出流
     * @param charset 编码格式
     * @throws Throwable
     */
    public abstract void export(QuarterFunds[] quarterFunds, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据年份获取季度资金统计
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code year <= 0} 则直接退出</li>
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
     * <li>查询{@link T7037}表   查询条件：{@code T7037.F01 = year}<li>
     * <li>按照{@code T7037.F02}字段降序排序</li>
     * 查询字段列表:
     * <ol>
     * <li>{@code QuarterFunds[T7037.F02 -1 ]}得到{@code QuarterFunds}</li>
     * <li>{@link QuarterFunds#quarter}对应{@code T7037.F02}</li>
     * <li>{@link QuarterFunds#amountIn}对应{@code T7037.F03}</li>
     * <li>{@link QuarterFunds#amountOut}对应{@code T7037.F04}</li>
     * <li>{@link QuarterFunds#sum}对应{@code T7037.F05}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param year 年份
     * @return {@link QuarterFunds}{@code []}查询结果数组
     * @throws Throwable
     */
    public abstract QuarterFunds[] getRosesStatistics(int year)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出平台资金统计csv文件
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
     * 
     * 
     * @param quarterFunds  平台月度资金统计
     * @param outputStream 输出流
     * @param charset 编码格式
     * @throws Throwable
     */
    public abstract void exportRosesStatistics(QuarterFunds[] quarterFunds, OutputStream outputStream, String charset)
        throws Throwable;
    
}
