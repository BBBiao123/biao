<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易风险控制管理</title>
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

            $("#selectExPair").change(function(){
                var exPair = $("#selectExPair").find("option:selected").text();
                $("#exPairSymbol").val(exPair);

                var coins = exPair.split('/');
                $("input[name='coinMain']").val(coins[1]);
                $("input[name='coinOther']").val(coins[0]);
            });
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/plat/tradeRiskControl/">交易风险控制列表</a></li>
		<li class="active"><a href="${ctx}/plat/tradeRiskControl/form?id=${tradeRiskControl.id}">交易风险控制<shiro:hasPermission name="plat:tradeRiskControl:edit">${not empty tradeRiskControl.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:tradeRiskControl:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tradeRiskControl" action="${ctx}/plat/tradeRiskControl/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">币币对：</label>
			<div class="controls">
				<form:select path="exPairId" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${exPairList}" itemLabel="pairSymbol" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主交易区：</label>
			<div class="controls">
				<form:input path="coinMain" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">被交易币种：</label>
			<div class="controls">
				<form:input path="coinOther" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>

		</div>
		<div class="control-group">
			<label class="control-label">风险比例：</label>
			<div class="controls">
				<form:input path="riskRatio" htmlEscape="false" maxlength="4" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">护盘资金：</label>
			<div class="controls">
				<form:input path="fixedVolume" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">操盘手标识：</label>
			<div class="controls">
				<form:select path="sourceUser" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sourceUser')}" itemLabel="label" itemValue="label" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:tradeRiskControl:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>