package login;
import file.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static tools.SetFont.SetFont;

public class logMode extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton,resetButton;
    private JPanel panel,norpanel,soupanel,centerpanel;
    private JLabel label;
    private static boolean success=false;

    public logMode() {
        setTitle("用户登录");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        init();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    void init(){
        label=new JLabel("开发者验证");
        panel = new JPanel(new BorderLayout(0, 30));
        norpanel=new JPanel();
        centerpanel = new JPanel();
        soupanel=new JPanel();

        norpanel.setOpaque(false);
        soupanel.setOpaque(false);
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

        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerpanel = new JPanel(new GridLayout(2,2,2,20));
        centerpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        centerpanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 整面板水平居中
        centerpanel.setOpaque(false);
        soupanel.setLayout(new BoxLayout(soupanel, BoxLayout.Y_AXIS));
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密  码：");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordField = new JPasswordField(20);

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

        label.setForeground(new Color(139, 69, 19));
        loginButton.setBackground(new Color(255, 215, 0));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setForeground(new Color(80, 40, 0));
        resetButton.setBackground(new Color(255, 215, 0));
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);
        resetButton.setForeground(new Color(80, 40, 0));
        SetFont(new Font("华文行楷",Font.BOLD+Font.ITALIC,70),label);
        SetFont(new Font("微软黑体",Font.PLAIN+Font.BOLD,24), usernameLabel, usernameField, passwordLabel, passwordField);
        SetFont(new Font("华文行楷", Font.PLAIN+Font.BOLD, 20), loginButton, resetButton);

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
        
        usernameField.requestFocusInWindow();
    }

    private void bindEvents() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(logMode.this, "用户名不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    usernameField.requestFocusInWindow();
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(logMode.this, "密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    passwordField.requestFocusInWindow();
                    return;
                }

                if ("123".equals(username) && "123".equals(password)) {
                    JOptionPane.showMessageDialog(logMode.this, "登陆成功！欢迎使用一遍过。", "成功", JOptionPane.INFORMATION_MESSAGE);
                    success=true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(logMode.this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
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
    public static void Setsu(boolean b) {
        success=b;
    }
    public static boolean Getsu(){
        return success;
    }
}
