<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.user.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.AssetsInfo"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.WdzqManage"%>
     <%
     	WdzqManage service=serviceSession.getService(WdzqManage.class);
     	AssetsInfo ai= service.getAssetsInfo();
     	UserInfoManage userManage = serviceSession.getService(UserInfoManage.class);
    	T6110 t6110Obj =userManage.getUserInfo(serviceSession.getSession().getAccountId());
    	final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
    	Paging paging=new Paging(){
    		public int getCurrentPage(){
    			return currentPage;
    		}
    		
    		public int getSize(){
    			return 10;
    		}
    	};
     	if(ai != null){
     %>
     
     <div class="debt_total clearfix">
            	<div class="total fl mt50 pt10">
                   	投资收益(元)
                    <span class="hover_tips">
                        <div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">
                                投资收益=债权已赚取利息+罚息+违约金-理财管理费
                            </div>
                        </div>
                    </span>
                    <br />
                    <span class="orange f22"><%=Formater.formatAmount(ai.makeMoney) %></span>
                </div>
                <div class="line"></div>
                <div class="total fl mt50 pt10">
                   	投资账户资产(元)
                    <br />
                    <span class="orange f22"><%=Formater.formatAmount(ai.money) %></span>
                </div>
                <div class="line"></div>
                <div class="total fl mt50 pt10">
                   	回收中的债权数量(个)
                    <br />
                    <span class="orange f22"><%=ai.assetsNum %></span>
                </div>
                <%-- <div class="list">
                	<ul>
                    	<li>
                            	投资收益(元)
                            <span class="hover_tips">
                                <div class="hover_tips_con">
                                    <div class="arrow"></div>
                                    <div class="border">
                                        利息收益=债权已赚利息罚息+违约金+已垫付利息
                                    </div>
                                </div>
                            </span>
                            <br />
                            <%=ai.accMakeMoney %>
                        </li>
                        <li>债权转让盈亏(元)<br /><%=ai.sellMakeMoney %></li>
                        <li>投资账户资产(元)<br /><%=ai.money %></li>
                        <li>回收中的债权数量(个)<br /><%=ai.assetsNum %></li>
                    </ul>
                </div> --%>
            </div>
            <%if(T6110_F07.HMD != t6110Obj.F07 && T6110_F06.ZRR==t6110Obj.F06 && BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_AUTOBID))){ %>
            <div class="user_mod border_t15">没时间亲自投资？试试<%configureProvider.format(out,SystemVariable.SITE_NAME); %>自动投资功能吧。<a href="<%configureProvider.format(out, URLVariable.USER_AUTO_BID);%>" class="btn04 ml10">自动投资</a></div>
     		<%} %>
		     <%
		     	}else{
		     %>
           <div class="debt_total clearfix">
           		<div class="total fl mt50 pt10">
                   	投资收益(元)
                    <span class="hover_tips">
                        <div class="hover_tips_con">
                            <div class="arrow"></div>
                            <div class="border">
                                                                                        投资收益=债权已赚取利息+罚息+违约金+已垫付利息-理财管理费
                            </div>
                        </div>
                    </span>
                    <br />
                    <span class="orange f22">0.00</span>
                </div>
                <div class="line"></div>
                <div class="total fl mt50 pt10">
                   	投资账户资产(元)
                    <br />
                    <span class="orange f22">0.00</span>
                </div>
                <div class="line"></div>
                <div class="total fl mt50 pt10">
                   	回收中的债权数量(个)
                    <br />
                    <span class="orange f22">0</span>
                </div>
            </div>
            <%if(T6110_F07.HMD != t6110Obj.F07){ %>
            <div class="user_mod border_t15">没时间亲自投资？试试<%configureProvider.format(out,SystemVariable.SITE_NAME); %>自动投资功能吧。<a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.zdtb.Index.class)%>" class="btn04 ml10">自动投资</a></div>
     		<%} %>
     <%
     	}
     %>
