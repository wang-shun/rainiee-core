package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.S62.enums.T6286_F07;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.entity.Bdlb;
import com.dimeng.p2p.account.user.service.entity.FinancialPlan;
import com.dimeng.p2p.account.user.service.entity.LoanAccount;
import com.dimeng.p2p.account.user.service.entity.LoanAccountInfo;
import com.dimeng.p2p.account.user.service.entity.Notice;
import com.dimeng.p2p.account.user.service.entity.TenderAccount;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

public class IndexManageImpl extends AbstractAccountService implements IndexManage
{
    protected BigDecimal unpayTotal = new BigDecimal(0);
    
    public IndexManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public UserBaseInfo getUserBaseInfo()
        throws Throwable
    {
        UserBaseInfo info = new UserBaseInfo();
        int aqdj = 0;
        int fyAqdj = 0;//富友版本专用
        int accountId = serviceResource.getSession().getAccountId();
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        String escrow_prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        // 判断实名、手机、邮箱、交易密码是否认证
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6118_F02.TG.name()))
                        {
                            info.realName = true;
                            aqdj = aqdj + 25;
                            fyAqdj = fyAqdj + 20;
                        }
                        if (resultSet.getString(2).equals(T6118_F03.TG.name()))
                        {
                            info.phone = true;
                            aqdj = aqdj + 25;
                            fyAqdj = fyAqdj + 20;
                        }
                        if (resultSet.getString(3).equals(T6118_F04.TG.name()))
                        {
                            info.email = true;
                            aqdj = aqdj + 25;
                            fyAqdj = fyAqdj + 20;
                        }
                        //是托管，要注册第三方账号。
                        if (tg)
                        {
                            String usrCustId = getUsrCustId(connection);
                            if (!StringHelper.isEmpty(usrCustId))
                            {
                                aqdj = aqdj + 25;
                                fyAqdj = fyAqdj + 20;
                            }
                            
                            if ("FUYOU".equals(escrow_prefix))
                            {
                                if (resultSet.getString(4).equals(T6118_F05.YSZ.name()))
                                {
                                    info.withdrawPsw = true;
                                    aqdj = aqdj + 25;
                                    fyAqdj = fyAqdj + 20;
                                }
                            }
                        }
                        else
                        {
                            if (resultSet.getString(4).equals(T6118_F05.YSZ.name()))
                            {
                                info.withdrawPsw = true;
                                aqdj = aqdj + 25;
                            }
                        }
                    }
                }
            }
            // 得到用户头像
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05,F07 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.photo = resultSet.getString(1);
                        info.idCard = resultSet.getString(2);
                    }
                }
            }
            //得到企业信息
            try (PreparedStatement pstmt =
					connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, accountId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {   
                        	if(resultSet.getString(1).equals("FZRR"))
                        	{
                        		info.auth = true;
                        	}
                        }
                    }
                }
            // 账户可用余额
            info.balance = getZhje(T6101_F03.WLZH, connection);
            // 冻结资金
            info.freezeFunds = getZhje(T6101_F03.SDZH, connection);
            // 风险保证金
            info.fxbzj = getZhje(T6101_F03.FXBZJZH, connection);
            // 体验金
            info.experienceAmount = getExperienceAmount(accountId, connection);
            // 安全等级
            info.safeLevel = "FUYOU".equals(escrow_prefix) ? fyAqdj : aqdj;
        }
        
        return info;
    }
    
    @Override
    public UserBaseInfo getUserBaseInfoTx()
        throws Throwable
    {
        UserBaseInfo info = new UserBaseInfo();
        int aqdj = 0;
        int accountId = serviceResource.getSession().getAccountId();
        // 判断实名、手机、邮箱、交易密码是否认证
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6118_F02.TG.name()))
                        {
                            info.realName = true;
                            aqdj = aqdj + 34;
                        }
                        if (resultSet.getString(2).equals(T6118_F03.TG.name()))
                        {
                            info.phone = true;
                            aqdj = aqdj + 33;
                        }
                        if (resultSet.getString(3).equals(T6118_F04.TG.name()))
                        {
                            info.email = true;
                            aqdj = aqdj + 33;
                        }
                        if (resultSet.getString(4).equals(T6118_F05.YSZ.name()))
                        {
                            info.withdrawPsw = true;
                        }
                    }
                }
            }
            // 得到用户头像
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.photo = resultSet.getString(1);
                    }
                }
            }
            // 账户余额
            info.balance = getZhje(T6101_F03.WLZH, connection);
            // 冻结资金
            info.freezeFunds = getZhje(T6101_F03.SDZH, connection);
            // 风险保证金
            info.fxbzj = getZhje(T6101_F03.FXBZJZH, connection);
            // 安全等级
            info.safeLevel = aqdj;
        }
        
        return info;
    }
    
    // 根据资金账户查询金额
    private BigDecimal getZhje(T6101_F03 f03, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setString(2, f03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return new BigDecimal(0);
    }
    
    // 获取用户的体验金
    private BigDecimal getExperienceAmount(int userId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F03),0) FROM S61.T6103 WHERE T6103.F02 = ? AND T6103.F06 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.setString(2, T6103_F06.WSY.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public Notice getNotice()
        throws Throwable
    {
        Notice notice = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01,F05,F09 FROM S50.T5015 WHERE F04='YFB' ORDER BY F09 DESC LIMIT 1 "))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        notice = new Notice();
                        notice.id = resultSet.getInt(1);
                        notice.title = resultSet.getString(2);
                        notice.time = resultSet.getTimestamp(3);
                    }
                }
            }
        }
        return notice;
    }
    
    @Override
    public BigDecimal getLoanAmount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public TenderAccount getTenderAccount()
        throws Throwable
    {
        TenderAccount tenderAccount = new TenderAccount();
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            // 优选理财账户数据
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S64.T6413 WHERE T6413.F01 = ? LIMIT 1"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.yxyz = resultSet.getBigDecimal(1);
                        tenderAccount.yxzc = resultSet.getBigDecimal(2);
                        tenderAccount.yxsyl = resultSet.getBigDecimal(3);
                        tenderAccount.yxcyl = resultSet.getInt(4);
                    }
                }
            }
            // 散标投资账户资产
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F09=? AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = T6252.F04  AND T6236.F02 = T6252.F02)"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbzc = resultSet.getBigDecimal(1);
                    }
                }
            }
            
            // 散标已挣利息总金额
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? GROUP BY T6252.F11,T6252.F06) TBL_LX"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = tenderAccount.sbyz.add(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            // 散标已挣罚息总金额
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004 AND T6255.F04=?) TBL_LX"))
            {
                ps.setInt(1, accountId);
                ps.setInt(2, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = tenderAccount.sbyz.add(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            // 散标已挣违约金总金额
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005 AND T6252.F04=?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = tenderAccount.sbyz.add(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            // 散标已挣总金额
            /*try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252  WHERE T6252.F05 IN (?, ?, ?) AND T6252.F04 = ? AND T6252.F09= ? AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = T6252.F04 AND T6236.F02 = T6252.F02)"))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_FX);
                ps.setInt(3, FeeCode.TZ_WYJ);
                ps.setInt(4, serviceResource.getSession().getAccountId());
                ps.setString(5, T6252_F09.YH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = resultSet.getBigDecimal(1);
                    }
                }
            }*/
            
            // 理财管理费
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=? AND T6101.F03=? AND T6101.F02=?"))
            {
                ps.setInt(1, FeeCode.GLF);
                ps.setString(2, T6101_F03.WLZH.name());
                ps.setInt(3, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = tenderAccount.sbyz.subtract(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            //债权转让盈亏
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(ZQZR.zqzryk),0) FROM (SELECT IFNULL(SUM(T6262.F08), 0) zqzryk,T6262.F03 userId FROM S62.T6262 GROUP BY T6262.F03 UNION SELECT IFNULL(SUM(T6262.F09), 0) zqzryk,T6251.F04 userId FROM S62.T6262, S62.T6260, S62.T6251 WHERE T6251.F01 = T6260.F02 AND T6260.F01 = T6262.F02 GROUP BY T6251.F04) ZQZR WHERE ZQZR.userId=?"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbyz = tenderAccount.sbyz.add(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            // 散标持有量
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S62.T6230 INNER JOIN S62.T6251 ON T6230.F01 = T6251.F03 WHERE T6230.F20 = ? AND T6251.F04 = ? AND T6251.F07 > 0"))
            {
                ps.setString(1, T6230_F20.HKZ.name());
                ps.setInt(2, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbcyl = resultSet.getInt(1);
                    }
                }
            }
            
            // 查询平均年化利率
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6230.F06)/COUNT(1) FROM S62.T6230 INNER JOIN S62.T6251 ON T6230.F01 = T6251.F03"
                    + " WHERE T6251.F04 = ? AND T6251.F07 > 0 AND T6230.F20 =?"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, T6230_F20.HKZ.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.sbsyl = resultSet.getBigDecimal(1);
                    }
                }
            }
            // 查询总本金
            // BigDecimal totalBj = new BigDecimal(0);
            // try (PreparedStatement ps = connection
            // .prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F04=? AND F05=?"))
            // {
            // ps.setInt(1, accountId);
            // ps.setInt(2, FeeCode.TZ_BJ);
            // try (ResultSet rs = ps.executeQuery()) {
            // if (rs.next()) {
            // totalBj = rs.getBigDecimal(1);
            // }
            // }
            // }
            // if (totalBj.compareTo(BigDecimal.ZERO) > 0) {
            // tenderAccount.sbsyl = totalLx.divide(totalBj, 2,
            // BigDecimal.ROUND_HALF_UP);
            // }
            tenderAccount.yzzje = tenderAccount.yxyz.add(tenderAccount.sbyz);
            
            // 体验金投资总额
            BigDecimal totalTyj = BigDecimal.ZERO;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6286.F04) FROM S62.T6286 WHERE T6286.F03 = ? AND "
                    + "(SELECT COUNT(*) FROM S62.T6285 WHERE T6285.F02 = T6286.F02 AND T6285.F04 = T6286.F03 AND T6285.F09 = ?) > 0"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, T6285_F09.WFH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        totalTyj = rs.getBigDecimal(1);
                    }
                }
            }
            tenderAccount.tyjze = totalTyj;
            
            // 体验金已赚金额
            BigDecimal tyjyz = BigDecimal.ZERO;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(TYJYZ.F02) FROM (SELECT Y_T6285.F02 AS F01,SUM(Y_T6285.F07) AS F02 FROM S62.T6285 Y_T6285 WHERE Y_T6285.F04 = ?"
                    /*
                     * +
                     * " AND Y_T6285.F05 = ? AND Y_T6285.F09 = ? AND (SELECT COUNT(*) FROM S62.T6285 WHERE Y_T6285.F02 = T6285.F02 AND T6285.F09 = ? ) > 0 GROUP BY Y_T6285.F02) TYJYZ"
                     * )) {
                     */
                    // 去掉 count 条件：否则全部已还的标的已挣金额查不出来 by
                    // zengzhihua@taojindi.com 2015-3-30
                    + " AND Y_T6285.F05 = ? AND Y_T6285.F09 = ? GROUP BY Y_T6285.F02) TYJYZ"))
            {
                ps.setInt(1, accountId);
                ps.setInt(2, FeeCode.TYJTZ);
                ps.setString(3, T6285_F09.YFH.name());
                // ps.setString(4, T6285_F09.WFH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        tyjyz = rs.getBigDecimal(1);
                    }
                }
            }
            tenderAccount.tyjyz = tyjyz;
            tenderAccount.yzzje = tenderAccount.yzzje.add(tenderAccount.tyjyz);
            
            // 体验金平均收益率
            if (totalTyj.compareTo(BigDecimal.ZERO) > 0)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT SUM(T6230.F06)/COUNT(1) FROM S62.T6230 INNER JOIN S62.T6286 ON T6286.F02 = T6230.F01 WHERE T6286.F03 = ? AND T6286.F07 = ?"))
                {
                    ps.setInt(1, accountId);
                    ps.setString(2, T6286_F07.S.name());
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            tenderAccount.tyjsyl = rs.getBigDecimal(1);
                        }
                    }
                }
            }
            
            // 体验金持有量，已经全部还款的不算
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM (SELECT Y_T6285.F02 AS F01,SUM(Y_T6285.F07) AS F02 FROM S62.T6285 Y_T6285 WHERE Y_T6285.F04 = ?"
                    + " AND Y_T6285.F05 = ? AND (SELECT COUNT(*) FROM S62.T6285 WHERE Y_T6285.F02 = T6285.F02 AND T6285.F09 = ? ) > 0 GROUP BY Y_T6285.F02) TYJYZ"))
            {
                ps.setInt(1, accountId);
                ps.setInt(2, FeeCode.TYJTZ);
                ps.setString(3, T6285_F09.WFH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.tyjcyl = resultSet.getInt(1);
                    }
                }
            }
        }
        return tenderAccount;
    }
    
    /**
     * 线上债权转让盈亏
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    /*private BigDecimal earnZqzryk(Connection connection)
        throws Throwable
    {
        BigDecimal zryk = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F08),0)  FROM S62.T6262 WHERE F03 = ?");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    zryk = rs.getBigDecimal(1);
                }
                
            }
        }
        BigDecimal zcyk = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(T6262.F09),0)  FROM S62.T6262,S62.T6260,S62.T6251 WHERE T6251.F04 = ? AND  T6251.F01 = T6260.F02 AND T6260.F01 =T6262.F02");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    zcyk = rs.getBigDecimal(1);
                }
                
            }
        }
        return zryk.add(zcyk);
    }*/
    
    // 优选理财利息加罚息
    /*private BigDecimal yxlcLx(Connection connection)
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0)	 FROM S64.T6412 WHERE F09 = 'YH' AND F04=? AND F05=7002");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    lx = rs.getBigDecimal(1);
                }
                
            }
        }
        return lx;
    }*/
    
    @Override
    public LoanAccount[] getLoanAccount()
        throws Throwable
    {
        List<LoanAccount> accounts = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6231.F01,T6230.F03,T6230.F11,T6230.F13,T6230.F14,T6230.F28, T6231.F27,T6231.F28 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?) ORDER BY F01 DESC"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YDF.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        LoanAccount loanAccount = new LoanAccount();
                        loanAccount.id = resultSet.getInt(1);
                        loanAccount.title = resultSet.getString(2);
                        loanAccount.F11 = EnumParser.parse(T6230_F11.class, resultSet.getString(3));
                        loanAccount.F13 = EnumParser.parse(T6230_F13.class, resultSet.getString(4));
                        loanAccount.F14 = EnumParser.parse(T6230_F14.class, resultSet.getString(5));
                        loanAccount.F15 = EnumParser.parse(T6230_F28.class, resultSet.getString(6));
                        loanAccount.F16 = EnumParser.parse(T6231_F27.class, resultSet.getString(7));
                        loanAccount.F17 = resultSet.getBigDecimal(8);
                        accounts.add(loanAccount);
                    }
                }
            }
            
            for (LoanAccount account : accounts)
            {
                int bidID = account.id;
                
                // 待还本金
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_BJ);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.dhbj = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 待还利息
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_LX);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.dhlx = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 管理费
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.CJFWF);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.glf = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 逾期费用
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_YQGLF);
                    pstmt.setInt(3, FeeCode.TZ_FX);
                    pstmt.setString(4, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.yqfy = resultSet.getBigDecimal(1);
                        }
                    }
                }
                unpayTotal = unpayTotal.add(account.dhbj).add(account.dhlx).add(account.yqfy).add(account.glf);
            }
        }
        return accounts.toArray(new LoanAccount[accounts.size()]);
    }
    
    @Override
    public LoanAccountInfo[] getLoanAccountInfo()
        throws Throwable
    {
        List<LoanAccountInfo> accounts = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6230.F01, T6230.F03,T6230.F11,T6230.F13,T6230.F14,T6230.F05,T6230.F06,T6230.F07,T6230.F08,T6230.F09,T6231.F21 AS F21 FROM S62.T6230 LEFT JOIN S62.T6231 ON T6230.F01=T6231.F01 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?) ORDER BY F01 DESC"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YDF.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        LoanAccountInfo loanAccount = new LoanAccountInfo();
                        loanAccount.id = resultSet.getInt(1);
                        loanAccount.title = resultSet.getString(2);
                        loanAccount.F11 = EnumParser.parse(T6230_F11.class, resultSet.getString(3));
                        loanAccount.F13 = EnumParser.parse(T6230_F13.class, resultSet.getString(4));
                        loanAccount.F14 = EnumParser.parse(T6230_F14.class, resultSet.getString(5));
                        loanAccount.F05 = resultSet.getBigDecimal("F05");
                        loanAccount.F06 = resultSet.getBigDecimal("F06");
                        loanAccount.F07 = resultSet.getBigDecimal("F07");
                        loanAccount.F08 = resultSet.getInt("F08");
                        loanAccount.F09 = resultSet.getInt("F09");
                        loanAccount.F21 = EnumParser.parse(T6231_F21.class, resultSet.getString("F21"));
                        loanAccount.proess =
                            (loanAccount.F05.doubleValue() - loanAccount.F07.doubleValue())
                                / loanAccount.F05.doubleValue();
                        accounts.add(loanAccount);
                    }
                }
            }
            
            for (LoanAccountInfo account : accounts)
            {
                int bidID = account.id;
                
                // 待还本金
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_BJ);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.dhbj = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 待还利息
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_LX);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.dhlx = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 管理费
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.CJFWF);
                    pstmt.setString(3, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.glf = resultSet.getBigDecimal(1);
                        }
                    }
                }
                // 逾期费用
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?"))
                {
                    pstmt.setInt(1, bidID);
                    pstmt.setInt(2, FeeCode.TZ_YQGLF);
                    pstmt.setInt(3, FeeCode.TZ_FX);
                    pstmt.setString(4, T6252_F09.WH.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            account.yqfy = resultSet.getBigDecimal(1);
                        }
                    }
                }
                unpayTotal = unpayTotal.add(account.dhbj).add(account.dhlx).add(account.yqfy).add(account.glf);
            }
        }
        return accounts.toArray(new LoanAccountInfo[accounts.size()]);
    }
    
    @Override
    public FinancialPlan getFinancialPlan()
        throws Throwable
    {
        FinancialPlan plan = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F07, F09, F22,CURRENT_TIMESTAMP(),(1-F04/F03) FROM S64.T6410 ORDER BY F20 DESC LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        plan = new FinancialPlan();
                        plan.id = resultSet.getInt(1);
                        plan.title = resultSet.getString(2);
                        plan.total = resultSet.getBigDecimal(3);
                        plan.remaining = resultSet.getBigDecimal(4);
                        plan.yield = resultSet.getBigDecimal(5);
                        plan.planState = EnumParser.parse(PlanState.class, resultSet.getString(6));
                        plan.fromSellTime = resultSet.getTimestamp(7);
                        plan.condition = resultSet.getBigDecimal(8);
                        plan.currentTime = resultSet.getTimestamp(9);
                        plan.progress = resultSet.getDouble(10);
                    }
                }
            }
        }
        return plan;
    }
    
    @Override
    public Bdlb[] getBids()
        throws Throwable
    {
        ArrayList<Bdlb> list = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, ");
        sql.append("T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20, T6230.F28 AS F21,T6231.F27 AS F22,T6231.F28 AS F23, T6231.F33 AS F24 ");
        sql.append("FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01");
        sql.append("  WHERE T6230.F20 = ?");
        sql.append(" AND T6231.F33 in( ?,?)");
        sql.append("  ORDER BY T6230.F20 DESC , T6230.F22 DESC LIMIT 0,2");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                ps.setString(1, T6230_F20.TBZ.name());
                //发布项目投资端选择在APP端显示，那么此项目处于“预发布”、“投资中”状态时，只在APP、微信端显示此项目，
                ps.setString(2, T6231_F33.PC.name());
                ps.setString(3, T6231_F33.PC_APP.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
                        record.F21 = T6230_F28.parse(resultSet.getString(21));
                        record.F22 = T6231_F27.parse(resultSet.getString(22));
                        record.F23 = resultSet.getBigDecimal(23);
                        record.F24 = T6231_F33.parse(resultSet.getString(24));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
    }
    
    @Override
    public BigDecimal getUnpayTotal()
        throws Throwable
    {
        return unpayTotal;
    }
    
    public static class IndexManageFactory implements ServiceFactory<IndexManage>
    {
        
        @Override
        public IndexManage newInstance(ServiceResource serviceResource)
        {
            return new IndexManageImpl(serviceResource);
        }
        
    }
    
    @Override
    public T5127_F03 getJkdj()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return getDj(T5127_F02.JK, resultSet.getBigDecimal(1));
                        
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T5127_F03 getTzdj()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return getDj(T5127_F02.TZ, resultSet.getBigDecimal(1));
                    }
                }
            }
        }
        return null;
    }
    
    // 根据金额,类型得到等级
    private T5127_F03 getDj(T5127_F02 type, BigDecimal money)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F04 > ? AND T5127.F05 <= ? AND T5127.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, type.name());
                pstmt.setBigDecimal(2, money);
                pstmt.setBigDecimal(3, money);
                pstmt.setString(4, T5127_F06.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T5127_F03.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public BigDecimal getJkje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                        
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public BigDecimal getTzje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public BigDecimal getCzze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(T6102.F06) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6101.F02 = ? AND T6102.F03 IN(?,?) "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, FeeCode.CZ);
                pstmt.setInt(3, FeeCode.CZ_XX);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getTxze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(T6102.F07)-SUM(T6102.F06) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6101.F02 = ? AND T6102.F03 IN(?,?,?) "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, FeeCode.TX);
                pstmt.setInt(3, FeeCode.TX_CB);
                pstmt.setInt(4, FeeCode.TX_SXF);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getDsbj()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, FeeCode.TZ_BJ);
                pstmt.setString(3, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getDslx()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setString(3, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    private String getUsrCustId(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
}
