package com.thinkgem.jeesite.modules.disruptor;

import com.lmax.disruptor.EventFactory;

public class DisruptorData {

	public static DisruptorFactory FACTORY_INSTANCE = new DisruptorFactory();
	//定义数据结构
	//数据类型
	private Integer type ;
	
	private Object data ;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static void savePublish(Object data,Integer type) {
    	DisruptorManager.instance().runConfig();
    	DisruptorData disruptorData = new DisruptorData();
    	disruptorData.setType(type);
    	disruptorData.setData(data);
		DisruptorManager.instance().publishData(disruptorData);
    }
	
	
	public static class DisruptorFactory implements EventFactory<DisruptorData>{
		@Override
		public DisruptorData newInstance() {
			return new DisruptorData();
		}
		
	}
}

