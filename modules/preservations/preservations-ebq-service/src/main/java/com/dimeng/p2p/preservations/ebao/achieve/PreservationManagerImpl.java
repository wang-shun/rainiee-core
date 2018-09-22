package com.dimeng.p2p.preservations.ebao.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapu.themis.api.common.PersonalIdentifer;
import org.mapu.themis.api.response.preservation.PreservationCreateResponse;

import rop.security.MainError;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.preservations.ebao.cond.EbaoCond;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;
import com.dimeng.p2p.preservations.ebao.util.PreservationTools;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.util.StringHelper;

public class PreservationManagerImpl extends AbstractP2PService implements PreservationManager
{
    
    public PreservationManagerImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 对合同进行保全
     */
    @Override
    public void contractPreservation(int preservationId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                List<EbaoCond> list = new ArrayList<EbaoCond>();
                EbaoCond cond = null;
                Map<Integer, String> map = new HashMap<Integer, String>();
                
                StringBuffer selSql =
                    new StringBuffer(
                        "SELECT T6271.F01, T6271.F02, T6271.F03, T6271.F04, T6271.F05, T6271.F06, T6271.F07, T6271.F08, T6271.F09, T6110.F04 t1, T6110.F05 t2, T6110.F06 t3, T6230.F03 t4 ");
                selSql.append("FROM S62.T6271 T6271 ");
                selSql.append("INNER JOIN S62.T6230 T6230 ON T6271.F03 = T6230.F01 ");
                selSql.append("INNER JOIN S61.T6110 T6110 ON T6271.F02 = T6110.F01 ");
                selSql.append("WHERE T6271.F07 = 'WBQ' AND T6271.F09 IS NOT  NULL ");
                if (preservationId > 0)
                {
                    selSql.append("AND T6271.F01=? ");
                }
                else
                {
                    selSql.append("LIMIT 50 ");
                }
                try (PreparedStatement pstmt = connection.prepareStatement(selSql.toString()))
                {
                    if (preservationId > 0)
                    {
                        pstmt.setInt(1, preservationId);
                    }
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            cond = new EbaoCond();
                            cond.setF01(resultSet.getInt(1));
                            cond.setWithFile(resultSet.getString(9));
                            cond.setWithPreservationTitle("[" + resultSet.getString(3) + "]" + resultSet.getString(13));
                            cond.setWithSourceRegistryId(resultSet.getString(2));
                            cond.setWithContractNumber(resultSet.getString(4));
                            cond.setWithPhoneNo(resultSet.getString(10));
                            cond.setWithUserEmail(resultSet.getString(11));
                            cond.setWithComments(resultSet.getString(13));
                            
                            map.put(resultSet.getInt(2), resultSet.getString(12));//取用户的ID和类型
                            list.add(cond);
                        }
                    }
                }
                
                Map<Integer, PersonalIdentifer> identifers = selectPreservationInfo(connection, map);
                updateContractInfo(connection, list, identifers);
            }
            catch (Exception e)
            {
                logger.error("合同保全失败：", e);
                throw e;
            }
        }
    }
    
    /**
     * 查询需要保全的用户信息
     * @param connection
     * @param list 保全参数
     * @param map 用户基础信息
     * @throws SQLException
     * @throws Throwable
     * @throws InterruptedException
     */
    private Map<Integer, PersonalIdentifer> selectPreservationInfo(Connection connection, Map<Integer, String> map)
        throws SQLException, Throwable, InterruptedException
    {
        final Map<Integer, PersonalIdentifer> identifers = new HashMap<Integer, PersonalIdentifer>();
        if (!map.isEmpty())
        {
            StringBuffer zrr = new StringBuffer();
            StringBuffer fzrr = new StringBuffer();
            for (Integer key : map.keySet())
            {
                if (map.get(key).equals(T6110_F06.ZRR.name()))
                {//组装自然人用户ID列表
                    zrr.append(key).append(",");
                }
                else
                {//组装非自然人用户id列表
                    fzrr.append(key).append(",");
                }
            }
            
            //查询自然人姓名及身份证
            if (!"".equals(zrr.toString()))
            {
                String zrrUserIDs = zrr.substring(0, zrr.length() - 1);
                StringBuilder sql =
                    new StringBuilder(
                        "SELECT T6141.F01, T6141.F02, T6141.F07 FROM S61.T6141 T6141 WHERE T6141.F01 IN (");
                List<Object> params = new ArrayList<Object>();
                sql.append(getBatchId(zrrUserIDs.toString(), params));
                sql.append(")");
                select(connection, new ItemParser<Map<Integer, PersonalIdentifer>>()
                {
                    
                    @Override
                    public Map<Integer, PersonalIdentifer> parse(ResultSet rs)
                        throws SQLException
                    {
                        while (rs.next())
                        {
                            try
                            {
                                PersonalIdentifer identifer =
                                    new PersonalIdentifer(StringHelper.decode(rs.getString(3)), rs.getString(2));
                                
                                identifers.put(rs.getInt(1), identifer);
                            }
                            catch (Throwable e)
                            {
                                logger.error(e, e);
                            }
                        }
                        return null;
                    }
                    
                },
                    sql.toString(),
                    params);
            }
            //查询非自然人法人姓名及身份证
            if (!"".equals(fzrr.toString()))
            {
                String fzrrUserIDs = fzrr.substring(0, fzrr.length() - 1);
                StringBuilder sql =
                    new StringBuilder(
                        "SELECT T6161.F01, T6161.F11, T6161.F13 FROM S61.T6161 T6161 WHERE T6161.F01 IN (");
                List<Object> params = new ArrayList<Object>();
                sql.append(getBatchId(fzrrUserIDs.toString(), params));
                sql.append(")");
                select(connection, new ItemParser<Map<Integer, PersonalIdentifer>>()
                {
                    
                    @Override
                    public Map<Integer, PersonalIdentifer> parse(ResultSet rs)
                        throws SQLException
                    {
                        while (rs.next())
                        {
                            try
                            {
                                PersonalIdentifer identifer =
                                    new PersonalIdentifer(StringHelper.decode(rs.getString(3)), rs.getString(2));
                                
                                identifers.put(rs.getInt(1), identifer);
                            }
                            catch (Throwable e)
                            {
                                logger.error(e, e);
                            }
                        }
                        return null;
                    }
                    
                },
                    sql.toString(),
                    params);
            }
            
        }
        return identifers;
    }
    
    /**
     * 保全信息更新
     * @param connection
     * @param list 保全参数
     * @param map 用户基础信息
     */
    private void updateContractInfo(Connection connection, List<EbaoCond> list,
        Map<Integer, PersonalIdentifer> identifers)
    {
        for (EbaoCond ebaoCond : list)
        {//进行合同保全及查看路径查询
            try
            {
                ebaoCond.setWithIdentifer(identifers.get(Integer.parseInt(ebaoCond.getWithSourceRegistryId())));
                PreservationCreateResponse response = PreservationTools.contractFilePreservation(ebaoCond);//合同保全
                if (response.isSuccess())
                {//保全成功
                    String date =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(response.getPreservationTime()));
                    logger.info("合同保全成功，保全ID:" + response.getPreservationId() + "，保全特征码:" + response.getDocHash()
                        + "，保全时间:" + date);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE  S62.T6271 SET F05=?,F06=?,F07='YBQ' WHERE F01=? "))
                    {
                        pstmt.setString(1, response.getPreservationId().toString());
                        pstmt.setString(2, date);
                        pstmt.setInt(3, ebaoCond.getF01());
                        
                        pstmt.execute();
                    }
                }
                else
                {
                    MainError error = response.getError();
                    logger.error("合同保全失败，失败代码：" + error.getCode() + ",错误消息：" + error.getMessage() + ",解决方案："
                        + error.getSolution());
                }
                //Thread.sleep(1000);
            }
            catch (Exception e)
            {
                logger.error("合同保全失败：", e);
            }
        }
    }
    
    /**
     * 对协议进行保全
     */
    @Override
    public void agreementPreservation(int preservationId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                List<EbaoCond> list = new ArrayList<EbaoCond>();
                EbaoCond cond = null;
                Map<Integer, String> map = new HashMap<Integer, String>();
                
                StringBuffer buffer =
                    new StringBuffer(
                        "SELECT T6272.F01, T6272.F02, T6272.F03, T6272.F04, T6272.F05, T6272.F06, T6272.F07, T6272.F08, T6110.F04 t1, T6110.F05 t2, T6110.F06 t3 ");
                buffer.append("FROM S62.T6272 T6272 ");
                buffer.append("INNER JOIN S61.T6110 T6110 ON T6272.F02 = T6110.F01 ");
                buffer.append("WHERE T6272.F06 = 'WBQ' AND T6272.F07 IS NOT NULL ");
                if (preservationId > 0)
                {
                    buffer.append("AND T6272.F01=? ");
                }
                else
                {
                    buffer.append("LIMIT 50 ");
                }
                
                try (PreparedStatement pstmt = connection.prepareStatement(buffer.toString()))
                {
                    if (preservationId > 0)
                    {
                        pstmt.setInt(1, preservationId);
                    }
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            cond = new EbaoCond();
                            cond.setF01(resultSet.getInt(1));
                            cond.setWithFile(resultSet.getString(7));
                            cond.setWithPreservationTitle("[" + resultSet.getString(8) + "]" + "协议");
                            cond.setWithSourceRegistryId(resultSet.getString(2));
                            cond.setWithContractNumber(resultSet.getString(3));
                            cond.setWithPhoneNo(resultSet.getString(9));
                            cond.setWithUserEmail(resultSet.getString(10));
                            //cond.setWithComments();
                            
                            map.put(resultSet.getInt(2), resultSet.getString(11));//取用户的ID和类型
                            list.add(cond);
                        }
                    }
                }
                
                Map<Integer, PersonalIdentifer> identifers = selectPreservationInfo(connection, map);
                updateAgreementInfo(connection, list, identifers);
            }
            catch (Exception e)
            {
                logger.error("协议保全失败：", e);
                throw e;
            }
        }
    }
    
    /**
     * 合同保全
     * @param connection
     * @param list 保全参数
     * @param map 用户基础信息
     */
    private void updateAgreementInfo(Connection connection, List<EbaoCond> list,
        Map<Integer, PersonalIdentifer> identifers)
    {
        for (EbaoCond ebaoCond : list)
        {//进行合同保全及查看路径查询
            try
            {
                ebaoCond.setWithIdentifer(identifers.get(Integer.parseInt(ebaoCond.getWithSourceRegistryId())));
                PreservationCreateResponse response = PreservationTools.contractFilePreservation(ebaoCond);//协议保全
                if (response.isSuccess())
                {//保全成功
                    String date =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(response.getPreservationTime()));
                    logger.info("协议保全成功，保全ID:" + response.getPreservationId() + "，保全特征码:" + response.getDocHash()
                        + "，保全时间:" + date);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE  S62.T6272 SET F04=?,F05=?,F06='YBQ' WHERE F01=? "))
                    {
                        pstmt.setString(1, response.getPreservationId().toString());
                        pstmt.setString(2, date);
                        pstmt.setInt(3, ebaoCond.getF01());
                        
                        pstmt.execute();
                    }
                }
                else
                {
                    MainError error = response.getError();
                    logger.error("协议保全失败，失败代码：" + error.getCode() + ",错误消息：" + error.getMessage() + ",解决方案："
                        + error.getSolution());
                }
                //Thread.sleep(1000);
            }
            catch (Exception e)
            {
                logger.error("协议保全失败：", e);
            }
        }
    }
    
    @Override
    public String getContractPreservationUrl(int userId, int bidId)
        throws Throwable
    {
        long preservationId = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6271.F05 FROM S62.T6271 T6271 WHERE T6271.F07 = 'YBQ' AND T6271.F02=? AND T6271.F03=? "))
            {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        preservationId = resultSet.getLong(1);
                    }
                }
            }
        }
        
        if (preservationId > 0)
        {
            return PreservationTools.contractFileViewUrl(preservationId).getViewUrl();
        }
        return null;
    }
    
    @Override
    public String getAgreementPreservationUrl(int userId, int agreeCode)
        throws Throwable
    {
        long preservationId = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6272.F04 FROM S62.T6272 T6272 WHERE T6272.F06 = 'YBQ' AND T6272.F02=? AND T6272.F08=? "))
            {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, agreeCode);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        preservationId = resultSet.getLong(1);
                    }
                }
            }
        }
        
        if (preservationId > 0)
        {
            return PreservationTools.contractFileViewUrl(preservationId).getViewUrl();
        }
        return null;
    }
    
    @Override
    public String getContractPreservationUrlById(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6271.F05 FROM S62.T6271 T6271 WHERE T6271.F07 = 'YBQ' AND T6271.F01=? "))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return PreservationTools.contractFileViewUrl(resultSet.getLong(1)).getViewUrl();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAgreementPreservationUrlById(int id, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String selSql = "SELECT T6272.F04 FROM S62.T6272 T6272 WHERE T6272.F06 = 'YBQ' AND T6272.F01=? ";
            if (userId > 0)
            {
                selSql += "AND T6272.F02=? ";
            }
            try (PreparedStatement pstmt = connection.prepareStatement(selSql))
            {
                pstmt.setInt(1, id);
                if (userId > 0)
                {
                    pstmt.setInt(2, userId);
                }
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return PreservationTools.contractFileViewUrl(resultSet.getLong(1)).getViewUrl();
                    }
                }
            }
        }
        
        return null;
    }
    
    @Override
    public String getZqzrContractPreservationUrl(int zqId, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6271.F05 FROM S62.T6271 T6271 WHERE T6271.F07 = 'YBQ' AND T6271.F02=?  AND T6271.F13=? "))
            {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, zqId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return PreservationTools.contractFileViewUrl(resultSet.getLong(1)).getViewUrl();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String getBlzqzrContractPreservationUrl(int zqId, int blzqId, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String selSql =
                "SELECT T6271.F05 FROM S62.T6271 T6271 WHERE T6271.F07 = 'YBQ' AND T6271.F02=?  AND T6271.F12=? ";
            if (zqId > 0)
            {
                selSql += "AND T6271.F11=? ";
            }
            try (PreparedStatement pstmt = connection.prepareStatement(selSql))
            {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, blzqId);
                if (zqId > 0)
                {
                    pstmt.setInt(3, zqId);
                }
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return PreservationTools.contractFileViewUrl(resultSet.getLong(1)).getViewUrl();
                    }
                }
            }
        }
        return null;
    }
    
}
