package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6262;
import com.dimeng.p2p.S62.entities.T6263;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S62.enums.T6271_F08;
import com.dimeng.p2p.S62.enums.T6271_F10;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

@ResourceAnnotation
public class TenderExchangeExecutor extends AbstractOrderExecutor
{
    
    public TenderExchangeExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return TenderExchangeExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 查询订单详情
            T6507 t6507 = selectT6507(connection, orderId);
            if (t6507 == null)
            {
                throw new LogicalException("订单详细信息不存在");
            }
            // 锁定债权申请记录
            T6260 t6260 = selectT6260(connection, t6507.F02);
            if (t6260 == null)
            {
                throw new LogicalException("债权转让信息不存在");
            }
            if (t6260.F07 != T6260_F07.ZRZ)
            {
                throw new LogicalException("债权不是转让中状态,不能转让");
            }
            // 锁定债权信息
            T6251 t6251 = selectT6251(connection, t6260.F02);
            if (t6251 == null)
            {
                throw new LogicalException("债权信息不存在");
            }
            if (t6251.F07.compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new LogicalException("债权价值不能为0");
            }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            boolean zjkt = BooleanParser.parse(configureProvider.getProperty(SystemVariable.ZQZR_SFZJKT));
            if (!zjkt && t6507.F03 == t6251.F04)
            {
                throw new LogicalException("不可购买自己的债权");
            }
            //获取标的担保方ID（购买人不能购买自己担保的标）
            int assureId = selectT6236(connection, t6251.F03);
            if (t6507.F03 == assureId)
            {
                throw new LogicalException("不可购买自己担保的标债权");
            }
            // 锁定还款计划
            T6252[] t6252s = selectAllT6252(connection, t6260.F02);
            // 锁定购买人往来账户
            T6101 srwlzh = selectT6101(connection, t6507.F03, T6101_F03.WLZH, true);
            if (srwlzh == null)
            {
                throw new LogicalException("受让人往来资金账户不存在");
            }
            if (srwlzh.F06.compareTo(t6507.F05) < 0)
            {
                throw new LogicalException("受让人往来资金账户余额不足");
            }
            T6101 zrwlzh = null;
            // 如果购买人和转让人是同一个人
            if (srwlzh.F02 == t6251.F04)
            {
                zrwlzh = srwlzh;
            }
            else
            {
                zrwlzh = selectT6101(connection, t6251.F04, T6101_F03.WLZH, true);
            }
            if (zrwlzh == null)
            {
                throw new LogicalException("转让人往来资金账户不存在");
            }
            {
                // 扣减受让人资金账户
                srwlzh.F06 = srwlzh.F06.subtract(t6507.F05);
                updateT6101(connection, srwlzh.F06, srwlzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = srwlzh.F01;
                t6102.F03 = FeeCode.ZQZR_MR;
                t6102.F04 = zrwlzh.F01;
                t6102.F07 = t6507.F05;
                t6102.F08 = srwlzh.F06;
                t6102.F09 = String.format("购买债权:%s", t6251.F02);
                insertT6102(connection, t6102);
            }
            {
                // 增加转让人资金账户
                zrwlzh.F06 = zrwlzh.F06.add(t6507.F05);
                updateT6101(connection, zrwlzh.F06, zrwlzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = zrwlzh.F01;
                t6102.F03 = FeeCode.ZQZR_MC;
                t6102.F04 = srwlzh.F01;
                t6102.F06 = t6507.F05;
                t6102.F08 = zrwlzh.F06;
                t6102.F09 = String.format("转让债权:%s", t6251.F02);
                insertT6102(connection, t6102);
            }
            BigDecimal fee = t6507.F06;
            if (fee.compareTo(BigDecimal.ZERO) > 0)
            {
                // 扣转让手续费
                if (zrwlzh.F06.compareTo(fee) < 0)
                {
                    throw new LogicalException("转让人往来资金账户余额不足,扣转让手续费失败");
                }
                int pid = getPTID(connection);
                // 锁定平台往来账户
                T6101 ptwlzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
                if (ptwlzh == null)
                {
                    throw new LogicalException("平台往来资金账户不存在");
                }
                {
                    // 扣减转让人资金账户
                    zrwlzh.F06 = zrwlzh.F06.subtract(fee);
                    updateT6101(connection, zrwlzh.F06, zrwlzh.F01);
                    T6102 t6102 = new T6102();
                    t6102.F02 = zrwlzh.F01;
                    t6102.F03 = FeeCode.ZQZR_SXF;
                    t6102.F04 = ptwlzh.F01;
                    t6102.F07 = fee;
                    t6102.F08 = zrwlzh.F06;
                    t6102.F09 = String.format("转让债权手续费:%s", t6251.F02);
                    insertT6102(connection, t6102);
                }
                {
                    // 增加平台资金账户
                    ptwlzh.F06 = ptwlzh.F06.add(fee);
                    updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                    T6102 t6102 = new T6102();
                    t6102.F02 = ptwlzh.F01;
                    t6102.F03 = FeeCode.ZQZR_SXF;
                    t6102.F04 = zrwlzh.F01;
                    t6102.F06 = fee;
                    t6102.F08 = ptwlzh.F06;
                    t6102.F09 = String.format("转让债权手续费:%s", t6251.F02);
                    insertT6102(connection, t6102);
                }
            }
            // 债权剩余期数
            int remainTerm = selectRemainTerm(connection, t6251);
            {
                // 扣减转出人持有债权
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6251 SET F07 = ? WHERE F01 = ?"))
                {
                    pstmt.setBigDecimal(1, BigDecimal.ZERO);
                    pstmt.setInt(2, t6251.F01);
                    pstmt.execute();
                }
                // 生成新的债权编码
                int index = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM S62.T6251 WHERE F11 = ?"))
                {
                    pstmt.setInt(1, t6251.F11);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            index = resultSet.getInt(1) + 1;
                        }
                        else
                        {
                            index = 1;
                        }
                    }
                }
                // 插入受让人债权记录
                T6251 newT6251 = new T6251();
                if (t6251.F02.contains("-"))
                {
                    newT6251.F02 = t6251.F02.substring(0, t6251.F02.length() - 1) + index;
                }
                else
                {
                    newT6251.F02 = String.format("%s-%s", t6251.F02, Integer.toString(index));
                }
                newT6251.F03 = t6251.F03;
                newT6251.F04 = t6507.F03;
                newT6251.F05 = t6507.F05;
                newT6251.F06 = t6507.F04;
                newT6251.F07 = t6507.F04;
                newT6251.F08 = T6251_F08.F;
                newT6251.F09 = getCurrentDate(connection);
                newT6251.F10 = t6251.F10;
                newT6251.F11 = t6251.F11;
                newT6251.F12 = orderId;
                int rightId = insertT6251(connection, newT6251);
                // 修改还款计划
                for (T6252 t6252 : t6252s)
                {
                    t6252.F04 = t6507.F03;
                    t6252.F11 = rightId;
                    updateT6252(connection, t6252);
                }
            }
            // BigDecimal dsbx = getDsbx(t6260.F02);
            {
                // 插入购买记录
                T6262 t6262 = new T6262();
                t6262.F02 = t6260.F01;
                t6262.F03 = t6507.F03;
                t6262.F04 = t6507.F04;
                t6262.F05 = t6507.F05;
                t6262.F06 = fee;
                t6262.F07 = getCurrentTimestamp(connection);
                t6262.F08 = t6262.F04.subtract(t6262.F05);
                t6262.F09 = t6262.F05.subtract(t6262.F04).subtract(fee);
                t6262.F10 = remainTerm;
                insertT6262(connection, t6262);
            }
            {
                // 插入购买人统计数据
                T6263 t6263 = new T6263();
                t6263.F01 = t6507.F03;
                t6263.F02 = t6507.F04.subtract(t6507.F05);
                t6263.F03 = t6507.F04;
                t6263.F04 = t6507.F04.subtract(t6507.F05);
                t6263.F05 = 1;
                t6263.F06 = BigDecimal.ZERO;
                t6263.F07 = BigDecimal.ZERO;
                t6263.F08 = 0;
                
                insertT6263(connection, t6263);
            }
            
            {
                // 插入卖出者统计数据
                T6263 t6263 = new T6263();
                t6263.F01 = t6251.F04;
                t6263.F02 = t6507.F05.subtract(t6507.F04).subtract(fee);
                t6263.F03 = BigDecimal.ZERO;
                t6263.F04 = BigDecimal.ZERO;
                t6263.F05 = 0;
                t6263.F06 = t6507.F04;
                t6263.F07 = t6507.F05.subtract(t6507.F04).subtract(fee);
                t6263.F08 = 1;
                
                insertT6263(connection, t6263);
            }
            {
                // 修改债权信息为非转让状态
                updateT6251(connection, T6251_F08.F, t6251.F01);
                // 修改转让申请为已转出状态
                BigDecimal dsbx = getDsbx(t6260.F02);
                updateT6260(connection, T6260_F07.YJS, t6260.F01, remainTerm, dsbx);
            }
            
            // 如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
            updateT6289(connection, t6251);
            
            //合同保全列表:转让人和受让人
            if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_TRANSFER_CONTRACT)))
            {
                insertT6271s(connection, t6251.F03, zrwlzh.F02, srwlzh.F02, t6260.F01);
            }
            // 托管接口调用
            callFace(connection, orderId, params);
        }
        catch (Exception e)
        {
            // 托管接口调用
            rollbackFace(connection, orderId, params);
            logger.error(e, e);
            throw e;
        }
    }
    
    // 待收本息
    private BigDecimal getDsbx(int zqId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F11 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?"))
            {
                pstmt.setInt(1, zqId);
                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setInt(3, FeeCode.TZ_BJ);
                pstmt.setString(4, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    protected void updateT6252(Connection connection, T6252 t6252)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6252 SET F04 = ?, F11 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, t6252.F04);
            pstmt.setInt(2, t6252.F11);
            pstmt.setInt(3, t6252.F01);
            pstmt.execute();
        }
    }
    
    protected void insertT6263(Connection connection, T6263 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6263"
                + " (F01, F02, F03, F04, F05, F06, F07, F08) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE"
                + " F02 = F02 + ?, F03 = F03 + ?, F04 = F04 + ?, F05 = F05 + ?, F06 = F06 + ?, F07 = F07 + ?, F08 = F08 + ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setBigDecimal(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setInt(5, entity.F05);
            pstmt.setBigDecimal(6, entity.F06);
            pstmt.setBigDecimal(7, entity.F07);
            pstmt.setInt(8, entity.F08);
            pstmt.setBigDecimal(9, entity.F02);
            pstmt.setBigDecimal(10, entity.F03);
            pstmt.setBigDecimal(11, entity.F04);
            pstmt.setInt(12, entity.F05);
            pstmt.setBigDecimal(13, entity.F06);
            pstmt.setBigDecimal(14, entity.F07);
            pstmt.setInt(15, entity.F08);
            pstmt.execute();
        }
    }
    
    private int insertT6251(Connection connection, T6251 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6251 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setString(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setString(7, entity.F08.name());
            pstmt.setDate(8, entity.F09);
            pstmt.setDate(9, entity.F10);
            pstmt.setInt(10, entity.F11);
            pstmt.setInt(11, entity.F12);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    private int insertT6262(Connection connection, T6262 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6262 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setTimestamp(6, entity.F07);
            pstmt.setBigDecimal(7, entity.F08);
            pstmt.setBigDecimal(8, entity.F09);
            pstmt.setInt(9, entity.F10);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    private void updateT6251(Connection connection, T6251_F08 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    private void updateT6260(Connection connection, T6260_F07 F01, int F02, int F09, BigDecimal F10)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6260 SET F07 = ?,F09 = ?,F10 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F09);
            pstmt.setBigDecimal(3, F10);
            pstmt.setInt(4, F02);
            pstmt.execute();
        }
    }
    
    private void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    private T6252[] selectAllT6252(Connection connection, int F11)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F09 = ? AND T6252.F11 = ? FOR UPDATE"))
        {
            pstmt.setString(1, T6252_F09.WH.name());
            pstmt.setInt(2, F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getInt(11);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    private T6251 selectT6251(Connection connection, int F01)
        throws SQLException
    {
        T6251 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6251();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = T6251_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getDate(9);
                    record.F10 = resultSet.getDate(10);
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
    }
    
    private T6507 selectT6507(Connection connection, int F01)
        throws SQLException
    {
        T6507 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6507 WHERE T6507.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6507();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                }
            }
        }
        return record;
    }
    
    private T6260 selectT6260(Connection connection, int F01)
        throws SQLException
    {
        T6260 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6260 WHERE T6260.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6260();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getDate(6);
                    record.F07 = T6260_F07.parse(resultSet.getString(7));
                    record.F08 = resultSet.getBigDecimal(8);
                }
            }
        }
        return record;
    }
    
    private int selectT6236(Connection connection, int F02)
        throws SQLException
    {
        int F03 = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    F03 = resultSet.getInt(1);
                }
            }
        }
        return F03;
    }
    
    private int selectRemainTerm(Connection connection, T6251 t6251)
        throws SQLException
    {
        int remainTerm = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(1) AS F01 FROM S62.T6252 WHERE T6252.F11 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, t6251.F01);
            /**
             * pstmt.setInt(2, FeeCode.TZ_BJ); Bug #19497
             * 【后台-统计管理-债权转让统计表】已转让成功的债权，借款人还款后该列表的“剩余期限”会变动
             */
            pstmt.setInt(2, FeeCode.TZ_LX);
            pstmt.setString(3, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    remainTerm = resultSet.getInt(1);
                }
            }
        }
        return remainTerm;
    }
    
    /**
     * 如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
     * 
     * @param connection
     * @param t6251
     * @throws Throwable
     */
    protected void updateT6289(Connection connection, T6251 t6251)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6289 SET T6289.F09 = ?, T6289.F10 = ? WHERE T6289.F13 = ? AND T6289.F09 = ?"))
        {
            pstmt.setString(1, T6289_F09.YSX.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, t6251.F01);
            pstmt.setString(4, T6289_F09.WFH.name());
            pstmt.execute();
        }
    }
    
    /**
     * 合同保全列表:转让人和受让人
     * @param connection
     * @param bidId 标的Id
     * @param zrUserId 转让人Id
     * @param srUserId 受让人Id
     * @param t6260F01 T6260.F01
     * @throws Throwable
     */
    protected void insertT6271s(Connection connection, int bidId, int zrUserId, int srUserId, int t6260F01)
        throws SQLException
    {
        
        //转让人
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6271 SET F02 = ?, F03 = ?, F08 = ?, F10 = ?, F13 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, zrUserId);
            pstmt.setInt(2, bidId);
            pstmt.setString(3, T6271_F08.ZQZRHT.name());
            pstmt.setString(4, T6271_F10.ZCR.name());
            pstmt.setInt(5, t6260F01);
            pstmt.execute();
        }
        
        //受让人
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6271 SET F02 = ?, F03 = ?, F08 = ?, F10 = ?, F13 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, srUserId);
            pstmt.setInt(2, bidId);
            pstmt.setString(3, T6271_F08.ZQZRHT.name());
            pstmt.setString(4, T6271_F10.SRR.name());
            pstmt.setInt(5, t6260F01);
            pstmt.execute();
        }
    }
}
