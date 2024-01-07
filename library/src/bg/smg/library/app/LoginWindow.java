package bg.smg.library.app;

import bg.smg.library.model.User;
import bg.smg.library.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> new RegistrationWindow());

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(e -> new ForgotPassword());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(forgotPasswordButton, gbc);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginWindow.this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    User loginUser = new User(username, password);
                    UserService userService = new UserService();
                    if (userService.verifyUser(loginUser)) {
                        openStorageManagementWindow(username);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginWindow.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginWindow.this, "Error during login", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void openStorageManagementWindow(String username) {
        JOptionPane.showMessageDialog(null, "Welcome to our library, " + username + "!\n \"Discover all our available books in the catalog.");
    }
}
