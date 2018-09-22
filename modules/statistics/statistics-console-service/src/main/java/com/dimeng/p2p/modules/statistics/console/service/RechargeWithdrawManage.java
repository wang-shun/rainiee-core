package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWit;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReport;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReportStatistics;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitTotal;
import com.dimeng.p2p.modules.statistics.console.service.query.RecWitReportQuery;
import com.dimeng.util.io.CVSWriter;

public abstract interface RechargeWithdrawManage extends Service
{
    
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
     * <li>查询{@code T7041}表<li>
     * 查询字段列表:
     * <ol>
     * <li>{@code int}对应{@code DISTINCT(T7041.F01)}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * 
     * 
     * @return  {@code int[]}查询结果
     * @throws Throwable
     */
    public abstract int[] getStatisticedYear()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述： 获取充值提现统计数据
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
     * <li>查询{@code T7041}表 查询条件{@code T7041.F01 = year}<li>
     * <li>如果{@code T7041.F02}小于1或则{@code T7041.F02}大于12 则返回</li>
     * 查询字段列表:
     * <ol>
     * <li>{@code RecWit[T7041.F02 -1 ]}得到{@code RecWit}</li>
     * <li>{@link RecWit#month}对应{@code T7041.F02}</li>
     * <li>{@link RecWit#recharge}对应{@code T7041.F03}</li>
     * <li>{@link RecWit#withdraw}对应{@code T7041.F04}</li>
     * <li>{@link RecWit#rechargeCount}对应{@code T7041.F05}</li>
     * <li>{@link RecWit#withdrawCount}对应{@code T7041.F06}</li>
     * 
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * @param year 年份
     * @return {@link RecWit}{@code []} 查询结果
     * @throws Throwable
     */
    public abstract RecWit[] getRechargeWithdraws(int year)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述： 获取充值提现按天统计报表数据
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
     * </dl>
     * </dt>
     * 
     * 
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @return PagingResult<RecWitReport> 查询结果分页对象
     * @throws Throwable
     */
    public abstract PagingResult<RecWitReport> getRecWitReports(RecWitReportQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 获取充值提现统计页面的汇总信息
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract RecWitReportStatistics getStatistics(RecWitReportQuery query)
        throws Throwable;
    
    /**
     *
     * <dt>
     * <dl>
     * 描述： 获取充值提现统计总和
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
     * <li>查询{@code T7041}表 查询条件{@code T7041.F01 = year}<li>
     * 查询字段列表:
     * <ol>
     * <li>{@link RecWitTotal#recharge}对应{@code SUM(T7041.F03)}</li>
     * <li>{@link RecWitTotal#withdraw}对应{@code SUM(T7041.F04)}</li>
     * <li>{@link RecWitTotal#rechargeCount}对应{@code SUM(T7041.F05)}</li>
     * <li>{@link RecWitTotal#withdrawCount}对应{@code SUM(T7041.F06)}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * 
     * 
     * 
     * @param year 年份
     * @return {@link RecWitTotal}查询结果 不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract RecWitTotal getRecWitTotal(int year)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出充值提现数据统计csv文件
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code outputStream == null}则直接返回</li>
     * <li>如果{@code recWits == null}或则{@code recWits}长度小于等于0   则直接返回</li>
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
     * @param recWits 充值提现统计
     * @param recWitTotal 充值提现统计总额
     * @param outputStream 输出流
     * @param charset 字符编码
     * @throws Throwable
     */
    public abstract void export(RecWit[] recWits, RecWitTotal recWitTotal, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出充值提现数据统计的统计报表分页csv文件
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code outputStream == null}则直接返回</li>
     * <li>如果{@code recWits == null}或则{@code recWits}长度小于等于0   则直接返回</li>
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
     * @param recWitReports 充值提现按天统计
     * @param outputStream 输出流
     * @param charset 字符编码
     * @throws Throwable
     */
    public abstract void exportStatistics(RecWitReport[] recWitReports, OutputStream outputStream, String charset)
        throws Throwable;
}
