package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S70.entities.T7203;
import com.dimeng.p2p.modules.bid.user.service.LctjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.EarnFinancingInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.EarnFinancingTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.InvestAmount;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class LctjManageImpl extends AbstractBidManage implements LctjManage
{
    
    public LctjManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 总数
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @return total
     * @throws Throwable
     */
    @Override
    public EarnFinancingTotal getEarnFinancingTotal(String sYear, String sMonth, String eYear, String eMonth)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Object> list = new ArrayList<>();
            int accountId = serviceResource.getSession().getAccountId();
            StringBuilder sql =
                new StringBuilder(
                    "SELECT IFNULL(SUM(F04+F06+F07+F08-F13),0) ljsy,IFNULL(SUM(F03),0) ljtzje,IFNULL(SUM(F05+F06+F07+F08),0) yhsje,IFNULL(SUM(F09+F10+F11),0) dhsje FROM S70.T7203 WHERE F12=?");
            list.add(accountId);
            /*统计数据不根据查询条件变动
            if (!StringHelper.isEmpty(sYear)) {
            	sql.append(" AND F01 >= ? ");
            	list.add(Integer.parseInt(sYear));
            }
            if (!StringHelper.isEmpty(eYear)) {
            	sql.append(" AND F01 <= ? ");
            	list.add(Integer.parseInt(eYear));
            }
            if (!StringHelper.isEmpty(sMonth)) {
            	sql.append(" AND F02 >= ? ");
            	list.add(Integer.parseInt(sMonth));
            }
            if (!StringHelper.isEmpty(eMonth)) {
            	sql.append(" AND F02 <= ? ");
            	list.add(Integer.parseInt(eMonth));
            }*/
            return select(connection, new ItemParser<EarnFinancingTotal>()
            {
                @Override
                public EarnFinancingTotal parse(ResultSet resultSet)
                    throws SQLException
                {
                    EarnFinancingTotal total = new EarnFinancingTotal();
                    if (resultSet.next())
                    {
                        total.ljsy = resultSet.getBigDecimal(1);
                        /*try
                        {
                            total.ljsy = total.ljsy.subtract(tzglf(connection));
                        }
                        catch (Throwable e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/
                        total.ljtzje = resultSet.getBigDecimal(2);
                        total.yhsje = resultSet.getBigDecimal(3);
                        total.dhsje = resultSet.getBigDecimal(4);
                    }
                    return total;
                }
            }, sql.toString(), list);
            
        }
        
    }
    
    private BigDecimal tzglf(Connection connection)
        throws Throwable
    {
        BigDecimal glf = new BigDecimal(0);
        StringBuilder sb = new StringBuilder("SELECT IFNULL(SUM(T6102.F07),0) from S61.T6102 WHERE T6102.F03=? AND ");
        sb.append("T6102.F02 = (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?)");
        try (PreparedStatement ps = connection.prepareStatement(sb.toString());)
        {
            ps.setInt(1, FeeCode.GLF);
            ps.setInt(2, serviceResource.getSession().getAccountId());
            ps.setString(3, T6101_F03.WLZH.name());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    glf = rs.getBigDecimal(1);
                }
            }
        }
        return glf;
    }
    
    /*private BigDecimal tzglf(Connection connection, String sYear, String sMonth, String eYear, String eMonth)
        throws Throwable
    {
        BigDecimal glf = new BigDecimal(0);
        StringBuilder sb = new StringBuilder("SELECT IFNULL(SUM(T6102.F07),0) from S61.T6102 WHERE T6102.F03=? AND ");
        sb.append("T6102.F02 = (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?)");
        if (StringHelper.isEmpty(sYear))
        {
            sYear = "1971";
        }
        if (StringHelper.isEmpty(eYear))
        {
            eYear = "2199";
        }
        if (StringHelper.isEmpty(sMonth))
        {
            sMonth = "01";
        }
        if (StringHelper.isEmpty(eMonth))
        {
            eMonth = "12";
        }
        sb.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') >= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
        sb.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') <= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
        try (PreparedStatement ps = connection.prepareStatement(sb.toString());)
        {
            ps.setInt(1, FeeCode.GLF);
            ps.setInt(2, serviceResource.getSession().getAccountId());
            ps.setString(3, T6101_F03.WLZH.name());
            ps.setString(4, sYear);
            ps.setString(5, sMonth);
            ps.setString(6, eYear);
            ps.setString(7, eYear);
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    glf = rs.getBigDecimal(1);
                }
            }
        }
        return glf;
    }*/
    
    /**
     * 理财统计列表
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @param paging
     *            日
     * @return list
     * @throws Throwable
     */
    @Override
    public PagingResult<EarnFinancingInfo> search(String sYear, String sMonth, String eYear, String eMonth,
        Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01,F02,(F04+F06+F07+F08) ljsy,F03 tzje,(F05+F06+F07+F08) yhsje,(F09+F10+F11) dhsje,F13 tzglf FROM S70.T7203 WHERE F12=?");
        list.add(serviceResource.getSession().getAccountId());
        /*
         * if(sYear.equals(eYear) || StringHelper.isEmpty(sYear) ||
         * StringHelper.isEmpty(eYear)){ if (!StringHelper.isEmpty(sYear)) {
         * sql.append(" AND F01 >= ? "); list.add(Integer.parseInt(sYear)); } if
         * (!StringHelper.isEmpty(eYear)) { sql.append(" AND F01 <= ? ");
         * list.add(Integer.parseInt(eYear)); } if
         * (!StringHelper.isEmpty(sMonth)) { sql.append(" AND F02 >= ? ");
         * list.add(Integer.parseInt(sMonth)); } if
         * (!StringHelper.isEmpty(eMonth)) { sql.append(" AND F02 <= ? ");
         * list.add(Integer.parseInt(eMonth)); } }
         */
        // if(!StringHelper.isEmpty(sYear) && !StringHelper.isEmpty(eYear) &&
        // !sYear.equals(eYear)){
        
        if (StringHelper.isEmpty(sYear))
        {
            sYear = "1971";
        }
        if (StringHelper.isEmpty(eYear))
        {
            eYear = "2199";
        }
        if (StringHelper.isEmpty(sMonth))
        {
            sMonth = "01";
        }
        if (StringHelper.isEmpty(eMonth))
        {
            eMonth = "12";
        }
        sql.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') >= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
        list.add(sYear);
        list.add(sMonth);
        sql.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') <= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
        list.add(eYear);
        list.add(eMonth);
        // }
        sql.append(" ORDER BY F01 DESC,F02 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<EarnFinancingInfo>()
            {
                @Override
                public EarnFinancingInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<EarnFinancingInfo> list = null;
                    while (resultSet.next())
                    {
                        EarnFinancingInfo record = new EarnFinancingInfo();
                        record.year = resultSet.getInt(1);
                        record.month = resultSet.getInt(2);
                        record.ljsy = resultSet.getBigDecimal(3).subtract(resultSet.getBigDecimal(7));
                        record.ljtzje = resultSet.getBigDecimal(4);
                        record.yhsje = resultSet.getBigDecimal(5);
                        record.dhsje = resultSet.getBigDecimal(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null
                        : list.toArray(new EarnFinancingInfo[list.size()]));
                }
            },
                paging,
                sql.toString(),
                list);
        }
    }
    
    /**
     * 理财统计详情列表
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @param paging
     *            日
     * @return list
     * @throws Throwable
     */
    @Override
    public PagingResult<T7203> searchDetail(String sYear, String sMonth, String eYear, String eMonth, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM S70.T7203 WHERE F12=?");
        list.add(serviceResource.getSession().getAccountId());
        if (sYear.equals(eYear) || StringHelper.isEmpty(sYear) || StringHelper.isEmpty(eYear))
        {
            if (!StringHelper.isEmpty(sYear))
            {
                sql.append(" AND F01 >= ? ");
                list.add(Integer.parseInt(sYear));
            }
            if (!StringHelper.isEmpty(eYear))
            {
                sql.append(" AND F01 <= ? ");
                list.add(Integer.parseInt(eYear));
            }
            if (!StringHelper.isEmpty(sMonth))
            {
                sql.append(" AND F02 >= ? ");
                list.add(Integer.parseInt(sMonth));
            }
            if (!StringHelper.isEmpty(eMonth))
            {
                sql.append(" AND F02 <= ? ");
                list.add(Integer.parseInt(eMonth));
            }
        }
        if (!StringHelper.isEmpty(sYear) && !StringHelper.isEmpty(eYear) && !sYear.equals(eYear))
        {
            if (StringHelper.isEmpty(sMonth))
            {
                sMonth = "01";
            }
            if (StringHelper.isEmpty(eMonth))
            {
                eMonth = "12";
            }
            sql.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') >= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
            list.add(sYear);
            list.add(sMonth);
            sql.append(" AND DATE_FORMAT(CONCAT(F01,'-',F02,'-','01'),'%Y-%m-%d') <= DATE_FORMAT(CONCAT(?,'-',?,'-','01'),'%Y-%m-%d')");
            list.add(eYear);
            list.add(eMonth);
        }
        sql.append(" ORDER BY F01 DESC,F02 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7203>()
            {
                @Override
                public T7203[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T7203> list = null;
                    while (resultSet.next())
                    {
                        T7203 record = new T7203();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.F10 = resultSet.getBigDecimal(10);
                        record.F11 = resultSet.getBigDecimal(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T7203[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
    @Override
    public void export(T7203[] recWits, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("年");
            writer.write("月");
            writer.write("投资金额(元)");
            writer.write("债权转让盈亏(元)");
            writer.write("已收本金(元)");
            writer.write("已收利息(元)");
            writer.write("已收罚息(元)");
            writer.write("已收违约金(元)");
            writer.write("理财管理费(元)");
            writer.write("待收本金(元)");
            writer.write("待收利息(元)");
            writer.write("待收罚息(元)");
            
            writer.newLine();
            int i = 0;
            for (T7203 recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.F01);
                writer.write(recWit.F02);
                writer.write(recWit.F03);
                writer.write(recWit.F04);
                writer.write(recWit.F05);
                writer.write(recWit.F06);
                writer.write(recWit.F07);
                writer.write(recWit.F08);
                writer.write(recWit.F13);
                writer.write(recWit.F09);
                writer.write(recWit.F10);
                writer.write(recWit.F11);
                writer.newLine();
            }
        }
    }
    
    /**
     * 理财统计-累计投资金额详情列表
     * 
     * @return PagingResult
     * @throws Throwable
     */
    @Override
    public PagingResult<InvestAmount> searchInvestAmountDetail(String year, String month, Paging paging)
        throws Throwable
    {
        
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT TZJE_T.`YEAR`,TZJE_T.`MONTH`,SUM(TZJE_T.tzje) tzje,SUM(TZJE_T.mrzqje) mrzqje FROM (SELECT DATE_FORMAT(T6250.F06, '%Y') AS YEAR,DATE_FORMAT(T6250.F06, '%c') AS MONTH,IFNULL(SUM(T6250.F04), 0) AS tzje,0 AS mrzqje ");
        sql.append("FROM S62.T6250 WHERE T6250.F07 = 'F' AND T6250.F08 = 'S' AND T6250.F03 = ? GROUP BY T6250.F03 ");
        sql.append("UNION SELECT DATE_FORMAT(T6262.F07, '%Y') AS YEAR,DATE_FORMAT(T6262.F07, '%c') AS MONTH,0 AS tzje,IFNULL(SUM(T6262.F05), 0) AS mrzqje FROM S62.T6262 WHERE T6262.F03 = ? GROUP BY T6262.F03) AS TZJE_T WHERE 1=1 ");
        list.add(serviceResource.getSession().getAccountId());
        list.add(serviceResource.getSession().getAccountId());
        if (!StringHelper.isEmpty(year))
        {
            sql.append(" AND TZJE_T.`YEAR` = ? ");
            list.add(Integer.parseInt(year));
        }
        if (!StringHelper.isEmpty(month))
        {
            sql.append(" AND TZJE_T.`MONTH` ? ");
            list.add(Integer.parseInt(month));
        }
        sql.append(" GROUP BY TZJE_T.`YEAR`,TZJE_T.`MONTH`");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<InvestAmount>()
            {
                @Override
                public InvestAmount[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<InvestAmount> list = null;
                    while (resultSet.next())
                    {
                        InvestAmount iInvestAmount = new InvestAmount();
                        iInvestAmount.year = resultSet.getInt(1);
                        iInvestAmount.month = resultSet.getInt(2);
                        iInvestAmount.investMoney = resultSet.getBigDecimal(3);
                        iInvestAmount.buyCreditMoney = resultSet.getBigDecimal(4);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(iInvestAmount);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new InvestAmount[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
    /**
     * 理财统计
     * 
     * @return PagingResult
     * @throws Throwable
     */
    @Override
    public PagingResult<T7203> searchStatistics(String year, String month, String type, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM S70.T7203 WHERE ");
        switch (type)
        {
            case "ljsy":
                sql.append("(F04 != 0 OR F06 != 0 OR F07 != 0 OR F08 != 0)");
                break;
            case "yhsje":
                sql.append("(F05>0 OR F06>0 OR F07>0 OR F08>0)");
                break;
            case "dhsje":
                sql.append("(F09>0 OR F10>0 OR F11>0)");
                break;
        }
        sql.append(" AND F12=?");
        list.add(serviceResource.getSession().getAccountId());
        if (year != null && !"".equals(year))
        {
            sql.append(" AND F01 = ? ");
            list.add(Integer.parseInt(year));
        }
        if (month != null && !"".equals(month))
        {
            sql.append(" AND F02= ? ");
            list.add(Integer.parseInt(month));
        }
        sql.append(" ORDER BY F01,F02");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7203>()
            {
                @Override
                public T7203[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T7203> list = null;
                    while (resultSet.next())
                    {
                        T7203 record = new T7203();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.F10 = resultSet.getBigDecimal(10);
                        record.F11 = resultSet.getBigDecimal(11);
                        record.F12 = resultSet.getInt(12);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T7203[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
}
