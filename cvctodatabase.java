import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class cvctodatabase {

    public static void main(String[] args) {
        String csvFilePath = "C:\\Users\\patel\\OneDrive\\Documents\\work\\Java_learning\\javapos\\gst_item_name.csv";
        String sqliteDbPath = "C:\\Users\\patel\\OneDrive\\Documents\\work\\Java_learning\\Estimateapp_db\\productsdb.db";

        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");

            // Connect to SQLite Database
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteDbPath);

            // Read CSV file and create table dynamically
            createTableFromCSV(connection);

            // Insert data into SQLite Database
            insertDataFromCSV(connection, csvFilePath);

            // Close the connection
            connection.close();

            System.out.println("Data inserted successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableFromCSV(Connection connection) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS products_3 (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "item_name TEXT, price TEXT)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDataFromCSV(Connection connection, String csvFilePath) {
        String insertQuery = "INSERT INTO products_3 (item_name, price) VALUES (?, ?)";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Skip the header line
            reader.readLine();

            // Process the data and insert into the table
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {  // Ensure the line has the expected number of columns
                    preparedStatement.setString(1, data[0].trim());  // item_name
                    preparedStatement.setString(2, data[1].trim());  // price

                    preparedStatement.executeUpdate();
                } else {
                    System.err.println("Skipping invalid data: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
