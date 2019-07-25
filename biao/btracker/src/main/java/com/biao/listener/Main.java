package com.biao.listener;

import com.azazar.bitcoin.jsonrpcclient.*;
import com.biao.util.RpcClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws MalformedURLException {
        try {
            //169PiWMZXwwFw88RDvp1LSRS5C1Jb4HUuw  1DMus15NJaWajQswc8E9R4DJ2rhXtsGtVo
            //String urlStr = "http://dazi:dazi@127.0.0.1:8332/";
            //String urlStr = "http://davidLove:Lovedazi@161.117.10.186:8446/";
            //String urlStr = "http://davidLove:Lovedazi@192.168.1.60:18666/";
            String urlStr = "http://dazi:dazi@127.0.0.1:8885/";

           BitcoinJSONRPCClient client = RpcClient.getClient(urlStr);
         String address = client.getNewAddress("aa");
        System.out.println(address);
            URL url = new URL(urlStr);
            final Bitcoin bitcoin = new BitcoinJSONRPCClient(url);
            receiveCoins(bitcoin);
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
    }


    public static void receiveCoins(Bitcoin bitcoin) throws BitcoinException {
        final BitcoinAcceptor acceptor = new BitcoinAcceptor(bitcoin);

        //System.out.println("Send bitcoins to " + bitcoin.getNewAddress("NewAccount"));

        acceptor.addListener(new ConfirmedPaymentListener() {
            HashSet processed = new HashSet();

            @Override
            public void confirmed(Bitcoin.Transaction transaction) {
                if (!processed.add(transaction.txId()))
                    return; // already processed

                System.out.println("txId:" + transaction.txId()+"  Payment received, amount: " + transaction.amount() + "; account: " + transaction.account()+ "address:"+transaction.address());

            }
        });
        acceptor.run();
    }
}
