/*
 * 文 件 名:  RiskQuesManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间:  2016年03月9日
 */
package com.dimeng.p2p.modules.regulators.policy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.entities.T6149;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.S61.enums.T6149_F07;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.AnswerQuery;
import com.dimeng.p2p.repeater.policy.query.QuesQuery;
import com.dimeng.p2p.repeater.policy.query.ResultQuery;
import com.dimeng.p2p.repeater.policy.query.RiskAssessmentResult;
import com.dimeng.p2p.repeater.policy.query.RiskQueryResult;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * <风险评估问题设置>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年03月09日]
 */
public class RiskQuesManageImpl extends AbstractPolicyService implements RiskQuesManage
{
    
    public RiskQuesManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 查询风险评估问题列表
     *
     * @param title
     * @param status
     * @param pag
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<QuesQuery> queryAllQues(String title, String status, Paging pag)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6149.F01,T6149.F02,T6149.F03,T6149.F04,T6149.F05,T6149.F06,T6149.F07,T6149.F08,T6149.F09,T6149.F10,T7110.F02 AS F11 FROM S61.T6149,S71.T7110 WHERE T6149.F10 = T7110.F01");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(title))
        {
            sql.append(" AND T6149.F02 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(title));
        }
        if (!StringHelper.isEmpty(status))
        {
            sql.append(" AND T6149.F07 = ? ");
            params.add(status);
        }
        sql.append(" ORDER BY T6149.F08,T6149.F09 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<QuesQuery>()
            {
                @Override
                public QuesQuery[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<QuesQuery> list = new ArrayList<QuesQuery>();
                    QuesQuery quesQuery;
                    while (rs.next())
                    {
                        quesQuery = new QuesQuery();
                        quesQuery.F01 = rs.getInt(1);
                        quesQuery.F02 = rs.getString(2);
                        quesQuery.F03 = rs.getString(3);
                        quesQuery.F04 = rs.getString(4);
                        quesQuery.F05 = rs.getString(5);
                        quesQuery.F06 = rs.getString(6);
                        quesQuery.F07 = T6149_F07.parse(rs.getString(7));
                        quesQuery.F08 = rs.getInt(8);
                        quesQuery.F09 = rs.getTimestamp(9);
                        quesQuery.F10 = rs.getInt(10);
                        quesQuery.opertor = rs.getString(11);
                        list.add(quesQuery);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new QuesQuery[list.size()]);
                }
            }, pag, sql.toString(), params);
        }
    }
    
    /**
     * 新增风险评估问题
     *
     * @param t6149
     * @throws Throwable
     */
    @Override
    public int addRiskQues(T6149 t6149)
        throws Throwable
    {
        int id = 0;
        try (Connection connection = getConnection())
        {
            
            //风险评估问题数限制
            judgeQy(connection, t6149);
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S61.T6149 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F10 = ? ",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, t6149.F02);
                ps.setString(2, t6149.F03);
                ps.setString(3, t6149.F04);
                ps.setString(4, t6149.F05);
                ps.setString(5, t6149.F06);
                ps.setString(6, t6149.F07.name());
                ps.setInt(7, t6149.F08);
                ps.setInt(8, serviceResource.getSession().getAccountId());
                ps.execute();
                try (ResultSet rs = ps.getGeneratedKeys())
                {
                    if (rs.next())
                    {
                        id = rs.getInt(1);
                    }
                }
            }
        }
        return id;
    }
    
    /**
     * 修改风险评估问题
     *
     * @param t6149
     * @throws Throwable
     */
    @Override
    public void updateRiskQues(T6149 t6149, String oldStatus)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            
            //风险评估问题数限制
            if (!T6149_F07.QY.name().equals(oldStatus))
            {
                judgeQy(connection, t6149);
            }
            
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S61.T6149 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F10 = ? WHERE F01 = ? ",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, t6149.F02);
                ps.setString(2, t6149.F03);
                ps.setString(3, t6149.F04);
                ps.setString(4, t6149.F05);
                ps.setString(5, t6149.F06);
                ps.setString(6, t6149.F07.name());
                ps.setInt(7, t6149.F08);
                ps.setInt(8, serviceResource.getSession().getAccountId());
                ps.setInt(9, t6149.F01);
                ps.execute();
            }
        }
    }
    
    /**
     * 根据ID查询风险评估问题
     *
     * @param id
     * @return
     * @throws Throwable
     */
    @Override
    public T6149 queryById(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6149.F01,T6149.F02,T6149.F03,T6149.F04,T6149.F05,T6149.F06,T6149.F07,T6149.F08,T6149.F09,T6149.F10 FROM S61.T6149 WHERE T6149.F01 = ? "))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    T6149 t6149 = null;
                    if (rs.next())
                    {
                        t6149 = new T6149();
                        t6149.F01 = rs.getInt(1);
                        t6149.F02 = rs.getString(2);
                        t6149.F03 = rs.getString(3);
                        t6149.F04 = rs.getString(4);
                        t6149.F05 = rs.getString(5);
                        t6149.F06 = rs.getString(6);
                        t6149.F07 = T6149_F07.parse(rs.getString(7));
                        t6149.F08 = rs.getInt(8);
                        t6149.F09 = rs.getTimestamp(9);
                        t6149.F10 = rs.getInt(10);
                    }
                    return t6149;
                }
            }
        }
    }
    
    /**
     * 查询当前用户的风险评估能力
     *
     * @return
     * @throws Throwable
     */
    @Override
    public T6147 getMyRiskResult()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<T6147>()
                {
                    @Override
                    public T6147 parse(ResultSet rs)
                        throws SQLException
                    {
                        T6147 t6147 = null;
                        if (rs.next())
                        {
                            t6147 = new T6147();
                            t6147.F01 = rs.getInt(1);
                            t6147.F02 = rs.getInt(2);
                            t6147.F03 = rs.getInt(3);
                            t6147.F04 = T6147_F04.parse(rs.getString(4));
                            t6147.F05 = rs.getInt(5);
                            t6147.F06 = rs.getTimestamp(6);
                        }
                        return t6147;
                    }
                },
                "SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6147 WHERE F02 = ? ORDER BY F06 DESC LIMIT 1 ",
                serviceResource.getSession().getAccountId());
        }
    }
    
    /**
     * 用户中心风险评估问题列表
     *
     * @return
     * @throws Throwable
     */
    @Override
    public List<QuesQuery> queryList()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection,
                new ItemParser<List<QuesQuery>>()
                {
                    @Override
                    public List<QuesQuery> parse(ResultSet rs)
                        throws SQLException
                    {
                        List<QuesQuery> list = null;
                        while (rs.next())
                        {
                            QuesQuery quesQuery = new QuesQuery();
                            quesQuery.F01 = rs.getInt(1);
                            quesQuery.F02 = rs.getString(2);
                            quesQuery.F03 = rs.getString(3);
                            quesQuery.F04 = rs.getString(4);
                            quesQuery.F05 = rs.getString(5);
                            quesQuery.F06 = rs.getString(6);
                            quesQuery.F07 = T6149_F07.parse(rs.getString(7));
                            quesQuery.F08 = rs.getInt(8);
                            quesQuery.F09 = rs.getTimestamp(9);
                            quesQuery.F10 = rs.getInt(10);
                            if (null == list)
                            {
                                list = new ArrayList<QuesQuery>();
                            }
                            list.add(quesQuery);
                        }
                        return list;
                    }
                },
                "SELECT T6149.F01,T6149.F02,T6149.F03,T6149.F04,T6149.F05,T6149.F06,T6149.F07,T6149.F08,T6149.F09,T6149.F10 FROM S61.T6149 WHERE T6149.F07 = ? ORDER BY T6149.F08,T6149.F09 DESC",
                T6149_F07.QY.name());
        }
        
    }
    
    /**
     * 风险评估
     *
     * @param results
     * @return
     * @throws Throwable
     */
    @Override
    public int assessment(List<RiskAssessmentResult> results)
        throws Throwable
    {
        T6147 t6147 = getMyRiskResult();
        int count =
            Integer.parseInt(serviceResource.getResource(ConfigureProvider.class)
                .getProperty(RegulatoryPolicyVariavle.ONE_YEAR_RISK_ASSESS_NUM));
        if (t6147 != null && t6147.F05 >= count)
        {
            throw new LoginException("评估次数已经超过最大评估次数");
        }
        int id = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6110 t6110 = getT6110(connection, serviceResource.getSession().getAccountId());
                if (T6110_F06.FZRR == t6110.F06)
                {
                    throw new LoginException("企业、机构不能参与评估");
                }
                String score =
                    serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(RegulatoryPolicyVariavle.RISK_ASSESS_VALUE);
                String[] scores = score.split(",");
                int a = scores.length > 0 ? Integer.parseInt(scores[0]) : 0;
                int b = scores.length > 1 ? Integer.parseInt(scores[1]) : 0;
                int c = scores.length > 2 ? Integer.parseInt(scores[2]) : 0;
                int d = scores.length > 3 ? Integer.parseInt(scores[3]) : 0;
                int aScore = 0;
                int bScore = 0;
                int cScore = 0;
                int dScore = 0;
                for (RiskAssessmentResult result : results)
                {
                    if ("A".equals(result.answer))
                    {
                        aScore += a;
                    }
                    else if ("B".equals(result.answer))
                    {
                        bScore += b;
                    }
                    else if ("C".equals(result.answer))
                    {
                        cScore += c;
                    }
                    else
                    {
                        dScore += d;
                    }
                }
                int totalScore = aScore + bScore + cScore + dScore;
                T6148 t6148 = selectT6148(connection, totalScore);
                if (null == t6148)
                {
                    throw new ParameterException("参数错误");
                }
                
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO S61.T6147 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? ",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, serviceResource.getSession().getAccountId());
                    ps.setInt(2, totalScore);
                    ps.setString(3, t6148.F02.name());
                    ps.setInt(4, alreadyCount(connection) + 1);
                    ps.setTimestamp(5, getCurrentTimestamp(connection));
                    ps.execute();
                    try (ResultSet rs = ps.getGeneratedKeys())
                    {
                        if (rs.next())
                        {
                            id = rs.getInt(1);
                        }
                    }
                }
                if (id > 0)
                {
                    for (RiskAssessmentResult result : results)
                    {
                        insert(connection,
                            "INSERT INTO S61.T6150 SET F02 = ?, F03 = ?, F04 = ? ",
                            id,
                            result.quesId,
                            result.answer);
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Throwable e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            
        }
        return id;
    }
    
    /**
     * 查询所有用户的风险评估结果
     *
     * @param paging
     * @param resultQuery
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<RiskQueryResult> queryAllResult(Paging paging, ResultQuery resultQuery)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6147.F01 AS F01, T6110.F02 AS F02, T6141.F02 AS F03, T6147.F03 AS F04, T6147.F04 AS F05, T6147.F06 AS F06 FROM S61.T6147,S61.T6110,S61.T6141 WHERE T6147.F02 = T6110.F01 AND T6110.F01 = T6141.F01 ");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(resultQuery.getName()))
        {
            sql.append(" AND T6110.F02 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(resultQuery.getName()));
        }
        
        if (resultQuery.getCreateTimeStart() != null)
        {
            sql.append(" AND DATE(T6147.F06) >= ? ");
            params.add(resultQuery.getCreateTimeStart());
        }
        
        if (resultQuery.getCreateTimeEnd() != null)
        {
            sql.append(" AND DATE(T6147.F06) <= ? ");
            params.add(resultQuery.getCreateTimeEnd());
        }
        
        sql.append(" ORDER BY T6147.F06 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<RiskQueryResult>()
            {
                @Override
                public RiskQueryResult[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<RiskQueryResult> list = null;
                    while (rs.next())
                    {
                        RiskQueryResult result = new RiskQueryResult();
                        result.riskId = rs.getInt(1);
                        result.userName = rs.getString(2);
                        result.realName = rs.getString(3);
                        result.score = rs.getInt(4);
                        result.riskType = T6147_F04.parse(rs.getString(5)).getChineseName();
                        result.time = rs.getTimestamp(6);
                        if (list == null)
                        {
                            list = new ArrayList<RiskQueryResult>();
                        }
                        list.add(result);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new RiskQueryResult[list.size()]);
                }
            }, paging, sql.toString(), params);
        }
    }
    
    /**
     * 风险评估详情
     *
     * @param riskId
     * @return
     * @throws Throwable
     */
    @Override
    public RiskQueryResult queryRiskDetail(int riskId)
        throws Throwable
    {
        RiskQueryResult result = null;
        try (Connection connection = getConnection())
        {
            result =
                select(connection,
                    new ItemParser<RiskQueryResult>()
                    {
                        @Override
                        public RiskQueryResult parse(ResultSet rs)
                            throws SQLException
                        {
                            RiskQueryResult r = null;
                            if (rs.next())
                            {
                                r = new RiskQueryResult();
                                r.riskId = rs.getInt(1);
                                r.userName = rs.getString(2);
                                r.realName = rs.getString(3);
                                r.score = rs.getInt(4);
                                r.riskType = T6147_F04.parse(rs.getString(5)).getChineseName();
                                r.time = rs.getTimestamp(6);
                            }
                            return r;
                        }
                    },
                    "SELECT T6147.F01 AS F01, T6110.F02 AS F02, T6141.F02 AS F03, T6147.F03 AS F04, T6147.F04 AS F05, T6147.F06 AS F06 FROM S61.T6147,S61.T6110,S61.T6141 WHERE T6147.F02 = T6110.F01 AND T6110.F01 = T6141.F01 AND T6147.F01 = ? ",
                    riskId);
            List<AnswerQuery> answerList = new ArrayList<AnswerQuery>();
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6149.F02 AS F01,T6149.F03 AS F02,T6149.F04 AS F03,T6149.F05 AS F04,T6149.F06 AS F05,T6150.F04 AS F06 FROM S61.T6150,S61.T6149 WHERE T6150.F03 = T6149.F01 AND T6150.F02 = ? "))
            {
                ps.setInt(1, riskId);
                try (ResultSet rs = ps.executeQuery())
                {
                    
                    while (rs.next())
                    {
                        AnswerQuery answer = new AnswerQuery();
                        answer.question = rs.getString(1);
                        answer.optionA = rs.getString(2);
                        answer.optionB = rs.getString(3);
                        answer.optionC = rs.getString(4);
                        answer.optionD = rs.getString(5);
                        answer.answer = rs.getString(6);
                        answerList.add(answer);
                    }
                }
            }
            result.answerList = answerList;
        }
        return result;
    }
    
    /**
     * 查询自然年内剩余的风险评估次数
     *
     * @return
     * @throws Throwable
     */
    @Override
    public int leftRiskCount()
        throws Throwable
    {
        int count = 0;
        try (Connection connection = getConnection())
        {
            count = alreadyCount(connection);
        }
        int haveCount =
            IntegerParser.parse(serviceResource.getResource(ConfigureProvider.class)
                .getProperty(RegulatoryPolicyVariavle.ONE_YEAR_RISK_ASSESS_NUM));
        if (haveCount - count < 0)
            return 0;
        return haveCount - count;
    }
    
    private int alreadyCount(Connection connection)
        throws Throwable
    {
        int count = 0;
        
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT T6147.F05 FROM S61.T6147 WHERE YEAR(T6147.F06) = YEAR(CURRENT_DATE()) AND T6147.F02 = ? ORDER BY T6147.F06 DESC LIMIT 1"))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    count = rs.getInt(1);
                }
            }
        }
        return count;
    }
    
    private T6148 selectT6148(Connection connection, int score)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S61.T6148 WHERE F03 <= ? AND F04 >= ? LIMIT 1 "))
        {
            ps.setInt(1, score);
            ps.setInt(2, score);
            try (ResultSet rs = ps.executeQuery())
            {
                T6148 t6148 = null;
                if (rs.next())
                {
                    t6148 = new T6148();
                    t6148.F01 = rs.getInt(1);
                    t6148.F02 = T6148_F02.parse(rs.getString(2));
                    t6148.F03 = rs.getInt(3);
                    t6148.F04 = rs.getInt(4);
                }
                return t6148;
            }
        }
    }
    
    /**
     * <风险评估问题数限制>
     * <功能详细描述>
     * @param connection
     * @param t6149
     * @throws Throwable
     */
    private void judgeQy(Connection connection, T6149 t6149)
        throws Throwable
    {
        
        if (T6149_F07.QY == t6149.F07)
        {
            if (getQyRiskCount(connection) >= 20)
            {
                throw new ParameterException("启用问题数已达20，不可启用");
            }
        }
    }
    
    private int getQyRiskCount(Connection connection)
        throws Throwable
    {
        int count = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(1) FROM S61.T6149 WHERE T6149.F07=?"))
        {
            pstmt.setString(1, T6149_F07.QY.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }
    
    @Override
    public int qyRiskCount()
        throws Throwable
    {
        int count = 0;
        try (Connection connection = getConnection())
        {
            count = getQyRiskCount(connection);
        }
        return count;
    }
    
    private T6110 getT6110(Connection connection, int userId)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6110 WHERE T6110.F01 = ?"))
        {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6110();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = T6110_F06.parse(resultSet.getString(6));
                }
            }
        }
        return record;
    }
}
