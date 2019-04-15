<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c广告管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            if ($("#messageBox")) {
                $("#messageBox").css({display: 'block'})
            }
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
		<li class="active"><a href="${ctx}/plat/offlineOrder/">c2c广告列表</a></li>
		<%--<shiro:hasPermission name="plat:offlineOrder:edit"><li><a href="${ctx}/plat/offlineOrder/form">c2c广告添加</a></li></shiro:hasPermission>--%>
		<shiro:hasPermission name="plat:offlineOrder:edit"><li><a href="${ctx}/plat/offlineOrder/batchAction">c2c广告批量操作</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="offlineOrder" action="${ctx}/plat/offlineOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>姓名：</label>
				<form:input path="realName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种id：</label>
				<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th>用户id</th>
				<th>姓名</th>
				<th>挂单币种标识</th>
				<th>挂单数量</th>
				<th>锁定数量</th>
				<th>成交数量</th>
				<th>价格</th>
				<th>总价</th>
				<th>手续费</th>
				<th>状态</th>
				<th>广告类型</th>
				<th>remarks</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:offlineOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="offlineOrder">
			<tr>
				<td><a href="${ctx}/plat/offlineOrder/form?id=${offlineOrder.id}">
					${offlineOrder.id}
				</a></td>
				<td>
					${offlineOrder.userId}
				</td>
				<td>
					${offlineOrder.realName}
				</td>
				<td>
					${offlineOrder.symbol}
				</td>
				<td>
					${offlineOrder.volume}
				</td>
				<td>
					${offlineOrder.lockVolume}
				</td>
				<td>
					${offlineOrder.successVolume}
				</td>
				<td>
					${offlineOrder.price}
				</td>
				<td>
					${offlineOrder.totalPrice}
				</td>
				<td>
					${offlineOrder.feeVolume}
				</td>
				<td>
					${offlineOrder.status}
				</td>
				<td>
					${offlineOrder.exType}
				</td>
				<td>
					${offlineOrder.remarks}
				</td>
				<td>
					<fmt:formatDate value="${offlineOrder.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:offlineOrder:edit"><td>
    				<a href="${ctx}/plat/offlineOrder/doCancel?id=${offlineOrder.id}">撤销</a>
					<%--<a href="${ctx}/plat/offlineOrder/delete?id=${offlineOrder.id}" onclick="return confirmx('确认要删除该c2c广告吗？', this.href)">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>