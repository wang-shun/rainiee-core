<%@page import="com.dimeng.p2p.S61.enums.T6103_F06" %>
<%@page import="com.dimeng.p2p.user.servlets.spread.Wdtyj" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的体验金-<%configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDJL";
    CURRENT_SUB_CATEGORY = "WDTYJ";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>

<%-- <div class="w994 pb5"><span class="f16"><a href="<%=controller.getViewURI(request, Index.class)%>">我的<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></a></span> &gt; 推广管理 &gt; 我的体验金</div> --%>
<form id="wdtyjFormId" action="<%=controller.getURI(request, Wdtyj.class)%>" method="post">
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <div class="r_main">
                <%@include file="/WEB-INF/include/tyjlc/header.jsp" %>
                <div class="user_mod border_t15">
                    <div class="user_tab Menubox clearfix">
                        <ul>
                            <li id="SQZ" class="hover">投资中 <i></i></li>
                            <li id="CYZ">回款中<i></i></li>
                            <li id="YJZ">已结清<i></i></li>
                        </ul>
                    </div>
                    <div class="user_table pt5">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <thead id="dataHead">
                            </thead>
                            <tbody id="dataBody">
                            </tbody>
                        </table>
                        <div id="pageContent">
                        </div>
                    </div>
                </div>
                <div class="user_mod border_t15">
                    <div class="user_til clearfix">
                        <div class="fr">当前可用体验金&nbsp;
                            <span class="highlight f18" id="experAmonut"></span>元
                        </div>
                        <i class="icon"></i>
                        <span class="gray3 f18">我的体验金详情</span>
                    </div>
                    <div class="user_screening clearfix">
                        <ul>
                            <li>状态：
                                <select name="status" class="select6" id="statusSltId">
                                    <option value="">--全部--</option>
                                    <%for (T6103_F06 t6103_F06 : T6103_F06.values()) { %>
                                    <option value="<%=t6103_F06.name()%>"  <%if (t6103_F06.name().equals(request.getParameter("status"))) { %>
                                            selected="selected"<% }%>><%=t6103_F06.getChineseName() %>
                                    </option>
                                    <%} %>
                                </select>
                            </li>
                        </ul>
                    </div>
                    <div class="user_table">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">体验金金额(元)</td>
                                <td align="center">赠送时间</td>
                                <td align="center">过期时间</td>
                                <td align="center">收益期</td>
                                <td align="center">体验金状态</td>
                                <td align="center">来源</td>
                            </tr>
                        </table>
                        <div class="page wdtyjPage" id="WdtyjPageContent"></div>
                    </div>
                    <div class="user_mod_gray lh24 p30 mt30">
                        <span class="highlight">体验金说明:</span><br/>
                        1、体验金可以通过平台不定期举办的各种活动来获取，获取的体验金可用于投资项目。<br/>
                        2、体验金可叠加，投资时一次性使用全部可用体验金。<br/>
                        3、体验金为平台虚拟金额，投资后不计入实际投资金额中。<br/>
                        4、体验金不可单独提现，不可转让，不可购买债权，不可单独使用。<br/>
                        5、在投资项目时，体验金可跟随投资追加使用。<br/>
                        6、体验金产生的利息可提现，体验金利息由平台支付。<br/>
                        7、体验金本身对应金额投资的债权不能转让，真实金额投资的债权可以转让。<br/>
                        8、体验金不可以投资新手标、奖励标。<br/>
                        9、体验金具有收益期：当所投项目的借款期限&gt;=体验金收益期，实际收益期=体验金收益期；当所投项目的借款期限&lt;体验金收益期，实际收益期=所投项目的借款期限。<br/>
                        10、体验金具有使用有效期，必须在有效期内使用，过期失效。<br/>
                        11、体验金最终解释权归平台所有。<br/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>

<script type="text/javascript">
    var url = "<%=controller.getURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.Index.class)%>";
    var type = '<%=request.getParameter("status")%>';
    if (type != 'CYZ') {
        type = "SQZ";
    }
    $(document).ready(function () {
        /* $("#statusSltId").change(function () {
            var status = $("#statusSltId").val();
            ajaxSubmit(status);
        }); */
        $("#statusSltId").selectlist({
    		zIndex: 15,
    		width: 105,
    		optionHeight: 28,
    		height: 28,
    		onChange: function(){
    			var status = $("input[name='status']").val();
    			ajaxSubmit(status);
    		}
    	});
        $("#statusSltId option").eq(0).attr("selected",true);
        ajaxSubmit();
    });


    /* var currentPage = 1;
    var pageSize = 10;
    var pageCount = 1; */
    var ajaxUrl = '<%=controller.getURI(request, com.dimeng.p2p.user.servlets.spread.Wdtyj.class) %>';
    function ajaxSubmit(status) {
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize, "status": status};
        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxUrl,
            data: dataParam,
            async: false,
            success: function (returnData) {
            	
            	//判断用户是否已经
    			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
    				$(".popup_bg").show();
            		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
    			}
            	
                //移除table中的tr
                $("#dataTable tr").empty();
                //填充数据,li样式需要改变
                var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>体验金金额(元)</td>" +
                        "<td align='center'>赠送时间</td><td align='center'>过期时间</td><td align='center'>收益期</td>" +
                        "<td align='center'>体验金状态</td><td align='center'>来源</td></tr>";
                $("#dataTable").append(trHTML);//在table最后面添加一行
                trHTML = "";
                //分页信息
                $("#WdtyjPageContent").html(returnData.pageStr);
                pageCount = returnData.pageCount;
                //分页点击事件
                $("#WdtyjPageContent a.page-link").click(function () {
                    pageParam(this, status);
                });
                if (null == returnData || null == returnData.retList || returnData.retList.length == 0) {
                    $("#WdtyjPageContent").html("");
                    $("#dataTable").append("<tr><td colspan='7' align='center'>暂无数据</td></tr>");
                }
                $("#experAmonut").html(returnData.experAmonut);
                if (returnData.retList != null && returnData.retList.length > 0) {
                    var rAssests = returnData.retList;
                    $.each(rAssests, function (n, value) {
                        var dayType;
                        if(value.F16 == 'false'){
                            dayType ="天";
                        }else{
                            dayType ="个月";
                        }
                        trHTML += "<tr>" +
                                "<td align='center'>" + (n + 1) + "</td>" +
                                "<td align='center'>" + value.expAmount + "</td>" +
                                "<td align='center'>" + value.beginDate + "</td>" +
                                "<td align='center'>" + value.endDate + "</td>" +
                                "<td align='center'>" + value.F07 + dayType +
                                "<td align='center'>" + value.state + "</td>" +
                                "<td align='center'>" + value.fromStr + "</td>" +
                                "</tr>";
                        $("#dataTable").append(trHTML);//在table最后面添加一行
                        trHTML = "";
                    });
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
            }
        });
    }
    
    function pageSubmit1(_obj){
    	setCurrentPage(_obj);
        ajaxSubmit($("#statusSltId").val());
    }
    
    function toAjaxPage(){
    	
    	if ($("#SQZ").hasClass("hover")) {
    		initData('SQZ');
    	}else if ($("#CYZ").hasClass("hover")) {
    		initData('CYZ');
    	}else if ($("#YJZ").hasClass("hover")) {
    		initData('YJZ');
    	}
    }

    document.onkeydown = function () {   	
    	if (window.event && window.event.keyCode == 13) {
    	var _obj=$("input.btn_ok.page-link.cur").selector;
    	pageSubmit1(_obj);
    	}   
    	};
    	
    	function onlyNum() 
    	{ 
    	if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39)) 
    	if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105))) 
    	event.returnValue=false; 
    	}
    /**
     *分页查询请求
     * @param obj
     * @param liId
     * @returns {boolean}
     */
    /* 	function pageParam(obj,status) {
     if ($(obj).hasClass("cur")) {
     return false;
     }
     $(obj).addClass("cur");
     $(obj).siblings("a").removeClass("cur");
     if ($(obj).hasClass("startPage")) {
     currentPage = 1;
     } else if ($(obj).hasClass("prev")) {
     currentPage = currentPage - 1;
     } else if ($(obj).hasClass("next")) {
     currentPage = currentPage + 1;
     } else if ($(obj).hasClass("endPage")) {
     currentPage = pageCount;
     } else {
     currentPage = parseInt($(obj).html());
     }
     ajaxSubmit(status);
     } */
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/financing/tyjlc/tyjlc.js"></script>
</body>
</html>