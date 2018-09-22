package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.account.user.service.EnterpriseManage;
import com.dimeng.p2p.account.user.service.entity.Enterprise;

public class EnterpriseManageImpl extends AbstractAccountService implements
		EnterpriseManage {

	public EnterpriseManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public Enterprise get(int id) throws Throwable{
		try(Connection connection = getConnection()){
			Enterprise record = null;
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6161.F01 AS F01, T6161.F02 AS F02, T6161.F03 AS F03, T6161.F04 AS F04, T6161.F05 AS F05, T6161.F06 AS F06, T6161.F07 AS F07, T6161.F08 AS F08, T6161.F09 AS F09, T6161.F10 AS F10, T6161.F11 AS F11, T6161.F12 AS F12, T6161.F13 AS F13, T6161.F14 AS F14, T6161.F15 AS F15, T6162.F02 AS F16, T6162.F03 AS F17, T6162.F04 AS F18, T6162.F05 AS F19 FROM S61.T6161 INNER JOIN S61.T6162 ON T6161.F01 = T6162.F01 WHERE T6161.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new Enterprise();
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
		            }
		        }
		    }
		    return record;
		}
	}

}
