package com.bbex.init;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.bbex.listener.btc.BitcoinEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

/**
 * @author xiaoyu(549477611 @ qq.com)
 */
@Component
@SuppressWarnings("unchecked")
public class InitService implements CommandLineRunner {

 @Value("${symbolUrl}")
 private String symbolUrl;
    private final BitcoinEventProcessor bitcoinEventProcessor;



    @Autowired
    public InitService(BitcoinEventProcessor bitcoinEventProcessor) {
        this.bitcoinEventProcessor = bitcoinEventProcessor;
    }

    @Override
    public void run(String... args) {


     String urlStr = symbolUrl;//"http://davidLove:Lovedazi@192.168.1.60:18666/";
     //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18666/";
     //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18333/";
     //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18222/";
     //String urlStr = "http://omnicorerpcdazi:5hMTZI9iBGFqKxsWfOUF@127.0.0.1:18111/";

     //final Bitcoin bitcoin = new BitcoinJSONRPCClient(url);
     try {
      bitcoinEventProcessor.receiveCoins();
     } catch (Exception e) {
      e.printStackTrace();
     }
    }

/* @Bean
 public ApplicationRunner run(BitcoinEventProcessor bitcoinEventProcessor) throws MalformedURLException {
  String urlStr = "http://davidLove:Lovedazi@192.168.1.60:18666/";
  //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18666/";
  //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18333/";
  //String urlStr = "http://davidLove:Lovedazi@127.0.0.1:18222/";
  //String urlStr = "http://omnicorerpcdazi:5hMTZI9iBGFqKxsWfOUF@127.0.0.1:18111/";
  URL url = new URL(urlStr);
  final Bitcoin bitcoin = new BitcoinJSONRPCClient(url);
  return args -> bitcoinEventProcessor.receiveCoins(bitcoin);
 }*/

}
