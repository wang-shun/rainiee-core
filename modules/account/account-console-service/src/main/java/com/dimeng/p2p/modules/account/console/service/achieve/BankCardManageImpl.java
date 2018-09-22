package com.dimeng.p2p.modules.account.console.service.achieve;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.modules.account.console.service.BankCardManage;
import com.dimeng.p2p.modules.account.console.service.entity.Bank;
import com.dimeng.p2p.modules.account.console.service.entity.BankCard;
import com.dimeng.p2p.modules.account.console.service.entity.BankDetail;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.modules.account.console.service.query.BankCardQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankCardManageImpl extends AbstractUserService implements
        BankCardManage {
	public BankCardManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class BankCarMnageFactory implements
			ServiceFactory<BankCardManage> {
		@Override
		public BankCardManage newInstance(ServiceResource serviceResource) {
			return new BankCardManageImpl(serviceResource);
		}
	}

	@Override
	public int AddBankCar(BankCardQuery query) throws Throwable {
		if (query == null) {
			throw new ParameterException("参数错误");
		}
		try (Connection connection = getConnection()) {
			String bankNum = query.getBankNumber();
			StringBuilder sb = new StringBuilder();
			sb.append(bankNum.substring(0, 3));
			sb.append("*************");
			sb.append(bankNum.substring(bankNum.length() - 4, bankNum.length()));
			int id = insert(
					connection,
					"INSERT INTO S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F11 = ?, F12 = ? ",
                    getPTID(connection),
					query.getBankId(), query.getCity(), query.getSubbranch(),
					sb.toString(), StringHelper.encode(bankNum),
					T6114_F08.QY.name(),getCurrentTimestamp(connection),
					query.getUserName(),query.getType());
			return id;
		}
	}

	@Override
	public Bank[] getBank() throws Throwable {
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			return selectAll(connection, new ArrayParser<Bank>() {
				@Override
				public Bank[] parse(ResultSet rs) throws SQLException {
					List<Bank> list = new ArrayList<Bank>();
					while (rs.next()) {
						Bank b = new Bank();
						b.id = rs.getInt(1);
						b.name = rs.getString(2);
						list.add(b);
					}
					return list.toArray(new Bank[list.size()]);
				}
			}, "SELECT F01,F02 FROM S50.T5020 WHERE F03 = ?", T5020_F03.QY);
		}
	}

	@Override
    public BankCard[] getBankCars(String status)
        throws Throwable
    {
        if (StringHelper.isEmpty(status))
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
        	int acount = getPTID(connection);
        	
            return selectAll(connection,
                new ArrayParser<BankCard>()
                {
                    @Override
                    public BankCard[] parse(ResultSet rs)
                        throws SQLException
                    {
                        List<BankCard> list = new ArrayList<BankCard>();
                        while (rs.next())
                        {
                            BankCard b = new BankCard();
                            b.id = rs.getInt(1);
                            b.acount = rs.getInt(2);
                            b.BankID = rs.getInt(3);
                            b.City = rs.getString(4);
                            b.BankKhhName = rs.getString(5);
                            b.BankNumber = rs.getString(6);
                            b.status = rs.getString(7);
                            b.Bankname = rs.getString(8);
                            list.add(b);
                        }
                        return list.toArray(new BankCard[list.size()]);
                    }
                },
                "SELECT T6114.F01 AS F01, T6114.F02 AS F02, T6114.F03 AS F03, T6114.F04 AS F04, T6114.F05 AS F05, T6114.F06 AS F06, T6114.F08 AS F07, T5020.F02 AS F08 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ?",
                acount,
                status);
        }
    }

	@Override
    public BankCard getBankCar(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BankCard>()
            {
                @Override
                public BankCard parse(ResultSet rs)
                    throws SQLException
                {
                    BankCard b = null;
                    if (rs.next())
                    {
                        b = new BankCard();
                        b.id = rs.getInt(1);
                        b.acount = rs.getInt(2);
                        b.BankID = rs.getInt(3);
                        b.City = rs.getString(4);
                        b.BankKhhName = rs.getString(5);
                        b.BankNumber = rs.getString(6);
                        b.status = rs.getString(7);
                        b.userName = rs.getString(8);
                        b.type = rs.getInt(9);
                    }
                    return b;
                }
            }, "SELECT F01, F02, F03, F04, F05, F06, F08, F11, F12 FROM S61.T6114 WHERE T6114.F01 = ?", id);
        }
    }

	@Override
    public void delete(int id, String status)
        throws Throwable
    {
        if (StringHelper.isEmpty(status) || id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S61.T6114 SET F08 = ? WHERE F01 = ? AND F02 = ?",
                status,
                id,
                getPTID(connection));
        }
    }

	@Override
    public BankCard getBankCar(String cardnumber)
        throws Throwable
    {
        if (StringHelper.isEmpty(cardnumber))
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<BankCard>()
                {
                    @Override
                    public BankCard parse(ResultSet rs)
                        throws SQLException
                    {
                        BankCard b = null;
                        if (rs.next())
                        {
                            b = new BankCard();
                            b.id = rs.getInt(1);
                            b.acount = rs.getInt(2);
                            b.BankID = rs.getInt(3);
                            b.City = rs.getString(4);
                            b.BankKhhName = rs.getString(5);
                            b.BankNumber = rs.getString(6);
                            b.status = rs.getString(7);
                        }
                        return b;
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F08 FROM S61.T6114 WHERE T6114.F07 = ?",
                StringHelper.encode(cardnumber));
        }
    }

	@Override
    public void update(int id, BankCardQuery query)
        throws Throwable
    {
        if (query == null || id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F08 = ?, F11 = ?, F12 = ? WHERE F01 = ?",
                query.getBankId(),
                query.getCity(),
                query.getSubbranch(),
                query.getStatus(),
                query.getUserName(),
                query.getType(),
                id);
        }
    }

	@Override
    public void updateTY(int id, BankCardQuery query)
        throws Throwable
    {
        if (query == null || id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F08 = ?, F11 = ?, F12 = ? WHERE F01 = ?",
                    getPTID(connection),
                query.getBankId(),
                query.getCity(),
                query.getSubbranch(),
                query.getStatus(),
                query.getUserName(),
                query.getType(),
                id);
        }
        
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
                            bankCard.BankID = resultSet.getInt(2);
                            bankCard.BankNumber = resultSet.getString(3);
                            bankCard.Bankname = resultSet.getString(4);
                            bankCards.add(bankCard);
                        }
                        return bankCards == null ? null : bankCards.toArray(new BankCard[bankCards.size()]);
                    }
                },
                "SELECT F01,F03,F06,F05 FROM S61.T6114 WHERE T6114.F02 = ? AND T6114.F08 = ?",
                getPTID(connection),
                BankCardStatus.QY);
        }
    }
	
	/**
	 * 查询银行卡明细 {@inheritDoc}
	 */
	@Override
	public BankDetail getBankDetail(int banId) throws Throwable {
		if (banId <= 0) {
			throw new ParameterException("参数错误");
		}
		try(Connection connection=getConnection()){
			int acount = getPTID(connection);
		return select(
				connection,
				new ItemParser<BankDetail>() {
					@Override
					public BankDetail parse(ResultSet rs) throws SQLException {
						BankDetail bd = null;
						if (rs.next()) {
							bd = new BankDetail();

							bd.id = rs.getInt(1);
							bd.BankKhhName = rs.getString(2);
							try {
								bd.BankNumber = StringHelper.decode(rs
										.getString(3));
							} catch (Throwable e) {
							}
							bd.status = rs.getString(4);
							bd.bankCode = rs.getString(5);
							bd.Bankname = rs.getString(6);
						}
						return bd;
					}
				},
				"SELECT T6114.F01,T6114.F05,T6114.F07,T6114.F08,T5020.F04,T5020.F02 from S61.T6114 INNER JOIN S50.T5020 ON T6114.F03=T5020.F01 WHERE T6114.F01=? and T6114.F02=?",
				banId, acount);
		}
	}

	/**
     * 获取平台信息
     * @param
     * @return
     * @throws Throwable
     */
    @Override
    public int getPTID() throws Throwable {
        try(Connection connection = getConnection()) {
            return getPTID(connection);
        }
    }
}
