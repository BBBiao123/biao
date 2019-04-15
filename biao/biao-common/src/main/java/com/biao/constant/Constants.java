package com.biao.constant;

/**
 * <p>Description: .</p>
 *
 *  ""(Myth)
 * @version 1.0
 * @date 2018/3/31 17:21
 * @since JDK 1.8
 */
public interface Constants {

    Integer SUCCESS_CODE = 10000000;

    int GLOBAL_ERROR_CODE = 10001000;

    Integer ADD_VALID_CODE_ERROR = 10001001;
    //登录错误次数过多
    Integer LOGIN_ERROR_MORE_ERROR = 10004001;
    //请输入验证码
    Integer VALIDCODE_NOT_NULL_ERROR = 10004002;
    //验证码无效,请刷新验证码
    Integer VALIDCODE_HAS_EXPIRE_ERROR = 10004003;
    //验证码输入错误
    Integer VALIDCODE_IS_ERROR = 10004004;
    //用户名已经存在
    Integer USERNAME_EXIST_ERROR = 10004005;

    Integer USER_IS_NULL_ERROR = 10004006;

    //验证密码错误
    Integer USER_VALID_PASSWORD_ERROR = 10004007;

    //验证密码一样
    Integer USER_VALID_REPASSWORD_ERROR = 10004008;

    //c2c发布广告 没有足够资产
    Integer OFFLINE_VOLUME_ERROR = 10004009;
    //参数错误
    int PARAM_ERROR = 10004010;

    String JOIN = "_";

    //重置密码token失效
    Integer RESET_PASSWORD_TOKEN_EXPIRE_ERROR = 10004011;

    Integer MESSAGE_TEMPLATE_NULL_ERROR = 10004012;

    //用户身份认证已经通话
    Integer USER_CARD_PASS_ERROR = 10004013;

    //银行卡号已经存在
    Integer USER_CARD_EXSIT_ERROR = 10004014;

    //操作 非法
    Integer OPERRATION_ERROR = 10004015;

    //自己买入自己错误
    Integer BUY_OWN_ERROR = 10004016;

    //请进行身份认证
    Integer IDENTIRY_ERROR = 10004017;

    //请想绑定银行卡
    Integer NO_BANK_ERROR = 10004018;

    //请想绑定手机号
    Integer NO_MOBILE_ERROR = 10005036;

    //邀请码冲突
    Integer INVOTE_CODE_SYNC_ERROR = 10004019;

    //未完成的买单
    Integer NOT_PAY_ERROR = 10004020;
    //未完成Google认证
    Integer NOT_GOOGLE_ERROR = 10004021;
    //用户资产不足
    Integer USER_VOLUME_NOT_ENOUGH_ERROR = 10004022;
    //提现失败
    Integer WITHDRAW_ERROR = 10004023;
    //当天禁止发广告 由于取消次数太多
    Integer PUBLISH_ADVERT_ERROR = 10004024;
    //当天禁止撤回广告 由于取消次数太多
    Integer CANCEL_ADVERT_ERROR = 10004025;
    //系统手续费设置有问题
    Integer WITHDRAW_FEE_ERROR = 10004026;

    Integer WITHDRAW_VOLUME_TOOLOW_ERROR = 10004027;

    //取消提现失败
    Integer WITHDRAW_CANCEL_ERROR = 10004028;

    //一次提现最大额度错误
    Integer WITHDRAW_MAX_ERROR = 10004029;

    //当天提现最大额度错误
    Integer WITHDRAW_DAY_MAX_ERROR = 10004030;

    //一次提现最小额度错误
    Integer WITHDRAW_MIN_ERROR = 10004031;

    //GOOGLE验证码错误
    Integer GOOGLE_ERROR = 10004032;

    //卖家取消订单错误
    int SELLER_CANCLE_ORDER_ERROR = 10004033;

    //更新失败
    Integer UPDATE_ERROR = 10004034;

    //订单存在申诉单，不允许操作
    Integer SUBORDER_EXISTS_APPEAL = 10004035;

    //营销定时任务失败
    Integer DISTRIBUTE_TASK_ERROR = 10004036;

    //冻结资产异常
    Integer LOCK_VOLUME_ERROR = 10004037;

    //用户名锁定
    Integer USERNAME_LOCKED_ERROR = 10004039;

    //用户名不可用
    Integer USERNAME_DISABLED_ERROR = 10004040;

    //接力撞奖异常
    Integer RELAY_PRIZE_ERROR = 10004041;

    Integer TRADE_C2C_NEED_VALID_ERROR = 10005041;

    Integer TRADE_C2C_OVER_TIMES_ERROR = 10005042;

    Integer OFFLINE_COIN_NOT_EXSIT_ERROR = 10005043;

    //保证金 操作 非法
    Integer BAIL_OPERRATION_ERROR = 10005044;

    Integer OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR = 10005045;

    Integer VOLUME_IN_OUT_ERROR = 10005046;

    //C2C转账
    Integer PLAT_USER_CHANGE_FORBIDDEN = 10006001;
    Integer MY_VOLUME_LIMIT_OVER = 10006002;

    Integer OFFLINE_BUY_VOLUME_ERROR = 10006003;

    Integer OFFLINE_SELL_VOLUME_ERROR = 10006004;

    Integer OFFLINE_COIN_MIN_VOLUME_ERROR = 10006005;

    Integer OFFLINE_COIN_MAX_VOLUME_ERROR = 10006005;

    Integer OFFLINE_ORDER_CANCEL_MORE_ERROR = 10006006;

    Integer COIN_OUT_ERROR = 10006007;


    //参数错误未在红包活动时间范围内
    Integer MK_RED_ENVELOPE_FORBIDDEN = 10007001;
    //已结束
    Integer MK_RED_ENVELOPE_END = 10007002;
    //已领取完/手慢了
    Integer MK_RED_ENVELOPE_FINISH = 10007003;

    Integer COMMON_ERROR_CODE = -2;

    //需要登录
    Integer NEED_LOGIN_CODE = -5;

    //用户被踢出
    Integer USER_TICKETOUT_CODE = -8;

    //用户交易需要谷歌验证
    Integer USER_TRADE_GOOGLE_CODE = -9;

    Integer USER_TRADE_VALID_TYPE_CODE = 10005049;

    long REDIS_EXPIRE_TIME_ONE_HOUR = 3600;

    String WS_PLAT = "/biao/websocket/plat";

    String WS_BUY_SELL = "/biao/websocket/buyAndSell";

    String WS_USER_ORDER = "/biao/websocket/userOrder";

    String WS_USER_VOLUME = "/biao/websocket/userVolume";

    String WS_KLINE = "/biao/websocket/kline";

    String WS_C2C = "/biao/websocket/c2cUser";

    String WS_HOME = "/biao/websocket/home";

    String WS_SHOW = "/biao/websocket/showView";

    String WS_RELAY_PRIZE = "/biao/websocket/relay";

    String FLOWING_WATER = "/biao/websocket/flowingWater";

    /**
     * 邮箱验证规则
     */
    String EMAIL_PATTERN = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";

    /**
     * 手机号验证规则
     */
    String MOBILE_PATTERN = "^(1[3|4|5|7|8])[0-9]{9}$";

    /**
     * 18位身份证|15位身份证
     */
    //String IDCRAD_PATTERN                     = "(^(\\d{17})(\\d|[xX])$)?";
    //String IDCRAD_PATTERN                     = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X])$)$";
    String IDCRAD_PATTERN = "^(\\d{15}|\\d{17}[\\dX])$";

    /**
     * 密码（包含数字和字母）  采用密码验证正则
     */
    String PASSWORD_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";

    /**
     * 密码（包含数字、字母和任意特殊字符两种及以上）
     */
    String PASSWORD_PATTERN2 = "^(?![A-z]+$)(?!\\d+$)(?![\\W]+$)\\S{8,20}$";

    /**
     * 密码（包含数字、字母和（~!@#$%^&*()_~！@#￥%……&*（）——）中的特殊字符中的两种及以上）
     */
    String PASSWORD_PATTERN3 = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![~!@#$%^&*()_~！@#￥%……&*（）——]+$)[A-z\\d~!@#$%^&*()_~！@#￥%……&*（）——]{8,20}$";

    /**
     * 密码（包含数字、字母和（~!@#$%^&*()_~！@#￥%……&*（）——）中的特殊字符三种以上)
     */
    String PASSWORD_PATTERN4 = "^(?=.*[A-z])(?=.*\\d)(?=.*[~!@#$%^&*()_~！@#￥%……&*（）——])[A-z\\d~!@#$%^&*()_~！@#￥%……&*（）——]{8,20}$";

    //String PASSWORD_PATTERN5                  = "^(?![0-9]+$)(?![A-z]+$)(?![^A-z0-9]+$)^.{8,20}$";

    /**
     * 不能含有特殊字符
     */
    String NAME_PATTERN = "^([a-zA-Z0-9\u4e00-\u9fa5()\\-（）——]*)$";

    String BASE_COIN = "BTC,BCC,ETH,ETC";

    String PING = "ping";

    String PONG = "pong";
}
