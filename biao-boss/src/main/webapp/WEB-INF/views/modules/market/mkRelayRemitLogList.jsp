<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力撞奖打款日志管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRelayRemitLog/">接力撞奖打款日志列表</a></li>
		<shiro:hasPermission name="market:mkRelayRemitLog:edit"><li><a href="${ctx}/market/mkRelayRemitLog/form">接力撞奖打款日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayRemitLog" action="${ctx}/market/mkRelayRemitLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键ID：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户类型：</label>
				<form:select path="userType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayUserType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayRemitLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayRemitLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>邮箱</th>
				<th>手机</th>
				<th>真是姓名</th>
				<th>币种</th>
				<th>数量</th>
				<th>用户类型</th>
				<th>推荐人ID</th>
				<th>推荐人邮箱</th>
				<th>推荐人手机</th>
				<th>是否打款</th>
				<th>备注</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkRelayRemitLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayRemitLog">
			<tr>
				<td><a href="${ctx}/market/mkRelayRemitLog/form?id=${mkRelayRemitLog.id}">
					${mkRelayRemitLog.id}
				</a></td>
				<td>
					${mkRelayRemitLog.mail}
				</td>
				<td>
					${mkRelayRemitLog.mobile}
				</td>
				<td>
					${mkRelayRemitLog.realName}
				</td>
				<td>
					${mkRelayRemitLog.coinSymbol}
				</td>
				<td>
					${mkRelayRemitLog.volume}
				</td>
				<td>
					${fns:getDictLabel(mkRelayRemitLog.userType, 'mkRelayUserType', '')}
				</td>
				<td>
					${mkRelayRemitLog.refereeId}
				</td>
				<td>
						${mkRelayRemitLog.referMail}
				</td>
				<td>
						${mkRelayRemitLog.referMobile}
				</td>
				<td>
						${fns:getDictLabel(mkRelayRemitLog.isRemit, 'mkRelayIsRemit', '')}
				</td>
				<td>
					${mkRelayRemitLog.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayRemitLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRelayRemitLog:edit"><td>
    				<a href="${ctx}/market/mkRelayRemitLog/form?id=${mkRelayRemitLog.id}">修改</a>
					<a href="${ctx}/market/mkRelayRemitLog/delete?id=${mkRelayRemitLog.id}" onclick="return confirmx('确认要删除该接力撞奖打款日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>