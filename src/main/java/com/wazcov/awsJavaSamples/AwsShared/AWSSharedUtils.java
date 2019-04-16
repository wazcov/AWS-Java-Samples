package com.wazcov.awsJavaSamples.AwsShared;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

/*
    Enter your own credentials here
 */
public class AWSSharedUtils {
    public static Regions region = null; //e.g Regions.US_EAST_1;
    public static String receiverEmailAddress = "XXX";
    public static String accessKey = "XXX";
    public static String secretKey = "XXX";

    //V1 SDK
    public static BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
    //V2 SDK
    public static AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
}
