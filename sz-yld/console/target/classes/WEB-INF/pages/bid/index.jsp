<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%CURRENT_CATEGORY = "YWGL";%>
<!--内容-->
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->

                <!--用户详细信息-->
                <div class="p20">
                    <div class="user-container border p20 pl30 pr30 gray6">
                        <ul class="lh24">
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">理财管理</h3>

                                <p class="">理财管理对包括债权、债权转让等理财项目的数据进行统计，便于平台管理。</p>
                            </li>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">借款管理</h3>

                                <p class="">借款管理对各种状态的借款进行统计管理，方便业务人员查看相应借款信息。</p>
                            </li>
                            <%//开关判断，为false，则不显示公益标
                                if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){%>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">公益捐赠</h3>

                                <p class="">公益捐赠对公益标进行统计管理，方便业务人员查看相应公益标信息。</p>
                            </li>
                            <%}%>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">催收管理</h3>

                                <p class="">催收管理记录催款的全过程，让催收变得更合理，更有效。</p>
                            </li>
                            <li class="pb10 pt20 border-b-s">
                                <h3 class="f18">合同管理</h3>

                                <p class="">可查看企业借款协议、个人借款协议、债权转让协议的相关合同。</p>
                            </li>
                        </ul>
                    </div>
                </div>

                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<!--内容-->
</body>
</html>