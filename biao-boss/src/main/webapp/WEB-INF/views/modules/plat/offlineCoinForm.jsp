<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c_coin管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var changeFeeType = $("#changeFeeType").val();
					var isCheck = true;
					if(changeFeeType == '1'){
					    var changeFeeStep = $("#changeFeeStep").val();
					    if(changeFeeStep =='null' || changeFeeStep == ''){
					        isCheck = false;
					        alertx("转账手续费公式不能為空！");
						}
					}else if(changeFeeType == '2'){
                        var changeFee = $("#changeFee").val();
                        if(changeFeeStep =='null' || changeFeeStep == '' || changeFee <= 0){
                            isCheck = false;
                            alertx("转账手续费不能為空！");
                        }
					}

                    if(!isCheck){
                        return ;
                    }

                    var changeMinVolume = $("#changeMinVolume").val();
                    if(changeMinVolume =='null' || changeMinVolume == '' || changeMinVolume <= 0){
                        isCheck = false;
                        alertx("转账最低数量不能為空或小于等于零！");
                    }

					if(!isCheck){
					    return ;
					}

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
		<li><a href="${ctx}/plat/offlineCoin/">c2c_coin列表</a></li>
		<li class="active"><a href="${ctx}/plat/offlineCoin/form?id=${offlineCoin.id}">c2c_coin<shiro:hasPermission name="plat:offlineCoin:edit">${not empty offlineCoin.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:offlineCoin:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="offlineCoin" action="${ctx}/plat/offlineCoin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">c2c买卖币种id：</label>
			<div class="controls">
				<form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>

		</div>
		<div class="control-group">
			<label class="control-label">最大价格限制：</label>
			<div class="controls">
				<form:input path="maxPrice" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">最小价格限制：</label>
			<div class="controls">
				<form:input path="minPrice" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">价格精度：</label>
			<div class="controls">
				<form:input path="pointPrice" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">最大数量限制：</label>
			<div class="controls">
				<form:input path="maxVolume" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">最小数量限制：</label>
			<div class="controls">
				<form:input path="minVolume" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">数量精度：</label>
			<div class="controls">
				<form:input path="pointVolume" maxlength="1" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手续费类型：</label>
			<div class="controls">
				<form:select path="feeType">
				   <form:option value="1">固定</form:option>
				   <form:option value="0">不收</form:option>
				   <form:option value="2">比例</form:option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">比例买入：</label>
			<div class="controls">
				<form:input path="buyFee" maxlength="45" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">比例卖出：</label>
			<div class="controls">
				<form:input path="sellFee" maxlength="45" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买入费率公式：</label>
			<div class="controls">
				<form:input path="buyFeeStep" maxlength="45" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖出费率公式：</label>
			<div class="controls">
				<form:input path="sellFeeStep" maxlength="45" class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">每日价格增量：</label>
			<div class="controls">
				<form:input path="dayIncPrice" maxlength="45" class="input-xlarge number"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否开启转账：</label>
			<div class="controls">
				<form:select path="isChangeAccount">
					<form:option value="1">否</form:option>
					<form:option value="0">是</form:option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">实名转账限额：</label>
			<div class="controls">
				<form:input path="realDayLimit" maxlength="45" class="input-xlarge number"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">非实名转账限额：</label>
			<div class="controls">
				<form:input path="nonRealDayLimit" maxlength="45" class="input-xlarge number"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">转账手续费类型：</label>
			<div class="controls">
				<form:select path="changeFeeType" id="changeFeeType">
					<form:option value="1">固定</form:option>
					<form:option value="0">不收</form:option>
					<form:option value="2">比例</form:option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">转账手续费公式：</label>
			<div class="controls">
				<form:input path="changeFeeStep" id="changeFeeStep" maxlength="45" class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">转账手续费：</label>
			<div class="controls">
				<form:input path="changeFee" id = "changeFee" maxlength="45" class="input-xlarge number"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">最低转账数量：</label>
			<div class="controls">
				<form:input path="changeMinVolume" id = "changeMinVolume" maxlength="45" class="input-xlarge number required"/>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="plat:offlineCoin:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>