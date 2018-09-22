package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.common.enums.InvestType;
import com.dimeng.p2p.modules.statistics.console.service.VolumeManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.Profile;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeRegion;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeTimeLimit;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeType;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class VolumeManageImpl extends AbstractStatisticsService implements VolumeManage
{
    
    public VolumeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class VolumeManageFactory implements ServiceFactory<VolumeManage>
    {
        
        @Override
        public VolumeManage newInstance(ServiceResource serviceResource)
        {
            return new VolumeManageImpl(serviceResource);
        }
        
    }
    
    @Override
    public VolumeEntity[] getVolumeEntities(int year)
        throws Throwable
    {
        List<VolumeEntity> volumeEntities = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F04 FROM S70.T7042 WHERE F01=? ORDER BY F02 ASC"))
            {
                ps.setInt(1, year);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        VolumeEntity entity = new VolumeEntity();
                        entity.month = resultSet.getInt(1);
                        entity.amount = resultSet.getBigDecimal(2);
                        entity.count = resultSet.getInt(3);
                        volumeEntities.add(entity);
                    }
                }
            }
        }
        return volumeEntities.toArray(new VolumeEntity[volumeEntities.size()]);
    }
    
    @Override
    public VolumeEntity[] getAllVolumeEntities()
        throws Throwable
    {
        List<VolumeEntity> volumeEntities = new ArrayList<>();
        StringBuffer sqlStr = new StringBuffer("");
        sqlStr.append("SELECT A.F01,A.F02, ");
        sqlStr.append("(SELECT SUM(S.F03) FROM S70.T7042 S WHERE DATE_FORMAT(CONCAT(S.F01,S.F02,'01'),'%Y-%m') <= DATE_FORMAT(CONCAT(A.F01,A.F02,'01'),'%Y-%m')) ");
        sqlStr.append("FROM S70.T7042 A ");
        sqlStr.append("WHERE DATE_FORMAT(CONCAT(A.F01,A.F02,'01'),'%Y-%m') ");
        sqlStr.append("BETWEEN DATE_FORMAT(date_add(CURRENT_DATE(), interval -11 month),'%Y-%m')  AND DATE_FORMAT(CURRENT_DATE(),'%Y-%m') ");
        sqlStr.append("GROUP BY A.F01,A.F02 ");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        VolumeEntity entity = new VolumeEntity();
                        entity.month = resultSet.getInt(2);
                        entity.amount = resultSet.getBigDecimal(3);
                        volumeEntities.add(entity);
                    }
                }
            }
        }
        return volumeEntities.toArray(new VolumeEntity[volumeEntities.size()]);
    }
    
    @Override
    public VolumeEntity[] getLastYearVolumeEntities(int year)
        throws Throwable
    {
        List<VolumeEntity> volumeEntities = new ArrayList<>();
        if (year > 0)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02,F03,F04 FROM S70.T7042 WHERE F01=? ORDER BY F02 ASC"))
                {
                    ps.setInt(1, year - 1);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            VolumeEntity entity = new VolumeEntity();
                            entity.month = resultSet.getInt(1);
                            entity.amount = resultSet.getBigDecimal(2);
                            entity.count = resultSet.getInt(3);
                            volumeEntities.add(entity);
                        }
                    }
                }
            }
        }
        return volumeEntities.toArray(new VolumeEntity[volumeEntities.size()]);
    }
    
    @Override
    public Profile getProfile(int year)
        throws Throwable
    {
        Profile profile = new Profile();
        BigDecimal lastAmount = null;
        BigDecimal lastCount = null;
        StringBuffer sqlStr = new StringBuffer("SELECT SUM(F03),SUM(F04) FROM S70.T7042 ");
        if (year > 0)
        {
            sqlStr.append("WHERE F01=? ");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        profile.totalAmount = resultSet.getBigDecimal(1);
                        profile.totalCount = resultSet.getBigDecimal(2);
                    }
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year - 1);
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        lastAmount = resultSet.getBigDecimal(1);
                        lastCount = resultSet.getBigDecimal(2);
                    }
                }
            }
        }
        
        if (lastAmount != null && lastAmount.floatValue() != 0)
        {
            profile.amountRate = profile.totalAmount.divide(lastAmount, 2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            profile.amountRate = BigDecimal.ONE;
        }
        if (lastCount != null && lastCount.floatValue() != 0)
        {
            profile.countRate = profile.totalCount.divide(lastAmount, 2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            profile.countRate = BigDecimal.ONE;
        }
        return profile;
    }
    
    @Override
    public int[] getStatisticedYear()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7042"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        years.add(resultSet.getInt(1));
                    }
                }
            }
        }
        int[] ys = new int[years.size()];
        for (int i = 0; i < years.size(); i++)
        {
            ys[i] = years.get(i);
        }
        return ys;
    }
    
    @Override
    public VolumeType[] getVolumeTypes(int year)
        throws Throwable
    {
        VolumeType[] volumeTypes = new VolumeType[5];
        volumeTypes[0] = new VolumeType();
        volumeTypes[0].type = InvestType.XYRZB;
        volumeTypes[1] = new VolumeType();
        volumeTypes[1].type = InvestType.JGDBB;
        volumeTypes[2] = new VolumeType();
        volumeTypes[2].type = InvestType.SDRZB;
        volumeTypes[3] = new VolumeType();
        volumeTypes[3].type = InvestType.DYRZB;
        volumeTypes[4] = new VolumeType();
        StringBuffer sqlStr = new StringBuffer("SELECT IFNULL(SUM(F03),0),IFNULL(SUM(F04),0),F05 FROM S70.T7043 ");
        if (year > 0)
        {
            sqlStr.append("WHERE F01=? ");
        }
        sqlStr.append("GROUP BY T7043.F05");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if ("SDRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[2].amount = resultSet.getBigDecimal(1);
                            volumeTypes[2].count = resultSet.getInt(2);
                        }
                        else if ("JGDB".equals(resultSet.getString(3)))
                        {
                            volumeTypes[1].amount = resultSet.getBigDecimal(1);
                            volumeTypes[1].count = resultSet.getInt(2);
                        }
                        else if ("XYRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[0].amount = resultSet.getBigDecimal(1);
                            volumeTypes[0].count = resultSet.getInt(2);
                        }
                        else if ("DYRZ".equals(resultSet.getString(3)))
                        {
                            volumeTypes[3].amount = resultSet.getBigDecimal(1);
                            volumeTypes[3].count = resultSet.getInt(2);
                        }
                        volumeTypes[4].amount = volumeTypes[4].amount.add(resultSet.getBigDecimal(1));
                        volumeTypes[4].count += resultSet.getInt(2);
                    }
                }
            }
        }
        return volumeTypes;
    }
    
    @Override
    public VolumeTimeLimit[] getVolumeTimeLimits(int year)
        throws Throwable
    {
        VolumeTimeLimit[] timeLimits = new VolumeTimeLimit[8];
        timeLimits[7] = new VolumeTimeLimit();
        StringBuffer sqlStr =
            new StringBuffer("SELECT IFNULL(SUM(F03),0),IFNULL(SUM(F04),0) FROM S70.T7044 WHERE 1=1 ");
        if (year > 0)
        {
            sqlStr.append("AND F01=? ");
        }
        sqlStr.append("AND F05>? AND F05<=? AND F07=? ");
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < 4; i++)
            {
                timeLimits[i] = new VolumeTimeLimit();
                try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
                {
                    if (year > 0)
                    {
                        ps.setInt(1, year);
                        ps.setInt(2, 3 * i);
                        ps.setInt(3, 3 * (i + 1));
                        ps.setString(4, "F");
                    }
                    else
                    {
                        ps.setInt(1, 3 * i);
                        ps.setInt(2, 3 * (i + 1));
                        ps.setString(3, "F");
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            timeLimits[i].amount = resultSet.getBigDecimal(1);
                            timeLimits[i].count = resultSet.getInt(2);
                            timeLimits[7].amount = timeLimits[7].amount.add(timeLimits[i].amount);
                            timeLimits[7].count = timeLimits[7].count + timeLimits[i].count;
                        }
                    }
                }
            }
            timeLimits[4] = new VolumeTimeLimit();
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                    ps.setInt(2, 12);
                    ps.setInt(3, 24);
                    ps.setString(4, "F");
                }
                else
                {
                    ps.setInt(1, 12);
                    ps.setInt(2, 24);
                    ps.setString(3, "F");
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        timeLimits[4].amount = resultSet.getBigDecimal(1);
                        timeLimits[4].count = resultSet.getInt(2);
                        timeLimits[7].amount = timeLimits[7].amount.add(resultSet.getBigDecimal(1));
                        timeLimits[7].count = timeLimits[7].count + timeLimits[4].count;
                    }
                }
            }
            timeLimits[5] = new VolumeTimeLimit();
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                    ps.setInt(2, 24);
                    ps.setInt(3, 37);
                    ps.setString(4, "F");
                }
                else
                {
                    ps.setInt(1, 24);
                    ps.setInt(2, 37);
                    ps.setString(3, "F");
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        timeLimits[5].amount = resultSet.getBigDecimal(1);
                        timeLimits[5].count = resultSet.getInt(2);
                        timeLimits[7].amount = timeLimits[7].amount.add(resultSet.getBigDecimal(1));
                        timeLimits[7].count = timeLimits[7].count + timeLimits[5].count;
                    }
                }
            }
            timeLimits[6] = new VolumeTimeLimit();
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                    ps.setInt(2, 0);
                    ps.setInt(3, 365);
                    ps.setString(4, "S");
                }
                else
                {
                    ps.setInt(1, 0);
                    ps.setInt(2, 365); //天标最大用365计算
                    ps.setString(3, "S");
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        timeLimits[6].amount = resultSet.getBigDecimal(1);
                        timeLimits[6].count = resultSet.getInt(2);
                        timeLimits[7].amount = timeLimits[7].amount.add(resultSet.getBigDecimal(1));
                        timeLimits[7].count = timeLimits[7].count + timeLimits[6].count;
                    }
                }
            }
        }
        return timeLimits;
    }
    
    @Override
    public VolumeRegion[] getVolumeRegions(int year)
        throws Throwable
    {
        List<VolumeRegion> volumeRegions = new ArrayList<>();
        VolumeRegion vr = new VolumeRegion();
        StringBuffer sqlStr =
            new StringBuffer("SELECT * from (SELECT IFNULL(SUM(F03),0) as tot ,IFNULL(SUM(F04),0),F05 FROM S70.T7045 ");
        if (year > 0)
        {
            sqlStr.append("WHERE F01=? ");
        }
        sqlStr.append("GROUP BY (SELECT T5019.F06 FROM S50.T5019 WHERE T5019.F01 = T7045.F05) )temp ORDER BY tot DESC");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sqlStr.toString()))
            {
                if (year > 0)
                {
                    ps.setInt(1, year);
                }
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        VolumeRegion volumeRegion = new VolumeRegion();
                        volumeRegion.amount = resultSet.getBigDecimal(1);
                        volumeRegion.count = resultSet.getInt(2);
                        volumeRegion.regionId = resultSet.getInt(3);
                        volumeRegions.add(volumeRegion);
                        vr.amount = vr.amount.add(resultSet.getBigDecimal(1));
                        vr.count = vr.count + volumeRegion.count;
                    }
                }
            }
            if (volumeRegions.size() > 0)
            {
                for (VolumeRegion volumeRegion : volumeRegions)
                {
                    try (PreparedStatement ps = connection.prepareStatement("SELECT F06 FROM S50.T5019 WHERE F01=?"))
                    {
                        ps.setInt(1, volumeRegion.regionId);
                        try (ResultSet resultSet = ps.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                volumeRegion.province = resultSet.getString(1);
                            }
                        }
                    }
                }
            }
        }
        vr.province = "总计";
        volumeRegions.add(vr);
        return volumeRegions.toArray(new VolumeRegion[volumeRegions.size()]);
    }
    
    @Override
    public void export(OutputStream outputStream, int year, String charset)
        throws Throwable
    {
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        VolumeType[] volumeTypes = getVolumeTypes(year);
        VolumeTimeLimit[] timeLimits = getVolumeTimeLimits(year);
        VolumeRegion[] volumeRegions = getVolumeRegions(year);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(zipOutputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            zipOutputStream.putNextEntry(new ZipEntry("按标的属性.csv"));
            writer.write("类型");
            writer.write("笔数");
            writer.write("金额(元)");
            writer.newLine();
            for (int i = 0; i < volumeTypes.length; i++)
            {
                VolumeType volumeType = volumeTypes[i];
                if (InvestType.XYRZB == volumeType.type)
                {
                    writer.write("信用认证标");
                }
                else if (InvestType.JGDBB == volumeType.type)
                {
                    writer.write("担保标");
                }
                else if (InvestType.DYRZB == volumeType.type)
                {
                    writer.write("抵押认证标");
                }
                else
                {
                    if (i == volumeTypes.length - 1)
                    {
                        writer.write("总计");
                    }
                    else
                    {
                        writer.write("实地认证标");
                    }
                }
                writer.write(volumeType.count);
                writer.write(volumeType.amount);
                writer.newLine();
            }
            out.flush();
            
            zipOutputStream.putNextEntry(new ZipEntry("按期限.csv"));
            writer.write("期限(月)");
            writer.write("笔数");
            writer.write("金额(元)");
            writer.newLine();
            String[] months = {"1—3个月", "4—6个月", "7—9个月", "10—12个月", "12—24个月", "24个月以上", "其他(天标)", "总计"};
            int i = 0;
            for (VolumeTimeLimit timeLimit : timeLimits)
            {
                writer.write(months[i++]);
                writer.write(timeLimit.count);
                writer.write(timeLimit.amount);
                writer.newLine();
            }
            out.flush();
            
            if (volumeRegions != null && volumeRegions.length > 0)
            {
                zipOutputStream.putNextEntry(new ZipEntry("按地域.csv"));
                writer.write("地域");
                writer.write("笔数");
                writer.write("金额(元)");
                writer.newLine();
                for (VolumeRegion volumeRegion : volumeRegions)
                {
                    writer.write(volumeRegion.province);
                    writer.write(volumeRegion.count);
                    writer.write(volumeRegion.amount);
                    writer.newLine();
                }
            }
            out.flush();
            zipOutputStream.flush();
            zipOutputStream.finish();
        }
    }
}
