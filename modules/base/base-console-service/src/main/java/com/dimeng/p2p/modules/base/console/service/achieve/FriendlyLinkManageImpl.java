package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.base.console.service.FriendlyLinkManage;
import com.dimeng.p2p.modules.base.console.service.entity.FriendlyLink;
import com.dimeng.p2p.modules.base.console.service.entity.FriendlyLinkRecord;
import com.dimeng.p2p.modules.base.console.service.query.FriendlyLinkQuery;
import com.dimeng.util.StringHelper;

public class FriendlyLinkManageImpl extends AbstractInformationService
		implements FriendlyLinkManage {

	public static class FriendlyLinkManageFactory implements
			ServiceFactory<FriendlyLinkManage> {

		@Override
		public FriendlyLinkManage newInstance(ServiceResource serviceResource) {
			return new FriendlyLinkManageImpl(serviceResource);
		}
	}

	public FriendlyLinkManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<FriendlyLinkRecord> ARRAY_PARSER = new ArrayParser<FriendlyLinkRecord>() {

		@Override
		public FriendlyLinkRecord[] parse(ResultSet resultSet)
				throws SQLException {
			ArrayList<FriendlyLinkRecord> list = null;
			while (resultSet.next()) {
				FriendlyLinkRecord record = new FriendlyLinkRecord();
				record.id = resultSet.getInt(1);
				record.viewTimes = resultSet.getInt(2);
				record.sortIndex = resultSet.getInt(3);
				record.name = resultSet.getString(4);
				record.url = resultSet.getString(5);
				record.publisherId = resultSet.getInt(6);
				record.createTime = resultSet.getTimestamp(7);
				record.updateTime = resultSet.getTimestamp(8);
				record.publisherName = resultSet.getString(9);
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(record);
			}
			return list == null || list.size() == 0 ? null : list
					.toArray(new FriendlyLinkRecord[0]);
		}
	};
	protected static final ItemParser<FriendlyLinkRecord> ITEM_PARSER = new ItemParser<FriendlyLinkRecord>() {

		@Override
		public FriendlyLinkRecord parse(ResultSet resultSet)
				throws SQLException {
			FriendlyLinkRecord record = null;
			if (resultSet.next()) {
				record = new FriendlyLinkRecord();
				record.id = resultSet.getInt(1);
				record.viewTimes = resultSet.getInt(2);
				record.sortIndex = resultSet.getInt(3);
				record.name = resultSet.getString(4);
				record.url = resultSet.getString(5);
				record.publisherId = resultSet.getInt(6);
				record.createTime = resultSet.getTimestamp(7);
				record.updateTime = resultSet.getTimestamp(8);
				record.publisherName = resultSet.getString(9);
			}
			return record;
		}
	};

	protected static final String SELECT_ALL_SQL = "SELECT T5014.F01,T5014.F02,T5014.F03,T5014.F04,T5014.F05,T5014.F06,T5014.F07,T5014.F08 AS PX,T7110.F04 AS F09 FROM S50.T5014 INNER JOIN S71.T7110 ON T5014.F06 = T7110.F01 ";

	@Override
	public PagingResult<FriendlyLinkRecord> search(FriendlyLinkQuery query,
 Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (query != null)
        {
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            String string = query.getName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5014.F04 LIKE ?");
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
                sql.append(" AND DATE(T5014.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5014.F07) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5014.F08) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5014.F08) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" ORDER BY PX DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public FriendlyLinkRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5014.F01 = ?", id);
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
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5014 WHERE F01 = ?"))
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
    public int add(FriendlyLink friendlyLink)
        throws Throwable
    {
        if (friendlyLink == null)
        {
            throw new ParameterException("没有指定友情链接信息 ");
        }
        String name;
        String url;
        {
            name = friendlyLink.getName();
            if (StringHelper.isEmpty(name))
            {
                throw new ParameterException("名称不能为空 ");
            }
            url = friendlyLink.getURL();
            if (StringHelper.isEmpty(url))
            {
                throw new ParameterException("链接地址不能为空 ");
            }
        }
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO S50.T5014 SET F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?",
                friendlyLink.getSortIndex(),
                name,
                url,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection));
        }
    }

	@Override
    public void update(int id, FriendlyLink friendlyLink)
        throws Throwable
    {
        if (id <= 0 || friendlyLink == null)
        {
            return;
        }
        String name;
        String url;
        {
            name = friendlyLink.getName();
            if (StringHelper.isEmpty(name))
            {
                throw new ParameterException("名称不能为空 ");
            }
            url = friendlyLink.getURL();
            if (StringHelper.isEmpty(url))
            {
                throw new ParameterException("链接地址不能为空 ");
            }
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S50.T5014 SET F03 = ?, F04 = ?,F05 = ? WHERE F01 = ?",
                friendlyLink.getSortIndex(),
                friendlyLink.getName(),
                friendlyLink.getURL(),
                id);
        }
    }

    @Override
    public void updateBatchOrder(String ids, int order)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S50.T5014 SET F03  = ? WHERE F01 IN (");
        List<Object> params = new ArrayList<Object>();
        params.add(order);
        sql.append(getBatchId(ids,params));
        sql.append(")");
        try (Connection connection = getConnection()) {
            execute(connection,sql.toString(),params.toArray());
        }
    }

}
