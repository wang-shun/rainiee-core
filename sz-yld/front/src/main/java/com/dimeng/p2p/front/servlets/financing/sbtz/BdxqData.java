package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.account.front.service.EnterpriseInfoManage;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.front.service.entity.Enterprise;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.DistrictManage;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.util.StringHelper;

public class BdxqData extends AbstractFinancingServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        String type = request.getParameter("type");
        int bid = Integer.parseInt(request.getParameter("id"));
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        BidManage investManage = serviceSession.getService(BidManage.class);
        if ("bdxq".equals(type))
        {
            getBdxq(serviceSession, bid, jsonMap, investManage);
        }
        else if ("fkxx".equals(type))
        {
            getFkxx(bid, jsonMap, investManage);
        }
        else if ("dywxx".equals(type))
        {
            getDywxx(bid, jsonMap, investManage);
        }
        else if ("xgwj".equals(type))
        {
            getXgwj(serviceSession, bid, jsonMap, investManage);
        }
        else if ("hkjh".equals(type))
        {
            getHkjh(request, bid, jsonMap, investManage);
        }
        else if ("tzjl".equals(type))
        {
            getTzjl(request, serviceSession, bid, jsonMap, investManage);
        }
        else if ("byjdfwb".equals(type))
        {
            getByjdfwb(serviceSession, jsonMap);
        }
        else if ("xmdt".equals(type))
        {
            getXmdt(bid, jsonMap, investManage);
        }
        
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    /**
     * 风控信息
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getFkxx(int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        ReturnBdxqData returnData = new ReturnBdxqData();
        T6237 fkInfos = investManage.getFk(bid);
        Dbxx dbInfos = investManage.getDB(bid);
        returnData.t6237 = fkInfos;
        returnData.dbxx = dbInfos;
        
        jsonMap.put("fkxxData", returnData);
    }
    
    /**
     * 抵押物信息
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getDywxx(int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        ReturnBdxqData returnData = new ReturnBdxqData();
        Bdylx dyxxs = investManage.getDylb(bid);
        
        returnData.dyxxs = dyxxs;
        jsonMap.put("dywxxData", returnData);
    }
    
    /**
     * 相关文件
     * @param serviceSession
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getXgwj(ServiceSession serviceSession, int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        ReturnBdxqData returnData = new ReturnBdxqData();
        T6250[] tenderRecords = investManage.getRecord(bid);
        T6232[] t6232s = null;
        T6233[] t6233s = null;
        /* Bdxq creditInfo = investManage.get(bid);
         if (creditInfo.F20 == T6230_F20.TBZ || creditInfo.F20 == T6230_F20.YFB)
         {
             t6232s = investManage.getFjgks(bid);
         }*/
        T6230 t6230 = investManage.queryT6230(bid);
        if (null != serviceSession.getSession() && serviceSession.getSession().isAuthenticated())
        {//登录状态下，优先显示非公开的附件，如果没有非公开的附件，才显示公开的附件
            int loginId = serviceSession.getSession().getAccountId();
            if (tenderRecords != null && tenderRecords.length > 0)
            {//有交易记录时，借款人和投资人都可以查看非公开附件
                for (T6250 tenderRecord : tenderRecords)
                {
                    if (tenderRecord.F03 == loginId || t6230.F02 == loginId)
                    {
                        t6233s = investManage.getFjfgks(bid);
                        break;
                    }
                }
            }
            else
            {//没有交易记录时，只有借款人能查看非公开的附件
                if (t6230.F02 == loginId)
                {
                    t6233s = investManage.getFjfgks(bid);
                }
            }
            //当没有非公开的附件时，查询显示公开的附件
            if (t6233s == null || t6233s.length == 0)
            {
                t6232s = investManage.getFjgks(bid);
            }
        }
        else
        {//未登录时，只显示公开的附件
            t6232s = investManage.getFjgks(bid);
        }
        
        T6212[] t6212s = null;
        if (t6233s == null || t6233s.length == 0)
        {
            t6212s = investManage.getT6212(bid, false);
        }
        else
        {
            t6212s = investManage.getT6212(bid, true);
        }
        returnData.t6232s = t6232s;
        returnData.t6233s = t6233s;
        returnData.t6212s = t6212s;
        jsonMap.put("xgwjData", returnData);
    }
    
    /**
     * 还款计划
     * @param request
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getHkjh(final HttpServletRequest request, int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        PagingResult<Hkjllb> result = investManage.getHks(bid, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.parseInt(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return Integer.parseInt(request.getParameter("currentPage"));
            }
        });
        jsonMap.put("pageStr", rendPaging(result));
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("hkjhList", result.getItems());
    }
    
    /**
     * 标的详情
     * @param serviceSession
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getBdxq(ServiceSession serviceSession, int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        ReturnBdxqData returnData = new ReturnBdxqData();
        Bdxq creditInfo = investManage.get(bid);
        if (creditInfo.F20 == T6230_F20.YJQ || creditInfo.F20 == T6230_F20.HKZ || creditInfo.F20 == T6230_F20.YDF
            || creditInfo.F20 == T6230_F20.YZR)
        {
            creditInfo.F05 = creditInfo.F05.subtract(creditInfo.F07);
        }
        returnData.bdxq = creditInfo;
        T6231 t6231 = investManage.getExtra(bid);
        returnData.t6231 = t6231;
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoManage.getUserInfo(creditInfo.F02);
        returnData.creditLevel = userInfoManage.getXyLevel(creditInfo.F02);
        String qyName = "";
        if (t6110.F06 == T6110_F06.ZRR)
        {
            if (t6110.F02 != null && t6110.F02.length() > 4)
            {
                qyName = t6110.F02.substring(0, 4) + "***";
            }
            else
            {
                qyName = t6110.F02;
            }
        }
        else
        {
            EnterpriseInfoManage enterpriseInfoManage = serviceSession.getService(EnterpriseInfoManage.class);
            Enterprise enterprise = enterpriseInfoManage.get(creditInfo.F02);
            if (enterprise != null)
            {
                if (enterprise.F04 != null && enterprise.F04.length() > 4)
                {
                    qyName = "***" + enterprise.F04.substring(enterprise.F04.length() - 4, enterprise.F04.length());
                }
                else
                {
                    qyName = enterprise.F04;
                }
            }
            returnData.enterprise = enterprise;
            returnData.t6163s = enterpriseInfoManage.search(creditInfo.F02);
        }
        returnData.qyName = qyName;
        DistrictManage districtManage = serviceSession.getService(DistrictManage.class);
        T5019 t5019 = districtManage.getShengName(t6231.F07);
        returnData.projectArea = t5019.F06 + " " + t5019.F07 + " " + t5019.F08;
        getByjdfwb(serviceSession, jsonMap);
        returnData.existByjdf = StringHelper.isEmpty((String)jsonMap.get("byjdfwb")) ? "false" : "true";
        jsonMap.put("bdxqData", returnData);
    }
    
    /**
     * 投资记录
     * @param request
     * @param serviceSession
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getTzjl(final HttpServletRequest request, ServiceSession serviceSession, int bid,
        Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        PagingResult<T6250> result = investManage.getRecords(bid, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.parseInt(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return Integer.parseInt(request.getParameter("currentPage"));
            }
            
        });
        if (result.getItems() != null)
        {
            for (T6250 t6250 : result.getItems())
            {
                String userTbr = userInfoManage.getUserName(t6250.F03);
                t6250.F10 =
                    userTbr.substring(0, 2) + "******" + userTbr.substring(userTbr.length() - 2, userTbr.length());
            }
        }
        jsonMap.put("pageStr", rendPaging(result));
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("totalCount", investManage.getTbCount(bid));
        jsonMap.put("tzjlList", result.getItems());
        jsonMap.put("totalMoney", investManage.getTzMoney(bid));
    }
    
    /**
     * 备用金垫付文本
     * @param serviceSession
     * @param jsonMap
     * @throws Throwable
     */
    private void getByjdfwb(ServiceSession serviceSession, Map<String, Object> jsonMap)
        throws Throwable
    {
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
        T5011 article = articleManage.get(T5011_F02.BYJDFWB);
        String countent = "";
        if (article != null)
        {
            articleManage.view(article.F01);
            countent = articleManage.getContent(article.F01);
        }
        jsonMap.put("byjdfwb", countent);
    }
    
    /**
     * 项目动态
     * @param request
     * @param serviceSession
     * @param bid
     * @param jsonMap
     * @param investManage
     * @throws Throwable
     */
    private void getXmdt(int bid, Map<String, Object> jsonMap, BidManage investManage)
        throws Throwable
    {
        
        PagingResult<T6248> t6248List = investManage.viewBidProgresList(bid, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 50;
            }
            
            @Override
            public int getCurrentPage()
            {
                return 1;
            }
        });
        jsonMap.put("t6248List", t6248List.getItems());
    }
}
