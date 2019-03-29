package SES;

import AWS_Shared.AWSSharedUtils;
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
        To Use AWS SNS, you need to ensure you have the following:

        - A user created in AWS IAM (Place creds in AWSSharedUtils)
        - AmazonSESFullAccess Group assigned to that user in IAM
        - Two valid, email addresses, both verified in AWS SES
        - A valid configuration set, created in AWS SES ( I selected CloudWatch with a tag of a:1)
     */

    static final String fromAddress = "wayne.covell@infinityworks.com";
    static final String toAddress = AWSSharedUtils.recieverEmailAddress;
    static final String configSet = "MyConfig";

    static final String subject = "Amazon SES test (AWS SDK for Java)";
    static final String htmlBody = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    static final String nonHtmlClientBody = "This email was sent through Amazon SES "
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
