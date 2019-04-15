<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户充值管理管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/userDepositLog/">用户充值管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="userDepositLog" action="${ctx}/plat/userDepositLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币符号：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('depositStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>归集状态：</label>
				<form:select path="raiseStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:option value="0" label="默认"/>
					<form:option value="1" label="归集中"/>
					<form:option value="2" label="归集成功"/>
					<form:option value="9" label="归集失败"/>
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
				<th>充值地址</th>
				<th>币符号</th>
				<th>充值hash</th>
				<th>数量</th>
				<th>状态</th>
				<th>归集状态</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>create_by</th>
				<th>update_by</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userDepositLog">
			<tr>
				<td><a href="${ctx}/plat/userDepositLog/form?id=${userDepositLog.id}">
					${userDepositLog.userId}
				</a></td>
				<td>
					${userDepositLog.address}
				</td>
				<td>
					${userDepositLog.coinSymbol}
				</td>
				<td>
					${userDepositLog.txId}
				</td>
				<td>
					${userDepositLog.volume}
				</td>
				<td>
					${fns:getDictLabel(userDepositLog.status, 'depositStatus', '')}
				</td>
				<td>
					<c:choose>
                		<c:when test="${userDepositLog.raiseStatus==0}">默认</c:when>
                    	<c:when test="${userDepositLog.raiseStatus==1}">归集中</c:when>
                    	<c:when test="${userDepositLog.raiseStatus==2}">归集成功</c:when>
                    	<c:when test="${userDepositLog.raiseStatus==9}">归集失败</c:when>
                    	<c:otherwise>未知</c:otherwise>
                </c:choose>
				</td>
				<td>
					<fmt:formatDate value="${userDepositLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${userDepositLog.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${userDepositLog.createBy.id}
				</td>
				<td>
					${userDepositLog.updateBy.id}
				</td>
				<td>
					<c:if test="${userDepositLog.raiseStatus==0||userDepositLog.raiseStatus==9}">
					  <a href="${ctx}/plat/userDepositLog/updateStatus?id=${userDepositLog.id}&raiseStatus=1" onclick="return confirmx('确认要归集吗？', this.href)">归集</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>