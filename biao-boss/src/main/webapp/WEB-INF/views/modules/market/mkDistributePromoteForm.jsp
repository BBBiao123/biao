<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员推广管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    var volume = $("input[name='volume']").val();
                    var grantVolume = $("input[name='grantVolume']").val();
                    if(!checkVolume(volume, grantVolume)){
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

        function checkVolume(volume, grantVolume){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(volume)) {
                alertx("请输入正确的推广总量！")
                return false;
            }

            if(volume <= 0){
                alertx("推广总量不能小于零！")
                return false;
            }

            if(parseFloat(volume) <= parseFloat(grantVolume)){
                alertx("推广总量不能小于已发放数量！")
                return false;
            }

            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkDistributePromote/">会员推广列表</a></li>
		<li class="active"><a href="${ctx}/market/mkDistributePromote/form?id=${mkDistributePromote.id}">会员推广<shiro:hasPermission name="market:mkDistributePromote:edit">${not empty mkDistributePromote.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkDistributePromote:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkDistributePromote" action="${ctx}/market/mkDistributePromote/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">规则名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('distributeRuleStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种代号：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广总量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label">已发放数量：</label>
            <div class="controls">
                <form:input path="grantVolume" readonly="true" htmlEscape="false" class="input-xlarge "/>
            </div>
        </div>
		<div class="form-actions">
			<shiro:hasPermission name="market:promote:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>