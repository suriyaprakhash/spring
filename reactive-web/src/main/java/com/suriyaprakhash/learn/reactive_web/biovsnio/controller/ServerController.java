package com.suriyaprakhash.learn.reactive_web.biovsnio.controller;

import com.suriyaprakhash.learn.reactive_web.biovsnio.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/server")

@RestController
public class ServerController {

    @Autowired
    private ListService listService;

    private final List<String> stringList = List.of(new String[]{"one", "two", "three", "four", "five", "six"});

    @GetMapping(value = "bio", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getBio() {
        List<String> updatedList = listService.getListBio();
        // the following is printed only after updateList is fully available
        log.info("Server Ctrl - Tomcat collected {}", updatedList);
        return updatedList;
    }

    // NOTE:- MediaType.TEXT_EVENT_STREAM_VALUE is being used to stream the events as text
    @GetMapping(value = "nio", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getNio() {
        Flux<String> updatedFlux = listService.getListNio();
        log.info("Server Ctrl - Netty collected {}", updatedFlux.count());
//        return updatedFlux.delayElements(Duration.ofSeconds(1));
        return updatedFlux;
    }

}
