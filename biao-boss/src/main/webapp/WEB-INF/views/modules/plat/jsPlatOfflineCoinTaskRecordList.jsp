<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>C2C币种价格更新记录管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatOfflineCoinTaskRecord/">C2C币种价格更新记录列表</a></li>
		<shiro:hasPermission name="plat:jsPlatOfflineCoinTaskRecord:edit"><li><a href="${ctx}/plat/jsPlatOfflineCoinTaskRecord/form">C2C币种价格更新记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatOfflineCoinTaskRecord" action="${ctx}/plat/jsPlatOfflineCoinTaskRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${jsPlatOfflineCoinTaskRecord.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${jsPlatOfflineCoinTaskRecord.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主键id</th>
				<th>状态</th>
				<th>币种标识</th>
				<th>最大价格（前）</th>
				<th>最低价格（前）</th>
				<th>价格增量</th>
				<th>备注</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:jsPlatOfflineCoinTaskRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatOfflineCoinTaskRecord">
			<tr>
				<td><a href="${ctx}/plat/jsPlatOfflineCoinTaskRecord/form?id=${jsPlatOfflineCoinTaskRecord.id}">
					${jsPlatOfflineCoinTaskRecord.id}
				</a></td>
				<td>
					${jsPlatOfflineCoinTaskRecord.status}
				</td>
				<td>
					${jsPlatOfflineCoinTaskRecord.symbol}
				</td>
				<td>
					${jsPlatOfflineCoinTaskRecord.beforeMaxPrice}
				</td>
				<td>
					${jsPlatOfflineCoinTaskRecord.beforeMinPrice}
				</td>
				<td>
					${jsPlatOfflineCoinTaskRecord.dayIncPrice}
				</td>
				<td>
					${jsPlatOfflineCoinTaskRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineCoinTaskRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatOfflineCoinTaskRecord:edit"><td>
    				<a href="${ctx}/plat/jsPlatOfflineCoinTaskRecord/form?id=${jsPlatOfflineCoinTaskRecord.id}">修改</a>
					<a href="${ctx}/plat/jsPlatOfflineCoinTaskRecord/delete?id=${jsPlatOfflineCoinTaskRecord.id}" onclick="return confirmx('确认要删除该C2C币种价格更新记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>