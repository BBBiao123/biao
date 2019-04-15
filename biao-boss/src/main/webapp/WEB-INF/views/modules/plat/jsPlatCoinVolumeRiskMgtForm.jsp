<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种资产风控管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/">币种资产风控管理列表</a></li>
		<li class="active"><a href="${ctx}/plat/jsPlatCoinVolumeRiskMgt/form?id=${jsPlatCoinVolumeRiskMgt.id}">币种资产风控管理<shiro:hasPermission name="plat:jsPlatCoinVolumeRiskMgt:edit">${not empty jsPlatCoinVolumeRiskMgt.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:jsPlatCoinVolumeRiskMgt:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="jsPlatCoinVolumeRiskMgt" action="${ctx}/plat/jsPlatCoinVolumeRiskMgt/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">风控阀值：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:jsPlatCoinVolumeRiskMgt:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>