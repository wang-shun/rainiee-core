package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.modules.base.front.service.entity.QuestionRecord;
import com.dimeng.p2p.modules.base.front.service.entity.QuestionTypeRecord;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_3_EXT;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_EXT;
import com.dimeng.p2p.repeater.donation.entity.Article;

/**
 * 文章管理
 * 
 */
public abstract interface ArticleManage extends Service
{
    
    /**
     * <dt>
     * <dl>
     * 描述：按文章类型分页查询文章列表.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>如果 {@code articleType == null} 则直接返回空{@link PagingResult}</li>
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
     * <li>分页查询{@link T5011}表</li>
     * <li>查询条件: {@link T5011#F05} {@code =} {@link ArticlePublishStatus#YFB}
     * {@code AND} {@link T5011#F02} {@code = articleType}</li>
     * <li>按照{@link T5011#F04}字段降序排序</li>
     * <li>
     * 查询字段列表
     * <ol>
     * <li>{@link T5011#F01}</li>
     * <li>{@link T5011#F03}</li>
     * <li>{@link T5011#F06}</li>
     * <li>{@link T5011#F07}</li>
     * <li>{@link T5011#F08}</li>
     * <li>{@link T5011#F09}</li>
     * <li>{@link T5011#F11}</li>
     * <li>{@link T5011#F12}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>{@link Article#id}对应{@link T5011#F01}</li>
     * <li>{@link Article#viewTimes}对应{@link T5011#F03}</li>
     * <li>{@link Article#title}对应{@link T5011#F06}</li>
     * <li>{@link Article#source}对应{@link T5011#F07}</li>
     * <li>{@link Article#summary}对应{@link T5011#F08}</li>
     * <li>{@link Article#imageCode}对应{@link T5011#F09}</li>
     * <li>{@link Article#createtime}对应{@link T5011#F11}</li>
     * <li>{@link Article#publishTime}对应{@link T5011#F12}</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param articleType
     *            文章类型
     * @param paging
     *            分页查询参数
     * @return {@link PagingResult}{@code <}{@link Article}{@code >} 分页查询结果
     * @throws Throwable
     */
    public abstract PagingResult<T5011> search(T5011_F02 articleType, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据文章ID查询已发布文章信息.
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
     * <li>如果 {@code id <= 0} 则直接返回 {@code null}</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@link T5011}表</li>
     * <li>查询条件: {@link T5011.F05} {@code =} {@link ArticlePublishStatus#YFB}
     * {@code AND} {@link T5011.F01} {@code  = id}</li>
     * <li>
     * 查询字段列表
     * <ol>
     * <li>{@link T5011#F01}</li>
     * <li>{@link T5011#F03}</li>
     * <li>{@link T5011#F06}</li>
     * <li>{@link T5011#F07}</li>
     * <li>{@link T5011#F08}</li>
     * <li>{@link T5011#F09}</li>
     * <li>{@link T5011#F11}</li>
     * <li>{@link T5011#F12}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>{@link Article#id}对应{@link T5011#F01}</li>
     * <li>{@link Article#viewTimes}对应{@link T5011#F03}</li>
     * <li>{@link Article#title}对应{@link T5011#F06}</li>
     * <li>{@link Article#source}对应{@link T5011#F07}</li>
     * <li>{@link Article#summary}对应{@link T5011#F08}</li>
     * <li>{@link Article#imageCode}对应{@link T5011#F09}</li>
     * <li>{@link Article#createtime}对应{@link T5011#F11}</li>
     * <li>{@link Article#publishTime}对应{@link T5011#F12}</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param id
     *            文章ID
     * @return {@link T5011} 文章信息,如果不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract T5011 get(int id)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据文章ID和类型查询已发布文章信息.
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
     * <li>如果 {@code id <= 0} 则直接返回 {@code null}</li>
     * <li>如果 {@code articleType == null} 则直接返回 {@code null}</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@link T5011}表</li>
     * <li>查询条件: {@link T5011.F05} {@code =} {@link ArticlePublishStatus#YFB}
     * {@code AND} {@link T5011.F01} {@code = id} {@code AND} {@link T5011.F02}
     * {@code = articleType}</li>
     * <li>
     * 查询字段列表
     * <ol>
     * <li>{@link T5011#F01}</li>
     * <li>{@link T5011#F03}</li>
     * <li>{@link T5011#F06}</li>
     * <li>{@link T5011#F07}</li>
     * <li>{@link T5011#F08}</li>
     * <li>{@link T5011#F09}</li>
     * <li>{@link T5011#F11}</li>
     * <li>{@link T5011#F12}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>{@link Article#id}对应{@link T5011#F01}</li>
     * <li>{@link Article#viewTimes}对应{@link T5011#F03}</li>
     * <li>{@link Article#title}对应{@link T5011#F06}</li>
     * <li>{@link Article#source}对应{@link T5011#F07}</li>
     * <li>{@link Article#summary}对应{@link T5011#F08}</li>
     * <li>{@link Article#imageCode}对应{@link T5011#F09}</li>
     * <li>{@link Article#createtime}对应{@link T5011#F11}</li>
     * <li>{@link Article#publishTime}对应{@link T5011#F12}</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param id
     *            文章ID
     * @param articleType
     *            文章类型
     * @return {@link T5011} 文章信息,如果不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract T5011 get(int id, T5011_F02 articleType)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据文章类型查询已发布的该类型第一篇文章信息.
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
     * <li>如果 {@code articleType == null} 则直接返回 {@code null}</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@code T5011}表,查询条件:{@code T5011.F05 = }
     * {@link ArticlePublishStatus#YFB} {@code AND T5011.F02 = articleType}</li>
     * <li>按照{@code T5011.F04}字段降序排序,取第一条记录</li>
     * <li>
     * 查询字段列表
     * <ol>
     * <li>{@code T5011.F01}</li>
     * <li>{@code T5011.F03}</li>
     * <li>{@code T5011.F06}</li>
     * <li>{@code T5011.F07}</li>
     * <li>{@code T5011.F08}</li>
     * <li>{@code T5011.F09}</li>
     * <li>{@code T5011.F11}</li>
     * <li>{@code T5011.F12}</li>
     * </ol>
     * </li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>{@link Article#id}对应{@code T5011.F01}</li>
     * <li>{@link Article#viewTimes}对应{@code T5011.F03}</li>
     * <li>{@link Article#title}对应{@code T5011.F06}</li>
     * <li>{@link Article#source}对应{@code T5011.F07}</li>
     * <li>{@link Article#summary}对应{@code T5011.F08}</li>
     * <li>{@link Article#imageCode}对应{@code T5011.F09}</li>
     * <li>{@link Article#createtime}对应{@code T5011.F11}</li>
     * <li>{@link Article#publishTime}对应{@code T5011.F12}</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param articleType
     *            文章类型
     * @return {@link T5011} 文章信息,如果不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract T5011 get(T5011_F02 articleType)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据ID,获取已发布文章的内容.
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
     * <li>如果{@code id<=0}则直接返回{@code null}</li>
     * <li>查询 {@code T5011}, 如果{@code T5011.F01 = id AND T5011.F05 = }
     * {@link ArticlePublishStatus#YFB}不存在记录,则返回{@code null}</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@code T5011_1.F02},查询条件:{@code T5011_1.F01 = id}</li>
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
     *            文章ID
     * @return {@link String} 文章内容,如果不存在记录则返回{@code null}
     * @throws Throwable
     */
    public abstract String getContent(int id)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据文章类型,获取已发布第一篇文章的内容.
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
     * <li>如果{@code articleType==null}则直接返回{@code null}</li>
     * <li>查询 {@code T5011}, 如果{@code T5011.F02 = articleType AND T5011.F05 = }
     * {@link ArticlePublishStatus#YFB}不存在记录,则返回{@code null}</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>查询{@code T5011_1.F02},查询条件:{@code T5011_1.F01 = id}</li>
     * <li>按照{@code T5011.F04}字段降序排序,取第一条记录</li>
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
     * @param articleType
     *            文章类型
     * @return {@link String} 文章内容,如果不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract String getContent(T5011_F02 articleType)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：累加已发布文章点击次数.
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
     * <li>如果{@code id<=0}则直接返回</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>修改{@code T5011.F03=F03+1},修改条件:{@code T5011.F01 = id AND T5011.F05 = }
     * {@link ArticlePublishStatus#YFB}</li>
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
     *            文章ID
     * @throws Throwable
     */
    public abstract void view(int id)
        throws Throwable;
    
    /**
     * 得到所有的问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract QuestionTypeRecord[] getQuestionTypes(T5011_F02 articleType)
        throws Throwable;
    
    /**
     * 得到所有的问题根据问题类型ID
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract QuestionRecord[] getQuestions(T5011_F02 articleType, int questionID)
        throws Throwable;
    
    /** <一句话功能简述>
    * 根据父类id和状态查询文章类别
    * @param parentId
    * @param status
    * @return
    * @throws Throwable
    */
    public abstract T5010[] getArticleCategoryAll(int parentId, T5010_F04 status)
        throws Throwable;
    
    /** <一句话功能简述>
     * 根据类别编码查询名称
     * @param code
     * @return
     * @throws Throwable
     */
    public abstract String getCategoryNameByCode(String code)
        throws Throwable;
    
    /** <一句话功能简述>
     * 根据类别编码查询状态
     * @param code
     * @return
     * @throws Throwable
     */
    public abstract T5010_F04 getCategoryStatusByCode(String code)
        throws Throwable;
    
    /**
     * 获取T5011
     * @param articleType
     * @return
     * @throws Throwable
     */
    public abstract T5011 get(String articleType)
        throws Throwable;
    
    /**
     * 获取文章
     * @param articleType
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T5011_EXT> search_EXT(String articleType, Paging paging, final FileStore fileStore)
        throws Throwable;
    
    /**
     * 得到所有的问题类型
     * @param articleType
     * @param articleType
     * @return
     * @throws Throwable
     */
    public abstract QuestionTypeRecord[] getQuestionTypes(String articleType, FileStore fileStore)
        throws Throwable;
    
    /**
     * <获取信息披露的最近年份>
     * <功能详细描述>
     * @return
     */
    public abstract String[] getYear()
        throws Throwable;
    
    /**
     * <获取信息披露列表>
     * <功能详细描述>
     * @param year
     * @param paging
     * @param fileStore
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T5011_3_EXT> searchInformation(String articleType, String year, Paging paging,
        final FileStore fileStore)
        throws Throwable;
    
    /**
     * 获取信息披露附件
     * @param articleType
     * @return
     * @throws Throwable
     */
    public abstract T5011_3 getFileInfo(String id)
        throws Throwable;
    
}
