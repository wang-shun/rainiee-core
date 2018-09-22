package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.entities.T5011_1;
import com.dimeng.p2p.S50.entities.T5011_2;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.common.enums.ArticleType;
import com.dimeng.p2p.modules.base.console.service.entity.Article;
import com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord;
import com.dimeng.p2p.modules.base.console.service.entity.OperateReport;
import com.dimeng.p2p.modules.base.console.service.entity.Question;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionRecord;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord;
import com.dimeng.p2p.modules.base.console.service.query.ArticleQuery;
import com.dimeng.p2p.modules.base.console.service.query.OperateReportQuery;
import com.dimeng.p2p.modules.base.console.service.query.QuestionQuery;

/**
 * 文章管理.
 * 
 */
public abstract interface ArticleManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询文章列表.
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
	 * <li>查询{@link T5011}{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5011.F10 = T7110.F01}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link ArticleQuery#getType()}不为空  则{@code T5011.F02 = }{@link ArticleQuery#getType()}的name()</li>
	 * <li>如果{@link ArticleQuery#getPublishStatus()}不为空  则{@code T5011.F05 = }{@link ArticleQuery#getPublishStatus()}的name()</li>
	 * <li>如果{@link ArticleQuery#getTitle()}不为空  则{@code T5011.F06 LIKE }{@link ArticleQuery#getTitle()}</li>
	 * <li>如果{@link ArticleQuery#getCreateTimeStart()}不为空  则{@code DATE(T5011.F11) >= }{@link ArticleQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link ArticleQuery#getCreateTimeEnd()}不为空  则{@code DATE(T5011.F11) <= }{@link ArticleQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link ArticleQuery#getPublishTimeStart()}不为空  则{@code DATE(T5011.F12) >=  }{@link ArticleQuery#getPublishTimeStart()}</li>
	 * <li>如果{@link ArticleQuery#getPublishTimeEnd()}不为空  则{@code DATE(T5011.F12) <=  }{@link ArticleQuery#getPublishTimeEnd()}</li>
	 * <li>如果{@link ArticleQuery#getSource()}不为空  则{@code T5011.F07 LIKE }{@link ArticleQuery#getSource()}</li>
	 * <li>如果{@link ArticleQuery#getSummary()}不为空  则{@code T5011.F08 LIKE }{@link ArticleQuery#getSummary()}</li>
	 * <li>如果{@link ArticleQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE }{@link ArticleQuery#getPublisherName()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5016.F11}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ArticleRecord#id}对应{@code T5011.F01}</li>
	 * <li>{@link ArticleRecord#articleType}对应{@code T5011.F02}</li>
	 * <li>{@link ArticleRecord#viewTimes}对应{@code T5011.F03}</li>
	 * <li>{@link ArticleRecord#sortIndex}对应{@code T5011.F04}</li>
	 * <li>{@link ArticleRecord#publishStatus}对应{@code T5011.F05}</li>
	 * <li>{@link ArticleRecord#title}对应{@code T5011.F06}</li>
	 * <li>{@link ArticleRecord#source}对应{@code T5011.F07}</li>
	 * <li>{@link ArticleRecord#summary}对应{@code T5011.F08}</li>
	 * <li>{@link ArticleRecord#imageCode}对应{@code T5011.F09}</li>
	 * <li>{@link ArticleRecord#publisherId}对应{@code T5011.F10}</li>
	 * <li>{@link ArticleRecord#createtime}对应{@code T5011.F11}</li>
	 * <li>{@link ArticleRecord#publishTime}对应{@code T5011.F12}</li>
	 * <li>{@link ArticleRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link ArticleRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<ArticleRecord> search(ArticleQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID查询文章.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>{@code id <= 0} 则返回 {@code null}</li>
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
	 * <li>查询{@link T5011}{@link T7110}表,查询条件:{@code T5011.F10 = T7110.F01 AND T5011.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ArticleRecord#id}对应{@code T5011.F01}</li>
	 * <li>{@link ArticleRecord#articleType}对应{@code T5011.F02}</li>
	 * <li>{@link ArticleRecord#viewTimes}对应{@code T5011.F03}</li>
	 * <li>{@link ArticleRecord#sortIndex}对应{@code T5011.F04}</li>
	 * <li>{@link ArticleRecord#publishStatus}对应{@code T5011.F05}</li>
	 * <li>{@link ArticleRecord#title}对应{@code T5011.F06}</li>
	 * <li>{@link ArticleRecord#source}对应{@code T5011.F07}</li>
	 * <li>{@link ArticleRecord#summary}对应{@code T5011.F08}</li>
	 * <li>{@link ArticleRecord#imageCode}对应{@code T5011.F09}</li>
	 * <li>{@link ArticleRecord#publisherId}对应{@code T5011.F10}</li>
	 * <li>{@link ArticleRecord#createtime}对应{@code T5011.F11}</li>
	 * <li>{@link ArticleRecord#publishTime}对应{@code T5011.F12}</li>
	 * <li>{@link ArticleRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            文章ID
	 * @return {@link ArticleRecord} 查询结果 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract ArticleRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取文章内容.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>{@code id <= 0} 则返回 {@code null}</li>
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
	 * <li>查询{@code T5011_1}表,查询条件:{@code T5011_1.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>返回结果{@link String} 对应{@code T5011_1.F02}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * 
	 * @param id
	 *            文章ID
	 * @return {@link String} 文章内容 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract String getContent(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：设置文章为置顶状态.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null }或则 {@code ids}的长度为0  则直接返回</li>
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
	 * <li>批量修改{@code T5011}表,修改条件:{@code T5011.F01 = ids }</li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>如果{@code top}为{@code true}时  则{@link Integer#MAX_VALUE}对应 {@code T5011.F04}否则是0对应</li>
	 * <li>当前系统时间 对应 {@code T5011.F12}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * @param top
	 *            置顶状态,为{@code true}表示置顶
	 * @param ids
	 *            文章ID列表,为小于等于零则不处理
	 * @throws Throwable
	 */
	public abstract void setTop(boolean top, int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：删除文章.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null} 或则 {@code ids}的长度等于0  则直接返回</li>
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
	 * <li>批量删除{@code T5011_1}表,删除条件:{@code T5011_1.F01 = ids }</li>
	 * <li>批量删除{@code T5011_2}表,删除条件:{@code T5011_2.F01 = ids }</li>
	 * <li>批量删除{@code T5011}表,删除条件:{@code T5011.F01 = ids }</li>
	 * </ol>
	 * </dl>
	 * 
	 * </dt>
	 * 
	 * 
	 * @param ids
	 *            文章ID列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID修改文章信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code articleType == null}或则 {@code id<= 0 } 则直接返回</li>
	 * <li>如果{@link Article#getTitle()}为空 则抛出参数异常  标题不能为空</li>
	 * <li>如果{@link Article#getTitle()}长度大于30  则抛出参数异常  标题不能超过30个字符</li>
	 * <li>如果{@link Article#getSource()}为不为空 并且{@link Article#getSource()}长度大于50   则抛出参数异常  文章来源不能超过50个字符</li>
	 * <li>如果{@link Article#getContent()}为空 则抛出参数异常  文章内容不能为空</li>
	 * <li>如果{@link Article#getImage()}{@code != null} 则上传文件得到{@link String}文件编码</li>
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
	 * <li>查询{@link T5011}表,查询条件:{@code T5011.F01 = id }锁表  如果不存在直接抛逻辑异常  文章不存在</li>
	 * <li>修改{@link T5011}表,修改条件：{@code T5011.F01 = id } </li>
	 * <li>如果{@link String}文件编码 为空 {@link T5011.F09}字段不修改  否则修改</li>
	 * <li>
	 * 修改字段列表
	 * <ol>
	 * <li>{@link Article#getSortIndex() } 对应{@code T5011.F04}</li>
	 * <li>{@link Article#getTitle()  } 对应{@code T5011.F06}</li>
	 * <li>{@link Article#getSource()  } 对应{@code T5011.F07}</li>
	 * <li>{@link Article#getSummary()  } 对应{@code T5011.F08}</li>
	 * <li>{@link String}文件编码 对应{@code T5011.F09}</li>
	 * <li>当前系统时间 对应{@code T5011.F11}</li>
	 * <li>{@link Article#publishTime() } 对应{@code T5011.F12}</li>
	 * <li>{@code id  } 对应{@code T5011_1.F02}</li>
	 * </ol>
	 * </li>
	 * <li>修改{@link T5011_1}表，修改条件：{@code T5011_1.F01 = id }</li>
	 * <li>修改字段  
	 * <ol>
	 * <li>  {@link Article#getContent()} 对应  {@link T5011_1.F02}</li>
	 * </ol>
	 * </li>
	 * <li>如果 {@link Article#getPublishStatus() }{@code != null } 则设置文章为发布状态</li>
	 * 
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            文章ID
	 * @param article
	 *            文章信息
	 * @throws Throwable
	 */
	public abstract void update(int id, Article article) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 新增文章.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code articleType == null} 则抛出参数异常  没有指定文章类型</li>
	 * <li>如果{@link Article#getTitle()}为空 则抛出参数异常  标题不能为空</li>
	 * <li>如果{@link Article#getTitle()}长度大于30  则抛出参数异常  标题不能超过30个字符</li>
	 * <li>如果{@link Article#getSource()}为不为空 并且{@link Article#getSource()}长度大于50   则抛出参数异常  文章来源不能超过50个字符</li>
	 * <li>如果{@link Article#getPublishStatus()}{@code == null}  则给{@link Article#getPublishStatus() }赋值{@link ArticlePublishStatus#WFB }</li>
	 * <li>如果{@link Article#getContent()}为空 则抛出参数异常  文章内容不能为空</li>
	 * <li>如果{@link Article#getImage()}{@code != null} 则上传文件 得到 {@link String}文件编码</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>如果{@link Article#getTitle()}为空 则抛出参数异常  标题不能为空</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>打开事务</li>
	 * <li>新增{@link T5011}表  得到一个新增文章id</li>
	 * <li>
	 * 新增字段
	 * <ol>
	 * <li>{@link ArticleType#name()}对应{@code T5011.F02}</li>
	 * <li>0对应{@code T5011.F03}</li>
	 * <li>{@link Article#getSortIndex()}对应{@code T5011.F04}</li>
	 * <li>数据校验掉的{@link Article#getPublishStatus()}的name()属性对应{@code T5011.F05}</li>
	 * <li>{@link Article#getTitle() }对应{@code T5011.F06}</li>
	 * <li>{@link Article#getSource() }对应{@code T5011.F07}</li>
	 * <li>{@link Article#getSummary() }对应{@code T5011.F08}</li>
	 * <li>{@link String}文件编码 对应{@code T5011.F09}</li>
	 * <li>当前登录的账户ID 对应{@code T5011.F10}</li>
	 * <li>当前系统登录时间 对应{@code T5011.F11}</li>
	 * <li>{@link Article#publishTime() }对应{@code T5011.F12}</li>
	 * </ol>
	 * </li>
	 * <li>新增{@link T5011_1}表</li>
	 * <li>
	 * 新增字段
	 * <ol>
	 * <li>新增{@link T5011}表得到一个文章id对应{@code T5011_1.F01}</li>
	 * <li>{@link Article#getContent()}对应{@code T5011_1.F02}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param articleType
	 *            文章类型
	 * @param article
	 *            文章信息
	 * @return {@code int} 文章ID
	 * @throws Throwable
	 */
	public abstract int add(T5011_F02 articleType, Article article)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 获取文章附件编码列表.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id <= 0}   则直接返回{@code null}</li>
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
	 * <li>查询{@link T5011_2}表,查询条件:{@code T5011_2.F01 = id }</li>
	 * <ol> 查询字段</ol>
	 * <li>{@link T5011_2.F02} 对应返回{@link String[]}</li>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id
	 *            文章ID
	 * @return {@link String}{@code []} 文章附件编码列表
	 * @throws Throwable
	 */
	public abstract String[] getAttachments(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 文章添加附件.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code attachmentCodes == null} 或则 {@code id <= 0} 或则 {@code attachmentCodes.length == 0}  则直接返回</li>
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
	 * <li>查询{@link T5011}表,查询条件:{@code T5011.F01 = id } 不存在直接抛逻辑异常 文章不存在</li>
	 * <li>{@code attachments}转成{@link String[]}</li>
	 * <li>如果{@link String[]}{@code != null} 循环新增{@link T5011_2}表</li>
	 * <ol> 新增字段</ol>
	 * <li>{@link T5011_2.F01} 对应{@code  id}</li>
	 * <li>{@link T5011_2.F02} 对应{@link String[]}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            文章ID
	 * @param attachments
	 *            附件列表
	 * @throws Throwable
	 */

	public abstract void addAttachments(int id, UploadFile... attachments)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 删除文章附件.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code attachmentCodes == null} 或则 {@code id <= 0} 或则 {@code attachmentCodes.length == 0}  则直接返回 </li>
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
	 * <li>查询{@link T5011}表,查询条件:{@code T5011.F01 = id } 不存在直接抛逻辑异常 文章不存在</li>
	 * <li>删除{@link T5011_2}表，删除条件：{@code T5011_2.F01 = id AND T5011_2.F02 = attachmentCode }</li>
	 * </ol>
	 * </dl>
	 * 
	 
	 * </dt>
	 * 
	 * 
	 * @param id
	 *            文章ID
	 * @param attachmentCodes
	 *            附件编码列表
	 * @throws Throwable
	 */
	public abstract void deleteAttachments(int id, String... attachmentCodes)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 根据ID获取已发布文章信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code articleType == null} 或则 {@code id <= 0} 则返回 {@code null}</li>
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
	 * <li>查询{@link T5011}{@link T7110}表,查询条件:{@code T5011.F10 = T7110.F01 WHERE T5011.F01 = id AND T5011.F02 = articleType}</li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5011.F01}</li>
	 * <li>{@code T5011.F02}</li>
	 * <li>{@code T5011.F03}</li>
	 * <li>{@code T5011.F04}</li>
	 * <li>{@code T5011.F05}</li>
	 * <li>{@code T5011.F06}</li>
	 * <li>{@code T5011.F07}</li>
	 * <li>{@code T5011.F08}</li>
	 * <li>{@code T5011.F09}</li>
	 * <li>{@code T5011.F10}</li>
	 * <li>{@code T5011.F11}</li>
	 * <li>{@code T5011.F12}</li>
	 * <li>{@code T7110.F13}</li>
	 * </ol>
	 * </ol>
	 * </dl>
	 * 
	 * 
	 * <dl>
	 * 查询结果说明：
	 * <ol>
	 * <li>{@link ArticleRecord#id}对应{@code T5011.F01}</li>
	 * <li>{@link ArticleRecord#articleType}对应{@code T5011.F02}</li>
	 * <li>{@link ArticleRecord#viewTimes}对应{@code T5011.F03}</li>
	 * <li>{@link ArticleRecord#sortIndex}对应{@code T5011.F04}</li>
	 * <li>{@link ArticleRecord#publishStatus}对应{@code T5011.F05}</li>
	 * <li>{@link ArticleRecord#title}对应{@code T5011.F06}</li>
	 * <li>{@link ArticleRecord#source}对应{@code T5011.F07}</li>
	 * <li>{@link ArticleRecord#summary}对应{@code T5011.F08}</li>
	 * <li>{@link ArticleRecord#imageCode}对应{@code T5011.F09}</li>
	 * <li>{@link ArticleRecord#publisherId}对应{@code T5011.F10}</li>
	 * <li>{@link ArticleRecord#createtime}对应{@code T5011.F11}</li>
	 * <li>{@link ArticleRecord#publishTime}对应{@code T5011.F12}</li>
	 * <li>{@link ArticleRecord#publisherName}对应{@code T7110.F13}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param id
	 *            已发布文章ID
	 * @param articleType
	 *            文章类型
	 * @return {@link ArticleRecord} 查询结果 不存在则返回{@code null}
	 * @throws Throwable
	 */

	public abstract ArticleRecord get(int id, T5011_F02 articleType)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 获取指定类型第一篇文章.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code articleType == null} 则返回 {@code null}</li>
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
	 * <li>查询{@code T5011,T7110}表,查询条件:{@code T5011.F10 = T7110.F01 WHERE T5011.F02 = articleType LIMIT 1}</li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5011.F01}</li>
	 * <li>{@code T5011.F02}</li>
	 * <li>{@code T5011.F03}</li>
	 * <li>{@code T5011.F04}</li>
	 * <li>{@code T5011.F05}</li>
	 * <li>{@code T5011.F06}</li>
	 * <li>{@code T5011.F07}</li>
	 * <li>{@code T5011.F08}</li>
	 * <li>{@code T5011.F09}</li>
	 * <li>{@code T5011.F10}</li>
	 * <li>{@code T5011.F11}</li>
	 * <li>{@code T5011.F12}</li>
	 * <li>{@code T7110.F13}</li>
	 * </ol>
	 * </ol>
	 * </dl>
	 * 
	 * 
	 * <dl>
	 * 查询结果说明：
	 * <ol>
	 * <li>{@link ArticleRecord#id}对应{@code T5011.F01}</li>
	 * <li>{@link ArticleRecord#articleType}对应{@code T5011.F02}</li>
	 * <li>{@link ArticleRecord#viewTimes}对应{@code T5011.F03}</li>
	 * <li>{@link ArticleRecord#sortIndex}对应{@code T5011.F04}</li>
	 * <li>{@link ArticleRecord#publishStatus}对应{@code T5011.F05}</li>
	 * <li>{@link ArticleRecord#title}对应{@code T5011.F06}</li>
	 * <li>{@link ArticleRecord#source}对应{@code T5011.F07}</li>
	 * <li>{@link ArticleRecord#summary}对应{@code T5011.F08}</li>
	 * <li>{@link ArticleRecord#imageCode}对应{@code T5011.F09}</li>
	 * <li>{@link ArticleRecord#publisherId}对应{@code T5011.F10}</li>
	 * <li>{@link ArticleRecord#createtime}对应{@code T5011.F11}</li>
	 * <li>{@link ArticleRecord#publishTime}对应{@code T5011.F12}</li>
	 * <li>{@link ArticleRecord#publisherName}对应{@code T7110.F13}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param articleType 文章类型
	 * @return {@link ArticleRecord} 查询结果 不存在则返回 {@code null}
	 * @throws Throwable
	 */
	public abstract ArticleRecord get(T5011_F02 articleType) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 获取指定类型第一篇文章内容.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code articleType == null} 则返回 {@code null}</li>
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
	 * <li>查询{@code T5011}表,查询条件:{@code T5011.F02 = } {@link ArticleType#name() } {@code LIMIT 1}</li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link T5011.F01}</li>
	 * </ol>
	 * <li>查询{@link T5011},{@link T5011_1}表,查询条件:{@code T5011.F01 = T5011_1.F01 AND T5011_1.F01 = }{@code T5011}查出来的{@code T5011.F01}字段</li>
	 * 查询字段列表：
	 * <ol>
	 * <li>{@link T5011_1.F02}</li>
	 * </ol>
	 * </ol>
	 * </dl>
	 * 
	 * 
	 * <dl>
	 * 查询结果说明：
	 * <ol>
	 * <li>返回结果{@link String } 对应{@link T5011_1.F02}</li>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param articleType 文章类型
	 * @return {@link String} 返回文章内容 不存在则返回 {@code null}
	 * @throws Throwable
	 */
	public abstract String getContent(T5011_F02 articleType) throws Throwable;
	
	
    
/**
 * 获取帮助中心的问题类型列表
 * @param query
 * @param paging
 * @param articleType
 * @return
 * @throws Throwable
 */
    public abstract PagingResult<QuestionTypeRecord> searchCzytxWtlx(QuestionQuery query, Paging paging)
            throws Throwable;
    
    
    /**
     * 获取帮助中心的问题
     * @param query
     * @param paging
     * @param articleType
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<QuestionRecord> searchCzytxWt(QuestionQuery query, Paging paging)
                throws Throwable;
    
    
    /**
     * 添加问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract int addQuestionType(T5011_F02 articleType, Question questionType)
            throws Throwable;
    
    
    /**
     * 添加问题
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract int addQuestion(T5011_F02 articleType,Question questionType) throws Throwable ;
    
    /**
     * 更新问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract void updateQuestionType(T5011_F02 articleType,Question questionType,int id)
            throws Throwable;
        
    /**
     * 更新问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract void updateQuestion(T5011_F02 articleType, Question question,int id) throws Throwable;
    
    /**
     * 根据问题类型ID得到问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
    public abstract QuestionTypeRecord getQuestionType(int id,T5011_F02 articleType)throws Throwable;
    
    /**
     * 根据问题ID得到问题
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
	public abstract QuestionRecord getQuestion(int id,T5011_F02 articleType) throws Throwable ;
	
	
    /**
     * 得到所有的问题类型
     * @param articleType
     * @param article
     * @return
     * @throws Throwable
     */
	public abstract QuestionTypeRecord[] getQuestionTypes(T5011_F02 articleType) throws Throwable ;
	
	/**
	 * 删除问题根据ID
	 * @param ids
	 * @throws Throwable
	 */
	 public abstract void deleteQuestion(int... ids)throws Throwable;
	 
	 /**
	 * 删除问题类型根据ID
	 * @param ids
	 * @throws Throwable
	 */
	 public abstract void deleteQuestionType(int... ids)throws Throwable;
	 
	 /**
     * 根据编码查询文章类别
     * @param ids
     * @throws Throwable
     */
     public abstract T5010 getArticleTypeByCode(String code)throws Throwable;
     
     
     /** <一句话功能简述>
     * 根据父类id查询文章类别
     * @param parentId
     * @param status
     * @return
     * @throws Throwable
     */
    public abstract T5010[] getArticleCategoryAll(int parentId)throws Throwable;
    
    /** <一句话功能简述>
     * 更新文章类别
     * @param t5010
     * @throws Throwable
     */
    public abstract void updateT5010(T5010 t5010)throws Throwable;
    
    /** <一句话功能简述>
     * 根据类别编码查询名称
     * @param code
     * @return
     * @throws Throwable
     */
    public abstract String getCategoryNameByCode(String code)throws Throwable;
    
    /** <一句话功能简述>
     * 批量更新文章排序值
     * @param ids
     * @param order
     * @return
     * @throws Throwable
     */
    public abstract void updateBatchArticleOrder(String ids,int order)throws Throwable;
    
    /**
     * 添加运营文件附件
     * <功能详细描述>
     * @param t5011_3
     * @return
     * @throws Throwable
     */
    public abstract int addFile(T5011_3 t5011_3, PartFile part)
        throws Throwable;
    
    /**
     * 获取运营文件附件
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract T5011_3 getFileInfo(int Id)
        throws Throwable;
    
    /**
     * 修改运营文件附件
     * <功能详细描述>
     * @param t5011_3
     * @return
     * @throws Throwable
     */
    public abstract void updateFile(T5011_3 t5011_3, PartFile part)
        throws Throwable;
    
    /**
     * 查询运营报告列表
     * <功能详细描述>
     * @param OperateReportQuery
     * @return PagingResult<OperateReport>
     * @throws Throwable
     */
    public abstract PagingResult<OperateReport> searchOperateReport(OperateReportQuery query, Paging paging)
        throws Throwable;
    
	/**
	 * 
	 * @param articleType 文章类型
	 * @param article 文章信息
	 * @param t5011_3 附件信息
	 * @param part 
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(T5011_F02 articleType, Article article,T5011_3 t5011_3, PartFile part)
			throws Throwable;
	
    /**
     * 
     * @param id  文章ID
     * @param articleType 文章类型
     * @param article 文章信息
     * @param t5011_3 附件信息
     * @param part
     * @throws Throwable
     */
	public abstract void update(int id,T5011_F02 articleType, Article article,T5011_3 t5011_3, PartFile part) throws Throwable;
	

}
