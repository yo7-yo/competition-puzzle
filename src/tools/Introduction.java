package tools;

import Choice.mode.ChoicePlay;
import Choice.mode.circleMode;
import Choice.mode.nineMode;
import Choice.mode.selfMode;
import file.Files;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import static tools.SetFont.SetFont;

public class Introduction extends JFrame{
    JPanel centerpanel,norpanel,panel,soupanel;
    JButton button;
    JLabel label;
    int nums,num,times,m;
    boolean b;
    private JFrame previousFrame;

    public Introduction(int num,int times,int nums,boolean b) {
        this(num, times, nums, b, null);
    }

    public Introduction(int num,int times,int nums,boolean b, JFrame previousFrame) {
        this.nums = nums;
        this.b=b;
        this.num = num;
        this.times=times;
        this.previousFrame=previousFrame;
        init();
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(Introduction.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    if (previousFrame != null) {
                        previousFrame.setVisible(true);
                    }
                }
            }
        });
        setTitle("规则说明");
        setSize(800,600);
        setLocationRelativeTo(null);
    }
    public Introduction(int num,int times,int m,int nums,boolean b) {
        this(num, times, m, nums, b, null);
    }
    public Introduction(int num,int times,int m,int nums,boolean b, JFrame previousFrame) {
        this.num = num;
        this.times=times;
        this.m=m;
        this.nums = nums;
        this.b=b;
        this.previousFrame=previousFrame;
        init();
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(Introduction.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    if (previousFrame != null) {
                        previousFrame.setVisible(true);
                    }
                }
            }
        });
        setTitle("规则说明");
        setSize(800,600);
        setLocationRelativeTo(null);
    }
    void init(){
        norpanel = new JPanel();
        soupanel = new JPanel();
        label=new JLabel();
        centerpanel = new JPanel(new BorderLayout());
        button=new JButton("确定");
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        //贴背景图
        panel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            {
                try {
                    bgImage =Files.loadImage("image/拼图放置背景.png");
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
        try {
            String textPath;
            switch (nums) {
                case 1:
                    textPath = "txt/intro_formal.txt";
                    label.setText("普通模式");
                    break;
                case 2:
                    textPath = "txt/intro_self.txt";
                    label.setText("自定义模式");
                    break;
                case 3:
                    textPath = "txt/intro_nine.txt";
                    label.setText("九宫格模式");
                    break;
                case 4:
                    textPath = "txt/intro_circle.txt";
                    label.setText("旋转模式");
                    break;
                default:
                    textPath = "txt/intro_formal.txt";
                    break;
            }
            // 设置字体和颜色
            SetFont(new Font("华文行楷", Font.BOLD, 36), label);
            label.setForeground(new Color(101, 67, 33));
            SetFont(new Font("华文行楷", Font.PLAIN+Font.BOLD, 28), button);
            button.setBackground(new Color(255, 239, 213));
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setForeground(new Color(101, 67, 33));

            try{
                // 先尝试从文件系统加载
                String userDir = System.getProperty("user.dir");
                String[] possiblePaths = {
                    userDir + "/resources/" + textPath,
                    userDir + "/" + textPath,
                    "resources/" + textPath,
                    textPath
                };
                
                BufferedReader reader = null;
                for (String filePath : possiblePaths) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                        break;
                    }
                }
                
                // 如果文件系统没有找到，尝试从classpath加载
                if (reader == null) {
                    InputStream is = getClass().getClassLoader().getResourceAsStream(textPath);
                    if (is != null) {
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    }
                }
                
                if (reader == null) {
                    throw new IOException("介绍文本未找到: " + textPath);
                }
                
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
                textArea.setText("读取失败：" + e.getMessage());
            }

            // 字体
            SetFont(new Font("微软雅黑", Font.BOLD, 24), textArea);
            textArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("读取失败：" + ex.getMessage());
        }

        button.setMinimumSize(new Dimension(120, 50));
        button.setMaximumSize(new Dimension(120, 50));
        button.setPreferredSize(new Dimension(120, 50));
        JScrollPane scrollPane = new JScrollPane(textArea);

        //添加
        centerpanel.add(scrollPane,BorderLayout.CENTER);
        norpanel.add(label);
        soupanel.add(button);
        panel.add(centerpanel,BorderLayout.CENTER);
        centerpanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panel.add(norpanel,BorderLayout.NORTH);
        panel.add(soupanel,BorderLayout.SOUTH);
        add(panel);
        //避免遮挡背景
        norpanel.setOpaque(false);
        centerpanel.setOpaque(false);
        soupanel.setOpaque(false);
        //监听器
        button.addActionListener(e->{
            System.out.println("确定按钮被点击, b=" + b + ", nums=" + nums + ", num=" + num + ", times=" + times + ", m=" + m);
            if (b) {
                try {
                    System.out.println("开始创建拼图页面...");
                    switch(nums) {
                        case 1:
                            new ChoicePlay(num, times, m, previousFrame);
                            break;
                        case 2:
                            new selfMode(num, times, previousFrame);
                            break;
                        case 3:
                            new nineMode(num, times, previousFrame);
                            break;
                        case 4:
                            new circleMode(num, times, previousFrame);
                            break;
                    }
                    System.out.println("拼图页面创建成功，关闭说明窗口");
                    dispose();
                } catch (Exception ex) {
                    System.out.println("进入拼图页面异常:");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "进入拼图页面失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("b为false，不创建拼图页面，直接关闭");
                dispose();
            }
        });
    }
}
