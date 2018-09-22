package com.dimeng.p2p.pay.servlets.fuyou;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.ComUpdate;
import com.dimeng.p2p.escrow.fuyou.entity.UserUpdateEntity;
import com.dimeng.p2p.escrow.fuyou.service.UpdateInfoManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

/**
 * 富友用户信息更新，通知Servlet
 * <当用户在富友系统上修改个人信息时>
 * 此类需于富绑定服务器：http://平台域名/pay/fuyou/updateInform.htm(方可生效)。
 * eg: http://61.145.159.156:8315/pay/fuyou/updateInform.htm>
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月4日]
 */
public class UpdateInform extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("用户信息修改通知接口,异步处理Servlet...");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("访问地址：" + request.getRemoteAddr());
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("富友充值成功信息：" + jsonData);
        String noticeMessage = null;
        if (jsonData.length() < 5)
        {
            noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        // 分析从对方传回来的数据
        UpdateInfoManage manage = serviceSession.getService(UpdateInfoManage.class);
        //WithDrawManage withdrawService = serviceSession.getService(WithDrawManage.class);
        String artif_nm = request.getParameter("artif_nm");
        
        UserUpdateEntity userEntity = null;
        ComUpdate legalEntity = null;
        int isPerson = 1;
        //判断是不是法人信息更新
        if (StringHelper.isEmpty(artif_nm))
        {
            //解析实体参数
            userEntity = manage.userUpdateReqDecode(request);
        }
        else
        {
            //解析实体参数
            legalEntity = manage.legalPerUpdateReqDecode(request);
            isPerson = 2;
        }
        // 验签判断
        if (userEntity == null && legalEntity == null)
        {
            response.getWriter().write("验签失败");
            response.getWriter().close();
            return;
        }
        //手机号
        String mobile_no = request.getParameter("mobile_no");
        //银行卡号
        String capAcntNo = request.getParameter("capAcntNo");
        //开户地区
        String city_id = request.getParameter("city_id");
        //行别
        String parent_bank_id = request.getParameter("parent_bank_id");
        //支行名称
        String bank_nm = request.getParameter("bank_nm");
        //法人姓名
        String cust_nm = request.getParameter("cust_nm");
        if (StringHelper.isEmpty(cust_nm))
        {
            cust_nm = "开户名";
        }
        manage.updateInfo(mobile_no, capAcntNo, city_id, parent_bank_id, bank_nm, cust_nm, isPerson);
        if (manage.setCardNumSuccessed(mobile_no, capAcntNo, city_id, parent_bank_id, bank_nm))
        {
            String sendxml = null;
            try
            {
                // 成功响应
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("resp_code", "0000");
                map.put("mchnt_cd", request.getParameter("mchnt_cd"));
                map.put("mchnt_txn_ssn", request.getParameter("mchnt_txn_ssn"));
                sendxml = manage.asynQuery(map);
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
            }
            catch (Exception e)
            {
                logger.error(e);
            }
            finally
            {
                response.getWriter().write(sendxml);
                response.getWriter().close();
            }
        }
        
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
}
