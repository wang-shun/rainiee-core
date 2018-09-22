/**
 * 
 */
package com.dimeng.p2p.service;

import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.*;
import com.dimeng.p2p.variables.defines.URLVariable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * @author guopeng
 * 
 */
public class SafetymsgViewManageImpl extends AbstractP2PService implements SafetymsgViewManage
{

    public SafetymsgViewManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public VariableBean getSafetymsgView()throws Throwable {
        int userId =0;
        VariableBean view = URLVariable.USER_ZRR_NCIIC;
        try{
            userId = serviceResource.getSession().getAccountId();
        }catch(Exception e){
        }
        try(Connection connection = getConnection()){
            T6110 record = new T6110();
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
                pstmt.setInt(1, userId);
                try(ResultSet resultSet = pstmt.executeQuery()) {
                    if(resultSet.next()) {
                        record.F06 = T6110_F06.parse(resultSet.getString(1));
                    }
                }
            }
            if(T6110_F06.FZRR == record.F06){
                view = URLVariable.USER_NCIIC;
            }
            return view;
        }
    }
    
}
