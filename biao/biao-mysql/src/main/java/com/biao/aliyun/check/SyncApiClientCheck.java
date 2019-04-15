package com.biao.aliyun.check;

import com.alibaba.cloudapi.sdk.core.BaseApiClient;
import com.alibaba.cloudapi.sdk.core.BaseApiClientBuilder;
import com.alibaba.cloudapi.sdk.core.enums.Method;
import com.alibaba.cloudapi.sdk.core.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.core.enums.Scheme;
import com.alibaba.cloudapi.sdk.core.model.ApiCallBack;
import com.alibaba.cloudapi.sdk.core.model.ApiRequest;
import com.alibaba.cloudapi.sdk.core.model.ApiResponse;
import com.alibaba.cloudapi.sdk.core.model.BuilderParams;

public class SyncApiClientCheck extends BaseApiClient {

	public final static String GROUP_HOST = "safrvcert.market.alicloudapi.com";

	private SyncApiClientCheck(BuilderParams builderParams) {
		super(builderParams);
	}

	public static class Builder extends BaseApiClientBuilder<Builder, SyncApiClientCheck> {

		@Override
		protected SyncApiClientCheck build(BuilderParams params) {
			return new SyncApiClientCheck(params);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public void aSafrv_cert_check(String serviceParameters,ApiCallBack apiCallBack) {
		String _apiPath = "/safrv_cert_check";// 其他产品换 对应的path
		ApiRequest _apiRequest = new ApiRequest(Scheme.HTTP, Method.GET, GROUP_HOST, _apiPath);
		_apiRequest.addParam("serviceParameters", serviceParameters, ParamPosition.QUERY, true);
		asyncInvoke(_apiRequest,apiCallBack);
	}

	public ApiResponse safrv_cert_check(String serviceParameters) {
		String _apiPath = "/safrv_cert_check";// 其他产品换 对应的path
		ApiRequest _apiRequest = new ApiRequest(Scheme.HTTP, Method.GET, GROUP_HOST, _apiPath);
		_apiRequest.addParam("serviceParameters", serviceParameters, ParamPosition.QUERY, true);
		return syncInvoke(_apiRequest);
	}
}
