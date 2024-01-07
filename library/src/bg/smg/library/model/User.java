package bg.smg.library.model;

import java.sql.Timestamp;
import java.util.Base64;

public class User {
    private String username;
    private String password;
    private Timestamp timestamp;
    private boolean isActive;

    public User() {
        this.username="";
        this.password="";
    }

    public User(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", timestamp=" + timestamp +
                ", isActive=" + isActive +
                '}';
    }
}
