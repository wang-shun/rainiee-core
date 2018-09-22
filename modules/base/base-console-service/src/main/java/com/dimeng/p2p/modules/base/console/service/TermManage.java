package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011_2;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S50.entities.T5017_1;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.console.service.entity.TermRecord;
import com.dimeng.p2p.modules.base.console.service.query.TermQuery;

/**
 * 协议条款管理.
 * 
 */
public interface TermManage extends Service
{
    /**
     * <dt>
     * <dl>
     * 描述：分页查询协议条款列表.
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
     * <li>查询{@link T5017}表{@link T7110}表</li>
     * <li>查询条件:
     * <ol>
     * <li>{@code T5017.F04 = T7110.F01}</li>
     * <li>如果{@code query != null} 则
     * <ol>
     * <li>如果{@link TermQuery#getType()}不为空 则{@code T5017.F01 = }
     * {@link TermQuery#getType()}</li>
     * <li>如果{@link TermQuery#getPublisherName()}不为空 则{@code T7110.F04 LIKE }
     * {@link TermQuery#getPublisherName()}</li>
     * <li>如果{@link TermQuery#getCreateTimeStart()}不为空 则
     * {@code DATE(T5017.F05) >= }{@link TermQuery#getCreateTimeStart()}</li>
     * <li>如果{@link TermQuery#getCreateTimeEnd()}不为空 则{@code DATE(T5017.F05) <= }
     * {@link TermQuery#getCreateTimeEnd()}</li>
     * <li>如果{@link TermQuery#getUpdateTimeStart()}不为空 则
     * {@code DATE(T5017.F06) >= }{@link TermQuery#getUpdateTimeStart()}</li>
     * <li>如果{@link TermQuery#getUpdateTimeEnd()}不为空 则{@code  DATE(T5017.F06) <= }
     * {@link TermQuery#getUpdateTimeEnd()}</li>
     * </ol>
     * </li>
     * </ol>
     * </li>
     * <li>按照{@code T5017.F06}字段降序排序</li>
     * <li>
     * 查询字段列表:
     * <ol>
     * <li>{@link TermRecord#type}对应{@code T5017.F01}</li>
     * <li>{@link TermRecord#viewTimes}对应{@code T5017.F02}</li>
     * <li>{@link TermRecord#content}对应{@code T5017.F03}</li>
     * <li>{@link TermRecord#publisherId}对应{@code T5017.F04}</li>
     * <li>{@link TermRecord#createTime}对应{@code T5017.F05}</li>
     * <li>{@link TermRecord#updateTime}对应{@code T5017.F06}</li>
     * <li>{@link TermRecord#publisherName}对应{@code T7110.F04}</li>
     * 
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param query
     *            查询条件
     * @param paging
     *            分页参数
     * @return {@link PagingResult}{@code <}{@link TermRecord}{@code >}
     *         分页查询结果,没有结果则返回{@code null}
     * @throws Throwable
     */
    public abstract PagingResult<TermRecord> search(TermQuery query, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据协议条款类型查询协议条款.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code type == null} 则直接返回 {@link null}</li>
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
     * <li>查询{@link T5017}表{@link T7110}表表 查询条件:
     * {@code T5017.F04 = T7110.F01 AND T5017.F01 = type}</li>
     * <li>
     * 查询字段列表:
     * <ol>
     * <li>{@link TermRecord#type}对应{@code T5017.F01}</li>
     * <li>{@link TermRecord#viewTimes}对应{@code T5017.F02}</li>
     * <li>{@link TermRecord#content}对应{@code T5017.F03}</li>
     * <li>{@link TermRecord#publisherId}对应{@code T5017.F04}</li>
     * <li>{@link TermRecord#createTime}对应{@code T5017.F05}</li>
     * <li>{@link TermRecord#updateTime}对应{@code T5017.F06}</li>
     * <li>{@link TermRecord#publisherName}对应{@code T7110.F04}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * 
     * 
     * @param type
     *            协议条款类型
     * @return {@link TermRecord} 查询结果 不存在则返回 {@code null}
     * @throws Throwable
     */
    public abstract TermRecord get(TermType type)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据协议条款类型修改协议条款.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code content == null}为空 则抛出参数异常 协议内容不能为空</li>
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
     * <li>修改{@link T5017}表 修改条件：{@code T5017.F01 = type}
     * <li>
     * 修改字段列表:
     * <ol>
     * <li>{@code content}对应{@code T5017.F03}</li>
     * <li>当前系统时间 对应{@code T5017.F06}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param type
     *            协议条款类型
     * @param content
     *            协议条款内容
     * @throws Throwable
     */
    public abstract void update(TermType type, String content)
        throws Throwable;

    /**
     * 修改协议条款状态
     * @param type
     * @param content
     * @throws Throwable
     */
    public abstract void updateStatus(TermType type, String content)
            throws Throwable;

    /**
     * <dt>
     * <dl>
     * 描述：根据协议类型添加协议内容.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果{@code type == null} 则抛出参数异常 协议类型不能为空</li>
     * <li>如果{@code content == null}为空 则抛出参数异常 协议内容不能为空</li>
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
     * 
     * <li>
     * 如果{@code type}在数据库中存在 ，则修改{@link T5017}表
     * <li>修改字段 ：{@code content}对应{@code T5017.F03}</li>
     * 否则 新增{@link T5017}表
     * <li>
     * 新增字段列表:
     * <ol>
     * <li>{@code type}对应{@code T5017.F01}</li>
     * <li>{@code content}对应{@code T5017.F03}</li>
     * <li>当前系统登录ID 对应{@code T5017.F04}</li>
     * <li>当前系统时间 对应{@code T5017.F05}</li>
     * </ol>
     * </li>
     * </dl>
     * </dt>
     * 
     * @param type
     *            协议条款类型
     * @param content
     *            协议条款内容
     * @throws Throwable
     */
    public abstract void add(TermType type, String content)
        throws Throwable;

    
    /**
    * 删除协议条款
    * <功能详细描述>
    * @param ids
    * @throws Throwable
    */
    public abstract void delete(String... ids)
        throws Throwable;
}
