<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种管理</title>
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
		<li class="active"><a href="${ctx}/plat/coin/">币种列表</a></li>
		<shiro:hasPermission name="plat:coin:edit"><li><a href="${ctx}/plat/coin/form">币种添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="coin" action="${ctx}/plat/coin/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易代号：</label>
				<form:input path="name" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>交易代号</th>
				<th>充值提现状态</th>
				<th>状态</th>
				<th>一次提现最低数量</th>
				<th>一次提现最大数量</th>
				<th>一天最大提现额度</th>
				<th>提现手续费类型</th>
				<th>提现手续费</th>
				<th>基于何种代币</th>
				<!-- <th>父类</th> -->
				<shiro:hasPermission name="plat:coin:edit"><th style="width : 70px;">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="coin">
			<tr>
				<td><a href="${ctx}/plat/coin/form?id=${coin.id}">
					${coin.name}
				</a></td>
				<td>
					${fns:getDictLabel(coin.tokenStatus, 'tokenStatus', '')}
				</td>
				<td>
					${fns:getDictLabel(coin.status, 'coinStatus', '')}
				</td>
				<td>
					${coin.withdrawMinVolume}
				</td>
				<td>
					${coin.withdrawMaxVolume}
				</td>
				<td>
					${coin.withdrawDayMaxVolume}
				</td>
				<td>
					${fns:getDictLabel(coin.withdrawFeeType, 'withdrawFeeType', '')}
				</td>
				<td>
					${coin.withdrawFee}
				</td>
				<td>
					${fns:getDictLabel(coin.coinType, 'coinType', '')}
				</td>
				<shiro:hasPermission name="plat:coin:edit"><td>
    				<a href="${ctx}/plat/coin/form?id=${coin.id}">修改</a>
					<a href="${ctx}/plat/coin/delete?id=${coin.id}" onclick="return confirmx('确认要删除该币种吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>