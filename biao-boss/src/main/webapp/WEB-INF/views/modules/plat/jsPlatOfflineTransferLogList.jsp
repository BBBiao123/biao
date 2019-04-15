<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>bb to c2c转账日志管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatOfflineTransferLog/">bb to c2c转账日志列表</a></li>
		<shiro:hasPermission name="plat:jsPlatOfflineTransferLog:edit"><li><a href="${ctx}/plat/jsPlatOfflineTransferLog/form">bb to c2c转账日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatOfflineTransferLog" action="${ctx}/plat/jsPlatOfflineTransferLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>手机 邮箱：</label>
				<form:input path="user.name" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</li>
			<li><label>用户id：</label>
				<form:input path="user.id" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</li>
			<li><label>币id：</label>
				<form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>转账类型：</label>
				<form:select path="type" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:option value="0" label="转入"/>
					<form:option value="1" label="转出"/>
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
				<th>用户真实名称</th>
				<th>币id</th>
				<th>币符号</th>
				<th>volume</th>
				<th>转账类型</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>create_by</th>
				<th>update_by</th>
				<shiro:hasPermission name="plat:jsPlatOfflineTransferLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatOfflineTransferLog">
			<tr>
			    <td>
					${jsPlatOfflineTransferLog.user.id}
				</td>
				<td>
					${jsPlatOfflineTransferLog.user.name}
				</td>
				<td>
					${jsPlatOfflineTransferLog.coinId}
				</td>
				<td>
					${jsPlatOfflineTransferLog.coinSymbol}
				</td>
				<td>
					${jsPlatOfflineTransferLog.volume}
				</td>
				<td>
					<c:choose>
					   <c:when test="${jsPlatOfflineTransferLog.type==0}">转入</c:when>
					   <c:when test="${jsPlatOfflineTransferLog.type==1}">转出</c:when>
					   <c:otherwise>
					   </c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineTransferLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineTransferLog.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatOfflineTransferLog.createBy.id}
				</td>
				<td>
					${jsPlatOfflineTransferLog.updateBy.id}
				</td>
				<shiro:hasPermission name="plat:jsPlatOfflineTransferLog:edit"><td>
    				<a href="${ctx}/plat/jsPlatOfflineTransferLog/form?id=${jsPlatOfflineTransferLog.id}">修改</a>
					<a href="${ctx}/plat/jsPlatOfflineTransferLog/delete?id=${jsPlatOfflineTransferLog.id}" onclick="return confirmx('确认要删除该bb to c2c转账日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>