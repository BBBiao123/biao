ALTER TABLE `js_plat_user`
ADD COLUMN `country_sysid`  varchar(64) NULL DEFAULT NULL COMMENT '国家数据字典id' AFTER `is_registered_cs`,
ADD COLUMN `country_syscode`  varchar(32) NULL DEFAULT NULL COMMENT '国家数据字典编号' AFTER `country_sysid`,
ADD COLUMN `country_sysname`  varchar(255) NULL DEFAULT NULL COMMENT '国家数据字典名称' AFTER `country_syscode`;

