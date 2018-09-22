package com.dimeng.p2p.modules.base.console.service.achieve;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.S61.enums.T6195_F06;
import com.dimeng.p2p.S61.enums.T6195_F08;
import com.dimeng.p2p.modules.base.console.service.TzjyManage;
import com.dimeng.p2p.modules.base.console.service.query.TzjyQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.ArrayList;

public class TzjyManageImpl extends AbstractInformationService implements  TzjyManage {

	public static class TzjyManageFactory implements
			ServiceFactory<TzjyManage> {

		@Override
		public TzjyManage newInstance(ServiceResource serviceResource) {
			return new TzjyManageImpl(serviceResource);
		}

	}

	public TzjyManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<T6195_EXT> ARRAY_PARSER = new ArrayParser<T6195_EXT>() {

		@Override
		public T6195_EXT[] parse(ResultSet resultSet) throws SQLException {
			ArrayList<T6195_EXT> list = new ArrayList<>();
			while (resultSet.next()) {
                T6195_EXT record = new T6195_EXT();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getString(3);
                record.F04 = resultSet.getTimestamp(4);
                record.F05 = resultSet.getString(5);
                record.F06 = T6195_F06.parse(resultSet.getString(6));
                record.F07 = resultSet.getInt(7);
                record.F08 = T6195_F08.parse(resultSet.getString(8));
                record.F09 = resultSet.getTimestamp(9);
                record.replyName = resultSet.getString(10);
                record.userName = resultSet.getString(11);
				list.add(record);
			}
			return list .toArray(new T6195_EXT[0]);
		}
	};

	protected static final String SELECT_ALL_SQL = "SELECT T6195.F01,T6195.F02,T6195.F03,T6195.F04,T6195.F05,T6195.F06,T6195.F07,T6195.F08,T6195.F09,T7110.F04 AS F10,T6110.F02 AS F11 " +
            "FROM S61.T6195 LEFT JOIN S71.T7110 ON T6195.F07 = T7110.F01 " +
            "INNER JOIN S61.T6110 ON T6195.F02 = T6110.F01 "  ;

	@Override
	public PagingResult<T6195_EXT> search(TzjyQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (query != null)
        {
            String replyStatus = query.getReplyStatus();
            if (!StringHelper.isEmpty(replyStatus))
            {
                sql.append(" AND T6195.F06 = ?");
                parameters.add(replyStatus);
            }
            String publishStatus = query.getPublishStatus();
            if (!StringHelper.isEmpty(publishStatus))
            {
                sql.append(" AND T6195.F08 = ?");
                parameters.add(publishStatus);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6195.F04) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6195.F04) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getPublishTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6195.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getPublishTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6195.F09) <= ?");
                parameters.add(timestamp);
            }
            int id = query.getId();
            if(id>0){
                sql.append(" AND T6195.F01 = ?");
                parameters.add(id);
            }
        }
        sql.append(" ORDER BY T6195.F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public void update(T6195 t6195)
        throws Throwable
    {
        if (t6195.F01 <= 0 || StringHelper.isEmpty(t6195.F05))
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S61.T6195 SET F05 = ?, F06 = ?,F07=? WHERE F01 = ?",
                    t6195.F05, T6195_F06.yes.name(), t6195.F07, t6195.F01);
        }
    }

	@Override
	public void setPublishStatus(T6195 t6195)
        throws Throwable
    {
        if(t6195.F01<0)
        {
            return;
        }
        StringBuffer upsateStr = new StringBuffer("UPDATE S61.T6195 SET F08 = ? ");
        if(t6195.F08==T6195_F08.YFB) {
            upsateStr.append(",F09 = ? ");
        }
        upsateStr.append("WHERE F01 = ?");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement(upsateStr.toString()))
            {

                pstmt.setString(1, t6195.F08.name());
                if(t6195.F08==T6195_F08.YFB) {
                    pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                    pstmt.setInt(3, t6195.F01);
                }else{
                    pstmt.setInt(2, t6195.F01);
                }
                pstmt.execute();
            }
        }
    }

	/**
     * 导出意见反馈列表
     * <功能详细描述>
     * @param t6195EXT
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    @Override
    public void export(T6195_EXT[] t6195Exts, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null) {
            return;
        }
        if (t6195Exts == null) {
            return;
        }
        if (StringHelper.isEmpty(charset)) {
            charset = "GBK";
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("反馈内容");
            writer.write("反馈时间");
            writer.write("回复状态");
            writer.write("回复人");
            writer.write("发布状态");
            writer.write("发布时间");
            writer.newLine();
            int index = 1;
            for (T6195_EXT t6195Ext : t6195Exts) {
                writer.write(index++);
                writer.write(t6195Ext.userName);
                writer.write(t6195Ext.F03);
                writer.write(DateTimeParser.format(t6195Ext.F04) + "\t");
                writer.write(t6195Ext.F06.getChineseName());
                writer.write(t6195Ext.replyName);
                writer.write(t6195Ext.F08.getChineseName());
                writer.write(DateTimeParser.format(t6195Ext.F09) + "\t");
                writer.newLine();
            }
        }
        
    }

}
