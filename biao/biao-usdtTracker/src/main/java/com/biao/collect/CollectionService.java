package com.biao.collect;

import com.alibaba.fastjson.JSON;
import com.biao.config.OmniConfig;
import com.biao.constant.UsdtConstants;
import com.biao.entity.DepositLog;
import com.biao.mapper.CoinCollectionDao;
import com.biao.mapper.DepositLogDao;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class CollectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionService.class);

    private final JsonRpcHttpClient client;

    private final DepositLogDao depositLogDao;

    private final OmniConfig omniConfig;

    private final CoinCollectionDao coinCollectionDao;

    @Autowired(required = false)
    public CollectionService(final DepositLogDao depositLogDao,
                             final JsonRpcHttpClient client,
                             final OmniConfig omniConfig, CoinCollectionDao coinCollectionDao) {
        this.depositLogDao = depositLogDao;
        this.client = client;
        this.omniConfig = omniConfig;
        this.coinCollectionDao = coinCollectionDao;
    }


    /**
     * Collect task.
     */
    public void collectTask() {
//        final List<CoinCollection> cos = coinCollectionDao.findBySymbol(UsdtConstants.USDT_SMYBOL);
        final List<DepositLog> depositLogList = depositLogDao.findAllByCoinSymbolAndRaiseStatus(UsdtConstants.USDT_SMYBOL, 0);
        if (CollectionUtils.isNotEmpty(depositLogList)) {

            LOGGER.info("=========归集条数为===={}", depositLogList.size());
            for (DepositLog cs : depositLogList) {
                //如果就是归集地址本身，不需要做归集操作
                if (cs.getAddress().equals(omniConfig.getCollectAddr())) {
                    depositLogDao.updateRaiseStatusSuccess(cs.getUserId(), UsdtConstants.USDT_SMYBOL);
                    continue;
                }

                final String txid = collect(cs.getAddress(), omniConfig.getFeeAddr(), omniConfig.getCollectAddr(),
                        cs.getVolume().toPlainString(), omniConfig.getCollectFee());
                if (StringUtils.isNoneBlank(txid)) {
//                    coinCollectionDao.updateStatusById(cs.getId());
                    // 更新归集归集日志为 2
                    depositLogDao.updateRaiseStatusSuccess(cs.getUserId(), UsdtConstants.USDT_SMYBOL);
                    LOGGER.info("=====归集成功,返回txid:{},归集记录为:==========={}", txid,
                            cs.getId());
                } else {
                    depositLogDao.updateRaiseStatusFail(cs.getUserId(), UsdtConstants.USDT_SMYBOL);
                    LOGGER.info("=====归集失败======= ");
                }

            }
        }
    }

    private String collect(final String fromaddr, final String feeaddr, final String toaddr, final String amount, final Double fee) {
        try {
            //查询USDT地址UTXO
            List<UtxoInfo> listunspent = client.invoke("listunspent", new Object[]{0, 99999999, new String[]{fromaddr}}, ArrayList.class);
            LOGGER.info(" from address : " + fromaddr + "  toaddr : " + toaddr);
            LOGGER.info(" listtunspent.size : " + listunspent.size());
            //查询BTC地址UTXO
            // 自己拼接input,USDT地址放在第一个，旷工费地址放在下面就可以了 凌晨3点钟效率最高转账
            List<UtxoInfo> arrayList = client.invoke("listunspent", new Object[]{0, 99999999, new String[]{feeaddr}}, ArrayList.class);
            LOGGER.info(" arrayList.size : " + arrayList.size());
            UtxoInfo usdtinput = JSON.parseObject(JSON.toJSONString(listunspent.get(0)), UtxoInfo.class);
            UtxoInfo btcinput = JSON.parseObject(JSON.toJSONString(arrayList.get(0)), UtxoInfo.class);
            //USDT
            RawTransaction usdt = new RawTransaction();
            usdt.setTxid(usdtinput.getTxid());
            usdt.setVout(usdtinput.getVout());
            //BTC
            RawTransaction btc = new RawTransaction();
            btc.setTxid(btcinput.getTxid());
            btc.setVout(btcinput.getVout());

            //2 构造发送代币类型和代币数量数据（payload）
            String payload = client.invoke("omni_createpayload_simplesend", new Object[]{31L, amount}, String.class);
            LOGGER.info("2payload: " + payload);
            //3 构造交易基本数据（transaction base）
            Map data = new HashMap();
            Map[] rawtransactionparams = getmaparr(usdt, btc);
            String txBaseStr = client.invoke("createrawtransaction", new Object[]{rawtransactionparams, data}, String.class);
            // String txBaseStr = createRawTransaction(client, usdt, btc);
            LOGGER.info("3txBaseStr: " + txBaseStr);
            //4 在交易数据中加上omni代币数据
            String opreturn = client.invoke("omni_createrawtx_opreturn", new Object[]{txBaseStr, payload}, String.class);
            LOGGER.info("4opreturn: " + opreturn);
            //5在交易数据上加上接收地址
            String reference = client.invoke("omni_createrawtx_reference", new Object[]{opreturn, toaddr}, String.class);
            LOGGER.info("5reference: " + reference);
            //6 在交易数据上指定矿工费用
            Map[] rawtxchangeparams = getmapinputarr(usdtinput, btcinput);
            String createRaw = client.invoke("omni_createrawtx_change", new Object[]{reference, rawtxchangeparams, feeaddr, fee}, String.class);
            LOGGER.info("6createRaw: " + reference);
            //7 签名
            SignTransaction signTransaction = client.invoke("signrawtransaction", new Object[]{createRaw}, SignTransaction.class);
            LOGGER.info("Hex: " + signTransaction.getHex());
            if (signTransaction.isComplete()) {
                //8 广播
                Object txid = client.invoke("sendrawtransaction", new Object[]{signTransaction.getHex()}, Object.class);
                return txid.toString();
            } else {
                return "";
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "";
        }
    }

    private static Map[] getmaparr(final RawTransaction usdt, final RawTransaction btc) {
        Map[] params = new Map[2];
        Map usdtmap = new HashMap();
        usdtmap.put("txid", usdt.getTxid());
        usdtmap.put("vout", usdt.getVout());
        Map btcmap = new HashMap();
        btcmap.put("txid", btc.getTxid());
        btcmap.put("vout", btc.getVout());
        params[0] = usdtmap;
        params[1] = btcmap;
        return params;
    }

    private static Map[] getmapinputarr(final UtxoInfo usdtinput, final UtxoInfo btcinput) {
        Map[] paramsinput = new Map[2];
        Map<String, Object> usdtmapinput = new HashMap<>();
        usdtmapinput.put("txid", usdtinput.getTxid());
        usdtmapinput.put("vout", usdtinput.getVout());
        usdtmapinput.put("scriptPubKey", usdtinput.getScriptPubKey());
        usdtmapinput.put("value", usdtinput.getAmount());
        Map<String, Object> btcmapinput = new HashMap<>();
        btcmapinput.put("txid", btcinput.getTxid());
        btcmapinput.put("vout", btcinput.getVout());
        btcmapinput.put("scriptPubKey", btcinput.getScriptPubKey());
        btcmapinput.put("value", btcinput.getAmount());
        paramsinput[0] = usdtmapinput;
        paramsinput[1] = btcmapinput;
        return paramsinput;
    }


}
