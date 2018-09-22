package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

import com.dimeng.p2p.app.servlets.ReturnCode;

/**
 * 
 * 
 * @author  
 * @version  [版本号, 2016年6月03日]
 */
public class LetterReturnCode extends ReturnCode implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7015648423831075635L;
    
    private int unRead;
    
    private int count;
    
    public int getUnRead()
    {
        return unRead;
    }
    
    public void setUnRead(int unRead)
    {
        this.unRead = unRead;
    }
    
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "LetterReturnCode [unRead=" + unRead + ", count=" + count + "]";
    }
    
    
}
