ALTER TABLE `js_plat_user_coin_balance`
ADD COLUMN `lock_flag`  smallint(6) NULL COMMENT '是否锁仓' AFTER `deposit_value`;

ALTER TABLE `js_plat_user_coin_balancechange`
ADD COLUMN `contract_time`  integer(10) NULL COMMENT '合约时长' AFTER `balance_id`;

ALTER TABLE `js_plat_user_coin_balance_count`
ADD COLUMN `coin_lock_balance`  decimal(32,16) DEFAULT 0.0000000000000000 COMMENT '锁仓市值' AFTER `differential_reward`;

ALTER TABLE `js_plat_user_coin_balance_count`
ADD COLUMN `statics_lock_income`  decimal(32,16) NULL DEFAULT 0.0000000000000000 AFTER `coin_lock_balance`;
 
ALTER TABLE `js_plat_user_coin_balance_count`
ADD COLUMN `coin_sum_balance`  decimal(32,16) NULL DEFAULT 0.0000000000000000 COMMENT '挖矿总市值' AFTER `coin_lock_balance`;










