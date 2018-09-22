package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.math.BigDecimal;
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
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6262;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.modules.bid.front.service.TransferAhManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.ZqzrEntity;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrtj;
import com.dimeng.p2p.modules.bid.front.service.query.TransferAhQuery;

public class TransferAhManageImpl extends AbstractBidManage implements
	TransferAhManage {

	public TransferAhManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<Zqzqlb> search(TransferAhQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6260.F02 AS F01, T6260.F03 AS F02, T6260.F04 AS F03, T6260.F05 AS F04, T6260.F06 AS F05, T6260.F07 AS F06, T6260.F08 AS F07, T6251.F04 AS F08, T6251.F05 AS F09, T6251.F06 AS F10, T6251.F07 AS F11, T6230.F03 AS F12, T6230.F04 AS F13, T6230.F06 AS F14, T6230.F09 AS F15, T6230.F23 AS F16,T6230.F11 AS F17,T6230.F13 AS F18,T6230.F14 AS F19,T6231.F02 AS F20,T6231.F03 AS F21,T6230.F01 AS F22,T6260.F01 AS F23,T6251.F01 AS F24,T6230.F21 AS F25 FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6251.F03 = T6231.F01");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE 1=1 AND T6260.F07 = ? AND T6251.F08 = ?");
        parameters.add(T6260_F07.ZRZ);
        parameters.add(T6251_F08.S);
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
            
            T5124[] levels = query.getCreditLevel();
            if (levels != null && levels.length > 0)
            {
                Set<T5124> valieLevels = new LinkedHashSet<>();
                for (T5124 level : levels)
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
                    for (T5124 valieLevel : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append(" T6230.F23 = ?");
                        parameters.add(valieLevel.F01);
                    }
                    sql.append(")");
                }
            }
            
            int zrje = query.getAmount();
            if (zrje > 0)
            {
                switch (zrje)
                {
                    case 1:
                        sql.append(" AND T6260.F03 < 10000");
                        break;
                    
                    case 2:
                        sql.append(" AND T6260.F03 >= 20000 AND T6260.F03 <= 30000");
                        break;
                    
                    case 3:
                        sql.append(" AND T6260.F03 >= 40000 AND T6260.F03 <= 50000");
                        break;
                    
                    case 4:
                        sql.append(" AND T6260.F03 > 50000");
                        break;
                }
            }
            
            int syqx = query.getResidue();
            if (syqx > 0)
            {
                switch (syqx)
                {
                    case 1:
                        sql.append(" AND T6231.F03 < 3");
                        break;
                    
                    case 2:
                        sql.append(" AND T6231.F03 >= 3 AND T6231.F03 <= 6");
                        break;
                    
                    case 3:
                        sql.append(" AND T6231.F03 >= 6 AND T6231.F03 <= 12");
                        break;
                    
                    case 4:
                        sql.append(" AND T6231.F03 > 12");
                        break;
                }
            }
            
            int rgsy = query.getEarn();
            if (rgsy > 0)
            {
                switch (rgsy)
                {
                    case 1:
                        sql.append(" AND T6230.F06 < 0.1");
                        break;
                    
                    case 2:
                        sql.append(" AND T6230.F06 >= 0.1 AND T6230.F06 <= 0.15");
                        break;
                    
                    case 3:
                        sql.append(" AND T6230.F06 >= 0.15 AND T6230.F06 <= 0.2");
                        break;
                }
            }
            
            int xmlx = query.getTypes();
            if (xmlx > 0)
            {
                switch (xmlx)
                {
                    case 1:
                        sql.append(" AND T6230.F11 = ?");
                        parameters.add("S");
                        break;
                    
                    case 2:
                        sql.append(" AND T6230.F11 = ?");
                        parameters.add("F");
                        break;
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
        sql.append("  ORDER BY T6260.F05 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Zqzqlb>()
            {
                @Override
                public Zqzqlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Zqzqlb> list = null;
                    while (resultSet.next())
                    {
                        Zqzqlb record = new Zqzqlb();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getBigDecimal(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6260_F07.parse(resultSet.getString(6));
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.F10 = resultSet.getBigDecimal(10);
                        record.F11 = resultSet.getBigDecimal(11);
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getInt(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = resultSet.getInt(15);
                        record.F16 = resultSet.getInt(16);
                        record.F18 = getBxydj(record.F16);
                        record.F19 = T6230_F11.parse(resultSet.getString(17));
                        record.F20 = T6230_F13.parse(resultSet.getString(18));
                        record.F21 = T6230_F14.parse(resultSet.getString(19));
                        record.F22 = resultSet.getInt(20);
                        record.F23 = resultSet.getInt(21);
                        record.F24 = resultSet.getInt(22);
                        record.F25 = resultSet.getInt(23);
                        record.F26 = resultSet.getString(25);
                        try
                        {
                            record.dsbx = getDsbx(resultSet.getInt(24));
                        }
                        catch (Throwable e)
                        {
                            logger.error("TransferAhManageImpl.search() error", e);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Zqzqlb[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
	
	// 待收本息
	private BigDecimal getDsbx(int zqId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F11 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?")) {
				pstmt.setInt(1, zqId);
				pstmt.setInt(2, FeeCode.TZ_LX);
				pstmt.setInt(3, FeeCode.TZ_BJ);
				pstmt.setString(4, T6252_F09.WH.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	/**
	 * 查询信用等级
	 * @param F01
	 * @return
	 * @throws SQLException
	 */
	private String getBxydj(int F01) throws SQLException {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, F01);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getString(1);
		            }
		        }
		    }
			return null;
	}
	}

	@Override
	public Zqzrtj getStatistics() throws Throwable {
		Zqzrtj credit = new Zqzrtj();
		String sql = "SELECT IFNULL(SUM(T6262.F05),0),IFNULL(SUM(T6262.F08),0), COUNT(T6262.F01) FROM S62.T6262";
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						credit.totleMoney = rs.getBigDecimal(1);
						credit.userEarnMoney = rs.getBigDecimal(2);
						credit.totleCount = rs.getLong(3);
					}
				}
			}
		}
		return credit;
	}

	@Override
	public ZqzrEntity getZqzrxx(int id) throws Throwable {
		try(Connection connection = getConnection()){
			ZqzrEntity record = null;
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6251.F02 AS F01, T6251.F03 AS F02, T6251.F04 AS F03, T6251.F05 AS F04, T6251.F06 AS F05, T6251.F07 AS F06, T6251.F08 AS F07, T6251.F09 AS F08, T6251.F10 AS F09, T6260.F01 AS F10, T6260.F02 AS F11, T6260.F03 AS F12, T6260.F04 AS F13, T6260.F05 AS F14, T6260.F06 AS F15, T6260.F07 AS F16, T6260.F08 AS F17 FROM S62.T6251 INNER JOIN S62.T6260 ON T6251.F01 = T6251.F01 WHERE T6260.F01 = ? AND T6260.F07 IN (?,?) LIMIT 1")) {
			        pstmt.setInt(1, id);
			        pstmt.setString(2, T6260_F07.ZRZ.name());
			        pstmt.setString(3, T6260_F07.YJS.name());
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                record = new ZqzrEntity();
			                record.F01 = resultSet.getString(1);
			                record.F02 = resultSet.getInt(2);
			                record.F03 = resultSet.getInt(3);
			                record.F04 = resultSet.getBigDecimal(4);
			                record.F05 = resultSet.getBigDecimal(5);
			                record.F06 = resultSet.getBigDecimal(6);
			                record.F07 = T6251_F08.parse(resultSet.getString(7));
			                record.F08 = resultSet.getDate(8);
			                record.F09 = resultSet.getDate(9);
			                record.F10 = resultSet.getInt(10);
			                record.F11 = resultSet.getInt(11);
			                record.F12 = resultSet.getBigDecimal(12);
			                record.F13 = resultSet.getBigDecimal(13);
			                record.F14 = resultSet.getTimestamp(14);
			                record.F15 = resultSet.getTimestamp(15);
			                record.F16 = T6260_F07.parse(resultSet.getString(16));
			                record.F17 = resultSet.getBigDecimal(17);
			            }
			        }
			    }
			    return record;
		}
	}

	@Override
	public T6262[] getZqzclb(int id) throws Throwable {
		try(Connection connection = getConnection()){
			 ArrayList<T6262> list = null;
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6262 WHERE T6262.F02 = ?")) {
			        pstmt.setInt(1, id);
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            while(resultSet.next()) {
			                T6262 record = new T6262();
			                record.F01 = resultSet.getInt(1);
			                record.F02 = resultSet.getInt(2);
			                record.F03 = resultSet.getInt(3);
			                record.F04 = resultSet.getBigDecimal(4);
			                record.F05 = resultSet.getBigDecimal(5);
			                record.F06 = resultSet.getBigDecimal(6);
			                record.F07 = resultSet.getTimestamp(7);
			                record.F08 = resultSet.getBigDecimal(8);
			                record.F09 = resultSet.getBigDecimal(9);
			                if(list == null) {
			                    list = new ArrayList<>();
			                }
			                list.add(record);
			            }
			        }
			    }
			    return ((list == null || list.size() == 0) ? null: list.toArray(new T6262[list.size()]));
		}
	}
	

}
