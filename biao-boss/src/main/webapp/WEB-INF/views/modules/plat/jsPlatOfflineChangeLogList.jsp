<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>C2C转账记录管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatOfflineChangeLog/">C2C转账记录列表</a></li>
		<shiro:hasPermission name="plat:jsPlatOfflineChangeLog:edit"><li><a href="${ctx}/plat/jsPlatOfflineChangeLog/form">C2C转账记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatOfflineChangeLog" action="${ctx}/plat/jsPlatOfflineChangeLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="changeNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币id：</label>
				<form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="symbol" itemValue="coinId" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('changeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>被用户ID：</label>
				<form:input path="otherUserId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>订单号</th>
				<th>状态</th>
				<th>用户ID</th>
				<th>币种符号</th>
				<th>转账数额</th>
				<th>类型</th>
				<th>手续费</th>
				<th>被交易用户ID</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:jsPlatOfflineChangeLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatOfflineChangeLog">
			<tr>
				<td><a href="${ctx}/plat/jsPlatOfflineChangeLog/form?id=${jsPlatOfflineChangeLog.id}">
					${jsPlatOfflineChangeLog.id}
				</a></td>
				<td>
					${jsPlatOfflineChangeLog.changeNo}
				</td>
				<td>
					成功
				</td>
				<td>
					${jsPlatOfflineChangeLog.userId}
				</td>
				<td>
					${jsPlatOfflineChangeLog.coinSymbol}
				</td>
				<td>
					${jsPlatOfflineChangeLog.volume}
				</td>
				<td>
					${fns:getDictLabel(jsPlatOfflineChangeLog.type, 'changeType', '')}
				</td>
				<td>
					${jsPlatOfflineChangeLog.fee}
				</td>
				<td>
					${jsPlatOfflineChangeLog.otherUserId}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineChangeLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatOfflineChangeLog:edit"><td>
    				<a href="${ctx}/plat/jsPlatOfflineChangeLog/form?id=${jsPlatOfflineChangeLog.id}">修改</a>
					<a href="${ctx}/plat/jsPlatOfflineChangeLog/delete?id=${jsPlatOfflineChangeLog.id}" onclick="return confirmx('确认要删除该C2C转账记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>