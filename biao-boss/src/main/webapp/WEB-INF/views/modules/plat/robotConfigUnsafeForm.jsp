<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币安自动化管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					var idValue = $("#id").val() ;
					if(idValue==""){
						var encrypt = new JSEncrypt();
						encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLADJL0WYJJrxmpNqKeoAXhW8P0GWMy7ZJG/I+8CwLZ2we83VnHcF4zXfpWrw3zY4RIYkFQT8EkW7FUDFeY9XzoxoQbcjyG3ywIzN6SI+7Jd07TGktNTTxFR6Bj4IjzAlazitFlUKAP77AyhT65YDChbNRul8u6M5qqt/ojjGb1QIDAQAB");
						var data = encrypt.encrypt($("#userPassword").val());
						$("#userPassword").val(data);
					}
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
		<li><a href="${ctx}/plat/robotConfigUnsafe/">币安自动化管理列表</a></li>
		<li class="active"><a href="${ctx}/plat/robotConfigUnsafe/form?id=${robotConfigUnsafe.id}">币安自动化管理<shiro:hasPermission name="plat:robotConfigUnsafe:edit">${not empty robotConfigUnsafe.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:robotConfigUnsafe:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="robotConfigUnsafe" action="${ctx}/plat/robotConfigUnsafe/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">type：</label>
			<div class="controls">
			    <form:select path="type">
				   <form:option value="0">买入</form:option>
				   <form:option value="1">卖出</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">coin_main：</label>
			<div class="controls">
				<form:select path="coinMain" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">coin_other：</label>
			<div class="controls">
				<form:select path="coinOther" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_id：</label>
			<div class="controls">
				<form:input path="user.id" id="user" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_name：</label>
			<div class="controls">
				<form:input path="userName" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">password：</label>
			<div class="controls">
				<form:input path="password" htmlEscape="false" id="userPassword" maxlength="1000" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易量范围：</label>
			<div class="controls">
				<form:input path="volumeRange" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">价格范围：</label>
			<div class="controls">
				<form:input path="priceRange" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态  ：</label>
			<div class="controls">
				<form:select path="status">
				   <form:option value="0">启用</form:option>
				   <form:option value="1">禁用</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">is_init：</label>
			<div class="controls">
				<form:select path="isInit">
				   <form:option value="0">不初始化</form:option>
				   <form:option value="1">初始化</form:option>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:robotConfigUnsafe:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>