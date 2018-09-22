package com.dimeng.p2p.console.servlets.fuyou;

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
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderConfirmExecutor;
import com.dimeng.p2p.escrow.fuyou.service.PublicManage;
import com.dimeng.p2p.escrow.fuyou.threads.ContractPreservationFormationThread;
import com.dimeng.p2p.modules.bid.console.service.TenderConfirmManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;
import com.dimeng.p2p.repeater.preservation.ContractPreservationManage;
import com.dimeng.p2p.signature.fdd.service.IFddSignatureService;
import com.dimeng.p2p.signature.fdd.threads.FddContractPreservationFormationThread;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 放款
 * <补充优化——前期，业务处理方式，先处理【查询富友】，现上上上 先处理平台，后执行富友，保证两方数据一致！>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月17日]
 */
@Right(id = "P2P_C_FINANCE_LOAN", name = "放款审核", moduleId = "P2P_C_FINANCE_ZJGL_FKGL", order = 1)
public class FyouLoan extends AbstractFuyouServlet
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
                sendRedirect(request, response, "/console/finance/zjgl/fksh/fkshList.htm");
                return;
            }
            // 标ID
            int id = IntegerParser.parse(request.getParameter("id"));
            BidReturn bidReturn;
            // 查询放款单是否已存在
            PublicManage publicManage = serviceSession.getService(PublicManage.class);
            bidReturn = publicManage.selectLoan(id);
            int[] orderIds = null;
            if (bidReturn == null)
            {
                // 生成放款单
                TenderConfirmManage tenderConfirmManage = serviceSession.getService(TenderConfirmManage.class);
                bidReturn = tenderConfirmManage.confirm(id);
                orderIds = bidReturn.bidOrderIds;
            }
            else
            {
                orderIds = bidReturn.bidOrderIds;
            }
            String experOrderIds = bidReturn.experOrderIds;
            // 投资放款
            if (orderIds != null && orderIds.length > 0)
            {
                // 插入流水号
                publicManage.updtateT6501F10(orderIds, FuyouTypeEnum.SDFK.name());
                int index = 0;
                ResourceProvider resourceProvider = getResourceProvider();
                FYTenderConfirmExecutor executor = resourceProvider.getResource(FYTenderConfirmExecutor.class);
                logger.info("标ID：" + id + "-满标放款，总条数：" + orderIds.length);
                Map<String, String> params = new HashMap<String, String>();
                for (int orderId : orderIds)
                {
                    if (index == orderIds.length - 1)
                    {
                        params.put("experOrderIds", experOrderIds);
                        // 加息券订单Id字符串
                        params.put("couponOrderIds", bidReturn.couponOrderIds);
                    }
                    publicManage.searchT6501(serviceSession, orderId, params, false);
                    switch (params.get("state"))
                    {
                        case "DTJ":
                            executor.submit(orderId, params);
                            executor.confirm(orderId, params);
                            if (!Boolean.parseBoolean(params.get("success")))
                            {
                                // 失败时将垫付记录改回未还
                                publicManage.updateT6250(T6250_F08.F, orderId);
                                logger.info("第" + index + "条满标放款-失败");
                            }
                            else
                            {
                                logger.info("第" + index + "条满标放款-成功");
                            }
                            break;
                        case "CG":
                            logger.info("第" + index + "条满标放款-已确认成功");
                            break;
                    }
                    index++;
                    
                }
                saveAdvanceContract(request, serviceSession, id);
            }
            sendRedirect(request, response, "/console/finance/zjgl/fksh/fkshList.htm");
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, "/console/finance/zjgl/fksh/fkshList.htm");
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
    {/*
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成放款合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT)))
        {
            ContractPreservationManage contractPreservationManage =
                serviceSession.getService(ContractPreservationManage.class);
            contractPreservationManage.insertT6271(id);
            new Thread(new ContractPreservationFormationThread(id)).start();
        }
    */

        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成放款合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_BID_CONTRACT)))
        {
           /* ContractPreservationManage contractPreservationManage =
                serviceSession.getService(ContractPreservationManage.class);
            contractPreservationManage.insertT6271(id);
            new Thread(new ContractPreservationFormationThread(id)).start();*/
            
            /*放款成功后，开启一个线程去创建合同保全pdf文件*/
                IFddSignatureService contractPreservationManage =
                    serviceSession.getService(IFddSignatureService.class);
                contractPreservationManage.insertT6273(id);
                //调用法大大电子签章
                Thread thread = new Thread(new FddContractPreservationFormationThread(id));
                thread.setName("生成借款合同线程类!");
                thread.start();
                
        }
    	
    
    }
}
