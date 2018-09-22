package com.dimeng.p2p.escrow.fuyou.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 提现手续费
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
public class PoundageUtil
{
    private static Log logger = LogFactory.getLog(PoundageUtil.class);
    
    public static BigDecimal getWithDrawPoundage(BigDecimal amt, ConfigureProvider configureProvider)
    {
        BigDecimal poundage = null;
        //查询提现手续费的模式 按额度还是按比例
        String pundageWay = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY);
        
        if ("BL".equals(pundageWay))
        {
            // 如果按比例计算，查询比例值
            String _proportion = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION);
            if (StringHelper.isEmpty(_proportion) || _proportion.contains("-"))
            {
                logger.info("提现手续费比例值为空或非法[WITHDRAW_POUNDAGE_PROPORTION]");
                throw new LogicalException("系统繁忙，请稍后再试！");
            }
            BigDecimal proportion = new BigDecimal(_proportion);
            poundage = amt.multiply(proportion).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            if (amt.compareTo(new BigDecimal("50000")) >= 0)
            { 
                logger.info(amt.compareTo(new BigDecimal("50000")));
                poundage = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20));
            }
            else
            {
                poundage = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5));
            }
        }
        return poundage;
    }
   
}
