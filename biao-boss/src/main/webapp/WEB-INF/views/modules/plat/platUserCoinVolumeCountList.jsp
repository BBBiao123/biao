<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>持币数量统计管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnQueryBtn').on('click', function () {
			    if ( ! $("#beginDate").val() && ! $("#endDate").val()) {
					alertx("请输入查询时间区域");
			        return;
				}
                $.ajax({
                    type: "POST",
                    async: false,
                    url: $("#searchChartForm").attr("action"),
                    beforeSend: function() { loading('正在提交，请稍等...'); },
                    data: {beginDate: $("#beginDate").val(), endDate: $("#endDate").val()},// 要提交的表单
                    success: function (msg) {
                        //var resultObj = $.parseJSON(msg);
                        debugger;
                        closeLoading();
                        showPersonLine(msg);
                        showVolumeLine(msg);

                    },
                    error: function (error) {
                        closeLoading();
                        $.jBox.error("输入参数错误");
                        console.log(error.responseText); // 日志输出控制台
                    }
                });
            });
			
			function showPersonLine(showData) {
			    var series = [];
				for (var areaType in showData.personMap) {
					var serie = {};
                    serie.name = areaType;
                    serie.type = 'line';
                    serie.data = showData.personMap[areaType];
                    series.push(serie);
				}
                var personLineChart = echarts.init(document.getElementById('personLineChart'));
                option = {
                    title: {
                        text: '人数'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: showData.typeList
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            magicType: {show: true, type: ['stack', 'tiled']},
                            saveAsImage: {show: true}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: showData.dateList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: series
                };
                personLineChart.setOption(option);
            }

            function showVolumeLine(showData) {
                var series = [];
                for (var areaType in showData.coinMap) {
                    var serie = {};
                    serie.name = areaType;
                    serie.type = 'line';
                    serie.data = showData.coinMap[areaType];
                    series.push(serie);
                }
                var volumeLineChart = echarts.init(document.getElementById('volumeLineChart'));
                option = {
                    title: {
                        text: '持币'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: showData.typeList
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            magicType: {show: true, type: ['stack', 'tiled']},
                            saveAsImage: {show: true}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: showData.dateList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: series
                };
                volumeLineChart.setOption(option);
            }
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/platUserCoinVolumeCount/">持币数量统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="platUserCoinVolumeCount" action="${ctx}/plat/platUserCoinVolumeCount/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>类型</th>
			<th>人数</th>
			<th>持币数量</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${countList}" var="platUserCoinVolumeCount">
			<tr>
				<td>
						${platUserCoinVolumeCount.type}
				</td>
				<td>
						${platUserCoinVolumeCount.personCount}
				</td>
				<td>
						${platUserCoinVolumeCount.holdCoinVolume}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<br/>
	<form:form id="searchChartForm" action="${ctx}/plat/platUserCoinVolumeCount/queryByDate" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>送币时间：</label>
				<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${mk2PopularizeTaskLog.beginCreateDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${mk2PopularizeTaskLog.endCreateDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnQueryBtn" class="btn btn-primary" type="button" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>

	<div id="personLineChart" style="width: 1500px;height:400px;"></div>
	<br/>
	<div id="volumeLineChart" style="width: 1500px;height:400px;"></div>
</body>
</html>