package com.dimeng.p2p.modules.capital.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.common.enums.RealNameVerfiyStatus;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.common.enums.WithdrawStatus;
import com.dimeng.p2p.modules.capital.user.service.WithdrawManage;
import com.dimeng.p2p.modules.capital.user.service.entity.BankCard;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class WithdrawManageimpl extends AbstractCapitalService implements
		WithdrawManage {

	public WithdrawManageimpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public BankCard[] bankCards()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectAll(connection,
                new ArrayParser<BankCard>()
                {
                    
                    @Override
                    public BankCard[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<BankCard> bankCards = null;
                        while (resultSet.next())
                        {
                            if (bankCards == null)
                            {
                                bankCards = new ArrayList<>();
                            }
                            BankCard bankCard = new BankCard();
                            bankCard.id = resultSet.getInt(1);
                            bankCard.bankId = resultSet.getInt(2);
                            bankCard.cardNumber = resultSet.getString(3);
                            bankCard.bank = resultSet.getString(4);
                            bankCards.add(bankCard);
                        }
                        return bankCards == null ? null : bankCards.toArray(new BankCard[bankCards.size()]);
                    }
                },
                "SELECT T6024.F01 AS F01,T6024.F03 AS F02,T6024.F06 AS F03,T5020.F02 AS F04 FROM S60.T6024 INNER JOIN S50.T5020 ON T6024.F03 = T5020.F01 WHERE T6024.F02 = ? AND T6024.F07 = ?",
                serviceResource.getSession().getAccountId(),
                BankCardStatus.QY);
        }
    }

	@Override
	public BigDecimal availableFunds() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F05 FROM T6023 WHERE F01=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	@Override
	public void withdraw(BigDecimal funds, String withdrawPsd, int cardId)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        BigDecimal min = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS));
        BigDecimal max = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS));
        if (funds.compareTo(min) < 0 || funds.compareTo(max) > 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            int accountId = serviceResource.getSession().getAccountId();
            int count = psdInputCount(connection);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            if (!getVerifyStatus())
            {
                throw new LogicalException("手机未认证、实名未认证或交易密码未设置");
            }
            try{
                boolean aa = false;// 标记交易密码是否正确
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM T6023 WHERE F01=? AND F02=?"))
                {
                    ps.setInt(1, accountId);
                    ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            aa = true;
                        }
                    }
                }
                if (!aa)
                {
                    addInputCount(connection);
                    String errorMsg = null;
                    if (count + 1 >= maxCount)
                    {
                        errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                    }
                    else
                    {
                        StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                        builder.append(maxCount - (count + 1));
                        builder.append("次");
                        errorMsg = builder.toString();
                    }
                    throw new LogicalException(errorMsg);
                }
            }catch (Exception e){
                throw e;
            }
            try
            {
                serviceResource.openTransactions(connection);

                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F01 FROM T6041 WHERE DATEDIFF(?,F10)>0 AND F12=? AND F03=?"))
                {
                    ps.setTimestamp(1, getCurrentTimestamp(connection));
                    ps.setString(2, RepayStatus.WH.name());
                    ps.setInt(3, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                        }
                    }
                }
                String cardNumber = null;
                try (PreparedStatement ps = connection.prepareStatement("SELECT F06 FROM T6024 WHERE F01=? AND F02=?"))
                {
                    ps.setInt(1, cardId);
                    ps.setInt(2, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            cardNumber = resultSet.getString(1);
                        }
                    }
                }
                if (StringHelper.isEmpty(cardNumber))
                {
                    throw new LogicalException("银行卡不存在");
                }
                BigDecimal poundage = null;
                if (funds.compareTo(new BigDecimal(50000)) < 0)
                {
                    poundage = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5));
                }
                else
                {
                    poundage = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20));
                }
                BigDecimal a = funds.add(poundage);// 提现应付金额
                try (PreparedStatement ps = connection.prepareStatement("SELECT F05 FROM T6023 WHERE F01=? FOR UPDATE"))
                {
                    ps.setInt(1, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            if (a.compareTo(resultSet.getBigDecimal(1)) > 0)
                            {
                                throw new LogicalException("账户余额不足");
                            }
                        }
                    }
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO T6034(F02,F03,F04,F05,F06,F07) VALUES(?,?,?,?,?,?)"))
                {
                    ps.setInt(1, accountId);
                    ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    ps.setBigDecimal(3, funds);
                    ps.setInt(4, cardId);
                    ps.setBigDecimal(5, poundage);
                    ps.setString(6, WithdrawStatus.WSH.toString());
                    ps.execute();
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE T6023 SET F04=F04+?,F05=F05-? WHERE F01=?"))
                {
                    ps.setBigDecimal(1, a);
                    ps.setBigDecimal(2, a);
                    ps.setInt(3, accountId);
                    ps.executeUpdate();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

	@Override
	public void addBankCard(String bank, String bankAddr, String branchBank,
			String cardNumber) throws Throwable {
		if (!checkBankCard(cardNumber)) {
			throw new ParameterException("输入银行卡错误");
		}

	}

	@Override
	public String[] getBanks() {
		return null;
	}

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	protected boolean checkBankCard(String cardId) {
		if (cardId.trim().length() < 16) {
			return false;
		}
		char bit = getBankCardCheckCode(cardId
				.substring(0, cardId.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	protected char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	@Override
	public boolean getVerifyStatus() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6023 WHERE F01=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (StringHelper.isEmpty(resultSet.getString(1))) {
							return false;
						}
					}
				}
			}
			String p1 = null;
			int accountId = serviceResource.getSession().getAccountId();
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F08 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						p1 = resultSet.getString(1);
						if (!RealNameVerfiyStatus.YYZ.toString().equals(
								resultSet.getString(2))) {
							return false;
						}
					}
				}
			}
			String p2 = null;
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F04 FROM T6010 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						p2 = resultSet.getString(1);
					}
				}
			}
			if (StringHelper.isEmpty(p1) || StringHelper.isEmpty(p2)) {
				return false;
			}
		}
		return true;
	}

    protected int psdInputCount(Connection connection) throws Throwable {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F09 FROM T6010 WHERE F01=?"))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

    protected void addInputCount(Connection connection) throws Throwable {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE T6010 SET F09=F09+1 WHERE F01=?"))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.executeUpdate();
		}
	}

	public static class WithdrawManageFactory implements
			ServiceFactory<WithdrawManage> {

		@Override
		public WithdrawManage newInstance(ServiceResource serviceResource) {
			return new WithdrawManageimpl(serviceResource);
		}

	}

}
