package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6299;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S70.entities.T7050;
import com.dimeng.p2p.S70.entities.T7051;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecord;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecordInfo;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.FrontReleaseBid;
import com.dimeng.p2p.modules.bid.front.service.entity.FrontT6250;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.modules.bid.front.service.entity.InvestorTotal;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Sytjsj;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.front.service.query.BidAllQuery;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.BidQueryAccount;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery_Order;
import com.dimeng.p2p.modules.bid.front.service.query.BidTypeQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQueryAccount;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQueryExt;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidTypeQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class BidManageImpl extends AbstractBidManage implements BidManage
{
    
    public BidManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Bdxq get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Bdxq record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18, T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, T6230.F26,ADDDATE(T6230.F22,INTERVAL T6230.F08 DAY),T6230.F28,T6230.F32,T6230.F33,T6236.F03 FROM S62.T6230 LEFT JOIN S62.T6236 ON T6230.F01 = T6236.F02"
                    + " WHERE T6230.F01 = ? AND T6230.F20 IN (?,?,?,?,?,?,?)"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6230_F20.YFB.name());
                pstmt.setString(3, T6230_F20.TBZ.name());
                pstmt.setString(4, T6230_F20.DFK.name());
                pstmt.setString(5, T6230_F20.HKZ.name());
                pstmt.setString(6, T6230_F20.YJQ.name());
                pstmt.setString(7, T6230_F20.YDF.name());
                pstmt.setString(8, T6230_F20.YZR.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.proess =
                            (record.F05.doubleValue() - record.F07.doubleValue()) / record.F05.doubleValue();
                        record.xsb = T6230_F28.parse(resultSet.getString(28));
                        record.F32 = resultSet.getInt(29);
                        record.F33 = T6230_F33.parse(resultSet.getString(30));
                        record.isXmdt = viewBidProgresCount(id);
                        record.productRiskLevel = getProductRiskLevel(connection, record.F32);
                        record.assureId = resultSet.getInt(31);
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 查询标的对应的产品风险等级
     * <功能详细描述>
     * @param connection
     * @param productId
     * @return
     * @throws Throwable
     */
    private T6216_F18 getProductRiskLevel(Connection connection, int productId)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F18 FROM S62.T6216 WHERE T6216.F01=?"))
        {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6216_F18.parse(rs.getString(1));
                }
            }
        }
        return T6216_F18.BSX;
    }
    
    @Override
    public T6231 getExtra(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6231 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F21 AS F17, F22 AS F18, F25 AS F19, F26 AS F20, F27 AS F21, F28 AS F22, F33 AS F23, F34 AS F24, F36 AS F25 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.F21 = T6231_F21.parse(resultSet.getString(17));
                        record.F22 = resultSet.getInt(18);
                        record.F25 = resultSet.getBigDecimal(19);
                        record.F26 = resultSet.getBigDecimal(20);
                        record.F27 = T6231_F27.parse(resultSet.getString(21));
                        record.F28 = resultSet.getBigDecimal(22);
                        record.F33 = T6231_F33.parse(resultSet.getString(23));
                        record.F34 = resultSet.getTimestamp(24);
                        record.F36 = T6231_F36.parse(resultSet.getString(25));
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
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20,T6230.F10 AS F21,T6230.F12 AS F22 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?,?,?) AND T6110.F06 = ?");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6110_F06.ZRR);
        // if (query != null)
        //{
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
        //}
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
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
                        record.F21 = T6230_F10.parse(resultSet.getString("F21"));
                        record.F22 = T6230_F12.parse(resultSet.getString("F22"));
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
    public PagingResult<Bdlb> search(BidQuery_Order query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20,T6230.F10 AS F21,T6230.F12 AS F22,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN ( T6230.F05- T6230.F07) ELSE T6230.F05 END AS F27, CASE WHEN T6231.F21='S' THEN (T6231.F22/DAYOFMONTH(last_day(T6230.F24))) ELSE T6230.F09 END AS F28 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 0 ELSE T6230.F07 END AS F29 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 100 ELSE case WHEN T6230.F20='YFB' THEN -1 ELSE((T6230.F05-T6230.F07)/T6230.F05)*100 END END AS F30,(SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F31, (      SELECT T6161.F04 FROM   S61.T6161   WHERE   T6161.F01 = T6236.F03   ) AS F32, T6230.F28 AS F33, T6231.F27 AS F34, T6231.F28 AS F35  FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02  LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 ");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?,?,?) AND T6110.F06 = ?");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6110_F06.ZRR);
        //        if (query != null)
        //        {
        if (query.getProductId() > 0)
        {
            sql.append(" AND T6230.F32 = ?");
            parameters.add(query.getProductId());
        }
        
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
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
                            //sql.append(" T6230.F09 < 3 ");
                            sql.append("CASE WHEN T6231.F21 = 'F' THEN T6230.F09 < 3 ELSE T6231.F22 < 90 END");
                            break;
                        }
                        case SDLGY:
                        {
                            //sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
                            sql.append("CASE WHEN T6231.F21 = 'F' THEN (T6230.F09 >= 3 AND T6230.F09 <= 6) ELSE (T6231.F22 >= 90 AND T6231.F22 <= 180) END ");
                            break;
                        }
                        case LDJGY:
                        {
                            //sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
                            sql.append("CASE WHEN T6231.F21 = 'F' THEN (T6230.F09 >= 6 AND T6230.F09 <= 9) ELSE (T6231.F22 >= 180 AND T6231.F22 <= 270) END ");
                            break;
                        }
                        case JDSEGY:
                        {
                            //sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
                            sql.append("CASE WHEN T6231.F21 = 'F' THEN (T6230.F09 >= 9 AND T6230.F09 <= 12) ELSE (T6231.F22 >= 270 AND T6231.F22 <= 360) END ");
                            break;
                        }
                        case SEGYYS:
                        {
                            //sql.append(" T6230.F09 > 12 ");
                            sql.append("CASE WHEN T6231.F21 = 'F' THEN T6230.F09 > 12 ELSE T6231.F22 > 360 END ");
                            break;
                        }
                        default:
                            break;
                    }
                }
                sql.append(")");
                
            }
        }
        sql.append(" GROUP BY T6230.F01 ");
        int order = query.getOrder();
        if (order == 0 || order == 3)
        {
            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        }
        else if (order == 11)
        {
            sql.append(" ORDER BY T6230.F06 DESC");
        }
        else if (order == 12)
        {
            sql.append(" ORDER BY T6230.F06 ASC");
        }
        else if (order == 41)
        {
            sql.append(" ORDER BY F27 DESC");
        }
        else if (order == 42)
        {
            sql.append(" ORDER BY F27 ASC");
        }
        else if (order == 51)
        {
            sql.append(" ORDER BY F28 DESC");
        }
        else if (order == 52)
        {
            sql.append(" ORDER BY F28 ASC");
        }
        else if (order == 61)
        {
            sql.append(" ORDER BY F29 DESC");
        }
        else if (order == 62)
        {
            sql.append(" ORDER BY F29 ASC");
        }
        else if (order == 71)
        {
            sql.append(" ORDER BY F30 DESC");
        }
        else if (order == 72)
        {
            sql.append(" ORDER BY F30 ASC");
        }
        //        }
        //        else
        //        {
        //            sql.append("  ORDER BY T6230.F20, T6230.F22 DESC");
        //        }
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
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
                        record.F21 = T6230_F10.parse(resultSet.getString("F21"));
                        record.F22 = T6230_F12.parse(resultSet.getString("F22"));
                        record.jgjc = resultSet.getString("F31");
                        record.jgqc = resultSet.getString("F32");
                        record.F28 = T6230_F28.parse(resultSet.getString(29));
                        record.F29 = T6231_F27.parse(resultSet.getString(30));
                        record.F30 = resultSet.getBigDecimal(31);
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
    public PagingResult<Bdlb> searchAll(QyBidQuery query, Paging paging)
        throws Throwable
    {
        return search(query, T6230_F27.F, null, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchNotTJB(QyBidQuery query, Paging paging)
        throws Throwable
    {
        return search(query, T6231_F29.F, null, paging);
    }
    
    @Override
    public Bdlb[] searchblx(QyBidQuery query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        return searchblx(query, T6230_F27.F, t6110_F06, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchQy(QyBidQuery query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        return search(query, T6230_F27.F, t6110_F06, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchQyblx(QyBidTypeQuery query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        return search(query, T6230_F27.F, t6110_F06, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchXXZQ(QyBidQuery query, Paging paging)
        throws Throwable
    {
        return search(query, T6230_F27.S, null, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchXSB(QyBidQuery query, Paging paging)
        throws Throwable
    {
        return search(query, T6230_F28.S, null, paging);
    }
    
    @Override
    public PagingResult<Bdlb> searchTJB(QyBidQuery query, Paging paging)
        throws Throwable
    {
        return search(query, T6231_F29.S, null, paging);
    }
    
    @Override
    public boolean haveTJB()
        throws Throwable
    {
        boolean haveTJB = false;
        PagingResult<Bdlb> bdlb = search(null, T6231_F29.S, null, null);
        if (bdlb != null && bdlb.getSize() != 0)
        {
            haveTJB = true;
        }
        return haveTJB;
    }
    
    private PagingResult<Bdlb> search(QyBidQuery query, T6231_F29 t6231_F29, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN ( T6230.F05- T6230.F07) ELSE T6230.F05 END AS F27, CASE WHEN T6231.F21='S' THEN (T6231.F22/DAYOFMONTH(last_day(T6230.F24))) ELSE T6230.F09 END AS F28, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 0 ELSE T6230.F07 END AS F29 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 100 ELSE case WHEN T6230.F20='YFB' THEN -1 ELSE((T6230.F05-T6230.F07)/T6230.F05)*100 END END AS F30, ( SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F31, T6230.F28 AS F32, T6231.F29 AS F33, T6231.F30 AS F34, T6231.F27 AS F35, T6231.F28 AS F36 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
        
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) ");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        if (t6231_F29 != null)
        {
            sql.append(" AND T6231.F29 = ? ");
            parameters.add(t6231_F29);
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
                        sql.append(" AND T6230.F06<?");
                        parameters.add(0.1);
                        break;
                    case 2:
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.1);
                        parameters.add(0.15);
                        break;
                    case 3:
                        sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                        parameters.add(0.15);
                        parameters.add(0.2);
                        break;
                    case 4:
                        sql.append(" AND T6230.F06>?");
                        parameters.add(0.2);
                        break;
                    case 5:
                        sql.append(" AND T6230.F04<?");
                        parameters.add(0.2);
                        break;
                    default:
                        break;
                }
            }
            
            int getProgress = query.getJd();
            if (getProgress > 0)
            {
                switch (getProgress)
                {
                    case 1:
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                        parameters.add(50);
                        break;
                    case 2:
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                        parameters.add(50);
                        parameters.add(80);
                        break;
                    case 3:
                        sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                        parameters.add(80);
                        break;
                    default:
                        break;
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
            sql.append(" GROUP BY T6230.F01 ");
            int order = query.getOrder();
            if (order == 0 || order == 3)
            {
                sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
            }
            else if (order == 11)
            {
                sql.append(" ORDER BY T6230.F06 DESC");
            }
            else if (order == 12)
            {
                sql.append(" ORDER BY T6230.F06 ASC");
            }
            else if (order == 41)
            {
                sql.append(" ORDER BY F27 DESC");
            }
            else if (order == 42)
            {
                sql.append(" ORDER BY F27 ASC");
            }
            else if (order == 51)
            {
                sql.append(" ORDER BY F28 DESC");
            }
            else if (order == 52)
            {
                sql.append(" ORDER BY F28 ASC");
            }
            else if (order == 61)
            {
                sql.append(" ORDER BY F29 DESC");
            }
            else if (order == 62)
            {
                sql.append(" ORDER BY F29 ASC");
            }
            else if (order == 71)
            {
                sql.append(" ORDER BY F30 DESC");
            }
            else if (order == 72)
            {
                sql.append(" ORDER BY F30 ASC");
            }
        }
        else
        {
            sql.append(" ORDER BY T6231.F30 DESC");
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.jxfs = resultSet.getInt(23);
                        record.F22 = T6230_F12.parse(resultSet.getString(24));
                        record.F23 = T6110_F06.parse(resultSet.getString(25));
                        record.image = resultSet.getString(26);
                        record.jgqc = resultSet.getString(31);
                        record.F28 = T6230_F28.parse(resultSet.getString(32));
                        record.F33 = T6231_F29.parse(resultSet.getString(33));
                        record.F29 = T6231_F27.parse(resultSet.getString(35));
                        record.F30 = resultSet.getBigDecimal(36);
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
    
    private PagingResult<Bdlb> search(QyBidQuery query, T6230_F28 t6230_F28, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN ( T6230.F05- T6230.F07) ELSE T6230.F05 END AS F27, CASE WHEN T6231.F21='S' THEN (T6231.F22/DAYOFMONTH(last_day(T6230.F24))) ELSE T6230.F09 END AS F28, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 0 ELSE T6230.F07 END AS F29 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 100 ELSE case WHEN T6230.F20='YFB' THEN -1 ELSE((T6230.F05-T6230.F07)/T6230.F05)*100 END END AS F30, ( SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F31, T6230.F28 AS F32 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
        
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) AND T6230.F29 = 'S'");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        if (t6230_F28 != null)
        {
            sql.append(" AND T6230.F28 = ? ");
            parameters.add(t6230_F28);
        }
        if (t6110_F06 != null)
        {
            sql.append(" AND T6110.F06 = ?");
            parameters.add(t6110_F06);
        }
        //        if (query != null)
        //        {
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        sql.append(" GROUP BY T6230.F01 ");
        int order = query.getOrder();
        if (order == 0 || order == 3)
        {
            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        }
        else if (order == 11)
        {
            sql.append(" ORDER BY T6230.F06 DESC");
        }
        else if (order == 12)
        {
            sql.append(" ORDER BY T6230.F06 ASC");
        }
        else if (order == 41)
        {
            sql.append(" ORDER BY F27 DESC");
        }
        else if (order == 42)
        {
            sql.append(" ORDER BY F27 ASC");
        }
        else if (order == 51)
        {
            sql.append(" ORDER BY F28 DESC");
        }
        else if (order == 52)
        {
            sql.append(" ORDER BY F28 ASC");
        }
        else if (order == 61)
        {
            sql.append(" ORDER BY F29 DESC");
        }
        else if (order == 62)
        {
            sql.append(" ORDER BY F29 ASC");
        }
        else if (order == 71)
        {
            sql.append(" ORDER BY F30 DESC");
        }
        else if (order == 72)
        {
            sql.append(" ORDER BY F30 ASC");
        }
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.jxfs = resultSet.getInt(23);
                        record.F22 = T6230_F12.parse(resultSet.getString(24));
                        record.F23 = T6110_F06.parse(resultSet.getString(25));
                        record.image = resultSet.getString(26);
                        record.jgqc = resultSet.getString(31);
                        record.F28 = T6230_F28.parse(resultSet.getString(32));
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
    
    private PagingResult<Bdlb> search(QyBidQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' or T6230.F20 = 'YZR' THEN ( T6230.F05- T6230.F07) ELSE T6230.F05 END AS F27, CASE WHEN T6231.F21='S' THEN (T6231.F22/DAYOFMONTH(last_day(T6230.F24))) ELSE T6230.F09 END AS F28, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' or T6230.F20 = 'YZR' THEN 0 ELSE T6230.F07 END AS F29 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' or T6230.F20 = 'YZR' THEN 100 ELSE case WHEN T6230.F20='YFB' THEN -1 ELSE((T6230.F05-T6230.F07)/T6230.F05)*100 END END AS F30, ( SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F31, T6230.F28 AS F32,T6231.F27 AS F33,T6231.F28 AS F34,T6231.F29 AS F35,T6231.F30 AS F36, ");
        sql.append("(CASE WHEN (T6231.F29 = 'S' AND T6230.F20 IN ('YFB', 'TBZ')) THEN 1 WHEN ( T6230.F20 IN ('YFB') ) THEN 2 WHEN (T6230.F28 = 'S' AND T6230.F20 IN ('YFB', 'TBZ') AND T6231.F29 = 'F') THEN 3 WHEN (T6231.F27 = 'S' AND T6230.F20 IN ('YFB', 'TBZ') AND T6231.F29 = 'F') THEN 4 ELSE 5 END) AS ORD, T6231.F33 AS F38 ");
        sql.append(" FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S' ");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?,?) ");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6230_F20.YZR);
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
        try (Connection connection = getConnection())
        {
            if (query != null)
            {
                if (query.getProductId() > 0)
                {
                    sql.append(" AND T6230.F32 = ?");
                    parameters.add(query.getProductId());
                }
                if (query.bidType() > 0)
                {
                    sql.append(" AND T6230.F04 = ?");
                    parameters.add(query.bidType());
                }
                int rate = query.getRate();
                if (rate > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F01 = ?"))
                    {
                        pstmt.setInt(1, rate);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
                            {
                                if (rs.getInt(2) <= 0)
                                {
                                    sql.append(" AND T6230.F06<?");
                                    parameters.add(new BigDecimal(rs.getInt(3)).divide(new BigDecimal(100)));
                                }
                                else if (rs.getInt(3) <= 0)
                                {
                                    sql.append(" AND T6230.F06>=?");
                                    parameters.add(new BigDecimal(rs.getInt(2)).divide(new BigDecimal(100)));
                                }
                                else
                                {
                                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                                    parameters.add(new BigDecimal(rs.getInt(2)).divide(new BigDecimal(100)));
                                    parameters.add(new BigDecimal(rs.getInt(3)).divide(new BigDecimal(100)));
                                }
                            }
                        }
                    }
                }
                
                int getProgress = query.getJd();
                if (getProgress > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F01 = ?"))
                    {
                        pstmt.setInt(1, getProgress);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
                            {
                                if (rs.getInt(2) <= 0)
                                {
                                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<? AND T6230.F20 NOT IN ('HKZ','YJQ','YDF') ");
                                    parameters.add(new BigDecimal(rs.getInt(3)));
                                }
                                else if (rs.getInt(3) <= 0)
                                {
                                    sql.append(" AND ((T6230.F05-T6230.F07)/T6230.F05*100>=? OR (T6230.F20 IN ('HKZ','YJQ','YDF'))) ");
                                    parameters.add(new BigDecimal(rs.getInt(2)));
                                }
                                else
                                {
                                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=? AND T6230.F20 NOT IN ('HKZ','YJQ','YDF') ");
                                    parameters.add(new BigDecimal(rs.getInt(2)));
                                    parameters.add(new BigDecimal(rs.getInt(3)));
                                }
                            }
                        }
                    }
                }
                T6230_F20 creditStatus = query.getStatus();
                if (creditStatus != null)
                {
                    if (creditStatus == T6230_F20.YJQ)
                    {
                        sql.append(" AND (T6230.F20 = ? OR T6230.F20=? OR T6230.F20=?)");
                        parameters.add(creditStatus);
                        parameters.add("YDF");
                        parameters.add("YZR");
                    }
                    else
                    {
                        sql.append(" AND T6230.F20 = ?");
                        parameters.add(creditStatus);
                    }
                }
                sql.append(" GROUP BY T6230.F01 ");
                int order = query.getOrder();
                if (order == 0 || order == 3)
                {
                    sql.append(" ORDER BY ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 11)
                {
                    sql.append(" ORDER BY T6230.F06 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 12)
                {
                    sql.append(" ORDER BY T6230.F06 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 41)
                {
                    sql.append(" ORDER BY F27 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 42)
                {
                    sql.append(" ORDER BY F27 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 51)
                {
                    sql.append(" ORDER BY F28 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 52)
                {
                    sql.append(" ORDER BY F28 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 61)
                {
                    sql.append(" ORDER BY F29 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 62)
                {
                    sql.append(" ORDER BY F29 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 71)
                {
                    sql.append(" ORDER BY F30 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                else if (order == 72)
                {
                    sql.append(" ORDER BY F30 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
                }
                
            }
            else
            {
                sql.append(" ORDER BY ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
            }
            
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.jxfs = resultSet.getInt(23);
                        record.F22 = T6230_F12.parse(resultSet.getString(24));
                        record.F23 = T6110_F06.parse(resultSet.getString(25));
                        record.image = resultSet.getString(26);
                        record.jgqc = resultSet.getString(31);
                        record.F28 = T6230_F28.parse(resultSet.getString(32));
                        record.F29 = T6231_F27.parse(resultSet.getString(33));
                        record.F30 = resultSet.getBigDecimal(34);
                        record.F31 = T6231_F33.parse(resultSet.getString(38));
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
    
    private PagingResult<Bdlb> search(QyBidTypeQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
        
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
        //        if (query != null)
        //        {
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.jxfs = resultSet.getInt(23);
                        record.F22 = T6230_F12.parse(resultSet.getString(24));
                        record.F23 = T6110_F06.parse(resultSet.getString(25));
                        record.image = resultSet.getString(26);
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
    
    private Bdlb[] searchblx(QyBidQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        ArrayList<Bdlb> list = null;
        T6211[] t6211s = getBidType();
        int T6211_F01 = 0;
        if (t6211s != null && t6211s.length > 0)
        {
            Bdlb bdlb = null;
            for (T6211 type : t6211s)
            {
                T6211_F01 = type.F01;
                bdlb = searchblxs(query, t6230_F27, T6211_F01, t6110_F06, paging);
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                if (bdlb != null)
                {
                    list.add(bdlb);
                }
            }
        }
        else
        {
            throw new LogicalException("标类型不存在！");
        }
        
        return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
    }
    
    private Bdlb searchblxs(QyBidQuery query, T6230_F27 t6230_F27, int typeid, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) ");
        sql.append(" AND T6230.F27 = ? ");
        sql.append(" AND T6230.F04 = ? ");
        if (t6110_F06 != null)
        {
            sql.append(" AND T6110.F06 = ?");
        }
        sql.append(" ORDER BY T6230.F20,T6230.F22 DESC LIMIT 1");
        Bdlb record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setString(1, T6230_F20.TBZ.name());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YFB.name());
                pstmt.setString(4, T6230_F20.DFK.name());
                pstmt.setString(5, T6230_F20.YJQ.name());
                pstmt.setString(6, T6230_F20.YDF.name());
                pstmt.setString(7, t6230_F27.name());
                pstmt.setInt(8, typeid);
                if (t6110_F06 != null)
                {
                    pstmt.setString(9, t6110_F06.name());
                }
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.jxfs = resultSet.getInt(23);
                        record.F22 = T6230_F12.parse(resultSet.getString(24));
                        record.F23 = T6110_F06.parse(resultSet.getString(25));
                        record.image = resultSet.getString(26);
                    }
                }
            }
            
        }
        return record;
    }
    
    @Override
    public T6250[] getRecord(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6250> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?  ORDER BY F06 DESC"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6250 record = new T6250();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6250_F07.parse(resultSet.getString(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6250[list.size()]));
        }
    }
    
    @Override
    public PagingResult<T6250> getRecords(int id, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01, F02, F03, F04, F05, F06, F07,F11,F09 FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?  ORDER BY F06 DESC,F04 DESC");
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(id);
        parameters.add(T6250_F07.F.name());
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6250>()
            {
                @Override
                public T6250[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6250> list = null;
                    while (resultSet.next())
                    {
                        T6250 record = new T6250();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6250_F07.parse(resultSet.getString(7));
                        record.F11 = T6250_F11.parse(resultSet.getString(8));
                        record.F09 = T6250_F09.parse(resultSet.getString(9));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6250[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public Hkjllb[] getHk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Hkjllb> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07), F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ),F06,F05 FROM S62.T6252 WHERE T6252.F02 = ? GROUP BY T6252.F08,F05"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        record.whAmount = getWhAmount(connection, id, resultSet.getInt(6), resultSet.getInt(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
        }
    }
    
    @Override
    public PagingResult<Hkjllb> getHks(int id, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT SUM(F07), F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ),F06,F05,F02 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 IN (?,?,?,?) AND T6252.F09 NOT IN (?,?) GROUP BY T6252.F08,F05");
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(id);
            parameters.add(FeeCode.TZ_BJ);
            parameters.add(FeeCode.TZ_LX);
            parameters.add(FeeCode.TZ_FX);
            parameters.add(FeeCode.TZ_WYJ);
            parameters.add(T6252_F09.TQH.name());
            parameters.add(T6252_F09.DF.name());
            return selectPaging(connection, new ArrayParser<Hkjllb>()
            {
                @Override
                public Hkjllb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Hkjllb> list = null;
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        record.whAmount =
                            getWhAmount(connection, resultSet.getInt(8), resultSet.getInt(6), resultSet.getInt(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PagingResult<Hkjllb> getMyHks(int id, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT SUM(F07), F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ),F06,F05,F02 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 IN (?,?,?,?,?) AND T6252.F09 != ? GROUP BY T6252.F08,F05");
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(id);
            parameters.add(FeeCode.TZ_BJ);
            parameters.add(FeeCode.TZ_LX);
            parameters.add(FeeCode.TZ_FX);
            parameters.add(FeeCode.TZ_WYJ);
            parameters.add(FeeCode.TZ_WYJ_SXF);
            parameters.add(T6252_F09.TQH.name());
            return selectPaging(connection, new ArrayParser<Hkjllb>()
            {
                @Override
                public Hkjllb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Hkjllb> list = null;
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        record.whAmount =
                            getWhAmount(connection, resultSet.getInt(8), resultSet.getInt(6), resultSet.getInt(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    private BigDecimal getWhAmount(Connection connection, int bidId, int number, int typeId)
        throws SQLException
    {
        BigDecimal whAmount = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F02=? AND F06=? AND F05=? AND F09='WH'"))
        {
            ps.setInt(1, bidId);
            ps.setInt(2, number);
            ps.setInt(3, typeId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    whAmount = rs.getBigDecimal(1);
                }
            }
        }
        return whAmount;
    }
    
    @Override
    public Hkjllb[] getHkNotGroup(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Hkjllb> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F07, F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ) FROM S62.T6252 WHERE T6252.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
        }
    }
    
    @Override
    public T6237 getFk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03 FROM S62.T6237 WHERE T6237.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    T6237 record = null;
                    if (resultSet.next())
                    {
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
    public Dbxx getDB(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6180.F04 AS F05,CASE T6110.F06 WHEN 'ZRR' THEN T6141.F02 ELSE T6161.F04 END AS F06, T6236.F05 AS F07, T6110.F10 AS F08 FROM S62.T6236 LEFT JOIN S61.T6180 ON T6236.F03 = T6180.F01 INNER JOIN S61.T6110 ON T6236.F03 = T6110.F01 LEFT JOIN S61.T6161 ON T6161.F01 = T6110.F01 LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 WHERE T6236.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Dbxx record = null;
                    if (resultSet.next())
                    {
                        record = new Dbxx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6236_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getString(8);
                    }
                    return record;
                }
            }
        }
    }
    
    @Override
    public Bdylx getDylb(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F04 FROM S62.T6235 WHERE T6235.F05 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Bdylx record = new Bdylx();
                        record.F01 = resultSet.getInt(1);
                        record.F04 = resultSet.getString(2);
                        return record;
                    }
                }
            }
            return null;
        }
    }
    
    @Override
    public Bdysx[] getDysx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Bdysx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03, F04 FROM S62.T6235 WHERE T6235.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Bdysx record = new Bdysx();
                        record.F03 = resultSet.getInt(1);
                        record.F04 = resultSet.getString(2);
                        record.dxsxName = getDbsx(record.F03);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Bdysx[list.size()]));
        }
    }
    
    /**
     * 获取担保类型名称
     *
     * @param id
     * @return
     */
    /* private String getDblx(int id)
             throws Throwable
     {
         try (Connection connection = getConnection())
         {
             try (PreparedStatement pstmt =
                          connection.prepareStatement("SELECT F02 FROM S62.T6213 WHERE T6213.F01 = ? LIMIT 1"))
             {
                 pstmt.setInt(1, id);
                 try (ResultSet resultSet = pstmt.executeQuery())
                 {
                     if (resultSet.next())
                     {
                         return resultSet.getString(1);
                     }
                 }
             }
             return null;
         }
     }*/
    
    /**
     * 获取担保属性名称
     *
     * @param id
     * @return
     */
    private String getDbsx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S62.T6214 WHERE T6214.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
            return null;
        }
    }
    
    @Override
    public T6233[] getFjfgk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6233> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6233 WHERE T6233.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
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
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6233[list.size()]));
        }
    }
    
    @Override
    public T6232[] getFjgk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6232> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
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
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6232[list.size()]));
        }
    }
    
    @Override
    public T6251[] getZqxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6251> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6251 WHERE T6251.F03 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
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
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6251[list.size()]));
        }
    }
    
    @Override
    public Zqzrxx[] getZqzrxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Zqzrxx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6262.F01 AS F01, T6262.F02 AS F02, T6262.F03 AS F03, T6262.F04 AS F04, T6262.F05 AS F05, T6262.F06 AS F06, T6262.F07 AS F07, T6262.F08 AS F08, T6262.F09 AS F09, T6260.F03 AS F10, T6260.F04 AS F11, T6251.F04 AS F12 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01  WHERE T6251.F03 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
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
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Zqzrxx[list.size()]));
        }
    }
    
    @Override
    public Tztjxx getStatistics()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F05),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? ";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.F.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
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
    public Tztjxx getStatisticsQy()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F05 - T6230.F07),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?,?) AND T6230.F27 = ? ";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F20.YZR.name());
                ps.setString(5, T6230_F27.F.name());
                //ps.setString(5, T6110_F06.FZRR.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
        }
        // fzrr非自然人,企业标为用户赚取的
        statistics.userEarnMoney = getEarnMoneyQyForFZRR();
        return statistics;
    }
    
    @Override
    public Tztjxx getStatisticsGr()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F05 - T6230.F07),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? AND T6110.F06 = ?";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.F.name());
                ps.setString(5, T6110_F06.ZRR.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
        }
        // zrr自然人,个人标为用户赚取的
        statistics.userEarnMoney = getEarnMoneyGr();
        return statistics;
    }
    
    @Override
    public Tztjxx getStatisticsXxzq()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F26),0),COUNT(*) FROM S62.T6230 WHERE  T6230.F20 IN (?,?,?) AND T6230.F27 = ?";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.S.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
        }
        
        statistics.userEarnMoney = getEarnMoneyXxzq();
        return statistics;
    }
    
    private BigDecimal getEarnMoneyQy()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
                    + " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND  T6230.F27 = ? "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setInt(3, FeeCode.TZ_FX);
                ps.setString(4, T6252_F09.YH.name());
                ps.setString(5, T6230_F27.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    private BigDecimal getEarnMoneyQyForFZRR()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
                    + " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND  T6230.F27 = ?  "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setInt(3, FeeCode.TZ_FX);
                ps.setString(4, T6252_F09.YH.name());
                ps.setString(5, T6230_F27.F.name());
                //ps.setString(6, T6110_F06.FZRR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    private BigDecimal getEarnMoneyGr()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
                    + " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND  T6110.F06 = ? AND  T6230.F27 = ? "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setInt(3, FeeCode.TZ_FX);
                ps.setString(4, T6252_F09.YH.name());
                ps.setString(5, T6110_F06.ZRR.name());
                ps.setString(6, T6230_F27.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    private BigDecimal getEarnMoneyXxzq()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230 WHERE"
                    + " T6252.F02 = T6230.F01  AND T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND  T6230.F27 = ? "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setInt(3, FeeCode.TZ_FX);
                ps.setString(4, T6252_F09.YH.name());
                ps.setString(5, T6230_F27.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
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
    private String getDjmc(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
            return "";
        }
    }
    
    @Override
    public T6211[] getBidType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?"))
            {
                pstmt.setString(1, T6211_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    @Override
    public T5124[] getLevel()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T5124> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S51.T5124 WHERE T5124.F05 = ?"))
            {
                pstmt.setString(1, T5124_F05.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T5124 record = new T5124();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T5124[list.size()]));
        }
    }
    
    @Override
    public Mbxx getMbxx(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Mbxx record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0), F08 FROM S62.T6252 WHERE F02=?  AND F05 IN (?,?) AND F09 = ?  LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setInt(3, FeeCode.TZ_BJ);
                pstmt.setString(4, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
    public Blob getAttachment(int id)
        throws Throwable
    {
        String sql = "SELECT F06 FROM S62.T6233 WHERE F01=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
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
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18,T6230.F10 AS F19 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S51.T5124 ON T6230.F23 = T5124.F01");
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
                        record.F21 = T6230_F10.parse(resultSet.getString(19));
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
    public Bdlb getNewBid()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Bdlb record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15, T6230.F11 AS F16, T6230.F13 AS F17, T6230.F14 AS F18,T6230.F10 AS F19,T6231.F21 AS F20,T6231.F22 AS F21, T6230.F28 AS F22, T6231.F27 AS F23, T6231.F28 AS F24 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01 WHERE T6230.F20 IN (?,?) ORDER BY T6230.F20 ,T6230.F22 DESC LIMIT 1"))
            {
                pstmt.setString(1, T6230_F20.YFB.name());
                pstmt.setString(2, T6230_F20.TBZ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.F21 = T6230_F10.parse(resultSet.getString(19));
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F28 = T6230_F28.parse(resultSet.getString(22));
                        record.F29 = T6231_F27.parse(resultSet.getString(23));
                        record.F30 = resultSet.getBigDecimal(24);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public int getTbCount(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(F01) FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public Sytjsj getSytj()
        throws Throwable
    {
        Sytjsj sytjsj = new Sytjsj();
        return sytjsj;
    }
    
    @Override
    public T6240 getXXZQ(int bidId)
        throws Throwable
    {
        if (bidId <= 0)
            return null;
        T6240 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
    public IndexStatic getIndexStatic()
        throws Throwable
    {
        IndexStatic indexStatic = new IndexStatic();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=?"))
            {
                ps.setString(1, T6250_F07.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.ljcj = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F05) FROM S62.T6230 WHERE F20 IN(?,?,?)"))
            {
                ps.setString(1, T6230_F20.HKZ.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.YDF.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.jkze = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND (F05=? OR F05=?)"))
            {
                ps.setString(1, T6252_F09.YH.name());
                ps.setInt(2, FeeCode.TZ_BJ);
                ps.setInt(3, FeeCode.TZ_LX);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.yhbx = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND (F05=? OR F05=?)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, FeeCode.TZ_BJ);
                ps.setInt(3, FeeCode.TZ_LX);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.dhbx = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F09=? AND DATEDIFF(?,F08)>0"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setDate(2, getCurrentDate(connection));
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.yqhk = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=? AND DATE(F06)=DATE_SUB(CURDATE(),INTERVAL 1 DAY)"))
            {
                ps.setString(1, T6250_F07.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.zrcj = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07=? AND YEAR(F06) = YEAR(CURDATE())"))
            {
                ps.setString(1, T6250_F07.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.bncj = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6250.F04) FROM S62.T6250 JOIN S62.T6230 ON T6250.F02=T6230.F01 WHERE T6250.F07 = ? AND T6250.F08 = ? AND T6230.F20 IN(?,?,?,?)"))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                ps.setString(3, T6230_F20.YJQ.name());
                ps.setString(4, T6230_F20.HKZ.name());
                ps.setString(5, T6230_F20.YDF.name());
                ps.setString(6, T6230_F20.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.rzzje = rs.getBigDecimal(1);
                    }
                }
            }
            StringBuffer sb =
                new StringBuffer(
                    "SELECT (SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') GROUP BY T6252.F11,T6252.F06) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005)");
            sb.append("-(SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=1202 AND T6101.F03='WLZH')");
            sb.append("+(SELECT IFNULL(SUM(ZQZR.zqzryk),0) FROM (SELECT IFNULL(SUM(T6262.F08), 0) zqzryk FROM S62.T6262 GROUP BY T6262.F03 UNION SELECT IFNULL(SUM(T6262.F09), 0) zqzryk FROM S62.T6262, S62.T6260, S62.T6251 WHERE T6251.F01 = T6260.F02 AND T6260.F01 = T6262.F02 GROUP BY T6251.F04) ZQZR)");
            try (PreparedStatement ps = connection.prepareStatement(sb.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.yhzsy = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(DISTINCT F03) FROM S62.T6250 WHERE F07 = ? AND F08 = ?"))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        indexStatic.yhjys = rs.getBigDecimal(1);
                    }
                }
            }
            
        }
        return indexStatic;
    }
    
    @Override
    public T6240 getT6240(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6240 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?)");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        //        if (query != null)
        //        {
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
        //}
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
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
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
    public T6212[] getT6212(int loanId, boolean b)
        throws Throwable
    {
        String sql = "";
        if (b)
        {
            sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6233 WHERE T6233.F02 = ?)";
        }
        else
        {
            sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6232 WHERE T6232.F02 = ?)";
        }
        try (Connection connection = getConnection())
        {
            ArrayList<T6212> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6212 record = new T6212();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6212[list.size()]));
        }
    }
    
    @Override
    public T5127_F03 getTzdj(int userID)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return getDj(T5127_F02.TZ, resultSet.getBigDecimal(1));
                    }
                }
            }
        }
        return null;
    }
    
    // 根据金额,类型得到等级
    private T5127_F03 getDj(T5127_F02 type, BigDecimal money)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F04 > ? AND T5127.F05 <= ? AND T5127.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, type.name());
                pstmt.setBigDecimal(2, money);
                pstmt.setBigDecimal(3, money);
                pstmt.setString(4, T5127_F06.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T5127_F03.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean isXgwj(int loanId)
        throws Throwable
    {
        boolean b = false;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6232 WHERE T6232.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        b = true;
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6233 WHERE T6233.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        b = true;
                    }
                }
            }
        }
        return b;
    }
    
    @Override
    public PagingResult<Bdlb> searchAllBid(BidAllQuery query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        return searchAll(query, T6230_F27.F, t6110_F06, paging);
    }
    
    private PagingResult<Bdlb> searchAll(BidAllQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
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
        //        if (query != null)
        //        {
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
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
    public PagingResult<Bdlb> searchXXZQ(BidAllQuery query, Paging paging)
        throws Throwable
    {
        return searchAll(query, T6230_F27.S, null, paging);
    }
    
    @Override
    public int lcTotle()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6115 WHERE T6115.F03 > 0"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public BigDecimal yhzqTotle()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F09 FROM S70.T7010 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return new BigDecimal(0);
        }
    }
    
    @Override
    public InvestorTotal getInvestorTotal()
        throws Throwable
    {
        InvestorTotal total = new InvestorTotal();
        total.investors = 0L;
        total.investAmount = new BigDecimal(0);
        total.investProfits = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM (SELECT 1 FROM S62.T6250 WHERE T6250.F07 = 'F' GROUP BY T6250.F02, T6250.F03) AS A"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        total.investors = resultSet.getLong(1);
                    }
                }
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        total.investAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F09 FROM S70.T7010 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        total.investProfits = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return total;
    }
    
    @Override
    public BidRecord[] getBidRecords(int count)
        throws Throwable
    {
        if (count <= 0)
        {
            count = 20;
        }
        ArrayList<BidRecord> records = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6250.F04, T6250.F06, T6110.F02, (SELECT T5019.F06 FROM S50.T5019 WHERE LEFT (T6141.F06, 2) = T5019.F02 LIMIT 1) FROM S62.T6250 INNER JOIN S61.T6110 ON T6250.F03 = T6110.F01 INNER JOIN S61.T6141 ON T6250.F03 = T6141.F01 WHERE T6250.F07 = 'F' ORDER BY T6250.F01 DESC LIMIT "
                    + count))
            {
                try (ResultSet rs = pstmt.executeQuery())
                {
                    BidRecord record;
                    while (rs.next())
                    {
                        if (records == null)
                        {
                            records = new ArrayList<>();
                        }
                        record = new BidRecord();
                        record.bidAmount = rs.getBigDecimal(1);
                        record.bidTime = rs.getTimestamp(2);
                        record.accountName = rs.getString(3);
                        record.province = rs.getString(4);
                        records.add(record);
                    }
                }
            }
        }
        return (records == null || records.size() == 0) ? null : records.toArray(new BidRecord[records.size()]);
    }
    
    @Override
    public T7051[] getUserBidRankForMonth(int count)
        throws Throwable
    {
        if (count <= 0)
        {
            count = 5;
        }
        String latestMonth = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S70.T7051 WHERE F03 > 0 ORDER BY F05 DESC LIMIT 1"))
            {
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        latestMonth = rs.getString(1);
                    }
                }
            }
            
            ArrayList<T7051> records = null;
            if (!StringHelper.isEmpty(latestMonth))
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S70.T7051 WHERE F03 > 0 AND F04 = ? ORDER BY F03 DESC LIMIT ?"))
                {
                    pstmt.setString(1, latestMonth);
                    pstmt.setInt(2, count);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        T7051 record;
                        while (rs.next())
                        {
                            if (records == null)
                            {
                                records = new ArrayList<>();
                            }
                            record = new T7051();
                            record.F01 = rs.getInt(1);
                            record.F02 = rs.getString(2);
                            record.F03 = rs.getBigDecimal(3);
                            record.F04 = rs.getString(4);
                            record.F05 = rs.getTimestamp(5);
                            records.add(record);
                        }
                    }
                }
            }
            return (records == null || records.size() == 0) ? null : records.toArray(new T7051[records.size()]);
        }
    }
    
    @Override
    public T7050[] getUserBidRankForWeek(int count)
        throws Throwable
    {
        if (count <= 0)
        {
            count = 5;
        }
        
        String latestWeek = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S70.T7050 WHERE F03 > 0 ORDER BY F04 DESC LIMIT 1"))
            {
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        latestWeek = rs.getString(1);
                    }
                }
            }
            
            ArrayList<T7050> records = null;
            if (!StringHelper.isEmpty(latestWeek))
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S70.T7050 WHERE F03 > 0 AND F04 = ? ORDER BY F03 DESC LIMIT ?"))
                {
                    pstmt.setString(1, latestWeek);
                    pstmt.setInt(2, count);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        T7050 record;
                        while (rs.next())
                        {
                            if (records == null)
                            {
                                records = new ArrayList<>();
                            }
                            record = new T7050();
                            record.F01 = rs.getInt(1);
                            record.F02 = rs.getString(2);
                            record.F03 = rs.getBigDecimal(3);
                            record.F04 = rs.getString(4);
                            record.F05 = rs.getTimestamp(5);
                            records.add(record);
                        }
                    }
                }
            }
            return (records == null || records.size() == 0) ? null : records.toArray(new T7050[records.size()]);
        }
    }
    
    @Override
    public PagingResult<Bdlb> searchQyExt(QyBidQueryExt query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?,?,?) ");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        sql.append(" AND T6230.F27 = ?");
        parameters.add(T6230_F27.F);
        if (t6110_F06 != null)
        {
            sql.append(" AND T6110.F06 = ?");
            parameters.add(t6110_F06);
        }
        //        if (query != null)
        //        {
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
        
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
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
    public BigDecimal getTotalAmount()
        throws Throwable
    {
        BigDecimal amount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(F03) FROM S70.T7039"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        amount = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    @Override
    public BigDecimal getUseAmount()
        throws Throwable
    {
        BigDecimal amount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(F04) FROM S70.T7039"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        amount = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    @Override
    public BigDecimal getBidMaxYearRate()
        throws Throwable
    {
        BigDecimal maxYearRate = new BigDecimal(0);
        try (Connection connection = getConnection("S62"))
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(T6230.F06) AS F06 FROM S62.T6230 "))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        maxYearRate = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return maxYearRate;
    }
    
    @Override
    public PagingResult<Bdlb> getBidDbType(BidTypeQuery query, T6230_F27 t6230_F27, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F12 AS F23 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
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
        //        if (query != null)
        //        {
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        
        T6230_F11 F11 = query.getDbTypeBid();
        if (F11 != null)
        {
            sql.append(" AND (T6230.F11 = ? OR (T6230.F13 = 'S' AND T6230.F11 ='S')) ");
            parameters.add(T6230_F11.S);
        }
        
        T6230_F13 F13 = query.getDyTypeBid();
        if (F13 != null)
        {
            sql.append(" AND (T6230.F13 = ? OR (T6230.F13 = 'S' AND T6230.F11 ='S')) ");
            parameters.add(T6230_F13.S);
        }
        
        // 获取查询条件
        Map<String, Object> paraMap = query.bidConditionQry();
        if (paraMap != null && paraMap.size() > 0)
        {
            // 获取标的标题查询条件
            String bidTitle = String.valueOf(paraMap.get("title"));
            if (!StringHelper.isEmpty(bidTitle))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add("%" + bidTitle + "%");
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
        
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.F22 = T6230_F12.parse(resultSet.getString(23));
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
    public BigDecimal getRzje()
        throws Throwable
    {
        BigDecimal rzje = new BigDecimal(0);
        try (Connection connection = getConnection("S62"))
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6230.F05) AS F06 FROM S62.T6230 WHERE F20 IN('HKZ','YJQ','YDF')"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        rzje = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return rzje;
    }
    
    @Override
    public BigDecimal total()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F05-F07) FROM S62.T6230 WHERE F20 IN ('TBZ','DFK','HKZ','YJQ','YDF') AND F22 > '2014-09-16'"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return null;
        }
    }
    
    @Override
    public PagingResult<Bdlb> searchQyAccount(QyBidQueryAccount query, T6110_F06 t6110_F06, Paging paging)
        throws Throwable
    {
        return searchAccount(query, T6230_F27.F, t6110_F06, paging);
    }
    
    private PagingResult<Bdlb> searchAccount(QyBidQueryAccount query, T6230_F27 t6230_F27, T6110_F06 t6110_F06,
        Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F12 AS F23 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S'");
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
        //        if (query != null)
        //        {
        int rate = query.getRate();
        if (rate > 0)
        {
            switch (rate)
            {
                case 1:
                    sql.append(" AND T6230.F06<?");
                    parameters.add(0.1);
                    break;
                case 2:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.1);
                    parameters.add(0.15);
                    break;
                case 3:
                    sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
                    parameters.add(0.15);
                    parameters.add(0.2);
                    break;
                case 4:
                    sql.append(" AND T6230.F06>?");
                    parameters.add(0.2);
                    break;
                case 5:
                    sql.append(" AND T6230.F04<?");
                    parameters.add(0.2);
                    break;
                default:
                    break;
            }
        }
        
        int getProgress = query.getJd();
        if (getProgress > 0)
        {
            switch (getProgress)
            {
                case 1:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<?");
                    parameters.add(50);
                    break;
                case 2:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=?");
                    parameters.add(50);
                    parameters.add(80);
                    break;
                case 3:
                    sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>?");
                    parameters.add(80);
                    break;
                default:
                    break;
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
        
        String way = query.getWay();
        if (!StringHelper.isEmpty(way))
        {
            switch (way)
            {
                case "x":
                    sql.append(" AND T6230.F05 >= 0 AND T6230.F05 <= 10000");
                    break;
                case "z":
                    sql.append(" AND T6230.F05 > 10000 AND T6230.F05 <= 30000");
                    break;
                case "v":
                    sql.append(" AND T6230.F05 > 30000");
                    break;
                default:
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
        //        }
        //        else
        //        {
        //            sql.append(" ORDER BY T6230.F20,T6230.F22 DESC");
        //        }
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
                        record.jgjc = resultSet.getString(19);
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(20));
                        record.F20 = resultSet.getInt(21);
                        record.F21 = T6230_F10.parse(resultSet.getString(22));
                        record.F22 = T6230_F12.parse(resultSet.getString(23));
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
    public PagingResult<Bdlb> searchAccount(BidQueryAccount query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?,?,?) AND T6110.F06 = ?");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6110_F06.ZRR);
        //        if (query != null)
        //        {
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
        
        String way = query.getWay();
        if (!StringHelper.isEmpty(way))
        {
            switch (way)
            {
                case "x":
                    sql.append(" AND T6230.F05 >= 0 AND T6230.F05 <= 10000");
                    break;
                case "z":
                    sql.append(" AND T6230.F05 > 10000 AND T6230.F05 <= 30000");
                    break;
                case "v":
                    sql.append(" AND T6230.F05 > 30000");
                    break;
                default:
                    break;
            }
        }
        //}
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
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
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
    public FrontT6250[] getBids(int limit)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<FrontT6250> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6250.F01, T6250.F02, T6250.F03, T6250.F04, T6250.F05, T6250.F06, T6250.F07,T6110.F02, T6230.F03 FROM S62.T6250 INNER JOIN S61.T6110 ON T6110.F01=T6250.F03 INNER JOIN S62.T6230 ON T6250.F02=T6230.F01 ORDER BY F06 DESC LIMIT ?"))
            {
                pstmt.setInt(1, limit);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        FrontT6250 record = new FrontT6250();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6250_F07.parse(resultSet.getString(7));
                        record.F09 = resultSet.getString(8);
                        record.F10 = resultSet.getString(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new FrontT6250[list.size()]));
        }
    }
    
    @Override
    public FrontReleaseBid[] getReleaseBids(int limit, String status)
        throws Throwable
    {
        String sql =
            "SELECT T6230.F01 AS F01,T6230.F03 AS F02,T6230.F02 AS F03,T6110.F02 AS F04,T6230.F22 AS F05,T6230.F05 AS F06,T6230.F07 AS F07 "
                + "FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 "
                + "LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01 INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 WHERE T6230.F27 = 'F' "
                + "AND T6230.F20=? ORDER BY  T6230.F22 DESC LIMIT ?";
        try (Connection connection = getConnection())
        {
            ArrayList<FrontReleaseBid> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, status);
                pstmt.setInt(2, limit);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        FrontReleaseBid record = new FrontReleaseBid();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new FrontReleaseBid[list.size()]));
        }
    }
    
    @Override
    public BigDecimal yhzsy()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 IN (?,?,?) AND T6252.F09 = ?"))
            {
                pstmt.setInt(1, FeeCode.TZ_FX);
                pstmt.setInt(2, FeeCode.TZ_WYJ);
                pstmt.setInt(3, FeeCode.TZ_LX);
                pstmt.setString(4, T6252_F09.YH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return new BigDecimal(0);
        }
    }
    
    @Override
    public BigDecimal getYqje(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND DATEDIFF(?,F08)>0 AND F09 = ? AND F05 IN (?,?,?,?)"))
            {
                pstmt.setInt(1, loanId);
                pstmt.setDate(2, getCurrentDate(connection));
                pstmt.setString(3, T6252_F09.WH.name());
                pstmt.setInt(4, FeeCode.TZ_BJ);
                pstmt.setInt(5, FeeCode.TZ_LX);
                pstmt.setInt(6, FeeCode.TZ_FX);
                pstmt.setInt(7, FeeCode.TZ_WYJ);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal LoanTotal()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F05-F07) FROM S62.T6230 WHERE F20 IN('HKZ','YHK','YDF')"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return new BigDecimal(0);
        }
    }
    
    @Override
    public BigDecimal backOnTime()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(IFNULL(F07,0)) FROM S62.T6252 WHERE DATE_FORMAT(F10,'%Y%m%d')<=DATE_FORMAT(F08,'%Y%m%d') AND F09='YH' AND F10 IS NOT NULL"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return new BigDecimal(0);
        }
    }
    
    @Override
    public String rendPaging(PagingResult<?> paging)
        throws Throwable
    {
        StringBuffer rtnPageStr = new StringBuffer();
        int currentPage = paging.getCurrentPage();
        rtnPageStr.append("<div class='paging'>总共");
        rtnPageStr.append("<span class='highlight2 ml5 mr5'>");
        rtnPageStr.append(paging.getItemCount());
        rtnPageStr.append("</span>条记录 &nbsp;");
        if (currentPage == 1 && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href=\"javascript:void(0);\" class='disabled prev'>&lt;</a>");
        }
        if (currentPage > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link prev'>&lt;</a>");
        }
        if (paging.getPageCount() > 1)
        {
            int total = 1;
            int max = 5;
            int index = paging.getPageCount() - currentPage;
            if (index > 2)
            {
                max = 3;
                index = 1;
            }
            else
            {
                index = index <= 0 ? (max - 1) : (max - index - 1);
            }
            int i;
            for (i = (currentPage - index); i <= paging.getPageCount() && total <= max; i++)
            {
                if (i <= 0)
                {
                    continue;
                }
                if (currentPage == i)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link cur'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                else
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                total++;
            }
            if (i <= paging.getPageCount())
            {
                int idx = paging.getPageCount() - 1;
                if (i <= idx)
                {
                    rtnPageStr.append("<span>...</span>");
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
                else if (i == idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
                idx++;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
            }
        }
        if (currentPage < paging.getPageCount())
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link next'>&gt;</a>");
        }
        if (currentPage == paging.getPageCount() && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class=' disabled'>&gt;</a>");
        }
        
        if (paging.getPageCount() > 1)
        {
            rtnPageStr.append("到<input type=\"text\"  id=\"goPage\" maxSize="
                + paging.getPageCount()
                + " class=\"page_input\" maxlength=\"7\">页<input type=\"button\"  class=\"btn_ok page-link cur\" value=\"确定\" onclick=\"pageSubmit(this);\" />");
        }
        
        rtnPageStr.append("</div>");
        return rtnPageStr.toString();
    }
    
    /**
     * 根据标的id,查询标的信息
     *
     * @param f01
     * @return 标的信息
     * @throws SQLException
     * @author luoyi
     * @date 2015-04-29
     */
    @Override
    public T6230 findT6230ByF01(int f01)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE";
            T6230 record = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, f01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6230();
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
                        record.F27 = EnumParser.parse(T6230_F27.class, resultSet.getString(27));
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 根据标的id,查询标的扩展信息
     *
     * @param f01
     * @return 标的信息
     * @throws SQLException
     * @author luoyi
     * @date 2015-04-29
     */
    @Override
    public T6231 findT6231ByF01(int f01)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            String sql = "SELECT F21, F22 FROM S62.T6231 WHERE F01 = ? LIMIT 1 FOR UPDATE";
            T6231 record = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, f01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6231();
                        record.F21 = T6231_F21.parse(resultSet.getString(1));
                        record.F22 = resultSet.getInt(2);
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 返回数据库当前时间
     * @param
     * @return CURRENT_DATE
     * @throws Throwable
     */
    @Override
    public Date getCurrentDate()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql = "SELECT CURRENT_DATE()";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getDate(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Timestamp getCurrentTimestamp()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return super.getCurrentTimestamp(connection);
        }
    }
    
    @Override
    public BigDecimal getTzMoney(int bid)
        throws SQLException
    {
        BigDecimal amount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?"))
            {
                ps.setInt(1, bid);
                ps.setString(2, T6250_F07.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        amount = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    @Override
    public T6233[] getFjfgks(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6233> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6233 WHERE T6233.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    FileStore fileStore = serviceResource.getResource(FileStore.class);
                    while (resultSet.next())
                    {
                        T6233 record = new T6233();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = fileStore.getURL(resultSet.getString(6));
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6233[list.size()]));
        }
    }
    
    @Override
    public T6232[] getFjgks(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6232> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    FileStore fileStore = serviceResource.getResource(FileStore.class);
                    while (resultSet.next())
                    {
                        T6232 record = new T6232();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = fileStore.getURL(record.F04);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6232[list.size()]));
        }
    }
    
    @Override
    public T6216[] getProducts()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6216> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15 FROM S62.T6216 WHERE F04 = ? "))
            {
                
                pstmt.setString(1, T6216_F04.QY.name());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        T6216 product = new T6216();
                        product.F01 = rs.getInt(1);
                        product.F02 = rs.getString(2);
                        product.F03 = rs.getString(3);
                        product.F04 = T6216_F04.valueOf(rs.getString(4));
                        product.F05 = rs.getBigDecimal(5);
                        product.F06 = rs.getBigDecimal(6);
                        
                        product.F07 = rs.getInt(7);
                        product.F08 = rs.getInt(8);
                        
                        product.F09 = rs.getBigDecimal(9);
                        product.F10 = rs.getBigDecimal(10);
                        product.F11 = rs.getString(11);
                        product.F12 = rs.getBigDecimal(12);
                        product.F13 = rs.getBigDecimal(13);
                        product.F14 = rs.getBigDecimal(14);
                        product.F15 = rs.getBigDecimal(15);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(product);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6216[list.size()]));
        }
    }
    
    /**
     * 是否能投新手标
     * @param loanId
     * @return
     * @throws Throwable
     */
    @Override
    public boolean isCanTXSB(int loanId)
        throws Throwable
    {
        
        final int accountId = serviceResource.getSession().getAccountId();
        // 标的详情
        T6230 t6230 = getT6230(loanId);
        if (T6230_F28.S.equals(t6230.xsb) && (getXsbCount(accountId) > 0 || getZqzrCount(accountId) > 0))
        {
            return false;
        }
        return true;
    }
    
    private T6230 getT6230(int loanId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            T6230 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F28 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6230();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.xsb = T6230_F28.parse(resultSet.getString(3));
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 已投新手标数量
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getXsbCount(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            int count = 0;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT count(*) FROM S62.T6250  WHERE T6250.F03 = ? "))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
    
    /**
     * 获取某用户的债权转让数量
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getZqzrCount(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            int count = 0;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT count(*) FROM S62.T6262  WHERE T6262.F03 = ? "))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
    
    @Override
    public T7051[] getUserBidRankForYear()
        throws Throwable
    {
        ArrayList<T7051> list = null;
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F02 AS account , SUM(F03) AS total FROM S70.T7051 WHERE F04 LIKE ? GROUP BY account ORDER BY total DESC LIMIT 5";
            try (PreparedStatement psts = connection.prepareStatement(sql))
            {
                Calendar ca = Calendar.getInstance();
                int year = ca.get(Calendar.YEAR);
                String yearStr = Integer.toString(year) + "%";
                psts.setString(1, yearStr);
                try (ResultSet resultSet = psts.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T7051 t7051 = new T7051();
                        t7051.F02 = resultSet.getString(1);
                        t7051.F03 = resultSet.getBigDecimal(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t7051);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T7051[list.size()]));
    }
    
    @Override
    public T7051[] getUserBidRankForYear(int count)
        throws Throwable
    {
        ArrayList<T7051> list = null;
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F02 AS account , SUM(F03) AS total FROM S70.T7051 WHERE F04 LIKE ? GROUP BY account ORDER BY total DESC LIMIT ?";
            try (PreparedStatement psts = connection.prepareStatement(sql))
            {
                psts.setInt(2, count);
                Calendar ca = Calendar.getInstance();
                int year = ca.get(Calendar.YEAR);
                String yearStr = Integer.toString(year) + "%";
                psts.setString(1, yearStr);
                try (ResultSet resultSet = psts.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T7051 t7051 = new T7051();
                        t7051.F02 = resultSet.getString(1);
                        t7051.F03 = resultSet.getBigDecimal(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t7051);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T7051[list.size()]));
    }
    
    @Override
    public BidRecordInfo[] getBidRecordList(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<BidRecordInfo> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t6250.F04, t6250.F06, t6141.F02 FROM S62.T6250 t6250, S61.T6141 t6141 WHERE t6250.F02 = ? AND t6250.F07 = ? AND t6250.F03 = t6141.F01 ORDER BY t6250.F06 DESC"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    while (resultSet.next())
                    {
                        BidRecordInfo record = new BidRecordInfo();
                        record.setAccountName(resultSet.getString(3) == null ? "" : resultSet.getString(3)
                            .substring(0, 1)
                            .concat("**"));
                        record.setBidAmount(resultSet.getBigDecimal(1));
                        record.setBidTime(dateSdf.format(resultSet.getTimestamp(2)));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new BidRecordInfo[list.size()]));
        }
    }
    
    @Override
    public BidRecordInfo[] getBidRecordList1(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<BidRecordInfo> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t6250.F04, t6250.F06, t6141.F02, t6110.F02 FROM S62.T6250 t6250, S61.T6141 t6141, S61.T6110 t6110 WHERE t6250.F02 = ? AND t6250.F07 = ? AND t6250.F03 = t6141.F01 AND t6110.F01 = t6141.F01 ORDER BY t6250.F06 DESC"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    while (resultSet.next())
                    {
                        BidRecordInfo record = new BidRecordInfo();
                        final String userTbr = resultSet.getString(4);
                        record.setAccountName(userTbr.substring(0, 2) + "******"
                            + userTbr.substring(userTbr.length() - 2, userTbr.length()));
                        record.setBidAmount(resultSet.getBigDecimal(1));
                        record.setBidTime(dateSdf.format(resultSet.getTimestamp(2)));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new BidRecordInfo[list.size()]));
        }
    }
    
    @Override
    public T6299 getFirst(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6299>()
            {
                @Override
                public T6299 parse(ResultSet rs)
                    throws SQLException
                {
                    T6299 filterFirst = null;
                    if (rs.next())
                    {
                        filterFirst = new T6299();
                        filterFirst.F01 = rs.getInt(1);
                        filterFirst.F03 = rs.getInt(3);
                        filterFirst.F04 = T6299_F04.parse(rs.getString(4));
                        filterFirst.F05 = rs.getTimestamp(5);
                    }
                    return filterFirst;
                }
            }, "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F02 = 0 AND F04 = ?", type.name());
        }
    }
    
    @Override
    public T6299 getLast(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6299>()
            {
                @Override
                public T6299 parse(ResultSet rs)
                    throws SQLException
                {
                    T6299 filterLast = null;
                    if (rs.next())
                    {
                        filterLast = new T6299();
                        filterLast.F01 = rs.getInt(1);
                        filterLast.F02 = rs.getInt(2);
                        filterLast.F04 = T6299_F04.parse(rs.getString(4));
                        filterLast.F05 = rs.getTimestamp(5);
                    }
                    return filterLast;
                }
            }, "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F03 = 0 AND F04 = ?", type.name());
        }
    }
    
    @Override
    public T6299[] getAddFilter(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F04 = ? AND F02 !=0 AND F03 !=0 ORDER BY T6299.F01 ASC";
            ArrayList<T6299> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, type.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6299 filter = new T6299();
                        filter.F01 = resultSet.getInt(1);
                        filter.F02 = resultSet.getInt(2);
                        filter.F03 = resultSet.getInt(3);
                        filter.F04 = T6299_F04.parse(resultSet.getString(4));
                        filter.F05 = resultSet.getTimestamp(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(filter);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6299[list.size()]));
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public T6211[] getBidTypeByCondition(String ids)
        throws Throwable
    {
        String[] idArray = ids.split(",");
        StringBuffer sql = new StringBuffer("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F01 IN ( ");
        for (int i = 0; i < idArray.length; i++)
        {
            if (i < idArray.length - 1)
            {
                sql.append(idArray[i] + ",");
            }
            else
            {
                sql.append(idArray[i]);
            }
        }
        sql.append(")");
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public T6216 getProductById(Integer id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,F16,F17,F18 FROM S62.T6216 WHERE T6216.F01=? "))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        T6216 product = new T6216();
                        product.F01 = rs.getInt(1);
                        product.F02 = rs.getString(2);
                        product.F03 = rs.getString(3);
                        product.F04 = T6216_F04.valueOf(rs.getString(4));
                        product.F05 = rs.getBigDecimal(5);
                        product.F06 = rs.getBigDecimal(6);
                        product.F07 = rs.getInt(7);
                        product.F08 = rs.getInt(8);
                        product.F09 = rs.getBigDecimal(9);
                        product.F10 = rs.getBigDecimal(10);
                        product.F11 = rs.getString(11);
                        product.F12 = rs.getBigDecimal(12);
                        product.F13 = rs.getBigDecimal(13);
                        product.F14 = rs.getBigDecimal(14);
                        product.F15 = rs.getBigDecimal(15);
                        product.F16 = rs.getInt(16);
                        product.F17 = rs.getInt(17);
                        product.F18 = T6216_F18.parse(rs.getString(18));
                        return product;
                    }
                }
            }
            return null;
        }
    }
    
    /**
     * 查看标动态管理列表
     * @param loandId
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<T6248> viewBidProgresList(int loandId, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT T6248.F04 AS F01, T6248.F05 AS F02, T6248.F06 AS F03, T6248.F07 AS F04, T6248.F08 AS F05, T6248.F09 AS F06, T7110.F02 AS F07, T6248.F01 AS F08 FROM S62.T6248 INNER JOIN S71.T7110 ON T6248.F02 = T7110.F01 WHERE T6248.F03 = ? AND T6248.F05 = ? ORDER BY T6248.F08 DESC";
            return selectPaging(connection, new ArrayParser<T6248>()
            {
                
                @Override
                public T6248[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6248> list = new ArrayList<T6248>();
                    while (resultSet.next())
                    {
                        T6248 t6248 = new T6248();
                        t6248.F04 = resultSet.getString(1);
                        t6248.F05 = T6248_F05.parse(resultSet.getString(2));
                        t6248.F06 = resultSet.getString(3);
                        t6248.F07 = resultSet.getTimestamp(4);
                        t6248.F08 = resultSet.getTimestamp(5);
                        t6248.F09 = resultSet.getString(6);
                        t6248.sysName = resultSet.getString(7);
                        t6248.F01 = resultSet.getInt(8);
                        list.add(t6248);
                    }
                    return list.toArray(new T6248[list.size()]);
                }
            }, paging, sql, loandId, T6248_F05.YFB.name());
        }
    }
    
    /**
     * 查询项目动态
     * @param loandId
     * @return
     * @throws Throwable
     */
    public boolean viewBidProgresCount(int loandId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S62.T6248 WHERE T6248.F03 = ? LIMIT 1"))
            {
                ps.setInt(1, loandId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    @Override
    public T6148 getT6148(String F02)
        throws Throwable
    {
        T6148 t6148 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S61.T6148 WHERE T6148.F02 = ? LIMIT 1"))
            {
                ps.setString(1, F02);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        t6148 = new T6148();
                        t6148.F01 = rs.getInt(1);
                        t6148.F02 = T6148_F02.parse(rs.getString(2));
                        t6148.F03 = rs.getInt(3);
                        t6148.F04 = rs.getInt(4);
                        t6148.F05 = rs.getTimestamp(5);
                    }
                }
            }
        }
        return t6148;
    }
    
    /**
     * 查询用户已投金额
     *
     * @param bid
     * @param userId
     * @return
     * @throws Throwable
     */
    @Override
    public BigDecimal selectUserMaxAmount(int bid, int userId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<BigDecimal>()
                {
                    @Override
                    public BigDecimal parse(ResultSet rs)
                        throws SQLException
                    {
                        if (rs.next())
                        {
                            return rs.getBigDecimal(1);
                        }
                        return BigDecimal.ZERO;
                    }
                },
                "SELECT SUM(t6504.F04) FROM S65.T6504 t6504 LEFT JOIN S65.T6501 t6501 ON t6504.F01 = t6501.F01 WHERE t6504.F02 = ? AND t6504.F03 = ? AND t6501.F03 = ?",
                userId,
                bid,
                T6501_F03.CG.name());
        }
    }
    
    @Override
    public T6230 queryT6230(int id)
        throws Throwable
    {
        return getT6230(id);
    }
    
    @Override
    public Zqzqlb getZqzrXq(int id)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6260.F02 AS F01, T6260.F03 AS F02, T6260.F04 AS F03, T6260.F05 AS F04, T6262.F07 AS F05, T6260.F07 AS F06, T6260.F08 AS F07, T6251.F04 AS F08, T6251.F05 AS F09, T6251.F06 AS F10, T6251.F07 AS F11, T6230.F03 AS F12, T6230.F04 AS F13,"
                    + " T6230.F06 AS F14, T6230.F09 AS F15, T6230.F23 AS F16,T6230.F11 AS F17,T6230.F13 AS F18,T6230.F14 AS F19,T6231.F02 AS F20,"
                    + "CASE T6260.F07 WHEN 'ZRZ' THEN T6231.F03 ELSE T6260.F09 END AS F21,T6230.F01 AS F22,T6260.F01 AS F23,T6251.F01 AS F24,T6230.F21 AS F25,T6230.F10 AS F26,T6230.F12 AS F27,(SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F28,"
                    + "CASE T6260.F07 WHEN 'ZRZ' THEN (SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F11 = T6260.F02 AND T6252.F05 IN (7001,7002) AND T6252.F09 = 'WH' ) ELSE T6260.F10 END AS F29,T6230.F32 AS F30,(SELECT S61.T6110.F02 FROM S61.T6110 WHERE T6251.F04 = T6110.F01) AS F31,(SELECT S61.T6110.F02 FROM S61.T6110 WHERE T6262.F03 = T6110.F01) AS F32,T6231.F06 AS F33, T6230.F01 AS F34 FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 "
                    + "INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6251.F03 = T6231.F01 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S' LEFT JOIN S62.T6262 ON T6260.F01 = T6262.F02");
        sql.append(" WHERE 1=1 AND T6251.F01 = ? AND T6260.F07 IN (?,?) ");
        try (Connection connection = getConnection())
        {
            Zqzqlb record = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6260_F07.ZRZ.name());
                pstmt.setString(3, T6260_F07.YJS.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Zqzqlb();
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
                        record.F27 = T6230_F10.parse(resultSet.getString(26));
                        record.F28 = T6230_F12.parse(resultSet.getString(27));
                        record.jgjc = resultSet.getString(28);
                        record.dsbx = resultSet.getBigDecimal(29);
                        record.F29 = resultSet.getInt(30);
                        record.yqsy = resultSet.getBigDecimal(29).subtract(resultSet.getBigDecimal(2));
                        try
                        {
                            record.F30 = getProductRiskLevel(connection, record.F29);
                        }
                        catch (Throwable ex)
                        {
                            logger.error("TransferManageImpl.search() error", ex);
                        }
                        record.zqzrz = resultSet.getString(31);
                        record.srr = resultSet.getString(32);
                        record.nextRepaymentDay = resultSet.getTimestamp(33);
                        record.bidId = resultSet.getInt(34);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public boolean isUserHasYQ(int userId)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < CURRENT_DATE ()";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 查询信用等级
     * @param F01
     * @return
     * @throws SQLException
     */
    private String getBxydj(int F01)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
            return null;
        }
    }
}