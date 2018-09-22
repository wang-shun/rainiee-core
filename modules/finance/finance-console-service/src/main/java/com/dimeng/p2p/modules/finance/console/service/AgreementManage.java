package com.dimeng.p2p.modules.finance.console.service;

import java.util.Calendar;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S60.entities.T6010;
import com.dimeng.p2p.S60.entities.T6011;
import com.dimeng.p2p.S60.entities.T6036;
import com.dimeng.p2p.S60.entities.T6037;
import com.dimeng.p2p.S60.entities.T6039;
import com.dimeng.p2p.S60.entities.T6040;
import com.dimeng.p2p.S60.entities.T6042;
import com.dimeng.p2p.S60.entities.T6043;
import com.dimeng.p2p.S70.entities.T7029;
import com.dimeng.p2p.S70.entities.T7031;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.modules.finance.console.service.entity.Borrower;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditTrs;
import com.dimeng.p2p.modules.finance.console.service.entity.JoinedUser;
import com.dimeng.p2p.modules.finance.console.service.entity.Organization;
import com.dimeng.p2p.modules.finance.console.service.entity.YxDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.YxJoined;
import com.dimeng.p2p.modules.finance.console.service.entity.YxUser;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrCreditDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrUser;

public abstract interface AgreementManage extends Service {
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取加入用户
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
	 * <li>查询{@link T6037}表 {@link T6036}表  {@link T6010}表 </li>
	 * <li>查询条件:{@code T5020.F01 = id}
	 * <ol>
	 * <li>{@code T6037.F02=T6036.F01}</li>
	 * <li>{@code T6010.F01=T6037.F03 WHERE T6037.F02= creditId}</li>
	 * </ol>
	 * </li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link JoinedUser#userName}对应{@code T6010.F02}</li>
	 * <li>{@link JoinedUser#amount}对应{@code T6037.F04}</li>
	 * <li>{@link JoinedUser#limitMonth}对应{@code T6036.F08}</li>
	 * <li>年化利率  对应  {@code T6036.F09}</li>
	 * </ol>
	 * </li>
	 * </dl>
	 * </dt>
	 * 
	 * @param creditId 借款标ID
	 * @return {@link JoinedUser}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract JoinedUser[] getJoinedUser(int creditId) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取借款者信息
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
	 * <li>查询{@link T6036}表  查询条件:{@code T6036.F01 = creditId}   得到{@code T6036.F02}  如果{@code T6036.F02<=0}则直接返回{@code null}</li>
	 * <li>查询{@link T6010}表  查询条件:{@code T6010.F01 = }查询{@code T6036}表得到的{@code T6036.F02}  得到{@code T6010.F02}字段对应{@link Borrower#accountName}</li>
	 * <li>查询{@link T6011}表  查询条件:{@code T6011.F01 = }查询{@code T6036}表得到的{@code T6036.F02}  得到{@code T6011.F06}字段对应{@link Borrower#realName}
	 * 得到{@code T6011.F07}字段对应{@link Borrower#identifyId}
	 * </li>
	 * </dl>
	 * </dt>
	 * 
	 * @param creditId 借款标ID
	 * @return {@link Borrower} 查询结果
	 * @throws Throwable
	 */
	public abstract Borrower getBorrower(int creditId) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取标的详情
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
	 * <li>查询{@link T6036}表   查询条件：{@code T6036.F01 = creditId}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link CreditDetail#use}对应{@code T6036.F05}</li>
	 * <li>{@link CreditDetail#amount}对应{@code T6036.F06}</li>
	 * <li>{@link CreditDetail#limitMonth}对应{@code T6036.F08}</li>
	 * <li>{@link CreditDetail#monthAmount}对应{@code T6036.F13}</li>
	 * <li>{@link CreditDetail#startDate}对应{@code T6036.F31}</li>
	 * <li>{@link CreditDetail#rate}对应{@code T6036.F09}</li>
	 * 
	 * </ol>
	 * </li>
	 * </dl>
	 * </dt>
	 * 
	 * @param creditId 借款标ID
	 * @return {@link CreditDetail} 查询结果
	 * @throws Throwable
	 */
	public abstract CreditDetail getCreditDetail(int creditId) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取债权转让信息
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
	 * <li>查询{@link T6039}表 {@link T6040}表   查询条件：{@code T6039.F01=T6040.F02 WHERE T6039.F03= creditId}  </li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link CreditTrs#outId}对应{@code T6039.F04}</li>
	 * <li>{@link CreditTrs#inId}对应{@code T6040.F03}</li>
	 * <li>{@link CreditTrs#amount}对应{@code T6040.F04*T6039.F06}</li>
	 * <li>{@link CreditTrs#time} 对应  {@code TT6040.F05}</li>
	 * <li>如果没有查询到结果，则直接返回 {@code null}</li>
	 * </ol>
	 * </li>
	 * <li>查询{@link T6010}表  查询条件：{@code T6010.F01 = }{@link CreditTrs#outId}  得到{@code T6010.F02}字段对应 {@link CreditTrs#out}</li>
	 * <li>查询{@link T6010}表  查询条件：{@code T6010.F01 = }{@link CreditTrs#inId}  得到{@code T6010.F02}字段对应 {@link CreditTrs#in}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param creditId 借款标ID
	 * @return {@link CreditTrs}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract CreditTrs[] getCreditTransfers(int creditId)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取担保机构
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
	 * <li>定义{@code int contractId = 0}</li>
	 * <li>查询{@link T6036}表   查询条件：{@code T6036.F01 = creditId} 得到{@code T6036.F25}字段赋值给{@code contractId} </li>
	 * <li>如果{@code contractId <= 0}  则直接返回{@code null}</li>
	 * <li>定义{@code int orgId = 0}</li>
	 * <li>查询{@link T7031}表   查询条件：{@code T7031.F01 = contractId} 得到{@code T7031.F02}字段赋值给{@code orgId} </li>
	 * <li>如果{@code orgId <= 0}  则直接返回{@code null}</li>
	 *  <li>查询{@link T7029}表   查询条件：{@code T7029.F01 = orgId} 得到{@code T7029.F02}字段赋值给{@link Organization#name}
	 *  得到{@code T7029.F04}字段赋值给{@link Organization#address} 
	 *  </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param creditId 借款标ID
	 * @return {@link Organization} 查询结果
	 * @throws Throwable
	 */
	public abstract Organization getOrganization(int creditId) throws Throwable;
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据借款标ID获取表的类型
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code creditId<=0} 则直接返回{@code null}</li>
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
	 * <li>查询{@link T6036}表  查询条件:{@code T6036.F01 = creditId}  得到字段{@code T6036.F19}对应{@link CreditType}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @param creditId 借款标ID
	 * @return {@link CreditType} 查询结果
	 * @throws Throwable
	 */
	public abstract CreditType getCreditType(int creditId) throws Throwable;
	
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：获取加入优选用户信息
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
	 * <li>如果{@code session == null}或则 未通过验证   则返回{@code null}</li>
	 * <li>查询{@link T6011}表  查询条件:{@code T6011.F01 = }当前系统登录ID
	 * <ol>
	 * 查询字段列表：
	 * <li>{@link YxUser#phone}对应{@code T6011.F02}</li>
	 * <li>{@link YxUser#name}对应{@code T6011.F06}</li>
	 * <li>{@link YxUser#identifyId}对应{@code T6011.F07}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * @return {@link YxUser} 查询结果
	 * @throws Throwable
	 */
	public abstract YxUser getYxUser(int userId) throws Throwable;
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据优选理财ID获取优选加入记录
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code yxID<=0} 则直接返回{@code null}</li>
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
	 * <li>查询{@link T6043}表  查询条件:{@code T6043.F02=yxID AND T6043.F03=}当前系统登录ID
	 * <ol>
	 * 查询字段列表：
	 * <li>{@link YxJoined#amount}对应{@code T6043.F04}</li>
	 * <li>{@link YxJoined#time}对应{@code T6043.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * 
	 * @param yxID 优选理财ID
	 * @return {@link YxJoined}{@code []} 查询结果
	 * @throws Throwable
	 */
	public abstract YxJoined[] getYxJoineds(int yxID,int userId) throws Throwable;
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据优选理财ID获取优选期限时间
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code yxID<=0} 则直接返回{@code null}</li>
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
	 * <li>查询{@link T6042}表  查询条件:{@code T6042.F02=yxID}
	 * <ol>
	 * 查询字段列表：
	 * <li>{@link YxDetail#title}对应{@code T6042.F02}</li>
	 * <li>{@link YxDetail#sqks}对应{@code T6042.F09}</li>
	 * <li>{@link YxDetail#sqjs}对应{@code T6042.F10}</li>
	 * <li>{@link YxDetail#lcjs}对应  调用{@link Calendar}转化{@code T6042.F11}</li>
	 * <li>{@link YxDetail#jrfl}对应{@code T6042.F15}</li>
	 * <li>{@link YxDetail#fwfl}对应{@code T6042.F16}</li>
	 * <li>{@link YxDetail#tcfl}对应{@code T6042.F17}</li>
	 * <li>{@link YxDetail#low}对应{@code T6042.F22}</li>
	 * <li>{@link YxDetail#upper}对应{@code T6042.F23}</li>
	 * <li>{@link YxDetail#mesj}对应{@code T6042.F12}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param yxID 优选理财ID
	 * @return {@link YxDetail}
	 * @throws Throwable
	 */
	public abstract YxDetail getYxDeadline(int yxID) throws Throwable;
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取转出者
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id<=0} 则直接返回{@code null}</li>
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
	 * <li>定义{@code int accountId = 0}</li>
	 * <li>查询{@link T6039}表  {@link T6040}表    查询条件:{@code T6039.F01=T6040.F02 AND T6040.F01= id}   得到{@code T6039.F04}字段赋值给{@code accountId}</li>
	 * <li>如果{@code accountId <= 0} 则直接返回{@code null} </li>
	 * <li>查询{@link T6010}表   查询条件:{@code T6010.F01= accountId}   得到{@code T6010.F02}字段赋值给{@link ZqzrUser#account}</li>
	 * <li>查询{@link T6011}表   查询条件:{@code T6011.F01= accountId}   得到{@code T6011.F06}字段赋值给{@link ZqzrUser#name}
	 *  得到{@code T6011.F07}字段赋值给{@link ZqzrUser#identifyId}
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id 债权转让-转入表自增ID
	 * @return {@link ZqzrUser} 查询结果
	 * @throws Throwable
	 */
	public abstract ZqzrUser getZcz(int id) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取转入者
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id<=0} 则直接返回{@code null}</li>
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
	 * <li>定义{@code int accountId = 0}</li>
	 * <li>查询{@link T6040}表    查询条件:{@code T6040.F01= id}   得到{@code T6040.F03}字段赋值给{@code accountId}</li>
	 * <li>如果{@code accountId <= 0} 则直接返回{@code null} </li>
	 * <li>查询{@link T6010}表   查询条件:{@code T6010.F01= accountId}   得到{@code T6010.F02}字段赋值给{@link ZqzrUser#account}</li>
	 * <li>查询{@link T6011}表   查询条件:{@code T6011.F01= accountId}   得到{@code T6011.F06}字段赋值给{@link ZqzrUser#name}
	 *  得到{@code T6011.F07}字段赋值给{@link ZqzrUser#identifyId}
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 债权转让-转入表自增ID
	 * @return {@link ZqzrUser} 查询结果
	 * @throws Throwable
	 */
	public abstract ZqzrUser getZrz(int id) throws Throwable;
	
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取债权转让的标的详情
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id<=0} 则直接返回{@code null}</li>
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
	 * <li>定义{@code int creditId = 0}</li>
	 * <li>查询{@link T6040}表  {@link T6039}表    查询条件:{@code T6039.F01=T6040.F02 AND T6040.F01=id}   得到{@code T6039.F03 }字段赋值给{@code creditId}</li>
	 * <li>如果{@code creditId <= 0} 则直接返回{@code null} </li>
	 * <li>查询{@link T6011}表 {@link T6036}表  查询条件:{@code T6036.F02=T6011.F01 AND T6036.F01= creditId}   得到{@code T6010.F02}字段赋值给{@link ZqzrUser#account}</li>
	 * <li>
	 * <ol>
	 * 查询列表字段：
	 * <li>{@link ZqzrCreditDetail#bName}对应{@code T6011.F06}</li>
	 * <li>{@link ZqzrCreditDetail#amount}对应{@code T6036.F06}</li>
	 * <li>{@link ZqzrCreditDetail#limitMonth}对应{@code T6036.F08}</li>
	 * <li>{@link ZqzrCreditDetail#monthAmount}对应{@code T6036.F13}</li>
	 * <li>{@link ZqzrCreditDetail#startDate}对应{@code T6036.F31}</li>
	 * <li>{@link ZqzrCreditDetail#rate}对应{@code T6036.F09}</li>
	 * <li>{@link ZqzrCreditDetail#id}对应{@code T6036.F01}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 债权转让-转入表自增ID
	 * @return {@link ZqzrCreditDetail} 查询结果
	 * @throws Throwable
	 */
	public abstract ZqzrCreditDetail getZqzrCreditDetail(int id) throws Throwable;
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取债权转让信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id<=0} 则直接返回{@code null}</li>
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
	 * <li>查询{@link T6040}表  {@link T6039}表  {@link T6036}表    查询条件:{@code T6039.F01=T6040.F02 AND T6040.F01=id} </li>
	 * <li>
	 * <ol>
	 * 查询列表字段：
	 * <li>{@link ZqzrInfo#zqjz}对应{@code T6039.F05}乘以{@code T6040.F04}</li>
	 * <li>{@link ZqzrInfo#zrjk}对应{@code T6039.F06}乘以{@code T6040.F04}</li>
	 * <li>{@link ZqzrInfo#zrjk}对应{@code T6040.F06}</li>
	 * <li>{@link ZqzrInfo#zrsj}对应{@code T6040.F05}</li>
	 * <li>{@link ZqzrInfo#syqs}对应{@code T6036.F24}</li>
	 * <li>{@code T6040.F04}</li>
	 * 
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 债权转让-转入表自增ID
	 * @return {@link ZqzrInfo} 查询结果
	 * @throws Throwable
	 */
	public abstract ZqzrInfo getZqzrInfo(int id) throws Throwable;
}
