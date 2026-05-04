package Choice;

import begin.BeginPuzzle;
import Choice.mode.ChoicePlay;
import Choice.mode.circleMode;
import Choice.mode.nineMode;
import Choice.mode.selfMode;
import file.Files;
import tools.Introduction;
import tools.LV;
import tools.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static tools.SetFont.SetFont;

public class WindowChoice extends JFrame {
    JButton selfMode,nineMode,Return,circleMode,commonMode;
    JComboBox number,countdown;
    JLabel life;
    JPanel bgpanel,centerpanel,northpanel;
    int num=3,times=0;
    JCheckBox shuoming;
    boolean shuo=true;//true就是需要之后new窗口
    private JFrame previousFrame;

    public WindowChoice(){
        this(null);
    }

    public WindowChoice(JFrame previousFrame){
        this.previousFrame=previousFrame;
        init();
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                } else {
                    new BeginPuzzle();
                }
            }
        });
    }

    void init() {
        //初始化
        Music.mmusic.playBGM("sound/main_thing.mp3");
        commonMode=new JButton("民居建筑");
        selfMode = new JButton("官府建筑");
        nineMode = new JButton("皇宫建筑");
        circleMode = new JButton("桥梁建筑");
        Return = new JButton("返回");
        number = new JComboBox();
        countdown = new JComboBox();
        bgpanel = new JPanel(new BorderLayout());
        centerpanel = new JPanel();
        northpanel = new JPanel();
        shuoming = new JCheckBox("显示规则", true);
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        centerpanel.setOpaque(false);
        // 按建筑类型分类标签
        JLabel categoryLabel = new JLabel("建筑类型分类：");
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(new Font("华文行楷", Font.BOLD, 28));
        centerpanel.add(categoryLabel);
        centerpanel.add(Box.createVerticalStrut(10));
        JPanel pa=new JPanel();
        //监视器
        commonMode.addActionListener(e -> {
            if(LV.lifevalue>=3) {
                LV.lifevalue -= 3;
                Music.mmusic.stopBGM();
                if (shuo == true) {
                    new Introduction(num, times, 1, 1, true, this);
                } else {
                    new ChoicePlay(num, times, 1, this);
                }
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "生命值不足，很可惜，你得充钱了");
            }
        });
        selfMode.addActionListener(e -> {
            if(LV.lifevalue>=3) {
                LV.lifevalue -= 3;
                Music.mmusic.stopBGM();
                if (shuo == true) {
                    new Introduction(num, times, 2, true, this);
                } else {
                    new selfMode(num, times, this);
                }
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "生命值不足，很可惜，你得充钱了");
            }
        });
        nineMode.addActionListener(e -> {
            if(LV.lifevalue>=3) {
                LV.lifevalue -= 3;
                Music.mmusic.stopBGM();
                if (shuo == true) {
                    new Introduction(num, times, 3, true, this);
                } else {
                    new nineMode(num, times, this);
                }
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "生命值不足，很可惜，你得充钱了");
            }
        });
        circleMode.addActionListener(e -> {
            if(LV.lifevalue>=3) {
                LV.lifevalue -= 3;
                Music.mmusic.stopBGM();
                if (shuo == true) {
                    new Introduction(num, times, 4, true, this);
                } else {
                    new circleMode(num, times, this);
                }
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "生命值不足，很可惜，你得充钱了");
            }
        });
        Return.addActionListener(e -> {
            dispose();
            if (previousFrame != null) {
                previousFrame.setVisible(true);
            } else {
                new BeginPuzzle();
            }
        });
        shuoming.addActionListener(e->{
            shuo=!shuo;
        });
        //下拉框添加
        number.addItem("几宫格(默认3宫格)");
        number.addItem("3×3");
        number.addItem("4×4");
        number.addItem("5×5");
        number.addItem("6×6");
        number.addItem("7×7");
        number.addItem("8×8");
        number.addItem("9×9");
        number.addItemListener(e -> {
            //这里不能用==，这是比较地址的，不是比较字面量的。
            if (e.getItem().equals("3×3")) {
                getNum(3);
            } else if (e.getItem().equals("4×4")) {
                getNum(4);
            } else if (e.getItem().equals("5×5")) {
                getNum(5);
            } else if (e.getItem().equals("6×6")) {
                getNum(6);
            } else if (e.getItem().equals("7×7")) {
                getNum(7);
            } else if (e.getItem().equals("8×8")) {
                getNum(8);
            } else if (e.getItem().equals("9×9")) {
                getNum(9);
            }
        });

        countdown.addItem("倒计时(默认无)");
        countdown.addItem("5秒");
        countdown.addItem("30秒");
        countdown.addItem("60秒(1分钟)");
        countdown.addItem("120秒(2分钟)");
        countdown.addItem("300秒(5分钟)");
        countdown.addItem("3000秒(10分钟)");
        countdown.addItemListener(e -> {
            if (e.getItem().equals("5秒")) {
                getTimes(5);
            } else if (e.getItem().equals("30秒")) {
                getTimes(30);
            } else if (e.getItem().equals("60秒(1分钟)")) {
                getTimes(60);
            } else if (e.getItem().equals("120秒(2分钟)")) {
                getTimes(120);
            } else if (e.getItem().equals("300秒(5分钟)")) {
                getTimes(300);
            } else if (e.getItem().equals("3000秒(10分钟)")) {
                getTimes(3000);
            }
        });

        SetFont(new Font("华文行楷", Font.BOLD, 32),countdown,number,shuoming);
        //布局与添加
        bgpanel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            {
                try {
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
        life=new JLabel(""+ LV.lifevalue);
        life.setOpaque(false);
        life.setHorizontalTextPosition(SwingConstants.CENTER);
        life.setForeground(Color.BLACK);
        life.setFont(new Font("华文行楷", Font.BOLD, 32));
        //爱心
        try{
            BufferedImage images=Files.loadImage("image/aixin.png");
            Image scaledImage = images.getScaledInstance(180, 60, Image.SCALE_SMOOTH); // SCALE_SMOOTH更清晰
            life.setIcon(new ImageIcon(scaledImage));
        }catch (IOException e) {
            System.out.println("加载失败，重新加载中");
        }

        //字体
        Font f=new Font("华文行楷",Font.PLAIN,50);
        SetFont(f,circleMode,nineMode,selfMode,commonMode);
        Font fff=new Font("微软黑体",Font.PLAIN+Font.BOLD,23);
        SetFont(fff,Return);
        //按钮颜色 - 配合黄色主色调（浅杏色背景）
        commonMode.setBackground(new Color(255, 239, 213));
        commonMode.setOpaque(true);
        commonMode.setBorderPainted(false);
        commonMode.setForeground(new Color(101, 67, 33));
        selfMode.setBackground(new Color(255, 239, 213));
        selfMode.setOpaque(true);
        selfMode.setBorderPainted(false);
        selfMode.setForeground(new Color(101, 67, 33));
        nineMode.setBackground(new Color(255, 239, 213));
        nineMode.setOpaque(true);
        nineMode.setBorderPainted(false);
        nineMode.setForeground(new Color(101, 67, 33));
        circleMode.setBackground(new Color(255, 239, 213));
        circleMode.setOpaque(true);
        circleMode.setBorderPainted(false);
        circleMode.setForeground(new Color(101, 67, 33));
        Return.setBackground(new Color(255, 239, 213));
        Return.setOpaque(true);
        Return.setBorderPainted(false);
        Return.setForeground(new Color(101, 67, 33));

        //按钮大小
        Return.setMinimumSize(new Dimension(120, 50));
        Return.setMaximumSize(new Dimension(120, 50));
        Return.setPreferredSize(new Dimension(120, 50));
        shuoming.setPreferredSize(new Dimension(170, 50));
        shuoming.setMinimumSize(new Dimension(170, 50));
        shuoming.setMaximumSize(new Dimension(170, 50));
        commonMode.setPreferredSize(new Dimension(500, 65));
        commonMode.setMinimumSize(new Dimension(500, 65));
        commonMode.setMaximumSize(new Dimension(500, 65));
        circleMode.setPreferredSize(new Dimension(500, 65));
        circleMode.setMinimumSize(new Dimension(500, 65));
        circleMode.setMaximumSize(new Dimension(500, 65));
        nineMode.setPreferredSize(new Dimension(500, 65));
        nineMode.setMinimumSize(new Dimension(500, 65));
        nineMode.setMaximumSize(new Dimension(500, 65));
        selfMode.setPreferredSize(new Dimension(500, 65));
        selfMode.setMinimumSize(new Dimension(500, 65));
        selfMode.setMaximumSize(new Dimension(500, 65));
        Dimension comboSize = new Dimension(500, 50);
        number.setPreferredSize(comboSize);
        number.setMaximumSize(comboSize);
        countdown.setPreferredSize(comboSize);
        countdown.setMaximumSize(comboSize);
        //按钮居中
        circleMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        nineMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        selfMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        commonMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        //使劲的加上
        pa.add(shuoming);
        pa.setBorder(BorderFactory.createEmptyBorder(100,0,0,48));
        pa.setOpaque(false);
        centerpanel.add(Box.createVerticalGlue());
        centerpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerpanel.add(pa);
        centerpanel.add(number);
        centerpanel.add(Box.createVerticalStrut(20));
        centerpanel.add(countdown);
        centerpanel.add(Box.createVerticalStrut(20));
        centerpanel.add(commonMode);
        centerpanel.add(Box.createVerticalStrut(20));
        centerpanel.add(selfMode);
        centerpanel.add(Box.createVerticalStrut(20));
        centerpanel.add(nineMode);
        centerpanel.add(Box.createVerticalStrut(20));
        centerpanel.add(circleMode);
        centerpanel.add(Box.createVerticalStrut(20));
        northpanel.setOpaque(false);
        northpanel.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(Return);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(life);
        northpanel.add(leftPanel, BorderLayout.WEST);
        northpanel.add(rightPanel, BorderLayout.EAST);
        bgpanel.add(northpanel, BorderLayout.NORTH);
        bgpanel.add(centerpanel, BorderLayout.CENTER);
        add(bgpanel);

        setTitle("中国古代建筑拼图");
        setBounds(490, 140,670, 800);
    }

    void getNum(int a){
        num=a;
    }
    void getTimes(int a){
        times=a;
    }

}
