package com.dimeng.p2p.modules.base.console.service.achieve;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5016_F12;
import com.dimeng.p2p.S50.enums.T5021_F07;
import com.dimeng.p2p.common.enums.AdvertisementStatus;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpsc;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord;
import com.dimeng.p2p.modules.base.console.service.entity.Advertisement;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementContent;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementType;
import com.dimeng.p2p.modules.base.console.service.query.AdvertisementRecordQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

/**
 * 后台广告管理实现类
 * @author  XiaoLang 2014 © dimeng.net 
 * @version v3.0
 * @LastModified 
 * 		<p>Modified 由JS-DatePicker,minDateTime#bug 时间点前后判定(服务端(addForType#M, updateForType#M)增加校验),by XiaoLang 2014年12月8日</p>
 */
public class AdvertisementManageImpl extends AbstractInformationService implements AdvertisementManage
{
    
    public static class AdvertisementManageFactory implements ServiceFactory<AdvertisementManage>
    {
        
        @Override
        public AdvertisementManage newInstance(ServiceResource serviceResource)
        {
            return new AdvertisementManageImpl(serviceResource);
        }
        
    }
    
    protected static final ArrayParser<AdvertisementRecord> ARRAY_PARSER = new ArrayParser<AdvertisementRecord>()
    {
        
        @Override
        public AdvertisementRecord[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<AdvertisementRecord> list = null;
            while (resultSet.next())
            {
                AdvertisementRecord record = new AdvertisementRecord();
                record.id = resultSet.getInt(1);
                record.sortIndex = resultSet.getInt(2);
                record.title = resultSet.getString(3);
                record.url = resultSet.getString(4);
                record.imageCode = resultSet.getString(5);
                record.publisherId = resultSet.getInt(6);
                record.showTime = resultSet.getTimestamp(7);
                record.unshowTime = resultSet.getTimestamp(8);
                record.createTime = resultSet.getTimestamp(9);
                record.updateTime = resultSet.getTimestamp(10);
                record.publisherName = resultSet.getString(11);
                Timestamp currentTime = resultSet.getTimestamp(12);
                if (currentTime.getTime() < record.showTime.getTime())
                {
                    record.status = AdvertisementStatus.DSJ;
                }
                else if (currentTime.getTime() < record.unshowTime.getTime())
                {
                    record.status = AdvertisementStatus.YSJ;
                }
                else
                {
                    record.status = AdvertisementStatus.YXJ;
                }
                record.advType = T5016_F12.parse(resultSet.getString(13)) != null ? T5016_F12.parse(resultSet.getString(13)).getChineseName() : "";
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(record);
            }
            return list == null || list.size() == 0 ? null : list.toArray(new AdvertisementRecord[list.size()]);
        }
    };
    
    protected static final ItemParser<AdvertisementRecord> ITEM_PARSER = new ItemParser<AdvertisementRecord>()
    {
        
        @Override
        public AdvertisementRecord parse(ResultSet resultSet)
            throws SQLException
        {
            AdvertisementRecord record = null;
            if (resultSet.next())
            {
                record = new AdvertisementRecord();
                record.id = resultSet.getInt(1);
                record.sortIndex = resultSet.getInt(2);
                record.title = resultSet.getString(3);
                record.url = resultSet.getString(4);
                record.imageCode = resultSet.getString(5);
                record.publisherId = resultSet.getInt(6);
                record.showTime = resultSet.getTimestamp(7);
                record.unshowTime = resultSet.getTimestamp(8);
                record.createTime = resultSet.getTimestamp(9);
                record.updateTime = resultSet.getTimestamp(10);
                record.publisherName = resultSet.getString(11);
                Timestamp currentTime = resultSet.getTimestamp(12);
                if (currentTime.getTime() < record.showTime.getTime())
                {
                    record.status = AdvertisementStatus.DSJ;
                }
                else if (currentTime.getTime() < record.unshowTime.getTime())
                {
                    record.status = AdvertisementStatus.YSJ;
                }
                else
                {
                    record.status = AdvertisementStatus.YXJ;
                }
            }
            return record;
        }
        
    };
    
    protected static final ItemParser<AdvertisementRecord> ITEM_PARSER_FOR_TYPE = new ItemParser<AdvertisementRecord>()
    {
        
        @Override
        public AdvertisementRecord parse(ResultSet resultSet)
            throws SQLException
        {
            AdvertisementRecord record = null;
            if (resultSet.next())
            {
                record = new AdvertisementRecord();
                record.id = resultSet.getInt(1);
                record.sortIndex = resultSet.getInt(2);
                record.title = resultSet.getString(3);
                record.url = resultSet.getString(4);
                record.imageCode = resultSet.getString(5);
                record.publisherId = resultSet.getInt(6);
                record.showTime = resultSet.getTimestamp(7);
                record.unshowTime = resultSet.getTimestamp(8);
                record.createTime = resultSet.getTimestamp(9);
                record.updateTime = resultSet.getTimestamp(10);
                record.publisherName = resultSet.getString(11);
                Timestamp currentTime = resultSet.getTimestamp(12);
                if (currentTime.getTime() < record.showTime.getTime())
                {
                    record.status = AdvertisementStatus.DSJ;
                }
                else if (currentTime.getTime() < record.unshowTime.getTime())
                {
                    record.status = AdvertisementStatus.YSJ;
                }
                else
                {
                    record.status = AdvertisementStatus.YXJ;
                }
                record.advType = resultSet.getString(13);
            }
            return record;
        }
        
    };
    
    protected static final String SELECT_ALL_SQL =
        "SELECT T5016.F01,T5016.F02,T5016.F03,T5016.F04,T5016.F05,T5016.F06,T5016.F07,T5016.F08,T5016.F09,T5016.F10,T7110.F04 AS F11,CURRENT_TIMESTAMP() AS F12,T5016.F12 AS F13 FROM S50.T5016 INNER JOIN S71.T7110 ON T5016.F06 = T7110.F01";
    
    protected static final String SELECT_ALL_SQL_FOR_TYPE =
        "SELECT T5016.F01,T5016.F02,T5016.F03,T5016.F04,T5016.F05,T5016.F06,T5016.F07,T5016.F08,T5016.F09,T5016.F10,T7110.F04 AS F11,CURRENT_TIMESTAMP() AS F12, T5016.F12 AS F13 FROM S50.T5016 INNER JOIN S71.T7110 ON T5016.F06 = T7110.F01";
    
    public AdvertisementManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<AdvertisementRecord> search(AdvertisementRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuffer sql = new StringBuffer(SELECT_ALL_SQL);
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE 1=1 AND T5016.F12 NOT IN ('IOSPIC','ANDROIDPIC','FINDPIC') ");
        if (query != null)
        {
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            String publisherName = query.getPublisherName();
            if (!StringHelper.isEmpty(publisherName))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(connectionProvider.allMatch(publisherName));
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5016.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5016.F09) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5016.F10) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5016.F10) <= ?");
                parameters.add(timestamp);
            }
            String title = query.getTitle();
            if (!StringHelper.isEmpty(title))
            {
                sql.append(" AND T5016.F03 LIKE ?");
                parameters.add(connectionProvider.allMatch(title));
            }
            AdvertisementStatus status = query.getStatus();
            if (status != null)
            {
                if (AdvertisementStatus.DSJ.equals(status))
                {
                    sql.append(" CURRENT_TIMESTAMP()<T5016.F07");
                }
                else if (AdvertisementStatus.YSJ.equals(status))
                {
                    sql.append(" CURRENT_TIMESTAMP()<T5016.F08");
                }
                else if (AdvertisementStatus.YXJ.equals(status))
                {
                    sql.append(" CURRENT_TIMESTAMP()>T5016.F08");
                }
            }
            String advType = query.getAdvType();
            if (!StringHelper.isEmpty(advType))
            {
                sql.append(" AND T5016.F12 = ?");
                parameters.add(advType);
            }
        }
        try (Connection connection = getConnection())
        {
            
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString()
                + " ORDER BY T5016.F02 DESC,T5016.F10 DESC", parameters);
        }
    }
    
    @Override
    public AdvertisementRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5016.F01 = ?", id);
        }
    }
    
    @Override
    public AdvertisementRecord getForType(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER_FOR_TYPE, SELECT_ALL_SQL_FOR_TYPE + " WHERE T5016.F01 = ?", id);
        }
    }
    
    @Override
    public int add(Advertisement advertisement)
        throws Throwable
    {
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        String imageCode;
        Timestamp showTime;
        Timestamp unshowTime;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            UploadFile image = advertisement.getImage();
            if (image == null)
            {
                throw new ParameterException("必须上传广告图片");
            }
            
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
            url = advertisement.getURL();
        }
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?",
                advertisement.getSortIndex(),
                title,
                url,
                imageCode,
                serviceResource.getSession().getAccountId(),
                showTime,
                unshowTime,
                getCurrentTimestamp(connection));
        }
    }
    
    @Override
    public int addForType(AdvertisementType advertisement)
        throws Throwable
    {
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        String imageCode;
        Timestamp showTime;
        Timestamp unshowTime;
        String advType;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            if (showTime.getTime() >= unshowTime.getTime())
            {
                throw new ParameterException("下架时间必须晚于上架时间");
            }
            UploadFile image = advertisement.getImage();
            if (image == null)
            {
                throw new ParameterException("必须上传广告图片");
            }
            advType = advertisement.getAdvType();
            if (advType == null)
            {
                throw new ParameterException("必须指定广告类型");
            }
            
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
            url = advertisement.getURL();
        }
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?, F12 = ?",
                advertisement.getSortIndex(),
                title,
                url,
                imageCode,
                serviceResource.getSession().getAccountId(),
                showTime,
                unshowTime,
                getCurrentTimestamp(connection),
                advType);
        }
    }
    
    @Override
    public void update(int id, Advertisement advertisement)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("没有给出广告ID");
        }
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        
        Timestamp showTime;
        Timestamp unshowTime;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            url = advertisement.getURL();
        }
        UploadFile image = advertisement.getImage();
        try (Connection connection = getConnection())
        {
            if (image == null)
            {
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F07 = ?,F08 = ? WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    showTime,
                    unshowTime,
                    id);
            }
            else
            {
                FileStore fileStore = serviceResource.getResource(FileStore.class);
                String imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F07 = ?,F08 = ? WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    imageCode,
                    showTime,
                    unshowTime,
                    id);
            }
        }
    }
    
    @Override
    public void updateForType(int id, AdvertisementType advertisement)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("没有给出广告ID");
        }
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        
        Timestamp showTime;
        Timestamp unshowTime;
        String advType;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            if (showTime.getTime() >= unshowTime.getTime())
            {
                throw new ParameterException("下架时间必须晚于上架时间");
            }
            advType = advertisement.getAdvType();
            if (advType == null)
            {
                throw new ParameterException("必须指定广告类型");
            }
            
            url = advertisement.getURL();
        }
        UploadFile image = advertisement.getImage();
        try (Connection connection = getConnection())
        {
            if (image == null)
            {
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F07 = ?,F08 = ?, F12 = ? WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    showTime,
                    unshowTime,
                    advType,
                    id);
            }
            else
            {
                FileStore fileStore = serviceResource.getResource(FileStore.class);
                String imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F07 = ?,F08 = ?, F12 = ? WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    imageCode,
                    showTime,
                    unshowTime,
                    advType,
                    id);
            }
        }
    }
    
    @Override
    public void delete(int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }

        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5016 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
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
    public int addTTDai(AdvertisementContent advertisement)
        throws Throwable
    {
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        String imageCode;
        Timestamp showTime;
        Timestamp unshowTime;
        String content;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            UploadFile image = advertisement.getImage();
            if (image == null)
            {
                throw new ParameterException("必须上传广告图片");
            }
            
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
            url = advertisement.getURL();
            
            content = advertisement.getContent();
            try (Connection connection = getConnection())
            {
                return insert(connection,
                    "INSERT INTO T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F11 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    imageCode,
                    serviceResource.getSession().getAccountId(),
                    showTime,
                    unshowTime,
                    getCurrentTimestamp(connection),
                    content);
            }
        }
    }
    
    @Override
    public void updateTTDai(int id, AdvertisementContent advertisement)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("没有给出广告ID");
        }
        if (advertisement == null)
        {
            throw new ParameterException("没有给出广告信息");
        }
        String title;
        String url;
        
        Timestamp showTime;
        Timestamp unshowTime;
        String content;
        {
            title = advertisement.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("必须指定广告标题");
            }
            if (title.length() > 50)
            {
                throw new ParameterException("广告标题必须小于50个字");
            }
            showTime = advertisement.getShowTime();
            if (showTime == null)
            {
                throw new ParameterException("必须指定上架时间");
            }
            unshowTime = advertisement.getUnshowTime();
            if (unshowTime == null)
            {
                throw new ParameterException("必须指定下架时间");
            }
            url = advertisement.getURL();
            content = advertisement.getContent();
        }
        UploadFile image = advertisement.getImage();
        try (Connection connection = getConnection())
        {
            if (image == null)
            {
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F07 = ?,F08 = ?,F11 = ?  WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    showTime,
                    unshowTime,
                    content,
                    id);
            }
            else
            {
                FileStore fileStore = serviceResource.getResource(FileStore.class);
                String imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
                execute(connection,
                    "UPDATE T5016 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F07 = ?,F08 = ? ,F11 = ? WHERE F01 = ?",
                    advertisement.getSortIndex(),
                    title,
                    url,
                    imageCode,
                    showTime,
                    unshowTime,
                    content,
                    id);
            }
        }
    }
    
    @Override
    public AdvertisementRecord getTTDdai(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<AdvertisementRecord>()
                {
                    
                    @Override
                    public AdvertisementRecord parse(ResultSet resultSet)
                        throws SQLException
                    {
                        AdvertisementRecord record = null;
                        if (resultSet.next())
                        {
                            record = new AdvertisementRecord();
                            record.id = resultSet.getInt(1);
                            record.sortIndex = resultSet.getInt(2);
                            record.title = resultSet.getString(3);
                            record.url = resultSet.getString(4);
                            record.imageCode = resultSet.getString(5);
                            record.publisherId = resultSet.getInt(6);
                            record.showTime = resultSet.getTimestamp(7);
                            record.unshowTime = resultSet.getTimestamp(8);
                            record.createTime = resultSet.getTimestamp(9);
                            record.updateTime = resultSet.getTimestamp(10);
                            record.publisherName = resultSet.getString(11);
                            Timestamp currentTime = resultSet.getTimestamp(12);
                            if (currentTime.getTime() < record.showTime.getTime())
                            {
                                record.status = AdvertisementStatus.DSJ;
                            }
                            else if (currentTime.getTime() < record.unshowTime.getTime())
                            {
                                record.status = AdvertisementStatus.YSJ;
                            }
                            else
                            {
                                record.status = AdvertisementStatus.YXJ;
                            }
                            record.content = resultSet.getString(13);
                        }
                        return record;
                    }
                },
                "SELECT T5016.F01,T5016.F02,T5016.F03,T5016.F04,T5016.F05,T5016.F06,T5016.F07,T5016.F08,T5016.F09,T5016.F10,T7110.F04 AS F11,CURRENT_TIMESTAMP() AS F12,T5016.F11 AS F13 FROM T5016 INNER JOIN S71.T7110 ON T5016.F06 = T7110.F01 WHERE T5016.F01 = ?",
                id);
        }
    }
   
    @Override
    public PagingResult<AdvertSpscRecord> search(Paging paging)
			throws Throwable {
		String sql = "SELECT T5022.F01,T5022.F02,T5022.F03,T5022.F04,T5022.F05,T5022.F06,T5022.F07,T5022.F08,"
				+ " T5022.F10,T5022.F11,T7110.F02 AS F13  FROM S50.T5022 ,S71.T7110 WHERE T5022.F09 = T7110.F01 ORDER BY  T5022.F02 DESC ,T5022.F11 DESC";
        try(Connection connection = getConnection()) {
            return selectPaging(connection,
                    new ArrayParser<AdvertSpscRecord>() {
                        @Override
                        public AdvertSpscRecord[] parse(ResultSet resultSet)
                                throws SQLException {
                            ArrayList<AdvertSpscRecord> list = null;
                            while (resultSet.next()) {
                                AdvertSpscRecord record = new AdvertSpscRecord();
                                record.id = resultSet.getInt(1);
                                record.sortIndex = resultSet.getInt(2);
                                record.title = resultSet.getString(3);
                                record.fileName = resultSet.getString(4);
                                record.fileSize = resultSet.getString(5);
                                record.fileFormat = resultSet.getString(6);
                                record.status = T5021_F07.parse(resultSet
                                        .getString(7));
                                record.isAuto = resultSet.getInt(8);
                                record.showTime = resultSet.getTimestamp(9);
                                record.updateTime = resultSet.getTimestamp(10);
                                record.publisherName = resultSet.getString(11);
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                list.add(record);
                            }
                            return list == null || list.size() == 0 ? null : list
                                    .toArray(new AdvertSpscRecord[list.size()]);
                        }
                    }, paging, sql);
        }
	};

	@Override
	public int insertSpsc(AdvertSpsc spsc) throws Throwable {
        String title;
        String imageCode = null;
        {
            // 校验
            title = spsc.getTitle();
            if (StringHelper.isEmpty(title)) {
                throw new ParameterException("标题不能为空.");
            }
            if (title.length() > 30) {
                throw new ParameterException("标题不能超过30个字.");
            }

        }
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = spsc.getFile();
        if (uploadFile != null) {// 保存文件路径
            imageCode = fileStore.upload(FileType.USER_AUTH_VIDEO.ordinal(),
                    uploadFile)[0];
        }

        try (Connection connection = getConnection()){
            try {
                serviceResource.openTransactions(connection);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if("1".equals(spsc.getsortIndex())){
                    try (PreparedStatement pstmt = connection
                        .prepareStatement("UPDATE S50.T5022 SET T5022.F02=0,T5022.F11=? ")) {
                        pstmt.setTimestamp(1, now);
                        pstmt.execute();
                    }
                }
                int id = insert(
                        connection,
                        "INSERT INTO S50.T5022 SET F02=?, F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?",
                        spsc.getsortIndex(), title, imageCode, spsc.fileSize(), spsc.fileFormat(),
                        T5021_F07.parse(spsc.getStatus()), spsc.getIsAuto(),
                        serviceResource.getSession().getAccountId(), now);
                serviceResource.commit(connection);
                return id;
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw e;
            }
        }

	}

	@Override
	public void updateSpsc(int id, AdvertSpsc spsc,String url) throws Throwable {
		if (id <= 0 || spsc == null) {
			return;
		}

		String title;
		String imageCode = null;
		{
			// 校验
			title = spsc.getTitle();
			if (StringHelper.isEmpty(title)) {
				throw new ParameterException("标题不能为空.");
			}
			if (title.length() > 30) {
				throw new ParameterException("标题不能超过30个字.");
			}

		}
		FileStore fileStore = serviceResource.getResource(FileStore.class);
		UploadFile uploadFile = spsc.getFile();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (uploadFile != null) {// 保存文件路径
			imageCode = fileStore.upload(FileType.USER_AUTH_VIDEO.ordinal(),
					uploadFile)[0];
		}
        try(Connection connection = getConnection()) {
            if("1".equals(spsc.getsortIndex())){
                try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S50.T5022 SET T5022.F02=0,T5022.F11=? ")) {
                    pstmt.setTimestamp(1, now);
                    
                    pstmt.execute();
                }
            }
            if (imageCode == null) {
                execute(connection,
                        "UPDATE S50.T5022 SET F02 = ?, F03 = ?,F07 = ?,F08 = ?,F11 = ? WHERE F01 = ? ",
                        spsc.getsortIndex(), spsc.getTitle(), spsc.getStatus(), spsc.getIsAuto(), now,
                        id);
            } else {
                execute(connection,
                        "UPDATE S50.T5022 SET F02 = ?, F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F11 = ? WHERE F01 = ? ",
                        spsc.getsortIndex(), spsc.getTitle(), imageCode, spsc.fileSize(),
                        spsc.fileFormat(), spsc.getStatus(), spsc.getIsAuto(), now,
                        id);
                File file = new File(url);
                file.delete();
            }
        }

	}

	@Override
	public AdvertSpscRecord searchSpsc(int id) throws Throwable {
		{
			if (id <= 0) {
				return null;
			}
            try(Connection connection = getConnection()) {
                return select(
                        connection,
                        new ItemParser<AdvertSpscRecord>() {

                            @Override
                            public AdvertSpscRecord parse(ResultSet resultSet)
                                    throws SQLException {
                                AdvertSpscRecord record = null;
                                if (resultSet.next()) {
                                    record = new AdvertSpscRecord();
                                    record.id = resultSet.getInt(1);
                                    record.sortIndex = resultSet.getInt(2);
                                    record.title = resultSet.getString(3);
                                    record.fileName = resultSet.getString(4);
                                    record.fileSize = resultSet.getString(5);
                                    record.fileFormat = resultSet.getString(6);
                                    record.status = T5021_F07.parse(resultSet
                                            .getString(7));
                                    record.isAuto = resultSet.getInt(8);
                                    record.showTime = resultSet.getTimestamp(9);
                                    record.updateTime = resultSet.getTimestamp(10);
                                }
                                return record;
                            }
                        },
                        "select T5022.F01,T5022.F02,T5022.F03,T5022.F04,T5022.F05,T5022.F06,T5022.F07,T5022.F08,T5022.F10,T5022.F11 from S50.T5022 WHERE T5022.F01 = ?",
                        id);
            }
		}
	}

	@Override
	public void deleteSpsc(int id,String url) throws Throwable {
		if (id == 0) {
			return;
		}
		try(Connection connection = getConnection()) {
            try {
                try (PreparedStatement pstmt = connection
                        .prepareStatement("DELETE FROM T5022 WHERE F01 = ?")) {
                    pstmt.setInt(1, id);
                    pstmt.execute();
                }
                File file = new File(url);
                file.delete();
            } catch (Exception e) {
                throw e;
            }
        }
	}

	@Override
	public AdvertSpscRecord searchqtSpsc() throws Throwable {
        try (Connection connection = getConnection()) {
            return select(connection,
                    new ItemParser<AdvertSpscRecord>() {

                        @Override
                        public AdvertSpscRecord parse(ResultSet resultSet)
                                throws SQLException {
                            AdvertSpscRecord record = null;
                            if (resultSet.next()) {
                                record = new AdvertSpscRecord();
                                record.id = resultSet.getInt(1);
                                record.sortIndex = resultSet.getInt(2);
                                record.title = resultSet.getString(3);
                                record.fileName = resultSet.getString(4);
                                record.fileSize = resultSet.getString(5);
                                record.fileFormat = resultSet.getString(6);
                                record.status = T5021_F07.parse(resultSet
                                        .getString(7));
                                record.isAuto = resultSet.getInt(8);
                                record.showTime = resultSet.getTimestamp(9);
                                record.updateTime = resultSet.getTimestamp(10);
                            }
                            return record;
                        }
                    },
                    "select T5022.F01,T5022.F02,T5022.F03,T5022.F04,T5022.F05,T5022.F06,T5022.F07,T5022.F08,T5022.F10,T5022.F11 from S50.T5022 WHERE T5022.F02='1' ORDER BY T5022.F11 DESC LIMIT 1 ");
        }
    }

	@Override
	public void IndexSpsc(int id) throws Throwable {
		
		if ( id == 0) {
			return;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		try(Connection connection = getConnection()) {
            try {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S50.T5022 SET T5022.F02=0,T5022.F11=? WHERE T5022.F01 <> ?")) {
                    pstmt.setTimestamp(1, now);
                    pstmt.setInt(2, id);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt = connection
                        .prepareStatement("UPDATE S50.T5022 SET T5022.F02=1,T5022.F11=? WHERE T5022.F01=?")) {
                    pstmt.setTimestamp(1, now);
                    pstmt.setInt(2, id);
                    pstmt.execute();
                }
                serviceResource.commit(connection);
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw e;
            }
        }
}

    @Override
    public void updateBatchOrder(String ids, int order)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S50.T5016 SET F02  = ? WHERE F01 IN (");
        List<Object> params = new ArrayList<Object>();
        params.add(order);
        sql.append(getBatchId(ids,params));
        sql.append(")");
        try (Connection connection = getConnection()) {
            execute(connection,sql.toString(),params.toArray());
        }
        
    }
}
