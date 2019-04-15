<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资产统计</title>
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
		<li class="active"><a href="${ctx}/plat/coinVolumeStat/">资产统计列表</a></li>
		<shiro:hasPermission name="plat:coinVolumeStat:edit"><li><a href="${ctx}/plat/coinVolumeStat/form">币币资产添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>币种代号</th>
				<th>可用资产</th>
				<th>冻结资产</th>
				<th>总资产</th>
				<th>c2c总资产</th>
				<th>充值总资产</th>
				<th>提币总资产</th>
				<th>差额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${statList}" var="coinVolumeStat">
			<tr>
				<td>${coinVolumeStat.coinSymbol}</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.volume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.lockVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.totalVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.c2cVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.depositVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.depositVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${coinVolumeStat.withdrawVolume}" pattern="#0.00000000" />
				</td>

				<shiro:hasPermission name="plat:coinVolumeStat:edit"><td>
    				<%--<a href="${ctx}/plat/coinVolumeStat/form?id=${coinVolumeStat.id}">修改</a>--%>
					<%--<a href="${ctx}/plat/coinVolumeStat/delete?id=${coinVolumeStat.id}" onclick="return confirmx('确认要删除该币币资产吗？', this.href)">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>