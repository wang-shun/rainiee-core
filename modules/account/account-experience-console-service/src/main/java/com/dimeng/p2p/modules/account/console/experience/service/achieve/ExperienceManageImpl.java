package com.dimeng.p2p.modules.account.console.experience.service.achieve;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.Experience;
import com.dimeng.p2p.modules.account.console.experience.service.entity.User;
import com.dimeng.p2p.modules.account.console.experience.service.query.AddExperienceUserQuery;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceQuery;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class ExperienceManageImpl extends AbstractExperienceService implements
		ExperienceManage {

	public ExperienceManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 查询用户名
	 * 
	 * @param userId
	 * @return
	 * @throws java.sql.SQLException
	 * @throws com.dimeng.framework.resource.ResourceNotFoundException
	 * @throws Throwable
	 */
	private String getUserName(int userId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6110.F02 FROM S61.T6110 T6110 WHERE T6110.F01=?")) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}


	/**
	 * 查询用户名ID
	 * @throws java.sql.SQLException
	 * @throws ResourceNotFoundException
	 * @throws Throwable
	 */
	private int getUserId(String userName) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6110.F01 FROM S61.T6110 T6110 WHERE T6110.F02 like ?")) {
				ps.setString(1, "%" + userName + "%");
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 查询用户真实姓名
	 * 
	 * @param userId
	 * @return
	 * @throws java.sql.SQLException
	 * @throws Throwable
	 */
	private String getRealName(int userId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}

    /**
     * 是否有未使用的体验金
     * @param userId 用户ID
     * @return
     * @throws SQLException
     */
    private boolean isHasWsy(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S61.T6103 WHERE F02=? AND F06 = ?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6103_F06.WSY.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                    return false;
                }
            }
        }
    }

	/**
	 * 更新用户其他体验金的失效时间
	 * 
	 * @throws Throwable
	 */
    private void updateUserExperience(int userId, int validDay, Connection connection)
			throws Throwable {
		String sql = "SELECT F01 FROM S61.T6103 WHERE F06=? AND F05 >= ? AND F02=?";
		List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, T6103_F06.WSY.name());
            ps.setDate(2, getCurrentDate(connection));
            ps.setInt(3, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    ids.add(rs.getInt(1));
				}
			}
		}
		if (ids != null && ids.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE S61.T6103 SET F05=DATE_ADD(?, INTERVAL ? DAY) WHERE F01 IN(");
			int i = 1;
			for (Integer id : ids) {
				sb.append(id);
				if (i < ids.size()) {
					sb.append(",");
				}
				i++;
			}
			sb.append(")");
            try (PreparedStatement ps = connection.prepareStatement(sb.toString()))
            {
                ps.setDate(1, getCurrentDate(connection));
				ps.setInt(2, validDay);
                ps.executeUpdate();
			}
		}
	}

	@Override
    public Experience get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Experience record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12 FROM S61.T6103 WHERE T6103.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Experience();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6103_F06.parse(resultSet.getString(6));
                        record.F07 = resultSet.getInt(7);
                        record.F08 = T6103_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getTimestamp(11);
                        record.accountName = getUserName(record.F02);
                        record.operName = getName(resultSet.getInt(12));
                    }
                }
            }
            return record;
        }
    }

	@Override
	public void export(Experience[] experiences, OutputStream outputStream)
			throws Throwable {
		if (experiences == null) {
			return;
		}
		if (outputStream == null) {
			return;
		}
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, "GBK"))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("序号");
			writer.write("用户名");
			writer.write("姓名");
			writer.write("金额(元)");
			writer.write("收益期");
			writer.write("来源");
			writer.write("开始时间");
			writer.write("失效时间");
			writer.write("操作人");
			writer.write("最后操作时间");
			writer.write("备注");
			writer.write("状态");
			writer.newLine();
			int i = 1;
			for (Experience experience : experiences) {
				writer.write(i++);
				writer.write(experience.accountName);
				writer.write(experience.name);
				writer.write(experience.F03);
				writer.write(experience.F07 + "个月");
				writer.write(experience.F08);
				writer.write(DateParser.format(experience.F04));
				writer.write(DateParser.format(experience.F05));
				if("".equals(experience.operName))
				{

					writer.write("平台账号");
				}else {
					writer.write(experience.operName);
				}
				writer.write(experience.F10);
				writer.write(experience.F09);
				writer.write(experience.F06.getChineseName());
				writer.newLine();
			}
		}
	}

	/**
	 * 查询后台账号
	 * 
	 * @param accountId
	 * @return
	 * @throws Throwable
	 */
	private String getName(int accountId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S71.T7110 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}

	@Override
    public T6110 getIdByName(String userName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F07 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, userName);
                //pstmt.setString(2, T6110_F07.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    T6110 t6110 = null;
                    if (resultSet.next())
                    {
                        //return resultSet.getInt(1);
                        t6110 = new T6110();
                        t6110.F01 = resultSet.getInt(1);
                        t6110.F07 = EnumParser.parse(T6110_F07.class, resultSet.getString(2));
                        
                    }
                    return t6110;
                }
            }
        }
    }

	@Override
	public BigDecimal getTotalExperience() throws Throwable {
		BigDecimal totalAmount = new BigDecimal(0);
		try (Connection connection = getConnection()) {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(IFNULL(T6103.F03,0)) FROM S61.T6103 WHERE T6103.F06 = 'WSY'"))
            {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						totalAmount = rs.getBigDecimal(1);
					}
				}
			}
		}
		return totalAmount;
	}


    /**
     * 体验金充值-新增选择充值人员
     * @param addExperienceUserQuery 查询条件
     * @param paging 分页
     * @return PagingResult
     * @throws Throwable
     */
    @Override
    public PagingResult<User> search(AddExperienceUserQuery addExperienceUserQuery, Paging paging)
            throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6141.F02 AS F03, T6110.F07 AS F04 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" AND T6110.F13 = ?");
        parameters.add(T6110_F13.F);
        sql.append(" AND T6110.F06 = ?");
        parameters.add(T6110_F06.ZRR);
        if (addExperienceUserQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String userName = addExperienceUserQuery.userName;
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(userName));
            }
            String realName = addExperienceUserQuery.realName;
            if (!StringHelper.isEmpty(realName))
            {
                sql.append(" AND T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(realName));
            }
        }
        sql.append(" ORDER BY T6110.F09 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<User>()
            {
                
                @Override
                public User[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<User> list = null;
                    while (resultSet.next())
                    {
                        User record = new User();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = T6110_F07.parse(resultSet.getString(4));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new User[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
}
