package com.dimeng.p2p.console.servlets.finance.zjgl.fksh;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.bid.console.service.FkshManage;
import com.dimeng.p2p.modules.bid.console.service.TenderConfirmManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;
import com.dimeng.p2p.order.TenderConfirmExecutor;
import com.dimeng.p2p.signature.fdd.service.IFddSignatureService;
import com.dimeng.p2p.signature.fdd.threads.FddContractPreservationFormationThread;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_LOAN", name = "放款审核", moduleId = "P2P_C_FINANCE_ZJGL_FKGL", order = 1)
public class Loan extends AbstractFinanceServlet
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
        try
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter("token")))
            {
                getController().prompt(request, response, PromptLevel.WARRING, "请不要重复提交请求！");
                sendRedirect(request, response, getController().getURI(request, FkshList.class));
                return;
            }
            
            TenderConfirmManage tenderConfirmManage = serviceSession.getService(TenderConfirmManage.class);
            // 标ID
            int id = IntegerParser.parse(request.getParameter("id"));
            // 投资记录ID列表, t6250
            BidReturn bidReturn = tenderConfirmManage.confirm(id);
            int[] orderIds = bidReturn.bidOrderIds;
            String experOrderIds = bidReturn.experOrderIds;
            // 投资放款
            if (orderIds != null && orderIds.length > 0)
            {
                int index = 0;
                ResourceProvider resourceProvider = getResourceProvider();
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                FkshManage fkshManage = serviceSession.getService(FkshManage.class);
                Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                Boolean isSaveBidContract =
                    BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT));
                String escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                TenderConfirmExecutor executor = resourceProvider.getResource(TenderConfirmExecutor.class);
                Map<String, String> paraMap = new HashMap<String, String>();
                for (int orderId : orderIds)
                {
                    if (index == orderIds.length - 1)
                    {
                        paraMap.put("experOrderIds", experOrderIds);
                        // 加息券订单Id字符串
                        paraMap.put("couponOrderIds", bidReturn.couponOrderIds);
                    }
                    executor.submit(orderId, paraMap);
                    if (!tg)
                    {
                        executor.confirm(orderId, paraMap);
                    }
                    index++;
                }
                fkshManage.writeLog("操作日志", "标的放款成功");
                
                /*放款成功后，开启一个线程去创建合同保全pdf文件
                if (isSaveBidContract && !"huifu".equals(escrowPrefix))
                {
                    ContractPreservationManage contractPreservationManage =
                        serviceSession.getService(ContractPreservationManage.class);
                    contractPreservationManage.insertT6271(id);
                    Thread thread = new Thread(new ContractPreservationFormationThread(id));
                    thread.setName("生成借款合同线程类!");
                    thread.start();
                }*/
                
                saveAdvanceContract(request, serviceSession, id);
                
            }
            sendRedirect(request, response, getController().getURI(request, FkshList.class));
        }
        catch (Throwable throwable)
        {
            logger.error("标的放款失败", throwable);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, FkshList.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
    /**
     * 放款时候生成合同保全
     * @param request
     * @param configureProvider
     * @param serviceSession
     * @param id
     * @throws Throwable 
     */
    public void saveAdvanceContract(HttpServletRequest request, ServiceSession serviceSession, int id)
        throws Throwable
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成放款合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT)))
        {
            /*放款成功后，开启一个线程去创建合同保全pdf文件*/
            IFddSignatureService contractPreservationManage = serviceSession.getService(IFddSignatureService.class);
            contractPreservationManage.insertT6273(id);
            //调用法大大电子签章
            Thread thread = new Thread(new FddContractPreservationFormationThread(id));
            thread.setName("生成借款合同线程类!");
            thread.start();
            
        }
    }
    
}
