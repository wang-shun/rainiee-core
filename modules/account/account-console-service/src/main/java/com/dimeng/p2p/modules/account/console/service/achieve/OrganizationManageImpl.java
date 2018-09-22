package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.modules.account.console.service.OrganizationManage;
import com.dimeng.p2p.modules.account.console.service.entity.Organization;
import com.dimeng.p2p.modules.account.console.service.query.EnterpriseQuery;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

public class OrganizationManageImpl extends AbstractUserService implements OrganizationManage
{
    
    public OrganizationManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void add(Organization enterprise)
        throws Throwable
    {
        /*
         * String name = "P2PBH";// 固定值 String nameZH = "P2PZH";// 固定值 Date date
         * = new Date(); SimpleDateFormat sdf = new
         * SimpleDateFormat("yyyyMMddHHmmss"); String account = name +
         * sdf.format(date); nameZH=nameZH+sdf.format(date);
         */
        Date date = new Date();
        String nameZH = "P2PZH";// 固定值
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        nameZH = nameZH + sdf.format(date);
        if (StringHelper.isEmpty(enterprise.F02))
        {
            throw new ParameterException("参数错误");
        }
        if (StringHelper.isEmpty(enterprise.F08))
        {
            throw new ParameterException("参数错误8");
        }
        /*
         * if(StringHelper.isEmpty(enterprise.F09)){ throw new
         * ParameterException("参数错误9"); }
         */
        String tpass = enterprise.password;// "888888";// 默认密码
        
        String pass = UnixCrypt.crypt(tpass, DigestUtils.sha256Hex(tpass));
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
                            throw new ParameterException("机构用户账号：" + enterprise.F02 + "已经存在");
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
                    pstmt.setString(8, T6110_F10.S.name());
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys();)
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
                String t6161 =
                    "INSERT INTO S61.T6161 (F01, F02, F03, F04, F05, F06, F11, F12) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F11 = VALUES(F11), F12 = VALUES(F12)";
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
                 * 企业担保信息表
                 */
                String t6018 = "INSERT INTO S61.T6180 SET F01=?";
                
                /**
                 * 企业车产信息表
                 */
                String t6113 = "INSERT INTO S61.T6113 SET F02=?";
                
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
                
                if (enterprise.F01 > 0)
                {
                    try (PreparedStatement pstmt = connection.prepareStatement(t6161))
                    {
                        pstmt.setInt(1, enterprise.F01);
                        pstmt.setString(2, enterprise.F08);
                        // pstmt.setString(3, enterprise.F09);
                        pstmt.setString(3, nameZH);
                        pstmt.setString(4, enterprise.F10);
                        pstmt.setString(5, enterprise.F11);
                        pstmt.setString(6, enterprise.F12);
                        pstmt.setString(7, enterprise.F13);
                        pstmt.setString(8, enterprise.F14);
                        
                        pstmt.execute();
                        
                        this.execute(connection, t6013, enterprise.F01);
                        int y1 = getYear(-1);
                        int y2 = getYear(-2);
                        int y3 = getYear(-3);
                        // 财务表默认记录前3年信息
                        this.execute(connection, t6014, enterprise.F01, y1);
                        this.execute(connection, t6014, enterprise.F01, y2);
                        this.execute(connection, t6014, enterprise.F01, y3);
                        
                        this.execute(connection, t6015, enterprise.F01, enterprise.F03);
                        if (!StringHelper.isEmpty(enterprise.jgProfs))
                        {
                            t6018 = "INSERT INTO S61.T6180 SET F01=?,F02=?";
                            this.execute(connection, t6018, enterprise.F01, enterprise.jgProfs);
                        }
                        else
                        {
                            this.execute(connection, t6018, enterprise.F01);
                        }
                        
                        this.execute(connection, t6113, enterprise.F01);
                        
                        this.writeLog(connection,
                            "add",
                            String.format("Organization id %s add success", enterprise.F01));
                        serviceResource.commit(connection);
                    }
                }
                else
                {
                    serviceResource.rollback(connection);
                }
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
    public String update(Organization enterprise)
        throws Throwable
    {
        String msg = "";
        if (enterprise.F01 < 1)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT t1.F01 FROM  S61.T6161 t1,S61.T6164 t4 WHERE t1.F01=t4.F01 AND t1.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, enterprise.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new ParameterException("机构用户不存在");
                        }
                    }
                }
                if (!StringHelper.isEmpty(enterprise.F03))
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01 FROM S61.T6164 WHERE F01=? AND F06 = ?"))
                    {
                        pstmt.setInt(1, enterprise.F01);
                        pstmt.setString(2, enterprise.F03);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
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
                String string = enterprise.F14;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F12 = ?,");
                    parameters.add(string);
                }
                string = enterprise.F10;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F04 = ?,");
                    parameters.add(string);
                }
                string = enterprise.F13;
                if (!StringHelper.isEmpty(string))
                {
                    tempsql.append(" F11 = ?,");
                    parameters.add(string);
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
                    parameters.add(enterprise.F01);
                    try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
                    {
                        this.serviceResource.setParameters(pstmt, parameters.toArray());
                        
                        pstmt.execute();
                        if (!StringHelper.isEmpty(enterprise.F03))
                        {
                            String tsql = "UPDATE S61.T6164 SET F06=? where F01=?";
                            this.execute(connection, tsql, enterprise.F03, enterprise.F01);
                        }
                        if (!StringHelper.isEmpty(enterprise.jgProfs))
                        {
                            String tsql = "UPDATE S61.T6180 SET F02=? where F01=?";
                            this.execute(connection, tsql, enterprise.jgProfs, enterprise.F01);
                        }
                        this.writeLog(connection,
                            "update",
                            String.format("Organization id %s update success", enterprise.F01));
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
        return msg;
    }
    
    @Override
    public PagingResult<Organization> enterpriseSearch(EnterpriseQuery enterpriseQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6164.F06 AS F03, T6110.F05 AS F04, T6110.F06 AS F05, T6110.F07 AS F06, T6110.F09 AS F07, T6161.F02 AS F08, T6161.F03 AS F09, T6161.F04 AS F10, T6161.F05 AS F11, T6161.F06 AS F12, T6161.F11 AS F13, T6161.F12 AS F14,T6164.F07 AS F15,T6164.F04 AS F16,T6161.F03 AS F17,T6161.F19 AS F18,T6161.F20 AS F19,T6110.F17 AS F20,T6110.F19 AS F21 FROM S61.T6110 , S61.T6161, S61.T6164 where T6110.F01 = T6164.F01 AND T6110.F01 = T6161.F01 AND T6110.F06=? AND T6110.F10=?  ");
        ArrayList<Object> parameters = new ArrayList<>();
        
        if (enterpriseQuery != null)
        {
            parameters.add(T6110_F06.FZRR.name());
            parameters.add(T6110_F10.S.name());
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
            string = enterpriseQuery.getContactsMobile();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6164.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = enterpriseQuery.getName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6161.F04 LIKE ?");
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
            
            T6110_F19 badClaimType = enterpriseQuery.getBadClaimType();
            if (badClaimType != null)
            {
                sql.append(" AND T6110.F19 = ?");
                parameters.add(badClaimType.name());
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
        
        sql.append(" ORDER BY T6110.F01 DESC");
        try (Connection connection = getConnection())
        {
            PagingResult<Organization> pagingResult =
                selectPaging(connection, ORGANIZATION_PARSER, paging, sql.toString(), parameters);
            
            return pagingResult;
        }
    }
    
    @Override
    public void export(Organization[] enterprises, OutputStream outputStream, String charset)
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
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        //托管前缀
        String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        boolean isHasBadClaim =
            BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("机构账号");
            writer.write("机构名称");
            writer.write("营业执照/社会信用代码");
            writer.write("联系人姓名");
            writer.write("联系人电话");
            writer.write("注册时间");
            writer.write("状态");
            if (!"huifu".equals(ESCROW_PREFIX))
            {
                writer.write("是否允许投资");
            }
            if (isHasBadClaim)
            {
                writer.write("是否允许购买不良债权");
            }
            writer.newLine();
            int index = 1;
            
            for (Organization enterprise : enterprises)
            {
                writer.write(index++);
                writer.write(enterprise.F02);
                writer.write(enterprise.F10);
                writer.write("Y".equals(enterprise.isShxydm) ? enterprise.shxydm : enterprise.licenseNumber);
                writer.write(enterprise.lxr);
                writer.write(enterprise.lxrPhone);
                writer.write(DateTimeParser.format(enterprise.F07) + "\t");
                writer.write(enterprise.F06.getChineseName());
                if (!"huifu".equals(ESCROW_PREFIX))
                {
                    writer.write(enterprise.investType.getChineseName());
                }
                if (isHasBadClaim)
                {
                    writer.write(enterprise.badClaimType.getChineseName());
                }
                writer.newLine();
            }
        }
    }
    
    protected static final ArrayParser<Organization> ORGANIZATION_PARSER = new ArrayParser<Organization>()
    {
        @Override
        public Organization[] parse(ResultSet rs)
            throws SQLException
        {
            ArrayList<Organization> list = null;
            while (rs.next())
            {
                Organization record = new Organization();
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
                record.licenseNumber = rs.getString(17);
                record.shxydm = rs.getString(18);
                record.isShxydm = rs.getString(19);
                record.investType = T6110_F17.parse(rs.getString(20));
                record.badClaimType = T6110_F19.parse(rs.getString(21));
                if (list == null)
                {
                    list = new ArrayList<Organization>();
                }
                list.add(record);
                
            }
            return list == null ? null : list.toArray(new Organization[list.size()]);
        }
    };
    
    @Override
    public Organization getEnterprise(int userId)
        throws Throwable
    {
        Organization organization = null;
        if (userId < 1)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t1.F01, t1.F02, t1.F03, t1.F04, t1.F05, t1.F06, t1.F07, t1.F08, t1.F09, t1.F10, t1.F11, t1.F12, t1.F13, t1.F14, t1.F15,t4.F06 AS F16,t8.F02 AS F17 ,t0.F02 AS F18 FROM S61.T6110 t0,S61.T6161 t1,S61.T6164 t4,S61.T6180 t8  WHERE t0.F01=t1.F01 AND t1.F01=t4.F01 AND t1.F01=t8.F01 AND t1.F01 = ?  LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        organization = new Organization();
                        organization.F01 = resultSet.getInt(1);
                        organization.F08 = resultSet.getString(2);
                        organization.F10 = resultSet.getString(4);
                        
                        organization.F13 = resultSet.getString(11);
                        organization.F14 = resultSet.getString(12);
                        organization.F03 = resultSet.getString(16);
                        organization.jgProfs = resultSet.getString(17);
                        organization.F02 = resultSet.getString(18);
                    }
                }
            }
        }
        return organization;
    }
    
    public int getYear(int y)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, y);
        return calendar.get(Calendar.YEAR);
    }
    
}
