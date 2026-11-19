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
        String lower = videoKey.toLowerCase();
        String streamUrl;
        if (lower.contains("sintel")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4";
        } else if (lower.contains("tears_of_steel") || lower.contains("tears-of-steel") || lower.contains("tearsofsteel")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4";
        } else if (lower.contains("charge") || lower.contains("elephant")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
        } else if (lower.contains("sprite") || lower.contains("bunny")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        } else if (lower.contains("breaking_bad") || lower.contains("chernobyl")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4";
        } else if (lower.contains("interstellar") || lower.contains("dark_knight")) {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4";
        } else {
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(streamUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
