package com.biao.mapper;

import com.biao.entity.PlatUser;
import com.biao.entity.UserRelation;
import com.biao.sql.build.PlatUserSqlBuild;
import com.biao.sql.build.UserRelationSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlatUserDao {

    @InsertProvider(type = PlatUserSqlBuild.class, method = "insert")
    void insert(PlatUser platUser);

    @SelectProvider(type = PlatUserSqlBuild.class, method = "findById")
    PlatUser findById(String id);

    @UpdateProvider(type = PlatUserSqlBuild.class, method = "updateById")
    long updateById(PlatUser platUser);

    @Update("update js_plat_user set google_auth = #{googleAuth},update_date = #{updateDate} where id = #{id}")
    long updateGoogleAuthById(PlatUser platUser);

    @Select("select 1 from js_plat_user where username = #{username} limit 1")
    Long findExistByUsername(String username);

    @Select("select 1 from js_plat_user where id_card = #{idcard} and id != #{uid} limit 1")
    Long findExistByIdcard(@Param("idcard") String idcard, @Param("uid") String uid);

    @Select("select 1 from js_plat_user where card_up_id = #{imageName} or card_down_id = #{imageName} or card_face_id = #{imageName} limit 1")
    Long findExistByImages(@Param("imageName") String imageName);

    @Select("select " + PlatUserSqlBuild.simple_columns + " from js_plat_user where refer_id = #{userId} ORDER BY create_date DESC ")
    List<PlatUser> findInvitesById(String userId);

    @Select("select " + PlatUserSqlBuild.simple_columns + " FROM js_plat_user WHERE mobile IS NOT NULL AND id NOT IN (SELECT user_id FROM `js_plat_phone_geocoder`)")
    List<PlatUser> findAll();

    @Select("select max(invite_code) from js_plat_user")
    String findMaxInviteCode();

    @Select("select id from js_plat_user where invite_code = #{inviteCode} limit 1")
    String findIdByInviteCode(String inviteCode);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user where mobile = #{mobileOrMail} or mail = #{mobileOrMail} or username = #{mobileOrMail} limit 1")
    PlatUser findExistByMobieOrMail(String mobileOrMail);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user where mobile = #{mobile} limit 1")
    PlatUser findExistByMobie(String mobile);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user where mail = #{mail} limit 1")
    PlatUser findExistByMmail(String mail);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user where username = #{username}")
    PlatUser findByUsername(String username);

    @Update("update js_plat_user set alipay_no = #{alipayNo},alipay_qrcode_id = #{qrcodeId} where id = #{id}")
    long updateAlipay(@Param("id") String id, @Param("alipayNo") String alipayNo, @Param("qrcodeId") String qrcodeId);

    @Update("update js_plat_user set wechat_no = #{wechatNo},wechat_qrcode_id = #{qrcodeId} where id = #{id}")
    long updateWechatPay(@Param("id") String id, @Param("wechatNo") String wechatNo, @Param("qrcodeId") String qrcodeId);

    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user t WHERE t.card_status = 1 AND t.audit_date >= CONCAT(#{date},' 00:00:00') AND t.audit_date <= CONCAT(#{date},' 23:59:59') ")
    List<PlatUser> findYesterdayAudit(String date);

    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user t WHERE t.mobile_audit_date > #{lastDateTime} AND t.mobile_audit_date <=  #{curDateTime} and t.mobile_audit_date is not null and t.is_award = 0")
    List<PlatUser> findAuditByBeginAndEnd(@Param("lastDateTime") String lastDateTime, @Param("curDateTime") String curDateTime);

    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user t WHERE t.card_status = 1 AND t.audit_date <= CONCAT(#{date},' 23:59:59') ")
    List<PlatUser> findFormerAudit(String date);

    @Select("select id, user_id, username, parent_id, top_parent_id, tree_id, deth, level from mk_common_user_relation where user_id = #{userId} order by level asc")
    List<UserRelation> findUserRelationById(@Param("userId") String userId);

    @InsertProvider(type = UserRelationSqlBuild.class, method = "batchInsert")
    long batchInsertUserRelation(@Param("listValues") List<UserRelation> list);

    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user WHERE card_status in (${status})  ")
    List<PlatUser> findByIdcardStatus(@Param("status") String status);

    @Select("SELECT " + PlatUserSqlBuild.columns + " from js_plat_user u where u.refer_id = #{referId} and u.create_date > #{createDateTime} and u.create_date <= #{endDateTime}")
    List<PlatUser> findByReferIdAndDate(@Param("referId") String referId, @Param("createDateTime") LocalDateTime createDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user t WHERE t.tag = #{tag} ORDER BY t.create_date DESC ")
    List<PlatUser> findByTag(@Param("tag") String tag);

    @Select("SELECT count(1) FROM js_plat_user t WHERE t.tag = #{tag} ")
    long countByTag(@Param("tag") String tag);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user where mobile = #{mobileOrMail} or mail = #{mobileOrMail} limit 1")
    PlatUser findByMobieOrMail(@Param("mobileOrMail") String mobileOrMail);

    @Update("UPDATE js_plat_user SET tag = #{tag}, c2c_out = '1' WHERE id = #{id} AND IFNULL(tag, '') = '' ")
    long updateUserTag(PlatUser user);
    
    @Select("SELECT " + PlatUserSqlBuild.columns + " FROM js_plat_user WHERE card_status = #{cardStatus} and card_level = #{cardLevel} and (card_status_check_time< #{cardStatusCheckTime} or card_status_check_time is null) and country_code = #{contryCode}")
    List<PlatUser> findByNeedCardStatusChech(@Param("contryCode")String contryCode,@Param("cardLevel")Integer cardLevel,@Param("cardStatus")Integer cardStatus,@Param("cardStatusCheckTime")Integer cardStatusCheckTime);
    
}
