<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.CsList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.business.SelectEmpNum"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.Del"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.AddJg"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.AddQy"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.AddGr"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.UnBlack"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.Black"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ResetLoginError"%>
<%@page import="com.dimeng.p2p.S71.entities.T7152"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.UnLock"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.Lock"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.Export"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F08"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ZhList"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyUnLock"%>
<%@ page import="com.dimeng.p2p.modules.account.console.service.entity.User" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "ZHGL";
    UserManage userManage = serviceSession.getService(UserManage.class);
    PagingResult<User> list = (PagingResult<User>) request.getAttribute("list");
    User[] userArray = (list == null ? null : list.getItems());
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
%>
<div class="right-container">
  <div class="viewFramework-body">
    <div class="viewFramework-content"> 
      <form id="form1" action="<%=controller.getURI(request, ZhList.class)%>" method="post">
		<div class="p20">
          <div class="border">
            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>账号管理</div>
            <div class="content-container pl40 pt30 pr40">
              <ul class="gray6 input-list-container clearfix">
                <li><span class="display-ib mr5">用户名</span>
                  <input type="text" name="userName" value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>" class="text border pl5 mr20" />
                </li>
                <li><span class="display-ib mr5">联系电话</span>
                  <input type="text" name="phone" value="<%StringHelper.filterHTML(out, request.getParameter("phone"));%>" class="text border pl5 mr20" />
                </li>
                <li><span class="display-ib mr5">邮箱</span>
                  <input type="text" name="eamil" value="<%StringHelper.filterHTML(out, request.getParameter("eamil"));%>" class="text border pl5 mr20" />
                </li>
                <li><span class="display-ib mr5">注册时间</span>
                  <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date"  value="<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>"/>
                  <span class="pl5 pr5">至</span>
                  <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date"  value="<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>"/>
                </li>
                <li><span class="display-ib mr5">注册来源</span>
                 
                  <select name="zcly" class="border mr20 h32 mw80">
                       <option value="">全部</option>
                        <%
                        	for (T6110_F08 t6110_F08 : T6110_F08.values()) {
                        %>
						<option value="<%=t6110_F08.name()%>" <%if (t6110_F08.name().equals(request.getParameter("zcly"))) {%> selected="selected" <%}%>><%=t6110_F08.getChineseName()%></option>
						<%
							}
						%>                                
                   </select>
                </li>
                <li><span class="display-ib mr5">用户类型</span>
                  <select name="zhlx" class="border mr20 h32 mw60">
                                	<option value="">全部</option>
                                	<option value="GR" <%if("GR".equals(request.getParameter("zhlx"))){%>selected="selected" <%}%>>个人</option>
                                	<option value="QY" <%if("QY".equals(request.getParameter("zhlx"))){%>selected="selected" <%}%>>企业</option>
                                	<option value="JG" <%if("JG".equals(request.getParameter("zhlx"))){%>selected="selected" <%}%>>机构</option>
                                </select>
                </li>
                <!-- <li><span class="display-ib mr5">业务员工号</span>
                  <input type="text" class="text border pl5 mr20" value="" />
                </li> -->
                <li><span class="display-ib mr5">状态</span>
                  <select name="status" class="border mr10 h32 mw60">
                                  	<option value="">全部</option>
                                  	<%
                                  		for (T6110_F07 t6110_F08 : T6110_F07.values()) {
                                  	%>
									<option value="<%=t6110_F08.name()%>" <%if (t6110_F08.name().equals(request.getParameter("status"))) {%> selected="selected" <%}%>><%=t6110_F08.getChineseName()%></option>
									<%
										}
									%>                                
                                  </select>
                </li>
                <%if(is_business){ %>
                <li><span class="display-ib mr5">业务员工号</span>
                  <input type="text" name="employNum" value="<%StringHelper.filterHTML(out, request.getParameter("employNum"));%>" class="text border pl5 mr20" />
                </li>
                <%} %>
                <li>
                
                <a href="javascript:void(0)" onclick="onSubmit()" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                </li>
                <li>
                	<%
                     		if (dimengSession.isAccessableResource(Export.class)) {
                     	%>
                     	<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                     	<%
                     		}else{
                     	%>
                     	<span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                     	<%
                     		}
                     	%>	
                </li>
                <li>
                	<%
                      		if (dimengSession.isAccessableResource(AddGr.class)) {
                      	%>
           				<a href="<%=controller.getURI(request,AddGr.class)%>" class="btn btn-blue2 radius-6 mr5 pl10 pr10">新增个人账号</a>
           				<%
           					}else{
           				%>
                      	<span class="btn btn-gray radius-6 mr5 pl10 pr10">新增个人账号</span>
                      	<%
                      		}
                      	%>
                </li>
                <li>
                	<%
                              			if (dimengSession.isAccessableResource(AddQy.class)) {
                              		%>
                   				<a href="<%=controller.getURI(request,AddQy.class)%>" class="btn btn-blue2 radius-6 mr5 pl10 pr10 click-link">新增企业账号</a>
                   				<%
                   					}else{
                   				%>
                              	<span class="btn btn-gray radius-6 mr5 pl10 pr10 click-link">新增企业账号</span>
                              	<%
                              		}
                              	%>
                </li>
                <li>
                	<%
                              		if (dimengSession.isAccessableResource(AddJg.class)) {
                              	%>
                   				<a href="<%=controller.getURI(request,AddJg.class)%>" class="btn btn-blue2 radius-6 mr5 pl10 pr10" >新增机构账号</a>
                   				<%
                   					}else{
                   				%>
                              	<span class="btn btn-gray radius-6 mr5 pl10 pr10">新增机构账号</span>
                              	<%
                              		}
                              	%>
                </li>
              </ul>
            </div>
          </div>
          <div class="border mt20 table-container">
            <table class="table table-style gray6 tl">
              <thead>
                <tr class="title tc">
                  <th>序号</th>
                  <th>用户名</th>
                  <th>联系电话</th>
                  <th>邮箱</th>
                  <th>状态</th>
                  <th>注册来源</th>
                  <th>注册时间</th>
                  <th>用户类型</th>
                  <%if(is_business){ %>
                  <th>业务员工号</th>
                  <%} %>
                  <th class="w200" style="text-align: center;">操作</th>
                </tr>
              </thead>
              <tbody class="f12">
                 <%
                        	if (userArray != null){
                                int i = 1;
                                for(User user :userArray){
                        %>
                        <tr class="tc">
                          <td><%=i++%></td>
                          <td><%
                          	StringHelper.filterHTML(out, user.F02);
                          %></td>
                          <td><%
                          	StringHelper.filterHTML(out, user.F03);
                          %></td>
                          <td><%
                          	StringHelper.filterHTML(out, user.F04);
                          %></td>
                          <td><%
                          	StringHelper.filterHTML(out, user.F06.getChineseName());
                          %></td>
                          <td><%
                          	StringHelper.filterHTML(out, user.F07.getChineseName());
                          %></td>
                          <td><%=DateTimeParser.format(user.F08)%></td>
                          <td>
                          	<%
                          		T6110_F06 yhlx = user.F05;
                          	                          		T6110_F10 dbf =  user.F09;
                          	                          		if(yhlx == T6110_F06.ZRR){
                          	                          			out.print("个人");
                          	                          		}else if(yhlx == T6110_F06.FZRR && dbf == T6110_F10.F){
                          	                          			out.print("企业");
                          	                          		}else if(yhlx == T6110_F06.FZRR && dbf == T6110_F10.S){
                          	                          			out.print("机构");
                          	                          		}
                          	%>
                          </td>
                          <%if(is_business){ %>
                          <td><%
                          	StringHelper.filterHTML(out, user.employNum);
                          %></td>
                          <%} %>
                          <td>
                        
                          	
                         	<%
                         		if(!user.F02.equals("平台账号")){
                         		                         		if (T6110_F07.QY == user.F06){
                         		                         	                         			if (dimengSession.isAccessableResource(Lock.class)) {
                         		                         	%>
		                          	<a href="javascript:void(0)" onclick="showSd('<%=i%>')" class="link-blue mr20 popup-link" data-wh="400*200">锁定</a>
		                          	<%
		                          		}else{
		                          	%>
		                          	<a class="disabled mr20">锁定</a>
                         	<%}
                         	  }else if(T6110_F07.SD == user.F06){
                         		 if(T6110_F06.FZRR == user.F05 && T6110_F10.F == user.F09){
                      	    		if (dimengSession.isAccessableResource(QyUnLock.class)) {%>
                      	    			<a href="javascript:void(0)" onclick="showJs('<%=i%>')" class="link-blue mr20 popup-link" data-wh="400*200">解锁</a>
                      	    		<%}else{ %>
                      	    			<a class="disabled mr20">解锁</a>
                      	    		<%}
                         		 }else{
                         			if (dimengSession.isAccessableResource(UnLock.class)) {%>
                         				<a href="javascript:void(0)" onclick="showJs('<%=i%>')" data-wh="400*200" class="link-blue mr20 popup-link">解锁</a>
                         			<%}else{ %>
                         				<a class="disabled mr20">解锁</a>
                         				
                         			<%} 
                         		 }
                         	  }                     			
                         	%>
                              <%
                                if(T6110_F07.SD == user.F06)
                                {
                               %>
                                    <a class="disabled mr20">拉黑</a>
                          	<%
                                }
                                else
                                {
                          		if(T6110_F07.HMD != user.F06 && user.isYzyq){


                                                if (dimengSession.isAccessableResource(Black.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showYzyqLh('<%=i%>')"
                                           class="libk-deepblue mr20">拉黑</a>
                                        <%
                                        } else {
                                        %>
                                        <a class="disabled mr20">拉黑</a>
                                        <%
                                            }
                                        %>
                                        <%
                                        } else if (T6110_F07.HMD != user.F06 && user.isYq) {
                                        %>
                                        <%
                                            if (dimengSession.isAccessableResource(Black.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showYqLh('<%=i%>')"
                                           class="libk-deepblue mr20">拉黑</a>
                                        <%
                                        } else {
                                        %>
                                        <a class="disabled mr20">拉黑</a>
                                        <%
                                            }
                                        %>
                                        <%
                                        } else if (T6110_F07.HMD != user.F06 && !user.isYzyq) {
                                        %>
                                        <%
                                            if (dimengSession.isAccessableResource(Black.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showLh('<%=i%>')"
                                           class="libk-deepblue mr20">拉黑</a>
                                        <%
                                        } else {
                                        %>
                                        <a class="disabled mr20">拉黑</a>
                                        <%
                                            }
                                        %>
                                        <%
                                                }
                                            }
                                        %>
                                        <%
                                            if (T6110_F07.HMD == user.F06) {
                                        %>
                                        <%if (dimengSession.isAccessableResource(Black.class)) { %>
                                        	<a href="javascript:void(0)" onclick="qxLh(<%=user.F01%>,'<%=user.F02%>')" class="libk-deepblue mr20">取消拉黑</a>
                                        <%}else{ %>
                                        	<a href="javascript:void(0)" class="disabled mr20">取消拉黑</a>
                                        <%} %>
                                        <%
                                            } %>

                                        <%--  	<%
                                                                               if (dimengSession.isAccessableResource(Del.class)) {
                                                                           %>
                                                 <span class="blue"><a href="javascript:void(0)" onclick="showDel('<%=i %>')" class="mr10">删除</a></span>
                                                 <%}else{ %>
                                                 <span class="disabled" class="mr10">删除</span>
                                                 <%} %> --%>
                                        <%if (dimengSession.isAccessableResource(ResetLoginError.class)) {%>
                                        <a href="javascript:void(0)" onclick="resetLoginError('<%=user.F02%>');"
                                           class="link-orangered">登录错误数重置</a>
                                        <%} else { %>
                                        <a class="disabled">登录错误数重置</a>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                            }
                                        }
                        	} else {
                                %>
                                <tr class="dhsbg">
                                    <td colspan="10" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>

                        <%
                            AbstractConsoleServlet.rendPagingResult(out, list);
                        %>

                        <!--分页 end-->

                        <!--加载内容 结束-->
                    </div>
                </form>

                <%
                    if (userArray != null) {
                        int i = 1;
                        for (User user : userArray) {
                            i++;
                %>

                <!--弹出框-->
                <div class="popup-box hide" id="sd_<%=i%>" style="min-height: 220px;">
                    <form action="<%=controller.getURI(request, Lock.class)%>" method="post" id="sdform_<%=i %>"
                          class="form1">
                        <input type="hidden" name="userId" value="<%=user.F01%>"/>

                        <div class="popup-title-container">
                            <h3 class="pl20 f18">锁定</h3>
                            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
                        </div>
                        <div class="popup-content-container pt20 pb20 clearfix">
                            <div class="tc mb20"><span
                                    class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                                    class="f20 h30 va-middle ml10">确定锁定用户“<span id="a"><%
                                StringHelper.filterHTML(out, user.F02);
                            %></span>”吗？</span></div>
                            <div class="tc f16">
                            <a href="javascript:void(0);" onclick="$('#sdform_<%=i %>').submit();"
                                                   class="btn-blue2 btn white radius-6 pl20 pr20">确定
                                                   </a>
                            <a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                   href="javascript:closeInfo();">取消</a>
                            </div>
                        </div>
                    </form>
                </div>


                <div class="wrap" id="del_<%=i%>" style="display: none">
                    <div class="popup_bg"></div>
                    <form action="<%=controller.getURI(request, Del.class)%>" method="post" class="form1">
                        <input type="hidden" name="userId" value="<%=user.F01%>"/>

                        <div id="sd" class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;">
                            <div class="tit">
                                <span class="fl pl15">删除</span>
                                <span class="fr pr15 close"
                                      onclick="javascript:document.getElementById('del_<%=i%>').style.display='none';return false;"><a
                                        href="#">关闭</a></span>
                            </div>
                            <div id="js" class="info clearfix">
                                <div class="clearfix">
                                    <span class="icon_cw" style="display:none;"></span>
                                    <span class="icon_yw" style="display:none;"></span>
                                    <span class="icon_cg" style="display:none;"></span>
				            <span class="fl tips"><span class="icon_gt"></span>确定删除用户“<span id="a"><%
                                StringHelper.filterHTML(out, user.F02);
                            %></span>”吗？</span>
                                </div>
                                <div class="clear"></div>
                                <div class="dialog_btn"><input type="submit" value="提交" class="btn4 ml50 sumbitForme"
                                                               fromname="form1"/></div>
                            </div>
                        </div>
                    </form>
                </div>

                <!--弹出框-->
                <div class="popup-box hide" id="js_<%=i%>" style="min-height: 220px;">
                    <form action="<%=controller.getURI(request, UnLock.class)%>" method="post" id="jsform_<%=i %>"
                          class="form1">
                        <input type="hidden" name="userId" value="<%=user.F01%>"/>

                        <div class="popup-title-container">
                            <h3 class="pl20 f18">解锁</h3>
                            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
                        </div>
                        <div class="popup-content-container pt20 pb20 clearfix">
                            <div class="tc mb20"><span
                                    class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                                    class="f20 h30 va-middle ml10">确定解锁用户“<span id="a"><%
                                StringHelper.filterHTML(out, user.F02);%></span>”吗？</span></div>
                            <div class="tc f16"><a href="javascript:void(0);" onclick="$('#jsform_<%=i %>').submit();"
                                                   class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                                                   <a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                   href="javascript:closeInfo();">取消</a>
                                                   </div>
                        </div>
                    </form>
                </div>
                <%
                    T7152[] csjllist = userManage.csjlSearch(user.F01);
                %>
                <div id="lh_<%=i%>" class="popup-box hide">
                        <div class="popup-title-container">
                            <h3 class="pl20 f18">拉黑</h3>
                            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
                    <div class="popup-content-container-2">
                    <form id="f_lh_<%=i %>" action="<%=controller.getURI(request, Black.class)%>" method="post"
                          class="form1">
                        <input type="hidden" name="type" value="1"/>
                        <input type="hidden" name="userId" value="<%=user.F01%>"/>
                        <input type="hidden" name="state" value="LH"/>
                        <input type="hidden" name="pageFlg" value="1"/>
                            <div class="p30">
                                <ul class="gray6">
                                    <li class="mb15"><span class="display-ib tr mr5 w120 fl">用户名：</span>
                                        <div class="pl120"><%StringHelper.filterHTML(out, user.F02); %></div>
                                    </li>
                                    <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red pr5">*</em>拉黑说明：</span>
                                        <div class="pl120"><textarea id="ta_lh_<%=i %>" name="blacklistDesc" cols="40" rows="4" class="area"></textarea></div>
                                        <div class="pl120"><p id="p_lh_<%=i %>" class="error_tip red" /></div>
                                    </li>
                                </ul>

                            <div class="tc f16">
                                <a href="javascript:submitlh('<%=i %>');"
                                   class="btn-blue2 btn white radius-6 pl20 pr20">确定</a>
                                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                   href="javascript:closelh('<%=i %>');">取消</a></div>

                            <div class="mt20 h30 lh30 gray6">最近催收记录(所有借款)：</div>
                            <div class="border table-container">
                                <table class="table table-style gray6 tl">
                                    <thead class="fb">
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>催收时间</th>
                                        <th class="">催收方式</th>
                                        <th>催收人</th>
                                        <th class="">结果概要</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (csjllist != null) {
                                            for (int n = 0, length = Math.min(csjllist.length, 3); n < length; n++) {
                                                T7152 t7152 = csjllist[n];
                                                if (t7152 == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=n + 1%>
                                        </td>
                                        <td><%=Formater.formatDate(t7152.F08) %>
                                        </td>
                                        <td><%=t7152.F04.getChineseName() %>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, t7152.F05); %></td>
                                        <td title="<%StringHelper.filterHTML(out, t7152.F06);%>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(t7152.F06, 15));%></td>
                                    </tr>
                                    <%
                                            }
                                        } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="5">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                                </div>
                            <div class="paging clearfix pt20">
                                <div class="page-size fr gray6 mr20 lh30"><span
                                        data-bind="text: formatedItemCount">总共<span
                                        class="main-color pl5 pr5"><%=csjllist == null ? 0 : csjllist.length %></span>条记录</span>
                                    |
                                    <a href="<%=controller.getURI(request, CsList.class)%>?userName=<%=user.F02%>" showObj="CSJL" data-title="business" 
                                       class="link-blue link_url" showObj="CSJL" data-title="business">查看全部&gt;&gt;</a>
                                </div>
                            </div>
                        </div>
                    </form>
                    </div>
                </div>
                <%
                        }
                    }
                %>
                <div class="popup_bg hide"></div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<div id="info"></div>
<%@include file="/WEB-INF/include/script.jsp" %>
 <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%	
		String message = controller.getPrompt(request, response, PromptLevel.WARRING); 
		if(!StringHelper.isEmpty(message)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"wrong"));
		</script>
	<%} %>
<%	
		String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO); 
		if(!StringHelper.isEmpty(messageInfo)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=messageInfo%>',"yes"));
		</script>
	<%} %>
<!--<div class="popup_bg"  style="display: none;"></div> -->
	<%@include file="/WEB-INF/include/datepicker.jsp"%>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
	
	<script type="text/javascript">
		$(function() {
			$("#datepicker1").datepicker({
				inline: true,
				onSelect : function(selectedDate) {
                    $("#datepicker2").datepicker("option", "minDate", selectedDate);  }
			});
		    $('#datepicker1').datepicker('option', {dateFormat:'yy-mm-dd'});
		    $("#datepicker2").datepicker({inline: true});
		   
		    $('#datepicker2').datepicker('option', {dateFormat:'yy-mm-dd'});
		    <%if(!StringHelper.isEmpty(createTimeStart)){%>
		   		$("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
		   	<%}%>
		   	<%if(!StringHelper.isEmpty(createTimeEnd)){%>
		    	$("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
		    <%}%>
		    $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());
		    
		    $("#plthywy").click(function() {
		    	
		    	$(".removeValue").val('');
		    	$(".removeHtml").html('');
		    	
		    	$("div.popup_bg").show();
				$("#replaceEmp").show();
			});
		});
		var userId;
		function onSubmit(){
			$("#form1").submit();
		}
		
		function showExport()
		{
			document.getElementById("form1").action ="<%=controller.getURI(request, Export.class)%>";
			$("#form1").submit();
			document.getElementById("form1").action ="<%=controller.getURI(request, ZhList.class)%>";
		}
		
		function showSd(i)
		{
			$(".popup_bg").show();
			$("#sd_"+i).show();
		}
		
		function showJs(i)
		{
			$(".popup_bg").show();
			$("#js_"+i).show();
		}
		
		function showLh(i)
		{
		    $(".popup_bg").show();
			$("#info").html(showConfirmDiv("此账号无逾期情况，请慎重操作！",i,'lh'));
		}
		
		function showYqLh(i)
		{
		    $(".popup_bg").show();
			$("#info").html(showConfirmDiv("此账号有逾期情况，是否拉黑！",i,'lh'));
		}
		
		function toConfirm(param,type){
			$("#info").html("");
			if(type=='qxlh'){
				location.href = "<%=controller.getURI(request, UnBlack.class)%>"+"?userId=" + userId + "&pageFlg=1";
			}else{
                if(type=="lh")
                {
                    //分辨率低是弹出框显示不全
                    var w = 500;
                    var h = 500;
                    var windos_h=$(window).height();
                    if(h>windos_h)
                    {
                        h=windos_h-40;
                    }
                    $(".popup-content-container-2").css({"max-height":h-50+"px"});
                    $(".popup-box").css({
                        "left" : 50 + "%",
                        "top" : 50 + "%",
                        "width" : w + "px",
                        "margin-left" : -w / 2 + "px",
                        "height" : h + "px",
                        "margin-top" : -h / 2 + "px"
                    });
                }
				$("#"+type+"_"+param).show();
			}
		}
		
		
		function showDel(i)
		{
			if(confirm("删除后不能恢复，确定删除此账号？"))
		    {
			$("#del_"+i).show();
		    }
		}
		
		function showYzyqLh(i)
		{
			$(".popup_bg").show();
			$("#info").html(showConfirmDiv("此账号有严重逾期情况，是否拉黑！",i,'lh'));
		}


		//取消拉黑
		function qxLh(id,userName)
		{
		    userId = id;
		    $(".popup_bg").show();
			$("#info").html(showConfirmDiv("是否取消拉黑"+userName+"?",0,'qxlh'));
		}
		
		function submitlh(index){
			var textarea = $("#ta_lh_"+index).val();
			if($.trim(textarea) == ""){
				$("#p_lh_"+index).html("不能为空！");
				return;
			}else{
				if(textarea.length>255){
					$("#p_lh_"+index).html("超过输入限制255，当前长度为"+textarea.length);
					return;
					
				}
			}
			$("#btn_"+index).attr('disabled',true);
			$("#f_lh_"+index).submit();
		}
		
		function closelh(index){
		 	$("#p_lh_"+index).html("");
			$("#ta_lh_"+index).val("");
			$("#btn_"+index).attr('disabled',false); 
			$("#lh_"+index).hide();
			$(".popup_bg").hide();
		}
		
		function resetLoginError(userName)
		{
			$.ajax({
				type:"post",
				url:"<%=controller.getURI(request, ResetLoginError.class)%>",
				data:{"userName" : userName},
			    dataType:"text",
			    success: function(returnData){
			    	$("#info").html(showDialogInfo(returnData,"yes"));
					$("div.popup_bg").show();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
				}
			});
		}
		
		function toSubmit(){
			if($(".cont span:visible").length>0){
				return false;
			}
		}
		
	</script>

</body>
</html>