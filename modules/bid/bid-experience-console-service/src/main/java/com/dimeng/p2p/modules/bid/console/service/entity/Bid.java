/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F29;

/**
 * 标的信息
 * 
 * @author guopeng
 * 
 */
public class Bid extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 借款用户ID,参考T6110.F01
	 */
	public int F02;

	/**
	 * 借款标题
	 */
	public String F03;

	/**
	 * 借款标类型ID,参考T6211.F01
	 */
	public int F04;

	/**
	 * 借款金额
	 */
	public BigDecimal F05 = new BigDecimal(0);

	/**
	 * 年化利率
	 */
	public BigDecimal F06 = new BigDecimal(0);

	/**
	 * 可投金额
	 */
	public BigDecimal F07 = new BigDecimal(0);

	/**
	 * 筹款期限,单位:天
	 */
	public int F08;

	/**
	 * 借款周期,单位:月
	 */
	public int F09;

	/**
	 * 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;
	 */
	public T6230_F10 F10;

	/**
	 * 是否有担保,S:是;F:否;
	 */
	public T6230_F11 F11;

	/**
	 * 担保方案,BXQEDB:本息全额担保;BJQEDB:本金全额担保;
	 */
	public T6230_F12 F12;

	/**
	 * 是否有抵押,S:是;F:否;
	 */
	public T6230_F13 F13;

	/**
	 * 是否实地认证,S:是;F:否;
	 */
	public T6230_F14 F14;

	/**
	 * 是否自动放款,S:是;F:否;
	 */
	public T6230_F15 F15;

	/**
	 * 是否允许流标,S:是;F:否;
	 */
	public T6230_F16 F16;

	/**
	 * 付息方式,ZRY:自然月;GDR:固定日;
	 */
	public T6230_F17 F17;

	/**
	 * 付息日,自然月在满标后设置为满标日+起息日,固定日则必须小于等于28
	 */
	public int F18;

	/**
	 * 起息天数,T+N,默认为0
	 */
	public int F19;

	/**
	 * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:
	 * 已流标;YDF:已垫付;
	 */
	public T6230_F20 F20;

	/**
	 * 封面图片编码
	 */
	public String F21;

	/**
	 * 发布时间,预发布状态有效
	 */
	public Timestamp F22;

	/**
	 * 信用等级,参考T5124.F01
	 */
	public int F23;

	/**
	 * 申请时间
	 */
	public Timestamp F24;

	/**
	 * 标编号
	 */
	public String F25;

	/**
	 * 计息金额
	 */
	public BigDecimal F26 = new BigDecimal(0);

	/**
	 * 还款总期数
	 */
	public int F27;

	/**
	 * 剩余期数
	 */
	public int F28;

	/**
	 * 项目区域位置ID,参考T5119.F01
	 */
	public int F29;

	/**
	 * 资金用途
	 */
	public String F30;

	/**
	 * 审核时间
	 */
	public Timestamp F31;

	/**
	 * 满标时间
	 */
	public Timestamp F32;

	/**
	 * 放款时间
	 */
	public Timestamp F33;

	/**
	 * 结清时间
	 */
	public Timestamp F34;

	/**
	 * 垫付时间
	 */
	public Timestamp F35;

	/**
	 * 流标时间
	 */
	public Timestamp F36;

	/**
	 * 用户登录账号
	 */
	public String F37;

	/**
	 * 用户类型,ZRR:自然人;FZRR:非自然人
	 */
	public T6110_F06 F38;

	/**
	 * 担保方,S:是;F:否;
	 */
	public T6110_F10 F39;

	/**
	 * 标的类型名称
	 */
	public String F40;
	
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	public T6231_F21 F41;
	
	/**
	 * 借款天数
	 */
	public int F42;
    
    /**
     * 是否为推荐标的,S:是;F:否
     */
    public T6231_F29 F43;

	public String productName;

}