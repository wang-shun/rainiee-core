package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.escrow.fuyou.entity.console.BalanceEntity;
import com.dimeng.p2p.escrow.fuyou.entity.console.T6110_FY;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.BalanceFace;
import com.dimeng.p2p.escrow.fuyou.service.UserAcctQueryManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 用户余额查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public class UserAcctQueryManageImpl extends AbstractEscrowService implements UserAcctQueryManage
{
    
    public UserAcctQueryManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<T6110_FY> selectUserList(final ServiceSession serviceSession, String userName, Paging paging,
        String userTag)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT T6110.F01,T6110.F02,T6110.F04,T6110.F05,T6110.F06,T6119.F03,T6110.F10,(SELECT T6101.F06 FROM S61.T6101 WHERE T6101.F02 = T6110.F01 AND T6101.F03 = 'WLZH'),");
            sql.append("(SELECT T6101.F06 FROM S61.T6101 WHERE T6101.F02 = T6110.F01 AND T6101.F03 = 'SDZH'),");
            sql.append("(SELECT T6101.F06 FROM S61.T6101 WHERE T6101.F02 = T6110.F01 AND T6101.F03 = 'FXBZJZH')");
            sql.append("FROM S61.T6110 LEFT JOIN S61.T6119 ON T6110.F01 = T6119.F01 JOIN S71.T7101 ON T6110.F01 <> T7101.F01");
            ArrayList<Object> parameters = new ArrayList<>();
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" WHERE T6110.F02 LIKE '%" + userName + "%'");
            }
            if (!StringHelper.isEmpty(userTag))
            {
                if ("ZRR".equals(userTag))
                {//个人
                    sql.append(" AND T6110.F06 = 'ZRR'");
                }
                else if ("QY".equals(userTag))
                {//企业
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if ("JG".equals(userTag))
                {//机构
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
            sql.append(" ORDER BY T6110.F09 DESC");
            return selectPaging(connection, new ArrayParser<T6110_FY>()
            {
                @Override
                public T6110_FY[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6110_FY> list = null;
                    ArrayList<T6110_FY> retList = new ArrayList<>();
                    StringBuilder thirdTags = new StringBuilder("");
                    while (resultSet.next())
                    {
                        T6110_FY record = new T6110_FY();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3) == null ? "" : resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = T6110_F06.parse(resultSet.getString(5));
                        String thirdTag = StringHelper.isEmpty(resultSet.getString(6)) ? "" : resultSet.getString(6);
                        record.thirdTag = thirdTag;
                        record.F10 = T6110_F10.parse(resultSet.getString(7));
                        record.pa_balance = resultSet.getBigDecimal(8).add(resultSet.getBigDecimal(10));
                        record.pf_balance = resultSet.getBigDecimal(9);
                        record.pt_balance = record.pa_balance.add(record.pf_balance);
                        
                        try
                        {
                            if (!StringHelper.isEmpty(thirdTag))
                            {
                                if (!StringHelper.isEmpty(thirdTags.toString()))
                                    thirdTags.append("|");
                                thirdTags.append(thirdTag);
                            }
                        }
                        catch (Throwable e)
                        {
                            logger.error(e);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    try
                    {
                        ArrayList<BalanceEntity> listBalance = null;
                        boolean flag = false;
                        if (thirdTags.toString().length() > 10)
                        {
                            flag = true;
                            BalanceFace face = new BalanceFace();
                            String mchnt_cd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
                            String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YECX.name());
                            String mchnt_txn_dt = new SimpleDateFormat("yyyyMMdd").format(new Date());
                            String cust_no = thirdTags.toString();
                            String actionUrl = configureProvider.format(FuyouVariable.FUYOU_QUERY_BLANCE);
                            listBalance =
                                face.executeBalance(mchnt_cd,
                                    mchnt_txn_ssn,
                                    mchnt_txn_dt,
                                    cust_no,
                                    actionUrl,
                                    serviceSession);
                        }
                        if (list == null)
                        {
                            return null;
                        }
                        int listSize = list.size();
                        for (int i = 0; i < listSize; i++)
                        {
                            String thirdTag = list.get(i).thirdTag;
                            T6110_FY t6110_FY = new T6110_FY();
                            t6110_FY.F01 = list.get(i).F01;
                            t6110_FY.F02 = list.get(i).F02;
                            t6110_FY.F04 = list.get(i).F04;
                            t6110_FY.F05 = list.get(i).F05;
                            t6110_FY.F06 = list.get(i).F06;
                            t6110_FY.F10 = list.get(i).F10;
                            t6110_FY.pa_balance = list.get(i).pa_balance;
                            t6110_FY.pf_balance = list.get(i).pf_balance;
                            t6110_FY.pt_balance = list.get(i).pt_balance;
                            t6110_FY.thirdTag = thirdTag;
                            if (StringHelper.isEmpty(thirdTag))
                            {
                                retList.add(t6110_FY);
                                continue;
                            }
                            if (flag)
                            {
                                for (int j = 0; j < listBalance.size(); j++)
                                {
                                    if (!thirdTag.equals(listBalance.get(j).thirdTag))
                                    {
                                        continue;
                                    }
                                    t6110_FY.ca_balance = listBalance.get(j).ca_balance;
                                    t6110_FY.cf_balance = listBalance.get(j).cf_balance;
                                    t6110_FY.ct_balance = listBalance.get(j).ct_balance;
                                    t6110_FY.cu_balance = listBalance.get(j).cu_balance;
                                    listBalance.remove(j);
                                    break;
                                }
                            }
                            retList.add(t6110_FY);
                        }
                    }
                    catch (Throwable e)
                    {
                        logger.error(e);
                    }
                    return ((retList == null || retList.size() == 0) ? null
                        : retList.toArray(new T6110_FY[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
        
    }
    
}