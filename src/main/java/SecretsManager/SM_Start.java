package SecretsManager;

import AWS_Shared.AWSSharedUtils;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ListSecretsRequest;
import com.amazonaws.services.secretsmanager.model.ListSecretsResult;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class SM_Start {
    public static void main(String[] args) {
        String region = "us-east-2";
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(AWSSharedUtils.creds))
                .build();

        ByteBuffer buf = ByteBuffer.allocate(50);
        CharBuffer cbuf = buf.asCharBuffer();
        cbuf.put("Hello World");
        String s = cbuf.toString();

        CreateSecretRequest createSecretRequest = new CreateSecretRequest().withName("TestKey").withSecretBinary(buf);
        client.createSecret(createSecretRequest);

        ListSecretsRequest listSecretsRequest = new ListSecretsRequest();
        ListSecretsResult secrets = client.listSecrets(listSecretsRequest);
        secrets.getSecretList().stream().forEach(e -> System.out.println(e.getKmsKeyId() + " " + e.getName()));

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId("TestKey");
        GetSecretValueResult secretValueResult = client.getSecretValue(getSecretValueRequest);
        ByteBuffer secretBinary = secretValueResult.getSecretBinary();

        System.out.println(secretBinary.asCharBuffer().toString());
    }
}
