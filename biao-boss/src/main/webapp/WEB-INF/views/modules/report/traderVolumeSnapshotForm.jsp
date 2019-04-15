<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操盘手资产快照管理</title>
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
		<li><a href="${ctx}/report/traderVolumeSnapshot/">操盘手资产快照列表</a></li>
		<li class="active"><a href="${ctx}/report/traderVolumeSnapshot/form?id=${traderVolumeSnapshot.id}">操盘手资产快照<shiro:hasPermission name="report:traderVolumeSnapshot:edit">${not empty traderVolumeSnapshot.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="report:traderVolumeSnapshot:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="traderVolumeSnapshot" action="${ctx}/report/traderVolumeSnapshot/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">快照日期：</label>
			<div class="controls">
				<input name="snapDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${traderVolumeSnapshot.snapDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_id：</label>
			<div class="controls">
				<sys:treeselect id="userId" name="userId" value="${traderVolumeSnapshot.userId}" labelName="" labelValue="${traderVolumeSnapshot.}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_tag：</label>
			<div class="controls">
				<form:input path="userTag" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种id：</label>
			<div class="controls">
				<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种代号：</label>
			<div class="controls">
				<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币币：</label>
			<div class="controls">
				<form:input path="tradeVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">c2c资产：</label>
			<div class="controls">
				<form:input path="offlineVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结资产：</label>
			<div class="controls">
				<form:input path="lockVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总资产：</label>
			<div class="controls">
				<form:input path="totalVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">流水汇总：</label>
			<div class="controls">
				<form:input path="billSumVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">差额：</label>
			<div class="controls">
				<form:input path="balance" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">拨币资产：</label>
			<div class="controls">
				<form:input path="bobiVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">remark：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="report:traderVolumeSnapshot:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>