package com.dimeng.p2p.modules.preservation.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S62.entities.T6272;
import com.dimeng.p2p.S62.enums.T6272_F06;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.modules.preservation.service.AbstractPreservationService;
import com.dimeng.p2p.repeater.preservation.AgreementSaveManage;
import com.dimeng.p2p.repeater.preservation.entity.AgreementSave;
import com.dimeng.p2p.repeater.preservation.entity.Wqxy;
import com.dimeng.p2p.repeater.preservation.query.AgreementSaveQuery;
import com.dimeng.util.StringHelper;

public class AgreementSaveManageImpl extends AbstractPreservationService implements AgreementSaveManage
{
    
    public AgreementSaveManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<AgreementSave> searchAgreementSaveList(AgreementSaveQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT T6272.F01 AS F01,T6272.F03 AS F02,T6272.F04 AS F03,T6272.F05 AS F04,T6272.F06 AS F05,T6272.F07 AS F06,T6110.F02 AS F07,T6110.F06 AS F08,T6110.F10 AS F09,T6141.F02 AS F10,T6161.F04 AS F11,T6272.F02 AS F12,T6272.F08 AS F13 ");
        sql.append(" FROM S62.T6272 LEFT JOIN S61.T6110 ON T6272.F02 = T6110.F01 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6272.F02 = T6141.F01 ");
        sql.append(" LEFT JOIN S61.T6161 ON T6272.F02 = T6161.F01 WHERE T6272.F06 is not null) tmp WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        searchAgreementSaveParameter(sql, query, parameters);
        sql.append(" ORDER BY tmp.F04 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<AgreementSave>()
            {
                @Override
                public AgreementSave[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<AgreementSave> list = null;
                    while (resultSet.next())
                    {
                        AgreementSave agreementSave = new AgreementSave();
                        agreementSave.F01 = resultSet.getInt(1);
                        agreementSave.F02 = resultSet.getInt(12);
                        agreementSave.F03 = resultSet.getInt(2);
                        agreementSave.F04 = resultSet.getString(3);
                        agreementSave.F05 = resultSet.getTimestamp(4);
                        agreementSave.F06 = T6272_F06.parse(resultSet.getString(5));
                        agreementSave.F08 = resultSet.getString(13);
                        agreementSave.userName = resultSet.getString(7);
                        String userType = resultSet.getString(8);// '用户类型',
                        if (T6110_F06.ZRR.name().equals(userType))
                        {
                            agreementSave.name = resultSet.getString(10);
                        }
                        else if (T6110_F06.FZRR.name().equals(userType))
                        {
                            agreementSave.name = resultSet.getString(11);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(agreementSave);
                    }
                    
                    return ((list == null || list.size() == 0) ? null : list.toArray(new AgreementSave[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public AgreementSave[] searchAgreementNotSaveList(AgreementSaveQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT T6272.F01 AS F01,T6272.F03 AS F02,T6272.F04 AS F03,T6272.F05 AS F04,T6272.F06 AS F05,T6272.F07 AS F06,T6110.F02 AS F07,T6110.F06 AS F08,T6110.F10 AS F09,T6141.F02 AS F10,T6161.F04 AS F11,T6272.F02 AS F12,T6272.F08 AS F13 ");
        sql.append(" FROM S62.T6272 LEFT JOIN S61.T6110 ON T6272.F02 = T6110.F01 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6272.F02 = T6141.F01 ");
        sql.append(" LEFT JOIN S61.T6161 ON T6272.F02 = T6161.F01 WHERE T6272.F06 = 'WBQ') tmp WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        searchAgreementSaveParameter(sql, query, parameters);
        sql.append(" ORDER BY tmp.F04 DESC ");
        try (Connection connection = getConnection())
        {
            ArrayList<AgreementSave> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        AgreementSave agreementSave = new AgreementSave();
                        agreementSave.F01 = resultSet.getInt(1);
                        agreementSave.F02 = resultSet.getInt(12);
                        agreementSave.F03 = resultSet.getInt(2);
                        agreementSave.F04 = resultSet.getString(3);
                        agreementSave.F05 = resultSet.getTimestamp(4);
                        agreementSave.F06 = T6272_F06.parse(resultSet.getString(5));
                        agreementSave.F07 = resultSet.getString(6);
                        agreementSave.userName = resultSet.getString(7);
                        String userType = resultSet.getString(8);// '用户类型',
                        if (T6110_F06.ZRR.name().equals(userType))
                        {
                            agreementSave.name = resultSet.getString(10);
                        }
                        else if (T6110_F06.FZRR.name().equals(userType))
                        {
                            agreementSave.name = resultSet.getString(11);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(agreementSave);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new AgreementSave[list.size()]));
        }
    }
    
    private void searchAgreementSaveParameter(StringBuilder sql, AgreementSaveQuery query, ArrayList<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String userName = query.getUserName();
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" AND tmp.F07 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(userName));
            }
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sql.append(" AND (tmp.F10 LIKE ? OR tmp.F11 LIKE ?) ");
                parameters.add(getSQLConnectionProvider().allMatch(name));
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            String agreementId = query.getAgreementId();
            if (!StringHelper.isEmpty(agreementId))
            {
                sql.append(" AND tmp.F03 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(agreementId));
            }
            String agreementNum = query.getAgreementNum();
            if (!StringHelper.isEmpty(agreementNum))
            {
                sql.append(" AND tmp.F13 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(agreementNum));
            }
            Timestamp agreementTimeStart = query.getAgreementTimeStart();
            if (agreementTimeStart != null)
            {
                sql.append(" AND DATE(tmp.F04) >= ? ");
                parameters.add(agreementTimeStart);
            }
            Timestamp agreementTimeEnd = query.getAgreementTimeEnd();
            if (agreementTimeEnd != null)
            {
                sql.append(" AND DATE(tmp.F04) <= ? ");
                parameters.add(agreementTimeEnd);
            }
            String agreementState = query.getAgreementState();
            if (!StringHelper.isEmpty(agreementState))
            {
                sql.append(" AND tmp.F05 = ? ");
                parameters.add(agreementState);
            }
        }
    }
    
    @Override
    public T6272 getAgreementInfo(int saveId)
        throws Throwable
    {
        T6272 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02,F03,F04,F05,F06,F07,F08 FROM S62.T6272 WHERE T6272.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, saveId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6272();
                        record.F02 = resultSet.getInt(1);
                        record.F03 = resultSet.getInt(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getTimestamp(4);
                        record.F06 = T6272_F06.parse(resultSet.getString(5));
                        record.F07 = resultSet.getString(6);
                        record.F08 = resultSet.getString(7);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public Map<String, Object> getValueMap(int userId)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        final Envionment envionment = configureProvider.createEnvionment();
        Map<String, Object> valueMap = new HashMap<String, Object>()
        {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Object get(Object key)
            {
                Object object = super.get(key);
                if (object == null)
                {
                    return envionment == null ? null : envionment.get(key.toString());
                }
                return object;
            }
        };
        Wqxy wqxy = new Wqxy();
        try (Connection connection = getConnection())
        {
            //获取网签协议的最新的版本号
            Dzxy dzxy = getSignContent();
            int versionNum = dzxy.versionNum;
            
            wqxy.xy_no = "wq-" + versionNum + "-" + userId;
            wqxy.t6110 = getUserInfo(connection, userId);
            wqxy.t6141 = selectT6141(connection, userId);
            wqxy.t6161 = selectT6161(connection, userId);
            wqxy.userName = wqxy.t6110.F02;
            if (T6110_F06.ZRR.name().equals(wqxy.t6110.F06.name()))
            {
                wqxy.realName = wqxy.t6141.F02;
                wqxy.IDNum = wqxy.t6141.F06;
            }
            if (T6110_F06.FZRR.name().equals(wqxy.t6110.F06.name()))
            {
                wqxy.realName = wqxy.t6161.F04;
                if ("Y".equals(wqxy.t6161.F20))
                {
                    wqxy.IDNum = wqxy.t6161.F19;
                }
                if ("N".equals(wqxy.t6161.F20))
                {
                    wqxy.IDNum = wqxy.t6161.F03;
                }
            }
        }
        valueMap.put("xy_no", wqxy.xy_no);
        valueMap.put("yf_userName", wqxy.userName == null ? "" : wqxy.userName);
        valueMap.put("yf_realName", wqxy.realName == null ? "" : wqxy.realName);
        valueMap.put("yf_IDNum", wqxy.IDNum == null ? "" : wqxy.IDNum);
        valueMap.put("yf_userType", wqxy.t6110.F06.name());
        return valueMap;
    }
    
    @Override
    public Dzxy getSignContent()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5125.F02,T5125.F03,T5126.F03,T5126.F04 FROM S51.T5126,S51.T5125 WHERE T5126.F01 = T5125.F01 AND T5125.F02 = T5126.F02 AND T5125.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, XyType.WQXY);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Dzxy dzxy = null;
                    if (resultSet.next())
                    {
                        dzxy = new Dzxy();
                        dzxy.versionNum = resultSet.getInt(1);
                        dzxy.xymc = resultSet.getString(2);
                        dzxy.content = resultSet.getString(3);
                    }
                    return dzxy;
                }
            }
        }
    }
    
    @Override
    public boolean updateAgreementContent(int id, String path)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6272 SET F05 = ?, F07 = ? WHERE F01 = ?"))
            {
                pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                pstmt.setString(2, path);
                pstmt.setInt(3, id);
                pstmt.execute();
            }
            return true;
        }
    }
    
    /**
     * 根据用户ID查询用户信息
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private T6110 getUserInfo(Connection connection, int userId)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userId);
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
                }
            }
        }
        return record;
    }
    
    /**
     * 根据用户ID查询用户基本信息
     * 
     * @param connection
     * @param userID
     * @return
     * @throws Throwable
     */
    private T6141 selectT6141(Connection connection, int userID)
        throws Throwable
    {
        T6141 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userID);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6141();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = T6141_F03.parse(resultSet.getString(3));
                    record.F04 = T6141_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getString(7);
                    record.F07 = StringHelper.decode(record.F07);
                    record.F08 = resultSet.getDate(8);
                }
            }
        }
        return record;
    }
    
    private T6161 selectT6161(Connection connection, int userID)
        throws Throwable
    {
        T6161 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userID);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6161();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getBigDecimal(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getInt(10);
                    record.F11 = resultSet.getString(11);
                    record.F12 = resultSet.getString(12);
                    record.F13 = resultSet.getString(13);
                    if (record.F13 != null)
                    {
                        record.F13 = StringHelper.decode(record.F13);
                    }
                    record.F14 = resultSet.getBigDecimal(14);
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getString(16);
                    record.F17 = resultSet.getString(17);
                    record.F18 = resultSet.getString(18);
                    record.F19 = resultSet.getString(19);
                    record.F20 = resultSet.getString(20);
                }
            }
        }
        return record;
    }
    
}
