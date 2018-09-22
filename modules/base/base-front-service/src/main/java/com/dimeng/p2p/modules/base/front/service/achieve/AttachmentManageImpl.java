package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.enums.T6212_F04;
import com.dimeng.p2p.modules.base.front.service.AttachmentManage;

public class AttachmentManageImpl extends AbstractBaseService implements
		AttachmentManage {

	public AttachmentManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public String getName(int id) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S62.T6212 WHERE T6212.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getString(1);
		            }
		        }
		    }
		}
		return "";
	}

	@Override
	public T6212[] search() throws Throwable {
		try(Connection connection = getConnection()){
		 ArrayList<T6212> list = null;
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02 FROM S62.T6212 WHERE T6212.F04 = ? ORDER BY F03")) {
		        pstmt.setString(1, T6212_F04.QY.name());
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            while(resultSet.next()) {
		                T6212 record = new T6212();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getString(2);
		                if(list == null) {
		                    list = new ArrayList<>();
		                }
		                list.add(record);
		            }
		        }
		    }
		    return ((list == null || list.size() == 0) ? null: list.toArray(new T6212[list.size()]));
		}
	}

}
