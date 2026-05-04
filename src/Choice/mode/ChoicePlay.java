package Choice.mode;

import Choice.WindowChoice;
import begin.BeginPuzzle;
import file.Check;
import file.Files;
import tools.*;
import tools.dialog.WinCue;
import login.logMode;
import tools.dialog.DefaultCue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static tools.SetFont.SetFont;

public class ChoicePlay extends JFrame implements MouseListener {
    JPanel centerpanel,bgpanel,norpanel;
    JPanel NEpanel,NWpanel,p;
    JButton buttonReturn,buttonVip,buttonrule,buttonPreview;
    LLabel[][] labels;
    Locate locate;          //记录空白块的label
    int num,times,m;
    private boolean isHandled = false;   //倒计时是否可以重置
    private Timer countdownTimer;
    Music music;
    private BufferedImage originalImage;
    private JFrame previousFrame;
    private String currentImagePath;
    
    private String getImageName(String path) {
        switch (path) {
            case "image/晋商大院.png": return "晋商大院";
            case "image/江南居民拼图.png": return "江南居民";
            default: return "民居建筑";
        }
    }
    
    private String getImageDescription(String path) {
        switch (path) {
            case "image/晋商大院.png":
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>晋商大院</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p><b>📍 位置：</b>山西省祁县</p>"
                    + "<p><b>📅 年代：</b>清乾隆年间（约1756年）</p>"
                    + "<p><b>🏛️ 规模：</b>占地8700平方米，6个大院、20个小院，共313间房屋</p>"
                    + "<p><b>🎨 特色：</b>北方四合院布局，砖雕、木雕、石雕精美绝伦</p>"
                    + "<p><b>🏆 地位：</b>国家重点文物保护单位，被誉为'北方民居建筑的一颗明珠'</p>"
                    + "</body></html>";
            case "image/江南居民拼图.png":
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>宏村古民居</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p><b>📍 位置：</b>安徽省黟县</p>"
                    + "<p><b>📅 年代：</b>南宋始建，明清鼎盛</p>"
                    + "<p><b>🏛️ 布局：</b>牛形水系，'画里乡村'</p>"
                    + "<p><b>🎨 特色：</b>粉墙黛瓦、马头墙、天井院落，木雕精美</p>"
                    + "<p><b>🏆 地位：</b>世界文化遗产，徽派民居代表</p>"
                    + "</body></html>";
            default:
                return "<html><body style='font-family:微软雅黑;font-size:16px;line-height:1.8;'>"
                    + "<h2 style='color:#8B4513;text-align:center;'>民居建筑</h2>"
                    + "<hr style='border:1px solid #D2B48C;'>"
                    + "<p>中国传统民居建筑，体现了古代劳动人民的智慧和建筑艺术。</p>"
                    + "</body></html>";
        }
    }

    public ChoicePlay(int num, int times,int m){
        this(num, times, m, null);
    }

    public ChoicePlay(int num, int times,int m, JFrame previousFrame){
        this.m=m;
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
                int result = JOptionPane.showConfirmDialog(ChoicePlay.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // 停止倒计时
                    if (countdownTimer != null && countdownTimer.isRunning()) {
                        countdownTimer.stop();
                    }
                    // 记录失败结果
                    Check.getRecord("民居建筑", num, times, false);
                    // 重置状态
                    isHandled = false;
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
                Check.getRecord("民居建筑", num, times, false);
            } else if (choice == JOptionPane.NO_OPTION) {
                if (times != 0 && countdownTimer != null) {
                    countdownTimer.start();
                }
            }
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
        buttonVip.addActionListener((e)->{
            //输入账号、密码时，计时停止。
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
            }
            logMode loginWindow = new logMode();
            loginWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    //登陆成功才能进去实现一遍过功能。
                    if (logMode.Getsu()) {
                        GetIt();
                        logMode.Setsu(false);//设置为false方便下次用
                        repaint();
                    }
                    //然后继续计时
                    if (times != 0 && countdownTimer != null) {
                        countdownTimer.start();
                    }
                }
            });
        });
        buttonrule.addActionListener(e->{
            new Introduction(num,times,1,false);
        });
        buttonPreview.addActionListener(e->{
            showPreview();
        });

        //设定
        bgpanel.setBackground(Color.gray);
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
        //times=0时就不计时。
        if(times!=0){
            p.add(getCountDown(times),BorderLayout.CENTER);
        }else{
            p.add(new JLabel(""),BorderLayout.CENTER);
        }
        //加加加加到厌倦（组件）
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
        int ci=0;
        while(ci++<10) {
            try {
                // 普通模式只能使用民居建筑图片
                String[] allowedImages = {"image/晋商大院.png", "image/江南居民拼图.png"};
                String imageToLoad = allowedImages[0];
                for (String img : allowedImages) {
                    if (img.equals(Picture.pic)) {
                        imageToLoad = Picture.pic;
                        break;
                    }
                }
                // 使用民居模式保存的图片
                String modeImage = file.PictureConfig.getSelectedImageForMode(file.PictureConfig.MODE_RESIDENTIAL);
                if (modeImage != null && !modeImage.isEmpty()) {
                    for (String img : allowedImages) {
                        if (img.equals(modeImage)) {
                            imageToLoad = modeImage;
                            break;
                        }
                    }
                }
                currentImagePath = imageToLoad;
                BufferedImage image=Files.loadImage(imageToLoad);
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
                // 保存原图引用
                originalImage = image;
                // 设置拼图面板大小，让窗口自适应图片
                centerpanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                // 创建白色空白块
                int blockWidth = image.getWidth() / num;
                int blockHeight = image.getHeight() / num;
                BufferedImage image2 = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image2.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, blockWidth, blockHeight);
                g2d.dispose();
                // 使用getSubimage直接切割
                int y = 0;
                for(int i=0;i<num;++i){
                    int x = 0;
                    for(int j=0;j<num;++j){
                        if(i==num-1&&j==num-1){
                            labels[i][j]=new LLabel(new IIcon(image2,i,j),i,j);
                            centerpanel.add(labels[i][j]);
                        }else {
                            labels[i][j] = new LLabel(
                                new IIcon(
                                    image.getSubimage(x, y, blockWidth, blockHeight),
                                    i, j
                                ), i, j
                            );
                            centerpanel.add(labels[i][j]);
                        }
                        x += blockWidth;
                    }
                    y += blockHeight;
                }
                break;
            } catch (IOException e) {
                System.out.println("加载失败，重新加载中");
            }
        }
        //适应图片大小
        pack();
        //空白块
        locate=new Locate(labels[num-1][num-1]);
        //每块拼图加监视器
        for(int i=0;i<num;++i){
            for(int j=0;j<num;++j){
                labels[i][j].addMouseListener(this);
            }
        }
        //打乱拼图
        Puzzle();
        repaint();
        //播放音乐
        music.playBGM("sound/main_thing.mp3");
    }
    void Puzzle() {
        int i=0;
        int cishu;
        //不同宫格不同的打乱次数
        if(num<=5&&num>=3){
            cishu=(int)(Math.random()*2+50);
        }else{
            cishu=(int)(Math.random()*4+50);
        }
        while(i++<cishu) {
            LLabel label=locate.getlabel();
            switch ((int)(Math.random()*4)) {
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
                case 3://向下
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
    //与白块交换位置
    @Override
    public void mousePressed(MouseEvent e) {
        LLabel llabel=(LLabel)e.getSource();//点到的块
        LLabel label=locate.getlabel();//白块

        if(llabel.getRow()+1==label.getRow()&&llabel.getCol()==label.getCol()||llabel.getRow()-1==label.getRow()&&llabel.getCol()==label.getCol()||llabel.getRow()==label.getRow()&&llabel.getCol()+1==label.getCol()||llabel.getRow()==label.getRow()&&llabel.getCol()-1==label.getCol()) {
           //交换图标
            Icon a;
            LLabel b=(LLabel)e.getSource();
            a=b.getIcon();
            b.setIcon(label.getIcon());
            label.setIcon(a);
            locate.change(b);
            //完成提示就会有赢的界面
            if(result()) {
                new WinCue(this,num,times,m,music,previousFrame);
            }

        }
    }
    public void mouseClicked(MouseEvent e){};
    public void mouseReleased(MouseEvent e){};
    public void mouseEntered(MouseEvent e){};
    public void mouseExited(MouseEvent e){};

    //开发者模式
    void GetIt() {
        int ci = 0;
        while (ci++ < 10) {
            try {
                BufferedImage image2 = Files.loadImage(currentImagePath);
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
                // 使用getSubimage直接切割
                int y = 0;
                for (int i = 0; i < num; ++i) {
                    int x = 0;
                    for (int j = 0; j < num; ++j) {
                        if (i == num - 1 && j == num - 1) {
                            labels[i][j].setIcon(new IIcon(image22, i, j));
                        } else {
                            labels[i][j].setIcon(new IIcon(
                                image2.getSubimage(x, y, blockWidth, blockHeight),
                                i, j
                            ));
                        }
                        x += blockWidth;
                    }
                    y += blockHeight;
                }
                break;
            } catch (IOException e) {
                System.out.println("加载失败，重新加载中");
            }
        }
        //记录白块位于的label（右下角）
        locate.change(labels[num - 1][num - 1]);
        LLabel whiteLabel = locate.getlabel();
        
        //打乱一步：将白块与它左边的拼图块交换
        IIcon whiteIcon = (IIcon) whiteLabel.getIcon();
        IIcon pieceIcon = (IIcon) labels[num - 1][num - 2].getIcon();
        whiteLabel.setIcon(pieceIcon);
        labels[num - 1][num - 2].setIcon(whiteIcon);
        locate.change(labels[num - 1][num - 2]);
        repaint();
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

    //倒计时
    public JLabel getCountDown(int a) {
        JLabel labelll = new JLabel("倒计时: " + a + " 秒", SwingConstants.CENTER);
        int[] remainingSeconds = {a};
        Font f=new Font("华文行楷",Font.PLAIN,40);
        SetFont(f,labelll);
        labelll.setForeground(Color.RED);
        //每秒执行一次的定时器
        countdownTimer = new Timer(1000, e -> {
            if (isHandled) {
                ((Timer) e.getSource()).stop();
                return;//
            }
            if (result() || remainingSeconds[0] <= 0) {
                isHandled = true;
                ((Timer) e.getSource()).stop();
                labelll.setText(result() ? "拼图完成！" : "时间结束");
                //延缓
                SwingUtilities.invokeLater(() -> {
                    if(!result()) new DefaultCue(this,num,times,m,music,previousFrame);
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
        //重置倒计时的方法
        isHandled = false;
    }
    
    //查看原图
    void showPreview() {
        JDialog previewDialog = new JDialog(this, "原图预览", true);
        previewDialog.setLayout(new BorderLayout());
        
        int scaledWidth = (int)(originalImage.getWidth() * 0.7);
        int scaledHeight = (int)(originalImage.getHeight() * 0.7);
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
