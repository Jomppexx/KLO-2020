import javax.swing.*;

public class AppMain {
    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainScreen.init();
            }
        });
    }
}
