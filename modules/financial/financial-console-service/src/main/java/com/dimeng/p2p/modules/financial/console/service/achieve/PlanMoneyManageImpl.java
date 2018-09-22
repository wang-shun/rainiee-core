package com.dimeng.p2p.modules.financial.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.modules.financial.console.service.PlanMoneyManage;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanAddRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoney;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoneyAdd;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoneyView;
import com.dimeng.p2p.modules.financial.console.service.query.PlanMoneyQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class PlanMoneyManageImpl extends AbstractFinancialService implements
        PlanMoneyManage {

    public static class CreditorTransferManageFactory implements
            ServiceFactory<PlanMoneyManage> {

        @Override
        public PlanMoneyManage newInstance(ServiceResource serviceResource) {
            return new PlanMoneyManageImpl(serviceResource);
        }
    }

    public PlanMoneyManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }

    @Override
    public PagingResult<PlanMoney> planMoneySearch(
            PlanMoneyQuery planMoneyQuery, Paging paging) throws Throwable {
        StringBuilder sql = new StringBuilder(
                "SELECT T6410.F01,T6410.F02,T6410.F03,T6410.F04,T6410.F05,(SELECT COUNT(T6411.F01) FROM S64.T6411 WHERE T6411.F02 = T6410.F01),T6410.F20,T6410.F07,T6410.F09,CURRENT_TIMESTAMP() "
                        + " FROM S64.T6410 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        try(Connection connection = getConnection())
        {
            if (planMoneyQuery != null) {
                SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
                String string = planMoneyQuery.getPlanName();
                if (!StringHelper.isEmpty(string)) {
                    sql.append(" AND T6410.F02 LIKE ?");
                    parameters.add(sqlConnectionProvider.allMatch(string));
                }
                string = planMoneyQuery.getState();
                if (!StringHelper.isEmpty(string)) {
                    if ("DSQ".equals(string)) {
                        sql.append(" AND T6410.F07 = ? AND T6410.F09 > ?");
                        parameters.add(PlanState.YFB);
                        parameters.add(getCurrentTimestamp(connection));
                    } else if ("SQZ".equals(string)) {
                        sql.append(" AND T6410.F07 = ? AND T6410.F09 < ?");
                        parameters.add(PlanState.YFB);
                        parameters.add(getCurrentTimestamp(connection));
                    } else {
                        sql.append(" AND T6410.F07 = ?");
                        parameters.add(EnumParser.parse(PlanState.class, string));
                    }
                }
            }

            sql.append(" ORDER BY T6410.F20 DESC");

            return selectPaging(connection, new ArrayParser<PlanMoney>() {

                @Override
                public PlanMoney[] parse(ResultSet resultSet) throws SQLException {
                    ArrayList<PlanMoney> list = null;
                    while (resultSet.next()) {

                        PlanMoney planMoney = new PlanMoney();
                        planMoney.planMoneyId = resultSet.getInt(1);
                        planMoney.planName = resultSet.getString(2);
                        planMoney.zje = resultSet.getBigDecimal(3);
                        planMoney.ktye = resultSet.getBigDecimal(4);
                        planMoney.earningsRate = resultSet.getDouble(5);
                        planMoney.joinNumber = resultSet.getInt(6);
                        planMoney.issueTime = resultSet.getTimestamp(7);
                        planMoney.state = EnumParser.parse(T6410_F07.class,
                                resultSet.getString(8));
                        planMoney.startTime = resultSet.getTimestamp(9);
                        planMoney.currentTime = resultSet.getTimestamp(10);

                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(planMoney);
                    }
                    return list == null ? null : list.toArray(new PlanMoney[list
                            .size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }

    @Override
    public void addPlanMoney(PlanMoneyAdd planMoneyAdd) throws Throwable {
        if (planMoneyAdd == null) {
            throw new ParameterException("没有指定计划信息");
        }
        String planName;
        BigDecimal investCeiling;
        BigDecimal investFloor;
        BigDecimal planMoneys;
        double earningsYearRate = 0;
        double addYearRate = 0;
        double serveRate = 0;
        double quitRate = 0;
        int tenderScope = 0;
        int lockTime;
        Timestamp applyStartTime;
        Timestamp applyEndTime;
        T6410_F14 earningsWay = null;
        T6410_F07 t6410_F07 = null;
        String planDesc;
        int userId = serviceResource.getSession().getAccountId();

        {
            planName = planMoneyAdd.getPlanName();
            if (StringHelper.isEmpty(planName)) {
                throw new ParameterException("计划名称不能为空.");
            }
            investCeiling = planMoneyAdd.getInvestCeiling();
            if (investCeiling == null) {
                throw new ParameterException("用户投资上限不能为空");
            }
            investFloor = planMoneyAdd.getInvestFloor();
            if (investFloor == null) {
                throw new ParameterException("用户投资下限不能为空.");
            }
            planMoneys = planMoneyAdd.getPlanMoneys();
            if (planMoneys == null) {
                throw new ParameterException("计划金额不能为空.");
            }
            if (investCeiling.compareTo(investFloor) < 0) {
                throw new ParameterException("投资上限必须大于投资下限.");
            }
            earningsYearRate = planMoneyAdd.getEarningsYearRate();
            addYearRate = planMoneyAdd.getAddYearRate();
            serveRate = planMoneyAdd.getServeRate();
            quitRate = planMoneyAdd.getQuitRate();
            tenderScope = planMoneyAdd.getTenderScope();
            if (tenderScope <= 0) {
                throw new ParameterException("投资范围不能为空.");
            }
            lockTime = planMoneyAdd.getLockTime();
            if (lockTime <= 0) {
                throw new ParameterException("锁定时间不能为空.");
            }
            applyStartTime = planMoneyAdd.getApplyStartTime();
            if (applyStartTime == null) {
                throw new ParameterException("申请开始时间不能为空.");
            }
            applyEndTime = planMoneyAdd.getApplyEndTime();
            if (applyEndTime == null) {
                throw new ParameterException("申请截止时间不能为空.");
            }
            earningsWay = planMoneyAdd.getEarningsWay();
            if (earningsWay == null) {
                throw new ParameterException("收益方式不能为空.");
            }
            t6410_F07 = planMoneyAdd.getState();
            if (t6410_F07 == null) {
                throw new ParameterException("计划状态不能为空.");
            }
            planDesc = planMoneyAdd.getPlanDesc();
            if (StringHelper.isEmpty(planDesc)) {
                throw new ParameterException("计划介绍不能为空.");
            }
        }
        try(Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT COUNT(*) FROM S64.T6410 WHERE F07  = ?")) {
                pstmt.setString(1, PlanState.YFB.name());
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        if (resultSet.getInt(1) > 0) {
                            throw new LogicalException("已存在已发布的优选理财.");
                        }
                    }
                }
            }

            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT F01 FROM S71.T7110 WHERE F01 = ?")) {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new LoginException("用户不存在.");
                    }
                }
            }

            try (PreparedStatement pstmt = connection
                    .prepareStatement(
                            "INSERT INTO S64.T6410 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F08 = 0, F09 = ?, F10 = ?, F11 = ?, F13 = ADDDATE(?,INTERVAL ? MONTH), F14 = ?, F15 = ?, F16 = ?, F17 = ?, F18 = ?, F19 = ?, F20 = ?, F22 = ?, F23 = ?, F24 = ?",
                            PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, planName);
                pstmt.setBigDecimal(2, planMoneys);
                pstmt.setBigDecimal(3, planMoneys);
                pstmt.setDouble(4, earningsYearRate);
                pstmt.setInt(5, tenderScope);
                pstmt.setString(6, t6410_F07.name());
                pstmt.setTimestamp(7, applyStartTime);
                pstmt.setTimestamp(8, applyEndTime);
                pstmt.setInt(9, lockTime);
                pstmt.setTimestamp(10, applyEndTime);
                pstmt.setInt(11, lockTime);
                pstmt.setString(12, earningsWay.name());
                pstmt.setDouble(13, addYearRate);
                pstmt.setDouble(14, serveRate);
                pstmt.setDouble(15, quitRate);
                pstmt.setString(16, planDesc);
                pstmt.setInt(17, userId);
                pstmt.setTimestamp(18, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(19, investFloor);
                pstmt.setBigDecimal(20, investCeiling);
                pstmt.setString(21, T6410_F24.QEBXBZ.name());

                pstmt.execute();
            }
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public PlanMoneyView findPlanMoneyView(int planMoneyId) throws Throwable {
        if (planMoneyId <= 0) {
            return null;
        }

        PlanMoneyView record = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT F01,F02,F06,F07,F11,F14,F05,F24,ADDDATE(F10,INTERVAL 1 DAY),F15,F16,F17,F04,F23,F18,F08,CURRENT_TIMESTAMP(),F13,F09,F22,F03 FROM S64.T6410 WHERE F01 = ? LIMIT 1")) {
                pstmt.setInt(1, planMoneyId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        record = new PlanMoneyView();

                        record.planMoneyId = rs.getInt(1);
                        record.planName = rs.getString(2);
                        record.tblx = rs.getInt(3);
                        record.tblxmc = getTblxmc(rs.getInt(3));
                        record.state = EnumParser.parse(T6410_F07.class,
                                rs.getString(4));
                        record.lockTime = rs.getInt(5);
                        record.earningsWay = EnumParser.parse(T6410_F14.class,
                                rs.getString(6));
                        record.expectEarnings = rs.getDouble(7);
                        record.safeguardWay = EnumParser.parse(T6410_F24.class,
                                rs.getString(8));
                        record.planEnd = rs.getTimestamp(9);
                        record.addRate = rs.getDouble(10);
                        record.serveRate = rs.getDouble(11);
                        record.quitRate = rs.getDouble(12);
                        record.residueMoney = rs.getBigDecimal(13);
                        record.investCeiling = rs.getBigDecimal(14);
                        record.planMoneyDesc = rs.getString(15);
                        record.fullTime = rs.getInt(16);
                        record.currentTime = rs.getTimestamp(17);
                        record.lockEndTime = rs.getTimestamp(18);
                        record.planStart = rs.getTimestamp(19);
                        record.investFloor = rs.getBigDecimal(20);
                        record.planMoneys = rs.getBigDecimal(21);

                    }
                }
            }
        }

        return record;
    }

    @Override
    public PagingResult<PlanAddRecord> addRecord(int planMoneyId, Paging paging)
            throws Throwable {
        if (planMoneyId <= 0) {
            return null;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT F03,F04,F06 FROM S64.T6411 WHERE F02 = ? ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(planMoneyId);
        try(Connection connection = getConnection())
        {
            PagingResult<PlanAddRecord> pagingResult = selectPaging(
                    connection, new ArrayParser<PlanAddRecord>() {

                        @Override
                        public PlanAddRecord[] parse(ResultSet resultSet)
                                throws SQLException {
                            ArrayList<PlanAddRecord> list = null;
                            while (resultSet.next()) {

                                PlanAddRecord planAddRecord = new PlanAddRecord();

                                planAddRecord.userName = resultSet.getString(1);
                                planAddRecord.addMoney = resultSet.getBigDecimal(2);
                                planAddRecord.investTime = resultSet
                                        .getTimestamp(3);

                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                list.add(planAddRecord);
                            }
                            return list == null ? null : list
                                    .toArray(new PlanAddRecord[list.size()]);
                        }
                    }, paging, sql.toString(), parameters);

            PlanAddRecord[] planAddRecords = pagingResult.getItems();
            if (planAddRecords != null) {
                for (int i = 0; i < planAddRecords.length; i++) {
                    PlanAddRecord planAddRecord = planAddRecords[i];

                    if (planAddRecord != null) {
                        try (PreparedStatement ps = connection
                                .prepareStatement("SELECT F02 AS USERNAME FROM S61.T6110 WHERE F01=?")) {
                            ps.setInt(1,
                                    IntegerParser.parse(planAddRecord.userName));

                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {

                                    planAddRecord.userName = rs.getString(1);
                                }
                            }
                        }
                    }
                }
            }

            return pagingResult;
        }
    }

    @Override
    public boolean isExist() throws Throwable {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT F01 FROM S64.T6410 WHERE F07 IN (?)")) {
                ps.setString(1, T6410_F07.YFB.name());
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getTblxmc(int id) throws Throwable {
        String str = "";
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn
                    .prepareStatement("SELECT F02 FROM S62.T6211 WHERE F01=?")) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        str = rs.getString(1);
                    }
                }
            }
        }

        return str;
    }
}
