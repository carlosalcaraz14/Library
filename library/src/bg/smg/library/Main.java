

package bg.smg.library;

import bg.smg.library.app.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, String> userDatabase = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }

    public static Map<String, String> getUserDatabase() {
        return userDatabase;
    }
}

