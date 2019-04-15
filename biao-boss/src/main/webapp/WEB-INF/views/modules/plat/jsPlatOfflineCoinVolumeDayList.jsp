<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c银商和广告商对账管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatOfflineCoinVolumeDay/">c2c银商和广告商对账列表</a></li>
		<shiro:hasPermission name="plat:jsPlatOfflineCoinVolumeDay:edit"><li><a href="${ctx}/plat/jsPlatOfflineCoinVolumeDay/form">c2c银商和广告商对账添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatOfflineCoinVolumeDay" action="${ctx}/plat/jsPlatOfflineCoinVolumeDay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户tag：</label>
				<form:select path="tag" htmlEscape="false" maxlength="64" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('user_tag_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>统计时间：</label>
				<input name="countDay" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${jsPlatOfflineCoinVolumeDay.countDay}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户id</th>
				<th>手机号</th>
				<th>邮箱</th>
				<th>用户tag标签</th>
				<th>统计时间</th>
				<th>收入金额</th>
				<th>支出金额</th>
				<th>余额</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatOfflineCoinVolumeDay">
			<tr>
				<td>
					${jsPlatOfflineCoinVolumeDay.userId}
				</td>
				<td>
					${jsPlatOfflineCoinVolumeDay.mobile}
				</td>
				<td>
					${jsPlatOfflineCoinVolumeDay.mail}
				</td>
				<td>
					${fns:getDictLabel(jsPlatOfflineCoinVolumeDay.tag, 'user_tag_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineCoinVolumeDay.countDay}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatOfflineCoinVolumeDay.buyTotal}
				</td>
				<td>
					${jsPlatOfflineCoinVolumeDay.sellTotal}
				</td>
				<td>
					${jsPlatOfflineCoinVolumeDay.surplusTotal}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineCoinVolumeDay.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>