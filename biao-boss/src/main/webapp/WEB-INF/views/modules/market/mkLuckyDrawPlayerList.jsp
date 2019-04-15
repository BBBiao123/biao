<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>抽奖活动参与者管理</title>
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
		<li class="active"><a href="${ctx}/market/mkLuckyDrawPlayer/">抽奖活动参与者列表</a></li>
		<shiro:hasPermission name="market:mkLuckyDrawPlayer:edit"><li><a href="${ctx}/market/mkLuckyDrawPlayer/form">抽奖活动参与者添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkLuckyDrawPlayer" action="${ctx}/market/mkLuckyDrawPlayer/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>期数：</label>
				<form:input path="periods" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkLuckyPlayerStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>PlatUserId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户名称：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th>期数</th>
				<th>状态</th>
				<th>PlatUserId</th>
				<th>用户名称</th>
				<th>手机</th>
				<th>真实姓名</th>
				<th>币种代号</th>
				<th>参与时币种数量</th>
				<th>扣除费用</th>
				<th>说明</th>
				<th>开奖时间</th>
				<th>参与时间</th>
				<shiro:hasPermission name="market:mkLuckyDrawPlayer:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkLuckyDrawPlayer">
			<tr>
				<td><a href="${ctx}/market/mkLuckyDrawPlayer/form?id=${mkLuckyDrawPlayer.id}">
					${mkLuckyDrawPlayer.id}
				</a></td>
				<td>
					${mkLuckyDrawPlayer.periods}
				</td>
				<td>
					${fns:getDictLabel(mkLuckyDrawPlayer.status, 'mkLuckyPlayerStatus', '')}
				</td>
				<td>
					${mkLuckyDrawPlayer.userId}
				</td>
				<td>
					${mkLuckyDrawPlayer.mail}
				</td>
				<td>
					${mkLuckyDrawPlayer.mobile}
				</td>
				<td>
					${mkLuckyDrawPlayer.realName}
				</td>
				<td>
					${mkLuckyDrawPlayer.coinSymbol}
				</td>
				<td>
					${mkLuckyDrawPlayer.volume}
				</td>
				<td>
					${mkLuckyDrawPlayer.deductFee}
				</td>
				<td>
					${mkLuckyDrawPlayer.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkLuckyDrawPlayer.drawDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkLuckyDrawPlayer.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkLuckyDrawPlayer:edit"><td>
    				<a href="${ctx}/market/mkLuckyDrawPlayer/form?id=${mkLuckyDrawPlayer.id}">修改</a>
					<a href="${ctx}/market/mkLuckyDrawPlayer/delete?id=${mkLuckyDrawPlayer.id}" onclick="return confirmx('确认要删除该抽奖活动参与者吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>