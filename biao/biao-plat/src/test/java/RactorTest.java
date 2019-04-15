import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * RactorTest .
 * ctime: 2018/9/10 15:05
 *
 *  "" sixh
 */
public class RactorTest {
    public static void main(String[] args) throws IOException {
        System.out.println(BigDecimal.valueOf(8.00E-7).longValue());
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        String step = "10000|20";
        String[] arr = step.split("|");
        System.out.println(arr[0]);
        BigDecimal a = BigDecimal.TEN;
        BigInteger c = a.toBigInteger().mod(BigInteger.valueOf(3L));
        System.out.println(c);
        System.out.println(a.multiply(BigDecimal.TEN));
        Mono.just("sssssss")
                .filter(e -> {
                    System.out.println("filter--->" + Thread.currentThread().getName());
                    return true;
                })
                .doOnNext(e -> {
                    System.out.println("doOnNext--->" + Thread.currentThread().getName());
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
        System.in.read();
    }


}
