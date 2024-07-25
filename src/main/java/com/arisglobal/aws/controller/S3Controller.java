package com.arisglobal.aws.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    String s3Region = "ap-south-1";
    String s3BucketName="dev-aris-lsmv";
    String s3Key="local/test/dummy.txt";
    String data = "Anaya Neha Aditya Anand";

    // Can Be Retrieved Via Properties File
    String accessKey;
    String secretKey;


    @GetMapping(path = "/store")
    public String storeData(){
        boolean status = false;
        try (S3Client s3Client = getDefaultAWSS3Client()){
            PutObjectRequest request = PutObjectRequest.builder().bucket(s3BucketName).key(s3Key).build();
            PutObjectResponse putObjectResponse = s3Client.putObject(request, RequestBody.fromBytes(data.getBytes(StandardCharsets.UTF_8)));
            status = putObjectResponse.sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status ? s3Key : "ERROR";
    }

    @GetMapping(path = "/retrieve")
    public String getData() {
        String data = "";
        try (S3Client s3Client = getDefaultAWSS3Client(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(s3BucketName).key(s3Key).build();
            ResponseInputStream<GetObjectResponse> objectResp = s3Client.getObject(getObjectRequest);
            byte[] response = objectResp.readAllBytes();
            data = new String(response, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private S3Client getDefaultAWSS3Client() {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(s3Region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return s3Client;
    }

    private S3Client getAWSS3Client() {
        S3Client s3Client = S3Client.builder().region(Region.of(s3Region))
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return accessKey;
                    }

                    @Override
                    public String secretAccessKey() {
                        return secretKey;
                    }
                })
                .build();
        return s3Client;
    }

}
