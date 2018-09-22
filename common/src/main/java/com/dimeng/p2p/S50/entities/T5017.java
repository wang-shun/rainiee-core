package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5017_F05;
import com.dimeng.p2p.common.enums.TermType;

/**
 * 协议条款
 */
public class T5017 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 协议类型,ZCXY:注册协议;TFJKXY:三方借款协议;FFJKXY:
	 * 四方借款协议;ZQZRXY:债权转让协议;GYJZXY:公益捐助协议;
	 * GRXXCQSQTK:个人信息采集授权条款;QYXXCJSQTK:企业信息采集授权条款;FXTSH:风险提示函
	 */
	public TermType F01;

	/**
	 * 协议内容
	 */
	public String F02;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F03;

	/**
	 * 最后修改时间
	 */
	public Timestamp F04;

	/**
	 * 状态
	 */
	public T5017_F05 F05;

}
