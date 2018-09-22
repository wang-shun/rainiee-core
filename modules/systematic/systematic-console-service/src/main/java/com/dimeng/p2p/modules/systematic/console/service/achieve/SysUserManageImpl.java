package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7110_F05;
import com.dimeng.p2p.S71.enums.T7110_F09;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.common.enums.IsPass;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.IndexCount;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysUser;
import com.dimeng.p2p.modules.systematic.console.service.query.SysUserQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
public class SysUserManageImpl extends AbstractSystemService implements SysUserManage
{
    
    public static class SysUserManageFactory implements ServiceFactory<SysUserManage>
    {
        
        @Override
        public SysUserManage newInstance(ServiceResource serviceResource)
        {
            return new SysUserManageImpl(serviceResource);
        }
    }
    
    public SysUserManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int add(String accountName, String password, String name, String status, String phone, String pos,
        String employeeNum, int roleId, String dept)
        throws Throwable
    {
    	if(!StringHelper.isEmpty(phone) && isExistPhone(phone)){
			throw new ParameterException("联系手机号码已经存在！");
		}
        int sysUserId = 0;
        String select = "SELECT F01 FROM S71.T7110 WHERE F02=?";
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement(select))
                {
                    ps.setString(1, accountName);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            throw new LogicalException("登录账户已经存在.");
                        }
                    }
                }
                
                String sql = "INSERT INTO S71.T7110 SET F02=?,F03=?,F04=?,F05=?,F06=?,F09=?,F10=?,F11=?,F12=?,F13=?";
                password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
                sysUserId =
                    insert(connection,
                        sql,
                        accountName,
                        password,
                        name,
                        status,
                        new Timestamp(System.currentTimeMillis()),
                        T7110_F09.S,
                        phone,
                        pos,
                        employeeNum,dept);
                if (T7110_F05.TY.name().equals(status) && Constant.BUSINESS_ROLE_ID == roleId)
                {
                    sql = "INSERT INTO S71.T7111 SET F02 = ?, F03 = now(), F05 = ? ";
                    insert(connection, sql, employeeNum, status);
                }
                writeLog(connection, "操作日志", "后台管理员账号新增");
                serviceResource.commit(connection);
                return sysUserId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public SysUser get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的系统账号ID不存在.");
        }
        String schema = serviceResource.getSystemDefine().getSchemaName(SessionManager.class);
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT T7110.F01,T7110.F02,T7110.F03,T7110.F04,T7110.F05,T7110.F06,T7110.F07,T7110.F08,_1020.F01 AS ROLEID ,T7110.F10,T7110.F11,T7110.F12,T7110.F13 FROM S71.T7110 ");
        sb.append(" INNER JOIN " + schema + "._1022 ON T7110.F01=_1022.F01 INNER JOIN " + schema
            + "._1020 ON _1022.F02=_1020.F01 WHERE T7110.F01=?");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<SysUser>()
            {
                
                @Override
                public SysUser parse(ResultSet resultSet)
                    throws SQLException
                {
                    SysUser sysUser = new SysUser();
                    if (resultSet.next())
                    {
                        sysUser.id = resultSet.getInt(1);
                        sysUser.accountName = resultSet.getString(2);
                        sysUser.password = resultSet.getString(3);
                        sysUser.name = resultSet.getString(4);
                        sysUser.status = EnumParser.parse(SysAccountStatus.class, resultSet.getString(5));
                        sysUser.createTime = resultSet.getTimestamp(6);
                        sysUser.lastTime = resultSet.getTimestamp(7);
                        sysUser.lastIp = resultSet.getString(8);
                        sysUser.roleId = resultSet.getInt(9);
                        sysUser.phone = resultSet.getString(10);
                        sysUser.pos = resultSet.getString(11);
                        sysUser.employNum = resultSet.getString(12);
                        sysUser.dept = resultSet.getString(13);
                    }
                    return sysUser;
                }
            }, sb.toString(), id);
        }
    }
    
    @Override
    public void update(int id, String name, String status, String phone, String pos, int roleId,String employNum, String dept) throws Throwable {
    	if(!StringHelper.isEmpty(phone) && isExistPhone(id,phone)){
			throw new ParameterException("联系手机号码已经存在！");
		}
        String sql = "UPDATE S71.T7110 SET F04=?,F05=?,F10=?,F11=?,F13=? WHERE F01=?";
        try (Connection connection = getConnection()){
            try{
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S71.T7110 WHERE F01=?")){
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()){
                        if (!rs.next()){
                            throw new LogicalException("指定的用户ID不存在.");
                        }
                    }
                }
                //修改管理员信息
                execute(connection, sql, name, status, phone, pos, dept, id);
                if (Constant.BUSINESS_ROLE_ID == roleId && !StringHelper.isEmpty(employNum)){
                    if (T7110_F05.QY.name().equals(status)){
                        if (getLogCount(connection, employNum) > 0){
                            sql = "UPDATE S71.T7111 SET F04 = now(), F05 = ?  WHERE F02 = ? AND F04 IS NULL";
                            execute(connection, sql, status, employNum);
                        }
                    }else{
                        if (getLogCount(connection, employNum) == 0){
                            sql = "INSERT INTO S71.T7111 SET F02 = ?, F03 = now(), F05 = ? ";
                            insert(connection, sql, employNum, status);
                        }
                    }
                }
                writeLog(connection, "操作日志", "后台管理员账号修改");
                serviceResource.commit(connection);
            }catch (Exception e){
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private int getLogCount(Connection connection, String employNum)
        throws Throwable
    {
        String sql = "SELECT COUNT(1) FROM S71.T7111 WHERE F02 = ? AND F05 = 'TY' AND F04 IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, employNum);
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
    
    @Override
    public PagingResult<SysUser> serarch(SysUserQuery query, Paging paging)
        throws Throwable
    {
        String schema = serviceResource.getSystemDefine().getSchemaName(SessionManager.class);
        String sql =
            "SELECT T7110.F01,T7110.F02,T7110.F04,T7110.F05,T7110.F06,T7110.F07,T7110.F08,_1020.F02 AS ROLENAME,T7110.F10,T7110.F11,T7110.F12,T7110.F13 FROM S71.T7110";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(" INNER JOIN " + schema + "._1022 ON T7110.F01=_1022.F01 INNER JOIN " + schema
            + "._1020 ON _1022.F02=_1020.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String accountName = query.getAccountName();
            String name = query.getName();
            SysAccountStatus status = query.getStatus();
            Timestamp startTime = query.getCreateTimeStart();
            Timestamp endTime = query.getCreateTimeEnd();
            if (!StringHelper.isEmpty(accountName))
            {
                sb.append(" AND T7110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(accountName));
            }
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T7110.F04 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            if (status != null)
            {
                sb.append(" AND T7110.F05=?");
                parameters.add(status);
            }
            if (startTime != null)
            {
                sb.append(" AND DATE(T7110.F07)>=?");
                parameters.add(startTime);
            }
            if (endTime != null)
            {
                sb.append(" AND DATE(T7110.F07)<=?");
                parameters.add(endTime);
            }
            if (query.getRoleId() > 0)
            {
                sb.append(" AND _1020.F01=?");
                parameters.add(query.getRoleId());
            }
            
            if(!StringHelper.isEmpty(query.getEmployNum())){
            	sb.append(" AND T7110.F12 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(query.getEmployNum()));
            }

            if(!StringHelper.isEmpty(query.getDept())){
                sb.append(" AND T7110.F13 LIKE ?");
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
                    List<SysUser> sysUsers = new ArrayList<>();
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
                        sysUser.roleName = rs.getString(8);
                        sysUser.phone = rs.getString(9);
                        sysUser.pos = rs.getString(10);
                        sysUser.employNum = rs.getString(11);
                        sysUser.dept = rs.getString(12);
                        sysUsers.add(sysUser);
                    }
                    return sysUsers.toArray(new SysUser[sysUsers.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public IndexCount getIndexCount()
        throws Throwable
    {
        IndexCount index = new IndexCount();
        try (Connection conn = getConnection("S70"))
        {
            try (PreparedStatement pst =
                conn.prepareStatement("SELECT F01, F02, F03, F04, F05, F06,F07, F08, F09 FROM S70.T7010"))
            {
                try (ResultSet rst = pst.executeQuery())
                {
                    if (rst.next())
                    {
                        index.todayRegisterUser = rst.getInt(1);
                        index.historyRegisterUser = rst.getInt(2);
                        index.todayLoginUser = rst.getInt(3);
                        index.todayTotalUserRecharge = rst.getBigDecimal(4);
                        index.historyTotalUserRecharge = rst.getBigDecimal(5);
                        index.todayTotalUserExtract = rst.getBigDecimal(6);
                        index.historyTotalUserExtract = rst.getBigDecimal(7);
                        index.platformTotalIncome = rst.getBigDecimal(8);
                        index.userInvestTotalIncome = rst.getBigDecimal(9);
                    }
                }
            }
        }
        return index;
    }
    
    @Override
    public boolean isOneLogin()
        throws Throwable
    {
        boolean isOneLogin = false;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F09 FROM S71.T7110 WHERE F01=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        IsPass isPass = EnumParser.parse(IsPass.class, rs.getString(1));
                        if (isPass == IsPass.S)
                        {
                            isOneLogin = true;
                        }
                    }
                }
            }
        }
        return isOneLogin;
    }
    
    @Override
    public void log(int accountId, String ip)
        throws Throwable
    {
        String updateT7110 = "UPDATE S71.T7110 SET F07=?,F08=? WHERE F01=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?,F06=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setString(3, "登录日志");
                ps.setString(4, "登录后台系统 ");
                ps.setString(5, ip);
                ps.executeUpdate();
            }
            execute(connection, updateT7110, getCurrentTimestamp(connection), ip, accountId);
        }
    }
    
    @Override
    public void updatePassWord(String oldPassWord, String newPassWord)
        throws Throwable
    {
        if (StringHelper.isEmpty(oldPassWord))
        {
            throw new ParameterException("原密码不能为空！");
        }
        if (StringHelper.isEmpty(newPassWord))
        {
            throw new ParameterException("新密码不能为空！");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection conn = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(conn);
                try (PreparedStatement pst = conn.prepareStatement("SELECT F03,F02 FROM S71.T7110 WHERE F01 = ?"))
                {
                    pst.setInt(1, accountId);
                    try (ResultSet rst = pst.executeQuery())
                    {
                        if (rst.next())
                        {
                            String pwd = rst.getString(1);
                            if (!UnixCrypt.crypt(oldPassWord, DigestUtils.sha256Hex(oldPassWord)).equals(pwd))
                            {
                                throw new LogicalException("原密码错误");
                            }
                            if (pwd.equals(UnixCrypt.crypt(oldPassWord, DigestUtils.sha256Hex(newPassWord))))
                            {
                                throw new LogicalException("修改的密码不能和原密码相同");
                            }
                            if (newPassWord.equals(rst.getString(2)))
                            {
                                throw new LogicalException("密码不能与用户名一致");
                            }
                        }
                        else
                        {
                            throw new LogicalException("用户信息不存在");
                        }
                    }
                }
                try (PreparedStatement pst = conn.prepareStatement("UPDATE S71.T7110 SET F03 = ? WHERE F01 = ?"))
                {
                    pst.setString(1, UnixCrypt.crypt(newPassWord, DigestUtils.sha256Hex(newPassWord)));
                    pst.setInt(2, accountId);
                    pst.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE S71.T7110 SET F09=? WHERE F01=?"))
                {
                    ps.setString(1, IsPass.F.name());
                    ps.setInt(2, accountId);
                    ps.executeUpdate();
                }
                writeLog(conn, "操作日志", "管理员密码修改");
                serviceResource.commit(conn);
            }
            catch (Exception e)
            {
                serviceResource.rollback(conn);
                throw e;
            }
        }
    }
    
    @Override
    public int readAccountId(String accountName, String password)
        throws AuthenticationException, SQLException
    {
        int accountId = 0;
        String sql = "SELECT F01,F03,F05 FROM S71.T7110 WHERE F02=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, accountName);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        accountId = rs.getInt(1);
                        String pwd = rs.getString(2);
                        String status = rs.getString(3);
                        if ("TY".equals(status))
                        {
                            throw new AuthenticationException("该账号被停用，禁止登录。", new LogicalException());
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
    public IndexCount getIndexCountExt()
        throws Throwable
    {
        IndexCount index = new IndexCount();
        try (Connection conn = getConnection("S70"))
        {
            try (PreparedStatement pst =
                conn.prepareStatement("SELECT F01, F02, F03, F04, F05, F06,F07, F08, F09, F10, F11 FROM S70.T7010"))
            {
                try (ResultSet rst = pst.executeQuery())
                {
                    if (rst.next())
                    {
                        index.todayRegisterUser = rst.getInt(1);
                        index.historyRegisterUser = rst.getInt(2);
                        index.todayLoginUser = rst.getInt(3);
                        index.todayTotalUserRecharge = rst.getBigDecimal(4);
                        index.historyTotalUserRecharge = rst.getBigDecimal(5);
                        index.todayTotalUserExtract = rst.getBigDecimal(6);
                        index.historyTotalUserExtract = rst.getBigDecimal(7);
                        index.platformTotalIncome = rst.getBigDecimal(8);
                        index.userInvestTotalIncome = rst.getBigDecimal(9);
                        index.todayTotalUserToRepay = rst.getBigDecimal(10);
                        index.totalUserToRepay = rst.getBigDecimal(11);
                    }
                }
            }
        }
        return index;
    }
    
    @Override
    public void resetConsoleLoginErrorNum(String userName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S10._1037 SET F03 = 0 WHERE _1037.F01 = ? "))
            {
                pstmt.setString(1, userName);
                pstmt.execute();
            }
        }
    }

	/** {@inheritDoc} */
	 
	@Override
	public void updateUserPwd(int id, String password) throws Throwable {
		if (StringHelper.isEmpty(password)){
            throw new ParameterException("密码不能为空！");
        }
        String cryptPW = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
		try(Connection connection=getConnection()){
            try (PreparedStatement pst = connection.prepareStatement("SELECT F03,F02 FROM S71.T7110 WHERE F01 = ?"))
            {
                pst.setInt(1, id);
                try (ResultSet rst = pst.executeQuery())
                {
                    if (rst.next())
                    {
                        String pwd = rst.getString(1);
                        if (pwd.equals(cryptPW))
                        {
                            throw new LogicalException("修改的密码不能和原密码相同");
                        }
                        if (password.equals(rst.getString(2)))
                        {
                            throw new LogicalException("密码不能与用户名一致");
                        }
                    }
                    else
                    {
                        throw new LogicalException("用户信息不存在");
                    }
                }
            }
			try (PreparedStatement pst = connection.prepareStatement("UPDATE S71.T7110 SET F03 = ? WHERE F01 = ?")){
                pst.setString(1, cryptPW);
                pst.setInt(2, id);
                pst.executeUpdate();
            }
		}
		
	}
	
	/**
	 * 检查手机号码是否存在
	 * @param phone
	 * @return
	 * @throws Throwable
	 */
	private boolean isExistPhone(String phone) throws Throwable{
		try(Connection connection=getConnection()){
			try (PreparedStatement pst = connection.prepareStatement("SELECT F01 FROM S71.T7110 WHERE F10 = ?")){
                pst.setString(1, phone);
                try(ResultSet rs=pst.executeQuery()){
                	if(rs.next()){
                		return true;
                	}
                }
            }
		}
		return false;
	}
	
	/**
	 * 修改时，检查手机号码是否被占用
	 * @param id
	 * @param phone
	 * @return
	 * @throws Throwable
	 */
	private boolean isExistPhone(int id,String phone) throws Throwable{
		try(Connection connection=getConnection()){
			try (PreparedStatement pst = connection.prepareStatement("SELECT F01 FROM S71.T7110 WHERE F01 <> ? AND F10 = ?")){
                pst.setInt(1, id);
                pst.setString(2, phone);
                try(ResultSet rs=pst.executeQuery()){
                	if(rs.next()){
                		return true;
                	}
                }
            }
		}
		return false;
	}
  
}
