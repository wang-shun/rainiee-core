package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S70.entities.T7042;
import com.dimeng.p2p.S70.entities.T7043;
import com.dimeng.p2p.S70.entities.T7044;
import com.dimeng.p2p.S70.entities.T7045;
import com.dimeng.p2p.modules.statistics.console.service.entity.Profile;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeRegion;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeTimeLimit;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeType;
import com.dimeng.util.io.CVSWriter;

public abstract interface VolumeManage extends Service {
	/**
	 *
	 * <dt>
	 * <dl>
	 * 描述： 获取按月成交统计列表
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
	 * <li>查询{@link T7042}表   查询条件：{@code T7042.F01 = year}<li>
	 * <li>按照{@code T7042.F02}字段升序排序</li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeEntity#month}对应{@code T7042.F02}</li>
	 * <li>{@link VolumeEntity#amount}对应{@code T7042.F03}</li>
	 * <li>{@link VolumeEntity#count}对应{@code T7042.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param year 年份
	 * @return {@link VolumeEntity}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract VolumeEntity[] getVolumeEntities(int year) throws Throwable;

	/**
	 * 获取平台12个月之内的成交数据
	 * @return
	 * @throws Throwable
	 */
	public abstract VolumeEntity[] getAllVolumeEntities() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 获取前一年按月统计数据
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
	 * <li>查询{@link T7042}表   查询条件：{@code T7042.F01 = year-1}<li>
	 * <li>按照{@code T7042.F02}字段升序排序</li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeEntity#month}对应{@code T7042.F02}</li>
	 * <li>{@link VolumeEntity#amount}对应{@code T7042.F03}</li>
	 * <li>{@link VolumeEntity#count}对应{@code T7042.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param year 年份
	 * @return {@link VolumeEntity}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract VolumeEntity[] getLastYearVolumeEntities(int year)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：获取概况
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code year <= 0} 则不执行sql语句</li>
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
	 * <li>查询{@link T7042}表   查询条件：{@code T7042.F01 = year}<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Profile#totalAmount}对应{@code SUM(T7042.F03)}</li>
	 * <li>{@link Profile#totalCount}对应{@code SUM(T7042.F04)}</li>
	 * </ol>
	 * <li>查询{@link T7042}表   查询条件：{@code T7042.F01 = year -1}<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Profile#amountRate}对应{@link Profile#totalAmount}除以{@code SUM(T7042.F03)}</li>
	 * <li>{@link Profile#countRate}对应{@link Profile#totalCount}除以{@code SUM(T7042.F03)}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param year 年份
	 * @return {@link Profile} 查询结果
	 * @throws Throwable
	 */
	public abstract Profile getProfile(int year) throws Throwable;

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
	 * <li>查询{@link T7042}表<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code int}对应{@code DISTINCT(T7042.F01)}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @return {@code int[]}查询结果
	 * @throws Throwable
	 */
	public abstract int[] getStatisticedYear() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 贷款数据统计-按产品类型
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
	 * <li>查询{@link T7043}表   查询条件：{@code T7043.F01 = year}<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeType#amount}对应{@code T7043.F03}</li>
	 * <li>{@link VolumeType#count}对应{@code T7043.F04}</li>
	 * <li>{@link VolumeType#type}对应{@code T7043.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * @param year 年份
	 * @return {@link VolumeType}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract VolumeType[] getVolumeTypes(int year) throws Throwable;

	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 贷款数据统计-按期限
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
	 * <li>查询{@link T7044}表   查询条件：{@code  WHERE T7044.F01=year AND T7044.F02>=}期限{@code AND T7044.F02<=}期限<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeTimeLimit#amount}对应{@code T7044.F03}</li>
	 * <li>{@link VolumeTimeLimit#count}对应{@code T7044.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * 
	 * 
	 * @param year 年份
	 * @return {@link VolumeTimeLimit}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract VolumeTimeLimit[] getVolumeTimeLimits(int year)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 贷款数据统计-按地域
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
	 * <li>查询{@link T7045}表   查询条件：{@code T7045.F01 = year}<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeRegion#amount}对应{@code T7045.F03}</li>
	 * <li>{@link VolumeRegion#count}对应{@code T7045.F04}</li>
	 * <li>{@link VolumeRegion#regionId}对应{@code T7045.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * <li>查询{@link T5019}表   查询条件：{@code T5019.F01 = }{@link VolumeRegion#regionId}<li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link VolumeRegion#province}对应{@code T5019.F06}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param year 年份
	 * @return {@link VolumeRegion}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract VolumeRegion[] getVolumeRegions(int year) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：导出成交数据
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
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
	 * @param outputStream 输出流
	 * @param year 年份
	 * @param charset 字符编码
	 * @throws Throwable
	 */
	public void export(OutputStream outputStream, int year, String charset)
			throws Throwable;
}