package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.SysMessageStatus;
import com.dimeng.p2p.common.enums.SysMessageType;
import com.dimeng.p2p.modules.systematic.console.service.TemplateManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Template;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class TemplateManageImpl extends AbstractSystemService implements
		TemplateManage {

	public TemplateManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void update(String key, String status, String title, String content,
 SysMessageType type)
        throws Throwable
    {
        String sql = "UPDATE S70.T7014 SET F04=?,F05=?,F07=?,F09=? WHERE F01=? AND F02=?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, title, content, new Timestamp(System.currentTimeMillis()), status, key, type);
        }
    }

	@Override
	public PagingResult<Template> serarch(String type, Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09 FROM S70.T7014 WHERE F02=?";
        if (StringHelper.isEmpty(type))
        {
            type = SysMessageType.ZNX.name();
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Template>()
            {
                
                @Override
                public Template[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Template> templates = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Template template = new Template();
                        template.key = resultSet.getString(1);
                        template.type = EnumParser.parse(SysMessageType.class, resultSet.getString(2));
                        template.eventDes = resultSet.getString(3);
                        template.title = resultSet.getString(4);
                        template.content = resultSet.getString(5);
                        template.paramDes = resultSet.getString(6);
                        template.lastUpdateTime = resultSet.getTimestamp(7);
                        template.defalutContent = resultSet.getString(8);
                        template.status = EnumParser.parse(SysMessageStatus.class, resultSet.getString(9));
                        templates.add(template);
                    }
                    return templates.toArray(new Template[templates.size()]);
                }
            }, paging, sql, type);
        }
    }

	@Override
    public Template get(String key)
        throws Throwable
    {
        if (StringHelper.isEmpty(key))
        {
            throw new ParameterException("未指定模板或指定的模板不存在.");
        }
        String sql = "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09 FROM S70.T7014 WHERE F01=?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Template>()
            {
                
                @Override
                public Template parse(ResultSet resultSet)
                    throws SQLException
                {
                    Template template = new Template();
                    if (resultSet.next())
                    {
                        template.key = resultSet.getString(1);
                        template.type = EnumParser.parse(SysMessageType.class, resultSet.getString(2));
                        template.eventDes = resultSet.getString(3);
                        template.title = resultSet.getString(4);
                        template.content = resultSet.getString(5);
                        template.paramDes = resultSet.getString(6);
                        template.lastUpdateTime = resultSet.getTimestamp(7);
                        template.defalutContent = resultSet.getString(8);
                        template.status = EnumParser.parse(SysMessageStatus.class, resultSet.getString(9));
                    }
                    return template;
                }
            }, sql, key);
        }
    }

}
