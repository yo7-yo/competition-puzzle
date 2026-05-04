package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Picture {
    //选择的图片 - 默认使用晋商大院
    public static String pic = "image/晋商大院.png";
    //购买的图片 - 默认第一张是有的
    public static List<String> ture = new ArrayList<>(Arrays.asList(
        "image/晋商大院.png",
        "image/直隶总督署.png",
        "image/太和殿.png",
        "image/赵州桥拼图.png",
        "image/卢沟桥.png"
    ));
}
