package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.AdvertisementStatus;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpsc;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord;
import com.dimeng.p2p.modules.base.console.service.entity.Advertisement;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementContent;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementType;
import com.dimeng.p2p.modules.base.console.service.query.AdvertisementRecordQuery;

/**
 * 广告管理.
 */
public abstract interface AdvertisementManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询广告列表.
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
	 * <li>查询{@code T5016}表{@code T7110},查询条件:{@code T5016.F06 = T7110.F01}</li>
	 * <li>按照{@code T5016.F01}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link AdvertisementRecord#id}对应{@code T5016.F01}</li>
	 * <li>{@link AdvertisementRecord#sortIndex}对应{@code T5016.F02}</li>
	 * <li>{@link AdvertisementRecord#title}对应{@code T5016.F03}</li>
	 * <li>{@link AdvertisementRecord#url}对应{@code T5016.F04}</li>
	 * <li>{@link AdvertisementRecord#imageCode}对应{@code T5016.F05}</li>
	 * <li>{@link AdvertisementRecord#publisherId}对应{@code T5016.F06}</li>
	 * <li>{@link AdvertisementRecord#showTime}对应{@code T5016.F07}</li>
	 * <li>{@link AdvertisementRecord#unshowTime}对应{@code T5016.F08}</li>
	 * <li>{@link AdvertisementRecord#createTime}对应{@code T5016.F09}</li>
	 * <li>{@link AdvertisementRecord#updateTime}对应{@code T5016.F10}</li>
	 * <li>{@link AdvertisementRecord#publisherName}对应{@code T7110.F04}</li>
	 * <li>{@link AdvertisementRecord#status}
	 * 如果当前系统时间{@code < T5016.F07}
	 * 	则对应{@link AdvertisementStatus#DSJ} 
	 * 如果当前系统时间{@code < T5016.F08}
	 * 	则对应{@link AdvertisementStatus#YSJ} 
	 * 	否则对应{@link AdvertisementStatus#YXJ} 
	 * </li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link AdvertisementRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<AdvertisementRecord> search(
			AdvertisementRecordQuery query, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：新增广告.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code advertisement==null} 则抛出参数异常  没有给出广告信息</li>
	 * <li>如果{@link Advertisement#getTitle()}为空   则抛出参数异常 必须指定广告标题</li>
	 * <li>如果{@link Advertisement#getTitle()}长度大于50  则抛出参数异常 广告标题必须小于50个字符</li>
	 * <li>如果{@link Advertisement#getShowTime()}为空   则抛出参数异常 必须指定上架时间</li>
	 * <li>如果{@link Advertisement#getUnshowTime()}为空  则抛出参数异常 必须指定下架时间</li>
	 * <li>如果{@link Advertisement#getImage()}为空  则抛出参数异常 必须上传广告图片</li>
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
	 * <li>上传文件，得到 文件编码{@link String}</li>
	 * <li>新增{@code T5016}表</li>
	 * <li>
	 * 
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link Advertisement#getSortIndex()}对应{@code T5016.F02}</li>
	 * <li>{@link Advertisement#getTitle()}对应{@code T5016.F03}</li>
	 * <li>{@link Advertisement#getURL()}对应{@code T5016.F04}</li>
	 * <li>文件编码{@link String} 对应{@code T5016.F05}</li>
	 * <li>当前登录ID对应{@code T5016.F06}</li>
	 * <li>{@link Advertisement#getShowTime()}对应{@code T5016.F07}</li>
	 * <li>{@link Advertisement#getUnshowTime()}对应{@code T5016.F08}</li>
	 * <li>当前系统时间对应{@code T5016.F09}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param advertisement
	 *            广告信息
	 * @return {@code int} 广告ID
	 * @throws Throwable
	 */
	public abstract int add(Advertisement advertisement) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：修改广告.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id <= 0} 则抛出参数异常  没有给出广告ID</li>
	 * <li>如果{@code advertisement==null} 则抛出参数异常  没有给出广告信息</li>
	 * <li>如果{@link Advertisement#getTitle()}为空   则抛出参数异常 必须指定广告标题</li>
	 * <li>如果{@link Advertisement#getTitle()}长度大于50  则抛出参数异常 广告标题必须小于50个字符</li>
	 * <li>如果{@link Advertisement#getShowTime()}为空   则抛出参数异常 必须指定上架时间</li>
	 * <li>如果{@link Advertisement#getUnshowTime()}为空  则抛出参数异常 必须指定下架时间</li>
	 * <li>如果{@link Advertisement#getImage()}为空  则抛出参数异常 必须上传广告图片</li>
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
	 * <li>修改{@code T5016}表,修改条件:{@code T5016.F01 = }{@code id}</li>
	 * <li>如果{@link Advertisement#getImage()}{@code == null} 则不修改{@code T5016.F05}字段
	 * 	否则修改 并且上传图片得到文件编码{@link String}
	 * </li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link Advertisement#getSortIndex()}对应{@code T5016.F02}</li>
	 * <li>{@link Advertisement#getTitle()}对应{@code T5016.F03}</li>
	 * <li>{@link Advertisement#getURL()}对应{@code T5016.F04}</li>
	 * <li>文件编码{@link String} 对应{@code T5016.F05}</li>
	 * <li>{@link Advertisement#getShowTime()}对应{@code T5016.F07}</li>
	 * <li>{@link Advertisement#getUnshowTime()}对应{@code T5016.F08}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * @param id
	 *            广告ID
	 * @param advertisement
	 *            广告信息
	 * @throws Throwable
	 */
	public abstract void update(int id, Advertisement advertisement)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：删除广告.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null } 或则 {@code ids}的长度等于0  则直接返回</li>
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
	 * <li>打开事务</li>
	 * <li>批量删除{@code T5016}表,删除条件:{@code T5016.F01 = }{@code ids}</li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * @param ids
	 *            广告ID列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询广告.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id <= 0 } 则直接返回 {@code null}</li>
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
	 * <li>查询{@link T5016}表{@link T7110}表,查询条件:{@code T5016.F06 = T7110.F01 AND T5016.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link AdvertisementRecord#id}对应{@code T5016.F01}</li>
	 * <li>{@link AdvertisementRecord#sortIndex}对应{@code T5016.F02}</li>
	 * <li>{@link AdvertisementRecord#title}对应{@code T5016.F03}</li>
	 * <li>{@link AdvertisementRecord#url}对应{@code T5016.F04}</li>
	 * <li>{@link AdvertisementRecord#imageCode}对应{@code T5016.F05}</li>
	 * <li>{@link AdvertisementRecord#publisherId}对应{@code T5016.F06}</li>
	 * <li>{@link AdvertisementRecord#showTime}对应{@code T5016.F07}</li>
	 * <li>{@link AdvertisementRecord#unshowTime}对应{@code T5016.F08}</li>
	 * <li>{@link AdvertisementRecord#createTime}对应{@code T5016.F09}</li>
	 * <li>{@link AdvertisementRecord#updateTime}对应{@code T5016.F10}</li>
	 * <li>{@link AdvertisementRecord#publisherName}对应{@code T7110.F04}</li>
	 * <li>{@link AdvertisementRecord#status}
	 * 如果当前系统时间{@code < T5016.F07}
	 * 	则对应{@link AdvertisementStatus#DSJ} 
	 * 如果当前系统时间{@code CURRENT_TIMESTAMP() < T5016.F08}
	 * 	则对应{@link AdvertisementStatus#YSJ} 
	 * 	否则对应{@link AdvertisementStatus#YXJ} 
	 * </li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * @param id 广告ID
	 * @return {@link AdvertisementRecord} 查询结果 没有则返回{@code null}
	 * @throws Throwable
	 */
	public abstract AdvertisementRecord get(int id) throws Throwable;
	
	public abstract int addTTDai(AdvertisementContent advertisement) throws Throwable;
	
	public abstract void updateTTDai(int id, AdvertisementContent advertisement) throws Throwable;
	
	public abstract AdvertisementRecord getTTDdai(int id) throws Throwable;
	
	public abstract int addForType(AdvertisementType advertisement) throws Throwable;
	
	public abstract AdvertisementRecord getForType(int id) throws Throwable;
	
	public abstract void updateForType(int id, AdvertisementType advertisement)
			throws Throwable;
	
	
	/**
	 * 查询所有视频
	 * */

	public abstract PagingResult<AdvertSpscRecord> search(Paging paging) throws Throwable;
	
	/**插入视频
	 * 
	 * */
	
	public abstract int insertSpsc(AdvertSpsc spsc) throws Throwable;
	
	/**更新视频
	 * */
	
	public abstract void updateSpsc(int id,AdvertSpsc spsc,String url) throws Throwable;
	
	/**查询单个视频
	 * */
	
	public abstract AdvertSpscRecord  searchSpsc(int id) throws Throwable;
	
	/**
	 *  删除视频
	 * */
	public abstract void  deleteSpsc(int id,String url) throws Throwable;
	
	/**
	 * 前台查询视频
	 * */
	
	public abstract AdvertSpscRecord  searchqtSpsc() throws Throwable;
	
	/**
	 * 置頂方法
	 * */
	
	public abstract void  IndexSpsc(int id) throws Throwable;
	
	/** <一句话功能简述>
     * 批量更新排序值
     * @param ids
     * @param order
     * @return
     * @throws Throwable
     */
    public abstract void updateBatchOrder(String ids,int order)throws Throwable;


}
