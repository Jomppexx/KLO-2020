import javax.swing.*;

public class AppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainScreen mainScreen = new MainScreen();
            }
        });
    }
}
