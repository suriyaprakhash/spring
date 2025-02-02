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


    @GetMapping(value="bio/1/hugefile")
    public String getHugeFileBio1() throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/suriya/sample-test-files/150MB.csv"))) {
            return Flux.fromStream(bufferedReader.lines())
                    .collect(Collectors.joining("\n")) // Collect lines into a single string with newlines
                    .block(); // Block and get the result
        }
    }

    @GetMapping(value="bio/2/hugefile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getHugeFileBio2(OutputStream stream) throws IOException {
        DeferredResult<Resource> deferredResult = new DeferredResult<>(5000L);
        try {
            Path filePath = Paths.get("/home/suriya/sample-test-files/").resolve("150MB.csv").normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Even though this is web servlet - it uses async task executor to work on the rest of the stream closing the main thread
     *
     * That is why you would see a portion of the download size in the networks tab - however the whole file downloads sequentially
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value="bio/stream/hugefile")
    public ResponseEntity<StreamingResponseBody> getHugeFileBioStream() throws IOException {

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("huge-file-download.csv").build());

        StreamingResponseBody stream = outputStream -> {
            int byteRead;
            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), 8192)) {
                while ((byteRead = bis.read()) != -1) {
                    outputStream.write(byteRead);
                }
            }
        };
        return ResponseEntity.ok().headers(headers).body(stream);
    }

    @GetMapping(value="nio/hugefile", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getHugeFileNio() {

        Flux<String> flux = Flux.using(

                // resource factory creates FileReader instance
                () -> new FileReader("/home/suriya/sample-test-files/150MB.csv"),

                // transformer function turns the FileReader into a Flux
                reader -> Flux.fromStream(new BufferedReader(reader).lines()),

                // resource cleanup function closes the FileReader when the Flux is complete
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return flux;
    }

}
