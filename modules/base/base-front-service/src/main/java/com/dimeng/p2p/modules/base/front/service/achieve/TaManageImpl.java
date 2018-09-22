package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.modules.base.front.service.TaManage;
import com.dimeng.p2p.modules.base.front.service.entity.InvestInfo;

public class TaManageImpl extends AbstractBaseService implements TaManage
{

    public TaManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }

    /**
     * 首页TA发布
     * @return InvestInfo
     * @throws Throwable
     */
    @Override
    public List<InvestInfo> getPublishBids()
        throws Throwable
    {
        ArrayList<InvestInfo> list = null;
        try (Connection connection = getConnection();PreparedStatement pstmt = connection
                .prepareStatement("SELECT t6230.F05 AS amount,t6230.F03 AS title,t6230.F22 AS publishTime,"
                    + "t6110.F02 AS loginName FROM S62.T6230 t6230 INNER JOIN S61.T6110 t6110 ON t6110.F01 = t6230.F02 "
                    + "WHERE t6230.F20 IN (?,?,?,?) ORDER BY t6230.F22 DESC LIMIT 0,10");) {
            pstmt.setString(1, T6230_F20.TBZ.name());
            pstmt.setString(2, T6230_F20.DFK.name());
            pstmt.setString(3, T6230_F20.YJQ.name());
            pstmt.setString(4, T6230_F20.HKZ.name());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                long nowTime = new Date().getTime();
                while (resultSet.next()) {
                    InvestInfo record = new InvestInfo();
                    record.amount = resultSet.getDouble("amount");
                    record.biddingTitle = resultSet.getString("title");
                    record.intervalTime = getIntervalTime(nowTime-resultSet.getTimestamp("publishTime").getTime());
                    String loginName = resultSet.getString("loginName");
                    int length = loginName.length();
                    if(length>6){
                        record.loginName = loginName.substring(0, 2)+"***"+loginName.substring(length-2, length);
                    }else{
                        record.loginName = loginName.substring(0, 2)+"***"+loginName.substring(length-1, length);
                    }
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
    }

    /**
     * TA投资功能
     * @return InvestInfo
     * @throws Throwable
     */
    @Override
    public List<InvestInfo> getInvestments()
        throws Throwable
    {
        ArrayList<InvestInfo> list = null;
        try (Connection connection = getConnection();PreparedStatement pstmt = connection
                .prepareStatement("SELECT t6250.F04 AS amount,t6230.F03 AS title,t6250.F06 AS bidTime,t6110.F02 AS loginName "
                    + "FROM S62.T6250 t6250 INNER JOIN S61.T6110 t6110 ON t6110.F01 = t6250.F03 "
                    + "INNER JOIN S62.T6230 t6230 ON t6230.F01=t6250.F02 WHERE t6250.F07 = ? ORDER BY t6250.F06 DESC LIMIT 0,10");) {
            pstmt.setString(1, T6250_F07.F.name());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                long nowTime = new Date().getTime();
                while (resultSet.next()) {
                    InvestInfo record = new InvestInfo();
                    record.amount = resultSet.getDouble("amount");
                    record.biddingTitle = resultSet.getString("title");
                    record.intervalTime = getIntervalTime(nowTime-resultSet.getTimestamp("bidTime").getTime());
                    String loginName = resultSet.getString("loginName");
                    int length = loginName.length();
                    if(length>6){
                        record.loginName = loginName.substring(0, 2)+"***"+loginName.substring(length-2, length);
                    }else{
                        record.loginName = loginName.substring(0, 2)+"***"+loginName.substring(length-1, length);
                    }
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
    }
    
    private String getIntervalTime(long intervalTime) {
        StringBuffer sb = new StringBuffer();
        int day = (int)(intervalTime / (24 * 3600 * 1000L));
        if(day > 0){
            sb.append((day<10?"0"+day:day) + "天");
            intervalTime = intervalTime % (24 * 3600 * 1000L);
        }
        
        int hour = (int)(intervalTime / (3600 * 1000L));
        if(hour > 0){
            sb.append((hour<10?"0"+hour:hour) + "小时");
            intervalTime = intervalTime % (3600 * 1000L);
        }
        
        int minute = (int)(intervalTime / (60 * 1000L));
        sb.append((minute<10?"0"+minute:minute) + "分钟");
        return sb.toString();
    }
    
}
