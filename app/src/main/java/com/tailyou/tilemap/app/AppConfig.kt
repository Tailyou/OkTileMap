package com.tailyou.tilemap.app


import android.os.Environment

//App配置类
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
