package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.modules.bid.user.service.BuyLoanManage;
import com.dimeng.p2p.modules.bid.user.service.entity.LoanInfo;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class BuyLoanManageImpl extends AbstractBidManage implements BuyLoanManage {

	public static class LoanInfoManageFactory implements ServiceFactory<BuyLoanManage> {

		@Override
		public BuyLoanManage newInstance(ServiceResource serviceResource) {
			return new BuyLoanManageImpl(serviceResource);
		}
	}

	public BuyLoanManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public PagingResult<LoanInfo> search(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT F01,F03 FROM S62.T6230 WHERE F20 = ? AND F01 NOT IN (SELECT T6332.F03 FROM S63.T6332) ORDER BY F24 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<LoanInfo>()
            {
                @Override
                public LoanInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<LoanInfo> list = null;
                    while (resultSet.next())
                    {
                        LoanInfo info = new LoanInfo();
                        info.loanId = resultSet.getInt(1);
                        info.loanTitle = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new LoanInfo[list.size()]);
                }
            }, paging, sql, T6230_F20.DSH);
        }
    }

	@Override
    public PagingResult<LoanInfo> searchLs(String tel, Date startTime, Date endTime, Paging paging)
        throws Throwable
    {
        int loginId = serviceResource.getSession().getAccountId();
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F02 AS F01, T6230.F03 AS F02, T6230.F05 AS F03, T6230.F06 AS F04, T6230.F09 AS F05, T6230.F24 AS F06, T6110.F04 AS F07 FROM S62.T6230 INNER JOIN S63.T6332 ON T6230.F01 = T6332.F03 INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 WHERE T6332.F02 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(loginId);
        if (tel != null && !tel.isEmpty())
        {
            sql.append("  AND T6110.F04 = ?");
            parameters.add(tel);
        }
        if (startTime != null)
        {
            sql.append("  AND DATE(T6230.F24) >= ?");
            parameters.add(DateParser.format(startTime));
        }
        if (endTime != null)
        {
            sql.append("  AND DATE(T6230.F24) <= ?");
            parameters.add(DateParser.format(endTime));
        }
        sql.append("  ORDER BY T6332.F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<LoanInfo>()
            {
                @Override
                public LoanInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<LoanInfo> list = null;
                    while (resultSet.next())
                    {
                        LoanInfo info = new LoanInfo();
                        info.jkUserId = resultSet.getInt(1);
                        info.loanTitle = resultSet.getString(2);
                        info.jkje = resultSet.getBigDecimal(3);
                        info.rate = resultSet.getDouble(4);
                        info.jkqx = resultSet.getInt(5);
                        info.tjsj = resultSet.getTimestamp(6);
                        info.tel = resultSet.getString(7);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new LoanInfo[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public LoanInfo search(int gmId) throws Throwable {
		try(Connection connection = getConnection()){
			LoanInfo record = null;
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6230.F02 AS F01, T6230.F03 AS F02, T6230.F05 AS F03, T6230.F06 AS F04, T6230.F09 AS F05, T6230.F24 AS F06 FROM S62.T6230 INNER JOIN S63.T6332 ON T6230.F01 = T6332.F03 WHERE T6332.F01 = ? AND T6332.F02 = ? LIMIT 1")) {
		        pstmt.setInt(1, gmId);
		        pstmt.setInt(2, serviceResource.getSession().getAccountId());
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new LoanInfo();
		                record.jkUserId = resultSet.getInt(1);
		                record.loanTitle = resultSet.getString(2);
		                record.jkje = resultSet.getBigDecimal(3);
		                record.rate = resultSet.getDouble(4);
		                record.jkqx = resultSet.getInt(5);
		                record.tjsj = resultSet.getTimestamp(6);
		            }
		        }
		    }
		    return record;
		}
		
	}

	@Override
    public int buyLoan(int loanId, BigDecimal gmfwf)
        throws Throwable
    {
        if (loanId < 0)
        {
            throw new ParameterException("指定的记录不存在");
        }
        int loginId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                /**
                 * 机构资金账户
                 */
                T6101 t6101JG = selectT6101(connection, loginId, T6101_F03.WLZH, true);
                /**
                 * 平台资金账户
                 */
                T6101 t6101PT = selectT6101(connection, getPTID(connection), T6101_F03.WLZH, true);
                
                if (t6101JG == null)
                {
                    throw new ParameterException("该用户不存在");
                }
                if (t6101PT == null)
                {
                    throw new ParameterException("平台账户不存在");
                }
                
                /**
                 * 判断账户余额是否大于购买服务费
                 */
                if (t6101JG.F06.compareTo(gmfwf) < 0)
                {
                    throw new LogicalException("你的账户余额不足，请充值");
                }
                
                /**
                 * 查询状态
                 */
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE F01 = ? FOR UPDATE");)
                {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery();)
                    {
                        if (rs.next())
                        {
                            if (EnumParser.parse(T6230_F20.class, rs.getString(1)) != T6230_F20.DSH)
                                throw new LogicalException("该标的状态不能进行购买");
                        }
                    }
                }
                
                /**
                 * 操作用户资金表
                 */
                t6101JG.F06 = t6101JG.F06.subtract(gmfwf);
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ?");)
                {
                    ps.setBigDecimal(1, t6101JG.F06);
                    ps.setTimestamp(2, timestamp);
                    ps.setInt(3, t6101JG.F01);
                    ps.executeUpdate();
                }
                
                T6102 t6102 = new T6102();
                t6102.F02 = t6101JG.F01;
                t6102.F03 = FeeCode.CBFWF;
                t6102.F04 = t6101PT.F01;
                t6102.F07 = gmfwf;
                t6102.F08 = t6101JG.F06;
                t6102.F09 = "查标服务费";
                insertT6102(connection, t6102);
                
                /**
                 * 操作平台资金表
                 */
                t6101PT.F06 = t6101PT.F06.add(gmfwf);
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ?");)
                {
                    ps.setBigDecimal(1, t6101PT.F06);
                    ps.setTimestamp(2, timestamp);
                    ps.setInt(3, t6101PT.F01);
                    ps.executeUpdate();
                }
                
                /**
                 * 资金交易记录新增一条记录
                 */
                T6102 t6102pt = new T6102();
                t6102pt.F02 = t6101PT.F01;
                t6102pt.F03 = FeeCode.CBFWF;
                t6102pt.F04 = t6101JG.F01;
                t6102pt.F07 = gmfwf;
                t6102pt.F08 = t6101PT.F06;
                t6102pt.F09 = "查标服务费";
                insertT6102(connection, t6102pt);
                
                /**
                 * 更新标的状态
                 */
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?");)
                {
                    ps.setString(1, T6230_F20.YZF.name());
                    ps.setInt(2, loanId);
                    ps.executeUpdate();
                }
                
                int gmId =
                    insert(connection,
                        "INSERT INTO S63.T6332 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                        serviceResource.getSession().getAccountId(),
                        loanId,
                        timestamp,
                        gmfwf);
                
                serviceResource.commit(connection);
                return gmId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
	
}
