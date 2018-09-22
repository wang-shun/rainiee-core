package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.modules.base.console.service.BankManage;
import com.dimeng.p2p.modules.base.console.service.query.BankQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

public class BankManageImpl extends AbstractInformationService implements
		BankManage {

	public static class TermManageFactory implements ServiceFactory<BankManage> {

		@Override
		public BankManage newInstance(ServiceResource serviceResource) {
			return new BankManageImpl(serviceResource);
		}

	}

	public BankManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public int add(String bankName, String code)
        throws Throwable
    {
        if (StringHelper.isEmpty(bankName))
        {
            throw new ParameterException("银行名称不能为空");
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return insert(connection, "INSERT INTO T5020 SET F02= ?,F03=?,F04=?", bankName, T5020_F03.QY, code);
        }
    }

	@Override
	public PagingResult<T5020> search(BankQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01,F02,F03,F04 FROM T5020 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String bankName = query.getBankName();
            T5020_F03 bankStatus = query.getStatus();
            if (!StringHelper.isEmpty(bankName))
            {
                sql.append(" AND F02 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(bankName));
            }
            if (bankStatus != null)
            {
                sql.append(" AND F03 = ? ");
                parameters.add(bankStatus.name());
            }
        }
        sql.append(" ORDER BY F01 DESC ");
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return selectPaging(connection, new ArrayParser<T5020>()
            {
                
                @Override
                public T5020[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5020> list = null;
                    while (rs.next())
                    {
                        T5020 bank = new T5020();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        bank.F01 = rs.getInt(1);
                        bank.F02 = rs.getString(2);
                        bank.F03 = T5020_F03.valueOf(rs.getString(3));
                        bank.F04 = rs.getString(4);
                        list.add(bank);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T5020[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
    public void update(int id, String bankName, String code)
        throws Throwable
    {
        if (StringHelper.isEmpty(bankName))
        {
            throw new ParameterException("银行名称不能为空");
        }
        if (id <= 0)
        {
            return;
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            execute(connection, "UPDATE T5020 SET F02 = ?, F04 = ? WHERE F01 = ? ", bankName, code, id);
        }
        try (Connection connection = getConnection())
        {
            writeLog(connection, "操作日志", "银行设置修改");
        }
    }

	@Override
    public T5020 get(int bankId)
        throws Throwable
    {
        if (bankId <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return select(connection, new ItemParser<T5020>()
            {
                
                @Override
                public T5020 parse(ResultSet rs)
                    throws SQLException
                {
                    T5020 bank = null;
                    if (rs.next())
                    {
                        bank = new T5020();
                        bank.F01 = rs.getInt(1);
                        bank.F02 = rs.getString(2);
                        bank.F03 = T5020_F03.valueOf(rs.getString(3));
                        bank.F04 = rs.getString(4);
                    }
                    return bank;
                }
                
            }, "SELECT F01,F02,F03,F04 FROM T5020 WHERE F01 = ? ", bankId);
        }
    }

	@Override
    public void enable(int bankId)
        throws Throwable
    {
        if (bankId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            execute(connection, "UPDATE T5020 SET F03 =? WHERE F01 = ?", T5020_F03.QY, bankId);
        }
    }

	@Override
    public void disable(int bankId)
        throws Throwable
    {
        if (bankId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            execute(connection, "UPDATE T5020 SET F03 =? WHERE F01 = ?", T5020_F03.TY, bankId);
        }
    }

	@Override
	public boolean isBankExists(String name) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S50.T5020 WHERE F02=?")) {
				ps.setString(1, name);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
