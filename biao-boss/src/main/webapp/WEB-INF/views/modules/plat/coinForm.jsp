<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种管理</title>
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
		<li><a href="${ctx}/plat/coin/">币种列表</a></li>
		<li class="active"><a href="${ctx}/plat/coin/form?id=${coin.id}">币种<shiro:hasPermission name="plat:coin:edit">${not empty coin.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:coin:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="coin" action="${ctx}/plat/coin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">交易代号：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="10" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种全称：</label>
			<div class="controls">
				<form:input path="fullName" htmlEscape="false" maxlength="24" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">官网：</label>
			<div class="controls">
				<form:input path="domain" htmlEscape="false" maxlength="24" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">白皮书地址：</label>
			<div class="controls">
				<form:input path="whitepaperUrl" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">token 总量：</label>
			<div class="controls">
				<form:input path="tokenVolume" htmlEscape="false" maxlength="24" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ico 价格：</label>
			<div class="controls">
				<form:input path="icoPrice" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">流通总量：</label>
			<div class="controls">
				<form:input path="circulateVolume" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">充值提现状态</label>
			<div class="controls">
				<form:select path="tokenStatus" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('tokenStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态 </label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('coinStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">最低挂单数量：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="exMinVolume" htmlEscape="false" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">一次提现最低数量：</label>
			<div class="controls">
				<form:input path="withdrawMinVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">一次提现最大数量：</label>
			<div class="controls">
				<form:input path="withdrawMaxVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">一天最大提现额度：</label>
			<div class="controls">
				<form:input path="withdrawDayMaxVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">V1一天最大提现额度：</label>
			<div class="controls">
				<form:input path="withdrawDayOneMaxVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">V2一天最大提现额度：</label>
			<div class="controls">
				<form:input path="withdrawDayTwoMaxVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提现手续费类型：</label>
			<div class="controls">
				<form:select path="withdrawFeeType" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('withdrawFeeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提现手续费：</label>
			<div class="controls">
				<form:input path="withdrawFee" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">基于何种代币</label>
			<div class="controls">
				<form:select path="coinType" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('coinType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">父类</label>
			<div class="controls">
				<form:select path="parentId" class="input-xlarge ">
					<form:option value="" label="无"/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种图标：</label>
			<div class="controls">
				<form:hidden id="iconId" path="iconId" htmlEscape="false" maxlength="45" class="input-xlarge"/>
				<sys:ckfinder input="iconId" type="files" uploadPath="/plat/coin" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种介绍：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" class="input-xxlarge "/>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="plat:coin:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>