package com.waynecovell.awsJavaSamples.SQS;

import com.waynecovell.awsJavaSamples.AWS_Shared.AWSSharedUtils;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class SQS_Start {

    /*
        To Use AWS com.waynecovell.awsJavaSamples.SQS, you need to ensure you have the following:

        - A user created in AWS IAM
        - Appropriate user, region, etc details updated in in AWSSharedUtils
        - AmazonSQSFullAccess Group assigned to that user in IAM
        - A queue created in the UI for com.waynecovell.awsJavaSamples.SQS, with the same name used below
        - A region specified in AWSSharedUtils that matches the region you created the aformetnioned queue in, in AWS UI.
     */

    private static SQSConnection connection = null;
    private static Session session = null;
    private static Queue queue = null;

    public static void main(String[] args) {
        try {
            openQueueConnection();
            sendMessageOnAQueue();
            receiveMessagesFromAQueue();
            closeQueueConnection();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void closeQueueConnection() throws Exception {
        connection.close();
    }

    public static void receiveMessagesFromAQueue() throws Exception {
        MessageConsumer consumer = session.createConsumer(queue);
        connection.start();
        Message receivedMessage = consumer.receive(1000);
        if (receivedMessage != null) {
            System.out.println("Received: " + ((TextMessage) receivedMessage).getText());
        }
    }

    public static void sendMessageOnAQueue() throws Exception {
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello World!");
        producer.send(message);
        System.out.println("JMS Message " + message.getJMSMessageID());
    }

    public static void openQueueConnection() throws Exception {
        //Get these credentials from IAM in the AWS Web UI
        BasicAWSCredentials creds = AWSSharedUtils.creds;
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(creds))
                        .withRegion(AWSSharedUtils.region).build()
        );
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue("MyQueue");
    }
}
