package bg.smg.library.app;

public class PasswordValidator {

    public static boolean isPasswordValid(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(regex);
    }
}

