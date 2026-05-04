import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CheckImage {
    public static void main(String[] args) throws Exception {
        String path = "resources/image/太和殿.png";
        BufferedImage img = ImageIO.read(new File(path));
        System.out.println("Width: " + img.getWidth() + ", Height: " + img.getHeight());
    }
}
