<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册送币规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			var defaultId = $("#id").val();
			if (!defaultId) {
				$("#giveVolume").val(0);
			}
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
		<li><a href="${ctx}/plat/mk2PopularizeRegisterConf/">注册送币规则列表</a></li>
		<li class="active"><a href="${ctx}/plat/mk2PopularizeRegisterConf/form?id=${mk2PopularizeRegisterConf.id}">注册送币规则<shiro:hasPermission name="plat:mk2PopularizeRegisterConf:edit">${not empty mk2PopularizeRegisterConf.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:mk2PopularizeRegisterConf:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeRegisterConf" action="${ctx}/plat/mk2PopularizeRegisterConf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">推广名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">送币币种：</label>
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
			<label class="control-label">总送币量：</label>
			<div class="controls">
				<form:input path="totalVolume" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已送币量：</label>
			<div class="controls">
				<form:input path="giveVolume" htmlEscape="false" maxlength="20" class="input-xlarge" disabled="true" />
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">币名称：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-xlarge required"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">注册送币数量：</label>
			<div class="controls">
				<form:input path="registerVolume" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐送币数量：</label>
			<div class="controls">
				<form:input path="referVolume" htmlEscape="false" maxlength="10" class="input-xlarge required digits" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_register_conf_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:mk2PopularizeRegisterConf:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>