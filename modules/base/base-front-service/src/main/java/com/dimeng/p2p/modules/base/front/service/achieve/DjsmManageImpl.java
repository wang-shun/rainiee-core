package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S51.entities.T5127;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.modules.base.front.service.DjsmManage;

public class DjsmManageImpl extends AbstractBaseService implements
		DjsmManage {

	public DjsmManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5127 get(T5127_F02 type, T5127_F03 dj) throws Throwable {
		try(Connection connection = getConnection()){
			 T5127 record = new T5127();
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F04, F05 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F03 = ? AND T5127.F06 = ? LIMIT 1")) {
			        pstmt.setString(1, type.name());
			        pstmt.setString(2, dj.name());
			        pstmt.setString(3, T5127_F06.QY.name());
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                record.F04 = resultSet.getBigDecimal(1);
			                record.F05 = resultSet.getBigDecimal(2);
			            }
			        }
			    }
			    return record;
		}
	}

}
