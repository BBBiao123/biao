<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台收入流水管理</title>
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
		<li class="active"><a href="${ctx}/market/mkCommonPlatIncomeLog/">平台收入流水列表</a></li>
		<shiro:hasPermission name="market:mkCommonPlatIncomeLog:edit"><li><a href="${ctx}/market/mkCommonPlatIncomeLog/form">平台收入流水添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkCommonPlatIncomeLog" action="${ctx}/market/mkCommonPlatIncomeLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>币种名称：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>开始时间：</label>
				<input name="beginBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeLog.beginBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeLog.endBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeLog.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeLog.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>mobile</th>
				<th>身份证</th>
				<th>真实姓名</th>
				<th>币种名称</th>
				<th>数量</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>状态</th>
				<th>创建时间</th>
				<th>成功或失败原因</th>
				<shiro:hasPermission name="market:mkCommonPlatIncomeLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkCommonPlatIncomeLog">
			<tr>
				<td><a href="${ctx}/market/mkCommonPlatIncomeLog/form?id=${mkCommonPlatIncomeLog.id}">
					${mkCommonPlatIncomeLog.userId}
				</a></td>
				<td>
					${mkCommonPlatIncomeLog.mail}
				</td>
				<td>
					${mkCommonPlatIncomeLog.mobile}
				</td>
				<td>
					${mkCommonPlatIncomeLog.idCard}
				</td>
				<td>
					${mkCommonPlatIncomeLog.realName}
				</td>
				<td>
					${mkCommonPlatIncomeLog.coinSymbol}
				</td>
				<td>
					${mkCommonPlatIncomeLog.volume}
				</td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeLog.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeLog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mkCommonPlatIncomeLog.status, 'mkTaskStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkCommonPlatIncomeLog.remark}
				</td>
				<shiro:hasPermission name="market:mkCommonPlatIncomeLog:edit"><td>
    				<a href="${ctx}/market/mkCommonPlatIncomeLog/form?id=${mkCommonPlatIncomeLog.id}">修改</a>
					<a href="${ctx}/market/mkCommonPlatIncomeLog/delete?id=${mkCommonPlatIncomeLog.id}" onclick="return confirmx('确认要删除该平台收入流水吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>