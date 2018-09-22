package com.dimeng.p2p.pay.servlets.fuyou;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.service.PublicManage;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 机构垫付
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月12日]
 */
public class FyouSbdf extends AbstractFuyouServlet
{
    
    /**
     * 散标垫付
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        logger.info("机构垫付——IP:" + request.getRemoteAddr());
        BidManage bidManage = serviceSession.getService(BidManage.class);
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int advanceNum = IntegerParser.parse(request.getParameter("advanceNum"));
        try
        {
            PublicManage publicManage = serviceSession.getService(PublicManage.class);
            List<Integer> orderIds = bidManage.addOrder(loanId);
            Map<String, String> params = new HashMap<String, String>();
            if (orderIds != null)
            {
                params.put("hint", "success");
                FYAdvanceExecutor advanceExecutor = getResourceProvider().getResource(FYAdvanceExecutor.class);
                params.put("loanId", String.valueOf(loanId));
                params.put("advanceNum", String.valueOf(advanceNum));
                Integer[] orderIdI = orderIds.toArray(new Integer[orderIds.size()]);
                int[] orderIdc = ArrayUtils.toPrimitive(orderIdI);
                // 插入流水
                publicManage.updtateT6501F10(orderIdc, FuyouTypeEnum.JGDF.name());
                logger.info("标ID：" + loanId + "-垫付总数：" + orderIds.size());
                int i = 0;
                for (Integer orderId : orderIds)
                {
                    i++;
                    // 查询是否已存订单
                    publicManage.searchT6501(serviceSession, orderId, params, false);
                    // 重复<初次还款失败后，再次还款>
                    switch (params.get("state"))
                    {
                        case "DTJ":
                            advanceExecutor.submit(orderId, params);
                            if (!"true".equals(params.get("success")))
                            {
                                logger.info("第" + i + "条垫付-失败");
                                params.put("hint", "fail");
                                // 失败时将垫付记录改回未还
                                publicManage.updateT6252(loanId, advanceNum);
                                break;
                            }
                            logger.info("第" + i + "条垫付-成功");
                            advanceExecutor.confirm(orderId, params);
                            break;
                        case "DQR":
                            // 第三方成功，平台未更新
                            advanceExecutor.confirm(orderId, params);
                            logger.info("第" + i + "条垫付-确认成功");
                            break;
                        case "CG":
                            logger.info("第" + i + "条垫付-已确认成功");
                            break;
                        default:
                            advanceExecutor.confirm(orderId, params);
                            logger.info("第" + i + "条垫付-确认成功");
                            break;
                    }
                }
                logger.info("标ID：" + loanId + "-机构垫付-调用结束");
            }
            bidManage.writeFrontLog(FrontLogType.JGDF.getName(), "机构垫付-标ID：" + loanId);
            if ("fail".equals(params.get("hint")))
            {
                getController().prompt(request, response, PromptLevel.ERROR, "操作失败！");
                bidManage.writeFrontLog(FrontLogType.TQHK.getName(), "机构垫付失败-标Id:" + loanId);
                publicManage.updateT6252(loanId, advanceNum);
                sendRedirect(request, response, "/user/fxbyj/dbywmx.htm");
                return;
            }
            //            else
            //            {
            //                getController().prompt(request, response, PromptLevel.INFO, "操作成功!");
            
            //            }
            //生成债权转让合同PDF并保全
            if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT)))
            {
                
                ContractManage manage = serviceSession.getService(ContractManage.class);
                List<T6271> t6271List = manage.getDfList(loanId);
                if ((null != t6271List) && (t6271List.size() > 0))
                {
                    StringBuffer sbContract = new StringBuffer();
                    StringBuffer sbContractSave = new StringBuffer();
                    int userId = serviceSession.getSession().getAccountId();
                    ContractManage contractMng = serviceSession.getService(ContractManage.class);
                    Map<String, Object> valueMap = contractMng.getAdvanceContentMap(loanId, userId);
                    if (null != valueMap)
                    {
                        sbContract.setLength(0);
                        sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                        sbContract.append((String)valueMap.get("dzxy_content"));
                        sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                        PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                        String charset = resourceProvider.getCharset();
                        sbContractSave.setLength(0);
                        sbContractSave.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                            .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                            .append(request.getContextPath())
                            .append("/");
                        PreservationExecutor preservationExecutor =
                            resourceProvider.getResource(PreservationExecutor.class);
                        for (T6271 t6271 : t6271List)
                        {
                            String dfrPath =
                                cpfe.createHTML(valueMap,
                                    "contract",
                                    (String)valueMap.get("dzxy_xymc"),
                                    sbContract.toString(),
                                    charset,
                                    (String)valueMap.get("xy_no"));
                            if (!StringHelper.isEmpty(dfrPath))
                            {
                                String dfrContractPath =
                                    cpfe.convertHtml2Pdf(dfrPath, sbContractSave.toString(), charset);
                                T6271 dfrT6271 = new T6271();
                                dfrT6271.F01 = t6271.F01;
                                dfrT6271.F04 = (String)valueMap.get("xy_no");
                                dfrT6271.F09 = dfrContractPath;
                                contractMng.updateT6271PdfPathNo(dfrT6271);
                                logger.info("生成垫付pdf合同文档成功！");/*调用第三方合同保全执行器*/
                                preservationExecutor.contractPreservation(dfrT6271.F01);
                            }
                        }
                    }
                }
            }
            getController().prompt(request, response, PromptLevel.INFO, "操作成功!");
            bidManage.writeFrontLog(FrontLogType.TQHK.getName(), "机构垫会成功-标Id:" + loanId);
            sendRedirect(request, response, "/user/fxbyj/dfzq.htm");
            
        }
        catch (Throwable throwable)
        {
            logger.error("垫付异常~~~", throwable);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, "/user/fxbyj/dbywmx.htm");
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
