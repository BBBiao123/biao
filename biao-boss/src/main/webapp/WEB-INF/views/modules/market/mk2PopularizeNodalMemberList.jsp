<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节点人管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeNodalMember/">节点人列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeNodalMember:edit"><li><a href="${ctx}/market/mk2PopularizeNodalMember/form">节点人添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeNodalMember" action="${ctx}/market/mk2PopularizeNodalMember/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>全部释放：</label>
				<form:select path="releaseOver" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeNodalMember.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeNodalMember.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>邮箱</th>
				<th>手机号</th>
				<th>冻结币种</th>
				<th>冻结数量</th>
				<th>已释放数量</th>
				<th>释放开始时间</th>
				<th>释放周期</th>
				<%--<th>周期释放数量</th>--%>
				<th>全部释放</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mk2PopularizeNodalMember:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeNodalMember">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeNodalMember/form?id=${mk2PopularizeNodalMember.id}">
					${mk2PopularizeNodalMember.userId}
				</a></td>
				<td>
					${mk2PopularizeNodalMember.mail}
				</td>
				<td>
					${mk2PopularizeNodalMember.mobile}
				</td>
				<td>
					${mk2PopularizeNodalMember.coinSymbol}
				</td>
				<td>
					${mk2PopularizeNodalMember.lockVolume}
				</td>
				<td>
					${mk2PopularizeNodalMember.releaseVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeNodalMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					 月
				</td>
				<%--<td>--%>
					<%--${mk2PopularizeNodalMember.releaseCycleRatio}--%>
				<%--</td>--%>
				<td>
					${fns:getDictLabel(mk2PopularizeNodalMember.releaseOver, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeNodalMember.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeNodalMember.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeNodalMember:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeNodalMember/form?id=${mk2PopularizeNodalMember.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeNodalMember/delete?id=${mk2PopularizeNodalMember.id}" onclick="return confirmx('确认要删除该节点人吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>