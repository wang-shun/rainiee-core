package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.modules.systematic.console.service.ToDoThings;
/**
 * 待变事项实现类
 * @author tangxianjie
 *
 */
public class ToDoThingsImpl extends AbstractSystemService implements ToDoThings {

	public static class SysUserManageFactory implements
			ServiceFactory<ToDoThings> {

		@Override
		public ToDoThings newInstance(ServiceResource serviceResource) {
			return new ToDoThingsImpl(serviceResource);
		}
	}

	public ToDoThingsImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	/**
	 * 根据类型查询借款项目总数
	 */
	public int queryDshProCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append(" SELECT COUNT(1) \n");  
		sbSQL.append(" FROM       \n"); 
		sbSQL.append(" S62.T6230  \n"); 	
		sbSQL.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01   \n"); 
		sbSQL.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01   \n"); 
		sbSQL.append(" INNER JOIN S62.T6211 ON T6230.F04 = T6211.F01   \n"); 	
		sbSQL.append(" WHERE  \n"); 
		sbSQL.append(" T6230.F27 = ? \n"); 
		sbSQL.append(" AND T6230.F20 = ? \n"); 
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1, T6230_F27.F.toString());
				ps.setString(2,type );
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
	 * 待处理的个人借款意向
	 */
	public int queryDshOwnLoanCountCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append("SELECT COUNT(1) FROM S62.T6282 WHERE 1 = 1 AND T6282.F11 = ? ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
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
	 * 待处理的企业借款意向
	 */
	public int queryDshEnterpriseLoanCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append("SELECT COUNT(1) FROM S62.T6281 WHERE 1 = 1 AND T6281.F14 = ? ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
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
	 * 待审核的认证信息
	 */
	public int queryDshAuthCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append("SELECT COUNT(1) FROM ( SELECT IFNULL( ( SELECT T6120.F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 WHERE T6120.F01 = T6110.F01 AND T5123.F04 = 'QY' AND T6120.F05 = 'DSH' LIMIT 1 ), 0 ) AS F08 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01 = T6110.F01 WHERE T6110.F06 = 'ZRR' AND T6110.F13 = 'F'  ) T WHERE T.F08 = ?");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
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
	 * 提现初审/复审数量
	 */
	public int queryTxTrialCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append("  SELECT COUNT(1) FROM S61.T6130 \n");
		sbSQL.append(" INNER JOIN S61.T6110 ON T6130.F02 = T6110.F01  \n");
		sbSQL.append(" INNER JOIN S61.T6114 ON T6130.F03 = T6114.F01  \n");
		sbSQL.append(" INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01  \n");
		sbSQL.append(" WHERE  T6130.F09 =? ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
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
	 * 线下充值
	 */
	public int queryunderLineChargingCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append(" SELECT COUNT(1) FROM S71.T7150 \n");
		/*sbSQL.append(" INNER JOIN S61.T6110 ON T7150.F02 = T6110.F01  \n");
		sbSQL.append(" LEFT JOIN S71.T7110 AS T7110_1 ON T7110_1.F01 = T7150.F06  \n");
		sbSQL.append(" LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01  \n");
		sbSQL.append(" LEFT JOIN S71.T7110 AS T7110_2 ON T7110_2.F01 = T7150.F09  \n");*/
		sbSQL.append(" WHERE  T7150.F05 = ? ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
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
	 * 债权转让数目
	 */
	public int queryAssignmentCount(String type) throws Throwable {
		StringBuffer sbSQL = new StringBuffer(512);
		sbSQL.append(" SELECT COUNT(1) FROM S62.T6260 \n");
		sbSQL.append(" LEFT JOIN S62.T6251 ON S62.T6260.F02 = S62.T6251.F01  \n");
		sbSQL.append(" LEFT JOIN S61.T6110 ON S61.T6110.F01 = S62.T6251.F04  \n");
		sbSQL.append(" LEFT JOIN S62.T6230 ON T6251.F03 = T6230.F01  \n");
		sbSQL.append(" LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01  \n");
		sbSQL.append(" WHERE S62.T6260.F07 = ?  ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sbSQL.toString())) {
				ps.setString(1,type );
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}
}
