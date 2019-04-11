# AWS Java Samples
A repository of basic AWS samples to get you started connecting to various AWS Services like com.waynecovell.awsJavaSamples.SQS.

## Assumptions:

Much of this code is intentionally not commented, as variables and methods are named in such a way that it should be fairly straightforward to understand what is happening.

This does assume a basic knowledge of Java.

This also assumes you have created an AWS account. Basic information on what you need to do in the UI is documented in each of the samples.

## Important:

Remember to add your own credentials into the AWS Shared package ([how-to](https://aws.amazon.com/premiumsupport/knowledge-center/create-access-key/)).

You can run the test [InitTest](src/test/java/InitTest.java) to confirm that you have at least changed all the values (although it does not validate them).

Each section contains a brief comment detailing the steps you need to take in the AWS Web UI to get set up. The program won't run unless you do these tasks.

## Samples:

* [Amazon SQS - Simple Queue Service](src/main/java/com/waynecovell/awsJavaSamples/SQS/SQS_Start.java)
* [Amazon SNS - Simple Notification Service](src/main/java/com/waynecovell/awsJavaSamples/SNS/SNS_Start.java)
* [Amazon SES - Simple Email Service](src/main/java/com/waynecovell/awsJavaSamples/SES/SES_Start.java)
* [Amazon RDS - Relational Database Service](src/main/java/com/waynecovell/awsJavaSamples/RDS/RDS_Start.java)
* [Amazon SM - Secrets Manager](src/main/java/com/waynecovell/awsJavaSamples/SM/SM_Start.java)

## Don't forget to star this repository if you find it useful :-)
