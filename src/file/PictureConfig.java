package file;

import tools.Picture;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PictureConfig {
    private static final String CONFIG_FILE = "resources/txt/picture_config.properties";
    
    // 每个模式选择的图片
    private static Map<String, String> modeSelectedImages = new HashMap<>();
    
    // 模式名称常量
    public static final String MODE_RESIDENTIAL = "residential";    // 民居建筑 (ChoicePlay)
    public static final String MODE_OFFICIAL = "official";          // 官府建筑 (selfMode)
    public static final String MODE_PALACE = "palace";              // 宫殿建筑 (nineMode)
    public static final String MODE_BRIDGE = "bridge";              // 桥梁建筑 (circleMode)
    
    public static void saveConfig() {
        try {
            Properties props = new Properties();
            
            // 保存每个模式选择的图片
            for (Map.Entry<String, String> entry : modeSelectedImages.entrySet()) {
                props.setProperty("selectedImage_" + entry.getKey(), entry.getValue());
            }
            
            // 保存已获得的图片列表
            StringBuilder ownedImages = new StringBuilder();
            for (String img : Picture.ture) {
                if (ownedImages.length() > 0) {
                    ownedImages.append(",");
                }
                ownedImages.append(img);
            }
            props.setProperty("ownedImages", ownedImages.toString());
            
            File configFile = new File(CONFIG_FILE);
            configFile.getParentFile().mkdirs();
            
            try (FileOutputStream out = new FileOutputStream(configFile)) {
                props.store(out, "Picture Configuration");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                return;
            }
            
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(configFile)) {
                props.load(in);
            }
            
            // 加载每个模式选择的图片
            modeSelectedImages.clear();
            modeSelectedImages.put(MODE_RESIDENTIAL, props.getProperty("selectedImage_" + MODE_RESIDENTIAL, "image/晋商大院.png"));
            modeSelectedImages.put(MODE_OFFICIAL, props.getProperty("selectedImage_" + MODE_OFFICIAL, "image/直隶总督署.png"));
            modeSelectedImages.put(MODE_PALACE, props.getProperty("selectedImage_" + MODE_PALACE, "image/太和殿.png"));
            modeSelectedImages.put(MODE_BRIDGE, props.getProperty("selectedImage_" + MODE_BRIDGE, "image/赵州桥拼图.png"));
            
            // 设置全局默认图片为第一个模式的图片
            Picture.pic = modeSelectedImages.get(MODE_RESIDENTIAL);
            
            // 加载已获得的图片列表
            String ownedImagesStr = props.getProperty("ownedImages");
            if (ownedImagesStr != null && !ownedImagesStr.isEmpty()) {
                Picture.ture = new ArrayList<>();
                String[] images = ownedImagesStr.split(",");
                for (String img : images) {
                    Picture.ture.add(img.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 获取指定模式选择的图片
    public static String getSelectedImageForMode(String mode) {
        return modeSelectedImages.getOrDefault(mode, "image/晋商大院.png");
    }
    
    // 设置指定模式选择的图片
    public static void setSelectedImageForMode(String mode, String imagePath) {
        modeSelectedImages.put(mode, imagePath);
        Picture.pic = imagePath; // 同时更新全局默认
        saveConfig();
    }
    
    // 获取当前全局选择的图片
    public static String getCurrentSelectedImage() {
        return Picture.pic;
    }
    
    // 设置当前全局选择的图片（用于向后兼容）
    public static void setCurrentSelectedImage(String imagePath) {
        Picture.pic = imagePath;
        saveConfig();
    }
}
