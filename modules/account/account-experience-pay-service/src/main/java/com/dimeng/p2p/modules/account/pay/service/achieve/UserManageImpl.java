package com.dimeng.p2p.modules.account.pay.service.achieve;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.enums.T5123_F06;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6111_F06;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.modules.account.pay.service.UserManage;
import com.dimeng.p2p.modules.account.pay.service.entity.OpenEscrowGuideEntity;
import com.dimeng.p2p.modules.account.pay.service.entity.QyUserInsert;
import com.dimeng.p2p.modules.account.pay.service.entity.UserInsert;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

public class UserManageImpl extends AbstractPayService implements UserManage
{
    
    public UserManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public String getAccountName()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
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
        }
        return null;
        
    }
    
    private String getAccountName(int userID)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
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
    
    @Override
    public boolean isAccountExists(String accountName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, accountName);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public int addUser(UserInsert user, ServiceSession serviceSession)
        throws Throwable
    {
        /**
         * 用户账号表
         */
        String t6110 =
            "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F04=?, F14 = ? ";
        /**
         * 用户理财统计表
         */
        String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
        /**
         * 用户信用账户表
         */
        String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
        /**
         * 用户安全认证表
         */
        String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
        /**
         * 用户站内信
         */
        String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
        /**
         * 用户站内信内容
         */
        String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
        /**
         * 个人基础信息
         */
        String t6141 = "INSERT INTO S61.T6141 SET F01 = ?, F03 = ?";
        /**
         * 资金账户
         */
        String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
        /**
         * 推广奖励统计
         */
        String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";
        /**
         * 优选理财统计
         */
        String t6413 = "INSERT INTO S64.T6413 SET F01 = ?";
        
        /**
         * 实名认证统计
         */
        String t6198 = "INSERT INTO S61.T6198 SET F02 = ? ";
        
        if (user == null)
        {
            throw new ParameterException("参数不合法.");
        }
        if (isAccountExists(user.getAccountName()))
        {
            throw new LogicalException("该用户名已存在，请输入其他用户名.");
        }
        // 查询我的推广码
        String selectT6111 = "SELECT F02 FROM S61.T6111";
        String myCode = getCode();
        // 邀请码
        String code = user.getCode();
        boolean isExists = false;
        boolean isPhoneCode = false;
        boolean isEmployNum = false;
        final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        try (Connection connection = getConnection())
        {
            
            if (!StringHelper.isEmpty(code) && code.length() == 11)
            { // 输入的邀请码是手机号
                String selectT6110 = "SELECT F01,F06 FROM S61.T6110 WHERE F04 = ? LIMIT 1";
                int userID = -1;
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
                if (userID > 0)
                { // 自然人才可以作为邀请码 &&
                  // t6110_F06.getChineseName().equalsIgnoreCase(T6110_F06.ZRR.getChineseName())
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
                                    isExists = true;
                                    isPhoneCode = true;
                                    code = currCode;
                                }
                                if (!StringHelper.isEmpty(currCode) && currCode.equalsIgnoreCase(myCode))
                                {
                                    myCode = getCode();
                                }
                            }
                        }
                    }
                }
            }
            else
            { // 正常的邀请码 6位数
                /*
                 * try (PreparedStatement ps =
                 * connection.prepareStatement(selectT6111)) { try
                 * (ResultSet rs = ps.executeQuery()) { while (rs.next()) {
                 * String s = rs.getString(1); if (!StringHelper.isEmpty(s)
                 * && s.equals(code)) { isPhoneCode = false; isExists =
                 * true; } if (!StringHelper.isEmpty(s) && s.equals(myCode))
                 * { myCode = getCode(); } } } }
                 */
                // 为当前注册用户生成一个唯一的推广码
                myCode = validateCode(connection, myCode);
                
                // 验证注册用户输入的邀请码是否存在，true：存在；false：不存在
                isExists = validateTgCode(connection, code);
                
                // 如果注册用户输入的邀请码不存在，则判断用户输入的是否是业务员号
                if (!isExists && is_business)
                {
                    String selectT7110 = "SELECT F12 FROM S71.T7110 WHERE F12 = ? ";
                    try (PreparedStatement ps = connection.prepareStatement(selectT7110))
                    {
                        ps.setString(1, code);
                        try (ResultSet rs = ps.executeQuery())
                        {
                            while (rs.next())
                            {
                                isEmployNum = true;
                                isExists = true;
                            }
                        }
                    }
                }
                
            }
            
            if (!isExists && !StringHelper.isEmpty(code))
            {
                throw new LogicalException("您填写的邀请码" + (is_business ? "/业务员工号" : "") + "不存在");
            }
            try
            {
                serviceResource.openTransactions(connection);
                
                String password = UnixCrypt.crypt(user.getPassword(), DigestUtils.sha256Hex(user.getPassword()));
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 当前注册用户ID
                String empNum = isEmployNum ? code : "";
                int userId =
                    insert(connection,
                        t6110,
                        user.getAccountName(),
                        password,
                        T6110_F06.ZRR.name(),
                        T6110_F07.QY.name(),
                        user.getRegisterType() == null ? T6110_F08.PCZC : user.getRegisterType(),
                        now,
                        T6110_F10.F.name(),
                        user.getPhone(),
                        empNum);
                execute(connection, t6115, userId);
                execute(connection, t6116, userId);
                execute(connection, t6118, userId);
                String template = configureProvider.getProperty(LetterVariable.REGESTER_SUCCESS);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("name", user.getAccountName());
                int letterId = insert(connection, t6123, userId, "注册成功", now);
                execute(connection, t6124, letterId, StringHelper.format(template, envionment));
                T6141_F03 type = user.getType() == null ? T6141_F03.LC : user.getType();
                execute(connection, t6141, userId, type.name());
                
                for (T6101_F03 t : T6101_F03.values())
                {
                    execute(connection, t6101, userId, t.name(), getAccount(t.name(), userId), getAccountName(userId));
                }
                execute(connection, t6310, userId);
                execute(connection, t6413, userId);
                // 推广人ID
                int tgrID = 0;
                if (!StringHelper.isEmpty(code) && !isEmployNum)
                {
                    String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
                    tgrID = getUserID(connection, code);
                    execute(connection, t6311, tgrID, userId);
                    // 推广统计人数+1
                    if (tgrID > 0)
                    {
                        execute(connection, "UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?", tgrID);
                        
                    }
                }
                if (isEmployNum)
                {
                    code = "";
                }
                execute(connection,
                    "INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?,F06 = ?,F07 = ?",
                    userId,
                    myCode,
                    code,
                    user.getPhone(),
                    user.getName(),
                    isPhoneCode ? T6111_F06.S.getChineseName() : T6111_F06.F.getChineseName(),
                    now);
                // 用户认证信息
                Integer[] rzxIds = getRzxID(connection, T5123_F06.GR);
                if (rzxIds != null)
                {
                    for (Integer id : rzxIds)
                    {
                        execute(connection, "INSERT INTO S61.T6120 SET F01 = ?, F02 = ?", userId, id);
                    }
                }
                execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
                
                boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
                if (is_mall)
                {
                    // 用户积分账户
                    execute(connection, "INSERT INTO S61.T6105 SET F02 = ?, F06 = ?, F07 = ?", userId, now, now);
                }
                
                //实名认证统计
                execute(connection, t6198, userId);
                //更新累计登陆次数和最后登陆时间
                udpateT6198F05F07(connection, userId);
                
                // 送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                activityCommon.sendActivity(connection,
                    userId,
                    T6340_F03.redpacket.name(),
                    T6340_F04.register.name(),
                    BigDecimal.ZERO,
                    0);
                activityCommon.sendActivity(connection,
                    userId,
                    T6340_F03.interest.name(),
                    T6340_F04.register.name(),
                    BigDecimal.ZERO,
                    0);
                activityCommon.sendActivity(connection,
                    userId,
                    T6340_F03.experience.name(),
                    T6340_F04.register.name(),
                    BigDecimal.ZERO,
                    0);
                
                // 默认信用额度
                BigDecimal creditAmount =
                    new BigDecimal(configureProvider.getProperty(SystemVariable.DEFAULT_CREDIT_AMOUNT));
                updateUserCredit(connection, userId, creditAmount);
                
                serviceResource.commit(connection);
                return userId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 生成资金账户 账号
     * 
     * @param type
     * @param id
     * @return
     */
    private String getAccount(String type, int id)
    {
        DecimalFormat df = new DecimalFormat("00000000000");
        StringBuilder sb = new StringBuilder();
        sb.append(type.substring(0, 1));
        sb.append(df.format(id));
        return sb.toString();
    }
    
    /**
     * 通过填写的邀请码得到用户ID
     * 
     * @return
     */
    private int getUserID(Connection connection, String code)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6111 WHERE T6111.F02 = ? LIMIT 1"))
        {
            pstmt.setString(1, code);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    // 得到所有启用的信用认证项ID
    private Integer[] getRzxID(Connection connection, T5123_F06 F06)
        throws SQLException
    {
        ArrayList<Integer> list = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S51.T5123 WHERE F06 = ?"))
        {
            // pstmt.setString(1, T5123_F04.QY.name());
            pstmt.setString(1, F06.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    if (list == null)
                        list = new ArrayList<>();
                    list.add(resultSet.getInt(1));
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new Integer[list.size()]));
        
    }
    
    private String getCode()
    {
        char[] chs =
            {'a', 'b', 'c', '1', '2', '3', '4', '5', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '6', '7', '8', '9'};
        SecureRandom random = new SecureRandom();
        final char[] value = new char[6];
        for (int i = 0; i < 6; i++)
        {
            value[i] = chs[random.nextInt(chs.length)];
        }
        return String.valueOf(value);
    }
    
    @Override
    public int readAccountId(String condition, String password)
        throws AuthenticationException, SQLException
    {
        int accountId = 0;
        String sql = "SELECT F01,F03,F07 FROM S61.T6110 WHERE F02=? OR F04=? OR F05=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, condition);
                ps.setString(2, condition);
                ps.setString(3, condition);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        accountId = rs.getInt(1);
                        String pwd = rs.getString(2);
                        String status = rs.getString(3);
                        if ("SD".equals(status))
                        {
                            throw new AuthenticationException("该账号被锁定，禁止登录。");
                        }
                        if ("HMD".equals(status))
                        {
                            throw new AuthenticationException("该账号被拉黑，禁止登录。");
                        }
                        if (!UnixCrypt.crypt(password, DigestUtils.sha256Hex(password)).equals(pwd))
                        {
                            throw new AuthenticationException("用户名或密码错误.");
                        }
                    }
                    else
                    {
                        throw new AuthenticationException("用户名或密码错误.");
                    }
                }
            }
        }
        return accountId;
    }
    
    @Override
    public int getUnReadLetter()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6123_F05.WD.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public void log(int accountId, String ip)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S61.T6190 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?,F06=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setString(3, "登录日志");
                ps.setString(4, "登录前台系统 ");
                ps.setString(5, ip);
                ps.executeUpdate();
            }
        }
    }
    
    @Override
    public void logRegisterInfo(int userId, String ip)
        throws Throwable
    {
        String sqlStr = "INSERT INTO S10._1054 SET F02 = ?, F03 = ?, F04 = ?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr))
            {
                ps.setString(1, ip);
                ps.setInt(2, userId);
                ps.setTimestamp(3, getCurrentTimestamp(connection));
                ps.execute();
            }
        }
    }
    
    @Override
    public boolean ifAllowRegister(String ip)
        throws Throwable
    {
        String sqlStr = "SELECT COUNT(1) FROM S10._1054 WHERE F02 = ? AND DATE_FORMAT(F04,'%Y-%m-%d')=?";
        try (Connection connection = getConnection())
        {
            ConfigureProvider configPro = serviceResource.getResource(ConfigureProvider.class);
            int count = 0;
            int allowCount = Integer.parseInt(configPro.getProperty(SystemVariable.IP_ALLOW_REGISTER_COUNT));
            if (allowCount == 0)
            {
                // 常量设置为0，这不限制
                return true;
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlStr))
            {
                ps.setString(1, ip);
                ps.setDate(2, getCurrentDate(connection));
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        count = resultSet.getInt(1);
                    }
                }
            }
            if (count >= allowCount)
            {
                return false;
            }
            return true;
        }
    }
    
    /**
     * 设置默认信用额度
     * 
     * @param connection
     * @param userId
     * @param creditAmount
     * @throws Throwable
     */
    private void updateUserCredit(Connection connection, int userId, BigDecimal creditAmount)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, creditAmount);
            pstmt.setInt(2, userId);
            pstmt.execute();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, FeeCode.XY_MR);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(4, creditAmount);
            pstmt.setBigDecimal(5, creditAmount);
            pstmt.setString(6, "默认信用额度");
            pstmt.execute();
        }
    }
    
    /**
     * 修改手机号
     * 
     * @param phoneNumber
     *            用户注册手机号码
     * @param userId
     *            用户ID
     * @throws Throwable
     */
    @Override
    public void updatePhoneNum(String phoneNumber, int userId)
        throws Throwable
    {
        if (StringHelper.isEmpty(phoneNumber) || userId <= 0)
        {
            throw new ParameterException("参数错误");
        }
        
        try (Connection connection = getConnection())
        {
            if (isPhoneExists(connection, phoneNumber))
            {
                throw new ParameterException("手机号码存在");
            }
            execute(connection, "UPDATE S61.T6110 SET F04=? WHERE F01 = ?", phoneNumber, userId);
            execute(connection, "UPDATE S61.T6118 SET F03=?,F06=? WHERE F01 = ? ", T6118_F03.TG, phoneNumber, userId);
        }
    }
    
    /**
     * 查询手机号码是否存在
     * 
     * @param idCard
     *            用户注册手机号码
     * @throws Throwable
     */
    private boolean isPhoneExists(Connection connection, String idCard)
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
    
    /**
     * 为当前注册用户生成一个唯一的推广码
     * 
     * @param connection
     * @param myCode
     * @return
     * @throws SQLException
     */
    private String validateCode(Connection connection, String myCode)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT T6111.F02 FROM S61.T6111 WHERE T6111.F02 = ?"))
        {
            ps.setString(1, myCode);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    myCode = getCode();
                    myCode = validateCode(connection, myCode);
                }
            }
        }
        return myCode;
    }
    
    /**
     * 验证注册用户输入的邀请码是否存在，true：存在；false：不存在
     * 
     * @param connection
     * @param code
     * @return
     * @throws SQLException
     */
    private boolean validateTgCode(Connection connection, String code)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT T6111.F02 FROM S61.T6111 WHERE T6111.F02 = ?"))
        {
            ps.setString(1, code);
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
    
    @Override
    public void udpateT6198F05F07(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            udpateT6198F05F07(connection, userId);
        }
        
    }
    
    private void udpateT6198F05F07(Connection connection, int userId)
        throws Throwable
    {
        execute(connection,
            "UPDATE S61.T6198 SET F05=F05+1,F07=? WHERE F02 = ?",
            getCurrentTimestamp(connection),
            userId);
        
    }
    
    @Override
    public String getUsrCustId()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
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
        }
        return null;
    }
    
    @Override
    public int addQy(QyUserInsert user, ServiceSession serviceSession)
        throws Throwable
    {
        /**
         * 用户账号表
         */
        String t6110 =
            "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F17 = ?, F18 = ?";
        /**
         * 用户理财统计表
         */
        String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
        /**
         * 用户信用账户表
         */
        String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
        /**
         * 用户安全认证表
         */
        String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
        /**
         * 用户站内信
         */
        String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
        /**
         * 用户站内信内容
         */
        String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
        /**
         * 个人基础信息
         */
        String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";
        /**
         * 资金账户
         */
        String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
        /**
         * 推广奖励统计
         */
        String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";
        
        if (user == null)
        {
            throw new ParameterException("参数不合法.");
        }
        
        if (isAccountExists(user.getAccountName()))
        {
            throw new LogicalException("该用户名已存在，请输入其他用户名.");
        }
        
        if (isEnterpriseNameExists(user.getEnterpriseName()))
        {
            throw new LogicalException("该企业名称已被注册.");
        }
        
        // 查询我的推广码
        String selectT6111 = "SELECT F02 FROM S61.T6111";
        String myCode = getCode();
        // 邀请码
        String code = "";
        boolean isExists = false;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement(selectT6111))
                {
                    try (ResultSet rs = ps.executeQuery())
                    {
                        while (rs.next())
                        
                        {
                            String s = rs.getString(1);
                            if (!StringHelper.isEmpty(s) && s.equals(code))
                            {
                                isExists = true;
                            }
                            if (!StringHelper.isEmpty(s) && s.equals(myCode))
                            {
                                myCode = getCode();
                            }
                            
                        }
                    }
                    
                }
                
                if (!isExists)
                {
                    code = "";
                }
                String jmPassword = UnixCrypt.crypt(user.getPassword(), DigestUtils.sha256Hex(user.getPassword()));
                Timestamp now = new Timestamp(System.currentTimeMillis());
                
                // 当前注册用户ID
                String userName = user.getAccountName();
                int userId =
                    insert(connection,
                        t6110,
                        userName,
                        jmPassword,
                        T6110_F06.FZRR.name(),
                        T6110_F07.QY.name(),
                        user.getRegisterType(),
                        now,
                        T6110_F10.F.name(),
                        T6110_F17.F.name(),
                        T6110_F17.F.name());
                execute(connection, t6115, userId);
                execute(connection, t6116, userId);
                execute(connection, t6118, userId);
                execute(connection, t6141, userId);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                String template = configureProvider.getProperty(LetterVariable.REGESTER_SUCCESS);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("name", userName);
                int letterId = insert(connection, t6123, userId, "注册成功", now);
                execute(connection, t6124, letterId, StringHelper.format(template, envionment));
                for (T6101_F03 t : T6101_F03.values())
                {
                    execute(connection, t6101, userId, t.name(), getAccount(t.name(), userId), user.getAccountName());
                }
                execute(connection, t6310, userId);
                // 推广人ID
                int tgrID = 0;
                if (!StringHelper.isEmpty(code))
                {
                    String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
                    tgrID = getUserID(connection, code);
                    execute(connection, t6311, tgrID, userId);
                }
                // 推广统计人数+1
                if (tgrID > 0)
                {
                    execute(connection, "UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?", tgrID);
                    // 填写的邀请码正确 插入的用户推广信息表
                    T6110 tgrUser = getUser(tgrID, connection);
                    execute(connection,
                        "INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
                        userId,
                        myCode,
                        code,
                        tgrUser.F04,
                        tgrUser.F02);
                }
                else
                {
                    // 填写的邀请码不正确 插入的用户推广信息表
                    execute(connection, "INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?", userId, myCode, code);
                }
                // 用户认证信息
                Integer[] rzxIds = getRzxID(connection, T5123_F06.QY);
                if (rzxIds != null)
                {
                    for (Integer id : rzxIds)
                    {
                        execute(connection, "INSERT INTO S61.T6120 SET F01 = ?, F02 = ?", userId, id);
                    }
                }
                
                // 企业联系信息
                execute(connection, "INSERT INTO S61.T6164 SET F01 = ?", userId);
                // 用户信用档案表
                execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
                // 企业介绍资料
                execute(connection, "INSERT INTO S61.T6162 SET F01 = ?", userId);
                // 企业基础信息
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F04 = ?"))
                {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, userName);
                    pstmt.setString(3, user.getEnterpriseName());
                    pstmt.execute();
                }
                
                // 默认信用额度
                BigDecimal creditAmount =
                    new BigDecimal(configureProvider.getProperty(SystemVariable.DEFAULT_CREDIT_AMOUNT));
                updateUserCredit(connection, userId, creditAmount);
                
                serviceResource.commit(connection);
                return userId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 通过id找到用户信息
     * 
     * @param id
     * @return
     */
    protected T6110 getUser(int id, Connection connection)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02, F04 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6110();
                    record.F02 = resultSet.getString(1);
                    record.F04 = resultSet.getString(2);
                }
            }
        }
        return record;
    }
    
    /**
     * 校验企业名称是否存在
     * @param enterpriseName
     * @return
     * @throws Throwable
     */
    private boolean isEnterpriseNameExists(String enterpriseName)
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
            }, "SELECT F01 FROM S61.T6161 WHERE T6161.F04 = ?", enterpriseName);
        }
    }

    @Override
    public T6119 selectT6119(int accountId)
        throws Throwable
    {
        
        T6119 t6119 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04 FROM S61.T6119 WHERE T6119.F01 = ? "))
            {
                pstmt.setInt(1, accountId);
                
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6119 = new T6119();
                        t6119.F01 = resultSet.getInt(1);
                        t6119.F02 = resultSet.getInt(2);
                        t6119.F03 = resultSet.getString(3);
                        t6119.F04 = resultSet.getString(4);
                    }
                }
            }
        }
        return t6119;
    }
    @Override
    public OpenEscrowGuideEntity getOpenEscrowGuideEntity()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            OpenEscrowGuideEntity entity = new OpenEscrowGuideEntity();
            int userId = serviceResource.getSession().getAccountId();
            T6110 t6110 = selectT6110(connection, userId);
            if (t6110 == null)
                return null;
            if (T6110_F06.ZRR.equals(t6110.F06))
            {
                entity.setRealNameLabel("真实姓名");
                entity.setIdentificationNoLabel("身份证号");
                T6141 t6141 = selectT6141(connection);
                entity.setRealName(t6141.F02 == null ? "" : t6141.F02);
                entity.setIdentificationNo(t6141.F07 == null ? "" : StringHelper.decode(t6141.F07));
                entity.setUserTag("ZRR");
                entity.setMaxLength("18");
            }
            else
            {
                String prefix = "";
                if (T6110_F10.S.equals(t6110.F10))
                {
                    prefix = "机构法人";
                    entity.setUserTag("JG");
                }
                else
                {
                    prefix = "企业法人";
                    entity.setUserTag("QY");
                }
                entity.setRealNameLabel(prefix + "真实姓名");
                T6161 t6161 = selectT6161(connection);
                entity.setRealName(t6161.F11 == null ? "" : t6161.F11);
                if ("Y".equals(t6161.F20))
                {
                    entity.setIdentificationNoLabel(prefix + "身份证号");
                    entity.setIdentificationNo(t6161.F13 == null ? "" : StringHelper.decode(t6161.F13));
                    entity.setMaxLength("18");// 社信代码只有18位
                }
                else
                {
                    entity.setIdentificationNoLabel(prefix + "身份证号");
                    entity.setIdentificationNo(t6161.F03 == null ? "" : t6161.F03);
                    entity.setIdentificationNo(t6161.F13 == null ? "" : StringHelper.decode(t6161.F13));
                    entity.setMaxLength("18");
                }
            }
            entity.setMobile(t6110.F04 == null ? "" : t6110.F04);
            return entity;
        }
    }
    
    
    
    private T6141 selectT6141(Connection connection)
        throws SQLException
    {
        T6141 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
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
                    record.F08 = resultSet.getDate(8);
                }
            }
        }
        return record;
    }
    
    
    private T6161 selectT6161(Connection conn)
            throws Throwable
        {
            T6161 record = null;
            try (PreparedStatement pstmt =
                conn.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
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