package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标已用还款垫付流水号记录
 * @author raoyujun
 *
 */
public class T6252_EXT extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	private int F01;    
    private int F02;    
    private String F03;        
    private String F04;
	public int getF01() {
		return F01;
	}
	public void setF01(int f01) {
		F01 = f01;
	}
	public int getF02() {
		return F02;
	}
	public void setF02(int f02) {
		F02 = f02;
	}
	public String getF03() {
		return F03;
	}
	public void setF03(String f03) {
		F03 = f03;
	}
	public String getF04() {
		return F04;
	}
	public void setF04(String f04) {
		F04 = f04;
	}
    
}
