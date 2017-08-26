package com.hengda.hdtilemap

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hengda.hdtilemap.app.AppConfig
import com.hengda.hdtilemap.bean.ExhibitBean
import com.hengda.smart.wusu.m.db.dbHelper
import com.hengda.zwf.oktilemap.common.Intents
import com.hengda.zwf.oktilemap.config.MapConfig
import com.hengda.zwf.oktilemap.config.MapConfigBuilder
import com.hengda.zwf.oktilemap.entity.BaseExhibit
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var exhibitList: ArrayList<BaseExhibit>
    private var isRouteShowing: Boolean = false
    private var isMarkerShowing: Boolean = false
    private val mapId = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //显示路线
        tvRoute.setOnClickListener {
            if (isRouteShowing) {
                hideRoute()
            } else {
                showRoute(AppConfig.mapFilePath + "/route.png")
            }
        }
        //显示Maker
        tvMarker.setOnClickListener {
            if (isMarkerShowing) {
                hideMarker()
            } else {
                showMarker(exhibitList)
            }
        }
        //收号定位
        tvAutoNo.setOnClickListener { receiveAutoNo(Random().nextInt(100)) }
        //我的位置
        tvLocation.setOnClickListener { toMyLocation() }
        //高亮
        tvHighLight.setOnClickListener {
        }
        showMap()
    }

    //显示地图
    fun showMap() {
        val mapConfig = initMapConfig()
        val mapFragment = MapFragment.newInstance(mapConfig)
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMapContainer, mapFragment)
                .commitAllowingStateLoss()
        loadExhibit()
    }

    //配置MapConfig
    private fun initMapConfig(): MapConfig {
        return MapConfigBuilder()
                .setBaseMapString(AppConfig.mapFilePath + mapId)
                .setMapId(mapId)
                .setMerge(true, 1f, 300f)
                .setSize(3090, 2273)
                .setInitScale(0f)
                .create()
    }

    //加载展品
    private fun loadExhibit() {
        doAsync {
            var list = dbHelper.use {
                select("EXHIBIT_CHINESE").parseList(ExhibitBean.PARSER)
            }
            exhibitList.clear()
            exhibitList.addAll(list)
            showMarker(exhibitList)
        }
    }

    //显示展品Marker
    private fun showMarker(arrayList: ArrayList<BaseExhibit>) {
        val intent = Intent(Intents.Action.SHOW_MARKERS)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        intent.putExtra(Intents.Extra.MARKERS, arrayList)
        sendBroadcast(intent)
        isMarkerShowing = true
    }

    //隐藏展品Marker
    private fun hideMarker() {
        val intent = Intent(Intents.Action.HIDE_MARKERS)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        sendBroadcast(intent)
        isMarkerShowing = false
    }

    //显示路线
    private fun showRoute(routeImgPath: String) {
        val intent = Intent(Intents.Action.SHOW_ROUTE)
        intent.putExtra(Intents.Extra.ROUTE_IMG_PATH, routeImgPath)
        sendBroadcast(intent)
        isRouteShowing = true
    }

    //隐藏路线
    private fun hideRoute() {
        val intent = Intent(Intents.Action.HIDE_ROUTE)
        sendBroadcast(intent)
        isRouteShowing = false
    }

    //收到多模号
    private fun receiveAutoNo(autoNo: Int) {
        val intent = Intent(Intents.Action.RECEIVE_NO)
        intent.putExtra(Intents.Extra.AUTO_NO, autoNo)
        sendBroadcast(intent)
    }

    //回到我的位置
    private fun toMyLocation() {
        val intent = Intent(Intents.Action.TO_MY_LOCATION)
        intent.putExtra(Intents.Extra.MAP_ID, mapId)
        sendBroadcast(intent)
    }

}
