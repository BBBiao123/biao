<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员规则设置管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeMemberRule/">会员规则设置列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeMemberRule:edit"><li><a href="${ctx}/market/mk2PopularizeMemberRule/form">会员规则设置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeMemberRule" action="${ctx}/market/mk2PopularizeMemberRule/" method="post" class="breadcrumb form-search">
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
				<th>会员类型</th>
				<th>释放开关</th>
				<th>释放版本</th>
				<th>天周期开关</th>
				<th>周周期开关</th>
				<th>月周期开关</th>
				<th>年周期开关</th>
				<th>释放冻结类型</th>
				<th>会员总量</th>
				<th>已售出会员总量</th>
				<th>固定分红比例百分比</th>
				<th>号码归属地分红</th>
				<th>推荐分红</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mk2PopularizeMemberRule:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeMemberRule">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeMemberRule/form?id=${mk2PopularizeMemberRule.id}">
					${mk2PopularizeMemberRule.type}
				</a></td>
				<td>
					${mk2PopularizeMemberRule.releaseOpen}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseVersion}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseDay}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseWeek}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseMonth}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseYear}
				</td>
				<td>
					${mk2PopularizeMemberRule.releaseType}
				</td>
				<td>
					${mk2PopularizeMemberRule.totalMember}
				</td>
				<td>
					${mk2PopularizeMemberRule.soldMember}
				</td>
				<td>
					${mk2PopularizeMemberRule.bonusRatio}
				</td>
				<td>
					${mk2PopularizeMemberRule.phoneBonusRatio}
				</td>
				<td>
					${mk2PopularizeMemberRule.referBonusRatio}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMemberRule.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeMemberRule:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeMemberRule/form?id=${mk2PopularizeMemberRule.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeMemberRule/delete?id=${mk2PopularizeMemberRule.id}" onclick="return confirmx('确认要删除该会员规则设置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>