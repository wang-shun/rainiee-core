package com.dimeng.p2p.order;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6246;
import com.dimeng.p2p.S62.enums.T6242_F10;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.S62.enums.T6246_F07;
import com.dimeng.p2p.S65.entities.T6554;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.parser.BooleanParser;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述: 公益标捐款处理器
 * 修 改 人:  wangming
 * 修改时间:  15-3-10
 */
public class DonationExecutor extends AbstractOrderExecutor {

    public DonationExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }

    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        T6554 t6554=selectT6554(connection,orderId);
        if(t6554==null){
            throw new LogicalException("订单详细不存在！");
        }
        //锁定标
        T6242 t6242=selectT6242(connection,t6554.F03);
        if(t6242==null){
            throw  new LogicalException("标的信息不存在!");
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean ajkt = BooleanParser.parse(configureProvider.getProperty(SystemVariable.BID_SFZJKT));
        if (!ajkt && t6554.F02 == t6242.F23)
        {
            throw new LogicalException("不可捐本账号发起的公益标");
        }
        if (t6242.F11 != T6242_F11.JKZ)
        {
            throw new LogicalException("不是捐款中状态,不能捐款");
        }
        if(t6554.F04.compareTo(t6242.F07) > 0)
        {
            throw  new LogicalException("捐款金额大于可捐金额");
        }
        // 校验最低起投金额
        BigDecimal min = t6242.F06;
        if (t6554.F04.compareTo(min) < 0)
        {
            throw new LogicalException("捐款金额不能低于最低起投金额");
        }
        //修改可捐金额
        t6242.F07 = t6242.F07.subtract(t6554.F04);

        if (t6242.F07.compareTo(BigDecimal.ZERO) > 0 && t6242.F07.compareTo(min) < 0)
        {
            throw new LogicalException("剩余可捐金额不能低于最低起捐金额");
        }
        // 锁定投资人资金账户
        T6101 czzh = selectT6101(connection, t6554.F02, T6101_F03.WLZH, true);
        if (czzh == null)
        {
            throw new LogicalException("投资人往来账户不存在");
        }
        // 扣减出账账户金额
        czzh.F06 = czzh.F06.subtract(t6554.F04);
        if (czzh.F06.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new LogicalException("账户金额不足");
        }
        T6101 rzzh=selectT6101(connection,t6242.F23, T6101_F03.WLZH, true);
        if(rzzh==null){
            throw  new LogicalException("借款人往来账号不存在!");
        }
        // 扣减可投金额
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6242 SET F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, t6242.F07);
            pstmt.setInt(2, t6242.F01);
            pstmt.execute();
        }
        {
            updateT6101(connection, czzh.F06, czzh.F01);
            // 资金流水
            T6102 t6102 = new T6102();
            t6102.F02 = czzh.F01;
            t6102.F03 = FeeCode.PUBLIC_GOOD_ID;
            t6102.F04 = rzzh.F01;
            t6102.F07 = t6554.F04;
            t6102.F08 = czzh.F06;
            t6102.F09 = String.format("公益捐款:%s，标题：%s", t6242.F21, t6242.F03);
            insertT6102(connection, t6102);
        }
        {
            // 增加入账账户金额
            rzzh.F06 = rzzh.F06.add(t6554.F04);
            updateT6101(connection, rzzh.F06, rzzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = rzzh.F01;
            t6102.F03 = FeeCode.PUBLIC_GOOD_ID;
            t6102.F04 = czzh.F01;
            t6102.F06 = t6554.F04;
            t6102.F08 = rzzh.F06;
            t6102.F09 = String.format("公益捐款:%s，标题：%s", t6242.F21, t6242.F03);
            insertT6102(connection, t6102);
        }
        // 插入捐款记录
        T6246 t6290 = new T6246();
        t6290.F02 = t6242.F01;
        t6290.F03 = t6554.F02;
        t6290.F04 = t6554.F04;
        t6290.F05 = t6554.F04;
        t6290.F07 = T6246_F07.F;
        int rid = insertT6246(connection, t6290);
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6554 SET F05 = ? WHERE F01 = ?"))
        {
            ps.setInt(1, rid);
            ps.setInt(2, t6554.F01);
            ps.executeUpdate();
        }
        if(BigDecimal.ZERO.compareTo(t6242.F07)==0){
            //没有可投金额 为满标情况
            try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S62.T6242 SET F11 = 'YJZ',F19=NOW() WHERE F01 = ?")) {
                pstmt.setInt(1, t6242.F01);
                pstmt.execute();
            }
        }
        // 发站内信
        T6110 t6110 = selectT6110(connection, t6554.F02);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("title", t6242.F03);
        envionment.set("money", t6554.F04.toString());
        String content = configureProvider.format(LetterVariable.TZR_JZCG, envionment);
        sendLetter(connection, t6554.F02, "捐赠成功", content);
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtils = new SMSUtils(configureProvider);
            int type = smsUtils.getTempleId(LetterVariable.TZR_JZCG.getDescription());
            sendMsg(connection, t6110.F04, t6242.F03, type);
        }
        else
        {
            sendMsg(connection, t6110.F04, content);
        }
    }

    private int insertT6246(Connection connection, T6246 entity)
            throws SQLException
    {
        try (PreparedStatement pstmt =
                     connection.prepareStatement("INSERT INTO S62.T6246 SET F02 = ?, F03 = ?, F04 = ?,F05=?,F07=?, F06 = CURRENT_TIMESTAMP()",
                             PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4,entity.F05);
            pstmt.setString(5,entity.F07.name());
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    private void updateT6101(Connection connection, BigDecimal F01, int F02)
            throws SQLException
    {
        try (PreparedStatement pstmt =
                     connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = now()  WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }

    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return DonationExecutor.class;
    }

    protected T6554 selectT6554(Connection connection,int orderId) throws  Throwable{
        T6554 record=null;
        try(PreparedStatement statement=connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S65.T6554 WHERE F01=?")){
            statement.setInt(1,orderId);
            try(ResultSet resultSet=statement.executeQuery()){
                if(resultSet.next()){
                    record=new T6554();
                    record.F01=resultSet.getInt(1);
                    record.F02=resultSet.getInt(2);
                    record.F03=resultSet.getInt(3);
                    record.F04=resultSet.getBigDecimal(4);
                    record.F05=resultSet.getInt(5);
                }
            }
        }
        return record;
    }

    public T6242 selectT6242(Connection connection, int loadId) throws Throwable {
        T6242 record = null;
        try (PreparedStatement pstmt =
                     connection.prepareStatement("SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, T6242.F15, "
                             + "T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21, T6242.F22, T6242.F23, T6242.F24"
                             + "  FROM S62.T6242  WHERE T6242.F01 = ? FOR UPDATE "))
        {
            pstmt.setInt(1, loadId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6242();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6242_F10.parse(resultSet.getString(10));
                    record.F11 = T6242_F11.parse(resultSet.getString(11));
                    record.F12 = resultSet.getString(12);
                    record.F13 = resultSet.getTimestamp(13);
                    record.F14 = resultSet.getInt(14);
                    record.F15 = resultSet.getTimestamp(15);
                    record.F16 = resultSet.getTimestamp(16);
                    record.F17 = resultSet.getTimestamp(17);
                    record.F18 = resultSet.getTimestamp(18);
                    record.F19 = resultSet.getTimestamp(19);
                    record.F20 = resultSet.getTimestamp(20);
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getString(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getString(24);

                }
            }
        }
        return record;
    }
}