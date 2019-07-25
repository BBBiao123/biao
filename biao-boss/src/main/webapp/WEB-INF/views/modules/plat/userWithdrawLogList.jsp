<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户提现管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function checkUserWithdrawLog(userId,id) {
			//查询用户资产情况
			$.ajax({
                type: "POST",
                async: false,
                url: "${ctx}/plat/userWithdrawLog/checkUserWithdrawLog/"+userId+"?id="+id,
                success: function (msg) {
                	var resultObj = $.parseJSON(msg);
                	var content = "<div  style='text-align: center; font-size: 18px; margin-top: 30px'><p>用户的差额为:"+resultObj.msg+"</p><input style='margin-top: 30px;width:320px' type='text' name='auditReason' id='remarkId' placeholder='不通过原因'/></div>" ;
                	$.jBox.open(content, "提现审核", 540, 360, { buttons: { '审核通过': 1, '审核不通过': 0}, submit: function (v, h, f){
                		var success = false ;
                		var remark = $("#remarkId").val();
                		console.info("remark:"+remark);
                		if (v == 1) {
                           $.ajax({
                               type: "POST",
                               async: false,
                               url: "${ctx}/plat/userWithdrawLog/audit/1",
                               data:$.param({"id":id,"auditReason":remark}),
                               success: function (msg) {
                                   debugger;
                                   success = true ;
                                   location.href= "${ctx}/plat/userWithdrawLog/" ;
                               },
                               error: function (error) {
                                   $.jBox.alert("输入参数错误");
                                   console.log(error.responseText); // 日志输出控制台
                                   success = false;
                               }
                           });
                           return success;
                       } else {
                    	   $.ajax({
                               type: "POST",
                               async: false,
                               url: "${ctx}/plat/userWithdrawLog/audit/2",
                               data:$.param({"id":id,"auditReason":remark}),
                               success: function (msg) {
                                   debugger;
                                   success = true ;
                                   location.href= "${ctx}/plat/userWithdrawLog/" ;
                               },
                               error: function (error) {
                                   $.jBox.alert("输入参数错误");
                                   console.log(error.responseText); // 日志输出控制台
                               }
                           });
                           return success;
                       }
                },
                error: function (error) {
                    $.jBox.alert("输入参数错误");
                    console.log(error.responseText); // 日志输出控制台
                }
            });
            
            }});
        };
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/userWithdrawLog/">用户提现管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="userWithdrawLog" action="${ctx}/plat/userWithdrawLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户手机：</label>
				<form:input path="mobile" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>用户邮箱：</label>
				<form:input path="mail" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>币种符号：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>提现地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>交易hash：</label>
				<form:input path="txId" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('withdrawStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户id</th>
				<th>币id</th>
				<th>提现地址</th>
				<th>币种符号</th>
				<th>交易hash</th>
				<th>状态</th>
				<th>审核时间</th>
				<th>提币数量</th>
				<th>到账数量</th>
				<th>描述</th>
				<th>不通过原因</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:userWithdrawLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userWithdrawLog">
			<tr>
				<td><a href="${ctx}/plat/userWithdrawLog/form?id=${userWithdrawLog.id}">
					${userWithdrawLog.userId}
				</a></td>
				<td>
					${userWithdrawLog.coinId}
				</td>
				<td>
					${userWithdrawLog.address}
				</td>
				<td>
					${userWithdrawLog.coinSymbol}
				</td>
				<td>
					${userWithdrawLog.txId}
				</td>
				<td>
					${fns:getDictLabel(userWithdrawLog.status, 'withdrawStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${userWithdrawLog.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${userWithdrawLog.volume}
				</td>
				<td>
					${userWithdrawLog.realVolume}
				</td>
				<td>
					${userWithdrawLog.remark}
				</td>
				<td>
					${userWithdrawLog.auditReason}
				</td>
				<td>
					<fmt:formatDate value="${userWithdrawLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:userWithdrawLog:edit"><td>
					<c:if test="${userWithdrawLog.status=='0'}">
						<a href="#" onclick="checkUserWithdrawLog('${userWithdrawLog.userId}', '${userWithdrawLog.id}')">审核</a>
					</c:if>
					<c:if test="${userWithdrawLog.confirmStatus==2}">
						<a href="${ctx}/plat/userWithdrawLog/bufa/3?id=${userWithdrawLog.id}" onclick="return confirmx('确认补发吗？', this.href)">补发</a>
					</c:if>
						<%-- <c:if test="${userWithdrawLog.status=='0'}">
                            <a href="${ctx}/plat/userWithdrawLog/audit/2?id=${userWithdrawLog.id}" onclick="return confirmx('确认审核不通过吗？', this.href)">不通过</a>
                        </c:if> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>