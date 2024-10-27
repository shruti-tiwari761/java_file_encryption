package dao;

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {

    // Fetch all files associated with the given email
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM data1 WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        List<Data> files = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id"); // Use actual column name
            String name = rs.getString("file_name"); // Ensure this matches your table
            String path = rs.getString("path");
            String userEmail = rs.getString("email"); // Get the email for completeness if needed
            files.add(new Data(id, name, path, userEmail)); // Add email to Data object if necessary
        }
        return files;
    }


    // Hide a file by saving its metadata and the file's contents to the database
    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();

        // Check that the column names match your table schema
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO data1(file_name, path, email, bin_data) VALUES(?,?,?,?)");
        ps.setString(1, file.getFileName()); // Ensure this is file_name
        ps.setString(2, file.getPath());      // Ensure this is path
        ps.setString(3, file.getEmail());     // Ensure this is email

        File f = new File(file.getPath());
        if (!f.exists()) {
            throw new FileNotFoundException("File not found: " + f.getAbsolutePath());
        }

        FileReader fr = new FileReader(f);
        ps.setCharacterStream(4, fr, f.length());

        int ans = ps.executeUpdate();
        fr.close();
        f.delete(); // Optionally delete the file after hiding
        return ans;
    }


    // Unhide a file by retrieving it from the database
    public static void unhide(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data1 WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String path = rs.getString("path");
            Clob c = rs.getClob("bin_data");

            // Check if the path is valid
            if (path != null && !path.isEmpty()) {
                try (Reader r = c.getCharacterStream();
                     FileWriter fw = new FileWriter(path)) {
                    int i;
                    while ((i = r.read()) != -1) {
                        fw.write((char) i);
                    }
                }

                // After writing back to the original location, remove from the database
                ps = connection.prepareStatement("DELETE FROM data1 WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Successfully Unhidden");
            } else {
                System.out.println("Path is invalid or empty.");
            }
        } else {
            System.out.println("No record found with the specified ID.");
        }
    }

}
