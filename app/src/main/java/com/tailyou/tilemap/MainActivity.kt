package com.tailyou.tilemap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tailyou.oktilemap.config.MapConfig
import com.tailyou.oktilemap.config.MapConfigBuilder
import com.tailyou.oktilemap.entity.BaseExhibit
import com.tailyou.oktilemap.util.MapUtil
import com.tailyou.tilemap.app.AppConfig
import com.tailyou.tilemap.bean.Exhibit
import com.tailyou.tilemap.db.dbHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
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
                .setFormatNew(true)
                .setProvider(MapConfig.PROVIDER_PICASSO)
                .setMerge(true, 1f, 300f)
                .setSize(3090, 2273)
                .setInitScale(0f)
                .create()
    }

    //加载展品
    private fun loadExhibit() {
        doAsync {
            var list = dbHelper.use {
                select("EXHIBIT_CHINESE").whereSimple("MapId=$mapId").parseList(Exhibit.PARSER)
            }
            uiThread {
                val exhibitList = list.mapTo(ArrayList<BaseExhibit>()) { Exhibit.exhibit2BaseExhibit(it) }
                showMarker(exhibitList)
            }
        }
    }

    //显示展品Marker
    private fun showMarker(arrayList: ArrayList<BaseExhibit>) {
        MapUtil.showMarker(this, mapId, arrayList)
        isMarkerShowing = true
    }

    //隐藏展品Marker
    private fun hideMarker() {
        MapUtil.hideMarker(this, mapId)
        isMarkerShowing = false
    }

    //显示路线
    private fun showRoute(routePath: String) {
        MapUtil.showRoute(this, mapId, routePath)
        isRouteShowing = true
    }

    //隐藏路线
    private fun hideRoute() {
        MapUtil.hideRoute(this, mapId)
        isRouteShowing = false
    }

    //收到多模号
    private fun receiveAutoNo(autoNo: Int) {
        MapUtil.onReceiveAutoNo(this, autoNo)
    }

    //回到我的位置
    private fun toMyLocation() {
        MapUtil.slide2MyLocation(this, mapId)
    }

}
