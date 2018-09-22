package com.dimeng.p2p.console.servlets.fuyou;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYPTAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.service.PublicManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * P2P垫付 <功能详细描述>
 * 
 * @author heshiping
 * @version [版本号, 2015年12月14日]
 */
@Right(id = "P2P_C_FINANCE_SBDF_PTDF", name = "垫付", moduleId = "P2P_C_FINANCE_ZJGL_PTDFGL", order = 1)
public class FyouSbdf extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("不良资产处理——IP:" + request.getRemoteAddr());
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int period = IntegerParser.parse(request.getParameter("period"));
        PublicManage publicManage = serviceSession.getService(PublicManage.class);
        try
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            List<Integer> orderIds = bidManage.addOrder(loanId);
            int accountId = serviceSession.getSession().getAccountId();
            Map<String, String> params = new HashMap<String, String>();
            {
                params.put("hint", "success");
                FYPTAdvanceExecutor advanceExecutor = getResourceProvider().getResource(FYPTAdvanceExecutor.class);
                params.put("accountId", String.valueOf(accountId));
                params.put("period", String.valueOf(period));
                Integer[] orderIdI = orderIds.toArray(new Integer[orderIds.size()]);
                int[] orderIdc = ArrayUtils.toPrimitive(orderIdI);
                // 插入流水
                publicManage.updtateT6501F10(orderIdc, FuyouTypeEnum.PTDF.name());
                logger.info("标ID：" + loanId + "-不良资产处理总数：" + orderIds.size());
                int i = 0;
                for (Integer orderId : orderIds)
                {
                    i++;
                    // 查询是否已存订单
                    publicManage.searchT6501(serviceSession, orderId, params, true);
                    // 重复<初次还款失败后，再次还款>
                    switch (params.get("state"))
                    {
                        case "DTJ":
                            advanceExecutor.submit(orderId, params);
                            if (!"true".equals(params.get("success")))
                            {
                                logger.info("第" + i + "条不良资产处理-失败");
                                params.put("hint", "fail");
                                // 失败时将垫付记录改回未还
                                publicManage.updateT6252(loanId, period);
                                break;
                            }
                            logger.info("第" + i + "条不良资产处理-成功");
                            advanceExecutor.confirm(orderId, params);
                            
                            break;
                        case "DQR":
                            // 第三方成功，平台未更新
                            advanceExecutor.confirm(orderId, params);
                            logger.info("第" + i + "条不良资产处理-确认成功");
                            break;
                        case "CG":
                            logger.info("第" + i + "条不良资产处理-已确认成功");
                            break;
                        default:
                            advanceExecutor.confirm(orderId, params);
                            logger.info("第" + i + "条不良资产处理-确认成功");
                            break;
                    }
                }
            }
            sendRedirect(request, response, "/console/finance/zjgl/dfgl/ydfList.htm");
        }
        catch (Throwable throwable)
        {
            publicManage.updateT6252(loanId, period);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                sendRedirect(request, response, "/console/finance/zjgl/dfgl/yqddfList.htm");
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
}
