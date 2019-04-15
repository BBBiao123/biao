package com.biao.service.impl;

import com.biao.disruptor.DisruptorData;
import com.biao.disruptor.DisruptorManager;
import com.biao.entity.PhoneGeocoder;
import com.biao.entity.PlatUser;
import com.biao.entity.SysArea;
import com.biao.mapper.PhoneGeocoderDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.SysAreaDao;
import com.biao.phone.utils.PhoneUtil;
import com.biao.service.PlatUserService;
import com.biao.service.UserPhoneGeocoderService;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("userPhoneGeocoderService")
public class UserPhoneGeocoderServiceImpl implements UserPhoneGeocoderService {

    private static final Logger logger = LoggerFactory.getLogger(UserPhoneGeocoderServiceImpl.class);

    @Autowired
    private PlatUserService platUserService;
    @Autowired
    private PlatUserDao platUserDao;
    @Autowired
    private SysAreaDao sysAreaDao;

    @Autowired
    private PhoneGeocoderDao phoneGeocoderDao;

    @Override
    public void insert(PhoneGeocoder phoneGeocoder) {
        if (StringUtils.isNotBlank(phoneGeocoder.getReferId())) {
            //设置其所有父id
            PhoneGeocoder geocoderParent = phoneGeocoderDao.findByUserId(phoneGeocoder.getReferId());
            if (geocoderParent != null) {
                StringBuilder builder = new StringBuilder(geocoderParent.getUserParents());
                builder.append("," + phoneGeocoder.getReferId());
                phoneGeocoder.setUserParents(builder.toString());
            } else {
                //通过platUser判断是否是顶级用户
                StringBuilder builder = new StringBuilder();
                buildParentUserIds(phoneGeocoder.getReferId(), builder);
                //增加直接父id
                builder.append("," + phoneGeocoder.getReferId());
                phoneGeocoder.setUserParents(builder.toString());
            }
            phoneGeocoder.setUserParent(phoneGeocoder.getReferId());
        } else {
            phoneGeocoder.setUserParents("0");
            phoneGeocoder.setUserParent("0");
        }
        String id = SnowFlake.createSnowFlake().nextIdString();
        phoneGeocoder.setId(id);
        //100000000 代表中国
        SysArea provinceSysArea = sysAreaDao.findByNameAndParentId(phoneGeocoder.getProvinceName(), "100000000");
        if (provinceSysArea != null) {
            phoneGeocoder.setProvinceId(provinceSysArea.getId());
            if (PhoneUtil.isSpecialProvince(phoneGeocoder.getProvinceName())) {
                phoneGeocoder.setCityId(provinceSysArea.getId());
            } else {
                SysArea citySysArea = sysAreaDao.findByNameAndParentId(phoneGeocoder.getCityName(), provinceSysArea.getId());
                if (citySysArea != null) {
                    phoneGeocoder.setCityId(citySysArea.getId());
                }
            }
        }
        phoneGeocoder.setCreateBy(phoneGeocoder.getUserId());
        phoneGeocoder.setCreateDate(LocalDateTime.now());
        phoneGeocoderDao.insert(phoneGeocoder);
    }

    /**
     * 递归查询所有的上级用户
     *
     * @param referId
     * @return
     */
    private void buildParentUserIds(String referId, StringBuilder builder) {
        if (StringUtils.isBlank(referId)) {
            builder.append("0");
            return;
        }
        PlatUser platUser = platUserService.findById(referId);
        if (platUser != null) {
            buildParentUserIds(platUser.getReferId(), builder);
            if (StringUtils.isNotBlank(platUser.getReferId())) {
                builder.append("," + platUser.getReferId());
            }
        } else {
            builder.append(referId);
        }
    }

    @Override
    public void onceTaskCompletionInsert() {
        List<PlatUser> platUsers = platUserDao.findAll();
        if (!CollectionUtils.isEmpty(platUsers)) {
            List<PlatUser> mobileUsers = platUsers.stream().filter(platUser -> StringUtils.isNotBlank(platUser.getMobile())).collect(Collectors.toList());
            logger.info("onceTaskCompletionInsert size:{}", mobileUsers.size());
            for (PlatUser platUser : mobileUsers) {
                //如果存在，忽略
                PhoneGeocoder geocoder = phoneGeocoderDao.findByUserId(platUser.getId());
                if (geocoder == null) {
                    DisruptorManager.instance().runConfig();
                    DisruptorData data = new DisruptorData();
                    data.setType(3);
                    data.setPlatUser(platUser);
                    DisruptorManager.instance().publishData(data);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
