package dao;

import db.MyConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Check if a user exists by email
    public static boolean isExists(String email) throws SQLException {
        String query = "SELECT 1 FROM user1 WHERE email = ?"; // Change users to user1
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();  // If a record is found, return true
        }
    }

    // Save a new user to the database
    public static int saveUser(User user) throws SQLException {
        String query = "INSERT INTO user1 (name, email) VALUES (?, ?)"; // Change users to user1
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps.executeUpdate();  // Returns the number of rows affected
        }
    }
}
