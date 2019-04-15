<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>抽奖活动规则管理</title>
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
		<li><a href="${ctx}/market/mkLuckyDrawConfig/">抽奖活动规则列表</a></li>
		<li class="active"><a href="${ctx}/market/mkLuckyDrawConfig/form?id=${mkLuckyDrawConfig.id}">抽奖活动规则<shiro:hasPermission name="market:mkLuckyDrawConfig:edit">${not empty mkLuckyDrawConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkLuckyDrawConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkLuckyDrawConfig" action="${ctx}/market/mkLuckyDrawConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">活动名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">奖金总量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge  number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">起步奖金：</label>
			<div class="controls">
				<form:input path="startVolume" htmlEscape="false" class="input-xlarge  number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新增奖金：</label>
			<div class="controls">
				<form:input path="stepAddVolume" htmlEscape="false" class="input-xlarge  number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">用户最低参与数量：</label>
			<div class="controls">
				<form:input path="minVolume" htmlEscape="false" class="input-xlarge  number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">扣除手续费：</label>
			<div class="controls">
				<form:input path="deductFee" htmlEscape="false" class="input-xlarge  number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已发放数量：</label>
			<div class="controls">
				<form:input path="grantVolume" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前奖池总量：</label>
			<div class="controls">
				<form:input path="poolVolume" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkLuckyDrawConfig:edit">
				<c:if test="${empty mkLuckyDrawConfig.id}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
				<c:if test="${mkLuckyDrawConfig.status eq '0' || mkLuckyDrawConfig.status eq '3'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>