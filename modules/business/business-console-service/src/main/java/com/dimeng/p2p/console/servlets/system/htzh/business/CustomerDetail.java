package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.CustomerEntity;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.query.CustomerQuery;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 
 * 客户详情
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年05月18日]
 */
@Right(id = "P2P_C_SYS_HTZH_YWYGL_KHXQ", name = "客户详情",moduleId="P2P_C_SYS_HTZH_YWYGL",order=2)
public class CustomerDetail extends AbstractBuisnessServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @SuppressWarnings(value="unchecked")
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
        SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
        final com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserM.get(serviceSession.getSession().getAccountId());
        PagingResult<CustomerEntity> result = sysBusinessManage.selectCustomers(new CustomerQuery() {
            @Override
            public String getUserName() {
                return request.getParameter("userName");
            }

            @Override
            public String getRealName() {
                return request.getParameter("realName");
            }

            @Override
            public String getMobile() {
                return request.getParameter("mobile");
            }

            @Override
            public String getIsThird() {
                return request.getParameter("isThird");
            }

            @Override
            public Timestamp getCreateTimeStart() {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }

            @Override
            public Timestamp getCreateTimeEnd() {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }

            /**
             * 业务员工号
             *
             * @return
             */
            @Override
            public String getEmployNum() {
                return Constant.BUSINESS_ROLE_ID == sysU.roleId ? sysU.employNum : request.getParameter("employNum");
            }
        }, new Paging() {
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }

            @Override
            public int getSize() {
                return 10;
            }
        });

        Performance performance = sysBusinessManage.findPerformance(new ResultsQuery(){

            /**
             * 业务员工号.
             *
             * @return {@link String}空值无效.
             */
            @Override
            public String getEmployNum() {
                return Constant.BUSINESS_ROLE_ID == sysU.roleId ? sysU.employNum : request.getParameter("employNum");
            }

            /**
             * 业务员姓名.
             *
             * @return {@link String}空值无效.
             */
            @Override
            public String getName() {
                return null;
            }

            /**
             * 时间,大于等于查询.
             *
             * @return {@link Timestamp}null无效.
             */
            @Override
            public Timestamp getCreateTimeStart() {
                return null;
            }

            /**
             * 时间,小于等于查询.
             *
             * @return {@link Timestamp}null无效.
             */
            @Override
            public Timestamp getCreateTimeEnd() {
                return null;
            }

            /**
             * 所属一级客户用户名.
             *
             * @return {@link String}空值无效.
             */
            @Override
            public String getNamelevel() {
                return null;
            }

            /**
             * 项目ID.
             *
             * @return {@link String}空值无效.
             */
            @Override
            public String getProject() {
                return null;
            }

            /**
             * 用户类型
             *
             * @return {@code String}空值无效.
             */
            @Override
            public String getUserType() {
                return null;
            }

            /**
             * 客户用户名
             *
             * @return
             */
            @Override
            public String getCustomName() {
                return null;
            }

            /**
             * 一级客户姓名
             *
             * @return
             */
            @Override
            public String getUserNameLevel() {
                return null;
            }

            /**
             * 客户姓名
             *
             * @return
             */
            @Override
            public String getCustomRealName() {
                return null;
            }

            /**
             * 交易类型
             *
             * @return
             */
            @Override
            public String getTradeType() {
                return null;
            }
        });
        request.setAttribute("performance", performance);
        request.setAttribute("result", result);
        forwardView(request, response, CustomerDetail.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
}
