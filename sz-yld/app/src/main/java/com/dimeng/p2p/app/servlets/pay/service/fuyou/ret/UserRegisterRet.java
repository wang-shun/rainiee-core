/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.ret;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.AbstractFuyouServlet;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.face.UserinfoQuery;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.google.gson.Gson;

*//**
 * 
 * 富友托管注册返回结果
 * <商户接收交易结果通知地址>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class UserRegisterRet extends AbstractFuyouServlet
{
    
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = -4845749452112806807L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("处理注册时返回数据开始——IP:" + request.getRemoteAddr());
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        UserManage manage = serviceSession.getService(UserManage.class);
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("处理注册时返回数据：" + jsonData);
        //分析从对方传回来的数据
        @SuppressWarnings("unchecked")
        Map<String, String> retMap = gs.fromJson(jsonData, Map.class);
        // 因富友个人与企业返回信息为共用
        retMap.remove("artif_nm");
        // 验签
        boolean flag = manage.registerReturnDecoder(retMap);
        
        String url = getSiteDomain(Config.registerRetUrl);
        if (!flag)
        {
            logger.info("注册验签失败-返回码：" + retMap.get("resp_code"));
            throw new LogicalException("注册验签失败-返回码：" + retMap.get("resp_code"));
        }
        if ("0000".equals(retMap.get("resp_code")))
        {
            try
            {
                //对获取的数据进行集中处理
                manage.updateUserInfo(retMap);
                url += "?code=000000&description=success";
            }
            catch (Exception e)
            {
                logger.error(e, e);
            }
        }
        else if ("5343".equals(retMap.get("resp_code")))
        {
            String mobile_no = retMap.get("mobile_no");
            // 5343用户已开户——针对注册异常处理<富友成功，平台第一次失败时，第二次 返回 5343时更新同步用户信息>
            logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no);
            try
            {
                if (manage.selectT6119(mobile_no))
                {
                    logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no + "备注:手机号未被占用-查询用户信息");
                    // 至 富友查询用户信息
                    UserinfoQuery userinfoQuery = new UserinfoQuery();
                    final UserQueryResponseEntity userQuery =
                        userinfoQuery.userinfoQuery(manage.userChargeQuery(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                            mobile_no),
                            serviceSession,
                            getConfigureProvider().format(FuyouVariable.FUYOU_QUERYUSERINFS_URL));
                    if (!"0000".equals(userQuery.getResp_code()))
                    {
                        logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no + "备注：查询信息失败");
                        throw new LogicalException("5343用户已开户-注册异常处理，查询信息失败！");
                    }
                    // 姓名
                    retMap.put("cust_nm", userQuery.getCust_nm());
                    // 身份证
                    retMap.put("certif_id", userQuery.getCertif_id());
                    // 邮箱
                    retMap.put("email", userQuery.getEmail());
                    // 开户行地区代码
                    retMap.put("city_id", userQuery.getCity_id());
                    // 开户行行别
                    retMap.put("parent_bank_id", userQuery.getParent_bank_id());
                    // 开户支行名称
                    retMap.put("bank_nm", userQuery.getBank_nm());
                    // 银行卡账号
                    retMap.put("capAcntNo", userQuery.getCapAcntNo());
                    
                    // 对获取的数据进行集中更新
                    manage.updateUserInfo(retMap);
                    logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no + "备注：处理成功");
                    url += "?code=000000&description=success";
                }
                else
                {
                    logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no + "备注：处理失败，手机号已被占用");
                    url += "?code=000004&description=您使用的手机号已被占用，请更换手机号！";
                }
            }
            catch (Exception e)
            {
                logger.info("5343用户已开户-注册异常处理-手机号：" + mobile_no + "备注：处理失败");
                logger.error(e, e);
                url += "?code=000004&description=开户失败！";
            }
        }
        else
        {
        	String msg = BackCodeInfo.info(retMap.get("resp_code"));
            logger.info(msg);
            url += "?code=000004&description=+"+msg+"！";
        }
        //打印交易唯一标识(这里好像没有)
        logger.info("处理注册时返回数据结束。");
        //重定向到用户中心
        sendRedirect(request, response, url);
    }
}
*/