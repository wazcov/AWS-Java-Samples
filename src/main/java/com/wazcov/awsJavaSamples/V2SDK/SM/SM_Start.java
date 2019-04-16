package com.wazcov.awsJavaSamples.V2SDK.SM;

import com.amazonaws.util.IOUtils;
import com.wazcov.awsJavaSamples.AwsShared.AWSSharedUtils;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsRequest;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class SM_Start {

    private static SecretsManagerClient client = null;
    private static final String STRING_KEY_NAME = "StringKeyX";
    private static final String BINARY_FILE_KEY_NAME = "FileKeyX";

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSecretManager() {
        client = SecretsManagerClient.builder().credentialsProvider(StaticCredentialsProvider.create(AWSSharedUtils.basicCredentials)).build();
    }

    private void createSecretString() {
        CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().name(STRING_KEY_NAME).secretString("Hello World").build();
        client.createSecret(createSecretRequest);
        System.out.println("Stored Secret String");
    }

    private void getSecretStringValue() {
        ListSecretsRequest listSecretsRequest = ListSecretsRequest.builder().build();
        ListSecretsResponse listSecretsResponse = client.listSecrets(listSecretsRequest);

        listSecretsResponse.secretList().forEach(e -> System.out.println("Retrieved: " + e.name()));

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(STRING_KEY_NAME).build();
        GetSecretValueResponse secretValueResponse = client.getSecretValue(getSecretValueRequest);
        String secretString = secretValueResponse.secretString();

        System.out.println("Retrieved String Value: " + secretString);
    }

    private void createSecretFromFile() throws Exception {
        InputStream publicKeyStream = this.getClass().getClassLoader().getResourceAsStream("publicKeyFile");
        byte[] targetArray = IOUtils.toByteArray(publicKeyStream);
        ByteBuffer secretBinaryByteBuffer = ByteBuffer.wrap(targetArray);

        CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().name(BINARY_FILE_KEY_NAME).secretBinary(SdkBytes.fromByteBuffer(secretBinaryByteBuffer)).build();
        client.createSecret(createSecretRequest);

        System.out.println("Stored Secret File");
    }

    private void getSecretFileValue() throws Exception {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(BINARY_FILE_KEY_NAME).build();
        GetSecretValueResponse secretValueResponse = client.getSecretValue(getSecretValueRequest);

        ByteBuffer secretBinaryByteBuffer = secretValueResponse.secretBinary().asByteBuffer();

        byte[] bytes = new byte[secretBinaryByteBuffer.capacity()];
        secretBinaryByteBuffer.get(bytes, 0, bytes.length);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey binary = kf.generatePublic(spec);

        System.out.println("Retrieved Secret Binary:");
        System.out.println(binary.toString());
    }
}
