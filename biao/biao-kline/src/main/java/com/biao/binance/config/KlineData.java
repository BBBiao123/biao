package com.biao.binance.config;

import com.biao.util.JsonUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.KlineVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class KlineData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String klimeEnum ;
	
	private LocalDateTime klimeTime ;
	
	private String klineData ;
	
	private String coinMain ;
	
	private String coinMainId ;
	
	private String coinOther ;
	
	private String coinOtherId ;
	
	private LocalDateTime createTime ;
	
	public static KlineData createKlineData(String klimeEnum,LocalDateTime klimeTime,String klineData,ExPair exPair) {
		KlineData klineDataObject = new KlineData();
		klineDataObject.setId(SnowFlake.createSnowFlake().nextIdString());
		klineDataObject.setCreateTime(LocalDateTime.now());
		klineDataObject.setKlimeEnum(klimeEnum);
		klineDataObject.setKlimeTime(klimeTime);
		klineDataObject.setKlineData(klineData);
		klineDataObject.setCoinMain(exPair.getPairOne());
		klineDataObject.setCoinOther(exPair.getPairOther());
		klineDataObject.setCoinMainId(exPair.getCoinId());
		klineDataObject.setCoinOtherId(exPair.getOtherCoinId());
		return klineDataObject ;
	}
	
	public static KlineData createKlineData(String klimeEnum,LocalDateTime klimeTime,KlineVO klineData,ExPair exPair) {
		KlineData klineDataObject = new KlineData();
		klineDataObject.setId(SnowFlake.createSnowFlake().nextIdString());
		klineDataObject.setCreateTime(LocalDateTime.now());
		klineDataObject.setKlimeEnum(klimeEnum);
		klineDataObject.setKlimeTime(klimeTime);
		klineDataObject.setKlineData(JsonUtils.toJson(klineData));
		klineDataObject.setCoinMain(exPair.getPairOne());
		klineDataObject.setCoinOther(exPair.getPairOther());
		klineDataObject.setCoinMainId(exPair.getCoinId());
		klineDataObject.setCoinOtherId(exPair.getOtherCoinId());
		return klineDataObject ;
	}
	
}
