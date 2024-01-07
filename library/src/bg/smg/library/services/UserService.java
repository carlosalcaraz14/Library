package bg.smg.library.services;

import bg.smg.library.model.User;
import bg.smg.library.util.DBManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Base64;

public class UserService implements UserServiceI {
    private DataSource dataSource;
    private Connection connection;

    public UserService() throws SQLException {
        dataSource = DBManager.getInstance().getDataSource();
    }
    @Override
    public void saveUser(User user) {
        try {
            this.connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, password, created, active) VALUES (?, ?, ?, ?)")) {

                statement.setString(1, user.getUsername());
                statement.setString(2, encode(user.getPassword()));
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.setBoolean(4, true);
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                System.out.println("Closing database connection...");
                try {
                    connection.close();
                    System.out.println("Connection valid: " + connection.isValid(5));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateUserPassword(User user) {
        try {
            this.connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET password=? WHERE username=?")) {

                statement.setString(1, encode(user.getPassword()));
                statement.setString(2, user.getUsername());
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        try {
            this.connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username=?")) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                resultSet.first();
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setTimestamp(resultSet.getTimestamp("created"));
                user.setActive(resultSet.getBoolean("active"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                System.out.println("Closing database connection...");
                connection.close();
                System.out.println("Connection valid: " + connection.isValid(5));
            }
        }
        return null;
    }

    public String encode(String password){
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        return encodedPassword;
    }

    public boolean verifyUser(User user) throws SQLException {
        User registeredUser = getUserByUsername(user.getUsername());
        String encodedPwd = encode(user.getPassword());
        if(registeredUser != null && registeredUser.getPassword().equals(encodedPwd))
            return true;
        else
            return false;
    }

    public String decodePassword(String passwordToDecode){
        byte[] decodedBytes = Base64.getDecoder().decode(passwordToDecode);
        return new String(decodedBytes);
    }
}
