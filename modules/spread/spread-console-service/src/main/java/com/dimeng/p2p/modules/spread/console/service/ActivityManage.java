package com.dimeng.p2p.modules.spread.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.entities.T6010;
import com.dimeng.p2p.S60.entities.T6011;
import com.dimeng.p2p.S70.entities.T7011;
import com.dimeng.p2p.S70.entities.T7046;
import com.dimeng.p2p.S70.entities.T7047;
import com.dimeng.p2p.modules.spread.console.service.entity.Activity;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityTotalInfo;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakePersonList;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakeQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakeTotalInfo;
/**
 * 活动管理
 *
 */
public abstract interface ActivityManage extends Service{
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：得到活动统计信息.
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
	 * <li>查询{@link T7046}表</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ActivityTotalInfo#totalMoney}对应{@code IFNULL(SUM(T7046.F07),0)}</li>
	 * <li>{@link ActivityTotalInfo#totalPerson}对应{@code IFNULL(SUM(T7046.F09),0)}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return {@link ActivityTotalInfo} 活动统计信息  没有则返回 {@code null}
	 * @throws Throwable
	 */
	public ActivityTotalInfo getActivityTotalInfo() throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询活动列表
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
	 * <li>查询{@link T7046}表{@link T7011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T7046.F05 = T7011.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link ActivityQuery#title()}不为空  则{@code T7046.F02 like }{@link ActivityQuery#title()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#startTime()}不为空  则{@code DATE(T7046.F03) >= }{@link ActivityQuery#startTime()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#endTime()}不为空  则{@code DATE(T7046.F03) <= }{@link ActivityQuery#endTime()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#status()}不为空  则 
	 * <ol>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串WKS  则  {@code T7046.F03 > }当前系统时间</li>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串YJS  则  {@code T7046.F04 < }当前系统时间</li>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串JXZ  则  {@code T7046.F03 <=  }当前系统时间{@code AND T7046.F04 >=  }当前系统时间</li>
	 * </ol>
	 * </li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T7046.F01}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ActivityList#id}对应{@code T7046.F01}</li>
	 * <li>{@link ActivityList#title}对应{@code T7046.F02}</li>
	 * <li>{@link ActivityList#startTime}对应{@code T7046.F03}</li>
	 * <li>{@link ActivityList#endTime}对应{@code T7046.F04}</li>
	 * <li>{@link ActivityList#publishName}对应{@code T7011.F02}</li>
	 * <li>{@link ActivityList#describe}对应{@code T7046.F06}</li>
	 * <li>{@link ActivityList#money}对应{@code T7046.F07}</li>
	 * <li>{@link ActivityList#partakeNum}对应{@code T7046.F08}</li>
	 * <li>{@link ActivityList#benefitNum}对应{@code T7046.F09}</li>
	 * <li>
	 * <ol>
	 * <li>如果当前系统时间在{@code T7046.F03}之前   则{@link ActivityList#status}赋值为 未开始</li>
	 * <li>如果当前系统时间在{@code T7046.F03}之后 并且 在{@code T7046.F04}之后  则{@link ActivityList#status}赋值为 进行中</li>
	 * <li>否则{@link ActivityList#status}赋值为 已结束</li>
	 * </ol>
	 * </li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>			
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link ActivityList}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public PagingResult<ActivityList> searchActivity(ActivityQuery query,Paging paging) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：添加活动信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@link Activity#title() }为空  或则{@link Activity#title() }的长度大于15  则抛出参数异常  活动主题不能为空且15字之内</li>
	 * <li>如果{@link Activity#startTime() }为空  或则{@link Activity#endTime() }  则抛出参数异常 开始/结束时间不能为空</li>
	 * <li>如果{@link Activity#describe() }为空 则抛出参数异常 活动描述不能为空</li>
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
	 * <li>新增{@link T7046}表   
	 * <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link Activity#title()}对应{@code T7046.F02}</li>
	 * <li>{@link Activity#startTime()}对应{@code T7046.F03}</li>
	 * <li>{@link Activity#endTime()}对应{@code T7046.F04}</li>
	 * <li>当前登录系统ID 对应{@code T7046.F05}</li>
	 * <li>{@link Activity#describe()} 对应{@code T7046.F06}</li>
	 * <li>{@link Activity#money()} 对应{@code T7046.F07}</li>
	 * <li>{@link Activity#createTime()}对应{@code T7046.F10}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param activity 活动信息
	 * @throws Throwable
	 */
	public void addActivity(Activity activity) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据活动ID查询活动详情
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
	 * <li>查询{@link T7046}表{@link T7011}表  查询条件:{@code T7046.F05 = T7011.F01 AND T7046.F01 = id}</li>
	 * <li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ActivityList#id}对应{@code T7046.F01}</li>
	 * <li>{@link ActivityList#title}对应{@code T7046.F02}</li>
	 * <li>{@link ActivityList#startTime}对应{@code T7046.F03}</li>
	 * <li>{@link ActivityList#endTime}对应{@code T7046.F04}</li>
	 * <li>{@link ActivityList#publishName}对应{@code T7011.F02}</li>
	 * <li>{@link ActivityList#describe}对应{@code T7046.F06}</li>
	 * <li>{@link ActivityList#money}对应{@code T7046.F07}</li>
	 * <li>{@link ActivityList#partakeNum}对应{@code T7046.F08}</li>
	 * <li>{@link ActivityList#benefitNum}对应{@code T7046.F09}</li>
	 * <li>{@link ActivityList#createTime}对应{@code T7046.F10}</li>
	 * <li>
	 * <ol>
	 * <li>如果当前系统时间在{@code T7046.F03}之前   则{@link ActivityList#status}赋值为 未开始</li>
	 * <li>如果当前系统时间在{@code T7046.F03}之后 并且 在{@code T7046.F04}之后  则{@link ActivityList#status}赋值为 进行中</li>
	 * <li>否则{@link ActivityList#status}赋值为 已结束</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 活动ID
	 * @return {@link ActivityList} 查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public ActivityList getActivity(int id) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改活动信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@link Activity#title() }为空  或则{@link Activity#title() }的长度大于15  则抛出参数异常  活动主题不能为空且15字之内</li>
	 * <li>如果{@link Activity#startTime() }为空  或则{@link Activity#endTime() }  则抛出参数异常 开始/结束时间不能为空</li>
	 * <li>如果{@link Activity#describe() }为空 则抛出参数异常 活动描述不能为空</li>
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
	 * <li>修改{@link T7046}表   修改条件：{@code T7046.F01 = id}<li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link Activity#title()}对应{@code T7046.F02}</li>
	 * <li>{@link Activity#startTime()}对应{@code T7046.F03}</li>
	 * <li>{@link Activity#endTime()}对应{@code T7046.F04}</li>
	 * <li>当前登录系统ID 对应{@code T7046.F05}</li>
	 * <li>{@link Activity#describe()} 对应{@code T7046.F06}</li>
	 * <li>{@link Activity#money()} 对应{@code T7046.F07}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 活动ID
	 * @param activity 活动信息
	 * @throws Throwable
	 */
	public void updateActivity(int id,Activity activity) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据活动id分页查询参与人信息列表.
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
	 * <li>查询{@link T6010}表{@link T6011}表{@link T7047}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T7047.F03=T6010.F01  T7047.F03=T6011.F01  T7047.F02 = id}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link PartakeQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link PartakeQuery#userName()}</li>
	 * <li>如果{@link PartakeQuery#startTime()}不为空  则{@code TDATE(T7047.F06) >= }{@link PartakeQuery#startTime()}</li>
	 * <li>如果{@link PartakeQuery#endTime()}不为空  则{@code DATE(T7047.F06) <= }{@link PartakeQuery#endTime()}</li>
	 * <li>如果{@link PartakeQuery#benefitEnum()}不为空  则{@code T7047.F05 = }{@link PartakeQuery#benefitEnum()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PartakePersonList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link PartakePersonList#name}对应{@code T6011.F06}</li>
	 * <li>{@link PartakePersonList#reward}对应{@code T7047.F04}</li>
	 * <li>{@link PartakePersonList#benefitEnum}对应{@code T7047.F05}</li>
	 * <li>{@link PartakePersonList#time}对应{@code T7047.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 活动ID
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link PartakePersonList}{@code >} 分页查询结果,没有结果则返回{@code null} 
	 * @throws Throwable
	 */
	public PagingResult<PartakePersonList> searchPartake(int id,PartakeQuery query,Paging paging) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询参与人统计信息
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
	 * <li>查询{@link T7047}表  查询条件{@code T7047.F02= id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PartakeTotalInfo#id}对应{@code T7047.F02}</li>
	 * <li>{@link PartakeTotalInfo#totalMoney}对应{@code IFNULL(SUM(T7047.F04),0)}</li>
	 * <li>{@link PartakeTotalInfo#personNum}对应{@code IFNULL(COUNT(T7047.F09),0)}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 活动ID
	 * @return {@link PartakeTotalInfo} 查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public PartakeTotalInfo getPartakeTotalInfo(int id) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：导出活动管理列表
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
	 * <li>查询{@link T7046}表{@link T7011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T7046.F05 = T7011.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link ActivityQuery#title()}不为空  则{@code T7046.F02 like }{@link ActivityQuery#title()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#startTime()}不为空  则{@code DATE(T7046.F03) >= }{@link ActivityQuery#startTime()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#endTime()}不为空  则{@code DATE(T7046.F03) <= }{@link ActivityQuery#endTime()}</li>
	 * 
	 * <li>如果{@link ActivityQuery#status()}不为空  则 
	 * <ol>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串WKS  则  {@code T7046.F03 > }当前系统时间</li>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串YJS  则  {@code T7046.F04 < }当前系统时间</li>
	 * <li>如果{@link ActivityQuery#status()}匹配上字符串JXZ  则  {@code T7046.F03 <=  }当前系统时间{@code AND T7046.F04 >=  }当前系统时间</li>
	 * </ol>
	 * </li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T7046.F01}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link ActivityList#id}对应{@code T7046.F01}</li>
	 * <li>{@link ActivityList#title}对应{@code T7046.F02}</li>
	 * <li>{@link ActivityList#startTime}对应{@code T7046.F03}</li>
	 * <li>{@link ActivityList#endTime}对应{@code T7046.F04}</li>
	 * <li>{@link ActivityList#publishName}对应{@code T7011.F02}</li>
	 * <li>{@link ActivityList#describe}对应{@code T7046.F06}</li>
	 * <li>{@link ActivityList#money}对应{@code T7046.F07}</li>
	 * <li>{@link ActivityList#partakeNum}对应{@code T7046.F08}</li>
	 * <li>{@link ActivityList#benefitNum}对应{@code T7046.F09}</li>
	 * <li>
	 * <ol>
	 * <li>如果当前系统时间在{@code T7046.F03}之前   则{@link ActivityList#status}赋值为 未开始</li>
	 * <li>如果当前系统时间在{@code T7046.F03}之后 并且 在{@code T7046.F04}之后  则{@link ActivityList#status}赋值为 进行中</li>
	 * <li>否则{@link ActivityList#status}赋值为 已结束</li>
	 * </ol>
	 * </li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>			
	 * 
	 * @param query 查询条件
	 * @return {@link ActivityList}{@code []} 查询结果 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public ActivityList[] exportActivity(ActivityQuery query) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据活动id导出活动参与人列表.
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
	 * <li>查询{@link T6010}表{@link T6011}表{@link T7047}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T7047.F03=T6010.F01  T7047.F03=T6011.F01  T7047.F02 = id}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link PartakeQuery#userName()}不为空  则{@code T6010.F02 LIKE }{@link PartakeQuery#userName()}</li>
	 * <li>如果{@link PartakeQuery#startTime()}不为空  则{@code DATE(T7047.F06) >= }{@link PartakeQuery#startTime()}</li>
	 * <li>如果{@link PartakeQuery#endTime()}不为空  则{@code DATE(T7047.F06) <= }{@link PartakeQuery#endTime()}</li>
	 * <li>如果{@link PartakeQuery#benefitEnum()}不为空  则{@code T7047.F05 = }{@link PartakeQuery#benefitEnum()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PartakePersonList#userName}对应{@code T6010.F02}</li>
	 * <li>{@link PartakePersonList#name}对应{@code T6011.F06}</li>
	 * <li>{@link PartakePersonList#reward}对应{@code T7047.F04}</li>
	 * <li>{@link PartakePersonList#benefitEnum}对应{@code T7047.F05}</li>
	 * <li>{@link PartakePersonList#time}对应{@code T7047.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id 活动ID
	 * @param query 查询条件
	 * @return {@link PartakePersonList}{@code []} 查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public PartakePersonList[] exportPartakePerson(int id,PartakeQuery query) throws Throwable;
}
