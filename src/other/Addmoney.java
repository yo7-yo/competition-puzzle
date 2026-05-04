package other;

import file.Files;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Addmoney extends JFrame {
    JPanel panel,norpanel,cenpanel;
    JLabel label;

    public Addmoney(){
        init();
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    void init(){
        norpanel=new JPanel();
        panel=new JPanel(new BorderLayout());
        label=new JLabel("谢谢谢谢谢谢谢谢！");

        cenpanel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            {
                try {
                    bgImage =Files.loadImage("image/微信图片.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "背景图片加载失败！");
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        //设置标签的字体
        label.setFont(new Font("华文行楷", Font.BOLD, 40));
        label.setForeground(Color.RED);
        //添加组件
        norpanel.add(label);
        norpanel.setBackground(Color.CYAN);
        panel.add(norpanel,BorderLayout.NORTH);
        panel.add(cenpanel,BorderLayout.CENTER);
        add(panel);
        //设置框的大小
        setSize(600,800);
        //设置位置在中间
        setLocationRelativeTo(null);
    }

}
