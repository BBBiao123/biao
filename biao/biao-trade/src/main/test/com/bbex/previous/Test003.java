package com.biao.previous;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public class Test003 {
    public static void main(String[] args) {
        Mono.just("3333")
                .flatMap((Function<String, Mono<?>>)
                        s -> {
                            throw new RuntimeException("11");
//                           return Mono.just("1234444");
                        })
                .doOnError(e -> {
                    System.out.println("error" + e);
                })
                .subscribe(e -> System.out.println(e));
    }
}
