<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力撞奖配置管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRelayPrizeConfig/">接力撞奖配置列表</a></li>
		<shiro:hasPermission name="market:mkRelayPrizeConfig:edit"><li><a href="${ctx}/market/mkRelayPrizeConfig/form">接力撞奖配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayPrizeConfig" action="${ctx}/market/mkRelayPrizeConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>规则名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
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
				<th>规则名称</th>
				<th>状态</th>
				<th>奖金总量</th>
				<th>起步奖金</th>
				<th>新增奖金</th>
				<th>每天开始时间</th>
				<th>每天结束时间</th>
				<th>币种代号</th>
				<th>归集账户邮箱</th>
				<th>归集账户手机</th>
				<th>是否打款</th>
				<th>最低参与数量</th>
				<th>已发放数量</th>
				<th>当前奖池总量</th>
				<shiro:hasPermission name="market:mkRelayPrizeConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayPrizeConfig">
			<tr>
				<td><a href="${ctx}/market/mkRelayPrizeConfig/form?id=${mkRelayPrizeConfig.id}">
					${mkRelayPrizeConfig.id}
				</a></td>
				<td>
					${mkRelayPrizeConfig.name}
				</td>
				<td>
					${fns:getDictLabel(mkRelayPrizeConfig.status, 'mkRelayConfigStatus', '')}
				</td>
				<td>
					${mkRelayPrizeConfig.volume}
				</td>
				<td>
					${mkRelayPrizeConfig.startVolume}
				</td>
				<td>
					${mkRelayPrizeConfig.stepAddVolume}
				</td>
				<td>
					${mkRelayPrizeConfig.beginTime}
				</td>
				<td>
					${mkRelayPrizeConfig.endTime}
				</td>
				<td>
					${mkRelayPrizeConfig.coinSymbol}
				</td>
				<td>
					${mkRelayPrizeConfig.mail}
				</td>
				<td>
					${mkRelayPrizeConfig.mobile}
				</td>
				<td>
					${fns:getDictLabel(mkRelayPrizeConfig.isRemit, 'mkRelayIsRemit', '')}
				</td>
				<td>
					${mkRelayPrizeConfig.minVolume}
				</td>
				<td>
					${mkRelayPrizeConfig.grantVolume}
				</td>
				<td>
					${mkRelayPrizeConfig.curPoolVolume}
				</td>
				<shiro:hasPermission name="market:mkRelayPrizeConfig:edit"><td>
					<c:if test="${mkRelayPrizeConfig.status == '0'}">
						<a href="${ctx}/market/mkRelayPrizeConfig/form?id=${mkRelayPrizeConfig.id}">修改</a>
					</c:if>
					<a href="${ctx}/market/mkRelayPrizeConfig/stop?id=${mkRelayPrizeConfig.id}" onclick="return confirmx('确认要终止吗？', this.href)">终止</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>