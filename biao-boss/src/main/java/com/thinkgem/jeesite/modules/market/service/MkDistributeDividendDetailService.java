/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividendDetail;
import com.thinkgem.jeesite.modules.market.dao.MkDistributeDividendDetailDao;

/**
 * 分红规则明细Service
 * @author zhangzijun
 * @version 2018-07-16
 */
@Service
@Transactional(readOnly = true)
public class MkDistributeDividendDetailService extends CrudService<MkDistributeDividendDetailDao, MkDistributeDividendDetail> {

	public MkDistributeDividendDetail get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributeDividendDetail> findList(MkDistributeDividendDetail mkDistributeDividendDetail) {
		return super.findList(mkDistributeDividendDetail);
	}
	
	public Page<MkDistributeDividendDetail> findPage(Page<MkDistributeDividendDetail> page, MkDistributeDividendDetail mkDistributeDividendDetail) {
		return super.findPage(page, mkDistributeDividendDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributeDividendDetail mkDistributeDividendDetail) {
		super.save(mkDistributeDividendDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributeDividendDetail mkDistributeDividendDetail) {
		super.delete(mkDistributeDividendDetail);
	}

	@Transactional(readOnly = false)
	public void saveDetails(List<MkDistributeDividendDetail> mkDistributeDividendDetails){
		for (MkDistributeDividendDetail mkDistributeDividendDetail : mkDistributeDividendDetails){
			super.save(mkDistributeDividendDetail);
		}
	}

	public String checkDetails(List<MkDistributeDividendDetail> mkDistributeDividendDetails){
	    String result = "";
		BigDecimal oneHundred = new BigDecimal("100");
	    BigDecimal percentageSum = BigDecimal.ZERO;
        for (MkDistributeDividendDetail mkDistributeDividendDetail : mkDistributeDividendDetails){

            percentageSum = percentageSum.add(BigDecimal.valueOf(Double.valueOf(mkDistributeDividendDetail.getPercentage())));

            if("1".equals(mkDistributeDividendDetail.getAccountType())){
                mkDistributeDividendDetail.setUserId("");
                mkDistributeDividendDetail.setUsername("");
            }

            if("9".equals(mkDistributeDividendDetail.getAccountType())){
                if(StringUtils.isEmpty(mkDistributeDividendDetail.getUserId())){
                    result = "用户不能为空！";
                    break;
                }
            }
        }

        if(percentageSum.compareTo(oneHundred) > 0){
			result = "百分比总和不能超过100!";
		}

        return result;
    }

	@Transactional(readOnly = false)
	public void deleteById(String id) {
		dao.deleteById(id);
	}


}