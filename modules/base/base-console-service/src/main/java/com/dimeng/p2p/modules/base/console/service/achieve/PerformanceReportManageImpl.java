package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

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
import com.dimeng.p2p.common.enums.PerformanceReportPublishStatus;
import com.dimeng.p2p.modules.base.console.service.PerformanceReportManage;
import com.dimeng.p2p.modules.base.console.service.entity.PerformanceReport;
import com.dimeng.p2p.modules.base.console.service.entity.PerformanceReportRecord;
import com.dimeng.p2p.modules.base.console.service.query.PerformanceReportQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

public class PerformanceReportManageImpl extends AbstractInformationService
		implements PerformanceReportManage {

	public static class PerformanceReportManageFactory implements
			ServiceFactory<PerformanceReportManage> {

		@Override
		public PerformanceReportManage newInstance(
				ServiceResource serviceResource) {
			return new PerformanceReportManageImpl(serviceResource);
		}
	}

	public PerformanceReportManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<PerformanceReportRecord> ARRAY_PARSER = new ArrayParser<PerformanceReportRecord>() {

		@Override
		public PerformanceReportRecord[] parse(ResultSet resultSet)
				throws SQLException {
			ArrayList<PerformanceReportRecord> list = null;
			while (resultSet.next()) {
				PerformanceReportRecord record = new PerformanceReportRecord();
				record.id = resultSet.getInt(1);
				record.sortIndex = resultSet.getInt(2);
				record.publishStatus = PerformanceReportPublishStatus
						.valueOf(resultSet.getString(3));
				record.viewTimes = resultSet.getInt(4);
				record.title = resultSet.getString(5);
				record.attachmentCode = resultSet.getString(6);
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
					.toArray(new PerformanceReportRecord[0]);
		}
	};
	protected static final ItemParser<PerformanceReportRecord> ITEM_PARSER = new ItemParser<PerformanceReportRecord>() {

		@Override
		public PerformanceReportRecord parse(ResultSet resultSet)
				throws SQLException {
			PerformanceReportRecord record = null;
			if (resultSet.next()) {
				record = new PerformanceReportRecord();
				record.id = resultSet.getInt(1);
				record.sortIndex = resultSet.getInt(2);
				record.publishStatus = PerformanceReportPublishStatus
						.valueOf(resultSet.getString(3));
				record.viewTimes = resultSet.getInt(4);
				record.title = resultSet.getString(5);
				record.attachmentCode = resultSet.getString(6);
				record.publisherId = resultSet.getInt(7);
				record.createTime = resultSet.getTimestamp(8);
				record.updateTime = resultSet.getTimestamp(9);
				record.publisherName = resultSet.getString(10);
			}
			return record;
		}
	};
	protected static final String SELECT_ALL_SQL = "SELECT T5018.F01,T5018.F02,T5018.F03,T5018.F04,T5018.F05,T5018.F06,T5018.F07,T5018.F08,T5018.F09,T7110.F04 AS F10 FROM T5018 INNER JOIN S71.T7110 ON T5018.F07 = T7110.F01 ";

	@Override
	public PagingResult<PerformanceReportRecord> search(
PerformanceReportQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (query != null)
        {
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            String string = query.getTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5018.F05 LIKE ?");
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
                sql.append(" AND DATE(T5018.F08) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5018.F08) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5018.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5018.F09) <= ?");
                parameters.add(timestamp);
            }
            PerformanceReportPublishStatus publishStatus = query.getPublishStatus();
            if (publishStatus != null)
            {
                sql.append(" AND T5018.F03 = ?");
                parameters.add(publishStatus);
            }
        }
        sql.append(" ORDER BY F09 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public PerformanceReportRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5018.F01 = ?", id);
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
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5018 WHERE F01 = ?");)
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
    public int add(PerformanceReport performanceReport)
        throws Throwable
    {
        if (performanceReport == null)
        {
            throw new ParameterException("没有指定业绩报告信息");
        }
        String title = performanceReport.getTitle();
        PerformanceReportPublishStatus publishStatus = performanceReport.getPublishStatus();
        if (StringHelper.isEmpty(title))
        {
            throw new ParameterException("标题不能为空");
        }
        if (publishStatus == null)
        {
            publishStatus = PerformanceReportPublishStatus.WFB;
        }
        UploadFile attachment = performanceReport.getAttachment();
        if (attachment == null)
        {
            throw new ParameterException("业绩报告必须上传附件");
        }
        
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        String attachmentCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), attachment)[0];
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO T5018 SET F03 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?",
                publishStatus,
                title,
                attachmentCode,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection));
        }
    }

	@Override
	public void update(int id, PerformanceReport performanceReport)
        throws Throwable
    {
        if (id <= 0 || performanceReport == null)
        {
            return;
        }
        String title = performanceReport.getTitle();
        String attachmentCode = null;
        PerformanceReportPublishStatus publishStatus = performanceReport.getPublishStatus();
        if (StringHelper.isEmpty(title))
        {
            throw new ParameterException("标题不能为空");
        }
        if (publishStatus == null)
        {
            publishStatus = PerformanceReportPublishStatus.WFB;
        }
        
        UploadFile attachment = performanceReport.getAttachment();
        if (attachment != null)
        {
            
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            attachmentCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), attachment)[0];
        }
        try (Connection connection = getConnection())
        {
            if (attachmentCode == null)
            {
                execute(connection,
                    "UPDATE T5018 SET F03 = ?, F05 = ?,F09= ? WHERE F01 = ?",
                    publishStatus,
                    title,
                    new Timestamp(System.currentTimeMillis()),
                    id);
            }
            else
            {
                execute(connection,
                    "UPDATE T5018 SET F03 = ?, F05 = ?, F06  =?,F09=? WHERE F01 = ?",
                    publishStatus,
                    title,
                    attachmentCode,
                    new Timestamp(System.currentTimeMillis()),
                    id);
            }
        }
    }

	@Override
	public void setPublishStatus(PerformanceReportPublishStatus publishStatus,
 int... ids)
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
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE T5018 SET F03 = ? WHERE F01 = ?");)
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
