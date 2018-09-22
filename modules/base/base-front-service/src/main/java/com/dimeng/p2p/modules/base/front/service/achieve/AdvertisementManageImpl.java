package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S50.entities.T5016;
import com.dimeng.p2p.S50.entities.T5016_1;
import com.dimeng.p2p.S50.enums.T5021_F07;
import com.dimeng.p2p.modules.base.front.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord;

public class AdvertisementManageImpl extends AbstractBaseService
		implements AdvertisementManage {

	public AdvertisementManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public T5016[] getAll()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Timestamp timestamp = getCurrentTimestamp(connection);
            return selectAll(connection,
                new ArrayParser<T5016>()
                {
                    @Override
                    public T5016[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T5016> list = null;
                        while (resultSet.next())
                        {
                            T5016 record = new T5016();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getString(4);
                            record.F05 = resultSet.getString(5);
                            record.F06 = resultSet.getInt(6);
                            record.F07 = resultSet.getTimestamp(7);
                            record.F08 = resultSet.getTimestamp(8);
                            record.F09 = resultSet.getTimestamp(9);
                            record.F10 = resultSet.getTimestamp(10);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new T5016[list.size()]);
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S50.T5016 WHERE F07 <= ? AND F08 >= ? ORDER BY F02",
                    timestamp,
                    timestamp);
        }
    }
	
	@Override
    public T5016[] getAll_BZB(String type)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Timestamp timestamp = getCurrentTimestamp(connection);
            return selectAll(connection,
                new ArrayParser<T5016>()
                {
                    @Override
                    public T5016[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T5016> list = null;
                        while (resultSet.next())
                        {
                            T5016 record = new T5016();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getString(4);
                            record.F05 = resultSet.getString(5);
                            record.F06 = resultSet.getInt(6);
                            record.F07 = resultSet.getTimestamp(7);
                            record.F08 = resultSet.getTimestamp(8);
                            record.F09 = resultSet.getTimestamp(9);
                            record.F10 = resultSet.getTimestamp(10);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new T5016[list.size()]);
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S50.T5016 WHERE F07 <= ? AND F08 >= ? AND F12=? ORDER BY F02",
                    timestamp,
                    timestamp,
                type);
        }
    }
	
	@Override
    public T5016_1[] getAllTTDai()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Timestamp timestamp = getCurrentTimestamp(connection);
            return selectAll(connection,
                new ArrayParser<T5016_1>()
                {
                    @Override
                    public T5016_1[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T5016_1> list = null;
                        while (resultSet.next())
                        {
                            T5016_1 record = new T5016_1();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getString(4);
                            record.F05 = resultSet.getString(5);
                            record.F06 = resultSet.getInt(6);
                            record.F07 = resultSet.getTimestamp(7);
                            record.F08 = resultSet.getTimestamp(8);
                            record.F09 = resultSet.getTimestamp(9);
                            record.F10 = resultSet.getTimestamp(10);
                            record.F11 = resultSet.getString(11);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new T5016_1[list.size()]);
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5016 WHERE F07 <= ? AND F08 >= ? ORDER BY F02",
                    timestamp,
                    timestamp);
        }
    }

	@Override
    public T5016_1 getById(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T5016_1>()
            {
                @Override
                public T5016_1 parse(ResultSet resultSet)
                    throws SQLException
                {
                    T5016_1 record = null;
                    while (resultSet.next())
                    {
                        record = new T5016_1();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getString(11);
                        
                    }
                    return record;
                }
            }, "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5016 WHERE T5016.F01 = ?", id);
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
                    "select T5022.F01,T5022.F02,T5022.F03,T5022.F04,T5022.F05,T5022.F06,T5022.F07,T5022.F08,T5022.F10,T5022.F11 from S50.T5022 WHERE T5022.F07='YFB' ORDER BY T5022.F02 DESC,T5022.F11 DESC LIMIT 1 ");
        }
    }

}
