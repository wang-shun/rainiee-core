package com.dimeng.p2p.user.servlets;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;

@PagingServlet(itemServlet = Term.class)
public class Term extends AbstractUserServlet {

	private static final long serialVersionUID = 1L;

}
