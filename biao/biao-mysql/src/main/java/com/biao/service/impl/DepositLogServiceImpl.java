package com.biao.service.impl;

import com.biao.entity.DepositLog;
import com.biao.mapper.DepositLogDao;
import com.biao.pojo.DepdrawLogVO;
import com.biao.pojo.ResponsePage;
import com.biao.service.DepositLogService;
import com.biao.util.SnowFlake;
import com.biao.vo.DepositListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepositLogServiceImpl implements DepositLogService {

    @Autowired
    private DepositLogDao depositLogDao;

    @Override
    public String save(DepositLog depositLog) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        depositLog.setStatus(0);
        depositLog.setCreateDate(LocalDateTime.now());
        depositLog.setId(id);
        depositLogDao.insert(depositLog);
        return id;
    }

    @Override
    public void updateById(DepositLog depositLog) {
    }

    @Override
    public ResponsePage<DepositLog> findPage(DepositListVO depositListVO) {
        ResponsePage<DepositLog> responsePage = new ResponsePage<>();
        Page<DepositLog> page = PageHelper.startPage(depositListVO.getCurrentPage(), depositListVO.getShowCount());
        List<DepositLog> data = null;
        if (StringUtils.isEmpty(depositListVO.getCoinId())) {

            data = depositLogDao.findDepositLogListByUserId(depositListVO.getUserId());
        } else {
            data = depositLogDao.findDepositLogListByUserIdAndCoinId(depositListVO.getUserId(), depositListVO.getCoinId());

        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public DepositLog findById(String id) {
        return depositLogDao.findById(id);
    }

    @Override
    public ResponsePage<DepdrawLogVO> findDepdrawLogPage(DepositListVO depositListVO) {
        ResponsePage<DepdrawLogVO> responsePage = new ResponsePage<>();
        Page<DepdrawLogVO> page = PageHelper.startPage(depositListVO.getCurrentPage(), depositListVO.getShowCount());
        List<DepdrawLogVO> data = null;
        if (StringUtils.isEmpty(depositListVO.getCoinId())) {
            data = depositLogDao.findDepdrawLogListByUserId(depositListVO.getUserId());
        } else {
            data = depositLogDao.findDepdrawLogListByUserIdAndCoinId(depositListVO.getUserId(), depositListVO.getCoinId());
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }
}
