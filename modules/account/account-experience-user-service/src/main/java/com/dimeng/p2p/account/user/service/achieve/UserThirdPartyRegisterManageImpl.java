package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6171_F03;
import com.dimeng.p2p.account.user.service.UserThirdPartyRegisterManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;

/**
 * 
 * <QQ,WEIBO,WEIXIN 登录记录第三方标识信息>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年5月25日]
 */
public class UserThirdPartyRegisterManageImpl extends AbstractAccountService implements UserThirdPartyRegisterManage
{
    
    public UserThirdPartyRegisterManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void addT6171(ThirdUser userThird)
        throws Throwable
    {
        if (userThird == null)
        {
            throw new ParameterException("参数不能为空.");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6171 SET  F01 = ?, F03 = ?,F04 = ?, F06=?, F07=? ,F08 =? , F09 =? "))
            {
                pstmt.setInt(1, userThird.F01);
                pstmt.setString(2, T6171_F03.Y.name());
                pstmt.setString(3, userThird.openId);
                pstmt.setString(4, userThird.qqTen);
                pstmt.setString(5, userThird.token);
                pstmt.setLong(6, userThird.tokenTime);
                pstmt.setString(7, userThird.wxAuth);
                pstmt.execute();
            }
        }
        
    }
    
}
