<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商拨币申请管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            <c:if test="${empty otcAgentRemitApply.payCoinType || otcAgentRemitApply.payCoinType == '0'}">
				$(':input[name=transferTo]').attr("readonly",true);
				$(':input[name=transferOut]').attr("readonly",true);
				$(':input[name=usdtRate]').attr("readonly",true);
            </c:if>

			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    var check = true;
                    var percentages = [];
                    $(':input[name=percentages]').each(function (i) {
                        debugger;
                        if (!$(this).val()) {
                            alertx("拨币占比不能为空！");
                            check = false;
                            return false;
                        }

                        if(!checkPercentage($(this).val())){
                            check = false;
                            return false;
						}
                        percentages.push($(this).val());
                    });

                    if (!check) {
                        return;
                    }

                    var percentageSum = 0;
                    for (var i = 0; i < percentages.length; i ++) {
                        percentageSum += parseFloat(percentages[i]);
                    }

                    if(percentageSum != 100){
                        alertx("会员拨币占比必须为100%");
                        return false;
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

            $("#selectPayCoinType").change(function(){
                var coinType = $("#selectPayCoinType").find("option:selected").val();
                if(coinType == 0){
                    $(':input[name=transferTo]').attr("readonly",true);
                    $(':input[name=transferOut]').attr("readonly",true);
                    $(':input[name=usdtRate]').attr("readonly",true);
                    $(':input[name=transferTo]').val("");
                    $(':input[name=transferOut]').val("");
                    $(':input[name=usdtRate]').val("");
				}else{
                    $(':input[name=transferTo]').attr("readonly",false);
                    $(':input[name=transferOut]').attr("readonly",false);
                    $(':input[name=usdtRate]').attr("readonly",false);
				}

                $(':input[name=volume]').val("");
                $(':input[name=applyVolume]').val("");
            });

            $(":input[name=percentages]").on("input",function(e){
                var applyVolume = $(':input[name=applyVolume]').val();
                if(!applyVolume){
                    return ;
				}
                applyVolume = parseFloat(applyVolume);
                $(':input[name=userIds]').each(function (i) {
                    var percentage = $("#percentage_" + $(this).val()).val();
                    if(!percentage){
                        return ;
                    }
                    var percentage = parseFloat(percentage) / parseFloat(100);
                    $("#applyVolume_" + $(this).val()).val((applyVolume * percentage).toFixed(6));
                });
            });


            $("#volume").on("input",function(e){
                //获取input输入的值
				var volume = e.delegateTarget.value;
                var coinType = $("#selectPayCoinType").find("option:selected").val();
                var tradeCoinRate = $(':input[name=tradeCoinRate]').val();
                var discount = $(':input[name=discount]').val();
                if(coinType == null || coinType == ""){
                    alertx("请选择支付币种！");
                    return;
				}

				if(coinType == 0){
                    var applyVolume = parseFloat(volume) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=applyVolume]').val(applyVolume.toFixed(6));
				}else{
                    var usdtRate =  $(':input[name=usdtRate]').val();
                    if(usdtRate == null || usdtRate == ""){
                        alertx("USDT汇率不能为空！");
                        $(':input[name=volume]').val("");
                        return;
					}
                    var applyVolume = (parseFloat(volume) * parseFloat(usdtRate)) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=applyVolume]').val(applyVolume.toFixed(6));
                }

                $(':input[name=userIds]').each(function (i) {
                    var percentage = $("#percentage_" + $(this).val()).val();
                    if(!percentage){
                        return ;
					}
                    var percentage = parseFloat(percentage) / parseFloat(100);
                    $("#applyVolume_" + $(this).val()).val((applyVolume * percentage).toFixed(6));
                });
            });

            $("#usdtRate").on("input",function(e){
                //获取input输入的值
                var usdtRate = e.delegateTarget.value;
                var tradeCoinRate =  $(':input[name=tradeCoinRate]').val();
                var coinType = $("#selectPayCoinType").find("option:selected").val();
                var volume = $(':input[name=volume]').val();
                var discount = $(':input[name=discount]').val();

                var applyVolume ;
                if(coinType == 0){
                    applyVolume = parseFloat(volume) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=remitVolume]').val(applyVolume.toFixed(6));
                }else{
                    if(usdtRate == null || usdtRate == ""){
                        return;
                    }

                    // if(!checkUsdtRate(usdtRate)){
                    //     return ;
                    // }
                    applyVolume = (parseFloat(volume) * parseFloat(usdtRate)) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=remitVolume]').val(applyVolume.toFixed(6));
                }

                $(':input[name=userIds]').each(function (i) {
                    var percentage = $("#percentage_" + $(this).val()).val();
                    if(!percentage){
                        return ;
                    }
                    var percentage = parseFloat(percentage) / parseFloat(100);
                    $("#applyVolume_" + $(this).val()).val((applyVolume * percentage).toFixed(6));

                });
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

        function checkUsdtRate(usdtRate) {
            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
            if (!reg.test(usdtRate)) {
                return false;
            }

            if (parseFloat(usdtRate) < 5) {
                alertx("usdt汇率小于5！");
                return false;
            }

            if (parseFloat(usdtRate) > 10) {
                alertx("usdt汇率不能超过10！");
                return false;
            }
            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/otc/otcAgentRemitApply/">银商拨币申请列表</a></li>
		<li class="active"><a href="${ctx}/otc/otcAgentRemitApply/form?id=${otcAgentRemitApply.id}">银商拨币申请<shiro:hasPermission name="otc:otcAgentRemitApply:edit">${not empty otcAgentRemitApply.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="otc:otcAgentRemitApply:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="otcAgentRemitApply" action="${ctx}/otc/otcAgentRemitApply/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">银商名称：</label>
			<div class="controls">
				<form:hidden path="agentId"/>
				<form:input path="agentName" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinId"/>
				<form:input path="coinSymbol" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">折扣：</label>
			<div class="controls">
				<form:hidden path="discount"/>
				<form:input path="discountPercentage" readonly="true" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付币种：</label>
			<div class="controls">
				<form:select path="payCoinType" class="input-xlarge " id="selectPayCoinType">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('otcPayCoinType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">usdt转入地址：</label>
			<div class="controls">
				<form:input path="transferTo" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">usdt转出地址：</label>
			<div class="controls">
				<form:input path="transferOut" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">USDT汇率：</label>
			<div class="controls">
				<form:input path="usdtRate" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量：</label>
			<div class="controls">
				<form:input path="volume" id="volume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易币种单价：</label>
			<div class="controls">
				<form:input path="tradeCoinRate" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请数量：</label>
			<div class="controls">
				<form:input path="applyVolume" id="applyVolume" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<th style="width: 100px">会员ID</th>
			<th>邮箱</th>
			<th>手机</th>
			<th>拨币占比(%)</th>
			<th>预估可拨币数量</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${otcAgentApplyUserList}" var="otcAgentApplyUser">
				<tr>
					<td>
						<input name ="userIds" value="${otcAgentApplyUser.userId}" readonly="readonly"/>
					</td>
					<td>
						${otcAgentApplyUser.mail}
					</td>
					<td>
						${otcAgentApplyUser.mobile}
					</td>
					<td>
						<input name ="percentages" id="percentage_${otcAgentApplyUser.userId}" value="${otcAgentApplyUser.percentage}"/>
					</td>
					<td>
						<input name ="applyVolumes" id="applyVolume_${otcAgentApplyUser.userId}" readonly value="${otcAgentApplyUser.volume}"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="form-actions">
			<shiro:hasPermission name="otc:otcAgentRemitApply:edit">
				<c:if test="${empty otcAgentRemitApply.status || otcAgentRemitApply.status == '0'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>