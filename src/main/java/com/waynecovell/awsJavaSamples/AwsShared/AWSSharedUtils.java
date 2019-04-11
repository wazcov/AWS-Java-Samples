package com.waynecovell.awsJavaSamples.AwsShared;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

public class AWSSharedUtils {
    //Enter your own credentials here
    public static BasicAWSCredentials creds = new BasicAWSCredentials("XXX", "XXX");
    public static Regions region = null;; //e.g Regions.US_EAST_1;
    public static String receiverEmailAddress = "XXX";
}
