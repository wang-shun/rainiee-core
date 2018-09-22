/**
 * 
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * @author zhoulantao
 *
 */
public class RegexInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 16062260216377176L;

    /**
     * 是否需要验证码
     */
    private String registerFlage;
    
    /**
     * 用户名校验
     */
    private String newUserNameRegex;
    
    /**
     * 用户名错误提示
     */
    private String userNameRegexContent;
    
    /**
     * 用户密码校验
     */
    private String newPasswordRegex;
    
    /**
     * 用户密码错误提示
     */
    private String passwordRegexContent;
    
    /**
     * 交易密码校验
     */
    private String txPwdRegex;
    
    /**
     * 交易密码错误提示
     */
    private String txPwdContent;
    
    public String getRegisterFlage()
    {
        return registerFlage;
    }
    
    public void setRegisterFlage(String registerFlage)
    {
        this.registerFlage = registerFlage;
    }
    
    public String getNewUserNameRegex()
    {
        return newUserNameRegex;
    }
    
    public void setNewUserNameRegex(String newUserNameRegex)
    {
        this.newUserNameRegex = newUserNameRegex;
    }
    
    public String getUserNameRegexContent()
    {
        return userNameRegexContent;
    }
    
    public void setUserNameRegexContent(String userNameRegexContent)
    {
        this.userNameRegexContent = userNameRegexContent;
    }
    
    public String getNewPasswordRegex()
    {
        return newPasswordRegex;
    }
    
    public void setNewPasswordRegex(String newPasswordRegex)
    {
        this.newPasswordRegex = newPasswordRegex;
    }
    
    public String getPasswordRegexContent()
    {
        return passwordRegexContent;
    }
    
    public void setPasswordRegexContent(String passwordRegexContent)
    {
        this.passwordRegexContent = passwordRegexContent;
    }
    
    public String getTxPwdRegex()
    {
        return txPwdRegex;
    }
    
    public void setTxPwdRegex(String txPwdRegex)
    {
        this.txPwdRegex = txPwdRegex;
    }
    
    public String getTxPwdContent()
    {
        return txPwdContent;
    }
    
    public void setTxPwdContent(String txPwdContent)
    {
        this.txPwdContent = txPwdContent;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "RegexInfo [registerFlage=" + registerFlage + ", newUserNameRegex=" + newUserNameRegex
            + ", userNameRegexContent=" + userNameRegexContent + ", newPasswordRegex=" + newPasswordRegex
            + ", passwordRegexContent=" + passwordRegexContent + ", txPwdRegex=" + txPwdRegex + ", txPwdContent="
            + txPwdContent + "]";
    }
}
