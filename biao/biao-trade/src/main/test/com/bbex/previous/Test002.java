package com.biao.previous;

import java.math.BigDecimal;

public class Test002 {
    public static void main(String[] args) {
//        17.05474693000000030451701604761183261871337890625
//        17.05474693
        BigDecimal decimal1 = new BigDecimal(17.05474693000000030451701604761183261871337890625);
        BigDecimal decimal2 = new BigDecimal(17.05474693);
        BigDecimal subtract = decimal2.subtract(decimal1);
        System.out.println(subtract);
    }
}
