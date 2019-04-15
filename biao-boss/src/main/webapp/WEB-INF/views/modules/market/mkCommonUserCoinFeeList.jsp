<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台收益流水管理</title>
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
		<li class="active"><a href="${ctx}/market/mkCommonUserCoinFee/">平台收益流水列表</a></li>
		<shiro:hasPermission name="market:mkCommonUserCoinFee:edit"><li><a href="${ctx}/market/mkCommonUserCoinFee/form">平台收益流水添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkCommonUserCoinFee" action="${ctx}/market/mkCommonUserCoinFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="width: 120px;">统计开始时间：</label>
				<input name="beginBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonUserCoinFee.beginBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonUserCoinFee.endBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label style="width: 120px;">统计结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonUserCoinFee.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonUserCoinFee.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>身份证</th>
				<th>真实姓名</th>
				<th>币种名称</th>
				<th>手续费量</th>
				<th>兑换USDT量</th>
				<th>统计开始时间</th>
				<th>统计结束时间</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkCommonUserCoinFee:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkCommonUserCoinFee">
			<tr>
				<td><a href="${ctx}/market/mkCommonUserCoinFee/form?id=${mkCommonUserCoinFee.id}">
					${mkCommonUserCoinFee.userId}
				</a></td>
				<td>
					${mkCommonUserCoinFee.mail}
				</td>
				<td>
					${mkCommonUserCoinFee.mobile}
				</td>
				<td>
					${mkCommonUserCoinFee.idCard}
				</td>
				<td>
					${mkCommonUserCoinFee.realName}
				</td>
				<td>
					${mkCommonUserCoinFee.coinSymbol}
				</td>
				<td>
					${mkCommonUserCoinFee.volume}
				</td>
				<td>
					${mkCommonUserCoinFee.exUsdtVol}
				</td>
				<td>
					<fmt:formatDate value="${mkCommonUserCoinFee.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkCommonUserCoinFee.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkCommonUserCoinFee.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkCommonUserCoinFee:edit"><td>
    				<a href="${ctx}/market/mkCommonUserCoinFee/form?id=${mkCommonUserCoinFee.id}">修改</a>
					<a href="${ctx}/market/mkCommonUserCoinFee/delete?id=${mkCommonUserCoinFee.id}" onclick="return confirmx('确认要删除该平台收益流水吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>