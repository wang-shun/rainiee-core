/*
 * 文 件 名:  FunctionExplainManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.modules.base.console.service.FunctionExplainManage;
import com.dimeng.p2p.modules.base.console.service.entity.ExplainInfo;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * <功能说明说明管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
public class FunctionExplainManageImpl extends AbstractInformationService implements FunctionExplainManage
{
    
    public static class TermManageFactory implements ServiceFactory<FunctionExplainManage>
    {
        @Override
        public FunctionExplainManage newInstance(ServiceResource serviceResource)
        {
            return new FunctionExplainManageImpl(serviceResource);
        }
        
    }
    
    public FunctionExplainManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 添加充值说明
     * @param explainInfo 线上实体
     * @param explainInfoLine 线下实体
     * @throws Throwable
     */
    @Override
    public void addCharge(ExplainInfo explainInfo, ExplainInfo explainInfoLine)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            if (isXxCharge())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    Timestamp now = getCurrentTimestamp(connection);
                    //线上充值说明
                    add(connection, explainInfo, now);
                    //线下充值说明
                    add(connection, explainInfoLine, now);
                    serviceResource.commit(connection);
                }
                catch (Exception e)
                {
                    serviceResource.rollback(connection);
                    throw e;
                }
            }
            else
            {
                Timestamp now = getCurrentTimestamp(connection);
                //线上充值说明
                add(connection, explainInfo, now);
            }
        }
    }
    
    /**
     * 添加提现说明
     * @param explainInfo 提现实体
     * @throws Throwable
     */
    @Override
    public void addWithDraw(ExplainInfo explainInfo)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Timestamp now = getCurrentTimestamp(connection);
            add(connection, explainInfo, now);
        }
    }
    
    /**
     * @param t5023 功能说明
     * @return {@code int} 功能说明ID
     * @throws Throwable
     */
    private int add(Connection connection, ExplainInfo explainInfo, Timestamp now)
        throws Throwable
    {
        String type = explainInfo.getType();
        if (StringHelper.isEmpty(type))
        {
            throw new ParameterException("没有指定说明类型");
        }
        String content = explainInfo.getContent();
        if (StringHelper.isEmpty(content))
        {
            throw new ParameterException("说明内容不能为空");
        }
        int id = insert(connection, "INSERT INTO T5023 SET F02 = ?,F03 = ?,F04 = ?", type, content, now);
        return id;
    }
    
    @Override
    public T5023 get(T5023_F02 F02)
        throws Throwable
    {
        if (F02 == null)
        {
            return null;
        }
        String sql = "SELECT F01,F02,F03,F04,F05 FROM S50.T5023 WHERE T5023.F02 = ? LIMIT 1";
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql, F02.name());
        }
    }
    
    protected static final ItemParser<T5023> ITEM_PARSER = new ItemParser<T5023>()
    {
        
        @Override
        public T5023 parse(ResultSet rs)
            throws SQLException
        {
            T5023 t5023 = null;
            if (rs.next())
            {
                t5023 = new T5023();
                t5023.F01 = rs.getInt(1);
                t5023.F02 = T5023_F02.parse(rs.getString(2));
                t5023.F03 = rs.getString(3);
                t5023.F04 = rs.getTimestamp(4);
                t5023.F05 = rs.getTimestamp(5);
            }
            return t5023;
        }
    };
    
    @Override
    public void updateCharge(ExplainInfo explainInfo, ExplainInfo explainInfoLine)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (isXxCharge())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    Timestamp now = getCurrentTimestamp(connection);
                    //线上充值说明
                    update(connection, explainInfo, now);
                    //线下充值说明
                    update(connection, explainInfoLine, now);
                    serviceResource.commit(connection);
                }
                catch (Exception e)
                {
                    serviceResource.rollback(connection);
                    throw e;
                }
            }
            else
            {
                Timestamp now = getCurrentTimestamp(connection);
                //线上充值说明
                update(connection, explainInfo, now);
            }
        }
    }
    
    @Override
    public void updateWithDraw(ExplainInfo explainInfo)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Timestamp now = getCurrentTimestamp(connection);
            update(connection, explainInfo, now);
        }
        
    }
    
    /**
     * 更新说明
     * @param F01 功能说明ID
     * @param t5023 功能说明
     * @throws Throwable
     */
    private void update(Connection connection, ExplainInfo explainInfo, Timestamp now)
        throws Throwable
    {
        int F01 = explainInfo.getId();
        if (F01 <= 0)
        {
            throw new ParameterException("参数错误");
        }
        String content = explainInfo.getContent();
        if (StringHelper.isEmpty(content))
        {
            throw new ParameterException("文章内容不能为空");
        }
        // 修改内容
        execute(connection, "UPDATE T5023 SET F03 = ?,F05 = ? WHERE F01 = ?", content, now, F01);
    }
    
    @Override
    public boolean isXxCharge()
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        if ("yeepay".equalsIgnoreCase(escrow) || "shuangqian".equalsIgnoreCase(escrow))
        {
            return false;
        }
        return true;
    }
    
}
