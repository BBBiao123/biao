<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/jsencrypt.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){

                    var startTime = new Date(Date.parse($("input[name='beginDate']").val()));
                    var endTime = new Date(Date.parse($("input[name='endDate']").val()));
                    if(startTime > endTime){
                        alertx("开始时间不能大于结束时间");
                        return ;
                    }

                    var minVolume = $("input[name='minVolume']").val();
				    var maxVolume = $("input[name='maxVolume']").val();
                    if(!checkVolume(minVolume)){
                        return ;
                    }

                    if(!checkVolume(maxVolume)){
                        return ;
                    }

                    if(parseFloat(minVolume) > parseFloat(maxVolume)){
                        alertx("最小成交量不能大于最大成交量");
                        return ;
					}

                    var minPrice = $("input[name='minPrice']").val();
                    var maxPrice = $("input[name='maxPrice']").val();
                    if(!checkPrice(minPrice)){
                        return ;
                    }

                    if(!checkPrice(maxPrice)){
                        return ;
                    }

                    if(parseFloat(minPrice) > parseFloat(maxPrice)){
                        alertx("最小成交价不能大于最大成交价");
                        return ;
                    }

                    var frequency = $("input[name='frequency']").val();
                    if(!checkFrequency(frequency)){
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

            $("body").on("click","#btnLogin", function(){

                var pass = $("input[name='pass']").val();
                var user = $("input[name='username']").val();
                if(pass == null || pass == ""){
                    alertx("密码不能为空！");
                    return ;
				}
				if(user == null || user == ""){
                    alertx("用户不能空！");
                    return ;
				}

                let encrypt = new JSEncrypt();
                encrypt.setPublicKey('MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLADJL0WYJJrxmpNqKeoAXhW8P0GWMy7ZJG/I+8CwLZ2we83VnHcF4zXfpWrw3zY4RIYkFQT8EkW7FUDFeY9XzoxoQbcjyG3ywIzN6SI+7Jd07TGktNTTxFR6Bj4IjzAlazitFlUKAP77AyhT65YDChbNRul8u6M5qqt/ojjGb1QIDAQAB');
                pass = encrypt.encrypt(pass);
                var prePass = pass;
                for (var i=0;i < pass.length; i++){
                    pass = pass .replace("\/", "@");
                    if(prePass == pass){
                        break;
					}else{
                        prePass = pass;
					}
                }
                $.get("${ctx}/market/mkAutoTradeSetting/checkLogin/" + user +"/"+ pass,function(data){
                    var result = JSON.parse(data)
                    if(result.code == '0000'){
                        alertx("登录验证成功！");
					}else{
                        alertx("登录验证失败！");
					}
                });
            });

            $("body").on("click","#usename", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/market/mkAutoTradeSetting/PlatUserDialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            var userLabel = user.attr("userLabel");
                            if(userLabel == null || userLabel == ""){
                                userLabel = user.attr("userMobile");
                            }
                            $(userThis).val(userLabel);
                            $("input[name='userId']").val(user.val());
                            $("input[name='mail']").val(userLabel);
                            $("input[name='mobile']").val(user.attr("userMobile"));
                            $("input[name='realName']").val($.trim(user.attr("userRealName")));
                            $("input[name='idCard']").val(user.attr("userIdCard"));
                        }
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
            });

            $("#selectCoinMain").change(function(){
                $("#coinMainSymbol").val($("#selectCoinMain").find("option:selected").text());
            });

            $("#selectCoinOther").change(function () {
                $("#coinOtherSymbol").val($("#selectCoinOther").find("option:selected").text());
            });

            $("#selectExPair").change(function(){
                var exPair = $("#selectExPair").find("option:selected").text();
                $("#exPairSymbol").val(exPair);

                var coins = exPair.split('/');
                $("input[name='coinOtherSymbol']").val(coins[0]);
                $("input[name='coinMainSymbol']").val(coins[1]);
            });
		});

        function checkVolume(volume){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(volume)) {
                alertx("请输入正确的成交量！");
                return false;
            }

            if(parseFloat(volume) <= 0){
                alertx("成交量不能小于零！");
                return false;

            }
            return true;
        }

        function checkPrice(price){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(price)) {
                alertx("请输入正确的成交价！");
                return false;
            }

            if(parseFloat(price) <= 0){
                alertx("成交价不能小于零！");
                return false;
            }
            return true;
        }

        function checkFrequency(frequency){
            var reg = new RegExp("^\\+?[1-9][0-9]*$");
            if (!reg.test(frequency)) {
                alertx("请输入正整数的频率！");
                return false;
            }

            if(parseFloat(frequency) <= 0){
                alertx("频率不能小于零！");
                return false;
            }
            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkAutoTradeSetting/">自动交易列表</a></li>
		<li class="active"><a href="${ctx}/market/mkAutoTradeSetting/form?id=${mkAutoTradeSetting.id}">自动交易<shiro:hasPermission name="market:mkAutoTradeSetting:edit">${not empty mkAutoTradeSetting.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkAutoTradeSetting:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkAutoTradeSetting" action="${ctx}/market/mkAutoTradeSetting/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">买卖类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账户：</label>
			<div class="controls">
				<form:hidden path="userId" id="userId"/>
				<form:hidden path="mail" id="mail"/>
				<form:hidden path="mobile" id="mobile"/>
				<form:hidden path="realName" id="realName"/>
				<form:hidden path="idCard" id="idCard"/>
				<form:input path="username" id="usename" readonly="true" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账户密码：</label>
			<div class="controls">
				<input type="password" style="display: none"/>
				<form:input path="pass" type="password" htmlEscape="false" class="input-xlarge required"/>
				<input id="btnLogin" class="btn" type="button" value="验证登录" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">交易币币对：</label>
			<div class="controls">
				<form:hidden path="exPairSymbol" id="exPairSymbol"/>
				<form:select path="exPairId" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${exPairList}" itemLabel="pairSymbol" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主区币：</label>
			<div class="controls">
				<form:input path="coinMainSymbol" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<%--<form:hidden path="coinMainSymbol" id="coinMainSymbol"/>--%>
				<%--<form:select path="coinMainId" class="input-xlarge required" id="selectCoinMain">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">被交易币：</label>
			<div class="controls">
				<form:input path="coinOtherSymbol" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<%--<form:hidden path="coinOtherSymbol" id="coinOtherSymbol"/>--%>
				<%--<form:select path="coinOtherId" class="input-xlarge required" id="selectCoinOther">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${mkAutoTradeSetting.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${mkAutoTradeSetting.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最小成交量：</label>
			<div class="controls">
				<form:input path="minVolume" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最大成交量：</label>
			<div class="controls">
				<form:input path="maxVolume" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最低价：</label>
			<div class="controls">
				<form:input path="minPrice" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最高价：</label>
			<div class="controls">
				<form:input path="maxPrice" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">频率：</label>
			<div class="controls">
				<form:input path="frequency" htmlEscape="false" maxlength="10" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">*每隔多长时间下一单</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间单位：</label>
			<div class="controls">
				<form:select path="timeUnit" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkTimeUnit')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkAutoTradeSetting:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>