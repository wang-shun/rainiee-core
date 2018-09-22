package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6118_F10;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6141_F09;
import com.dimeng.p2p.S61.enums.T6198_F04;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestionAnswer;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

public class SafetyManageImpl extends AbstractAccountService implements SafetyManage
{
    public SafetyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class SafetyManageFactory implements ServiceFactory<SafetyManage>
    {
        @Override
        public SafetyManage newInstance(ServiceResource serviceResource)
        {
            return new SafetyManageImpl(serviceResource);
        }
    }
    
    @Override
    public Safety get()
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        try (final Connection connection = getConnection())
        {
            Safety safety =
                select(connection,
                    new ItemParser<Safety>()
                    {
                        @Override
                        public Safety parse(ResultSet rs)
                            throws SQLException
                        {
                            Safety s = new Safety();
                            if (rs.next())
                            {
                                s.username = rs.getString(1);
                                s.password = rs.getString(2);
                                s.phoneNumber = rs.getString(3);
                                s.emil = rs.getString(4);
                                s.name = rs.getString(5);
                                s.idCard = rs.getString(6);
                                s.isIdCard = isSmrz(connection);
                                s.sfzh = rs.getString(7);
                                s.qyName = rs.getString(8);
                                s.userType = rs.getString(9);
                                try
                                {
                                    s.birthday = getBirthday(StringHelper.decode(s.sfzh));
                                }
                                catch (Throwable throwable)
                                {
                                    logger.error(throwable, throwable);
                                }
                                s.sex = getSex(s.sfzh);
                            }
                            return s;
                        }
                    },
                    "SELECT T6110.F02 AS F01, T6110.F03 AS F02, T6110.F04 AS F03, T6110.F05 AS F04, T6141.F02 AS F05, T6141.F06 AS F06,T6141.F07 AS F07,T6161.F04 AS F08 ,T6110.F06 AS F09 FROM S61.T6110 LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 LEFT JOIN S61.T6161 ON T6110.F01 = T6161.F01 WHERE T6110.F01 = ? ",
                    acount);
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04, F08, F03,F10 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, acount);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        safety.isEmil = resultSet.getString(1);
                        safety.txpassword = resultSet.getString(2);
                        safety.isPhone = resultSet.getString(3);
                        safety.isPwdSafety = resultSet.getString(4);
                    }
                }
            }
            return safety;
        }
    }
    
    // 是否实名通过
    private String isSmrz(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    if (T6118_F02.parse(resultSet.getString(1)) == T6118_F02.TG)
                    {
                        return "TG";
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateName(String name, String idcard, String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(name) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                boolean is = isIdcard(connection, idcard, T6110_F06.ZRR);
                if (is)
                {
                    throw new ParameterException("该身份证已认证过！");
                }
                Timestamp date = TimestampParser.parse(getBirthday(idcard));
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6141 SET F02 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, name);
                    pstmt.setString(2, T6141_F04.TG.name());
                    pstmt.setString(3, idcard.substring(0, 2) + "***************");
                    pstmt.setString(4, StringHelper.encode(idcard));
                    pstmt.setTimestamp(5, date);
                    pstmt.setInt(6, acount);
                    pstmt.execute();
                }
                execute(connection, "UPDATE S61.T6118 SET F02 = ? WHERE F01 = ?", T6118_F02.TG.name(), acount);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updateNameSFZRR(String name, String idcard, String status, T6110_F06 t6110_f06)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(name) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                boolean is = isIdcard(connection, idcard, t6110_f06);
                if (is)
                {
                    throw new ParameterException("该身份证已认证过！");
                }
                Timestamp date = TimestampParser.parse(getBirthday(idcard));
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6141 SET F02 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, name);
                    pstmt.setString(2, T6141_F04.TG.name());
                    pstmt.setString(3, idcard.substring(0, 2) + "***************");
                    pstmt.setString(4, StringHelper.encode(idcard));
                    pstmt.setTimestamp(5, date);
                    pstmt.setString(6, getSexNotDecode(idcard));
                    pstmt.setInt(7, acount);
                    pstmt.execute();
                }
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                if (StringHelper.isEmpty(escrow)
                    || (!"shuangqian".equals(escrow.toLowerCase()) && !"yeepay".equals(escrow.toLowerCase())))
                {
                    execute(connection, "UPDATE S61.T6118 SET F02 = ? WHERE F01 = ?", T6118_F02.TG.name(), acount);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updateNameForFZRR(String name, String idcard, String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(name) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp date = TimestampParser.parse(getBirthday(idcard));
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6141 SET F02 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, name);
                    pstmt.setString(2, T6141_F04.TG.name());
                    pstmt.setString(3, idcard.substring(0, 2) + "***************");
                    pstmt.setString(4, StringHelper.encode(idcard));
                    pstmt.setTimestamp(5, date);
                    pstmt.setString(6, getSexNotDecode(idcard));
                    pstmt.setInt(7, acount);
                    pstmt.execute();
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6161 SET F11 = ?, F12 = ?, F13 = ?, F22 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, name);
                    pstmt.setString(2, idcard.substring(0, 2) + "***************");
                    pstmt.setString(3, StringHelper.encode(idcard));
                    pstmt.setString(4, getSexNotDecode(idcard));
                    pstmt.setInt(5, acount);
                    pstmt.execute();
                }
                
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                if (StringHelper.isEmpty(escrow)
                    || (!"shuangqian".equals(escrow.toLowerCase()) && !"yeepay".equals(escrow.toLowerCase())
                        && !"huifu".equals(escrow.toLowerCase()) && !"FUYOU".equals(escrow.toLowerCase())))
                {
                    execute(connection, "UPDATE S61.T6118 SET F02 = ? WHERE F01 = ?", T6118_F02.TG.name(), acount);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updatePassword(String password)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(password) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE  S61.T6110 SET F03=? WHERE F01 = ?", password, acount);
        }
    }
    
    @Override
    public void updateEmil(String email)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (isEmil(connection, email))
                {
                    throw new ParameterException("邮箱存在");
                }
                if (StringHelper.isEmpty(email) || acount <= 0)
                {
                    throw new ParameterException("参数错误");
                }
                execute(connection, "UPDATE  S61.T6110 SET F05=? WHERE F01 = ?", email, acount);
                execute(connection, "UPDATE S61.T6118 SET F04=?,F07=? WHERE F01 = ? ", T6118_F04.TG, email, acount);
                
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updatePhone(String phoneNumber)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (StringHelper.isEmpty(phoneNumber) || acount <= 0)
                {
                    throw new ParameterException("参数错误");
                }
                if (isPhone(connection, phoneNumber))
                {
                    throw new ParameterException("手机号码存在");
                }
                execute(connection, "UPDATE  S61.T6110 SET F04=? WHERE F01 = ?", phoneNumber, acount);
                execute(connection,
                    "UPDATE S61.T6118 SET F03=?,F06=? WHERE F01 = ? ",
                    T6118_F03.TG,
                    phoneNumber,
                    acount);
                execute(connection, "UPDATE S61.T6164 SET F06 = ? WHERE F01 = ?", phoneNumber, acount);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updateTxpassword(String txPassword)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(txPassword) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE  S61.T6118 SET F05=?, F08=? WHERE F01 = ?",
                T6118_F05.YSZ.name(),
                txPassword,
                acount);
        }
    }
    
    @Override
    public void bindEmil(String emil, String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(emil) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                execute(connection, "UPDATE  S61.T6110 SET F05=? WHERE F01 = ?", emil, acount);
                execute(connection, "UPDATE S61.T6118 SET F04=?,F07=? WHERE F01 = ? ", status, emil, acount);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public boolean isIdcard(String idCard)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            }, "SELECT F01 FROM S61.T6141 WHERE T6141.F07 = ?", StringHelper.encode(idCard));
        }
    }
    
    @Override
    public boolean isIdcard(String idCard, T6110_F06 t6110_f06)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<Boolean>()
                {
                    @Override
                    public Boolean parse(ResultSet rs)
                        throws SQLException
                    {
                        boolean is = false;
                        if (rs.next())
                        {
                            is = true;
                        }
                        return is;
                    }
                },
                "SELECT T6141.F01 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01=T6110.F01 WHERE T6141.F07 = ? AND T6110.F06=? AND T6110.F01 <> ?",
                StringHelper.encode(idCard),
                t6110_f06.name(),
                serviceResource.getSession().getAccountId());
        }
    }
    
    private boolean isIdcard(Connection connection, String idCard, T6110_F06 t6110_f06)
        throws Throwable
    {
        return select(connection,
            new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            },
            "SELECT T6141.F01 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01=T6110.F01 WHERE T6141.F07 = ? AND T6110.F06=? AND T6110.F01 <> ?",
            StringHelper.encode(idCard),
            t6110_f06.name(),
            serviceResource.getSession().getAccountId());
    }
    
    @Override
    public boolean isEmil(String idCard)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            }, "SELECT F01 FROM S61.T6110 WHERE T6110.F05 = ?", idCard);
        }
    }
    
    private boolean isEmil(Connection connection, String idCard)
        throws Throwable
    {
        return select(connection, new ItemParser<Boolean>()
        {
            @Override
            public Boolean parse(ResultSet rs)
                throws SQLException
            {
                boolean is = false;
                if (rs.next())
                {
                    is = true;
                }
                return is;
            }
        }, "SELECT F01 FROM S61.T6110 WHERE T6110.F05 = ?", idCard);
    }
    
    @Override
    public boolean isPhone(String idCard)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            }, "SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ?", idCard);
        }
    }
    
    private boolean isPhone(Connection connection, String idCard)
        throws Throwable
    {
        return select(connection, new ItemParser<Boolean>()
        {
            @Override
            public Boolean parse(ResultSet rs)
                throws SQLException
            {
                boolean is = false;
                if (rs.next())
                {
                    is = true;
                }
                return is;
            }
        }, "SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ?", idCard);
    }
    
    @Override
    public boolean udpateSendTotal(String sendtype)
        throws Throwable
    {
        // if(StringHelper.isEmpty(sendtype)){
        // throw new ParameterException("参数错误");
        // }
        // final ConfigureProvider configureProvider =
        // serviceResource.getResource(ConfigureProvider.class);
        // Map<String,Integer> list = null;
        // list = select(getConnection(),new ItemParser<Map<String,Integer>>() {
        // @Override
        // public Map<String,Integer> parse(ResultSet rs) throws SQLException {
        // Map<String,Integer> list = null;
        // if(rs.next()){
        // list = new HashMap<String,Integer>();
        // //邮件次数
        // int et = rs.getInt(1);
        // //短信次数
        // int mt = rs.getInt(2);
        // list.put("emil", et);
        // list.put("phone", mt);
        // }
        // return list;
        // }
        // },
        // " SELECT F31,F32 FROM T6011 WHERE F01=? FOR UPDATE ",serviceResource.getSession().getAccountId());
        //
        // if(list !=null){
        // for (Map.Entry<String, Integer> entry : list.entrySet()) {
        // String t = entry.getKey();
        // if("emil".equals(t) && "emil".equals(sendtype) ){
        // if(entry.getValue()>=IntegerParser.parse(configureProvider.getProperty(SystemVariable.YJ_RZCS))){
        // throw new ParameterException("您当天邮件发送次数已达上限！");
        // }else{
        // execute(getConnection(),"UPDATE  T6011 SET F31=F31+1 WHERE F01 = ?",serviceResource.getSession().getAccountId());
        // }
        // }else if("phone".equals(t) && "phone".equals(sendtype) ){
        // if(entry.getValue()>=IntegerParser.parse(configureProvider.getProperty(SystemVariable.DX_RZCS))){
        // throw new ParameterException("您当天短信发送次数已达上限！");
        // }else{
        // execute(getConnection(),"UPDATE  T6011 SET F32=F32+1 WHERE F01 = ?",serviceResource.getSession().getAccountId());
        // }
        // }
        // }
        //
        // }else{
        // throw new ParameterException("参数错误");
        // }
        
        return true;
    }
    
    @Override
    public void udpatetxSize()
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                int total = select(connection, new ItemParser<Integer>()
                {
                    @Override
                    public Integer parse(ResultSet rs)
                        throws SQLException
                    {
                        int total = 0;
                        if (rs.next())
                        {
                            total = rs.getInt(1);
                        }
                        return total;
                    }
                }, "SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ? ", acount);
                final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                int zcs = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
                if (total >= zcs)
                {
                    throw new ParameterException("您今日交易密码输入错误已到最大次数，请改日再试!");
                }
                else
                {
                    execute(connection, "UPDATE  S61.T6110 SET F11=F11+1 WHERE T6110.F01 = ?", acount);
                }
                if ((total + 1) >= zcs)
                {
                    throw new ParameterException("您今日交易密码输入错误已到最大次数，请改日再试!");
                }
                else
                {
                    throw new ParameterException("交易密码错误,您最多还可以输入" + (zcs - (total + 1)) + "次!");
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    /**
     * 根据身份证得到出生年月
     * 
     * @param cardID
     * @return
     */
    private static String getBirthday(String cardID)
    {
        StringBuffer tempStr = new StringBuffer("");
        if (cardID != null && cardID.trim().length() > 0)
        {
            if (cardID.trim().length() == 15)
            {
                tempStr.append(cardID.substring(6, 12));
                tempStr.insert(4, '-');
                tempStr.insert(2, '-');
                tempStr.insert(0, "19");
            }
            else if (cardID.trim().length() == 18)
            {
                tempStr = new StringBuffer(cardID.substring(6, 14));
                tempStr.insert(6, '-');
                tempStr.insert(4, '-');
            }
        }
        return tempStr.toString();
    }
    
    @Override
    public void isUpdateTxmm()
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            int total = select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    int total = 0;
                    if (rs.next())
                    {
                        total = rs.getInt(1);
                    }
                    return total;
                }
            }, "SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ?  FOR UPDATE ", acount);
            final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            int zcs = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (total >= zcs)
            {
                throw new ParameterException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
        }
    }
    
    @Override
    public void updateVideoAuth(String fileName, String fileType, int fileSize, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstat =
                connection.prepareStatement("SELECT F09 FROM S61.T6118 WHERE F01 = ? LIMIT 1"))
            {
                pstat.setInt(1, accountId);
                try (ResultSet rs = pstat.executeQuery())
                {
                    if (rs.next())
                    {
                        String authStatus = rs.getString(1);
                        if ("TG".equals(authStatus))
                        {
                            throw new ParameterException("该用户已做过视频认证.");
                        }
                    }
                }
            }
            
            String sql = "INSERT INTO S61.T6145 (F02, F03, F04, F05, F06) VALUES (?,?,?,?,?)";
            try (PreparedStatement pstat = connection.prepareStatement(sql))
            {
                pstat.setInt(1, accountId);
                pstat.setString(2, fileType);
                pstat.setString(3, fileName);
                pstat.setInt(4, fileSize);
                pstat.setTimestamp(5, getCurrentTimestamp(connection));
                pstat.execute();
            }
        }
    }
    
    @Override
    public Safety getSafety()
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        try (final Connection connection = getConnection())
        {
            Safety safety =
                select(connection,
                    new ItemParser<Safety>()
                    {
                        @Override
                        public Safety parse(ResultSet rs)
                            throws SQLException
                        {
                            Safety s = new Safety();
                            if (rs.next())
                            {
                                s.username = rs.getString(1);
                                s.password = rs.getString(2);
                                s.phoneNumber = rs.getString(3);
                                s.emil = rs.getString(4);
                                s.name = rs.getString(5);
                                s.idCard = rs.getString(6);
                                s.isIdCard = isSmrz(connection);
                                s.sfzh = rs.getString(7);
                            }
                            return s;
                        }
                    },
                    "SELECT T6110.F02 AS F01, T6110.F03 AS F02, T6110.F04 AS F03, T6110.F05 AS F04, T6141.F02 AS F05, T6141.F06 AS F06,T6141.F07 F07 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6110.F01 = ? ",
                    acount);
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04, F08, F09 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, acount);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        safety.isEmil = resultSet.getString(1);
                        safety.txpassword = resultSet.getString(2);
                        safety.videoAuth = resultSet.getString(3);
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F07, F08 FROM S61.T6145 WHERE (F07 = 'DSH' OR F07 = 'SHBTG') AND F02 = ? ORDER BY F01 DESC LIMIT 1"))
            {
                pstmt.setInt(1, acount);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        safety.videoAuth = resultSet.getString(1);
                        safety.videoAuthOpinion = resultSet.getString(2);
                    }
                }
            }
            return safety;
        }
    }
    
    @Override
    public boolean isBindPhone()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        String phone = rs.getString(1);
                        if (!StringHelper.isEmpty(phone))
                        {
                            is = true;
                        }
                    }
                    return is;
                }
            }, "SELECT F04 FROM S61.T6110 WHERE T6110.F01 = ?", serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public Integer bindPhoneCount(String phone, Integer pType)
        throws Throwable
    {
        StringBuffer sbSQL = new StringBuffer(512);
        sbSQL.append(" SELECT COUNT(1) FROM S10._1040 t  \n");
        sbSQL.append(" LEFT JOIN S10._1041 w ON t.F01 = w.F01 \n");
        sbSQL.append(" WHERE w.F02 = ? \n");
        sbSQL.append(" AND t.F02 = ? \n");
        sbSQL.append(" AND DATE_FORMAT(t.F04, '%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') \n");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                    }
                    return count;
                }
            }, sbSQL.toString(), phone, pType, getCurrentTimestamp(connection));
        }
    }
    
    @Override
    public Integer phoneMatchVerifyCodeErrorCount(String phone, Integer pType)
        throws Throwable
    {
        String sql =
            "SELECT COUNT(1) FROM S10._1043 WHERE F02 = ?  AND F03 = ?  AND DATE_FORMAT(F05, '%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                    }
                    return count;
                }
            }, sql, pType, phone, getCurrentTimestamp(connection));
        }
    }
    
    @Override
    public void insertPhoneMatchVerifyCodeError(String phone, Integer pType, String code)
        throws Throwable
    {
        String sql = " INSERT INTO S10._1043(F02,F03,F04,F05) VALUES(?,?,?,?) \n";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstat = connection.prepareStatement(sql))
            {
                pstat.setInt(1, pType);
                pstat.setString(2, phone);
                pstat.setString(3, code);
                pstat.setTimestamp(4, getCurrentTimestamp(connection));
                pstat.execute();
            }
        }
        catch (Exception e)
        {
            logger.error("SafetyManageImpl.insertPhoneMatchVerifyCodeError() 插入手机验证码匹配错误表失败", e);
            throw e;
        }
        
    }
    
    @Override
    public Integer sendEmailCount(final String email, Integer pType)
        throws Throwable
    {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append(" SELECT COUNT(1) FROM S10._1046 t  \n");
        sbSQL.append(" LEFT JOIN S10._1047 w ON t.F01 = w.F01 \n");
        sbSQL.append(" WHERE w.F02 = ? \n");
        sbSQL.append(" AND t.F04 = ? \n");
        sbSQL.append(" AND DATE_FORMAT(t.F05, '%Y-%m-%d') = DATE_FORMAT(CURRENT_TIMESTAMP (),'%Y-%m-%d') \n");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                        logger.info("邮箱" + email + "今天发送邮件的次数为：" + count);
                    }
                    return count;
                }
            }, sbSQL.toString(), email, pType);
        }
    }
    
    @Override
    public Integer userSendEmailCount(Integer pType)
        throws Throwable
    {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append(" SELECT COUNT(1) FROM S10._1046 t  \n");
        sbSQL.append(" LEFT JOIN S10._1047 w ON t.F01 = w.F01 \n");
        sbSQL.append(" WHERE t.F04 = ? \n");
        sbSQL.append(" AND t.F08 = ? \n");
        sbSQL.append(" AND DATE_FORMAT(t.F05, '%Y-%m-%d') = DATE_FORMAT(CURRENT_TIMESTAMP (),'%Y-%m-%d') \n");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                        logger.info("用户" + serviceResource.getSession().getAccountId() + "今天发送邮件的次数为：" + count);
                    }
                    return count;
                }
            }, sbSQL.toString(), pType, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public Integer userSendPhoneCount(Integer pType)
        throws Throwable
    {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append(" SELECT COUNT(1) FROM S10._1040 t  \n");
        sbSQL.append(" LEFT JOIN S10._1041 w ON t.F01 = w.F01 \n");
        sbSQL.append(" WHERE t.F02 = ? \n");
        sbSQL.append(" AND t.F07 = ? \n");
        sbSQL.append(" AND DATE_FORMAT(t.F04, '%Y-%m-%d') = DATE_FORMAT(CURRENT_TIMESTAMP (),'%Y-%m-%d') \n");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                        logger.info("用户" + serviceResource.getSession().getAccountId() + "今天发送短信的次数为：" + count);
                    }
                    return count;
                }
            }, sbSQL.toString(), pType, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public Integer iPAddrSendSmsCount(String ip, Integer pType)
        throws Throwable
    {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append(" SELECT COUNT(1) FROM S10._1040 t  \n");
        sbSQL.append(" LEFT JOIN S10._1041 w ON t.F01 = w.F01 \n");
        sbSQL.append(" WHERE t.F02 = ? \n");
        sbSQL.append(" AND t.F08 = ? \n");
        sbSQL.append(" AND DATE_FORMAT(t.F04, '%Y-%m-%d') = DATE_FORMAT(CURRENT_TIMESTAMP (),'%Y-%m-%d') \n");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    Integer count = 0;
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                    }
                    return count;
                }
            }, sbSQL.toString(), pType, ip);
        }
    }
    
    private String getSex(String sfzh)
    {
        if (StringHelper.isEmpty(sfzh))
        {
            return null;
        }
        
        String sexSfzh = null;
        String sex = null;
        try
        {
            sexSfzh = StringHelper.decode(sfzh);
            if (Integer.parseInt(sexSfzh.substring(sexSfzh.length() - 2, sexSfzh.length() - 1)) % 2 != 0)
            {
                sex = "男";
            }
            else
            {
                sex = "女";
            }
        }
        catch (Throwable throwable)
        {
            sex = null;
        }
        return sex;
    }
    
    private String getSexNotDecode(String sfzh)
    {
        if (StringHelper.isEmpty(sfzh))
        {
            return null;
        }
        String sex = null;
        try
        {
            if (Integer.parseInt(sfzh.substring(sfzh.length() - 2, sfzh.length() - 1)) % 2 != 0)
            {
                sex = T6141_F09.M.name();
            }
            else
            {
                sex = T6141_F09.F.name();
            }
        }
        catch (Throwable throwable)
        {
            logger.error("SafetyManageImpl.getSexNotDecode error", throwable);
        }
        return sex;
    }
    
    /**
     * 设置密保问题
     * 
     * @param params
     * @return
     * @throws Throwable
     */
    @Override
    public boolean updatePwdSafeInfo(List<PwdSafetyQuestionAnswer> params)
        throws Throwable
    {
        if (params == null || params.size() <= 0)
        {
            throw new Exception("密保问题不能为空");
        }
        int acount = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                Integer count = select(connection, new ItemParser<Integer>()
                {
                    @Override
                    public Integer parse(ResultSet rs)
                        throws SQLException
                    {
                        Integer count = 0;
                        if (rs.next())
                        {
                            count = rs.getInt(1);
                        }
                        return count;
                    }
                }, "SELECT COUNT(1) FROM S61.T6194 WHERE F02 = ? ", acount);
                serviceResource.openTransactions(connection);
                if (count > 0)
                {
                    execute(connection, "DELETE from S61.T6194 where F02 = ? ", acount);
                }
                for (PwdSafetyQuestionAnswer qAnswer : params)
                {
                    execute(connection,
                        "INSERT INTO S61.T6194(F02,F03,F04,F05) VALUES (?,?,?,?) \n",
                        acount,
                        qAnswer.questionId,
                        qAnswer.answer,
                        getCurrentTimestamp(connection));
                }
                execute(connection, "UPDATE S61.T6118 SET F10 = ?  WHERE F01 = ? ", T6118_F10.TG.name(), acount);
                serviceResource.commit(connection);
                return true;
            }
            catch (Exception e)
            {
                logger.error(e, e);
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 获取用户的密码答案
     * 
     * @param userId
     *            用户ID
     * @param QuestionId
     *            问题ID
     * @return
     * @throws Throwable
     */
    @Override
    public String getUserAnswerByQuestionId(int userId, int questionId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, "SELECT F04 FROM S61.T6194 WHERE F02 = ? AND F03 = ?", userId, questionId);
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * 获取用户待绑定的邮箱
     */
    @Override
    public String getUnBindEmail()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet paramResultSet)
                    throws SQLException
                {
                    if (paramResultSet.next())
                    {
                        return paramResultSet.getString(1);
                    }
                    return null;
                }
            }, "SELECT F11 FROM S61.T6118 WHERE T6118.F01=? LIMIT 1", serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public void updateT6118Email(String email)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S61.T6118 SET T6118.F11=? WHERE T6118.F01=? "))
            {
                ps.setString(1, email);
                ps.setInt(2, serviceResource.getSession().getAccountId());
                ps.execute();
            }
        }
    }
    
    @Override
    public void bindEmail(String email, String status, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (isEmil(connection, email))
                {
                    throw new ParameterException("邮箱存在");
                }
                if (StringHelper.isEmpty(email) || userId <= 0)
                {
                    throw new ParameterException("参数错误");
                }
                execute(connection, "UPDATE  S61.T6110 SET F05=? WHERE F01 = ?", email, userId);
                execute(connection,
                    "UPDATE S61.T6118 SET F04=?,F07=?,F11=NULL WHERE F01 = ? ",
                    T6118_F04.TG,
                    email,
                    userId);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public boolean getEmailByUserId(String email, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE T6118.F01=? AND T6118.F11=? "))
            {
                ps.setInt(1, userId);
                ps.setString(2, email);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void updatePhoneById(String phoneNumber, int acount)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (StringHelper.isEmpty(phoneNumber) || acount <= 0)
                {
                    throw new ParameterException("参数错误");
                }
                execute(connection, "UPDATE  S61.T6110 SET F04=? WHERE F01 = ?", phoneNumber, acount);
                execute(connection,
                    "UPDATE S61.T6118 SET F03=?,F06=? WHERE F01 = ? ",
                    T6118_F03.TG,
                    phoneNumber,
                    acount);
                execute(connection, "UPDATE S61.T6164 SET F04 = ? WHERE F01 = ?", phoneNumber, acount);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public String getTxPwdByPhone(String phoneNumber)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, "select T6118.F08 from S61.T6110, S61.T6118 where T6110.F01 = T6118.F01 AND T6110.F04 = ?", phoneNumber);
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    @Override
    public boolean isAuthingUpdate()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6118.F01 FROM S61.T6118 INNER JOIN S61.T6141 ON T6118.F01=T6141.F01 WHERE T6118.F01=? AND DATE_ADD(T6118.F12,INTERVAL ? MINUTE)>SYSDATE() AND T6118.F02='BTG' "))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setInt(2, Integer.parseInt(SystemVariable.SMRZ_MAX_WAITTIME.getValue()));
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void updateT6198F03()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE  S61.T6198 SET F03=F03+1,F04=? WHERE F02 = ?",
                T6198_F04.PC.name(),
                serviceResource.getSession().getAccountId());
        }
        
    }
    
    @Override
    public void updateT6198F06(int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE  S61.T6198 SET F04=?,F06=? WHERE F02 = ?",
                T6198_F04.PC.name(),
                getCurrentTimestamp(connection),
                accountId);
        }
    }
    
    @Override
    public void udpateT6198F03(Connection connection, int userId)
        throws Throwable
    {
        execute(connection, "UPDATE  S61.T6198 SET F03=F03+1,F04=? WHERE F02 = ?", T6198_F04.PC.name(), userId);
    }
    
    @Override
    public void udpateT6198F06(Connection connection, int userId)
        throws Throwable
    {
        execute(connection,
            "UPDATE  S61.T6198 SET F04=?,F06=? WHERE F02 = ?",
            T6198_F04.PC.name(),
            getCurrentTimestamp(connection),
            userId);
    }
    
    @Override
    public int psdInputCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getShort(1);
                    }
                }
            }
            return 0;
        }
    }
    
    @Override
    public void addInputCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", serviceResource.getSession()
                .getAccountId());
        }
    }
    
    @Override
    public boolean isMoreThanErrorCount(int userId, int number)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S71.T7122 t WHERE t.F06= ? AND DATE(t.F05)=DATE(SYSDATE()) "))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1) > (number - 1);
                    }
                }
            }
        }
        return false;
    }

	@Override
	public boolean isPhone(String phone, int userId) throws Throwable {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Boolean>()
            {
                @Override
                public Boolean parse(ResultSet rs)
                    throws SQLException
                {
                    boolean is = false;
                    if (rs.next())
                    {
                        is = true;
                    }
                    return is;
                }
            }, "SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ? AND T6110.F01 = ?", phone, userId);
        }
    }
    
}
