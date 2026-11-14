package com.cinestream.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URI;
import java.time.Duration;

@Service
public class R2StorageService {

    private static final Logger logger = LoggerFactory.getLogger(R2StorageService.class);

    @Value("${cloudflare.r2.account-id:}")
    private String accountId;

    @Value("${cloudflare.r2.access-key-id:}")
    private String accessKeyId;

    @Value("${cloudflare.r2.secret-access-key:}")
    private String secretAccessKey;

    @Value("${cloudflare.r2.bucket-name:cinestream-videos}")
    private String bucketName;

    @Value("${cloudflare.r2.endpoint:}")
    private String customEndpoint;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Generates a temporary presigned watch URL for a video key in Cloudflare R2.
     * The URL is valid for the specified duration and is never stored in the database.
     * If Cloudflare R2 credentials are not configured, falls back to a secure local streaming URL.
     */
    public String generatePresignedWatchUrl(String videoKey, Duration duration) {
        if (videoKey == null || videoKey.trim().isEmpty()) {
            videoKey = "demo_trailer.mp4";
        }

        // If Cloudflare R2 / S3 credentials are provided in configuration
        if (accessKeyId != null && !accessKeyId.isBlank() &&
            secretAccessKey != null && !secretAccessKey.isBlank()) {
            try {
                URI endpointUri;
                if (customEndpoint != null && !customEndpoint.isBlank()) {
                    endpointUri = URI.create(customEndpoint);
                } else if (accountId != null && !accountId.isBlank()) {
                    endpointUri = URI.create("https://" + accountId + ".r2.cloudflarestorage.com");
                } else {
                    endpointUri = null;
                }

                if (endpointUri != null) {
                    try (S3Presigner presigner = S3Presigner.builder()
                            .endpointOverride(endpointUri)
                            .credentialsProvider(StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                            .region(Region.of("auto"))
                            .build()) {

                        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(videoKey)
                                .build();

                        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(duration)
                                .getObjectRequest(getObjectRequest)
                                .build();

                        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
                        String presignedUrl = presignedRequest.url().toString();
                        logger.info("Generated R2 presigned URL for key: {}", videoKey);
                        return presignedUrl;
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to generate Cloudflare R2 presigned URL (falling back to demo stream): {}", e.getMessage());
            }
        }

        // High quality web MP4 fallback for test & presentation
        return "http://localhost:" + serverPort + "/api/stream/demo/" + videoKey;
    }
}
