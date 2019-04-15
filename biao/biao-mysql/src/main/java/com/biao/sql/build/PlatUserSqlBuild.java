package com.biao.sql.build;

import com.biao.entity.PlatUser;
import com.biao.sql.BaseSqlBuild;

public class PlatUserSqlBuild extends BaseSqlBuild<PlatUser, Integer> {

    public static final String columns = "id,username,password,user_type,status,mobile,mail,ex_password,google_auth,sex,age,real_name,id_card,card_up_id,card_down_id,card_face_id,card_status,card_level,card_status_check_time,card_status_reason,nick_name,open_discount,remarks,country_id,country_code,invite_code,refer_invite_code,refer_id,alipay_no,alipay_qrcode_id,wechat_qrcode_id,wechat_no,ex_valid_type,tag,c2c_change,create_by,update_by,create_date,update_date,lock_date,receive_msg,is_registered_cs";

    public static final String simple_columns = "id,username,user_type,status,mobile,mail,google_auth,sex,age,real_name,card_status,card_level,card_status_check_time,nick_name,remarks,country_id,country_code,invite_code,refer_invite_code,refer_id,ex_valid_type,tag,create_date,update_date,lock_date,receive_msg,is_registered_cs";
}
