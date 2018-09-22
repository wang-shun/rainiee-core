package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.common.enums.SkinStatus;
import com.dimeng.p2p.modules.systematic.console.service.SkinManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Skin;
import com.dimeng.p2p.modules.systematic.console.service.entity.Skin.Status;
import com.dimeng.util.parser.EnumParser;

public class SkinManageImpl extends AbstractSystemService implements SkinManage {

	public static class SkinManageFactory implements ServiceFactory<SkinManage> {

		@Override
		public SkinManage newInstance(ServiceResource serviceResource) {
			return new SkinManageImpl(serviceResource);
		}
	}

	public SkinManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int add(String themeName, String location, String pic)
        throws Throwable
    {
        String sql = "INSERT INTO S70.T7021 SET F02=?,F03=?,F04=?,F05=?,F06=?,F08=?";
        try (Connection connection = getConnection())
        {
            return insert(connection,
                sql,
                themeName,
                location,
                pic,
                serviceResource.getSession().getAccountId(),
                new Timestamp(System.currentTimeMillis()),
                Status.S);
        }
    }

	@Override
    public Skin[] getAll()
        throws Throwable
    {
        String sql =
            "SELECT T7021.F01,T7021.F02,T7021.F03,T7021.F04,T7021.F06,T7021.F07,T7021.F08,T7110.F02 AS NAME FROM S70.T7021 INNER JOIN S71.T7110 ON T7021.F05=T7110.F01";
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<Skin>()
            {
                
                @Override
                public Skin[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Skin> skins = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Skin skin = new Skin();
                        skin.id = resultSet.getInt(1);
                        skin.themeName = resultSet.getString(2);
                        skin.location = EnumParser.parse(SkinStatus.class, resultSet.getString(3));
                        skin.pic = resultSet.getString(4);
                        skin.createTime = resultSet.getTimestamp(5);
                        skin.lastUpdateTime = resultSet.getTimestamp(6);
                        skin.isEffective = EnumParser.parse(Status.class, resultSet.getString(7));
                        skin.name = resultSet.getString(8);
                        skins.add(skin);
                    }
                    return skins.toArray(new Skin[skins.size()]);
                }
            }, sql);
        }
    }

	@Override
    public Skin get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的皮肤记录ID不存在.");
        }
        String sql =
            "SELECT T7021.F01,T7021.F02,T7021.F03,T7021.F04,T7021.F06,T7021.F07,T7021.F08,T7110.F02 AS NAME FROM S70.T7021 INNER JOIN S71.T7110 ON T7021.F05=T7110.F01 WHERE T7021.F01=?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Skin>()
            {
                
                @Override
                public Skin parse(ResultSet resultSet)
                    throws SQLException
                {
                    Skin skin = new Skin();
                    if (resultSet.next())
                    {
                        skin.id = resultSet.getInt(1);
                        skin.themeName = resultSet.getString(2);
                        skin.location = EnumParser.parse(SkinStatus.class, resultSet.getString(3));
                        skin.pic = resultSet.getString(4);
                        skin.createTime = resultSet.getTimestamp(5);
                        skin.lastUpdateTime = resultSet.getTimestamp(6);
                        skin.isEffective = EnumParser.parse(Status.class, resultSet.getString(7));
                        skin.name = resultSet.getString(8);
                    }
                    return skin;
                }
            }, sql, id);
        }
    }

	@Override
    public String get(String location)
        throws Throwable
    {
        String sql = "SELECT F04 FROM S70.T7021 WHERE F03=?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                
                @Override
                public String parse(ResultSet resultSet)
                    throws SQLException
                {
                    String pic = "";
                    if (resultSet.next())
                    {
                        pic = resultSet.getString(1);
                    }
                    return pic;
                }
            }, sql, location);
        }
    }

	@Override
	public void update(int id, String themeName, String location, String pic)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的皮肤记录ID不存在.");
        }
        String sql = "UPDATE S70.T7021 SET F02=?,F03=?,F04=?,F07=? WHERE F01=?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, themeName, location, pic, new Timestamp(System.currentTimeMillis()), id);
        }
    }

	@Override
    public void del(int... ids)
        throws Throwable
    {
        if (ids == null)
        {
            throw new ParameterException("未指定记录或指定的记录不存在.");
        }
        String sql = "DELETE FROM S70.T7021 WHERE F01= ? ";
        try (Connection connection = getConnection())
        {
            for (int id : ids)
            {
                execute(connection, sql, id);
            }
        }
    }
}
