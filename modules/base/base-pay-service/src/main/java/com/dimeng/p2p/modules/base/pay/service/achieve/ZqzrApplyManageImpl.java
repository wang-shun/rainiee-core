package com.dimeng.p2p.modules.base.pay.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.modules.base.pay.service.ZqzrApplyManage;
import com.dimeng.p2p.modules.base.pay.service.entity.ZqzrApplyRecord;
import com.dimeng.p2p.variables.defines.LetterVariable;

public class ZqzrApplyManageImpl extends AbstractBaseManage implements ZqzrApplyManage
{
    
    public ZqzrApplyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void zqzrInvalid()
        throws Throwable
    {
        logger.info("开始执行【债权转让自动下架】任务，开始时间：" + new Date());
        try (Connection connection = getConnection())
        {
            List<ZqzrApplyRecord> recordList = getZrzList(connection);
            try
            {
                serviceResource.openTransactions(connection);
                for (int i = 0; i < recordList.size(); i++)
                {
                    ZqzrApplyRecord record = recordList.get(i);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6260 SET T6260.F07 = ?,T6260.F09 = ? WHERE T6260.F01 = ?"))
                    {
                        pstmt.setString(1, T6260_F07.YXJ.name());
                        pstmt.setInt(2, getT6231F03(connection, record.F01));
                        pstmt.setInt(3, record.zqAplId);
                        pstmt.executeUpdate();
                    }
                    if (record.F01 > 0)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?"))
                        {
                            pstmt.setString(1, T6251_F08.F.name());
                            pstmt.setInt(2, record.F01);
                            pstmt.execute();
                        }
                    }
                    T6110 t6110 = selectT6110(connection, record.F04);
                    ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                    Envionment envionment = configureProvider.createEnvionment();
                    envionment.set("userName", t6110.F02);
                    envionment.set("title", record.F02);
                    String content = configureProvider.format(LetterVariable.ZQ_AUTOMATIC_CANCEL, envionment);
                    sendLetter(connection, record.F04, "自动下架债权", content);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        logger.info("结束执行【债权转让自动下架】任务，结束时间：" + new Date());
    }
    
    /**
     * 获取转让中即将到期的债权
     * @param connection
     * @return
     * @throws Throwable
     */
    private List<ZqzrApplyRecord> getZrzList(Connection connection)
        throws Throwable
    {
        ZqzrApplyRecord record = null;
        List<ZqzrApplyRecord> recordList = new ArrayList<>();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT * FROM ( ");
        sqlStr.append("SELECT T6251.F01 AS F01,T6251.F03 AS F02, T6251.F04 AS F03, ");
        sqlStr.append("(SELECT DATE_SUB(T6231.F06,INTERVAL 3 DAY) FROM S62.T6231 WHERE T6231.F01=T6251.F03) AS F04, ");
        sqlStr.append("T6251.F02 AS F05,DATE(NOW()) AS F06,T6260.F01 AS F07 ");
        sqlStr.append("FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 ");
        sqlStr.append("WHERE T6260.F07 = ?)TMP ");
        sqlStr.append("WHERE TMP.F04 < TMP.F06 ");
        try (PreparedStatement pstmt = connection.prepareStatement(sqlStr.toString()))
        {
            pstmt.setString(1, T6260_F07.ZRZ.name());
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    record = new ZqzrApplyRecord();
                    record.F01 = rs.getInt(1);
                    record.F03 = rs.getInt(2);
                    record.F04 = rs.getInt(3);
                    record.nextRepayDate = rs.getTimestamp(4);
                    record.F02 = rs.getString(5);
                    record.zqAplId = rs.getInt(7);
                    recordList.add(record);
                }
            }
        }
        return recordList;
    }
    
    private int getT6231F03(Connection connection, int t6251F01)
        throws Throwable
    {
        
        String sql = "SELECT T6231.F03 FROM S62.T6231 JOIN S62.T6251 ON T6231.F01=T6251.F03 WHERE T6251.F01=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, t6251F01);
            
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
}
