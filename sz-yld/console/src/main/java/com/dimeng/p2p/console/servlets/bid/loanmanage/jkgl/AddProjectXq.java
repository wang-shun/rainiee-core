package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6231_F36;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.EnterpriseManage;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.account.console.service.entity.AT6161;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_BID_ADDPROJECT", name = "新增", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 1)
public class AddProjectXq extends AbstractBidServlet
{
    
    /**
     *
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        String productIdStr = request.getParameter("productId");
        BidManage bidManage = serviceSession.getService(BidManage.class);
        ProductManage productManage = serviceSession.getService(ProductManage.class);
        int userId = IntegerParser.parse(request.getParameter("userId"));
        userId = userId > 0 ? userId : IntegerParser.parse(request.getAttribute("userId"));
        UserManage userManage = serviceSession.getService(UserManage.class);
        Paging paging = new Paging()
        {
            @Override
            public int getSize()
            {
                return 100;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        PagingResult<T6216> result = productManage.search(new ProductQuery()
        {
            @Override
            public T6216_F04 getStatus()
            {
                return T6216_F04.QY;
            }
            
            @Override
            public String getProductName()
            {
                return "";
            }
            
            @Override
            public String getBidType()
            {
                
                return "";
            }
            
            @Override
            public String getRepaymentWay()
            {
                return "";
            }
        }, paging);
        
        T6216 model = null; ////产品信息
        if (StringHelper.isEmpty(productIdStr))
        {
            model = result.getItems() != null ? result.getItems()[0] : null;
        }
        else
        {
            model = productManage.get(Integer.parseInt(productIdStr));
        }
        /*int loanId = IntegerParser.parse(request.getParameter("loanId"));
        if(loanId>0){
        	model.F15 = new BigDecimal(request.getParameter("zxtze"));       //最小投资金额
        	model.F12 =	new BigDecimal(request.getParameter("cjfwfl"));	    //成交服务费率
        	model.F13 = new BigDecimal(request.getParameter("tzglfl"));			//投资管理费率
        	model.F14 =	new BigDecimal(request.getParameter("yqflfl"));		//逾期罚息利率
        }*/
        
        String userName = userManage.getUserNameById(userId);
        request.setAttribute("product", model);
        T6211[] t6211s = bidManage.getBidType();
        request.setAttribute("t6211s", t6211s);
        request.setAttribute("userName", userName);
        request.setAttribute("productResult", result);
        forwardView(request, response, getClass());
        
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            //产品下拉列表改变事件：选择产品，重新带出产品规则
            String submitType = request.getParameter("submitType");
            if ("processGet".equalsIgnoreCase(submitType))
            {
                processGet(request, response, serviceSession);
                return;
            }
            
            String productId = request.getParameter("productId");
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
            {
                throw new LogicalException("请不要重复提交请求！");
            }
            BidManage bidManage = serviceSession.getService(BidManage.class);
            GrManage personalManage = serviceSession.getService(GrManage.class);
            EnterpriseManage enterpriseManage = serviceSession.getService(EnterpriseManage.class);
            UserManage userManage = serviceSession.getService(UserManage.class);
            int loanId = IntegerParser.parse(request.getParameter("loanId"));
            int userId = IntegerParser.parse(request.getParameter("userId"));
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            T6230 t6230 = new T6230();
            if (userId <= 0)
            {
                T6110 t6110 = userManage.getFrontUserByName(request.getParameter("name"));
                if (null == t6110)
                {
                    throw new ParameterException("该用户名不存在，请重新输入。");
                }
                if (T6110_F07.SD.name().equals(t6110.F07.name()))
                {
                    throw new ParameterException("该用户名被锁定，请重新输入。");
                }
                if (T6110_F07.HMD.name().equals(t6110.F07.name()))
                {
                    throw new ParameterException("该用户名被拉黑，请重新输入。");
                }
                userId = t6110.F01;
            }
            String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            if (tg && !userManage.isTg(userId))
            {
                throw new ParameterException("该用户没有注册第三方托管账号。");
            }
            if ("shuangqian".equals(escrow))
            {//如为双乾托管则需校验借款人是否授权
                T6119 t6119 = userManage.selectT6119(userId);
                if (StringHelper.isEmpty(t6119.F04))
                {
                    throw new ParameterException("该用户没有授权二次分配，不能发标借款。");
                }
            }
            
            T6110_F06 userType = userManage.getUserType(userId);
            T6110_F10 t6110_F10 = userManage.getDb(userId);
            if (userType == T6110_F06.ZRR)
            {
                Grxx user = personalManage.getUser(userId);
                boolean email = false;
                boolean jymm = false;
                if (configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL).equals("true"))
                {
                    email = StringHelper.isEmpty(user.email);
                }
                if (configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD).equals("true"))
                {
                    jymm = StringHelper.isEmpty(user.jymm);
                }
                if (user == null || StringHelper.isEmpty(user.name) || StringHelper.isEmpty(user.userName)
                    || StringHelper.isEmpty(user.phone) || email || StringHelper.isEmpty(user.sfzh) || jymm)
                {
                    throw new LogicalException("该用户的个人资料未填写完整,不能进行发标操作。");
                }
            }
            else if (userType == T6110_F06.FZRR)
            {
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S)
                {
                    throw new LogicalException("机构不允许借款。");
                }
                else
                {
                    AT6161 t6161 = enterpriseManage.getEnterprise(userId);
                    if (t6161 == null || StringHelper.isEmpty(t6161.F02)
                        || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F03))
                        || StringHelper.isEmpty(t6161.F04)
                        || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F05))
                        || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F06))
                        || ("Y".equals(t6161.F20) && StringHelper.isEmpty(t6161.F19))
                        || StringHelper.isEmpty(t6161.F13))
                    {
                        throw new LogicalException("该企业的必要基础信息未填写完整,不能进行发标操作。");
                    }
                    /*if (BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD)) && !tg
                        && StringHelper.isEmpty(t6161.F18))
                    {
                        throw new LogicalException("该企业的交易密码没有设置,不能进行发标操作。");
                    }*/
                }
            }
            Rzxx[] t6120s = personalManage.getRzxx(userId);
            StringBuffer rzMsg = new StringBuffer("");
            if (t6120s != null && t6120s.length > 0)
            {
                for (Rzxx rzxx : t6120s)
                {
                    if (T5123_F03.S.name().equals(rzxx.mustRz.name()) && !T6120_F05.TG.equals(rzxx.status))
                    {
                        rzMsg.append("," + rzxx.lxmc);
                    }
                }
            }
            
            if (!StringHelper.isEmpty(rzMsg.toString()))
            {
                throw new LogicalException(rzMsg.substring(1).toString() + "认证项没有通过,不能进行发标操作。");
            }
            t6230.parse(request);
            
            t6230.F32 = IntegerParser.parse(productId);
            t6230.F02 = userId;
            t6230.F01 = loanId;
            T6231 t6231 = new T6231();
            t6231.F07 = IntegerParser.parse(request.getParameter("xian"));
            t6231.F08 = request.getParameter("jkyt");
            t6231.F09 = request.getParameter("jkms");
            t6231.F16 = request.getParameter("hkly");
            t6231.F25 = BigDecimalParser.parse(request.getParameter("zxtze"));
            t6231.F26 = BigDecimalParser.parse(request.getParameter("zdtze"));
            t6231.F28 = BigDecimalParser.parse(request.getParameter("jlll"));
            T6231_F33 f33 = T6231_F33.PC_APP;
            switch (request.getParameter("terminal"))
            {
                case "PC":
                    f33 = T6231_F33.PC;
                    break;
                case "APP":
                    f33 = T6231_F33.APP;
                    break;
            }
            t6231.F33 = f33;
            t6231.F36 = T6231_F36.NONE;
            /* t6231.F36 =
                 StringHelper.isEmpty(request.getParameter("rewardUsedType")) ? T6231_F36.ALL
                     : T6231_F36.parse(request.getParameter("rewardUsedType"));*/
            T6238 t6238 = new T6238();
            t6238.F02 = BigDecimalParser.parse(request.getParameter("cjfwfl"));
            t6238.F03 = BigDecimalParser.parse(request.getParameter("tzglfl"));
            t6238.F04 = BigDecimalParser.parse(request.getParameter("yqflfl"));
            String bidFlag = request.getParameter("bidFlag"); //是否为奖励标或新手标
            if ("xsb".equals(bidFlag))
            {
                t6230.xsb = T6230_F28.S;
            }
            else if ("jlb".equals(bidFlag))
            {
                t6231.F27 = T6231_F27.S;
            }
            else
            {
                t6231.F36 = T6231_F36.SINGLE;
            }
            
            //借款金额不能小于起投金额
            if (t6230.F05.compareTo(t6231.F26) == -1)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "最大投资金额不能大于借款金额。");
                processGet(request, response, serviceSession);
                return;
            }
            
            if (t6231.F26.compareTo(t6231.F25) == -1)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "最小投资金额不能大于最大投资金额。");
                processGet(request, response, serviceSession);
                return;
            }
            
            // 本息到期一次付清
            String accDay = request.getParameter("accDay");
            if (T6230_F10.YCFQ == t6230.F10 && "S".equals(accDay))
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                long curDateInMills = cal.getTimeInMillis();
                cal.add(Calendar.MONTH, 36);
                long upDateInMills = cal.getTimeInMillis();
                int upDays = (int)((upDateInMills - curDateInMills) / DateHelper.DAY_IN_MILLISECONDS);
                int lowDays = IntegerParser.parse(configureProvider.getProperty(SystemVariable.JK_BXDQ_LEAST_DAYS), 1);
                if (t6230.F09 < lowDays || t6230.F09 > upDays)
                {
                    getController().prompt(request,
                        response,
                        PromptLevel.WARRING,
                        "本息到期，一次付清(按天计)，借款期限超出有效天数[" + lowDays + "," + upDays + "]。");
                    processGet(request, response, serviceSession);
                    return;
                }
                t6231.F21 = T6231_F21.S;
                t6231.F22 = t6230.F09;
                t6230.F09 = 0;
            }
            else
            {
                if (t6230.F09 < 1 || t6230.F09 > 36)
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "借款期限超出有效月数[1-36]。");
                    processGet(request, response, serviceSession);
                    return;
                }
                t6231.F21 = T6231_F21.F;
                t6231.F22 = 0;
            }
            
            //标的图片
            Part part = request.getPart("image");
            UploadFile uploadFile = null;
            if (!(part == null || part.getContentType() == null || part.getSize() == 0))
            {
                String content = part.getHeaders("content-disposition").toString();
                String fileType = content.substring(content.lastIndexOf(".") + 1, content.length() - 2);
                if (!("jpg".equalsIgnoreCase(fileType) || "png".equalsIgnoreCase(fileType)))
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "图片格式不正确，仅支持jpg、png格式。");
                    processGet(request, response, serviceSession);
                    return;
                }
                if (part.getSize() >= 1024 * 1024)
                {
                    getController().prompt(request, response, PromptLevel.WARRING, "图片大小不正确，不能大于1M。");
                    processGet(request, response, serviceSession);
                    return;
                }
                
                uploadFile = new PartFile(part);
            }
            
            int id = 0;
            if (loanId > 0)
            {
                bidManage.update(t6230, t6231, t6238, uploadFile);
            }
            else
            {
                id = bidManage.add(t6230, t6231, t6238, uploadFile);
            }
            loanId = loanId == 0 ? id : loanId;
            request.setAttribute("loanId", loanId);
            request.setAttribute("F25", t6230.F25);
            request.setAttribute("userId", userId);
            request.setAttribute("bidFlag", bidFlag);
            int flag = IntegerParser.parse(request.getParameter("flag"));
            if (flag == 1 && userType == T6110_F06.ZRR)
            {
                // 跳转到个人信息页面
                sendRedirect(request, response, getController().getURI(request, AddUserInfoXq.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
            else if (flag == 1 && userType == T6110_F06.FZRR)
            {
                sendRedirect(request, response, getController().getURI(request, AddEnterpriseXm.class) + "?loanId="
                    + loanId + "&userId=" + userId);
            }
            else
            {
                getController().prompt(request, response, PromptLevel.INFO, "保存成功");
                processGet(request, response, serviceSession);
            }
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                //super.onThrowable(request, response, throwable);
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
        }
    }
}
