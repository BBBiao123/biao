<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员分红日志管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeBonusMemberLog/">会员分红日志列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeBonusMemberLog:edit"><li><a href="${ctx}/market/mk2PopularizeBonusMemberLog/form">会员分红日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeBonusMemberLog" action="${ctx}/market/mk2PopularizeBonusMemberLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_member_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>城市：</label>
				<form:input path="areaName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="width: 130px;">分红开始时间：</label>
				<input name="beginBonusDateBegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusMemberLog.beginBonusDateBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBonusDateBegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusMemberLog.endBonusDateBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label style="width: 130px;">分红结束时间：</label>
				<input name="beginBonusDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusMemberLog.beginBonusDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBonusDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusMemberLog.endBonusDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>类型</th>
				<th>用户ID</th>
				<th>邮箱</th>
				<th>手机号</th>
				<th>分红币种</th>
				<th>分红数量</th>
				<th>城市</th>
				<th>参与占比量</th>
				<th>参与占比总量</th>
				<th>分红开始时间</th>
				<th>分红结束时间</th>
				<%--<th>创建时间</th>--%>
				<th>描述</th>
				<shiro:hasPermission name="market:mk2PopularizeBonusMemberLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeBonusMemberLog">
			<tr>
				<td>
					${fns:getDictLabel(mk2PopularizeBonusMemberLog.type, 'mk2_popularize_member_type', '')}
				</td>
				<td>
					<a href="${ctx}/market/mk2PopularizeBonusMemberLog/form?id=${mk2PopularizeBonusMemberLog.id}">
					${mk2PopularizeBonusMemberLog.userId}
					</a>
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.mail}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.mobile}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.coinSymbol}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.incomeVolume}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.areaName}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.joinVolume}
				</td>
				<td>
					${mk2PopularizeBonusMemberLog.joinTotalVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusMemberLog.bonusDateBegin}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusMemberLog.bonusDateEnd}" pattern="yyyy-MM-dd"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${mk2PopularizeBonusMemberLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<td>
					${mk2PopularizeBonusMemberLog.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeBonusMemberLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeBonusMemberLog/form?id=${mk2PopularizeBonusMemberLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeBonusMemberLog/delete?id=${mk2PopularizeBonusMemberLog.id}" onclick="return confirmx('确认要删除该会员分红日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>