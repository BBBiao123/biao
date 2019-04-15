<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种销毁记录管理</title>
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
		<li><a href="${ctx}/market/mkCoinDestroyRecord/">币种销毁记录列表</a></li>
		<li class="active"><a href="${ctx}/market/mkCoinDestroyRecord/form?id=${mkCoinDestroyRecord.id}">币种销毁记录<shiro:hasPermission name="market:mkCoinDestroyRecord:edit">${not empty mkCoinDestroyRecord.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkCoinDestroyRecord:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkCoinDestroyRecord" action="${ctx}/market/mkCoinDestroyRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">币种标识：</label>
			<div class="controls">
				<form:hidden path="symbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge require" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">销毁数量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge  number require"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销毁日期：</label>
			<div class="controls">
				<fmt:parseDate value="${mkCoinDestroyRecord.destroyDateStr}" pattern="yyyy-MM-dd HH:mm:ss" var="destroyDate"/>
				<input name="destroyDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${destroyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkCoinDestroyRecord:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>