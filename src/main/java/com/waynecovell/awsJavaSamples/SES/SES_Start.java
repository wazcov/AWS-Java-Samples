package com.waynecovell.awsJavaSamples.SES;

import com.waynecovell.awsJavaSamples.AwsShared.AWSSharedUtils;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import java.io.IOException;

public class SES_Start {

    /*
        To Use AWS com.waynecovell.awsJavaSamples.SES, you need to ensure you have the following:

        - A user created in AWS IAM
        - Appropriate user, region, etc details updated in in AWSSharedUtils
        - AmazonSESFullAccess Group assigned to that user in IAM
        - Two valid, email addresses, both verified in AWS com.waynecovell.awsJavaSamples.SES
        - A valid configuration set, created in AWS com.waynecovell.awsJavaSamples.SES ( I selected CloudWatch with a tag of a:1)
     */

    private static final String fromAddress = "wayne.covell@infinityworks.com";
    private static final String toAddress = AWSSharedUtils.receiverEmailAddress;
    private static final String configSet = "MyConfig";

    private static final String subject = "Amazon com.waynecovell.awsJavaSamples.SES test (AWS SDK for Java)";
    private static final String htmlBody = "<h1>Amazon com.waynecovell.awsJavaSamples.SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon com.waynecovell.awsJavaSamples.SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    private static final String nonHtmlClientBody = "This email was sent through Amazon com.waynecovell.awsJavaSamples.SES "
            + "using the AWS SDK for Java.";

    public static void main(String[] args) throws IOException {

        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
                            .standard()
                            .withCredentials(new AWSStaticCredentialsProvider(AWSSharedUtils.creds))
                            .withRegion(AWSSharedUtils.region).build();

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(toAddress))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(htmlBody))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(nonHtmlClientBody)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource(fromAddress)
                    .withConfigurationSetName(configSet);
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }
}
