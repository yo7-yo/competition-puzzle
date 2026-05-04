package tools;

import javax.swing.*;

public class LLabel extends JLabel {
    int row;
    int col;
    //贴图的laibel
    public LLabel(){}
    public LLabel(Icon image,int i,int j) {
        super(image);
        row=i;
        col=j;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
