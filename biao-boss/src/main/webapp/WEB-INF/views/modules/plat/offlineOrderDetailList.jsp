<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c广告详情管理</title>
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
		<li class="active"><a href="${ctx}/plat/offlineOrderDetail/">c2c广告详情列表</a></li>
		<shiro:hasPermission name="plat:offlineOrderDetail:edit"><li><a href="${ctx}/plat/offlineOrderDetail/form">c2c广告详情添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="offlineOrderDetail" action="${ctx}/plat/offlineOrderDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>订单号：</label>
				<form:input path="subOrderId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>广告id</th>
				<th>订单号</th>
				<th>成交数量</th>
				<th>币种id</th>
				<th>币种标识</th>
				<th>价格</th>
				<th>总价</th>
				<th>用户id</th>
				<th>被交易用户id</th>
				<th>remarks</th>
				<th>状态</th>
				<th>radom_num</th>
				<th>更新时间</th>
				<th>update_by</th>
				<shiro:hasPermission name="plat:offlineOrderDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="offlineOrderDetail">
			<tr>
				<td><a href="${ctx}/plat/offlineOrderDetail/form?id=${offlineOrderDetail.id}">
					${offlineOrderDetail.orderId}
				</a></td>
				<td>
					${offlineOrderDetail.subOrderId}
				</td>
				<td>
					${offlineOrderDetail.volume}
				</td>
				<td>
					${offlineOrderDetail.coinId}
				</td>
				<td>
					${offlineOrderDetail.symbol}
				</td>
				<td>
					${offlineOrderDetail.price}
				</td>
				<td>
					${offlineOrderDetail.totalPrice}
				</td>
				<td>
					${offlineOrderDetail.userId}
				</td>
				<td>
					${offlineOrderDetail.askUserId}
				</td>
				<td>
					${offlineOrderDetail.remarks}
				</td>
				<td>
					${fns:getDictLabel(offlineOrderDetail.status, 'OfflineOrderDetailStatus', '')}
				</td>
				<td>
					${offlineOrderDetail.radomNum}
				</td>
				<td>
					<fmt:formatDate value="${offlineOrderDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${offlineOrderDetail.updateBy.id}
				</td>
				<shiro:hasPermission name="plat:offlineOrderDetail:edit"><td>
    				<a href="${ctx}/plat/offlineOrderDetail/form?id=${offlineOrderDetail.id}">修改</a>
					<a href="${ctx}/plat/offlineOrderDetail/delete?id=${offlineOrderDetail.id}" onclick="return confirmx('确认要删除该c2c广告详情吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>