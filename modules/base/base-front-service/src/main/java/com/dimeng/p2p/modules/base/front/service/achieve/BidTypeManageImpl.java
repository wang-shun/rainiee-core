package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.modules.base.front.service.BidTypeManage;

public class BidTypeManageImpl extends AbstractBaseService
		implements BidTypeManage {

	public BidTypeManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}


	@Override
	public String getName(int id) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S62.T6211 WHERE T6211.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getString(1);
		            }
		        }
		    }
		}
		return null;
	}

	

}
