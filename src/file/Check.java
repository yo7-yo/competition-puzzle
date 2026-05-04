package file;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 假设你有一个 SetFont 工具类，如果没有，请注释掉 import 并删除相关调用
// import static tools.SetFont.SetFont;

public class Check extends JFrame {
    JPanel panel, norpanel, southPanel;
    // 表头：时间、模式、难度、限时、结果
    Object lie[] = {"操作时间", "修复模式", "难度(宫格)", "限时设置", "修复结果"};
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane scrollPane;
    JButton buttonSearch, buttonExport, buttonReturn;
    JTextField text;

    // 文件名常量，方便修改
    private static final String RECORD_FILE = "resources/txt/sign_table.txt";

    public Check(JFrame parentFrame) {
        init();
        setTitle("古建修复工程日志"); // 修改标题，更有文化感
        setSize(900, 600); // 稍微调小一点，适应笔记本屏幕
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(Check.this, "确定要退出历史记录吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    void init() {
        // 1. 创建不可编辑表
        tableModel = new DefaultTableModel(lie, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止用户修改表格数据
            }
        };

        table = new JTable(tableModel);
        // 设置行高
        table.setRowHeight(35);
        // 设置选中样式
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(200, 200, 200));

        // 2. 字体与样式设置 (如果没有SetFont类，直接在这里设置)
        Font headerFont = new Font("宋体", Font.BOLD, 16);
        Font contentFont = new Font("微软雅黑", Font.PLAIN, 14);

        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setBackground(new Color(139, 69, 19)); // 深褐色表头
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(contentFont);
        table.setBackground(new Color(253, 245, 230)); // 宣纸色背景

        // 3. 组件初始化
        scrollPane = new JScrollPane(table);
        buttonSearch = new JButton("查询日志");
        buttonExport = new JButton("导出记录"); // 新增导出按钮
        buttonReturn = new JButton("返回主页"); // 新增返回按钮
        text = new JTextField(25); // 增大搜索框长度

        panel = new JPanel(new BorderLayout());
        norpanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15)); // 调整间距
        southPanel = new JPanel(new BorderLayout()); // 改为BorderLayout，方便左右分布

        // 4. 搜索框样式
        text.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 增大字体
        text.setPreferredSize(new Dimension(200, 40)); // 搜索框变窄
        text.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19)));

        // 按钮样式
        styleButton(buttonSearch);
        styleButton(buttonExport);
        styleButton(buttonReturn);

        // 增大按钮尺寸
        buttonSearch.setPreferredSize(new Dimension(150, 50));
        buttonExport.setPreferredSize(new Dimension(150, 50));
        buttonReturn.setPreferredSize(new Dimension(150, 50));

        // 5. 布局组装
        // 顶部搜索区域 - 增大标签字体
        JLabel searchLabel = new JLabel("搜索关键字:");
        searchLabel.setFont(new Font("微软雅黑", Font.BOLD, 22)); // 增大字体
        norpanel.add(searchLabel);
        norpanel.add(text);
        norpanel.add(buttonSearch);

        // 底部按钮 - 一个靠左，一个靠右
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftButtonPanel.setOpaque(false);
        leftButtonPanel.add(buttonReturn);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.add(buttonExport);

        southPanel.add(leftButtonPanel, BorderLayout.WEST);
        southPanel.add(rightButtonPanel, BorderLayout.EAST);

        panel.add(norpanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        // 给主面板加个边框，像画框一样
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 230, 210)); // 整体背景色

        add(panel);

        // 6. 初始化加载数据
        refreshTable();

        // 7. 监听器
        buttonSearch.addActionListener(e -> {
            String ss = text.getText();
            if (ss.trim().isEmpty()) {
                refreshTable();
                table.setBackground(new Color(253, 245, 230));
            } else {
                searchRecord(ss.trim());
            }
        });

        // 导出功能监听
        buttonExport.addActionListener(e -> {
            exportRecord();
        });

        // 返回主页监听
        buttonReturn.addActionListener(e -> {
            dispose();
        });
    }

    // 辅助方法：按钮样式
    private void styleButton(JButton btn) {
        btn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        btn.setBackground(new Color(160, 82, 45)); // 赭石色
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    // 刷新表格（读取全部）
    private void refreshTable() {
        tableModel.setRowCount(0);
        readRecord(RECORD_FILE, tableModel);
        repaint();
    }

    // 搜索功能
    private void searchRecord(String keyword) {
        tableModel.setRowCount(0);
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(RECORD_FILE), "UTF-8"));
            String line;
            read.readLine(); // 跳过表头
            while ((line = read.readLine()) != null) {
                // 简单的模糊搜索
                if (line.contains(keyword)) {
                    String[] parts = line.split(" \\s+");
                    // 简单的数据清洗，防止列数不对
                    if(parts.length >= 5) {
                        tableModel.addRow(parts);
                    }
                }
            }
            read.close();
            // 搜索时高亮背景
            table.setBackground(new Color(204, 255, 255));
        } catch (Exception ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(this, "读取日志失败");
        }
    }

    // 从文件中读取数据
    public void readRecord(String file, DefaultTableModel tableModel) {
        try {
            File f = new File(file);
            if(!f.exists()) return; // 文件不存在直接返回

            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            // 跳过第一行（如果是表头的话，不过你的文件似乎没有表头，只有数据）
            // 如果文件第一行是数据，就不要 read.readLine()
            // 根据你的 getRecord 逻辑，文件第一行可能是天数，所以这里逻辑要小心

            // 修正逻辑：假设文件里全是数据行
            while ((line = read.readLine()) != null) {
                // 过滤空行
                if(line.trim().isEmpty()) continue;

                String[] parts = line.split(" \\s+");
                // 只有列数匹配才添加，防止报错
                if (parts.length >= 5) {
                    tableModel.addRow(parts);
                }
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得数据并写入文件 (静态方法，供其他类调用)
    public static void getRecord(String mode, int num, int times, boolean win) {
        LocalDateTime time = LocalDateTime.now();
        String timeStr = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 写入
        try {
            // 使用相对路径
            BufferedWriter writer = new BufferedWriter(new FileWriter(RECORD_FILE, true));

            // 格式化输出，让表格对齐更好看
            String record = String.format("%-19s   %-10s   %-8s   %-8s   %-6s",
                    timeStr, mode, num + "宫格", (times == 0 ? "无限制" : times + "秒"), (win ? "成功" : "失败"));

            writer.append(record);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 新增：导出功能
    private void exportRecord() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出日志文件");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                // 简单的文件复制
                BufferedReader reader = new BufferedReader(new FileReader(RECORD_FILE));
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));
                String line;
                while((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                reader.close();
                writer.close();
                JOptionPane.showMessageDialog(this, "日志导出成功！");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "导出失败：" + e.getMessage());
            }
        }
    }
}