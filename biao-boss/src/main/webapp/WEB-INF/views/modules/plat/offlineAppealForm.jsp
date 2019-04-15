<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申诉管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#btnBuyer").click(function () {
				var reson = $("#examineResultReason").val();
				if (!reson || reson == '') {
                    alertx("请输入判诉原因！");
                    return;
				} else {
                    $("#buyReason").val($("#examineResultReason").val());
                    confirmx("确认判给<font color=\"red\">买币方</font>吗？（注：判给<font color=\"red\">买币方</font>，即将本单涉及到的币转移给<font color=\"red\">买币方</font>的C2C资产账户。）", function () {
                        $("#buyerForm").submit();
                    });
				}
            });

            $("#btnSeller").click(function () {
                var reson = $("#examineResultReason").val();
                if (!reson || reson == '') {
                    alertx("请输入判诉原因！");
                    return;
                } else {
                    $("#sellReason").val($("#examineResultReason").val());
                    confirmx("确认判给<font color=\"red\">卖币方</font>吗？（注：判给<font color=\"red\">卖币方</font>，即将本单涉及到的币归还给<font color=\"red\">卖币方</font>的C2C资产账户。）", function () {
                        $("#sellerForm").submit();
                    });
				}
            });


		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/plat/offlineAppeal/">申诉列表</a></li>
		<li class="active"><a href="${ctx}/plat/offlineAppeal/form?id=${offlineAppeal.id}">申诉<shiro:hasPermission name="plat:offlineAppeal:edit">${not empty offlineAppeal.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:offlineAppeal:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="offlineAppeal" action="${ctx}/plat/offlineAppeal/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">申诉人姓名：</label>
			<div class="controls">
				<form:input path="appealRealName" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单号：</label>
			<div class="controls">
				<form:input path="subOrderId" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">卖币方</font>姓名：</label>
			<div class="controls">
				<form:input path="sellRealName" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖币方手机号：</label>
			<div class="controls">
				<form:input path="sellMobile" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖币方Email：</label>
			<div class="controls">
				<form:input path="sellMail" htmlEscape="false" maxlength="64" class="input-xlarge " disabled="true"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">卖币方微信号：</label>
			<div class="controls">
				<form:input path="sellWechat" htmlEscape="false" class="input-xlarge" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖币方支付宝：</label>
			<div class="controls">
				<form:input path="sellAlipay" htmlEscape="false" class="input-xlarge" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖币方银行卡名称：</label>
			<div class="controls">
				<form:input path="sellBankName" htmlEscape="false" class="input-xlarge" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖币方银行卡号：</label>
			<div class="controls">
				<form:input path="sellBankNo" htmlEscape="false" class="input-xlarge" disabled="true"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label"><font color="red">买币方</font>姓名：</label>
			<div class="controls">
				<form:input path="buyRealName" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买币方手机号：</label>
			<div class="controls">
				<form:input path="buyMobile" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买币方Email：</label>
			<div class="controls">
				<form:input path="buyMail" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required" disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('c2c_appeal')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原因类型：</label>
			<div class="controls">
				<form:input path="appealType" htmlEscape="false" maxlength="20" class="input-xlarge required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申诉原因：</label>
			<div class="controls">
				<form:textarea path="reason" htmlEscape="false" maxlength="500" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申诉付款凭证1：</label>
			<div class="controls">
				<img src="${imageUrl}/${offlineAppeal.imagePath}" height="200" width="300">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申诉付款凭证2：</label>
			<div class="controls">
				<img src="${imageUrl}/${offlineAppeal.imagePath2}" height="200" width="300">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申诉付款凭证3：</label>
			<div class="controls">
				<img src="${imageUrl}/${offlineAppeal.imagePath3}" height="200" width="300">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核人：</label>
			<div class="controls">
				<form:input path="examineUserId" htmlEscape="false" maxlength="64" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核时间：</label>
			<div class="controls">
				<input name="examineDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${offlineAppeal.examineDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">判诉胜方：</label>
			<div class="controls">
				<form:input path="examineResultUserName" htmlEscape="false" maxlength="64" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">判诉原因：</label>
			<div class="controls">
				<form:textarea path="examineResultReason" htmlEscape="false" maxlength="500" class="input-xlarge " disabled="${offlineAppeal.status ne '1' ? true : false}"/>
				<c:if test="${offlineAppeal.status eq '1'}">
				<span class="help-inline"><font color="red">*</font> </span>
				</c:if>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${offlineAppeal.status eq '1'}">
			<shiro:hasPermission name="plat:offlineAppeal:examine"><input id="btnBuyer" class="btn btn-primary" type="button" value="判给买币方"/>&nbsp;</shiro:hasPermission>
			<shiro:hasPermission name="plat:offlineAppeal:examine"><input id="btnSeller" class="btn btn-primary" type="button" value="判给卖币方"/>&nbsp;</shiro:hasPermission>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="location.href='${ctx}/plat/offlineAppeal'"/>
		</div>
	</form:form>
	<form:form id="buyerForm" action="${ctx}/plat/offlineAppeal/examineBuyer" method="post">
		<input type="hidden" id ="buyReason" name="examineResultReason" value=""/>
		<input type="hidden" name="appealId" value="${offlineAppeal.id}"/>
	</form:form>
	<form:form id="sellerForm" action="${ctx}/plat/offlineAppeal/examineSeller" method="post">
		<input type="hidden" id="sellReason" name="examineResultReason" value=""/>
		<input type="hidden" name="appealId" value="${offlineAppeal.id}"/>
	</form:form>
</body>
</html>