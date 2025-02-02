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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/hugefile")

@RestController
public class HugeFileController {

    @GetMapping(value="bio/read-all")
    public void getHugeFileReadAll(HttpServletResponse httpServletResponse) throws IOException {
        Path filePath = Paths.get("/home/suriya/sample-test-files/1G.pdf");
        byte[] bytes = Files.readAllBytes(filePath);
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=1G.pdf");
        for (byte aByte : bytes) {
            httpServletResponse.getWriter().write(aByte);
        }
        log.info("Download completed");
    }


    @GetMapping(value="bio/buffered")
    public void getHugeFileBio1(HttpServletResponse httpServletResponse) throws IOException {
        int bufferByteSize = 1024; // Adjust buffer size as needed
        int bytesRead; // keeps track of no.of byte filled in the buffer
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/1G.pdf"), bufferByteSize)) {
            byte[] buffer = new byte[bufferByteSize];
            while ((bytesRead = bis.read(buffer)) != -1) {
                // Process the bytes in the buffer
                for (int i = 0; i < bytesRead; i++) {
                    httpServletResponse.getWriter().write(buffer[i]);
                    httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=1G.pdf");
                }
            }
        }
        log.info("Download completed");
    }

    @GetMapping(value="bio/resource", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getHugeFileBio2() throws IOException {

        try {
            Path filePath = Paths.get("/home/suriya/sample-test-files/").resolve("1G.pdf").normalize();
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
    @GetMapping(value="bio/stream")
    public ResponseEntity<StreamingResponseBody> getHugeFileBioStream() throws IOException {
//        DeferredResult<StreamingResponseBody> deferredResult = new DeferredResult<>(10000L);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("huge-file-download.pdf").build());

        StreamingResponseBody stream = outputStream -> {
            int byteRead;
            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/1G.pdf"), 8192)) {
                while ((byteRead = bis.read()) != -1) {
                    outputStream.write(byteRead);
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
