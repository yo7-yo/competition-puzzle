package login;

import begin.BeginPuzzle;
import file.Files;
import tools.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static tools.SetFont.SetFont;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton,resetButton;
    private JPanel panel,norpanel,soupanel,centerpanel;
    private JLabel label;
    public static String name;

    public Login() {
        setTitle("中国古代建筑拼图 - 用户登录");
        setSize(600, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        init();
        setLocationRelativeTo(null);//居中
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(Login.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    void init(){
        label=new JLabel("古建拼图");
        norpanel=new JPanel();
        centerpanel = new JPanel();
        soupanel=new JPanel();
        //为什么背景图不被挡住
        norpanel.setOpaque(false);
        soupanel.setOpaque(false);
        //加组件
        panel = new JPanel(new BorderLayout(0,30)) {
            private BufferedImage bgImage;
            {
                try {
                    bgImage = Files.loadImage("image/登录.png");
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
        panel.setBorder(BorderFactory.createEmptyBorder(5, 40, 20, 40));
        centerpanel = new JPanel(new GridLayout(2,2,2,20));
        centerpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        centerpanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 整面板水平居中
        centerpanel.setOpaque(false);
        soupanel.setLayout(new BoxLayout(soupanel, BoxLayout.Y_AXIS));
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(usernameField.getPreferredSize().width, 20));
        JLabel passwordLabel = new JLabel("密  码：");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 20));
        //按钮
        loginButton = new JButton("登录");
        resetButton = new JButton("重置");
        loginButton.setPreferredSize(new Dimension(150,40));
        loginButton.setMaximumSize(new Dimension(150,40));
        loginButton.setMinimumSize(new Dimension(150,40));
        resetButton.setPreferredSize(new Dimension(150,40));
        resetButton.setMaximumSize(new Dimension(150,40));
        resetButton.setMinimumSize(new Dimension(150,40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //组件相关装饰 - 配合黄色主色调（浅杏色背景）
        label.setForeground(new Color(101, 67, 33)); // 深棕色
        loginButton.setForeground(new Color(101, 67, 33)); // 深棕色
        loginButton.setBackground(new Color(255, 239, 213)); // 浅杏色
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        resetButton.setForeground(new Color(101, 67, 33)); // 深棕色
        resetButton.setBackground(new Color(255, 239, 213)); // 浅杏色
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);
        usernameLabel.setForeground(new Color(101, 67, 33));
        passwordLabel.setForeground(new Color(101, 67, 33));
        usernameField.setBackground(new Color(255, 248, 220)); // 浅黄色
        passwordField.setBackground(new Color(255, 248, 220)); // 浅黄色
        SetFont(new Font("华文行楷",Font.BOLD+Font.ITALIC,70),label);
        SetFont(new Font("微软黑体",Font.PLAIN+Font.BOLD,24), usernameLabel, usernameField, passwordLabel, passwordField);
        SetFont(new Font("华文行楷", Font.PLAIN+Font.BOLD, 28), loginButton, resetButton);
        //加组件
        norpanel.add(label);
        centerpanel.add(usernameLabel);
        centerpanel.add(usernameField);
        centerpanel.add(passwordLabel);
        centerpanel.add(passwordField);
        soupanel.add(loginButton);
        soupanel.add(Box.createVerticalStrut(10));
        soupanel.add(resetButton);
        panel.add(norpanel, BorderLayout.NORTH);
        panel.add(centerpanel, BorderLayout.CENTER);
        panel.add(soupanel, BorderLayout.SOUTH);
        add(panel);

        bindEvents();
        
        usernameField.addActionListener(e -> loginButton.doClick());
        passwordField.addActionListener(e -> loginButton.doClick());
        
        //焦点放置
        usernameField.requestFocusInWindow();
        
        Music.mmusic = new Music();
        Music.mmusic.playBGM("sound/main_thing.mp3");
    }

    private void bindEvents() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(Login.this, "用户名不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    usernameField.requestFocusInWindow();
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(Login.this, "密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    passwordField.requestFocusInWindow();
                    return;
                }
                if ("123".equals(username) && "123".equals(password)) {
                    name="123";
                    JOptionPane.showMessageDialog(Login.this, "登录成功！欢迎使用", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new BeginPuzzle();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    passwordField.requestFocusInWindow();
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
                usernameField.requestFocusInWindow();
            }
        });
    }

}
