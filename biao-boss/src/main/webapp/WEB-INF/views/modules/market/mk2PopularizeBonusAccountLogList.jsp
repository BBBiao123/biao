<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台运营分红日志管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeBonusAccountLog/">平台运营分红日志列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeBonusAccountLog:edit"><li><a href="${ctx}/market/mk2PopularizeBonusAccountLog/form">平台运营分红日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeBonusAccountLog" action="${ctx}/market/mk2PopularizeBonusAccountLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusAccountLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusAccountLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>分红开始时间</th>
				<th>分红结束时间</th>
				<%--<th>分红币种ID</th>--%>
				<th>分红币种</th>
				<th>分红数量</th>
				<th>分红前数量</th>
				<th>创建时间</th>
				<th>描述</th>
				<shiro:hasPermission name="market:mk2PopularizeBonusAccountLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeBonusAccountLog">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeBonusAccountLog/form?id=${mk2PopularizeBonusAccountLog.id}">
					${mk2PopularizeBonusAccountLog.userId}
				</a></td>
				<td>
					${mk2PopularizeBonusAccountLog.mail}
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.mobile}
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.idCard}
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.realName}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusAccountLog.bonusDateBegin}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusAccountLog.bonusDateEnd}" pattern="yyyy-MM-dd"/>
				</td>
				<%--<td>--%>
					<%--${mk2PopularizeBonusAccountLog.coinId}--%>
				<%--</td>--%>
				<td>
					${mk2PopularizeBonusAccountLog.coinSymbol}
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.incomeVolume}
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.beforIncomeVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusAccountLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mk2PopularizeBonusAccountLog.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeBonusAccountLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeBonusAccountLog/form?id=${mk2PopularizeBonusAccountLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeBonusAccountLog/delete?id=${mk2PopularizeBonusAccountLog.id}" onclick="return confirmx('确认要删除该平台运营分红日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>