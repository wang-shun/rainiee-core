/**
 *
 */
package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S51.enums.T5129_F03;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6234;
import com.dimeng.p2p.S62.entities.T6235;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6241;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6280;
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
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6230_F33;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F29;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6231_F35;
import com.dimeng.p2p.S62.enums.T6231_F36;
import com.dimeng.p2p.S62.enums.T6233_F10;
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.S62.enums.T6241_F05;
import com.dimeng.p2p.S62.enums.T6248_F05;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F09;
import com.dimeng.p2p.S62.enums.T6250_F11;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6280_F10;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Bid;
import com.dimeng.p2p.modules.bid.console.service.entity.BidCheck;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDyw;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDywsx;
import com.dimeng.p2p.modules.bid.console.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.console.service.entity.TbRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.console.service.query.LoanExtendsQuery;
import com.dimeng.p2p.modules.bid.console.service.query.LoanQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * @author guopeng
 *
 */
public class BidManageImpl extends AbstractBidService implements BidManage
{
    
    public BidManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final String SQL =
        "SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F06 AS F06, T6230.F07 AS F07, T6230.F08 AS F08, T6230.F09 AS F09, T6230.F10 AS F10, T6230.F11 AS F11, T6230.F12 AS F12, T6230.F13 AS F13, T6230.F14 AS F14, T6230.F15 AS F15, T6230.F16 AS F16, T6230.F17 AS F17, T6230.F18 AS F18, T6230.F19 AS F19, T6230.F20 AS F20, T6230.F21 AS F21, T6230.F22 AS F22, T6230.F23 AS F23, T6230.F24 AS F24, T6230.F25 AS F25, T6230.F26 AS F26, T6231.F02 AS F27, T6231.F03 AS F28, T6231.F07 AS F29, T6231.F08 AS F30, T6231.F10 AS F31, T6231.F11 AS F32, T6231.F12 AS F33, T6231.F13 AS F34, T6231.F14 AS F35, T6231.F15 AS F36, T6110.F02 AS F37, T6110.F06 AS F38, T6110.F10 AS F39 ,T6211.F02 AS F40, T6231.F21 AS F41, T6231.F22 AS F42, T6216.F02 AS F43, T6231.F29 AS F44, CASE T6230.F20 WHEN 'SQZ' THEN\t'A' WHEN 'DSH' THEN\t'B' WHEN 'DFB' THEN 'C' WHEN 'YFB' THEN 'D' WHEN 'TBZ' THEN 'E' WHEN 'DFK' THEN\t'F' WHEN 'HKZ' THEN 'G' WHEN 'YJQ' THEN\t'H' WHEN 'YDF' THEN\t'I' WHEN 'YZR' THEN\t'J' WHEN 'YLB' THEN\t'K' WHEN 'YZF' THEN 'L'\n ELSE T6230.F20 END AS F45 FROM S62.T6230 INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 INNER JOIN S62.T6231 ON T6230.F01=T6231.F01 LEFT JOIN S62.T6211 ON T6230.F04=T6211.F01 LEFT JOIN S62.T6216 ON T6230.F32=T6216.F01 WHERE T6230.F27=?";
    
    protected static final ArrayParser<Bid> ARRAY_PARSER = new ArrayParser<Bid>()
    {
        
        @Override
        public Bid[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<Bid> bids = new ArrayList<>();
            while (resultSet.next())
            {
                Bid record = new Bid();
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
                record.F27 = resultSet.getInt(27);
                record.F28 = resultSet.getInt(28);
                record.F29 = resultSet.getInt(29);
                record.F30 = resultSet.getString(30);
                record.F31 = resultSet.getTimestamp(31);
                record.F32 = resultSet.getTimestamp(32);
                record.F33 = resultSet.getTimestamp(33);
                record.F34 = resultSet.getTimestamp(34);
                record.F35 = resultSet.getTimestamp(35);
                record.F36 = resultSet.getTimestamp(36);
                record.F37 = resultSet.getString(37);
                record.F38 = EnumParser.parse(T6110_F06.class, resultSet.getString(38));
                record.F39 = EnumParser.parse(T6110_F10.class, resultSet.getString(39));
                record.F40 = resultSet.getString(40);
                record.F41 = T6231_F21.parse(resultSet.getString(41));
                record.F42 = resultSet.getInt(42);
                record.productName = resultSet.getString(43);
                record.F43 = T6231_F29.parse(resultSet.getString(44));
                bids.add(record);
            }
            return bids.toArray(new Bid[bids.size()]);
        }
    };
    
    @Override
    public PagingResult<Bid> search(LoanQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder(SQL);
        List<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F27.F);
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6230.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            int typeId = query.getType();
            if (typeId > 0)
            {
                sb.append(" AND T6211.F01 = ?");
                parameters.add(typeId);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) <=?");
                parameters.add(timestamp);
            }
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            T6230_F20 status = query.getStatus();
            if (status != null)
            {
                sb.append(" AND T6230.F20 =?");
                parameters.add(status);
            }
        }
        sb.append(" ORDER BY T6230.F24 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public T6230 get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return getT6230(connection, id);
        }
    }
    
    private T6230 getT6230(Connection connection, int id)
        throws Throwable
    {
        
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18, T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, T6230.F26,T6231.F07, T6230.F28,T6230.F33 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01=T6231.F01 WHERE T6230.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
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
                    record.xsb = T6230_F28.parse(resultSet.getString(28));
                    record.F33 = T6230_F33.parse(resultSet.getString(29));
                }
            }
        }
        return record;
    }
    
    @Override
    public T6231 getExtra(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            T6231 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F21, F22, F25, F26, F27, F28, F33, F36 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
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
                        record.F36 = T6231_F36.parse(resultSet.getString(24));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public PagingResult<TbRecord> getRecord(int id, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT T6250.F01 AS F01, T6250.F02 AS F02, T6250.F03 AS F03, T6250.F04 AS F04, T6250.F05 AS F05, T6250.F06 AS F06, T6250.F07 AS F07, T6110.F02 AS F08, T6286.F04 AS F09, T6250.F11 AS F10 , T6292.F04 AS F11, T6250.F09 AS F12, T6288.F04 AS F13, T6110.F06 AS F14, T6110.F10 AS F15 "
                    + " FROM S62.T6250 INNER JOIN S61.T6110 ON T6250.F03 = T6110.F01 LEFT JOIN S62.T6286 ON T6286.F09 = T6250.F01 LEFT JOIN S62.T6292 ON T6292.F09 = T6250.F01 LEFT JOIN S62.T6288 ON T6288.F09 = T6250.F01 WHERE T6250.F02 = ? ORDER BY T6250.F06 DESC ";
            return selectPaging(connection, new ArrayParser<TbRecord>()
            {
                
                @Override
                public TbRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TbRecord> list = new ArrayList<TbRecord>();
                    while (resultSet.next())
                    {
                        TbRecord record = new TbRecord();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6250_F07.parse(resultSet.getString(7));
                        record.userName = resultSet.getString(8);
                        record.exAmount = resultSet.getBigDecimal(9);
                        record.F11 = T6250_F11.parse(resultSet.getString(10));
                        record.hbAmount = resultSet.getBigDecimal(11);
                        record.bidWay = "手动";
                        if (T6250_F09.S.name().equals(resultSet.getString(12)))
                        {
                            record.bidWay = "自动";
                        }
                        record.jxll =
                            resultSet.getBigDecimal(13) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(13)
                                .multiply(new BigDecimal(100));
                        record.userType = T6110_F06.parse(resultSet.getString(14));
                        record.sfdb = T6110_F10.parse(resultSet.getString(15));
                        list.add(record);
                    }
                    return list.toArray(new TbRecord[list.size()]);
                }
            },
                paging,
                sql,
                id);
        }
    }
    
    @Override
    public PagingResult<Hkjllb> getHk(int id, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT SUM(T6252.F07), T6252.F08, T6252.F09, T6252.F10, T5122.F02 FROM S62.T6252 LEFT JOIN S51.T5122 ON  T6252.F05 = T5122.F01 WHERE T6252.F02 = ? AND T6252.F09 <> 'DF' GROUP BY T6252.F05,T6252.F06 ORDER BY T6252.F06,T6252.F05 ASC";
            return selectPaging(connection, new ArrayParser<Hkjllb>()
            {
                
                @Override
                public Hkjllb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Hkjllb> list = new ArrayList<Hkjllb>();
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        list.add(record);
                    }
                    return list.toArray(new Hkjllb[list.size()]);
                }
            }, paging, sql, id);
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
                connection.prepareStatement("SELECT T6262.F01 AS F01, T6262.F02 AS F02, T6262.F03 AS F03, T6262.F04 AS F04, T6262.F05 AS F05, T6262.F06 AS F06, T6262.F07 AS F07, T6262.F08 AS F08, T6262.F09 AS F09, T6260.F03 AS F10, T6260.F04 AS F11 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 WHERE T6251.F03 = ?"))
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
        String sql = "SELECT IFNULL(SUM(F26),0),COUNT(*) FROM S62.T6230 WHERE F20 IN (?,?,?)";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
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
        statistics.userEarnMoney = getEarnMoney();
        return statistics;
    }
    
    private BigDecimal getEarnMoney()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F05 IN (?,?) AND F09 = ?"))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setString(3, T6252_F09.YH.name());
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
    
    private String isYuqi(int userId, Connection connection)
        throws Throwable
    {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setTimestamp(1, getCurrentTimestamp(connection));
            ps.setString(2, T6252_F09.WH.name());
            ps.setInt(3, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int days = rs.getInt(1);
                    if (days > 0)
                    {
                        return "Y";
                    }
                }
            }
        }
        return "N";
    }
    
    @Override
    public int add(T6230 entity, T6231 t6231, T6238 t6238, UploadFile uploadFile)
        throws Throwable
    {
        if (entity == null || t6238 == null)
        {
            return 0;
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        if (entity.F05 == null || entity.F05.compareTo(t6231.F25) < 0)
        {
            throw new LogicalException("借款金额不能低于最低起投金额(" + t6231.F25 + ")");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (!isNetSign(entity.F02, connection))
                {
                    throw new LogicalException("该借款用户未进行网签不能借款");
                }
                if ("Y".equals(isYuqi(entity.F02, connection)))
                {
                    throw new LogicalException("该用户存在逾期不能借款");
                }
                
                String imageCode = null;
                if (null != uploadFile)
                {
                    FileStore fileStore = serviceResource.getResource(FileStore.class);
                    imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), uploadFile)[0];
                }
                int id = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6230 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F16 = ?, F17 = ?, F18 = ?, F19 = ?, F20 = ?, F21 = ?, F24 = ?, F25 = ? ,F26=?, F28 = ?, F29 = ?,F32=?,F33=? ",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, entity.F02);
                    pstmt.setString(2, entity.F03);
                    pstmt.setInt(3, entity.F04);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setBigDecimal(5, entity.F06.divide(new BigDecimal(100)));
                    pstmt.setBigDecimal(6, entity.F05);
                    pstmt.setInt(7, entity.F08);
                    pstmt.setInt(8, entity.F09);
                    pstmt.setString(9, entity.F10.name());
                    pstmt.setString(10, entity.F11 == null ? T6230_F11.F.name() : T6230_F11.S.name());
                    pstmt.setString(11, entity.F12.name());
                    pstmt.setString(12, entity.F13 == null ? T6230_F13.F.name() : T6230_F13.S.name());
                    pstmt.setString(13, entity.F14 == null ? T6230_F14.F.name() : T6230_F14.S.name());
                    pstmt.setString(14, entity.F15 == null ? T6230_F15.F.name() : entity.F15.name());
                    pstmt.setString(15, T6230_F16.F.name());
                    pstmt.setString(16, entity.F17.name());
                    int gdr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.GDR));
                    if (entity.F18 <= 0 || entity.F18 > 28)
                    {
                        entity.F18 = gdr;
                    }
                    int qxr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.QXTS));
                    if (entity.F17 == T6230_F17.GDR)
                    {
                        pstmt.setInt(17, entity.F18);
                    }
                    else if (entity.F17 == T6230_F17.ZRY)
                    {
                        pstmt.setInt(17, qxr);
                    }
                    pstmt.setInt(18, entity.F19);
                    pstmt.setString(19, T6230_F20.SQZ.name());
                    pstmt.setString(20, imageCode);
                    pstmt.setTimestamp(21, getCurrentTimestamp(connection));
                    pstmt.setString(22, getCrid(connection, entity));
                    pstmt.setBigDecimal(23, entity.F05);
                    pstmt.setString(24, entity.xsb == null ? T6230_F28.F.name() : T6230_F28.S.name());
                    pstmt.setString(25, entity.xsb == null ? T6230_F28.F.name() : T6230_F28.S.name());
                    pstmt.setInt(26, entity.F32);
                    pstmt.setString(27, entity.F33 == null ? T6230_F33.F.name() : T6230_F33.S.name());
                    pstmt.execute();
                    
                    try (ResultSet resultSet = pstmt.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            id = resultSet.getInt(1);
                            t6231.F01 = id;
                            t6231.F02 = entity.F09;
                            t6231.F03 = entity.F09;
                            t6231.F04 = entity.F06.divide(new BigDecimal(12 * 100), 9, BigDecimal.ROUND_HALF_UP);
                            t6231.F05 =
                                entity.F06.divide(new BigDecimal(
                                    100 * IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                                    9,
                                    BigDecimal.ROUND_HALF_UP);
                            t6238.F01 = id;
                            addT6231(t6231, t6238, connection, entity.F02);
                        }
                    }
                    writeLog(connection, "操作日志", "新增标的");
                }
                serviceResource.commit(connection);
                return id;
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    // 生成标的编号
    protected String getCrid(Connection connection, T6230 entity)
        throws Throwable
    {
        // 序号
        String serNo = "";
        if (StringHelper.isEmpty(entity.F25))
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement(" SELECT MAX(F04) FROM S51.T5129 WHERE T5129.F02 = CURDATE() AND T5129.F03 = ? FOR UPDATE"))
            {
                pstmt.setString(1, T5129_F03.BDBH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        serNo = String.format("%04d", resultSet.getInt(1) + 1);
                        try (PreparedStatement inpstmt =
                            connection.prepareStatement(" INSERT INTO S51.T5129 (F02, F03, F04) VALUES (CURDATE(), ?, ?) ON DUPLICATE KEY UPDATE F04 = F04 + 1"))
                        {
                            inpstmt.setString(1, T5129_F03.BDBH.name());
                            inpstmt.setInt(2, resultSet.getInt(1) + 1);
                            inpstmt.execute();
                        }
                    }
                }
            }
            
            // 获取当前时间
            String nowDate = DateParser.format(getCurrentDate(connection), "yyyyMMdd");
            // 担保
            if (T6230_F11.S == entity.F11)
            {
                serNo = "D" + nowDate + serNo;
                // 抵押
            }
            else if (T6230_F13.S == entity.F13)
            {
                serNo = "Y" + nowDate + serNo;
                // 实地
            }
            else if (T6230_F14.S == entity.F14)
            {
                serNo = "S" + nowDate + serNo;
                // 信用
            }
            else
            {
                serNo = "X" + nowDate + serNo;
            }
        }
        else
        {
            // 担保
            if (T6230_F11.S == entity.F11)
            {
                serNo = "D" + entity.F25.substring(1);
                // 抵押
            }
            else if (T6230_F13.S == entity.F13)
            {
                serNo = "Y" + entity.F25.substring(1);
                // 实地
            }
            else if (T6230_F14.S == entity.F14)
            {
                serNo = "S" + entity.F25.substring(1);
                // 信用
            }
            else
            {
                serNo = "X" + entity.F25.substring(1);
            }
        }
        entity.F25 = serNo;
        return serNo;
    }
    
    /**
     * 插入扩展信息
     *
     * @param entity
     * @throws Throwable
     */
    private void addT6231(T6231 entity, T6238 t6238, Connection connection, int userId)
        throws Throwable
    {
        if (entity == null || t6238 == null)
        {
            return;
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6231 (F01, F02, F03, F04, F05, F07, F08, F09,F16,F21,F22,F25,F26, F27, F28, F31, F32, F33, F36) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02=VALUES(F02), F03 = VALUES(F03),F04 = VALUES(F04), F05 = VALUES(F05), F07 = VALUES(F07), F08 = VALUES(F08), F09 = VALUES(F09), F16 = VALUES(F16), F21 = VALUES(F21), F22 = VALUES(F22),F25 = VALUES(F25), F26 = VALUES(F26), F27 = VALUES(F27), F28 = VALUES(F28), F31 = VALUES(F31), F32 = VALUES(F32), F33 = VALUES(F33), F36 = VALUES(F36)"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F07);
            pstmt.setString(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, entity.F16);
            pstmt.setString(10, entity.F21 == null ? T6231_F21.F.name() : entity.F21.name());
            pstmt.setInt(11, entity.F22 <= 0 ? 0 : entity.F22);
            pstmt.setBigDecimal(12, entity.F25);
            pstmt.setBigDecimal(13, entity.F26);
            pstmt.setString(14, entity.F27 == null ? T6231_F27.F.name() : entity.F27.name());
            //如果是奖励标，则计算奖励利率
            entity.F28 = entity.F28.divide(new BigDecimal(100), 9, BigDecimal.ROUND_HALF_UP);
            pstmt.setBigDecimal(15, entity.F28);
            pstmt.setString(16, (String)getEmpInfo(userId, connection).get("empNum"));
            pstmt.setString(17, (String)getEmpInfo(userId, connection).get("empStatus"));
            pstmt.setString(18, entity.F33.name());
            pstmt.setString(19, entity.F36.name());
            pstmt.execute();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6238 (F01, F02, F03, F04) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04)"))
        {
            pstmt.setInt(1, t6238.F01);
            pstmt.setBigDecimal(2, t6238.F02);
            pstmt.setBigDecimal(3, t6238.F03);
            pstmt.setBigDecimal(4, t6238.F04);
            pstmt.execute();
        }
    }
    
    @Override
    public void submit(int loanId)
        throws Throwable
    {
        String sql = "UPDATE S62.T6230 SET F20 = ?, F24 = ? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, T6230_F20.DSH, getCurrentTimestamp(connection), loanId);
        }
    }
    
    @Override
    public void addGuarantee(T6236 t6236)
        throws Throwable
    {
        if (t6236 == null)
        {
            throw new LogicalException("担保信息不存在");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6236 WHERE T6236.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, t6236.F02);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        throw new LogicalException("担保信息已存在");
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S62.T6236 SET F02 = ?, F03 = ?, F04 = ?,F05=?"))
            {
                pstmt.setInt(1, t6236.F02);
                pstmt.setInt(2, t6236.F03);
                pstmt.setString(3, T6236_F04.S.name());
                pstmt.setString(4, t6236.F05);
                pstmt.execute();
            }
            markYCL(connection, t6236.F01);
        }
    }
    
    @Override
    public void updateGuarantee(T6236 t6236)
        throws Throwable
    {
        if (t6236 == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            int bidId;
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S62.T6236 WHERE F01 = ?"))
            {
                pstmt.setInt(1, t6236.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        bidId = resultSet.getInt(1);
                    }
                    else
                    {
                        return;
                    }
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6236 SET F05 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, t6236.F05);
                pstmt.setInt(2, t6236.F01);
                pstmt.execute();
            }
            markYCL(connection, bidId);
        }
    }
    
    protected void markYCL(Connection connection, int bidId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6241 SET F05 = 'YCL' WHERE F02 = ? AND F05 = 'WCL'"))
        {
            pstmt.setInt(1, bidId);
            pstmt.execute();
        }
    }
    
    @Override
    public void addFx(T6237 t6237)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S62.T6237 (F01,F02, F03) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F03 = VALUES(F03)"))
            {
                pstmt.setInt(1, t6237.F01);
                pstmt.setString(2, t6237.F02);
                pstmt.setString(3, t6237.F03);
                pstmt.execute();
            }
            markYCL(connection, t6237.F01);
        }
    }
    
    @Override
    public void updateFx(T6237 t6237)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("update S62.T6237 SET F02 = ?, F03 = ? where F01=?"))
            {
                pstmt.setString(1, t6237.F02);
                pstmt.setString(2, t6237.F03);
                pstmt.setInt(3, t6237.F01);
                pstmt.execute();
            }
            markYCL(connection, t6237.F01);
        }
    }
    
    @Override
    public void update(T6230 entity, T6231 t6231, T6238 t6238, UploadFile uploadFile)
        throws Throwable
    {
        if (entity == null || t6238 == null)
        {
            return;
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        //BigDecimal minBidAmount =  BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.MIN_BIDING_AMOUNT));
        BigDecimal minBidAmount = t6231.F25;
        if (entity.F05 == null || entity.F05.compareTo(minBidAmount) < 0)
        {
            throw new LogicalException("借款金额不能低于最低起投金额(" + minBidAmount + ")");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                //String imageCode = null;
                StringBuffer sb =
                    new StringBuffer(
                        "UPDATE S62.T6230 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12=?,F13 = ?, F14 = ?,F15=?,F16=?,F17=?, F24 = ?,F26=?,F18=?,F19=?,F25=? ,F28=?,F29=?,F32=?,F33=?,F21=? WHERE F01 = ? ");
                if (null != uploadFile)
                {
                    FileStore fileStore = serviceResource.getResource(FileStore.class);
                    entity.F21 = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), uploadFile)[0];
                    //sb.append(",F21=? ");
                }
                //sb.append(" WHERE F01 = ? ");
                try (PreparedStatement pstmt = connection.prepareStatement(sb.toString()))
                {
                    pstmt.setString(1, entity.F03);
                    pstmt.setInt(2, entity.F04);
                    pstmt.setBigDecimal(3, entity.F05);
                    pstmt.setBigDecimal(4, entity.F06.divide(new BigDecimal(100)));
                    pstmt.setBigDecimal(5, entity.F05);
                    pstmt.setInt(6, entity.F08);
                    pstmt.setInt(7, entity.F09);
                    pstmt.setString(8, entity.F10.name());
                    pstmt.setString(9, entity.F11 == null ? T6230_F11.F.name() : T6230_F11.S.name());
                    pstmt.setString(10, entity.F12.name());
                    pstmt.setString(11, entity.F13 == null ? T6230_F13.F.name() : T6230_F13.S.name());
                    pstmt.setString(12, entity.F14 == null ? T6230_F14.F.name() : T6230_F14.S.name());
                    pstmt.setString(13, entity.F15 == null ? T6230_F15.F.name() : entity.F15.name());
                    pstmt.setString(14, T6230_F16.F.name());
                    pstmt.setString(15, entity.F17.name());
                    pstmt.setTimestamp(16, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(17, entity.F05);
                    int qxr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.QXTS));
                    if (entity.F17 == T6230_F17.GDR)
                    {
                        pstmt.setInt(18, entity.F18);
                    }
                    else if (entity.F17 == T6230_F17.ZRY)
                    {
                        pstmt.setInt(18, qxr);
                    }
                    pstmt.setInt(19, entity.F19);
                    pstmt.setString(20, getCrid(connection, entity));
                    pstmt.setString(21, entity.xsb == null ? T6230_F28.F.name() : T6230_F28.S.name());
                    pstmt.setString(22, entity.xsb == null ? T6230_F28.F.name() : T6230_F28.S.name());
                    pstmt.setInt(23, entity.F32);
                    pstmt.setString(24, entity.F33 == null ? T6230_F33.F.name() : T6230_F33.S.name());
                    pstmt.setString(25, entity.F21);
                    pstmt.setInt(26, entity.F01);
                    /*if (null != imageCode)
                    {
                        pstmt.setString(25, imageCode);
                        pstmt.setInt(26, entity.F01);
                    }
                    else
                    {
                        pstmt.setInt(25, entity.F01);
                    }*/
                    pstmt.execute();
                }
                t6231.F01 = entity.F01;
                t6231.F02 = entity.F09;
                t6231.F03 = entity.F09;
                t6231.F04 = entity.F06.divide(new BigDecimal(12 * 100), 9, BigDecimal.ROUND_HALF_UP);
                t6231.F05 =
                    entity.F06.divide(new BigDecimal(
                        100 * IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                        9,
                        BigDecimal.ROUND_HALF_UP);
                t6238.F01 = entity.F01;
                addT6231(t6231, t6238, connection, entity.F02);
                //修改时，不需要更新审核记录表
                //markYCL(connection, entity.F01);
                writeLog(connection, "操作日志", "修改标的");
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public T6238 getBidFl(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        T6238 record = new T6238();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getBigDecimal(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6211[] getBidType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = new ArrayList<>();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ? ORDER BY F01 DESC "))
            {
                pstmt.setString(1, T6211_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    @Override
    public T6230 getBidType(int loanId)
        throws Throwable
    {
        T6230 record = new T6230();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6230.F11, T6230.F13, T6230.F14, T6230.F20,T6230.F05, T6230.F32 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F11 = T6230_F11.parse(resultSet.getString(1));
                        record.F13 = T6230_F13.parse(resultSet.getString(2));
                        record.F14 = T6230_F14.parse(resultSet.getString(3));
                        record.F20 = T6230_F20.parse(resultSet.getString(4));
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F32 = resultSet.getInt(6);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void addBidDyw(BidDyw bidDyw)
        throws Throwable
    {
        if (bidDyw == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int dywId = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6234 SET F02 = ?, F03 = ?, F04 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, bidDyw.F02);
                    pstmt.setInt(2, bidDyw.F03);
                    pstmt.setString(3, bidDyw.F04);
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                    {
                        if (resultSet.next())
                        {
                            dywId = resultSet.getInt(1);
                        }
                    }
                }
                if (bidDyw.t6235s != null)
                {
                    for (T6235 t6235 : bidDyw.t6235s)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("INSERT INTO S62.T6235 SET F02 = ?, F03 = ?, F04 = ?"))
                        {
                            pstmt.setInt(1, dywId);
                            pstmt.setInt(2, t6235.F03);
                            pstmt.setString(3, t6235.F04);
                            pstmt.execute();
                        }
                    }
                }
                markYCL(connection, bidDyw.F02);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updateBidDyw(BidDyw bidDyw)
        throws Throwable
    {
        if (bidDyw == null)
        {
            return;
        }
        if (bidDyw.F01 == 0 || bidDyw.t6235s == null || bidDyw.t6235s.length == 0)
        {
            return;
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int bidId;
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S62.T6234 WHERE F01 = ?"))
                {
                    pstmt.setInt(1, bidDyw.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            bidId = resultSet.getInt(1);
                        }
                        else
                        {
                            return;
                        }
                    }
                }
                for (T6235 t6235 : bidDyw.t6235s)
                {
                    int dywsxId = 0;
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F01 FROM S62.T6235 WHERE T6235.F02 = ? AND T6235.F03=? LIMIT 1"))
                    {
                        pstmt.setInt(1, bidDyw.F01);
                        pstmt.setInt(2, t6235.F03);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                dywsxId = resultSet.getInt(1);
                            }
                        }
                    }
                    if (dywsxId > 0)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6235 SET F02 = ?,F03 = ?, F04 = ? WHERE F01 = ?"))
                        {
                            pstmt.setInt(1, bidDyw.F01);
                            pstmt.setInt(2, t6235.F03);
                            pstmt.setString(3, t6235.F04);
                            pstmt.setInt(4, dywsxId);
                            pstmt.execute();
                        }
                    }
                }
                markYCL(connection, bidId);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public T6234 getBidDyw(int id)
        throws Throwable
    {
        T6234 record = new T6234();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6234 WHERE T6234.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6234[] searchBidDyw(int loanId)
        throws Throwable
    {
        ArrayList<T6234> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6234 WHERE F02=?"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6234 record = new T6234();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        list.add(record);
                    }
                }
            }
        }
        return list.toArray(new T6234[list.size()]);
    }
    
    @Override
    public BidDywsx[] searchBidDywsx(int id)
        throws Throwable
    {
        ArrayList<BidDywsx> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6235.F01, T6235.F02, T6235.F03, T6235.F04,T6214.F03 FROM S62.T6235 INNER JOIN S62.T6214 ON T6235.F03=T6214.F01 WHERE T6235.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        BidDywsx record = new BidDywsx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.name = resultSet.getString(5);
                        list.add(record);
                    }
                }
            }
        }
        return list.toArray(new BidDywsx[list.size()]);
    }
    
    @Override
    public Dbxx[] searchBidDb(int loanId)
        throws Throwable
    {
        ArrayList<Dbxx> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6236.F05 AS F05, CASE T6110.F06 WHEN 'ZRR' THEN T6141.F02 ELSE T6161.F04 END AS F06 FROM S62.T6236 INNER JOIN S61.T6110 ON T6236.F03 = T6110.F01 LEFT JOIN S61.T6161 ON T6161.F01 = T6110.F01 LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 WHERE T6236.F02 = ?"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Dbxx record = new Dbxx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6236_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.jgjs = getJgDes(record.F03);
                        list.add(record);
                    }
                }
            }
        }
        return list.toArray(new Dbxx[list.size()]);
    }
    
    private String getJgDes(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6180 WHERE T6180.F01 = ? LIMIT 1"))
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
        }
        return "";
    }
    
    @Override
    public Dbxx getBidDb(int id)
        throws Throwable
    {
        Dbxx record = new Dbxx();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6236.F05 AS F05, CASE T6110.F06 WHEN 'ZRR' THEN T6141.F02 ELSE T6161.F04 END AS F06 FROM S62.T6236 INNER JOIN S61.T6110 ON T6236.F03 = T6110.F01 LEFT JOIN S61.T6161 ON T6161.F01 = T6110.F01 LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 WHERE T6236.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6236_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6237 getBidFx(int loanId)
        throws Throwable
    {
        T6237 record = new T6237();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03 FROM S62.T6237 WHERE T6237.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void through(int loanId)
        throws Throwable
    {
        
        if (loanId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                int userId = 0;
                BigDecimal amount = new BigDecimal(0);
                T6230_F11 t6230_F11 = T6230_F11.F;
                T6230_F20 t6230_F20 = null;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02,F05,F11,F20 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, loanId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userId = resultSet.getInt(1);
                            amount = resultSet.getBigDecimal(2);
                            t6230_F11 = EnumParser.parse(T6230_F11.class, resultSet.getString(3));
                            t6230_F20 = EnumParser.parse(T6230_F20.class, resultSet.getString(4));
                        }
                    }
                }
                if (T6230_F20.DSH != t6230_F20)
                {
                    throw new LogicalException("不是待审核状态不能审核通过");
                }
                int xyTypeId = 0;
                if (t6230_F11 == T6230_F11.S)
                {
                    xyTypeId = 1002;
                }
                else
                {
                    xyTypeId = 1001;
                }
                int version = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, xyTypeId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            version = resultSet.getInt(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6239 (F01, F02, F03) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03)"))
                {
                    pstmt.setInt(1, loanId);
                    pstmt.setInt(2, xyTypeId);
                    pstmt.setInt(3, version);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F24 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.DFB.name());
                    pstmt.setTimestamp(2, timestamp);
                    pstmt.setInt(3, loanId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6231 SET F10 = ? WHERE F01 = ?"))
                {
                    pstmt.setTimestamp(1, timestamp);
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                BigDecimal totalAmount = new BigDecimal(0);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, userId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            totalAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                if (totalAmount.compareTo(amount) < 0)
                {
                    throw new LogicalException("用户信用额度小于借款金额不能审核通过");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
                {
                    pstmt.setBigDecimal(1, totalAmount.subtract(amount));
                    pstmt.setInt(2, userId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F06 = ?, F07 = ?, F08 = ?"))
                {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, FeeCode.XY_FB_TZ);
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setBigDecimal(4, amount);
                    pstmt.setBigDecimal(5, totalAmount.subtract(amount));
                    pstmt.setString(6, "标的审核调整信用额度");
                    pstmt.execute();
                }
                // 查询担保机构的信用额度
                BigDecimal DbtotalAmount = new BigDecimal(0);
                int DbUserId = 0;
                String sql =
                    "SELECT T6116.F03,T6236.F03 FROM S62.T6236 INNER JOIN S61.T6116 ON T6236.F03 = T6116.F01 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1";
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                boolean isHasGuarant =
                    BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                int dbId = 0;
                if (isHasGuarant && t6230_F11 == T6230_F11.S)
                {
                    sql =
                        "SELECT T6125.F01,T6236.F03 FROM S62.T6236 INNER JOIN S61.T6125 ON T6236.F03 = T6125.F02 WHERE T6236.F02 = ? AND T6236.F04 = 'S' AND T6125.F05 = ? LIMIT 1";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql))
                    {
                        pstmt.setInt(1, loanId);
                        pstmt.setString(2, T6125_F05.SQCG.name());
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                dbId = resultSet.getInt(1);
                                DbUserId = resultSet.getInt(2);
                            }
                        }
                    }
                    
                    if (DbUserId == userId)
                    {
                        throw new LogicalException("担保方不能担保自己借款的项目");
                    }
                    
                    if (dbId <= 0)
                    {
                        throw new LogicalException("担保方不存在或已取消担保资格");
                    }
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT T6125.F04 FROM S61.T6125 WHERE T6125.F01 = ? FOR UPDATE "))
                    {
                        pstmt.setInt(1, dbId);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
                            {
                                DbtotalAmount = rs.getBigDecimal(1);
                            }
                        }
                    }
                }
                else
                {
                    try (PreparedStatement pstmt = connection.prepareStatement(sql))
                    {
                        pstmt.setInt(1, loanId);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                DbtotalAmount = resultSet.getBigDecimal(1);
                                DbUserId = resultSet.getInt(2);
                            }
                        }
                    }
                }
                if (t6230_F11 == T6230_F11.S)
                {
                    if (DbtotalAmount.compareTo(amount) < 0)
                    {
                        throw new LogicalException("担保额度小于借款金额不能审核通过");
                    }
                    execute(connection,
                        "UPDATE S61.T6125 SET F04 = ? WHERE F01 = ? ",
                        DbtotalAmount.subtract(amount),
                        dbId);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F06 = ?, F07 = ?, F08 = ?"))
                    {
                        pstmt.setInt(1, DbUserId);
                        pstmt.setInt(2, FeeCode.DB_FB_KC);
                        pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                        pstmt.setBigDecimal(4, amount);
                        pstmt.setBigDecimal(5, DbtotalAmount.subtract(amount));
                        pstmt.setString(6, "标的审核扣除担保额度");
                        pstmt.execute();
                    }
                }
                writeLog(connection, "操作日志", "审核标的通过");
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6241 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, loanId);
                    pstmt.setInt(2, serviceResource.getSession().getAccountId());
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setString(4, T6241_F05.YCL.name());
                    pstmt.execute();
                }
                
                //发标审核成功，发站内信
                Bid bid = selectBid(connection, loanId);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("datetime", DateTimeParser.format(timestamp));
                envionment.set("name", bid.F37);
                envionment.set("title", bid.F03);
                envionment.set("lookUrl", configureProvider.format(URLVariable.USER_JKSQCX));
                String content = configureProvider.format(LetterVariable.LOAN_CHECKED, envionment);
                sendLetter(connection, bid.F02, "发标审核成功", content);
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void notThrough(int loanId, String des)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = get(loanId);
                if (T6230_F20.DSH != t6230.F20)
                {
                    throw new LogicalException("不是待审核状态不能审核不通过");
                }
                Timestamp timestamp = getCurrentTimestamp(connection);
                int count = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM S62.T6241 WHERE T6241.F02 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, loanId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            count = resultSet.getInt(1);
                        }
                    }
                }
                if (count < 10)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F24 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.SQZ.name());
                        pstmt.setTimestamp(2, timestamp);
                        pstmt.setInt(3, loanId);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F10 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, timestamp);
                        pstmt.setInt(2, loanId);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S62.T6241 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                            PreparedStatement.RETURN_GENERATED_KEYS))
                    {
                        pstmt.setInt(1, loanId);
                        pstmt.setInt(2, serviceResource.getSession().getAccountId());
                        pstmt.setTimestamp(3, timestamp);
                        pstmt.setString(4, T6241_F05.WCL.name());
                        pstmt.setString(5, des);
                        pstmt.execute();
                    }
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F24 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.YZF.name());
                        pstmt.setInt(2, loanId);
                        pstmt.execute();
                    }
                }
                
                //发站内信你
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("datetime", TimestampParser.format(t6230.F24));
                envionment.set("title", t6230.F03);
                envionment.set("reason", des);
                String content = configureProvider.format(LetterVariable.LOAN_CHECKED_FAILD, envionment);
                sendLetter(connection, t6230.F02, "借款申请审核不通过", content);
                
                writeLog(connection, "操作日志", "审核标的不通过,原因:" + des);
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void release(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = getT6230(connection, loanId);
                Timestamp timestamp = getCurrentTimestamp(connection);
                if (t6230.F20 != T6230_F20.DFB)
                {
                    throw new LogicalException("不是待发布状态");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F22 = ?, F24 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.TBZ.name());
                    pstmt.setTimestamp(2, timestamp);
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setInt(4, loanId);
                    pstmt.execute();
                }
                T6110 t6110 = selectT6110(connection, t6230.F02);
                
                //发站内信你
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("userName", t6110.F02);
                envionment.set("title", t6230.F03);
                envionment.set("lookUrl", configureProvider.format(URLVariable.USER_JKSQCX));
                String content = configureProvider.format(LetterVariable.LOAN_RELEASE_SUCCESS, envionment);
                sendLetter(connection, t6230.F02, "借款申请已发布", content);
                writeLog(connection, "操作日志", "发布标的成功");
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void preRelease(int loanId, Timestamp releaseTime)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = getT6230(connection, loanId);
                if (t6230.F20 != T6230_F20.DFB)
                {
                    throw new LogicalException("不是待发布状态");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F22 = ?, F24 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.YFB.name());
                    pstmt.setTimestamp(2, releaseTime);
                    pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                    pstmt.setInt(4, loanId);
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "预发布成功，发布时间" + DateTimeParser.format(releaseTime));
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public BidCheck[] searchCheck(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        ArrayList<T6241> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6241.F01, T6241.F02, T6241.F03, T6241.F04, T6241.F05, T6241.F06,T7110.F02 FROM S62.T6241 INNER JOIN S71.T7110 ON T6241.F03=T7110.F01 WHERE T6241.F02 = ? ORDER BY T6241.F04 DESC "))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        BidCheck record = new BidCheck();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = T6241_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.name = resultSet.getString(7);
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new BidCheck[list.size()]));
    }
    
    @Override
    public void delDyw(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6234 WHERE F01=?"))
                {
                    ps.setInt(1, id);
                    ps.execute();
                }
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6235 WHERE F02=?"))
                {
                    ps.setInt(1, id);
                    ps.execute();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void delDbxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6236 WHERE F01=?"))
            {
                ps.setInt(1, id);
                ps.execute();
            }
        }
    }
    
    @Override
    public void setPic(int loanId, String code, int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F21 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, code);
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                
                execute(connection, "UPDATE S62.T6233 SET F10 = ? WHERE F02 = ?", T6233_F10.F, loanId);
                
                execute(connection, "UPDATE S62.T6233 SET F10 = ? WHERE F01 = ?", T6233_F10.S, id);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void setPic(int loanId, String code)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6230 SET F21 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, code);
                pstmt.setInt(2, loanId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateBidStatus(int loanId, String reason, int userId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230_F20 t6230_F20 = null;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE F01=? FOR UPDATE"))
                {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            t6230_F20 = EnumParser.parse(T6230_F20.class, rs.getString(1));
                        }
                    }
                }
                T6230 t6230 = get(loanId);
                if (!(t6230_F20 == T6230_F20.SQZ || t6230_F20 == T6230_F20.DSH || t6230_F20 == T6230_F20.DFB))
                {
                    throw new ParameterException("不是申请中、待审核、待发布状态");
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20=?, F24 = ? WHERE F01=?"))
                {
                    ps.setString(1, T6230_F20.YZF.name());
                    ps.setTimestamp(2, getCurrentTimestamp(connection));
                    ps.setInt(3, loanId);
                    ps.executeUpdate();
                }
                try (PreparedStatement it =
                    connection.prepareStatement("INSERT INTO S62.T6241 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F05 = ?,F06 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    it.setInt(1, loanId);
                    it.setInt(2, userId);
                    it.setString(3, T6241_F05.YZF.name());
                    it.setString(4, reason);
                    it.execute();
                }
                
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                //审核后作废，需要回滚信用额度
                if (t6230_F20 == T6230_F20.DFB)
                {
                    // 回滚借款人信用额度
                    rollbackCreditLineGr(t6230);
                    boolean isHasGuarant =
                        BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                    //平台开启了申请担保方功能，则回滚担保方的担保额度，未开启则按原有逻辑处理（回滚信用额度）
                    if (!isHasGuarant)
                    {
                        //回滚机构信用额度
                        rollbackCreditLineJG(connection, t6230);
                    }
                    else
                    {
                        //还款时退还担保方的担保额度
                        rebackGuaranteeAmount(connection, t6230);
                    }
                }
                T6110 t6110 = selectT6110(connection, t6230.F02);
                
                //发站内信你
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("userName", t6110.F02);
                envionment.set("title", t6230.F03);
                envionment.set("reason", reason);
                String content = configureProvider.format(LetterVariable.LOAN_RELEASE_CANCEL, envionment);
                sendLetter(connection, t6230.F02, "借款申请已作废", content);
                writeLog(connection, "操作日志", "作废标的");
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void updateBidTJStatus(int loanId, T6231_F29 sftj, int userId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            T6230_F20 t6230_F20 = T6230_F20.TBZ;
            try (PreparedStatement ps = connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE F01=? "))
            {
                ps.setInt(1, loanId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        t6230_F20 = EnumParser.parse(T6230_F20.class, rs.getString(1));
                    }
                }
            }
            if (sftj.name().equals("S") && !(t6230_F20 == T6230_F20.TBZ || t6230_F20 == T6230_F20.YFB))
            {
                throw new LogicalException("只能推荐预发布和投资中的标的！");
            }
            else
            {
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S62.T6231 WHERE F01=? "))
                {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            try (PreparedStatement ps2 =
                                connection.prepareStatement("UPDATE S62.T6231 SET F29=?, F30=? WHERE F01=?"))
                            {
                                ps2.setString(1, sftj.name());
                                ps2.setTimestamp(2, sftj.name().equals("S") ? getCurrentTimestamp(connection) : null);
                                ps2.setInt(3, loanId);
                                ps2.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public BigDecimal getDbxxInfo(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        String sql =
            "SELECT T6116.F03 AS F09 FROM S62.T6236 INNER JOIN S61.T6116 ON T6236.F03 = T6116.F01 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1";
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        if (isHasGuarant)
        {
            sql =
                "SELECT T6125.F04 AS F01 FROM S62.T6236 INNER JOIN S61.T6125 ON T6236.F03 = T6125.F02 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1";
        }
        BigDecimal DbCreditAmount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        DbCreditAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return DbCreditAmount;
    }
    
    @Override
    public PagingResult<TbRecord> getTbRecords(int loanId, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F03, F05, F06 FROM S62.T6250 WHERE F02 = ? ORDER BY F06 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TbRecord>()
            {
                
                @Override
                public TbRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TbRecord> list = null;
                    while (resultSet.next())
                    {
                        TbRecord entity = new TbRecord();
                        entity.F03 = resultSet.getInt(1);
                        entity.F05 = resultSet.getBigDecimal(2);
                        entity.F06 = resultSet.getTimestamp(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(getInfo(entity));
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new TbRecord[list.size()]);
                }
            }, paging, sql.toString(), loanId);
        }
    }
    
    // 根据投资人id，查询投资人信息
    private TbRecord getInfo(TbRecord entity)
        throws SQLException
    {
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6141.F02,T6141.F07,T6110.F02,T6110.F04 FROM S61.T6141,S61.T6110 WHERE T6110.F01=T6141.F01 AND T6110.F01=?"))
            {
                ps.setInt(1, entity.F03);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        entity.name = resultSet.getString(1);
                        entity.idCard = resultSet.getString(2);
                        entity.userName = resultSet.getString(3);
                        entity.phone = resultSet.getString(4);
                    }
                }
                return entity;
            }
        }
        
    }
    
    @Override
    public T6280 selectT6280(int F01)
        throws Throwable
    {
        
        T6280 t6280 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6280 WHERE T6280.F01 = ?"))
            {
                ps.setInt(1, F01);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6280 = new T6280();
                        t6280.F01 = resultSet.getInt(1);
                        t6280.F02 = resultSet.getBigDecimal(2);
                        t6280.F03 = resultSet.getBigDecimal(3);
                        t6280.F04 = resultSet.getBigDecimal(4);
                        t6280.F05 = resultSet.getInt(5);
                        t6280.F06 = resultSet.getInt(6);
                        t6280.F07 = resultSet.getInt(7);
                        t6280.F08 = resultSet.getInt(8);
                        t6280.F09 = resultSet.getBigDecimal(9);
                        t6280.F10 = T6280_F10.parse(resultSet.getString(10));
                        t6280.F11 = resultSet.getTimestamp(11);
                    }
                }
            }
        }
        return t6280;
    }
    
    @Override
    public PagingResult<Bid> search(LoanExtendsQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder(SQL);
        List<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F27.F);
        searchParameter(sb, query, parameters);
        sb.append(" ORDER BY F45, T6230.F24 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public Bid searchAmount(LoanExtendsQuery query)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6230.F05),0) AS F01,IFNULL(SUM(T6230.F07),0) AS F02 FROM S62.T6230 "
                    + " INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 INNER JOIN S62.T6231 ON T6230.F01=T6231.F01 LEFT JOIN S62.T6211 ON T6230.F04=T6211.F01 LEFT JOIN S62.T6216 ON T6230.F32=T6216.F01 WHERE T6230.F27=? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(T6230_F27.F);
        // sql语句和查询参数处理
        searchParameter(sb, query, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Bid>()
            {
                @Override
                public Bid parse(ResultSet resultSet)
                    throws SQLException
                {
                    Bid count = new Bid();
                    if (resultSet.next())
                    {
                        count.F05 = resultSet.getBigDecimal(1);
                        count.F07 = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sb.toString(), parameters);
        }
    }
    
    private void searchParameter(StringBuilder sb, LoanExtendsQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6230.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            int typeId = query.getType();
            if (typeId > 0)
            {
                sb.append(" AND T6211.F01 = ?");
                parameters.add(typeId);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) <=?");
                parameters.add(timestamp);
            }
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            T6230_F20 status = query.getStatus();
            if (status != null)
            {
                sb.append(" AND T6230.F20 =?");
                parameters.add(status);
            }
            String bidNo = query.getBidNo();
            if (!StringHelper.isEmpty(bidNo))
            {
                sb.append(" AND T6230.F25 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(bidNo));
            }
            String userType = query.getUserType();
            if (!StringHelper.isEmpty(userType))
            {
                sb.append(" AND T6110.F06 = ?");
                parameters.add(userType);
            }
            int productId = query.getProductId();
            if (productId > 0)
            {
                sb.append(" AND T6216.F01 = ?");
                parameters.add(productId);
            }
            String bidFlag = query.getBidFlag();
            if (!StringHelper.isEmpty(bidFlag))
            {
                if ("wbz".equals(bidFlag))
                {
                    sb.append(" AND T6230.F28 = ? AND T6231.F27 = ? ");
                    parameters.add(T6230_F28.F.name());
                    parameters.add(T6231_F27.F.name());
                }
                else if ("xsb".equals(bidFlag))
                {
                    sb.append(" AND T6230.F28 = ? ");
                    parameters.add(T6230_F28.S.name());
                }
                else if ("jlb".equals(bidFlag))
                {
                    sb.append(" AND T6231.F27 = ? ");
                    parameters.add(T6231_F27.S.name());
                }
            }
            String bidAttr = query.getBidAttr();
            if (!StringHelper.isEmpty(bidAttr))
            {
                if ("dbb".equals(bidAttr))
                {
                    sb.append(" AND T6230.F11 = ? ");
                    parameters.add(T6230_F11.S.name());
                }
                if ("dyb".equals(bidAttr))
                {
                    sb.append(" AND T6230.F13 = ? ");
                    parameters.add(T6230_F13.S.name());
                }
                if ("sdb".equals(bidAttr))
                {
                    sb.append(" AND T6230.F14 = ? ");
                    parameters.add(T6230_F14.S.name());
                }
                if ("xyb".equals(bidAttr))
                {
                    sb.append(" AND T6230.F33 = ? ");
                    parameters.add(T6230_F33.S.name());
                }
            }
            T6231_F35 source = query.getSource();
            if (source != null)
            {
                sb.append(" AND T6231.F35 =?");
                parameters.add(source);
            }
        }
    }
    
    /**
     * 回滚借款人信用额度
     * @param t6230
     * @throws Throwable
     */
    private void rollbackCreditLineGr(T6230 t6230)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            BigDecimal creditAmount = BigDecimal.ZERO;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE F01 = ? FOR UPDATE"))
            {
                pstmt.setInt(1, t6230.F02);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        creditAmount = resultSet.getBigDecimal(1);
                    }
                    else
                    {
                        throw new LogicalException("借款人信用记录不存在");
                    }
                }
            }
            creditAmount = creditAmount.add(t6230.F05);
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, creditAmount);
                pstmt.setInt(2, t6230.F02);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = 0, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, t6230.F02);
                pstmt.setInt(2, FeeCode.XY_LB_FH);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(4, t6230.F05);
                pstmt.setBigDecimal(5, creditAmount);
                pstmt.setString(6, String.format("标的作废返还:%s", t6230.F25));
                pstmt.execute();
            }
        }
    }
    
    /**
     * 回滚机构信用额度
     * @param t6230
     * @throws Throwable
     */
    private void rebackGuaranteeAmount(Connection connection, T6230 t6230)
        throws Throwable
    {
        
        if (t6230.F11 == T6230_F11.S)
        {
            // 查询担保机构userId
            int dbUserId = 0;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1"))
            {
                pstmt.setInt(1, t6230.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dbUserId = resultSet.getInt(1);
                    }
                }
            }
            if (dbUserId > 0)
            {
                BigDecimal dbTotalAmount = new BigDecimal(0);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F04 FROM S61.T6125 WHERE T6125.F01 = (SELECT T1.F01 FROM S61.T6125 T1 WHERE T1.F02 = ? LIMIT 1) FOR UPDATE"))
                {
                    pstmt.setInt(1, dbUserId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            dbTotalAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6125 SET F04 = ? WHERE F02 = ?"))
                {
                    pstmt.setBigDecimal(1, dbTotalAmount.add(t6230.F05));
                    pstmt.setInt(2, dbUserId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
                {
                    pstmt.setInt(1, dbUserId);
                    pstmt.setInt(2, FeeCode.DB_LB_FH);
                    pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(4, t6230.F05);
                    pstmt.setBigDecimal(5, dbTotalAmount.add(t6230.F05));
                    pstmt.setString(6, "标的作废担保额度返还");
                    pstmt.execute();
                }
            }
        }
    }
    
    /**
     * 回滚担保方担保额度
     * @param t6230
     * @throws Throwable
     */
    private void rollbackCreditLineJG(Connection connection, T6230 t6230)
        throws Throwable
    {
        // 查询担保机构的信用额度
        BigDecimal dbtotalAmount = new BigDecimal(0);
        BigDecimal creditAmount = BigDecimal.ZERO;
        int dbUserId = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6116.F03,T6236.F03 FROM S62.T6236 INNER JOIN S61.T6116 ON T6236.F03 = T6116.F01 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1"))
        {
            pstmt.setInt(1, t6230.F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    dbtotalAmount = resultSet.getBigDecimal(1);
                    dbUserId = resultSet.getInt(2);
                }
            }
        }
        if (t6230.F11 == T6230_F11.S && dbUserId > 0)
        {
            creditAmount = dbtotalAmount.add(t6230.F05);
            // 更新机构的信用额度
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6116 SET F03 = F03 + ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, t6230.F05);
                pstmt.setInt(2, dbUserId);
                pstmt.execute();
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F06 = 0, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, dbUserId);
                pstmt.setInt(2, FeeCode.XY_HK_FH);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(4, t6230.F05);
                pstmt.setBigDecimal(5, creditAmount);
                pstmt.setString(6, String.format("标的作废返还:%s", t6230.F25));
                pstmt.execute();
            }
        }
    }
    
    @Override
    public List<Integer> addOrder(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("指定的记录不存在");
        }
        int orderId = 0;
        List<Integer> orderIds = new ArrayList<Integer>();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                T6252[] t6252s = selectAllT6252(loanId);
                if (t6252s == null)
                {
                    throw new LogicalException("没有找到还款记录");
                }
                //获取平台用户id
                int pid = getPTID(connection);
                //获取平台准备金账户
                T6101 dfrzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
                if (dfrzh == null)
                {
                    throw new LogicalException("垫付人账户不存在");
                }
                
                // 垫付总额
                BigDecimal amount = BigDecimal.ZERO;
                for (T6252 t6252 : t6252s)
                {
                    amount = amount.add(t6252.F07);
                }
                if (dfrzh.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("往来账户余额不足，不能进行垫付！");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S65.T6514 WHERE T6514.F02 = ?"))
                {
                    pstmt.setInt(1, loanId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            orderIds.add(resultSet.getInt(1));
                        }
                    }
                }
                if (orderIds.size() == 0)
                {
                    T6501 t6501 = null;
                    for (T6252 t6252 : t6252s)
                    {
                        if (t6252 == null)
                        {
                            continue;
                        }
                        if (orderIds.size() < t6252s.length)
                        {
                            /*try (PreparedStatement pstmt =
                                connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = CURRENT_TIMESTAMP(), F07 = ?, F08 = ?",
                                    PreparedStatement.RETURN_GENERATED_KEYS))
                            {
                                pstmt.setInt(1, OrderType.ADVANCE.orderType());
                                pstmt.setString(2, T6501_F03.DTJ.name());
                                pstmt.setString(3, T6501_F07.YH.name());
                                pstmt.setInt(4, pid);
                                pstmt.execute();
                                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                                {
                                    if (resultSet.next())
                                    {
                                        orderId = resultSet.getInt(1);
                                    }
                                }
                            }*/
                            t6501 = new T6501();
                            t6501.F02 = OrderType.ADVANCE.orderType();
                            t6501.F03 = T6501_F03.DTJ;
                            t6501.F04 = getCurrentTimestamp(connection);
                            t6501.F07 = T6501_F07.YH;
                            t6501.F08 = pid;
                            t6501.F13 = t6252.F07;
                            orderId = insertT6501(connection, t6501);
                            
                            try (PreparedStatement pstmt =
                                connection.prepareStatement("INSERT INTO S65.T6514 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
                            {
                                pstmt.setInt(1, orderId);
                                pstmt.setInt(2, loanId);
                                pstmt.setInt(3, t6252.F11);
                                pstmt.setInt(4, pid);
                                pstmt.setBigDecimal(5, t6252.F07);
                                pstmt.setInt(6, t6252.F05);
                                pstmt.execute();
                            }
                            orderIds.add(orderId);
                        }
                    }
                }
                serviceResource.commit(connection);
                return orderIds;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            
        }
    }
    
    /**
     * 查询还款记录
     *
     * @param loanId 标id
     * @return
     * @throws SQLException
     */
    protected T6252[] selectAllT6252(int loanId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            
            StringBuilder sb = new StringBuilder("SELECT F05, F07, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND F09=?");
            List<Object> parameters = new ArrayList<>();
            parameters.add(loanId);
            parameters.add(T6252_F09.WH);
            if (T5131_F02.BJ == selectT5131(connection))
            {
                sb.append(" AND F05=?");
                parameters.add(FeeCode.TZ_BJ);
            }
            sb.append(" ORDER BY T6252.F05 DESC");
            return selectAll(connection, new ArrayParser<T6252>()
            {
                
                @Override
                public T6252[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6252> list = null;
                    while (resultSet.next())
                    {
                        T6252 record = new T6252();
                        record.F05 = resultSet.getInt(1);
                        record.F07 = resultSet.getBigDecimal(2);
                        record.F11 = resultSet.getInt(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
                }
            }, sb.toString(), parameters);
        }
    }
    
    /**
     * 查询平台垫付类型
     */
    private T5131_F02 selectT5131(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5131 LIMIT 1"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return T5131_F02.parse(resultSet.getString(1));
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateBidDywDate(T6235 t6235)
        throws Throwable
    {
        if (t6235 == null)
        {
            return;
        }
        
        int count = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(1) FROM S62.T6235 where T6235.F05 =?"))
                {
                    pstmt.setInt(1, t6235.F05);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            count = resultSet.getInt(1);
                        }
                    }
                }
                
                if (count > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6235 SET T6235.F04 = ? where T6235.F05 =?"))
                    {
                        pstmt.setString(1, t6235.F04);
                        pstmt.setInt(2, t6235.F05);
                        pstmt.execute();
                    }
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S62.T6235 SET T6235.F04 = ?,T6235.F05 = ?"))
                    {
                        pstmt.setString(1, t6235.F04);
                        pstmt.setInt(2, t6235.F05);
                        pstmt.execute();
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            
        }
    }
    
    @Override
    public T6235 finBidDywDate(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6235 record = null;
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F04 FROM S62.T6235 WHERE T6235.F05 = ?"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6235();
                        record.F04 = resultSet.getString(1);
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 查看标动态管理
     * @param progresId
     * @return
     * @throws Throwable
     */
    @Override
    public T6248 viewBidProgres(int progresId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6248 t6248 = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6248.F04 AS F01, T6248.F05 AS F02, T6248.F06 AS F03, T6248.F07 AS F04, T6248.F08 AS F05, T6248.F09 AS F06, T7110.F02 AS F07 FROM S62.T6248 INNER JOIN S71.T7110 ON T6248.F02 = T7110.F01 WHERE T6248.F01 = ?"))
            {
                pstmt.setInt(1, progresId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6248 = new T6248();
                        t6248.F04 = resultSet.getString(1);
                        t6248.F05 = T6248_F05.parse(resultSet.getString(2));
                        t6248.F06 = resultSet.getString(3);
                        t6248.F07 = resultSet.getTimestamp(4);
                        t6248.F08 = resultSet.getTimestamp(5);
                        t6248.F09 = resultSet.getString(6);
                        t6248.sysName = resultSet.getString(7);
                    }
                }
            }
            return t6248;
        }
    }
    
    /**
     * 更新标动态管理
     * @param t6248
     * @throws Throwable
     */
    @Override
    public void updateBidProgres(T6248 t6248)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6230 t6230 = getT6230(connection, t6248.F03);
            if (!(t6230.F20 == T6230_F20.HKZ || t6230.F20 == T6230_F20.YJQ || t6230.F20 == T6230_F20.YDF || t6230.F20 == T6230_F20.DFZ))
            {
                throw new LogicalException("不是还款中、已结清、已垫付、垫付中状态");
            }
            String sql = "UPDATE S62.T6248 SET F04 = ?, F05 = ?, F06 = ?, F07 = NULL,F08 = ?, F09 = ?  WHERE F01 = ?";
            if (T6248_F05.YFB == t6248.F05)
            {
                sql = "UPDATE S62.T6248 SET F04 = ?, F05 = ?, F06 = ?, F07 = NOW(),F08 = ?, F09 = ?  WHERE F01 = ?";
            }
            execute(connection, sql, t6248.F04, t6248.F05, t6248.F06, t6248.F08, t6248.F09, t6248.F01);
        }
        
    }
    
    /**
     * 添加标动态管理
     * @param t6248
     * @throws Throwable
     */
    @Override
    public void addBidProgres(T6248 t6248)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6230 t6230 = getT6230(connection, t6248.F03);
            if (!(t6230.F20 == T6230_F20.HKZ || t6230.F20 == T6230_F20.YJQ || t6230.F20 == T6230_F20.YDF || t6230.F20 == T6230_F20.DFZ))
            {
                throw new LogicalException("不是还款中、已结清、已垫付、垫付中状态");
            }
            String sql = "INSERT INTO S62.T6248(F02,F03,F04,F05,F06,F08,F09) VALUES(?,?,?,?,?,?,?)";
            if (T6248_F05.YFB == t6248.F05)
            {
                sql = "INSERT INTO S62.T6248(F02,F03,F04,F05,F06,F07,F08,F09) VALUES(?,?,?,?,?,NOW(),?,?)";
            }
            execute(connection, sql, t6248.F02, t6248.F03, t6248.F04, t6248.F05, t6248.F06, t6248.F08, t6248.F09);
        }
    }
    
    /**
     * 删除标动态管理
     * @param loanId
     * @param progresId
     * @throws Throwable
     */
    @Override
    public void deleteBidProgres(int loanId, int progresId)
        throws Throwable
    {
        String sql = "DELETE FROM S62.T6248 WHERE F01 = ? AND F03 = ?";
        try (Connection connection = getConnection())
        {
            T6230 t6230 = getT6230(connection, loanId);
            if (!(t6230.F20 == T6230_F20.HKZ || t6230.F20 == T6230_F20.YJQ || t6230.F20 == T6230_F20.YDF || t6230.F20 == T6230_F20.DFZ))
            {
                throw new LogicalException("不是还款中、已结清、已垫付、垫付中状态");
            }
            execute(connection, sql, progresId, loanId);
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
                "SELECT T6248.F04 AS F01, T6248.F05 AS F02, T6248.F06 AS F03, T6248.F07 AS F04, T6248.F08 AS F05, T6248.F09 AS F06, T7110.F02 AS F07, T6248.F01 AS F08 FROM S62.T6248 INNER JOIN S71.T7110 ON T6248.F02 = T7110.F01 WHERE T6248.F03 = ? ORDER BY T6248.F07 DESC,T6248.F08 DESC ";
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
            }, paging, sql, loandId);
        }
    }
    
    private Bid selectBid(Connection connection, int F01)
        throws SQLException
    {
        Bid record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6230.F02 F01,T6230.F03 F02,T6110.F02 F03 FROM S62.T6230 JOIN S61.T6110 ON T6230.F02=T6110.F01 WHERE T6230.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new Bid();
                    record.F02 = resultSet.getInt(1);
                    record.F03 = resultSet.getString(2);
                    record.F37 = resultSet.getString(3);
                }
            }
        }
        return record;
    }
    
    @Override
    public T6211[] getT6211s()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = new ArrayList<>();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6211 ORDER BY F01 DESC "))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    @Override
    public String selectBidState(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6230_F20 t6230_F20 = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6230_F20 = EnumParser.parse(T6230_F20.class, resultSet.getString(1));
                    }
                }
            }
            return t6230_F20.name();
        }
    }
    
}
