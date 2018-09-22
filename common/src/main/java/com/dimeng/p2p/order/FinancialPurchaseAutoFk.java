package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S64.entities.T6410;
import com.dimeng.p2p.S64.entities.T6411;
import com.dimeng.p2p.S64.entities.T6412;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;
import com.dimeng.p2p.S64.enums.T6412_F09;
import com.dimeng.p2p.S65.entities.T6511;
import com.dimeng.util.DateHelper;

@ResourceAnnotation
public class FinancialPurchaseAutoFk extends AbstractOrderExecutor
{
    
    public FinancialPurchaseAutoFk(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return FinancialPurchaseAutoFk.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try {
			// 订单详情
			T6511 t6511 = selectT6511(connection, orderId);
			// 优选理财记录
			T6410 t6410 = selectT6410(connection, t6511.F03);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, t6410.F11);
			Date date = new Date(calendar.getTimeInMillis());
			// 生成还款计划
			hkjh(connection, t6410);
			updateT6410(connection, date, t6410.F01);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
    }
    
    protected BigDecimal selectBigDecimal(Connection connection, int ycId, int userId)
        throws SQLException
    {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07) FROM S64.T6412 WHERE T6412.F02 = ? AND T6412.F04 = ? AND T6412.F05 = ?"))
        {
            pstmt.setInt(1, ycId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, FeeCode.TZ_LX);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    bigDecimal = resultSet.getBigDecimal(1);
                }
            }
        }
        return bigDecimal;
    }
    
    protected T6411[] selectAllT6411_1(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6411> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S64.T6411 WHERE T6411.F02 = ? GROUP BY T6411.F03"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6411 record = new T6411();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = resultSet.getTimestamp(7);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6411[list.size()]));
    }
    
    protected T6511 selectT6511(Connection connection, int F01)
        throws SQLException
    {
        T6511 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S65.T6511 WHERE T6511.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6511();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                }
            }
        }
        return record;
    }
    
    private T6410 selectT6410(Connection connection, int F01)
        throws SQLException
    {
        T6410 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24 FROM S64.T6410 WHERE T6410.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6410();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = T6410_F07.parse(resultSet.getString(7));
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getTimestamp(9);
                    record.F10 = resultSet.getDate(10);
                    record.F11 = resultSet.getInt(11);
                    record.F12 = resultSet.getTimestamp(12);
                    record.F13 = resultSet.getDate(13);
                    record.F14 = T6410_F14.parse(resultSet.getString(14));
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getBigDecimal(16);
                    record.F17 = resultSet.getBigDecimal(17);
                    record.F18 = resultSet.getString(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = resultSet.getTimestamp(20);
                    record.F21 = resultSet.getDate(21);
                    record.F22 = resultSet.getBigDecimal(22);
                    record.F23 = resultSet.getBigDecimal(23);
                    record.F24 = T6410_F24.parse(resultSet.getString(24));
                }
            }
        }
        return record;
    }
    
    private void hkjh(Connection connection, T6410 t6410)
        throws Throwable
    {
        final Date currentDate = getCurrentDate(connection); // 数据库当前日期
        final Date interestDate = new Date(currentDate.getTime());
        final Date endDate = new Date(DateHelper.rollMonth(interestDate.getTime(), t6410.F11));
        T6411[] t6411s = selectAllT6411(connection, t6410.F01);
        switch (t6410.F14)
        {
            case DEBX:
            {
                for (T6411 t6411 : t6411s)
                {
                    insertT6412s(connection, calZRY_DEBX(connection, t6410, t6411, interestDate, endDate));
                }
                break;
            }
            case MYHXDQHB:
            {
                for (T6411 t6411 : t6411s)
                {
                    insertT6412s(connection, calZRY_MYFX(connection, t6410, t6411, interestDate, endDate));
                }
                break;
            }
            case YCXHBFX:
            {
                for (T6411 t6411 : t6411s)
                {
                    insertT6412s(connection, calYCFQ(connection, t6410, t6411, interestDate, endDate));
                }
                break;
            }
            default:
                throw new LogicalException("不支持的还款方式");
        }
    }
    
    protected T6411[] selectAllT6411(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6411> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S64.T6411 WHERE T6411.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6411 record = new T6411();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = resultSet.getTimestamp(7);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6411[list.size()]));
    }
    
    private ArrayList<T6412> calZRY_DEBX(Connection connection, T6410 t6410, T6411 t6411, Date interestDate,
        Date endDate)
        throws Throwable
    {
        ArrayList<T6412> t6412s = new ArrayList<>();
        BigDecimal monthRate =
            t6410.F05.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal remainTotal = t6411.F04;
        BigDecimal monthPayTotal = debx(t6411.F04, monthRate, t6410.F11);
        int ptUserId = getPTID(connection);
        for (int term = 1; term <= t6410.F11; term++)
        {
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), term));
            BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (1 == term)
            {
                updateT6410_1(connection, date, t6410.F01);
            }
            {
                // 利息
                T6412 t6412 = new T6412();
                t6412.F02 = t6410.F01;
                t6412.F03 = ptUserId;
                t6412.F04 = t6411.F03;
                t6412.F05 = FeeCode.TZ_LX;
                t6412.F06 = term;
                if (t6410.F11 == term)
                {
                    t6412.F07 = monthPayTotal.subtract(remainTotal);
                }
                else
                {
                    t6412.F07 = interest;
                }
                t6412.F08 = date;
                t6412.F09 = T6412_F09.WH;
                t6412.F10 = null;
                // t6252.F11 = t6251.F01;
                t6412s.add(t6412);
            }
            {
                // 本金
                T6412 t6412 = new T6412();
                t6412.F02 = t6410.F01;
                t6412.F03 = ptUserId;
                t6412.F04 = t6411.F03;
                t6412.F05 = FeeCode.TZ_BJ;
                t6412.F06 = term;
                if (t6410.F11 == term)
                {
                    t6412.F07 = remainTotal;
                }
                else
                {
                    t6412.F07 = monthPayTotal.subtract(interest);
                }
                remainTotal = remainTotal.subtract(t6412.F07);
                t6412.F08 = date;
                t6412.F09 = T6412_F09.WH;
                t6412.F10 = null;
                t6412s.add(t6412);
            }
        }
        return t6412s;
    }
    
    private ArrayList<T6412> calZRY_MYFX(Connection connection, T6410 t6410, T6411 t6411, Date interestDate,
        Date endDate)
        throws Throwable
    {
        ArrayList<T6412> t6412s = new ArrayList<>();
        BigDecimal monthes = new BigDecimal(12);
        int ptUserId = getPTID(connection);
        for (int term = 1; term <= t6410.F11; term++)
        {
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), term));
            if (1 == term)
            {
                updateT6410_1(connection, date, t6410.F01);
            }
            {
                // 利息
                T6412 t6412 = new T6412();
                t6412.F02 = t6410.F01;
                t6412.F03 = ptUserId;
                t6412.F04 = t6411.F03;
                t6412.F05 = FeeCode.TZ_LX;
                t6412.F06 = term;
                t6412.F07 = t6411.F04.setScale(9, BigDecimal.ROUND_HALF_UP);
                t6412.F07 = t6411.F04.multiply(t6410.F05).divide(monthes, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6412.F08 = date;
                t6412.F09 = T6412_F09.WH;
                t6412.F10 = null;
                // t6412.F11 = t6251.F01;
                t6412s.add(t6412);
            }
            if (term == t6410.F11)
            {
                // 本金
                T6412 t6412 = new T6412();
                t6412.F02 = t6410.F01;
                t6412.F03 = ptUserId;
                t6412.F04 = t6411.F03;
                t6412.F05 = FeeCode.TZ_BJ;
                t6412.F06 = term;
                t6412.F07 = t6411.F04;
                t6412.F08 = date;
                t6412.F09 = T6412_F09.WH;
                t6412.F10 = null;
                // t6412.F11 = t6251.F01;
                t6412s.add(t6412);
            }
        }
        return t6412s;
    }
    
    private ArrayList<T6412> calYCFQ(Connection connection, T6410 t6410, T6411 t6411, Date interestDate, Date endDate)
        throws Throwable
    {
        int ptUserId = getPTID(connection);
        // 更新下个还款日
        updateT6410_1(connection, endDate, t6410.F01);
        ArrayList<T6412> t6412s = new ArrayList<>();
        {
            // 利息
            T6412 t6412 = new T6412();
            t6412.F02 = t6410.F01;
            t6412.F03 = ptUserId;
            t6412.F04 = t6411.F03;
            t6412.F05 = FeeCode.TZ_LX;
            t6412.F06 = 1;
            t6412.F07 = t6411.F04.setScale(9, BigDecimal.ROUND_HALF_UP);
            t6412.F07 =
                t6411.F04.multiply(t6410.F05)
                    .multiply(new BigDecimal(t6410.F11))
                    .divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            t6412.F08 = endDate;
            t6412.F09 = T6412_F09.WH;
            t6412.F10 = null;
            t6412s.add(t6412);
        }
        {
            // 本金
            T6412 t6412 = new T6412();
            t6412.F02 = t6410.F01;
            t6412.F03 = ptUserId;
            t6412.F04 = t6411.F03;
            t6412.F05 = FeeCode.TZ_BJ;
            t6412.F06 = 1;
            t6412.F07 = t6411.F04;
            t6412.F08 = endDate;
            t6412.F09 = T6412_F09.WH;
            t6412.F10 = null;
            t6412s.add(t6412);
        }
        return t6412s;
    }
    
    private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms)
    {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate)
            .multiply(tmp)
            .divide(tmp.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    private void insertT6412s(Connection connection, List<T6412> entities)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S64.T6412 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?"))
        {
            for (T6412 entity : entities)
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setInt(3, entity.F04);
                pstmt.setInt(4, entity.F05);
                pstmt.setInt(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F07);
                pstmt.setDate(7, entity.F08);
                pstmt.setString(8, entity.F09.name());
                pstmt.setTimestamp(9, entity.F10);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    private void updateT6410(Connection connection, Date F01, int F02)
        throws Throwable
    {
        Timestamp timestamp = getCurrentTimestamp(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S64.T6410 SET F04 = ?, F07 = ?, F08 = (UNIX_TIMESTAMP(?) - UNIX_TIMESTAMP(F09)), F12 = ?, F13 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, BigDecimal.ZERO);
            pstmt.setString(2, T6410_F07.YSX.name());
            pstmt.setTimestamp(3,timestamp);
            pstmt.setTimestamp(4,timestamp);
            pstmt.setDate(5, F01);
            pstmt.setInt(6, F02);
            pstmt.execute();
        }
    }
   
    
    protected void updateT6410_1(Connection connection, Date F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S64.T6410 SET F21 = ? WHERE F01 = ?"))
        {
            pstmt.setDate(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
}
