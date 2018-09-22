package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.NoticePublishStatus;
import com.dimeng.p2p.common.enums.NoticeType;
import com.dimeng.p2p.modules.base.console.service.NoticeManage;
import com.dimeng.p2p.modules.base.console.service.entity.Notice;
import com.dimeng.p2p.modules.base.console.service.entity.NoticeRecord;
import com.dimeng.p2p.modules.base.console.service.query.NoticeQuery;
import com.dimeng.util.StringHelper;

public class NoticeManageImpl extends AbstractInformationService implements
		NoticeManage {

	public static class NoticeManageFactory implements
			ServiceFactory<NoticeManage> {

		@Override
		public NoticeManage newInstance(ServiceResource serviceResource) {
			return new NoticeManageImpl(serviceResource);
		}

	}

	public NoticeManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<NoticeRecord> ARRAY_PARSER = new ArrayParser<NoticeRecord>() {

		@Override
		public NoticeRecord[] parse(ResultSet resultSet) throws SQLException {
			ArrayList<NoticeRecord> list = null;
			while (resultSet.next()) {
				NoticeRecord record = new NoticeRecord();
				record.id = resultSet.getInt(1);
				record.type = NoticeType.valueOf(resultSet.getString(2));
				record.viewTimes = resultSet.getInt(3);
				record.publishStatus = NoticePublishStatus.valueOf(resultSet
						.getString(4));
				record.title = resultSet.getString(5);
				record.content = resultSet.getString(6);
				record.publisherId = resultSet.getInt(7);
				record.createTime = resultSet.getTimestamp(8);
				record.updateTime = resultSet.getTimestamp(9);
				record.publisherName = resultSet.getString(10);
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(record);
			}
			return list == null || list.size() == 0 ? null : list
					.toArray(new NoticeRecord[0]);
		}
	};
	protected static final ItemParser<NoticeRecord> ITEM_PARSER = new ItemParser<NoticeRecord>() {

		@Override
		public NoticeRecord parse(ResultSet resultSet) throws SQLException {
			NoticeRecord record = null;
			if (resultSet.next()) {
				record = new NoticeRecord();
				record.id = resultSet.getInt(1);
				record.type = NoticeType.valueOf(resultSet.getString(2));
				record.viewTimes = resultSet.getInt(3);
				record.publishStatus = NoticePublishStatus.valueOf(resultSet
						.getString(4));
				record.title = resultSet.getString(5);
				record.content = resultSet.getString(6);
				record.publisherId = resultSet.getInt(7);
				record.createTime = resultSet.getTimestamp(8);
				record.updateTime = resultSet.getTimestamp(9);
				record.publisherName = resultSet.getString(10);
			}
			return record;
		}
	};

	protected static final String SELECT_ALL_SQL = "SELECT T5015.F01,T5015.F02,T5015.F03,T5015.F04,T5015.F05,T5015.F06,T5015.F07,T5015.F08,T5015.F09,T7110.F04 AS F10 FROM T5015 INNER JOIN S71.T7110 ON T5015.F07 = T7110.F01 ";

	@Override
	public PagingResult<NoticeRecord> search(NoticeQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (query != null)
        {
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            NoticeType type = query.getType();
            if (type != null)
            {
                sql.append(" AND T5015.F02 = ?");
                parameters.add(type);
            }
            NoticePublishStatus status = query.getPublishStatus();
            if (status != null)
            {
                sql.append(" AND T5015.F04 = ?");
                parameters.add(status);
            }
            String string = query.getTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5015.F05 LIKE ?");
                parameters.add(connectionProvider.allMatch(string));
            }
            string = query.getPublisherName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(connectionProvider.allMatch(string));
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5015.F08) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5015.F08) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5015.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5015.F09) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" ORDER BY T5015.F09 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public NoticeRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5015.F01 = ?", id);
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
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5015 WHERE F01 = ?"))
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
    public int add(Notice notice)
        throws Throwable
    {
        if (notice == null)
        {
            throw new ParameterException("没有指定公告信息");
        }
        String title;
        NoticePublishStatus publishStatus;
        NoticeType type;
        String content;
        {
            title = notice.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("公告标题不能为空");
            }
            content = notice.getContent();
            if (StringHelper.isEmpty(content))
            {
                throw new ParameterException("公告内容不能为空");
            }
            type = notice.getType();
            if (type == null)
            {
                throw new ParameterException("公告类型不能为空");
            }
            publishStatus = notice.getPublishStatus();
            if (publishStatus == null)
            {
                throw new ParameterException("公告发布状态不能为空");
            }
        }
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO S50.T5015 SET F02 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?",
                type,
                publishStatus,
                title,
                content,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection));
        }
    }

	@Override
    public void update(int id, Notice notice)
        throws Throwable
    {
        if (id <= 0 || notice == null)
        {
            return;
        }
        String title;
        NoticePublishStatus publishStatus;
        NoticeType type;
        String content;
        {
            title = notice.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("公告标题不能为空");
            }
            content = notice.getContent();
            if (StringHelper.isEmpty(content))
            {
                throw new ParameterException("公告内容不能为空");
            }
            type = notice.getType();
            if (type == null)
            {
                throw new ParameterException("公告类型不能为空");
            }
            publishStatus = notice.getPublishStatus();
            if (publishStatus == null)
            {
                throw new ParameterException("公告发布状态不能为空");
            }
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S50.T5015 SET F02 = ?, F04 = ?, F05 = ?, F06 = ?,F09=? WHERE F01 = ?",
                type,
                publishStatus,
                title,
                content,
                new Timestamp(System.currentTimeMillis()),
                id);
        }
    }

	@Override
	public void setPublishStatus(NoticePublishStatus publishStatus, int... ids)
        throws Throwable
    {
        if (publishStatus == null || ids == null || ids.length == 0)
        {
            return;
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S50.T5015 SET F04 = ? WHERE F01 = ?");)
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setString(1, publishStatus.name());
                        pstmt.setInt(2, id);
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

}
