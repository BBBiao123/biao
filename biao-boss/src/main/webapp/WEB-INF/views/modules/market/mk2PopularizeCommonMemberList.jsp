<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>普通用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#btnImport").click(function(){
                $.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true},
                    bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
            });
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/market/mk2PopularizeCommonMember/import" method="post" enctype="multipart/form-data"
			  class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/market/mk2PopularizeCommonMember/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/market/mk2PopularizeCommonMember/">普通用户列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit"><li><a href="${ctx}/market/mk2PopularizeCommonMember/form">普通用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeCommonMember" action="${ctx}/market/mk2PopularizeCommonMember/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>全部释放：</label>
				<form:select path="releaseOver" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeCommonMember.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeCommonMember.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnImport" class="btn btn-primary" type="button" value="导入"/></li>
			</li>

			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>类型</th>
				<th>邮箱</th>
				<th>手机号</th>
				<th>币种名称</th>
				<th>是否冻结</th>
				<th>冻结数量</th>
				<th>释放数量</th>
				<th>释放开始时间</th>
				<th>释放周期</th>
				<th>周期释放比例(百分比)</th>
				<th>全部释放</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeCommonMember">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeCommonMember/form?id=${mk2PopularizeCommonMember.id}">
					${mk2PopularizeCommonMember.userId}
				</a></td>
				<td>
					${mk2PopularizeCommonMember.type}
				</td>
				<td>
					${mk2PopularizeCommonMember.mail}
				</td>
				<td>
					${mk2PopularizeCommonMember.mobile}
				</td>
				<td>
					${mk2PopularizeCommonMember.coinSymbol}
				</td>
				<td>
					${mk2PopularizeCommonMember.lockStatus}
				</td>
				<td>
					${mk2PopularizeCommonMember.lockVolume}
				</td>
				<td>
					${mk2PopularizeCommonMember.releaseVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeCommonMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeCommonMember.releaseCycle, 'cycle', '')}
				</td>
				<td>
					${mk2PopularizeCommonMember.releaseCycleRatio}
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeCommonMember.releaseOver, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeCommonMember.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeCommonMember/form?id=${mk2PopularizeCommonMember.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeCommonMember/delete?id=${mk2PopularizeCommonMember.id}" onclick="return confirmx('确认要删除该普通用户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>