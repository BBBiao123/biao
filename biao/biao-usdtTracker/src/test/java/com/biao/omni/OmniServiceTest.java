package com.biao.omni;

import com.biao.constant.UsdtConstants;
import foundation.omni.CurrencyID;
import foundation.omni.OmniValue;
import foundation.omni.PropertyType;
import foundation.omni.rpc.BalanceEntry;
import foundation.omni.rpc.OmniClient;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *  ""(Myth)
 */
public class OmniServiceTest {

    private static final String fromAddress = "1rVevbaD1y9yjEKnrwjQ8ZVCfAixG4GnD";

    private static final String toAddress = "1KWMxyRTFrLmrshMWywETBGwr1hBRrH1bk";

    private OmniClient omniClient;

    {
        try {
            omniClient = new OmniClient(MainNetParams.get(),
                    new URI("http://192.168.1.60:18111/"), "dazi", "dazi");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBalance() throws IOException {
        final BalanceEntry coin =
                omniClient.omniGetBalance(Address.fromBase58(MainNetParams.get(), toAddress),
                        CurrencyID.of(UsdtConstants.USDT_ID));

        final Coin bitcoinBalance = omniClient.getBitcoinBalance(Address.fromBase58(MainNetParams.get(), fromAddress));
        System.out.println(bitcoinBalance);


        System.out.println(coin);
    }

    @Test
    public void testSend() throws IOException {
       /* BigDecimal amount =
                new BigDecimal("1.01").setScale(2, RoundingMode.HALF_UP);*/

        BigDecimal amount =
                new BigDecimal("0.01");

        final Sha256Hash sha256Hash = omniClient.omniSend(Address.fromBase58(MainNetParams.get(), fromAddress),
                Address.fromBase58(MainNetParams.get(), toAddress),
                new CurrencyID(UsdtConstants.USDT_ID), OmniValue.of(amount, PropertyType.DIVISIBLE));
        System.out.println(sha256Hash.toString());
    }

    @Test
    public void testHelp() throws IOException {
        final String help = omniClient.help();
        System.out.println(help);
    }
}
