package tools;

import javax.swing.*;
import java.awt.*;

public class IIcon extends ImageIcon {
    //原始行列
    public int row;
    public int col;
    public IIcon(Image image, int i, int j){
        super(image);
        row=i;
        col=j;
    }
    public IIcon(ImageIcon icon, int i, int j){
        super(icon.getImage());
        row=i;
        col=j;
    }
}
