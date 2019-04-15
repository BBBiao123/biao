<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>抽奖活动规则管理</title>
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
		<li class="active"><a href="${ctx}/market/mkLuckyDrawConfig/">抽奖活动规则列表</a></li>
		<%--<shiro:hasPermission name="market:mkLuckyDrawConfig:edit"><li><a href="${ctx}/market/mkLuckyDrawConfig/form">抽奖活动规则添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mkLuckyDrawConfig" action="${ctx}/market/mkLuckyDrawConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>活动名称</th>
				<th>期数</th>
				<th>状态</th>
				<th>奖金总量</th>
				<th>起步奖金</th>
				<th>新增奖金</th>
				<th>币种代号</th>
				<th>已发放数量</th>
				<th>当前奖池总量</th>
				<th>用户最低参与数量</th>
				<th>扣除手续费</th>
				<th>当前参与人数</th>
				<th>当前收纳手续费</th>
				<th>说明</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkLuckyDrawConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkLuckyDrawConfig">
			<tr>
				<td><a href="${ctx}/market/mkLuckyDrawConfig/form?id=${mkLuckyDrawConfig.id}">
					${mkLuckyDrawConfig.name}
				</a></td>
				<td>
					${mkLuckyDrawConfig.periods}
				</td>
				<td>
					${fns:getDictLabel(mkLuckyDrawConfig.status, 'mkLuckyDrawStatus', '')}
				</td>
				<td>
					${mkLuckyDrawConfig.volume}
				</td>
				<td>
					${mkLuckyDrawConfig.startVolume}
				</td>
				<td>
					${mkLuckyDrawConfig.stepAddVolume}
				</td>
				<td>
					${mkLuckyDrawConfig.coinSymbol}
				</td>
				<td>
					${mkLuckyDrawConfig.grantVolume}
				</td>
				<td>
					${mkLuckyDrawConfig.poolVolume}
				</td>
				<td>
					${mkLuckyDrawConfig.minVolume}
				</td>
				<td>
					${mkLuckyDrawConfig.deductFee}
				</td>
				<td>
					${mkLuckyDrawConfig.playerNumber}
				</td>
				<td>
					${mkLuckyDrawConfig.fee}
				</td>
				<td>
					${mkLuckyDrawConfig.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkLuckyDrawConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkLuckyDrawConfig:edit"><td>
					<c:if test="${mkLuckyDrawConfig.status eq '0' || mkLuckyDrawConfig.status eq '3'}">
						<a href="${ctx}/market/mkLuckyDrawConfig/form?id=${mkLuckyDrawConfig.id}">修改</a>
					</c:if>
					<c:if test="${mkLuckyDrawConfig.status eq '0' || mkLuckyDrawConfig.status eq '3'}">
						<a href="${ctx}/market/mkLuckyDrawConfig/start?id=${mkLuckyDrawConfig.id}" onclick="return confirmx('确认要开启吗？', this.href)">开启</a>
					</c:if>
					<c:if test="${mkLuckyDrawConfig.status eq '1'}">
						<a href="${ctx}/market/mkLuckyDrawConfig/end?id=${mkLuckyDrawConfig.id}" onclick="return confirmx('确认要结束吗？', this.href)">结束</a>
					</c:if>
					<c:if test="${mkLuckyDrawConfig.status eq '2'}">
						<a href="${ctx}/market/mkLuckyDrawConfig/draw?id=${mkLuckyDrawConfig.id}" onclick="return confirmx('确认要开奖吗？', this.href)">开奖</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>