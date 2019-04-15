<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });

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
		<li><a href="${ctx}/market/mk2PopularizeMiningConf/">挖矿规则列表</a></li>
		<li class="active"><a href="${ctx}/market/mk2PopularizeMiningConf/form?id=${mk2PopularizeMiningConf.id}">挖矿规则<shiro:hasPermission name="market:mk2PopularizeMiningConf:edit">${not empty mk2PopularizeMiningConf.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mk2PopularizeMiningConf:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeMiningConf" action="${ctx}/market/mk2PopularizeMiningConf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_mining_conf_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">挖矿总量：</label>
			<div class="controls">
				<form:input path="totalVolume" htmlEscape="false" class="input-xlarge  number" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已挖总量：</label>
			<div class="controls">
				<form:input path="grantVolume" htmlEscape="false" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已挖显示倍数：</label>
			<div class="controls">
				<form:input path="showMultiple" htmlEscape="false" class="input-xlarge  number" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已挖显示倍数（待更新）：</label>
			<div class="controls">
				<form:input path="delayShowMultiple" htmlEscape="false" class="input-xlarge  number" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每次挖矿量：</label>
			<div class="controls">
				<form:input path="perVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参于挖矿持有量：</label>
			<div class="controls">
				<form:input path="greaterVolume" htmlEscape="false" class="input-xlarge  number"/>&nbsp;<label><font style="color: red">释义：【持币挖矿】时为持币人的最小数量；【团队挖矿】时为团队持币总量最小数量。</font></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">挖矿基数：</label>
			<div class="controls">
				<form:input path="baseVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">挖矿基数倍数：</label>
			<div class="controls">
				<form:input path="baseMultiple" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队Leader最小持有量：</label>
			<div class="controls">
				<form:input path="leaderGreaterVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">可得挖矿量与持用量占比：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="incomeHoldRatio" htmlEscape="false" class="input-xlarge  number"/>--%>
			<%--</div>--%>
		<%--</div>--%>

		<div class="control-group">
			<label class="control-label">币种名称：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin" cssStyle="width: 176px;">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
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
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mk2PopularizeMiningConf:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>