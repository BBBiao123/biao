<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种资产风控管理管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/">币种资产风控管理列表</a></li>
		<shiro:hasPermission name="plat:jsPlatCoinVolumeRiskMgt:edit"><li><a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/form">币种资产风控管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatCoinVolumeRiskMgt" action="${ctx}/plat/jsPlatCoinVolumeRiskMgt/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种id：</label>
				<form:select path="coinId" class="input-medium">
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
				<th>主键id</th>
				<th>币种代号</th>
				<th>风控阀值</th>
				<th>备注</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:jsPlatCoinVolumeRiskMgt:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatCoinVolumeRiskMgt">
			<tr>
				<td><a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/form?id=${jsPlatCoinVolumeRiskMgt.id}">
					${jsPlatCoinVolumeRiskMgt.id}
				</a></td>
				<td>
					${jsPlatCoinVolumeRiskMgt.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${jsPlatCoinVolumeRiskMgt.volume}" pattern="#.####" minFractionDigits="4" > </fmt:formatNumber>
				</td>
				<td>
					${jsPlatCoinVolumeRiskMgt.remark}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatCoinVolumeRiskMgt.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatCoinVolumeRiskMgt.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatCoinVolumeRiskMgt:edit"><td>
    				<a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/form?id=${jsPlatCoinVolumeRiskMgt.id}">修改</a>
					<a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/delete?id=${jsPlatCoinVolumeRiskMgt.id}" onclick="return confirmx('确认要删除该币种资产风控管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>