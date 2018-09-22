package com.dimeng.p2p.modules.financing.front.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.modules.financing.front.service.entity.AlsoMoney;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditHoldInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditOutRecode;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditUserInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.TenderRecord;
import com.dimeng.p2p.modules.financing.front.service.entity.UserRZInfo;

/**
 * 散标详情接口
 *
 */
public interface InvestInfoManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：获取借款用户的信息.
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
	 * <li>查询{@code T6011},{@code T6010},{@code T6012},{@code T6014},{@code T6015},{@code T6036}
	 * {@code T6036.F01 = }
	 * </li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6010.F02}</li>
	 * <li>{@code T6011.F17}</li>
	 * <li>{@code T6011.F21}</li>
	 * <li>{@code T6012.F02}</li>
	 * <li>{@code T6014.F09}</li>
	 * <li>{@code T6014.F08}</li>
	 * <li>{@code T6014.F05}</li>
	 * <li>{@code T6014.F13}</li>
	 * <li>{@code T6014.F10}</li>
	 * <li>{@code T6014.F11}</li>
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
	 * <li>{@link CreditUserInfo#companySize}对应{@code T6014.F09}</li>
	 * <li>{@link CreditUserInfo#companyType}对应{@code T6014.F08}</li>
	 * <li>{@link CreditUserInfo#city}对应{@code T6014.F05}</li>
	 * <li>{@link CreditUserInfo#gwzz}对应{@code T6014.F13}</li>
	 * <li>{@link CreditUserInfo#workAge}对应{@code T6014.F10}</li>
	 * <li>{@link CreditUserInfo#earnMoey}对应{@code T6014.F11}</li>
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
	 * @param id
	 * 			借款标ID
	 * @param creditType
	 * 			借款标类型
	 * @return {@link CreditUserInfo} 获取借款用户的信息.
	 * @throws Throwable
	 */
	public abstract CreditUserInfo getUser(int id,CreditType creditType)throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：获取投资记录.
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
	 * <li>查询{@code T6036},{@code T6037},{@code T6010}
	 * {@code T6036.F01 = }
	 * </li>
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
	 * @param id
	 * 			借款标ID
	 * @param creditType
	 * 			借款标类型
	 * @return {@link TenderRecord[]} 获取投资记录.
	 * @throws Throwable
	 */
	public abstract TenderRecord[] getRecord(int id)throws Throwable;
	/**
	 * 获取还款记录
	 * @param 借款标id
	 * @param paging
	 * @return
	 */
	public abstract AlsoMoney[] getAlso(int id)throws Throwable;
	/**
	 * 债权持有信息
	 * @param 借款标id
	 * @param paging
	 * @return
	 */
	public abstract CreditHoldInfo[] getHoldInfo(int id)throws Throwable;
	/**
	 * 转出记录
	 * @param 借款标id
	 * @param paging
	 * @return
	 */
	public abstract CreditOutRecode[] getOutRecode(int id)throws Throwable;
	
	/**
	 * 用户认证信息
	 * @param 借款标id
	 * @return
	 * @throws Throwable
	 */
	public abstract UserRZInfo[] getRZInfo(int id)throws Throwable;
	/**
	 * 债权待交易总额
	 * @param 借款标id
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal getDjyMoney(int id)throws Throwable;
	/**
	 * 债权已交易总额
	 * @param 借款标id
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal getyjyMoney(int id)throws Throwable;
	
}
