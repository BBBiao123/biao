package com.biao.lottery;

import com.biao.entity.register.UserRegisterLotteryRule;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  ""(Myth)
 */
@SuppressWarnings("all")
public class LotteryUtils {

    private static final Integer MAXSOPE = 100000000;

    public static void calAwardProbability(LotteryContext lottery, List<UserRegisterLotteryRule> lotteryItemList) {
        Integer codeScope = 1;
        for (UserRegisterLotteryRule item : lotteryItemList) {
            Integer nowScope = 1;
            Double awardProbability = item.getRatio();
            while (true) {
                Double test = awardProbability * nowScope;
                // 概率的精确度，调整到小数点后10位，概率太小等于不中奖，跳出
                if (test < 0.0000000001) {
                    break;
                }
                if ((test >= 1L && (test - test.longValue()) < 0.0001D) || nowScope >= MAXSOPE) {
                    if (nowScope > codeScope) {
                        // 设置中奖范围
                        codeScope = nowScope;
                    }
                    break;
                } else {
                    // 中奖数字范围以10倍进行增长
                    nowScope = nowScope * 10;
                }
            }
        }
        Integer winningStartCode = 0;
        Integer winningEndCode = winningStartCode;

        for (UserRegisterLotteryRule item : lotteryItemList) {
            Integer codeNum = (int) (item.getRatio() * codeScope); // 获得其四舍五入的整数值
            // 无人中奖时，将中奖的起始范围设置在随机数的范围之外
            if (codeNum == 0) {
                item.setAwardStartCode(codeScope + 1);
                item.setAwardEndCode(codeScope + 1);
            } else {
                item.setAwardStartCode(winningEndCode);
                item.setAwardEndCode(winningEndCode + codeNum - 1);
                winningEndCode = winningEndCode + codeNum;
            }
        }
        // 设置用户的中奖随机码信息
        lottery.setWinningStartCode(winningStartCode);
        lottery.setWinningEndCode(winningEndCode);
        lottery.setCodeScope(codeScope);
    }

    public static UserRegisterLotteryRule beginLottery(LotteryContext lottery, List<UserRegisterLotteryRule> lotteryItemList) {
        // 确定活动是否有效,如果活动无效则，直接抽奖失败
        Integer randomCode = ThreadLocalRandom.current().nextInt(lottery.getCodeScope());
        if (randomCode >= lottery.getWinningStartCode() && randomCode <= lottery.getWinningEndCode()) {
            for (UserRegisterLotteryRule item : lotteryItemList) {
                if (randomCode >= item.getAwardStartCode() && randomCode <= item.getAwardEndCode()) {
                    return item;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        List<UserRegisterLotteryRule> lotteryItemList = new ArrayList<UserRegisterLotteryRule>();
        UserRegisterLotteryRule awardItem1 = new UserRegisterLotteryRule();
        awardItem1.setRatio(0.25D);
        awardItem1.setName("1");
        lotteryItemList.add(awardItem1);

        UserRegisterLotteryRule awardItem2 = new UserRegisterLotteryRule();
        awardItem2.setRatio(0.25D);
        awardItem2.setName("2");
        lotteryItemList.add(awardItem2);

        UserRegisterLotteryRule awardItem3 = new UserRegisterLotteryRule();
        awardItem3.setRatio(0.5D);
        awardItem3.setName("3");
        lotteryItemList.add(awardItem3);

        LotteryContext lottery = new LotteryContext();
        LotteryUtils.calAwardProbability(lottery, lotteryItemList);
        System.out.println("抽奖活动中奖数字范围：[" + lottery.getWinningStartCode() + "," + lottery.getWinningEndCode() + ")");
        LotteryUtils.beginLottery(lottery, lotteryItemList);
        for (UserRegisterLotteryRule item : lotteryItemList) {
            System.out.println(" 中奖数字范围：[" + item.getAwardStartCode() + "," + item.getAwardEndCode() + "]");
        }
        Map<String, Integer> total = new HashMap<>(3);
        for (int i = 0; i < 1000000; i++) {
            UserRegisterLotteryRule award = LotteryUtils.beginLottery(lottery, lotteryItemList);
            final Integer integer = total.get(award.getName());
            if (Objects.isNull(integer)) {
                total.put(award.getName(), 1);
            } else {
                final int i1 = integer.intValue() + 1;
                total.put(award.getName(), i1);
            }
        }
        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            System.out.println("名称：" + entry.getKey() + ", count=" + entry.getValue());
        }
    }
}
