package com.hengda.zwf.oktilemap.util

import android.content.Context
import android.content.Intent
import com.hengda.zwf.oktilemap.common.Intents
import com.hengda.zwf.oktilemap.entity.BaseExhibit
import java.util.*

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/7/27 16:17
 * 描述：MapUtil
 */
object MapUtil {

    fun showMarker(mContext: Context, mapId: Int, arrayList: ArrayList<BaseExhibit>) {
        val intent = Intent(Intents.Action.SHOW_MARKERS)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        intent.putExtra(Intents.Extra.MARKERS, arrayList)
        mContext.sendBroadcast(intent)
    }

    fun hideMarker(mContext: Context, mapId: Int) {
        val intent = Intent(Intents.Action.HIDE_MARKERS)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        mContext.sendBroadcast(intent)
    }

    fun showRoute(mContext: Context, mapId: Int, routePath: String) {
        val intent = Intent(Intents.Action.SHOW_ROUTE)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        intent.putExtra(Intents.Extra.ROUTE_IMG_PATH, routePath)
        mContext.sendBroadcast(intent)
    }

    fun hideRoute(mContext: Context, mapId: Int) {
        val intent = Intent(Intents.Action.HIDE_ROUTE)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        mContext.sendBroadcast(intent)
    }

    fun onReceiveAutoNo(mContext: Context, autoNo: Int) {
        val intent = Intent(Intents.Action.RECEIVE_NO)
        intent.putExtra(Intents.Extra.AUTO_NO, autoNo)
        mContext.sendBroadcast(intent)
    }

    fun slide2MyLocation(mContext: Context, mapId: Int) {
        val intent = Intent(Intents.Action.TO_MY_LOCATION)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        mContext.sendBroadcast(intent)
    }

}
