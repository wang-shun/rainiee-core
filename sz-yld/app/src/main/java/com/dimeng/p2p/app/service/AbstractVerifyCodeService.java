/*
 * 文 件 名:  AbstractAppServices.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月30日
 */
package com.dimeng.p2p.app.service;


import javax.servlet.http.Cookie;

import com.dimeng.framework.http.session.VerifyCodeGenerator;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.service.Service;

/**
 * 关于验证码的操作新增的抽象接口
 * 
 * @author  suwei
 * @version  [版本号, 2016年3月30日]
 */
public abstract interface AbstractVerifyCodeService extends Service
{
    /**
     * 获取验证码的方法
     * <功能详细描述>
     * @param type
     * @param generator
     * @param cookie
     * @return 验证码
     */
    String getVerifyCode(String type, VerifyCodeGenerator generator, String cookie) throws Throwable;
    
    /**
     * 校验验证码的方法
     * <功能详细描述>
     * @param authentication
     * @param cookie
     */
    void authenticateVerifyCode(VerifyCodeAuthentication authentication, String cookie) throws Throwable;
    
    /**
     * 获取用户cookie
     * <功能详细描述>
     * @param cookies
     * @return cookie
     */
    String getCookie(Cookie[] cookies) throws Throwable;
}
