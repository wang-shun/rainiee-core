/*
 * 文 件 名:  GyLoanManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.modules.bid.donation.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.dimeng.p2p.S51.enums.T5129_F03;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.entities.T6246;
import com.dimeng.p2p.S62.entities.T6247;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.S62.enums.T6242_F10;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.S62.enums.T6246_F07;
import com.dimeng.p2p.S62.enums.T6247_F05;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.Article;
import com.dimeng.p2p.repeater.donation.entity.Donation;
import com.dimeng.p2p.repeater.donation.entity.GyBidCheck;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.p2p.repeater.donation.query.DonationQuery;
import com.dimeng.p2p.repeater.donation.query.GyLoanQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class GyLoanManageImpl extends AbstractBidManage implements GyLoanManage
{
    
    public GyLoanManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int add(T6242 entity)
        throws Throwable
    {
        if (entity == null)
        {
            return 0;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S62.T6242 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = CURRENT_TIMESTAMP(), F16 = ?, F17 = ?, F18 = ?, F19 = ?, F20 = ?, F21 = ?, F22 = ?, F23 = ?, F24 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setInt(3, entity.F04);
                pstmt.setBigDecimal(4, entity.F05);
                pstmt.setBigDecimal(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F05);
                pstmt.setInt(7, entity.F08);
                pstmt.setInt(8, entity.F09);
                pstmt.setString(9, entity.F10 == null ? T6242_F10.S.name() : entity.F10.name());
                pstmt.setString(10, T6242_F11.SQZ.name());
                pstmt.setString(11, entity.F12);
                pstmt.setTimestamp(12, entity.F13);
                pstmt.setInt(13, entity.F14);
                pstmt.setTimestamp(14, entity.F16);
                pstmt.setTimestamp(15, entity.F17);
                pstmt.setTimestamp(16, entity.F18);
                pstmt.setTimestamp(17, entity.F19);
                pstmt.setTimestamp(18, entity.F20);
                pstmt.setString(19, getCrid(connection));
                pstmt.setString(20, entity.F22);
                pstmt.setInt(21, entity.F23);
                pstmt.setString(22, entity.F24);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        int id = resultSet.getInt(1);
                        return id;
                    }
                    return 0;
                }
            }
        }
    }
    
    // 根据标生成标的编号
    protected String getCrid(Connection connection)
        throws Throwable
    {
        // 序号
        String serNo = "";
        try (PreparedStatement pstmt =
            connection.prepareStatement(" SELECT MAX(F04) FROM S51.T5129 WHERE T5129.F02 = CURDATE() AND T5129.F03 = ? FOR UPDATE"))
        {
            pstmt.setString(1, T5129_F03.GYBDBH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    if (resultSet.getInt(1) >= 99)
                    {
                        throw new ParameterException("公益标每天不能超过99条");
                    }
                    serNo = String.format("%04d", resultSet.getInt(1) + 1);
                    try (PreparedStatement inpstmt =
                        connection.prepareStatement(" INSERT INTO S51.T5129 (F02, F03 , F04) VALUES (CURDATE(), ?, ?) ON DUPLICATE KEY UPDATE F04 = F04 + 1"))
                    {
                        inpstmt.setString(1, T5129_F03.GYBDBH.name());
                        inpstmt.setInt(2, resultSet.getInt(1) + 1);
                        inpstmt.execute();
                    }
                }
            }
        }
        
        // 获取当前时间
        String nowDate = DateParser.format(getCurrentDate(), "yyyyMMdd");
        //G201503101
        serNo = "G" + nowDate + serNo;
        
        return serNo;
    }
    
    @Override
    public int update(T6242 entity)
        throws Throwable
    {
        if (entity == null)
        {
            return 0;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE  S62.T6242 SET  F03 = ?,  F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F14 = ?, F15 = CURRENT_TIMESTAMP(),  F22 = ?, F24 = ? WHERE F01=?"))
            {
                pstmt.setString(1, entity.F03);
                pstmt.setBigDecimal(2, entity.F05);
                pstmt.setBigDecimal(3, entity.F06);
                pstmt.setBigDecimal(4, entity.F05);
                pstmt.setInt(5, entity.F08);
                pstmt.setInt(6, entity.F09);
                pstmt.setString(7, entity.F10 == null ? T6242_F10.S.name() : entity.F10.name());
                pstmt.setString(8, T6242_F11.SQZ.name());
                pstmt.setString(9, entity.F12);
                pstmt.setInt(10, entity.F14);
                pstmt.setString(11, entity.F22);
                pstmt.setString(12, entity.F24);
                pstmt.setInt(13, entity.F01);
                return pstmt.executeUpdate();
            }
        }
    }
    
    @Override
    public void delete(T6242 t6242)
        throws Throwable
    {
        
    }
    
    @Override
    public T6242 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            T6242 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, T6242.F15, "
                    + "T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21, T6242.F22, T6242.F23, T6242.F24"
                    + "  FROM S62.T6242  WHERE T6242.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6242();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = T6242_F10.parse(resultSet.getString(10));
                        record.F11 = T6242_F11.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getTimestamp(15);
                        record.F16 = resultSet.getTimestamp(16);
                        record.F17 = resultSet.getTimestamp(17);
                        record.F18 = resultSet.getTimestamp(18);
                        record.F19 = resultSet.getTimestamp(19);
                        record.F20 = resultSet.getTimestamp(20);
                        record.F21 = resultSet.getString(21);
                        record.F22 = resultSet.getString(22);
                        record.F23 = resultSet.getInt(23);
                        record.F24 = resultSet.getString(24);
                        
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public T6243 getT6243(int id)
        throws Throwable
    {
        
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            T6243 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6243.F01, T6243.F02"
                    + "  FROM S62.T6243 INNER JOIN S62.T6242 ON T6243.F01=T6242.F01  WHERE T6243.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6243();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                    }
                }
            }
            return record;
        }
    }
    
    protected static final ArrayParser<GyLoan> ARRAY_PARSER_T6242 = new ArrayParser<GyLoan>()
    {
        
        @Override
        public GyLoan[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<GyLoan> bids = new ArrayList<GyLoan>();
            Calendar calendar = Calendar.getInstance();
            GyLoan gyLoan = null;
            T6242 record = null;
            while (resultSet.next())
            {
                record = new T6242();
                gyLoan = new GyLoan();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getString(3);
                record.F04 = resultSet.getInt(4);
                record.F05 = resultSet.getBigDecimal(5);
                record.F06 = resultSet.getBigDecimal(6);
                record.F07 = resultSet.getBigDecimal(7);
                record.F08 = resultSet.getInt(8);
                record.F09 = resultSet.getInt(9);
                record.F10 = T6242_F10.parse(resultSet.getString(10));
                record.F11 = T6242_F11.parse(resultSet.getString(11));
                record.F12 = resultSet.getString(12);
                record.F13 = resultSet.getTimestamp(13);
                record.F14 = resultSet.getInt(14);
                record.F15 = resultSet.getTimestamp(15);
                record.F16 = resultSet.getTimestamp(16);
                record.F17 = resultSet.getTimestamp(17);
                record.F18 = resultSet.getTimestamp(18);
                record.F19 = resultSet.getTimestamp(19);
                record.F20 = resultSet.getTimestamp(20);
                record.F21 = resultSet.getString(21);
                record.F22 = resultSet.getString(22);
                record.F23 = resultSet.getInt(23);
                record.F24 = resultSet.getString(24);
                gyLoan.perCent = (record.F05.doubleValue() - record.F07.doubleValue()) / record.F05.doubleValue();
                gyLoan.perCentFormat = Formater.formatProgress(gyLoan.perCent);
                if (!StringHelper.isEmpty(resultSet.getString(25)))
                {
                    T6243 t6243 = new T6243();
                    t6243.F01 = resultSet.getInt(1);
                    t6243.F02 = resultSet.getString(25);
                    gyLoan.t6243 = t6243;
                }
                gyLoan.t6242 = record;
                boolean isTimeEnd = false;
                if (record.F19 == null)
                {
                    if (null != record.F13)
                    {
                        calendar.setTime(record.F13);
                        calendar.add(Calendar.DAY_OF_MONTH, record.F08 - 1);
                        if (Calendar.getInstance().getTime().after(calendar.getTime()))
                        {
                            isTimeEnd = true;
                        }
                    }
                }
                else
                {
                    isTimeEnd = true;
                }
                gyLoan.isTimeEnd = isTimeEnd;
                bids.add(gyLoan);
            }
            return (bids.size() == 0) ? null : bids.toArray(new GyLoan[bids.size()]);
        }
    };
    
    @Override
    public PagingResult<GyLoan> search(GyLoanQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, "
                    + "T6242.F15, T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21,T6242.F22 , T6242.F23,T6242.F24,T6243.F02 AS F25"
                    + "  FROM S62.T6242 LEFT JOIN S62.T6243 ON T6242.F01=T6243.F01  WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        searchParameter(sb, query, parameters);
        sb.append(" ORDER BY T6242.F15 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER_T6242, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public T6242 searchAmoun(GyLoanQuery query)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder("SELECT IFNULL(SUM(T6242.F05),0) AS F01,IFNULL(SUM(T6242.F07),0) AS F02 FROM S62.T6242 "
                + " LEFT JOIN S62.T6243 ON T6242.F01=T6243.F01 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        searchParameter(sb, query, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6242>()
            {
                @Override
                public T6242 parse(ResultSet resultSet)
                    throws SQLException
                {
                    T6242 count = new T6242();
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
    
    private void searchParameter(StringBuilder sb, GyLoanQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6242.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F16) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F16) <=?");
                parameters.add(timestamp);
            }
            String no = query.getBidNo();
            if (!StringHelper.isEmpty(no))
            {
                sb.append(" AND T6242.F21 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(no));
            }
            
            String gyName = query.getGyName();
            if (!StringHelper.isEmpty(gyName))
            {
                sb.append(" AND T6242.F22 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(gyName));
            }
            T6242_F11 status = query.getStatus();
            if (status != null)
            {
                sb.append(" AND T6242.F11 =?");
                parameters.add(status);
            }
            //            else
            //            {
            //                sb.append(" AND T6242.F11 <>?");
            //                parameters.add(T6242_F11.YZF);
            //            }
        }
    }
    
    @Override
    public PagingResult<GyLoan> search4front(GyLoanQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, "
                    + "T6242.F15, T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21,T6242.F22 , T6242.F23,T6242.F24,T6243.F02 AS F25"
                    + "  FROM S62.T6242 LEFT JOIN S62.T6243 ON T6242.F01=T6243.F01  WHERE 1=1 AND (DATE(DATE_ADD(T6242.F13, interval T6242.F08 day)) >= CURRENT_DATE() || T6242.F05 <> T6242.F07) ");
        List<Object> parameters = new ArrayList<>();
        
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6242.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F15) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F15) <=?");
                parameters.add(timestamp);
            }
            String no = query.getBidNo();
            if (!StringHelper.isEmpty(no))
            {
                sb.append(" AND T6242.F21 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(no));
            }
            
            String gyName = query.getGyName();
            if (!StringHelper.isEmpty(gyName))
            {
                sb.append(" AND T6242.F22 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(gyName));
            }
        }
        //前台查询“捐款中和捐助完成的公益标”
        sb.append(" AND T6242.F11 in ('" + T6242_F11.JKZ + "','" + T6242_F11.YJZ + "')");
        sb.append(" ORDER BY T6242.F11,T6242.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER_T6242, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public void addT6243(T6243 entity, Article article)
        throws Throwable
    {
        if (entity == null || entity.F02 == null)
        {
            throw new LogicalException("请填写完整信息");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO  S62.T6243 SET F01 = ?, F02 = ?"))
            {
                pstmt.setInt(1, entity.F01);
                pstmt.setString(2, entity.F02);
                
                pstmt.execute();
                
            }
        }
    }
    
    @Override
    public void addT6246(T6246 entity)
        throws Throwable
    {
        if (entity == null || entity.F01 <= 0)
        {
            throw new LogicalException("请填写完整信息");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO  S62.T6246 SET  F02 = ?,F03 = ?, F04 = ?,F05 = ?, F06 = CURRENT_TIMESTAMP(), F07 = ?"))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setBigDecimal(3, entity.F04);
                pstmt.setBigDecimal(4, entity.F05);
                pstmt.setString(5, entity.F07 == null ? T6246_F07.F.name() : entity.F07.name());
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void addT6247(T6247 entity)
        throws Throwable
    {
        if (entity == null || entity.F02 <= 0)
        {
            throw new LogicalException("请填写完整信息,公益标不存在");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO  S62.T6247 SET  F02 = ?,F03 = ?, F04 = CURRENT_TIMESTAMP(),F05 = ?, F06 = ?"))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setString(3, entity.F05 == null ? T6247_F05.WCL.name() : entity.F05.name());
                pstmt.setString(4, entity.F06);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateT6243(T6243 entity, Article article)
        throws Throwable
    {
        //String imageCode = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = article.getImage();
        if (uploadFile != null)
        {// 保存封面图片
         //imageCode = fileStore.upload(FileType.GYLOAN_CYS_FILE.ordinal(), uploadFile)[0];
            fileStore.upload(FileType.GYLOAN_CYS_FILE.ordinal(), uploadFile);
        }
        
        if (entity == null || entity.F02 == null)
        {
            throw new LogicalException("请填写完整信息");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6243 SET  F02 = ? WHERE F01=? "))
            {
                pstmt.setString(1, entity.F02);
                pstmt.setInt(2, entity.F01);
                pstmt.execute();
                
            }
        }
    }
    
    protected void markYCL(Connection connection, int bidId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6247 SET F05 = 'YCL' WHERE F02 = ? AND F05 = 'WCL'"))
        {
            pstmt.setInt(1, bidId);
            pstmt.execute();
        }
    }
    
    @Override
    public GyLoan getInfo(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        GyLoan gyLoan = null;
        try (Connection connection = getConnection())
        {
            
            T6242 record = null;
            T6243 t6243 = null;
            // T6244 t6244 = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, "
                    + "T6242.F15, T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21, T6242.F22, T6242.F23, T6242.F24,T6243.F02"
                    + "  FROM S62.T6242 LEFT JOIN S62.T6243 ON T6242.F01=T6243.F01  WHERE T6242.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        gyLoan = new GyLoan();
                        record = new T6242();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = T6242_F10.parse(resultSet.getString(10));
                        record.F11 = T6242_F11.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getTimestamp(15);
                        record.F16 = resultSet.getTimestamp(16);
                        record.F17 = resultSet.getTimestamp(17);
                        record.F18 = resultSet.getTimestamp(18);
                        record.F19 = resultSet.getTimestamp(19);
                        record.F20 = resultSet.getTimestamp(20);
                        record.F21 = resultSet.getString(21);
                        record.F22 = resultSet.getString(22);
                        record.F23 = resultSet.getInt(23);
                        record.F24 = resultSet.getString(24);
                        if (!StringHelper.isEmpty(resultSet.getString(25)))
                        {
                            t6243 = new T6243();
                            t6243.F01 = resultSet.getInt(1);
                            t6243.F02 = resultSet.getString(25);
                        }
                        
                        //                        if(!StringHelper.isEmpty(resultSet.getString(22)))
                        //                        {
                        //                            t6244 = new T6244();
                        //                            t6243.F01=resultSet.getInt(1);
                        //                            t6243.F02=resultSet.getString(22);
                        //                        }
                        gyLoan.t6242 = record;
                        gyLoan.t6243 = t6243;
                        gyLoan.t6247s = getT6247s(connection, id);
                        
                    }
                }
            }
            return gyLoan;
        }
    }
    
    private List<T6247> getT6247s(Connection connection, int bid)
        throws Throwable
    {
        ArrayList<T6247> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6247.F01, T6247.F02, T6247.F03, T6247.F04,T6247.F05,T6247.F06,T7110.F02  FROM S62.T6247 INNER JOIN S71.T7110 ON  T6247.F03 = T7110.F01 WHERE T6247.F02 = ? GROUP BY T6247.F04 DESC"))
        {
            pstmt.setInt(1, bid);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6247 record = new T6247();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = T6247_F05.parse(resultSet.getString(5));
                    record.F06 = resultSet.getString(5);
                    record.sysName = resultSet.getString(7);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;//((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
        
    }
    
    @Override
    public PagingResult<Donation> searchTbjl(DonationQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6246.F01, T6246.F02, T6246.F03, T6246.F04, T6246.F05, T6246.F06, T6246.F07,T6110.F02 AS F08,"
                    + " T6242.F02 AS F09,T6242.F03 AS F10,T6242.F04 AS F11,T6242.F05 AS F12,T6242.F06 AS F13,T6242.F07 AS F14,"
                    + " T6242.F08 AS F15,T6242.F11 AS F16,T6242.F15 AS F17,T6242.F16 AS F18,T6242.F17 AS F19,T6242.F19 AS F20,"
                    + " T6242.F21 AS F21,T6242.F22 AS F22,T6242.F23 AS F23,T6242.F24 AS F24"
                    + "  FROM S62.T6246 INNER JOIN S61.T6110 ON T6246.F03=T6110.F01 INNER JOIN S62.T6242 ON T6246.F02=T6242.F01  WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        
        //        if (query != null)
        //        {
        //            String title = query.getLoanTitle();
        //            if (!StringHelper.isEmpty(title))
        //            {
        //                sb.append(" AND T6242.F03 LIKE ?");
        //                parameters.add(getSQLConnectionProvider().allMatch(title));
        //            }
        
        Timestamp timestamp = query.getCreateTimeStart();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T6246.F06) >=?");
            parameters.add(timestamp);
        }
        timestamp = query.getCreateTimeEnd();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T6246.F06) <=?");
            parameters.add(timestamp);
        }
        int userId = query.getUserId();
        if (userId > 0)
        {
            sb.append(" AND T6246.F03 = ?");
            parameters.add(userId);
        }
        //根据bid查询捐助记录
        int bid = query.getBidId();
        if (bid > 0)
        {
            sb.append(" AND T6246.F02 = ?");
            parameters.add(bid);
        }
        //  T6242_F11 status = query.getStatus();
        //            if (status != null)
        //            {
        //                sb.append(" AND T6242_F11 =?");
        //                parameters.add(status);
        //            }
        //}
        sb.append(" ORDER BY T6246.F06 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Donation>()
            {
                
                @Override
                public Donation[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Donation> bids = new ArrayList<Donation>();
                    Donation record = null;
                    T6242 t6242 = null;
                    while (resultSet.next())
                    {
                        record = new Donation();
                        t6242 = new T6242();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6246_F07.parse(resultSet.getString(7));
                        record.userName = resultSet.getString(8);
                        t6242.F02 = resultSet.getInt(9);
                        t6242.F03 = resultSet.getString(10);
                        t6242.F04 = resultSet.getInt(11);
                        t6242.F05 = resultSet.getBigDecimal(12);
                        t6242.F06 = resultSet.getBigDecimal(13);
                        t6242.F07 = resultSet.getBigDecimal(14);
                        t6242.F08 = resultSet.getInt(15);
                        t6242.F11 = T6242_F11.parse(resultSet.getString(16));
                        t6242.F15 = resultSet.getTimestamp(17);
                        t6242.F16 = resultSet.getTimestamp(18);
                        t6242.F17 = resultSet.getTimestamp(19);
                        t6242.F19 = resultSet.getTimestamp(20);
                        t6242.F21 = resultSet.getString(21);
                        t6242.F22 = resultSet.getString(22);
                        t6242.F23 = resultSet.getInt(23);
                        t6242.F24 = resultSet.getString(24);
                        record.t6242 = t6242;
                        bids.add(record);
                    }
                    return bids.toArray(new Donation[bids.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public int getPTID()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return getPTID(connection);
        }
    }
    
    @Override
    public T6211[] getBidType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = new ArrayList<>();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?"))
            {
                pstmt.setString(1, T6211_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if (resultSet.getString(2).contains("公益") || resultSet.getString(2).contains("捐赠"))
                        {
                            T6211 record = new T6211();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getString(2);
                            list.add(record);
                        }
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    @Override
    public GyBidCheck[] getGyBidCheck(int bid)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<GyBidCheck> list = new ArrayList<GyBidCheck>();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6247.F01, T6247.F02,T6247.F03,T6247.F04,T6247.F05,T6247.F06,T7110.F02 FROM S62.T6247 INNER JOIN S71.T7110 ON T6247.F03=T7110.F01 WHERE T6247.F02 = ? ORDER BY T6247.F04 DESC "))
            {
                pstmt.setInt(1, bid);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        
                        GyBidCheck record = new GyBidCheck();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = T6247_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.name = resultSet.getString(7);
                        list.add(record);
                        
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new GyBidCheck[list.size()]));
        }
    }
    
    @Override
    public void CheckGyBid(int bid, T6242_F11 status, T6242_F11 oldStatus)
        throws Throwable
    {
        if (bid <= 0)
        {
            throw new LogicalException("标ID参数错误");
        }
        this.logger.info("公益标ID=" + bid + ",本次操作状态为=" + status.name() + ",原状态为=" + oldStatus.name() + ",操作人ID="
            + serviceResource.getSession().getAccountId());
        //如果作废标,已捐助或捐助中的标不能作废
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (status == T6242_F11.YZF)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6242 SET  F11 = ?,F16=CURRENT_TIMESTAMP() WHERE F01=? AND F11 NOT IN(?,?) "))
                    {
                        pstmt.setString(1, status.name());
                        pstmt.setInt(2, bid);
                        pstmt.setString(3, T6242_F11.JKZ.name());
                        pstmt.setString(4, T6242_F11.YJZ.name());
                        pstmt.execute();
                        
                    }
                }
                else if (status == T6242_F11.JKZ)//如果从待发布改成捐款中,要修改发布时间
                {
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6242 SET  F11 = ?,F13=CURRENT_TIMESTAMP() WHERE F01=? AND F11=? "))
                    {
                        pstmt.setString(1, status.name());
                        pstmt.setInt(2, bid);
                        pstmt.setString(3, oldStatus.name());
                        pstmt.execute();
                    }
                }
                else
                {
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6242 SET  F11 = ?,F16=CURRENT_TIMESTAMP() WHERE F01=? AND F11=? "))
                    {
                        pstmt.setString(1, status.name());
                        pstmt.setInt(2, bid);
                        pstmt.setString(3, oldStatus.name());
                        pstmt.execute();
                    }
                    //如果标审核算通过,添加协议关系
                    if (status == T6242_F11.DFB)
                    {
                        //公益标协议类型:
                        int xyTypeId = 5001;
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
                            connection.prepareStatement("INSERT INTO S62.T6244 (F01, F02, F03) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03)"))
                        {
                            pstmt.setInt(1, bid);
                            pstmt.setInt(2, xyTypeId);
                            pstmt.setInt(3, version);
                            pstmt.execute();
                        }
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Throwable e)
            {
                serviceResource.error("GyLoanManageImpl.CheckGyBid", e);
                serviceResource.rollback(connection);
                throw new Exception("GyLoanManageImpl.CheckGyBid Exception");
            }
        }
    }
    
    @Override
    public GyLoanStatis gyLoanStatistics(int bid)
        throws Throwable
    {
        
        this.logger.info("公益标ID=" + bid);
        GyLoanStatis statis = new GyLoanStatis();
        //如果不传ID
        try (Connection connection = getConnection())
        {
            if (bid <= 0)
            {
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT SUM(F05) FROM S62.T6242 WHERE T6242.F11 IN (?,?) "))
                {
                    pstmt.setString(1, T6242_F11.JKZ.name());
                    pstmt.setString(2, T6242_F11.YJZ.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.totalAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(DISTINCT(F03)) FROM S62.T6246 "))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.totalNum = resultSet.getInt(1);
                        }
                    }
                }
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(F01) FROM S62.T6246"))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.donationsNum = resultSet.getInt(1);
                        }
                    }
                }
                
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(F04) FROM S62.T6246"))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.donationsAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                
            }
            else
            {
                //如果传ID
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT SUM(F05) FROM S62.T6242 WHERE T6242.F11 IN (?,?) "))
                {
                    pstmt.setString(1, T6242_F11.JKZ.name());
                    pstmt.setString(2, T6242_F11.YJZ.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.totalAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(DISTINCT(F03)) FROM S62.T6246 WHERE F02=? "))
                {
                    pstmt.setInt(1, bid);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.totalNum = resultSet.getInt(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(F01) FROM S62.T6246 WHERE F02=?"))
                {
                    pstmt.setInt(1, bid);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.donationsNum = resultSet.getInt(1);
                        }
                    }
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT SUM(F04) FROM S62.T6246 WHERE F02=?"))
                {
                    pstmt.setInt(1, bid);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            statis.donationsAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
            }
            return statis;
        }
    }
    
    @Override
    public void exportGyLoan(GyLoan[] loans, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (loans == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("公益ID");
            writer.write("公益标题");
            writer.write("公益方");
            writer.write("筹款金额(元)");
            writer.write("已筹金额(元)");
            writer.write("起筹金额(元)");
            writer.write("筹款期限(天)");
            writer.write("处理时间");
            writer.write("状态");
            writer.newLine();
            int index = 1;
            for (GyLoan gyLoan : loans)
            {
                writer.write(index++);
                writer.write(gyLoan.t6242.F21);
                writer.write(StringHelper.isEmpty(gyLoan.t6242.F03) ? "" : gyLoan.t6242.F03);
                writer.write(StringHelper.isEmpty(gyLoan.t6242.F22) ? "" : gyLoan.t6242.F22);
                writer.write(Formater.formatAmount(gyLoan.t6242.F05));
                writer.write(Formater.formatAmount(gyLoan.t6242.F05.subtract(gyLoan.t6242.F07)));
                writer.write(Formater.formatAmount(gyLoan.t6242.F06));
                writer.write(gyLoan.t6242.F08);
                writer.write(DateParser.format(gyLoan.t6242.F16, "yyyy-MM-dd HH:mm:ss"));
                writer.write(gyLoan.t6242.F11.getChineseName());
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportGyLoanProgers(GyLoan[] loans, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (loans == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("公益ID");
            writer.write("公益标题");
            writer.write("公益方");
            writer.write("筹款金额(元)");
            writer.write("捐款完成时间");
            writer.write("进展数");
            writer.write("创建人");
            writer.write("创建时间");
            writer.newLine();
            int index = 1;
            for (GyLoan gyLoan : loans)
            {
                writer.write(index++);
                writer.write(gyLoan.t6242.F21);
                writer.write(StringHelper.isEmpty(gyLoan.t6242.F03) ? "" : gyLoan.t6242.F03);
                writer.write(StringHelper.isEmpty(gyLoan.t6242.F22) ? "" : gyLoan.t6242.F22);
                writer.write(Formater.formatAmount(gyLoan.t6242.F05));
                writer.write("\t" + DateParser.format(gyLoan.t6242.F19, "yyyy-MM-dd HH:mm:ss"));
                writer.write(gyLoan.progres);
                writer.write(gyLoan.sysName);
                writer.write("\t" + DateParser.format(gyLoan.t6242.F15, "yyyy-MM-dd HH:mm:ss"));
                writer.newLine();
            }
        }
    }
    
    @Override
    public GyLoanStatis gyLoanStatisticsByUid(int userId)
        throws Throwable
    {
        GyLoanStatis statis = new GyLoanStatis();
        //如果不传ID
        try (Connection connection = getConnection())
        {
            //如果传ID
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F05) FROM S62.T6242 WHERE T6242.F11 IN (?,?) "))
            {
                pstmt.setString(1, T6242_F11.JKZ.name());
                pstmt.setString(2, T6242_F11.YJZ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        statis.totalAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(DISTINCT(F03)) FROM S62.T6246  "))
            {
                //  pstmt.setInt(1, bid);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        statis.totalNum = resultSet.getInt(1);
                    }
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(F01) FROM S62.T6246 WHERE F03=?"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        statis.donationsNum = resultSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(F04) FROM S62.T6246 WHERE F03=?"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        statis.donationsAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(DISTINCT(F02)) FROM S62.T6246 WHERE F03=? "))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        statis.totalDonBs = resultSet.getInt(1);
                    }
                }
            }
        }
        return statis;
    }
    
    @Override
    public Dzxy getBidContent(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5126.F03,T5125.F03 AS f03 FROM S62.T6244,S51.T5126,S51.T5125 WHERE T6244.F02 = T5126.F01 AND T6244.F03 = T5126.F02 AND T5126.F01 = T5125.F01 AND T6244.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Dzxy dzxy = null;
                    if (resultSet.next())
                    {
                        dzxy = new Dzxy();
                        dzxy.content = resultSet.getString(1);
                        dzxy.xymc = resultSet.getString(2);
                    }
                    return dzxy;
                }
            }
        }
    }
    
}
