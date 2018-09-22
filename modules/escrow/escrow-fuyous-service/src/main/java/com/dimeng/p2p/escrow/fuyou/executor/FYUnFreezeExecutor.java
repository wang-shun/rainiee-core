/*
 * 文 件 名:  FYUnFreezeExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月3日
 */
package com.dimeng.p2p.escrow.fuyou.executor;

import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170;
import com.dimeng.p2p.escrow.fuyou.entity.unfreeze.UnFreezeRet;
import com.dimeng.p2p.escrow.fuyou.face.FundUnFreezeFace;
import com.dimeng.p2p.escrow.fuyou.service.unfreeze.UnFreezeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.UnFreezeExecutor;

/**
 *  富友 - 资金解冻处理器  Executor
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月3日]
 */

@AchieveVersion(version = 2160229)
@ResourceAnnotation
public class FYUnFreezeExecutor extends UnFreezeExecutor
{
    
    public FYUnFreezeExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, Map<String, String> params)
        throws Throwable
    {
        try (ServiceSession serviceSession = resourceProvider.getResource(ServiceProvider.class).createServiceSession())
        {
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            
            UnFreezeManage unFreezeManage = serviceSession.getService(UnFreezeManage.class);
            T6170[] t6170s = unFreezeManage.getT6170s();
            
            for (T6170 t6170 : t6170s)
            {
                logger.info("开始执行自动【资金解冻】任务，开始时间：" + new java.util.Date());
                try
                {
                    logger.info("------------- 需要解冻的冻结流水号：" + t6170.F02 + "----------------");
                    //获取参数 
                    FundUnFreezeFace face = new FundUnFreezeFace();
                    
                    //商户代码
                    String mchntCd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
                    
                    //解冻流水号
                    String mchntTxnSsn = MchntTxnSsn.getMts(FuyouTypeEnum.ZJJD.name());
                    logger.info("------------- 解冻请求流水号：" + mchntTxnSsn + "----------------");
                    
                    //用户第三方帐号
                    String custNo = unFreezeManage.getAccountId(t6170.F03);
                    
                    //解冻金额
                    String amt = t6170.F04.toString();
                    
                    String rem = "";
                    String actionUrl = configureProvider.format(FuyouVariable.FUYOU_FUND_UNFREEZE_URL);
                    
                    //请求接口
                    UnFreezeRet unFreeze =
                        face.executeFundUnFreeze(mchntCd, mchntTxnSsn, custNo, amt, rem, actionUrl, serviceSession);
                    
                    //处理请求结果
                    if (FuyouRespCode.JYCG.getRespCode().equals(unFreeze.getRespCode()))
                    {
                        //登录帐号
                        unFreeze.setCustNo(t6170.F03);
                        //冻结流水号
                        unFreeze.setFreezeSerialNo(t6170.F02);
                        //设定的解冻时间
                        unFreeze.setThawDate(t6170.F05);
                        //更新T6101用户资金
                        unFreezeManage.updateT6101(unFreeze);
                        
                        logger.info("-------------------- 自动资金解冻成功 ---------------------");
                    }
                    else
                    {
                        logger.info("解冻失败!失败的原因[" + BackCodeInfo.info(unFreeze.getRespCode()) + "]");
                    }
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
                logger.info("开始执行自动【资金解冻】任务，开始时间：" + new java.util.Date());
            }
        }
    }
    
}
