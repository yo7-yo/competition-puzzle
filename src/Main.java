import file.PictureConfig;
import login.Login;

public class Main {
    public static void main(String[] args) {
        PictureConfig.loadConfig();
        new Login();
    }
}
