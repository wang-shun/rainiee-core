package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.account.user.service.AgreementManage;
import com.dimeng.p2p.account.user.service.entity.Borrower;
import com.dimeng.p2p.account.user.service.entity.CreditDetail;
import com.dimeng.p2p.account.user.service.entity.CreditTrs;
import com.dimeng.p2p.account.user.service.entity.JoinedUser;
import com.dimeng.p2p.account.user.service.entity.Organization;
import com.dimeng.p2p.account.user.service.entity.YxDetail;
import com.dimeng.p2p.account.user.service.entity.YxJoined;
import com.dimeng.p2p.account.user.service.entity.YxUser;
import com.dimeng.p2p.account.user.service.entity.ZqzrCreditDetail;
import com.dimeng.p2p.account.user.service.entity.ZqzrInfo;
import com.dimeng.p2p.account.user.service.entity.ZqzrUser;
import com.dimeng.util.StringHelper;

public class AgreementManageImpl extends AbstractAccountService implements
		AgreementManage {

	public AgreementManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class AgreementManageFactory implements
			ServiceFactory<AgreementManage> {

		@Override
		public AgreementManage newInstance(ServiceResource serviceResource) {
			return new AgreementManageImpl(serviceResource);
		}

	}

	@Override
    public JoinedUser[] getJoinedUser(final int creditId)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            return selectAll(connection,
                new ArrayParser<JoinedUser>()
                {

                    @Override
                    public JoinedUser[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<JoinedUser> joinedUsers = null;
                        while (resultSet.next())
                        {
                            if (joinedUsers == null)
                            {
                                joinedUsers = new ArrayList<>();
                            }
                            JoinedUser joinedUser = new JoinedUser();
                            joinedUser.limitMonth = resultSet.getInt(1);
                            joinedUser.amount = resultSet.getBigDecimal(2);
                            joinedUser.userName = resultSet.getString(3);

                            try
                            {
                                joinedUser.monthAmount = getYjlxje(connection,creditId, FeeCode.TZ_LX);
                            }
                            catch (Throwable e)
                            {
                                logger.error("AgreementManageImpl.getJoinedUser() error", e);
                            }

                            joinedUsers.add(joinedUser);
                        }
                        return joinedUsers == null ? null : joinedUsers.toArray(new JoinedUser[joinedUsers.size()]);
                    }
                },
                "SELECT T6230.F09 AS F01, T6250.F04 AS F02, T6110.F02 AS F03 FROM S62.T6230 INNER JOIN S62.T6250 ON T6230.F01 = T6250.F02 INNER JOIN S61.T6110 ON T6110.F01 = T6250.F03 WHERE T6250.F02 = ?",
                creditId);
        }catch (Throwable e){
			logger.error("AgreementManageImpl.getJoinedUser() error", e);
			throw e;
		}
    }

	private BigDecimal getYjlxje(Connection connection,int loanId, int feeCode) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F04 = ? AND F05  = ? AND F02 = ?")) {
			pstmt.setInt(1, serviceResource.getSession().getAccountId());
			pstmt.setInt(2, feeCode);
			pstmt.setInt(3, loanId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getBigDecimal(1);
				}
			}
		}
		return new BigDecimal(0);
	}

	@Override
	public Borrower getBorrower(int creditId) throws Throwable {
		int accountId = 0;
		Borrower borrower = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S62.T6230 WHERE F01=?")) {
				ps.setInt(1, creditId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (borrower == null) {
							borrower = new Borrower();
						}
						borrower.accountName = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F07 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (borrower == null) {
							borrower = new Borrower();
						}
						borrower.realName = resultSet.getString(1);
						borrower.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return borrower;
	}

	@Override
    public CreditDetail getCreditDetail(final int creditId)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<CreditDetail>()
                {
                    
                    @Override
                    public CreditDetail parse(ResultSet resultSet)
                        throws SQLException
                    {
                        CreditDetail creditDetail = null;
                        if (resultSet.next())
                        {
                            if (creditDetail == null)
                            {
                                creditDetail = new CreditDetail();
                            }
                            creditDetail.rate = resultSet.getBigDecimal(1);
                            creditDetail.limitMonth = resultSet.getInt(2);
                            creditDetail.amount = resultSet.getBigDecimal(3);
                            creditDetail.use = resultSet.getString(4);
                            creditDetail.startDate = resultSet.getTimestamp(5);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(creditDetail.startDate.getTime());
                            calendar.add(Calendar.MONTH, creditDetail.limitMonth);
                            creditDetail.endDate = new Timestamp(calendar.getTimeInMillis());
                            try
                            {
                                creditDetail.monthAmount = getJkrlxje(connection,creditId, FeeCode.TZ_LX);
                            }
                            catch (Throwable e)
                            {
                                logger.error("AgreementManageImpl.getCreditDetail() error", e);
                            }
                        }
                        return creditDetail;
                    }
                },
                "SELECT T6230.F06 AS F01, T6230.F09 AS F02, T6230.F26 AS F03, T6231.F08 AS F04, T6231.F12 AS F05 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F01 = ? LIMIT 1",
                creditId);
        }
    }

	private BigDecimal getJkrlxje(Connection connection, int loanId, int feeCode) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ? AND F05  = ? AND F02 = ?")) {
			pstmt.setInt(1, serviceResource.getSession().getAccountId());
			pstmt.setInt(2, feeCode);
			pstmt.setInt(3, loanId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getBigDecimal(1);
				}
			}
		}
		return new BigDecimal(0);
	}

	@Override
    public CreditTrs[] getCreditTransfers(int creditId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            CreditTrs[] creditTransfers =
                selectAll(connection,
                    new ArrayParser<CreditTrs>()
                    {
                        
                        @Override
                        public CreditTrs[] parse(ResultSet resultSet)
                            throws SQLException
                        {
                            List<CreditTrs> transfers = null;
                            while (resultSet.next())
                            {
                                if (transfers == null)
                                {
                                    transfers = new ArrayList<>();
                                }
                                CreditTrs transfer = new CreditTrs();
                                transfer.inId = resultSet.getInt(1);
                                transfer.amount = resultSet.getBigDecimal(2);
                                transfer.time = resultSet.getTimestamp(3);
                                transfer.outId = resultSet.getInt(4);
                                transfers.add(transfer);
                            }
                            return transfers == null ? null : transfers.toArray(new CreditTrs[transfers.size()]);
                        }
                    },
                    "SELECT T6262.F03 AS F01, T6262.F04 AS F02, T6262.F07 AS F03, T6251.F04 AS F04 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6260.F02 WHERE T6251.F03=?",
                    creditId);
            if (creditTransfers == null || creditTransfers.length <= 0)
            {
                return null;
            }
            String sql = "SELECT F02 FROM S61.T6110 WHERE F01=?";
            for (CreditTrs creditTransfer : creditTransfers)
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ps.setInt(1, creditTransfer.outId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            creditTransfer.out = resultSet.getString(1);
                        }
                    }
                }
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ps.setInt(1, creditTransfer.inId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            creditTransfer.in = resultSet.getString(1);
                        }
                    }
                }
            }
            return creditTransfers;
        }
    }

	@Override
    public Organization getOrganization(int creditId)
        throws Throwable
    {
        int contractId = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE F02=? LIMIT 1"))
            {
                ps.setInt(1, creditId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        contractId = resultSet.getInt(1);
                    }
                }
            }
            if (contractId <= 0)
            {
                return null;
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6161.F04,T6164.F03 FROM S61.T6161 LEFT JOIN S61.T6164 ON T6161.F01 = T6164.F01  WHERE T6161.F01=?"))
            {
                ps.setInt(1, contractId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        Organization org = new Organization();
                        org.name = resultSet.getString(1);
                        org.address = resultSet.getString(2);
                        return org;
                    }
                }
            }
        }
        return null;
    }

	@Override
	public YxUser getYxUser() throws Throwable {
		if (serviceResource.getSession() == null
				|| !serviceResource.getSession().isAuthenticated()) {
			return null;
		}
		int accountId = serviceResource.getSession().getAccountId();
		YxUser yxUser = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6110.F04,T6141.F02,T6141.F07 FROM S61.T6110 LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01  WHERE T6110.F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						yxUser = new YxUser();
						yxUser.phone = resultSet.getString(1);
						yxUser.name = resultSet.getString(2);
						yxUser.identifyId = StringHelper.decode(resultSet
								.getString(3));
					}
				}
			}
		}
		return yxUser;
	}

	@Override
	public YxJoined[] getYxJoineds(int yxID) throws Throwable {
		if (yxID <= 0) {
			return null;
		}
		List<YxJoined> joineds = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F04,F06 FROM S64.T6411 WHERE F02=? AND F03=?")) {
				ps.setInt(1, yxID);
				ps.setInt(2, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						if (joineds == null) {
							joineds = new ArrayList<>();
						}
						YxJoined joined = new YxJoined();
						joined.amount = resultSet.getBigDecimal(1);
						joined.time = resultSet.getTimestamp(2);
						joineds.add(joined);
					}
				}
			}
		}
		return joineds == null ? null : joineds.toArray(new YxJoined[joineds
				.size()]);
	}

	@Override
	public YxDetail getYxDeadline(int yxID) throws Throwable {
		if (yxID <= 0) {
			return null;
		}
		YxDetail detail = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F09,F10,F11,F15,F16,F17,F22,F23,F12 FROM S64.T6410 WHERE F01=?")) {
				ps.setInt(1, yxID);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						detail = new YxDetail();
						detail.title = resultSet.getString(1);
						detail.sqks = resultSet.getTimestamp(2);
						detail.sqjs = resultSet.getTimestamp(3);
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(detail.sqjs.getTime());
						calendar.add(Calendar.MONTH, resultSet.getInt(4));
						detail.lcjs = new Timestamp(calendar.getTimeInMillis());
						detail.jrfl = resultSet.getBigDecimal(5);
						detail.fwfl = resultSet.getBigDecimal(6);
						detail.tcfl = resultSet.getBigDecimal(7);
						detail.low = resultSet.getBigDecimal(8);
						detail.upper = resultSet.getBigDecimal(9);
						detail.mesj = resultSet.getTimestamp(10);
					}
				}
			}
		}
		return detail;
	}

	@Override
	public ZqzrUser getZcz(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		int accountId = 0;
		ZqzrUser zqzrUser = new ZqzrUser();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6251.F04 AS F04 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6260.F02 WHERE T6262.F01=?")) {
				ps.setInt(1, id);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.account = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F07 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.name = resultSet.getString(1);
						zqzrUser.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return zqzrUser;
	}

	@Override
	public ZqzrUser getZrz(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		int accountId = 0;
		ZqzrUser zqzrUser = new ZqzrUser();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F03 FROM S62.T6262 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.account = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F07 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.name = resultSet.getString(1);
						zqzrUser.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return zqzrUser;
	}

	private int getId(Connection connection, int id) throws Throwable {
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT T6251.F03 AS F04 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6260.F02 WHERE T6262.F01=?")) {
			ps.setInt(1, id);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

	@Override
    public ZqzrCreditDetail getZqzrCreditDetail(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }

        

        try (final Connection connection = getConnection())
        {
			final int creditId = getId(connection,id);
			if (creditId <= 0)
			{
				return null;
			}
            ZqzrCreditDetail creditDetail =
                select(connection,
                    new ItemParser<ZqzrCreditDetail>()
                    {
                        
                        @Override
                        public ZqzrCreditDetail parse(ResultSet resultSet)
                            throws SQLException
                        {
                            ZqzrCreditDetail creditDetail = null;
                            if (resultSet.next())
                            {
                                if (creditDetail == null)
                                {
                                    creditDetail = new ZqzrCreditDetail();
                                }
                                creditDetail.bName = resultSet.getString(1);
                                creditDetail.amount = resultSet.getBigDecimal(2);
                                creditDetail.limitMonth = resultSet.getInt(3);
                                
                                creditDetail.startDate = resultSet.getTimestamp(5);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(creditDetail.startDate.getTime());
                                calendar.add(Calendar.MONTH, creditDetail.limitMonth);
                                creditDetail.endDate = new Timestamp(calendar.getTimeInMillis());
                                creditDetail.rate = resultSet.getBigDecimal(6);
                                creditDetail.id = resultSet.getInt(7);
                                
                                try
                                {
                                    creditDetail.monthAmount = getJkrlxje(connection,creditId, FeeCode.TZ_LX);
                                }
                                catch (Throwable e)
                                {
                                    logger.error("AgreementManageImpl.getZqzrCreditDetail() error", e);
                                }
                            }
                            return creditDetail;
                        }
                    },
                    "SELECT T6141.F02,T6230.F26,T6230.F09,T6231.F12,T6230.F06,T6230.F01 FROM S62.T6230,S61.T6141,S62.T6231 WHERE T6230.F01=T6231.F01 AND T6230.F02=T6141.F01 AND T6230.F01=?",
                    creditId);
            return creditDetail;
        }
    }

	@Override
    public ZqzrInfo getZqzrInfo(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<ZqzrInfo>()
                {
                    
                    @Override
                    public ZqzrInfo parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ZqzrInfo zqzrInfo = null;
                        if (resultSet.next())
                        {
                            zqzrInfo = new ZqzrInfo();
                            zqzrInfo.zqjz = resultSet.getBigDecimal(1).multiply(resultSet.getBigDecimal(6));
                            zqzrInfo.zrjk = resultSet.getBigDecimal(2).multiply(resultSet.getBigDecimal(6));
                            zqzrInfo.zrglf = resultSet.getBigDecimal(3);
                            zqzrInfo.zrsj = resultSet.getTimestamp(4);
                        }
                        return zqzrInfo;
                    }
                },
                "SELECT T6262.F05 AS F01,T6262.F04 AS F02,T6262.F06 AS F03,T6262.F07 AS F04 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6260.F02 WHERE T6262.F01=?",
                id);
        }
    }
}
