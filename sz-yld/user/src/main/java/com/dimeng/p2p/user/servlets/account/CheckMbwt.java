/*
 * 文 件 名:  CheckMbwt.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年5月20日
 */
package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.RSAUtils;

/**
 * 个人基础信息页面修改密保问题校验原密保问题
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年5月20日]
 */
public class CheckMbwt extends AbstractAccountServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        String question1 = request.getParameter("answer1");
        String question2 = request.getParameter("answer2");
        String question3 = request.getParameter("answer3");
        
        String[] q1 = question1.split(":");
        String[] q2 = question2.split(":");
        String[] q3 = question3.split(":");
        if (q1.length < 2 || q2.length < 2 || q3.length < 2)
        {
            out.write("01");
            return;
        }
        String questionId1 = q1[0];
        String answer1 = q1[1];
        String questionId2 = q2[0];
        String answer2 = q2[1];
        String questionId3 = q3[0];
        String answer3 = q3[1];
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        String inputPwdValue1 = RSAUtils.decryptStringByJs(answer1);
        String inputPwdValue2 = RSAUtils.decryptStringByJs(answer2);
        String inputPwdValue3 = RSAUtils.decryptStringByJs(answer3);
        
        inputPwdValue1 = UnixCrypt.crypt(inputPwdValue1, DigestUtils.sha256Hex(inputPwdValue1));
        inputPwdValue2 = UnixCrypt.crypt(inputPwdValue2, DigestUtils.sha256Hex(inputPwdValue2));
        inputPwdValue3 = UnixCrypt.crypt(inputPwdValue3, DigestUtils.sha256Hex(inputPwdValue3));
        
        int accountId = serviceSession.getSession().getAccountId();
        String dbValue1 = safetyManage.getUserAnswerByQuestionId(accountId, Integer.parseInt(questionId1));
        String dbValue2 = safetyManage.getUserAnswerByQuestionId(accountId, Integer.parseInt(questionId2));
        String dbValue3 = safetyManage.getUserAnswerByQuestionId(accountId, Integer.parseInt(questionId3));
        
        if (!inputPwdValue1.equals(dbValue1))
        {
            out.write("02");
            return;
        }
        if (!inputPwdValue2.equals(dbValue2))
        {
            out.write("03");
            return;
        }
        if (!inputPwdValue3.equals(dbValue3))
        {
            out.write("04");
            return;
        }
        //校验成功后，标记校验状态
        serviceSession.getSession().setAttribute(String.valueOf(accountId), "CHECK_SUCCESS");
        out.write("00");
    }
}
