package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

public abstract class AbstractBidService extends AbstractP2PService
{
    
    public AbstractBidService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    protected SQLConnectionProvider getSQLConnectionProvider()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }
    
    @Override
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(db);
    }
    
    protected BigDecimal selectBigDecimal(String db, String sql, Object... paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
        try (Connection connection = getConnection(db))
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                    return decimal;
                }
            }, sql, paramters);
        }
    }
    
    protected BigDecimal selectBigDecimal(Connection connection, String sql, Object... paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
        return select(connection, new ItemParser<BigDecimal>()
        {
            @Override
            public BigDecimal parse(ResultSet resultSet)
                throws SQLException
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
                return decimal;
            }
        }, sql, paramters);
    }
    
    @Override
    protected BigDecimal selectBigDecimal(Connection connection, String sql, ArrayList<Object> paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
        return select(connection, new ItemParser<BigDecimal>()
        {
            @Override
            public BigDecimal parse(ResultSet resultSet)
                throws SQLException
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
                return decimal;
            }
        }, sql, paramters);
    }
    
    @Override
    protected void writeLog(Connection connection, String type, String log)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setString(3, type);
            pstmt.setString(4, log);
            pstmt.setString(5, serviceResource.getSession().getRemoteIP());
            pstmt.execute();
        }
    }
    
    protected static final DecimalFormat AMOUNT_SPLIT_FORMAT = new DecimalFormat("0.00");
    
    protected String format(BigDecimal amount)
    {
        if (amount == null)
        {
            return "0";
        }
        return AMOUNT_SPLIT_FORMAT.format(amount);
    }
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public String getRegion(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sb.append(rs.getString(1));
                        sb.append(",");
                        sb.append(rs.getString(2));
                        sb.append(",");
                        sb.append(rs.getString(3));
                    }
                }
            }
        }
        return sb.toString();
    }
    
    /*protected int insertT6501(Connection connection, T6501 entity)
    		throws SQLException {
    	try (PreparedStatement pstmt = connection
    			.prepareStatement(
    					"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
    					PreparedStatement.RETURN_GENERATED_KEYS)) {
    		pstmt.setInt(1, entity.F02);
    		pstmt.setString(2, entity.F03.name());
    		pstmt.setTimestamp(3, entity.F04);
    		pstmt.setTimestamp(4, entity.F05);
    		pstmt.setTimestamp(5, entity.F06);
    		pstmt.setString(6, entity.F07.name());
    		pstmt.setInt(7, entity.F08);
    		pstmt.setInt(8, entity.F09);
    		pstmt.execute();
    		try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
    			if (resultSet.next()) {
    				return resultSet.getInt(1);
    			}
    			return 0;
    		}
    	}
    }*/
    
    protected void sendLetter(Connection connection, int userId, String title, String content)
        throws Throwable
    {
        int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
        insertT6124(connection, letterId, content);
    }
    
    private int insertT6123(Connection connection, int F02, String F03, T6123_F05 F05)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setString(4, F05.name());
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    protected void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F02);
            pstmt.execute();
        }
    }
    
    /*@Override
    protected void sendMsg(Connection connection, String mobile, String content)
    		throws Throwable {
    	if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) {
    		long msgId = 0;
    		try (PreparedStatement ps = connection.prepareStatement(
    				"INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
    				Statement.RETURN_GENERATED_KEYS)) {
    			ps.setInt(1, 0);
    			ps.setString(2, content);
    			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
    			ps.setString(4, "W");
    			ps.execute();
    			try (ResultSet resultSet = ps.getGeneratedKeys()) {
    				if (resultSet.next()) {
    					msgId = resultSet.getLong(1);
    				}
    			}
    		}
    		if (msgId > 0) {
    			try (PreparedStatement ps = connection
    					.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
    				ps.setLong(1, msgId);
    				ps.setString(2, mobile);
    				ps.execute();
    			}
    		}
    		return;
    	}
    }*/
    
    /**
     * 发送站内信
     * @throws Throwable 
     */
    protected void sendLetter(ServiceResource serviceResource, T6230 t6230, Connection connection, String des)
        throws Throwable
    {
        String reason = StringHelper.isEmpty(des) ? "没有人投资" : des;
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("datetime", DateTimeParser.format(t6230.F24));
        envionment.set("title", t6230.F03);
        envionment.set("reason", reason);
        String content = configureProvider.format(LetterVariable.LOAN_FAILED, envionment);
        sendLetter(connection, t6230.F02, "标的流标", content);
        writeLog(connection, "操作日志", "“" + t6230.F03 + "”放款审核不通过,原因:" + reason);
    }
}
