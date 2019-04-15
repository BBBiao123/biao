<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币交易对管理</title>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/plat/exPair/">币币交易对列表</a></li>
		<li class="active"><a href="${ctx}/plat/exPair/form?id=${exPair.id}">币币交易对<shiro:hasPermission name="plat:exPair:edit">${not empty exPair.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:exPair:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="exPair" action="${ctx}/plat/exPair/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label class="control-label">主交易区id：</label>
			<div class="controls">
				<form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">交易区符号：</label>
			<div class="controls">
				<form:input path="pairOne" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> --%>	
		<div class="control-group">
			<label class="control-label">被交易币种id：</label>
			<div class="controls">
				<form:select path="otherCoinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">被交易的币种符号：</label>
			<div class="controls">
				<form:input path="pairOther" htmlEscape="false" maxlength="24" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">是否锁定 0：未发布 1：发布：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exPairStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">交易区：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exPairType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手续费：</label>
			<div class="controls">
				<form:select path="fee" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('feeRate')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">最小成交量：</label>
			<div class="controls">
				<form:input path="minVolume" maxlength="64" class="input-xlarge required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">最大成交量：</label>
			<div class="controls">
				<form:input path="maxVolume" maxlength="64" class="input-xlarge required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">价格精度：</label>
			<div class="controls">
				<form:input path="pricePrecision" maxlength="1" class="input-xlarge required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">数量精度：</label>
			<div class="controls">
				<form:input path="volumePrecision" maxlength="1" class="input-xlarge required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">数量百分比：</label>
			<div class="controls">
				<form:input path="volumePercent" maxlength="10" class="input-xlarge required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" maxlength="64" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:exPair:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>