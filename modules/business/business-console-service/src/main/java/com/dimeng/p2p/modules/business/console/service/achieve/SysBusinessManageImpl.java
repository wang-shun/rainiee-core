package com.dimeng.p2p.modules.business.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7190;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.BusinessOptLog;
import com.dimeng.p2p.repeater.business.entity.CustomerEntity;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.entity.Results;
import com.dimeng.p2p.repeater.business.entity.SysUser;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.p2p.repeater.business.query.CustomerQuery;
import com.dimeng.p2p.repeater.business.query.PerformanceQuery;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.p2p.repeater.business.query.SysUserQuery;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
public class SysBusinessManageImpl extends AbstractBusinessService implements SysBusinessManage
{
    
    public SysBusinessManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int getMaxEmployNum()
        throws Throwable
    {
        int num = 1;
        String sql = "SELECT  IFNULL(MAX(LPAD(SUBSTRING(t.F12,3),5,'0')+1),1) from S71.T7110 AS  t ";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                try (ResultSet rst = pstmt.executeQuery())
                {
                    if (rst.next())
                    {
                        num = rst.getInt(1);
                    }
                }
            }
        }
        return num;
    }
    
    @Override
    public List<SysUser> getEmployNumUsers(SysUserQuery query)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder();
        String sql =
            "SELECT F01, F02, F04, F05, F06,F07, F08, F09,F10,F11,F12 FROM S71.T7110 WHERE T7110.F12 IS NOT NULL ";
        sb.append(sql);
        
        if (query != null)
        {
            String accountName = query.getAccountName();
            if (!StringHelper.isEmpty(accountName))
            {
                sb.append(" AND (T7110.F02 LIKE ? OR T7110.F04 LIKE ? OR T7110.F12 LIKE ?)");
            }
            
        }
        sb.append(" ORDER BY T7110.F12 ASC");
        List<SysUser> list = new ArrayList<SysUser>();
        try (Connection conn = getConnection("S71"))
        {
            
            try (PreparedStatement pst = conn.prepareStatement(sb.toString()))
            {
                if (query != null)
                {
                    String accountName = query.getAccountName();
                    if (!StringHelper.isEmpty(accountName))
                    {
                        pst.setString(1, getSQLConnectionProvider().allMatch(accountName));
                        pst.setString(2, getSQLConnectionProvider().allMatch(accountName));
                        pst.setString(3, getSQLConnectionProvider().allMatch(accountName));
                    }
                    
                }
                
                try (ResultSet rs = pst.executeQuery())
                {
                    while (rs.next())
                    {
                        SysUser sysUser = new SysUser();
                        sysUser.id = rs.getInt(1);
                        sysUser.accountName = rs.getString(2);
                        sysUser.name = rs.getString(3);
                        sysUser.status = EnumParser.parse(SysAccountStatus.class, rs.getString(4));
                        sysUser.createTime = rs.getTimestamp(5);
                        sysUser.lastTime = rs.getTimestamp(6);
                        sysUser.lastIp = rs.getString(7);
                        //sysUser.roleName = rs.getString(8);
                        sysUser.phone = rs.getString(9);
                        sysUser.pos = rs.getString(10);
                        sysUser.employNum = rs.getString(11);
                        list.add(sysUser);
                        
                    }
                }
            }
        }
        return list;
    }
    
    @Override
    public PagingResult<SysUser> seachBusinessUser(BusinessUserQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String sql =
            "SELECT F01, F02, F04, F05, F06,F07, F08, F09,F10,F11,F12,F13 FROM S71.T7110 WHERE T7110.F12 IS NOT NULL ";
        sb.append(sql);
        if (query != null)
        {
            String accountName = query.getAccountName();
            String employNum = query.getEmployNum();
            String name = query.getName();
            SysAccountStatus stauts = query.getStatus();
            if (!StringHelper.isEmpty(accountName))
            {
                sb.append(" AND T7110.F02 LIKE ? ");
                
                parameters.add(getSQLConnectionProvider().allMatch(accountName));
            }
            
            if (!StringHelper.isEmpty(employNum))
            {
                sb.append(" AND T7110.F12 LIKE ? ");
                
                parameters.add(getSQLConnectionProvider().allMatch(employNum));
            }
            
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T7110.F04 LIKE ? ");
                
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            
            if (stauts != null)
            {
                sb.append(" AND T7110.F05 = ? ");
                
                parameters.add(stauts.name());
            }
            if (Constant.BUSINESS_ROLE_ID == query.getRoleId())
            {
                sb.append(" AND T7110.F01 = ? ");
                parameters.add(serviceResource.getSession().getAccountId());
            }
            if (!StringHelper.isEmpty(query.getDept()))
            {
                sb.append(" AND T7110.F13 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(query.getDept()));
            }
        }
        
        sb.append(" ORDER BY T7110.F06 DESC");
        
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SysUser>()
            {
                
                @Override
                public SysUser[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<SysUser> list = new ArrayList<SysUser>();
                    while (rs.next())
                    {
                        SysUser sysUser = new SysUser();
                        sysUser.id = rs.getInt(1);
                        sysUser.accountName = rs.getString(2);
                        sysUser.name = rs.getString(3);
                        sysUser.status = EnumParser.parse(SysAccountStatus.class, rs.getString(4));
                        sysUser.createTime = rs.getTimestamp(5);
                        sysUser.lastTime = rs.getTimestamp(6);
                        sysUser.lastIp = rs.getString(7);
                        //sysUser.roleName = rs.getString(8);
                        sysUser.phone = rs.getString(9);
                        sysUser.pos = rs.getString(10);
                        sysUser.employNum = rs.getString(11);
                        sysUser.customNum = getCustomNum(connection, sysUser.employNum);
                        sysUser.dept = rs.getString(12);
                        list.add(sysUser);
                    }
                    return list.toArray(new SysUser[list.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
        
    }
    
    protected static final ArrayParser<SysUser> ARRAY_PARSER = new ArrayParser<SysUser>()
    {
        
        @Override
        public SysUser[] parse(ResultSet rs)
            throws SQLException
        {
            List<SysUser> list = new ArrayList<SysUser>();
            while (rs.next())
            {
                SysUser sysUser = new SysUser();
                sysUser.id = rs.getInt(1);
                sysUser.accountName = rs.getString(2);
                sysUser.name = rs.getString(3);
                sysUser.status = EnumParser.parse(SysAccountStatus.class, rs.getString(4));
                sysUser.createTime = rs.getTimestamp(5);
                sysUser.lastTime = rs.getTimestamp(6);
                sysUser.lastIp = rs.getString(7);
                //sysUser.roleName = rs.getString(8);
                sysUser.phone = rs.getString(9);
                sysUser.pos = rs.getString(10);
                sysUser.employNum = rs.getString(11);
                sysUser.dept = rs.getString(12);
                list.add(sysUser);
            }
            return list.toArray(new SysUser[list.size()]);
        }
    };
    
    @Override
    public Performance findPerformance(PerformanceQuery query)
        throws Throwable
    {
        final Performance findPerformance = new Performance();
        /*
        if (StringHelper.isEmpty(query.getEmployNum()))
        {
            
            throw new ParameterException("参数值不能为空！");
        }*/
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T7110.F04,T7110.F12 FROM S71.T7110 WHERE S71.T7110.F12=?"))
            {
                ps.setString(1, query.getEmployNum());
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        findPerformance.name = rs.getString(1);
                        findPerformance.employNum = rs.getString(2);
                        
                    }
                }
            }
            
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(F01) FROM S61.T6110 WHERE T6110.F14=?"))
            {
                ps.setString(1, query.getEmployNum());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        findPerformance.levelCustomerNumber = rs.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(F01) FROM S61.T6111 WHERE T6111.F03 IN (SELECT T6111.F02 FROM S61.T6111 WHERE T6111.F01 IN (SELECT T6110.F01 FROM S61.T6110 WHERE T6110.F14=?))"))
            {
                ps.setString(1, query.getEmployNum());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        findPerformance.secondaryCustomerNumber = rs.getInt(1);
                    }
                }
            }
            
            List<Object> params = new ArrayList<Object>();
            
            StringBuilder sql =
                new StringBuilder(
                    "SELECT SUM(T7190.F01),SUM(T7190.F02),SUM(T7190.F03),SUM(T7190.F04), SUM(T7190.F05) AS F05, SUM(T7190.F06) AS F06, SUM(T7190.F07) AS F07, SUM(T7190.F08) AS F08 FROM S71.T7190 LEFT JOIN S71.T7110 ON T7190.F11 = T7110.F12 WHERE 1=1 ");
            if (!StringHelper.isEmpty(query.getEmployNum()))
            {
                sql.append(" AND T7190.F11 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getEmployNum()));
            }
            if (query.getCreateTimeStart() != null)
            {
                sql.append(" AND DATE(T7190.F10) >= ? ");
                params.add(query.getCreateTimeStart());
                
            }
            if (query.getCreateTimeEnd() != null)
            {
                sql.append(" AND DATE(T7190.F10) <= ?");
                params.add(query.getCreateTimeEnd());
                
            }
            
            if (!StringHelper.isEmpty(query.getName()))
            {
                sql.append(" AND T7110.F04 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getName()));
            }
            
            select(conn, new ItemParser<Performance>()
            
            {
                
                @Override
                public Performance parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        findPerformance.levelInvestmentAmount = rs.getDouble(1);
                        findPerformance.levelLoanAmount = rs.getDouble(2);
                        findPerformance.levelChargeAmount = rs.getDouble(3);
                        findPerformance.levelWithDrawAmount = rs.getDouble(4);
                        findPerformance.secondaryInvestmentAmount = rs.getDouble(5);
                        findPerformance.secondaryLoanAmount = rs.getDouble(6);
                        findPerformance.secondChargeAmount = rs.getDouble(7);
                        findPerformance.secondWithDrawAmount = rs.getDouble(8);
                    }
                    return findPerformance;
                }
            }, sql.toString(), params);
            
        }
        return findPerformance;
    }
    
    @Override
    public PagingResult<T7190> serarchTbjl(PerformanceQuery query, Paging paging)
        throws Throwable
    {
        
        String sql =
            "SELECT T7190.F10 AS F01,T7190.F01 AS F02,T7190.F02 AS F03,T7190.F03 AS F04,T7190.F04 AS F05,T7190.F05 AS F06,T7190.F06 AS F07,T7190.F07 AS F08,T7190.F08 AS F09 FROM S71.T7190 WHERE 1=1 ";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String employNum = query.getEmployNum();
            Timestamp startTime = query.getCreateTimeStart();
            Timestamp endTime = query.getCreateTimeEnd();
            if (!StringHelper.isEmpty(employNum))
            {
                sb.append("  AND T7190.F11=?");
                parameters.add(employNum);
                
            }
            
            if (startTime != null)
            {
                sb.append(" AND DATE(T7190.F10)>=?");
                parameters.add(startTime);
            }
            if (endTime != null)
            {
                sb.append(" AND DATE(T7190.F10)<=?");
                parameters.add(endTime);
            }
        }
        sb.append(" AND (T7190.F01 > 0 OR T7190.F02 > 0  OR T7190.F03 > 0 OR T7190.F04 > 0 OR T7190.F05 > 0 OR T7190.F06 > 0 OR T7190.F07 > 0 OR T7190.F08 > 0 )");
        sb.append(" ORDER BY T7190.F10 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7190>()
            {
                
                @Override
                public T7190[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<T7190> t7190 = new ArrayList<>();
                    while (rs.next())
                    {
                        
                        T7190 record = new T7190();
                        record.F01 = rs.getDate(1);
                        record.F02 = rs.getDouble(2);
                        record.F03 = rs.getDouble(3);
                        record.F04 = rs.getDouble(4);
                        record.F05 = rs.getDouble(5);
                        record.F06 = rs.getDouble(6);
                        record.F07 = rs.getDouble(7);
                        record.F08 = rs.getDouble(8);
                        record.F09 = rs.getDouble(9);
                        t7190.add(record);
                    }
                    return ((t7190 == null || t7190.size() == 0) ? null : t7190.toArray(new T7190[t7190.size()]));
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<T7190> serarchTjgl(PerformanceQuery query, Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T7190.F10 AS F01,T7190.F11 AS F02,T7110.F04 AS F03,T7190.F01 AS F04,T7190.F02 AS F05,T7190.F03 AS F06,T7190.F04 AS F07, T7190.F05 AS F08, T7190.F06 AS F09, T7190.F07 AS F10, T7190.F08 AS F11 FROM S71.T7190 LEFT JOIN S71.T7110 ON T7110.F12=T7190.F11 WHERE 1=1 ";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String employNum = query.getEmployNum();
            String name = query.getName();
            Timestamp startTime = query.getCreateTimeStart();
            Timestamp endTime = query.getCreateTimeEnd();
            
            if (!StringHelper.isEmpty(employNum))
            {
                sb.append("  AND T7190.F11 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(employNum));
                
            }
            if (!StringHelper.isEmpty(name))
            {
                sb.append("  AND T7110.F04 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(name));
                
            }
            
            if (startTime != null)
            {
                sb.append(" AND DATE(T7190.F10) >= ? ");
                parameters.add(startTime);
            }
            if (endTime != null)
            {
                sb.append(" AND DATE(T7190.F10) <= ? ");
                parameters.add(endTime);
            }
        }
        sb.append(" AND (T7190.F01 > 0 OR T7190.F02 > 0  OR T7190.F03 > 0 OR T7190.F04 > 0 OR T7190.F05 > 0 OR T7190.F06 > 0 OR T7190.F07 > 0 OR T7190.F08 > 0)");
        sb.append(" ORDER BY T7190.F10 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7190>()
            {
                
                @Override
                public T7190[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<T7190> t7190 = new ArrayList<>();
                    while (rs.next())
                    {
                        
                        T7190 record = new T7190();
                        record.F01 = rs.getDate(1);
                        record.employNum = rs.getString(2);
                        record.name = rs.getString(3);
                        record.F02 = rs.getDouble(4);
                        record.F03 = rs.getDouble(5);
                        record.F04 = rs.getDouble(6);
                        record.F05 = rs.getDouble(7);
                        record.F06 = rs.getDouble(8);
                        record.F07 = rs.getDouble(9);
                        record.F08 = rs.getDouble(10);
                        record.F09 = rs.getDouble(11);
                        t7190.add(record);
                        
                    }
                    return ((t7190 == null || t7190.size() == 0) ? null : t7190.toArray(new T7190[t7190.size()]));
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public Performance findPerformances()
        throws Throwable
    {
        Performance findPerformance = new Performance();
        
        try (Connection conn = getConnection())
        
        {
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT SUM(T7190.F01) AS F01,SUM(T7190.F02) AS F02,SUM(T7190.F03) AS F03,SUM(T7190.F04) AS F04, SUM(T7190.F05) AS F05, SUM(T7190.F06) AS F06, SUM(T7190.F07) AS F07, SUM(T7190.F08) AS F08 FROM S71.T7190 LEFT JOIN S71.T7110 ON T7110.F12 = T7190.F11 "))
            {
                try (ResultSet rs = ps.executeQuery())
                
                {
                    if (rs.next())
                    {
                        findPerformance.levelInvestmentAmount = rs.getDouble(1);
                        findPerformance.levelLoanAmount = rs.getDouble(2);
                        findPerformance.levelChargeAmount = rs.getDouble(3);
                        findPerformance.levelWithDrawAmount = rs.getDouble(4);
                        findPerformance.secondaryInvestmentAmount = rs.getDouble(5);
                        findPerformance.secondaryLoanAmount = rs.getDouble(6);
                        findPerformance.secondChargeAmount = rs.getDouble(7);
                        findPerformance.secondWithDrawAmount = rs.getDouble(8);
                    }
                }
            }
            
            return findPerformance;
        }
    }
    
    @Override
    public SysUser[] getEmployUsers(BusinessUserQuery query)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder();
        String sql =
            "SELECT F01, F02, F04, F05, F06,F07, F08, F09,F10,F11,F12 FROM S71.T7110 WHERE T7110.F12 IS NOT NULL ";
        sb.append(sql);
        List<Object> params = new ArrayList<Object>();
        if (query != null)
        {
            String accountName = query.getAccountName();
            if (!StringHelper.isEmpty(accountName))
            {
                sb.append(" AND (T7110.F02 LIKE ? OR T7110.F04 LIKE ? OR T7110.F12 LIKE ?)");
                params.add(getSQLConnectionProvider().allMatch(accountName));
                params.add(getSQLConnectionProvider().allMatch(accountName));
                params.add(getSQLConnectionProvider().allMatch(accountName));
                
            }
            if (query.getStatus() != null)
            {
                sb.append(" AND T7110.F05 = ? ");
                params.add(query.getStatus().name());
            }
            
            if (!StringHelper.isEmpty(query.getEmployNum()))
            {
                sb.append(" AND T7110.F12 <> ? ");
                params.add(query.getEmployNum());
            }
            
        }
        sb.append(" ORDER BY T7110.F12 ASC");
        
        try (Connection conn = getConnection("S71"))
        {
            return selectAll(conn, new ArrayParser<SysUser>()
            {
                List<SysUser> list = new ArrayList<SysUser>();
                
                @Override
                public SysUser[] parse(ResultSet rs)
                    throws SQLException
                {
                    while (rs.next())
                    {
                        SysUser sysUser = new SysUser();
                        sysUser.id = rs.getInt(1);
                        sysUser.accountName = rs.getString(2);
                        sysUser.name = rs.getString(3);
                        sysUser.status = EnumParser.parse(SysAccountStatus.class, rs.getString(4));
                        sysUser.createTime = rs.getTimestamp(5);
                        sysUser.lastTime = rs.getTimestamp(6);
                        sysUser.lastIp = rs.getString(7);
                        //sysUser.roleName = rs.getString(8);
                        sysUser.phone = rs.getString(9);
                        sysUser.pos = rs.getString(10);
                        sysUser.employNum = rs.getString(11);
                        list.add(sysUser);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SysUser[list.size()]);
                }
                
            }, sb.toString(), params);
        }
    }
    
    @Override
    public BusinessOptLog[] searchLogs(String employNum)
        throws Throwable
    {
        StringBuffer sb = new StringBuffer("SELECT F01, F02 ,F03 ,F04 ,F05 FROM S71.T7111 WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(employNum))
        {
            sb.append(" AND F02 = ?");
            params.add(employNum);
        }
        sb.append(" ORDER BY F03 DESC LIMIT 15");
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<BusinessOptLog>()
            {
                List<BusinessOptLog> optList = new ArrayList<BusinessOptLog>();
                
                @Override
                public BusinessOptLog[] parse(ResultSet rs)
                    throws SQLException
                {
                    while (rs.next())
                    {
                        BusinessOptLog log = new BusinessOptLog();
                        log.id = rs.getInt(1);
                        log.employNum = rs.getString(2);
                        log.lockTime = rs.getTimestamp(3);
                        log.unLockTime = rs.getTimestamp(4);
                        log.status = rs.getString(5);
                        optList.add(log);
                        
                    }
                    return optList == null || optList.size() == 0 ? null
                        : optList.toArray(new BusinessOptLog[optList.size()]);
                }
                
            },
                sb.toString(),
                params);
        }
    }
    
    @Override
    public Map<String, Object> searchAll(ResultsQuery query, Paging paging)
        throws Throwable
    {
        Map<String, Object> result = new HashMap<String, Object>();
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        ArrayList<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlAll = new StringBuilder();
        StringBuffer loanSql = new StringBuffer("SELECT SUM(TEMP.F06) FROM (");
        StringBuffer investSql = new StringBuffer("SELECT SUM(TEMP.F06) FROM (");
        StringBuffer chargeSql = new StringBuffer("SELECT SUM(TEMP.F06) FROM (");
        StringBuffer withdrawSql = new StringBuffer("SELECT SUM(TEMP.F06) FROM (");
        sqlAll.append("SELECT * FROM (");
        /*if (query == null || StringHelper.isEmpty(query.getNamelevel()) || "1".equals(query.getNamelevel()))
        {*/
        sql.append(" SELECT T7110.F12 AS F01,T7110.F04 AS F02, T6110.F02 AS F03,T6230.F25 AS F04,T6230.F03 AS F05,(T6230.F05 - T6230.F07) AS F06,T6231.F12 AS F07,'loan' AS F08,'一级用户' AS F09,T6110.F06 AS F10, T6110.F10 AS F11,T6110.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S62.T6230");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        sql.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" LEFT JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S71.T7110 ON T6231.F31 = T7110.F12");
        sql.append(" WHERE  T6231.F31 IS NOT NULL AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR') AND T6110.F14 IS NOT NULL AND T6231.F32 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01,T7110.F04 AS F02, T6110.F02 AS F03,T6230.F25 AS F04,T6230.F03 AS F05,T6250.F04 AS F06,T6231.F12 AS F07,'invest' AS F08,'一级用户' AS F09,T6110.F06 AS F10, T6110.F10 AS F11,T6110.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S62.T6250");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6250.F03");
        sql.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" LEFT JOIN S62.T6230 ON T6230.F01 = T6250.F02");
        sql.append(" LEFT JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S71.T7110 ON T6250.F12 = T7110.F12");
        /* sql.append(" LEFT JOIN S65.T6504 ON T6250.F01 = T6504.F05 AND T6504.F03 = T6230.F01 AND T6504.F02 = T6110.F01 ");*/
        sql.append(" WHERE T6250.F12 IS NOT NULL AND T6250.F08 = 'S' AND T6110.F14 IS NOT NULL AND T6250.F13 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110.F02 AS F03,'' AS F04,'' AS F05, T6502.F03 AS F06,T6501.F06 AS F07,'charge' AS F08, '一级用户' AS F09, T6110.F06 AS F10, T6110.F10 AS F11,T6110.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S65.T6502 ");
        sql.append(" INNER JOIN S71.T7110 ON T6502.F09 = T7110.F12 ");
        sql.append(" LEFT JOIN S61.T6110 ON T6110.F01 = T6502.F02 ");
        sql.append(" LEFT JOIN S65.T6501 ON T6501.F01 = T6502.F01 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" WHERE T6502.F09 IS NOT NULL AND T6501.F03 = 'CG' AND T6110.F14 IS NOT NULL AND T6502.F10 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110.F02 AS F03,'' AS F04,'' AS F05, T7150.F04 AS F06,T7150.F10 AS F07,'charge' AS F08, '一级用户' AS F09, T6110.F06 AS F10, T6110.F10 AS F11,T6110.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S71.T7150 ");
        sql.append(" INNER JOIN S71.T7110 ON T7150.F16 = T7110.F12 ");
        sql.append(" LEFT JOIN S61.T6110 ON T6110.F01 = T7150.F02 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" WHERE T7150.F16 IS NOT NULL AND T7150.F05 = 'YCZ' AND T6110.F14 IS NOT NULL AND T7150.F17 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110.F02 AS F03,'' AS F04,'' AS F05, T6130.F04 AS F06,T6130.F14 AS F07,'withdraw' AS F08, '一级用户' AS F09, T6110.F06 AS F10, T6110.F10 AS F11,T6110.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S61.T6130 ");
        sql.append(" INNER JOIN S71.T7110 ON T6130.F17 = T7110.F12 ");
        sql.append(" LEFT JOIN S61.T6110 ON T6110.F01 = T6130.F02 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" WHERE T6130.F17 IS NOT NULL AND T6130.F09 = 'YFK' AND T6110.F14 IS NOT NULL AND T6130.F18 = 'QY' ");
        
        // }
        /*if (query == null || StringHelper.isEmpty(query.getNamelevel()) && StringHelper.isEmpty(query.getNamelevel()))
        {*/
        sql.append(" UNION ALL");
        // }
        /*if (query == null || StringHelper.isEmpty(query.getNamelevel()) || "2".equals(query.getNamelevel()))
        {*/
        sql.append(" SELECT T7110.F12 AS F01,T7110.F04 AS F02, T6110_1.F02 AS F03,T6230.F25 AS F04,T6230.F03 AS F05,(T6230.F05 - T6230.F07) AS F06,T6231.F12 AS F07,'loan' AS F08,'二级用户' AS F09,T6110_1.F03 AS F10, T6110_1.F04 AS F11,T6110_2.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S62.T6230");
        sql.append(" INNER JOIN (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F06 AS F03,T6110.F10 AS F04,T6111.F03 AS F05 FROM S61.T6110 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 AND T6111.F03 IS NOT NULL WHERE T6110.F14 IS NULL) T6110_1 ON T6110_1.F01 = T6230.F02");
        sql.append(" LEFT JOIN S61.T6141 ON T6110_1.F01 = T6141.F01");
        sql.append(" LEFT JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S71.T7110 ON T6231.F31 = T7110.F12");
        sql.append(" LEFT JOIN S61.T6111 ON T6111.F02 = T6110_1.F05");
        sql.append(" LEFT JOIN S61.T6110 AS T6110_2 ON T6110_2.F01 = T6111.F01");
        sql.append(" WHERE  T6231.F31 IS NOT NULL AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR') AND T6231.F32 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01,T7110.F04 AS F02, T6110_1.F02 AS F03,T6230.F25 AS F04,T6230.F03 AS F05,T6250.F04 AS F06,T6231.F12 AS F07,'invest' AS F08,'二级用户' AS F09,T6110_1.F03 AS F10, T6110_1.F04 AS F11, T6110_2.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S62.T6250 ");
        sql.append(" INNER JOIN (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F06 AS F03,T6110.F10 AS F04,T6111.F03 AS F05  FROM S61.T6110 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 AND T6111.F03 IS NOT NULL WHERE T6110.F14 IS NULL) T6110_1 ON T6110_1.F01 = T6250.F03");
        sql.append(" LEFT JOIN S61.T6141 ON T6110_1.F01 = T6141.F01");
        sql.append(" LEFT JOIN S62.T6230 ON T6230.F01 = T6250.F02");
        sql.append(" LEFT JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S71.T7110 ON T6250.F12 = T7110.F12");
        sql.append(" LEFT JOIN S61.T6111 ON T6111.F02 = T6110_1.F05");
        sql.append(" LEFT JOIN S61.T6110 AS T6110_2 ON T6110_2.F01 = T6111.F01");
        /* sql.append(" LEFT JOIN S65.T6504 ON T6250.F01 = T6504.F05 AND T6504.F03 = T6230.F01 AND T6504.F02 = T6110_1.F01 ");*/
        sql.append(" WHERE T6250.F12 IS NOT NULL AND T6250.F08 = 'S' AND T6250.F13 = 'QY'");
        
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110_1.F02 AS F03,'' AS F04,'' AS F05, T6502.F03 AS F06,T6501.F06 AS F07,'charge' AS F08, '二级用户' AS F09, T6110_1.F03 AS F10, T6110_1.F04 AS F11,T6110_2.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S65.T6502 ");
        sql.append(" INNER JOIN S71.T7110 ON T6502.F09 = T7110.F12 ");
        sql.append(" INNER JOIN (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F06 AS F03,T6110.F10 AS F04,T6111.F03 AS F05 FROM S61.T6110 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 AND T6111.F03 IS NOT NULL WHERE T6110.F14 IS NULL) T6110_1 ON T6110_1.F01 = T6502.F02 ");
        sql.append(" LEFT JOIN S65.T6501 ON T6501.F01 = T6502.F01 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110_1.F01 = T6141.F01");
        sql.append(" LEFT JOIN S61.T6111 ON T6111.F02 = T6110_1.F05");
        sql.append(" LEFT JOIN S61.T6110 AS T6110_2 ON T6110_2.F01 = T6111.F01");
        sql.append(" WHERE T6502.F09 IS NOT NULL AND T6501.F03 = 'CG' AND T6502.F10 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110_1.F02 AS F03,'' AS F04,'' AS F05, T7150.F04 AS F06,T7150.F10 AS F07,'charge' AS F08, '二级用户' AS F09, T6110_1.F03 AS F10, T6110_1.F04 AS F11,T6110_2.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13 ");
        sql.append(" FROM S71.T7150 ");
        sql.append(" INNER JOIN S71.T7110 ON T7150.F16 = T7110.F12 ");
        sql.append(" INNER JOIN (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F06 AS F03,T6110.F10 AS F04,T6111.F03 AS F05 FROM S61.T6110 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 AND T6111.F03 IS NOT NULL WHERE T6110.F14 IS NULL) T6110_1 ON T6110_1.F01 = T7150.F02 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110_1.F01 = T6141.F01");
        sql.append(" LEFT JOIN S61.T6111 ON T6111.F02 = T6110_1.F05");
        sql.append(" LEFT JOIN S61.T6110 AS T6110_2 ON T6110_2.F01 = T6111.F01");
        sql.append(" WHERE T7150.F16 IS NOT NULL AND T7150.F05 = 'YCZ'  AND T7150.F17 = 'QY' ");
        sql.append(" UNION ALL");
        sql.append(" SELECT T7110.F12 AS F01, T7110.F04 AS F02, T6110_1.F02 AS F03,'' AS F04,'' AS F05, T6130.F04 AS F06,T6130.F14 AS F07,'withdraw' AS F08, '二级用户' AS F09, T6110_1.F03 AS F10, T6110_1.F04 AS F11,T6110_2.F02 AS F12 ");
        sql.append(" , T6141.F02 AS F13  ");
        sql.append(" FROM S61.T6130 ");
        sql.append(" INNER JOIN S71.T7110 ON T6130.F17 = T7110.F12 ");
        sql.append(" INNER JOIN (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F06 AS F03,T6110.F10 AS F04,T6111.F03 AS F05 FROM S61.T6110 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 AND T6111.F03 IS NOT NULL WHERE T6110.F14 IS NULL) T6110_1 ON T6110_1.F01 = T6130.F02 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6110_1.F01 = T6141.F01");
        sql.append(" LEFT JOIN S61.T6111 ON T6111.F02 = T6110_1.F05");
        sql.append(" LEFT JOIN S61.T6110 AS T6110_2 ON T6110_2.F01 = T6111.F01");
        sql.append(" WHERE T6130.F17 IS NOT NULL AND T6130.F09 = 'YFK' AND T6130.F18 = 'QY' ");
        // }
        sql.append(" ) TEMP WHERE 1=1 ");
        sqlAll.append(sql);
        loanSql.append(sql);
        investSql.append(sql);
        chargeSql.append(sql);
        withdrawSql.append(sql);
        loanSql.append(" AND TEMP.F08 = 'loan' ");
        investSql.append(" AND TEMP.F08 = 'invest' ");
        chargeSql.append(" AND TEMP.F08 = 'charge' ");
        withdrawSql.append(" AND TEMP.F08 = 'withdraw' ");
        StringBuilder conditionSql = new StringBuilder();
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.getEmployNum()))
            {
                conditionSql.append(" AND TEMP.F01 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getEmployNum()));
            }
            if (!StringHelper.isEmpty(query.getName()))
            {
                conditionSql.append(" AND TEMP.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getName()));
            }
            if (!StringHelper.isEmpty(query.getCustomName()))
            {
                conditionSql.append(" AND TEMP.F03 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getCustomName()));
            }
            if (!StringHelper.isEmpty(query.getProject()))
            {
                conditionSql.append(" AND TEMP.F04 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getProject()));
            }
            if (query.getCreateTimeStart() != null)
            {
                conditionSql.append(" AND DATE(TEMP.F07) >= ? ");
                parameters.add(query.getCreateTimeStart());
            }
            if (query.getCreateTimeEnd() != null)
            {
                conditionSql.append(" AND DATE(TEMP.F07) <= ? ");
                parameters.add(query.getCreateTimeEnd());
            }
            
            if (!StringHelper.isEmpty(query.getUserNameLevel()))
            {
                conditionSql.append(" AND TEMP.F12 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getUserNameLevel()));
            }
            
            if (!StringHelper.isEmpty(query.getCustomRealName()))
            {
                conditionSql.append(" AND TEMP.F13 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.getCustomRealName()));
            }
            
            if (!StringHelper.isEmpty(query.getTradeType()))
            {
                conditionSql.append(" AND TEMP.F08 = ? ");
                parameters.add(query.getTradeType());
            }
            
            if (!StringHelper.isEmpty(query.getNamelevel()))
            {
                conditionSql.append(" AND TEMP.F09 = ? ");
                parameters.add(query.getNamelevel());
            }
        }
        
        conditionSql.append(" ORDER BY TEMP.F07 DESC");
        sqlAll.append(conditionSql);
        loanSql.append(conditionSql);
        investSql.append(conditionSql);
        chargeSql.append(conditionSql);
        withdrawSql.append(conditionSql);
        try (Connection connection = getConnection())
        {
            PagingResult<Results> items = selectPaging(connection, new ArrayParser<Results>()
            {
                
                @Override
                public Results[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Results> list = null;
                    while (rs.next())
                    {
                        Results record = new Results();
                        record.employNum = rs.getString(1);
                        record.name = rs.getString(2);
                        record.customName = rs.getString(3);
                        record.projectID = rs.getString(4);
                        record.amount = rs.getDouble(6);
                        record.showTime = rs.getTimestamp(7);
                        record.tradeType = rs.getString(8);
                        record.namelevel = rs.getString(9);
                        record.firstCustomName = rs.getString(12);
                        record.customRealName = rs.getString(13);
                        /* record.orderId = rs.getInt(14);*/
                        if (list == null)
                        {
                            list = new ArrayList<Results>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Results[list.size()]));
                }
            }, paging, sqlAll.toString(), parameters);
            result.put("pagItems", items);
            Double loanTotalAmount = select(connection, new ItemParser<Double>()
            {
                
                @Override
                public Double parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getDouble(1);
                    }
                    return 0.00;
                }
            }, loanSql.toString(), parameters);
            
            result.put("loanTotalAmount", loanTotalAmount);
            
            Double investTotalAmount = select(connection, new ItemParser<Double>()
            {
                
                @Override
                public Double parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getDouble(1);
                    }
                    return 0.00;
                }
            }, investSql.toString(), parameters);
            result.put("investTotalAmount", investTotalAmount);
            
            Double chargeTotalAmount = select(connection, new ItemParser<Double>()
            {
                
                @Override
                public Double parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getDouble(1);
                    }
                    return 0.00;
                }
            }, chargeSql.toString(), parameters);
            result.put("chargeTotalAmount", chargeTotalAmount);
            
            Double withdrawTotalAmount = select(connection, new ItemParser<Double>()
            {
                
                @Override
                public Double parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getDouble(1);
                    }
                    return 0.00;
                }
            }, withdrawSql.toString(), parameters);
            result.put("withdrawTotalAmount", withdrawTotalAmount);
        }
        return result;
    }
    
    /**
     * 查询业务员名下的所有客户信息
     *
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<CustomerEntity> selectCustomers(CustomerQuery query, Paging paging)
        throws Throwable
    {
        final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isTg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        StringBuilder sql =
            new StringBuilder(
                "SELECT * FROM ( SELECT T6110.F02 AS F01, T6141.F02 AS F02, T6110.F04 AS F03,T6101.F06 AS F04,(SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F03 = T6110.F01 AND T6252.F09 IN ('WH','HKZ','DF')) AS F05, T6110.F09 AS F06,T6119.F01 AS F07,T6119.F03 AS F08 FROM S61.T6110 ");
        sql.append(" LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 ");
        //if(isTg){
        sql.append(" LEFT JOIN S61.T6119 ON T6119.F01 = T6110.F01 ");
        // }
        sql.append(" LEFT JOIN S61.T6101 ON T6101.F02 = T6110.F01 AND T6101.F03 = 'WLZH' WHERE 1=1 AND T6110.F14 = ? ");
        sql.append(" UNION ALL ");
        sql.append(" SELECT T6110_1.F02 AS F01, T6141.F02 AS F02, T6110_1.F03 AS F03,T6101.F06 AS F04,(SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F03 = T6110_1.F01 AND T6252.F09 IN ('WH','HKZ','DF')) AS F05, T6110_1.F04 AS F06,T6119.F01 AS F07,T6119.F03 AS F08 FROM ");
        sql.append(" (SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F04 AS F03,T6110.F09 AS F04,T6110.F14 AS F05 FROM S61.T6110 WHERE T6110.F01 IN (SELECT T1.F01 FROM S61.T6111 T1 WHERE T1.F03 IN (SELECT T2.F02 FROM S61.T6111 T2 LEFT JOIN S61.T6110 T3 ON T3.F01 = T2.F01 WHERE T3.F14 = ? )) AND T6110.F14 IS NULL) T6110_1");
        sql.append(" LEFT JOIN S61.T6141 ON T6141.F01 = T6110_1.F01 ");
        //if(isTg){
        sql.append(" LEFT JOIN S61.T6119 ON T6119.F01 = T6110_1.F01 ");
        //}
        sql.append(" LEFT JOIN S61.T6101 ON T6101.F02 = T6110_1.F01 AND T6101.F03 = 'WLZH' WHERE 1=1 ) TEMP WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        params.add(query.getEmployNum());
        params.add(query.getEmployNum());
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.getUserName()))
            {
                sql.append(" AND TEMP.F01 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getUserName()));
            }
            
            if (!StringHelper.isEmpty(query.getRealName()))
            {
                sql.append(" AND TEMP.F02 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getRealName()));
            }
            
            if (!StringHelper.isEmpty(query.getMobile()))
            {
                sql.append(" AND TEMP.F03 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getMobile()));
            }
            
            if (!StringHelper.isEmpty(query.getIsThird()) && isTg)
            {
                if (YesOrNo.yes.name().equals(query.getIsThird()))
                {
                    sql.append(" AND TEMP.F08 IS NOT NULL ");
                }
                else
                {
                    sql.append(" AND (TEMP.F07 IS NULL OR TEMP.F08 IS NULL)");
                }
            }
            
            if (query.getCreateTimeStart() != null)
            {
                sql.append(" AND DATE(TEMP.F06) >= ? ");
                params.add(query.getCreateTimeStart());
            }
            if (query.getCreateTimeEnd() != null)
            {
                sql.append(" AND DATE(TEMP.F06) <= ? ");
                params.add(query.getCreateTimeEnd());
            }
        }
        sql.append(" ORDER BY TEMP.F06 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CustomerEntity>()
            {
                
                @Override
                public CustomerEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<CustomerEntity> resultList = null;
                    while (resultSet.next())
                    {
                        CustomerEntity entity = new CustomerEntity();
                        entity.userName = resultSet.getString(1);
                        entity.realName = resultSet.getString(2);
                        entity.mobile = resultSet.getString(3);
                        entity.usableAmount = resultSet.getBigDecimal(4);
                        entity.loanAmount = resultSet.getBigDecimal(5);
                        entity.registeTime = resultSet.getTimestamp(6);
                        if (resultList == null)
                        {
                            resultList = new ArrayList<CustomerEntity>();
                        }
                        resultList.add(entity);
                    }
                    return ((resultList == null || resultList.size() == 0) ? null
                        : resultList.toArray(new CustomerEntity[resultList.size()]));
                }
                
            },
                paging,
                sql.toString(),
                params);
            
        }
    }
    
    private int getCustomNum(Connection conn, String employNum)
        throws SQLException
    {
        int num = 0;
        try (PreparedStatement ps =
            conn.prepareStatement("SELECT ((SELECT COUNT(F01) FROM S61.T6110 WHERE T6110.F14=?) + (SELECT COUNT(F01) FROM S61.T6111 WHERE T6111.F03 IN (SELECT T6111.F02 FROM S61.T6111 WHERE T6111.F01 IN (SELECT T6110.F01 FROM S61.T6110 WHERE T6110.F14=?))))"))
        {
            ps.setString(1, employNum);
            ps.setString(2, employNum);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    num = rs.getInt(1);
                }
            }
        }
        return num;
    }
}
