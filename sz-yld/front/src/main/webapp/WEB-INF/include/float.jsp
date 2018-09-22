<%@ page import="com.dimeng.p2p.modules.base.front.service.CustomerServiceManage" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5012_F03" %>
<%@ page import="com.dimeng.p2p.S50.entities.T5012" %>


<!--浮层-->
<div class="floating_layer">
  <ul>
    <li class="item">
      <div class="block calculator">
        <div class="con">
          <div class="border"><i class="arrow"></i><a href="<%configureProvider.format(out, URLVariable.LC_LCJSQ); %>">理财计算器</a></div>
        </div>
      </div>
    </li>
    <li class="item">
      <div class="block service">
        <div class="con">
          <div class="border"> <i class="arrow"></i>
            <ul>
                    <%
                            CustomerServiceManage customerServiceManage = serviceSession.getService(CustomerServiceManage.class);
                            T5012[] customerServices = customerServiceManage.getAll(T5012_F03.QQ);
                            if (customerServices != null && customerServices.length > 0) {
                                int index = 0;
                                for (T5012 customerService : customerServices) {
                                    index++;
                                    String image = fileStore.getURL(customerService.F07);
                                    if(T5012_F03.QQ == customerService.F03){
                    %>
                    
               <li><a href="http://wpa.qq.com/msgrd?v=<%StringHelper.filterHTML(out,customerService.F06);%>&uin=&site=qq&menu=yes" class="noborder"><span class="enterprise_q1"><img src="images/scqq_pic.jpg"/></span><%StringHelper.filterHTML(out, customerService.F05);%></a></li>
                  <% }
                     }
                    }
                    %>
            </ul>
          </div>
        </div>
      </div>
    </li>
    <li class="item">
      <div class="block weixin code">
        <div class="con">
          <div class="border"> <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>">
            <p>关注雨莅贷微信</p>
            <i class="arrow"></i> </div>
        </div>
      </div>
    </li>
   <li class="item" id="returnTop"><a class="block back" href="javascript:void(0);"></a></li>
  </ul>
</div>
<!--浮层--> 

<%-- <%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>
<div class="floating_layer">
    <ul>
        <li>
            <div class="item computer">
                <i></i>
                <div class="con">
                    <div class="text">
                        <p class="border_b"><a href="<%configureProvider.format(out, URLVariable.LC_LCJSQ); %>">理财计算器</a></p>
                        <p><a href="<%configureProvider.format(out, URLVariable.JK_JKJSQ); %>">借款计算器</a></p>
                    </div>
                </div>
            </div>
        </li>
        <li>
            <div class="item service">
                <i></i>
                <div class="con">
                    <%
                        {
                            CustomerServiceManage customerServiceManage = serviceSession.getService(CustomerServiceManage.class);
                            T5012[] customerServices = customerServiceManage.getAll(T5012_F03.QQ);
                            if (customerServices != null && customerServices.length > 0) {
                    %>
                    <div class="service_box">
                        <ul>
                            <%
                                int index = 0;
                                for (T5012 customerService : customerServices) {
                                    index++;
                                    String image = fileStore.getURL(customerService.F07);
                                    if(T5012_F03.QQ == customerService.F03){
                            %>
                            <li>
                                <a href="http://wpa.qq.com/msgrd?v=3&uin=<%StringHelper.filterHTML(out,customerService.F06);%>&site=qq&menu=yes" target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                                    <%
                                        if(!StringHelper.isEmpty(image)){
                                    %>
                                    <span class="enterprise_q1"><img src="<%=image%>"/></span>
                                    <%}else{%>
                                    <span class="enterprise_q1"></span>
                                    <%}%>
                                    <%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
                                </a>
                            </li>
                            <%}else{%>
                            <li>
                                <a href="<%StringHelper.filterHTML(out,customerService.F06);%>" target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                                    <%
                                        if(!StringHelper.isEmpty(image)){
                                    %>
                                    <span class="enterprise_q1"><img src="<%=image%>"/></span>
                                    <%}else{%>
                                    <span class="enterprise_q1"></span>
                                    <%}%>
                                    <%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
                                </a>
                            </li>
                            <%}} %>
                        </ul>
                    </div>
                    <% }
                    }
                    %>
                </div>
            </div>
        </li>
        <li>
            <a class="item code" href="javascript:void(0)">
                <i></i>
                <div class="con"><img src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))== "" ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"></div>
            </a>
        </li>
        <li><a class="item back" href="javascript:void(0);" id="re_top"><i></i><span class="con">返回顶部</span></a></li>
    </ul>
</div>

<%}else{%>
<div class="floating_layer">
  <ul>
    <li>
        <div class="item computer">
        	<i></i>
            <div class="con">
            	<div class="text">
                	<p class="border_b"><a href="<%configureProvider.format(out, URLVariable.LC_LCJSQ); %>">理财计算器</a></p>
                    <p><a href="<%configureProvider.format(out, URLVariable.JK_JKJSQ); %>">借款计算器</a></p>
                </div>
            </div>
        </div>
    </li>
    <li>
      <div class="item service">
          <i></i>
          <div class="con">
              <%
                  {
                      CustomerServiceManage customerServiceManage = serviceSession.getService(CustomerServiceManage.class);
                      T5012[] customerServices = customerServiceManage.getAll(T5012_F03.QQ);
                      if (customerServices != null && customerServices.length > 0) {
              %>
              <div class="service_box">
                  <ul>
                      <%
                              int index = 0;
                              for (T5012 customerService : customerServices) {
                                  index++;
                                  String image = fileStore.getURL(customerService.F07);
                                  if(T5012_F03.QQ == customerService.F03){
                      %>
                      <li>
                          <a href="http://wpa.qq.com/msgrd?v=3&uin=<%StringHelper.filterHTML(out,customerService.F06);%>&site=qq&menu=yes" target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                              <%
                                  if(!StringHelper.isEmpty(image)){
                              %>
                              <span class="enterprise_q1"><img src="<%=image%>"/></span>
                              <%}else{%>
                              <span class="enterprise_q1"></span>
                              <%}%>
                              <%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
                          </a>
                      </li>
                      <%}else{%>
                      <li>
                          <a href="<%StringHelper.filterHTML(out,customerService.F06);%>" target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
                              <%
                                  if(!StringHelper.isEmpty(image)){
                              %>
                              <span class="enterprise_q1"><img src="<%=image%>"/></span>
                              <%}else{%>
                              <span class="enterprise_q1"></span>
                              <%}%>
                              <%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
                          </a>
                      </li>
					<%}} %>
                  </ul>
              </div>
              <% }
              }
              %>
          </div>
      </div>
    </li>
    <li>
        <a class="item code" href="javascript:void(0)">
        	<i></i>
            <div class="con"><img src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))== "" ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"></div>
        </a>
    </li>
    <li><a class="item back" href="javascript:void(0);" id="re_top"><i></i><span class="con">返回顶部</span></a></li>
  </ul>
</div>
<%}%> --%>