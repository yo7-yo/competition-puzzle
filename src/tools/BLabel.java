package tools;

import javax.swing.*;

public class BLabel extends JLabel {
    public boolean use=false;
    //选定的图的路径
    public String pi;
    //已选择的图的标签和第几张
    public static BLabel blabel;
    public static int num=1;
    public BLabel(String m) {
        pi=m;
    }
}
