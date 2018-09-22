package com.dimeng.p2p.modules.capital.user.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.ChargeStatus;
import com.dimeng.p2p.modules.capital.user.service.UnpayManage;
import com.dimeng.p2p.modules.capital.user.service.entity.Order;
import com.dimeng.util.parser.EnumParser;

public class UnpayManageImpl extends AbstractCapitalService implements
		UnpayManage {

	public UnpayManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class UnpayManageFactory implements
			ServiceFactory<UnpayManage> {

		@Override
		public UnpayManage newInstance(ServiceResource serviceResource) {
			return new UnpayManageImpl(serviceResource);
		}

	}

	@Override
    public PagingResult<Order> search(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<Order>()
                {
                    
                    @Override
                    public Order[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<Order> orders = null;
                        while (resultSet.next())
                        {
                            if (orders == null)
                            {
                                orders = new ArrayList<>();
                            }
                            Order order = new Order();
                            order.id = resultSet.getInt(1);
                            order.orderTime = resultSet.getTimestamp(2);
                            order.amount = resultSet.getBigDecimal(3);
                            order.status = EnumParser.parse(ChargeStatus.class, resultSet.getString(4));
                            orders.add(order);
                        }
                        return orders == null ? null : orders.toArray(new Order[orders.size()]);
                    }
                },
                paging,
                "SELECT F01,F03,F04,F05 FROM T6033 WHERE F02=? AND F05=? ORDER BY F01 DESC",
                serviceResource.getSession().getAccountId(),
                ChargeStatus.WZF.name());
        }
    }

}
