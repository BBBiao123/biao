<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){

                    var volume = $("input[name='volume']").val();
                    var grantVolume = $("input[name='grantVolume']").val();

                    if(!checkVolume(volume,grantVolume)){
                        return ;
                    }

                    var percentage = $("input[name='percentage']").val();
                    if(!checkPercentage(percentage)){
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

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });
		});

        function checkPercentage(percentage){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
            if (!reg.test(percentage)) {
                alertx("请输入正确百分比！")
                return false;
            }

            if(parseFloat(percentage) <= 0){
                alertx("百分比不能小于等于零！")
                return false;
            }

            if(parseFloat(percentage) > 100){
                alertx("百分比不能超过100！")
                return false;
            }

            return true;
        }

        function checkVolume(volume, grantVolume){

            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(volume)) {
                alertx("请输入正确的币种总数量！");
                return false;
            }

            if(parseFloat(volume) <= 0){
                alertx("币种总数量不能小于零！");
                return false;
            }

            if(parseFloat(volume) <= parseFloat(grantVolume)){
                alertx("币种总数量不能小于已发放数量！")
                return false;
            }

            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkDistributeMining/">挖矿规则列表</a></li>
		<li class="active"><a href="${ctx}/market/mkDistributeMining/form?id=${mkDistributeMining.id}">挖矿规则<shiro:hasPermission name="market:mining:edit">${not empty mkDistributeMining.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mining:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkDistributeMining" action="${ctx}/market/mkDistributeMining/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">规则名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('distributeRuleStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">百分比(手续费)%：</label>
			<div class="controls">
				<form:input path="percentage" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*例如：100%,输入: 100</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种代号：</label>
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
			<label class="control-label">币种总数量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已发放数量：</label>
			<div class="controls">
				<form:input path="grantVolume" readonly="true" htmlEscape="false" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mining:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>