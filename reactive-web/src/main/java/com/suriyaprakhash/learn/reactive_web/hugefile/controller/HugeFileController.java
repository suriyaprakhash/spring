package com.suriyaprakhash.learn.reactive_web.hugefile.controller;

import com.suriyaprakhash.learn.reactive_web.hugefile.data.Pair;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequestMapping("/hugefile")

@RestController
public class HugeFileController {

    /**
     * This does not work with just reactor Netty - since HttpServletResponse is from tomcat
     *
     * THIS IS NOT RECOMMENDED as it by passes the spring boot's abstraction
     *
     * @param httpServletResponse
     * @throws IOException
     */
    @GetMapping(value="bio/read-all")
    public void getHugeFileReadAll(HttpServletResponse httpServletResponse) throws IOException {
        Path filePath = Paths.get("/home/suriya/sample-test-files/150MB.csv");
        byte[] bytes = Files.readAllBytes(filePath);
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=150MB.csv");
        for (byte aByte : bytes) {
            httpServletResponse.getWriter().write(aByte);
        }
        log.info("Download completed");
    }

    /**
     * This does not work with just reactor Netty - since HttpServletResponse is from tomcat
     *
     * THIS IS NOT RECOMMENDED as it by passes the spring boot's abstraction
     *
     * @param httpServletResponse
     * @throws IOException
     */
    @GetMapping(value="bio/buffered")
    public void getHugeFileBio1(HttpServletResponse httpServletResponse) throws IOException {
        int bufferByteSize = 1024; // Adjust buffer size as needed
        int bytesRead; // keeps track of no.of byte filled in the buffer
        byte[] buffer = new byte[bufferByteSize];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
            while ((bytesRead = bis.read(buffer)) != -1) {
                // Process the bytes in the buffer
                for (int i = 0; i < bytesRead; i++) {
                    httpServletResponse.getWriter().write(buffer[i]);
                    httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=150MB.csv");
                }
            }
        }
        log.info("Download completed");
    }

    @GetMapping(value="bio/resource", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getHugeFileBio2() throws IOException {

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
     * Even though this is web servlet - it uses async task executor to work on the rest of the stream closing the main thread.
     * So spring.mvc.async.request-timeout - matters by default its set to 30 sec
     * Transfer-Encoding: chunked - response header is set so content length doesn't matter
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value="bio/stream")
    public ResponseEntity<StreamingResponseBody> getHugeFileBioStream() throws IOException {
//       DeferredResult<ResponseEntity<StreamingResponseBody>> deferredResult = new DeferredResult<>(10000L);

        int bufferByteSize = 8192; // Adjust buffer size as needed - default 8192

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("stream.csv").build());

        StreamingResponseBody stream = outputStream -> {

//            int byteRead;
//            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
//                // gets only one byte so does more read from the stream
//                while ((byteRead = bis.read()) != -1) {
//                    outputStream.write(byteRead);
//                }
//            }

            int bytesRead; // keeps track of no.of byte filled in the buffer
            byte[] buffer = new byte[bufferByteSize];
            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
                // reads the buffer into the array
                while ((bytesRead = bis.read(buffer)) != -1) {
                    // Process the bytes in the buffer
                    for (int i = 0; i < bytesRead; i++) {
                        outputStream.write(buffer[i]);
                    }
                }
            }
        };
        return ResponseEntity.ok().headers(headers).body(stream);
    }

    @GetMapping(value="nio", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Pair<byte[],Integer>> getHugeFileNio() {

//        Flux<String> flux = Flux.using(
//
//                // resource factory creates FileReader instance
//                () -> new FileReader("/home/suriya/sample-test-files/150MB.csv"),
//
//                // transformer function turns the FileReader into a Flux
//                reader -> Flux.fromStream(new BufferedReader(reader).lines()),
//
//                // resource cleanup function closes the FileReader when the Flux is complete
//                reader -> {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );

        int bufferByteSize = 1024; // Adjust buffer size as needed


        Flux<Pair<byte[],Integer>> flux = Flux.generate(
                () ->  {
                    byte[] bytes = new byte[bufferByteSize];
                    Integer countStart = 0;
                    return new Pair<>(bytes, countStart);
                }, (state, sink) -> {
                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
                        byte[] buffer = new byte[bufferByteSize];
                        int bytesRead = bis.read(buffer, state.getValue(), bufferByteSize);// keeps track of no.of byte filled in the buffer
                        if (bytesRead == -1) {
                            sink.complete();
                        } else {
                            var nextPair = new Pair<>(buffer, bytesRead + state.getValue());
                            sink.next(nextPair);
                            return nextPair;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
        );

        return flux;
    }

}
