<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>抽奖活动开奖记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mkLuckyDrawRecord/">抽奖活动开奖记录列表</a></li>
		<shiro:hasPermission name="market:mkLuckyDrawRecord:edit"><li><a href="${ctx}/market/mkLuckyDrawRecord/form">抽奖活动开奖记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkLuckyDrawRecord" action="${ctx}/market/mkLuckyDrawRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键ID：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>期数：</label>
				<form:input path="periods" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>中奖人id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>中奖人邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>中奖人手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkLuckyDrawRecord.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkLuckyDrawRecord.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>状态</th>
				<th>期数</th>
				<th>奖金总量</th>
				<th>已发放数量</th>
				<th>奖池总量</th>
				<th>参与人数</th>
				<th>奖金数量</th>
				<th>总手续费</th>
				<th>币符号</th>
				<th>中奖人id</th>
				<th>中奖人邮箱</th>
				<th>中奖人手机</th>
				<th>真实姓名</th>
				<th>remark</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkLuckyDrawRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkLuckyDrawRecord">
			<tr>
				<td><a href="${ctx}/market/mkLuckyDrawRecord/form?id=${mkLuckyDrawRecord.id}">
					${mkLuckyDrawRecord.id}
				</a></td>
				<td>
					${fns:getDictLabel(mkLuckyDrawRecord.status, 'mkLuckyRecordStatus', '')}
				</td>
				<td>
					${mkLuckyDrawRecord.periods}
				</td>
				<td>
					${mkLuckyDrawRecord.volume}
				</td>
				<td>
					${mkLuckyDrawRecord.grantVolume}
				</td>
				<td>
					${mkLuckyDrawRecord.poolVolume}
				</td>
				<td>
					${mkLuckyDrawRecord.playerNumber}
				</td>
				<td>
					${mkLuckyDrawRecord.luckyVolume}
				</td>
				<td>
					${mkLuckyDrawRecord.deductFee}
				</td>
				<td>
					${mkLuckyDrawRecord.coinSymbol}
				</td>
				<td>
					${mkLuckyDrawRecord.userId}
				</td>
				<td>
					${mkLuckyDrawRecord.mail}
				</td>
				<td>
					${mkLuckyDrawRecord.mobile}
				</td>
				<td>
					${mkLuckyDrawRecord.realName}
				</td>
				<td>
					${mkLuckyDrawRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkLuckyDrawRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkLuckyDrawRecord:edit"><td>
    				<a href="${ctx}/market/mkLuckyDrawRecord/form?id=${mkLuckyDrawRecord.id}">修改</a>
					<a href="${ctx}/market/mkLuckyDrawRecord/delete?id=${mkLuckyDrawRecord.id}" onclick="return confirmx('确认要删除该抽奖活动开奖记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>