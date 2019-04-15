package com.biao.aliyun.check;

import com.alibaba.cloudapi.sdk.core.model.ApiCallBack;
import com.alibaba.cloudapi.sdk.core.model.ApiResponse;
import com.biao.util.JsonUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class SyncCardCheck {

	private SyncApiClientCheck syncClient = null;
	
	private static Logger logger = LoggerFactory.getLogger(SyncCardCheck.class);

	public SyncCardCheck(String appKey, String appSecret) {
		this.syncClient = SyncApiClientCheck.newBuilder().appKey(appKey) // 阿里云市场自动分配的AppKey。
				.appSecret(appSecret).build();
	}

	public String safrv_cert_checkStr(String aliyunUserId, String verifyKey, String realName, String idCard,
			String customerID) throws UnsupportedEncodingException {
		Map<String, String> param = new HashMap<>();
		param.put("__userId", aliyunUserId);
		param.put("verifyKey", verifyKey);

		Map<String, String> extData = new HashMap<>();
		extData.put("callBack", realName);
		extData.put("failCallBack", realName);
		extData.put("validator_attr_user_name", realName);
		extData.put("validator_attr_card_num", idCard);
		param.put("extData", JsonUtils.toJson(extData));
		param.put("customerID", customerID);
		ApiResponse response = syncClient.safrv_cert_check(JsonUtils.toJson(param));
		return new String(response.getBody(), "utf-8");
	}
	
	public boolean safrv_cert_checkBool(String aliyunUserId, String verifyKey, String realName, String idCard,
			String customerID) throws UnsupportedEncodingException {
		Map<String, String> param = new HashMap<>();
		param.put("__userId", aliyunUserId);
		param.put("verifyKey", verifyKey);

		Map<String, String> extData = new HashMap<>();
		extData.put("validator_attr_user_name", realName);
		extData.put("validator_attr_card_num", idCard);
		param.put("extData", JsonUtils.toJson(extData));
		param.put("customerID", customerID);
		logger.debug("请求阿里云身份证接口，request = {}",JsonUtils.toJson(extData));
		ApiResponse response = syncClient.safrv_cert_check(JsonUtils.toJson(param));
		return parseResult(new String(response.getBody(), "utf-8"));
	}
	
	/**
	 * response content = {"code":200,"value":{"verifyUrl":"","bizCode":0,"message":"success"},"message":"success"}
	 * response content = {"code":200,"value":{"bizCode":13053,"message":"姓名证件号校验不匹配"},"message":"姓名证件号校验不匹配"}
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean parseResult(String jsonString) {
		try {
			logger.debug("请求阿里云身份证接口，response = {}",jsonString);
			Gson gson = new Gson();
			Map<String, Object> mapData = gson.fromJson(jsonString, Map.class);
			String codeStatus = mapData.get("code").toString();
			Object valueData = mapData.get("value");
			if(Double.parseDouble(codeStatus)==200 && valueData instanceof Map) {
				Double code = Double.parseDouble(((Map)valueData).get("bizCode").toString());
				return code==0 ;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}

	public void aSafrv_cert_checkDemo(String aliyunUserId, String verifyKey, String realName, String idCard,
			String customerID, ApiCallBack apiCallBack) {
		Map<String, String> param = new HashMap<>();
		param.put("__userId", aliyunUserId);
		param.put("verifyKey", verifyKey);

		Map<String, String> extData = new HashMap<>();
		extData.put("validator_attr_user_name", realName);
		extData.put("validator_attr_card_num", idCard);
		param.put("extData", JsonUtils.toJson(extData));
		param.put("customerID", customerID);
		logger.debug("请求阿里云身份证接口，request = {}",JsonUtils.toJson(extData));
		syncClient.aSafrv_cert_check(JsonUtils.toJson(param), apiCallBack);
	}

	public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
		// StringUtils
		String appKey = "25311851";
		String appSecret = "3a793e35374540981524edae454fbcce";
		SyncCardCheck ss = new SyncCardCheck(appKey, appSecret);
		String verifyKey = "IVvScR6c4Zl7mW";

		String realNameTest = "林燕燕" ;
		String idCardNumTest = "460030199102286922" ;
		
		String realName = "刘志远" ;
		String idCardNum = "330182199101184310" ;
		//response content = {"code":200,"value":{"verifyUrl":"","bizCode":0,"message":"success"},"message":"success"}
		//response content = {"code":200,"value":{"bizCode":13053,"message":"姓名证件号校验不匹配"},"message":"姓名证件号校验不匹配"}
		boolean isTreu = ss.safrv_cert_checkBool("1772728882136172",verifyKey,realName,idCardNum,"123");
		System.out.println(isTreu);
		/*ss.aSafrv_cert_checkDemo("1772728882136172", verifyKey, "林燕", "460030199102286922", "123", new ApiCallBack() {
			@Override
			public void onResponse(ApiRequest request, ApiResponse response) {
				System.out.println("response code = " + response.getStatusCode());
				try {
					System.out.println("response content = " + new String(response.getBody(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println(request);
			}

			@Override
			public void onFailure(ApiRequest request, Exception e) {
				System.out.println("Exception code = " + e);
				System.out.println(request);
			}
		});*/
	}

}
