package com.suriyaprakhash.learn.reactive_web.hugefile.controller;

import com.suriyaprakhash.learn.reactive_web.hugefile.data.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
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
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequestMapping("/hugefile")
@RestController
public class HugeFileController {


    String filePath = "/hugefile";
    String fileName = "hugefile";
    String fileExtension = ".hugefile";
    int bufferByteSize = 8192; // Adjust buffer size as needed

    // private String filePath = "\"/home/suriya/sample-test-files/\"";
    // private String fileName = "150MB";
    // private String fileType = "csv";

//    /**
//     * This does not work with just reactor Netty - since HttpServletResponse is from tomcat
//     *
//     * THIS IS NOT RECOMMENDED as it by passes the spring boot's abstraction
//     *
//     * @param httpServletResponse
//     * @throws IOException
//     */
//    @GetMapping(value="bio/read-all")
//    public void getHugeFileReadAll(HttpServletResponse httpServletResponse) throws IOException {
//        Path filePath = Paths.get("/home/suriya/sample-test-files/150MB.csv");
//        byte[] bytes = Files.readAllBytes(filePath);
//        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=150MB.csv");
//        for (byte aByte : bytes) {
//            httpServletResponse.getWriter().write(aByte);
//        }
//        log.info("Download completed");
//    }
//
//    /**
//     * This does not work with just reactor Netty - since HttpServletResponse is from tomcat
//     *
//     * THIS IS NOT RECOMMENDED as it by passes the spring boot's abstraction
//     *
//     * @param httpServletResponse
//     * @throws IOException
//     */
//    @GetMapping(value="bio/buffered")
//    public void getHugeFileBio1(HttpServletResponse httpServletResponse) throws IOException {
//        int bufferByteSize = 1024; // Adjust buffer size as needed
//        int bytesRead; // keeps track of no.of byte filled in the buffer
//        byte[] buffer = new byte[bufferByteSize];
//        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
//            while ((bytesRead = bis.read(buffer)) != -1) {
//                // Process the bytes in the buffer
//                for (int i = 0; i < bytesRead; i++) {
//                    httpServletResponse.getWriter().write(buffer[i]);
//                    httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=150MB.csv");
//                }
//            }
//        }
//        log.info("Download completed");
//    }

    /**
     *
     * Tomcat - writes the actual file
     * Netty - writes the actual file
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value="bio/resource", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getHugeFileBio2() throws IOException {

        try {
            Path filePath = Paths.get(this.filePath).resolve(fileName + "." + fileExtension).normalize();
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
     * Here, HTTP response ends after the first pass from the buffer - the aync is handled from the 2nd pass
     *
     * Tomcat - writes the actual file
     * Netty - needs handler and can't process StreamingBodyResponse so spits out an empty object in csv file
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value="bio/stream")
    public ResponseEntity<StreamingResponseBody> getHugeFileBioStream()  {
//       DeferredResult<ResponseEntity<StreamingResponseBody>> deferredResult = new DeferredResult<>(10000L);


        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("stream.csv").build());

        StreamingResponseBody stream = outputStream -> {

            int byteRead;
            try (var bis = new BufferedInputStream(new FileInputStream(filePath + fileName + "." + fileExtension),
                    bufferByteSize)) {
                // gets only one byte so does more read from the stream
                while ((byteRead = bis.read()) != -1) {
                    outputStream.write(byteRead);
                }
            }
            outputStream.flush();
            outputStream.close();

//            int bytesRead; // keeps track of no.of byte filled in the buffer
//            byte[] buffer = new byte[bufferByteSize];
//            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
//                // reads the buffer into the array
//                while ((bytesRead = bis.read(buffer)) != -1) {
//                    // Process the bytes in the buffer
//                    for (int i = 0; i < bytesRead; i++) {
//                        outputStream.write(buffer[i]);
//                    }
//                }
 //           outputStream.flush();
//            }
        };
        return ResponseEntity.ok().headers(headers).body(stream);
    }

    // TODO: Check in this context if DefferedResult & StreamingResponseBody are one and the same - ie) aync
    // TODO: Sine the for loop is blocking - it does seem to be processed in async thread - meaning expect http response and a separate aync for downloading the rest of data

    @GetMapping("bio/stream/deferred")
    public ResponseEntity<Set<Future<Pair<String, Integer>>>> stream() throws ExecutionException, InterruptedException {
        DeferredResult<ResponseEntity<StreamingResponseBody>> deferredResult = new DeferredResult<>(5000L); // 5 seconds timeout

//        CompletableFuture.runAsync(() -> {
//            try {
//                StreamingResponseBody responseBody = outputStream -> {
//                    // Simulate streaming data
//                    for (int i = 0; i < 20; i++) {
//                        outputStream.write(("Data " + i + "\n").getBytes());
//                        outputStream.flush();
////                        try {
////                            Thread.sleep(500); // Simulate some processing delay
////                        } catch (InterruptedException e) {
////                            throw new RuntimeException(e);
////                        }
//                    }
//                };
//
//                deferredResult.setResult(ResponseEntity.ok(responseBody));
//
//            } catch (Exception e) {
//                deferredResult.setErrorResult(ResponseEntity.internalServerError().build()); // Handle errors
//            }
//        });
//
//        deferredResult.onTimeout(() -> {
//            deferredResult.setErrorResult(ResponseEntity.status(408).build()); // Request Timeout
//        });

//        StreamingResponseBody responseBody = outputStream -> {
////                    // Simulate streaming data
//                    for (int i = 0; i < 20; i++) {
//                        outputStream.write(("Data " + i + "\n").getBytes());
//                        outputStream.flush();
//                    }
//                };


//        CompletableFuture.runAsync(() -> {
//            OutputStream outputStream = null;
//
//            int bufferByteSize = 8192; // Adjust buffer size as needed - default 8192
//            int byteRead;
//            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
//                // gets only one byte so does more read from the stream
//                while ((byteRead = bis.read()) != -1) {
//                    outputStream.write(byteRead);
//                }
//            }  catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        Callable<byte[]> callable = () -> {
//            int bufferByteSize = 8192; // Adjust buffer size as needed - default 8192
//            int byteRead;
//            byte[] buffer = new byte[bufferByteSize];
//            try (var bis = new BufferedInputStream(new FileInputStream("/home/suriya/sample-test-files/150MB.csv"), bufferByteSize)) {
//                // gets only one byte so does more read from the stream
//                while ((byteRead = bis.read(buffer)) != -1) {
//                    return buffer;
//                }
//            }  catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };

        AtomicInteger globalCounter = new AtomicInteger();
//        int globalCounter = 0;
        Set<Future<Pair<String, Integer>>> futureList = new HashSet<>();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            Future<Pair<String, Integer>> future = executor.submit(new NamedTask(i, globalCounter));
//            globalCounter++;
            log.info(String.valueOf(future.get()));
            futureList.add(future);
        }

        return ResponseEntity.ok(futureList);
    }

    private static class NamedTask implements Callable<Pair<String,Integer>> {  //Inner class
        private final String name;
        private final AtomicInteger threadCounter;

        public NamedTask(int threadId, AtomicInteger threadCounter) {
            this.name = "t-"+ threadId;
            this.threadCounter = threadCounter;
        }

        @Override
        public Pair<String,Integer> call() throws Exception {
            return new Pair<String,Integer>(name, threadCounter.addAndGet(1));
        }
    }

    /**
     * This reads the file in the nio fashion - however, writes the file in bio
     *
     * Tomcat - writes the actual file
     * Netty - needs handler and can't process StreamingBodyResponse so spits out an empty object in csv file
     *
     * @return
     */
    @GetMapping(value="nio/write-block")
    public ResponseEntity<StreamingResponseBody> getHugeFileNioTomcat() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename("nio.csv").build());

        StreamingResponseBody stream = outputStream -> {
            Path nioPath = Paths.get(filePath + fileName + "." + fileExtension);
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferByteSize);
            // represents connection to the network
            try (FileChannel fileChannel = FileChannel.open(nioPath, StandardOpenOption.READ)) {
                int bytesRead;
                while ((bytesRead = fileChannel.read(byteBuffer)) != -1) {
                    if (bytesRead > 0) { // Important check!
                        byteBuffer.flip(); // Prepare buffer for reading

                        byte[] bytes = new byte[bytesRead]; // Create byte array with actual size read
                        byteBuffer.get(bytes); // Read from buffer
                        // here even though we read in nio fashion we write in bio fashion
                        // so this negates the benefit of non blocking io
                        outputStream.write(bytes); // Write to output stream
                        byteBuffer.clear();      // Prepare buffer for next read
                    }
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return ResponseEntity.ok().headers(headers).body(stream);

    }

    /**
     * Tomcat - returns the nativeBuffer in the data.nativeBuffer in byte chunks into the csv file
     * Netty - returns the csv file with right info
     *
     * @return
     */
    @GetMapping(value="nio", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getHugeFileNio() {

        try {
            // represents connection to the network
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(
                    Paths.get(filePath + fileName + "." + fileExtension), StandardOpenOption.READ); // Use AsynchronousFileChannel
            DefaultDataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

            Flux<DataBuffer> dataBufferFlux =  Flux.create(sink -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(bufferByteSize); // Adjust byteBuffer size
                AtomicLong position = new AtomicLong(0);
                // processes incoming and outgoing data
                java.nio.channels.CompletionHandler<Integer, Void> handler = new java.nio.channels.CompletionHandler<Integer, Void>() {
                    @Override
                    public void completed(Integer result, Void attachment) {
                        if (result == -1) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                sink.error(e);
                            }
                            sink.complete();
                            return;
                        }

                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        DataBuffer dataBuffer = dataBufferFactory.wrap(ByteBuffer.wrap(bytes));
                        sink.next(dataBuffer);
                        byteBuffer.clear();
                        position.addAndGet(result);
                        channel.read(byteBuffer, position.get(), null, this);
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            exc.addSuppressed(e);
                        }
                        sink.error(exc);
                    }
                };

                channel.read(byteBuffer, 0, null, handler);
            });

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"nio.csv\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(dataBufferFlux); // Return Flux<DataBuffer>

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build(); // Or handle the error in a reactive way
        }

    }


}
