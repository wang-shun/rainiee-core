/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.modules.bid.front.service.IndexManage;

/**
 * @author guopeng
 * 
 */
public class IndexManageImpl extends AbstractBidManage implements IndexManage {

	public IndexManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int getUserCount() throws Throwable {
		int count = 0;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT COUNT(*) FROM S61.T6110 WHERE S61.T6110.F08 = 'ZC'")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						count = rs.getInt(1);
					}
				}
			}
		}
		return count;
	}

	@Override
	public BigDecimal getTzAmount() throws Throwable {
		BigDecimal amount = new BigDecimal(0);
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F06) FROM S62.T6251")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						amount = rs.getBigDecimal(1);
					}
				}
			}
		}
		return amount;
	}

	@Override
	public int getAchieveVersion() {
		return 0;
	}

}
