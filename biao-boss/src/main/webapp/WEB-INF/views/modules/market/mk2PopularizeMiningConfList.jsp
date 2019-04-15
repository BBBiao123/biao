<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿规则管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeMiningConf/">挖矿规则列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeMiningConf:edit"><li><a href="${ctx}/market/mk2PopularizeMiningConf/form">挖矿规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeMiningConf" action="${ctx}/market/mk2PopularizeMiningConf/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_mining_conf_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>类型</th>
				<th>挖矿总量</th>
				<th>已挖总量</th>
				<th>已挖显示倍数</th>
				<%--<th>已挖显示倍数</th>--%>
				<th>每天挖矿量</th>
				<th>参于挖矿持有量</th>
				<th>挖矿基数</th>
				<th>挖矿基数倍数</th>
				<th>团队Leader最小持有量</th>
				<th>币种名称</th>
				<th>状态</th>
				<th>创建时间</th>
				<th>描述</th>
				<shiro:hasPermission name="market:mk2PopularizeMiningConf:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeMiningConf">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeMiningConf/form?id=${mk2PopularizeMiningConf.id}">
					${fns:getDictLabel(mk2PopularizeMiningConf.type, 'mk2_popularize_mining_conf_type', '')}
				</a></td>
				<td>
					${mk2PopularizeMiningConf.totalVolume}
				</td>
				<td>
					${mk2PopularizeMiningConf.grantVolume}
				</td>
				<td>
					${mk2PopularizeMiningConf.showMultiple}
				</td>
				<%--<td>--%>
					<%--${mk2PopularizeMiningConf.delayShowMultiple}--%>
				<%--</td>--%>
				<td>
					${mk2PopularizeMiningConf.perVolume}
				</td>
				<td>
					${mk2PopularizeMiningConf.greaterVolume}
				</td>
				<td>
					${mk2PopularizeMiningConf.baseVolume}
				</td>
				<td>
						${mk2PopularizeMiningConf.baseMultiple}
				</td>
				<td>
						${mk2PopularizeMiningConf.leaderGreaterVolume}
				</td>
				<td>
					${mk2PopularizeMiningConf.coinSymbol}
				</td>
				<td>
						${fns:getDictLabel(mk2PopularizeMiningConf.status, 'mk2_register_conf_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMiningConf.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mk2PopularizeMiningConf.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeMiningConf:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeMiningConf/form?id=${mk2PopularizeMiningConf.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeMiningConf/delete?id=${mk2PopularizeMiningConf.id}" onclick="return confirmx('确认要删除该挖矿规则吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>