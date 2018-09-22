package com.dimeng.p2p.modules.financing.front.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.common.enums.AttestationStatus;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.TransferStatus;
import com.dimeng.p2p.modules.financing.front.service.entity.AlsoMoney;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditHoldInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditOutRecode;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditUserInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.TenderRecord;
import com.dimeng.p2p.modules.financing.front.service.entity.UserRZInfo;

/**
 * 债权详情接口
 *
 */
public interface CreditAssignmentInfoManage extends Service {
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询用户信息信息.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6011}{@code T6010}{@code T6012}{@code T6013}{@code T6015}{@code T6036}{@code T6039}表,查询条件:{@code T6039.F01 = }
	 * </li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6010.F02}</li>
	 * <li>{@code T6011.F17}</li>
	 * <li>{@code T6011.F21}</li>
	 * <li>{@code T6012.F02}</li>
	 * <li>{@code T6013.F12}</li>
	 * <li>{@code T6013.F10}</li>
	 * <li>{@code T6013.F08}</li>
	 * <li>{@code T6013.F05}</li>
	 * <li>{@code T6013.F13}</li>
	 * <li>{@code T6013.F14}</li>
	 * <li>{@code T6015.F03}</li>
	 * <li>{@code T6015.F04}</li>
	 * <li>{@code T6015.F06}</li>
	 * <li>{@code T6015.F08}</li>
	 * <li>{@code T6010.F01}</li>
	 * <li>{@code T6011.F16}</li>
	 * <li>{@code T6011.F07}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditUserInfo#userName}对应{@code T6010.F02}</li>
	 * <li>{@link CreditUserInfo#xueli}对应{@code T6011.F17}</li>
	 * <li>{@link CreditUserInfo#university}对应{@code T6011.F21}</li>
	 * <li>{@link CreditUserInfo#hunyin}对应{@code T6012.F02}</li>
	 * <li>{@link CreditUserInfo#companySize}对应{@code T6013.F12}</li>
	 * <li>{@link CreditUserInfo#companyType}对应{@code T6013.F10}</li>
	 * <li>{@link CreditUserInfo#city}对应{@code T6013.F08}</li>
	 * <li>{@link CreditUserInfo#gwzz}对应{@code T6013.F05}</li>
	 * <li>{@link CreditUserInfo#workAge}对应{@code T6013.F13}</li>
	 * <li>{@link CreditUserInfo#earnMoey}对应{@code T6013.F14}</li>
	 * <li>{@link CreditUserInfo#isHouse}对应{@code T6015.F03}</li>
	 * <li>{@link CreditUserInfo#isHouseCredit}对应{@code T6015.F04}</li>
	 * <li>{@link CreditUserInfo#isCar}对应{@code T6015.F06}</li>
	 * <li>{@link CreditUserInfo#isCarCredit}对应{@code T6015.F08}</li>
	 * <li>{@link CreditUserInfo#userId}对应{@code T6010.F01}</li>
	 * <li>{@link CreditUserInfo#sex}对应{@code T6011.F16}</li>
	 * <li>{@link CreditUserInfo#card}对应{@code T6011.F07}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link CreditUserInfo} 用户信息,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CreditUserInfo getUser(int id,CreditType creditType)throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询投资记录.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6037}{@code T6010}{@code T6039}表,查询条件:{@code T6039.F01 = }
	 * </li>
	 * <li>按照{@code T6037.F05}字段降序排序</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6037.F04}</li>
	 * <li>{@code T6037.F05}</li>
	 * <li>{@code T6010.F02}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link TenderRecord#tenderMoney}对应{@code T6037.F04}</li>
	 * <li>{@link TenderRecord#tenderTime}对应{@code T6037.F05}</li>
	 * <li>{@link TenderRecord#tenderName}对应{@code T6010.F02}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link TenderRecord[]} 投资记录,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract TenderRecord[] getRecord(int id)throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询还款记录.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6041}{@code T6039}表,查询条件:{@code T6039.F01 = }
	 * </li>
	 * <li>按照{@code T6041.F10}字段升序排序</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6041.F10}</li>
	 * <li>{@code T6041.F12}</li>
	 * <li>{@code T6041.F05}</li>
	 * <li>{@code T6041.F07}</li>
	 * <li>{@code T6041.F11}</li>
	 * <li>{@code T6041.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link AlsoMoney#hyhkrq}对应{@code T6041.F10}</li>
	 * <li>{@link AlsoMoney#status}对应{@code T6041.F12}</li>
	 * <li>{@link AlsoMoney#yhbj}对应{@code T6041.F05}</li>
	 * <li>{@link AlsoMoney#yffx}对应{@code T6041.F07}</li>
	 * <li>{@link AlsoMoney#sjhkTime}对应{@code T6041.F11}</li>
	 * <li>{@link AlsoMoney#yhlx}对应{@code T6041.F06}</li>
	 * <li>{@link AlsoMoney#yhbx}对应{@code (yhlx+yhbj)}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link AlsoMoney[]} 还款记录,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract AlsoMoney[] getAlso(int id)throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询债权持有人.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6038}{@code T6010}{@code T6039}表,查询条件:{@code T6039.F01 = }
	 * </li>
	 * <li>按照{@code T6038.F06}字段降序排序</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6010.F02}</li>
	 * <li>{@code T6038.F04}</li>
	 * <li>{@code T6039.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditHoldInfo#userName}对应{@code T6010.F02}</li>
	 * <li>{@link CreditHoldInfo#touzje}对应{@code T6038.F04}</li>
	 * <li>{@link CreditHoldInfo#mfje}对应{@code T6039.F06}</li>
	 * <li>{@link CreditHoldInfo#cyfs}对应{@code touzje/mfje}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link CreditHoldInfo[]} 持有人记录,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CreditHoldInfo[] getHoldInfo(int id)throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询转出记录.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6040}{@code T6039}{@code T6010}表,查询条件:{@code T6039.F01 = }
	 * </li>
	 * <li>按照{@code T6040.F05}字段降序排序</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6010.F02}</li>
	 * <li>{@code T6040.F04}</li>
	 * <li>{@code T6039.F06}</li>
	 * <li>{@code T6040.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditOutRecode#creditBuy}对应{@code T6010.F02}</li>
	 * <li>{@link CreditOutRecode#jyfs}对应{@code T6040.F04}</li>
	 * <li>{@link CreditOutRecode#jydj}对应{@code T6039.F06}</li>
	 * <li>{@link CreditOutRecode#sellTime}对应{@code T6040.F05}</li>
	 * <li>{@link CreditOutRecode#sellMoney}对应{@code jyfs*jydj}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link CreditOutRecode[]} 转出记录,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CreditOutRecode[] getOutRecode(int id)throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：根据转出表ID查询用户认证信息.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6018}{@code T6039}{@code T6036}表,查询条件:{@code T6039.F01 = }
	 * {@link AttestationStatus#YYZ}</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6018.F03}</li>
	 * <li>{@code T6018.F04}</li>
	 * <li>{@code T6018.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link UserRZInfo#shProject}对应{@code T6018.F03}</li>
	 * <li>{@link UserRZInfo#rzStatus}对应{@code T6018.F04}</li>
	 * <li>{@link UserRZInfo#rzDate}对应{@code T6018.F06}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link UserRZInfo[]} 用户认证状态,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract UserRZInfo[] getRZInfo(int id)throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：债权待交易总额.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6039}表,查询条件:{@code T6039.F01 =,T6039.F11 =,T6039.F12 = }
	 * {@link id}{@link TransferStatus#YX}{@link OverdueStatus#S}</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6039.F08}</li>
	 * <li>{@code T6039.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link BigDecimal}对应{@code SUM(T6039.F08*T6039.F06)}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link BigDecimal} 债权待交易总额,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract BigDecimal getDjyMoney(int id)throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：债权已交易总额.
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
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6039}{@code T6040}表,查询条件:{@code T6039.F01 = }
	 * {@link id}</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6040.F04}</li>
	 * <li>{@code T6039.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link BigDecimal}对应{@code SUM(T6040.F04*T6039.F06)}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            转出表ID
	 * @return {@link BigDecimal} 债权已交易总额,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract BigDecimal getyjyMoney(int id)throws Throwable;
	
}
