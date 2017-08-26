package com.hengda.hdtilemap.db;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;


/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/30 15:41
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HBriteDatabase {

    private BriteDatabase db;
    private static volatile HBriteDatabase mInstance;

    private HBriteDatabase(Context context) {
        initDb(context);
    }

    /**
     * 单例
     *
     * @return
     */
    public static HBriteDatabase getInstance(Context context) {
        HBriteDatabase hBriteDatabase = mInstance;
        if (mInstance == null) {
            synchronized (HBriteDatabase.class) {
                hBriteDatabase = mInstance;
                if (mInstance == null) {
                    hBriteDatabase = new HBriteDatabase(context);
                    mInstance = hBriteDatabase;
                }
            }
        }
        return hBriteDatabase;
    }

    /**
     * 初始化数据库
     */
    private void initDb(Context context) {
        SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Logger.i(message);
            }
        });
        db = sqlBrite.wrapDatabaseHelper(new HSQLiteOpenHelper(context), Schedulers.io());
        db.setLoggingEnabled(true);
    }

    /**
     * 获取数据库
     *
     * @return
     */
    public BriteDatabase getDb() {
        return db;
    }

}
