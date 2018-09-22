package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S51.enums.T5129_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F33;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.bid.front.service.BusinessManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

public class BusinessManageImpl extends AbstractBidManage implements BusinessManage
{
    
    public BusinessManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int add(T6230 entity, T6231 t6231, T6236 t6236)
        throws Throwable
    {
        int rtn = 0;
        try (Connection connection = getConnection())
        {
            if (!isNetSign(serviceResource.getSession().getAccountId(), connection))
            {
                throw new LogicalException("您还未网签，不能申请借款");
            }
            try
            {
                serviceResource.openTransactions(connection);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6230 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?,F11 = ?,  F15 = ?, F16 = ?, F17 = ?, F18 = ?, F19 = ?, F20 = ?, F21 = ?,F23 = ?, F24 = ?, F25 = ? ,F26=?, F33=?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, serviceResource.getSession().getAccountId());
                    pstmt.setString(2, entity.F03);
                    pstmt.setInt(3, entity.F04);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setBigDecimal(5, entity.F06.divide(new BigDecimal(100)));
                    pstmt.setBigDecimal(6, entity.F05);
                    pstmt.setInt(7, entity.F08);
                    pstmt.setInt(8, entity.F09);
                    pstmt.setString(9, entity.F10.name());
                    pstmt.setString(10, entity.F11 == null ? T6230_F11.F.name() : entity.F11.name());
                    pstmt.setString(11, T6230_F15.F.name());
                    pstmt.setString(12, T6230_F16.F.name());
                    /*
                     * entity.F17 = EnumParser.parse(T6230_F17.class,
                     * configureProvider.getProperty(SystemVariable.FXFS));
                     */
                    /* entity.F17 = T6230_F17.ZRY; */
                    pstmt.setString(13, entity.F17.name());
                    int gdr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.GDR));
                    if (entity.F18 <= 0 || entity.F18 > 28)
                    {
                        entity.F18 = gdr;
                    }
                    int qxr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.QXTS));
                    
                    if (entity.F17 == T6230_F17.GDR)
                    {
                        pstmt.setInt(14, entity.F18);
                    }
                    else if (entity.F17 == T6230_F17.ZRY)
                    {
                        pstmt.setInt(14, qxr);
                    }
                    pstmt.setInt(15, entity.F19);
                    pstmt.setString(16, T6230_F20.SQZ.name());
                    pstmt.setString(17, entity.F21);
                    pstmt.setInt(18, entity.F23);
                    pstmt.setTimestamp(19, getCurrentTimestamp(connection));
                    pstmt.setString(20, getCrid(connection, entity));
                    pstmt.setBigDecimal(21, entity.F05);
                    pstmt.setString(22, T6230_F33.S.name());
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                    {
                        if (resultSet.next())
                        {
                            t6231.F01 = resultSet.getInt(1);
                            t6231.F02 = entity.F09;
                            t6231.F03 = entity.F09;
                            t6231.F04 = entity.F06.divide(new BigDecimal(100 * 12), 9, BigDecimal.ROUND_HALF_UP);
                            t6231.F05 =
                                entity.F06.divide(new BigDecimal(
                                    100 * IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                                    9,
                                    BigDecimal.ROUND_HALF_UP);
                            t6231.F26 = entity.F05;
                            addT6231(t6231, connection);
                            rtn = t6231.F01;
                        }
                    }
                    if (t6236 != null)
                    {
                        try (PreparedStatement ps =
                            connection.prepareStatement("INSERT INTO S62.T6236 SET F02 = ?, F03 = ?, F04 = ?, F05 = (SELECT F02 FROM S61.T6180 WHERE F01 = ?)",
                                PreparedStatement.RETURN_GENERATED_KEYS))
                        {
                            ps.setInt(1, rtn);
                            ps.setInt(2, t6236.F03);
                            ps.setString(3, t6236.F04.name());
                            ps.setInt(4, t6236.F03);
                            ps.execute();
                        }
                    }
                }
                serviceResource.commit(connection);
                return rtn;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    // 生成标的编号
    private String getCrid(Connection connection, T6230 entity)
        throws Throwable
    {
        // 序号
        String serNo = "";
        try (PreparedStatement pstmt =
            connection.prepareStatement(" SELECT MAX(F04) FROM S51.T5129 WHERE T5129.F02 = CURDATE() AND T5129.F03 = ? FOR UPDATE"))
        {
            pstmt.setString(1, T5129_F03.BDBH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    serNo = String.format("%04d", resultSet.getInt(1) + 1);
                    try (PreparedStatement inpstmt =
                        connection.prepareStatement(" INSERT INTO S51.T5129 (F02, F03, F04) VALUES (CURDATE(),?, ?)ON DUPLICATE KEY UPDATE F04 = F04 + 1"))
                    {
                        inpstmt.setString(1, T5129_F03.BDBH.name());
                        inpstmt.setInt(2, resultSet.getInt(1) + 1);
                        inpstmt.execute();
                    }
                }
            }
        }
        // 获取当前时间
        String nowDate = DateParser.format(getCurrentDate(connection), "yyyyMMdd");
        serNo = (entity.F11 == T6230_F11.S ? "D" : "X") + nowDate + serNo;
        return serNo;
        
    }
    
    /**
     * 插入扩展信息
     * 
     * @param entity
     * @throws Throwable
     */
    private void addT6231(T6231 entity, Connection connection)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6231 (F01, F02,F03,F04, F05, F07, F08 , F09 , F16 , F21 , F22, F25, F26, F31, F32, F35) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F04 = VALUES(F04), F05 = VALUES(F05), F07 = VALUES(F07), F08 = VALUES(F08), F09 = VALUES(F09), F21 = VALUES(F21) , F22 = VALUES(F22), F25 = VALUES(F25), F26 = VALUES(F26)"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F07);
            pstmt.setString(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, entity.F16);
            if (entity.F21 == null)
            {
                pstmt.setString(10, T6231_F21.F.name());
            }
            else
            {
                pstmt.setString(10, entity.F21.name());
            }
            pstmt.setInt(11, entity.F22);
            pstmt.setBigDecimal(12, BigDecimal.ZERO);
            pstmt.setBigDecimal(13, entity.F26);
            pstmt.setString(14, (String)getEmpInfo(userId, connection).get("empNum"));
            pstmt.setString(15, (String)getEmpInfo(userId, connection).get("empStatus"));
            pstmt.setString(16, entity.F35.name());
            pstmt.execute();
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        BigDecimal cjfwfl = BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.SUCCESS_BMONEY_RATE));
        BigDecimal tzfwfl = BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.SUCCESS_TZ_RATE));
        BigDecimal yqfxfl = BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.YU_QI_FAXI_RATE));
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6238 (F01, F02, F03, F04) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04)"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setBigDecimal(2, cjfwfl);
            pstmt.setBigDecimal(3, tzfwfl);
            pstmt.setBigDecimal(4, yqfxfl);
            pstmt.execute();
        }
    }
    
    @Override
    public void submit(int id)
        throws Throwable
    {
        String sql = "UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, T6230_F20.DSH, id);
        }
    }
    
}
