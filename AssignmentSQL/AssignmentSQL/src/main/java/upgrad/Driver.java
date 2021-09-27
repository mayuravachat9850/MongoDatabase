package upgrad;

import java.sql.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.cj.protocol.Resultset;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.*;
import java.util.Arrays;

public class Driver {

    /**
     * Driver class main method
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // MySql credentials
        String url = "jdbc:mysql://pgc-sd-bigdata.cyaielc9bmnf.us-east-1.rds.amazonaws.com:3306/pgcdata";
        String user ="student";
        String password="STUDENT123";

        // MongoDB Configurations
        String url_1 ="mongodb://ec2-3-219-168-222.compute-1.amazonaws.com";

        // Connection Default Value Initialization
        Connection sqlConnection = null;
        MongoClient mongoClient = null;

        try {
            // Creating database connections
            sqlConnection = DriverManager.getConnection(url,user,password);
            mongoClient = MongoClients.create(url_1);



            // Import data into MongoDb
            MongoDatabase db = mongoClient.getDatabase("AssignmentDataBase");
            MongoCollection<Document> Products = db.getCollection("Products");
            String table = "mobiles";
            String table2 = "cameras";
            String table3 = "headphones";
            fetchMobiledata(sqlConnection,table, Products);
            fetchCameradata(sqlConnection,table2,Products);
            fetchheadphonedata(sqlConnection,table3,Products);
            

            // List all products in the inventory
            CRUDHelper.displayAllProducts(Products);



            // Display top 5 Mobiles
            CRUDHelper.displayTop5Mobiles(Products);

            // Display products ordered by their categories in Descending Order Without autogenerated Id
            CRUDHelper.displayCategoryOrderedProductsDescending(Products);

            // Display product count in each category
            CRUDHelper.displayProductCountByCategory(Products);

            // Display wired headphones
            CRUDHelper.displayWiredHeadphones(Products);
        }
        catch(Exception ex) {
            System.out.println("Got Exception.");
            ex.printStackTrace();
        }
        finally {
            // Close Connections
            sqlConnection.close();
            mongoClient.close();

        }
    }


    private static void fetchCameradata(Connection sqlConnection, String table2, MongoCollection<Document> products) throws SQLException {
        Statement statement = sqlConnection.createStatement();
        String query = "select * from " + table2;
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();


        while(resultSet.next()) {
            Document document = new Document();
            for(int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                document.append(resultSetMetaData.getColumnName(i), resultSet.getString(resultSetMetaData.getColumnName(i)));
                document.append("Category","Camera");
            }
            products.insertOne(document);
        }

        statement.close();
    }

    private static void fetchMobiledata(Connection sqlConnection, String table, MongoCollection<Document> products) throws SQLException {

        Statement statement = sqlConnection.createStatement();
        String query = "select * from " + table;
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();


        while(resultSet.next()) {
            Document document = new Document();
            for(int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                document.append(resultSetMetaData.getColumnName(i), resultSet.getString(resultSetMetaData.getColumnName(i)));
                document.append("Category","Mobile");
            }
            products.insertOne(document);
        }

        statement.close();
    }
    private static void fetchheadphonedata(Connection sqlConnection, String table3, MongoCollection<Document> products) throws SQLException {


        Statement statement = sqlConnection.createStatement();
        String query = "select * from " + table3;
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();


        while(resultSet.next()) {
            Document document = new Document();
            for(int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                document.append(resultSetMetaData.getColumnName(i), resultSet.getString(resultSetMetaData.getColumnName(i)));
                document.append("Category","Headphone");
            }
            products.insertOne(document);
        }

        statement.close();
    }

}