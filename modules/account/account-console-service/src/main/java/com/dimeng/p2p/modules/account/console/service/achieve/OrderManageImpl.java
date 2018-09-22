/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6550;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.account.console.service.OrderManager;
import com.dimeng.p2p.modules.account.console.service.entity.Order;
import com.dimeng.p2p.modules.account.console.service.entity.OrderException;
import com.dimeng.p2p.modules.account.console.service.query.OrderExceptionQuery;
import com.dimeng.p2p.modules.account.console.service.query.OrderQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

/**
 * @author guopeng
 * 
 */
public class OrderManageImpl extends AbstractUserService implements OrderManager
{
    
    public OrderManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Order> search(OrderQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6501.F01 AS F01, T6501.F02 AS F02, T6501.F03 AS F03, T6501.F04 AS F04, T6501.F05 AS F05, T6501.F06 AS F06, T6501.F07 AS F07, T6501.F08 AS F08, T6501.F09 AS F09, T6110.F02 AS F10 FROM S65.T6501 INNER JOIN S61.T6110 ON T6501.F08 = T6110.F01 WHERE 1=1 AND T6501.F02 != 20014 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String userName = query.getUserName();
            if (!StringHelper.isEmpty(userName))
            {
                sb.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(userName));
            }
            int type = query.getType();
            if (type > 0)
            {
                sb.append(" AND T6501.F02=?");
                parameters.add(type);
            }
            Timestamp timestamp = query.getCreateStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6501.F04)>=?");
                parameters.add(timestamp);
            }
            timestamp = query.getEndStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6501.F04)<=?");
                parameters.add(timestamp);
            }
            String orderId = query.getOrderId();
            if (!StringHelper.isEmpty(orderId))
            {
                sb.append(" AND T6501.F01 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(orderId));
            }
        }
        sb.append(" ORDER BY T6501.F04 DESC");
        try (Connection connection = getConnection())
        {
            PagingResult<Order> result = selectPaging(connection, new ArrayParser<Order>()
            {
                
                @Override
                public Order[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Order> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Order record = new Order();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6501_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6501_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.userName = resultSet.getString(10);
                        lists.add(record);
                    }
                    return lists.toArray(new Order[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
            if (result != null)
            {
                Order[] orders = result.getItems();
                for (Order order : orders)
                {
                    if (order.F02 == OrderType.CHARGE.orderType() || order.F02 == OrderType.PLATFORM_CHARGE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F03 FROM S65.T6502 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.WITHDRAW.orderType()
                        || order.F02 == OrderType.PLATFORM_WITHDRAW.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F03 FROM S65.T6503 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BONDIN.orderType() || order.F02 == OrderType.BONDOUT.orderType()
                        || order.F02 == OrderType.BOND.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F03 FROM S65.T6513 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F04 FROM S65.T6504 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_CANCEL.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection,
                                "SELECT T6250.F04 FROM S65.T6508 INNER JOIN S62.T6250 ON T6508.F03=T6250.F01 WHERE T6508.F01=?",
                                order.F01);
                    }
                    else if (order.F02 == OrderType.BID_CONFIRM.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6505 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_REPAYMENT.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F06 FROM S65.T6506 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_EXCHANGE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6507 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.FINANCIAL_PURCHASE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F04 FROM S65.T6510 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.FINANCIAL_REPAYMENT.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6512 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.FREEZE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F03 FROM S65.T6515 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.UNFREEZE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F03 FROM S65.T6516 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.TRANSFER.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F02 FROM S65.T6517 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.AUTOTRANSFER.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F02 FROM S65.T6517_ext_order WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.ADVANCE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6514 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_EXPERIENCE.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F04 FROM S65.T6518 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_EXPERIENCE_CONFIRM.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6519 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_EXPERIENCE_REPAYMENT.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6520 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.PREPAYMENT_LOAN.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F06 FROM S65.T6521 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_COUPON_CONFIRM.orderType())
                    {
                        //加息券放款没有金额
                        //order.amount = selectBigDecimal(connection, "SELECT convert(T6524.F05*T6524.F06,decimal) FROM S65.T6524 WHERE T6524.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_COUPON_REPAYMENT.orderType())
                    {
                        order.amount = selectBigDecimal(connection, "SELECT F05 FROM S65.T6525 WHERE F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_COUPON.orderType())
                    {
                        //加息券投资没有金额
                        //order.amount = selectBigDecimal(connection, "SELECT convert(T6504.F04*T6523.F04,decimal) FROM S65.T6523 INNER JOIN S65.T6504 ON T6523.F06 = T6504.F01 WHERE T6523.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.BID_COUPON_CANCEL.orderType())
                    {
                        //加息券投资取消没有金额
                        //order.amount = selectBigDecimal(connection, "SELECT convert(T6288.F04*T6288.F11,decimal) FROM S62.T6288 INNER JOIN S65.T6526 ON T6526.F03 = T6288.F01 WHERE T6526.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.GYBTRANSFER.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F04 FROM S65.T6554 WHERE T6554.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.MALLTRANSFER.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F04 FROM S65.T6555 WHERE T6555.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.MALLREFUND.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F03 FROM S65.T6528 WHERE T6528.F01=?", order.F01);
                    }
                    else if (order.F02 == OrderType.OFFLINECHARGE.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F03 FROM S65.T6509 WHERE T6509.F01 = ?", order.F01);
                    }
                    else if (order.F02 == OrderType.BUY_BAD_CLAIM.orderType())
                    {
                        order.amount =
                            selectBigDecimal(connection, "SELECT F06 FROM S65.T6529 WHERE T6529.F01 = ?", order.F01);
                    }
                }
            }
            return result;
        }
    }
    
    @Override
    public PagingResult<OrderException> search(OrderExceptionQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6550.F01, T6550.F02, T6550.F03, T6550.F04,T6501.F02 AS TYPEID FROM S65.T6550 INNER JOIN S65.T6501 ON T6550.F02=T6501.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String orderId = query.getOrderId();
            if (!"".equals(orderId) && null != orderId)
            {
                sb.append(" AND T6550.F02 = ?");
                parameters.add(orderId);
            }
            Timestamp timestamp = query.getTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6550.F04)>=?");
                parameters.add(timestamp);
            }
            timestamp = query.getTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6550.F04)<=?");
                parameters.add(timestamp);
            }
        }
        sb.append(" ORDER BY T6550.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<OrderException>()
            {
                
                @Override
                public OrderException[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<T6550> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        OrderException record = new OrderException();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.typeId = resultSet.getInt(5);
                        lists.add(record);
                    }
                    return lists.toArray(new OrderException[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public T6550 get(int id)
        throws Throwable
    {
        T6550 record = new T6550();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S65.T6550 WHERE T6550.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void export(Order[] orders, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (orders == null)
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
            writer.write("订单ID");
            writer.write("用户名");
            writer.write("类型");
            writer.write("金额(元)");
            writer.write("状态");
            writer.write("创建时间");
            writer.write("提交时间");
            writer.write("完成时间");
            writer.write("订单来源");
            writer.newLine();
            int index = 1;
            for (Order order : orders)
            {
                if (order == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(order.F01);
                writer.write(order.userName);
                writer.write(OrderType.getTypeName(order.F02));
                writer.write(Formater.formatAmount(order.amount));
                writer.write(order.F03.getChineseName());
                writer.write(DateTimeParser.format(order.F04) + "\t");
                writer.write(DateTimeParser.format(order.F05) + "\t");
                writer.write(DateTimeParser.format(order.F06) + "\t");
                writer.write(order.F07.getChineseName());
                writer.newLine();
            }
        }
    }
}
