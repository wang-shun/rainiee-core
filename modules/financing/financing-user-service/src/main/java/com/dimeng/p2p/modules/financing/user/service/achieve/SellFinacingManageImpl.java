package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.common.enums.IssueState;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.common.enums.TransferStatus;
import com.dimeng.p2p.modules.financing.user.service.SellFinacingManage;
import com.dimeng.p2p.modules.financing.user.service.entity.InSellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.MaySettleFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.OutSellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SellFinacingInfo;
import com.dimeng.p2p.modules.financing.user.service.query.addTransfer;
import com.dimeng.util.parser.EnumParser;

public class SellFinacingManageImpl extends AbstractFinancingManage implements SellFinacingManage{
	public static class SellFinacingManageFactory implements ServiceFactory<SellFinacingManage> {
		@Override
		public SellFinacingManageImpl newInstance(ServiceResource serviceResource) {
			return new SellFinacingManageImpl(serviceResource);
		}

	}
	public SellFinacingManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
		
	}

	@Override
	public SellFinacingInfo getSellFinacingInfo() throws Throwable{
		SellFinacingInfo info = null;
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps =
						connection.prepareStatement("SELECT F02,F03,F06,F04,F07,F05,F08 FROM S60.T6027 WHERE F01=?"))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = new SellFinacingInfo();
						info.money = rs.getBigDecimal(1);
						info.inMoney = rs.getBigDecimal(2);
						info.outMoney = rs.getBigDecimal(3);
						info.inAssetsMoney = rs.getBigDecimal(4);
						info.outAssetsMoney = rs.getBigDecimal(5);
						info.inNum = rs.getInt(6);
						info.outNum = rs.getInt(7);
					}
				}
			}
			return info;
		}
	}
	
	@Override
	public PagingResult<SellFinacing> getSellFinacing(Paging paging) throws Throwable{
		ArrayList<Object> parameters = new ArrayList<>();
        String sql =
            "SELECT T6039.F02, T6036.F03 assetsID, T6036.F24 num, T6036.F09 rate, T6039.F05, T6039.F08 F80,T6036.F08,T6039.F01,T6036.F01 jkbId,T6039.F06 zrjg FROM S60.T6039 INNER JOIN S60.T6036 ON T6036.F01 = T6039.F03 WHERE T6039.F04 =? AND T6039.F11 = ? AND T6039.F12 = ? ORDER BY T6039.F10 DESC";
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(TransferStatus.YX);
		parameters.add(IssueState.S);
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<SellFinacing>() {
				ArrayList<SellFinacing> list =  null;
				@Override
				public SellFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						SellFinacing finacing = new SellFinacing();
						finacing.sellID = resultSet.getInt(1);
						finacing.assetsID = resultSet.getInt(2);
						finacing.num = resultSet.getInt(3);
						finacing.rate = resultSet.getDouble(4);
						finacing.assetsValue = resultSet.getBigDecimal(5);
						finacing.overNum = resultSet.getInt(6);
						finacing.jkTime = resultSet.getInt(7);
						finacing.zcbId = resultSet.getInt(8);
						finacing.jkbId = resultSet.getInt(9);
						finacing.assetsMoney = resultSet.getBigDecimal(10);
						list.add(finacing);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new SellFinacing[list.size()]);
				}
			}, paging, sql, parameters);
		}
	}

	@Override
	public PagingResult<MaySettleFinacing> getMaySettleFinacing(Paging paging) throws Throwable{
		ArrayList<Object> parameters = new ArrayList<>();
        String sql =
            "SELECT T6036.F01 jkbId,T6036.F03 zqId, T6036.F24 syqs, T6036.F08 jkqs, T6036.F23 nextHK, T6036.F09 lv, T6036.F37 meje, T6038.F04 cyje, (SELECT IFNULL(SUM(T6056.F05 + T6056.F06), 0)  FROM S60.T6056 WHERE T6056.F02 = T6038.F02  AND T6056.F03 = T6038.F03 AND T6056.F10 = ? ), T6038.F08 FROM S60.T6036, S60.T6038, S60.T6041 WHERE T6036.F20 = ? AND T6038.F02 = T6036.F01 AND T6041.F02 = T6036.F01 AND DATE_ADD(T6036.F31, INTERVAL 90 DAY) < CURRENT_TIMESTAMP () AND DATE_ADD(CURRENT_TIMESTAMP (), INTERVAL 3 DAY) < T6036.F23 AND T6036.F39 = ? AND T6038.F03 = ? AND T6041.F12 = ? AND T6038.F07 = ? AND T6038.F04>0  GROUP BY T6036.F01 ORDER BY T6036.F31 DESC";
		parameters.add(DsStatus.WS);
		parameters.add(CreditStatus.YFK);
		parameters.add(OverdueStatus.F);
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(RepayStatus.WH);
		parameters.add(OverdueStatus.F);
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<MaySettleFinacing>() {
				ArrayList<MaySettleFinacing> list =  null;
				@Override
				public MaySettleFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						MaySettleFinacing finacing = new MaySettleFinacing();
						finacing.jkbId = resultSet.getInt(1);
						finacing.assestsID = resultSet.getInt(2);
						finacing.overNum = resultSet.getInt(3);
						finacing.jkTime = resultSet.getInt(4);
						finacing.nextDay = resultSet.getTimestamp(5);
						finacing.rate = resultSet.getDouble(6);
						finacing.tbMoney = resultSet.getBigDecimal(8);
						finacing.money = resultSet.getBigDecimal(9);
						finacing.mayNum = resultSet.getInt(10);
						if(finacing.mayNum > 0){
							finacing.assestsValue = finacing.tbMoney.divide(new BigDecimal(finacing.mayNum),BigDecimal.ROUND_HALF_UP);
						}
						list.add(finacing);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new MaySettleFinacing[list.size()]);
				}
			}, paging, sql, parameters);
		}
	}
	
	@Override
	public PagingResult<OutSellFinacing> getOutSellFinacing(Paging paging) throws Throwable{
        String sql =
            "SELECT T6036.F03, T6039.F07, T6039.F05, T6039.F06 F60, T6040.F06, T6040.F04 zrfs,T6036.F01, T6040.F01 AS INID FROM S60.T6039 INNER JOIN S60.T6036 ON T6036.F01 = T6039.F03 INNER JOIN S60.T6040 ON T6040.F02 = T6039.F01 WHERE T6039.F04=? ORDER BY T6040.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<OutSellFinacing>() {
				@Override
				public OutSellFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<OutSellFinacing> list = null;
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						OutSellFinacing assests = new OutSellFinacing();
						assests.asstestsID = resultSet.getInt(1);
						assests.zcNum = resultSet.getInt(2);
						assests.zqjg = resultSet.getBigDecimal(3);
						assests.zrjg =  resultSet.getBigDecimal(4);
						assests.cost =   resultSet.getBigDecimal(5);
						assests.outNum =   resultSet.getInt(6);
						assests.jkbId =   resultSet.getInt(7);
						assests.outTotalValue =new BigDecimal(assests.zqjg.doubleValue()*assests.outNum);
						assests.outTotalMoney =new BigDecimal(assests.zrjg.doubleValue()*assests.outNum);
						assests.realityMoney = assests.outTotalMoney.subtract(assests.cost) ;
						assests.money = assests.realityMoney.subtract(assests.outTotalValue);
						assests.inId = resultSet.getInt(8);
						list.add(assests);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new OutSellFinacing[list.size()]);

				}
			}, paging,sql,serviceResource.getSession().getAccountId());
		}
	}

	@Override
	public PagingResult<InSellFinacing> getInSellFinacing(Paging paging) throws Throwable{
		String sql = "SELECT T6036.F03,T6036.F24,T6036.F09,T6040.F04,T6040.F05 ,T6036.F08 , T6039.F06 zrjg,T6039.F05 zqjz ,T6036.F01,T6040.F01 AS inID"
                + " FROM S60.T6040 INNER JOIN S60.T6039 ON T6040.F02 = T6039.F01 INNER JOIN T6036 ON T6036.F01=T6039.F03 WHERE T6040.F03 = ? ORDER BY T6040.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<InSellFinacing>() {
				ArrayList<InSellFinacing> list =  null;
				@Override
				public InSellFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						InSellFinacing finacing = new InSellFinacing();
						finacing.assestsID = resultSet.getInt(1);
						finacing.overNum = resultSet.getInt(2);
						finacing.rate = resultSet.getBigDecimal(3);
						finacing.inNum = resultSet.getInt(4);
						finacing.inTime = resultSet.getTimestamp(5);
						finacing.jkNum = resultSet.getInt(6);
						finacing.inValue = resultSet.getBigDecimal(7);
						finacing.zqjg = resultSet.getBigDecimal(8);
						finacing.jkbId =   resultSet.getInt(9);
						finacing.busMoney = new BigDecimal(finacing.inValue.doubleValue()*finacing.inNum);
						finacing.money = finacing.zqjg.subtract(finacing.inValue).multiply(new BigDecimal(finacing.inNum));
						finacing.inId = resultSet.getInt(10);
						list.add(finacing);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new InSellFinacing[list.size()]);
				}
			}, paging, sql, serviceResource.getSession().getAccountId());
		}
	}

	@Override
    public void cancel(int zcbId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F07 FROM S60.T6038 WHERE F02 = (SELECT F03 FROM T6039 WHERE F01=?) AND F03 = ?");)
            {
                ps.setInt(1, zcbId);
                ps.setInt(2, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        if (EnumParser.parse(OverdueStatus.class, rs.getString(1)) == OverdueStatus.F)
                        {
                            throw new LogicalException("该债权已下架");
                        }
                    }
                    
                }
            }
            
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S60.T6039 SET F11 = ? ,F12 = ? WHERE F01 = ?"))
                {
                    ps.setString(1, TransferStatus.WX.toString());
                    ps.setString(2, OverdueStatus.F.toString());
                    ps.setInt(3, zcbId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S60.T6038 SET F07 = ? WHERE F02 = (SELECT F03 FROM T6039 WHERE F01=?) AND F03 = ?"))
                {
                    ps.setString(1, OverdueStatus.F.name());
                    ps.setInt(2, zcbId);
                    ps.setInt(3, serviceResource.getSession().getAccountId());
                    ps.executeUpdate();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

	@Override
    public void transfer(addTransfer query)
        throws Throwable
    {
        if (query == null)
        {
            throw new ParameterException("没有债权转让信息");
        }
        BigDecimal money = null;
        {
            money = query.getTransferValue();
            if (money == null)
            {
                throw new ParameterException("转让价格不能为空");
            }
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F07 FROM S60.T6038 WHERE F02 = ? AND F03 = ?");)
            {
                ps.setInt(1, query.getLoanId());
                ps.setInt(2, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        if (EnumParser.parse(OverdueStatus.class, rs.getString(1)) == OverdueStatus.S)
                        {
                            throw new LogicalException("该债权已转出");
                        }
                    }
                    
                }
            }
            try
            {
                serviceResource.openTransactions(connection);
                
                final String sql =
                    "INSERT INTO S60.T6039 ( F02, F03, F04, F05, F06, F07, F08, F09, F13 ) VALUES (?,?,?,?,?,?,?,SYSDATE(),?)";
                insert(connection,
                    sql,
                    query.getTransferId(),
                    query.getLoanId(),
                    serviceResource.getSession().getAccountId(),
                    query.getBidValue(),
                    query.getTransferValue(),
                    query.getOverNum(),
                    query.getOverNum(),
                    query.getMayMoney());

                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S60.T6038 SET F07 = ? WHERE F02 = ? AND F03 = ?"))
                {
                    ps.setString(1, OverdueStatus.S.toString());
                    ps.setInt(2, query.getLoanId());
                    ps.setInt(3, serviceResource.getSession().getAccountId());
                    ps.executeUpdate();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
	/**
	 * 查询预计收益
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */   
	@Override
	public BigDecimal getDslx(int loanId)throws Throwable {
        String sql = "SELECT IFNULL(SUM(F06),0) FROM S60.T6056 WHERE F02 = ? AND F03 = ? AND F10 = ?";
		BigDecimal bigDecimal  = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql)){
				ps.setInt(1,loanId);
				ps.setInt(2, serviceResource.getSession().getAccountId());
				ps.setString(3, DsStatus.WS.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBigDecimal(1);
					}

				}
			}
			return bigDecimal;
		}
	}

	@Override
	public int getCrid() throws Throwable {
		StringBuffer sb = new StringBuffer();
		sb.append(" select max(F02) from  ");
        sb.append(" S60.T6039 where date_format(F09,'%Y-%m')=date_format(?,'%Y-%m') ");
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<Integer>() {
				@Override
				public Integer parse(ResultSet rs) throws SQLException {
					int id = 0;
					if (rs.next()) {
						id = rs.getInt(1);
					}
					return id;
				}
			}, sb.toString(),getCurrentTimestamp(connection));
		}
	}



}
