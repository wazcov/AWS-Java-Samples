package com.waynecovell.awsJavaSamples.V1SDK.RDS;

import com.waynecovell.awsJavaSamples.AwsShared.AWSSharedUtils;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class RDS_Start {

     /*
        To Use AWS com.waynecovell.awsJavaSamples.V1SDK.RDS, you need to ensure you have the following:

        - A user created in AWS IAM
        - Appropriate user, region, etc details updated in in AWSSharedUtils
        - AmazonRDSFullAccess Group assigned to that user in IAM
     */

    private static AmazonRDS client;
    private static Statement statement;
    private static Connection connection;
    private static String jdbcUrl;

    public static void main(String[] args) throws IOException {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connectToAwsRds();
            createDatabase();

            /*
            If we run the following commands straight away, it returns a null database
             so we must run the create script, then the others a few minutes afterwards
             */


            /*
            TODO You currently have to edit the default VPC to allow traffic from anywhere on all ports.
                There should be a way to do this programmatically
             */

/*            listDatabases();
            createDatabaseTable();
            insertDatabaseData();
            validateDatabaseData();*/
        } catch (Exception e) {
            System.out.println("Problem managing database " + e.toString());
            e.printStackTrace();
        }
    }

    private static void connectToAwsRds() {
        client = AmazonRDSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(AWSSharedUtils.creds))
                .withRegion(AWSSharedUtils.region).build();
    }

    private static void listDatabases() {
        //When listing, make sure you are using the region where you created the database, by editing it in AWSSharedUtils
        DescribeDBInstancesResult result = client.describeDBInstances();
        List<DBInstance> instances = result.getDBInstances();
        DBInstance instance = instances.get(0); //If you have multiple databases, this just gets the first one
        // Information about each com.waynecovell.awsJavaSamples.V1SDK.RDS instance
        String identifier = instance.getDBInstanceIdentifier();
        String engine = instance.getEngine();
        String status = instance.getDBInstanceStatus();
        Endpoint endpoint = instance.getEndpoint();

        engine += engine == "postgres" ? "ql" : ""; //The JDBC connection string needs "postgresql" yet amazon just return "postgres"
        jdbcUrl = "jdbc:" + engine + "ql://" + endpoint.getAddress() + ":" + endpoint.getPort() + "/" + identifier;

        System.out.println("Database exists: " + jdbcUrl);
    }

    private static void validateDatabaseData() throws Exception {
        String sql = "SELECT  count(*) as count FROM awstest";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String count = resultSet.getString("count");
            System.out.println("Total Records: " + count);
        }
        System.out.println("Validated database data");
    }

    private static void insertDatabaseData() throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO awstest (content) VALUES (?)");
        String content = "" + UUID.randomUUID();
        preparedStatement.setString(1, content);
        preparedStatement.executeUpdate();
        System.out.println("Inserted database data");
    }

    private static void createDatabaseTable() throws Exception {
        connection = DriverManager.getConnection(jdbcUrl, "fred", "fredfred123");
        statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS awstest (id SERIAL PRIMARY KEY, content VARCHAR(80))";
        statement.executeUpdate(sql);
        System.out.println("Added db table");
    }

    private static void createDatabase() {
        CreateDBInstanceRequest request = new CreateDBInstanceRequest();
        request.setDBInstanceIdentifier("awsjava");
        request.setDBInstanceClass("db.t2.micro");
        request.setEngine("postgres");
        request.setMultiAZ(false);
        request.setPort(5432);
        request.setMasterUsername("fred");
        request.setMasterUserPassword("fredfred123");
        request.setDBName("awsjava");
        request.setStorageType("gp2");
        request.setAllocatedStorage(10);

        client.createDBInstance(request);
        System.out.println("Created database");
    }
}
