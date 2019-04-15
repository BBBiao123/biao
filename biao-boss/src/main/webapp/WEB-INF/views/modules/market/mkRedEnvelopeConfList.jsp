<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>红包配置管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRedEnvelopeConf/">红包配置列表</a></li>
		<%--<shiro:hasPermission name="market:mkRedEnvelopeConf:edit"><li><a href="${ctx}/market/mkRedEnvelopeConf/form">红包配置添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRedEnvelopeConf" action="${ctx}/market/mkRedEnvelopeConf/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>币种代号</th>
				<th>金额下限</th>
				<th>金额上限</th>
				<th>个数下限</th>
				<th>个数上限</th>
				<%--<th>手续费</th>--%>
				<th>手气金额下限</th>
				<th>手气金额上限</th>
				<th>价格精度</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkRedEnvelopeConf:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRedEnvelopeConf">
			<tr>
				<td><a href="${ctx}/market/mkRedEnvelopeConf/form?id=${mkRedEnvelopeConf.id}">
					${mkRedEnvelopeConf.name}
				</a></td>
				<td>
					${mkRedEnvelopeConf.coinSymbol}
				</td>
				<td>
					${mkRedEnvelopeConf.singleLowerVolume}
				</td>
				<td>
					${mkRedEnvelopeConf.singleHigherVolume}
				</td>
				<td>
					${mkRedEnvelopeConf.lowerNumber}
				</td>
				<td>
					${mkRedEnvelopeConf.higherNumber}
				</td>
				<%--<td>--%>
					<%--${mkRedEnvelopeConf.fee}--%>
				<%--</td>--%>
				<td>
					${mkRedEnvelopeConf.luckyLowerVolume}
				</td>
				<td>
					${mkRedEnvelopeConf.luckyHigherVolume}
				</td>
				<td>
					${mkRedEnvelopeConf.pointVolume}
				</td>
				<td>
					${fns:getDictLabel(mkRedEnvelopeConf.status, 'mkRelayConfigStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${mkRedEnvelopeConf.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRedEnvelopeConf:edit"><td>
    				<a href="${ctx}/market/mkRedEnvelopeConf/form?id=${mkRedEnvelopeConf.id}">修改</a>
					<a href="${ctx}/market/mkRedEnvelopeConf/delete?id=${mkRedEnvelopeConf.id}" onclick="return confirmx('确认要删除该红包配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>