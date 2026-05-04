package file;

import login.Login;
import tools.LV;
import tools.Picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Files {
    public static void readfiles(String filename) {
        try {
            String line;
            BufferedReader read = new BufferedReader(new FileReader(filename));
            // 跳过时间戳行
            read.readLine();
            // 清空已有数据，防止重复
            Picture.ture.clear();
            // 读取数据
            while ((line = read.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("lifevalue=")) {
                    String[] parts = line.split("=");
                    LV.lifevalue = Integer.parseInt(parts[1].trim());
                } else if (line.startsWith("havePicture=")) {
                    String[] parts = line.split("[=,]");
                    for(int i=1;i<parts.length;++i){
                        String picPath = parts[i].trim();
                        if (!picPath.equals("null") && !picPath.isEmpty() && !Picture.ture.contains(picPath)) {
                            Picture.ture.add(picPath);
                        }
                    }
                    // 设置第一个图片为默认选择
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        Picture.pic = parts[1].trim();
                    }
                } else if (line.startsWith("choose=")) {
                    // 读取每个模式选择的图片
                    for (int i = 1; i <= 4; i++) {
                        String chooseLine = read.readLine();
                        if (chooseLine != null) {
                            String[] parts = chooseLine.trim().split(" ");
                            if (parts.length >= 2) {
                                String imagePath = parts[1].trim();
                                if (!imagePath.isEmpty()) {
                                    switch (i) {
                                        case 1: PictureConfig.setSelectedImageForMode(PictureConfig.MODE_RESIDENTIAL, imagePath); break;
                                        case 2: PictureConfig.setSelectedImageForMode(PictureConfig.MODE_OFFICIAL, imagePath); break;
                                        case 3: PictureConfig.setSelectedImageForMode(PictureConfig.MODE_PALACE, imagePath); break;
                                        case 4: PictureConfig.setSelectedImageForMode(PictureConfig.MODE_BRIDGE, imagePath); break;
                                    }
                                }
                            }
                        }
                    }
                } else if (line.startsWith("sign_day=")) {
                    String[] parts = line.split("=");
                    LV.signDate = parts[1].trim();
                }
            }
            read.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    //写入数据到文件中
    public static void Writefiles(String file) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            LocalDateTime time = LocalDateTime.now();
            writer.append(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.newLine();
            writer.append("lifevalue="+LV.lifevalue);
            writer.newLine();
            
            writer.write("havePicture=");
            for(int i=0;i<Picture.ture.size();++i){
                if(i>0) writer.append(",");
                writer.append(Picture.ture.get(i));
            }
            writer.newLine();
            
            writer.write("choose=");
            writer.newLine();
            writer.write("1 " + PictureConfig.getSelectedImageForMode(PictureConfig.MODE_RESIDENTIAL));
            writer.newLine();
            writer.write("2 " + PictureConfig.getSelectedImageForMode(PictureConfig.MODE_OFFICIAL));
            writer.newLine();
            writer.write("3 " + PictureConfig.getSelectedImageForMode(PictureConfig.MODE_PALACE));
            writer.newLine();
            writer.write("4 " + PictureConfig.getSelectedImageForMode(PictureConfig.MODE_BRIDGE));
            writer.newLine();
            
            writer.append("sign_day="+LV.signDate);
            writer.newLine();
            
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通用图片加载方法
    public static BufferedImage loadImage(String path) throws IOException {
        try {
            // 尝试从文件系统加载 - 多种可能的路径
            String userDir = System.getProperty("user.dir");
            String[] possiblePaths = {
                userDir + "/resources/" + path,
                userDir + "/" + path,
                "resources/" + path,
                path
            };
            
            for (String filePath : possiblePaths) {
                File file = new File(filePath);
                if (file.exists()) {
                    return ImageIO.read(file);
                }
            }
            
            // 最后尝试从classpath加载
            String classpathPath = path.startsWith("image/") ? path.substring(6) : path;
            InputStream is = Login.class.getClassLoader().getResourceAsStream("image/" + classpathPath);
            if (is != null) {
                return ImageIO.read(is);
            }
            
            throw new IllegalArgumentException("资源不存在: " + path + " (当前工作目录: " + userDir + ")");
        } catch (IOException e) {
            throw new RuntimeException("加载图片失败: " + path, e);
        }
    }
}
