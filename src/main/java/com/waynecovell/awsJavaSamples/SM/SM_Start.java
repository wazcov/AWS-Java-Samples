package com.waynecovell.awsJavaSamples.SM;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ListSecretsRequest;
import com.amazonaws.services.secretsmanager.model.ListSecretsResult;
import com.amazonaws.util.IOUtils;
import com.waynecovell.awsJavaSamples.AwsShared.AWSSharedUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class SM_Start {

    public static final String region = "us-east-2";
    public static AWSSecretsManager client = null;

    public static final String STRING_KEY_NAME = "StringKeyX";
    public static final String BINARY_FILE_KEY_NAME = "FileKeyX";

    public static void main(String[] args) {

        SM_Start secretsManagerApplication = new SM_Start();

        try {
            secretsManagerApplication.setupSecretManager();

            //Example Storing Key String
            secretsManagerApplication.createSecretString();
            secretsManagerApplication.getSecretStringValue();

            //Example Storing Key Binary
            secretsManagerApplication.createSecretFromFile();
            secretsManagerApplication.getSecretFileValue();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setupSecretManager() {
        client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(AWSSharedUtils.creds))
                .build();
    }

    public void createSecretString() {
        CreateSecretRequest createSecretRequest = new CreateSecretRequest().withName(STRING_KEY_NAME).withSecretString("Hello World");
        client.createSecret(createSecretRequest);
        System.out.println("Stored Secret String");
    }

    public void getSecretStringValue() {
        ListSecretsRequest listSecretsRequest = new ListSecretsRequest();
        ListSecretsResult secretsResult = client.listSecrets(listSecretsRequest);
        secretsResult.getSecretList().stream().forEach(e -> System.out.println("Retrieved: " + e.getName()));

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(STRING_KEY_NAME);
        GetSecretValueResult secretValueResult = client.getSecretValue(getSecretValueRequest);
        String secretString = secretValueResult.getSecretString();

        System.out.println("Retrieved String Value: " + secretString);
    }

    public void createSecretFromFile() throws Exception {
        InputStream publicKeyStream = this.getClass().getClassLoader().getResourceAsStream("publicKeyFile");
        byte[] targetArray = IOUtils.toByteArray(publicKeyStream);
        ByteBuffer secretBinaryByteBuffer = ByteBuffer.wrap(targetArray);

        CreateSecretRequest createSecretRequest = new CreateSecretRequest().withName(BINARY_FILE_KEY_NAME).withSecretBinary(secretBinaryByteBuffer);
        client.createSecret(createSecretRequest);

        System.out.println("Stored Secret File");
    }

    public void getSecretFileValue() throws Exception {
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(BINARY_FILE_KEY_NAME);
        GetSecretValueResult secretValueResult = client.getSecretValue(getSecretValueRequest);
        ByteBuffer secretBinaryByteBuffer = secretValueResult.getSecretBinary();

        byte[] bytes = new byte[secretBinaryByteBuffer.capacity()];
        secretBinaryByteBuffer.get(bytes, 0, bytes.length);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey binary = kf.generatePublic(spec);

        System.out.println("Retrieved Secret Binary:");
        System.out.println(binary.toString());
    }
}
