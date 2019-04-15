<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>冻结数量释放记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeReleaseLog/">冻结数量释放记录列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeReleaseLog:edit"><li><a href="${ctx}/market/mk2PopularizeReleaseLog/form">冻结数量释放记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeReleaseLog" action="${ctx}/market/mk2PopularizeReleaseLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label="无"/>
					<form:options items="${fns:getDictList('mk2_popularize_member_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>释放时间：</label>
				<input name="beginReleaseCycleDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeReleaseLog.beginReleaseCycleDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endReleaseCycleDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeReleaseLog.endReleaseCycleDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>区域名称：</label>
				<form:input path="areaName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型</th>
				<th>用户ID</th>
				<th>邮箱</th>
				<th>手机号</th>
				<th>冻结币种</th>
				<th>释放数量</th>
				<th>释放时间</th>
				<th>释放状态</th>
				<th>区域名称</th>
				<th>创建时间</th>
				<th>结果</th>
				<shiro:hasPermission name="market:mk2PopularizeReleaseLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeReleaseLog">
			<tr>
				<td>
					${fns:getDictLabel(mk2PopularizeReleaseLog.type, 'mk2_popularize_member_type', '')}
				</td>
				<td>
					<a href="${ctx}/market/mk2PopularizeReleaseLog/form?id=${mk2PopularizeReleaseLog.id}">
					${mk2PopularizeReleaseLog.userId}
					</a>
				</td>
				<td>
					${mk2PopularizeReleaseLog.mail}
				</td>
				<td>
					${mk2PopularizeReleaseLog.mobile}
				</td>
				<td>
					${mk2PopularizeReleaseLog.coinSymbol}
				</td>
				<td>
					${mk2PopularizeReleaseLog.releaseVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeReleaseLog.releaseCycleDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeReleaseLog.releaseStatus, 'mkTaskStatus', '')}
				</td>
				<td>
					${mk2PopularizeReleaseLog.areaName}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeReleaseLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mk2PopularizeReleaseLog.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeReleaseLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeReleaseLog/form?id=${mk2PopularizeReleaseLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeReleaseLog/delete?id=${mk2PopularizeReleaseLog.id}" onclick="return confirmx('确认要删除该冻结数量释放记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>