<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易授权管理</title>
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
		<li class="active"><a href="${ctx}/market/mkSysUserExPair/">自动交易授权列表</a></li>
		<shiro:hasPermission name="market:mkSysUserExPair:edit"><li><a href="${ctx}/market/mkSysUserExPair/form">自动交易授权添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkSysUserExPair" action="${ctx}/market/mkSysUserExPair/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<%--<li><label>主键ID：</label>--%>
				<%--<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>用户名称：</label>
				<form:input path="sysUserName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>主交易区：</label>
				<form:input path="coinMainSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>被交易币种：</label>
				<form:input path="coinOtherSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>营销用户ID</th>
				<th>用户名称</th>
				<th>前端账户邮箱</th>
				<th>前端账户手机</th>
				<th>前端账户真实姓名</th>
				<th>主交易区</th>
				<th>被交易币种</th>
				<th>说明</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkSysUserExPair:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkSysUserExPair">
			<tr>
				<td><a href="${ctx}/market/mkSysUserExPair/form?id=${mkSysUserExPair.id}">
					${mkSysUserExPair.sysUserId}
				</a></td>
				<td>
						${mkSysUserExPair.sysUserName}
				</td>
				<td>
						${mkSysUserExPair.mail}
				</td>
				<td>
						${mkSysUserExPair.mobile}
				</td>
				<td>
						${mkSysUserExPair.realName}
				</td>
				<td>
					${mkSysUserExPair.coinMainSymbol}
				</td>
				<td>
					${mkSysUserExPair.coinOtherSymbol}
				</td>
				<td>
					${mkSysUserExPair.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkSysUserExPair.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkSysUserExPair.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkSysUserExPair:edit"><td>
    				<a href="${ctx}/market/mkSysUserExPair/form?id=${mkSysUserExPair.id}">修改</a>
					<a href="${ctx}/market/mkSysUserExPair/delete?id=${mkSysUserExPair.id}" onclick="return confirmx('确认要删除该营销用户币币对吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>