package com.biao.service.otc;

import com.biao.vo.otc.OtcVolumeChangeQueryVO;
import com.biao.vo.otc.OtcVolumeChangeRequestResultVO;
import com.biao.vo.otc.OtcVolumeChangeRequestVO;
import com.biao.vo.otc.OtcVolumeChangeResultVO;

public interface OtcVolumeChangeService {

    OtcVolumeChangeRequestResultVO change(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO);

    OtcVolumeChangeResultVO findByBatchNo(OtcVolumeChangeQueryVO otcVolumeChangeQueryVO);

}
