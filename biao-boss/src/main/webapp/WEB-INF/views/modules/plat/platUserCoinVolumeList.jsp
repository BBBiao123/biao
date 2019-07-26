<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>前台用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	</script>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>用户id</th>
			<th>币种id</th>
			<th>币种代号</th>
			<th>币种数量</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${userCoinVolumeList}" var="jsPlatUserCoinVolume">
			<tr>
				<td>
						${jsPlatUserCoinVolume.userId}
				</td>
				<td>
						${jsPlatUserCoinVolume.coinId}
				</td>
				<td>
						${jsPlatUserCoinVolume.coinSymbol}
				</td>
				<td>
						${jsPlatUserCoinVolume.volume}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>