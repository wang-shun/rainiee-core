package com.dimeng.p2p.modules.bid.pay.service.achieve;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;

public abstract class AbstractBidService extends AbstractP2PService
{
    
    public AbstractBidService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final int DECIMAL_SCALE = 9;
    
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
    
}
