package tools;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public class Music {
    private Thread bgmThread;
    private Clip bgmClip;
    private Player mp3Player;
    public static Music mmusic;
    private InputStream bgmStream;
    private volatile boolean shouldPlay = false;
    private String currentBgmPath = null; //当前BGM路径

        public static void playSound(String wavFileName) {
            // 开启新线程
            new Thread(() -> {
                Clip clip = null;
                try {
                    // 读取jar同目录的wav文件
                    File wavFile = new File(wavFileName);
                    // 加载音频文件流
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
                    // 打开并播放
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start(); // 只播放一次

                    // 播放完自动释放资源
                    Clip finalClip = clip;
                    //音频状态事件
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            finalClip.close();
                            try {
                                audioStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("音效播放失败：" + wavFileName + "，原因：" + e.getMessage());
                }
            }).start();
        }

    public void playBGM(String resourcePath) {
        if (resourcePath != null && resourcePath.equals(currentBgmPath)) {
            return;
        }
        stopBGM();
        currentBgmPath = resourcePath;

        shouldPlay = true;
        bgmThread = new Thread(() -> {
            while (shouldPlay) {
                try {
                    if (resourcePath != null && resourcePath.endsWith(".mp3")) {
                        // 先尝试从classpath加载
                        bgmStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
                        
                        // 如果classpath中没有，尝试从文件系统加载
                        if (bgmStream == null) {
                            String userDir = System.getProperty("user.dir");
                            String[] possiblePaths = {
                                userDir + "/resources/" + resourcePath,
                                userDir + "/" + resourcePath,
                                "resources/" + resourcePath,
                                resourcePath
                            };
                            
                            for (String filePath : possiblePaths) {
                                File file = new File(filePath);
                                if (file.exists()) {
                                    bgmStream = new java.io.FileInputStream(file);
                                    break;
                                }
                            }
                        }
                        
                        if (bgmStream == null) {
                            System.err.println("BGM文件未找到: " + resourcePath);
                            break;
                        }
                        mp3Player = new Player(bgmStream);
                        mp3Player.play();
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    if (shouldPlay) {
                        System.err.println("BGM 播放异常: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                } finally {
                    try {
                        if (mp3Player != null) mp3Player = null;
                        if (bgmStream != null) {
                            bgmStream.close();
                            bgmStream = null;
                        }
                    } catch (IOException ignored) {}
                }
            }
        }, "BGM-Player");

        bgmThread.setDaemon(true);
        bgmThread.start();
    }

    public void stopBGM() {
        shouldPlay = false;
        currentBgmPath = null;
        try {
            if (mp3Player != null) {
                mp3Player = null;
            }
            if (bgmStream != null) {
                bgmStream.close();
                bgmStream = null;
            }
        } catch (IOException ignored) {}

        if (bgmThread != null) {
            bgmThread.interrupt();
            bgmThread = null;
        }

        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }
}