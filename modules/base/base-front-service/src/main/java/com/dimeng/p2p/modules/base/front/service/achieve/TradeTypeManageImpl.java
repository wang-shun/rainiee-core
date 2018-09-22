package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.entities.T5132;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S51.enums.T5132_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.modules.base.front.service.TradeTypeManage;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

public class TradeTypeManageImpl extends AbstractBaseService implements TradeTypeManage
{
    
    public TradeTypeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T5122[] search(T6110_F06 userType, T6110_F10 t6110_F10)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            List<Object> param = new ArrayList<Object>();
            StringBuffer sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? ");
            param.add(T5122_F03.QY.name());
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
            String status = "";
            try (PreparedStatement ps = connection.prepareStatement("SELECT F05 FROM S61.T6125 WHERE F02 = ? ")) {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString(1);
                    }
                }
            }
            if(isHasGuarant && !StringHelper.isEmpty(status) && (T6125_F05.SQCG.name().equals(status) || T6125_F05.QXCG.name().equals(status)) && (userType == T6110_F06.ZRR || (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.F))) {
                if(userType == T6110_F06.FZRR)
                {
                    sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? AND ( T5122.F05 = ? OR T5122.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                }
                else
                {
                    sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? AND ( T5122.F04 = ? OR T5122.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                }
            }
            else
            {
                if (userType == T6110_F06.ZRR) {
                    //个人
                    sqlBuff.append("AND T5122.F04 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.F) {
                    //企业
                    sqlBuff.append("AND T5122.F05 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S) {
                    //机构
                    sqlBuff.append("AND T5122.F06 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == null) {
                    //平台
                    sqlBuff.append("AND T5122.F07 = ? ");
                }
                if (userType == null && t6110_F10 == null) {
                    //信用类型
                    sqlBuff.append("AND T5122.F08 = ? ");
                }
                param.add(YesOrNo.yes.name());
            }

            return selectAll(connection, new ArrayParser<T5122>()
            {
                @Override
                public T5122[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5122> list = new ArrayList<>();
                    while (rs.next())
                    {
                        T5122 record = new T5122();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        list.add(record);
                    }
                    return list.toArray(new T5122[list.size()]);
                }
            }, sqlBuff.toString(), param);
        }
    }
    
    @Override
    public String get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S51.T5122 WHERE T5122.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public T5132[] searchT5132(T6110_F06 userType, T6110_F10 t6110_F10)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            List<Object> param = new ArrayList<Object>();
            StringBuffer sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5132 WHERE T5132.F03 = ? ");
            param.add(T5132_F03.QY.name());
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
            String status = "";
            try (PreparedStatement ps = connection.prepareStatement("SELECT F05 FROM S61.T6125 WHERE F02 = ? ")) {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString(1);
                    }
                }
            }
            if(isHasGuarant && !StringHelper.isEmpty(status) && T6125_F05.SQCG.name().equals(status) && (userType == T6110_F06.ZRR || (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.F))) {
                if(userType == T6110_F06.FZRR)
                {
                    sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5132 WHERE T5132.F03 = ? AND ( T5132.F05 = ? OR T5132.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                }
                else
                {
                    sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5132 WHERE T5132.F03 = ? AND ( T5132.F04 = ? OR T5132.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                }
            }
            else
            {
                if (userType == T6110_F06.ZRR) {
                    //个人
                    sqlBuff.append("AND T5132.F04 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.F) {
                    //企业
                    sqlBuff.append("AND T5132.F05 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S) {
                    //机构
                    sqlBuff.append("AND T5132.F06 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == null) {
                    //平台
                    sqlBuff.append("AND T5132.F07 = ? ");
                }
                param.add(YesOrNo.yes.name());
            }
            return selectAll(connection, new ArrayParser<T5132>()
            {
                @Override
                public T5132[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5132> list = new ArrayList<>();
                    T5132 record = null;
                    while (rs.next())
                    {
                        record = new T5132();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        list.add(record);
                    }
                    return list.toArray(new T5132[list.size()]);
                }
            }, sqlBuff.toString(), param);
        }
    }
    
}
