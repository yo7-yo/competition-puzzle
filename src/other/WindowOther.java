package other;

import begin.BeginPuzzle;
import file.Files;
import file.PictureConfig;
import tools.BLabel;
import tools.LV;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static tools.SetFont.SetFont;
import tools.Music;
import tools.Picture;

public class WindowOther extends JFrame {
    JPanel panel,norpanel;
    JButton button;
    JLabel life;
    Music music;
    private JFrame previousFrame;
    
    JTabbedPane tabbedPane;
    Map<String, JPanel> categoryPanels = new HashMap<>();
    Map<String, BLabel[]> categoryLabels = new HashMap<>();
    Map<String, BLabel> categorySelectedLabels = new HashMap<>();

    private static final String[][] CATEGORIES = {
        {"民居建筑", "image/晋商大院.png", "image/江南居民拼图.png"},
        {"官府建筑", "image/直隶总督署.png", "image/平遥县衙.png"},
        {"皇宫建筑", "image/太和殿.png", "image/乾清宫.png"},
        {"桥梁建筑", "image/赵州桥拼图.png", "image/卢沟桥.png"}
    };
    
    private static final Map<String, String> IMAGE_NAMES = new HashMap<>();
    static {
        IMAGE_NAMES.put("image/晋商大院.png", "晋商大院");
        IMAGE_NAMES.put("image/江南居民拼图.png", "江南居民");
        IMAGE_NAMES.put("image/直隶总督署.png", "直隶总督署");
        IMAGE_NAMES.put("image/平遥县衙.png", "平遥县衙");
        IMAGE_NAMES.put("image/太和殿.png", "太和殿");
        IMAGE_NAMES.put("image/乾清宫.png", "乾清宫");
        IMAGE_NAMES.put("image/赵州桥拼图.png", "赵州桥");
        IMAGE_NAMES.put("image/卢沟桥.png", "卢沟桥");
    }
    
    private static final Map<String, String> IMAGE_DESCRIPTIONS = new HashMap<>();
    static {
        IMAGE_DESCRIPTIONS.put("image/晋商大院.png", 
            "晋商大院，位于山西省祁县，始建于清乾隆年间（约1756年），是清代晋商乔氏家族的宅院。占地面积约8700平方米，由6个大院、20个小院组成，共313间房屋。建筑风格为典型的北方四合院布局，砖雕、木雕、石雕精美绝伦，被誉为'北方民居建筑的一颗明珠'，现为国家重点文物保护单位。");
        IMAGE_DESCRIPTIONS.put("image/江南居民拼图.png", 
            "宏村古民居，位于安徽省黟县，始建于南宋时期，明清时期达到鼎盛，是徽派民居的代表。以牛形水系布局闻名，有'画里乡村'之美誉。建筑特点为粉墙黛瓦、马头墙、天井院落，木雕装饰精美。承志堂、树人堂等代表性民居建筑保存完好，被列为世界文化遗产。");
        IMAGE_DESCRIPTIONS.put("image/直隶总督署.png", 
            "直隶总督署，位于河北省保定市裕华西路，始建于清代，是中国现存最完整的清代省级衙署。建筑为硬山顶砖木结构，面阔五间，青砖灰瓦。正门上方悬挂'直隶总督部堂'匾额。门前有石阶七级，两侧各有一尊石狮子。现为全国重点文物保护单位，是研究清代官署建筑的重要实物资料。");
        IMAGE_DESCRIPTIONS.put("image/平遥县衙.png", 
            "平遥县衙，位于山西省平遥古城内西南角，是中国现存最完整的古代县级衙署。建筑为悬山顶砖木结构，面阔三间，青砖灰瓦。正门上方悬挂'平遥县衙'匾额，门前有石阶五级，两侧有'肃静''回避'告示牌。正堂内悬挂'明镜高悬'匾额，是研究明代地方官署建筑的重要遗存。");
        IMAGE_DESCRIPTIONS.put("image/太和殿.png", 
            "太和殿，位于北京故宫核心位置，是中国古代宫殿建筑的最高杰作。重檐庑殿顶，屋面覆盖金黄色琉璃瓦，殿建在三层汉白玉须弥座台基上。殿身面阔十一间，进深五间，朱红色圆柱排列整齐，檐下有金龙和玺彩画，金碧辉煌。是明清两代皇帝举行重大典礼的场所。");
        IMAGE_DESCRIPTIONS.put("image/乾清宫.png", 
            "乾清宫，位于北京故宫内廷，是明清皇帝的寝宫。重檐歇山顶，金黄色琉璃瓦屋面。正门上方悬挂'乾清宫'匾额，为皇帝御笔。殿前有宽阔的月台，汉白玉栏杆围绕，栏板雕刻龙凤呈祥图案。殿身面阔九间，朱红色圆柱，柱础为汉白玉莲花座，是皇家宫殿建筑的典范。");
        IMAGE_DESCRIPTIONS.put("image/赵州桥拼图.png", 
            "赵州桥，位于河北省赵县洨河上，建于隋代，是世界上现存最早、保存最完整的敞肩石拱桥。桥身由青石砌筑，单孔大拱跨度约三十七米，大拱两端各有两个小拱，形成独特的敞肩式结构。桥面两侧有石栏杆，栏板上雕刻有精美的龙纹图案，距今已有1400多年历史。");
        IMAGE_DESCRIPTIONS.put("image/苏州园林拼图.png", 
            "苏州古典园林，是中国江南私家园林的代表，以拙政园、留园、网师园等最为著名。园林以山水为主题，运用叠山、理水、建筑、植物等手法，创造'虽由人作，宛自天开'的艺术境界。园内有亭台楼阁、假山池沼、曲廊回环，粉墙黛瓦，漏窗花墙，体现中国古典园林的独特魅力。");
    }

    public WindowOther(JFrame previousFrame){
        this.previousFrame = previousFrame;
        init();
        setVisible(true);
        setSize(830,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(WindowOther.this, "确定要退出吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    if (previousFrame != null) {
                        previousFrame.setVisible(true);
                    }
                }
            }
        });
    }
    
    void init() {
        music=new Music();
        button=new JButton("返回");
        norpanel=new JPanel(new BorderLayout());
        
        SetFont(new Font("微软黑体",Font.PLAIN+Font.BOLD,25),button);
        button.setBackground(new Color(255, 239, 213));
        button.setForeground(new Color(101, 67, 33));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setMinimumSize(new Dimension(110, 50));
        button.setMaximumSize(new Dimension(110, 50));
        button.setPreferredSize(new Dimension(110, 50));
        
        life=new JLabel(""+ LV.lifevalue);
        life.setOpaque(false);
        life.setHorizontalTextPosition(SwingConstants.CENTER);
        life.setForeground(Color.BLACK);
        life.setFont(new Font("华文行楷", Font.BOLD, 35));
        
        try{
            BufferedImage images=Files.loadImage("image/aixin.png");
            Image scaledImage = images.getScaledInstance(170, 53, Image.SCALE_SMOOTH);
            life.setIcon(new ImageIcon(scaledImage));
        }catch (IOException e) {
            System.out.println("加载失败，重新加载中");
        }
        
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 205));

        // 创建分类标签页 - 固定在顶部不随滚动
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软黑体", Font.BOLD, 18));
        tabbedPane.setBackground(new Color(255, 250, 205));
        tabbedPane.setForeground(new Color(101, 67, 33));
        
        UIManager.put("TabbedPane.contentAreaColor", new Color(255, 250, 205));
        UIManager.put("TabbedPane.selected", new Color(255, 250, 205));
        
        // 为每个分类创建面板
        for (String[] category : CATEGORIES) {
            String categoryName = category[0];
            String[] imagePaths = new String[category.length - 1];
            System.arraycopy(category, 1, imagePaths, 0, imagePaths.length);
            
            // 使用 GridBagLayout 使内容居中
            JPanel categoryPanel = new JPanel(new GridBagLayout());
            categoryPanel.setBorder(BorderFactory.createEmptyBorder(-40, 10, 30, 10));
            categoryPanel.setBackground(new Color(255, 250, 205));
            categoryPanel.setOpaque(true);
            
            // 内部面板用于水平排列图片
            JPanel imagesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
            imagesPanel.setBackground(new Color(255, 250, 205));
            imagesPanel.setOpaque(true);
            
            BLabel[] labels = new BLabel[imagePaths.length];
            
            // 获取该分类之前保存的图片选择
            String mode = getCategoryMode(categoryName);
            String savedImage = (mode != null) ? PictureConfig.getSelectedImageForMode(mode) : null;
            boolean hasSetSelection = false;
            
            for (int i = 0; i < imagePaths.length; i++) {
                String path = imagePaths[i];
                final int index = i;
                
                try {
                    Image image = Files.loadImage(path);
                    Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                    final Image ma = scaledImage;
                    
                    labels[i] = new BLabel(path);
                    
                    String imageName = IMAGE_NAMES.getOrDefault(path, "第" + (i + 1) + "张");
                    TitledBorder title = BorderFactory.createTitledBorder(imageName);
                    title.setTitleFont(new Font("微软黑体", Font.BOLD, 23));
                    
                    if (Picture.ture.contains(labels[i].pi)) {
                        labels[i].use = true;
                    } else {
                        labels[i].use = false;
                    }
                    
                    if(Picture.pic.equals(labels[i].pi)){
                        BLabel.blabel = labels[i];
                    }
                    
                    // 先设置默认灰色边框
                    title.setBorder(new LineBorder(Color.darkGray, 1, true));
                    
                    Image IImage = ma;
                    if (labels[i].use == false) {
                        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = img.createGraphics();
                        g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                        g.drawImage(IImage, 0, 0, null);
                        g.dispose();
                        IImage = img;
                    }
                    labels[i].setIcon(new ImageIcon(IImage));
                    labels[i].setBorder(title);
                    imagesPanel.add(labels[i]);
                    
                    labels[i].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            BLabel l = (BLabel) e.getSource();
                            if (l.use == true) {
                                BLabel currentSelected = categorySelectedLabels.get(categoryName);
                                if(l.equals(currentSelected)){
                                    JOptionPane.showMessageDialog(WindowOther.this, "已是模板游戏图！");
                                } else {
                                    String oldName = IMAGE_NAMES.getOrDefault(currentSelected.pi, "未知图片");
                                    TitledBorder title1 = BorderFactory.createTitledBorder(oldName);
                                    title1.setTitleFont(new Font("微软黑体", Font.BOLD, 23));
                                    title1.setBorder(new LineBorder(Color.darkGray, 1, true));
                                    currentSelected.setBorder(title1);

                                    String newName = IMAGE_NAMES.getOrDefault(l.pi, "未知图片");
                                    TitledBorder title2 = BorderFactory.createTitledBorder(newName);
                                    title2.setTitleFont(new Font("微软黑体", Font.BOLD, 23));
                                    title2.setBorder(new LineBorder(Color.CYAN, 2, true));
                                    l.setBorder(title2);
                                    
                                    BLabel.blabel = l;
                                    BLabel.num = index + 1;
                                    categorySelectedLabels.put(categoryName, l);
                                    JOptionPane.showMessageDialog(WindowOther.this, "已把它设置成模板游戏图");
                                    Picture.pic = l.pi;
                                    // 根据分类保存对应模式的图片选择
                                    String mode = getCategoryMode(categoryName);
                                    if (mode != null) {
                                        PictureConfig.setSelectedImageForMode(mode, l.pi);
                                    }
                                }
                            } else {
                                int choice = JOptionPane.showConfirmDialog(WindowOther.this, "现在这个不属于您，您确定要花30生命值购买吗？", "确认", JOptionPane.YES_NO_OPTION);
                                if (choice == JOptionPane.YES_OPTION) {
                                    if (LV.lifevalue >= 30) {
                                        LV.lifevalue -= 30;
                                        life.setText("" + LV.lifevalue);
                                        l.use = true;
                                        Picture.ture.add(l.pi);
                                        PictureConfig.saveConfig();
                                        try {
                                            Image originalImage = Files.loadImage(l.pi);
                                            Image newScaled = originalImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                                            l.setIcon(new ImageIcon(newScaled));
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(WindowOther.this, "生命值不足");
                                    }
                                }
                            }
                        }
                    });
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "图片 " + path + " 加载失败！");
                }
            }
            
            // 统一设置选中状态：只给一个图片设置绿色框
            if (savedImage != null) {
                // 有保存的选择，找到对应的图片
                for (int i = 0; i < labels.length; i++) {
                    if (labels[i] != null && savedImage.equals(labels[i].pi)) {
                        String imageName = IMAGE_NAMES.getOrDefault(labels[i].pi, "第" + (i + 1) + "张");
                        TitledBorder selectedTitle = BorderFactory.createTitledBorder(imageName);
                        selectedTitle.setTitleFont(new Font("微软黑体", Font.BOLD, 23));
                        selectedTitle.setBorder(new LineBorder(Color.CYAN, 2, true));
                        labels[i].setBorder(selectedTitle);
                        BLabel.blabel = labels[i];
                        BLabel.num = i + 1;
                        Picture.pic = labels[i].pi;
                        categorySelectedLabels.put(categoryName, labels[i]);
                        hasSetSelection = true;
                        break;
                    }
                }
            }
            
            // 没有保存的选择，默认选第一个
            if (!hasSetSelection && labels[0] != null) {
                String imageName = IMAGE_NAMES.getOrDefault(labels[0].pi, "第1张");
                TitledBorder selectedTitle = BorderFactory.createTitledBorder(imageName);
                selectedTitle.setTitleFont(new Font("微软黑体", Font.BOLD, 23));
                selectedTitle.setBorder(new LineBorder(Color.CYAN, 2, true));
                labels[0].setBorder(selectedTitle);
                BLabel.blabel = labels[0];
                BLabel.num = 1;
                Picture.pic = labels[0].pi;
                // 根据分类保存对应模式的图片选择
                String m = getCategoryMode(categoryName);
                if (m != null) {
                    PictureConfig.setSelectedImageForMode(m, labels[0].pi);
                }
                categorySelectedLabels.put(categoryName, labels[0]);
            }
            
            // 将图片面板放入滚动面板（只有图片区域可滚动）
            JScrollPane imageScrollPane = new JScrollPane(imagesPanel);
            imageScrollPane.setBorder(BorderFactory.createEmptyBorder());
            imageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            imageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            imageScrollPane.setOpaque(false);
            imageScrollPane.getViewport().setOpaque(false);
            imageScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
            
            // 将滚动面板添加到分类面板中央
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            categoryPanel.add(imageScrollPane, gbc);
            
            categoryPanels.put(categoryName, categoryPanel);
            categoryLabels.put(categoryName, labels);
            tabbedPane.addTab(categoryName, categoryPanel);
        }

        // tabbedPane 直接放在内容面板中，不随滚动
        norpanel.add(button, BorderLayout.WEST);
        norpanel.add(life, BorderLayout.EAST);
        norpanel.setOpaque(false);
        norpanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(255, 250, 205));
        contentPanel.setOpaque(true);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(norpanel, BorderLayout.NORTH);
        add(panel);

        button.addActionListener(e -> {
            music.stopBGM();
            Music.mmusic.playBGM("sound/main_thing.mp3");
            if (previousFrame != null) {
                previousFrame.setVisible(true);
            }
            dispose();
        });
        music.playBGM("sound/picture.mp3");
    }
    
    // 根据分类名称获取对应的模式常量
    private String getCategoryMode(String categoryName) {
        switch (categoryName) {
            case "民居建筑": return PictureConfig.MODE_RESIDENTIAL;
            case "官府建筑": return PictureConfig.MODE_OFFICIAL;
            case "皇宫建筑": return PictureConfig.MODE_PALACE;
            case "桥梁建筑": return PictureConfig.MODE_BRIDGE;
            default: return null;
        }
    }
}
