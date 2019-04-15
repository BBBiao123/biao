package com.biao.enums;

/**
 *
 * @date 2018/4/15
 */
public class OrderEnum {
    /**
     * 是否可取消状态标识；
     */
    public enum OrderFlag {
        /**
         * 可取消
         */
        YES,
        /**
         * 不可取消
         */
        NO
    }

    /**
     * 订单处理的状态
     */
    public enum OrderStatus {

        /**
         * 未成功
         */
        NOT_SUCCESS(0) {
            @Override
            public OrderStatus cancelChangeStatus() {
                return ALL_CANCEL;
            }
        },
        /**
         * 部分成功
         */
        PART_SUCCESS(1) {
            @Override
            public OrderStatus cancelChangeStatus() {
                return PART_CANCEL;
            }
        },

        /**
         * 全部成功；
         */
        ALL_SUCCESS(2),
        /**
         * 部分取消
         */
        PART_CANCEL(3),
        /**
         * 全部取消；
         */
        ALL_CANCEL(4);

        private Integer code;

        /**
         * 获取一个code;
         *
         * @return code;
         */
        public Integer getCode() {
            return code;
        }

        OrderStatus(Integer code) {
            this.code = code;
        }

        /**
         * 返回一个可取消后的修改状态；
         *
         * @return 状态；
         */
        public OrderStatus cancelChangeStatus() {
            return this;
        }

        /**
         * 把数字转换成状态；
         *
         * @param status status;
         * @return 状态；
         */
        public static OrderStatus valueOf(int status) {
            switch (status) {
                case 0:
                    return OrderStatus.NOT_SUCCESS;
                case 1:
                    return OrderStatus.PART_SUCCESS;
                case 2:
                    return OrderStatus.ALL_SUCCESS;
                case 3:
                    return OrderStatus.PART_CANCEL;
                case 4:
                    return OrderStatus.ALL_CANCEL;
                default:
                    return OrderStatus.NOT_SUCCESS;
            }
        }
    }

}
