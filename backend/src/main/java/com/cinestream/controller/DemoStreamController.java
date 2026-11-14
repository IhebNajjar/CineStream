package com.cinestream.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/stream")
public class DemoStreamController {

    /**
     * Demo streaming fallback that redirects to an official high-res open-source demo MP4 stream.
     */
    @GetMapping("/demo/{videoKey}")
    public ResponseEntity<Void> streamDemoVideo(@PathVariable String videoKey) {
        String streamUrl;
        if (videoKey.contains("breaking_bad") || videoKey.contains("chernobyl")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4";
        } else if (videoKey.contains("interstellar") || videoKey.contains("dark_knight")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4";
        } else {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(streamUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
