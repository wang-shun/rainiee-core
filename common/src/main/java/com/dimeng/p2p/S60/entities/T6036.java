package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6036_F10;
import com.dimeng.p2p.S60.enums.T6036_F11;
import com.dimeng.p2p.S60.enums.T6036_F18;
import com.dimeng.p2p.S60.enums.T6036_F19;
import com.dimeng.p2p.S60.enums.T6036_F20;
import com.dimeng.p2p.S60.enums.T6036_F39;
import com.dimeng.p2p.S60.enums.T6036_F40;

/**
 * 借款标
 */
public class T6036 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款标自增ID
	 */
	public int F01;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 债权ID
	 */
	public int F03;

	/**
	 * 标题
	 */
	public String F04;

	/**
	 * 借款用途
	 */
	public String F05;

	/**
	 * 借款金额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 可投资余额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 借款期限
	 */
	public int F08;

	/**
	 * 年化利率
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 还款周期,AYHK:按月还款
	 */
	public T6036_F10 F10;

	/**
	 * 还款方式,DEBX:等额本息;MYHXDQHB（每月还息，到期还本）
	 */
	public T6036_F11 F11;

	/**
	 * 筹标期限
	 */
	public int F12;

	/**
	 * 每月还款本息
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 成交服务费
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

	/**
	 * 实地审核费用
	 */
	public BigDecimal F15 = BigDecimal.ZERO;

	/**
	 * 每月交借款管理费
	 */
	public BigDecimal F16 = BigDecimal.ZERO;

	/**
	 * 借款描述
	 */
	public String F17;

	/**
	 * 是否发布,S:是;F:否
	 */
	public T6036_F18 F18;

	/**
	 * 借款标类型,XJD:薪金贷、SYD:生意贷、SDRZ:实地认证、XYDB信用担保、个人抵押担保，个人信用，企业信用，企业抵押担保
	 */
	public T6036_F19 F19;

	/**
	 * 标的状态,SQZ:申请中;DSH:待审核;TBZ:投资中;YMB:已满标;YFK:已放款(还款中);YJQ:已结清;YDF:已垫付;LB:流标
	 */
	public T6036_F20 F20;

	/**
	 * 提前还清金额
	 */
	public BigDecimal F21 = BigDecimal.ZERO;

	/**
	 * 提前还清违约金
	 */
	public BigDecimal F22 = BigDecimal.ZERO;

	/**
	 * 下一还款日
	 */
	public Date F23;

	/**
	 * 剩余期数
	 */
	public int F24;

	/**
	 * 关联机构合同ID,参考T7031.F01
	 */
	public int F25;

	/**
	 * 申请时间
	 */
	public Timestamp F26;

	/**
	 * 审核人,参考T7011.F01
	 */
	public int F27;

	/**
	 * 审核时间
	 */
	public Timestamp F28;

	/**
	 * 满标时间
	 */
	public Timestamp F29;

	/**
	 * 放款人,参考T7011.F01
	 */
	public int F30;

	/**
	 * 放款时间
	 */
	public Timestamp F31;

	/**
	 * 结清时间
	 */
	public Timestamp F32;

	/**
	 * 流标原因
	 */
	public String F33;

	/**
	 * 格式合同号
	 */
	public String F34;

	/**
	 * 垫付时间
	 */
	public Timestamp F35;

	/**
	 * 垫付开始期数
	 */
	public int F36;

	/**
	 * 每份金额
	 */
	public BigDecimal F37 = BigDecimal.ZERO;

	/**
	 * 垫付机构,0为平台垫付,参考T7029.F01
	 */
	public int F38;

	/**
	 * 是否逾期,S:是(逾期);F:否;YZYQ:严重逾期
	 */
	public T6036_F39 F39;

	/**
	 * 标的等级
	 */
	public T6036_F40 F40;

}
