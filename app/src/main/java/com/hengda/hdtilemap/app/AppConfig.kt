package com.hengda.hdtilemap.app


import android.os.Environment

/**
 * App配置类
 * @author 祝文飞（Tailyou）
 * @time 2017/4/28 15:58
 */
object AppConfig {

    //    获取默认文件存储目录
    val defaultFileDir: String
        get() = Environment.getExternalStorageDirectory().absolutePath + "/JinSha_Res/"

    //    获取数据库文件路径
    val dbFilePath: String
        get() = defaultFileDir + "filemanage.s3db"

    //    获取地图文件路径
    val mapFilePath: String
        get() = defaultFileDir + "map/"

}
