/*
 * 文 件 名:  OperateDataManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.modules.regulators.policy.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.S61.entities.T6197;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S70.entities.T7054;
import com.dimeng.p2p.common.enums.InvestType;
import com.dimeng.p2p.repeater.policy.OperateDataManage;
import com.dimeng.p2p.repeater.policy.entity.AgeDistributionEntity;
import com.dimeng.p2p.repeater.policy.entity.InvestmentLoanEntity;
import com.dimeng.p2p.repeater.policy.entity.PlatformRiskControlEntity;
import com.dimeng.p2p.repeater.policy.entity.StaticEntity;
import com.dimeng.p2p.repeater.policy.entity.VolumeTimeLimit;
import com.dimeng.p2p.repeater.policy.entity.VolumeType;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * <运营数据管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月10日]
 */
public class OperateDataManageImpl extends AbstractPolicyService implements OperateDataManage
{
    
    public OperateDataManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T6196 getT6196()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return getT6196(connection);
        }
    }
    
    private T6196 getT6196(Connection connection)
        throws Throwable
    {
        
        T6196 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21,F22,F23,F24,F25,F26,F27,F28,F29,F30,F31,F32,F33,F34,F35,F36,F37,F38,F39,F40,F41,F42,F43,F44,F45,F46 FROM S61.T6196 LIMIT 1"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6196();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getInt(10);
                    record.F11 = resultSet.getInt(11);
                    record.F12 = resultSet.getInt(12);
                    record.F13 = resultSet.getBigDecimal(13);
                    record.F14 = resultSet.getBigDecimal(14);
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getBigDecimal(16);
                    record.F17 = resultSet.getBigDecimal(17);
                    record.F18 = resultSet.getBigDecimal(18);
                    record.F19 = resultSet.getBigDecimal(19);
                    record.F20 = resultSet.getBigDecimal(20);
                    record.F21 = resultSet.getBigDecimal(21);
                    record.F22 = resultSet.getBigDecimal(22);
                    record.F23 = resultSet.getBigDecimal(23);
                    record.F24 = resultSet.getBigDecimal(24);
                    record.F25 = resultSet.getBigDecimal(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.F27 = resultSet.getBigDecimal(27);
                    record.F28 = resultSet.getBigDecimal(28);
                    record.F29 = resultSet.getTimestamp(29);
                    record.F30 = resultSet.getBigDecimal(30);
                    record.F31 = resultSet.getBigDecimal(31);
                    record.F32 = resultSet.getInt(32);
                    record.F33 = resultSet.getBigDecimal(33);
                    record.F34 = resultSet.getInt(34);
                    record.F35 = resultSet.getInt(35);
                    record.F36 = resultSet.getInt(36);
                    record.F37 = resultSet.getInt(37);
                    record.F38 = resultSet.getBigDecimal(38);
                    record.F39 = resultSet.getBigDecimal(39);
                    record.F40 = resultSet.getBigDecimal(40);
                    record.F41 = resultSet.getInt(41);
                    record.F42 = resultSet.getInt(42);
                    record.F43 = resultSet.getBigDecimal(43);
                    record.F44 = resultSet.getBigDecimal(44);
                    record.F45 = resultSet.getInt(45);
                    record.F46 = resultSet.getInt(46);
                }
            }
        }
        return record;
    }
    
    @Override
    public List<T6197> getT6197List()
        throws Throwable
    {
        
        ArrayList<T6197> list = null;
        try (Connection connection = getConnection();
            PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04 FROM S61.T6197 WHERE EXTRACT(YEAR_MONTH FROM F03)<= EXTRACT(YEAR_MONTH FROM CURRENT_DATE()) ORDER BY F03 DESC LIMIT 0,12");)
        {
            T6197 record = null;
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    record = new T6197();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getDate(3);
                    record.F04 = resultSet.getTimestamp(4);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
                return gainT6197DataList(list);
            }
        }
    }
    
    private ArrayList<T6197> gainT6197DataList(ArrayList<T6197> list)
    {
        
        if (list == null)
        {
            list = new ArrayList<>();
            return gainT6197DataList(list, 0);
        }
        return gainT6197DataList(list, 12);
    }
    
    private ArrayList<T6197> gainT6197DataList(ArrayList<T6197> list, int size)
    {
        
        T6197 t6197 = null;
        Calendar calend = null;
        ArrayList<T6197> t6197List = new ArrayList<T6197>();
        if (size != 0)
        {
            Date F03 = list.get(0).F03;
            if (!beforeOrMatchDate(new java.util.Date(), F03))
            {
                int monthNum = getMonthNum(F03);//相差几个月
                for (int i = 0; i < monthNum && i < 12; i++)
                {
                    gainT6197DataList(t6197List, calend, t6197, i);
                }
                for (T6197 t : list)
                {
                    if (t6197List.size() >= 12)
                    {
                        return t6197List;
                    }
                    t6197List.add(t);
                }
                int t6197Size = t6197List.size();
                if (t6197Size < 12)
                {
                    for (int i = t6197Size; i < 12; i++)
                    {
                        gainT6197DataList(t6197List, calend, t6197, i);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < 12; i++)
            {
                gainT6197DataList(t6197List, calend, t6197, i);
            }
        }
        return t6197List;
    }
    
    private boolean beforeOrMatchDate(java.util.Date date1, java.util.Date date2)
    {
        int time1 = IntegerParser.parse(DateParser.format(date1, "yyyyMM"));
        int time2 = IntegerParser.parse(DateParser.format(date2, "yyyyMM"));
        if (time1 < time2)
        {
            return true;
        }
        return false;
    }
    
    private void gainT6197DataList(ArrayList<T6197> t6197List, Calendar calend, T6197 t6197, int i)
    {
        calend = Calendar.getInstance();
        calend.add(Calendar.MONTH, -i);
        t6197 = new T6197();
        t6197.F02 = BigDecimal.ZERO;
        t6197.F03 = new Date(calend.getTime().getTime());
        t6197List.add(t6197);
    }
    
    /**
     * 两个时间相差月份数
     * @param operateData
     * @return
     * @throws Throwable
     */
    private int getMonthNum(Date F03)
    {
        Calendar nowCalendar = Calendar.getInstance();
        Calendar f03Calendar = Calendar.getInstance();
        f03Calendar.setTime(F03);
        return ((nowCalendar.get(Calendar.YEAR) - f03Calendar.get(Calendar.YEAR)) * 12 + (nowCalendar.get(Calendar.MONTH) - f03Calendar.get(Calendar.MONTH)));
    }
    
    /**
     * 更新运营数据
     * @param operateData
     * @return
     * @throws Throwable
     */
    @Override
    public void updateOperateData(T6196 t6196, String[] amounts)
        throws Throwable
    {
        
        if (amounts == null || amounts.length <= 0)
        {
            throw new ParameterException("运营数据参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp now = getCurrentTimestamp(connection);
                execute(connection, "DELETE FROM S61.T6197");
                insterT6197(connection, amounts, now);
                T6196 t6196Data = getT6196(connection);
                if (null == t6196Data)
                {
                    throw new ParameterException("运营数据不存在");
                }
                execute(connection,
                    "UPDATE S61.T6196 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,"
                        + "F11=?,F12=?,F13=?,F14=?,F15=?,F16=?,F17=?,F18=?,F19=?,F20=?,F21=?,F22=?,F23=?,F24=?,F25=?,"
                        + "F26=?,F27=?,F28=?,F29=?,F30=?,F31=?,F32=?,F33=?,F34=?,F35=?,F36=?,F37=?,F38=?,F39=?,F40=? "
                        + ",F41=?,F42=?,F43=?,F44=?,F45=?,F46=? " + "WHERE F01 = ?",
                    t6196.F02,
                    t6196.F03,
                    t6196.F04,
                    t6196.F05,
                    t6196.F06,
                    t6196.F07,
                    t6196.F08,
                    t6196.F09,
                    t6196.F10,
                    t6196.F11,
                    t6196.F12,
                    t6196.F13,
                    t6196.F14,
                    t6196.F15,
                    t6196.F16,
                    t6196.F17,
                    t6196.F18,
                    t6196.F19,
                    t6196.F20,
                    t6196.F21,
                    t6196.F22,
                    t6196.F23,
                    t6196.F24,
                    t6196.F25,
                    t6196.F26,
                    t6196.F27,
                    t6196.F28,
                    now,
                    t6196.F30,
                    t6196.F31,
                    t6196.F32,
                    t6196.F33,
                    t6196.F34,
                    t6196.F35,
                    t6196.F36,
                    t6196.F37,
                    t6196.F38,
                    t6196.F39,
                    t6196.F40,
                    t6196.F41,
                    t6196.F42,
                    t6196.F43,
                    t6196.F44,
                    t6196.F45,
                    t6196.F46,
                    t6196Data.F01);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private void insterT6197(Connection connection, String[] amounts, Timestamp now)
        throws Throwable
    {
        int length = amounts.length;
        Calendar calend = null;
        for (int i = 0; i < length; i++)
        {
            calend = Calendar.getInstance();
            calend.add(Calendar.MONTH, -i);
            String insterT6340_sql = "INSERT INTO S61.T6197 SET F02=?,F03=?,F04=?";
            try (PreparedStatement ps = connection.prepareStatement(insterT6340_sql, Statement.RETURN_GENERATED_KEYS))
            {
                ps.setBigDecimal(1, new BigDecimal(amounts[length - 1 - i]));
                ps.setDate(2, new Date(calend.getTime().getTime()));
                ps.setTimestamp(3, now);
                ps.execute();
            }
        }
    }
    
    @Override
    public int getRegisterUser()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1)-1 FROM S61.T6110 "))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public int getTradeCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(T6230.F01) FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01  WHERE T6230.F20 IN (?,?,?,?)"))
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F20.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public InvestmentLoanEntity getInvestmentLoanData()
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT (SELECT COUNT(1) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR'))) as loan,");
        sql.append("(SELECT COUNT(DISTINCT F03) FROM S62.T6250 WHERE F07 = 'F' AND F08 = 'S') as invest FROM DUAL");
        InvestmentLoanEntity quarterData = new InvestmentLoanEntity();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        quarterData.totalLoan = resultSet.getInt(1);
                        quarterData.totalInvestment = resultSet.getInt(2);
                        return quarterData;
                    }
                }
                return quarterData;
            }
        }
    }
    
    @Override
    public AgeDistributionEntity[] getAgeRanageData()
        throws Throwable
    {
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[5];
        String ageRanage = "";
        try (Connection connection = getConnection())
        {
            int num = 0;
            int slNum = 0;
            for (int i = 0; i < ageDistributionDatas.length - 1; i++)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141  WHERE LEFT(F08,3)= ? AND F08 IS NOT NULL"))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setString(1, "199");
                            ageRanage = "90后";
                            break;
                        case 1:
                            ps.setString(1, "198");
                            ageRanage = "80后";
                            break;
                        case 2:
                            ps.setString(1, "197");
                            ageRanage = "70后";
                            break;
                        case 3:
                            ps.setString(1, "196");
                            ageRanage = "60后";
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            ageDistributionDatas[i] = new AgeDistributionEntity();
                            slNum = resultSet.getInt(1);
                            num += slNum;
                            ageDistributionDatas[i].number = slNum;
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        ageDistributionDatas[4] = new AgeDistributionEntity();
                        ageDistributionDatas[4].number = resultSet.getInt(1) - num;
                        ageDistributionDatas[4].ageRanage = "其他";
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    @Override
    public VolumeTimeLimit[] getVolumeTimeLimits()
        throws Throwable
    {
        VolumeTimeLimit[] timeLimits = new VolumeTimeLimit[7];
        StringBuffer sqlStr =
            new StringBuffer(
                "SELECT IFNULL(SUM(F03),0),IFNULL(SUM(F04),0) FROM S70.T7044 WHERE F05>=? AND F05<=? AND F07=? ");
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < timeLimits.length; i++)
            {
                timeLimits[i] = new VolumeTimeLimit();
                try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setInt(1, 1);
                            ps.setInt(2, 3);
                            ps.setString(3, "F");
                            break;
                        case 1:
                            ps.setInt(1, 4);
                            ps.setInt(2, 6);
                            ps.setString(3, "F");
                            break;
                        case 2:
                            ps.setInt(1, 7);
                            ps.setInt(2, 9);
                            ps.setString(3, "F");
                            break;
                        case 3:
                            ps.setInt(1, 10);
                            ps.setInt(2, 12);
                            ps.setString(3, "F");
                            break;
                        case 4:
                            ps.setInt(1, 13);
                            ps.setInt(2, 24);
                            ps.setString(3, "F");
                            break;
                        case 5:
                            ps.setInt(1, 25);
                            ps.setInt(2, 36);
                            ps.setString(3, "F");
                            break;
                        default:
                            ps.setInt(1, 1);
                            ps.setInt(2, 365);
                            ps.setString(3, "S");
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            timeLimits[i].amount = resultSet.getBigDecimal(1);
                            timeLimits[i].count = resultSet.getInt(2);
                        }
                    }
                }
            }
        }
        return timeLimits;
    }
    
    @Override
    public VolumeType[] getVolumeTypes()
        throws Throwable
    {
        VolumeType[] volumeTypes = new VolumeType[4];
        volumeTypes[0] = new VolumeType();
        volumeTypes[0].type = InvestType.JGDBB;
        volumeTypes[1] = new VolumeType();
        volumeTypes[1].type = InvestType.DYRZB;
        volumeTypes[2] = new VolumeType();
        volumeTypes[2].type = InvestType.SDRZB;
        volumeTypes[3] = new VolumeType();
        volumeTypes[3].type = InvestType.XYRZB;
        StringBuffer sqlStr = new StringBuffer("SELECT IFNULL(SUM(F03),0),IFNULL(SUM(F04),0),F05 FROM S70.T7043 ");
        sqlStr.append("GROUP BY T7043.F05");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if ("JGDB".equals(resultSet.getString(3)))
                        {
                            volumeTypes[0].amount = resultSet.getBigDecimal(1);
                            volumeTypes[0].count = resultSet.getInt(2);
                        }
                        else if ("DYRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[1].amount = resultSet.getBigDecimal(1);
                            volumeTypes[1].count = resultSet.getInt(2);
                        }
                        else if ("SDRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[2].amount = resultSet.getBigDecimal(1);
                            volumeTypes[2].count = resultSet.getInt(2);
                        }
                        else if ("XYRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[3].amount = resultSet.getBigDecimal(1);
                            volumeTypes[3].count = resultSet.getInt(2);
                        }
                    }
                }
            }
        }
        return volumeTypes;
    }
    
    @Override
    public PlatformRiskControlEntity getPlatformRCE()
        throws Throwable
    {
        PlatformRiskControlEntity prce = new PlatformRiskControlEntity();
        BigDecimal totalLoanBalance = BigDecimal.ZERO;
        BigDecimal loanBadDebtBalance = BigDecimal.ZERO;
        BigDecimal loanUnBackBalance = BigDecimal.ZERO;
        BigDecimal maxUserBalanceAmount = BigDecimal.ZERO;
        BigDecimal maxTenUserBalanceAmount = BigDecimal.ZERO;
        try (Connection connection = getConnection())
        {
            //借款垫付金额
            try (PreparedStatement ps = connection.prepareStatement("SELECT IFNULL(SUM(F03),0) FROM S62.T6255"))
            {
                //ps.setInt(1, FeeCode.TZ_FX);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        prce.totalAdvancedAmount = rs.getBigDecimal(1);
                    }
                }
            }
            //借款逾期金额
            prce.loanOverdueBalanceAmount = getAmount(connection, 0);
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F04) FROM S62.T6250 WHERE F07 = ? AND F08 = ?"))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        totalLoanBalance = rs.getBigDecimal(1);
                    }
                }
            }
            //坏账金额
            loanBadDebtBalance = getAmount(connection, 11);
            if (totalLoanBalance.compareTo(BigDecimal.ZERO) > 0)
            {
                //借款逾期率
                prce.loanOverdueBalanceRate =
                    prce.loanOverdueBalanceAmount.divide(totalLoanBalance, 4, 5).multiply(new BigDecimal(100));
                //借贷坏账率
                prce.loanBadDebtRate = loanBadDebtBalance.divide(totalLoanBalance, 4, 5).multiply(new BigDecimal(100));
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F09 IN (?,?,?)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6252_F09.HKZ.name());
                ps.setString(3, T6252_F09.DF.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        loanUnBackBalance = rs.getBigDecimal(1);
                    }
                }
            }
            maxUserBalanceAmount = getCollectionAmount(connection, 0, 1);
            maxTenUserBalanceAmount = getCollectionAmount(connection, 0, 10);
            if (loanUnBackBalance.compareTo(BigDecimal.ZERO) > 0)
            {
                prce.maxUserLoanBalanceProportion =
                    maxUserBalanceAmount.divide(loanUnBackBalance, 4, 5).multiply(new BigDecimal(100));
                prce.maxTenUsersLoanBalancePropertion =
                    maxTenUserBalanceAmount.divide(loanUnBackBalance, 4, 5).multiply(new BigDecimal(100));
            }
        }
        return prce;
    }
    
    private BigDecimal getAmount(Connection connection, int month)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F09= ? AND F08< DATE_SUB(CURDATE(), INTERVAL ? MONTH) "))
        {
            ps.setString(1, T6252_F09.WH.name());
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal getCollectionAmount(Connection connection, int start, int end)
        throws Throwable
    {
        BigDecimal totalBalance = BigDecimal.ZERO;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT SUM(TT.F01) FROM (SELECT IFNULL(SUM(T6252.F07),0) AS F01 FROM S62.T6252 WHERE T6252.F05=? AND T6252.F09 IN (?,?,?) GROUP BY T6252.F03 ORDER BY F01 DESC LIMIT ?,?) TT "))
        {
            ps.setInt(1, FeeCode.TZ_BJ);
            ps.setString(2, T6252_F09.WH.name());
            ps.setString(3, T6252_F09.HKZ.name());
            ps.setString(4, T6252_F09.DF.name());
            ps.setInt(5, start);
            ps.setInt(6, end);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    totalBalance = totalBalance.add(rs.getBigDecimal(1));
                }
            }
        }
        return totalBalance;
    }
    
    @Override
    public BigDecimal[] getTotalInvestAmount()
        throws Throwable
    {
        BigDecimal[] totals = new BigDecimal[12];
        Calendar cal = Calendar.getInstance();
        try (Connection connection = getConnection())
        {
            cal.setTime(getCurrentDate(connection));
            for (int i = 0; i < totals.length; i++)
            {
                if (i != 0)
                {
                    cal.add(Calendar.MONTH, -1);
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT IFNULL(SUM(F03),0) FROM S70.T7042 WHERE F01= ? AND F02=?  "))
                {
                    ps.setInt(1, cal.get(Calendar.YEAR));
                    ps.setInt(2, cal.get(Calendar.MONTH) + 1);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            totals[i] = rs.getBigDecimal(1);
                        }
                    }
                }
            }
        }
        return totals;
    }
    
    @Override
    public void businessStatic()
        throws Throwable
    {
        StaticEntity businessStatic = new StaticEntity();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F04),0) FROM S62.T6250 WHERE F07=? AND F08 =? "))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //累计交易金额
                        businessStatic.totalDealAmont = rs.getBigDecimal(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(COUNT(T6230.F01),0) FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01  WHERE T6230.F20 IN (?,?,?,?)"))
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F20.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //累计交易笔数
                        businessStatic.totalDealCount = rs.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1)-1 FROM S61.T6110 "))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //注册用户数
                        businessStatic.registerCount = rs.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F09 IN (?,?)  AND T6252.F05 = ? "))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6252_F09.HKZ.name());
                ps.setInt(3, FeeCode.TZ_BJ);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //借贷余额
                        businessStatic.loanBalance = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(COUNT(F01),0) FROM S62.T6230 WHERE T6230.F20 IN (?,?,?) "))
            {
                ps.setString(1, T6230_F20.HKZ.name());
                ps.setString(2, T6230_F20.YDF.name());
                ps.setString(3, T6230_F20.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //借贷余额笔数
                        businessStatic.loanBalanceCount = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F09 IN (?,?) AND T6252.F05 IN (?,?) "))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6252_F09.HKZ.name());
                ps.setInt(3, FeeCode.TZ_LX);
                ps.setInt(4, FeeCode.TZ_FX);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        // 借贷利息余额
                        businessStatic.loanInterestBalance = rs.getBigDecimal(1);
                    }
                }
            }
            
            StringBuffer sql =
                new StringBuffer(
                    "SELECT (SELECT IFNULL(COUNT(1),0) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR'))) as totalLoan,");
            sql.append("(SELECT IFNULL(COUNT(DISTINCT F03),0) FROM S62.T6250 WHERE F07 = 'F' AND F08 = 'S') as totalInvest ,");
            sql.append("(SELECT IFNULL(COUNT(1),0) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YDF','YZR'))) as cuurLoan,");
            sql.append("(SELECT IFNULL(COUNT(DISTINCT T6250.F03),0) FROM S62.T6250,S62.T6230 WHERE T6250.F02 = T6230.F01 AND T6230.F20 IN ('HKZ','YDF','YZR') AND T6250.F07 = 'F' AND T6250.F08 = 'S') as cuurInvest FROM DUAL ");
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //累计借款人数
                        businessStatic.totalLoan = rs.getInt(1);
                        //累计投资人数
                        businessStatic.totalInvestment = rs.getInt(2);
                        //当前借款人数
                        businessStatic.cuurLoan = rs.getInt(3);
                        //当前投资人数
                        businessStatic.cuurInvestment = rs.getInt(4);
                    }
                }
            }
            
            StringBuffer sb =
                new StringBuffer(
                    "SELECT (SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') GROUP BY T6252.F11,T6252.F06) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005)");
            sb.append("-(SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=1202 AND T6101.F03='WLZH')");
            sb.append("+(SELECT IFNULL(SUM(ZQZR.zqzryk),0) FROM (SELECT IFNULL(SUM(T6262.F08), 0) zqzryk FROM S62.T6262 GROUP BY T6262.F03 UNION SELECT IFNULL(SUM(T6262.F09), 0) zqzryk FROM S62.T6262, S62.T6260, S62.T6251 WHERE T6251.F01 = T6260.F02 AND T6260.F01 = T6262.F02 GROUP BY T6251.F04) ZQZR)");
            try (PreparedStatement ps = connection.prepareStatement(sb.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //累计赚取收益
                        businessStatic.totalProfit = rs.getBigDecimal(1);
                    }
                }
            }
            
            //BigDecimal totalLoanBalance = BigDecimal.ZERO;
            BigDecimal loanUnBackBalance = BigDecimal.ZERO;
            BigDecimal maxUserBalanceAmount = BigDecimal.ZERO;
            BigDecimal maxTenUserBalanceAmount = BigDecimal.ZERO;
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F09 IN (?,?,?)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6252_F09.HKZ.name());
                ps.setString(3, T6252_F09.DF.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        loanUnBackBalance = rs.getBigDecimal(1);
                    }
                }
            }
            maxUserBalanceAmount = getCollectionAmount(connection, 0, 1);
            maxTenUserBalanceAmount = getCollectionAmount(connection, 0, 10);
            if (loanUnBackBalance.compareTo(BigDecimal.ZERO) > 0)
            {
                // 最大单一借款人待还金额占比
                businessStatic.maxUserLoanBalanceProportion =
                    maxUserBalanceAmount.divide(loanUnBackBalance, 4, 5).multiply(new BigDecimal(100));
                // 前十大借款人待还金额占比
                businessStatic.maxTenUsersLoanBalancePropertion =
                    maxTenUserBalanceAmount.divide(loanUnBackBalance, 4, 5).multiply(new BigDecimal(100));
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F09= ? AND F08< DATE_SUB(CURDATE(), INTERVAL ? MONTH) "))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, 0);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        ////借款逾期金额
                        businessStatic.overdueBalanceAmount = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(COUNT(DISTINCT F02),0) FROM S62.T6252 WHERE F09= ? AND F08< DATE_SUB(CURDATE(), INTERVAL ? MONTH)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, 0);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //借款逾期笔数
                        businessStatic.overdueBalanceCount = rs.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F09= ? AND F08< DATE_SUB(CURDATE(), INTERVAL ? DAY) AND F05 = ? "))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, 90);
                ps.setInt(3, FeeCode.TZ_BJ);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //逾期90天以上金额
                        businessStatic.overdueBalanceAmount90 = rs.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(COUNT(DISTINCT F02),0) FROM S62.T6252 WHERE F09= ? AND F08< DATE_SUB(CURDATE(), INTERVAL ? DAY)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, 90);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //逾期90天以上笔数
                        businessStatic.overdueBalanceCount90 = rs.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT(SELECT IFNULL(SUM(F03),0) FROM S62.T6255) as totalAdvancedAmount,(SELECT IFNULL(COUNT(DISTINCT F02),0) FROM S62.T6255) AS totalAdvancedCount FROM DUAL"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        //累计代偿金额
                        businessStatic.totalAdvancedAmount = rs.getBigDecimal(1);
                        //累计代偿笔数
                        businessStatic.totalAdvancedCount = rs.getInt(2);
                    }
                }
            }
            
            //项目逾期率：逾期笔数/累计成交笔数
            businessStatic.projectBalanceRate =
                new BigDecimal(businessStatic.overdueBalanceCount).divide(new BigDecimal(businessStatic.totalDealCount),
                    4,
                    5)
                    .multiply(new BigDecimal(100));
            //businessStatic.projectBalanceRate = businessStatic.overdueBalanceCount.divide(businessStatic.totalInvestment, 4, 5);
            //金额逾期率：逾期金额//累计成交金额
            businessStatic.amountBalanceRate =
                businessStatic.overdueBalanceAmount.divide(businessStatic.totalDealAmont, 4, 5)
                    .multiply(new BigDecimal(100));
            //businessStatic.amountBalanceRate = businessStatic.overdueBalanceAmount.divide(businessStatic.totalDealAmont, 4, 5);
            insertT7054(businessStatic, connection);
        }
    }
    
    @Override
    public T7054 getT7054()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T7054 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21,F22,F23,F24 FROM S70.T7054 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T7054();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.F10 = resultSet.getBigDecimal(10);
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getBigDecimal(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = resultSet.getInt(15);
                        record.F16 = resultSet.getBigDecimal(16);
                        record.F17 = resultSet.getBigDecimal(17);
                        record.F18 = resultSet.getInt(18);
                        record.F19 = resultSet.getBigDecimal(19);
                        record.F20 = resultSet.getBigDecimal(20);
                        record.F21 = resultSet.getInt(21);
                        record.F22 = resultSet.getBigDecimal(22);
                        record.F23 = resultSet.getInt(23);
                        record.F24 = resultSet.getTimestamp(24);
                    }
                }
            }
            return record;
        }
    }
    
    /**
    *
    * 添加经营信息统计
    */
    private void insertT7054(StaticEntity businessStatic, Connection connection)
        throws Throwable
    {
        String sql =
            "UPDATE S70.T7054 SET F01=?,F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,F11=?,F12=?,F13=?,F14=?,F15=?,F16=?,F17=?,F18=?,F19=?,F20=?,F21=?,F22=?,F23=?,F24=?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setBigDecimal(1, businessStatic.totalDealAmont);
            ps.setInt(2, businessStatic.totalDealCount);
            ps.setBigDecimal(3, businessStatic.loanBalance);
            ps.setInt(4, businessStatic.loanBalanceCount);
            ps.setInt(5, businessStatic.totalInvestment);
            ps.setInt(6, businessStatic.cuurInvestment);
            ps.setInt(7, businessStatic.totalLoan);
            ps.setInt(8, businessStatic.cuurLoan);
            ps.setBigDecimal(9, businessStatic.maxTenUsersLoanBalancePropertion);
            ps.setBigDecimal(10, businessStatic.maxUserLoanBalanceProportion);
            ps.setInt(11, businessStatic.registerCount);
            ps.setBigDecimal(12, businessStatic.loanInterestBalance);
            ps.setBigDecimal(13, businessStatic.totalProfit);
            ps.setBigDecimal(14, businessStatic.correlationBalance);
            ps.setInt(15, businessStatic.correlationCount);
            ps.setBigDecimal(16, businessStatic.overdueBalanceAmount);
            ps.setBigDecimal(17, businessStatic.amountBalanceRate);
            ps.setInt(18, businessStatic.overdueBalanceCount);
            ps.setBigDecimal(19, businessStatic.projectBalanceRate);
            ps.setBigDecimal(20, businessStatic.totalAdvancedAmount);
            ps.setInt(21, businessStatic.totalAdvancedCount);
            ps.setBigDecimal(22, businessStatic.overdueBalanceAmount90);
            ps.setInt(23, businessStatic.overdueBalanceCount90);
            ps.setTimestamp(24, getCurrentTimestamp(connection));
            ps.executeUpdate();
        }
    }
    
    @Override
    public java.util.Date getStatisticalDate()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT DATE_SUB(CURRENT_DATE(),INTERVAL 1 DAY)"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getDate(1);
                    }
                }
            }
        }
        return null;
    }
    
}
