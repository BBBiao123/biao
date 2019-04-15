<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币资产同步</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function synUserRedisVolume(userId,coinId,symbol,redisVolume,volume,redisLockVolume,lockVolume) {
			var content = "<div  style='text-align: center; font-size: 18px; margin-top: 30px'><div style='text-align: left; padding-left:150px'>币种:"+symbol+"<br/><br/> redis资产:"+redisVolume+"<br/><br/> 数据库资产:"+volume+"<br/><br/>redis锁定资产:"+redisLockVolume+"<br/><br/> 数据库锁定资产:"+lockVolume+"</div><input style='margin-top: 30px;width:320px' type='text' name='userRedisVolume' id='userRedisVolume' placeholder='redis需要同步的资产'/><br/><input style='margin-top: 10px;width:320px' type='text' name='userRedisLockVolume' id='userRedisLockVolume' placeholder='redis需要同步的锁定资产'/></div>" ;
			$.jBox.open(content,"同步资产",640, 420, { buttons: { '同步': 1},submit: function (v, h, f){
				var success = false ;
        		var userRedisVolume = $("#userRedisVolume").val();
        		var userRedisLockVolume = $("#userRedisLockVolume").val();
        		console.info("userRedisVolume:"+userRedisVolume);
        		if (v == 1) {
        			$.ajax({
                        type: "POST",
                        async: false,
                        url: "${ctx}/plat/synVolume/save",
                        data:$.param({"userId":userId,"volume":userRedisVolume,"lockVolume":userRedisLockVolume,"coinId":coinId,"symbol":symbol}),
                        success: function (msg) {
                        	var resultObj = $.parseJSON(msg);
                            debugger;
                            if(resultObj.success==1){
                            	 success = true ;
                                 $("#searchForm").submit();
                            }else{
                            	success = false ;
                            	$.jBox.alert("拿不到redis的锁");
                            }
                        },
                        error: function (error) {
                            $.jBox.alert("输入参数错误");
                            console.log(error.responseText); // 日志输出控制台
                        }
                    });
        		}
        		return success;
			},error: function (error) {
                $.jBox.alert("输入参数错误");
                console.log(error.responseText); // 日志输出控制台
            }});	
        };
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/synVolume/">币币资产同步</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="synVolume" action="${ctx}/plat/synVolume/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
			     <form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户id</th>
				<th>用户邮箱</th>
				<th>用户手机</th>
				<th>币种id</th>
				<th>币种符号</th>
				<th>redis资产</th>
				<th>数据库资产</th>
				<th>redis锁定资产</th>
				<th>数据库锁定资产</th>
				<shiro:hasPermission name="plat:synVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${userCoinVolumes}" var="userCoinVolume">
        <tr>
            <td>
                ${userCoinVolume.userId}
            </td>
            <td>
                ${queryPlat.mail}
            </td>
            <td>
                ${queryPlat.mobile}
            </td>
            <td>
                ${userCoinVolume.coinId}
            </td>
            <td>
                ${userCoinVolume.coinSymbol}
            </td>
            <td>
                ${userCoinVolume.redisVolume}
            </td>
            <td>
                ${userCoinVolume.volume}
            </td>
            <td>
                ${userCoinVolume.redisLockVolume}
            </td>
            <td>
                ${userCoinVolume.lockVolume}
            </td>
            <td>
            <shiro:hasPermission name="plat:synVolume:edit">
            	<a href="#" onclick="synUserRedisVolume('${userCoinVolume.userId}', '${userCoinVolume.coinId}', '${userCoinVolume.coinSymbol}', '${userCoinVolume.redisVolume}', '${userCoinVolume.volume}', '${userCoinVolume.redisLockVolume}', '${userCoinVolume.lockVolume}')">同步资产</a>
             </shiro:hasPermission>
            </td>
         </tr>
    	</c:forEach>
		</tbody>
	</table>
</body>
</html>