<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>主区币兑人民币汇率管理</title>
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
		<li><a href="${ctx}/plat/jsPlatMainCnb/">主区币兑人民币汇率列表</a></li>
		<li class="active"><a href="${ctx}/plat/jsPlatMainCnb/form?id=${jsPlatMainCnb.id}">主区币兑人民币汇率<shiro:hasPermission name="plat:jsPlatMainCnb:edit">${not empty jsPlatMainCnb.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:jsPlatMainCnb:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="jsPlatMainCnb" action="${ctx}/plat/jsPlatMainCnb/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<%--<div class="control-group">--%>
			<%--<label class="control-label">主区币ID：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">主区币名称：</label>
			<div class="controls">
				<%--<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin" cssStyle="width: 176px;">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">兑人民币汇率：</label>
			<div class="controls">
				<form:input path="cnbRate" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:jsPlatMainCnb:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>