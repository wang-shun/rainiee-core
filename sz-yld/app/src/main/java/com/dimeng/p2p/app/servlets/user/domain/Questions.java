/*
 * 文 件 名:  Questions.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月22日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 风险评估问题
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class Questions implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2734883039284010296L;

    /**
     * 问题ID
     */
    private int questionId;
    
    /**
     * 问题描述
     */
    private String question;
    
    /**
     * 问题答案A
     */
    private String answerA;
    
    /**
     * 问题答案B
     */
    private String answerB;
    
    /**
     * 问题答案C
     */
    private String answerC;
    
    /**
     * 问题答案D
     */
    private String answerD;
    
    public int getQuestionId()
    {
        return questionId;
    }
    
    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }
    
    public String getQuestion()
    {
        return question;
    }
    
    public void setQuestion(String question)
    {
        this.question = question;
    }
    
    public String getAnswerA()
    {
        return answerA;
    }
    
    public void setAnswerA(String answerA)
    {
        this.answerA = answerA;
    }
    
    public String getAnswerB()
    {
        return answerB;
    }
    
    public void setAnswerB(String answerB)
    {
        this.answerB = answerB;
    }
    
    public String getAnswerC()
    {
        return answerC;
    }
    
    public void setAnswerC(String answerC)
    {
        this.answerC = answerC;
    }
    
    public String getAnswerD()
    {
        return answerD;
    }
    
    public void setAnswerD(String answerD)
    {
        this.answerD = answerD;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "Questions [questionId=" + questionId + ", question=" + question + ", answerA=" + answerA + ", answerB="
            + answerB + ", answerC=" + answerC + ", answerD=" + answerD + "]";
    }
}
