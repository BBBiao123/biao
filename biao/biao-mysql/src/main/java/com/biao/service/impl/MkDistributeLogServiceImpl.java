package com.biao.service.impl;

import com.biao.entity.MkDistributeLog;
import com.biao.mapper.MkDistributeLogDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.MkDistributeLogService;
import com.biao.vo.MkDistributeLogListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MkDistributeLogServiceImpl implements MkDistributeLogService {

    @Autowired
    private MkDistributeLogDao mkDistributeLogDao;

    @Override
    public MkDistributeLog findById(String id) {
        return null;
    }

    @Override
    public ResponsePage<MkDistributeLog> findPage(MkDistributeLogListVO mkDistributeLogListVO) {
        ResponsePage<MkDistributeLog> responsePage = new ResponsePage<>();
        Page<MkDistributeLog> page = PageHelper.startPage(mkDistributeLogListVO.getCurrentPage(), mkDistributeLogListVO.getShowCount());
        List<MkDistributeLog> data = null;
        if (StringUtils.isEmpty(mkDistributeLogListVO.getCoinId())) {
            data = mkDistributeLogDao.findDistributeLogByUserId(mkDistributeLogListVO.getUserId());
        } else {
            data = mkDistributeLogDao.findDistributeLogByUserIdAndCoinId(mkDistributeLogListVO.getUserId(), mkDistributeLogListVO.getCoinId());
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }
}
