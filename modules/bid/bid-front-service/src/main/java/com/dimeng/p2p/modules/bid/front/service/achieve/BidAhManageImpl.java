package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.modules.bid.front.service.BidAhManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Sytjsj;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyAhBidQuery;

public class BidAhManageImpl extends AbstractBidManage implements BidAhManage {

	public BidAhManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public Bdxq get(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			Bdxq record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26,ADDDATE(T6230.F22,INTERVAL T6230.F08 DAY) FROM S62.T6230"
							+ " WHERE T6230.F01 = ? AND T6230.F20 IN (?,?,?,?,?,?)")) {
				pstmt.setInt(1, id);
				pstmt.setString(2, T6230_F20.YFB.name());
				pstmt.setString(3, T6230_F20.TBZ.name());
				pstmt.setString(4, T6230_F20.DFK.name());
				pstmt.setString(5, T6230_F20.HKZ.name());
				pstmt.setString(6, T6230_F20.YJQ.name());
				pstmt.setString(7, T6230_F20.YDF.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new Bdxq();
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
						record.jsTime = resultSet.getTimestamp(27);
						record.djmc = getDjmc(record.F23);
					}
				}
			}
			return record;
		}
	}

	@Override
	public T6231 getExtra(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			T6231 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6231();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getDate(6);
						record.F07 = resultSet.getInt(7);
						record.F08 = resultSet.getString(8);
						record.F09 = resultSet.getString(9);
						record.F10 = resultSet.getTimestamp(10);
						record.F11 = resultSet.getTimestamp(11);
						record.F12 = resultSet.getTimestamp(12);
						record.F13 = resultSet.getTimestamp(13);
						record.F14 = resultSet.getTimestamp(14);
						record.F15 = resultSet.getTimestamp(15);
						record.F16 = resultSet.getString(16);
					}
				}
			}
			return record;
		}
	}

	@Override
	public PagingResult<Bdlb> search(BidQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?,?,?) AND T6110.F06 = ?");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6110_F06.ZRR);
        if (query != null)
        {
            boolean notFirst = false;
            T6211[] t6211s = query.getType();
            if (t6211s != null && t6211s.length > 0)
            {
                Set<T6211> valieLevels = new LinkedHashSet<>();
                for (T6211 t6211 : t6211s)
                {
                    if (t6211 == null)
                    {
                        continue;
                    }
                    valieLevels.add(t6211);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6211 info : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append("T6230.F04 = ?");
                        parameters.add(info.F01);
                    }
                    sql.append(")");
                }
            }
            
            T6230_F20[] levels = query.getStatus();
            if (levels != null && levels.length > 0)
            {
                Set<T6230_F20> valieLevels = new LinkedHashSet<>();
                for (T6230_F20 level : levels)
                {
                    if (level == null)
                    {
                        continue;
                    }
                    valieLevels.add(level);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6230_F20 valieLevel : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append(" T6230.F20 = ?");
                        parameters.add(valieLevel.name());
                    }
                    sql.append(")");
                }
            }
            CreditTerm[] terms = query.getTerm();
            if (terms != null && terms.length > 0)
            {
                Set<CreditTerm> validTerms = new LinkedHashSet<>();
                for (CreditTerm term : terms)
                {
                    if (term == null)
                    {
                        continue;
                    }
                    validTerms.add(term);
                }
                if (validTerms.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (CreditTerm term : validTerms)
                    {
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        switch (term)
                        {
                            case SGYYX:
                            {
                                sql.append(" T6230.F09 < 3 ");
                                break;
                            }
                            case SDLGY:
                            {
                                sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
                                break;
                            }
                            case LDJGY:
                            {
                                sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
                                break;
                            }
                            case JDSEGY:
                            {
                                sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
                                break;
                            }
                            case SEGYYS:
                            {
                                sql.append(" T6230.F09 > 12 ");
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    sql.append(")");
                    
                }
            }
        }
        sql.append("  ORDER BY T6230.F20, T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }

	@Override
	public PagingResult<Bdlb> searchAll(QyAhBidQuery query, Paging paging)
			throws Throwable {
		return search(query, T6230_F27.F, null, paging);
	}

	@Override
	public PagingResult<Bdlb> searchQy(QyAhBidQuery query, T6110_F06 t6110_F06,
			Paging paging) throws Throwable {
		return search(query, T6230_F27.F, t6110_F06, paging);
	}

	@Override
	public PagingResult<Bdlb> searchXXZQ(QyAhBidQuery query, Paging paging)
			throws Throwable {
		return search(query, T6230_F27.S, null, paging);
	}

	private PagingResult<Bdlb> search(QyAhBidQuery query, T6230_F27 t6230_F27,
 T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) ");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        if (t6230_F27 != null)
        {
            sql.append(" AND T6230.F27 = ?");
            parameters.add(t6230_F27);
        }
        if (t6110_F06 != null)
        {
            sql.append(" AND T6110.F06 = ?");
            parameters.add(t6110_F06);
        }
        if (query != null)
        {
            int rate = query.getRate();
            if (rate > 0)
            {
                switch (rate)
                {
                    case 1:
                    {
                        sql.append(" AND T6230.F06<?");
                        parameters.add(0.1);
                        break;
                    }
                    case 2:
                    {
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.1);
                        parameters.add(0.15);
                        break;
                    }
                    case 3:
                    {
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.15);
                        parameters.add(0.2);
                        break;
                    }
                    case 4:
                    {
                        sql.append(" AND T6230.F06>?");
                        parameters.add(0.2);
                        break;
                    }
                    case 5:
                        sql.append(" AND T6230.F04<?");
                        parameters.add(0.2);
                        break;
                }
            }
            
            int getProgress = query.getJd();
            if (getProgress > 0)
            {
                switch (getProgress)
                {
                    case 1:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                        parameters.add(50);
                        break;
                    }
                    case 2:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                        parameters.add(50);
                        parameters.add(80);
                        break;
                    }
                    case 3:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                        parameters.add(80);
                    }
                }
            }
            T6230_F20 creditStatus = query.getStatus();
            if (creditStatus != null)
            {
                if (creditStatus == T6230_F20.YJQ)
                {
                    sql.append(" AND (T6230.F20 = ? OR T6230.F20=?)");
                    parameters.add(creditStatus);
                    parameters.add("YDF");
                }
                else
                {
                    sql.append(" AND T6230.F20 = ?");
                    parameters.add(creditStatus);
                }
            }
            
            int qx = query.getQx();
            if (qx > 0)
            {
                switch (qx)
                {
                    case 1:
                        sql.append(" AND T6230.F09 < 3");
                        break;
                    case 2:
                        sql.append(" AND T6230.F09 >= 3 AND T6230.F09 <= 6");
                        break;
                    case 3:
                        sql.append(" AND T6230.F09 >= 6 AND T6230.F09 <= 12");
                        break;
                    case 4:
                        sql.append(" AND T6230.F09 >= 12");
                        break;
                }
            }
            
            int gm = query.getGm();
            if (gm > 0)
            {
                switch (gm)
                {
                    case 1:
                        sql.append(" AND T6230.F05 < 2000000");
                        break;
                    case 2:
                        sql.append(" AND T6230.F05 >= 2000000 AND T6230.F05 <= 5000000");
                        break;
                    case 3:
                        sql.append(" AND T6230.F05 >= 5000000 AND T6230.F05 <= 8000000");
                        break;
                    case 4:
                        sql.append(" AND T6230.F05 > 10000000");
                        break;
                }
            }
            
            int order = query.getOrder();
            if (order == 0 || order == 3)
            {
                sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
            }
            else if (order == 1)
            {
                sql.append(" ORDER BY T6230.F06 DESC");
            }
            else if (order == 2)
            {
                sql.append(" ORDER BY (T6230.F05-T6036.F07)/T6036.F05 DESC");
            }
        }
        else
        {
            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }

	@Override
	public T6250[] getRecord(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6250> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?  ORDER BY F06 DESC")) {
				pstmt.setInt(1, id);
				pstmt.setString(2, T6250_F07.F.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6250 record = new T6250();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getTimestamp(6);
						record.F07 = T6250_F07.parse(resultSet.getString(7));
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6250[list.size()]));
		}
	}

	@Override
	public Hkjllb[] getHk(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Hkjllb> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT SUM(F07), F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ) FROM S62.T6252 WHERE T6252.F02 = ? GROUP BY T6252.F08,F05")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Hkjllb record = new Hkjllb();
						record.F01 = resultSet.getBigDecimal(1);
						record.F02 = resultSet.getDate(2);
						record.F03 = T6252_F09.parse(resultSet.getString(3));
						record.F04 = resultSet.getTimestamp(4);
						record.F05 = resultSet.getString(5);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Hkjllb[list.size()]));
		}
	}

	@Override
	public T6237 getFk(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03 FROM S62.T6237 WHERE T6237.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					T6237 record = null;
					if (resultSet.next()) {
						record = new T6237();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getString(3);
					}
					return record;
				}
			}
		}
	}

	@Override
	public Dbxx getDB(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6180.F02 AS F05,(SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03), T6236.F05 AS F07 FROM S62.T6236 LEFT JOIN S61.T6180 ON T6236.F03 = T6180.F01 WHERE T6236.F02 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					Dbxx record = null;
					if (resultSet.next()) {
						record = new Dbxx();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = T6236_F04.parse(resultSet.getString(4));
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
					}
					return record;
				}
			}
		}
	}

	@Override
	public Bdylx[] getDylb(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Bdylx> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F03 FROM S62.T6234 WHERE T6234.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Bdylx record = new Bdylx();
						record.F01 = resultSet.getInt(1);
						record.F03 = resultSet.getInt(2);
						record.dyName = getDblx(record.F03);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Bdylx[list.size()]));
		}
	}

	@Override
	public Bdysx[] getDysx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Bdysx> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F03, F04 FROM S62.T6235 WHERE T6235.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Bdysx record = new Bdysx();
						record.F03 = resultSet.getInt(1);
						record.F04 = resultSet.getString(2);
						record.dxsxName = getDbsx(record.F03);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Bdysx[list.size()]));
		}
	}

	/**
	 * 获取担保类型名称
	 * 
	 * @param id
	 * @return
	 */
	private String getDblx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S62.T6213 WHERE T6213.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
			return null;
		}
	}

	/**
	 * 获取担保属性名称
	 * 
	 * @param id
	 * @return
	 */
	private String getDbsx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F03 FROM S62.T6214 WHERE T6214.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
			return null;
		}
	}

	@Override
	public T6233[] getFjfgk(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6233> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6233 WHERE T6233.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6233 record = new T6233();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getInt(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6233[list.size()]));
		}
	}

	@Override
	public T6232[] getFjgk(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6232> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6232 record = new T6232();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6232[list.size()]));
		}
	}

	@Override
	public T6251[] getZqxx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6251> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6251 WHERE T6251.F03 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6251 record = new T6251();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
						record.F08 = T6251_F08.parse(resultSet.getString(8));
						record.F09 = resultSet.getDate(9);
						record.F10 = resultSet.getDate(10);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6251[list.size()]));
		}
	}

	@Override
	public Zqzrxx[] getZqzrxx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Zqzrxx> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6262.F01 AS F01, T6262.F02 AS F02, T6262.F03 AS F03, T6262.F04 AS F04, T6262.F05 AS F05, T6262.F06 AS F06, T6262.F07 AS F07, T6262.F08 AS F08, T6262.F09 AS F09, T6260.F03 AS F10, T6260.F04 AS F11, T6251.F04 AS F12 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01  WHERE T6251.F03 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Zqzrxx record = new Zqzrxx();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getTimestamp(7);
						record.F08 = resultSet.getBigDecimal(8);
						record.F09 = resultSet.getBigDecimal(9);
						record.F10 = resultSet.getBigDecimal(10);
						record.F11 = resultSet.getBigDecimal(11);
						record.F12 = resultSet.getInt(12);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Zqzrxx[list.size()]));
		}
	}

	@Override
	public Tztjxx getStatisticsQy() throws Throwable {
		Tztjxx statistics = new Tztjxx();
		String sql = "SELECT IFNULL(SUM(T6230.F05),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? AND T6110.F06 = ?";
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, T6230_F20.YDF.name());
				ps.setString(2, T6230_F20.YJQ.name());
				ps.setString(3, T6230_F20.HKZ.name());
				ps.setString(4, T6230_F27.F.name());
				ps.setString(5, T6110_F06.FZRR.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						statistics.totleMoney = rs.getBigDecimal(1);
						statistics.totleCount = rs.getLong(2);
					}
				}
			}
		}

		statistics.userEarnMoney = getEarnMoneyQy();
		return statistics;
	}

	@Override
	public Tztjxx getStatisticsGr() throws Throwable {
		Tztjxx statistics = new Tztjxx();
		String sql = "SELECT IFNULL(SUM(T6230.F05),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? AND T6110.F06 = ?";
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, T6230_F20.YDF.name());
				ps.setString(2, T6230_F20.YJQ.name());
				ps.setString(3, T6230_F20.HKZ.name());
				ps.setString(4, T6230_F27.F.name());
				ps.setString(5, T6110_F06.ZRR.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						statistics.totleMoney = rs.getBigDecimal(1);
						statistics.totleCount = rs.getLong(2);
					}
				}
			}
		}

		statistics.userEarnMoney = getEarnMoneyGr();
		return statistics;
	}

	@Override
	public Tztjxx getStatisticsXxzq() throws Throwable {
		Tztjxx statistics = new Tztjxx();
		String sql = "SELECT IFNULL(SUM(T6230.F26),0),COUNT(*) FROM S62.T6230 WHERE  T6230.F20 IN (?,?,?) AND T6230.F27 = ?";
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, T6230_F20.YDF.name());
				ps.setString(2, T6230_F20.YJQ.name());
				ps.setString(3, T6230_F20.HKZ.name());
				ps.setString(4, T6230_F27.S.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						statistics.totleMoney = rs.getBigDecimal(1);
						statistics.totleCount = rs.getLong(2);
					}
				}
			}
		}

		statistics.userEarnMoney = getEarnMoneyXxzq();
		return statistics;
	}

	private BigDecimal getEarnMoneyQy() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
							+ " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND  T6110.F06 = ? AND  T6230.F27 = ? ")) {
				ps.setInt(1, FeeCode.TZ_LX);
				ps.setInt(2, FeeCode.TZ_WYJ);
				ps.setString(3, T6252_F09.YH.name());
				ps.setString(4, T6110_F06.FZRR.name());
				ps.setString(5, T6230_F27.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	private BigDecimal getEarnMoneyGr() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
							+ " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND  T6110.F06 = ? AND  T6230.F27 = ? ")) {
				ps.setInt(1, FeeCode.TZ_LX);
				ps.setInt(2, FeeCode.TZ_WYJ);
				ps.setString(3, T6252_F09.YH.name());
				ps.setString(4, T6110_F06.ZRR.name());
				ps.setString(5, T6230_F27.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	private BigDecimal getEarnMoneyXxzq() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230 WHERE"
							+ " T6252.F02 = T6230.F01  AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND  T6230.F27 = ? ")) {
				ps.setInt(1, FeeCode.TZ_LX);
				ps.setInt(2, FeeCode.TZ_WYJ);
				ps.setString(3, T6252_F09.YH.name());
				ps.setString(4, T6230_F27.S.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	/**
	 * 获取等级名称
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	private String getDjmc(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
			return "";
		}
	}

	@Override
	public T6211[] getBidType() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6211> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?")) {
				pstmt.setString(1, T6211_F03.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6211 record = new T6211();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6211[list.size()]));
		}
	}

	@Override
	public T5124[] getLevel() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T5124> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02 FROM S51.T5124 WHERE T5124.F05 = ?")) {
				pstmt.setString(1, T5124_F05.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T5124 record = new T5124();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T5124[list.size()]));
		}
	}

	@Override
	public Mbxx getMbxx(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			Mbxx record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT IFNULL(SUM(F07),0), F08 FROM S62.T6252 WHERE F02=?  AND F05 IN (?,?) AND F09 = ?  LIMIT 1")) {
				pstmt.setInt(1, loanId);
				pstmt.setInt(2, FeeCode.TZ_LX);
				pstmt.setInt(3, FeeCode.TZ_BJ);
				pstmt.setString(4, T6252_F09.WH.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new Mbxx();
						record.dhje = resultSet.getBigDecimal(1);
						record.F08 = resultSet.getDate(2);
					}
				}
			}
			return record;
		}
	}

	@Override
	public Blob getAttachment(int id) throws Throwable {
		String sql = "SELECT F06 FROM S62.T6233 WHERE F01=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBlob(1);
					}
				}
			}
		}
		return null;
	}

	@Override
	public PagingResult<Bdlb> searchCredit(int userId, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S51.T5124 ON T6230.F23 = T5124.F01");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?) AND T6230.F02 = ?");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(userId);
        
        sql.append("  ORDER BY T6230.F20 , T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }

	@Override
	public Bdlb getNewBid() throws Throwable {
		try (Connection connection = getConnection()) {
			Bdlb record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15, T6230.F11 AS F16, T6230.F13 AS F17, T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01 WHERE T6230.F20 IN (?,?) ORDER BY T6230.F20 ,F22 DESC LIMIT 1")) {
				pstmt.setString(1, T6230_F20.YFB.name());
				pstmt.setString(2, T6230_F20.TBZ.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new Bdlb();
						record.F01 = resultSet.getString(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getInt(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
						record.F08 = resultSet.getBigDecimal(8);
						record.F09 = resultSet.getInt(9);
						record.F10 = resultSet.getInt(10);
						record.F11 = T6230_F20.parse(resultSet.getString(11));
						record.F12 = resultSet.getString(12);
						record.F13 = resultSet.getTimestamp(13);
						record.F14 = resultSet.getInt(14);
						record.F15 = resultSet.getString(15);
						record.F16 = T6230_F11.parse(resultSet.getString(16));
						record.F17 = T6230_F13.parse(resultSet.getString(17));
						record.F18 = T6230_F14.parse(resultSet.getString(18));
						record.proess = (record.F06.doubleValue() - record.F08
								.doubleValue()) / record.F06.doubleValue();

					}
				}
			}
			return record;
		}
	}

	@Override
	public int getTbCount(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(F01) FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				pstmt.setString(2, T6250_F07.F.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public Sytjsj getSytj() throws Throwable {
		Sytjsj sytjsj = new Sytjsj();
		return sytjsj;
	}

	@Override
	public T6240 getXXZQ(int bidId) throws Throwable {
		if (bidId <= 0)
			return null;
		T6240 record = null;
		try (PreparedStatement pstmt = getConnection()
				.prepareStatement(
						"SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, bidId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6240();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getString(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getBigDecimal(7);
				}
			}
		}
		return record;
	}

	@Override
	public IndexStatic getIndexStatic() throws Throwable {
		IndexStatic indexStatic = new IndexStatic();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=?")) {
				ps.setString(1, T6250_F07.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.ljcj = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F05) FROM S62.T6230 WHERE F20 IN(?,?,?)")) {
				ps.setString(1, T6230_F20.HKZ.name());
				ps.setString(2, T6230_F20.YJQ.name());
				ps.setString(3, T6230_F20.YDF.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.jkze = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND (F05=? OR F05=?)")) {
				ps.setString(1, T6252_F09.YH.name());
				ps.setInt(2, FeeCode.TZ_BJ);
				ps.setInt(3, FeeCode.TZ_LX);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.yhbx = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND (F05=? OR F05=?)")) {
				ps.setString(1, T6252_F09.WH.name());
				ps.setInt(2, FeeCode.TZ_BJ);
				ps.setInt(3, FeeCode.TZ_LX);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.dhbx = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND DATEDIFF(?,F08)>0")) {
				ps.setString(1, T6252_F09.WH.name());
				ps.setDate(2, getCurrentDate(connection));
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.yqhk = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=? AND DATE(F06)=DATE_SUB(CURDATE(),INTERVAL 1 DAY)")) {
				ps.setString(1, T6250_F07.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.zrcj = rs.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=? AND YEAR(F06) = YEAR(CURDATE())")) {
				ps.setString(1, T6250_F07.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						indexStatic.bncj = rs.getBigDecimal(1);
					}
				}
			}
			try(PreparedStatement ps = connection.prepareStatement("select SUM(F06) from S62.T6282")){
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()){
						indexStatic.rzzje = rs.getBigDecimal(1);
					}
				}
			}
			try(PreparedStatement ps = connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 IN (?,?,?) AND T6252.F09 = ?")){
				ps.setInt(1, FeeCode.TZ_FX);
				ps.setInt(2, FeeCode.TZ_WYJ);
				ps.setInt(3, FeeCode.TZ_LX);
				ps.setString(4, T6252_F09.YH.name());
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()){
						indexStatic.yhzsy = rs.getBigDecimal(1);
					}
				}
			}
		}
		return indexStatic;
	}

	@Override
	public T6240 getT6240(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			T6240 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6240();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
					}
				}
			}
			return record;
		}
	}

	@Override
	public PagingResult<Bdlb> searchBids(BidQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?)");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        if (query != null)
        {
            boolean notFirst = false;
            T6211[] t6211s = query.getType();
            if (t6211s != null && t6211s.length > 0)
            {
                Set<T6211> valieLevels = new LinkedHashSet<>();
                for (T6211 t6211 : t6211s)
                {
                    if (t6211 == null)
                    {
                        continue;
                    }
                    valieLevels.add(t6211);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6211 info : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append("T6230.F04 = ?");
                        parameters.add(info.F01);
                    }
                    sql.append(")");
                }
            }
            
            T6230_F20[] levels = query.getStatus();
            if (levels != null && levels.length > 0)
            {
                Set<T6230_F20> valieLevels = new LinkedHashSet<>();
                for (T6230_F20 level : levels)
                {
                    if (level == null)
                    {
                        continue;
                    }
                    valieLevels.add(level);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6230_F20 valieLevel : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append(" T6230.F20 = ?");
                        parameters.add(valieLevel.name());
                    }
                    sql.append(")");
                }
            }
            CreditTerm[] terms = query.getTerm();
            if (terms != null && terms.length > 0)
            {
                Set<CreditTerm> validTerms = new LinkedHashSet<>();
                for (CreditTerm term : terms)
                {
                    if (term == null)
                    {
                        continue;
                    }
                    validTerms.add(term);
                }
                if (validTerms.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (CreditTerm term : validTerms)
                    {
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        switch (term)
                        {
                            case SGYYX:
                            {
                                sql.append(" T6230.F09 < 3 ");
                                break;
                            }
                            case SDLGY:
                            {
                                sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
                                break;
                            }
                            case LDJGY:
                            {
                                sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
                                break;
                            }
                            case JDSEGY:
                            {
                                sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
                                break;
                            }
                            case SEGYYS:
                            {
                                sql.append(" T6230.F09 > 12 ");
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    sql.append(")");
                    
                }
            }
        }
        sql.append("  ORDER BY T6230.F20, T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }

	@Override
	public T6212[] getT6212(int loanId, boolean b) throws Throwable {
		String sql = "";
		if (b) {
			sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6233 WHERE T6233.F02 = ?)";
		} else {
			sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6232 WHERE T6232.F02 = ?)";
		}
		try (Connection connection = getConnection()) {
			ArrayList<T6212> list = null;
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6212 record = new T6212();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6212[list.size()]));
		}
	}

	@Override
	public T5127_F03 getTzdj(int userID) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, userID);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return getDj(T5127_F02.TZ, resultSet.getBigDecimal(1));
		            }
		        }
		    }
	}
	return null;
	}
	
	//根据金额,类型得到等级
		private T5127_F03 getDj(T5127_F02 type,BigDecimal money) throws SQLException{
			try(Connection connection = getConnection()){
				 try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F04 > ? AND T5127.F05 <= ? AND T5127.F06 = ? LIMIT 1")) {
				        pstmt.setString(1, type.name());
				        pstmt.setBigDecimal(2, money);
				        pstmt.setBigDecimal(3, money);
				        pstmt.setString(4, T5127_F06.QY.name());
				        try(ResultSet resultSet = pstmt.executeQuery()) {
				            if(resultSet.next()) {
				                return T5127_F03.parse(resultSet.getString(1));
				            }
				        }
				    }
			}
			return null;
		}

	
		
		
		
		@Override
		public PagingResult<Bdlb> searchDb(QyAhBidQuery query, T6110_F06 t6110_F06,
				Paging paging) throws Throwable {
			return searchAhtc(query, T6230_F27.F, T6230_F11.S, t6110_F06, paging);
		}

		@Override
		public PagingResult<Bdlb> searchRz(QyAhBidQuery query, T6110_F06 t6110_F06,
				Paging paging) throws Throwable {
			return searchAhtc(query, T6230_F27.F, T6230_F11.F, t6110_F06, paging);
		}
		
		private PagingResult<Bdlb> searchAhtc(QyAhBidQuery query, T6230_F27 t6230_F27,
 T6230_F11 t6230_F11,
        T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) ");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        
        sql.append(" AND T6230.F11 = ?");
        parameters.add(t6230_F11);
        
        if (t6230_F27 != null)
        {
            sql.append(" AND T6230.F27 = ?");
            parameters.add(t6230_F27);
        }
        if (t6110_F06 != null)
        {
            sql.append(" AND T6110.F06 = ?");
            parameters.add(t6110_F06);
        }
        if (query != null)
        {
            int rate = query.getRate();
            if (rate > 0)
            {
                switch (rate)
                {
                    case 1:
                    {
                        sql.append(" AND T6230.F06<?");
                        parameters.add(0.1);
                        break;
                    }
                    case 2:
                    {
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.1);
                        parameters.add(0.15);
                        break;
                    }
                    case 3:
                    {
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.15);
                        parameters.add(0.2);
                        break;
                    }
                    case 4:
                    {
                        sql.append(" AND T6230.F06>?");
                        parameters.add(0.2);
                        break;
                    }
                    case 5:
                        sql.append(" AND T6230.F04<?");
                        parameters.add(0.2);
                        break;
                }
            }
            
            int getProgress = query.getJd();
            if (getProgress > 0)
            {
                switch (getProgress)
                {
                    case 1:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                        parameters.add(50);
                        break;
                    }
                    case 2:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                        parameters.add(50);
                        parameters.add(80);
                        break;
                    }
                    case 3:
                    {
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                        parameters.add(80);
                    }
                }
            }
            T6230_F20 creditStatus = query.getStatus();
            if (creditStatus != null)
            {
                if (creditStatus == T6230_F20.YJQ)
                {
                    sql.append(" AND (T6230.F20 = ? OR T6230.F20=?)");
                    parameters.add(creditStatus);
                    parameters.add("YDF");
                }
                else
                {
                    sql.append(" AND T6230.F20 = ?");
                    parameters.add(creditStatus);
                }
            }
            
            int qx = query.getQx();
            if (qx > 0)
            {
                switch (qx)
                {
                    case 1:
                        sql.append(" AND T6230.F09 < 3");
                        break;
                    case 2:
                        sql.append(" AND T6230.F09 >= 3 AND T6230.F09 <= 6");
                        break;
                    case 3:
                        sql.append(" AND T6230.F09 >= 6 AND T6230.F09 <= 12");
                        break;
                    case 4:
                        sql.append(" AND T6230.F09 >= 12");
                        break;
                }
            }
            
            int gm = query.getGm();
            if (gm > 0)
            {
                switch (gm)
                {
                    case 1:
                        sql.append(" AND T6230.F05 < 2000000");
                        break;
                    case 2:
                        sql.append(" AND T6230.F05 >= 2000000 AND T6230.F05 <= 5000000");
                        break;
                    case 3:
                        sql.append(" AND T6230.F05 >= 5000000 AND T6230.F05 <= 8000000");
                        break;
                    case 4:
                        sql.append(" AND T6230.F05 > 10000000");
                        break;
                }
            }
            
            int order = query.getOrder();
            if (order == 0 || order == 3)
            {
                sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
            }
            else if (order == 1)
            {
                sql.append(" ORDER BY T6230.F06 DESC");
            }
            else if (order == 2)
            {
                sql.append(" ORDER BY (T6230.F05-T6036.F07)/T6036.F05 DESC");
            }
        }
        else
        {
            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
}
