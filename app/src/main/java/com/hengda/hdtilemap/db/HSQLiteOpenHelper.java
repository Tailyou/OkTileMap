package com.hengda.hdtilemap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.hengda.hdtilemap.app.AppConfig;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/30 15:35
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public HSQLiteOpenHelper(Context context) {
        super(context, AppConfig.getDbFilePath(), null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
