package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.Bank;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.account.user.service.entity.BankDetail;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

public class BankCardManageImpl extends AbstractAccountService implements BankCardManage
{
    public BankCardManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class BankCarMnageFactory implements ServiceFactory<BankCardManage>
    {
        @Override
        public BankCardManage newInstance(ServiceResource serviceResource)
        {
            return new BankCardManageImpl(serviceResource);
        }
    }
    
    @Override
    public int AddBankCar(BankCardQuery query)
        throws Throwable
    {
        if (query == null)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            String bankNum = query.getBankNumber();
            StringBuilder sb = new StringBuilder();
            sb.append(bankNum.substring(0, 3));
            sb.append("*************");
            sb.append(bankNum.substring(bankNum.length() - 4, bankNum.length()));
            int id =
                insert(connection,
                    "INSERT INTO S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F11 = ?, F12 = ? ",
                    serviceResource.getSession().getAccountId(),
                    query.getBankId(),
                    query.getCity(),
                    query.getSubbranch(),
                    sb.toString(),
                    StringHelper.encode(bankNum),
                    T6114_F08.QY.name(),
                    getCurrentTimestamp(connection),
                    query.getUserName(),
                    query.getType());
            return id;
        }
    }
    
    @Override
    public Bank[] getBank()
        throws Throwable
    {
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return selectAll(connection, new ArrayParser<Bank>()
            {
                @Override
                public Bank[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<Bank> list = new ArrayList<Bank>();
                    while (rs.next())
                    {
                        Bank b = new Bank();
                        b.id = rs.getInt(1);
                        b.name = rs.getString(2);
                        b.code = rs.getString(3);
                        list.add(b);
                    }
                    return list.toArray(new Bank[list.size()]);
                }
            }, "SELECT F01,F02,F04 FROM S50.T5020 WHERE F03 = ?", T5020_F03.QY);
        }
    }
    
    @Override
    public BankCard[] getBankCars(String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(status) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
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
                            b.jbRequestNo = rs.getString(9);
                            b.bindId = rs.getString(10);
                            list.add(b);
                        }
                        return list.toArray(new BankCard[list.size()]);
                    }
                },
                "SELECT T6114.F01 AS F01, T6114.F02 AS F02, T6114.F03 AS F03, T6114.F04 AS F04, T6114.F05 AS F05, T6114.F06 AS F06, T6114.F08 AS F07, T5020.F02 AS F08, T6114.F15 AS F15, T6114.F16 AS F16 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ?",
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
                            b.userName = rs.getString(8);
                            b.type = rs.getInt(9);
                        }
                        return b;
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F08, F11, F12 FROM S61.T6114 WHERE T6114.F01 = ? AND T6114.F02 = ?",
                id,
                serviceResource.getSession().getAccountId());
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
                serviceResource.getSession().getAccountId());
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
                "UPDATE S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F08 = ?, F11 = ?, F12 = ? WHERE F01 = ? AND F02 = ?",
                query.getBankId(),
                query.getCity(),
                query.getSubbranch(),
                query.getStatus(),
                query.getUserName(),
                query.getType(),
                id,
                serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public void updateTY(int id, BankCardQuery query, int userId)
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
                userId,
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
                serviceResource.getSession().getAccountId(),
                BankCardStatus.QY);
        }
    }
    
    /**
     * 查询银行卡明细 {@inheritDoc}
     */
    @Override
    public BankDetail getBankDetail(int banId)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (banId <= 0 || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<BankDetail>()
                {
                    @Override
                    public BankDetail parse(ResultSet rs)
                        throws SQLException
                    {
                        BankDetail bd = null;
                        if (rs.next())
                        {
                            bd = new BankDetail();
                            
                            bd.id = rs.getInt(1);
                            bd.BankKhhName = rs.getString(2);
                            try
                            {
                                bd.BankNumber = StringHelper.decode(rs.getString(3));
                            }
                            catch (Throwable e)
                            {
                            }
                            bd.status = rs.getString(4);
                            bd.bankCode = rs.getString(5);
                            bd.Bankname = rs.getString(6);
                        }
                        return bd;
                    }
                },
                "SELECT T6114.F01,T6114.F05,T6114.F07,T6114.F08,T5020.F04,T5020.F02 from S61.T6114 INNER JOIN S50.T5020 ON T6114.F03=T5020.F01 WHERE T6114.F01=? and T6114.F02=?",
                banId,
                acount);
        }
    }

	@Override
	public Bank[] getQuickBank() throws Throwable {
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return selectAll(connection, new ArrayParser<Bank>()
            {
                @Override
                public Bank[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<Bank> list = new ArrayList<Bank>();
                    Bank b = null;
                    while (rs.next())
                    {
                        b = new Bank();
                        b.id = rs.getInt(1);
                        b.name = rs.getString(2);
                        list.add(b);
                    }
                    return list.toArray(new Bank[list.size()]);
                }
            }, "SELECT F01,F02 FROM S50.T5020 WHERE F03 = ? AND F06 = 1", T5020_F03.QY);
        }
    }

	@Override
	public T6114 selectT6114() throws Throwable {
        T6114 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F11 FROM S61.T6114 WHERE T6114.F02 = ? AND F08='QY' LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6114();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = StringHelper.decode(resultSet.getString(7));
                        record.F08 = T6114_F08.parse(resultSet.getString(8));
                    }
                }
            }
        }
        return record;
    }

	@Override
	public boolean isBindleBanked() throws Throwable {
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(
                "SELECT T6114.F01 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ? LIMIT 1"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setString(2, T6114_F08.QY.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }

	@Override
	public T6141 selectT6141() throws Throwable {
        T6141 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F07 FROM S61.T6141 WHERE T6141.F01 = ?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6141();
                        record.F02 = resultSet.getString(1);
                        record.F07 = resultSet.getString(2);
                    }
                }
            }
        }
        return record;
    }

	@Override
	public BankCard getBankCard(int accountId) throws Throwable {
        BankCard bankCard = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
					connection.prepareStatement(
							"SELECT T6114.F01,T6114.F03, T6114.F07, T6114.F16 FROM S61.T6114 WHERE T6114.F02 = ? AND T6114.F08 = ? ORDER BY T6114.F09 DESC LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setString(2, T6114_F08.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        bankCard = new BankCard();
                        bankCard.id = resultSet.getInt(1);
                        bankCard.BankID = resultSet.getInt(2);
                        bankCard.BankNumber = resultSet.getString(3);
                        bankCard.rzzt = resultSet.getString(4);
                    }
                }
            }
        }
        return bankCard;
    }
    
}
