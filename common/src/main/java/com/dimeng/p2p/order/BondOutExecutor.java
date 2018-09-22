package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6513;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;

@ResourceAnnotation
public class BondOutExecutor extends AbstractOrderExecutor {

	public BondOutExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return BondOutExecutor.class;
	}

	/**
	 * 风险保证金转出
	 *
	 * @param connection connection
	 * @param orderId    orderId
	 * @param params     params
	 * @throws Throwable
	 */
	@Override
	protected void doConfirm(SQLConnection connection, int orderId,
							 Map<String, String> params) throws Throwable {
		try {
			// 查询订单
			T6513 t6513 = selectT6513(connection, orderId);
			// 用户往来账户
			T6101 wlAcount = selectT6101(connection, t6513.F02, T6101_F03.WLZH, true);
			// 用户风险备用金账户
			T6101 fxAcount = selectT6101(connection, t6513.F02, T6101_F03.FXBZJZH, true);
			if (fxAcount == null) {
				throw new LogicalException("用户风险备用金账户不存在");
			}
			if (fxAcount.F06.compareTo(t6513.F03) < 0) {
				throw new LogicalException("用户风险备用金账户余额不足");
			}
			// 用户风险保证金账户
			T6101 sdAcount = selectT6101(connection, t6513.F02, T6101_F03.FXBZJZH, true);
			if (sdAcount == null) {
				throw new LogicalException("用户锁定账户不存在");
			}

			//往来账户加上从风险备用金账户转出的金额
			wlAcount.F06 = wlAcount.F06.add(t6513.F03);
			updateT6101(connection, wlAcount.F06, wlAcount.F01);
			//风险备用金账户减去往来账户转入的金额
			sdAcount.F06 = sdAcount.F06.subtract(t6513.F03);
			updateT6101(connection, sdAcount.F06, sdAcount.F01);

			//将交易记录插入资金流水表中，支出和收入各一条
			T6102 t6102 = new T6102();
			t6102.F02 = wlAcount.F01;
			t6102.F03 = FeeCode.FXBZJ_ZC;
			t6102.F04 = sdAcount.F01;
			t6102.F06 = t6513.F03;
			t6102.F08 = wlAcount.F06;
			t6102.F09 = "从风险保证金充值转入";
			insertT6102(connection, t6102);

			fxAcount.F06 = fxAcount.F06.subtract(t6513.F03);
			t6102 = new T6102();
			t6102.F02 = fxAcount.F01;
			t6102.F03 = FeeCode.FXBZJ_ZC;
			t6102.F04 = sdAcount.F01;
			t6102.F07 = t6513.F03;
			t6102.F08 = fxAcount.F06;
			t6102.F09 = "从风险保证金充值转出";
			insertT6102(connection, t6102);

			// 发站内信
			ConfigureProvider configureProvider = resourceProvider
					.getResource(ConfigureProvider.class);
			T6110 t6110 = selectT6110(connection, t6513.F02);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("amount", Formater.formatAmount(t6513.F03));
			String content = configureProvider.format(LetterVariable.FXBZJTX,
					envionment);
			sendLetter(connection, t6513.F02, "风险保证金提现成功", content);
			String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
			if ("1".equals(isUseYtx)) {
				SMSUtils smsUtils = new SMSUtils(configureProvider);
				int type = smsUtils.getTempleId(MsgVariavle.FXBZJTX.getDescription());
				sendMsg(connection, t6110.F04, envionment.get("amount"), type);
			} else {
				String msgContent = configureProvider.format(MsgVariavle.FXBZJTX, envionment);
				sendMsg(connection, t6110.F04, msgContent);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	private void updateT6101(Connection connection, BigDecimal F01, int F02)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private T6513 selectT6513(Connection connection, int F01)
			throws SQLException {
		T6513 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03 FROM S65.T6513 WHERE T6513.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6513();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getBigDecimal(3);
				}
			}
		}
		return record;
	}
}