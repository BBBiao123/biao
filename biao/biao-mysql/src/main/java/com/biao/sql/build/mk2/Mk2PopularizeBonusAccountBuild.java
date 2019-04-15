package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusAccount;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeBonusAccountBuild extends BaseSqlBuild<Mk2PopularizeBonusAccount, Integer> {

    public static final String columns = " id,type,user_id,mail,mobile,id_card,real_name,create_date,update_date ";
}
