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
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.modules.account.console.service.FundsJYManage;
import com.dimeng.p2p.modules.account.console.service.entity.FundsJYView;
import com.dimeng.p2p.modules.account.console.service.entity.UserTotle;
import com.dimeng.p2p.modules.account.console.service.query.FundsJYQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class FundsJYManageImpl extends AbstractUserService implements
        FundsJYManage {

    public FundsJYManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }

    protected static final ArrayParser<FundsJYView> FUNDJYSVIEW_PARSER = new ArrayParser<FundsJYView>() {
        @Override
        public FundsJYView[] parse(ResultSet resultSet) throws SQLException {
            ArrayList<FundsJYView> list = null;
            while (resultSet.next()) {
                FundsJYView record = new FundsJYView();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getInt(3);
                record.F04 = resultSet.getInt(4);
                record.F05 = resultSet.getTimestamp(5);
                record.F06 = resultSet.getBigDecimal(6);
                record.F07 = resultSet.getBigDecimal(7);
                record.F08 = resultSet.getBigDecimal(8);
                record.F09 = resultSet.getString(9);
                record.F10 = T6102_F10.parse(resultSet.getString(10));
                record.F11 = resultSet.getTimestamp(11);
                if (list == null) {
                    list = new ArrayList<FundsJYView>();
                }
                list.add(record);
            }
            return list == null ? null : list.toArray(new FundsJYView[list
                    .size()]);
        }
    };

    @Override
    public PagingResult<FundsJYView> search(FundsJYQuery query, Paging paging)
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
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6102 WHERE T6102.F02=?");
        PagingResult<FundsJYView> pagingResult = null;
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(query.getId());
        int type = query.getType();
        if (type > 0)
        {
            sql.append(" AND F03= ?");
            parameters.add(type);
        }
        Timestamp date = query.getStartPayTime();
        if (date != null)
        {
            sql.append(" AND DATE(F05) >=?");
            parameters.add(date);
        }
        date = query.getEndPayTime();
        if (date != null)
        {
            sql.append(" AND DATE(F05) <=?");
            parameters.add(date);
        }
        sql.append(" ORDER BY F01 DESC");
        try (Connection connection = getConnection())
        {
            pagingResult = selectPaging(connection, FUNDJYSVIEW_PARSER, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }

    @Override
    public void export(FundsJYView[] fundsJYViews, OutputStream outputStream,
                       String charset) throws Throwable {
        if (outputStream == null) {
            return;
        }
        if (StringHelper.isEmpty(charset)) {
            charset = "GBK";
        }
        if (fundsJYViews == null) {
            return;
        }
       

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                outputStream, charset))) {
            List<T5122> t22list = getTradeTypes();
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

            for (FundsJYView fundsJYView : fundsJYViews) {
                if (fundsJYView == null) {
                    continue;
                }
                writer.write(index++);
                writer.write(DateTimeParser.format(fundsJYView.F05)+ "\t");
                String msg = "";
                for (T5122 type : t22list) {
                    if (type.F01 == fundsJYView.F03) {
                        msg = type.F02;
                        break;
                    }
                }
                writer.write(msg);
                writer.write(Formater.formatAmount(fundsJYView.F06));
                writer.write(Formater.formatAmount(fundsJYView.F07));
                writer.write(Formater.formatAmount(fundsJYView.F08));
                writer.write(fundsJYView.F09);

                writer.newLine();
            }
        }
    }

    @Override
    public List<T5122> getTradeTypes() throws Throwable {
        List<T5122> t5122List = new ArrayList<T5122>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT t1.F01, t1.F02, t1.F03 FROM S51.T5122 t1")) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        T5122 t5122 = new T5122();
                        t5122.F01 = resultSet.getInt(1);
                        t5122.F02 = resultSet.getString(2);
                        t5122.F03 = T5122_F03.parse(resultSet.getString(3));
                        t5122List.add(t5122);
                    }
                }
            }
        }
        return t5122List;
    }

    @Override
    public T6101_F03 getType(int id) throws Throwable {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT F03 FROM S61.T6101 WHERE F01=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return EnumParser.parse(T6101_F03.class,
                                rs.getString(1));
                    }
                }
            }
        }
        return T6101_F03.WLZH;
    }

    @Override
    public UserTotle getUserTotle(int id)
        throws Throwable
    {
        int userId = 0;
        int ptUserId = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6101.F01 FROM S71.T7101 INNER JOIN S61.T6101 WHERE T7101.F01=T6101.F02 AND T6101.F03=? LIMIT 1"))
            {
                ps.setString(1, T6101_F03.WLZH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        ptUserId = rs.getInt(1);
                    }
                }
            }
            UserTotle userTotle = new UserTotle();
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6101 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        userId = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        userTotle.loginName = resultSet.getString(1);
                        userTotle.userType = T6110_F06.parse(resultSet.getString(2));
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 = '7001' LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        userTotle.tzTotle = resultSet.getBigDecimal(1);
                    }
                }
            }
            if (id == ptUserId)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06-T6102.F07),0) FROM S61.T6102 WHERE T6102.F02=?"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userTotle.syTotle = resultSet.getBigDecimal(1);
                        }
                    }
                }
                return userTotle;
            }
            else
            {
                //利息罚息+债权盈亏
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06),0) from S61.T6102 WHERE T6102.F03 IN (?, ?, ?) AND "
                        + "T6102.F02 = (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?)");)
                {
                    ps.setInt(1, FeeCode.TZ_LX);
                    ps.setInt(2, FeeCode.TZ_FX);
                    ps.setInt(3, FeeCode.TZ_WYJ);
                    ps.setInt(4, userId);
                    ps.setString(5, T6101_F03.WLZH.name());
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userTotle.syTotle = resultSet.getBigDecimal(1).add(earnZqzrykById(userId));
                        }
                    }
                }
                return userTotle;
            }
        }
    }

    /**
     * 线上债权转让盈亏
     * @return BigDecimal
     * @throws Throwable
     */
    private BigDecimal earnZqzrykById(int userId)
        throws Throwable
    {
        //转入盈亏
        BigDecimal zryk = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F08),0)  FROM S62.T6262,S61.T6110 WHERE T6262.F03 = T6110.F01 AND T6110.F01=?");)
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        zryk = rs.getBigDecimal(1);
                    }

                }
            }
            //转出盈亏
            BigDecimal zcyk = new BigDecimal(0);
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F09),0)  FROM S62.T6262,S62.T6260,S62.T6251,S61.T6110 WHERE T6251.F04 = T6110.F01 AND  T6251.F01 = T6260.F02 AND T6260.F01 =T6262.F02 AND T6110.F01=?");)
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        zcyk = rs.getBigDecimal(1);
                    }

                }
            }
            return zryk.add(zcyk);
        }
    }

    @Override
    public T6101_F03 getTypeT6101_F03(int id) throws Throwable {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT F03 FROM S61.T6101 WHERE F02=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return EnumParser.parse(T6101_F03.class,
                                rs.getString(1));
                    }
                }
            }
        }
        return T6101_F03.WLZH;
    }

}
