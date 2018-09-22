package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.S61.entities.T6180;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.S61.enums.T6165_F02;
import com.dimeng.p2p.modules.account.console.service.EnterpriseManage;
import com.dimeng.p2p.modules.account.console.service.entity.AT6161;
import com.dimeng.p2p.modules.account.console.service.entity.Enterprise;
import com.dimeng.p2p.modules.account.console.service.entity.Jg;
import com.dimeng.p2p.modules.account.console.service.query.EnterpriseQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

public class EnterpriseManageImpl extends AbstractUserService implements EnterpriseManage
{
    
    public EnterpriseManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final ArrayParser<Enterprise> Enterprise_PARSER = new ArrayParser<Enterprise>()
    {
        @Override
        public Enterprise[] parse(ResultSet rs)
            throws SQLException
        {
            ArrayList<Enterprise> list = null;
            while (rs.next())
            {
                Enterprise record = new Enterprise();
                
                /*
                 * record.userId= rs.getInt(1); record.name=rs.getString(2);
                 * record.contacts= rs.getString(3);
                 * record.contactsMobile=rs.getString(4); record.email =
                 * rs.getString(5); record.organizationNumber = rs.getString(6);
                 */
                
                record.F01 = rs.getInt(1);
                record.F02 = rs.getString(2);
                record.F03 = rs.getString(3);
                record.F04 = rs.getString(4);
                record.F05 = T6110_F06.parse(rs.getString(5));
                record.F06 = T6110_F07.parse(rs.getString(6));
                record.F07 = rs.getTimestamp(7);
                record.F08 = rs.getString(8);
                record.F09 = rs.getString(9);
                record.F10 = rs.getString(10);
                record.F11 = rs.getString(11);
                record.F12 = rs.getString(12);
                record.F13 = rs.getString(13);
                record.F14 = rs.getString(14);
                record.lxr = rs.getString(15);
                record.lxrPhone = rs.getString(16);
                record.shxydm = rs.getString(17);
                record.isShxydm = rs.getString(18);
                record.investType = T6110_F17.parse(rs.getString(19));
                record.dshFlg = rs.getString(20);
                
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(record);
                
            }
            return list == null ? null : list.toArray(new Enterprise[list.size()]);
        }
    };
    
    @Override
    public PagingResult<Enterprise> enterpriseSearch(EnterpriseQuery enterpriseQuery, Paging paging)
        throws Throwable
    {
        
        /*
         * StringBuilder sql = new StringBuilder(
         * "SELECT T6110.F01 AS userId,T6161.F04 AS NAME, T6110.F02 AS contacts, "
         * +
         * "T6110.F04 AS contactsMobile, T6110.F05 AS email, T6110.F06 AS organizationNumber, T6110.F07 AS userState, "
         * +
         * "T6110.F09 AS registrationTime, T6161.F02 AS F08, T6161.F03 AS licenseNumber, T6161.F04 AS F10, "
         * +
         * "T6161.F05 AS dutyNumber, T6161.F06 AS F12, T6161.F11 AS F13, T6161.F12 AS F14 "
         * +
         * "FROM S61.T6110 INNER JOIN S61.T6161 ON T6110.F01 = T6161.F01 WHERE 1=1 "
         * );
         */
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F04 AS F03, T6110.F05 AS F04, T6110.F06 AS F05, T6110.F07 AS F06, T6110.F09 AS F07, T6161.F02 AS F08, ");
        sql.append(" T6161.F03 AS F09, T6161.F04 AS F10, T6161.F05 AS F11, T6161.F06 AS F12, T6161.F11 AS F13, T6161.F12 AS F14,T6164.F07 AS F15,T6164.F04 AS F16,T6161.F19 AS F17,T6161.F20 AS F18,T6110.F17 AS F19,");
        sql.append(" IFNULL(( SELECT T6120.F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 WHERE T6120.F01 = T6110.F01 AND T5123.F04 = 'QY' AND T6120.F05 = 'DSH' LIMIT 1),0) AS F20 ");
        sql.append(" FROM S61.T6110 INNER JOIN S61.T6161 ON T6110.F01 = T6161.F01 AND T6110.F06=? AND T6110.F10=?  INNER JOIN S61.T6164 ON T6164.F01= T6161.F01  WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (enterpriseQuery != null)
        {
            parameters.add(T6110_F06.FZRR.name());
            parameters.add(T6110_F10.F.name());
            sql.append(" AND T6110.F13 = ?");
            parameters.add(T6110_F13.F);
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = enterpriseQuery.getUserId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F01 = ?");
                parameters.add(IntegerParser.parse(string));
            }
            string = enterpriseQuery.getAccount();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getContacts();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6164.F07 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6161.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            string = enterpriseQuery.getContactsMobile();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6164.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getLicenseNumber();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6161.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getOrganizationNumber();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6161.F06 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getDutyNumber();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6161.F05 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            T6110_F07 userState = enterpriseQuery.getUserState();
            if (userState != null)
            {
                sql.append(" AND T6110.F07 = ?");
                parameters.add(userState.name());
            }
            T6110_F17 investType = enterpriseQuery.getInvestType();
            if (investType != null)
            {
                sql.append(" AND T6110.F17 = ?");
                parameters.add(investType.name());
            }
            
            Timestamp timestamp = enterpriseQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6110.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = enterpriseQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6110.F09) <= ?");
                parameters.add(timestamp);
            }
            
        }
        
        sql.append(" ORDER BY F20 DESC,T6110.F01 DESC");
        try (Connection connection = getConnection())
        {
            PagingResult<Enterprise> pagingResult =
                selectPaging(connection, Enterprise_PARSER, paging, sql.toString(), parameters);
            
            return pagingResult;
        }
    }
    
    @Override
    public int add(Enterprise enterprise)
        throws Throwable
    {
        if (StringHelper.isEmpty(enterprise.F02))
        {
            throw new ParameterException("参数错误");
        }
        if (StringHelper.isEmpty(enterprise.F08))
        {
            throw new ParameterException("参数错误8");
        }
        if (StringHelper.isEmpty(enterprise.F09))
        {
            throw new ParameterException("参数错误9");
        }
        
        String pass = "888888";// 默认密码
        if (!StringHelper.isEmpty(enterprise.password))
        {
            pass = enterprise.password;
        }
        pass = UnixCrypt.crypt(pass, DigestUtils.sha256Hex(pass));
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01,F02 FROM  S61.T6110 WHERE F02 = ?"))
                {
                    pstmt.setString(1, enterprise.F02);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            throw new ParameterException("企业用户账号：" + enterprise.F02 + "已经存在");
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F09 = ?,F08 = ?,F10 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setString(1, enterprise.F02);
                    pstmt.setString(2, pass);
                    pstmt.setString(3, enterprise.F03);
                    pstmt.setString(4, enterprise.F04);
                    pstmt.setString(5, T6110_F06.FZRR.name());
                    pstmt.setTimestamp(6, getCurrentTimestamp(connection));
                    pstmt.setString(7, T6110_F08.HTTJ.name());
                    pstmt.setString(8, T6110_F10.F.name());
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            enterprise.F01 = resultSet.getInt(1);
                        }
                    }
                }
                /**
                 * 企业基础信息表
                 */
                //String t6161 = "INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F11 = ?, F12 = ? ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F11 = VALUES(F11), F12 = VALUES(F12)";
                String t6161 =
                    "INSERT INTO S61.T6161 (F01, F02, F03, F04, F05, F06, F11, F12) VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F11 = VALUES(F11), F12 = VALUES(F12)";
                /**
                 * 企业介绍资料表
                 */
                String t6013 = "INSERT INTO S61.T6162 SET F01=?";
                /**
                 * 企业财务状况表
                 */
                String t6014 = "INSERT INTO S61.T6163 SET F01=?,F02=?";
                /**
                 * 企业联系信息表
                 */
                String t6015 = "INSERT INTO S61.T6164 SET F01=?,F06=?";
                /**
                 * 用户信用额度
                 */
                String t6116 = "INSERT INTO S61.T6116 SET F01=?";
                /**
                 * 资金账户
                 */
                String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
                
                execute(connection, t6116, enterprise.F01);
                
                for (T6101_F03 t : T6101_F03.values())
                {
                    execute(connection,
                        t6101,
                        enterprise.F01,
                        t.name(),
                        getAccount(t.name(), enterprise.F01),
                        getAccountName(enterprise.F01, connection));
                }
                
                /**
                 * 企业房产信息表
                 */
                // String t6112 = "INSERT INTO S61.T6112 SET F02=?";
                /**
                 * 企业车产信息表
                 */
                String t6113 = "INSERT INTO S61.T6113 SET F02=?";
                if (enterprise.F01 > 0)
                {
                    try (PreparedStatement pstmt = connection.prepareStatement(t6161))
                    {
                        pstmt.setInt(1, enterprise.F01);
                        pstmt.setString(2, enterprise.F08);
                        pstmt.setString(3, enterprise.F09);
                        pstmt.setString(4, enterprise.F10);
                        pstmt.setString(5, enterprise.F11);
                        pstmt.setString(6, enterprise.F12);
                        pstmt.setString(7, enterprise.F13);
                        pstmt.setString(8, enterprise.F14);
                        pstmt.execute();
                    }
                    this.execute(connection, t6013, enterprise.F01);
                    int y1 = getYear(-1);
                    int y2 = getYear(-2);
                    int y3 = getYear(-3);
                    // 财务表默认记录前3年信息
                    this.execute(connection, t6014, enterprise.F01, y1);
                    this.execute(connection, t6014, enterprise.F01, y2);
                    this.execute(connection, t6014, enterprise.F01, y3);
                    
                    this.execute(connection, t6015, enterprise.F01, enterprise.F03);
                    // this.execute(connection, t6112, enterprise.F01);
                    this.execute(connection, t6113, enterprise.F01);
                    this.writeLog(connection, "add", String.format("Enterprise id %s add success", enterprise.F01));
                }
                
                serviceResource.commit(connection);
                return enterprise.F01;
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
    
    private String getAccountName(int userID, Connection connection)
        throws Throwable
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
        return null;
        
    }
    
    @Override
    public void update(Enterprise enterprise)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6110 SET F04 = ?, F05 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, enterprise.F03);
                    pstmt.setString(2, enterprise.F04);
                    pstmt.setInt(3, enterprise.F01);
                    pstmt.execute();
                }
                if (enterprise.F01 > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6161 (F01, F02, F03, F04, F05, F06, F11, F12) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F11 = VALUES(F11), F12 = VALUES(F12)"))
                    {
                        pstmt.setInt(1, enterprise.F01);
                        pstmt.setString(2, enterprise.F08);
                        pstmt.setString(3, enterprise.F09);
                        pstmt.setString(4, enterprise.F10);
                        pstmt.setString(5, enterprise.F11);
                        pstmt.setString(6, enterprise.F12);
                        pstmt.setString(7, enterprise.F13);
                        pstmt.setString(8, enterprise.F14);
                        
                        pstmt.execute();
                    }
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
    public void export(Enterprise[] enterprises, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (enterprises == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("企业账号");
            writer.write("企业名称");
            writer.write("联系人姓名");
            writer.write("联系人电话");
            writer.write("营业执照/社会信用代码");
            writer.write("注册时间");
            writer.write("状态");
            writer.write("是否允许投资");
            writer.newLine();
            int index = 1;
            
            for (Enterprise enterprise : enterprises)
            {
                writer.write(index++);
                writer.write(enterprise.F02);
                writer.write(enterprise.F10);
                writer.write(enterprise.lxr);
                writer.write(enterprise.lxrPhone);
                writer.write("Y".equals(enterprise.isShxydm) ? enterprise.shxydm : enterprise.F09);
                writer.write(DateTimeParser.format(enterprise.F07) + "\t");
                writer.write(enterprise.F06.getChineseName());
                writer.write(enterprise.investType.getChineseName());
                writer.newLine();
            }
        }
    }
    
    @Override
    public AT6161 getEnterprise(int userId)
        throws Throwable
    {
        AT6161 record = null;
        if (userId < 1)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t1.F01, t1.F02, t1.F03, t1.F04, t1.F05, t1.F06, t1.F07, t1.F08, t1.F09, t1.F10, t1.F11, t1.F12, t1.F13, t1.F14, t1.F15,T6.F06 AS F16,T5.F05,T6.F08,t1.F19,t1.F20,t1.F21 FROM S61.T6161 t1,S61.T6110 T5,S61.T6118 T6 WHERE T5.F01=t1.F01 AND T6.F01 = t1.F01 AND t1.F01 = ?  LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new AT6161();
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
                        record.F21 = resultSet.getString(21);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public String updateEnterpriseBase(AT6161 t6161)
        throws Throwable
    {
        String msg = "";
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT t1.F01 FROM  S61.T6161 t1,S61.T6164 t4 WHERE t1.F01=t4.F01 AND t1.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, t6161.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new ParameterException("企业用户不存在");
                        }
                    }
                }
                if (!StringHelper.isEmpty(t6161.F16))
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01 FROM S61.T6164 WHERE F01=? AND F06 = ?"))
                    {
                        pstmt.setInt(1, t6161.F01);
                        pstmt.setString(2, t6161.F16);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                msg = "手机号码已经存在！";
                                serviceResource.rollback(connection);
                                return msg;
                            }
                        }
                    }
                }
                
                StringBuilder sql = new StringBuilder("UPDATE S61.T6161 SET ");
                StringBuilder tempsql = new StringBuilder();
                ArrayList<Object> parameters = new ArrayList<Object>();
                String string = t6161.F04;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F04 = ?,");
                    parameters.add(string);
                }
                string = t6161.F05;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F05 = ?,");
                    parameters.add(string);
                }
                string = t6161.F06;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F06 = ?,");
                    parameters.add(string);
                }
                int temp = t6161.F10;
                if (temp > 0)
                {
                    tempsql.append(" F10 = ?,");
                    parameters.add(temp);
                }
                if (!StringHelper.isEmpty(tempsql.toString()))
                {
                    String sqlt = "";
                    if (tempsql.toString().lastIndexOf(",") != -1)
                    {
                        sqlt = tempsql.substring(0, tempsql.length() - 1).toString();
                    }
                    else
                    {
                        sqlt = tempsql.toString();
                    }
                    
                    sql.append(sqlt).append(" where F01 = ? limit 1");
                    parameters.add(t6161.F01);
                    try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
                    {
                        this.serviceResource.setParameters(pstmt, parameters.toArray());
                        pstmt.execute();
                    }
                    if (!StringHelper.isEmpty(t6161.F16))
                    {
                        this.execute(connection, "UPDATE S61.T6164 SET F06=? where F01=?", t6161.F16, t6161.F01);
                    }
                    this.writeLog(connection, "update", String.format("Enterprise id %s update success", t6161.F01));
                }
                
                serviceResource.commit(connection);
                return msg;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    public void updateEnterpriseJs(T6162 t6162, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6162 (F01, F02, F03, F04, F05) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05)"))
        {
            pstmt.setInt(1, t6162.F01);
            pstmt.setString(2, t6162.F02);
            pstmt.setString(3, t6162.F03);
            pstmt.setString(4, t6162.F04);
            pstmt.setString(5, t6162.F05);
            pstmt.execute();
        }
    }
    
    public void updateEnterpriseCw(T6163[] t6163s, Connection connection)
        throws Throwable
    {
        for (T6163 t6163 : t6163s)
        {
            boolean f = false;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02, F03,F04, F05 FROM S61.T6163 WHERE T6163.F01 = ? and T6163.F02=? LIMIT 1"))
            {
                pstmt.setInt(1, t6163.F01);
                pstmt.setInt(2, t6163.F02);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        f = true;
                        break;
                    }
                }
            }
            if (t6163.F01 > 0 && f)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6163 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ? WHERE T6163.F01 = ? AND T6163.F02 = ?"))
                {
                    pstmt.setBigDecimal(1, t6163.F03);
                    pstmt.setBigDecimal(2, t6163.F04);
                    pstmt.setBigDecimal(3, t6163.F05);
                    pstmt.setBigDecimal(4, t6163.F06);
                    pstmt.setBigDecimal(5, t6163.F07);
                    pstmt.setString(6, t6163.F08);
                    pstmt.setInt(7, t6163.F01);
                    pstmt.setInt(8, t6163.F02);
                    pstmt.execute();
                }
            }
            else
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6163 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?"))
                {
                    pstmt.setInt(1, t6163.F01);
                    pstmt.setInt(2, t6163.F02);
                    pstmt.setBigDecimal(3, t6163.F03);
                    pstmt.setBigDecimal(4, t6163.F04);
                    pstmt.setBigDecimal(5, t6163.F05);
                    pstmt.setBigDecimal(6, t6163.F06);
                    pstmt.setBigDecimal(7, t6163.F07);
                    pstmt.execute();
                }
            }
        }
    }
    
    public void updateEnterpriseLxr(T6164 t6164, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6164 (F01, F02, F03, F04, F05) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05)"))
        {
            pstmt.setInt(1, t6164.F01);
            pstmt.setInt(2, t6164.F02);
            pstmt.setString(3, t6164.F03);
            pstmt.setString(4, t6164.F04);
            pstmt.setString(5, t6164.F05);
            pstmt.execute();
        }
    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.dimeng.p2p.modules.account.console.service.EnterpriseManage#
     * getEnterpriseCw(int)
     */
    @Override
    public T6163[] getEnterpriseCw(int userId)
        throws Throwable
    {
        T6163[] records = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02, F03,F04, F05,F08 FROM S61.T6163 WHERE T6163.F01 = ? order by T6163.F02"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    List<T6163> recordList = new ArrayList<T6163>();
                    while (resultSet.next())
                    {
                        T6163 record = new T6163();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F08 = resultSet.getString(6);
                        recordList.add(record);
                    }
                    if (recordList.size() > 0)
                        records = recordList.toArray(new T6163[recordList.size()]);
                    
                }
            }
        }
        return records;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.dimeng.p2p.modules.account.console.service.EnterpriseManage#
     * getEnterpriseJs(int)
     */
    @Override
    public T6162 getEnterpriseJs(int userId)
        throws Throwable
    {
        T6162 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02, F03,F04, F05 FROM S61.T6162 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6162();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        
                    }
                }
            }
        }
        return record;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * com.dimeng.p2p.modules.account.console.service.EnterpriseManage#searchJg
     * ()
     */
    @Override
    public Jg[] searchJg()
        throws Throwable
    {
        ArrayList<Jg> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6110.F01 AS F01, CASE T6110.F06 WHEN 'ZRR' THEN concat(T6110.F02,'/',T6141.F02) ELSE T6161.F04 END AS F02"
                    + " FROM S61.T6125 INNER JOIN S61.T6110 ON T6125.F02 = T6110.F01 LEFT JOIN S61.T6161 ON T6161.F01 = T6110.F01"
                    + " LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 WHERE T6125.F05 = ? AND T6110.F07 =?"
                    + " AND T6110.F01 NOT IN (SELECT F03 FROM S62.T6252 WHERE F09='WH' AND F08 < CURRENT_DATE ()) "))
            {
                pstmt.setString(1, T6125_F05.SQCG.name());
                pstmt.setString(2, T6110_F07.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Jg record = null;
                    while (resultSet.next())
                    {
                        if (!StringHelper.isEmpty(resultSet.getString(2)) && isNetSign(resultSet.getInt(1), connection))
                        {
                            record = new Jg();
                            record.id = resultSet.getInt(1);
                            record.name = resultSet.getString(2);
                            list.add(record);
                        }
                    }
                }
            }
        }
        return list.toArray(new Jg[list.size()]);
    }
    
    @Override
    public Jg[] searchTgJg()
        throws Throwable
    {
        ArrayList<Jg> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6110.F01 AS F01, CASE T6110.F06 WHEN 'ZRR' THEN concat(T6110.F02,'/',T6141.F02)"
                    + " ELSE (SELECT T6161.F04 FROM S61.T6161 INNER JOIN S61.T6165 ON T6161.F01 = T6165.F01 AND T6165.F02 = ? WHERE T6161.F01 = T6110.F01) END AS F02"
                    + " FROM S61.T6125 INNER JOIN S61.T6110 ON T6125.F02 = T6110.F01 LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01"
                    + " WHERE T6125.F05 = ? AND T6110.F07 =? AND T6110.F01 NOT IN (SELECT F03 FROM S62.T6252 WHERE F09='WH' AND F08 < CURRENT_DATE ()) "))
            {
                pstmt.setString(1, T6165_F02.Y.name());
                pstmt.setString(2, T6125_F05.SQCG.name());
                pstmt.setString(3, T6110_F07.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Jg record = null;
                    while (resultSet.next())
                    {
                        if (!StringHelper.isEmpty(resultSet.getString(2)) && isNetSign(resultSet.getInt(1), connection))
                        {
                            record = new Jg();
                            record.id = resultSet.getInt(1);
                            record.name = resultSet.getString(2);
                            list.add(record);
                        }
                    }
                }
            }
        }
        return list.toArray(new Jg[list.size()]);
    }
    
    @Override
    public T6161 getEnterpriseByBH(String bhId)
        throws Throwable
    {
        T6161 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S61.T6161 WHERE T6161.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, bhId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6161();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        
                    }
                }
            }
        }
        return record;
    }
    
    public int getYear(int y)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, y);
        return calendar.get(Calendar.YEAR);
    }
    
    @Override
    public void updateEnterpriseOtherInfos(T6161 t6161, T6162 t6162, T6163[] t6163, T6164 t6164)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT t1.F01 FROM  S61.T6161 t1,S61.T6164 t4 WHERE t1.F01=t4.F01 AND t1.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, t6161.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new ParameterException("企业用户不存在");
                        }
                    }
                }
                if (t6162 != null)
                {
                    updateEnterpriseJs(t6162, connection);
                }
                if (t6164 != null)
                {
                    updateEnterpriseLxr(t6164, connection);
                }
                if (t6163 != null)
                {
                    updateEnterpriseCw(t6163, connection);
                }
                StringBuilder sql6161 = new StringBuilder("UPDATE S61.T6161 SET ");
                StringBuilder tempsql = new StringBuilder();
                ArrayList<Object> parameters = new ArrayList<Object>();
                String string = t6161.F09;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F09 = ?,");
                    parameters.add(string);
                }
                
                int temp = t6161.F10;
                if (temp > 0)
                {
                    tempsql.append(" F10 = ?,");
                    parameters.add(temp);
                }
                BigDecimal tempbd = t6161.F08;
                if (tempbd != null && tempbd.floatValue() > 0)
                {
                    tempsql.append(" F08 = ?,");
                    parameters.add(tempbd);
                }
                temp = t6161.F07;
                if (temp > 0)
                {
                    tempsql.append(" F07 = ?,");
                    parameters.add(temp);
                }
                if (!StringHelper.isEmpty(tempsql.toString()))
                {
                    String sqlt = "";
                    if (tempsql.toString().lastIndexOf(",") != -1)
                    {
                        sqlt = tempsql.substring(0, tempsql.length() - 1).toString();
                    }
                    else
                    {
                        sqlt = tempsql.toString();
                    }
                    
                    sql6161.append(sqlt).append(" where F01 = ? limit 1");
                    parameters.add(t6161.F01);
                    try (PreparedStatement pstmt = connection.prepareStatement(sql6161.toString()))
                    {
                        this.serviceResource.setParameters(pstmt, parameters.toArray());
                        pstmt.execute();
                    }
                    this.writeLog(connection,
                        "update",
                        String.format("Enterprise id %s update otherinfos success", t6161.F01));
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
    public T6164 getEnterpriseLxr(int userId)
        throws Throwable
    {
        T6164 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03, F05 FROM S61.T6164 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6164();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F05 = resultSet.getString(3);
                        
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public String getJgDes(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6180 WHERE T6180.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public T6180 getJgInfo(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6180 t6180 = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F04 FROM S61.T6180 WHERE T6180.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6180 = new T6180();
                        t6180.F02 = resultSet.getString(1);
                        t6180.F04 = resultSet.getString(2);
                    }
                }
            }
            return t6180;
        }
    }
    
    @Override
    public int addOrupdateHourse(T6112 entity)
        throws Throwable
    {
        if (entity == null)
        {
            throw new ParameterException("参数不能为空.");
        }
        int rtn = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                boolean isadd = true;
                if (entity.F01 > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01, F02 FROM S61.T6112 where F01=? limit 1"))
                    {
                        pstmt.setInt(1, entity.F01);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                isadd = false;
                            }
                        }
                    }
                }
                if (isadd)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6112 (F02, F03, F04, F05, F06, F07, F08, F09, F10, F11) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F07 = VALUES(F07), F08 = VALUES(F08), F09 = VALUES(F09), F10 = VALUES(F10), F11 = VALUES(F11)",
                            PreparedStatement.RETURN_GENERATED_KEYS))
                    {
                        pstmt.setInt(1, entity.F02);
                        pstmt.setString(2, entity.F03);
                        pstmt.setFloat(3, entity.F04);
                        pstmt.setInt(4, entity.F05);
                        pstmt.setBigDecimal(5, entity.F06);
                        pstmt.setBigDecimal(6, entity.F07);
                        pstmt.setInt(7, entity.F08);
                        pstmt.setString(8, entity.F09);
                        pstmt.setString(9, entity.F10);
                        pstmt.setBigDecimal(10, entity.F11);
                        pstmt.execute();
                        try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                        {
                            if (resultSet.next())
                            {
                                rtn = resultSet.getInt(1);
                            }
                        }
                    }
                }
                else
                {//
                    if (entity.F01 > 0)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("update S61.T6112 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? where F01= ? AND F02 = ?"))
                        {
                            
                            pstmt.setString(1, entity.F03);
                            pstmt.setFloat(2, entity.F04);
                            pstmt.setInt(3, entity.F05);
                            pstmt.setBigDecimal(4, entity.F06);
                            pstmt.setBigDecimal(5, entity.F07);
                            pstmt.setInt(6, entity.F08);
                            pstmt.setString(7, entity.F09);
                            pstmt.setString(8, entity.F10);
                            pstmt.setBigDecimal(9, entity.F11);
                            pstmt.setInt(10, entity.F01);
                            pstmt.setInt(11, entity.F02);
                            pstmt.execute();
                            rtn = 1;
                        }
                    }
                }
                return rtn;
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    @Override
    public int addOrupdateCar(T6113 entity)
        throws Throwable
    {
        if (entity == null)
        {
            throw new ParameterException("参数不能为空.");
        }
        int rtn = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                boolean isadd = true;
                if (entity.F01 > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01, F02 FROM S61.T6113 where F01=? limit 1"))
                    {
                        pstmt.setInt(1, entity.F01);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                isadd = false;
                            }
                        }
                    }
                }
                
                if (isadd)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6113 (F02, F03, F04, F05, F06, F07) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F07 = VALUES(F07)",
                            PreparedStatement.RETURN_GENERATED_KEYS))
                    {
                        pstmt.setInt(1, entity.F02);
                        pstmt.setString(2, entity.F03);
                        pstmt.setString(3, entity.F04);
                        pstmt.setInt(4, entity.F05);
                        pstmt.setBigDecimal(5, entity.F06);
                        pstmt.setBigDecimal(6, entity.F07);
                        pstmt.execute();
                        try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                        {
                            if (resultSet.next())
                            {
                                rtn = resultSet.getInt(1);
                            }
                        }
                    }
                }
                else
                {// 更新
                    if (entity.F01 > 0)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("update S61.T6113 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? where F01= ? and F02=? "))
                        {
                            pstmt.setString(1, entity.F03);
                            pstmt.setString(2, entity.F04);
                            pstmt.setInt(3, entity.F05);
                            pstmt.setBigDecimal(4, entity.F06);
                            pstmt.setBigDecimal(5, entity.F07);
                            pstmt.setInt(6, entity.F01);
                            pstmt.setInt(7, entity.F02);
                            pstmt.execute();
                            rtn = 1;
                        }
                    }
                }
                return rtn;
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    @Override
    public T6112[] getHourseInfo(int userId)
        throws Throwable
    {
        ArrayList<T6112> list = null;
        if (userId < 0)
        {
            throw new ParameterException("参数不能为空.");
        }
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,  F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 where F02=?"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6112 record = new T6112();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getFloat(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        record.F08 = resultSet.getInt(7);
                        record.F09 = resultSet.getString(8);
                        record.F10 = resultSet.getString(9);
                        record.F11 = resultSet.getBigDecimal(10);
                        record.F02 = userId;
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6112[list.size()]));
    }
    
    @Override
    public T6113[] getCarInfo(int userId)
        throws Throwable
    {
        ArrayList<T6113> list = null;
        if (userId < 0)
        {
            throw new ParameterException("参数不能为空.");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,  F03, F04, F05, F06, F07 FROM S61.T6113 where F02=?"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    
                    while (resultSet.next())
                    {
                        T6113 record = new T6113();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = userId;
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6113[list.size()]));
    }
    
}
