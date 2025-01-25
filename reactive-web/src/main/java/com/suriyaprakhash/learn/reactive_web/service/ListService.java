package com.suriyaprakhash.learn.reactive_web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ListService {

    private final List<String> stringList = List.of(new String[]{"one", "two", "three", "four", "five", "six", "seven"});

    public List<String> getListBio() {
        log.info("bio service started");
        return IntStream.range(1, stringList.size() + 1)
                .mapToObj(index -> {
                    try {
                        log.info("bio waiting {}", index);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return stringList.get(index - 1) + "-" + index;
                }).collect(Collectors.toList());
    }

    public Flux<String> getListNio() {
        log.info("nio service started");
        return Flux.fromStream(IntStream.range(1, stringList.size() + 1)
                .mapToObj(index -> {
                    try {
                        log.info("nio waiting {}", index);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return stringList.get(index - 1) + "-" + index;
                })).log();
    }

}
