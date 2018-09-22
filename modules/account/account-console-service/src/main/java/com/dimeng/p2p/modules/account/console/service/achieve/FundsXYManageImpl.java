package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.modules.account.console.service.FundsXYManage;
import com.dimeng.p2p.modules.account.console.service.entity.FundXYAccountType;
import com.dimeng.p2p.modules.account.console.service.entity.FundsXYJYView;
import com.dimeng.p2p.modules.account.console.service.entity.FundsXYView;
import com.dimeng.p2p.modules.account.console.service.query.FundsJYQuery;
import com.dimeng.p2p.modules.account.console.service.query.FundsXYQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

public class FundsXYManageImpl extends AbstractUserService implements FundsXYManage
{
    
    public FundsXYManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final ArrayParser<FundsXYView> FUNDSXYVIEW_PARSER = new ArrayParser<FundsXYView>()
    {
        @Override
        public FundsXYView[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<FundsXYView> list = null;
            while (resultSet.next())
            {
                FundsXYView record = new FundsXYView();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getBigDecimal(3);
                record.F04 = resultSet.getTimestamp(4);
                record.F05 = resultSet.getString(5);
                record.F06 = resultSet.getString(6);
                record.F07 = resultSet.getString(7);
                record.F08 = resultSet.getString(8);
                record.F09 = T6110_F06.parse(resultSet.getString(9));
                record.F10 = T6110_F07.parse(resultSet.getString(10));
                record.F11 = T6110_F08.parse(resultSet.getString(11));
                record.F12 = resultSet.getTimestamp(12);
                record.F13 = T6110_F10.parse(resultSet.getString(13));
                record.F14 = resultSet.getString(14);
                if (list == null)
                {
                    list = new ArrayList<FundsXYView>();
                }
                list.add(record);
            }
            return list == null ? null : list.toArray(new FundsXYView[list.size()]);
        }
    };
    
    protected static final ArrayParser<FundsXYJYView> FUNDSXYJYVIEW_PARSER = new ArrayParser<FundsXYJYView>()
    {
        @Override
        public FundsXYJYView[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<FundsXYJYView> list = null;
            while (resultSet.next())
            {
                FundsXYJYView record = new FundsXYJYView();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getString(12);
                record.F03 = resultSet.getTimestamp(3);
                record.F04 = resultSet.getBigDecimal(4);
                record.F05 = resultSet.getBigDecimal(5);
                record.F06 = resultSet.getBigDecimal(6);
                record.F07 = resultSet.getString(7);
                record.F08 = resultSet.getInt(8);
                record.F09 = resultSet.getInt(9);
                record.F10 = resultSet.getBigDecimal(10);
                record.F11 = resultSet.getTimestamp(11);
                if (list == null)
                {
                    list = new ArrayList<FundsXYJYView>();
                }
                list.add(record);
            }
            return list == null ? null : list.toArray(new FundsXYJYView[list.size()]);
        }
    };
    
    @Override
    public PagingResult<FundsXYView> search(FundsXYQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6116.F01 AS F01, (SELECT IFNULL(SUM(T6120.F04),0) FROM S61.T6120 WHERE T6120.F01 = T6116.F01) AS F02, T6116.F03 AS F03, T6116.F04 AS F04, T6110.F02 AS F05, T6110.F03 AS F06, T6110.F04 AS F07, T6110.F05 AS F08, T6110.F06 AS F09, T6110.F07 AS F10, T6110.F08 AS F11, T6110.F09 AS F12, T6110.F10 AS F13, T6116.F05 AS F14 FROM S61.T6116 INNER JOIN S61.T6110 ON T6116.F01 = T6110.F01 where 1 =1");
        PagingResult<FundsXYView> pagingResult = null;
        ArrayList<Object> parameters = new ArrayList<Object>();
        searchParams(query, sql, parameters);
        sql.append(" ORDER BY T6116.F04 DESC");
        try (Connection connection = getConnection())
        {
            pagingResult = selectPaging(connection, FUNDSXYVIEW_PARSER, paging, sql.toString(), parameters);
            if (pagingResult != null && pagingResult.getItemCount() > 0)
            {
                for (FundsXYView fundsXYView : pagingResult.getItems())
                {
                    if (fundsXYView == null)
                    {
                        continue;
                    }
                    T6110_F06 t6110_F06 = fundsXYView.F09;
                    if (t6110_F06 == T6110_F06.ZRR)
                    {
                        fundsXYView.userType = "个人";
                        fundsXYView.userName = selectGrName(connection, fundsXYView.F01);
                    }
                    if (t6110_F06 == T6110_F06.FZRR && fundsXYView.F13 == T6110_F10.F)
                    {
                        fundsXYView.userType = "企业";
                        fundsXYView.userName = selectQyName(connection, fundsXYView.F01);
                    }
                    if (t6110_F06 == T6110_F06.FZRR && fundsXYView.F13 == T6110_F10.S)
                    {
                        fundsXYView.userType = "机构";
                        fundsXYView.userName = selectQyName(connection, fundsXYView.F01);
                    }
                }
            }
            return pagingResult;
        }
    }
    
    @Override
    public BigDecimal searchAmount(FundsXYQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6116.F03),0) AS F01 FROM S61.T6116 INNER JOIN S61.T6110 ON T6116.F01 = T6110.F01 WHERE 1 =1");
        ArrayList<Object> parameters = new ArrayList<>();
        searchParams(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    private void searchParams(FundsXYQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        if (query != null)
        {
            FundXYAccountType userType = query.getFundAccountType();
            if (userType != null)
            {
                if (userType == FundXYAccountType.GR)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.ZRR);
                    parameters.add(T6110_F10.F);
                }
                else if (userType == FundXYAccountType.QY)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR);
                    parameters.add(T6110_F10.F);
                }
                else if (userType == FundXYAccountType.JG)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR);
                    parameters.add(T6110_F10.S);
                }
            }
            String loginName = query.getLoginName();
            if (!StringHelper.isEmpty(loginName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(loginName));
            }
        }
    }
    
    //查询个人用户名
    protected String selectGrName(Connection connection, int F01)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return "";
    }
    
    //查询企业名称
    protected String selectQyName(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return "";
    }
    
    @Override
    public void export(FundsXYView[] FundXYRecord, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (FundXYRecord == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("用户类型");
            writer.write("姓名或企业名称");
            /*writer.write("信用积分");*/
            writer.write("授信额度(元)");
            writer.write("信用等级");
            writer.write("最后更新时间");
            writer.newLine();
            int index = 1;
            for (FundsXYView fundsXYView : FundXYRecord)
            {
                if (fundsXYView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(fundsXYView.F05);
                writer.write(fundsXYView.userType);
                writer.write(fundsXYView.userName);
                /*writer.write(Formater.formatAmount(fundsXYView.F02));*/
                writer.write(Formater.formatAmount(fundsXYView.F03));
                writer.write(fundsXYView.F14);
                writer.write(DateTimeParser.format(fundsXYView.F04) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public T5122[] getTradeTypes()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T5122[]>()
            {
                @Override
                public T5122[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5122> list = new ArrayList<>();
                    while (rs.next())
                    {
                        T5122 record = new T5122();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        list.add(record);
                    }
                    return list.toArray(new T5122[list.size()]);
                }
            }, "SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = 'QY' AND F08 = 'yes'");
        }
    }
    
    @Override
    public PagingResult<FundsXYJYView> xyjlSearch(FundsJYQuery query, Paging paging)
        throws Throwable
    {
        if (query == null)
        {
            throw new ParameterException("参数错误");
        }
        if (query.getId() < 1)
        {
            throw new ParameterException("参数错误");
        }
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6117.F01 AS F01, T6117.F03 AS F02, T6117.F04 AS F03, T6117.F05 AS F04, T6117.F06 AS F05, T6117.F07 AS F06, T6117.F08 AS F07, T6116.F01 AS F08, T6116.F02 AS F09, T6116.F03 AS F10, T6116.F04 AS F11, T5122.F02 AS F12 FROM S61.T6117 INNER JOIN S61.T6116 ON T6117.F02 = T6116.F01 LEFT JOIN S51.T5122 ON T5122.F01 = T6117.F03 WHERE T6116.F01=?");
        PagingResult<FundsXYJYView> pagingResult = null;
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(Integer.valueOf(query.getId()));
        
        int type = query.getType();
        if (type > 0)
        {
            sql.append(" AND T6117.F03= ?");
            parameters.add(type);
        }
        Timestamp date = query.getStartPayTime();
        if (date != null)
        {
            sql.append(" AND DATE(T6117.F04) >=?");
            parameters.add(date);
        }
        date = query.getEndPayTime();
        if (date != null)
        {
            sql.append(" AND DATE(T6117.F04) <=?");
            parameters.add(date);
        }
        sql.append(" ORDER BY T6117.F01 DESC");
        try (Connection connection = getConnection())
        {
            pagingResult = selectPaging(connection, FUNDSXYJYVIEW_PARSER, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public void export(FundsXYJYView[] FundXYJYRecord, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (FundXYJYRecord == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("时间");
            writer.write("类型明细");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("结余(元)");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            
            for (FundsXYJYView fundsXYJYView : FundXYJYRecord)
            {
                if (fundsXYJYView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(DateTimeParser.format(fundsXYJYView.F03, "yyyy-MM-dd HH:mm"));
                writer.write(fundsXYJYView.F02);
                writer.write(Formater.formatAmount(fundsXYJYView.F04));
                writer.write(Formater.formatAmount(fundsXYJYView.F05));
                writer.write(Formater.formatAmount(fundsXYJYView.F06));
                writer.write(fundsXYJYView.F07);
                
                writer.newLine();
            }
        }
    }
    
}
