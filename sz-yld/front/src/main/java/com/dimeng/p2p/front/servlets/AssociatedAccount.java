package com.dimeng.p2p.front.servlets;



public class AssociatedAccount extends AbstractFrontServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}
	
}
