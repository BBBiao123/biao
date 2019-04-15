<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域合伙人售卖规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function NumberCheck(num) {
            var re=/^\d*\.{0,1}\d*$/;
            return re.exec(num) != null;
        };
		$(document).ready(function() {
            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });
            $("#batchSubmit").on('click', function () {
				if ($('#lockVolume').val() && !NumberCheck($('#lockVolume').val())) {
                    alertx("冻结数量请输入数字！");
                    return false;
				}
                if ($('#releaseCycleRatio').val() && !NumberCheck($('#releaseCycleRatio').val())) {
                    alertx("周期释放数量请输入数字！");
                    return false;
                }
                if (!$('#selectCoin').val() && !$('#lockVolume').val() && !$('#releaseCycleRatio').val()) {
                    alertx("请输入需要批量设置的属性！");
                    return false;
				}
				$('#batchSetForm').submit();
            });
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeAreaMember/">区域合伙人售卖规则列表</a></li>
		<%--<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><li><a href="${ctx}/market/mk2PopularizeAreaMember/form">区域合伙人售卖规则添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeAreaMember" action="${ctx}/market/mk2PopularizeAreaMember/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>城市：</label>
				<form:input path="areaName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>省份：</label>
				<form:input path="areaParaentName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>售出：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>全部释放：</label>
				<form:select path="releaseOver" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>

	<form:form id="batchSetForm" modelAttribute="mk2PopularizeAreaMember" action="${ctx}/market/mk2PopularizeAreaMember/batchset" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>冻结币种：</label>
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin" cssStyle="width: 176px;">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</li>
			<li><label>冻结数量：</label>
				<form:input path="lockVolume" htmlEscape="false" maxlength="64" class="input-medium required number"/>
			</li>
			<li><label style="width: 120px;">周期释放比例：</label>
				<form:input path="releaseCycleRatio" htmlEscape="false" maxlength="64" class="input-medium required number"/>
			</li>
			<li class="btns"><input id="batchSubmit" class="btn btn-primary" type="submit" value="批量设置"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>城市</th>
				<th>省份</th>
				<th>售出</th>
				<th>合伙人邮箱</th>
				<th>合伙人手机</th>
				<th>冻结数量</th>
				<th>冻结币种</th>
				<th>已释放数量</th>
				<th>释放开始时间</th>
				<th>释放周期</th>
				<th>周期释比例(百分比)</th>
				<th>全部释放</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeAreaMember">
			<tr>
				<%--<td><a href="${ctx}/market/mk2PopularizeAreaMember/form?id=${mk2PopularizeAreaMember.id}">--%>
					<%--${mk2PopularizeAreaMember.coinId}--%>
				<%--</a></td>--%>
				<td>
						${mk2PopularizeAreaMember.areaName}
				</td>
				<td>
						${mk2PopularizeAreaMember.areaParaentName}
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeAreaMember.status, 'yes_no', '')}
				</td>
				<td>
					${mk2PopularizeAreaMember.mail}
				</td>
				<td>
					${mk2PopularizeAreaMember.mobile}
				</td>
				<td>
						${mk2PopularizeAreaMember.lockVolume}
				</td>
				<td>
						${mk2PopularizeAreaMember.coinSymbol}
				</td>
				<td>
					${mk2PopularizeAreaMember.releaseVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeAreaMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<%--${fns:getDictLabel(mk2PopularizeAreaMember.releaseCycle, 'date_cycle', '')}--%>
					月
				</td>
				<td>
					${mk2PopularizeAreaMember.releaseCycleRatio}
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeAreaMember.releaseOver, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeAreaMember.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeAreaMember/form?id=${mk2PopularizeAreaMember.id}">修改</a>
					<%--<a href="${ctx}/market/mk2PopularizeAreaMember/delete?id=${mk2PopularizeAreaMember.id}" onclick="return confirmx('确认要删除该区域合伙人售卖规则吗？', this.href)">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>