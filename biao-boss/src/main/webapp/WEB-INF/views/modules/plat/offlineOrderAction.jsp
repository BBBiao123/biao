<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c广告管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            if ($("#messageBox")) {
                $("#messageBox").css({display: 'block'})
            }
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/plat/offlineOrder/">c2c广告列表</a></li>
		<shiro:hasPermission name="plat:offlineOrder:edit"><li><a href="${ctx}/plat/offlineOrder/batchAction">c2c广告批量操作</a></li></shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm"  action="${ctx}/plat/offlineOrder/doBatchCancel" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<%--<form:hidden path="coinSymbol" id="coinSymbol"/>--%>
				<%--<form:select path="coinId" class="input-xlarge required" id="selectCoin" cssStyle="width: 176px;">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />--%>
				<%--</form:select>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
				<input name="coinSymbol" id="coinSymbol" type="text"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告类型：</label>
			<div class="controls">
				<%--<form:input path="exType" htmlEscape="false" maxlength="1" class="input-xlarge "/>--%>
				<input name="exType" id="exType" type="text"/><label><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;0：买广告；1：卖广告</font></label>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:offlineOrder:view"><input id="btnSubmit" class="btn btn-primary" type="submit" value="批量撤销"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>