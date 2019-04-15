<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>超级钱包配置管理</title>
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
		<li class="active"><a href="${ctx}/plat/superCoinVolumeConf/">超级钱包配置列表</a></li>
		<shiro:hasPermission name="plat:superCoinVolumeConf:edit"><li><a href="${ctx}/plat/superCoinVolumeConf/form">超级钱包配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="superCoinVolumeConf" action="${ctx}/plat/superCoinVolumeConf/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th>名称</th>
				<th>币种代号</th>
				<th>最小转入数量</th>
				<th>最小转出数量</th>
				<th>放大倍数(超级钱包)</th>
				<th>放大倍数(冻结)</th>
				<th>锁定周期（天）</th>
				<th>冻结天数</th>
				<th>违约金比例</th>
				<th>销毁总账户</th>
				<th>划转状态</th>
				<th>状态</th>
				<shiro:hasPermission name="plat:superCoinVolumeConf:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="superCoinVolumeConf">
			<tr>
				<td><a href="${ctx}/plat/superCoinVolumeConf/form?id=${superCoinVolumeConf.id}">
					${superCoinVolumeConf.id}
				</a></td>
				<td>
					${superCoinVolumeConf.name}
				</td>
				<td>
					${superCoinVolumeConf.coinSymbol}
				</td>
				<td>
					${superCoinVolumeConf.inMinVolume}
				</td>
				<td>
					${superCoinVolumeConf.outMinVolume}
				</td>
				<td>
					${superCoinVolumeConf.multiple}
				</td>
				<td>
					${superCoinVolumeConf.memberLockMultiple}
				</td>
				<td>
					${superCoinVolumeConf.lockCycle}
				</td>
				<td>
					${superCoinVolumeConf.frozenDay}
				</td>
				<td>
					${superCoinVolumeConf.breakRatio}
				</td>
				<td>
					${superCoinVolumeConf.destroyUserId}
				</td>
				<td>
						${fns:getDictLabel(superCoinVolumeConf.transferStatus, 'superTransferStatus', '')}
				</td>
				<td>
						${fns:getDictLabel(superCoinVolumeConf.status, 'superConfStatus', '')}
				</td>
				<shiro:hasPermission name="plat:superCoinVolumeConf:edit"><td>
    				<a href="${ctx}/plat/superCoinVolumeConf/form?id=${superCoinVolumeConf.id}">修改</a>
					<a href="${ctx}/plat/superCoinVolumeConf/delete?id=${superCoinVolumeConf.id}" onclick="return confirmx('确认要删除该超级钱包配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>