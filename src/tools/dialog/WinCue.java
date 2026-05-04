package tools.dialog;

import Choice.WindowChoice;
import Choice.mode.ChoicePlay;
import Choice.mode.circleMode;
import Choice.mode.nineMode;
import Choice.mode.selfMode;
import file.Check;
import file.Files;
import tools.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WinCue extends JDialog {
    private int num, times, m;
    private Music music;
    private JFrame parentFrame;
    private JFrame previousFrame;

    public WinCue(JFrame jFrame, int num, int times, int m, Music music, JFrame previousFrame) {
        super(jFrame, "恭喜通关", true);
        this.num = num;
        this.times = times;
        this.m = m;
        this.music = music;
        this.parentFrame = jFrame;
        this.previousFrame = previousFrame;

        init();
    }

    private void init() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            {
                try {
                    bgImage = Files.loadImage("image/胜利背景.png");
                } catch (IOException e) {
                    e.printStackTrace();
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
        mainPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton returnBtn = new JButton("返回");
        returnBtn.setFont(new Font("华文行楷", Font.BOLD, 20));
        returnBtn.setPreferredSize(new Dimension(120, 45));
        returnBtn.setBackground(new Color(255, 239, 213));
        returnBtn.setForeground(new Color(101, 67, 33));
        returnBtn.setBorderPainted(false);
        returnBtn.setFocusPainted(false);

        JButton replayBtn = new JButton("再来一局");
        replayBtn.setFont(new Font("华文行楷", Font.BOLD, 20));
        replayBtn.setPreferredSize(new Dimension(120, 45));
        replayBtn.setBackground(new Color(255, 239, 213));
        replayBtn.setForeground(new Color(101, 67, 33));
        replayBtn.setBorderPainted(false);
        replayBtn.setFocusPainted(false);

        buttonPanel.add(returnBtn);
        buttonPanel.add(replayBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modeName = getModeName(m);
                Check.getRecord(modeName, num, times, true);
                music.stopBGM();
                Music.mmusic.playBGM("sound/main_thing.mp3");
                dispose();
                parentFrame.dispose();
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                } else {
                    new WindowChoice();
                }
            }
        });

        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modeName = getModeName(m);
                Check.getRecord(modeName, num, times, true);
                System.out.println("再来一局按钮被点击，m=" + m + ", num=" + num + ", times=" + times);
                dispose();
                parentFrame.dispose();
                System.out.println("准备创建新游戏窗口...");
                switch (m) {
                    case 0:
                    case 1:
                        new ChoicePlay(num, times, 1);
                        break;
                    case 2:
                        new selfMode(num, times);
                        break;
                    case 3:
                        new nineMode(num, times);
                        break;
                    case 4:
                        new circleMode(num, times);
                        break;
                }
                System.out.println("新游戏窗口创建完成");
            }
        });
        
        setVisible(true);
    }

    private String getModeName(int m) {
        switch (m) {
            case 0:
            case 1:
                return "民居建筑";
            case 2:
                return "官府建筑";
            case 3:
                return "皇宫建筑";
            case 4:
                return "桥梁建筑";
            default:
                return "未知模式";
        }
    }
}
