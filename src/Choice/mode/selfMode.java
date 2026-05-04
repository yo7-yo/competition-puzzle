package Choice.mode;

import Choice.WindowChoice;
import file.Check;
import file.Files;
import tools.*;
import tools.dialog.WinCue;
import login.logMode;
import tools.dialog.DefaultCue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static tools.SetFont.SetFont;
//选择后的游戏界面
public class selfMode extends JFrame implements MouseListener {
    JPanel centerpanel,bgpanel,norpanel;
    JPanel NEpanel,NWpanel,p;
    JButton buttonReturn,buttonVip,buttonrule,buttonPreview;
    LLabel[][] labels;
    Locate locate;          //空白块
    Random random;
    int num,times;//九宫格与倒计时
    private boolean isHandled = false;
    private Timer countdownTimer;
    boolean focus=true;     //选完白块后才能启用一遍过
    Music music;
    private BufferedImage originalImage;
    private JFrame previousFrame;
    private String currentImagePath;
    
    private String getImageName(String path) {
        switch (path) {
            case "image/直隶总督署.png": return "直隶总督署";
            case "image/平遥县衙.png": return "平遥县衙";
            default: return "官府建筑";
        }
    }
    
    private String getImageDescription(String path) {
        switch (path) {
            case "image/直隶总督署.png":
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>直隶总督署</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p><b>📍 位置：</b>河北省保定市裕华西路</p>"
                    + "<p><b>📅 年代：</b>清代</p>"
                    + "<p><b>🏛️ 规模：</b>硬山顶砖木结构，面阔五间，青砖灰瓦</p>"
                    + "<p><b>🎨 特色：</b>正门悬挂'直隶总督部堂'匾额，门前石阶七级，两侧石狮子</p>"
                    + "<p><b>🏆 地位：</b>全国重点文物保护单位，中国现存最完整的清代省级衙署</p>"
                    + "</body></html>";
            case "image/平遥县衙.png":
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>平遥县衙</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p><b> 位置：</b>山西省平遥古城内西南角</p>"
                    + "<p><b>📅 年代：</b>明代</p>"
                    + "<p><b>🏛️ 规模：</b>悬山顶砖木结构，面阔三间，青砖灰瓦</p>"
                    + "<p><b>🎨 特色：</b>正堂悬挂'明镜高悬'匾额，门前'肃静''回避'告示牌</p>"
                    + "<p><b>🏆 地位：</b>中国现存最完整的古代县级衙署</p>"
                    + "</body></html>";
            default:
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>官府建筑</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p>中国古代官府建筑，体现了传统官署建筑的威严与精美。</p>"
                    + "</body></html>";
        }
    }

    public selfMode(int num, int times){
        this(num, times, null);
    }

    public selfMode(int num, int times, JFrame previousFrame){
        this.num=num;
        this.times=times;
        this.previousFrame=previousFrame;
        init();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(selfMode.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // 停止倒计时
                    if (countdownTimer != null && countdownTimer.isRunning()) {
                        countdownTimer.stop();
                    }
                    // 记录失败结果
                    Check.getRecord("官府建筑", num, times, false);
                    isHandled = false; // 重置状态
                    dispose();
                    // 返回到进入游戏前的界面
                    if (previousFrame != null) {
                        previousFrame.setVisible(true);
                    }
                }
            }
        });
    }

    public void init() {
        //初始化
        music=new Music();
        random=new Random();
        bgpanel=new JPanel(new BorderLayout());
        p=new JPanel(new BorderLayout());
        centerpanel=new JPanel(new GridLayout(num,num,5,5));
        centerpanel.setBackground(Color.WHITE);
        norpanel=new JPanel(new BorderLayout());
        NEpanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        NWpanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonReturn=new JButton("返回");
        buttonVip=new JButton("开发者模式");
        buttonrule=new JButton("规则");
        buttonPreview=new JButton("查看原图");

        //监视器
        buttonReturn.addActionListener((e)-> {
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
            }
            int choice = JOptionPane.showConfirmDialog(this, "确定要退出吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                isHandled = false;
                music.stopBGM();
                Music.mmusic.playBGM("sound/main_thing.mp3");
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                } else {
                    new WindowChoice();
                }
                dispose();
                Check.getRecord("官府建筑",num,times,false);
            } else if (choice == JOptionPane.NO_OPTION) {
                if (times != 0 && countdownTimer != null) {
                    countdownTimer.start();
                }
            }
        });
        buttonVip.addActionListener((e)->{
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
            }
            logMode loginWindow = new logMode();
            loginWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (logMode.Getsu()) {
                        GetIt();
                        logMode.Setsu(false);
                        repaint();
                    }
                    if (times != 0 && countdownTimer != null) {
                        countdownTimer.start();
                    }
                }
            });
        });
        buttonrule.addActionListener(e->{
            new Introduction(num,times,2,false);
        });
        buttonPreview.addActionListener(e->{
            showPreview();
        });
        //按钮字体
        Font f=new Font("华文行楷",Font.PLAIN,30);
        JButton buttonIntro = new JButton("建筑介绍");
        SetFont(f, buttonIntro);
        buttonIntro.setBackground(new Color(255, 239, 213));
        buttonIntro.setOpaque(true);
        buttonIntro.setBorderPainted(false);
        buttonIntro.setForeground(new Color(101, 67, 33));
        buttonIntro.addActionListener(e -> {
            String description = getImageDescription(currentImagePath);
            JLabel label = new JLabel(description);
            label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            JOptionPane.showMessageDialog(this, label, "建筑介绍", JOptionPane.INFORMATION_MESSAGE);
        });
        SetFont(f,buttonVip,buttonReturn,buttonrule,buttonPreview,buttonIntro);
        //开发者模式按钮颜色 - 金黄色背景配深棕色文字
        buttonVip.setBackground(new Color(255, 215, 0));
        buttonVip.setOpaque(true);
        buttonVip.setBorderPainted(false);
        buttonVip.setForeground(new Color(80, 40, 0));
        buttonReturn.setBackground(new Color(255, 239, 213));
        buttonReturn.setOpaque(true);
        buttonReturn.setBorderPainted(false);
        buttonReturn.setForeground(new Color(101, 67, 33));
        buttonrule.setBackground(new Color(255, 239, 213));
        buttonrule.setOpaque(true);
        buttonrule.setBorderPainted(false);
        buttonrule.setForeground(new Color(101, 67, 33));
        buttonPreview.setBackground(new Color(255, 239, 213));
        buttonPreview.setOpaque(true);
        buttonPreview.setBorderPainted(false);
        buttonPreview.setForeground(new Color(101, 67, 33));

        //加加加加到厌倦
        JPanel leftPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        leftPanel.setOpaque(false);
        leftPanel.add(buttonReturn);
        leftPanel.add(buttonPreview);
        leftPanel.add(buttonIntro);
        norpanel.add(leftPanel,BorderLayout.WEST);
        p.add(buttonrule,BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
        norpanel.add(p,BorderLayout.CENTER);
        norpanel.add(buttonVip,BorderLayout.EAST);
        bgpanel.add(norpanel,BorderLayout.NORTH);
        bgpanel.add(centerpanel,BorderLayout.CENTER);
        add(bgpanel);

        //贴拼图
        labels=new LLabel[num][num];
        int u=0;
        while(u++<10) {
            try {
                // 自定义模式只能使用官府建筑图片
                String[] allowedImages = {"image/直隶总督署.png", "image/平遥县衙.png"};
                String imageToLoad = allowedImages[0];
                for (String img : allowedImages) {
                    if (img.equals(Picture.pic)) {
                        imageToLoad = Picture.pic;
                        break;
                    }
                }
                // 使用官府模式保存的图片
                String modeImage = file.PictureConfig.getSelectedImageForMode(file.PictureConfig.MODE_OFFICIAL);
                if (modeImage != null && !modeImage.isEmpty()) {
                    for (String img : allowedImages) {
                        if (img.equals(modeImage)) {
                            imageToLoad = modeImage;
                            break;
                        }
                    }
                }
                originalImage = Files.loadImage(imageToLoad);
                currentImagePath = imageToLoad;  // 保存当前图片路径
                BufferedImage image = originalImage;
                // 固定总大小（基于9x9，每块155像素）
                int fixedTotalSize = 9 * 155;
                // 计算每个拼图块的大小
                int targetBlockSize = fixedTotalSize / num;
                // 计算缩放比例
                double scale = (double)targetBlockSize / (image.getWidth() / num);
                int targetWidth = (int)(image.getWidth() * scale);
                int targetHeight = (int)(image.getHeight() * scale);
                // 调整尺寸使其能被num整除
                targetWidth = (targetWidth / num) * num;
                targetHeight = (targetHeight / num) * num;
                // 缩放图片
                BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
                g.dispose();
                image = resizedImage;
                int y=0;
                for(int i=0;i<num;++i){
                    int x=0;
                    for(int j=0;j<num;++j){
                        // 所有格子都用原图填充（没有白块）
                        labels[i][j] = new LLabel(new IIcon(image.getSubimage(x, y, image.getWidth() / num, image.getHeight() / num),i,j),i,j);
                        centerpanel.add(labels[i][j]);
                        x += image.getWidth() / num;
                    }
                    y+=image.getHeight() / num;
                }
                break;
            } catch (IOException e) {
                System.out.println("加载失败，重新加载中");
            }
        }
        pack();
        repaint();
        for(int i=0;i<num;++i){
            for(int j=0;j<num;++j){
                labels[i][j].addMouseListener(this);
            }
        }
        //选择白块
        JDialog dialog = new JDialog(this, "提示", false);
        Object[] options = {"确定"};
        //数组中选一个
        JOptionPane optionPane = new JOptionPane("请选择白块",JOptionPane.INFORMATION_MESSAGE,JOptionPane.DEFAULT_OPTION, null, options);
        //监听选择
        optionPane.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                // 关闭对话框
                dialog.dispose();
            }
        });
        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        repaint();
        music.playBGM("sound/main_thing.mp3");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        LLabel llabel=(LLabel)e.getSource();
        if(focus==true){
            locate=new Locate(llabel);
            int ci=0;
            while(ci++<10) {
                try {
                    BufferedImage image = Files.loadImage(Picture.pic);
                    // 使用与初始化相同的缩放逻辑
                    int fixedTotalSize = 9 * 155;
                    int targetBlockSize = fixedTotalSize / num;
                    double scale = (double)targetBlockSize / (image.getWidth() / num);
                    int targetWidth = (int)(image.getWidth() * scale);
                    int targetHeight = (int)(image.getHeight() * scale);
                    targetWidth = (targetWidth / num) * num;
                    targetHeight = (targetHeight / num) * num;
                    
                    // 创建与拼图块大小相同的白块
                    int blockWidth = targetWidth / num;
                    int blockHeight = targetHeight / num;
                    BufferedImage whiteBlock = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = whiteBlock.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, blockWidth, blockHeight);
                    g.dispose();
                    
                    llabel.setIcon(new IIcon(whiteBlock, llabel.getRow(), llabel.getCol()));
                    break;
                } catch (IOException ex) {
                    System.out.println("加载失败，重新加载中");
                }
            }
            focus=false;
            JOptionPane.showMessageDialog(null, "游戏开始", "提示", JOptionPane.INFORMATION_MESSAGE);
            if(times!=0){
                p.add(getCountDown(times),BorderLayout.CENTER);
            }else{
                p.add(new JLabel(""),BorderLayout.CENTER);
            }
            Puzzle();
            norpanel.repaint();
        }else {
            LLabel label = locate.getlabel();
            if (llabel.getRow() + 1 == label.getRow() && llabel.getCol() == label.getCol() || llabel.getRow() - 1 == label.getRow() && llabel.getCol() == label.getCol() || llabel.getRow() == label.getRow() && llabel.getCol() + 1 == label.getCol() || llabel.getRow() == label.getRow() && llabel.getCol() - 1 == label.getCol()) {
                Icon a;
                LLabel b = (LLabel) e.getSource();
                a = b.getIcon();
                b.setIcon(label.getIcon());
                label.setIcon(a);
                locate.change(b);

                //完成提示+贴图
                if (result()) {
                    new WinCue(this,num,times,2,music,previousFrame);
                }
            }
        }
    }
    public void mouseClicked(MouseEvent e){};
    public void mouseReleased(MouseEvent e){};
    public void mouseEntered(MouseEvent e){};
    public void mouseExited(MouseEvent e){};

    //打乱
    void Puzzle() {
        int i=0;
        int cishu=0;
        //足够的次数
        if(num<=5&&num>=3){
            cishu=(int)(Math.random()*2+50);
        }else{
            cishu=(int)(Math.random()*4+50);
        }
        while(i++<cishu) {
            LLabel label=locate.getlabel();
            switch (random.nextInt(4)) {
                case 0://向左
                    if (label.getCol() >= 1) {
                        LLabel llabel = labels[label.getRow()][label.getCol() - 1];
                        Icon a;
                        a = llabel.getIcon();
                        llabel.setIcon(label.getIcon());
                        label.setIcon(a);
                        locate.change(llabel);
                    }
                    break;
                case 1://向右
                    if (label.getCol() <= num-2) {
                        LLabel llabel = labels[label.getRow()][label.getCol() + 1];
                        Icon a;
                        a = llabel.getIcon();
                        llabel.setIcon(label.getIcon());
                        label.setIcon(a);
                        locate.change(llabel);
                    }
                    break;
                case 2://向上
                    if (label.getRow() >=1) {
                        LLabel llabel = labels[label.getRow() - 1][label.getCol()];
                        Icon a;
                        a = llabel.getIcon();
                        llabel.setIcon(label.getIcon());
                        label.setIcon(a);
                        locate.change(llabel);
                    }
                    break;
                case 3:
                    if (label.getRow() <= num-2) {
                        LLabel llabel = labels[label.getRow() + 1][label.getCol()];
                        Icon a;
                        a = llabel.getIcon();
                        llabel.setIcon(label.getIcon());
                        label.setIcon(a);
                        locate.change(llabel);
                    }
                    break;

            }
        }
    }

    //开发者模式
    void GetIt(){
        if(!focus) {
            IIcon b = (IIcon) locate.getlabel().getIcon();  // 保存当前白块的位置信息
            int ci=0;
            while(ci++<10) {
                try {
                    BufferedImage image2 =Files.loadImage(currentImagePath);
                    // 固定总大小（基于9x9，每块155像素）
                    int fixedTotalSize = 9 * 155;
                    int targetBlockSize = fixedTotalSize / num;
                    double scale = (double)targetBlockSize / (image2.getWidth() / num);
                    int targetWidth = (int)(image2.getWidth() * scale);
                    int targetHeight = (int)(image2.getHeight() * scale);
                    // 调整尺寸使其能被num整除
                    targetWidth = (targetWidth / num) * num;
                    targetHeight = (targetHeight / num) * num;
                    // 缩放图片
                    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.drawImage(image2, 0, 0, targetWidth, targetHeight, null);
                    g.dispose();
                    image2 = resizedImage;
                    // 创建白色空白块
                    int blockWidth = image2.getWidth() / num;
                    int blockHeight = image2.getHeight() / num;
                    BufferedImage image22 = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = image22.createGraphics();
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, blockWidth, blockHeight);
                    g2d.dispose();
                    // 所有格子都先用原图填充（包括最后一个）
                    int y = 0;
                    for (int i = 0; i < num; ++i) {
                        int x = 0;
                        for (int j = 0; j < num; ++j) {
                            labels[i][j].setIcon(new IIcon(
                                image2.getSubimage(x, y, blockWidth, blockHeight),
                                i, j
                            ));
                            x += blockWidth;
                        }
                        y += blockHeight;
                    }
                    // 然后把原来白块位置的格子替换成白块
                    labels[b.row][b.col].setIcon(new IIcon(image22, b.row, b.col));
                    break;
                } catch (IOException e) {
                    System.out.println("加载失败，重新加载中");
                }
            }

            // 重新初始化 locate
            locate = new Locate(labels[num - 1][num - 1]);
            
            // 更新白块位置到原来的位置
            locate.change(labels[b.row][b.col]);
            LLabel label = locate.getlabel();
            
            // 只打乱最后两步
            switch (random.nextInt(2)) {
                case 0:
                    // 白块与左边交换
                    IIcon i = (IIcon) label.getIcon();
                    label.setIcon(labels[b.row][b.col-1].getIcon());
                    labels[b.row][b.col-1].setIcon(i);
                    locate.change(labels[b.row][b.col-1]);
                    break;
                case 1:
                    // 白块与上边交换
                    IIcon ii = (IIcon) label.getIcon();
                    label.setIcon(labels[b.row-1][b.col].getIcon());
                    labels[b.row-1][b.col].setIcon(ii);
                    locate.change(labels[b.row-1][b.col]);
                    break;
            }
            repaint();
        }
    }

    //判断是否结束
    public boolean result() {
        for (int i = 0; i < num; ++i) {
            for (int j = 0; j < num; ++j) {
                IIcon ii=(IIcon)labels[i][j].getIcon();
                if (ii.row!=i||ii.col!=j) return false;
            }
        }
        return true;
    }


    public JLabel getCountDown(int a) {
        JLabel labelll = new JLabel("倒计时: " + a + " 秒", SwingConstants.CENTER);
        int[] remainingSeconds = {a};
        Font f=new Font("华文行楷",Font.PLAIN,40);
        SetFont(f,labelll);
        labelll.setForeground(Color.RED);

        // 改为赋值给类成员countdownTimer
        countdownTimer = new Timer(1000, e -> {
            if (isHandled) {
                ((Timer) e.getSource()).stop();
                return;
            }
            if (result() || remainingSeconds[0] <= 0) {
                isHandled = true;
                ((Timer) e.getSource()).stop();
                labelll.setText(result() ? "拼图完成！" : "时间结束");
                SwingUtilities.invokeLater(() -> {
                    if(!result()) new DefaultCue(this,num,times,2,music,previousFrame);
                });
                return;
            }
            remainingSeconds[0]--;
            labelll.setText("倒计时: " + remainingSeconds[0] + " 秒");
        });
        countdownTimer.start();
        return labelll;
    }
    public void reset() {
        isHandled = false;
        focus=true;
    }
    
    //查看原图
    void showPreview() {
        JDialog previewDialog = new JDialog(this, "原图预览", true);
        previewDialog.setLayout(new BorderLayout());
        
        int scaledWidth = (int)(originalImage.getWidth() * 0.4);
        int scaledHeight = (int)(originalImage.getHeight() * 0.4);
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage previewImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = previewImg.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        
        JLabel imageLabel = new JLabel(new ImageIcon(previewImg));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("华文行楷", Font.PLAIN, 16));
        closeBtn.setPreferredSize(new Dimension(80, 30));
        closeBtn.setBackground(new Color(255, 239, 213));
        closeBtn.setForeground(new Color(101, 67, 33));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> previewDialog.dispose());
        buttonPanel.add(closeBtn);
        
        previewDialog.add(centerPanel, BorderLayout.CENTER);
        previewDialog.add(buttonPanel, BorderLayout.SOUTH);
        previewDialog.pack();
        previewDialog.setLocationRelativeTo(null);
        previewDialog.setVisible(true);
    }
}
