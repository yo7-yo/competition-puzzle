package tools;

import javax.swing.*;
import java.awt.*;

public class SetFont {
    public static void SetFont(Font f, JComponent...p){
        for(JComponent q:p)
        {
            q.setFont(f);
        }
    }
}
