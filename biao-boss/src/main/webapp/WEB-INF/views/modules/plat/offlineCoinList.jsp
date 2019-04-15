<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>c2c_coin管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/plat/offlineCoin/">c2c_coin列表</a></li>
    <shiro:hasPermission name="plat:offlineCoin:edit">
        <li><a href="${ctx}/plat/offlineCoin/form">c2c_coin添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="offlineCoin" action="${ctx}/plat/offlineCoin/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>币种标识：</label>
            <form:input path="symbol" htmlEscape="false" maxlength="11" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>c2c买卖币种标识</th>
        <th>币种id</th>
        <th>最大价格限制</th>
        <th>最小价格限制</th>
        <th>价格精度</th>
        <th>最大数量限制</th>
        <th>最小数量限制</th>
        <th>数量精度</th>
        <th>比例买入</th>
        <th>比例卖出</th>
        <th>手续费类型</th>
        <th>买入费率公式</th>
        <th>卖出费率公式</th>
        <th>每日价格增量</th>
        <th>是否开启转账功能</th>
        <shiro:hasPermission name="plat:offlineCoin:edit">
            <th>操作</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:offlineCoin:disable">
            <th>禁用</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:offlineCoin:disable">
            <th>c2c显示</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="offlineCoin">
        <tr>
            <td><a href="${ctx}/plat/offlineCoin/form?id=${offlineCoin.id}">
                    ${offlineCoin.symbol}
            </a></td>
            <td>
                    ${offlineCoin.coinId}
            </td>
            <td>
                    ${offlineCoin.maxPrice}
            </td>
            <td>
                    ${offlineCoin.minPrice}
            </td>
            <td>
                    ${offlineCoin.pointPrice}
            </td>

            <td>
                    ${offlineCoin.maxVolume}
            </td>
            <td>
                    ${offlineCoin.minVolume}
            </td>
            <td>
                    ${offlineCoin.pointVolume}
            </td>
            <td>
                    ${offlineCoin.buyFee}
            </td>
            <td>
                    ${offlineCoin.sellFee}
            </td>

            <td>
                <c:choose>
                   <c:when test="${offlineCoin.feeType==0}">不收 </c:when>
                   <c:when test="${offlineCoin.feeType==1}">固定</c:when>
                   <c:when test="${offlineCoin.feeType==2}">比例</c:when>
                   <c:otherwise>
                   </c:otherwise>
                </c:choose>
            </td>
            <td>
                    ${offlineCoin.buyFeeStep}
            </td>
            <td>
                    ${offlineCoin.sellFeeStep}
            </td>
            <td>
                    ${offlineCoin.dayIncPrice}
            </td>

            <td>
                <c:choose>
                    <c:when test="${offlineCoin.isChangeAccount==0}">是</c:when>
                    <c:when test="${offlineCoin.isChangeAccount==1}">否</c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </td>
            <shiro:hasPermission name="plat:offlineCoin:edit">
                <td>
                    <a href="${ctx}/plat/offlineCoin/form?id=${offlineCoin.id}">修改</a>
                        <%--<a href="${ctx}/plat/offlineCoin/delete?id=${offlineCoin.id}" onclick="return confirmx('确认要删除该c2c_coin吗？', this.href)">删除</a>--%>
                </td>
            </shiro:hasPermission>


            <shiro:hasPermission name="plat:offlineCoin:disable">
                <td>
                    <c:if test="${offlineCoin.disable== 0 || offlineCoin.disable==null}">
                        <a href="${ctx}/plat/offlineCoin/disable/1?id=${offlineCoin.id}"
                           onclick="return confirmx('确认要禁止当前币种交易吗？', this.href)">禁用</a>
                    </c:if>
                    <c:if test="${offlineCoin.disable==1}">
                        <a href="${ctx}/plat/offlineCoin/disable/0?id=${offlineCoin.id}"
                           onclick="return confirmx('确认要启用当前币种交易？？', this.href)">启用</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:offlineCoin:disable">
                <td>
                    <c:if test="${offlineCoin.isVolume== 0 || offlineCoin.isVolume==null}">
                        <a href="${ctx}/plat/offlineCoin/isVolume/1?id=${offlineCoin.id}"
                           onclick="return confirmx('确认要关闭当前币种资产显示？', this.href)">关闭</a>
                    </c:if>
                    <c:if test="${offlineCoin.isVolume==1}">
                        <a href="${ctx}/plat/offlineCoin/isVolume/0?id=${offlineCoin.id}"
                           onclick="return confirmx('确认要启用当前币种资产显示？？', this.href)">启用</a>
                    </c:if>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>