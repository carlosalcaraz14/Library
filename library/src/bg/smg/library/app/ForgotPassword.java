package bg.smg.library.app;

import bg.smg.library.model.User;
import bg.smg.library.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ForgotPassword extends JFrame {

    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JPasswordField repeatNewPasswordField;

    public ForgotPassword() {
        setTitle("Forgotten Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(15);
        newPasswordField = new JPasswordField(15);
        repeatNewPasswordField = new JPasswordField(15);

        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Repeat New Password:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(repeatNewPasswordField, gbc);

        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(new ResetPasswordButtonListener());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(resetPasswordButton, gbc);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ResetPasswordButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            String repeatNewPassword = new String(repeatNewPasswordField.getPassword());

            PasswordValidator passwordValidator = new PasswordValidator();

            if (username.isEmpty() || newPassword.isEmpty() || repeatNewPassword.isEmpty()) {
                JOptionPane.showMessageDialog(ForgotPassword.this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!newPassword.equals(repeatNewPassword)) {
                JOptionPane.showMessageDialog(ForgotPassword.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!passwordValidator.isPasswordValid(newPassword)) {
                JOptionPane.showMessageDialog(ForgotPassword.this,
                        "Password must have 8 or more characters, contain at least one uppercase letter, one digit, and one special symbol",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    User userToUpdate = new User(username, newPassword);
                    UserService userService = new UserService();
                    User existingUser = userService.getUserByUsername(username);

                    if (existingUser != null) {
                        userService.updateUserPassword(userToUpdate);
                        JOptionPane.showMessageDialog(ForgotPassword.this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(ForgotPassword.this, "Username not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ForgotPassword.this, "Error resetting password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
