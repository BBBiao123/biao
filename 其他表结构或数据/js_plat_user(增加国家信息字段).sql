ALTER TABLE `js_plat_user`
ADD COLUMN `country_sysid`  varchar(64) NULL DEFAULT NULL COMMENT '���������ֵ�id' AFTER `is_registered_cs`,
ADD COLUMN `country_syscode`  varchar(32) NULL DEFAULT NULL COMMENT '���������ֵ���' AFTER `country_sysid`,
ADD COLUMN `country_sysname`  varchar(255) NULL DEFAULT NULL COMMENT '���������ֵ�����' AFTER `country_syscode`;

