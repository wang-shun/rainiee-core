package com.dimeng.p2p.modules.base.console.service.achieve;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.S62.enums.T6216_F18;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.util.StringHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductManageImpl extends AbstractInformationService implements
	ProductManage
{

	public static class TermManageFactory implements ServiceFactory<ProductManage> {

		@Override
		public ProductManage newInstance(ServiceResource serviceResource) {
			return new ProductManageImpl(serviceResource);
		}

	}

	public ProductManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int add(T6216 t6216) throws Throwable {
		if (StringHelper.isEmpty(t6216.F02)) {
			throw new ParameterException("产品名称不能为空");
		}
		try(Connection connection = getConnection()){
			//return insert(getConnection(),
			return insert(connection,
					"INSERT INTO S62.T6216 SET F02= ?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,F11=?,F12=?,F13=?,F14=?,F15=?,F16=?,F17=?,F18=?",
					 t6216.F02,t6216.F03, T6216_F04.QY, t6216.F05, t6216.F06, t6216.F07, t6216.F08, t6216.F09, t6216.F10,
					 t6216.F11, t6216.F12, t6216.F13, t6216.F14, t6216.F15,t6216.F16,t6216.F17,t6216.F18);
		}
		
	}

	@Override
	public PagingResult<T6216> search(ProductQuery query, Paging paging)
			throws Throwable {
		String SELECT_ALL_SQL = "SELECT t.F01,t.F02,t.F03,t.F04,t.F05,t.F06,t.F07,t.F08,t.F09,t.F10,t.F11,t.F12,t.F13,t.F14,t.F15,s.F02 as bidType,t.F16,t.F17,t.F18 FROM S62.T6216 t left join S62.T6211  s on t.F03=s.F01 ";
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
		sql.append(" WHERE 1=1 ");
		if (query != null) {
			SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
			
			if (!StringHelper.isEmpty(query.getProductName())) {
				sql.append(" AND t.F02 LIKE ?");
				parameters.add(connectionProvider.allMatch(query.getProductName()));
			}
			
			if (query.getStatus()!=null && !StringHelper.isEmpty(query.getStatus().name())) {
				sql.append(" AND t.F04 = ?");
				parameters.add(query.getStatus().name());
			}
			
			if (!StringHelper.isEmpty(query.getRepaymentWay())) {
				sql.append(" AND t.F11 LIKE ?");
				parameters.add(connectionProvider.allMatch(query.getRepaymentWay()));
			}
			
			if (!StringHelper.isEmpty(query.getBidType())) {
				sql.append(" AND FIND_IN_SET(?,t.F03) <> 0 ");
				parameters.add(query.getBidType());
				
			}
		}
		sql.append(" ORDER BY t.F01 DESC");
		try(Connection connection = getConnection()){
			//return selectPaging(getConnection(),
			return selectPaging(connection,
					new ArrayParser<T6216>() {

						@Override
						public T6216[] parse(ResultSet rs) throws SQLException {
							ArrayList<T6216> list = null;
							while (rs.next()) {
								T6216 product = new T6216();
								if (list == null) {
									list = new ArrayList<>();
								}
								product.F01 = rs.getInt(1);
								product.F02 = rs.getString(2);
								product.F03 = rs.getString(3);
								product.F04 = T6216_F04.valueOf(rs.getString(4));
								product.F05 = rs.getBigDecimal(5);
								product.F06 = rs.getBigDecimal(6);
								
								product.F07 = rs.getInt(7);
								product.F08 = rs.getInt(8);
								
								product.F09 = rs.getBigDecimal(9);
								product.F10 = rs.getBigDecimal(10);
								product.F11 = rs.getString(11);
								product.F12 = rs.getBigDecimal(12);
								product.F13 = rs.getBigDecimal(13);
								product.F14 = rs.getBigDecimal(14);
								product.F15 = rs.getBigDecimal(15);
								product.bidType = rs.getString(16);
								product.F16 = rs.getInt(17);
								product.F17 = rs.getInt(18);
								product.F18 = T6216_F18.parse(rs.getString(19));
								list.add(product);
							}
							return list == null || list.size() == 0 ? null : list
									.toArray(new T6216[list.size()]);

						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public void update(T6216 t6216) throws Throwable {
		if (StringHelper.isEmpty(t6216.F02)) {
			throw new ParameterException("产品名称不能为空");
		}
		try(Connection connection = getConnection()){
			//execute(getConnection(),
			execute(connection,
					"UPDATE S62.T6216 SET F02= ?,F03=?,F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,F11=?,F12=?,F13=?,F14=?,F15=?,F16 = ?,F17 = ?,F18 = ? WHERE F01 = ?",
					 t6216.F02,t6216.F03, t6216.F05, t6216.F06, t6216.F07, t6216.F08, t6216.F09, t6216.F10,
					 t6216.F11, t6216.F12, t6216.F13, t6216.F14, t6216.F15,t6216.F16,t6216.F17,t6216.F18,t6216.F01);
		}
		
	}

	@Override
	public T6216 get(int productId) throws Throwable {
		if (productId <= 0) {
			return new T6216();
		}
		try(Connection connection = getConnection()){
			return select(connection,
					new ItemParser<T6216>() {

						@Override
						public T6216 parse(ResultSet rs) throws SQLException {
							T6216 product = new T6216();
							if (rs.next()) {
								product.F01 = rs.getInt(1);
								product.F02 = rs.getString(2);
								product.F03 = rs.getString(3);
								product.F04 = T6216_F04.valueOf(rs.getString(4));
								product.F05 = rs.getBigDecimal(5);
								product.F06 = rs.getBigDecimal(6);
								product.F07 = rs.getInt(7);
								product.F08 = rs.getInt(8);
								product.F09 = rs.getBigDecimal(9);
								product.F10 = rs.getBigDecimal(10);
								product.F11 = rs.getString(11);
								product.F12 = rs.getBigDecimal(12);
								product.F13 = rs.getBigDecimal(13);
								product.F14 = rs.getBigDecimal(14);
								product.F15 = rs.getBigDecimal(15);
								product.F16 = rs.getInt(16);
								product.F17 = rs.getInt(17);
								product.F18 = T6216_F18.parse(rs.getString(18));
							}
							return product;
						}

					}, "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,F16,F17,F18 FROM S62.T6216 WHERE F01 = ? ", productId);
		}
	}

	@Override
	public void enable(int productId) throws Throwable {
		if (productId <= 0) {
			return;
		}
		try(Connection connection = getConnection("S62")){
			//execute(getConnection("S62"),
			execute(connection,
					"UPDATE T6216 SET F04 =? WHERE F01 = ?", T6216_F04.QY, productId);
		}
		
	}

	@Override
	public void disable(int productId) throws Throwable {
		if (productId <= 0) {
			return;
		}
		try(Connection connection = getConnection("S62")){
			//execute(getConnection("S62"),
			execute(connection,
					"UPDATE T6216 SET F04 =? WHERE F01 = ?", T6216_F04.TY, productId);
		}
		
	}

	@Override
	public boolean isProductExists(String name) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S62.T6216 WHERE F02=?")) {
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

	@Override
	public int getValidProdcutsCount() throws Throwable {
		int count=0;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT COUNT(F01) FROM S62.T6216")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						count=rs.getInt(1);
					}
				}
			}
		}
		return count;
	}
	
}
