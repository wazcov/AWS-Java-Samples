package com.wazcov.awsJavaSamples.V2SDK.S3;

import com.wazcov.awsJavaSamples.AwsShared.AWSSharedUtils;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

public class S3_Start {

    private static final String BUCKET_NAME = "testing";
    private static S3Client client = null;

    public static void main(String[] args) {
        setupS3();
        createBucket(BUCKET_NAME);
        if (listBuckets().contains(BUCKET_NAME)) {
            System.out.println("Bucket Exists");
            uploadToS3(BUCKET_NAME, "Sample_File.txt");
            listObjectsInS3Bucket(BUCKET_NAME);
        }
    }

    private static void setupS3() {
        client = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(AWSSharedUtils.basicCredentials)).build();
    }

    private static void createBucket(String bucket_name) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucket_name)
                .createBucketConfiguration(CreateBucketConfiguration.builder()
                        .locationConstraint(AWSSharedUtils.region.getName())
                        .build())
                .build();
        client.createBucket(createBucketRequest);
        System.out.println("Created Bucket");
    }

    private static List<String> listBuckets() {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = client.listBuckets(listBucketsRequest);
        List<String> buckets = listBucketsResponse.buckets().stream().map(x -> x.name()).collect(Collectors.toList());
        System.out.println("Listing Buckets");
        return buckets;
    }

    private static void uploadToS3(String bucket_name, String key_name) {
        try {
            client.putObject(PutObjectRequest.builder()
                    .bucket(bucket_name)
                    .key(key_name)
                    .build(), RequestBody.fromByteBuffer(ByteBuffer.wrap("Hello World".getBytes())));
            System.out.println("Uploaded File");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listObjectsInS3Bucket(String bucket_name) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucket_name).build();
        ListObjectsV2Response listObjectsV2Response;
        listObjectsV2Response = client.listObjectsV2(listObjectsV2Request);
        listObjectsV2Response.contents().stream().map(x -> x.key()).forEach(e -> System.out.println(e));
    }
}
