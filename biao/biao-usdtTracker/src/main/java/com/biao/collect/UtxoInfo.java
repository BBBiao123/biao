package com.biao.collect;

import lombok.Data;

@Data
public class UtxoInfo {

    private String txid;

    private Integer vout;

    private String scriptPubKey;

    private String amount;

}
