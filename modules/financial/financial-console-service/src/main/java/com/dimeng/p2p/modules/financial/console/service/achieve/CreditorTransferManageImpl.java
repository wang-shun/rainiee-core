package com.dimeng.p2p.modules.financial.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S61.enums.T6143_F03;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.common.enums.Sex;
import com.dimeng.p2p.modules.financial.console.service.CreditorTransferManage;
import com.dimeng.p2p.modules.financial.console.service.entity.AttestationCheck;
import com.dimeng.p2p.modules.financial.console.service.entity.CreditorInfo;
import com.dimeng.p2p.modules.financial.console.service.entity.RefundInfo;
import com.dimeng.p2p.modules.financial.console.service.entity.RefundRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.TenderInfo;
import com.dimeng.p2p.modules.financial.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferDshEntity;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferFinish;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferProceed;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferRecordInfo;
import com.dimeng.p2p.modules.financial.console.service.entity.ViewLoanRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.ViewTransfer;
import com.dimeng.p2p.modules.financial.console.service.query.TransferDshQuery;
import com.dimeng.p2p.modules.financial.console.service.query.TransferFinishQuery;
import com.dimeng.p2p.modules.financial.console.service.query.TransferProceedQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class CreditorTransferManageImpl extends AbstractFinancialService implements CreditorTransferManage
{
    
    public static class CreditorTransferManageFactory implements ServiceFactory<CreditorTransferManage>
    {
        
        @Override
        public CreditorTransferManage newInstance(ServiceResource serviceResource)
        {
            return new CreditorTransferManageImpl(serviceResource);
        }
    }
    
    public CreditorTransferManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<TransferProceed> transferProceedSearch(TransferProceedQuery transferProceedQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6262.F01,T6230.F03,T6230.F25,T6251.F04,T6231.F03 AS F031,T6230.F09,T6230.F06,T6262.F04 AS F041,T6262.F05,T6260.F07,T6260.F01 AS F011,T6230.F01 AS F012,T6230.F02 "
                    + " FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6262.F02 = T6251.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01"
                    + " WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (transferProceedQuery != null)
        {
            Timestamp timestamp = transferProceedQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = transferProceedQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) <= ?");
                parameters.add(timestamp);
            }
        }
        
        sql.append(" AND T6251.F08 = ? ORDER BY T6262.F07 DESC");
        parameters.add(T6251_F08.S.name());
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TransferProceed>()
            {
                
                @Override
                public TransferProceed[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TransferProceed> list = null;
                    while (resultSet.next())
                    {
                        
                        TransferProceed creditorTransfer = new TransferProceed();
                        creditorTransfer.transferId = resultSet.getInt(1);
                        creditorTransfer.title = resultSet.getString(2);
                        creditorTransfer.creditorId = resultSet.getInt(3);
                        creditorTransfer.saleUserName = getUserName(resultSet.getInt(4));
                        creditorTransfer.residueDeadline = resultSet.getInt(5);
                        creditorTransfer.deadline = resultSet.getInt(6);
                        creditorTransfer.yearRate = resultSet.getDouble(7);
                        creditorTransfer.creditorValue = resultSet.getBigDecimal(8);
                        creditorTransfer.transferPrice = resultSet.getBigDecimal(9);
                        creditorTransfer.creditorTransferState =
                            EnumParser.parse(T6260_F07.class, resultSet.getString(10));
                        creditorTransfer.tempId = resultSet.getInt(11);
                        creditorTransfer.jkId = resultSet.getInt(12);
                        creditorTransfer.userId = resultSet.getInt(13);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditorTransfer);
                    }
                    return list == null ? null : list.toArray(new TransferProceed[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PagingResult<TransferFinish> transferFinishSearch(TransferFinishQuery transferFinishQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6262.F01,T6230.F03,T6230.F25,T6251.F04,T6231.F03 AS F031,T6230.F09,T6230.F06,T6262.F04 AS F041,T6262.F05,T6260.F07,T6260.F01 AS F011,T6230.F01 AS F012,T6230.F02,T6262.F07 AS F071 "
                    + " FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6262.F02 = T6251.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01"
                    + " WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (transferFinishQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            int intValue = transferFinishQuery.getCreditorId();
            if (intValue > 0)
            {
                sql.append(" AND T6230.F25 = ?");
                parameters.add(intValue);
            }
            String string = transferFinishQuery.getTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = transferFinishQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = transferFinishQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) <= ?");
                parameters.add(timestamp);
            }
        }
        
        sql.append(" AND T6251.F08 = ? ORDER BY T6262.F07 DESC");
        parameters.add(T6251_F08.F.name());
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TransferFinish>()
            {
                
                @Override
                public TransferFinish[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TransferFinish> list = null;
                    while (resultSet.next())
                    {
                        
                        TransferFinish creditorTransfer = new TransferFinish();
                        creditorTransfer.transferId = resultSet.getInt(1);
                        creditorTransfer.title = resultSet.getString(2);
                        creditorTransfer.creditorId = resultSet.getInt(3);
                        creditorTransfer.saleUserName = getUserName(resultSet.getInt(4));
                        creditorTransfer.residueDeadline = resultSet.getInt(5);
                        creditorTransfer.deadline = resultSet.getInt(6);
                        creditorTransfer.yearRate = resultSet.getDouble(7);
                        creditorTransfer.creditorValue = resultSet.getBigDecimal(8);
                        creditorTransfer.transferPrice = resultSet.getBigDecimal(9);
                        creditorTransfer.creditorTransferState =
                            EnumParser.parse(T6260_F07.class, resultSet.getString(10));
                        creditorTransfer.tempId = resultSet.getInt(11);
                        creditorTransfer.jkId = resultSet.getInt(12);
                        creditorTransfer.userId = resultSet.getInt(13);
                        creditorTransfer.outTime = resultSet.getTimestamp(14);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditorTransfer);
                    }
                    return list == null ? null : list.toArray(new TransferFinish[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public void shtg(String ids)
        throws Throwable
    {
        if (!StringHelper.isEmpty(ids))
        {
            try (Connection connection = getConnection())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    String[] idArray = ids.split(",");
                    for (String id : idArray)
                    {
                        if (StringHelper.isEmpty(id))
                        {
                            continue;
                        }
                        
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("SELECT T6260.F01 FROM S62.T6260 WHERE T6260.F01 = ? AND T6260.F07 = ? FOR UPDATE"))
                        {
                            pstmt.setInt(1, IntegerParser.parse(id));
                            pstmt.setString(2, T6260_F07.DSH.name());
                            try (ResultSet rs = pstmt.executeQuery())
                            {
                                if (!rs.next())
                                {
                                    throw new LogicalException("债权转让信息不存在！");
                                }
                            }
                        }
                        
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6260 SET T6260.F07 = ? WHERE T6260.F01 = ?"))
                        {
                            
                            pstmt.setString(1, T6260_F07.ZRZ.name());
                            pstmt.setInt(2, IntegerParser.parse(id));
                            
                            pstmt.executeUpdate();
                        }
                    }
                    writeLog(connection, "操作日志", "债权转让审核通过");
                    serviceResource.commit(connection);
                }
                catch (Exception e)
                {
                    serviceResource.rollback(connection);
                    throw e;
                }
            }
        }
    }
    
    @Override
    public void shbtg(String ids)
        throws Throwable
    {
        if (!StringHelper.isEmpty(ids))
        {
            try (Connection connection = getConnection())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    String[] idArray = ids.split(",");
                    for (String id : idArray)
                    {
                        if (StringHelper.isEmpty(id))
                        {
                            continue;
                        }
                        int zqId = 0;
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("SELECT T6260.F02 FROM S62.T6260 WHERE T6260.F01 = ? AND T6260.F07 = ? FOR UPDATE"))
                        {
                            pstmt.setInt(1, IntegerParser.parse(id));
                            pstmt.setString(2, T6260_F07.DSH.name());
                            try (ResultSet rs = pstmt.executeQuery())
                            {
                                if (!rs.next())
                                {
                                    throw new LogicalException("债权转让信息不存在！");
                                }
                                else
                                {
                                    zqId = rs.getInt(1);
                                }
                            }
                        }
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6260 SET T6260.F07 = ?,T6260.F09 = ? WHERE T6260.F01 = ?"))
                        {
                            
                            pstmt.setString(1, T6260_F07.YQX.name());
                            pstmt.setInt(2, getT6231F03(connection, zqId));
                            pstmt.setInt(3, IntegerParser.parse(id));
                            pstmt.executeUpdate();
                        }
                        if (zqId > 0)
                        {
                            try (PreparedStatement pstmt =
                                connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?"))
                            {
                                pstmt.setString(1, T6251_F08.F.name());
                                pstmt.setInt(2, zqId);
                                pstmt.execute();
                            }
                        }
                        
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
    }
    
    @Override
    public ViewTransfer findTransferInfo(int transferId)
        throws Throwable
    {
        if (transferId <= 0)
        {
            return null;
        }
        
        String sql =
            "SELECT T6230.F03,T6230.F02,T6231.F03 AS F031,T6230.F09,T6262.F05,T6231.F06,T6262.F04,T6251.F06,T6260.F03-T6260.F04,T6230.F06,T6230.F10,T6230.F12,T6230.F05,T6231.F09 AS F091,T6262.F01,T6230.F01 AS F011 "
                + "FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6262.F02 = T6251.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6262.F01 = ?";
        ViewTransfer transferInfo = null;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql))
            {
                ps.setInt(1, transferId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        
                        transferInfo = new ViewTransfer();
                        transferInfo.loanRecordTitle = rs.getString(1);
                        transferInfo.userId = rs.getInt(2);
                        transferInfo.userName = getUserName(rs.getInt(2));
                        transferInfo.residueDeadline = rs.getInt(3);
                        transferInfo.loanDeadline = rs.getInt(4);
                        transferInfo.transferPrice = rs.getBigDecimal(5);
                        transferInfo.refundDay = rs.getTimestamp(6);
                        transferInfo.residueValue = rs.getBigDecimal(7);
                        transferInfo.tenderMoney = rs.getBigDecimal(8);
                        transferInfo.gmzx = rs.getBigDecimal(9);
                        transferInfo.yearRate = rs.getDouble(10);
                        transferInfo.repaymentType = EnumParser.parse(T6230_F10.class, rs.getString(11));
                        transferInfo.safeguardWay = EnumParser.parse(T6230_F12.class, rs.getString(12));
                        transferInfo.repaymentMoney = rs.getBigDecimal(13);
                        transferInfo.jkms = rs.getString(14);
                        transferInfo.id = rs.getInt(15);
                        transferInfo.jkId = rs.getInt(16);
                    }
                }
            }
        }
        
        return transferInfo;
    }
    
    @Override
    public ViewLoanRecord findViewLoanRecord(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            return null;
        }
        ViewLoanRecord viewLoanRecord = null;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6110.F01,T6110.F02,T6141.F07 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6110.F01 = ?"))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        viewLoanRecord = new ViewLoanRecord();
                        
                        viewLoanRecord.userId = rs.getInt(1);
                        viewLoanRecord.userName = rs.getString(2);
                        String identityCard = rs.getString(3);
                        int age = 0;
                        if (!StringHelper.isEmpty(identityCard))
                        {
                            identityCard = StringHelper.decode(identityCard);
                            
                            Calendar calendar = Calendar.getInstance();
                            int year1 = calendar.get(Calendar.YEAR);
                            int month1 = calendar.get(Calendar.MONTH) + 1;
                            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                            if (month1 > IntegerParser.parse(identityCard.substring(10, 12)))
                            {
                                age = year1 - IntegerParser.parse(identityCard.substring(6, 10));
                            }
                            else if (month1 == IntegerParser.parse(identityCard.substring(10, 12))
                                && day1 > IntegerParser.parse(identityCard.substring(12, 14)))
                            {
                                age = year1 - IntegerParser.parse(identityCard.substring(6, 10));
                            }
                            else
                            {
                                age = year1 - IntegerParser.parse(identityCard.substring(6, 10)) - 1;
                            }
                            
                            if (Integer.parseInt(identityCard.substring(identityCard.length() - 2,
                                identityCard.length() - 1)) % 2 != 0)
                            {
                                viewLoanRecord.sex = Sex.M;
                            }
                            else
                            {
                                viewLoanRecord.sex = Sex.F;
                            }
                        }
                        viewLoanRecord.age = age;
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                conn.prepareStatement("SELECT F03 FROM S61.T6142 WHERE T6142.F02 = ? ORDER BY T6142.F04 DESC LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        viewLoanRecord.graduateSchool = rs.getString(1);
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                conn.prepareStatement("SELECT F10, F11, F05, F07 FROM S61.T6143 WHERE T6143.F02 = ? AND T6143.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                pstmt.setString(2, T6143_F03.ZZ.name());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        viewLoanRecord.companyBusiness = rs.getString(1);
                        viewLoanRecord.companyScale = rs.getString(2);
                        viewLoanRecord.position = rs.getString(3);
                        viewLoanRecord.workCity = getRegion(rs.getInt(4));
                    }
                }
            }
            
            ArrayList<AttestationCheck> list = null;
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T5123.F02,T6120.F06 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 WHERE T6120.F01=? AND T5123.F04 = ? AND T6120.F05=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F04.QY.name());
                ps.setString(3, T6120_F05.TG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        AttestationCheck attestationCheck = new AttestationCheck();
                        attestationCheck.shxm = rs.getString(1);
                        attestationCheck.rzsj = rs.getTimestamp(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(attestationCheck);
                    }
                }
            }
            
            if (list != null)
            {
                viewLoanRecord.attestationCheck = list.toArray(new AttestationCheck[list.size()]);
            }
        }
        
        return viewLoanRecord;
    }
    
    @Override
    public TenderRecord findTenderRecord(int loanRecordId)
        throws Throwable
    {
        if (loanRecordId < 0)
        {
            return null;
        }
        
        TenderRecord tenderRecord = new TenderRecord();
        try (Connection conn = getConnection())
        {
            ArrayList<TenderInfo> list = null;
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6250.F03,T6250.F04,T6250.F06 FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?"))
            {
                ps.setInt(1, loanRecordId);
                ps.setString(2, T6250_F07.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        TenderInfo tenderInfo = new TenderInfo();
                        tenderInfo.userName = getUserName(rs.getInt(1));
                        tenderInfo.tenderMoney = rs.getBigDecimal(2);
                        tenderInfo.tenderTime = rs.getTimestamp(3);
                        
                        if (tenderRecord.totalMoney == null)
                        {
                            tenderRecord.totalMoney = new BigDecimal("0");
                            tenderRecord.totalMoney = tenderRecord.totalMoney.add(tenderInfo.tenderMoney);
                        }
                        else
                        {
                            tenderRecord.totalMoney = tenderRecord.totalMoney.add(tenderInfo.tenderMoney);
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(tenderInfo);
                    }
                }
            }
            
            if (list != null)
            {
                tenderRecord.tenderRecords = list.toArray(new TenderInfo[list.size()]);
                tenderRecord.totalNumber = list.size();
            }
        }
        return tenderRecord;
    }
    
    @Override
    public RefundRecord findRefundRecord(int loanRecordId)
        throws Throwable
    {
        if (loanRecordId < 0)
        {
            return null;
        }
        
        RefundRecord refundRecord = new RefundRecord();
        try (Connection conn = getConnection())
        {
            ArrayList<RefundInfo> list = null;
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6252.F08,T6252.F09,T6252.F07,T6252.F10 FROM S62.T6252 WHERE T6252.F02 = ?"))
            {
                ps.setInt(1, loanRecordId);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        RefundInfo refundInfo = new RefundInfo();
                        refundInfo.contractRefundTime = rs.getTimestamp(1);
                        refundInfo.state = EnumParser.parse(T6252_F09.class, rs.getString(2));
                        refundInfo.principalInterest = rs.getBigDecimal(3);
                        //TODO 应付罚息
                        refundInfo.defaultInterest = new BigDecimal(0);
                        refundInfo.practicalRefundTime = rs.getTimestamp(4);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(refundInfo);
                    }
                }
            }
            
            refundRecord.refundInfos = list != null ? list.toArray(new RefundInfo[list.size()]) : null;
        }
        return refundRecord;
    }
    
    @Override
    public CreditorInfo[] findCreditorInfo(int loanRecordId)
        throws Throwable
    {
        if (loanRecordId < 0)
        {
            return null;
        }
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F04,T6251.F07 FROM S62.T6251 WHERE T6251.F03=? AND T6251.F08 = ? ORDER BY T6251.F01 DESC");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(loanRecordId);
        parameters.add(T6251_F08.S.name());
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<CreditorInfo>()
            {
                
                @Override
                public CreditorInfo[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<CreditorInfo> list = null;
                    while (rs.next())
                    {
                        CreditorInfo creditorInfo = new CreditorInfo();
                        creditorInfo.userName = getUserName(rs.getInt(1));
                        creditorInfo.tenderTime = rs.getBigDecimal(2).divide(new BigDecimal(100)).intValue();
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditorInfo);
                    }
                    return list == null ? null : list.toArray(new CreditorInfo[list.size()]);
                }
            }, sql.toString(), parameters);
        }
    }
    
    @Override
    public TransferRecord findTransferRecord(int loanRecordId)
        throws Throwable
    {
        if (loanRecordId < 0)
        {
            return null;
        }
        
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6262.F03,T6251.F04,T6262.F04,T6262.F07 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02=T6260.F01 INNER JOIN S62.T6251 ON T6260.F02=T6251.F01 WHERE T6251.F03 = ? ORDER BY T6262.F07 DESC");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(loanRecordId);
        
        TransferRecord transferRecord = new TransferRecord();
        try (Connection connection = getConnection())
        {
            TransferRecordInfo[] transferRecordInfos = selectAll(connection, new ArrayParser<TransferRecordInfo>()
            {
                
                @Override
                public TransferRecordInfo[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<TransferRecordInfo> list = null;
                    while (rs.next())
                    {
                        
                        TransferRecordInfo transferRecordInfo = new TransferRecordInfo();
                        transferRecordInfo.transferCome = getUserName(rs.getInt(1));
                        transferRecordInfo.transferOut = getUserName(rs.getInt(2));
                        transferRecordInfo.dealMoney = rs.getBigDecimal(3);
                        transferRecordInfo.dealTime = rs.getTimestamp(4);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        
                        list.add(transferRecordInfo);
                    }
                    return list == null ? null : list.toArray(new TransferRecordInfo[list.size()]);
                }
            }, sql.toString(), parameters);
            
            if (transferRecordInfos != null)
            {
                for (int i = 0; i < transferRecordInfos.length; i++)
                {
                    TransferRecordInfo transferRecordInfo = transferRecordInfos[i];
                    
                    if (transferRecord.totalDealMoney == null)
                    {
                        transferRecord.totalDealMoney = transferRecordInfo.dealMoney;
                    }
                    else
                    {
                        transferRecord.totalDealMoney = transferRecord.totalDealMoney.add(transferRecordInfo.dealMoney);
                    }
                }
            }
            
            transferRecord.refundInfos = transferRecordInfos;
            
            return transferRecord;
        }
    }
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private String getRegion(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06,F07,F08 FROM T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sb.append(rs.getString(1));
                        sb.append(",");
                        sb.append(rs.getString(2));
                        sb.append(",");
                        sb.append(rs.getString(3));
                    }
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * 查询用户账号
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private String getUserName(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        
        String str = "";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        str = rs.getString(1);
                    }
                }
            }
        }
        return str;
    }
    
    @Override
    public PagingResult<TransferDshEntity> transferDshSearch(TransferDshQuery transferDshQuery, final T6260_F07 f07,
        Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameter = new ArrayList<>();
        StringBuffer sbSQL = new StringBuffer(512);
        
        sbSQL.append(" SELECT  S62.T6260.F01 F01,S62.T6251.F02 F02,S61.T6110.F02 F03, S62.T6231.F02 F04,S62.T6231.F03 F05,S62.T6230.F06 F06,S62.T6251.F07 F07, S62.T6260.F03 F08,S62.T6260.F08 F09,S62.T6260.F05 F10,S62.T6260.F04 F11,S62.T6251.F03 F12,S62.T6230.F03 F13,S62.T6230.F25 F14,S62.T6260.F09 F15  FROM S62.T6260");
        sbSQL.append(" LEFT JOIN S62.T6251 ON S62.T6260.F02 = S62.T6251.F01");
        sbSQL.append(" LEFT JOIN S61.T6110 ON S61.T6110.F01 = S62.T6251.F04");
        sbSQL.append(" LEFT JOIN S62.T6230 ON T6251.F03 = T6230.F01");
        sbSQL.append(" LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01");
        sbSQL.append(" WHERE S62.T6260.F07 = ? ");
        parameter.add(f07);
        transferDshParameter(sbSQL, transferDshQuery, parameter);
        sbSQL.append(" ORDER BY S62.T6260.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TransferDshEntity>()
            {
                @Override
                public TransferDshEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TransferDshEntity> list = new ArrayList<TransferDshEntity>();
                    while (resultSet.next())
                    {
                        try
                        {
                            TransferDshEntity record = new TransferDshEntity();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getString(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getInt(4);
                            record.F05 = resultSet.getInt(5);
                            record.F06 = resultSet.getBigDecimal(6);
                            record.F07 = resultSet.getBigDecimal(7);
                            record.F08 = resultSet.getBigDecimal(8);
                            record.F09 = resultSet.getBigDecimal(9);
                            record.F10 = resultSet.getTimestamp(10);
                            record.F11 = resultSet.getBigDecimal(11);
                            record.F12 = resultSet.getInt(12);
                            record.F13 = resultSet.getString(13);
                            record.F14 = resultSet.getString(14);
                            if (T6260_F07.YJS == f07)
                            {
                                record.F05 = resultSet.getInt(15);
                            }
                            list.add(record);
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                    }
                    return list.isEmpty() ? null : list.toArray(new TransferDshEntity[list.size()]);
                }
            }, paging, sbSQL.toString(), parameter);
        }
    }
    
    @Override
    public TransferDshEntity transferDshAmount(TransferDshQuery transferDshQuery, T6260_F07 f07)
        throws Throwable
    {
        List<Object> parameter = new ArrayList<Object>();
        StringBuffer sbSQL = new StringBuffer(512);
        
        sbSQL.append(" SELECT IFNULL(SUM(S62.T6260.F04),0) AS F01,IFNULL(SUM(S62.T6260.F03),0) AS F02 FROM S62.T6260");
        sbSQL.append(" LEFT JOIN S62.T6251 ON S62.T6260.F02 = S62.T6251.F01");
        sbSQL.append(" LEFT JOIN S61.T6110 ON S61.T6110.F01 = S62.T6251.F04");
        sbSQL.append(" LEFT JOIN S62.T6230 ON T6251.F03 = T6230.F01");
        sbSQL.append(" LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01");
        sbSQL.append(" WHERE S62.T6260.F07 = ? ");
        parameter.add(f07);
        // sql语句和查询参数处理
        transferDshParameter(sbSQL, transferDshQuery, parameter);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<TransferDshEntity>()
            {
                @Override
                public TransferDshEntity parse(ResultSet resultSet)
                    throws SQLException
                {
                    TransferDshEntity count = new TransferDshEntity();
                    if (resultSet.next())
                    {
                        count.F07 = resultSet.getBigDecimal(1);
                        count.F08 = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sbSQL.toString(), parameter);
        }
    }
    
    private void transferDshParameter(StringBuffer sbSQL, TransferDshQuery transferDshQuery, List<Object> parameter)
        throws ResourceNotFoundException, SQLException
    {
        if (transferDshQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            Timestamp timestamp = transferDshQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sbSQL.append(" AND DATE(S62.T6260.F05) >= ? ");
                parameter.add(timestamp);
            }
            timestamp = transferDshQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sbSQL.append(" AND DATE(S62.T6260.F05) <= ?");
                parameter.add(timestamp);
            }
            
            //过滤条件 债权id 匹配查询， 债权转让者模糊查询
            String string = transferDshQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sbSQL.append(" AND S62.T6251.F02 LIKE ?");
                parameter.add(sqlConnectionProvider.allMatch(string));
            }
            string = transferDshQuery.getCreditorOwner();
            if (!StringHelper.isEmpty(string))
            {
                sbSQL.append(" AND S61.T6110.F02 LIKE ?");
                parameter.add(sqlConnectionProvider.allMatch(string));
            }
            
            String LoanId = transferDshQuery.getLoanId();
            if (!StringHelper.isEmpty(LoanId))
            {
                sbSQL.append(" AND S62.T6230.F25 LIKE ?");
                parameter.add(sqlConnectionProvider.allMatch(LoanId));
            }
            
            String LoanTitle = transferDshQuery.getLoanTitle();
            if (!StringHelper.isEmpty(LoanTitle))
            {
                sbSQL.append(" AND S62.T6230.F03 LIKE ?");
                parameter.add(sqlConnectionProvider.allMatch(LoanTitle));
            }
        }
    }
    
    @Override
    public void zqzrxj(String ids)
        throws Throwable
    {
        if (!StringHelper.isEmpty(ids))
        {
            try (Connection connection = getConnection())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    String[] idArray = ids.split(",");
                    for (String id : idArray)
                    {
                        if (StringHelper.isEmpty(id))
                        {
                            continue;
                        }
                        int zqId = 0;
                        int userId = 0;
                        String zqNumber = "";
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("SELECT T6260.F02 AS F01, T6251.F02 AS F02, T6251.F04 AS F03 FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02=T6251.F01 WHERE T6260.F01 = ? AND T6260.F07 = ? FOR UPDATE"))
                        {
                            pstmt.setInt(1, IntegerParser.parse(id));
                            pstmt.setString(2, T6260_F07.ZRZ.name());
                            try (ResultSet rs = pstmt.executeQuery())
                            {
                                if (!rs.next())
                                {
                                    throw new LogicalException("债权转让信息不存在！");
                                }
                                else
                                {
                                    zqId = rs.getInt(1);
                                    zqNumber = rs.getString(2);
                                    userId = rs.getInt(3);
                                }
                            }
                        }
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6260 SET T6260.F07 = ?,T6260.F09 = ? WHERE T6260.F01 = ?"))
                        {
                            
                            pstmt.setString(1, T6260_F07.YXJ.name());
                            pstmt.setInt(2, getT6231F03(connection, zqId));
                            pstmt.setInt(3, IntegerParser.parse(id));
                            pstmt.executeUpdate();
                        }
                        
                        if (zqId > 0)
                        {
                            try (PreparedStatement pstmt =
                                connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?"))
                            {
                                pstmt.setString(1, T6251_F08.F.name());
                                pstmt.setInt(2, zqId);
                                pstmt.execute();
                            }
                        }
                        T6110 t6110 = selectT6110(connection, userId);
                        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                        Envionment envionment = configureProvider.createEnvionment();
                        envionment.set("userName", t6110.F02);
                        envionment.set("title", zqNumber);
                        String content = configureProvider.format(LetterVariable.ZQ_MANUAL_CANCEL, envionment);
                        sendLetter(connection, userId, "手动下架债权", content);
                    }
                    writeLog(connection, "操作日志", "债权下架");
                    serviceResource.commit(connection);
                }
                catch (Exception e)
                {
                    serviceResource.rollback(connection);
                    throw e;
                }
            }
        }
    }
    
    @Override
    protected T6110 selectT6110(Connection connection, int F01)
        throws SQLException
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = T6110_F06.parse(resultSet.getString(5));
                        record.F07 = T6110_F07.parse(resultSet.getString(6));
                        record.F08 = T6110_F08.parse(resultSet.getString(7));
                        record.F09 = resultSet.getTimestamp(8);
                        record.F10 = T6110_F10.parse(resultSet.getString(9));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return record;
    }
    
    @Override
    public TransferDshEntity transferZrsbAmount(TransferDshQuery transferDshQuery)
        throws Throwable
    {
        List<Object> parameter = new ArrayList<Object>();
        StringBuffer sbSQL = new StringBuffer(512);
        
        sbSQL.append(" SELECT IFNULL(SUM(S62.T6260.F04),0) AS F01,IFNULL(SUM(S62.T6260.F03),0) AS F02 FROM S62.T6260");
        sbSQL.append(" LEFT JOIN S62.T6251 ON S62.T6260.F02 = S62.T6251.F01");
        sbSQL.append(" LEFT JOIN S61.T6110 ON S61.T6110.F01 = S62.T6251.F04");
        sbSQL.append(" LEFT JOIN S62.T6230 ON T6251.F03 = T6230.F01");
        sbSQL.append(" LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01");
        sbSQL.append(" WHERE S62.T6260.F07 IN (?,?) ");
        parameter.add(T6260_F07.YQX.name());
        parameter.add(T6260_F07.YXJ.name());
        // sql语句和查询参数处理
        transferDshParameter(sbSQL, transferDshQuery, parameter);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<TransferDshEntity>()
            {
                @Override
                public TransferDshEntity parse(ResultSet resultSet)
                    throws SQLException
                {
                    TransferDshEntity count = new TransferDshEntity();
                    if (resultSet.next())
                    {
                        count.F07 = resultSet.getBigDecimal(1);
                        count.F08 = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sbSQL.toString(), parameter);
        }
    }
    
    @Override
    public PagingResult<TransferDshEntity> transferZrsbSearch(TransferDshQuery transferDshQuery, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameter = new ArrayList<>();
        StringBuffer sbSQL = new StringBuffer(512);
        
        sbSQL.append(" SELECT  S62.T6260.F01 F01,S62.T6251.F02 F02,S61.T6110.F02 F03, S62.T6231.F02 F04,S62.T6260.F09 F05,S62.T6230.F06 F06,S62.T6251.F07 F07, S62.T6260.F03 F08,S62.T6260.F08 F09,S62.T6260.F05 F10,S62.T6260.F04 F11,S62.T6251.F03 F12,S62.T6230.F03 F13,S62.T6230.F25 F14  FROM S62.T6260");
        sbSQL.append(" LEFT JOIN S62.T6251 ON S62.T6260.F02 = S62.T6251.F01");
        sbSQL.append(" LEFT JOIN S61.T6110 ON S61.T6110.F01 = S62.T6251.F04");
        sbSQL.append(" LEFT JOIN S62.T6230 ON T6251.F03 = T6230.F01");
        sbSQL.append(" LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01");
        sbSQL.append(" WHERE S62.T6260.F07 IN (?,?) ");
        parameter.add(T6260_F07.YQX.name());
        parameter.add(T6260_F07.YXJ.name());
        transferDshParameter(sbSQL, transferDshQuery, parameter);
        sbSQL.append(" ORDER BY S62.T6260.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TransferDshEntity>()
            {
                @Override
                public TransferDshEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TransferDshEntity> list = new ArrayList<TransferDshEntity>();
                    while (resultSet.next())
                    {
                        try
                        {
                            TransferDshEntity record = new TransferDshEntity();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getString(2);
                            record.F03 = resultSet.getString(3);
                            record.F04 = resultSet.getInt(4);
                            record.F05 = resultSet.getInt(5);
                            record.F06 = resultSet.getBigDecimal(6);
                            record.F07 = resultSet.getBigDecimal(7);
                            record.F08 = resultSet.getBigDecimal(8);
                            record.F09 = resultSet.getBigDecimal(9);
                            record.F10 = resultSet.getTimestamp(10);
                            record.F11 = resultSet.getBigDecimal(11);
                            record.F12 = resultSet.getInt(12);
                            record.F13 = resultSet.getString(13);
                            record.F14 = resultSet.getString(14);
                            list.add(record);
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                    }
                    return list.isEmpty() ? null : list.toArray(new TransferDshEntity[list.size()]);
                }
            }, paging, sbSQL.toString(), parameter);
        }
    }
    
    private int getT6231F03(Connection conn, int t6251F01)
        throws Throwable
    {
        
        String sql = "SELECT T6231.F03 FROM S62.T6231 JOIN S62.T6251 ON T6231.F01=T6251.F03 WHERE T6251.F01=? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, t6251F01);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
}
