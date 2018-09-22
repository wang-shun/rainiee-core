package com.dimeng.p2p.escrow.fuyou.entity.console;

import java.io.Serializable;

public class UserAcctQueryEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    public double sonAccountsBalance;
    
    public double accountsBalance;
    
    public double sonBlockedBalances;
    
    public String platformId;
}
