package com.biao.reactive;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: .</p>
 *
 *  ""(Myth)
 * @version 1.0
 * @date 2018/4/8 9:22
 * @since JDK 1.8
 */
public class FluxTest {


    @Test
    public void test1() {

        List<Integer> list = new ArrayList<>();
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe(System.out::println);

        ints.subscribe(list::add);


    }
}
