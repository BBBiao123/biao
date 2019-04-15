<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>红包配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkRedEnvelopeConf/">红包配置列表</a></li>
		<li class="active"><a href="${ctx}/market/mkRedEnvelopeConf/form?id=${mkRedEnvelopeConf.id}">红包配置<shiro:hasPermission name="market:mkRedEnvelopeConf:edit">${not empty mkRedEnvelopeConf.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkRedEnvelopeConf:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkRedEnvelopeConf" action="${ctx}/market/mkRedEnvelopeConf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">币种id：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">币种代号：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
					<%--<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>--%>
					<%--<span class="help-inline"><font color="red">*</font> </span>--%>
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金额下限：</label>
			<div class="controls">
				<form:input path="singleLowerVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金额上限：</label>
			<div class="controls">
				<form:input path="singleHigherVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">个数下限：</label>
			<div class="controls">
				<form:input path="lowerNumber" htmlEscape="false" maxlength="3" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">个数上限：</label>
			<div class="controls">
				<form:input path="higherNumber" htmlEscape="false" maxlength="3" class="input-xlarge  digits"/>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">手续费：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="fee" htmlEscape="false" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">手续费归集账户：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="destroyUserId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">手气金额下限：</label>
			<div class="controls">
				<form:input path="luckyLowerVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手气金额上限：</label>
			<div class="controls">
				<form:input path="luckyHigherVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">价格精度：</label>
			<div class="controls">
				<form:input path="pointVolume" htmlEscape="false" maxlength="2" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayConfigStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本号：</label>
			<div class="controls">
				<form:input path="version" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkRedEnvelopeConf:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>