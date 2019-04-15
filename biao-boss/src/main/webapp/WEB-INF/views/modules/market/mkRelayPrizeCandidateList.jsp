<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力撞奖名单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#btnImport").click(function(){
                $.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true},
                    bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
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
<div id="importBox" class="hide">
	<form id="importForm" action="${ctx}/market/mkRelayPrizeCandidate/opt" method="post" enctype="multipart/form-data"
		  class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
		<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
		<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
		<a href="${ctx}/market/mkRelayPrizeCandidate/import/template">下载模板</a>
	</form>
</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/market/mkRelayPrizeCandidate/">接力撞奖名单列表</a></li>
		<shiro:hasPermission name="market:mkRelayPrizeCandidate:edit"><li><a href="${ctx}/market/mkRelayPrizeCandidate/form">接力撞奖名单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayPrizeCandidate" action="${ctx}/market/mkRelayPrizeCandidate/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayCandidateStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>参与时间：</label>
				<input name="beginAchieveDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayPrizeCandidate.beginAchieveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endAchieveDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayPrizeCandidate.endAchieveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>是否中奖：</label>
				<form:select path="isPrize" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayIsPrize')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnImport" class="btn btn-primary" type="button" value="导入"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>状态</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>真实姓名</th>
				<th>币种代号</th>
				<th>持有数量</th>
				<th>参与时间</th>
				<th>是否中奖</th>
				<th>奖金</th>
				<th>失效时间</th>
				<th>说明</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkRelayPrizeCandidate:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayPrizeCandidate">
			<tr>
				<td><a href="${ctx}/market/mkRelayPrizeCandidate/form?id=${mkRelayPrizeCandidate.id}">
					${mkRelayPrizeCandidate.id}
				</a></td>
				<td>
					${fns:getDictLabel(mkRelayPrizeCandidate.status, 'mkRelayCandidateStatus', '')}
				</td>
				<td>
					${mkRelayPrizeCandidate.mail}
				</td>
				<td>
					${mkRelayPrizeCandidate.mobile}
				</td>
				<td>
					${mkRelayPrizeCandidate.realName}
				</td>
				<td>
					${mkRelayPrizeCandidate.coinSymbol}
				</td>
				<td>
					${mkRelayPrizeCandidate.volume}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayPrizeCandidate.achieveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mkRelayPrizeCandidate.isPrize, 'mkRelayIsPrize', '')}
				</td>
				<td>
					${mkRelayPrizeCandidate.prizeVolume}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayPrizeCandidate.lostTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkRelayPrizeCandidate.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayPrizeCandidate.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkRelayPrizeCandidate.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRelayPrizeCandidate:edit"><td>
    				<a href="${ctx}/market/mkRelayPrizeCandidate/form?id=${mkRelayPrizeCandidate.id}">修改</a>
					<a href="${ctx}/market/mkRelayPrizeCandidate/delete?id=${mkRelayPrizeCandidate.id}" onclick="return confirmx('确认要删除该接力撞奖名单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>