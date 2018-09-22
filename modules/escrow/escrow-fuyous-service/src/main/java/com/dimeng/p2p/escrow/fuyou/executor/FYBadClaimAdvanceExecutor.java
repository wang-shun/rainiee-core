/*
 * 文 件 名:  FYBadClaimAdvanceExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  不良债权购买执行器
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.escrow.fuyou.executor;

import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6529;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.order.BadClaimAdvanceExecutor;

/**
 * 不良债权购买执行器
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月29日]
 */
@AchieveVersion(version = 20151216)
@ResourceAnnotation
public class FYBadClaimAdvanceExecutor extends BadClaimAdvanceExecutor {
    
    /** <默认构造函数>
     */
    public FYBadClaimAdvanceExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYBadClaimAdvanceExecutor.class;
    }
    
    /**
     * 购买机构转账给债权所有者
     */
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            T6501 t6501 = selectT6501(connection, orderId);
            if (null == t6501) {
                throw new LogicalException("订单不存在！");
            }
            
            //不良债权转让订单
            T6529 t6529 = selectT6529(connection, orderId);
            if (null == t6529) {
                throw new LogicalException("不良债权转让订单不存在！");
            }
            //购买人第三方标识
            String buyer = getUserCustId(connection, t6529.F04);
            if (null == buyer) {
                throw new LogicalException("购买人第三方标识不存在！");
            }
            //购买人风险保证金账户
            T6101 gmrzh = selectT6101(connection, t6529.F04, T6101_F03.FXBZJZH, false);
            if (null == gmrzh) {
                throw new LogicalException("购买人风险保证金账户不存在！");
            }
            if (gmrzh.F06.compareTo(t6529.F06) < 0) {
                throw new LogicalException("风险保证金账户余额不足，不能进行该笔不良资产购买！");
            }
            //标债权记录
            T6251 t6251 = selectT6251(connection, t6529.F05);
            if (null == t6251) {
                throw new LogicalException("债权不存在！");
            }
            T6230 t6230 = selectT6230(connection, t6251.F03);
            if (t6230 == null) {
                throw new LogicalException("借款标不存在");
            }
            if (t6230.F20 != T6230_F20.HKZ) {
                throw new LogicalException("借款标不是还款中状态，不能进行购买操作。");
            }
            T6101 tzrzh = selectT6101(connection, t6251.F04, T6101_F03.WLZH, false);
            if (null == tzrzh) {
                throw new LogicalException("投资人往来账户不存在！");
            }
            //投资人第三方标识
            String creditor = getUserCustId(connection, t6251.F04);
            
            //当购买人与债权人为同一人时,不进行转账
            if (!buyer.equals(creditor)) {
                logger.info("不良债权购买时，划拨开始...");
                
                //调用第三方划拨接口,债权购买人把资金转给投资人
                TransferManage manage = serviceSession.getService(TransferManage.class);
                Map<String, Object> map = manage.createTransferMap(
                        t6501.F10, buyer, creditor, PayUtil.getAmt(t6529.F06),  "");
                
                // 实体不为空，说明验签通过。
                if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                    params.put("result", "success");
                    logger.info("不良债权购买，划拨成功。" + "债权编码：" + t6251.F02);
                } else {
                    params.put("result", "fail");
                    logger.info("不良债权购买时，划拨失败。 债权编码：" + t6251.F02 + "， 购买人ID：" + t6529.F04 + ", 购买金额：" + t6251.F05);
                    logger.info("第三方返回失败描述：" + map.get("resp_desc").toString());
                }
            } else {
                params.put("result", "success");
                logger.info(String.format("不良债权购买人与不良债权持有人为同一人，不良债权购买人富友帐号：%s，不良债权持有人富友帐号：%s。", buyer, creditor));
            }            
        }
    }
}
