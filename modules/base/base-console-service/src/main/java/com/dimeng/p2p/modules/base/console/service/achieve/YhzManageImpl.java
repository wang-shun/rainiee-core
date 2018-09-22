package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.modules.base.console.service.YhzManage;
import com.dimeng.p2p.modules.base.console.service.entity.Role;

public class YhzManageImpl extends AbstractInformationService implements YhzManage {

	public YhzManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}


	@Override
	public void update(int id, String name, String desc) throws Throwable {
		if(id < 0){
			throw new ParameterException("指定的记录不存在！");
		}
		
		try(Connection connection = getConnection()){
			try(PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S10._1020 WHERE F02 = ? AND F01 <> ?")){
				pstmt.setString(1,  name);
				pstmt.setInt(2,  id);
				try(ResultSet resultSet = pstmt.executeQuery()){
					if(resultSet.next()){
						throw new ParameterException("用户组名称重复,请重新输入.");
					}
				}
				
			}
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S10._1020 SET F02 = ?, F03 = ? WHERE F01 = ?")) {
				pstmt.setString(1,  name);
				pstmt.setString(2,  desc);
				pstmt.setInt(3,  id);
				pstmt.execute();
			}
			
		}
	}
	
	


	@Override
	public Role get(int roleId) throws Throwable {
		try(Connection connection = getConnection()){
		Role record = null;
	    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03 FROM S10._1020 WHERE _1020.F01 = ? LIMIT 1")) {
	        pstmt.setInt(1, roleId);
	        try(ResultSet resultSet = pstmt.executeQuery()) {
	            if(resultSet.next()) {
	                record = new Role();
	                record.roleId = resultSet.getInt(1);
	                record.roleName = resultSet.getString(2);
	                record.desc = resultSet.getString(3);
	            }
	        }
	    }
	    return record;
	}
	}


	@Override
    public void del(int roleId)
        throws Throwable
    {
        if (roleId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            if (isExist(connection, roleId))
            {
                throw new LogicalException("该用户组下面存在用户,不能删除.");
            }
            execute(connection, "DELETE FROM S10._1020 WHERE F01 = ? ", roleId);
        }
    }
	
	protected Boolean isExist(Connection connection, int F02) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S10._1022 WHERE _1022.F02 = ? LIMIT 1")) {
	        pstmt.setInt(1, F02);
	        try(ResultSet resultSet = pstmt.executeQuery()) {
	            if(resultSet.next()) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

}
