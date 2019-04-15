<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币订单管理</title>
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
		<li class="active"><a href="${ctx}/plat/exOrder/">币币订单详情列表</a></li>
	</ul>
	<div class="form-actions">
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型</th>
				<th>成交价格</th>
				<th>成交数量</th>
				<th>手续费</th>
				<th>主区币种标识</th>
				<th>被交易币种</th>
				<th>交易时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${details}" var="detail">
			<tr>
				<td>
					${fns:getDictLabel(detail.type, 'exType', '')}
				</td>
				<td>
					${detail.price}
				</td>
				<td>
					${detail.volume}
				</td>
				<td>
						${detail.exFee}
				</td>
				<td>
					${detail.coinMain}
				</td>
				<td>
					${detail.coinOther}
				</td>
				<td>
					<fmt:formatDate value="${detail.tradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>