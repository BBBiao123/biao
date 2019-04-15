<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力撞奖执行记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRelayTaskRecord/">接力撞奖执行记录列表</a></li>
		<shiro:hasPermission name="market:mkRelayTaskRecord:edit"><li><a href="${ctx}/market/mkRelayTaskRecord/form">接力撞奖执行记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayTaskRecord" action="${ctx}/market/mkRelayTaskRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键ID：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayRecordStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>执行时间：</label>
				<input name="beginBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayTaskRecord.beginBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayTaskRecord.endBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主键ID</th>
				<th>状态</th>
				<th>执行时间</th>
				<th>新增人数</th>
				<th>新增奖金数</th>
				<th>奖池总量</th>
				<th>获奖人数</th>
				<th>获奖数量</th>
				<th>币种</th>
				<th>说明</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkRelayTaskRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayTaskRecord">
			<tr>
				<td><a href="${ctx}/market/mkRelayTaskRecord/form?id=${mkRelayTaskRecord.id}">
					${mkRelayTaskRecord.id}
				</a></td>
				<td>
					${fns:getDictLabel(mkRelayTaskRecord.status, 'mkRelayRecordStatus', '')}

				</td>
				<td>
					<fmt:formatDate value="${mkRelayTaskRecord.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkRelayTaskRecord.increaseNumber}
				</td>
				<td>
					${mkRelayTaskRecord.increaseVolume}
				</td>
				<td>
					${mkRelayTaskRecord.poolVolume}
				</td>
				<td>
					${mkRelayTaskRecord.prizeNumber}
				</td>
				<td>
					${mkRelayTaskRecord.prizeVolume}
				</td>
				<td>
					${mkRelayTaskRecord.coinSymbol}
				</td>
				<td>
					${mkRelayTaskRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayTaskRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRelayTaskRecord:edit"><td>
    				<a href="${ctx}/market/mkRelayTaskRecord/form?id=${mkRelayTaskRecord.id}">修改</a>
					<a href="${ctx}/market/mkRelayTaskRecord/delete?id=${mkRelayTaskRecord.id}" onclick="return confirmx('确认要删除该接力撞奖执行记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>