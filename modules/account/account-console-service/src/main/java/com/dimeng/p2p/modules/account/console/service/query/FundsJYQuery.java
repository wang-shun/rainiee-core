package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

public interface FundsJYQuery {
	 public abstract int getType();

	  public abstract Timestamp getStartPayTime();

	  public abstract Timestamp getEndPayTime();
	  public abstract int getId();
}
