package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6111_F06;
import com.dimeng.p2p.account.user.service.SpreadManage;
import com.dimeng.p2p.account.user.service.entity.SpreadEntity;
import com.dimeng.p2p.account.user.service.entity.SpreadTotle;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.StringHelper;

public class SpreadManageImpl extends AbstractAccountService implements SpreadManage
{
    
    public SpreadManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class SpreadManageFactory implements ServiceFactory<SpreadManage>
    {
        @Override
        public SpreadManage newInstance(ServiceResource serviceResource)
        {
            return new SpreadManageImpl(serviceResource);
        }
    }
    
    @Override
    public String getMyyqNo()
        throws Throwable
    {
        String sql = "SELECT F02 FROM S61.T6111 WHERE T6111.F01 = ?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet resultSet)
                    throws SQLException
                {
                    String str = "";
                    while (resultSet.next())
                    {
                        str = resultSet.getString(1);
                    }
                    return str;
                }
            }, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public PagingResult<SpreadEntity> getAllReward(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T6110.F02 AS F01, T6110.F09 AS F02 FROM S63.T6311 INNER JOIN S61.T6110 ON T6311.F03 = T6110.F01 WHERE T6311.F02 = ? ORDER BY T6110.F09 DESC ";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SpreadEntity>()
            {
                @Override
                public SpreadEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<SpreadEntity> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        SpreadEntity spreadEntity = new SpreadEntity();
                        spreadEntity.userName = resultSet.getString(1);
                        spreadEntity.zcTime = resultSet.getTimestamp(2);
                        list.add(spreadEntity);
                    }
                    return list == null ? null : list.toArray(new SpreadEntity[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public SpreadTotle search()
        throws Throwable
    {
        SpreadTotle st = new SpreadTotle();
        try (Connection connection = getConnection())
        {
            int accountId = serviceResource.getSession().getAccountId();
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02, F04, F05 FROM S63.T6310 WHERE T6310.F01 = ?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        st.yqCount = resultSet.getInt(1);
                        st.rewardCxtg = resultSet.getBigDecimal(2);
                        st.rewardYxtg = resultSet.getBigDecimal(3);
                    }
                }
            }
        }
        st.rewardTotle = st.rewardCxtg.add(st.rewardYxtg);
        return st;
    }
    
    @Override
    public void updateYqm(String code, int userID)
        throws Throwable
    {
        String newYqm = code;
        boolean isPhoneCode = false;
        try (Connection connection = getConnection())
        {
            try
            {
                
                if (!StringHelper.isEmpty(code) && code.length() == 11)
                { // 输入的邀请码是手机号
                    String selectT6110 = "SELECT F01,F06 FROM S61.T6110 WHERE F04 = ? LIMIT 1";
                    try (PreparedStatement ps = connection.prepareStatement(selectT6110))
                    {
                        ps.setString(1, code);
                        try (ResultSet rs = ps.executeQuery())
                        {
                            if (rs.next())
                            {
                                userID = rs.getInt(1);
                                isPhoneCode = true;
                            }
                        }
                    }
                    if (userID > 0)
                    {
                        String _selectT6111 = "SELECT F02 FROM S61.T6111 WHERE F01 = ? LIMIT 1";
                        try (PreparedStatement ps = connection.prepareStatement(_selectT6111))
                        {
                            ps.setInt(1, userID);
                            try (ResultSet rs = ps.executeQuery())
                            {
                                if (rs.next())
                                {
                                    String currCode = rs.getString(1);
                                    if (!StringHelper.isEmpty(currCode))
                                    {
                                        newYqm = currCode;
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    String selectT6110 = "SELECT F01 FROM S61.T6111 WHERE F02 = ? LIMIT 1";
                    try (PreparedStatement ps = connection.prepareStatement(selectT6110))
                    {
                        ps.setString(1, code);
                        try (ResultSet rs = ps.executeQuery())
                        {
                            if (rs.next())
                            {
                                userID = rs.getInt(1);
                            }
                        }
                    }
                }
                
                if (userID < 0)
                {
                    return;
                }
                serviceResource.openTransactions(connection);
                String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
                execute(connection, t6311, userID, serviceResource.getSession().getAccountId());
                execute(connection, "UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?", userID);
                
                String selectT6111 = "UPDATE S61.T6111 SET F03 = ?,F06 = ?,F07 = ? WHERE F01 = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(selectT6111))
                {
                    pstmt.setString(1, newYqm);
                    pstmt.setString(2, isPhoneCode ? T6111_F06.S.getChineseName() : T6111_F06.F.getChineseName());
                    pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    pstmt.setInt(4, serviceResource.getSession().getAccountId());
                    pstmt.execute();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error("SpreadManageImpl.updateYqm() error", e);
                throw e;
            }
        }
    }
    
    @Override
    public int checkExistsYqm(String code, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (!StringHelper.isEmpty(code))
            {
                T6110 t6110 = getT6110(connection);
                
                if (t6110.F06 == T6110_F06.FZRR)
                {
                    return -5;
                }
                // 校验是否为被推广人的推广码
                String T6111sql =
                    "SELECT T6111.F02,T6110.F04 FROM S61.T6111 INNER JOIN S61.T6110 ON T6111.F01 = T6110.F01 WHERE T6111.F01 = ?  LIMIT 1";
                try (PreparedStatement pstmt = connection.prepareStatement(T6111sql))
                {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            String yqCode = rs.getString(1);
                            String yqPhone = rs.getString(2);
                            try (PreparedStatement pstmt2 =
                                connection.prepareStatement("SELECT T6111.F02,T6110.F04 FROM S61.T6111 INNER JOIN S61.T6110 ON T6111.F01 = T6110.F01 WHERE T6111.F03 = ? OR T6111.F04= ? "))
                            {
                                pstmt2.setString(1, yqCode);
                                pstmt2.setString(2, yqPhone);
                                try (ResultSet rst = pstmt2.executeQuery())
                                {
                                    while (rst.next())
                                    {
                                        if (code.equals(rst.getString(1)) || code.equals(rst.getString(2)))
                                        {
                                            return -4;
                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                }
                
                String selectT6111 =
                    "SELECT T6110.F06, T6110.F01 FROM S61.T6111 INNER JOIN S61.T6110 ON T6111.F01 = T6110.F01 WHERE T6111.F02 = ? OR T6110.F04= ? LIMIT 1";
                try (PreparedStatement pstmt = connection.prepareStatement(selectT6111))
                {
                    pstmt.setString(1, code);
                    pstmt.setString(2, code);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            String userType = rs.getString(1);
                            int uId = rs.getInt(2);
                            if (uId == userId)
                            {
                                return -2;
                            }
                            if (!T6110_F06.ZRR.name().equals(userType))
                            {
                                return -3;
                            }
                            return 0;
                        }
                    }
                }
            }
            return -1;
        }
    }
    
    @Override
    public T6111_F06 getT6111_F06()
        throws Throwable
    {
        
        String sql = "SELECT F06 FROM S61.T6111 WHERE T6111.F01 = ?";
        try (Connection connection = getConnection())
        {
            if (StringHelper.isEmpty(getExistsYqm(connection)))
            { // 是否有邀请码
                return null;
            }
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        T6111_F06 f06 = T6111_F06.parse(resultSet.getString(1));
                        return f06;
                    }
                }
            }
        }
        return null;
    }
    
    private String getEmpNo(Connection connection)
        throws Throwable
    {
        String sql = "SELECT F14 FROM S61.T6110 WHERE T6110.F01 = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public String getMyNewYqm()
        throws Throwable
    {
        T6111_F06 t6111_f06 = getT6111_F06();
        final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        String sql = "SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ?";
        try (Connection connection = getConnection())
        {
            if (t6111_f06 == null)
            {
                if (is_business)
                {
                    String empNo = getEmpNo(connection);
                    if (!StringHelper.isEmpty(empNo))
                    {
                        return empNo;
                    }
                }
                return null;
            }
            String yqm = getExistsYqm(connection);
            if (StringHelper.isEmpty(yqm))
            {
                return null;
            }
            int userID = getUserIDByYqm(connection, yqm);
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, userID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    // 根据邀请码得到推广人的ID
    private int getUserIDByYqm(Connection connection, String yqm)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S61.T6111 WHERE T6111.F02 = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, yqm);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return -1;
    }
    
    // 是否有填邀请码
    private String getExistsYqm(Connection connection)
        throws Throwable
    {
        String sql = "SELECT F03 FROM S61.T6111 WHERE T6111.F01 = ?";
        String yqm = "";
        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    yqm = resultSet.getString(1);
                }
            }
        }
        return yqm;
    }
    
    private T6110 getT6110(Connection connection)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15 FROM S61.T6110 WHERE T6110.F01 = ?"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6110();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = T6110_F06.parse(resultSet.getString(6));
                    record.F07 = T6110_F07.parse(resultSet.getString(7));
                    record.F08 = T6110_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getTimestamp(9);
                    record.F10 = T6110_F10.parse(resultSet.getString(10));
                    record.F15 = resultSet.getTimestamp(11);
                }
            }
        }
        return record;
    }
    
}
