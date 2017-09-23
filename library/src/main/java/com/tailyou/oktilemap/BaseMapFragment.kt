package com.tailyou.oktilemap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.qozix.tileview.TileView
import com.qozix.tileview.graphics.BitmapProvider
import com.qozix.tileview.widgets.ZoomPanLayout
import com.tailyou.oktilemap.common.Intents
import com.tailyou.oktilemap.config.MapConfig
import com.tailyou.oktilemap.entity.BaseExhibit
import com.tailyou.oktilemap.entity.Location
import com.tailyou.oktilemap.util.GlideBitmapProvider
import com.tailyou.oktilemap.util.GlideImageLoader
import com.tailyou.oktilemap.util.MergeUtil
import com.tailyou.oktilemap.util.PicassoBitmapProvider
import java.util.*

abstract open class BaseMapFragment : Fragment() {

    companion object {
        val MAP_CONFIG = "MAP_CONFIG"
    }

    lateinit var tileView: TileView
    lateinit var mapConfig: MapConfig
    lateinit var bitmapProvider: BitmapProvider
    lateinit var imageLoader: GlideImageLoader
    lateinit var baseMapPath: String
    var mapId: Int = 0
    //Route
    lateinit var routePath: String
    var bmRoute: Bitmap? = null
    var ivRoute: ImageView? = null
    //Marker
    var exhibits: MutableList<BaseExhibit> = mutableListOf()
    var exhibitMarkerMap: MutableMap<String, ImageView> = HashMap()
    var mergeMarkerList: MutableList<ImageView> = ArrayList()
    var isMergeStatus: Boolean = false
    //Location
    var location: Location? = null

    var mapActionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intents.Action.SHOW_ROUTE -> if (intent.getIntExtra(Intents.Extra.MAP_ID, -100) == mapId) {
                    routePath = intent.getStringExtra(Intents.Extra.ROUTE_PATH)
                    showRoute()
                }
                Intents.Action.HIDE_ROUTE -> if (intent.getIntExtra(Intents.Extra.MAP_ID, -100) == mapId) {
                    hideRoute()
                }
                Intents.Action.SHOW_MARKERS -> if (intent.getIntExtra(Intents.Extra.MAP_ID, -100) == mapId) {
                    exhibits = intent.getSerializableExtra(Intents.Extra.MARKERS) as MutableList<BaseExhibit>
                    showMarker()
                }
                Intents.Action.HIDE_MARKERS -> if (intent.getIntExtra(Intents.Extra.MAP_ID, -100) == mapId) {
                    hideMarker()
                }
                Intents.Action.RECEIVE_NO -> {
                    val autoNo = intent.getIntExtra(Intents.Extra.AUTO_NO, 0)
                    receiveNo(autoNo)
                }
                Intents.Action.TO_MY_LOCATION -> if (intent.getIntExtra(Intents.Extra.MAP_ID, -100) == mapId) {
                    toMyLocation()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intents.Action.SHOW_ROUTE)
        intentFilter.addAction(Intents.Action.HIDE_ROUTE)
        intentFilter.addAction(Intents.Action.SHOW_MARKERS)
        intentFilter.addAction(Intents.Action.HIDE_MARKERS)
        intentFilter.addAction(Intents.Action.RECEIVE_NO)
        intentFilter.addAction(Intents.Action.TO_MY_LOCATION)
        context.registerReceiver(mapActionReceiver, intentFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = GlideImageLoader()
        mapConfig = arguments.getSerializable(MAP_CONFIG) as MapConfig
        bitmapProvider = if (mapConfig.provider == MapConfig.PROVIDER_GLIDE) GlideBitmapProvider() else PicassoBitmapProvider()
        baseMapPath = mapConfig.baseMapPath
        mapId = mapConfig.mapId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initTileView()
        addDetailLevel()
        setDownSample()
        setInitScale(mapConfig.initScale)
        return tileView
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(mapActionReceiver)
        tileView.destroy()
    }

    //初始化TileView
    fun initTileView() {
        tileView = TileView(context)
        tileView.setSize(mapConfig.sizeWidth, mapConfig.sizeHeight)
        tileView.defineBounds(mapConfig.boundLeft, mapConfig.boundTop, mapConfig.boundRight, mapConfig.boundBottom)
        tileView.setMarkerAnchorPoints(mapConfig.anchorX, mapConfig.anchorY)
        tileView.setScaleLimits(mapConfig.minScale, mapConfig.maxScale)
        tileView.setMinimumScaleMode(mapConfig.minimumScaleMode)
        tileView.addZoomPanListener(object : ZoomPanLayout.ZoomPanListener {
            override fun onPanBegin(i: Int, i1: Int, origination: ZoomPanLayout.ZoomPanListener.Origination?) {}

            override fun onPanUpdate(i: Int, i1: Int, origination: ZoomPanLayout.ZoomPanListener.Origination?) {}

            override fun onPanEnd(i: Int, i1: Int, origination: ZoomPanLayout.ZoomPanListener.Origination?) {}

            override fun onZoomBegin(v: Float, origination: ZoomPanLayout.ZoomPanListener.Origination?) {}

            override fun onZoomUpdate(v: Float, origination: ZoomPanLayout.ZoomPanListener.Origination?) {
                onZoomChange(v)
            }

            override fun onZoomEnd(v: Float, origination: ZoomPanLayout.ZoomPanListener.Origination?) {
                onZoomChange(v)
            }
        })
    }

    //根据缩放状态变化：显示聚合展项/全部展项
    private fun onZoomChange(v: Float) {
        if (mapConfig.isMerge) {
            if (v < mapConfig.mergeScale && !isMergeStatus) {
                showMarker()
            }
            if (v > mapConfig.mergeScale && isMergeStatus) {
                showMarker()
            }
        }
    }

    //添加各级瓦片
    fun addDetailLevel() {
        tileView.setBitmapProvider(bitmapProvider)
        if (mapConfig.isFormatNew) {
            tileView.addDetailLevel(1.000f, baseMapPath + "/1_1000_%d_%d.png")
            tileView.addDetailLevel(0.500f, baseMapPath + "/1_500_%d_%d.png")
            tileView.addDetailLevel(0.250f, baseMapPath + "/1_250_%d_%d.png")
            tileView.addDetailLevel(0.125f, baseMapPath + "/1_125_%d_%d.png")
        } else {
            tileView.addDetailLevel(1.000f, baseMapPath + "/1000/%d_%d.png")
            tileView.addDetailLevel(0.500f, baseMapPath + "/500/%d_%d.png")
            tileView.addDetailLevel(0.250f, baseMapPath + "/250/%d_%d.png")
            tileView.addDetailLevel(0.125f, baseMapPath + "/125/%d_%d.png")
        }
    }

    //设置底图
    fun setDownSample() {
        if (mapConfig.isDownSample) {
            val downSample = ImageView(context)
            imageLoader.displayImage(context, baseMapPath + "/img.png", downSample)
            tileView.addView(downSample, 0)
        }
    }

    //设置初始缩放比例
    fun setInitScale(initScale: Float) {
        tileView.post {
            if (tileView != null) {
                tileView.scale = initScale
            }
        }
    }

    //隐藏路线
    fun hideRoute() {
        if (ivRoute != null) {
            if (bmRoute != null && !bmRoute!!.isRecycled) {
                bmRoute!!.recycle()
                bmRoute = null
            }
            tileView.removeView(ivRoute)
        }
    }

    //显示路线
    fun showRoute() {
        if (!TextUtils.isEmpty(routePath)) {
            tileView.post {
                ivRoute = ImageView(context)
                imageLoader.displayImage(context, routePath, ivRoute!!)
                tileView.addView(ivRoute, 3, ViewGroup.LayoutParams(tileView.baseWidth, tileView.baseHeight))
            }
        }
    }

    //隐藏Marker
    fun hideMarker() {
        for (marker in exhibitMarkerMap.values) {
            tileView.removeMarker(marker)
        }
        exhibitMarkerMap.clear()
        for (marker in mergeMarkerList) {
            tileView.removeMarker(marker)
        }
        mergeMarkerList.clear()
    }

    //显示Marker
    fun showMarker() {
        hideMarker()
        if (mapConfig.isMerge) {
            if (tileView.scale >= mapConfig.mergeScale) {
                placeNormalMarker(exhibits)
                isMergeStatus = false
            } else {
                val copy = MergeUtil.copy(exhibits)
                val merge = MergeUtil.merge(copy, mapConfig.mergeDistance)
                placeMergeMarker(merge)
                isMergeStatus = true
            }
        } else {
            placeNormalMarker(exhibits)
        }
    }

    private fun placeMergeMarker(merge: List<List<BaseExhibit>>) {
        for (list in merge) {
            val averLoc = MergeUtil.calAverCoords(list)
            val mergeMarker = genMergeMarker(list, averLoc)
            mergeMarkerList.add(mergeMarker)
            tileView.addMarker(mergeMarker, averLoc.locX, averLoc.locY, mapConfig.anchorX, mapConfig.anchorY)
        }
    }

    private fun placeNormalMarker(copy: MutableList<BaseExhibit>) {
        for (exhibit in copy) {
            val exhibitMarker = genExhibitMarker(exhibit)
            exhibitMarker.tag = exhibit
            exhibitMarkerMap.put(exhibit.fileNo, exhibitMarker)
            tileView.addMarker(exhibitMarker, exhibit.locX, exhibit.locY, mapConfig.anchorX, mapConfig.anchorY)
        }
    }

    //收号
    fun receiveNo(autoNo: Int) {
        if (autoNo != 0) {
            for (exhibit in exhibits) {
                if (exhibit.autoNo == autoNo) {
                    saveLocation(exhibit)
                    positioning(exhibit)
                    showCallout(exhibit)
                    break
                }
            }
        }
    }

    //保存定位位置
    private fun saveLocation(exhibit: BaseExhibit) {
        if (location == null) {
            location = Location(exhibit.locX, exhibit.locY)
        } else {
            location!!.locX = exhibit.locX
            location!!.locY = exhibit.locY
        }
    }

    //回到定位位置
    private fun toMyLocation() {
        if (location == null) {
            Toast.makeText(context, "没有位置信息", Toast.LENGTH_SHORT).show()
        } else {
            tileView.slideToAndCenter(location!!.locX, location!!.locY)
        }
    }

    //生成展项Marker
    abstract fun genExhibitMarker(exhibit: BaseExhibit): ImageView

    //生成聚合Marker
    abstract fun genMergeMarker(list: List<BaseExhibit>, averLoc: Location): ImageView

    //定位
    abstract fun positioning(exhibit: BaseExhibit)

    //显示展品定位大图
    abstract fun showCallout(exhibit: BaseExhibit)

}
