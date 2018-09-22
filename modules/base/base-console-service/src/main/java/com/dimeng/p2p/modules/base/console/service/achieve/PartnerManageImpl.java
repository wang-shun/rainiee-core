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
import com.dimeng.p2p.modules.base.console.service.PartnerManage;
import com.dimeng.p2p.modules.base.console.service.entity.Partner;
import com.dimeng.p2p.modules.base.console.service.entity.PartnerRecord;
import com.dimeng.p2p.modules.base.console.service.query.PartnerQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

public class PartnerManageImpl extends AbstractInformationService implements
		PartnerManage {

	public static class PartnerManageFactory implements
			ServiceFactory<PartnerManage> {

		@Override
		public PartnerManage newInstance(ServiceResource serviceResource) {
			return new PartnerManageImpl(serviceResource);
		}

	}

	public PartnerManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<PartnerRecord> ARRAY_PARSER = new ArrayParser<PartnerRecord>() {

		@Override
		public PartnerRecord[] parse(ResultSet resultSet) throws SQLException {
			ArrayList<PartnerRecord> list = null;
			while (resultSet.next()) {
				PartnerRecord record = new PartnerRecord();
				record.id = resultSet.getInt(1);
				record.sortIndex = resultSet.getInt(2);
				record.viewTimes = resultSet.getInt(3);
				record.name = resultSet.getString(4);
				record.url = resultSet.getString(5);
				record.imageCode = resultSet.getString(6);
				record.address = resultSet.getString(7);
				record.description = resultSet.getString(8);
				record.publisherId = resultSet.getInt(9);
				record.createTime = resultSet.getTimestamp(10);
				record.updateTime = resultSet.getTimestamp(11);
				record.publisherName = resultSet.getString(12);
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(record);
			}
			return list == null || list.size() == 0 ? null : list
					.toArray(new PartnerRecord[0]);
		}
	};
	protected static final ItemParser<PartnerRecord> ITEM_PARSER = new ItemParser<PartnerRecord>() {

		@Override
		public PartnerRecord parse(ResultSet resultSet) throws SQLException {
			PartnerRecord record = null;
			if (resultSet.next()) {
				record = new PartnerRecord();
				record.id = resultSet.getInt(1);
				record.sortIndex = resultSet.getInt(2);
				record.viewTimes = resultSet.getInt(3);
				record.name = resultSet.getString(4);
				record.url = resultSet.getString(5);
				record.imageCode = resultSet.getString(6);
				record.address = resultSet.getString(7);
				record.description = resultSet.getString(8);
				record.publisherId = resultSet.getInt(9);
				record.createTime = resultSet.getTimestamp(10);
				record.updateTime = resultSet.getTimestamp(11);
				record.publisherName = resultSet.getString(12);
			}
			return record;
		}
	};

	protected static final String SELECT_ALL_SQL = "SELECT T5013.F01,T5013.F02,T5013.F03,T5013.F04,T5013.F05,T5013.F06,T5013.F07,T5013.F08,T5013.F09,T5013.F10,T5013.F11,T7110.F04 AS F12 FROM T5013 INNER JOIN S71.T7110 ON T5013.F09 = T7110.F01 ";

	@Override
	public PagingResult<PartnerRecord> search(PartnerQuery query, Paging paging)
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
                sql.append(" AND T5013.F04 LIKE ?");
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
                sql.append(" AND DATE(T5013.F10) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5013.F10) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5013.F11) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5013.F11) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" ORDER BY T5013.F02,T5013.F11 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public PartnerRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5013.F01 = ?", id);
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
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5013 WHERE F01 = ?");)
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
    public int add(Partner partner)
        throws Throwable
    {
        if (partner == null)
        {
            throw new ParameterException("没有指定合作伙伴信息");
        }
        String name = partner.getName();
        if (StringHelper.isEmpty(name))
        {
            throw new ParameterException("合作伙伴名称不能为空");
        }
        UploadFile image = partner.getImage();
        if (image == null)
        {
            throw new ParameterException("合作伙伴图片不能为空");
        }
        if (StringHelper.isEmpty(partner.getDescription()))
        {
            throw new ParameterException("公司简介不能为空");
        }
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        String imageCode = fileStore.upload(FileType.CUSTOMER_SERVICE_IMAGE.ordinal(), image)[0];
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO S50.T5013 SET F02 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?",
                partner.getSortIndex(),
                name,
                partner.getURL(),
                imageCode,
                partner.getAddress(),
                partner.getDescription(),
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection));
        }
    }

	@Override
    public void update(int id, Partner partner)
        throws Throwable
    {
        if (id <= 0 || partner == null)
        {
            return;
        }
        String name = partner.getName();
        if (StringHelper.isEmpty(name))
        {
            throw new ParameterException("合作伙伴名称不能为空");
        }
        if (StringHelper.isEmpty(partner.getDescription()))
        {
            throw new ParameterException("公司简介不能为空");
        }
        String imageCode = null;
        UploadFile image = partner.getImage();
        if (image != null)
        {
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            imageCode = fileStore.upload(FileType.CUSTOMER_SERVICE_IMAGE.ordinal(), image)[0];
        }
        try (Connection connection = getConnection())
        {
            if (imageCode == null)
            {
                execute(connection,
                    "UPDATE S50.T5013 SET F02 = ?,F04 = ?,F05 = ?,F07 = ?,F08 = ? WHERE F01 = ?",
                    partner.getSortIndex(),
                    name,
                    partner.getURL(),
                    partner.getAddress(),
                    partner.getDescription(),
                    id);
            }
            else
            {
                execute(connection,
                    "UPDATE S50.T5013 SET F02 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ? WHERE F01 = ?",
                    partner.getSortIndex(),
                    name,
                    partner.getURL(),
                    imageCode,
                    partner.getAddress(),
                    partner.getDescription(),
                    id);
            }
        }
    }
}
