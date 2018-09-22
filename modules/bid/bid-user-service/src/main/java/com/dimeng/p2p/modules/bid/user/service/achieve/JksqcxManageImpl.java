package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.bid.user.service.JksqcxManage;

public class JksqcxManageImpl extends AbstractBidManage implements JksqcxManage{

	public JksqcxManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<T6230> getApplyLoans(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT A.F01, A.F02, A.F03, A.F04, A.F05, A.F06, A.F07, A.F08, A.F09, A.F10, A.F11, A.F12, A.F13,A.F14, A.F15, A.F16, A.F17, A.F18, A.F19, A.F20, A.F21, A.F22, A.F23, A.F24, A.F25, A.F26 ,B.F21 as F27 ,B.F22 as F28 FROM S62.T6230 A,S62.T6231 B WHERE A.F01 = B.F01 AND A.F02 = ? AND A.F20 IN (?,?,?,?,?,?,?,?) ORDER BY A.F01 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6230>()
                {
                    @Override
                    public T6230[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6230> list = null;
                        while (resultSet.next())
                        {
                            T6230 record = new T6230();
                            T6231 t6231 = new T6231();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getInt(4);
                            record.F05 = resultSet.getBigDecimal(5);
                            record.F06 = resultSet.getBigDecimal(6);
                            record.F07 = resultSet.getBigDecimal(7);
                            record.F08 = resultSet.getInt(8);
                            record.F09 = resultSet.getInt(9);
                            record.F10 = T6230_F10.parse(resultSet.getString(10));
                            record.F11 = T6230_F11.parse(resultSet.getString(11));
                            record.F12 = T6230_F12.parse(resultSet.getString(12));
                            record.F13 = T6230_F13.parse(resultSet.getString(13));
                            record.F14 = T6230_F14.parse(resultSet.getString(14));
                            record.F15 = T6230_F15.parse(resultSet.getString(15));
                            record.F16 = T6230_F16.parse(resultSet.getString(16));
                            record.F17 = T6230_F17.parse(resultSet.getString(17));
                            record.F18 = resultSet.getInt(18);
                            record.F19 = resultSet.getInt(19);
                            record.F20 = T6230_F20.parse(resultSet.getString(20));
                            record.F21 = resultSet.getString(21);
                            record.F22 = resultSet.getTimestamp(22);
                            record.F23 = resultSet.getInt(23);
                            record.F24 = resultSet.getTimestamp(24);
                            record.F25 = resultSet.getString(25);
                            record.F26 = resultSet.getBigDecimal(26);
                            t6231.F21 = T6231_F21.parse(resultSet.getString(27));
                            t6231.F22 = resultSet.getInt(28);
                            record.F28 = t6231;
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new T6230[list.size()]);
                    }
                },
                paging,
                sql,
                serviceResource.getSession().getAccountId(),
                T6230_F20.DFB,
                T6230_F20.DFK,
                T6230_F20.DSH,
                T6230_F20.SQZ,
                T6230_F20.TBZ,
                T6230_F20.YFB,
                T6230_F20.YLB,
                T6230_F20.YZF);
        }
    }

}
