package RDS;

import AWS_Shared.AWSSharedUtils;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;

import java.io.IOException;
import java.util.List;

public class RDS_Start {

     /*
        To Use AWS RDS, you need to ensure you have the following:

        - A user created in AWS IAM (Place creds in AWSSharedUtils)
        - AmazonRDSFullAccess Group assigned to that user in IAM

     */

    public static AmazonRDS client;

    public static void main(String[] args) throws IOException {
        try {
            createDatabase();
            Thread.sleep(5000); //Wait for database to be created else list database will return null
            listDatabases();
            createDatabaseTable();
            insertDatabaseData();
            validateDatabaseData();
        } catch (Exception ex) {
            System.out.println("Problem managing database " + ex.getMessage());
        }
    }

    private static void listDatabases() {
        DescribeDBInstancesResult result = client.describeDBInstances();
        List<DBInstance> instances = result.getDBInstances();
        for (DBInstance instance : instances) {
            // Information about each RDS instance
            String identifier = instance.getDBInstanceIdentifier();
            String engine = instance.getEngine();
            String status = instance.getDBInstanceStatus();
            Endpoint endpoint = instance.getEndpoint();

            System.out.println("Created Database: " + endpoint);
        }
    }

    private static void validateDatabaseData() {
        /*
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO jdbc_test (content) VALUES (?)");
        String content = "" + UUID.randomUUID();
        preparedStatement.setString(1, content);
        preparedStatement.executeUpdate();
        String sql = "SELECT  count(*) as count FROM jdbc_test";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String count = resultSet.getString("count");
            Logger.log("Total Records: " + count);
        }
         */
    }

    private static void insertDatabaseData() {
        /*
    }
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO jdbc_test (content) VALUES (?)");
        String content = "" + UUID.randomUUID();
        preparedStatement.setString(1, content);
        preparedStatement.executeUpdate();
        String sqlQuery = "SELECT  count(*) as count FROM jdbc_test";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        while (resultSet.next()) {
            String count = resultSet.getString("count");
            System.out.println("Total Records: " + count);
            */
    }

    private static void createDatabaseTable() {
                    /*
            Connection conn = DriverManager.getConnection(jdbc_url, "username", "password");
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS aws-test (id SERIAL PRIMARY KEY, content VARCHAR(80))";
            statement.executeUpdate(sql);


            }*/
    }

    private static void createDatabase() {
        client = AmazonRDSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(AWSSharedUtils.creds))
                .withRegion(AWSSharedUtils.region).build();

        CreateDBInstanceRequest request = new CreateDBInstanceRequest();
        request.setDBInstanceIdentifier("aws-sample-1");
        request.setDBInstanceClass("db.t2.micro");
        request.setEngine("postgres");
        request.setMultiAZ(false);
        request.setMasterUsername("username");
        request.setMasterUserPassword("password");
        request.setDBName("mydb");
        request.setStorageType("gp2");
        request.setAllocatedStorage(10);

        client.createDBInstance(request);
    }
}
