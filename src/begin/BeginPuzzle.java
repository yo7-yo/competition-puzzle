package begin;

import Choice.WindowChoice;
import file.Check;
import file.Files;
import other.WindowOther;
import tools.Introduction;
import tools.LV;
import tools.Music;
import tools.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import static tools.SetFont.SetFont;
//开始界面
public class BeginPuzzle extends JFrame {
    //成员函数声明
    JButton buttonBegin,buttonChoice,buttonOther,buttonExit;
    JButton buttonSign,buttonRecord;
    JLabel label,life;
    JPanel backgroundPanel,topPanel,centerPanel;
    int num=3,times=0;//几宫格和用时
    public BeginPuzzle(){
        init();
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(BeginPuzzle.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    //关闭这个界面时，存储数据和关闭音乐。
                    dispose();
                    Music.mmusic.stopBGM();
                    Files.Writefiles("information.txt");
                    System.exit(0);
                }
            }
        });
    }

    void init(){
        //设置默认拼图图片
        if (Picture.pic == null) {
            Picture.pic = "image/江南居民拼图.png";
        }
        //程序开启后只需要读取一次就可以了
        if (LV.bool) {
            //打开程序后只准读取一次，不然每次打开这个生命值都会是文件中的值。
            Files.readfiles("resources/txt/information.txt");
            LV.bool = false;
        }
        //初始化
        label = new JLabel("古建拼图");
        buttonRecord = new JButton("历史记录");
        buttonSign = new JButton("签到");
        buttonBegin = new JButton("快速开始");
        buttonChoice = new JButton("选择建筑类型");
        buttonOther = new JButton("建筑图鉴");
        buttonExit = new JButton("退出游戏");
        topPanel=new JPanel(new BorderLayout());
        //监听器
        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Music.playSound("resources/sound/cion.wav");
                LocalDateTime tt = LocalDateTime.now();
                String today = tt.getYear() + "" + tt.getMonthValue() + "" + tt.getDayOfMonth();
                if (!LV.signDate.equals(today)) {
                    int m = (int) (Math.random() * 5) + 1;
                    LV.lifevalue += m;
                    life.setText("" + LV.lifevalue);
                    life.repaint();
                    JOptionPane.showMessageDialog(BeginPuzzle.this, "恭喜您抽到了" + m + "个生命值！");
                    LV.signDate = today;
                } else {
                    JOptionPane.showMessageDialog(BeginPuzzle.this, "sorry~~~,您今天签到过了");
                }
            }
        });
        buttonBegin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LV.lifevalue >= 3) {
                    //进入一次就扣3个生命值，能扣的前提下。
                    LV.lifevalue -= 3;
                    //写上值
                    life.setText("" + LV.lifevalue);
                    Music.mmusic.stopBGM();
                    //规则说明
                    new Introduction(num, times, 0, 1, true, BeginPuzzle.this);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(BeginPuzzle.this, "生命值不足，很可惜，你得充钱了");
                }
            }
        });
        buttonChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowChoice(BeginPuzzle.this);
                setVisible(false);
            }
        });
        buttonOther.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Music.mmusic.stopBGM();
                new WindowOther(BeginPuzzle.this);
                setVisible(false);
            }
        });
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(BeginPuzzle.this, "真的，真的要退出吗？", "真的吗？", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    Music.mmusic.stopBGM();
                    Files.Writefiles("resources/txt/information.txt");
                    System.exit(0);
                }
            }
        });
        buttonRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Check(BeginPuzzle.this);
            }
        });

        //贴图
        backgroundPanel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            //实例初始化，创建变量时立马加载图片给它。
            {
                try {
                    //加载图片
                    bgImage = Files.loadImage("image/封面.png");
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
        //生命值
        life = new JLabel(" " + LV.lifevalue);
        life.setOpaque(false);
        life.setHorizontalTextPosition(SwingConstants.CENTER);
        life.setVerticalTextPosition(SwingConstants.CENTER);
        life.setForeground(Color.BLACK);
        life.setFont(new Font("华文行楷", Font.BOLD, 32));
        //加爱心图片 - 与建筑类型选择界面保持一致的大小
        try {
            BufferedImage images = Files.loadImage("image/aixin.png");
            Image scaledImage = images.getScaledInstance(180, 60, Image.SCALE_SMOOTH);
            life.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            System.out.println("加载失败，重新加载中");
        }
        //组件摆放
        //设置组件为透明状态，就不会挡到背景了。
        topPanel.setOpaque(false);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(buttonSign);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 61, 0, 0));
        centerPanel.add(buttonRecord);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(life);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalStrut(40));
        //字体
        Font f=new Font("华文行楷",Font.PLAIN,50);
        SetFont(f,buttonBegin,buttonChoice,buttonOther,buttonExit);
        Font ff=new Font("华文行楷",Font.BOLD+Font.ITALIC,125);
        SetFont(ff,label);
        Font fff=new Font("微软黑体",Font.PLAIN+Font.BOLD,23);
        SetFont(fff,buttonSign,buttonRecord);
        label.setForeground(new Color(70, 45, 28));
        //按钮颜色 - 配合黄色主色调（浅杏色背景）
        buttonSign.setBackground(new Color(255, 239, 213));
        buttonSign.setOpaque(true);
        buttonSign.setBorderPainted(false);
        buttonSign.setForeground(new Color(101, 67, 33));
        buttonRecord.setBackground(new Color(255, 239, 213));
        buttonRecord.setOpaque(true);
        buttonRecord.setBorderPainted(false);
        buttonRecord.setForeground(new Color(101, 67, 33));
        buttonBegin.setBackground(new Color(255, 239, 213));
        buttonBegin.setOpaque(true);
        buttonBegin.setBorderPainted(false);
        buttonBegin.setForeground(new Color(101, 67, 33));
        buttonChoice.setBackground(new Color(255, 239, 213));
        buttonChoice.setOpaque(true);
        buttonChoice.setBorderPainted(false);
        buttonChoice.setForeground(new Color(101, 67, 33));
        buttonOther.setBackground(new Color(255, 239, 213));
        buttonOther.setOpaque(true);
        buttonOther.setBorderPainted(false);
        buttonOther.setForeground(new Color(101, 67, 33));
        buttonExit.setBackground(new Color(255, 239, 213));
        buttonExit.setOpaque(true);
        buttonExit.setBorderPainted(false);
        buttonExit.setForeground(new Color(101, 67, 33));
        //按钮大小
        buttonSign.setMinimumSize(new Dimension(120, 50));
        buttonSign.setMaximumSize(new Dimension(120, 50));
        buttonSign.setPreferredSize(new Dimension(120, 50));
        buttonRecord.setMinimumSize(new Dimension(160, 40));
        buttonRecord.setMaximumSize(new Dimension(160, 40));
        buttonRecord.setPreferredSize(new Dimension(160, 40));
        buttonBegin.setPreferredSize(new Dimension(200, 65));
        buttonChoice.setPreferredSize(new Dimension(200, 65));
        buttonOther.setPreferredSize(new Dimension(200, 65));
        buttonExit.setPreferredSize(new Dimension(200, 65));
        buttonBegin.setMinimumSize(new Dimension(500, 65));
        buttonBegin.setMaximumSize(new Dimension(500, 65));
        buttonChoice.setMinimumSize(new Dimension(500, 65));
        buttonChoice.setMaximumSize(new Dimension(500, 65));
        buttonOther.setMinimumSize(new Dimension(500, 65));
        buttonOther.setMaximumSize(new Dimension(500, 65));
        buttonExit.setMinimumSize(new Dimension(500, 65));
        buttonExit.setMaximumSize(new Dimension(500, 65));
        //按钮沿X轴居中对齐
        buttonBegin.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonChoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonOther.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        //使劲的加上组件
        centerPanel.add(label);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  //加内边距
        centerPanel.add(Box.createVerticalStrut(20));//空白占位符
        centerPanel.add(buttonBegin);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonChoice);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonOther);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonExit);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        add(backgroundPanel);
        //基础设置
        setTitle("中国古代建筑拼图");
        setBounds(490, 140,670, 800);//大小
        Music.mmusic.playBGM("sound/main_thing.mp3");
    }
}
