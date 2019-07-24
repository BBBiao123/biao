package com.biao.init;

import com.biao.listener.btc.BitcoinEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author xiaoyu(549477611 @ qq.com)
 */
@Component
@SuppressWarnings("unchecked")
public class InitService implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(InitService.class);

 @Value("${symbolUrl}")
 private String symbolUrl;
    private final BitcoinEventProcessor bitcoinEventProcessor;



    @Autowired
    public InitService(BitcoinEventProcessor bitcoinEventProcessor) {
        this.bitcoinEventProcessor = bitcoinEventProcessor;
    }

    @Override
    public void run(String... args) {


     String urlStr = symbolUrl;
     logger.info("初始化比特币服务 symbolUrl ： " + urlStr);
     //"http://davidLove:Lovedazi@192.168.1.60:18666/";
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
