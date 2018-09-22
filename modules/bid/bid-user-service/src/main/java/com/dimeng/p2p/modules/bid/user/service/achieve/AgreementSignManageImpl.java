package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
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
import com.dimeng.p2p.modules.bid.user.service.AgreementSignManage;
import com.dimeng.p2p.modules.bid.user.service.entity.AgreementSign;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.user.service.entity.Wqxy;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.TimestampParser;

public class AgreementSignManageImpl extends AbstractBidManage implements AgreementSignManage
{
    
    public AgreementSignManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    
    @Override
    public AgreementSign[] getAgreementSaveList()
        throws Throwable
    {
        List<AgreementSign> agreementSigns = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6272.F01,T6272.F03,T6272.F04,T6272.F05,T6272.F06,T5126.F02,T5126.F04 FROM S62.T6272 INNER JOIN S51.T5126 ON T6272.F03 = T5126.F02 WHERE T6272.F02 = ? AND T5126.F01 = ? ORDER BY T6272.F05 DESC"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setInt(2, XyType.WQXY);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if (agreementSigns == null)
                        {
                            agreementSigns = new ArrayList<>();
                        }
                        AgreementSign agreementSign = new AgreementSign();
                        agreementSign.agreementId = resultSet.getInt(1);
                        agreementSign.agreementVersionNum = resultSet.getInt(2);
                        agreementSign.agreementSaveID = resultSet.getString(3);
                        agreementSign.agreementSignTime = TimestampParser.parse(resultSet.getString(4));
                        agreementSign.agreementSaveState = T6272_F06.parse(resultSet.getString(5));
                        agreementSign.agreementUpdateTime = TimestampParser.parse(resultSet.getString(7));
                        agreementSigns.add(agreementSign);
                    }
                }
            }
        }
        return agreementSigns == null ? null : agreementSigns.toArray(new AgreementSign[agreementSigns.size()]);
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
    public Wqxy getUserInfo(int versionNum)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        Wqxy wqxy = new Wqxy();
        try (Connection connection = getConnection())
        {
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
        return wqxy;
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
    
    @Override
    public boolean isNetSign()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6272.F01 FROM S62.T6272 WHERE T6272.F02 = ? AND T6272.F03 = (SELECT T5125.F02 FROM S51.T5125 WHERE T5125.F01 = ? ORDER BY T5125.F02 DESC LIMIT 1)  LIMIT 1"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setInt(2, XyType.WQXY);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean isSaveAgreement()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6272.F01 FROM S62.T6272 WHERE T6272.F02 = ? AND T6272.F03 = (SELECT T5125.F02 FROM S51.T5125 WHERE T5125.F01 = ? ORDER BY T5125.F02 DESC LIMIT 1)  AND T6272.F06 is not null LIMIT 1"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setInt(2, XyType.WQXY);
                /*ps.setString(3, T6272_F06.YBQ.name());*/
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    @Override
    public int insertSignAgreement()
        throws Throwable
    {
        //获取网签协议的最新的版本号
        Dzxy dzxy = getSignContent();
        int versionNum = dzxy.versionNum;
        //当前用户ID
        int userId = serviceResource.getSession().getAccountId();
        //协议编号
        String agreementNum = "wq-" + versionNum + "-" + userId;
        String t6272 = "INSERT INTO S62.T6272 SET F02 = ?, F03 = ?, F05 = ?, F08 = ? ";
        try (Connection connection = getConnection())
        {
            int signAgreementId =
                insert(connection, t6272, userId, versionNum, getCurrentTimestamp(connection), agreementNum);
            return signAgreementId;
        }
    }

    @Override
    public Map<String, Object> getValueMap()
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
        int userId = serviceResource.getSession().getAccountId();
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
    public int insertAgreementContent(T6272 t6272)
        throws Throwable
    {
        String sql = "INSERT INTO S62.T6272 SET F02 = ?, F03 = ?, F05 = ?,F06 = ? ,F07 = ? , F08 = ? ";
        int signAgreementId = 0;
        int userId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            signAgreementId =
                insert(connection,
                    sql,
                    userId,
                    t6272.F03,
                    getCurrentTimestamp(connection),
                    t6272.F06,
                    t6272.F07,
                    t6272.F08);
        }
        return signAgreementId;
    }
}
