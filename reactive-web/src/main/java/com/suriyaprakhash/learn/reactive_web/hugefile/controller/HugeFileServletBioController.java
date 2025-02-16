package com.suriyaprakhash.learn.reactive_web.hugefile.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequestMapping("/hugefile/servlet")
@RestController
public class HugeFileServletBioController {

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
}
