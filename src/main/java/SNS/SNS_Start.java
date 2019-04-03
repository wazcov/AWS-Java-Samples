package SNS;

import AWS_Shared.AWSSharedUtils;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SNS_Start {

    /*
        To Use AWS SNS, you need to ensure you have the following:

        - A user created in AWS IAM
        - Appropriate user, region, etc details updated in in AWSSharedUtils
        - AmazonSNSFullAccess Group assigned to that user in IAM
        - A valid email address, specified below (after subscribing you have to confirm with your email)

        Create A Topic method shows how one could create a topic programmatically, however you could create
        this in the AWS UI and hard-code it.
     */

    private static AmazonSNS amazonSNSClient = null;

    public static void main(String[] args) {
        openSNSConnection();
        String arn = createATopic();
        subscribeToATopic(arn);
        publishToATopic(arn);
    }

    public static void openSNSConnection() {
        BasicAWSCredentials creds = AWSSharedUtils.creds;
        amazonSNSClient = AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(AWSSharedUtils.region)
                .build();
    }

    public static String createATopic() {
        final CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyTopic");
        final CreateTopicResult createTopicResponse = amazonSNSClient.createTopic(createTopicRequest);
        System.out.println("TopicArn:" + createTopicResponse.getTopicArn());
        System.out.println("CreateTopicRequest: " + amazonSNSClient.getCachedResponseMetadata(createTopicRequest));
        return createTopicResponse.getTopicArn();
    }

    public static void subscribeToATopic(String topicArn) {
        final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", AWSSharedUtils.receiverEmailAddress);
        amazonSNSClient.subscribe(subscribeRequest);

        System.out.println("SubscribeRequest: " + amazonSNSClient.getCachedResponseMetadata(subscribeRequest));
        System.out.println("To confirm the subscription, check your email.");
    }

    public static void publishToATopic(String topicArn) {
        try {
            Thread.sleep(30000); //You have 30 seconds to confirm the subscription in your email account
        }catch(InterruptedException ie){}

        final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
        final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        final PublishResult publishResponse = amazonSNSClient.publish(publishRequest);

        System.out.println("MessageId: " + publishResponse.getMessageId());
        //Now check your email for a notification
    }
}
