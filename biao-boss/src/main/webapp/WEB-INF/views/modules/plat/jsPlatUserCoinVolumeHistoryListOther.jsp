<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手动转账管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatUserCoinVolumeHistoryOther/">手动转账列表</a></li>
		<shiro:hasPermission name="plat:jsPlatUserCoinVolumeHistoryOther:edit"><li><a href="${ctx}/plat/jsPlatUserCoinVolumeHistoryOther/form">手动转账添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatUserCoinVolumeHistory" action="${ctx}/plat/jsPlatUserCoinVolumeHistoryOther/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>account：</label>
				<form:input path="account" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>拨币场景：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('transfer_sceneType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>币种代号：</label>
			     <form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
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
				<th>account</th>
				<th>拨币场景</th>
				<th>币种代号</th>
				<th>币种数量</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>备注</th>
				<th>create_by</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatUserCoinVolumeHistory">
			<tr>
				<td>
					${jsPlatUserCoinVolumeHistory.account}
				</td>
				<td>
					${fns:getDictLabel(jsPlatUserCoinVolumeHistory.type, 'transfer_sceneType', '')}
				</td>
				<td>
					${jsPlatUserCoinVolumeHistory.coinSymbol}
				</td>
				<td>
					${jsPlatUserCoinVolumeHistory.volume}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserCoinVolumeHistory.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserCoinVolumeHistory.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatUserCoinVolumeHistory.remark}
				</td>
				<td>
					${jsPlatUserCoinVolumeHistory.createBy.id}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>