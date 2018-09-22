package com.dimeng.p2p.pay.service.fuyou.entity;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * 代付响应详细信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "plain")
public class Plain {
	
	private String order_pay_code;
	private String order_pay_error;
	private String order_id;
	private String order_st;
	private String fy_ssn;
	private String resv1;
	
	public String getOrder_pay_code() {
		return order_pay_code;
	}
	public void setOrder_pay_code(String order_pay_code) {
		this.order_pay_code = order_pay_code;
	}
	public String getOrder_pay_error() {
		return order_pay_error;
	}
	public void setOrder_pay_error(String order_pay_error) {
		this.order_pay_error = order_pay_error;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_st() {
		return order_st;
	}
	public void setOrder_st(String order_st) {
		this.order_st = order_st;
	}
	public String getFy_ssn() {
		return fy_ssn;
	}
	public void setFy_ssn(String fy_ssn) {
		this.fy_ssn = fy_ssn;
	}
	public String getResv1() {
		return resv1;
	}
	public void setResv1(String resv1) {
		this.resv1 = resv1;
	}
   
}
