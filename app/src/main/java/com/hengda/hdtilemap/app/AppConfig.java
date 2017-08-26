package com.hengda.hdtilemap.app;


import android.os.Environment;

/**
 * App配置类
 *
 * @author 祝文飞（Tailyou）
 * @time 2017/4/28 15:58
 */
public class AppConfig {

    //    获取默认文件存储目录
    public static String getDefaultFileDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/JinSha_Res/";
    }

    //    获取数据库文件路径
    public static String getDbFilePath() {
        return getDefaultFileDir() + "filemanage.s3db";
    }

    //    获取地图文件路径
    public static String getMapFilePath() {
        return getDefaultFileDir() + "map/";
    }

}
