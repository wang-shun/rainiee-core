/*
 * 文 件 名:  BadClaimManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年7月4日
 */
package com.dimeng.p2p.modules.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.service.AbstractBadClaimService;
import com.dimeng.p2p.repeater.claim.BadClaimManage;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecord;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecordQuery;
import com.dimeng.util.StringHelper;

/**
 * <不良债权管理实现类>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年7月4日]
 */
public class BadClaimManageImpl extends AbstractBadClaimService implements BadClaimManage
{
    
    /** <默认构造函数>
     */
    public BadClaimManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<BuyBadClaimRecord> getBuyBadClaimRecord(BuyBadClaimRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6264.F02 AS F01, T6230.F03 AS F02, T6230.F05 AS F03, T6231.F03 AS F04, T6231.F02 AS F05, ");
        sql.append("T6265.F05 AS F06, T6265.F06 AS F07 ,T6265.F07 AS F08 ");
        sql.append("FROM S62.T6264 LEFT JOIN S62.T6265 ON T6264.F01 = T6265.F02 LEFT JOIN S62.T6230 ON T6230.F01 = T6264.F03 ");
        sql.append("LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6264.F04 = 'YZR' ");
        List<Object> parameters = new ArrayList<Object>();
        if (query != null)
        {
            if (query.getUserId() > 0)
            {
                sql.append(" AND T6265.F03 = ? ");
                parameters.add(query.getUserId());
            }
            else
            {
                return null;
            }
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String string = query.getCreditNo();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6264.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = query.getLoanTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = query.getStartTime();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6265.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getEndTime();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6265.F07) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" ORDER BY T6265.F07 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BuyBadClaimRecord>()
            {
                
                @Override
                public BuyBadClaimRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<BuyBadClaimRecord> list = null;
                    BuyBadClaimRecord badClaim = null;
                    while (rs.next())
                    {
                        badClaim = new BuyBadClaimRecord();
                        badClaim.creditNo = rs.getString(1);
                        badClaim.loanTitle = rs.getString(2);
                        badClaim.loanAmount = rs.getBigDecimal(3);
                        badClaim.syPeriods = rs.getInt(4);
                        badClaim.zPeriods = rs.getInt(5);
                        badClaim.creditPrice = rs.getBigDecimal(6);
                        badClaim.subscribePrice = rs.getBigDecimal(7);
                        badClaim.subscribeTime = rs.getTimestamp(8);
                        if (list == null)
                        {
                            list = new ArrayList<BuyBadClaimRecord>();
                        }
                        list.add(badClaim);
                    }
                    return list == null ? null : list.toArray(new BuyBadClaimRecord[list.size()]);
                }
                
            }, paging, sql.toString(), parameters);
        }
    }
    
}
