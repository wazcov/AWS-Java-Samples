package com.wazcov.awsJavaSamples.V1SDK.Lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.net.URLDecoder;

/*
    This is an example function for processing data from an S3 Bucket in AWS Lambda.

    1) Upload a .jar file containing a class like this one to AWS Lambda.
    2) Set an S3 Trigger of "Event type: ObjectCreated" TODO: Add infrastructure code to build this
    3) Set the Handler to match "com.wazcov.awsJavaSamples.V1SDK.Lambda.Lambda_Start::testCode"
    4) Upload a file with text in it to S3
    5) Check CloudWatch logs for the System.out statements
 */

public class Lambda_Start {
    public static void testCode(S3Event s3Event) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();

        s3Event.getRecords().forEach(e -> {
            try {
                String bucket = e.getS3().getBucket().getName();

                String key = e.getS3().getObject().getKey().replace('+', ' ');
                key = URLDecoder.decode(key, "UTF-8");

                AmazonS3 s3Client = AmazonS3Client.builder().build();
                s3Client.getObjectAsString(bucket, key);
                stringBuilder.append(s3Client.getObjectAsString(bucket, key));

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        });
    }
}
