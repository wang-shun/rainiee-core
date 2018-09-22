package com.dimeng.p2p.escrow.fuyou.achieve;

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
import com.dimeng.p2p.escrow.fuyou.entity.BankCard;
import com.dimeng.p2p.escrow.fuyou.service.BankCardManage;
import com.dimeng.util.StringHelper;

/**
 * 
 * 银行卡
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
public class BankCardManageImpl extends AbstractEscrowService implements BankCardManage
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
    public BankCard[] getBankCars(String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(status) || acount <= 0)
        {
            throw new ParameterException("参数错误!");
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
                            b.bankID = rs.getInt(3);
                            b.city = rs.getString(4);
                            b.bankKhhName = rs.getString(5);
                            b.bankNumber = rs.getString(6);
                            b.status = rs.getString(7);
                            b.bankname = rs.getString(8);
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
            throw new ParameterException("参数错误!");
        }
        BankCard bankCard = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6114.F01,T6114.F06,T5020.F02,T5019.F06,T5019.F07,T6114.F05,T6114.F12 AS type FROM S61.T6114 "
                    + "LEFT JOIN S50.T5020 ON T5020.F01 = T6114.F03 LEFT JOIN S50.T5019 ON T5019.F01 = T6114.F04 "
                    + "OR T5019.F03 = T6114.F04 WHERE T6114.F01 = ? "))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        bankCard = new BankCard();
                        bankCard.id = resultSet.getInt(1);
                        bankCard.bankNumber = resultSet.getString(2);
                        bankCard.bankname = resultSet.getString(3);
                        bankCard.province = resultSet.getString(4);
                        bankCard.county = resultSet.getString(5);
                        bankCard.bankKhhName = resultSet.getString(6);
                        bankCard.type = resultSet.getInt("type");
                    }
                }
            }
        }
        return bankCard;
    }
    
    @Override
    public BankCard getBankCar(String cardnumber)
        throws Throwable
    {
        if (StringHelper.isEmpty(cardnumber))
        {
            throw new ParameterException("参数错误!");
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
                            b.bankID = rs.getInt(3);
                            b.city = rs.getString(4);
                            b.bankKhhName = rs.getString(5);
                            b.bankNumber = rs.getString(6);
                            b.status = rs.getString(7);
                        }
                        return b;
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F08 FROM S61.T6114 WHERE T6114.F07 = ?",
                StringHelper.encode(cardnumber));
        }
    }
}
