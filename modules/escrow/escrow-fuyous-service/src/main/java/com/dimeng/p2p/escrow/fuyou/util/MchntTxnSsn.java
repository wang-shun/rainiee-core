package com.dimeng.p2p.escrow.fuyou.util;

/**
 * 
 * 流水号
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 */
public class MchntTxnSsn
{
    /**
     * 业务+流水号
     * 描述:请用一句话描述
     * 作者:heshiping 
     * 创建时间：2015年9月24日
     * @return
     */
    public static String getMts(String type)
    {
        String end = String.valueOf(System.nanoTime());
        end = end.substring(11);
        return type.concat(String.valueOf(System.currentTimeMillis()).concat(end));
    }
}
