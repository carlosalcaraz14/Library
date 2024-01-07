package bg.smg.library.app;
import bg.smg.library.model.User;
import bg.smg.library.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegistrationWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;

    public RegistrationWindow() {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(5, 2));
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        repeatPasswordField = new JPasswordField(15);

        registrationPanel.add(new JLabel("New Username:"));
        registrationPanel.add(usernameField);
        registrationPanel.add(new JLabel("New Password:"));
        registrationPanel.add(passwordField);
        registrationPanel.add(new JLabel("Repeat Password:"));
        registrationPanel.add(repeatPasswordField);
        registrationPanel.add(new JPanel());

        JButton registerUserButton = new JButton("Register");
        registerUserButton.addActionListener(new RegisterUserButtonListener());

        registrationPanel.add(registerUserButton);

        add(registrationPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class RegisterUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String repeatPassword = new String(repeatPasswordField.getPassword());

            PasswordValidator passwordValidator = new PasswordValidator();

            if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(repeatPassword)) {
                JOptionPane.showMessageDialog(RegistrationWindow.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!passwordValidator.isPasswordValid(password)) {
                JOptionPane.showMessageDialog(RegistrationWindow.this,
                        "Password must have 8 or more characters, contain at least one uppercase letter, one digit, and one special symbol",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    User newUser = new User(username, password);
                    UserService userService = new UserService();
                    userService.saveUser(newUser);

                    JOptionPane.showMessageDialog(RegistrationWindow.this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(RegistrationWindow.this, "Error registering user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

