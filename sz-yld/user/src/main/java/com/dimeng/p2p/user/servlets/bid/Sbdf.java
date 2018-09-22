package com.dimeng.p2p.user.servlets.bid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.order.AdvanceExecutor;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.signature.fdd.service.FddContractManage;
import com.dimeng.p2p.user.servlets.fxbyj.Dbywmx;
import com.dimeng.p2p.user.servlets.fxbyj.Dfzq;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 垫付
 * 
 * @author gaoshaolong
 * 
 */
public class Sbdf extends AbstractBidServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3297138790344438598L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            int loanId = IntegerParser.parse(request.getParameter("loanId"));
            int advanceNum = IntegerParser.parse(request.getParameter("advanceNum"));
            Map<String, String> params = new HashMap<String, String>();
            params.put("loanId", String.valueOf(loanId));
            params.put("advanceNum", String.valueOf(advanceNum));
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            AdvanceExecutor advanceExecutor = getResourceProvider().getResource(AdvanceExecutor.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            boolean isYq = bidManage.isYQ(loanId);
            if (!isYq)
            {
                throw new LogicalException("该标的目前没有逾期！");
            }
            
            SubscribeBadClaimManage sbcManage = serviceSession.getService(SubscribeBadClaimManage.class);
            Boolean isBadClaim =
                Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
            //如果开启了不良债权转让功能
            if (isBadClaim)
            {
                //校验标的是否已被不良债权转让购买
                if (!sbcManage.checkBidBadClaim(loanId))
                {
                    throw new LogicalException("标的已被不良债权转让购买,不能进行垫付操作！");
                }
            }
            
            List<Integer> orderIds = bidManage.addOrder(loanId);
            if (orderIds != null)
            {
                for (Integer orderId : orderIds)
                {
                    advanceExecutor.submit(orderId, params);
                    if (!tg)
                    {
                        advanceExecutor.confirm(orderId, params);
                    }
                }
            }
            
            //生成散标垫付合同PDF并保全
            try
            {
                if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT)))
                {
                    //使用法大大电子签章
                    FddContractManage manage = serviceSession.getService(FddContractManage.class);
                    List<T6273> t6273List = manage.getDfList(loanId);
                    if ((null == t6273List) || (t6273List.size() <= 0))
                    {
                        manage.insertT6273DF(loanId);
                    }
                    t6273List = manage.getDfList(loanId);
                    if ((null != t6273List) && (t6273List.size() > 0))
                    {
                        // 合同内容
                        StringBuffer sbContract = new StringBuffer();
                        StringBuffer sbContractSave = new StringBuffer();
                        int userId = serviceSession.getSession().getAccountId();
                        Map<String, Object> valueMap = manage.getAdvanceContentMap(loanId, userId, "");
                        if (null != valueMap)
                        {
                            PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                            String charset = resourceProvider.getCharset();
                            sbContractSave.setLength(0);
                            sbContractSave.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                                .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                                .append(request.getContextPath())
                                .append("/");
                            
                            sbContract.setLength(0);
                            sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                            sbContract.append((String)valueMap.get("dzxy_content"));
                            sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                            /*PreservationExecutor preservationExecutor =
                                resourceProvider.getResource(PreservationExecutor.class);*/
                            for (T6273 t6273 : t6273List)
                            {
                                
                                valueMap.put("xy_no", t6273.F04);
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
                                    t6273.F09 = dfrContractPath;
                                    manage.updateT6273PdfPathNo(t6273);
                                    logger.info("生成垫付pdf合同文档成功！");
                                    //调用第三方合同保全执行器
                                    //preservationExecutor.contractPreservation(dfrT6271.F01);
                                }
                            }
                        }
                    }
                    
                }
            }
            catch (Throwable throwable)
            {
                logger.error("Sbdf.processPost()", throwable);
            }
            bidManage.writeFrontLog(FrontLogType.JGDF.getName(), "机构垫付");
            //getController().prompt(request, response, PromptLevel.INFO, "操作成功");
            sendRedirect(request, response, getController().getURI(request, Dfzq.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, Dbywmx.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
