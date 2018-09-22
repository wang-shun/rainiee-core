/*
 * 文 件 名:  ScoreMallIndexManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.modules.regulators.policy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.repeater.policy.RiskAssessManage;
import com.dimeng.p2p.repeater.policy.entity.RiskSetCondition;

/**
 * <风险评估>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月9日]
 */
public class RiskAssessManageImpl extends AbstractPolicyService implements RiskAssessManage
{
    
    public RiskAssessManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T6147 getT6147(int userId)
        throws Throwable
    {
        
        if (0 >= userId)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            T6147 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6147 WHERE T6147.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6147();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6147_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getTimestamp(6);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public int updateT6147F05(int userId)
        throws Throwable
    {
        
        if (userId <= 0)
        {
            throw new ParameterException("参数值不能为空");
        }
        try (Connection conn = getConnection())
        {
            Integer t6147F01 = null;
            Integer t6147F05 = null;
            try (PreparedStatement pstmt =
                conn.prepareStatement("SELECT F01,F05 FROM S61.T6147 WHERE F02 = ? ORDER BY F06 DESC LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        t6147F01 = rs.getInt(1);
                        t6147F05 = rs.getInt(2);
                    }
                }
            }
            if (t6147F01 == null)
            {
                throw new ParameterException("用户风险评估记录不存在");
            }
            if (t6147F05 > 0)
            {
                execute(conn, "UPDATE S61.T6147 SET F05 = F05-1 WHERE F01 = ?", t6147F01);
                return t6147F05 - 1;
            }
            else
            {
                throw new ParameterException("不能超过用户一自然年内可进行的风险评估次数");
            }
            
        }
    }
    
    @Override
    public List<T6148> getT6148List()
        throws Throwable
    {
        ArrayList<T6148> list = null;
        try (Connection connection = getConnection();
            PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S61.T6148 ORDER BY F01");)
        {
            T6148 record = null;
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    record = new T6148();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = T6148_F02.parse(resultSet.getString(2));
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getTimestamp(5);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
    }
    
    @Override
    public void updateT6148s(RiskSetCondition riskSetCondition)
        throws Throwable
    {
        
        try (Connection conn = getConnection())
        {
            try
            {
                serviceResource.openTransactions(conn);
                Timestamp now = getCurrentTimestamp(conn);
                execute(conn,
                    "UPDATE S61.T6148 SET F04=?,F05=? WHERE F02 = ?",
                    riskSetCondition.maxBsx,
                    now,
                    T6148_F02.BSX);
                execute(conn,
                    "UPDATE S61.T6148 SET F03=?,F04=?,F05=? WHERE F02 = ?",
                    riskSetCondition.minJsx,
                    riskSetCondition.maxJsx,
                    now,
                    T6148_F02.JSX);
                execute(conn,
                    "UPDATE S61.T6148 SET F03=?,F04=?,F05=? WHERE F02 = ?",
                    riskSetCondition.minWjx,
                    riskSetCondition.maxWjx,
                    now,
                    T6148_F02.WJX);
                execute(conn,
                    "UPDATE S61.T6148 SET F03=?,F04=?,F05=? WHERE F02 = ?",
                    riskSetCondition.minJqx,
                    riskSetCondition.maxJqx,
                    now,
                    T6148_F02.JQX);
                execute(conn,
                    "UPDATE S61.T6148 SET F03=?,F05=? WHERE F02 = ?",
                    riskSetCondition.minJjx,
                    now,
                    T6148_F02.JJX);
                serviceResource.commit(conn);
            }
            catch (Exception e)
            {
                serviceResource.rollback(conn);
                throw e;
            }
        }
        
    }
    
}
