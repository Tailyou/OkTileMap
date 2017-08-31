package com.tailyou.tilemap.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.tailyou.tilemap.app.AppConfig
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

class DbOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, AppConfig.dbFilePath, null, 1) {

    companion object {
        private var instance: DbOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbOpenHelper {
            if (instance == null) {
                instance = DbOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

}

// Access property for Context
val Context.dbHelper: DbOpenHelper
    get() = DbOpenHelper.getInstance(applicationContext)

//val Context.db: SQLiteDatabase
//    get() = DbOpenHelper.getInstance(applicationContext).writableDatabase